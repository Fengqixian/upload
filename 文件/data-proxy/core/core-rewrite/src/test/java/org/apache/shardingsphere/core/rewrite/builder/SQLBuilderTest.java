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

package com.clinbrain.bd.core.rewrite.builder;

import com.clinbrain.bd.api.config.sharding.ShardingRuleConfiguration;
import com.clinbrain.bd.api.config.sharding.TableRuleConfiguration;
import com.clinbrain.bd.core.parse.constant.QuoteCharacter;
import com.clinbrain.bd.core.rewrite.placeholder.IndexPlaceholder;
import com.clinbrain.bd.core.rewrite.placeholder.TablePlaceholder;
import com.clinbrain.bd.core.rule.ShardingRule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public final class SQLBuilderTest {
    
    @Test
    public void assertAppendLiteralsOnly() {
        SQLBuilder sqlBuilder = new SQLBuilder();
        sqlBuilder.appendLiterals("SELECT ");
        sqlBuilder.appendLiterals("table_x");
        sqlBuilder.appendLiterals(".id");
        sqlBuilder.appendLiterals(" FROM ");
        sqlBuilder.appendLiterals("table_x");
        assertThat(sqlBuilder.toSQL(null, Collections.<String, String>emptyMap()), is("SELECT table_x.id FROM table_x"));
    }
    
    @Test
    public void assertAppendTableWithoutTableToken() {
        SQLBuilder sqlBuilder = new SQLBuilder();
        sqlBuilder.appendLiterals("SELECT ");
        sqlBuilder.appendPlaceholder(new TablePlaceholder("table_x", QuoteCharacter.NONE));
        sqlBuilder.appendLiterals(".id");
        sqlBuilder.appendLiterals(" FROM ");
        sqlBuilder.appendPlaceholder(new TablePlaceholder("table_x", QuoteCharacter.NONE));
        assertThat(sqlBuilder.toSQL(null, Collections.<String, String>emptyMap()), is("SELECT table_x.id FROM table_x"));
    }
    
    @Test
    public void assertAppendTableWithTableToken() {
        SQLBuilder sqlBuilder = new SQLBuilder();
        sqlBuilder.appendLiterals("SELECT ");
        sqlBuilder.appendPlaceholder(new TablePlaceholder("table_x", QuoteCharacter.NONE));
        sqlBuilder.appendLiterals(".id");
        sqlBuilder.appendLiterals(" FROM ");
        sqlBuilder.appendPlaceholder(new TablePlaceholder("table_x", QuoteCharacter.NONE));
        Map<String, String> tableTokens = new HashMap<>(1, 1);
        tableTokens.put("table_x", "table_x_1");
        assertThat(sqlBuilder.toSQL(null, tableTokens), is("SELECT table_x_1.id FROM table_x_1"));
    }
    
    @Test
    public void assertIndexPlaceholderAppendTableWithoutTableToken() {
        SQLBuilder sqlBuilder = new SQLBuilder();
        sqlBuilder.appendLiterals("CREATE INDEX ");
        sqlBuilder.appendPlaceholder(new IndexPlaceholder("index_name", "index_name", QuoteCharacter.NONE));
        sqlBuilder.appendLiterals(" ON ");
        sqlBuilder.appendPlaceholder(new TablePlaceholder("table_x", QuoteCharacter.NONE));
        sqlBuilder.appendLiterals(" ('column')");
        assertThat(sqlBuilder.toSQL(null, Collections.<String, String>emptyMap()), is("CREATE INDEX index_name ON table_x ('column')"));
    }
    
    @Test
    public void assertIndexPlaceholderAppendTableWithTableToken() {
        SQLBuilder sqlBuilder = new SQLBuilder();
        sqlBuilder.appendLiterals("CREATE INDEX ");
        sqlBuilder.appendPlaceholder(new IndexPlaceholder("index_name", "table_x", QuoteCharacter.NONE));
        sqlBuilder.appendLiterals(" ON ");
        sqlBuilder.appendPlaceholder(new TablePlaceholder("table_x", QuoteCharacter.NONE));
        sqlBuilder.appendLiterals(" ('column')");
        Map<String, String> tableTokens = new HashMap<>(1, 1);
        tableTokens.put("table_x", "table_x_1");
        assertThat(sqlBuilder.toSQL(null, tableTokens), is("CREATE INDEX index_name_table_x_1 ON table_x_1 ('column')"));
    }
    
    @Test
    public void assertAppendTableWithoutTableTokenWithBackQuotes() {
        SQLBuilder sqlBuilder = new SQLBuilder();
        sqlBuilder.appendLiterals("SELECT ");
        sqlBuilder.appendPlaceholder(new TablePlaceholder("table_x", QuoteCharacter.BACK_QUOTE));
        sqlBuilder.appendLiterals(".id");
        sqlBuilder.appendLiterals(" FROM ");
        sqlBuilder.appendPlaceholder(new TablePlaceholder("table_x", QuoteCharacter.BACK_QUOTE));
        assertThat(sqlBuilder.toSQL(null, Collections.<String, String>emptyMap()), is("SELECT `table_x`.id FROM `table_x`"));
    }
    
    @Test
    public void assertAppendTableWithTableTokenWithBackQuotes() {
        SQLBuilder sqlBuilder = new SQLBuilder();
        sqlBuilder.appendLiterals("SELECT ");
        sqlBuilder.appendPlaceholder(new TablePlaceholder("table_x", QuoteCharacter.BACK_QUOTE));
        sqlBuilder.appendLiterals(".id");
        sqlBuilder.appendLiterals(" FROM ");
        sqlBuilder.appendPlaceholder(new TablePlaceholder("table_x", QuoteCharacter.BACK_QUOTE));
        Map<String, String> tableTokens = new HashMap<>(1, 1);
        tableTokens.put("table_x", "table_x_1");
        assertThat(sqlBuilder.toSQL(null, tableTokens), is("SELECT `table_x_1`.id FROM `table_x_1`"));
    }
    
    @Test
    public void assertIndexPlaceholderAppendTableWithoutTableTokenWithBackQuotes() {
        SQLBuilder sqlBuilder = new SQLBuilder();
        sqlBuilder.appendLiterals("CREATE INDEX ");
        sqlBuilder.appendPlaceholder(new IndexPlaceholder("index_name", "index_name", QuoteCharacter.NONE));
        sqlBuilder.appendLiterals(" ON ");
        sqlBuilder.appendPlaceholder(new TablePlaceholder("table_x", QuoteCharacter.BACK_QUOTE));
        sqlBuilder.appendLiterals(" ('column')");
        assertThat(sqlBuilder.toSQL(null, Collections.<String, String>emptyMap()), is("CREATE INDEX index_name ON `table_x` ('column')"));
    }
    
    @Test
    public void assertIndexPlaceholderAppendTableWithTableTokenWithBackQuotes() {
        SQLBuilder sqlBuilder = new SQLBuilder();
        sqlBuilder.appendLiterals("CREATE INDEX ");
        sqlBuilder.appendPlaceholder(new IndexPlaceholder("index_name", "table_x", QuoteCharacter.NONE));
        sqlBuilder.appendLiterals(" ON ");
        sqlBuilder.appendPlaceholder(new TablePlaceholder("table_x", QuoteCharacter.BACK_QUOTE));
        sqlBuilder.appendLiterals(" ('column')");
        Map<String, String> tableTokens = new HashMap<>(1, 1);
        tableTokens.put("table_x", "table_x_1");
        assertThat(sqlBuilder.toSQL(null, tableTokens), is("CREATE INDEX index_name_table_x_1 ON `table_x_1` ('column')"));
    }
    
    @Test
    public void assertAppendTableWithoutTableTokenWithDoubleQuotes() {
        SQLBuilder sqlBuilder = new SQLBuilder();
        sqlBuilder.appendLiterals("SELECT ");
        sqlBuilder.appendPlaceholder(new TablePlaceholder("table_x", QuoteCharacter.QUOTE));
        sqlBuilder.appendLiterals(".id");
        sqlBuilder.appendLiterals(" FROM ");
        sqlBuilder.appendPlaceholder(new TablePlaceholder("table_x", QuoteCharacter.QUOTE));
        assertThat(sqlBuilder.toSQL(null, Collections.<String, String>emptyMap()), is("SELECT \"table_x\".id FROM \"table_x\""));
    }
    
    @Test
    public void assertAppendTableWithTableTokenWithDoubleQuotes() {
        SQLBuilder sqlBuilder = new SQLBuilder();
        sqlBuilder.appendLiterals("SELECT ");
        sqlBuilder.appendPlaceholder(new TablePlaceholder("table_x", QuoteCharacter.QUOTE));
        sqlBuilder.appendLiterals(".id");
        sqlBuilder.appendLiterals(" FROM ");
        sqlBuilder.appendPlaceholder(new TablePlaceholder("table_x", QuoteCharacter.QUOTE));
        Map<String, String> tableTokens = new HashMap<>(1, 1);
        tableTokens.put("table_x", "table_x_1");
        assertThat(sqlBuilder.toSQL(null, tableTokens), is("SELECT \"table_x_1\".id FROM \"table_x_1\""));
    }
    
    @Test
    public void assertIndexPlaceholderAppendTableWithoutTableTokenWithDoubleQuotes() {
        SQLBuilder sqlBuilder = new SQLBuilder();
        sqlBuilder.appendLiterals("CREATE INDEX ");
        sqlBuilder.appendPlaceholder(new IndexPlaceholder("index_name", "index_name", QuoteCharacter.NONE));
        sqlBuilder.appendLiterals(" ON ");
        sqlBuilder.appendPlaceholder(new TablePlaceholder("table_x", QuoteCharacter.QUOTE));
        sqlBuilder.appendLiterals(" ('column')");
        assertThat(sqlBuilder.toSQL(null, Collections.<String, String>emptyMap()), is("CREATE INDEX index_name ON \"table_x\" ('column')"));
    }
    
    @Test
    public void assertIndexPlaceholderAppendTableWithTableTokenWithDoubleQuotes() {
        SQLBuilder sqlBuilder = new SQLBuilder();
        sqlBuilder.appendLiterals("CREATE INDEX ");
        sqlBuilder.appendPlaceholder(new IndexPlaceholder("index_name", "table_x", QuoteCharacter.NONE));
        sqlBuilder.appendLiterals(" ON ");
        sqlBuilder.appendPlaceholder(new TablePlaceholder("table_x", QuoteCharacter.QUOTE));
        sqlBuilder.appendLiterals(" ('column')");
        Map<String, String> tableTokens = new HashMap<>(1, 1);
        tableTokens.put("table_x", "table_x_1");
        assertThat(sqlBuilder.toSQL(null, tableTokens), is("CREATE INDEX index_name_table_x_1 ON \"table_x_1\" ('column')"));
    }
    
    @Test
    public void assertShardingPlaceholderToString() {
        assertThat(new IndexPlaceholder("index_name", "table_x", QuoteCharacter.NONE).toString(null, Collections.<String, String>emptyMap()), is("index_name"));
        assertThat(new TablePlaceholder("table_name", QuoteCharacter.BACK_QUOTE).toString(null, Collections.<String, String>emptyMap()), is("`table_name`"));
    }
    
    private ShardingRule createShardingRule() {
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        TableRuleConfiguration tableRuleConfig = new TableRuleConfiguration("LOGIC_TABLE", "ds${0..1}.table_${0..2}");
        shardingRuleConfig.getTableRuleConfigs().add(tableRuleConfig);
        return new ShardingRule(shardingRuleConfig, createDataSourceNames());
    }
    
    private Collection<String> createDataSourceNames() {
        return Arrays.asList("ds0", "ds1");
    }
}
