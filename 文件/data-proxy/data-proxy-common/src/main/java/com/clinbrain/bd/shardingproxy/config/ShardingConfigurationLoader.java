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

package com.clinbrain.bd.shardingproxy.config;

import com.clinbrain.bd.shardingproxy.config.yaml.YamlProxyRuleConfiguration;
import com.clinbrain.bd.shardingproxy.config.yaml.YamlProxyServerConfiguration;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.clinbrain.bd.core.yaml.engine.YamlEngine;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Sharding configuration loader.
 *
 * @author chenqingyang
 */
public final class ShardingConfigurationLoader {
    
    //private static final String CONFIG_PATH = "/opt/complements/metadatajars/data-proxy-config/conf/";
    private static final String CONFIG_PATH = "E:/conf/";

    private static final String SERVER_CONFIG_FILE = "server.yaml";
    
    private static final Pattern RULE_CONFIG_FILE_PATTERN = Pattern.compile("config-.+\\.yaml");
    
    /**
     * Load configuration of Sharding-Proxy.
     *
     * @return configuration of Sharding-Proxy
     * @throws IOException IO exception
     */
    public ShardingConfiguration load() throws IOException {
        Collection<String> schemaNames = new HashSet<>();
        YamlProxyServerConfiguration serverConfig = loadServerConfiguration(new File(CONFIG_PATH + SERVER_CONFIG_FILE));
        File configPath = new File(CONFIG_PATH);
        Collection<YamlProxyRuleConfiguration> ruleConfigurations = new LinkedList<>();
        for (File each : findRuleConfigurationFiles(configPath)) {
            Optional<YamlProxyRuleConfiguration> ruleConfig = loadRuleConfiguration(each, serverConfig);
            if (ruleConfig.isPresent()) {
                Preconditions.checkState(schemaNames.add(ruleConfig.get().getSchemaName()), "Schema name `%s` must unique at all rule configurations.", ruleConfig.get().getSchemaName());
                ruleConfigurations.add(ruleConfig.get());
            }
        }
        Preconditions.checkState(!ruleConfigurations.isEmpty() || null != serverConfig.getOrchestration(), "Can not find any sharding rule configuration file in path `%s`.", configPath.getPath());
        Map<String, YamlProxyRuleConfiguration> ruleConfigurationMap = new HashMap<>(ruleConfigurations.size(), 1);
        for (YamlProxyRuleConfiguration each : ruleConfigurations) {
            ruleConfigurationMap.put(each.getSchemaName(), each);
        }
        return new ShardingConfiguration(serverConfig, ruleConfigurationMap);
    }
    
    private YamlProxyServerConfiguration loadServerConfiguration(final File yamlFile) throws IOException {
        YamlProxyServerConfiguration result = YamlEngine.unmarshal(yamlFile, YamlProxyServerConfiguration.class);
        Preconditions.checkNotNull(result, "Server configuration file `%s` is invalid.", yamlFile.getName());
        Preconditions.checkState(null != result.getAuthentication() || null != result.getOrchestration(), "Authority configuration is invalid.");
        return result;
    }
    
    private Optional<YamlProxyRuleConfiguration> loadRuleConfiguration(final File yamlFile, final YamlProxyServerConfiguration serverConfiguration) throws IOException {
        YamlProxyRuleConfiguration result = YamlEngine.unmarshal(yamlFile, YamlProxyRuleConfiguration.class);
        if (null == result) {
            return Optional.absent();
        }
        Preconditions.checkNotNull(result.getSchemaName(), "Property `schemaName` in file `%s` is required.", yamlFile.getName());
        Preconditions.checkState(!result.getDataSources().isEmpty(), "Data sources configuration in file `%s` is required.", yamlFile.getName());
        return Optional.of(result);
    }
    
    private File[] findRuleConfigurationFiles(final File path) {
        return path.listFiles(new FileFilter() {
            
            @Override
            public boolean accept(final File pathname) {
                return RULE_CONFIG_FILE_PATTERN.matcher(pathname.getName()).matches();
            }
        });
    }
}
