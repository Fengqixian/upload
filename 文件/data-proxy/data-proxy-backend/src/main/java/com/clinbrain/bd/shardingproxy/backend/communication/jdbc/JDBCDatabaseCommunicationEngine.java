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

package com.clinbrain.bd.shardingproxy.backend.communication.jdbc;

import lombok.RequiredArgsConstructor;
import com.clinbrain.bd.core.constant.DatabaseType;
import com.clinbrain.bd.core.constant.SQLType;
import com.clinbrain.bd.core.merge.MergeEngineFactory;
import com.clinbrain.bd.core.merge.MergedResult;
import com.clinbrain.bd.core.merge.dal.show.ShowTablesMergedResult;
import com.clinbrain.bd.core.parse.constant.DerivedColumn;
import com.clinbrain.bd.core.parse.sql.statement.SQLStatement;
import com.clinbrain.bd.core.route.SQLRouteResult;
import com.clinbrain.bd.shardingproxy.backend.communication.DatabaseCommunicationEngine;
import com.clinbrain.bd.shardingproxy.backend.communication.jdbc.connection.BackendConnection;
import com.clinbrain.bd.shardingproxy.backend.communication.jdbc.connection.ConnectionStatus;
import com.clinbrain.bd.shardingproxy.backend.communication.jdbc.execute.JDBCExecuteEngine;
import com.clinbrain.bd.shardingproxy.backend.exception.TableModifyInTransactionException;
import com.clinbrain.bd.shardingproxy.backend.response.BackendResponse;
import com.clinbrain.bd.shardingproxy.backend.response.error.ErrorResponse;
import com.clinbrain.bd.shardingproxy.backend.response.query.QueryData;
import com.clinbrain.bd.shardingproxy.backend.response.query.QueryHeader;
import com.clinbrain.bd.shardingproxy.backend.response.query.QueryResponse;
import com.clinbrain.bd.shardingproxy.backend.response.update.UpdateResponse;
import com.clinbrain.bd.shardingproxy.backend.schema.LogicSchema;
import com.clinbrain.bd.shardingproxy.backend.schema.LogicSchemas;
import com.clinbrain.bd.shardingproxy.backend.schema.ShardingSchema;
import com.clinbrain.bd.transaction.core.TransactionType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Database access engine for JDBC.
 *
 * @author zhaojun
 * @author zhangliang
 * @author panjuan
 * @author maxiaoguang
 */
@RequiredArgsConstructor
public final class JDBCDatabaseCommunicationEngine implements DatabaseCommunicationEngine {
    
    private final LogicSchema logicSchema;
    
    private final String sql;
    
    private final JDBCExecuteEngine executeEngine;
    
    private final DatabaseType databaseType = DatabaseType.valueOf(LogicSchemas.getInstance().getDatabaseType().getName());
    
    private BackendResponse response;
    
    private MergedResult mergedResult;
    
    @Override
    public BackendResponse execute() {
        try {
            SQLRouteResult routeResult = executeEngine.getJdbcExecutorWrapper().route(sql, databaseType);
            return execute(routeResult);
        } catch (final SQLException ex) {
            return new ErrorResponse(ex);
        }
    }
    
    private BackendResponse execute(final SQLRouteResult routeResult) throws SQLException {
        if (routeResult.getRouteUnits().isEmpty()) {
            return new UpdateResponse();
        }
        SQLStatement sqlStatement = routeResult.getSqlStatement();
        if (isExecuteDDLInXATransaction(sqlStatement.getType())) {
            return new ErrorResponse(new TableModifyInTransactionException(sqlStatement.getTables().isSingleTable() ? sqlStatement.getTables().getSingleTableName() : "unknown_table"));
        }
        response = executeEngine.execute(routeResult);
        if (logicSchema instanceof ShardingSchema) {
            logicSchema.refreshTableMetaData(routeResult.getSqlStatement());
        }
        return merge(routeResult);
    }
    
    private boolean isExecuteDDLInXATransaction(final SQLType sqlType) {
        BackendConnection connection = executeEngine.getBackendConnection();
        return TransactionType.XA == connection.getTransactionType() && SQLType.DDL == sqlType && ConnectionStatus.TRANSACTION == connection.getStateHandler().getStatus();
    }
    
    private BackendResponse merge(final SQLRouteResult routeResult) throws SQLException {
        if (response instanceof UpdateResponse) {
            if (!isAllBroadcastTables(routeResult.getSqlStatement())) {
                ((UpdateResponse) response).mergeUpdateCount();
            }
            return response;
        }
        mergedResult = MergeEngineFactory.newInstance(
            databaseType, logicSchema.getShardingRule(), routeResult, logicSchema.getMetaData().getTable(), ((QueryResponse) response).getQueryResults()).merge();
        if (mergedResult instanceof ShowTablesMergedResult) {
            ((ShowTablesMergedResult) mergedResult).resetColumnLabel(logicSchema.getName());
        }
        return getQueryHeaderResponseWithoutDerivedColumns(((QueryResponse) response).getQueryHeaders());
    }
    
    private boolean isAllBroadcastTables(final SQLStatement sqlStatement) {
        return logicSchema instanceof ShardingSchema && logicSchema.getShardingRule().isAllBroadcastTables(sqlStatement.getTables().getTableNames());
    }
    
    private QueryResponse getQueryHeaderResponseWithoutDerivedColumns(final List<QueryHeader> queryHeaders) {
        List<QueryHeader> derivedColumnQueryHeaders = new LinkedList<>();
        for (QueryHeader each : queryHeaders) {
            if (DerivedColumn.isDerivedColumn(each.getColumnLabel())) {
                derivedColumnQueryHeaders.add(each);
            }
        }
        queryHeaders.removeAll(derivedColumnQueryHeaders);
        return new QueryResponse(queryHeaders);
    }
    
    @Override
    public boolean next() throws SQLException {
        return null != mergedResult && mergedResult.next();
    }
    
    @Override
    public QueryData getQueryData() throws SQLException {
        List<QueryHeader> queryHeaders = ((QueryResponse) response).getQueryHeaders();
        List<Object> row = new ArrayList<>(queryHeaders.size());
        for (int columnIndex = 1; columnIndex <= queryHeaders.size(); columnIndex++) {
            row.add(mergedResult.getValue(columnIndex, Object.class));
        }
        return new QueryData(getColumnTypes(queryHeaders), row);
    }
    
    private List<Integer> getColumnTypes(final List<QueryHeader> queryHeaders) {
        List<Integer> result = new ArrayList<>(queryHeaders.size());
        for (QueryHeader each : queryHeaders) {
            result.add(each.getColumnType());
        }
        return result;
    }
}
