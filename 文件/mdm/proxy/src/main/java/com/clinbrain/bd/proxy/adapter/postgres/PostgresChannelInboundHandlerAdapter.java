package com.clinbrain.bd.proxy.adapter.postgres;

import com.clinbrain.bd.proxy.config.DatabaseConnectionInfo;
import com.clinbrain.bd.proxy.store.RegisterStore;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author WANGYI
 * @className com.clinbrain.bd.proxy.adapter.postgres.PostgresChannelInboundHandlerAdapter
 * @createdDate 2019/8/30 16:34
 * @description TODO 数据库前置handler
 * 主要完成
 * 1、回应握手包 --->获取握手信息：包括连接相关的信息（除密码）
 * 2、密码请求包 --->获取密码信息
 * 3、以上信息注册
 * 4、移除handler
 * @e-mail WANGYI@clinbrain.com
 * @group bigdata develop group (mdm)
 */
public class PostgresChannelInboundHandlerAdapter extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(final ChannelHandlerContext context) throws InterruptedException {
        RegisterStore.register(context.channel().id().asLongText(),new DatabaseConnectionInfo());
        //发送一个握手包获取客户端连接的目标数据库
        ByteBuf payload = context.alloc().buffer();
        payload.writeByte('N');
        context.writeAndFlush(payload);

        ByteBuf password  = context.alloc().buffer();
        password.writeByte('R');
        password.writeInt(8);
        password.writeInt(3);
        context.writeAndFlush(password);
        //context.fireChannelActive();
    }
    @Override
    public void channelRead(final ChannelHandlerContext context, final Object message) throws Exception {
        /*读取前端发送回来的包*/
        if(message instanceof ByteBuf){
            ByteBuf msg = (ByteBuf) message;
            byte b = msg.markReaderIndex().readByte();
            msg.resetReaderIndex();
            /*这是客户端握手包，主要包括数据库连接的信息，不包括密码*/
            if(b == '\0' && msg.readableBytes()>8){
                /*跳过8个字节*/
                msg.skipBytes(8);
                int len = 0;
                Map<String ,String> params = new HashMap<>();
                while((len=msg.bytesBefore((byte)0))!=0){
                    byte[] bytes = new byte[len];
                    msg.readBytes(bytes);
                    String key = new String (bytes);
                    msg.skipBytes(1);
                    String value = null;
                    if((len=msg.bytesBefore((byte)0))!=0){
                        bytes = new byte[len];
                        msg.readBytes(bytes);
                        value = new String (bytes);
                        msg.skipBytes(1);
                    }
                    params.put(key,value);
                }
                RegisterStore.getDatabaseConnectionInfo(context.channel().id().asLongText()).setParams(params);
                RegisterStore.getDatabaseConnectionInfo(context.channel().id().asLongText()).setUsername(params.get("user"));
                RegisterStore.getDatabaseConnectionInfo(context.channel().id().asLongText()).setDatabase(params.get("database"));
                /*这个包需要留存 但是还是需要篡改所以还是在另外地方去留存*/
            }else if(b == 'p'){
                msg.skipBytes(1);
                int len = msg.readInt()-4;
                byte[] password = new byte[len];
                msg.readBytes(password);
                String md5Password = new String(password);
                RegisterStore.getDatabaseConnectionInfo(context.channel().id().asLongText()).setPassword(md5Password);
                /*验证账号密码的正确性这里应该去统一认证中心去认证  -- 暂时简单认证*/
                DatabaseConnectionInfo connectionInfo = RegisterStore.getDatabaseConnectionInfo(context.channel().id().asLongText());
                if(!"admin".equals(StringUtils.trim(connectionInfo.getPassword()))){
                    context.close();
                    throw new Exception();
                }
                context.pipeline().remove(this);
                context.fireChannelActive();
                /*密码包需要篡改 因为需要后端数据库的盐所以在其他地方去篡改*/
            }
        }
    }
}
