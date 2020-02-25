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

package com.clinbrain.bd.shardingproxy.frontend;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import com.clinbrain.bd.core.spi.NewInstanceServiceLoader;
import com.clinbrain.bd.shardingproxy.frontend.spi.DatabaseProtocolFrontendEngine;
import com.clinbrain.bd.spi.DbType;

/**
 * Database protocol frontend engine factory.
 *
 * @author zhangliang
 * @author xiaoyu
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DatabaseProtocolFrontendEngineFactory {
    
    static {
        NewInstanceServiceLoader.register(DatabaseProtocolFrontendEngine.class);
    }
    
    /**
     * Create new instance of database protocol frontend engine.
     *
     * @param databaseType database type
     * @return new instance of database protocol frontend engine
     */
    public static DatabaseProtocolFrontendEngine newInstance(final DbType databaseType) {
        for (DatabaseProtocolFrontendEngine each : NewInstanceServiceLoader.newServiceInstances(DatabaseProtocolFrontendEngine.class)) {
            if (each.getDatabaseType().equals(databaseType.getName())) {
                return each;
            }
        }
        throw new UnsupportedOperationException(String.format("Cannot support database type '%s'", databaseType));
    }
}
