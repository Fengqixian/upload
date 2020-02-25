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

package com.clinbrain.bd.core.route.router.sharding;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import com.clinbrain.bd.api.hint.HintManager;
import com.clinbrain.bd.core.metadata.ShardingMetaData;
import com.clinbrain.bd.core.optimize.GeneratedKey;
import com.clinbrain.bd.core.optimize.OptimizeEngineFactory;
import com.clinbrain.bd.core.optimize.condition.ShardingCondition;
import com.clinbrain.bd.core.optimize.condition.ShardingConditions;
import com.clinbrain.bd.core.optimize.result.OptimizeResult;
import com.clinbrain.bd.core.parse.cache.ParsingResultCache;
import com.clinbrain.bd.core.parse.entry.ShardingSQLParseEntry;
import com.clinbrain.bd.core.parse.hook.ParsingHook;
import com.clinbrain.bd.core.parse.hook.SPIParsingHook;
import com.clinbrain.bd.core.parse.sql.statement.SQLStatement;
import com.clinbrain.bd.core.parse.sql.statement.dml.InsertStatement;
import com.clinbrain.bd.core.parse.sql.statement.dml.SelectStatement;
import com.clinbrain.bd.core.route.SQLRouteResult;
import com.clinbrain.bd.core.route.type.RoutingResult;
import com.clinbrain.bd.core.rule.BindingTableRule;
import com.clinbrain.bd.core.rule.ShardingRule;
import com.clinbrain.bd.core.rule.TableRule;
import com.clinbrain.bd.core.strategy.route.value.ListRouteValue;
import com.clinbrain.bd.core.strategy.route.value.RouteValue;
import com.clinbrain.bd.spi.DbType;

import java.util.LinkedList;
import java.util.List;

/**
 * Sharding router with parse.
 *
 * @author zhangliang
 * @author maxiaoguang
 * @author panjuan
 * @author zhangyonglun
 */
@RequiredArgsConstructor
public final class ParsingSQLRouter implements ShardingRouter {
    
    private final ShardingRule shardingRule;
    
    private final ShardingMetaData shardingMetaData;
    
    private final DbType databaseType;
    
    private final ParsingResultCache parsingResultCache;
    
    private final List<Comparable<?>> generatedKeys = new LinkedList<>();
    
    private final ParsingHook parsingHook = new SPIParsingHook();
    
    @Override
    public SQLStatement parse(final String logicSQL, final boolean useCache) {
        parsingHook.start(logicSQL);
        try {
            SQLStatement result = new ShardingSQLParseEntry(databaseType, shardingRule, shardingMetaData.getTable(), parsingResultCache).parse(logicSQL, useCache);
            parsingHook.finishSuccess(result, shardingMetaData.getTable());
            return result;
            // CHECKSTYLE:OFF
        } catch (final Exception ex) {
            // CHECKSTYLE:ON
            parsingHook.finishFailure(ex);
            throw ex;
        }
    }
    
    @Override
    public SQLRouteResult route(final SQLStatement sqlStatement, final List<Object> parameters) {
        Optional<GeneratedKey> generatedKey = sqlStatement instanceof InsertStatement
                ? GeneratedKey.getGenerateKey(shardingRule, parameters, (InsertStatement) sqlStatement) : Optional.<GeneratedKey>absent();
        SQLRouteResult result = new SQLRouteResult(sqlStatement, generatedKey.orNull());
        OptimizeResult optimizeResult = OptimizeEngineFactory.newInstance(shardingRule, sqlStatement, parameters, generatedKey.orNull()).optimize();
        if (generatedKey.isPresent()) {
            setGeneratedKeys(result, generatedKey.get());
        }
        boolean needMerge = false;
        if (sqlStatement instanceof SelectStatement) {
            needMerge = isNeedMergeShardingValues((SelectStatement) sqlStatement);
        }
        if (needMerge) {
            checkSubqueryShardingValues(sqlStatement, optimizeResult.getShardingConditions());
            mergeShardingValues(optimizeResult.getShardingConditions());
        }
        RoutingResult routingResult = RoutingEngineFactory.newInstance(shardingRule, shardingMetaData.getDataSource(), sqlStatement, optimizeResult).route();
        if (needMerge) {
            Preconditions.checkState(1 == routingResult.getRoutingUnits().size(), "Must have one sharding with subquery.");
        }
        result.setRoutingResult(routingResult);
        result.setOptimizeResult(optimizeResult);
        return result;
    }
    
