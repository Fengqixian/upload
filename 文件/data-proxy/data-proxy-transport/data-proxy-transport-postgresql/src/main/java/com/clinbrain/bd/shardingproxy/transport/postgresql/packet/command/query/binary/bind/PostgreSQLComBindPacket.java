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

package com.clinbrain.bd.shardingproxy.transport.postgresql.packet.command.query.binary.bind;

import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.command.PostgreSQLCommandPacket;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.command.PostgreSQLCommandPacketType;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.command.query.binary.BinaryStatementRegistry;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.command.query.binary.ConnectionScopeBinaryStatementRegistry;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.command.query.binary.PostgreSQLBinaryStatement;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.command.query.binary.PostgreSQLBinaryStatementParameterType;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.command.query.binary.bind.protocol.PostgreSQLBinaryProtocolValue;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.command.query.binary.bind.protocol.PostgreSQLBinaryProtocolValueFactory;
import com.clinbrain.bd.shardingproxy.transport.postgresql.payload.PostgreSQLPacketPayload;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.apache.commons.lang3.StringUtils;
import com.clinbrain.bd.core.parse.PostgreSQLRebuilderListener;
import com.clinbrain.bd.core.parse.autogen.PostgreSQLStatementLexer;
import com.clinbrain.bd.core.parse.autogen.PostgreSQLStatementParser;
import com.clinbrain.bd.core.parse.util.ParseTreeWalker;
import com.clinbrain.bd.core.parse.util.PostgreSQLParseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Command bind packet for PostgreSQL.
 *
 * @author zhangyonglun
 */
@Slf4j
@Getter
@ToString
public final class PostgreSQLComBindPacket extends PostgreSQLCommandPacket {
    private final static Logger LOGGER = LoggerFactory.getLogger(PostgreSQLComBindPacket.class);
    private final String statementId;
    private final String username;
    @Setter
    private String sql;
    
    private final List<Object> parameters;
    
    private final boolean binaryRowData;
    
    public PostgreSQLComBindPacket(final PostgreSQLPacketPayload payload, final int connectionId, String username) throws SQLException {
        this.username = username;
        //消息总长度（4+portalName+1+statementId+1+parameterFormatsLength）
        int contentLen =  payload.readInt4();
        log.info("----------------消息包总长度："+contentLen+"-------------------------");
        String portalName = payload.readStringNul();
        log.info("----------------消息包portalName："+portalName+"-------------------------");
        statementId = payload.readStringNul();
        log.info("----------------消息包statementId："+statementId+"-------------------------");
        int parameterFormatsLength = payload.readInt2();
        log.info("----------------消息包parameterFormatsLength："+parameterFormatsLength+"-------------------------");
        for (int i = 0; i < parameterFormatsLength; i++) {
            payload.readInt2();
        }
        ConnectionScopeBinaryStatementRegistry registry = BinaryStatementRegistry.getInstance().get(connectionId);
        PostgreSQLBinaryStatement binaryStatement = null;
        if(registry!=null){
            binaryStatement = registry.getBinaryStatement(statementId);
        }
        sql = null == binaryStatement ? null : binaryStatement.getSql();
        parameters = null == sql ? Collections.emptyList() : getParameters(payload, binaryStatement.getParameterTypes());
        int resultFormatsLength = 0;
        if((payload.getByteBuf().writerIndex() - payload.getByteBuf().readerIndex())<2){
            resultFormatsLength = payload.readInt1();
        }else{
            resultFormatsLength = payload.readInt2();
        }
        binaryRowData = resultFormatsLength > 0;
        for (int i = 0; i < resultFormatsLength; i++) {
            payload.readInt2();
        }
        LOGGER.info("The SQL front input by {} :{} with agrs {}",username,sql,parameters);
        /*if(StringUtils.isNotBlank(sql)){
            rebuildSQL();
        }*/
        LOGGER.info("The SQL real excute for {}:{} with agrs {}",username,sql,parameters);
    }
    private void rebuildSQL(){
        PostgreSQLRebuilderListener listener = new PostgreSQLRebuilderListener(username);
        CharStream input = CharStreams.fromString(sql);
        PostgreSQLStatementLexer lexer = new PostgreSQLStatementLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PostgreSQLStatementParser parser = new PostgreSQLStatementParser(tokens);
        ParserRuleContext parserRuleContext = parser.execute();
        /*if(parserRuleContext == null) {
            LOGGER.info("sql {} is not a select type .", sql);
            return;
        }*/
        ParseTreeWalker.DEFAULT.walkLMR(listener,parserRuleContext);
        sql = PostgreSQLParseUtil.tree2Sql(parserRuleContext);
    }
    
    private List<Object> getParameters(final PostgreSQLPacketPayload payload, final List<PostgreSQLBinaryStatementParameterType> parameterTypes) throws SQLException {
        int parametersCount = payload.readInt2();
        List<Object> result = new ArrayList<>(parametersCount);
        for (int parameterIndex = 0; parameterIndex < parametersCount; parameterIndex++) {
            payload.readInt4();
            PostgreSQLBinaryProtocolValue binaryProtocolValue = PostgreSQLBinaryProtocolValueFactory.getBinaryProtocolValue(parameterTypes.get(parameterIndex).getColumnType());
            result.add(binaryProtocolValue.read(payload));
        }
        return result;
    }
    
    @Override
    public void write(final PostgreSQLPacketPayload payload) {
    }
    
    @Override
    public char getMessageType() {
        return PostgreSQLCommandPacketType.BIND.getValue();
    }
}
