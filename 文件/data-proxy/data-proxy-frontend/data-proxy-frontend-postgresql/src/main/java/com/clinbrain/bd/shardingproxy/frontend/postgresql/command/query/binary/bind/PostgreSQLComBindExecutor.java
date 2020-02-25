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

package com.clinbrain.bd.shardingproxy.frontend.postgresql.command.query.binary.bind;

import com.google.common.base.Optional;
import com.clinbrain.bd.shardingproxy.backend.communication.DatabaseCommunicationEngine;
import com.clinbrain.bd.shardingproxy.backend.communication.DatabaseCommunicationEngineFactory;
import com.clinbrain.bd.shardingproxy.backend.communication.jdbc.connection.BackendConnection;
import com.clinbrain.bd.shardingproxy.backend.response.BackendResponse;
import com.clinbrain.bd.shardingproxy.backend.response.error.ErrorResponse;
import com.clinbrain.bd.shardingproxy.backend.response.query.QueryData;
import com.clinbrain.bd.shardingproxy.backend.response.query.QueryHeader;
import com.clinbrain.bd.shardingproxy.backend.response.query.QueryResponse;
import com.clinbrain.bd.shardingproxy.backend.response.update.UpdateResponse;
import com.clinbrain.bd.shardingproxy.context.ShardingProxyContext;
import com.clinbrain.bd.shardingproxy.frontend.api.QueryCommandExecutor;
import com.clinbrain.bd.shardingproxy.transport.packet.DatabasePacket;
import com.clinbrain.bd.shardingproxy.transport.postgresql.constant.PostgreSQLColumnType;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.PostgreSQLPacket;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.command.query.PostgreSQLColumnDescription;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.command.query.PostgreSQLRowDescriptionPacket;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.command.query.binary.bind.PostgreSQLBinaryResultSetRowPacket;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.command.query.binary.bind.PostgreSQLBindCompletePacket;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.command.query.binary.bind.PostgreSQLComBindPacket;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.command.query.text.PostgreSQLDataRowPacket;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.generic.PostgreSQLCommandCompletePacket;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.generic.PostgreSQLErrorResponsePacket;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Command bind executor for PostgreSQL.
 *
 * @author zhangyonglun
 * @author zhangliang
 */
@Slf4j
public final class PostgreSQLComBindExecutor implements QueryCommandExecutor {
    
    private final PostgreSQLComBindPacket packet;
            
    private final DatabaseCommunicationEngine databaseCommunicationEngine;
    
    private volatile boolean isQuery;
    
    public PostgreSQLComBindExecutor(final PostgreSQLComBindPacket packet, final BackendConnection backendConnection) {
        this.packet = packet;
        databaseCommunicationEngine = null == packet.getSql()
                ? null : DatabaseCommunicationEngineFactory.getInstance().newBinaryProtocolInstance(backendConnection.getLogicSchema(), packet.getSql(), packet.getParameters(), backendConnection);
    }
    
    @Override
    public Collection<DatabasePacket> execute() {
        if (ShardingProxyContext.getInstance().isCircuitBreak()) {
            return Collections.<DatabasePacket>singletonList(new PostgreSQLErrorResponsePacket());
        }
        List<DatabasePacket> result = new LinkedList<>();
        /*发送bind成功数据包*/
        result.add(new PostgreSQLBindCompletePacket());
        if (null == databaseCommunicationEngine) {
            log.debug("databaseCommunicationEngine 为空。。。。。。。。。。。。。。。。。。。。。。。");
            return result;
        }
        BackendResponse backendResponse = databaseCommunicationEngine.execute();
        log.debug("backendResponse ：{}",backendResponse);
        if (backendResponse instanceof ErrorResponse) {
            result.add(createErrorPacket((ErrorResponse) backendResponse));
        }
        if (backendResponse instanceof UpdateResponse) {
            result.add(createUpdatePacket((UpdateResponse) backendResponse));
        }
        if (backendResponse instanceof QueryResponse) {
            Optional<PostgreSQLRowDescriptionPacket> postgreSQLRowDescriptionPacket = createQueryPacket((QueryResponse) backendResponse);
            log.debug("postgreSQLRowDescriptionPacket ：{}",postgreSQLRowDescriptionPacket.isPresent());
            if (postgreSQLRowDescriptionPacket.isPresent()) {
                result.add(postgreSQLRowDescriptionPacket.get());
            }
        }
        return result;
    }
    
    private PostgreSQLErrorResponsePacket createErrorPacket(final ErrorResponse errorResponse) {
        return new PostgreSQLErrorResponsePacket();
    }
    
    private PostgreSQLCommandCompletePacket createUpdatePacket(final UpdateResponse updateResponse) {
        return new PostgreSQLCommandCompletePacket();
    }
    
    private Optional<PostgreSQLRowDescriptionPacket> createQueryPacket(final QueryResponse queryResponse) {
        List<PostgreSQLColumnDescription> columnDescriptions = getPostgreSQLColumnDescriptions(queryResponse);
        log.debug("columnDescriptions:{}",columnDescriptions);
        isQuery = !columnDescriptions.isEmpty();
        if (columnDescriptions.isEmpty() || packet.isBinaryRowData()) {
            log.debug("columnDescriptions:{},isBinaryRowData:{}",columnDescriptions.isEmpty(),packet.isBinaryRowData());
            return Optional.absent();
        }
        return Optional.of(new PostgreSQLRowDescriptionPacket(columnDescriptions.size(), columnDescriptions));
    }
    
    private List<PostgreSQLColumnDescription> getPostgreSQLColumnDescriptions(final QueryResponse queryResponse) {
        List<PostgreSQLColumnDescription> result = new LinkedList<>();
        int columnIndex = 0;
        for (QueryHeader each : queryResponse.getQueryHeaders()) {
            result.add(new PostgreSQLColumnDescription(each.getColumnName(), ++columnIndex, each.getColumnType(), each.getColumnLength()));
        }
        return result;
    }
    
    @Override
    public boolean isQuery() {
        return isQuery;
    }
    
    @Override
    public boolean next() throws SQLException {
        return null != databaseCommunicationEngine && databaseCommunicationEngine.next();
    }
    
    @Override
    public PostgreSQLPacket getQueryData() throws SQLException {
        QueryData queryData = databaseCommunicationEngine.getQueryData();
        return packet.isBinaryRowData() ? new PostgreSQLBinaryResultSetRowPacket(queryData.getData(), getPostgreSQLColumnTypes(queryData)) : new PostgreSQLDataRowPacket(queryData.getData());
    }
    
    private List<PostgreSQLColumnType> getPostgreSQLColumnTypes(final QueryData queryData) {
        List<PostgreSQLColumnType> result = new ArrayList<>(queryData.getColumnTypes().size());
        for (int i = 0; i < queryData.getColumnTypes().size(); i++) {
            result.add(PostgreSQLColumnType.valueOfJDBCType(queryData.getColumnTypes().get(i)));
        }
        return result;
    }
}
