package com.clinbrain.bd.proxy.codec.mysql;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author WANGYI
 * @className com.clinbrain.netty.codec.PacketCodec
 * @createdDate 2019/8/22 8:53
 * @description TODO
 * @e-mail WANGYI@clinbrain.com
 * @group bigdata develop group (Simple-proxy)
 */
@Slf4j
public final class MysqlServerPacketCodec extends ByteToMessageCodec<ByteBuf>{
    private static final int messageTypeLength = 4;
    @Override
    protected void encode(ChannelHandlerContext context, ByteBuf in, ByteBuf out) throws Exception {
        log.debug("Proxy write to client : \n {}", ByteBufUtil.prettyHexDump(in));
        out.writeBytes(in);
    }

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf in, List out) throws Exception {
        /*数据包最少要有4个长度*/
        int readableBytes = in.readableBytes();
        if (!isValidHeader(readableBytes)) {
            return;
        }
        int payloadLength = in.markReaderIndex().readMediumLE();
        int realPacketLength = payloadLength + 4;
        in.resetReaderIndex();
        if (readableBytes < realPacketLength) {
            return;
        }
        ByteBuf byteBuf = in.readBytes(realPacketLength);
        log.info("Proxy read from client : \n {}", ByteBufUtil.prettyHexDump(byteBuf));
        out.add(byteBuf);
    }
    protected boolean isValidHeader(int readableBytes){
        return readableBytes>=4;
    }
}
