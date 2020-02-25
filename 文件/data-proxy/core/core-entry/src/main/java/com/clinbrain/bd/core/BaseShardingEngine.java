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

package com.clinbrain.bd.core;

import lombok.RequiredArgsConstructor;
import com.clinbrain.bd.api.hint.HintManager;
import com.clinbrain.bd.core.constant.properties.ShardingProperties;
import com.clinbrain.bd.core.constant.properties.ShardingPropertiesConstant;
import com.clinbrain.bd.core.metadata.ShardingMetaData;
import com.clinbrain.bd.core.rewrite.SQLRewriteEngine;
import com.clinbrain.bd.core.rewrite.rewriter.parameter.ParameterRewriter;
import com.clinbrain.bd.core.rewrite.rewriter.parameter.ShardingParameterRewriter;
import com.clinbrain.bd.core.rewrite.rewriter.sql.EncryptSQLRewriter;
import com.clinbrain.bd.core.rewrite.rewriter.sql.ShardingSQLRewriter;
import com.clinbrain.bd.core.route.RouteUnit;
import com.clinbrain.bd.core.route.SQLLogger;
import com.clinbrain.bd.core.route.SQLRouteResult;
import com.clinbrain.bd.core.route.SQLUnit;
import com.clinbrain.bd.core.route.hook.SPIRoutingHook;
import com.clinbrain.bd.core.route.type.RoutingUnit;
import com.clinbrain.bd.core.rule.ShardingRule;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Base sharding engine.
 *
 * @author zhangliang
 * @author panjuan
 */
@RequiredArgsConstructor
public abstract class BaseShardingEngine {
    
    private final ShardingRule shardingRule;
    
    private final ShardingProperties shardingProperties;
    
    private final ShardingMetaData metaData;
    
    private final SPIRoutingHook routingHook = new SPIRoutingHook();
    
    /**
     * Shard.
     *
     * @param sql SQL
     * @param parameters parameters of SQL
     * @return SQL route result
     */
    public SQLRouteResult shard(final String sql, final List<Object> parameters) {
        List<Object> clonedParameters = cloneParameters(parameters);
        SQLRouteResult result = executeRoute(sql, clonedParameters);
        result.getRouteUnits().addAll(HintManager.isDatabaseShardingOnly() ? convert(sql, clonedParameters, result) : rewriteAndConvert(clonedParameters, result));
        if (shardingProperties.getValue(ShardingPropertiesConstant.SQL_SHOW)) {
            boolean showSimple = shardingProperties.getValue(ShardingPropertiesConstant.SQL_SIMPLE);
            SQLLogger.logSQL(sql, showSimple, result.getSqlStatement(), result.getRouteUnits());
        }
        return result;
    }
    
    protected abstract List<Object> cloneParameters(List<Object> parameters);
    
    protected abstract SQLRouteResult route(String sql, List<Object> parameters);
    
    private SQLRouteResult executeRoute(final String sql, final List<Object> clonedParameters) {
        routingHook.start(sql);
        try {
            SQLRouteResult result = route(sql, clonedParameters);
            routingHook.finishSuccess(result, metaData.getTable());
            return result;
            // CHECKSTYLE:OFF
        } catch (final Exception ex) {
            // CHECKSTYLE:ON
            routingHook.finishFailure(ex);
            throw ex;
        }
    }
    
    private Collection<RouteUnit> convert(final String sql, final List<Object> parameters, final SQLRouteResult sqlRouteResult) {
        Collection<RouteUnit> result = new LinkedHashSet<>();
        for (RoutingUnit each : sqlRouteResult.getRoutingResult().getRoutingUnits()) {
            result.add(new RouteUnit(each.getDataSourceName(), new SQLUnit(sql, parameters)));
        }
        return result;
    }
    
    private Collection<RouteUnit> rewriteAndConvert(final List<Object> parameters, final SQLRouteResult sqlRouteResult) {
        SQLRewriteEngine rewriteEngine = new SQLRewriteEngine(shardingRule, sqlRouteResult.getSqlStatement(), parameters, sqlRouteResult.getRoutingResult().isSingleRouting());
        ShardingParameterRewriter shardingParameterRewriter = new ShardingParameterRewriter(sqlRouteResult);
        ShardingSQLRewriter shardingSQLRewriter = new ShardingSQLRewriter(shardingRule, sqlRouteResult, sqlRouteResult.getOptimizeResult());
        EncryptSQLRewriter encryptSQLRewriter = new EncryptSQLRewriter(shardingRule.getEncryptRule().getEncryptorEngine(), sqlRouteResult.getSqlStatement(), sqlRouteResult.getOptimizeResult());
        rewriteEngine.init(Collections.<ParameterRewriter>singletonList(shardingParameterRewriter), Arrays.asList(shardingSQLRewriter, encryptSQLRewriter));
        Collection<RouteUnit> result = new LinkedHashSet<>();
        for (RoutingUnit each : sqlRouteResult.getRoutingResult().getRoutingUnits()) {
            result.add(new RouteUnit(each.getDataSourceName(), rewriteEngine.generateSQL(each)));
        }
        return result;
    }
}
