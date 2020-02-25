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

package com.clinbrain.bd.core.route.router.sharding;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import com.clinbrain.bd.api.hint.HintManager;
import com.clinbrain.bd.core.metadata.ShardingMetaData;
import com.clinbrain.bd.core.parse.cache.ParsingResultCache;
import com.clinbrain.bd.core.rule.ShardingRule;
import com.clinbrain.bd.spi.DbType;

/**
 * Sharding router factory.
 * 
 * @author zhangliang
 * @author panjuan
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ShardingRouterFactory {
    
    /**
     * Create new instance of sharding router.
     * 
     * @param shardingRule sharding rule
     * @param shardingMetaData sharding meta data
     * @param databaseType database type
     * @param parsingResultCache parsing result cache
     * @return sharding router instance
     */
    public static ShardingRouter newInstance(final ShardingRule shardingRule, final ShardingMetaData shardingMetaData, final DbType databaseType, final ParsingResultCache parsingResultCache) {
        return HintManager.isDatabaseShardingOnly()
                ? new DatabaseHintSQLRouter(databaseType, shardingRule) : new ParsingSQLRouter(shardingRule, shardingMetaData, databaseType, parsingResultCache);
    }
}
