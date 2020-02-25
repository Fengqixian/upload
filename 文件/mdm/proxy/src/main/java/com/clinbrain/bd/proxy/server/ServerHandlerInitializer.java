package com.clinbrain.bd.proxy.server;

import com.clinbrain.bd.proxy.adapter.postgres.PostgresChannelInboundHandlerAdapter;
import com.clinbrain.bd.proxy.client.Client;
import com.clinbrain.bd.proxy.client.mysql.MysqlClient;
import com.clinbrain.bd.proxy.client.postgres.PostgresClient;
import com.clinbrain.bd.proxy.codec.mysql.MysqlServerPacketCodec;
import com.clinbrain.bd.proxy.codec.postgres.PostgresServerPacketCodec;
import com.clinbrain.bd.proxy.config.NettyProxyConfig;
import com.clinbrain.bd.proxy.store.RegisterStore;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public final class ServerHandlerInitializer extends ChannelInitializer<SocketChannel> {
    private NettyProxyConfig config;
    public ServerHandlerInitializer(){}
    public ServerHandlerInitializer(NettyProxyConfig config){this.config = config;}
    private final static AtomicInteger connectionCount = new AtomicInteger(0);
    @Override
    protected void initChannel(final SocketChannel socketChannel) {
        /*判断是什么数据库类型 添加相应的拆包工具*/
        switch (config.getType()){
            case GP:
            case PG:
                socketChannel.pipeline().addLast(new PostgresServerPacketCodec());
                socketChannel.pipeline().addLast(new PostgresChannelInboundHandlerAdapter());
                break;
            case MYSQL:
                socketChannel.pipeline().addLast(new MysqlServerPacketCodec());
                break;
            case SQLSERVER:
                break;
            case ORACLE:
                break;
            default:
                break;
        }
        socketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
            @Override
            public void channelActive(final ChannelHandlerContext context) throws Exception {
                //在这里可以获取到客户端需要连接什么数据库
                RegisterStore.register(context.channel().id().asLongText());
                Client client = null;
                switch (config.getType()){
                    case GP:
                    case PG:
                        client = new PostgresClient(context,config.get(RegisterStore.getDatabaseConnectionInfo(context.channel().id().asLongText()).getDatabase()));
                        break;
                    case MYSQL:
                        client = new MysqlClient(context,config.get("metadata"));
                        break;
                    case SQLSERVER:
                        break;
                    case ORACLE:
                        break;
                    default:
                        break;
                }
                RegisterStore.register(context.channel().id().asLongText(),client);
                client.startClient();
                log.debug("A connection is adding current total:{}",connectionCount.incrementAndGet());
            }

            @Override
            public void channelInactive(ChannelHandlerContext context) throws Exception {
                super.channelInactive(context);
                RegisterStore.unregister(context.channel().id().asLongText());
                log.debug("A connection is leaving current total:{}",connectionCount.decrementAndGet());
            }

            @Override
            public void channelRead(final ChannelHandlerContext context, final Object message) throws InterruptedException {
                RegisterStore.getClientMessageStore(context.channel().id().asLongText()).put(message);
            }
        });
    }
}