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

package com.clinbrain.bd.core.merge.dal;

import lombok.RequiredArgsConstructor;
import com.clinbrain.bd.core.execute.sql.execute.result.QueryResult;
import com.clinbrain.bd.core.merge.MergeEngine;
import com.clinbrain.bd.core.merge.MergedResult;
import com.clinbrain.bd.core.merge.dal.show.ShowCreateTableMergedResult;
import com.clinbrain.bd.core.merge.dal.show.ShowDatabasesMergedResult;
import com.clinbrain.bd.core.merge.dal.show.ShowIndexMergedResult;
import com.clinbrain.bd.core.merge.dal.show.ShowOtherMergedResult;
import com.clinbrain.bd.core.merge.dal.show.ShowTableStatusMergedResult;
import com.clinbrain.bd.core.merge.dal.show.ShowTablesMergedResult;
import com.clinbrain.bd.core.metadata.table.ShardingTableMetaData;
import com.clinbrain.bd.core.parse.sql.statement.dal.DALStatement;
import com.clinbrain.bd.core.parse.sql.statement.dal.dialect.mysql.statement.ShowCreateTableStatement;
import com.clinbrain.bd.core.parse.sql.statement.dal.dialect.mysql.statement.ShowDatabasesStatement;
import com.clinbrain.bd.core.parse.sql.statement.dal.dialect.mysql.statement.ShowIndexStatement;
import com.clinbrain.bd.core.parse.sql.statement.dal.dialect.mysql.statement.ShowTableStatusStatement;
import com.clinbrain.bd.core.parse.sql.statement.dal.dialect.mysql.statement.ShowTablesStatement;
import com.clinbrain.bd.core.rule.ShardingRule;

import java.sql.SQLException;
import java.util.List;

/**
 * DAL result set merge engine.
 *
 * @author zhangliang
 * @author panjuan
 */
@RequiredArgsConstructor
public final class DALMergeEngine implements MergeEngine {
    
    private final ShardingRule shardingRule;
    
    private final List<QueryResult> queryResults;
    
    private final DALStatement dalStatement;
    
    private final ShardingTableMetaData shardingTableMetaData;
    
    @Override
    public MergedResult merge() throws SQLException {
        if (dalStatement instanceof ShowDatabasesStatement) {
            return new ShowDatabasesMergedResult();
        }
        if (dalStatement instanceof ShowTableStatusStatement) {
            return new ShowTableStatusMergedResult(shardingRule, queryResults, shardingTableMetaData);
        }
        if (dalStatement instanceof ShowTablesStatement) {
            return new ShowTablesMergedResult(shardingRule, queryResults, shardingTableMetaData);
        }
        if (dalStatement instanceof ShowCreateTableStatement) {
            return new ShowCreateTableMergedResult(shardingRule, queryResults, shardingTableMetaData);
        }
        if (dalStatement instanceof ShowIndexStatement) {
            return new ShowIndexMergedResult(shardingRule, queryResults, shardingTableMetaData);
        }
        return new ShowOtherMergedResult(queryResults.get(0));
    }
}
