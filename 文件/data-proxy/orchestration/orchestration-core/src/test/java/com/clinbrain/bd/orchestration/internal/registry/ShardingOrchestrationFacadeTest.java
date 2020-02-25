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

package com.clinbrain.bd.orchestration.internal.registry;

import com.clinbrain.bd.api.config.RuleConfiguration;
import com.clinbrain.bd.core.config.DataSourceConfiguration;
import com.clinbrain.bd.core.rule.Authentication;
import com.clinbrain.bd.core.rule.ProxyUser;
import com.clinbrain.bd.orchestration.config.OrchestrationConfiguration;
import com.clinbrain.bd.orchestration.internal.registry.config.service.ConfigurationService;
import com.clinbrain.bd.orchestration.internal.registry.listener.ShardingOrchestrationListenerManager;
import com.clinbrain.bd.orchestration.internal.registry.state.service.StateService;
import com.clinbrain.bd.orchestration.reg.api.RegistryCenter;
import com.clinbrain.bd.orchestration.reg.api.RegistryCenterConfiguration;
import com.clinbrain.bd.orchestration.util.FieldUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public final class ShardingOrchestrationFacadeTest {
    
    private ShardingOrchestrationFacade shardingOrchestrationFacade;
    
    @Mock
    private RegistryCenter regCenter;
    
    @Mock
    private ConfigurationService configService;
    
    @Mock
    private StateService stateService;
    
    @Mock
    private ShardingOrchestrationListenerManager listenerManager;
    
    @Before
    public void setUp() {
        OrchestrationConfiguration orchestrationConfiguration = new OrchestrationConfiguration("test", new RegistryCenterConfiguration("SecondTestRegistryCenter"), true);
        shardingOrchestrationFacade = new ShardingOrchestrationFacade(orchestrationConfiguration, Arrays.asList("sharding_db", "masterslave_db"));
        FieldUtil.setField(shardingOrchestrationFacade, "regCenter", regCenter);
        FieldUtil.setField(shardingOrchestrationFacade, "configService", configService);
        FieldUtil.setField(shardingOrchestrationFacade, "stateService", stateService);
        FieldUtil.setField(shardingOrchestrationFacade, "listenerManager", listenerManager);
    }
    
    @Test
    public void assertInitWithParameters() {
        Map<String, DataSourceConfiguration> dataSourceConfigurationMap = Collections.singletonMap("test_ds", mock(DataSourceConfiguration.class));
        Map<String, RuleConfiguration> ruleConfigurationMap = Collections.singletonMap("sharding_db", mock(RuleConfiguration.class));
        ProxyUser proxyUser = new ProxyUser("root", Collections.singleton("db1"));
        Authentication authentication = new Authentication();
        authentication.getUsers().put("root", proxyUser);
        Properties props = new Properties();
        shardingOrchestrationFacade.init(Collections.singletonMap("sharding_db", dataSourceConfigurationMap), ruleConfigurationMap, authentication, props);
        verify(configService).persistConfiguration("sharding_db", dataSourceConfigurationMap, ruleConfigurationMap.get("sharding_db"), authentication, props, true);
        verify(stateService).persistInstanceOnline();
        verify(stateService).persistDataSourcesNode();
        verify(listenerManager).initListeners();
    }
    
    @Test
    public void assertInitWithoutParameters() {
        shardingOrchestrationFacade.init();
        verify(stateService).persistInstanceOnline();
        verify(stateService).persistDataSourcesNode();
        verify(listenerManager).initListeners();
    }
    
    @Test
    public void assertCloseSuccess() {
        shardingOrchestrationFacade.close();
        verify(regCenter).close();
    }
    
    @Test
    public void assertCloseFailure() {
        doThrow(new RuntimeException()).when(regCenter).close();
        shardingOrchestrationFacade.close();
        verify(regCenter).close();
    }
}
