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

package com.clinbrain.bd.shardingproxy.frontend.mysql.command.query.text.query;

import com.clinbrain.bd.core.constant.DatabaseType;
import com.clinbrain.bd.shardingproxy.backend.communication.jdbc.connection.BackendConnection;
import com.clinbrain.bd.shardingproxy.backend.response.BackendResponse;
import com.clinbrain.bd.shardingproxy.backend.response.error.ErrorResponse;
import com.clinbrain.bd.shardingproxy.backend.response.query.QueryHeader;
import com.clinbrain.bd.shardingproxy.backend.response.query.QueryResponse;
import com.clinbrain.bd.shardingproxy.backend.response.update.UpdateResponse;
import com.clinbrain.bd.shardingproxy.backend.text.TextProtocolBackendHandler;
import com.clinbrain.bd.shardingproxy.backend.text.TextProtocolBackendHandlerFactory;
import com.clinbrain.bd.shardingproxy.context.ShardingProxyContext;
import com.clinbrain.bd.shardingproxy.error.CommonErrorCode;
import com.clinbrain.bd.shardingproxy.frontend.api.QueryCommandExecutor;
import com.clinbrain.bd.shardingproxy.frontend.mysql.MySQLErrPacketFactory;
import com.clinbrain.bd.shardingproxy.transport.mysql.constant.MySQLColumnType;
import com.clinbrain.bd.shardingproxy.transport.mysql.packet.MySQLPacket;
import com.clinbrain.bd.shardingproxy.transport.mysql.packet.command.query.MySQLColumnDefinition41Packet;
import com.clinbrain.bd.shardingproxy.transport.mysql.packet.command.query.MySQLFieldCountPacket;
import com.clinbrain.bd.shardingproxy.transport.mysql.packet.command.query.text.MySQLTextResultSetRowPacket;
import com.clinbrain.bd.shardingproxy.transport.mysql.packet.command.query.text.query.MySQLComQueryPacket;
import com.clinbrain.bd.shardingproxy.transport.mysql.packet.generic.MySQLEofPacket;
import com.clinbrain.bd.shardingproxy.transport.mysql.packet.generic.MySQLErrPacket;
import com.clinbrain.bd.shardingproxy.transport.mysql.packet.generic.MySQLOKPacket;
import com.clinbrain.bd.shardingproxy.transport.packet.DatabasePacket;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * COM_QUERY command packet executor for MySQL.
 *
 * @author zhangliang
 */
public final class MySQLComQueryPacketExecutor implements QueryCommandExecutor {
    
    private final TextProtocolBackendHandler textProtocolBackendHandler;
    
    private volatile boolean isQuery;
    
    private int currentSequenceId;
    
    public MySQLComQueryPacketExecutor(final MySQLComQueryPacket comQueryPacket, final BackendConnection backendConnection) {
        textProtocolBackendHandler = TextProtocolBackendHandlerFactory.newInstance(DatabaseType.MySQL, comQueryPacket.getSql(), backendConnection);
    }
    
    @Override
    public Collection<DatabasePacket> execute() {
        if (ShardingProxyContext.getInstance().isCircuitBreak()) {
            return Collections.<DatabasePacket>singletonList(new MySQLErrPacket(1, CommonErrorCode.CIRCUIT_BREAK_MODE));
        }
        BackendResponse backendResponse = textProtocolBackendHandler.execute();
        if (backendResponse instanceof ErrorResponse) {
            return Collections.<DatabasePacket>singletonList(createErrorPacket(((ErrorResponse) backendResponse).getCause()));
        }
        if (backendResponse instanceof UpdateResponse) {
            return Collections.<DatabasePacket>singletonList(createUpdatePacket((UpdateResponse) backendResponse));
        }
        isQuery = true;
        return createQueryPackets((QueryResponse) backendResponse);
    }
    
    private MySQLErrPacket createErrorPacket(final Exception cause) {
        return MySQLErrPacketFactory.newInstance(1, cause);
    }
    
    private MySQLOKPacket createUpdatePacket(final UpdateResponse updateResponse) {
        return new MySQLOKPacket(1, updateResponse.getUpdateCount(), updateResponse.getLastInsertId());
    }
    
    private Collection<DatabasePacket> createQueryPackets(final QueryResponse backendResponse) {
        Collection<DatabasePacket> result = new LinkedList<>();
        List<QueryHeader> queryHeader = backendResponse.getQueryHeaders();
        result.add(new MySQLFieldCountPacket(++currentSequenceId, queryHeader.size()));
        for (QueryHeader each : queryHeader) {
            result.add(new MySQLColumnDefinition41Packet(++currentSequenceId, each.getSchema(), each.getTable(), each.getTable(), 
                    each.getColumnLabel(), each.getColumnName(), each.getColumnLength(), MySQLColumnType.valueOfJDBCType(each.getColumnType()), each.getDecimals()));
        }
        result.add(new MySQLEofPacket(++currentSequenceId));
        return result;
    }
    
    @Override
    public boolean isQuery() {
        return isQuery;
    }
    
    @Override
    public boolean next() throws SQLException {
        return textProtocolBackendHandler.next();
    }
    
    @Override
    public MySQLPacket getQueryData() throws SQLException {
        return new MySQLTextResultSetRowPacket(++currentSequenceId, textProtocolBackendHandler.getQueryData().getData());
    }
}
