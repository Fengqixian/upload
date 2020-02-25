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

package com.clinbrain.bd.core.yaml.swapper.impl;

import com.google.common.base.Preconditions;
import com.clinbrain.bd.api.config.sharding.strategy.ComplexShardingStrategyConfiguration;
import com.clinbrain.bd.api.config.sharding.strategy.HintShardingStrategyConfiguration;
import com.clinbrain.bd.api.config.sharding.strategy.InlineShardingStrategyConfiguration;
import com.clinbrain.bd.api.config.sharding.strategy.NoneShardingStrategyConfiguration;
import com.clinbrain.bd.api.config.sharding.strategy.ShardingStrategyConfiguration;
import com.clinbrain.bd.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import com.clinbrain.bd.api.sharding.complex.ComplexKeysShardingAlgorithm;
import com.clinbrain.bd.api.sharding.hint.HintShardingAlgorithm;
import com.clinbrain.bd.api.sharding.standard.PreciseShardingAlgorithm;
import com.clinbrain.bd.api.sharding.standard.RangeShardingAlgorithm;
import com.clinbrain.bd.core.strategy.route.ShardingAlgorithmFactory;
import com.clinbrain.bd.core.yaml.config.sharding.YamlShardingStrategyConfiguration;
import com.clinbrain.bd.core.yaml.config.sharding.strategy.YamlComplexShardingStrategyConfiguration;
import com.clinbrain.bd.core.yaml.config.sharding.strategy.YamlHintShardingStrategyConfiguration;
import com.clinbrain.bd.core.yaml.config.sharding.strategy.YamlInlineShardingStrategyConfiguration;
import com.clinbrain.bd.core.yaml.config.sharding.strategy.YamlStandardShardingStrategyConfiguration;
import com.clinbrain.bd.core.yaml.swapper.YamlSwapper;

/**
 * Sharding strategy configuration YAML swapper.
 *
 * @author zhangliang
 */
public final class ShardingStrategyConfigurationYamlSwapper implements YamlSwapper<YamlShardingStrategyConfiguration, ShardingStrategyConfiguration> {
    
    @Override
    public YamlShardingStrategyConfiguration swap(final ShardingStrategyConfiguration data) {
        YamlShardingStrategyConfiguration result = new YamlShardingStrategyConfiguration();
        if (data instanceof StandardShardingStrategyConfiguration) {
            result.setStandard(createYamlStandardShardingStrategyConfiguration((StandardShardingStrategyConfiguration) data));
        }
        if (data instanceof ComplexShardingStrategyConfiguration) {
            result.setComplex(createYamlComplexShardingStrategyConfiguration((ComplexShardingStrategyConfiguration) data));
        }
        if (data instanceof HintShardingStrategyConfiguration) {
            result.setHint(createYamlHintShardingStrategyConfiguration((HintShardingStrategyConfiguration) data));
        }
        if (data instanceof InlineShardingStrategyConfiguration) {
            result.setInline(createYamlInlineShardingStrategyConfiguration((InlineShardingStrategyConfiguration) data));
        }
        return result;
    }
    
    @Override
    public ShardingStrategyConfiguration swap(final YamlShardingStrategyConfiguration yamlConfiguration) {
        int shardingStrategyConfigCount = 0;
        ShardingStrategyConfiguration result = null;
        if (null != yamlConfiguration.getStandard()) {
            shardingStrategyConfigCount++;
            if (null == yamlConfiguration.getStandard().getRangeAlgorithmClassName()) {
                result = new StandardShardingStrategyConfiguration(yamlConfiguration.getStandard().getShardingColumn(),
                        ShardingAlgorithmFactory.newInstance(yamlConfiguration.getStandard().getPreciseAlgorithmClassName(), PreciseShardingAlgorithm.class));
            } else {
                result = new StandardShardingStrategyConfiguration(yamlConfiguration.getStandard().getShardingColumn(),
                        ShardingAlgorithmFactory.newInstance(yamlConfiguration.getStandard().getPreciseAlgorithmClassName(), PreciseShardingAlgorithm.class),
                        ShardingAlgorithmFactory.newInstance(yamlConfiguration.getStandard().getRangeAlgorithmClassName(), RangeShardingAlgorithm.class));
            }
        }
        if (null != yamlConfiguration.getComplex()) {
            shardingStrategyConfigCount++;
            result = new ComplexShardingStrategyConfiguration(yamlConfiguration.getComplex().getShardingColumns(), 
                    ShardingAlgorithmFactory.newInstance(yamlConfiguration.getComplex().getAlgorithmClassName(), ComplexKeysShardingAlgorithm.class));
        }
        if (null != yamlConfiguration.getInline()) {
            shardingStrategyConfigCount++;
            result = new InlineShardingStrategyConfiguration(yamlConfiguration.getInline().getShardingColumn(), yamlConfiguration.getInline().getAlgorithmExpression());
        }
        if (null != yamlConfiguration.getHint()) {
            shardingStrategyConfigCount++;
            result = new HintShardingStrategyConfiguration(ShardingAlgorithmFactory.newInstance(yamlConfiguration.getHint().getAlgorithmClassName(), HintShardingAlgorithm.class));
        }
        if (null != yamlConfiguration.getNone()) {
            shardingStrategyConfigCount++;
            result = new NoneShardingStrategyConfiguration();
        }
        Preconditions.checkArgument(shardingStrategyConfigCount <= 1, "Only allowed 0 or 1 sharding strategy configuration.");
        return result;
    }
    
    private YamlStandardShardingStrategyConfiguration createYamlStandardShardingStrategyConfiguration(final StandardShardingStrategyConfiguration data) {
        YamlStandardShardingStrategyConfiguration result = new YamlStandardShardingStrategyConfiguration();
        result.setShardingColumn(data.getShardingColumn());
        result.setPreciseAlgorithmClassName(data.getPreciseShardingAlgorithm().getClass().getName());
        if (null != data.getRangeShardingAlgorithm()) {
            result.setRangeAlgorithmClassName(data.getRangeShardingAlgorithm().getClass().getName());
        }
        return result;
    }
    
    private YamlComplexShardingStrategyConfiguration createYamlComplexShardingStrategyConfiguration(final ComplexShardingStrategyConfiguration data) {
        YamlComplexShardingStrategyConfiguration result = new YamlComplexShardingStrategyConfiguration();
        result.setShardingColumns(data.getShardingColumns());
        result.setAlgorithmClassName(data.getShardingAlgorithm().getClass().getName());
        return result;
    }
    
    private YamlHintShardingStrategyConfiguration createYamlHintShardingStrategyConfiguration(final HintShardingStrategyConfiguration data) {
        YamlHintShardingStrategyConfiguration result = new YamlHintShardingStrategyConfiguration();
        result.setAlgorithmClassName(data.getShardingAlgorithm().getClass().getName());
        return result;
    }
    
    private YamlInlineShardingStrategyConfiguration createYamlInlineShardingStrategyConfiguration(final InlineShardingStrategyConfiguration data) {
        YamlInlineShardingStrategyConfiguration result = new YamlInlineShardingStrategyConfiguration();
        result.setShardingColumn(data.getShardingColumn());
        result.setAlgorithmExpression(data.getAlgorithmExpression());
        return result;
    }
}
