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

package com.clinbrain.bd.core.parse.extractor.impl.dml.select;

import com.google.common.base.Optional;
import org.antlr.v4.runtime.ParserRuleContext;
import com.clinbrain.bd.core.parse.extractor.api.OptionalSQLSegmentExtractor;
import com.clinbrain.bd.core.parse.extractor.util.RuleName;
import com.clinbrain.bd.core.parse.sql.segment.dml.expr.complex.SubquerySegment;

import java.util.Map;

/**
 * Subquery extractor.
 *
 * @author duhongjun
 */
public final class SubqueryExtractor implements OptionalSQLSegmentExtractor {
    
    @Override
    public Optional<SubquerySegment> extract(final ParserRuleContext subqueryNode, final Map<ParserRuleContext, Integer> parameterMarkerIndexes) {
        return RuleName.SUBQUERY.getName().equals(subqueryNode.getClass().getSimpleName())
                ? Optional.of(new SubquerySegment(subqueryNode.getStart().getStartIndex(), subqueryNode.getStop().getStopIndex(), subqueryNode.getText())) : Optional.<SubquerySegment>absent();
    }
}
