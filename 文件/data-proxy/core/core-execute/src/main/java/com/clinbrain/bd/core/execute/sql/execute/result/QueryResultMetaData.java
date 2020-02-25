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

package com.clinbrain.bd.core.execute.sql.execute.result;

import com.google.common.base.Optional;
import lombok.SneakyThrows;
import com.clinbrain.bd.core.rule.EncryptRule;
import com.clinbrain.bd.core.rule.ShardingRule;
import com.clinbrain.bd.core.rule.TableRule;
import com.clinbrain.bd.core.strategy.encrypt.ShardingEncryptorEngine;
import com.clinbrain.bd.spi.encrypt.ShardingEncryptor;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Query result meta data.
 *
 * @author panjuan
 */
public final class QueryResultMetaData {
    
    private final Map<String, Integer> columnLabelAndIndexes;
    
    private final ResultSetMetaData resultSetMetaData;
    
    private final ShardingRule shardingRule;
    
    private final ShardingEncryptorEngine shardingEncryptorEngine;
    
    @SneakyThrows
    public QueryResultMetaData(final ResultSetMetaData resultSetMetaData, final ShardingRule shardingRule) {
        columnLabelAndIndexes = getColumnLabelAndIndexMap(resultSetMetaData);
        this.resultSetMetaData = resultSetMetaData;
        this.shardingRule = shardingRule;
        this.shardingEncryptorEngine = shardingRule.getEncryptRule().getEncryptorEngine();
    }
    
    @SneakyThrows
    public QueryResultMetaData(final ResultSetMetaData resultSetMetaData, final EncryptRule encryptRule) {
        columnLabelAndIndexes = getColumnLabelAndIndexMap(resultSetMetaData);
        this.resultSetMetaData = resultSetMetaData;
        this.shardingRule = null;
        this.shardingEncryptorEngine = encryptRule.getEncryptorEngine();
    }
    
    @SneakyThrows
    public QueryResultMetaData(final ResultSetMetaData resultSetMetaData) {
        columnLabelAndIndexes = getColumnLabelAndIndexMap(resultSetMetaData);
        this.resultSetMetaData = resultSetMetaData;
        this.shardingRule = null;
        this.shardingEncryptorEngine = new ShardingEncryptorEngine();
    }
    
    @SneakyThrows
    private Map<String, Integer> getColumnLabelAndIndexMap(final ResultSetMetaData resultSetMetaData) {
        Map<String, Integer> result = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (int columnIndex = resultSetMetaData.getColumnCount(); columnIndex > 0; columnIndex--) {
            result.put(resultSetMetaData.getColumnLabel(columnIndex), columnIndex);
        }
        return result;
    }
    
    /**
     * Get column count.
     * 
     * @return column count
     */
    @SneakyThrows
    public int getColumnCount() {
        return resultSetMetaData.getColumnCount();
    }
    
    /**
     * Get column label.
     * 
     * @param columnIndex column index
     * @return column label
     */
    @SneakyThrows
    public String getColumnLabel(final int columnIndex) {
        return resultSetMetaData.getColumnLabel(columnIndex);
    }
    
    /**
     * Get column name.
     * 
     * @param columnIndex column index
     * @return column name
     */
    @SneakyThrows
    public String getColumnName(final int columnIndex) {
        return resultSetMetaData.getColumnName(columnIndex);
    }
    
    /**
     * Get column index.
     * 
     * @param columnLabel column label
     * @return column name
     */
    public Integer getColumnIndex(final String columnLabel) {
        return columnLabelAndIndexes.get(columnLabel);
    }
    
    /**
     * Get sharding encryptor.
     * 
     * @param columnIndex column index
     * @return sharding encryptor optional
     */
    @SneakyThrows
    public Optional<ShardingEncryptor> getShardingEncryptor(final int columnIndex) {
        return shardingEncryptorEngine.getShardingEncryptor(getTableName(columnIndex), resultSetMetaData.getColumnName(columnIndex));
    }
    
    private String getTableName(final int columnIndex) throws SQLException {
        String actualTableName = resultSetMetaData.getTableName(columnIndex);
        if (null == shardingRule) {
            return actualTableName;
        }
        Optional<TableRule> tableRule = shardingRule.findTableRuleByActualTable(actualTableName);
        return tableRule.isPresent() ? tableRule.get().getLogicTable() : actualTableName;
    }
}
