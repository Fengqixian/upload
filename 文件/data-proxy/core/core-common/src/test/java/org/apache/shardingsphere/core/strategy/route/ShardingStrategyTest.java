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

package com.clinbrain.bd.core.strategy.route;

import com.google.common.collect.Range;
import com.google.common.collect.Sets;
import com.clinbrain.bd.api.config.sharding.strategy.ComplexShardingStrategyConfiguration;
import com.clinbrain.bd.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import com.clinbrain.bd.core.strategy.route.complex.ComplexShardingStrategy;
import com.clinbrain.bd.core.strategy.route.fixture.ComplexKeysShardingAlgorithmFixture;
import com.clinbrain.bd.core.strategy.route.fixture.PreciseShardingAlgorithmFixture;
import com.clinbrain.bd.core.strategy.route.fixture.RangeShardingAlgorithmFixture;
import com.clinbrain.bd.core.strategy.route.none.NoneShardingStrategy;
import com.clinbrain.bd.core.strategy.route.standard.StandardShardingStrategy;
import com.clinbrain.bd.core.strategy.route.value.BetweenRouteValue;
import com.clinbrain.bd.core.strategy.route.value.ListRouteValue;
import com.clinbrain.bd.core.strategy.route.value.RouteValue;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public final class ShardingStrategyTest {
    
    private final Collection<String> targets = Sets.newHashSet("1", "2", "3");
    
    @Test
    public void assertDoShardingWithoutShardingColumns() {
        NoneShardingStrategy strategy = new NoneShardingStrategy();
        assertThat(strategy.doSharding(targets, Collections.<RouteValue>emptySet()), is(targets));
    }
    
    @Test
    public void assertDoShardingForBetweenSingleKey() {
        StandardShardingStrategy strategy = new StandardShardingStrategy(
                new StandardShardingStrategyConfiguration("column", new PreciseShardingAlgorithmFixture(), new RangeShardingAlgorithmFixture()));
        assertThat(strategy.doSharding(targets, Collections.<RouteValue>singletonList(new BetweenRouteValue<>("column", "logicTable", Range.open(1, 3)))), 
                is((Collection<String>) Sets.newHashSet("1")));
    }
    
    @Test
    public void assertDoShardingForMultipleKeys() {
        ComplexShardingStrategy strategy = new ComplexShardingStrategy(new ComplexShardingStrategyConfiguration("column", new ComplexKeysShardingAlgorithmFixture()));
        assertThat(strategy.doSharding(targets, Collections.<RouteValue>singletonList(new ListRouteValue<>("column", "logicTable", Collections.singletonList(1)))), 
                is((Collection<String>) Sets.newHashSet("1", "2", "3")));
    }
}
