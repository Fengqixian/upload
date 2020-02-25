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

package com.clinbrain.bd.core.route.router.masterslave;

import lombok.RequiredArgsConstructor;
import com.clinbrain.bd.api.hint.HintManager;
import com.clinbrain.bd.core.constant.SQLType;
import com.clinbrain.bd.core.parse.entry.MasterSlaveSQLParseEntry;
import com.clinbrain.bd.core.route.SQLLogger;
import com.clinbrain.bd.core.rule.MasterSlaveRule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Master slave router interface.
 * 
 * @author zhangliang
 * @author panjuan
 */
@RequiredArgsConstructor
public final class MasterSlaveRouter {
    
    private final MasterSlaveRule masterSlaveRule;
    
    private final MasterSlaveSQLParseEntry parseEngine;
    
    private final boolean showSQL;
    
    /**
     * Route Master slave.
     *
     * @param sql SQL
     * @param useCache use cache or not
     * @return data source names
     */
    // TODO for multiple masters may return more than one data source
    public Collection<String> route(final String sql, final boolean useCache) {
        Collection<String> result = route(parseEngine.parse(sql, useCache).getType());
        if (showSQL) {
            SQLLogger.logSQL(sql, result);
        }
        return result;
    }
    
    private Collection<String> route(final SQLType sqlType) {
        if (isMasterRoute(sqlType)) {
            MasterVisitedManager.setMasterVisited();
            return Collections.singletonList(masterSlaveRule.getMasterDataSourceName());
        }
        return Collections.singletonList(masterSlaveRule.getLoadBalanceAlgorithm().getDataSource(
                masterSlaveRule.getName(), masterSlaveRule.getMasterDataSourceName(), new ArrayList<>(masterSlaveRule.getSlaveDataSourceNames())));
    }
    
    private boolean isMasterRoute(final SQLType sqlType) {
        return SQLType.DQL != sqlType || MasterVisitedManager.isMasterVisited() || HintManager.isMasterRouteOnly();
    }
}
