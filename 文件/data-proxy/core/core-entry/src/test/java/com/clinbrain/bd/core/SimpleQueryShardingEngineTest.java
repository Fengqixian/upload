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

import lombok.SneakyThrows;
import com.clinbrain.bd.core.metadata.ShardingMetaData;
import com.clinbrain.bd.core.parse.cache.ParsingResultCache;
import com.clinbrain.bd.core.route.SQLRouteResult;
import com.clinbrain.bd.core.route.StatementRoutingEngine;
import com.clinbrain.bd.core.rule.EncryptRule;
import com.clinbrain.bd.core.rule.ShardingRule;
import com.clinbrain.bd.core.strategy.encrypt.ShardingEncryptorEngine;
import com.clinbrain.bd.spi.DatabaseTypes;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class SimpleQueryShardingEngineTest extends BaseShardingEngineTest {
    
    @Mock
    private StatementRoutingEngine routingEngine;
    
    private SimpleQueryShardingEngine shardingEngine;
    
    public SimpleQueryShardingEngineTest() {
        super("SELECT 1", Collections.emptyList());
    }
    
    @Before
    public void setUp() {
        ShardingRule shardingRule = mock(ShardingRule.class);
        EncryptRule encryptRule = mock(EncryptRule.class);
        when(encryptRule.getEncryptorEngine()).thenReturn(new ShardingEncryptorEngine());
        when(shardingRule.getEncryptRule()).thenReturn(encryptRule);
        shardingEngine = new SimpleQueryShardingEngine(shardingRule, getShardingProperties(), mock(ShardingMetaData.class), DatabaseTypes.getActualDatabaseType("MySQL"), new ParsingResultCache());
        setRoutingEngine();
    }
    
    @SneakyThrows
    private void setRoutingEngine() {
        Field field = SimpleQueryShardingEngine.class.getDeclaredField("routingEngine");
        field.setAccessible(true);
        field.set(shardingEngine, routingEngine);
    }
    
    protected void assertShard() {
        SQLRouteResult sqlRouteResult = createSQLRouteResult();
        sqlRouteResult.getSqlStatement().setLogicSQL("SELECT 1");
        when(routingEngine.route(getSql())).thenReturn(sqlRouteResult);
        assertSQLRouteResult(shardingEngine.shard(getSql(), getParameters()));
    }
}
