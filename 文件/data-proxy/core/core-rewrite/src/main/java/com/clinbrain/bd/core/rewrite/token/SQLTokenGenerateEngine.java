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

package com.clinbrain.bd.core.rewrite.token;

import com.clinbrain.bd.core.rewrite.token.generator.CollectionSQLTokenGenerator;
import com.clinbrain.bd.core.rewrite.token.generator.IgnoreForSingleRoute;
import com.clinbrain.bd.core.rewrite.token.generator.OptionalSQLTokenGenerator;
import com.clinbrain.bd.core.rewrite.token.generator.SQLTokenGenerator;
import com.clinbrain.bd.core.rewrite.token.pojo.SQLToken;
import com.google.common.base.Optional;
import com.clinbrain.bd.core.parse.sql.statement.SQLStatement;
import com.clinbrain.bd.core.rule.BaseRule;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * SQL token generator.
 *
 * @author zhangliang
 * 
 * @param <T> type of rule 
 */
public abstract class SQLTokenGenerateEngine<T extends BaseRule> {
    
    /**
     * Generate SQL tokens.
     *
     * @param sqlStatement SQL statement
     * @param parameters SQL parameters
     * @param rule rule
     * @param isSingleRoute is single route
     * @return SQL tokens
     */
    @SuppressWarnings("unchecked")
    public final List<SQLToken> generateSQLTokens(final SQLStatement sqlStatement, final List<Object> parameters, final T rule, final boolean isSingleRoute) {
        List<SQLToken> result = new LinkedList<>();
        for (SQLTokenGenerator each : getSQLTokenGenerators()) {
            if (isSingleRoute && each instanceof IgnoreForSingleRoute) {
                continue;
            }
            if (each instanceof OptionalSQLTokenGenerator) {
                Optional<? extends SQLToken> sqlToken = ((OptionalSQLTokenGenerator) each).generateSQLToken(sqlStatement, parameters, rule);
                if (sqlToken.isPresent()) {
                    result.add(sqlToken.get());
                }
            } else {
                result.addAll(((CollectionSQLTokenGenerator) each).generateSQLTokens(sqlStatement, parameters, rule));
            }
        }
        return result;
    }
    
    protected abstract Collection<SQLTokenGenerator> getSQLTokenGenerators();
}
