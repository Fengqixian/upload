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

package com.clinbrain.bd.core.parse.filler.encrypt.dml.insert;

import lombok.Setter;
import com.clinbrain.bd.core.parse.filler.api.EncryptRuleAwareFiller;
import com.clinbrain.bd.core.parse.filler.api.SQLSegmentFiller;
import com.clinbrain.bd.core.parse.sql.context.insertvalue.InsertValue;
import com.clinbrain.bd.core.parse.sql.segment.dml.InsertValuesSegment;
import com.clinbrain.bd.core.parse.sql.statement.SQLStatement;
import com.clinbrain.bd.core.parse.sql.statement.dml.InsertStatement;
import com.clinbrain.bd.core.rule.EncryptRule;

import java.util.Collection;

/**
 * Insert values filler for encrypt.
 *
 * @author zhangliang
 * @author panjuan
 */
@Setter
public final class EncryptInsertValuesFiller implements SQLSegmentFiller<InsertValuesSegment>, EncryptRuleAwareFiller {
    
    private EncryptRule encryptRule;
    
    @Override
    public void fill(final InsertValuesSegment sqlSegment, final SQLStatement sqlStatement) {
        InsertStatement insertStatement = (InsertStatement) sqlStatement;
        InsertValue insertValue = new InsertValue(sqlSegment.getValues());
        insertStatement.getValues().add(insertValue);
        insertStatement.setParametersIndex(insertStatement.getParametersIndex() + insertValue.getParametersCount());
        reviseInsertStatement(insertStatement, sqlSegment);
    }
    
    private void reviseInsertStatement(final InsertStatement insertStatement, final InsertValuesSegment sqlSegment) {
        reviseInsertColumnNames(insertStatement);
        setNeededToAppendAssistedColumns(insertStatement);
        
    }
    
    private void reviseInsertColumnNames(final InsertStatement insertStatement) {
        insertStatement.getColumnNames().removeAll(encryptRule.getEncryptorEngine().getAssistedQueryColumns(insertStatement.getTables().getSingleTableName()));
    }
    
    private void setNeededToAppendAssistedColumns(final InsertStatement insertStatement) {
        Collection<String> assistedQueryColumns = encryptRule.getEncryptorEngine().getAssistedQueryColumns(insertStatement.getTables().getSingleTableName());
        if (!assistedQueryColumns.isEmpty()) {
            insertStatement.setNeededToAppendAssistedColumns(true);
        }
    }
}
