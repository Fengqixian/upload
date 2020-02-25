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

package com.clinbrain.bd.core.rewrite.token.generator;

import com.clinbrain.bd.core.rewrite.token.pojo.RemoveToken;
import com.clinbrain.bd.core.parse.sql.segment.RemoveAvailable;
import com.clinbrain.bd.core.parse.sql.segment.SQLSegment;
import com.clinbrain.bd.core.parse.sql.statement.SQLStatement;
import com.clinbrain.bd.core.rule.BaseRule;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Remove token generator.
 *
 * @author zhangliang
 */
public final class RemoveTokenGenerator implements CollectionSQLTokenGenerator<BaseRule> {
    
    @Override
    public Collection<RemoveToken> generateSQLTokens(final SQLStatement sqlStatement, final List<Object> parameters, final BaseRule baseRule) {
        Collection<RemoveToken> result = new LinkedList<>();
        for (SQLSegment each : sqlStatement.getSqlSegments()) {
            if (each instanceof RemoveAvailable) {
                result.add(new RemoveToken(each.getStartIndex(), each.getStopIndex()));
            }
        }
        return result;
    }
}
