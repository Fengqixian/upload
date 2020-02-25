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

package com.clinbrain.bd.core.parse.filler.encrypt.dml;

import com.google.common.base.Optional;
import lombok.Setter;
import com.clinbrain.bd.core.metadata.table.ShardingTableMetaData;
import com.clinbrain.bd.core.parse.exception.SQLParsingException;
import com.clinbrain.bd.core.parse.filler.api.EncryptRuleAwareFiller;
import com.clinbrain.bd.core.parse.filler.api.SQLSegmentFiller;
import com.clinbrain.bd.core.parse.filler.api.ShardingTableMetaDataAwareFiller;
import com.clinbrain.bd.core.parse.filler.common.dml.PredicateUtils;
import com.clinbrain.bd.core.parse.sql.context.condition.AndCondition;
import com.clinbrain.bd.core.parse.sql.context.condition.Column;
import com.clinbrain.bd.core.parse.sql.context.condition.Condition;
import com.clinbrain.bd.core.parse.sql.segment.dml.predicate.AndPredicate;
import com.clinbrain.bd.core.parse.sql.segment.dml.predicate.OrPredicateSegment;
import com.clinbrain.bd.core.parse.sql.segment.dml.predicate.PredicateSegment;
import com.clinbrain.bd.core.parse.sql.segment.dml.predicate.value.PredicateBetweenRightValue;
import com.clinbrain.bd.core.parse.sql.segment.dml.predicate.value.PredicateCompareRightValue;
import com.clinbrain.bd.core.parse.sql.segment.dml.predicate.value.PredicateInRightValue;
import com.clinbrain.bd.core.parse.sql.statement.SQLStatement;
import com.clinbrain.bd.core.rule.EncryptRule;

import java.util.Collection;
import java.util.HashSet;

/**
 * Or predicate filler for encrypt.
 *
 * @author duhongjun
 * @author panjuan
 */
@Setter
public final class EncryptOrPredicateFiller implements SQLSegmentFiller<OrPredicateSegment>, EncryptRuleAwareFiller, ShardingTableMetaDataAwareFiller {
    
    private EncryptRule encryptRule;
    
    private ShardingTableMetaData shardingTableMetaData;
    
    @Override
    public void fill(final OrPredicateSegment sqlSegment, final SQLStatement sqlStatement) {
        Collection<Integer> stopIndexes = new HashSet<>();
        for (AndPredicate each : sqlSegment.getAndPredicates()) {
            for (PredicateSegment predicate : each.getPredicates()) {
                if (stopIndexes.add(predicate.getStopIndex())) {
                    fill(predicate, sqlStatement);
                }
            }
        }
    }
    
    private void fill(final PredicateSegment predicateSegment, final SQLStatement sqlStatement) {
        Optional<String> tableName = PredicateUtils.findTableName(predicateSegment, sqlStatement, shardingTableMetaData);
        if (!tableName.isPresent() || !isNeedEncrypt(predicateSegment, tableName.get())) {
            return;
        }
        Column column = new Column(predicateSegment.getColumn().getName(), tableName.get());
        Optional<Condition> condition = createCondition(predicateSegment, column);
        if (condition.isPresent()) {
            AndCondition andCondition;
            if (sqlStatement.getEncryptConditions().getOrConditions().isEmpty()) {
                andCondition = new AndCondition();
                sqlStatement.getEncryptConditions().getOrConditions().add(andCondition);
            } else {
                andCondition = sqlStatement.getEncryptConditions().getOrConditions().get(0);
            }
            andCondition.getConditions().add(condition.get());
        }
    }
    
    private Optional<Condition> createCondition(final PredicateSegment predicateSegment, final Column column) {
        if (predicateSegment.getRightValue() instanceof PredicateBetweenRightValue) {
            throw new SQLParsingException("The SQL clause 'BETWEEN...AND...' is unsupported in encrypt rule.");
        }
        if (predicateSegment.getRightValue() instanceof PredicateCompareRightValue) {
            PredicateCompareRightValue compareRightValue = (PredicateCompareRightValue) predicateSegment.getRightValue();
            return isOperatorSupportedWithEncrypt(compareRightValue.getOperator()) 
                    ? PredicateUtils.createCompareCondition(compareRightValue, column, predicateSegment) : Optional.<Condition>absent();
        }
        if (predicateSegment.getRightValue() instanceof PredicateInRightValue) {
            return PredicateUtils.createInCondition((PredicateInRightValue) predicateSegment.getRightValue(), column, predicateSegment);
        }
        return Optional.absent();
    }
    
    private boolean isNeedEncrypt(final PredicateSegment predicate, final String tableName) {
        return encryptRule.getEncryptorEngine().getShardingEncryptor(tableName, predicate.getColumn().getName()).isPresent();
    }
    
    private boolean isOperatorSupportedWithEncrypt(final String operator) {
        return "=".equals(operator) || "<>".equals(operator) || "!=".equals(operator);
    }
}
