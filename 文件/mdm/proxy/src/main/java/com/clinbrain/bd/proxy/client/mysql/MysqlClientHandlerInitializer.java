package com.clinbrain.bd.proxy.client.mysql;

import com.clinbrain.bd.proxy.codec.mysql.MysqlClientPacketCodec;
import com.clinbrain.bd.proxy.codec.postgres.PostgresClientPacketCodec;
import com.clinbrain.bd.proxy.config.NettyClientConfig;
import com.clinbrain.bd.proxy.store.RegisterStore;
import com.clinbrain.bd.proxy.util.MD5Digest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public final class MysqlClientHandlerInitializer extends ChannelInitializer<SocketChannel> {
    private ChannelHandlerContext proxyContext;
    private NettyClientConfig config;
    @Override
    protected void initChannel(SocketChannel nioSocketChannel) throws Exception {
        /*需要改造成根据不同数据库连接用不同的解码器*/
        nioSocketChannel.pipeline().addLast(new MysqlClientPacketCodec());
        nioSocketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                 proxyContext.writeAndFlush(msg);
            }
            @Override
            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                log.error("ProxyClient has error",cause);
            }
        });
    }
}