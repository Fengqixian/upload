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

package com.clinbrain.bd.shardingproxy.transport.mysql.packet.command.query.text.query.fixture;

import lombok.Getter;
import com.clinbrain.bd.core.constant.DatabaseType;
import com.clinbrain.bd.transaction.core.ResourceDataSource;
import com.clinbrain.bd.transaction.core.TransactionOperationType;
import com.clinbrain.bd.transaction.core.TransactionType;
import com.clinbrain.bd.transaction.spi.ShardingTransactionManager;

import java.sql.Connection;
import java.util.Collection;
import java.util.LinkedList;

public final class ShardingTransactionManagerFixture implements ShardingTransactionManager {
    
    @Getter
    private static Collection<TransactionOperationType> invocations = new LinkedList<>();
    
    @Override
    public void init(final DatabaseType databaseType, final Collection<ResourceDataSource> resourceDataSources) {
    }
    
    @Override
    public TransactionType getTransactionType() {
        return TransactionType.XA;
    }
    
    @Override
    public boolean isInTransaction() {
        return true;
    }
    
    @Override
    public Connection getConnection(final String dataSourceName) {
        return null;
    }
    
    @Override
    public void begin() {
        invocations.add(TransactionOperationType.BEGIN);
    }
    
    @Override
    public void commit() {
        invocations.add(TransactionOperationType.COMMIT);
    }
    
    @Override
    public void rollback() {
        invocations.add(TransactionOperationType.ROLLBACK);
    }
    
    @Override
    public void close() {
    }
}
