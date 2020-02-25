package com.clinbrain.bd.proxy.listener;

import com.clinbrain.bd.proxy.config.NettyClientConfig;
import com.clinbrain.bd.proxy.config.NettyProxyConfig;
import com.clinbrain.bd.proxy.config.NettyServerConfig;
import com.clinbrain.bd.proxy.server.ServerHandlerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

@Component
@Slf4j
public class NettyServerListener {
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    @Resource
    private NettyServerConfig nettyConfig;
    @Resource
    private NettyProxyConfig nettyProxyConfig;
    @PreDestroy
    public void close() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
    @PostConstruct
    public void start() throws InterruptedException {
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bossGroup = createEventLoopGroup();
            if (bossGroup instanceof EpollEventLoopGroup) {
                groupsEpoll(bootstrap);
            } else {
                groupsNio(bootstrap);
            }
            ChannelFuture future = bootstrap.bind(nettyConfig.getPort()).sync();
            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
    private EventLoopGroup createEventLoopGroup() {
        return Epoll.isAvailable() ? new EpollEventLoopGroup(1) : new NioEventLoopGroup(1);
    }

    private void groupsEpoll(final ServerBootstrap bootstrap) {
        workerGroup = new EpollEventLoopGroup();
        bootstrap.group(bossGroup, workerGroup)
                .channel(EpollServerSocketChannel.class)
                .option(EpollChannelOption.SO_BACKLOG, 128)
                .option(EpollChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(8 * 1024 * 1024, 16 * 1024 * 1024))
                .option(EpollChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childOption(EpollChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childOption(EpollChannelOption.TCP_NODELAY, true)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ServerHandlerInitializer(nettyProxyConfig));
    }

    private void groupsNio(final ServerBootstrap bootstrap) {
        workerGroup = new NioEventLoopGroup();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .option(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(8 * 1024 * 1024, 16 * 1024 * 1024))
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ServerHandlerInitializer(nettyProxyConfig));
    }
}