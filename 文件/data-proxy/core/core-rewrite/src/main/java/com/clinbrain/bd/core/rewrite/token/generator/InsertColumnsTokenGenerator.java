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

package com.clinbrain.bd.core.rewrite.token.generator;

import com.clinbrain.bd.core.rewrite.token.pojo.InsertColumnsToken;
import com.google.common.base.Optional;
import com.clinbrain.bd.core.parse.sql.segment.dml.column.InsertColumnsSegment;
import com.clinbrain.bd.core.parse.sql.statement.SQLStatement;
import com.clinbrain.bd.core.parse.sql.statement.dml.InsertStatement;
import com.clinbrain.bd.core.rule.BaseRule;

import java.util.LinkedList;
import java.util.List;

/**
 * Insert columns token generator.
 *
 * @author panjuan
 */
public final class InsertColumnsTokenGenerator implements OptionalSQLTokenGenerator<BaseRule> {
    
    @Override
    public Optional<InsertColumnsToken> generateSQLToken(final SQLStatement sqlStatement, final List<Object> parameters, final BaseRule baseRule) {
        Optional<InsertColumnsSegment> insertColumnsSegment = sqlStatement.findSQLSegment(InsertColumnsSegment.class);
        if (!(sqlStatement instanceof InsertStatement && insertColumnsSegment.isPresent())) {
            return Optional.absent();
        }
        return createInsertColumnsToken((InsertStatement) sqlStatement, insertColumnsSegment.get());
    }
    
    private Optional<InsertColumnsToken> createInsertColumnsToken(final InsertStatement insertStatement, final InsertColumnsSegment segment) {
        if (!segment.getColumns().isEmpty()) {
            return Optional.absent();
        }
        InsertColumnsToken result = new InsertColumnsToken(segment.getStopIndex(), new LinkedList<>(insertStatement.getColumnNames()), !insertStatement.isNeededToAppendColumns());
        return Optional.of(result);
    }
    
}
