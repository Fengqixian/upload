package com.clinbrain.bd.proxy.client.postgres;

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
public final class PostgresClientHandlerInitializer extends ChannelInitializer<SocketChannel> {
    private ChannelHandlerContext proxyContext;
    private NettyClientConfig config;
    @Override
    protected void initChannel(SocketChannel nioSocketChannel) throws Exception {
        /*需要改造成根据不同数据库连接用不同的解码器*/
        nioSocketChannel.pipeline().addLast(new PostgresClientPacketCodec());
        nioSocketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                /*密码获取包*/
                ByteBuf message = (ByteBuf) msg;
                byte b = message.markReaderIndex().readByte();
                message.resetReaderIndex();
                if(b == 'R'){
                    message.skipBytes(5);
                    int type = message.readInt();
                    if(type==5){
                        byte[] bytes = new byte[4];
                        message.readBytes(bytes);
                        byte[] md5Password = MD5Digest.encode(config.getUsername().getBytes(),config.getPassword().getBytes(),bytes);
                        ByteBuf byteBuf = ctx.alloc().buffer();
                        byteBuf.writeByte('p');
                        byteBuf.writeInt(md5Password.length+5);
                        byteBuf.writeBytes(md5Password);
                        byteBuf.writeByte(0);
                        RegisterStore.getClientMessageStore(proxyContext.channel().id().asLongText()).put(byteBuf);
                    }else if(type ==3){

                    }else if(type == 0){
                        message.resetReaderIndex();
                        proxyContext.writeAndFlush(message);
                    }else{
                        message.resetReaderIndex();
                        proxyContext.writeAndFlush(message);
                    }
                }else if(b == 'N'){
                    /*拦截掉*/
                } else{
                    proxyContext.writeAndFlush(msg);
                }
            }
            @Override
            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                log.error("ProxyClient has error",cause);
            }
        });
    }
}