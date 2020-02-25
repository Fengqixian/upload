/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.clinbrain.bd.shardingproxy.frontend.postgresql.command;

import io.netty.channel.ChannelHandlerContext;
import com.clinbrain.bd.core.constant.properties.ShardingPropertiesConstant;
import com.clinbrain.bd.shardingproxy.backend.communication.jdbc.connection.BackendConnection;
import com.clinbrain.bd.shardingproxy.context.ShardingProxyContext;
import com.clinbrain.bd.shardingproxy.frontend.api.CommandExecutor;
import com.clinbrain.bd.shardingproxy.frontend.api.QueryCommandExecutor;
import com.clinbrain.bd.shardingproxy.frontend.engine.CommandExecuteEngine;
import com.clinbrain.bd.shardingproxy.transport.packet.CommandPacket;
import com.clinbrain.bd.shardingproxy.transport.packet.CommandPacketType;
import com.clinbrain.bd.shardingproxy.transport.packet.DatabasePacket;
import com.clinbrain.bd.shardingproxy.transport.payload.PacketPayload;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.command.PostgreSQLCommandPacket;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.command.PostgreSQLCommandPacketFactory;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.command.PostgreSQLCommandPacketType;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.command.PostgreSQLCommandPacketTypeLoader;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.generic.PostgreSQLCommandCompletePacket;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.generic.PostgreSQLErrorResponsePacket;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.generic.PostgreSQLReadyForQueryPacket;
import com.clinbrain.bd.shardingproxy.transport.postgresql.payload.PostgreSQLPacketPayload;

import java.sql.SQLException;

/**
 * Command execute engine for PostgreSQL.
 *
 * @author zhangliang
 */
public final class PostgreSQLCommandExecuteEngine implements CommandExecuteEngine {
    
    @Override
    public PostgreSQLCommandPacketType getCommandPacketType(final PacketPayload payload) {
        return PostgreSQLCommandPacketTypeLoader.getCommandPacketType((PostgreSQLPacketPayload) payload);
    }
    
    @Override
    public PostgreSQLCommandPacket getCommandPacket(final PacketPayload payload, final CommandPacketType type, final BackendConnection backendConnection) throws SQLException {
        return PostgreSQLCommandPacketFactory.newInstance((PostgreSQLCommandPacketType) type, (PostgreSQLPacketPayload) payload, backendConnection.getConnectionId(),backendConnection.getUserName());
    }

    @Override
    public CommandExecutor getCommandExecutor(final CommandPacketType type, final CommandPacket packet, final BackendConnection backendConnection) {
        return PostgreSQLCommandExecutorFactory.newInstance((PostgreSQLCommandPacketType) type, (PostgreSQLCommandPacket) packet, backendConnection);
    }
    
    @Override
    public DatabasePacket getErrorPacket(final Exception cause) {
        return new PostgreSQLErrorResponsePacket();
    }
    
    @Override
    public void writeQueryData(final ChannelHandlerContext context,
                               final BackendConnection backendConnection, final QueryCommandExecutor queryCommandExecutor, final int headerPackagesCount) throws SQLException {
        if (queryCommandExecutor.isQuery() && !context.channel().isActive()) {
            context.write(new PostgreSQLCommandCompletePacket());
            context.write(new PostgreSQLReadyForQueryPacket());
            return;
        }
        int count = 0;
        int proxyFrontendFlushThreshold = ShardingProxyContext.getInstance().getShardingProperties().<Integer>getValue(ShardingPropertiesConstant.PROXY_FRONTEND_FLUSH_THRESHOLD);
        while (queryCommandExecutor.next()) {
            count++;
            while (!context.channel().isWritable() && context.channel().isActive()) {
                context.flush();
                //backendConnection.getResourceSynchronizer().doAwait();
            }
            DatabasePacket resultValue = queryCommandExecutor.getQueryData();
            context.write(resultValue);
            if (proxyFrontendFlushThreshold == count) {
                context.flush();
                count = 0;
            }
        }
        context.write(new PostgreSQLCommandCompletePacket());
        context.write(new PostgreSQLReadyForQueryPacket());
    }
}
