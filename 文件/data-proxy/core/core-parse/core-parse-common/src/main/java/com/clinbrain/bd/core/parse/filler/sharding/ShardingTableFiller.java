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

package com.clinbrain.bd.core.parse.filler.sharding;

import lombok.Setter;
import com.clinbrain.bd.core.parse.exception.SQLParsingException;
import com.clinbrain.bd.core.parse.filler.api.SQLSegmentFiller;
import com.clinbrain.bd.core.parse.filler.api.ShardingRuleAwareFiller;
import com.clinbrain.bd.core.parse.sql.context.table.Table;
import com.clinbrain.bd.core.parse.sql.segment.common.TableSegment;
import com.clinbrain.bd.core.parse.sql.statement.SQLStatement;
import com.clinbrain.bd.core.parse.sql.statement.dml.DMLStatement;
import com.clinbrain.bd.core.parse.sql.statement.dml.SelectStatement;
import com.clinbrain.bd.core.rule.ShardingRule;

/**
 * Table filler for sharding.
 *
 * @author duhongjun
 * @author zhangliang
 * @author panjuan
 */
@Setter
public final class ShardingTableFiller implements SQLSegmentFiller<TableSegment>, ShardingRuleAwareFiller {
    
    private ShardingRule shardingRule;
    
    @Override
    public void fill(final TableSegment sqlSegment, final SQLStatement sqlStatement) {
        if (isTableInShardingRule(sqlSegment.getName()) || !(sqlStatement instanceof SelectStatement)) {
            sqlStatement.getTables().add(new Table(sqlSegment.getName(), sqlSegment.getAlias().orNull()));
        }
        if (sqlStatement instanceof DMLStatement && !sqlStatement.getTables().isSingleTable()) {
            throw new SQLParsingException("Cannot support Multiple-Table.");
        }
    }
    
    private boolean isTableInShardingRule(final String tableName) {
        return shardingRule.contains(tableName) || shardingRule.findBindingTableRule(tableName).isPresent() || shardingRule.isBroadcastTable(tableName)
                || shardingRule.getShardingDataSourceNames().getDataSourceNames().contains(shardingRule.getShardingDataSourceNames().getDefaultDataSourceName());
    }
}
