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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.clinbrain.bd.api.hint.HintManager;
import com.clinbrain.bd.core.constant.properties.ShardingProperties;
import com.clinbrain.bd.core.constant.properties.ShardingPropertiesConstant;
import com.clinbrain.bd.core.optimize.condition.ShardingCondition;
import com.clinbrain.bd.core.optimize.condition.ShardingConditions;
import com.clinbrain.bd.core.optimize.result.OptimizeResult;
import com.clinbrain.bd.core.parse.sql.statement.dml.SelectStatement;
import com.clinbrain.bd.core.route.RouteUnit;
import com.clinbrain.bd.core.route.SQLRouteResult;
import com.clinbrain.bd.core.route.type.RoutingResult;
import com.clinbrain.bd.core.route.type.RoutingUnit;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RequiredArgsConstructor
@Getter
public abstract class BaseShardingEngineTest {
    
    private final String sql;
    
    private final List<Object> parameters;
    
    protected final ShardingProperties getShardingProperties() {
        Properties result = new Properties();
        result.setProperty(ShardingPropertiesConstant.SQL_SHOW.getKey(), Boolean.TRUE.toString());
        return new ShardingProperties(result);
    }
    
    protected final SQLRouteResult createSQLRouteResult() {
        SQLRouteResult result = new SQLRouteResult(new SelectStatement());
        RoutingResult routingResult = new RoutingResult();
        routingResult.getRoutingUnits().add(new RoutingUnit("ds"));
        result.setRoutingResult(routingResult);
        result.setOptimizeResult(new OptimizeResult(new ShardingConditions(Collections.<ShardingCondition>emptyList())));
        return result;
    }
    
    protected final void assertSQLRouteResult(final SQLRouteResult actual) {
        assertThat(actual.getRouteUnits().size(), is(1));
        RouteUnit actualRouteUnit = actual.getRouteUnits().iterator().next();
        assertThat(actualRouteUnit.getDataSourceName(), is("ds"));
        assertThat(actualRouteUnit.getSqlUnit().getSql(), is(sql));
        assertThat(actualRouteUnit.getSqlUnit().getParameters(), is(parameters));
    }
    
    @Test
    public void assertShardWithHintDatabaseShardingOnly() {
        HintManager.getInstance().setDatabaseShardingValue("1");
        assertShard();
        HintManager.clear();
    }
    
    @Test
    public void assertShardWithoutHint() {
        assertShard();
    }
    
    protected abstract void assertShard();
}
