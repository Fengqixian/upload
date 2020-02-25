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

package com.clinbrain.bd.shardingproxy.frontend.command;

import com.clinbrain.bd.shardingproxy.backend.communication.jdbc.connection.ConnectionStatus;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.clinbrain.bd.core.execute.hook.RootInvokeHook;
import com.clinbrain.bd.core.execute.hook.SPIRootInvokeHook;
import com.clinbrain.bd.shardingproxy.backend.communication.jdbc.connection.BackendConnection;
import com.clinbrain.bd.shardingproxy.frontend.api.CommandExecutor;
import com.clinbrain.bd.shardingproxy.frontend.api.QueryCommandExecutor;
import com.clinbrain.bd.shardingproxy.frontend.engine.CommandExecuteEngine;
import com.clinbrain.bd.shardingproxy.frontend.spi.DatabaseProtocolFrontendEngine;
import com.clinbrain.bd.shardingproxy.transport.packet.CommandPacket;
import com.clinbrain.bd.shardingproxy.transport.packet.CommandPacketType;
import com.clinbrain.bd.shardingproxy.transport.packet.DatabasePacket;
import com.clinbrain.bd.shardingproxy.transport.payload.PacketPayload;

import java.sql.SQLException;
import java.util.Collection;

/**
 * 所有命令进入通道后，被包装成一个command Task 放入线程池执行
 * 一个command Task包含 数据库连接池前端引擎
 *
 * @author WANGYI
 */
@RequiredArgsConstructor
@Slf4j
public final class CommandExecutorTask implements Runnable {
    
    private final DatabaseProtocolFrontendEngine databaseProtocolFrontendEngine;
    
    private final BackendConnection backendConnection;
    
    private final ChannelHandlerContext context;
    
    private final Object message;
    
    @Override
    public void run() {
        //执行(ByteBuf) message
        ByteBuf msg = (ByteBuf) message;
        char c = (char)msg.markReaderIndex().readByte();
        msg.resetReaderIndex();
        log.debug("线程：{}，开始处理数据包：{}",Thread.currentThread().getName(),c);
        RootInvokeHook rootInvokeHook = new SPIRootInvokeHook();
        rootInvokeHook.start();
        int connectionSize = 0;
        boolean isNeedFlush = false;
        BackendConnection backendConnection = this.backendConnection;
        try {
            PacketPayload payload = databaseProtocolFrontendEngine.getCodecEngine().createPacketPayload((ByteBuf) message);
            backendConnection.getStateHandler().waitUntilConnectionReleasedIfNecessary();
            backendConnection.getStateHandler().setRunningStatusIfNecessary();
            isNeedFlush = executeCommand(context, payload, backendConnection);
            connectionSize = backendConnection.getConnectionSize();
            // CHECKSTYLE:OFF
        } catch (final Exception ex) {
            // CHECKSTYLE:ON
            log.error("Exception occur: ", ex);
            context.writeAndFlush(databaseProtocolFrontendEngine.getCommandExecuteEngine().getErrorPacket(ex));
        } finally {
            if (isNeedFlush) {
                context.flush();
            }
            backendConnection.getStateHandler().setStatus(ConnectionStatus.RELEASE);
            rootInvokeHook.finish(connectionSize);
        }
        log.debug("线程：{}，结束处理数据包：{}",Thread.currentThread().getName(),c);
    }
    
    private boolean executeCommand(final ChannelHandlerContext context, final PacketPayload payload, final BackendConnection backendConnection) throws SQLException {
        CommandExecuteEngine commandExecuteEngine = databaseProtocolFrontendEngine.getCommandExecuteEngine();
        CommandPacketType type = commandExecuteEngine.getCommandPacketType(payload);

        log.info("----------------------------<< CommandPacketType："+type.toString()+"  >>-------------------------------");

        CommandPacket commandPacket = commandExecuteEngine.getCommandPacket(payload, type, backendConnection);
        CommandExecutor commandExecutor = commandExecuteEngine.getCommandExecutor(type, commandPacket, backendConnection);
        Collection<DatabasePacket> responsePackets = commandExecutor.execute();
        if (responsePackets ==null) {
            return false;
        }
        for (DatabasePacket each : responsePackets) {
            context.write(each);
        }
        if (commandExecutor instanceof QueryCommandExecutor) {
            commandExecuteEngine.writeQueryData(context, backendConnection, (QueryCommandExecutor) commandExecutor, responsePackets.size());
            return true;
        }
        return databaseProtocolFrontendEngine.getFrontendContext().isFlushForPerCommandPacket();
    }
}
