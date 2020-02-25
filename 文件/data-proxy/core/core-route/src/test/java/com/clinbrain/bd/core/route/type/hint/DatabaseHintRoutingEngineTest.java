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

package com.clinbrain.bd.core.route.type.hint;

import com.clinbrain.bd.api.config.sharding.ShardingRuleConfiguration;
import com.clinbrain.bd.api.config.sharding.TableRuleConfiguration;
import com.clinbrain.bd.api.config.sharding.strategy.HintShardingStrategyConfiguration;
import com.clinbrain.bd.api.hint.HintManager;
import com.clinbrain.bd.core.route.fixture.HintShardingAlgorithmFixture;
import com.clinbrain.bd.core.route.type.RoutingResult;
import com.clinbrain.bd.core.route.type.RoutingUnit;
import com.clinbrain.bd.core.rule.ShardingRule;
import com.clinbrain.bd.core.strategy.route.hint.HintShardingStrategy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public final class DatabaseHintRoutingEngineTest {
    
    private final HintManager hintManager = HintManager.getInstance();
    
    private DatabaseHintRoutingEngine databaseHintRoutingEngine;
    
    @Before
    public void setEngineContext() {
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(new TableRuleConfiguration("t_order", "ds_${0..1}.t_order"));
        shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(new HintShardingStrategyConfiguration(new HintShardingAlgorithmFixture()));
        ShardingRule shardingRule = new ShardingRule(shardingRuleConfig, Arrays.asList("ds_0", "ds_1"));
        databaseHintRoutingEngine = new DatabaseHintRoutingEngine(
                shardingRule.getShardingDataSourceNames().getDataSourceNames(), (HintShardingStrategy) shardingRule.getDefaultDatabaseShardingStrategy());
    }
    
    @After
    public void tearDown() {
        hintManager.close();
    }
    
    @Test
    public void assertRoute() {
        hintManager.setDatabaseShardingValue(1);
        RoutingResult routingResult = databaseHintRoutingEngine.route();
        List<RoutingUnit> tableUnitList = new ArrayList<>(routingResult.getRoutingUnits());
        assertThat(routingResult, instanceOf(RoutingResult.class));
        assertThat(routingResult.getRoutingUnits().size(), is(1));
        assertThat(tableUnitList.get(0).getDataSourceName(), is("ds_1"));
    }
}
