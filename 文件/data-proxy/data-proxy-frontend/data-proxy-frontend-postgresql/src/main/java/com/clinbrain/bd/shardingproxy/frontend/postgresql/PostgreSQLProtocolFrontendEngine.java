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

package com.clinbrain.bd.shardingproxy.frontend.postgresql;

import lombok.Getter;
import com.clinbrain.bd.core.constant.DatabaseType;
import com.clinbrain.bd.shardingproxy.backend.communication.jdbc.connection.BackendConnection;
import com.clinbrain.bd.shardingproxy.frontend.context.FrontendContext;
import com.clinbrain.bd.shardingproxy.frontend.postgresql.auth.PostgreSQLAuthenticationEngine;
import com.clinbrain.bd.shardingproxy.frontend.postgresql.command.PostgreSQLCommandExecuteEngine;
import com.clinbrain.bd.shardingproxy.frontend.spi.DatabaseProtocolFrontendEngine;
import com.clinbrain.bd.shardingproxy.transport.codec.DatabasePacketCodecEngine;
import com.clinbrain.bd.shardingproxy.transport.postgresql.codec.PostgreSQLPacketCodecEngine;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.command.query.binary.BinaryStatementRegistry;

/**
 * Frontend engine for PostgreSQL protocol.
 *
 * @author zhangyonglun
 * @author zhangliang
 */
@Getter
public final class PostgreSQLProtocolFrontendEngine implements DatabaseProtocolFrontendEngine {
    
    private final FrontendContext frontendContext = new FrontendContext(true, false);
    
    private final PostgreSQLAuthenticationEngine authEngine = new PostgreSQLAuthenticationEngine();
    
    private final PostgreSQLCommandExecuteEngine commandExecuteEngine = new PostgreSQLCommandExecuteEngine();
    
    private final DatabasePacketCodecEngine codecEngine = new PostgreSQLPacketCodecEngine();
    
    @Override
    public String getDatabaseType() {
        return DatabaseType.PostgreSQL.name();
    }


    @Override
    public void release(final BackendConnection backendConnection) {
        BinaryStatementRegistry.getInstance().unregister(backendConnection.getConnectionId());
    }
}
