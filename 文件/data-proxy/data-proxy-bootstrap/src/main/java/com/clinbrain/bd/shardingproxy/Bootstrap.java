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

package com.clinbrain.bd.shardingproxy;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import com.clinbrain.bd.api.config.RuleConfiguration;
import com.clinbrain.bd.core.config.DataSourceConfiguration;
import com.clinbrain.bd.core.constant.properties.ShardingPropertiesConstant;
import com.clinbrain.bd.core.rule.Authentication;
import com.clinbrain.bd.core.yaml.config.common.YamlAuthenticationConfiguration;
import com.clinbrain.bd.core.yaml.swapper.impl.AuthenticationYamlSwapper;
import com.clinbrain.bd.core.yaml.swapper.impl.EncryptRuleConfigurationYamlSwapper;
import com.clinbrain.bd.core.yaml.swapper.impl.MasterSlaveRuleConfigurationYamlSwapper;
import com.clinbrain.bd.core.yaml.swapper.impl.ShardingRuleConfigurationYamlSwapper;
import com.clinbrain.bd.opentracing.ShardingTracer;
import com.clinbrain.bd.orchestration.internal.registry.ShardingOrchestrationFacade;
import com.clinbrain.bd.orchestration.yaml.swapper.OrchestrationConfigurationYamlSwapper;
import com.clinbrain.bd.shardingproxy.backend.schema.LogicSchemas;
import com.clinbrain.bd.shardingproxy.config.ShardingConfiguration;
import com.clinbrain.bd.shardingproxy.config.ShardingConfigurationLoader;
import com.clinbrain.bd.shardingproxy.config.yaml.YamlDataSourceParameter;
import com.clinbrain.bd.shardingproxy.config.yaml.YamlProxyRuleConfiguration;
import com.clinbrain.bd.shardingproxy.config.yaml.YamlProxyServerConfiguration;
import com.clinbrain.bd.shardingproxy.context.ShardingProxyContext;
import com.clinbrain.bd.shardingproxy.frontend.bootstrap.ShardingProxy;
import com.clinbrain.bd.shardingproxy.util.DataSourceConverter;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

