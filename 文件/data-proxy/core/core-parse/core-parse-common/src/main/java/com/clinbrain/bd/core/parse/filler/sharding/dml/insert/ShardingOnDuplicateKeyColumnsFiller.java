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

package com.clinbrain.bd.core.parse.filler.sharding.dml.insert;

import lombok.Setter;
import com.clinbrain.bd.core.parse.exception.SQLParsingException;
import com.clinbrain.bd.core.parse.filler.api.SQLSegmentFiller;
import com.clinbrain.bd.core.parse.filler.api.ShardingRuleAwareFiller;
import com.clinbrain.bd.core.parse.sql.segment.dml.column.ColumnSegment;
import com.clinbrain.bd.core.parse.sql.segment.dml.column.OnDuplicateKeyColumnsSegment;
import com.clinbrain.bd.core.parse.sql.statement.SQLStatement;
import com.clinbrain.bd.core.rule.ShardingRule;

/**
 * On duplicate key columns filler.
 *
 * @author zhangliang
 */
@Setter
public final class ShardingOnDuplicateKeyColumnsFiller implements SQLSegmentFiller<OnDuplicateKeyColumnsSegment>, ShardingRuleAwareFiller {
    
    private ShardingRule shardingRule;
    
    @Override
    public void fill(final OnDuplicateKeyColumnsSegment sqlSegment, final SQLStatement sqlStatement) {
        String tableName = sqlStatement.getTables().getSingleTableName();
        for (ColumnSegment each : sqlSegment.getColumns()) {
            if (shardingRule.isShardingColumn(each.getName(), tableName)) {
                throw new SQLParsingException("INSERT INTO .... ON DUPLICATE KEY UPDATE can not support on sharding column, token is 'identifier', literals is '%s'.", each);
            }
        }
    }
}
