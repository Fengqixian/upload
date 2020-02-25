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

package com.clinbrain.bd.core.parse.sql.segment.dml.predicate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.clinbrain.bd.core.parse.sql.segment.SQLSegment;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Subquery predicate segment.
 *
 * @author duhongjun
 */
@RequiredArgsConstructor
@Getter
// TODO SubqueryPredicateSegment is a aggregation of all SubqueryPredicateSegments, should split them to multiple SubqueryPredicateSegment
public final class SubqueryPredicateSegment implements SQLSegment {
    
    private final int startIndex = 0;
    
    private final int stopIndex = 0;
    
    private Collection<OrPredicateSegment> orPredicates = new LinkedList<>();
}
