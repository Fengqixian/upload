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

package com.clinbrain.bd.core.rewrite.rewriter.sql;

import com.clinbrain.bd.core.rewrite.builder.ParameterBuilder;
import com.clinbrain.bd.core.rewrite.builder.SQLBuilder;
import com.clinbrain.bd.core.rewrite.placeholder.*;
import com.clinbrain.bd.core.rewrite.token.pojo.*;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.clinbrain.bd.core.optimize.result.OptimizeResult;
import com.clinbrain.bd.core.optimize.result.insert.InsertOptimizeResult;
import com.clinbrain.bd.core.optimize.result.insert.InsertOptimizeResultUnit;
import com.clinbrain.bd.core.parse.sql.context.condition.Condition;
import com.clinbrain.bd.core.parse.sql.context.condition.Conditions;
import com.clinbrain.bd.core.parse.sql.segment.dml.expr.ExpressionSegment;
import com.clinbrain.bd.core.parse.sql.segment.dml.expr.simple.ParameterMarkerExpressionSegment;
import com.clinbrain.bd.core.parse.sql.segment.dml.predicate.PredicateSegment;
import com.clinbrain.bd.core.parse.sql.statement.SQLStatement;
import com.clinbrain.bd.core.parse.sql.statement.dml.UpdateStatement;
import com.clinbrain.bd.core.rule.ColumnNode;
import com.clinbrain.bd.core.strategy.encrypt.ShardingEncryptorEngine;
import com.clinbrain.bd.spi.encrypt.ShardingEncryptor;
import com.clinbrain.bd.spi.encrypt.ShardingQueryAssistedEncryptor;

import java.util.*;
import java.util.Map.Entry;

/**
 * SQL rewriter encrypt.
 * 
 * @author panjuan
 */
public final class EncryptSQLRewriter implements SQLRewriter {
    
    private final ShardingEncryptorEngine encryptorEngine;
    
    private final SQLStatement sqlStatement;
    
    private final InsertOptimizeResult insertOptimizeResult;
    
    public EncryptSQLRewriter(final ShardingEncryptorEngine encryptorEngine, final SQLStatement sqlStatement, final OptimizeResult optimizeResult) {
        this.encryptorEngine = encryptorEngine;
        this.sqlStatement = sqlStatement;
        this.insertOptimizeResult = getInsertOptimizeResult(optimizeResult);
    }
    
    private InsertOptimizeResult getInsertOptimizeResult(final OptimizeResult optimizeResult) {
        if (null == optimizeResult) {
            return null;
        }
        Optional<InsertOptimizeResult> insertOptimizeResult = optimizeResult.getInsertOptimizeResult();
        if (!insertOptimizeResult.isPresent()) {
            return null;
        }
        for (InsertOptimizeResultUnit each : insertOptimizeResult.get().getUnits()) {
            encryptInsertOptimizeResultUnit(each, insertOptimizeResult.get().getColumnNames());
        }
        return insertOptimizeResult.get();
    }
    
    private void encryptInsertOptimizeResultUnit(final InsertOptimizeResultUnit unit, final Collection<String> columnNames) {
        for (String each : columnNames) {
            Optional<ShardingEncryptor> shardingEncryptor = encryptorEngine.getShardingEncryptor(sqlStatement.getTables().getSingleTableName(), each);
            if (shardingEncryptor.isPresent()) {
                encryptInsertOptimizeResult(unit, each, shardingEncryptor.get());
            }
        }
    }
    
    private void encryptInsertOptimizeResult(final InsertOptimizeResultUnit unit, final String columnName, final ShardingEncryptor shardingEncryptor) {
        if (shardingEncryptor instanceof ShardingQueryAssistedEncryptor) {
            Optional<String> assistedColumnName = encryptorEngine.getAssistedQueryColumn(sqlStatement.getTables().getSingleTableName(), columnName);
            Preconditions.checkArgument(assistedColumnName.isPresent(), "Can not find assisted query Column Name");
            unit.setColumnValue(assistedColumnName.get(), ((ShardingQueryAssistedEncryptor) shardingEncryptor).queryAssistedEncrypt(unit.getColumnValue(columnName).toString()));
        }
        unit.setColumnValue(columnName, shardingEncryptor.encrypt(unit.getColumnValue(columnName)));
    }
    
