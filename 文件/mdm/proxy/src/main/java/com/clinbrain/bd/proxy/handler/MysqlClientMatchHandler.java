package com.clinbrain.bd.proxy.handler;

import com.clinbrain.bd.proxy.consts.MySQLServerInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author WANGYI
 * @className com.clinbrain.bd.proxy.handler.MysqlClientMatchHandler
 * @createdDate 2019/8/23 14:04
 * @description TODO
 * @e-mail WANGYI@clinbrain.com
 * @group bigdata develop group (mdm)
 */
@Slf4j
public class MysqlClientMatchHandler extends ChannelInboundHandlerAdapter {
    private AtomicBoolean matching = new AtomicBoolean(true);
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        /*发送握手包*/
        ByteBuf payload = ctx.alloc().buffer();
        //协议版本
        payload.writeByte(MySQLServerInfo.PROTOCOL_VERSION);
        //服务信息
        payload.writeBytes(MySQLServerInfo.SERVER_VERSION.getBytes("utf-8"));
        payload.writeByte(0);

        payload.writeIntLE(0); //connectionId

        /*生成秘钥对*/
        byte[] part1 = new byte[8];
        byte[] part2 = new byte[10];
        Random random = new Random();
        random.nextBytes(part1);
        random.nextBytes(part2);

        payload.writeBytes(part1);
        payload.writeByte(0);

        payload.writeShortLE(0);
        payload.writeByte(MySQLServerInfo.CHARSET);
        payload.writeShortLE(0);
        payload.writeShortLE(0);
        payload.writeByte(0);

        for (int i = 0; i < 10; i++) {
            payload.writeByte(0);
        }

        payload.writeBytes(part2);
        payload.writeByte(0);

        ByteBuf byteBuf = ctx.alloc().buffer();
        byteBuf.writeMediumLE(payload.readableBytes());
        byteBuf.writeByte(0);
        byteBuf.writeBytes(payload);
        log.debug("Proxy write to client: \n {}", ByteBufUtil.prettyHexDump(byteBuf));
        ctx.writeAndFlush(byteBuf);

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        if(byteBuf.readableBytes()<4){
            throw new Exception();
        }
        /*读取收到的第一个包*/
        if(matching.getAndSet(false)){
            log.debug("Proxy read from client: \n {}", ByteBufUtil.prettyHexDump((ByteBuf) msg));
            char c = (char) byteBuf.markReaderIndex().readableBytes();
            byteBuf.resetReaderIndex();
            // c =='0'


            ctx.pipeline().remove(this);
            ctx.pipeline().fireChannelActive();
        }
    }
}
