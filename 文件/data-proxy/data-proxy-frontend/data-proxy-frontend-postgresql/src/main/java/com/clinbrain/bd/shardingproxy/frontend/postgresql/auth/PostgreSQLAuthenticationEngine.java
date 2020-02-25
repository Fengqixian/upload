/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.clinbrain.bd.shardingproxy.frontend.postgresql.auth;

import com.google.common.base.Strings;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import com.clinbrain.bd.shardingproxy.backend.communication.jdbc.connection.BackendConnection;
import com.clinbrain.bd.shardingproxy.backend.schema.LogicSchemas;
import com.clinbrain.bd.shardingproxy.frontend.ConnectionIdGenerator;
import com.clinbrain.bd.shardingproxy.frontend.engine.AuthenticationEngine;
import com.clinbrain.bd.shardingproxy.transport.payload.PacketPayload;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.command.query.binary.BinaryStatementRegistry;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.generic.PostgreSQLErrorResponsePacket;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.generic.PostgreSQLReadyForQueryPacket;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.handshake.*;
import com.clinbrain.bd.shardingproxy.transport.postgresql.payload.PostgreSQLPacketPayload;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.postgresql.util.Base64;

import java.util.*;

/**
 * Authentication engine for PostgreSQL.
 *
 * @author zhangliang
 */
@Slf4j
public final class PostgreSQLAuthenticationEngine implements AuthenticationEngine {
    private final static Map userPasswordMap = new TreeMap();
    static {
        userPasswordMap.put("gpadmin","gpadmin");
        userPasswordMap.put("root1","root");
        userPasswordMap.put("root2","root");
    }
    private static final int SSL_REQUEST_PAYLOAD_LENGTH = 8;
    private static final int AUTHENTICATION_ING_CODE  = 112;

    private static final int SSL_REQUEST_CODE = 80877103;
    
    private static final String DATABASE_NAME_KEYWORD = "database";
    private static final String USER_NAME_KEYWORD = "user";

    @Override
    public void handshake(final ChannelHandlerContext context, final BackendConnection backendConnection) {
        log.info("-----------------------------------------------------------------------------");
        log.info("-------------------《《《《   开始握手  》》》》--------------------------------");
        log.info("-----------------------------------------------------------------------------");

        int connectionId = ConnectionIdGenerator.getInstance().nextId();
        log.info("-----------------------------------------------------------------------------");
        log.info("-------------------《《《《   连接ID:"+connectionId+"  》》》》--------------------------------");
        log.info("-----------------------------------------------------------------------------");
        backendConnection.setConnectionId(connectionId);
        BinaryStatementRegistry.getInstance().register(connectionId);
    }
    
    @Override
    public boolean auth(final ChannelHandlerContext context, final PacketPayload payload, final BackendConnection backendConnection) {
        int code = payload.getByteBuf().markReaderIndex().readInt();
        if (SSL_REQUEST_PAYLOAD_LENGTH == code && SSL_REQUEST_CODE == payload.getByteBuf().readInt()) {
            context.writeAndFlush(new PostgreSQLSSLNegativePacket());
            return false;
        }
        payload.getByteBuf().resetReaderIndex();
        code = payload.getByteBuf().readByte();
        /*如果是密码包*/
        if(AUTHENTICATION_ING_CODE ==code){
            log.info("-----------------------------------------------------------------------------");
            log.info("-------------------《《《《    密码包   》》》》--------------------------------");
            log.info("-----------------------------------------------------------------------------");
            //payload.getByteBuf().resetReaderIndex();
            PasswordMessagePacket passwordMessagePacket = new PasswordMessagePacket((PostgreSQLPacketPayload) payload);
            String username = backendConnection.getUserName();
            String password = passwordMessagePacket.getPassword();
            log.info("-----------------------------------------------------------------------------");
            log.info("-------------------《《《《账号："+username +"  》》》》--------------------------------");
            log.info("-------------------《《《《密码："+password +"  》》》》--------------------------------");
            log.info("-----------------------------------------------------------------------------");
            if(userPasswordMap.get(username)==null||!userPasswordMap.get(username).equals(password)){
                context.writeAndFlush(new PostgreSQLErrorResponsePacket());
                return false;
            }
            context.writeAndFlush(new PostgreSQLAuthenticationOKPacket(true));
            context.write(new PostgreSQLParameterStatusPacket("server_version", "8.4"));
            context.write(new PostgreSQLParameterStatusPacket("client_encoding", "UTF8"));
            context.write(new PostgreSQLParameterStatusPacket("server_encoding", "UTF8"));
            context.writeAndFlush(new PostgreSQLReadyForQueryPacket());
            log.info("-----------------------------------------------------------------------------");
            log.info("-------------------《《《《   账密完成  》》》》--------------------------------");
            log.info("-----------------------------------------------------------------------------");
            return true;
        }
        log.info("-----------------------------------------------------------------------------");
        log.info("-------------------《《《《   开始认证  》》》》--------------------------------");
        log.info("-----------------------------------------------------------------------------");
        payload.getByteBuf().resetReaderIndex();
        PostgreSQLComStartupPacket comStartupPacket = new PostgreSQLComStartupPacket((PostgreSQLPacketPayload) payload);
        String databaseName = comStartupPacket.getParametersMap().get(DATABASE_NAME_KEYWORD);
        String username = comStartupPacket.getParametersMap().get(USER_NAME_KEYWORD);
        log.info("-----------------------------------------------------------------------------");
        log.info("-------------------《《《《   连接DB:"+databaseName+"  》》》》--------------------------------");
        log.info("-------------------《《《《   连接用户:"+username+"  》》》》--------------------------------");
        log.info("-----------------------------------------------------------------------------");
        if (!Strings.isNullOrEmpty(databaseName) && !LogicSchemas.getInstance().schemaExists(databaseName)) {
            // TODO send an error message
            return true;
        }
        backendConnection.setCurrentSchema(databaseName);
        backendConnection.setUserName(username);

        context.writeAndFlush(new AuthenticationCleartextPasswordPacket());
        return false;
    }
}
