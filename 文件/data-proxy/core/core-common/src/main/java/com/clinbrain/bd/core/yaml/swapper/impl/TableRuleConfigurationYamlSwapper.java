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
import com.clinbrain.bd.api.config.sharding.TableRuleConfiguration;
import com.clinbrain.bd.core.yaml.config.sharding.YamlTableRuleConfiguration;
import com.clinbrain.bd.core.yaml.swapper.YamlSwapper;

/**
 * Table rule configuration YAML swapper.
 *
 * @author zhangliang
 * @author panjuan
 */
public final class TableRuleConfigurationYamlSwapper implements YamlSwapper<YamlTableRuleConfiguration, TableRuleConfiguration> {
    
    private final ShardingStrategyConfigurationYamlSwapper shardingStrategyConfigurationYamlSwapper = new ShardingStrategyConfigurationYamlSwapper();
    
    private final KeyGeneratorConfigurationYamlSwapper keyGeneratorConfigurationYamlSwapper = new KeyGeneratorConfigurationYamlSwapper();
    
    @Override
    public YamlTableRuleConfiguration swap(final TableRuleConfiguration data) {
        YamlTableRuleConfiguration result = new YamlTableRuleConfiguration();
        result.setLogicTable(data.getLogicTable());
        result.setActualDataNodes(data.getActualDataNodes());
        if (null != data.getDatabaseShardingStrategyConfig()) {
            result.setDatabaseStrategy(shardingStrategyConfigurationYamlSwapper.swap(data.getDatabaseShardingStrategyConfig()));
        }
        if (null != data.getTableShardingStrategyConfig()) {
            result.setTableStrategy(shardingStrategyConfigurationYamlSwapper.swap(data.getTableShardingStrategyConfig()));
        }
        if (null != data.getKeyGeneratorConfig()) {
            result.setKeyGenerator(keyGeneratorConfigurationYamlSwapper.swap(data.getKeyGeneratorConfig()));
        }
        result.setLogicIndex(data.getLogicIndex());
        return result;
    }
    
    @Override
    public TableRuleConfiguration swap(final YamlTableRuleConfiguration yamlConfiguration) {
        Preconditions.checkNotNull(yamlConfiguration.getLogicTable(), "Logic table cannot be null.");
        TableRuleConfiguration result = new TableRuleConfiguration(yamlConfiguration.getLogicTable(), yamlConfiguration.getActualDataNodes());
        if (null != yamlConfiguration.getDatabaseStrategy()) {
            result.setDatabaseShardingStrategyConfig(shardingStrategyConfigurationYamlSwapper.swap(yamlConfiguration.getDatabaseStrategy()));
        }
        if (null != yamlConfiguration.getTableStrategy()) {
            result.setTableShardingStrategyConfig(shardingStrategyConfigurationYamlSwapper.swap(yamlConfiguration.getTableStrategy()));
        }
        if (null != yamlConfiguration.getKeyGenerator()) {
            result.setKeyGeneratorConfig(keyGeneratorConfigurationYamlSwapper.swap(yamlConfiguration.getKeyGenerator()));
        }
        result.setLogicIndex(yamlConfiguration.getLogicIndex());
        return result;
    }
}