    @Override
    public void rewrite(final SQLBuilder sqlBuilder, final ParameterBuilder parameterBuilder, final SQLToken sqlToken) {
        parameterBuilder.setInsertParameterUnits(insertOptimizeResult);
        if (sqlToken instanceof InsertValuesToken) {
            appendInsertValuesPlaceholder(sqlBuilder, insertOptimizeResult);
        } else if (sqlToken instanceof InsertSetEncryptValueToken) {
            appendInsertSetEncryptValuePlaceholder(sqlBuilder, (InsertSetEncryptValueToken) sqlToken, insertOptimizeResult);
        } else if (sqlToken instanceof InsertSetAddAssistedColumnsToken) {
            appendInsertSetAddItemsPlaceholder(sqlBuilder, (InsertSetAddAssistedColumnsToken) sqlToken, insertOptimizeResult);
        } else if (sqlToken instanceof InsertAssistedColumnsToken) {
            appendInsertAssistedColumnsPlaceholder(sqlBuilder, (InsertAssistedColumnsToken) sqlToken);
        } else if (sqlToken instanceof EncryptColumnToken) {
            appendEncryptColumnPlaceholder(sqlBuilder, (EncryptColumnToken) sqlToken, parameterBuilder);
        }
    }
    
    private void appendInsertValuesPlaceholder(final SQLBuilder sqlBuilder, final InsertOptimizeResult insertOptimizeResult) {
        List<InsertValuePlaceholder> insertValues = new LinkedList<>();
        for (InsertOptimizeResultUnit each : insertOptimizeResult.getUnits()) {
            insertValues.add(new InsertValuePlaceholder(new ArrayList<>(each.getColumnNames()), Arrays.asList(each.getValues()), each.getDataNodes()));
        }
        sqlBuilder.appendPlaceholder(new InsertValuesPlaceholder(insertValues));
    }
    
    private void appendInsertSetEncryptValuePlaceholder(final SQLBuilder sqlBuilder, final InsertSetEncryptValueToken insertSetEncryptValueToken, final InsertOptimizeResult insertOptimizeResult) {
        sqlBuilder.appendPlaceholder(new InsertSetEncryptValuePlaceholder(insertOptimizeResult.getUnits().get(0).getColumnSQLExpression(insertSetEncryptValueToken.getColumnName())));
    }
    
    private void appendInsertSetAddItemsPlaceholder(
            final SQLBuilder sqlBuilder, final InsertSetAddAssistedColumnsToken insertSetAddAssistedColumnsToken, final InsertOptimizeResult insertOptimizeResult) {
        List<ExpressionSegment> columnValues = new LinkedList<>();
        for (String each : insertSetAddAssistedColumnsToken.getColumnNames()) {
            columnValues.add(insertOptimizeResult.getUnits().get(0).getColumnSQLExpression(each));
        }
        sqlBuilder.appendPlaceholder(new InsertSetAddItemsPlaceholder(new LinkedList<>(insertSetAddAssistedColumnsToken.getColumnNames()), columnValues));
    }
    
    private void appendInsertAssistedColumnsPlaceholder(final SQLBuilder sqlBuilder, final InsertAssistedColumnsToken insertAssistedColumnsToken) {
        sqlBuilder.appendPlaceholder(new InsertAssistedColumnsPlaceholder(insertAssistedColumnsToken.getColumns(), insertAssistedColumnsToken.isToAddCloseParenthesis()));
    }
    
