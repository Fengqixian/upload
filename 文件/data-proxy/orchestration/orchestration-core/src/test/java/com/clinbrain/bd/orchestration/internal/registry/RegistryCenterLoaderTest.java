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

import com.clinbrain.bd.orchestration.internal.registry.fixture.SecondTestRegistryCenter;
import com.clinbrain.bd.orchestration.reg.api.RegistryCenter;
import com.clinbrain.bd.orchestration.reg.api.RegistryCenterConfiguration;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public final class RegistryCenterLoaderTest {
    
    @Test
    public void assertLoad() {
        RegistryCenter regCenter = new RegistryCenterServiceLoader().load(new RegistryCenterConfiguration("SecondTestRegistryCenter"));
        assertThat(regCenter, instanceOf(SecondTestRegistryCenter.class));
    }
}
