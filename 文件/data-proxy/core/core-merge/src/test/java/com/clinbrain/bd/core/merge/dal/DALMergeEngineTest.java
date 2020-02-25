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

package com.clinbrain.bd.core.merge.dal;

import com.clinbrain.bd.core.execute.sql.execute.result.QueryResult;
import com.clinbrain.bd.core.merge.dal.show.ShowCreateTableMergedResult;
import com.clinbrain.bd.core.merge.dal.show.ShowDatabasesMergedResult;
import com.clinbrain.bd.core.merge.dal.show.ShowOtherMergedResult;
import com.clinbrain.bd.core.merge.dal.show.ShowTablesMergedResult;
import com.clinbrain.bd.core.merge.fixture.TestQueryResult;
import com.clinbrain.bd.core.parse.sql.statement.dal.DALStatement;
import com.clinbrain.bd.core.parse.sql.statement.dal.dialect.mysql.statement.ShowCreateTableStatement;
import com.clinbrain.bd.core.parse.sql.statement.dal.dialect.mysql.statement.ShowDatabasesStatement;
import com.clinbrain.bd.core.parse.sql.statement.dal.dialect.mysql.statement.ShowOtherStatement;
import com.clinbrain.bd.core.parse.sql.statement.dal.dialect.mysql.statement.ShowTablesStatement;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public final class DALMergeEngineTest {
    
    private List<QueryResult> queryResults;
    
    @Before
    public void setUp() {
        ResultSet resultSet = mock(ResultSet.class);
        queryResults = Collections.<QueryResult>singletonList(new TestQueryResult(resultSet));
    }
    
    @Test
    public void assertMergeForShowDatabasesStatement() throws SQLException {
        DALStatement dalStatement = new ShowDatabasesStatement();
        DALMergeEngine dalMergeEngine = new DALMergeEngine(null, queryResults, dalStatement, null);
        assertThat(dalMergeEngine.merge(), instanceOf(ShowDatabasesMergedResult.class));
    }
    
    @Test
    public void assertMergeForShowShowTablesStatement() throws SQLException {
        DALStatement dalStatement = new ShowTablesStatement();
        DALMergeEngine dalMergeEngine = new DALMergeEngine(null, queryResults, dalStatement, null);
        assertThat(dalMergeEngine.merge(), instanceOf(ShowTablesMergedResult.class));
    }
    
    @Test
    public void assertMergeForShowCreateTableStatement() throws SQLException {
        DALStatement dalStatement = new ShowCreateTableStatement();
        DALMergeEngine dalMergeEngine = new DALMergeEngine(null, queryResults, dalStatement, null);
        assertThat(dalMergeEngine.merge(), instanceOf(ShowCreateTableMergedResult.class));
    }
    
    @Test
    public void assertMergeForShowOtherStatement() throws SQLException {
        DALStatement dalStatement = new ShowOtherStatement();
        DALMergeEngine dalMergeEngine = new DALMergeEngine(null, queryResults, dalStatement, null);
        assertThat(dalMergeEngine.merge(), instanceOf(ShowOtherMergedResult.class));
    }
}