    private void appendEncryptColumnPlaceholder(final SQLBuilder sqlBuilder, final EncryptColumnToken encryptColumnToken, final ParameterBuilder parameterBuilder) {
        Optional<Condition> encryptCondition = getEncryptCondition(sqlStatement.getEncryptConditions(), encryptColumnToken);
        Preconditions.checkArgument(!encryptColumnToken.isInWhere() || encryptCondition.isPresent(), "Can not find encrypt condition");
        ShardingPlaceholder result = encryptColumnToken.isInWhere() ? getEncryptColumnPlaceholderFromConditions(encryptColumnToken, encryptCondition.get(), parameterBuilder)
                : getEncryptColumnPlaceholderFromUpdateItem(encryptColumnToken, parameterBuilder);
        sqlBuilder.appendPlaceholder(result);
    }
    
    private Optional<Condition> getEncryptCondition(final Conditions encryptConditions, final EncryptColumnToken encryptColumnToken) {
        for (Condition each : encryptConditions.findConditions(encryptColumnToken.getColumn())) {
            if (isSameIndexes(each.getPredicateSegment(), encryptColumnToken)) {
                return Optional.of(each);
            }
        }
        return Optional.absent();
    }
    
    private boolean isSameIndexes(final PredicateSegment predicateSegment, final EncryptColumnToken encryptColumnToken) {
        return predicateSegment.getStartIndex() == encryptColumnToken.getStartIndex() && predicateSegment.getStopIndex() == encryptColumnToken.getStopIndex();
    }
    
    private WhereEncryptColumnPlaceholder getEncryptColumnPlaceholderFromConditions(
            final EncryptColumnToken encryptColumnToken, final Condition encryptCondition, final ParameterBuilder parameterBuilder) {
        ColumnNode columnNode = new ColumnNode(encryptColumnToken.getColumn().getTableName(), encryptColumnToken.getColumn().getName());
        List<Comparable<?>> encryptColumnValues = encryptValues(columnNode, encryptCondition.getConditionValues(parameterBuilder.getOriginalParameters()));
        encryptParameters(encryptCondition.getPositionIndexMap(), encryptColumnValues, parameterBuilder);
        Optional<String> assistedColumnName = encryptorEngine.getAssistedQueryColumn(columnNode.getTableName(), columnNode.getColumnName());
        return new WhereEncryptColumnPlaceholder(assistedColumnName.isPresent() ? assistedColumnName.get() : columnNode.getColumnName(),
                getPositionValues(encryptCondition.getPositionValueMap().keySet(), encryptColumnValues), encryptCondition.getPositionIndexMap().keySet(), encryptCondition.getOperator());
    }
    
    private List<Comparable<?>> encryptValues(final ColumnNode columnNode, final List<Comparable<?>> columnValues) {
        return encryptorEngine.getAssistedQueryColumn(columnNode.getTableName(), columnNode.getColumnName()).isPresent()
                ? encryptorEngine.getEncryptAssistedColumnValues(columnNode, columnValues) : encryptorEngine.getEncryptColumnValues(columnNode, columnValues);
    }
    
    private void encryptParameters(final Map<Integer, Integer> positionIndexes, final List<Comparable<?>> encryptColumnValues, final ParameterBuilder parameterBuilder) {
        if (!positionIndexes.isEmpty()) {
            for (Entry<Integer, Integer> entry : positionIndexes.entrySet()) {
                parameterBuilder.getOriginalParameters().set(entry.getValue(), encryptColumnValues.get(entry.getKey()));
            }
        }
    }
    
    private Map<Integer, Comparable<?>> getPositionValues(final Collection<Integer> valuePositions, final List<Comparable<?>> encryptColumnValues) {
        Map<Integer, Comparable<?>> result = new LinkedHashMap<>();
        for (int each : valuePositions) {
            result.put(each, encryptColumnValues.get(each));
        }
        return result;
    }
    
