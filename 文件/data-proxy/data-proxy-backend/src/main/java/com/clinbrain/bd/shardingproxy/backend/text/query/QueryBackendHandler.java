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

package com.clinbrain.bd.shardingproxy.backend.text.query;

import lombok.RequiredArgsConstructor;
import com.clinbrain.bd.shardingproxy.backend.communication.DatabaseCommunicationEngine;
import com.clinbrain.bd.shardingproxy.backend.communication.DatabaseCommunicationEngineFactory;
import com.clinbrain.bd.shardingproxy.backend.communication.jdbc.connection.BackendConnection;
import com.clinbrain.bd.shardingproxy.backend.exception.NoDatabaseSelectedException;
import com.clinbrain.bd.shardingproxy.backend.response.BackendResponse;
import com.clinbrain.bd.shardingproxy.backend.response.error.ErrorResponse;
import com.clinbrain.bd.shardingproxy.backend.response.query.QueryData;
import com.clinbrain.bd.shardingproxy.backend.text.TextProtocolBackendHandler;

import java.sql.SQLException;

/**
 * Backend handler with query.
 *
 * @author zhangliang
 */
@RequiredArgsConstructor
public final class QueryBackendHandler implements TextProtocolBackendHandler {
    
    private final DatabaseCommunicationEngineFactory databaseCommunicationEngineFactory = DatabaseCommunicationEngineFactory.getInstance();
    
    private final String sql;
    
    private final BackendConnection backendConnection;
    
    private DatabaseCommunicationEngine databaseCommunicationEngine;
    
    @Override
    public BackendResponse execute() {
        if (null == backendConnection.getLogicSchema()) {
            return new ErrorResponse(new NoDatabaseSelectedException());
        }
        databaseCommunicationEngine = databaseCommunicationEngineFactory.newTextProtocolInstance(backendConnection.getLogicSchema(), sql, backendConnection);
        return databaseCommunicationEngine.execute();
    }
    
    @Override
    public boolean next() throws SQLException {
        return databaseCommunicationEngine.next();
    }
    
    @Override
    public QueryData getQueryData() throws SQLException {
        return databaseCommunicationEngine.getQueryData();
    }
}
