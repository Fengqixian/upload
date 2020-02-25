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

package com.clinbrain.bd.core.merge;

import com.google.common.collect.Lists;
import com.clinbrain.bd.core.constant.DatabaseType;
import com.clinbrain.bd.core.execute.sql.execute.result.QueryResult;
import com.clinbrain.bd.core.merge.dal.DALMergeEngine;
import com.clinbrain.bd.core.merge.dql.DQLMergeEngine;
import com.clinbrain.bd.core.merge.fixture.TestQueryResult;
import com.clinbrain.bd.core.parse.sql.statement.dal.DALStatement;
import com.clinbrain.bd.core.parse.sql.statement.dml.InsertStatement;
import com.clinbrain.bd.core.parse.sql.statement.dml.SelectStatement;
import com.clinbrain.bd.core.route.SQLRouteResult;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class MergeEngineFactoryTest {
    
    private List<QueryResult> queryResults;
    
    @Before
    public void setUp() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData resultSetMetaData = mock(ResultSetMetaData.class);
        when(resultSet.getMetaData()).thenReturn(resultSetMetaData);
        when(resultSetMetaData.getColumnCount()).thenReturn(1);
        when(resultSetMetaData.getColumnLabel(1)).thenReturn("label");
        List<ResultSet> resultSets = Lists.newArrayList(resultSet);
        queryResults = new ArrayList<>(resultSets.size());
        queryResults.add(new TestQueryResult(resultSets.get(0)));
    }
    
    @Test
    public void assertNewInstanceWithSelectStatement() throws SQLException {
        SQLRouteResult routeResult = new SQLRouteResult(new SelectStatement());
        assertThat(MergeEngineFactory.newInstance(DatabaseType.MySQL, null, routeResult, null, queryResults), instanceOf(DQLMergeEngine.class));
    }
    
    @Test
    public void assertNewInstanceWithDALStatement() throws SQLException {
        SQLRouteResult routeResult = new SQLRouteResult(new DALStatement());
        assertThat(MergeEngineFactory.newInstance(DatabaseType.MySQL, null, routeResult, null, queryResults), instanceOf(DALMergeEngine.class));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void assertNewInstanceWithOtherStatement() throws SQLException {
        SQLRouteResult routeResult = new SQLRouteResult(new InsertStatement());
        MergeEngineFactory.newInstance(DatabaseType.MySQL, null, routeResult, null, queryResults);
    }
}