    private ShardingPlaceholder getEncryptColumnPlaceholderFromUpdateItem(final EncryptColumnToken encryptColumnToken, final ParameterBuilder parameterBuilder) {
        ColumnNode columnNode = new ColumnNode(encryptColumnToken.getColumn().getTableName(), encryptColumnToken.getColumn().getName());
        Comparable<?> originalColumnValue = ((UpdateStatement) sqlStatement).getColumnValue(encryptColumnToken.getColumn(), parameterBuilder.getOriginalParameters());
        List<Comparable<?>> encryptColumnValues = encryptorEngine.getEncryptColumnValues(columnNode, Collections.<Comparable<?>>singletonList(originalColumnValue));
        encryptParameters(getPositionIndexesFromUpdateItem(encryptColumnToken), encryptColumnValues, parameterBuilder);
        Optional<String> assistedColumnName = encryptorEngine.getAssistedQueryColumn(columnNode.getTableName(), columnNode.getColumnName());
        if (!assistedColumnName.isPresent()) {
            return getUpdateEncryptItemPlaceholder(encryptColumnToken, encryptColumnValues);
        }
        List<Comparable<?>> encryptAssistedColumnValues = encryptorEngine.getEncryptAssistedColumnValues(columnNode, Collections.<Comparable<?>>singletonList(originalColumnValue));
        parameterBuilder.getAddedIndexAndParameters().putAll(getIndexAndParameters(encryptColumnToken, encryptAssistedColumnValues));
        return getUpdateEncryptAssistedItemPlaceholder(encryptColumnToken, encryptColumnValues, encryptAssistedColumnValues);
    }
    
    private Map<Integer, Integer> getPositionIndexesFromUpdateItem(final EncryptColumnToken encryptColumnToken) {
        ExpressionSegment result = ((UpdateStatement) sqlStatement).getAssignments().get(encryptColumnToken.getColumn());
        return result instanceof ParameterMarkerExpressionSegment
                ? Collections.singletonMap(0, ((ParameterMarkerExpressionSegment) result).getParameterMarkerIndex()) : new LinkedHashMap<Integer, Integer>();
    }
    
    private Map<Integer, Object> getIndexAndParameters(final EncryptColumnToken encryptColumnToken, final List<Comparable<?>> encryptAssistedColumnValues) {
        if (encryptAssistedColumnValues.isEmpty()) {
            return Collections.emptyMap();
        }
        if (!isUsingParameter(encryptColumnToken)) {
            return Collections.emptyMap();
        }
        return Collections.singletonMap(getPositionIndexesFromUpdateItem(encryptColumnToken).values().iterator().next() + 1, (Object) encryptAssistedColumnValues.get(0));
    }
    
    private UpdateEncryptItemPlaceholder getUpdateEncryptItemPlaceholder(final EncryptColumnToken encryptColumnToken, final List<Comparable<?>> encryptColumnValues) {
        if (isUsingParameter(encryptColumnToken)) {
            return new UpdateEncryptItemPlaceholder(encryptColumnToken.getColumn().getTableName(), encryptColumnToken.getColumn().getName());
        }
        return new UpdateEncryptItemPlaceholder(encryptColumnToken.getColumn().getName(), encryptColumnValues.get(0));
    }
    
    private UpdateEncryptAssistedItemPlaceholder getUpdateEncryptAssistedItemPlaceholder(final EncryptColumnToken encryptColumnToken,
                                                                                         final List<Comparable<?>> encryptColumnValues, final List<Comparable<?>> encryptAssistedColumnValues) {
        String assistedColumnName = encryptorEngine.getAssistedQueryColumn(encryptColumnToken.getColumn().getTableName(), encryptColumnToken.getColumn().getName()).get();
        if (isUsingParameter(encryptColumnToken)) {
            return new UpdateEncryptAssistedItemPlaceholder(encryptColumnToken.getColumn().getName(), assistedColumnName);
        }
        return new UpdateEncryptAssistedItemPlaceholder(encryptColumnToken.getColumn().getName(), encryptColumnValues.get(0), assistedColumnName, encryptAssistedColumnValues.get(0));
    }
    
    private boolean isUsingParameter(final EncryptColumnToken encryptColumnToken) {
        return ((UpdateStatement) sqlStatement).getAssignments().get(encryptColumnToken.getColumn()) instanceof ParameterMarkerExpressionSegment;
    }
}
