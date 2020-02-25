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

package com.clinbrain.bd.shardingproxy.frontend.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import com.clinbrain.bd.core.constant.properties.ShardingPropertiesConstant;
import com.clinbrain.bd.shardingproxy.backend.communication.jdbc.connection.BackendConnection;
import com.clinbrain.bd.shardingproxy.context.ShardingProxyContext;
import com.clinbrain.bd.shardingproxy.frontend.command.CommandExecutorTask;
import com.clinbrain.bd.shardingproxy.frontend.executor.ChannelThreadExecutorGroup;
import com.clinbrain.bd.shardingproxy.frontend.executor.CommandExecutorSelector;
import com.clinbrain.bd.shardingproxy.frontend.spi.DatabaseProtocolFrontendEngine;
import com.clinbrain.bd.shardingproxy.transport.payload.PacketPayload;
import com.clinbrain.bd.transaction.core.TransactionType;

import java.util.concurrent.ExecutorService;

/**
 * Frontend channel inbound handler.
 * 
 * @author zhangliang 
 */
@RequiredArgsConstructor
@Slf4j
public final class FrontendChannelInboundHandler extends ChannelInboundHandlerAdapter {
    
    private final DatabaseProtocolFrontendEngine databaseProtocolFrontendEngine;
    
    private volatile boolean authorized;
    
    private final BackendConnection backendConnection = new BackendConnection(
            TransactionType.valueOf(ShardingProxyContext.getInstance().getShardingProperties().<String>getValue(ShardingPropertiesConstant.PROXY_TRANSACTION_TYPE)));
    
    @Override
    public void channelActive(final ChannelHandlerContext context) {
        ChannelThreadExecutorGroup.getInstance().register(context.channel().id());
        databaseProtocolFrontendEngine.getAuthEngine().handshake(context, backendConnection);
    }
    
    @Override
    public void channelRead(final ChannelHandlerContext context, final Object message) {
        if (!authorized) {
            log.info("----------------------------<< 开始认证  >>-------------------------------");
            authorized = auth(context, (ByteBuf) message);
            return;
        }
        ExecutorService executorService = CommandExecutorSelector.getExecutor(databaseProtocolFrontendEngine.getFrontendContext().isOccupyThreadForPerConnection(), backendConnection.getTransactionType(), context.channel().id());
        CommandExecutorTask executorTask = new CommandExecutorTask(databaseProtocolFrontendEngine, backendConnection, context, message);
        executorService.submit(executorTask);
    }
    
    private boolean auth(final ChannelHandlerContext context, final ByteBuf message) {
        try (PacketPayload payload = databaseProtocolFrontendEngine.getCodecEngine().createPacketPayload(message)) {
            return databaseProtocolFrontendEngine.getAuthEngine().auth(context, payload, backendConnection);
            // CHECKSTYLE:OFF
        } catch (final Exception ex) {
            // CHECKSTYLE:ON
            log.error("Exception occur: ", ex);
            context.write(databaseProtocolFrontendEngine.getCommandExecuteEngine().getErrorPacket(ex));
        }
        return false;
    }
    
    @Override
    @SneakyThrows
    public void channelInactive(final ChannelHandlerContext context) {
        context.fireChannelInactive();
        databaseProtocolFrontendEngine.release(backendConnection);
        backendConnection.close(true);
        ChannelThreadExecutorGroup.getInstance().unregister(context.channel().id());
    }
    
    @Override
    public void channelWritabilityChanged(final ChannelHandlerContext context) {
        if (context.channel().isWritable()) {
            backendConnection.getResourceSynchronizer().doNotify();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("通道出错",cause);
    }
}
