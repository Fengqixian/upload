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

package com.clinbrain.bd.shardingproxy.backend.schema;

import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import com.clinbrain.bd.api.config.masterslave.MasterSlaveRuleConfiguration;
import com.clinbrain.bd.api.config.sharding.ShardingRuleConfiguration;
import com.clinbrain.bd.core.constant.DatabaseType;
import com.clinbrain.bd.core.metadata.ShardingMetaData;
import com.clinbrain.bd.core.metadata.datasource.ShardingDataSourceMetaData;
import com.clinbrain.bd.core.metadata.table.ShardingTableMetaData;
import com.clinbrain.bd.core.parse.entry.MasterSlaveSQLParseEntry;
import com.clinbrain.bd.core.rule.MasterSlaveRule;
import com.clinbrain.bd.core.rule.ShardingRule;
import com.clinbrain.bd.orchestration.internal.registry.config.event.MasterSlaveRuleChangedEvent;
import com.clinbrain.bd.orchestration.internal.registry.state.event.DisabledStateChangedEvent;
import com.clinbrain.bd.orchestration.internal.registry.state.schema.OrchestrationShardingSchema;
import com.clinbrain.bd.orchestration.internal.rule.OrchestrationMasterSlaveRule;
import com.clinbrain.bd.shardingproxy.config.yaml.YamlDataSourceParameter;

import java.util.Map;

/**
 * Master-slave schema.
 *
 * @author panjuan
 */
@Getter
public final class MasterSlaveSchema extends LogicSchema {
    
    private MasterSlaveRule masterSlaveRule;
    
    private final ShardingRule shardingRule;
    
    private final ShardingMetaData metaData;
    
    private final MasterSlaveSQLParseEntry parseEngine;
    
    public MasterSlaveSchema(final String name, final Map<String, YamlDataSourceParameter> dataSources, final MasterSlaveRuleConfiguration masterSlaveRuleConfig, final boolean isUsingRegistry) {
        super(name, dataSources);
        masterSlaveRule = createMasterSlaveRule(masterSlaveRuleConfig, isUsingRegistry);
        // TODO we should remove it after none-sharding parsingEngine completed.
        shardingRule = new ShardingRule(new ShardingRuleConfiguration(), getDataSources().keySet());
        metaData = createShardingMetaData();
        parseEngine = new MasterSlaveSQLParseEntry(LogicSchemas.getInstance().getDatabaseType());
    }
    
    private MasterSlaveRule createMasterSlaveRule(final MasterSlaveRuleConfiguration masterSlaveRuleConfig, final boolean isUsingRegistry) {
        return isUsingRegistry ? new OrchestrationMasterSlaveRule(masterSlaveRuleConfig) : new MasterSlaveRule(masterSlaveRuleConfig);
    }
    
    private ShardingMetaData createShardingMetaData() {
        ShardingDataSourceMetaData shardingDataSourceMetaData = new ShardingDataSourceMetaData(
                getDataSourceURLs(getDataSources()), shardingRule, DatabaseType.valueOf(LogicSchemas.getInstance().getDatabaseType().getName()));
        ShardingTableMetaData shardingTableMetaData = new ShardingTableMetaData(getTableMetaDataInitializer(shardingDataSourceMetaData).load(shardingRule));
        return new ShardingMetaData(shardingDataSourceMetaData, shardingTableMetaData);
    }
    
    /**
     * Renew master-slave rule.
     *
     * @param masterSlaveRuleChangedEvent master-slave rule changed event.
     */
    @Subscribe
    public synchronized void renew(final MasterSlaveRuleChangedEvent masterSlaveRuleChangedEvent) {
        if (getName().equals(masterSlaveRuleChangedEvent.getShardingSchemaName())) {
            masterSlaveRule = new OrchestrationMasterSlaveRule(masterSlaveRuleChangedEvent.getMasterSlaveRuleConfiguration());
        }
    }
    
    /**
     * Renew disabled data source names.
     *
     * @param disabledStateChangedEvent disabled state changed event
     */
    @Subscribe
    public synchronized void renew(final DisabledStateChangedEvent disabledStateChangedEvent) {
        OrchestrationShardingSchema shardingSchema = disabledStateChangedEvent.getShardingSchema();
        if (getName().equals(shardingSchema.getSchemaName())) {
            ((OrchestrationMasterSlaveRule) masterSlaveRule).updateDisabledDataSourceNames(shardingSchema.getDataSourceName(), disabledStateChangedEvent.isDisabled());
        }
    }
}
