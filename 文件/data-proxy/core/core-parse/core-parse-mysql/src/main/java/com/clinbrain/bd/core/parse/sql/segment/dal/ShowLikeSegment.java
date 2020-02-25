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

package com.clinbrain.bd.core.parse.sql.segment.dal;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.clinbrain.bd.core.parse.constant.QuoteCharacter;
import com.clinbrain.bd.core.parse.sql.segment.SQLSegment;
import com.clinbrain.bd.core.parse.sql.segment.TableAvailable;

/**
 * Show like segment.
 * 
 * @author zhangliang
 */
@RequiredArgsConstructor
@Getter
public final class ShowLikeSegment implements SQLSegment, TableAvailable {
    
    private final int startIndex;
    
    private final int stopIndex;
    
    private final String pattern;
    
    @Override
    public String getTableName() {
        return pattern;
    }
    
    @Override
    public QuoteCharacter getTableQuoteCharacter() {
        return QuoteCharacter.NONE;
    }
}
