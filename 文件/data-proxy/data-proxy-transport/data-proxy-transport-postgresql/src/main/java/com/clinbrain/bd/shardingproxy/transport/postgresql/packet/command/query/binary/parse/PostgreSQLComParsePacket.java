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

package com.clinbrain.bd.shardingproxy.transport.postgresql.packet.command.query.binary.parse;

import com.clinbrain.bd.shardingproxy.transport.postgresql.constant.PostgreSQLColumnType;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.command.PostgreSQLCommandPacket;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.command.PostgreSQLCommandPacketType;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.command.query.binary.PostgreSQLBinaryStatementParameterType;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.command.query.binary.bind.protocol.PostgreSQLBinaryProtocolValue;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.command.query.binary.bind.protocol.PostgreSQLBinaryProtocolValueFactory;
import com.clinbrain.bd.shardingproxy.transport.postgresql.payload.PostgreSQLPacketPayload;
import lombok.Getter;
import lombok.ToString;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import com.clinbrain.bd.core.parse.PostgreSQLRebuilderListener;
import com.clinbrain.bd.core.parse.autogen.PostgreSQLStatementLexer;
import com.clinbrain.bd.core.parse.autogen.PostgreSQLStatementParser;
import com.clinbrain.bd.core.parse.util.ParseTreeWalker;
import com.clinbrain.bd.core.parse.util.PostgreSQLParseUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Command parse packet for PostgreSQL.
 *
 * @author zhangyonglun
 */
@Getter
@ToString
public final class PostgreSQLComParsePacket extends PostgreSQLCommandPacket {
    
    private final String statementId;
    
    private String sql;
    private String username;
    private final List<PostgreSQLBinaryStatementParameterType> binaryStatementParameterTypes;
    
    public PostgreSQLComParsePacket(final PostgreSQLPacketPayload payload, String username) {
        this.username = username;
        int len = payload.readInt4();
        statementId = payload.readStringNul();
        sql = alterSQLToJDBCStyle(payload.readStringNul());
        String num =String.valueOf( payload.getByteBuf().markReaderIndex().readBytes(2));
        payload.getByteBuf().resetReaderIndex();
        binaryStatementParameterTypes = sql.isEmpty() ? Collections.<PostgreSQLBinaryStatementParameterType>emptyList() : getParameterTypes(payload);
        /*if(StringUtils.isNotBlank(sql)){
            rebuildSQL();
        }*/
    }
    
    private List<PostgreSQLBinaryStatementParameterType> getParameterTypes(final PostgreSQLPacketPayload payload) {
        int parameterCount = payload.readInt2();
        List<PostgreSQLBinaryStatementParameterType> result = new ArrayList<>(parameterCount);
        for (int i = 0; i < parameterCount; i++) {
            result.add(new PostgreSQLBinaryStatementParameterType(PostgreSQLColumnType.valueOf(payload.readInt4())));
        }
        return result;
    }
    
    private String alterSQLToJDBCStyle(final String sql) {
        return sql.replaceAll("\\$[0-9]+", "?");
    }
    
    @Override
    public void write(final PostgreSQLPacketPayload payload) {

    }
    
    @Override
    public char getMessageType() {
        return PostgreSQLCommandPacketType.PARSE.getValue();
    }

    private void rebuildSQL(){
        PostgreSQLRebuilderListener listener = new PostgreSQLRebuilderListener(username);
        CharStream input = CharStreams.fromString(sql);
        PostgreSQLStatementLexer lexer = new PostgreSQLStatementLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PostgreSQLStatementParser parser = new PostgreSQLStatementParser(tokens);
        ParserRuleContext parserRuleContext = parser.select();
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
}
