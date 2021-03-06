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

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.RequiredArgsConstructor;
import com.clinbrain.bd.shardingproxy.backend.schema.LogicSchemas;
import com.clinbrain.bd.shardingproxy.frontend.DatabaseProtocolFrontendEngineFactory;
import com.clinbrain.bd.shardingproxy.frontend.spi.DatabaseProtocolFrontendEngine;
import com.clinbrain.bd.shardingproxy.transport.codec.PacketCodec;

/**
 * Channel initializer.
 * 
 * @author xiaoyu
 * @author zhangliang
 */
@RequiredArgsConstructor
public final class ServerHandlerInitializer extends ChannelInitializer<SocketChannel> {
    
    @Override
    protected void initChannel(final SocketChannel socketChannel) {
        DatabaseProtocolFrontendEngine databaseProtocolFrontendEngine = DatabaseProtocolFrontendEngineFactory.newInstance(LogicSchemas.getInstance().getDatabaseType());
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new PacketCodec(databaseProtocolFrontendEngine.getCodecEngine()));
        pipeline.addLast(new FrontendChannelInboundHandler(databaseProtocolFrontendEngine));
    }
}
