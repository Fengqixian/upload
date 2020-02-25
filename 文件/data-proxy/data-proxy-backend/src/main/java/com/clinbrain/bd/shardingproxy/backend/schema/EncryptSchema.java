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

import lombok.Getter;
import com.clinbrain.bd.api.config.encryptor.EncryptRuleConfiguration;
import com.clinbrain.bd.api.config.sharding.ShardingRuleConfiguration;
import com.clinbrain.bd.core.constant.DatabaseType;
import com.clinbrain.bd.core.metadata.ShardingMetaData;
import com.clinbrain.bd.core.metadata.datasource.ShardingDataSourceMetaData;
import com.clinbrain.bd.core.metadata.table.ShardingTableMetaData;
import com.clinbrain.bd.core.parse.entry.EncryptSQLParseEntry;
import com.clinbrain.bd.core.rule.EncryptRule;
import com.clinbrain.bd.core.rule.ShardingRule;
import com.clinbrain.bd.shardingproxy.config.yaml.YamlDataSourceParameter;

import java.util.Map;

/**
 * Encrypt schema.
 *
 * @author panjuan
 */
@Getter
public final class EncryptSchema extends LogicSchema {
    
    private final EncryptRule encryptRule;
    
    private final ShardingMetaData metaData;
    
    private final ShardingRule shardingRule;
    
    private final EncryptSQLParseEntry parseEngine;
    
    public EncryptSchema(final String name, final Map<String, YamlDataSourceParameter> dataSources, final EncryptRuleConfiguration encryptRuleConfiguration) {
        super(name, dataSources);
        encryptRule = new EncryptRule(encryptRuleConfiguration);
        shardingRule = new ShardingRule(new ShardingRuleConfiguration(), getDataSources().keySet());
        metaData = createShardingMetaData();
        parseEngine = new EncryptSQLParseEntry(LogicSchemas.getInstance().getDatabaseType(), encryptRule, metaData.getTable());
    }
    
    private ShardingMetaData createShardingMetaData() {
        ShardingDataSourceMetaData shardingDataSourceMetaData = new ShardingDataSourceMetaData(getDataSourceURLs(
                getDataSources()), shardingRule, DatabaseType.valueOf(LogicSchemas.getInstance().getDatabaseType().getName()));
        ShardingTableMetaData shardingTableMetaData = new ShardingTableMetaData(getTableMetaDataInitializer(shardingDataSourceMetaData).load(shardingRule));
        return new ShardingMetaData(shardingDataSourceMetaData, shardingTableMetaData);
    }
}
