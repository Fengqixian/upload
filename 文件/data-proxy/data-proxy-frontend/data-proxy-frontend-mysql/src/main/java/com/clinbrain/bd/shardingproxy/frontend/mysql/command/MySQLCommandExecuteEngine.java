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

package com.clinbrain.bd.shardingproxy.frontend.mysql.command;

import io.netty.channel.ChannelHandlerContext;
import com.clinbrain.bd.core.constant.properties.ShardingPropertiesConstant;
import com.clinbrain.bd.shardingproxy.backend.communication.jdbc.connection.BackendConnection;
import com.clinbrain.bd.shardingproxy.context.ShardingProxyContext;
import com.clinbrain.bd.shardingproxy.frontend.api.CommandExecutor;
import com.clinbrain.bd.shardingproxy.frontend.api.QueryCommandExecutor;
import com.clinbrain.bd.shardingproxy.frontend.engine.CommandExecuteEngine;
import com.clinbrain.bd.shardingproxy.frontend.mysql.MySQLErrPacketFactory;
import com.clinbrain.bd.shardingproxy.transport.mysql.packet.command.MySQLCommandPacket;
import com.clinbrain.bd.shardingproxy.transport.mysql.packet.command.MySQLCommandPacketFactory;
import com.clinbrain.bd.shardingproxy.transport.mysql.packet.command.MySQLCommandPacketType;
import com.clinbrain.bd.shardingproxy.transport.mysql.packet.command.MySQLCommandPacketTypeLoader;
import com.clinbrain.bd.shardingproxy.transport.mysql.packet.generic.MySQLEofPacket;
import com.clinbrain.bd.shardingproxy.transport.mysql.payload.MySQLPacketPayload;
import com.clinbrain.bd.shardingproxy.transport.packet.CommandPacket;
import com.clinbrain.bd.shardingproxy.transport.packet.CommandPacketType;
import com.clinbrain.bd.shardingproxy.transport.packet.DatabasePacket;
import com.clinbrain.bd.shardingproxy.transport.payload.PacketPayload;

import java.sql.SQLException;

/**
 * Command execute engine for MySQL.
 *
 * @author zhangliang
 */
public final class MySQLCommandExecuteEngine implements CommandExecuteEngine {
    
    @Override
    public MySQLCommandPacketType getCommandPacketType(final PacketPayload payload) {
        return MySQLCommandPacketTypeLoader.getCommandPacketType((MySQLPacketPayload) payload);
    }
    
    @Override
    public MySQLCommandPacket getCommandPacket(final PacketPayload payload, final CommandPacketType type, final BackendConnection backendConnection) throws SQLException {
        return MySQLCommandPacketFactory.newInstance((MySQLCommandPacketType) type, (MySQLPacketPayload) payload);
    }
    
    @Override
    public CommandExecutor getCommandExecutor(final CommandPacketType type, final CommandPacket packet, final BackendConnection backendConnection) {
        return MySQLCommandExecutorFactory.newInstance((MySQLCommandPacketType) type, packet, backendConnection);
    }
    
    @Override
    public DatabasePacket getErrorPacket(final Exception cause) {
        return MySQLErrPacketFactory.newInstance(1, cause);
    }
    
    @Override
    public void writeQueryData(final ChannelHandlerContext context,
                               final BackendConnection backendConnection, final QueryCommandExecutor queryCommandExecutor, final int headerPackagesCount) throws SQLException {
        if (!queryCommandExecutor.isQuery() || !context.channel().isActive()) {
            return;
        }
        int count = 0;
        int flushThreshold = ShardingProxyContext.getInstance().getShardingProperties().<Integer>getValue(ShardingPropertiesConstant.PROXY_FRONTEND_FLUSH_THRESHOLD);
        int currentSequenceId = 0;
        while (queryCommandExecutor.next()) {
            count++;
            while (!context.channel().isWritable() && context.channel().isActive()) {
                context.flush();
                backendConnection.getResourceSynchronizer().doAwait();
            }
            DatabasePacket dataValue = queryCommandExecutor.getQueryData();
            context.write(dataValue);
            if (flushThreshold == count) {
                context.flush();
                count = 0;
            }
            currentSequenceId++;
        }
        context.write(new MySQLEofPacket(++currentSequenceId + headerPackagesCount));
    }
}
