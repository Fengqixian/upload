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

import com.clinbrain.bd.core.rewrite.token.pojo.AggregationDistinctToken;
import com.google.common.base.Optional;
import com.clinbrain.bd.core.parse.constant.DerivedColumn;
import com.clinbrain.bd.core.parse.sql.segment.SQLSegment;
import com.clinbrain.bd.core.parse.sql.segment.dml.item.AggregationDistinctSelectItemSegment;
import com.clinbrain.bd.core.parse.sql.statement.SQLStatement;
import com.clinbrain.bd.core.rule.ShardingRule;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Aggregation distinct token generator.
 *
 * @author panjuan
 */
public final class AggregationDistinctTokenGenerator implements CollectionSQLTokenGenerator<ShardingRule> {
    
    @Override
    public Collection<AggregationDistinctToken> generateSQLTokens(final SQLStatement sqlStatement, final List<Object> parameters, final ShardingRule shardingRule) {
        Collection<AggregationDistinctToken> result = new LinkedList<>();
        for (SQLSegment each : sqlStatement.getSqlSegments()) {
            if (each instanceof AggregationDistinctSelectItemSegment) {
                result.add(createAggregationDistinctToken((AggregationDistinctSelectItemSegment) each));
            }
        }
        return result;
    }
    
    private AggregationDistinctToken createAggregationDistinctToken(final AggregationDistinctSelectItemSegment segment) {
        Optional<String> derivedAlias = Optional.absent();
        if (DerivedColumn.isDerivedColumn(segment.getAlias().get())) {
            derivedAlias = Optional.of(segment.getAlias().get());
        }
        return new AggregationDistinctToken(segment.getStartIndex(), segment.getStopIndex(), segment.getDistinctExpression(), derivedAlias);
    }
}
