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

package com.clinbrain.bd.core.parse.filler.common.ddl.constraint;

import com.google.common.base.Optional;
import lombok.Setter;
import com.clinbrain.bd.core.metadata.table.ShardingTableMetaData;
import com.clinbrain.bd.core.parse.filler.api.SQLSegmentFiller;
import com.clinbrain.bd.core.parse.filler.api.ShardingTableMetaDataAwareFiller;
import com.clinbrain.bd.core.parse.sql.segment.ddl.column.ColumnDefinitionSegment;
import com.clinbrain.bd.core.parse.sql.segment.ddl.constraint.ConstraintDefinitionSegment;
import com.clinbrain.bd.core.parse.sql.statement.SQLStatement;
import com.clinbrain.bd.core.parse.sql.statement.ddl.AlterTableStatement;
import com.clinbrain.bd.core.parse.sql.statement.ddl.CreateTableStatement;

/**
 * Constraint definition filler.
 *
 * @author duhongjun
 */
@Setter
public final class ConstraintDefinitionFiller implements SQLSegmentFiller<ConstraintDefinitionSegment>, ShardingTableMetaDataAwareFiller {
    
    private ShardingTableMetaData shardingTableMetaData;
    
    @Override
    public void fill(final ConstraintDefinitionSegment sqlSegment, final SQLStatement sqlStatement) {
        if (sqlStatement instanceof CreateTableStatement) {
            fill(sqlSegment, (CreateTableStatement) sqlStatement);
        } else if (sqlStatement instanceof AlterTableStatement) {
            fill(sqlSegment, (AlterTableStatement) sqlStatement, shardingTableMetaData);
        }
    }
    
    private void fill(final ConstraintDefinitionSegment sqlSegment, final CreateTableStatement createTableStatement) {
        for (ColumnDefinitionSegment each : createTableStatement.getColumnDefinitions()) {
            if (sqlSegment.getPrimaryKeyColumnNames().contains(each.getColumnName())) {
                each.setPrimaryKey(true);
            }
        }
    }
    
    private void fill(final ConstraintDefinitionSegment sqlSegment, final AlterTableStatement alterTableStatement, final ShardingTableMetaData shardingTableMetaData) {
        for (String each : sqlSegment.getPrimaryKeyColumnNames()) {
            Optional<ColumnDefinitionSegment> modifiedColumn = alterTableStatement.findColumnDefinition(each, shardingTableMetaData);
            if (modifiedColumn.isPresent()) {
                modifiedColumn.get().setPrimaryKey(true);
                alterTableStatement.getModifiedColumnDefinitions().put(each, modifiedColumn.get());
            }
        }
    }
}
