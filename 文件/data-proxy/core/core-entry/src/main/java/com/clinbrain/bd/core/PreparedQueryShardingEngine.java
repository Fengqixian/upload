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

package com.clinbrain.bd.core;

import com.clinbrain.bd.core.constant.properties.ShardingProperties;
import com.clinbrain.bd.core.metadata.ShardingMetaData;
import com.clinbrain.bd.core.parse.cache.ParsingResultCache;
import com.clinbrain.bd.core.route.PreparedStatementRoutingEngine;
import com.clinbrain.bd.core.route.SQLRouteResult;
import com.clinbrain.bd.core.rule.ShardingRule;
import com.clinbrain.bd.spi.DbType;

import java.util.ArrayList;
import java.util.List;

/**
 * Sharding engine for prepared query.
 * 
 * <pre>
 *     Prepared query:  
 *       for JDBC is PreparedStatement; 
 *       for MyQL is COM_STMT; 
 *       for PostgreSQL is Extended Query;
 * </pre>
 * 
 * @author zhangliang
 */
public final class PreparedQueryShardingEngine extends BaseShardingEngine {
    
    private final PreparedStatementRoutingEngine routingEngine;
    
    public PreparedQueryShardingEngine(final String sql, final ShardingRule shardingRule, final ShardingProperties shardingProperties,
                                       final ShardingMetaData metaData, final DbType databaseType, final ParsingResultCache cache) {
        super(shardingRule, shardingProperties, metaData);
        routingEngine = new PreparedStatementRoutingEngine(sql, shardingRule, metaData, databaseType, cache);
    }
    
    @Override
    protected List<Object> cloneParameters(final List<Object> parameters) {
        return new ArrayList<>(parameters);
    }
    
    @Override
    protected SQLRouteResult route(final String sql, final List<Object> parameters) {
        return routingEngine.route(parameters);
    }
}
