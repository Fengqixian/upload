package com.clinbrain.bd.proxy.client.mysql;

import com.clinbrain.bd.proxy.client.Client;
import com.clinbrain.bd.proxy.config.NettyClientConfig;
import com.clinbrain.bd.proxy.store.RegisterStore;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class MysqlClient extends Client {
    public MysqlClient(ChannelHandlerContext proxyContext, NettyClientConfig config) throws InterruptedException {
        super(proxyContext,config,new MysqlClientHandlerInitializer(proxyContext,config));
    }
    @Override
    public void init(){
    }
}
