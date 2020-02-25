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

package com.clinbrain.bd.shardingproxy.transport.postgresql.codec;

import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.PostgreSQLPacket;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.handshake.PostgreSQLSSLNegativePacket;
import com.clinbrain.bd.shardingproxy.transport.postgresql.payload.PostgreSQLPacketPayload;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import com.clinbrain.bd.shardingproxy.transport.codec.DatabasePacketCodecEngine;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Database packet codec for MySQL.
 *
 * @author zhangyonglun
 */
@Slf4j
public final class PostgreSQLPacketCodecEngine implements DatabasePacketCodecEngine<PostgreSQLPacket> {
    
    @Override
    public boolean isValidHeader(final int readableBytes) {
        return readableBytes >= PostgreSQLPacket.MESSAGE_TYPE_LENGTH + PostgreSQLPacket.PAYLOAD_LENGTH;
    }
    
    @Override
    public void decode(final ChannelHandlerContext context, final ByteBuf in, final List<Object> out, final int readableBytes) {
        int messageTypeLength = PostgreSQLPacket.MESSAGE_TYPE_LENGTH;
        byte b = in.markReaderIndex().readByte();
        if ('\0' == b) {
            messageTypeLength = 0;
            in.resetReaderIndex();
        }
        int payloadLength = in.readInt();
        in.resetReaderIndex();
        int realPacketLength = payloadLength + messageTypeLength;
        if (readableBytes < realPacketLength) {
            return;
        }
        out.add(in.readBytes(payloadLength + messageTypeLength));
        log.debug("数据包可读：{}  ， 消息类型：{} ，数据总长度：{} ，",readableBytes,(char)b,payloadLength);
    }
    
    @Override
    public void encode(final ChannelHandlerContext context, final PostgreSQLPacket message, final ByteBuf out) {
        try{
            PostgreSQLPacketPayload payload = new PostgreSQLPacketPayload(context.alloc().buffer());
            message.write(payload);
            //首先写入messageType  再写入消息总长度
            if (!(message instanceof PostgreSQLSSLNegativePacket)) {
                out.writeByte(message.getMessageType());
                out.writeInt(payload.getByteBuf().readableBytes() + PostgreSQLPacket.PAYLOAD_LENGTH);
            }
            out.writeBytes(payload.getByteBuf());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    @Override
    public PostgreSQLPacketPayload createPacketPayload(final ByteBuf message) {
        return new PostgreSQLPacketPayload(message);
    }
}
