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

package com.clinbrain.bd.shardingproxy.frontend.postgresql.command.query.binary.parse;

import com.clinbrain.bd.core.constant.DatabaseType;
import com.clinbrain.bd.core.parse.entry.ShardingSQLParseEntry;
import com.clinbrain.bd.core.parse.sql.statement.SQLStatement;
import com.clinbrain.bd.shardingproxy.backend.communication.jdbc.connection.BackendConnection;
import com.clinbrain.bd.shardingproxy.backend.schema.LogicSchema;
import com.clinbrain.bd.shardingproxy.frontend.api.CommandExecutor;
import com.clinbrain.bd.shardingproxy.transport.packet.DatabasePacket;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.command.query.binary.BinaryStatementRegistry;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.command.query.binary.ConnectionScopeBinaryStatementRegistry;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.command.query.binary.parse.PostgreSQLComParsePacket;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.command.query.binary.parse.PostgreSQLParseCompletePacket;
import com.clinbrain.bd.spi.DatabaseTypes;

import java.util.Collection;
import java.util.Collections;

/**
 * PostgreSQL command parse executor.
 *
 * @author zhangyonglun
 * @author zhangliang
 */
public final class PostgreSQLComParseExecutor implements CommandExecutor {
    
    private final PostgreSQLComParsePacket packet;
    
    private final LogicSchema logicSchema;
    
    private  volatile ConnectionScopeBinaryStatementRegistry binaryStatementRegistry;
    
    public PostgreSQLComParseExecutor(final PostgreSQLComParsePacket packet, final BackendConnection backendConnection) {
        this.packet = packet;
        logicSchema = backendConnection.getLogicSchema();
        binaryStatementRegistry = BinaryStatementRegistry.getInstance().get(backendConnection.getConnectionId());
    }
    
    @Override
    public Collection<DatabasePacket> execute() {
        // TODO we should use none-sharding parsing engine in future.
        ShardingSQLParseEntry shardingSQLParseEntry = new ShardingSQLParseEntry(
                DatabaseTypes.getTrunkDatabaseType(DatabaseType.PostgreSQL.name()), logicSchema.getShardingRule(), logicSchema.getMetaData().getTable(), logicSchema.getParsingResultCache());
        if (!packet.getSql().isEmpty()) {
            SQLStatement sqlStatement = shardingSQLParseEntry.parse(packet.getSql(), true);
            int parametersIndex = sqlStatement.getParametersIndex();
            binaryStatementRegistry.register(packet.getStatementId(), packet.getSql(), parametersIndex, packet.getBinaryStatementParameterTypes());
        }
        return Collections.<DatabasePacket>singletonList(new PostgreSQLParseCompletePacket());
    }
}
