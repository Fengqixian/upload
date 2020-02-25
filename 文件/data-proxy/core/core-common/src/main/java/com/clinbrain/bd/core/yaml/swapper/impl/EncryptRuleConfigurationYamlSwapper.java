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

import com.clinbrain.bd.api.config.encryptor.EncryptRuleConfiguration;
import com.clinbrain.bd.api.config.encryptor.EncryptorRuleConfiguration;
import com.clinbrain.bd.core.yaml.config.encrypt.YamlEncryptRuleConfiguration;
import com.clinbrain.bd.core.yaml.config.encrypt.YamlEncryptorRuleConfiguration;
import com.clinbrain.bd.core.yaml.swapper.YamlSwapper;

import java.util.Map.Entry;

/**
 * Encrypt rule configuration yaml swapper.
 *
 * @author panjuan
 */
public final class EncryptRuleConfigurationYamlSwapper implements YamlSwapper<YamlEncryptRuleConfiguration, EncryptRuleConfiguration> {
    
    @Override
    public YamlEncryptRuleConfiguration swap(final EncryptRuleConfiguration encryptRuleConfiguration) {
        YamlEncryptRuleConfiguration result = new YamlEncryptRuleConfiguration();
        for (Entry<String, EncryptorRuleConfiguration> entry : encryptRuleConfiguration.getEncryptorRuleConfigs().entrySet()) {
            result.getEncryptors().put(entry.getKey(), new EncryptorRuleConfigurationYamlSwapper().swap(entry.getValue()));
        }
        return result;
    }
    
    @Override
    public EncryptRuleConfiguration swap(final YamlEncryptRuleConfiguration yamlEncryptRuleConfiguration) {
        EncryptRuleConfiguration result = new EncryptRuleConfiguration();
        for (Entry<String, YamlEncryptorRuleConfiguration> entry : yamlEncryptRuleConfiguration.getEncryptors().entrySet()) {
            result.getEncryptorRuleConfigs().put(entry.getKey(), new EncryptorRuleConfigurationYamlSwapper().swap(entry.getValue()));
        }
        return result;
    }
}
