package com.clinbrain.bd.proxy.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author WANGYI
 * @className com.clinbrain.bd.proxy.handler.MysqlClientMatchHandler
 * @createdDate 2019/8/23 14:04
 * @description TODO
 * @e-mail WANGYI@clinbrain.com
 * @group bigdata develop group (mdm)
 */
public class PostgresClientMatchHandler extends ChannelInboundHandlerAdapter {
    private AtomicBoolean matching = new AtomicBoolean(true);
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        /*读取收到的第一个包*/
        if(matching.getAndSet(false)){

        }
    }
}
