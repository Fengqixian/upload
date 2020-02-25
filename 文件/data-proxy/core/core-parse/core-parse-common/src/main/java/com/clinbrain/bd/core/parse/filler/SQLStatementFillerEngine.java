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

package com.clinbrain.bd.core.parse.filler;

import com.google.common.base.Optional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import com.clinbrain.bd.core.metadata.table.ShardingTableMetaData;
import com.clinbrain.bd.core.parse.filler.api.EncryptRuleAwareFiller;
import com.clinbrain.bd.core.parse.filler.api.SQLSegmentFiller;
import com.clinbrain.bd.core.parse.filler.api.ShardingRuleAwareFiller;
import com.clinbrain.bd.core.parse.filler.api.ShardingTableMetaDataAwareFiller;
import com.clinbrain.bd.core.parse.rule.registry.ParseRuleRegistry;
import com.clinbrain.bd.core.parse.rule.registry.statement.SQLStatementRule;
import com.clinbrain.bd.core.parse.sql.segment.SQLSegment;
import com.clinbrain.bd.core.parse.sql.statement.SQLStatement;
import com.clinbrain.bd.core.rule.BaseRule;
import com.clinbrain.bd.core.rule.EncryptRule;
import com.clinbrain.bd.core.rule.ShardingRule;
import com.clinbrain.bd.spi.DbType;

import java.util.Collection;

/**
 * SQL statement filler engine.
 *
 * @author zhangliang
 * @author panjuan
 * @author duhongjun
 */
@RequiredArgsConstructor
public final class SQLStatementFillerEngine {
    
    private final ParseRuleRegistry parseRuleRegistry;
    
    private final DbType databaseType;
    
    private final String sql;
    
    private final BaseRule rule;
    
    private final ShardingTableMetaData shardingTableMetaData;
    
    /**
     * Fill SQL statement.
     *
     * @param sqlSegments SQL segments
     * @param rule SQL statement rule
     * @return SQL statement
     */
    @SneakyThrows
    public SQLStatement fill(final Collection<SQLSegment> sqlSegments, final SQLStatementRule rule) {
        SQLStatement result = rule.getSqlStatementClass().newInstance();
        result.setLogicSQL(sql);
        result.getSqlSegments().addAll(sqlSegments);
        for (SQLSegment each : sqlSegments) {
            Optional<SQLSegmentFiller> filler = parseRuleRegistry.findSQLSegmentFiller(databaseType, each.getClass());
            if (filler.isPresent()) {
                doFill(each, result, filler.get());
            }
        }
        return result;
    }
    
    @SuppressWarnings("unchecked")
    private void doFill(final SQLSegment sqlSegment, final SQLStatement sqlStatement, final SQLSegmentFiller filler) {
        if (filler instanceof ShardingRuleAwareFiller) {
            ((ShardingRuleAwareFiller) filler).setShardingRule((ShardingRule) rule);
        }
        if (filler instanceof EncryptRuleAwareFiller) {
            ((EncryptRuleAwareFiller) filler).setEncryptRule((EncryptRule) rule);
        }
        if (filler instanceof ShardingTableMetaDataAwareFiller) {
            ((ShardingTableMetaDataAwareFiller) filler).setShardingTableMetaData(shardingTableMetaData);
        }
        filler.fill(sqlSegment, sqlStatement);
    }
}
