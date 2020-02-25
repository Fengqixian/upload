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
import com.clinbrain.bd.core.constant.properties.ShardingProperties;
import com.clinbrain.bd.core.constant.properties.ShardingPropertiesConstant;
import com.clinbrain.bd.core.execute.metadata.TableMetaDataInitializer;
import com.clinbrain.bd.core.metadata.ShardingMetaData;
import com.clinbrain.bd.core.metadata.datasource.ShardingDataSourceMetaData;
import com.clinbrain.bd.core.parse.cache.ParsingResultCache;
import com.clinbrain.bd.core.parse.sql.statement.SQLStatement;
import com.clinbrain.bd.core.rule.ShardingRule;
import com.clinbrain.bd.orchestration.internal.eventbus.ShardingOrchestrationEventBus;
import com.clinbrain.bd.orchestration.internal.registry.config.event.DataSourceChangedEvent;
import com.clinbrain.bd.shardingproxy.backend.communication.jdbc.datasource.JDBCBackendDataSource;
import com.clinbrain.bd.shardingproxy.backend.executor.BackendExecutorContext;
import com.clinbrain.bd.shardingproxy.config.yaml.YamlDataSourceParameter;
import com.clinbrain.bd.shardingproxy.context.ShardingProxyContext;
import com.clinbrain.bd.shardingproxy.util.DataSourceConverter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Logic schema.
 *
 * @author panjuan
 */
@Getter
public abstract class LogicSchema {
    
    private final String name;
    
    private final ParsingResultCache parsingResultCache;
    
    private JDBCBackendDataSource backendDataSource;
    
    public LogicSchema(final String name, final Map<String, YamlDataSourceParameter> dataSources) {
        this.name = name;
        parsingResultCache = new ParsingResultCache();
        backendDataSource = new JDBCBackendDataSource(dataSources);
        ShardingOrchestrationEventBus.getInstance().register(this);
    }
    
    /**
     * Get sharding meta data.
     * 
     * @return sharding meta data.
     */
    public abstract ShardingMetaData getMetaData();
    
    /**
     * Get Sharding rule.
     * 
     * @return sharding rule
     */
    // TODO : It is used in many places, but we can consider how to optimize it because of being irrational for logic schema.
    public abstract ShardingRule getShardingRule();
    
    /**
     * Get data source parameters.
     * 
     * @return data source parameters
     */
    public Map<String, YamlDataSourceParameter> getDataSources() {
        return backendDataSource.getDataSourceParameters();
    }
    
    protected final Map<String, String> getDataSourceURLs(final Map<String, YamlDataSourceParameter> dataSourceParameters) {
        Map<String, String> result = new LinkedHashMap<>(dataSourceParameters.size(), 1);
        for (Entry<String, YamlDataSourceParameter> entry : dataSourceParameters.entrySet()) {
            result.put(entry.getKey(), entry.getValue().getUrl());
        }
        return result;
    }
    
    protected final TableMetaDataInitializer getTableMetaDataInitializer(final ShardingDataSourceMetaData shardingDataSourceMetaData) {
        ShardingProperties shardingProperties = ShardingProxyContext.getInstance().getShardingProperties();
        return new TableMetaDataInitializer(
                shardingDataSourceMetaData, BackendExecutorContext.getInstance().getExecuteEngine(), new ProxyTableMetaDataConnectionManager(getBackendDataSource()),
                shardingProperties.<Integer>getValue(ShardingPropertiesConstant.MAX_CONNECTIONS_SIZE_PER_QUERY),
                shardingProperties.<Boolean>getValue(ShardingPropertiesConstant.CHECK_TABLE_METADATA_ENABLED));
    }
    
    /**
     * Renew data source configuration.
     *
     * @param dataSourceChangedEvent data source changed event.
     * @throws Exception exception
     */
    @Subscribe
    public final synchronized void renew(final DataSourceChangedEvent dataSourceChangedEvent) throws Exception {
        if (!name.equals(dataSourceChangedEvent.getShardingSchemaName())) {
            return;
        }
        backendDataSource.renew(DataSourceConverter.getDataSourceParameterMap(dataSourceChangedEvent.getDataSourceConfigurations()));
    }
    
    /**
     * Refresh table meta data.
     * 
     * @param sqlStatement SQL statement
     */
    public void refreshTableMetaData(final SQLStatement sqlStatement) {
    }
}
