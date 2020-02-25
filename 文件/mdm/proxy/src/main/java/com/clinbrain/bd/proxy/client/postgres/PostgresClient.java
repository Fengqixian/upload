package com.clinbrain.bd.proxy.client.postgres;

import com.clinbrain.bd.proxy.client.Client;
import com.clinbrain.bd.proxy.config.NettyClientConfig;
import com.clinbrain.bd.proxy.store.RegisterStore;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;

@Slf4j
public class PostgresClient extends Client {
    public PostgresClient(ChannelHandlerContext proxyContext, NettyClientConfig config) throws InterruptedException {
        super(proxyContext,config,new PostgresClientHandlerInitializer(proxyContext,config));
    }
    @Override
    public void init(){
        /*添加两个包*/
        ByteBuf sslRequest  = future.channel().alloc().buffer(8);
        sslRequest.writeInt(8);
        sslRequest.writeInt(80877103);
        try {
            RegisterStore.getClientMessageStore(proxyContext.channel().id().asLongText()).put(sslRequest);
        } catch (InterruptedException e) {
            log.error("put sslRequest message to proxy has error:",e);
        }

        ByteBuf byteBuf = future.channel().alloc().buffer();
        byteBuf.writeInt(196608);
        Map<String,String> params = RegisterStore.getDatabaseConnectionInfo(proxyContext.channel().id().asLongText()).getParams();

        String value = null;
        value = config.getUsername();
        byteBuf.writeBytes("user".getBytes());
        byteBuf.writeByte('\0');
        if(value !=null){
            byteBuf.writeBytes(value.getBytes());
        }
        byteBuf.writeByte('\0');

        value = params.get("database");
        byteBuf.writeBytes("database".getBytes());
        byteBuf.writeByte('\0');
        if(value !=null){
            byteBuf.writeBytes(value.getBytes());
        }
        byteBuf.writeByte('\0');

        value = params.get("client_encoding");
        byteBuf.writeBytes("client_encoding".getBytes());
        byteBuf.writeByte('\0');
        if(value !=null){
            byteBuf.writeBytes(value.getBytes());
        }
        byteBuf.writeByte('\0');

        value = params.get("DateStyle");
        byteBuf.writeBytes("DateStyle".getBytes());
        byteBuf.writeByte('\0');
        if(value !=null){
            byteBuf.writeBytes(value.getBytes());
        }
        byteBuf.writeByte('\0');

        value = params.get("TimeZone");
        byteBuf.writeBytes("TimeZone".getBytes());
        byteBuf.writeByte('\0');
        if(value !=null){
            byteBuf.writeBytes(value.getBytes());
        }
        byteBuf.writeByte('\0');

        value = params.get("extra_float_digits");
        byteBuf.writeBytes("extra_float_digits".getBytes());
        byteBuf.writeByte('\0');
        if(value !=null){
            byteBuf.writeBytes(value.getBytes());
        }
        byteBuf.writeByte('\0');
        byteBuf.writeByte('\0');

        ByteBuf startupMessage  = future.channel().alloc().buffer();
        startupMessage.writeInt(4+byteBuf.readableBytes());
        startupMessage.writeBytes(byteBuf);
        try {
            RegisterStore.getClientMessageStore(proxyContext.channel().id().asLongText()).put(startupMessage);
        } catch (InterruptedException e) {
            log.error("put startupMessage message to proxy has error:",e);
        }
    }
}
