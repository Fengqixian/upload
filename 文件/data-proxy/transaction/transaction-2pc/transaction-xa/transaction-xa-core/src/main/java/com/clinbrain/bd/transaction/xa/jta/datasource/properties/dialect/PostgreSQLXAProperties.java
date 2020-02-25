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

package com.clinbrain.bd.transaction.xa.jta.datasource.properties.dialect;

import com.clinbrain.bd.transaction.xa.jta.datasource.properties.XAProperties;
import com.google.common.base.Optional;
import com.clinbrain.bd.core.config.DatabaseAccessConfiguration;
import com.clinbrain.bd.core.metadata.datasource.dialect.PostgreSQLDataSourceMetaData;

import java.util.Properties;

/**
 * XA properties for PostgreSQL.
 *
 * @author zhaojun
 */
public final class PostgreSQLXAProperties implements XAProperties {
    
    @Override
    public Properties build(final DatabaseAccessConfiguration databaseAccessConfiguration) {
        Properties result = new Properties();
        PostgreSQLDataSourceMetaData dataSourceMetaData = new PostgreSQLDataSourceMetaData(databaseAccessConfiguration.getUrl());
        result.setProperty("user", databaseAccessConfiguration.getUsername());
        result.setProperty("password", Optional.fromNullable(databaseAccessConfiguration.getPassword()).or(""));
        result.setProperty("serverName", dataSourceMetaData.getHostName());
        result.setProperty("portNumber", String.valueOf(dataSourceMetaData.getPort()));
        result.setProperty("databaseName", dataSourceMetaData.getSchemaName());
        return result;
    }
}
