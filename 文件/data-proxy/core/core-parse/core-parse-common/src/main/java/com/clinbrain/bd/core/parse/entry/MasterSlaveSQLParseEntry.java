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

package com.clinbrain.bd.core.parse.entry;

import com.clinbrain.bd.core.parse.SQLParseEngine;
import com.clinbrain.bd.core.parse.cache.ParsingResultCache;
import com.clinbrain.bd.core.parse.entry.SQLParseEntry;
import com.clinbrain.bd.core.parse.rule.registry.MasterSlaveParseRuleRegistry;
import com.clinbrain.bd.spi.DbType;

/**
 * SQL parse entry for master-slave.
 *
 * @author zhangliang
 */
public final class MasterSlaveSQLParseEntry extends SQLParseEntry {
    
    private final DbType databaseType;
    
    public MasterSlaveSQLParseEntry(final DbType databaseType) {
        super(new ParsingResultCache());
        this.databaseType = databaseType;
    }
    
    @Override
    protected SQLParseEngine getSQLParseEngine(final String sql) {
        return new SQLParseEngine(MasterSlaveParseRuleRegistry.getInstance(), databaseType, sql, null, null);
    }
}