/**
 * Sharding-Proxy Bootstrap.
 *
 * @author zhangliang
 * @author wangkai
 * @author panjuan
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Bootstrap {
    
    private static final int DEFAULT_PORT = 3307;
    
    /**
     * Main entrance.
     *
     * @param args startup arguments
     * @throws IOException IO exception
     */
    public static void main(final String[] args) throws IOException {
        ShardingConfiguration shardingConfig = new ShardingConfigurationLoader().load();
        int port = getPort(args);
        if (null == shardingConfig.getServerConfiguration().getOrchestration()) {
            startWithoutRegistryCenter(shardingConfig.getRuleConfigurationMap(), shardingConfig.getServerConfiguration().getAuthentication(), shardingConfig.getServerConfiguration().getProps(), port);
        } else {
            startWithRegistryCenter(shardingConfig.getServerConfiguration(), shardingConfig.getRuleConfigurationMap().keySet(), shardingConfig.getRuleConfigurationMap(), port);
        }
    }
    
    private static int getPort(final String[] args) {
        if (0 == args.length) {
            return DEFAULT_PORT;
        }
        try {
            return Integer.parseInt(args[0]);
        } catch (final NumberFormatException ex) {
            return DEFAULT_PORT;
        }
    }
    
    private static void startWithoutRegistryCenter(final Map<String, YamlProxyRuleConfiguration> ruleConfigs, 
                                                   final YamlAuthenticationConfiguration authentication, final Properties prop, final int port) {
        ShardingProxyContext.getInstance().init(getAuthentication(authentication), prop);
        LogicSchemas.getInstance().init(getDataSourceParameterMap(ruleConfigs), getRuleConfiguration(ruleConfigs));
        initOpenTracing();
        ShardingProxy.getInstance().start(port);
    }

    
    private static void startWithRegistryCenter(final YamlProxyServerConfiguration serverConfig,
                                                final Collection<String> shardingSchemaNames, final Map<String, YamlProxyRuleConfiguration> ruleConfigs, final int port) {
        try (ShardingOrchestrationFacade shardingOrchestrationFacade = new ShardingOrchestrationFacade(
                new OrchestrationConfigurationYamlSwapper().swap(serverConfig.getOrchestration()), shardingSchemaNames)) {
            initShardingOrchestrationFacade(serverConfig, ruleConfigs, shardingOrchestrationFacade);
            ShardingProxyContext.getInstance().init(shardingOrchestrationFacade.getConfigService().loadAuthentication(), shardingOrchestrationFacade.getConfigService().loadProperties());
            LogicSchemas.getInstance().init(shardingSchemaNames, getSchemaDataSourceParameterMap(shardingOrchestrationFacade), getSchemaRules(shardingOrchestrationFacade), true);
            initOpenTracing();
            ShardingProxy.getInstance().start(port);
        }
    }
    
    private static Map<String, Map<String, YamlDataSourceParameter>> getSchemaDataSourceParameterMap(final ShardingOrchestrationFacade shardingOrchestrationFacade) {
        Map<String, Map<String, YamlDataSourceParameter>> result = new LinkedHashMap<>();
        for (String each : shardingOrchestrationFacade.getConfigService().getAllShardingSchemaNames()) {
            result.put(each, DataSourceConverter.getDataSourceParameterMap(shardingOrchestrationFacade.getConfigService().loadDataSourceConfigurations(each)));
        }
        return result;
    }
    
    private static Map<String, RuleConfiguration> getSchemaRules(final ShardingOrchestrationFacade shardingOrchestrationFacade) {
        Map<String, RuleConfiguration> result = new LinkedHashMap<>();
        for (String each : shardingOrchestrationFacade.getConfigService().getAllShardingSchemaNames()) {
            if (shardingOrchestrationFacade.getConfigService().isShardingRule(each)) {
                result.put(each, shardingOrchestrationFacade.getConfigService().loadShardingRuleConfiguration(each));
            } else {
                result.put(each, shardingOrchestrationFacade.getConfigService().loadMasterSlaveRuleConfiguration(each));
            }
        }
        return result;
    }
    
    private static void initShardingOrchestrationFacade(
            final YamlProxyServerConfiguration serverConfig, final Map<String, YamlProxyRuleConfiguration> ruleConfigs, final ShardingOrchestrationFacade shardingOrchestrationFacade) {
        if (ruleConfigs.isEmpty()) {
            shardingOrchestrationFacade.init();
        } else {
            shardingOrchestrationFacade.init(getDataSourceConfigurationMap(ruleConfigs), 
                    getRuleConfiguration(ruleConfigs), getAuthentication(serverConfig.getAuthentication()), serverConfig.getProps());
        }
    }
    
    private static void initOpenTracing() {
        if (ShardingProxyContext.getInstance().getShardingProperties().<Boolean>getValue(ShardingPropertiesConstant.PROXY_OPENTRACING_ENABLED)) {
            ShardingTracer.init();
        }
    }
    
    private static Map<String, Map<String, DataSourceConfiguration>> getDataSourceConfigurationMap(final Map<String, YamlProxyRuleConfiguration> ruleConfigs) {
        Map<String, Map<String, DataSourceConfiguration>> result = new LinkedHashMap<>();
        for (Entry<String, YamlProxyRuleConfiguration> entry : ruleConfigs.entrySet()) {
            result.put(entry.getKey(), DataSourceConverter.getDataSourceConfigurationMap(entry.getValue().getDataSources()));
        }
        return result;
    }

    private static Map<String, Map<String, YamlDataSourceParameter>> getDataSourceParameterMap(final Map<String, YamlProxyRuleConfiguration> localRuleConfigs) {
        Map<String, Map<String, YamlDataSourceParameter>> result = new HashMap<>(localRuleConfigs.size(), 1);
        for (Entry<String, YamlProxyRuleConfiguration> entry : localRuleConfigs.entrySet()) {
            result.put(entry.getKey(), entry.getValue().getDataSources());
        }
        return result;
    }
    
    private static Map<String, RuleConfiguration> getRuleConfiguration(final Map<String, YamlProxyRuleConfiguration> localRuleConfigs) {
        Map<String, RuleConfiguration> result = new HashMap<>();
        for (Entry<String, YamlProxyRuleConfiguration> entry : localRuleConfigs.entrySet()) {
            if (null != entry.getValue().getShardingRule()) {
                result.put(entry.getKey(), new ShardingRuleConfigurationYamlSwapper().swap(entry.getValue().getShardingRule()));
            } else if (null != entry.getValue().getMasterSlaveRule()) {
                result.put(entry.getKey(), new MasterSlaveRuleConfigurationYamlSwapper().swap(entry.getValue().getMasterSlaveRule()));
            } else if (null != entry.getValue().getEncryptRule()) {
                result.put(entry.getKey(), new EncryptRuleConfigurationYamlSwapper().swap(entry.getValue().getEncryptRule()));
            }
        }
        return result;
    }
    
    private static Authentication getAuthentication(final YamlAuthenticationConfiguration yamlAuthenticationConfig) {
        return new AuthenticationYamlSwapper().swap(yamlAuthenticationConfig);
    }
}