    private void setGeneratedKeys(final SQLRouteResult sqlRouteResult, final GeneratedKey generatedKey) {
        generatedKeys.addAll(generatedKey.getGeneratedKeys());
        sqlRouteResult.getGeneratedKey().getGeneratedKeys().clear();
        sqlRouteResult.getGeneratedKey().getGeneratedKeys().addAll(generatedKeys);
    }
    
    private boolean isNeedMergeShardingValues(final SelectStatement selectStatement) {
        return !selectStatement.getSubqueryShardingConditions().isEmpty() && !shardingRule.getShardingLogicTableNames(selectStatement.getTables().getTableNames()).isEmpty();
    }
    
    private void checkSubqueryShardingValues(final SQLStatement sqlStatement, final ShardingConditions shardingConditions) {
        for (String each : sqlStatement.getTables().getTableNames()) {
            Optional<TableRule> tableRule = shardingRule.findTableRule(each);
            if (tableRule.isPresent() && shardingRule.isRoutingByHint(tableRule.get()) && !HintManager.getDatabaseShardingValues(each).isEmpty()
                    && !HintManager.getTableShardingValues(each).isEmpty()) {
                return;
            }
        }
        Preconditions.checkState(null != shardingConditions.getShardingConditions() && !shardingConditions.getShardingConditions().isEmpty(), "Must have sharding column with subquery.");
        if (shardingConditions.getShardingConditions().size() > 1) {
            Preconditions.checkState(isSameShardingCondition(shardingConditions), "Sharding value must same with subquery.");
        }
    }
    
    private boolean isSameShardingCondition(final ShardingConditions shardingConditions) {
        ShardingCondition example = shardingConditions.getShardingConditions().remove(shardingConditions.getShardingConditions().size() - 1);
        for (ShardingCondition each : shardingConditions.getShardingConditions()) {
            if (!isSameShardingCondition(example, each)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isSameShardingCondition(final ShardingCondition shardingCondition1, final ShardingCondition shardingCondition2) {
        if (shardingCondition1.getShardingValues().size() != shardingCondition2.getShardingValues().size()) {
            return false;
        }
        for (int i = 0; i < shardingCondition1.getShardingValues().size(); i++) {
            RouteValue shardingValue1 = shardingCondition1.getShardingValues().get(i);
            RouteValue shardingValue2 = shardingCondition2.getShardingValues().get(i);
            if (!isSameShardingValue((ListRouteValue) shardingValue1, (ListRouteValue) shardingValue2)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isSameShardingValue(final ListRouteValue shardingValue1, final ListRouteValue shardingValue2) {
        return isSameLogicTable(shardingValue1, shardingValue2)
                && shardingValue1.getColumnName().equals(shardingValue2.getColumnName()) && shardingValue1.getValues().equals(shardingValue2.getValues());
    }
    
    private boolean isSameLogicTable(final ListRouteValue shardingValue1, final ListRouteValue shardingValue2) {
        return shardingValue1.getTableName().equals(shardingValue2.getTableName()) || isBindingTable(shardingValue1, shardingValue2);
    }
    
    private boolean isBindingTable(final ListRouteValue shardingValue1, final ListRouteValue shardingValue2) {
        Optional<BindingTableRule> bindingRule = shardingRule.findBindingTableRule(shardingValue1.getTableName());
        return bindingRule.isPresent() && bindingRule.get().hasLogicTable(shardingValue2.getTableName());
    }
    
    private void mergeShardingValues(final ShardingConditions shardingConditions) {
        if (shardingConditions.getShardingConditions().size() > 1) {
            ShardingCondition shardingCondition = shardingConditions.getShardingConditions().remove(shardingConditions.getShardingConditions().size() - 1);
            shardingConditions.getShardingConditions().clear();
            shardingConditions.getShardingConditions().add(shardingCondition);
        }
    }
}
