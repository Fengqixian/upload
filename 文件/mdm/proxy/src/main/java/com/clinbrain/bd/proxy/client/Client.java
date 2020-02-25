package com.clinbrain.bd.proxy.client;

import com.clinbrain.bd.proxy.client.postgres.PostgresClientHandlerInitializer;
import com.clinbrain.bd.proxy.config.NettyClientConfig;
import com.clinbrain.bd.proxy.store.RegisterStore;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public abstract class Client {
    private AtomicBoolean running = new AtomicBoolean(true);
    private EventLoopGroup group;
    private Bootstrap client;
    protected ChannelHandlerContext proxyContext;
    protected ChannelFuture future;
    protected NettyClientConfig config;
    public Client(ChannelHandlerContext proxyContext, NettyClientConfig config, ChannelInitializer handler) throws InterruptedException {
        this.proxyContext = proxyContext;
        this.config = config;
        group = new NioEventLoopGroup();
        client = new Bootstrap();
        client.group(group);
        client.channel(NioSocketChannel.class);
        client.remoteAddress(config.getHost(),config.getPort());
        client.handler(handler);
        future = client.connect().sync();
        init();
    }
    public abstract void init();
    public void stopClient(){
        running.set(false);
        future.channel().closeFuture();
        group.shutdownGracefully();
    }
    public void startClient(){
        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                while(running.get()){
                    Object msg = null;
                    try {
                        msg = RegisterStore.getClientMessageStore(proxyContext.channel().id().asLongText()).take();
                    } catch (InterruptedException e) {
                        log.error("acept message from client has error:",e);
                    }
                    future.channel().writeAndFlush(msg);
                }
            }
        });
    }
}
