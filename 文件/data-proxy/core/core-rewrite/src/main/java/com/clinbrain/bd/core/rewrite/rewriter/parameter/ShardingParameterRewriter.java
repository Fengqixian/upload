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

package com.clinbrain.bd.core.rewrite.rewriter.parameter;

import com.clinbrain.bd.core.rewrite.builder.ParameterBuilder;
import com.google.common.base.Optional;
import lombok.RequiredArgsConstructor;
import com.clinbrain.bd.core.parse.sql.statement.dml.SelectStatement;
import com.clinbrain.bd.core.route.SQLRouteResult;

/**
 * Parameter rewriter for sharding.
 *
 * @author zhangliang
 */
@RequiredArgsConstructor
public final class ShardingParameterRewriter implements ParameterRewriter {
    
    private final SQLRouteResult sqlRouteResult;
    
    @Override
    public void rewrite(final ParameterBuilder parameterBuilder) {
        if (isNeedRewritePagination()) {
            Optional<Integer> offsetParameterIndex = sqlRouteResult.getOptimizeResult().getPagination().getOffsetParameterIndex();
            if (offsetParameterIndex.isPresent()) {
                rewriteOffset(parameterBuilder, offsetParameterIndex.get());
            }
            Optional<Integer> rowCountParameterIndex = sqlRouteResult.getOptimizeResult().getPagination().getRowCountParameterIndex();
            if (rowCountParameterIndex.isPresent()) {
                rewriteRowCount(parameterBuilder, rowCountParameterIndex.get());
            }
        }
    }
    
    private boolean isNeedRewritePagination() {
        return null != sqlRouteResult.getOptimizeResult().getPagination() && !sqlRouteResult.getRoutingResult().isSingleRouting();
    }
    
    private void rewriteOffset(final ParameterBuilder parameterBuilder, final int offsetParameterIndex) {
        parameterBuilder.getReplacedIndexAndParameters().put(offsetParameterIndex, sqlRouteResult.getOptimizeResult().getPagination().getRevisedOffset());
    }
    
    private void rewriteRowCount(final ParameterBuilder parameterBuilder, final int rowCountParameterIndex) {
        parameterBuilder.getReplacedIndexAndParameters().put(
                rowCountParameterIndex, sqlRouteResult.getOptimizeResult().getPagination().getRevisedRowCount((SelectStatement) sqlRouteResult.getSqlStatement()));
    }
}
