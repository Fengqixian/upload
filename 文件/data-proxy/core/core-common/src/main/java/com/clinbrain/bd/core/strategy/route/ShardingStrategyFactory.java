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

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import com.clinbrain.bd.api.config.sharding.strategy.ComplexShardingStrategyConfiguration;
import com.clinbrain.bd.api.config.sharding.strategy.HintShardingStrategyConfiguration;
import com.clinbrain.bd.api.config.sharding.strategy.InlineShardingStrategyConfiguration;
import com.clinbrain.bd.api.config.sharding.strategy.ShardingStrategyConfiguration;
import com.clinbrain.bd.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import com.clinbrain.bd.core.strategy.route.complex.ComplexShardingStrategy;
import com.clinbrain.bd.core.strategy.route.hint.HintShardingStrategy;
import com.clinbrain.bd.core.strategy.route.inline.InlineShardingStrategy;
import com.clinbrain.bd.core.strategy.route.none.NoneShardingStrategy;
import com.clinbrain.bd.core.strategy.route.standard.StandardShardingStrategy;

/**
 * Sharding strategy factory.
 * 
 * @author zhangliang
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ShardingStrategyFactory {
    
    /**
     * Create sharding algorithm.
     * 
     * @param shardingStrategyConfig sharding strategy configuration
     * @return sharding strategy instance
     */
    public static ShardingStrategy newInstance(final ShardingStrategyConfiguration shardingStrategyConfig) {
        if (shardingStrategyConfig instanceof StandardShardingStrategyConfiguration) {
            return new StandardShardingStrategy((StandardShardingStrategyConfiguration) shardingStrategyConfig);
        }
        if (shardingStrategyConfig instanceof InlineShardingStrategyConfiguration) {
            return new InlineShardingStrategy((InlineShardingStrategyConfiguration) shardingStrategyConfig);
        }
        if (shardingStrategyConfig instanceof ComplexShardingStrategyConfiguration) {
            return new ComplexShardingStrategy((ComplexShardingStrategyConfiguration) shardingStrategyConfig);
        }
        if (shardingStrategyConfig instanceof HintShardingStrategyConfiguration) {
            return new HintShardingStrategy((HintShardingStrategyConfiguration) shardingStrategyConfig);
        }
        return new NoneShardingStrategy();
    }
}
