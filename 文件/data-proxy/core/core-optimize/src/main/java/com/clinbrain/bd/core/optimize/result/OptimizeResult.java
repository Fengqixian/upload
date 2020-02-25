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

package com.clinbrain.bd.core.optimize.result;

import com.clinbrain.bd.core.optimize.condition.ShardingCondition;
import com.clinbrain.bd.core.optimize.condition.ShardingConditions;
import com.clinbrain.bd.core.optimize.pagination.Pagination;
import com.clinbrain.bd.core.optimize.result.insert.InsertOptimizeResult;
import com.google.common.base.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Collections;

/**
 * Optimize result.
 *
 * @author panjuan
 */
@RequiredArgsConstructor
@Getter
@Setter
public final class OptimizeResult {
    
    private final ShardingConditions shardingConditions;
    
    @Getter(AccessLevel.NONE)
    private final InsertOptimizeResult insertOptimizeResult;
    
    private Pagination pagination;
    
    public OptimizeResult(final ShardingConditions shardingConditions) {
        this(shardingConditions, null);
    }
    
    public OptimizeResult(final InsertOptimizeResult insertOptimizeResult) {
        this(new ShardingConditions(Collections.<ShardingCondition>emptyList()), insertOptimizeResult);
    }
    
    /**
     * Get insert optimize result.
     * 
     * @return insert optimize result
     */
    public Optional<InsertOptimizeResult> getInsertOptimizeResult() {
        return Optional.fromNullable(insertOptimizeResult);
    }
}
