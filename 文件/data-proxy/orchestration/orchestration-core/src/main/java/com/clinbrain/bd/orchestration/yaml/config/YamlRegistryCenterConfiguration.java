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

package com.clinbrain.bd.orchestration.yaml.config;

import lombok.Getter;
import lombok.Setter;
import com.clinbrain.bd.core.yaml.config.YamlConfiguration;

import java.util.Properties;

/**
 * Yaml registry center configuration.
 *
 * @author zhaojun
 */
@Getter
@Setter
public class YamlRegistryCenterConfiguration implements YamlConfiguration {
    
    private String type;
    
    private String serverLists;
    
    private String namespace;
    
    private String digest;
    
    private int operationTimeoutMilliseconds = 500;
    
    private int maxRetries = 3;
    
    private int retryIntervalMilliseconds = 500;
    
    private int timeToLiveSeconds = 60;
    
    private Properties props = new Properties();
}
