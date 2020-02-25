package com.clinbrain.bd.proxy.codec.postgres;

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
public final class PostgresServerPacketCodec extends ByteToMessageCodec<ByteBuf>{
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
        int messageTypeLength = 1;
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
        ByteBuf byteBuf = in.readBytes(payloadLength + messageTypeLength);
        log.info("Proxy read from client {} : \n {}",context.channel().id().asShortText(), ByteBufUtil.prettyHexDump(byteBuf));
        out.add(byteBuf);
        log.info("数据包可读：{}  ， 消息类型：{} ，数据总长度：{} ，",readableBytes,(char)b,payloadLength);
    }
    protected boolean isValidHeader(int readableBytes){
        return readableBytes>4;
    }
}
