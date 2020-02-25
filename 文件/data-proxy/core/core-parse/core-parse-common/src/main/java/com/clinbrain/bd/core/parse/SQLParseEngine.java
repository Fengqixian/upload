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

package com.clinbrain.bd.core.parse;

import com.clinbrain.bd.core.metadata.table.ShardingTableMetaData;
import com.clinbrain.bd.core.parse.extractor.SQLSegmentsExtractorEngine;
import com.clinbrain.bd.core.parse.filler.SQLStatementFillerEngine;
import com.clinbrain.bd.core.parse.optimizer.SQLStatementOptimizerEngine;
import com.clinbrain.bd.core.parse.parser.SQLAST;
import com.clinbrain.bd.core.parse.parser.SQLParserEngine;
import com.clinbrain.bd.core.parse.rule.registry.ParseRuleRegistry;
import com.clinbrain.bd.core.parse.sql.segment.SQLSegment;
import com.clinbrain.bd.core.parse.sql.statement.SQLStatement;
import com.clinbrain.bd.core.rule.BaseRule;
import com.clinbrain.bd.spi.DatabaseTypes;
import com.clinbrain.bd.spi.DbType;

import java.util.Collection;

/**
 * SQL parse engine.
 *
 * @author duhongjun
 * @author zhangliang
 */
public final class SQLParseEngine {
    
    private final SQLParserEngine parserEngine;
    
    private final SQLSegmentsExtractorEngine extractorEngine;
    
    private final SQLStatementFillerEngine fillerEngine;
    
    private final SQLStatementOptimizerEngine optimizerEngine;
    
    public SQLParseEngine(final ParseRuleRegistry parseRuleRegistry, final DbType databaseType, final String sql, final BaseRule rule, final ShardingTableMetaData shardingTableMetaData) {
        DbType trunkDatabaseType = DatabaseTypes.getTrunkDatabaseType(databaseType.getName());
        parserEngine = new SQLParserEngine(parseRuleRegistry, trunkDatabaseType, sql);
        extractorEngine = new SQLSegmentsExtractorEngine();
        fillerEngine = new SQLStatementFillerEngine(parseRuleRegistry, trunkDatabaseType, sql, rule, shardingTableMetaData);
        optimizerEngine = new SQLStatementOptimizerEngine(shardingTableMetaData);
    }
    
    /**
     * Parse SQL.
     *
     * @return SQL statement
     */
    public SQLStatement parse() {
        SQLAST ast = parserEngine.parse();
        Collection<SQLSegment> sqlSegments = extractorEngine.extract(ast);
        SQLStatement result = fillerEngine.fill(sqlSegments, ast.getSqlStatementRule());
        optimizerEngine.optimize(ast.getSqlStatementRule(), result);
        return result;
    }
}
