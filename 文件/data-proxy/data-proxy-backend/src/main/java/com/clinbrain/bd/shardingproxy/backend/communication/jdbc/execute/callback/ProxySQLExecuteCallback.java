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

package com.clinbrain.bd.shardingproxy.backend.communication.jdbc.execute.callback;

import com.clinbrain.bd.core.constant.ConnectionMode;
import com.clinbrain.bd.core.constant.DatabaseType;
import com.clinbrain.bd.core.execute.sql.execute.SQLExecuteCallback;
import com.clinbrain.bd.core.execute.sql.execute.result.MemoryQueryResult;
import com.clinbrain.bd.core.execute.sql.execute.result.QueryResult;
import com.clinbrain.bd.core.execute.sql.execute.result.StreamQueryResult;
import com.clinbrain.bd.core.route.RouteUnit;
import com.clinbrain.bd.core.rule.EncryptRule;
import com.clinbrain.bd.core.rule.ShardingRule;
import com.clinbrain.bd.shardingproxy.backend.communication.jdbc.connection.BackendConnection;
import com.clinbrain.bd.shardingproxy.backend.communication.jdbc.execute.response.ExecuteQueryResponse;
import com.clinbrain.bd.shardingproxy.backend.communication.jdbc.execute.response.ExecuteResponse;
import com.clinbrain.bd.shardingproxy.backend.communication.jdbc.execute.response.ExecuteUpdateResponse;
import com.clinbrain.bd.shardingproxy.backend.communication.jdbc.wrapper.JDBCExecutorWrapper;
import com.clinbrain.bd.shardingproxy.backend.response.query.QueryHeader;
import com.clinbrain.bd.shardingproxy.backend.schema.EncryptSchema;
import com.clinbrain.bd.shardingproxy.backend.schema.LogicSchema;
import com.clinbrain.bd.shardingproxy.backend.schema.LogicSchemas;
import com.clinbrain.bd.shardingproxy.backend.schema.ShardingSchema;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

/**
 * SQL execute callback for Sharding-Proxy.
 *
 * @author zhangliang
 */
public final class ProxySQLExecuteCallback extends SQLExecuteCallback<ExecuteResponse> {
    
    private final BackendConnection backendConnection;
    
    private final JDBCExecutorWrapper jdbcExecutorWrapper;
    
    private final boolean isReturnGeneratedKeys;
    
    private final boolean fetchMetaData;
    
    private boolean hasMetaData;
    
    public ProxySQLExecuteCallback(final BackendConnection backendConnection, final JDBCExecutorWrapper jdbcExecutorWrapper, 
                                   final boolean isExceptionThrown, final boolean isReturnGeneratedKeys, final boolean fetchMetaData) {
        super(DatabaseType.valueOf(LogicSchemas.getInstance().getDatabaseType().getName()), isExceptionThrown);
        this.backendConnection = backendConnection;
        this.jdbcExecutorWrapper = jdbcExecutorWrapper;
        this.isReturnGeneratedKeys = isReturnGeneratedKeys;
        this.fetchMetaData = fetchMetaData;
    }
    
    @Override
    public ExecuteResponse executeSQL(final RouteUnit routeUnit, final Statement statement, final ConnectionMode connectionMode) throws SQLException {
        boolean withMetaData = false;
        if (fetchMetaData && !hasMetaData) {
            hasMetaData = true;
            withMetaData = true;
        }
        return executeSQL(statement, routeUnit.getSqlUnit().getSql(), connectionMode, withMetaData);
    }
    
    private ExecuteResponse executeSQL(final Statement statement, final String sql, final ConnectionMode connectionMode, final boolean withMetadata) throws SQLException {
        backendConnection.add(statement);
        if (jdbcExecutorWrapper.executeSQL(statement, sql, isReturnGeneratedKeys)) {
            ResultSet resultSet = statement.getResultSet();
            backendConnection.add(resultSet);
            return new ExecuteQueryResponse(withMetadata ? getQueryHeaders(resultSet.getMetaData()) : null, createQueryResult(resultSet, connectionMode));
        }
        return new ExecuteUpdateResponse(statement.getUpdateCount(), isReturnGeneratedKeys ? getGeneratedKey(statement) : 0L);
    }
    
    private List<QueryHeader> getQueryHeaders(final ResultSetMetaData resultSetMetaData) throws SQLException {
        List<QueryHeader> result = new LinkedList<>();
        for (int columnIndex = 1; columnIndex <= resultSetMetaData.getColumnCount(); columnIndex++) {
            result.add(new QueryHeader(resultSetMetaData, backendConnection.getLogicSchema(), columnIndex));
        }
        return result;
    }
    
    private QueryResult createQueryResult(final ResultSet resultSet, final ConnectionMode connectionMode) {
        LogicSchema logicSchema = backendConnection.getLogicSchema();
        if (logicSchema instanceof ShardingSchema) {
            ShardingRule shardingRule = logicSchema.getShardingRule();
            return connectionMode == ConnectionMode.MEMORY_STRICTLY ? new StreamQueryResult(resultSet, shardingRule) : new MemoryQueryResult(resultSet, shardingRule);
        }
        if (logicSchema instanceof EncryptSchema) {
            EncryptRule encryptRule = ((EncryptSchema) logicSchema).getEncryptRule();
            return connectionMode == ConnectionMode.MEMORY_STRICTLY ? new StreamQueryResult(resultSet, encryptRule) : new MemoryQueryResult(resultSet, encryptRule);
        }
        return connectionMode == ConnectionMode.MEMORY_STRICTLY ? new StreamQueryResult(resultSet) : new MemoryQueryResult(resultSet);
    }
    
    private long getGeneratedKey(final Statement statement) throws SQLException {
        ResultSet resultSet = statement.getGeneratedKeys();
        return resultSet.next() ? resultSet.getLong(1) : 0L;
    }
}
