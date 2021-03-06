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

package com.clinbrain.bd.opentracing.hook;

import io.opentracing.ActiveSpan;
import io.opentracing.ActiveSpan.Continuation;
import io.opentracing.mock.MockSpan;
import io.opentracing.tag.Tags;
import com.clinbrain.bd.core.execute.ShardingExecuteDataMap;
import com.clinbrain.bd.core.execute.hook.SPISQLExecutionHook;
import com.clinbrain.bd.core.execute.hook.SQLExecutionHook;
import com.clinbrain.bd.core.metadata.datasource.DataSourceMetaData;
import com.clinbrain.bd.core.route.RouteUnit;
import com.clinbrain.bd.core.route.SQLUnit;
import com.clinbrain.bd.core.spi.NewInstanceServiceLoader;
import com.clinbrain.bd.opentracing.constant.ShardingTags;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public final class OpenTracingSQLExecutionHookTest extends BaseOpenTracingHookTest {
    
    private final SQLExecutionHook sqlExecutionHook = new SPISQLExecutionHook();
    
    private ActiveSpan activeSpan;
    
    @BeforeClass
    public static void registerSPI() {
        NewInstanceServiceLoader.register(SQLExecutionHook.class);
    }
    
    @Before
    public void setUp() {
        activeSpan = mockActiveSpan();
    }
    
    private ActiveSpan mockActiveSpan() {
        Continuation continuation = mock(Continuation.class);
        ActiveSpan result = mock(ActiveSpan.class);
        when(continuation.activate()).thenReturn(result);
        ShardingExecuteDataMap.getDataMap().put(OpenTracingRootInvokeHook.ACTIVE_SPAN_CONTINUATION, continuation);
        return result;
    }
    
    @After
    public void tearDown() {
        ShardingExecuteDataMap.getDataMap().remove(OpenTracingRootInvokeHook.ACTIVE_SPAN_CONTINUATION);
    }
    
    @Test
    public void assertExecuteSuccessForTrunkThread() {
        DataSourceMetaData dataSourceMetaData = mock(DataSourceMetaData.class);
        when(dataSourceMetaData.getHostName()).thenReturn("localhost");
        when(dataSourceMetaData.getPort()).thenReturn(8888);
        sqlExecutionHook.start(createRouteUnit("success_ds", "SELECT * FROM success_tbl;", Arrays.<Object>asList("1", 2)), dataSourceMetaData, true, null);
        sqlExecutionHook.finishSuccess();
        MockSpan actual = getActualSpan();
        assertThat(actual.operationName(), is("/Sharding-Sphere/executeSQL/"));
        Map<String, Object> actualTags = actual.tags();
        assertThat(actualTags.get(Tags.COMPONENT.getKey()), CoreMatchers.<Object>is(ShardingTags.COMPONENT_NAME));
        assertThat(actualTags.get(Tags.SPAN_KIND.getKey()), CoreMatchers.<Object>is(Tags.SPAN_KIND_CLIENT));
        assertThat(actualTags.get(Tags.PEER_HOSTNAME.getKey()), CoreMatchers.<Object>is("localhost"));
        assertThat(actualTags.get(Tags.PEER_PORT.getKey()), CoreMatchers.<Object>is(8888));
        assertThat(actualTags.get(Tags.DB_TYPE.getKey()), CoreMatchers.<Object>is("sql"));
        assertThat(actualTags.get(Tags.DB_INSTANCE.getKey()), CoreMatchers.<Object>is("success_ds"));
        assertThat(actualTags.get(Tags.DB_STATEMENT.getKey()), CoreMatchers.<Object>is("SELECT * FROM success_tbl;"));
        assertThat(actualTags.get(ShardingTags.DB_BIND_VARIABLES.getKey()), CoreMatchers.<Object>is("[1, 2]"));
        verify(activeSpan, times(0)).deactivate();
        sqlExecutionHook.start(createRouteUnit("success_ds", "SELECT * FROM success_tbl;", null), dataSourceMetaData, true, null);
        sqlExecutionHook.finishSuccess();
    }

    @Test
    public void assertExecuteSuccessForTrunkThreadWhenParamsIsNull() {
        DataSourceMetaData dataSourceMetaData = mock(DataSourceMetaData.class);
        when(dataSourceMetaData.getHostName()).thenReturn("localhost");
        when(dataSourceMetaData.getPort()).thenReturn(8888);
        sqlExecutionHook.start(createRouteUnit("success_ds", "SELECT * FROM success_tbl;", null), dataSourceMetaData, true, null);
        sqlExecutionHook.finishSuccess();
        MockSpan actual = getActualSpan();
        assertThat(actual.operationName(), is("/Sharding-Sphere/executeSQL/"));
        Map<String, Object> actualTags = actual.tags();
        assertThat(actualTags.get(Tags.COMPONENT.getKey()), CoreMatchers.<Object>is(ShardingTags.COMPONENT_NAME));
        assertThat(actualTags.get(Tags.SPAN_KIND.getKey()), CoreMatchers.<Object>is(Tags.SPAN_KIND_CLIENT));
        assertThat(actualTags.get(Tags.PEER_HOSTNAME.getKey()), CoreMatchers.<Object>is("localhost"));
        assertThat(actualTags.get(Tags.PEER_PORT.getKey()), CoreMatchers.<Object>is(8888));
        assertThat(actualTags.get(Tags.DB_TYPE.getKey()), CoreMatchers.<Object>is("sql"));
        assertThat(actualTags.get(Tags.DB_INSTANCE.getKey()), CoreMatchers.<Object>is("success_ds"));
        assertThat(actualTags.get(Tags.DB_STATEMENT.getKey()), CoreMatchers.<Object>is("SELECT * FROM success_tbl;"));
        assertThat(actualTags.get(ShardingTags.DB_BIND_VARIABLES.getKey()), CoreMatchers.<Object>is(""));
        verify(activeSpan, times(0)).deactivate();
    }
    
    @Test
    public void assertExecuteSuccessForBranchThread() {
        DataSourceMetaData dataSourceMetaData = mock(DataSourceMetaData.class);
        when(dataSourceMetaData.getHostName()).thenReturn("localhost");
        when(dataSourceMetaData.getPort()).thenReturn(8888);
        sqlExecutionHook.start(
                createRouteUnit("success_ds", "SELECT * FROM success_tbl;", Arrays.<Object>asList("1", 2)), dataSourceMetaData, false, ShardingExecuteDataMap.getDataMap());
        sqlExecutionHook.finishSuccess();
        MockSpan actual = getActualSpan();
        assertThat(actual.operationName(), is("/Sharding-Sphere/executeSQL/"));
        Map<String, Object> actualTags = actual.tags();
        assertThat(actualTags.get(Tags.COMPONENT.getKey()), CoreMatchers.<Object>is(ShardingTags.COMPONENT_NAME));
        assertThat(actualTags.get(Tags.SPAN_KIND.getKey()), CoreMatchers.<Object>is(Tags.SPAN_KIND_CLIENT));
        assertThat(actualTags.get(Tags.PEER_HOSTNAME.getKey()), CoreMatchers.<Object>is("localhost"));
        assertThat(actualTags.get(Tags.PEER_PORT.getKey()), CoreMatchers.<Object>is(8888));
        assertThat(actualTags.get(Tags.DB_TYPE.getKey()), CoreMatchers.<Object>is("sql"));
        assertThat(actualTags.get(Tags.DB_INSTANCE.getKey()), CoreMatchers.<Object>is("success_ds"));
        assertThat(actualTags.get(Tags.DB_STATEMENT.getKey()), CoreMatchers.<Object>is("SELECT * FROM success_tbl;"));
        assertThat(actualTags.get(ShardingTags.DB_BIND_VARIABLES.getKey()), CoreMatchers.<Object>is("[1, 2]"));
        verify(activeSpan).deactivate();
    }
    
    @Test
    public void assertExecuteFailure() {
        DataSourceMetaData dataSourceMetaData = mock(DataSourceMetaData.class);
        when(dataSourceMetaData.getHostName()).thenReturn("localhost");
        when(dataSourceMetaData.getPort()).thenReturn(8888);
        sqlExecutionHook.start(createRouteUnit("failure_ds", "SELECT * FROM failure_tbl;", Collections.emptyList()), dataSourceMetaData, true, null);
        sqlExecutionHook.finishFailure(new RuntimeException("SQL execution error"));
        MockSpan actual = getActualSpan();
        assertThat(actual.operationName(), is("/Sharding-Sphere/executeSQL/"));
        Map<String, Object> actualTags = actual.tags();
        assertThat(actualTags.get(Tags.COMPONENT.getKey()), CoreMatchers.<Object>is(ShardingTags.COMPONENT_NAME));
        assertThat(actualTags.get(Tags.SPAN_KIND.getKey()), CoreMatchers.<Object>is(Tags.SPAN_KIND_CLIENT));
        assertThat(actualTags.get(Tags.PEER_HOSTNAME.getKey()), CoreMatchers.<Object>is("localhost"));
        assertThat(actualTags.get(Tags.PEER_PORT.getKey()), CoreMatchers.<Object>is(8888));
        assertThat(actualTags.get(Tags.DB_TYPE.getKey()), CoreMatchers.<Object>is("sql"));
        assertThat(actualTags.get(Tags.DB_INSTANCE.getKey()), CoreMatchers.<Object>is("failure_ds"));
        assertThat(actualTags.get(Tags.DB_STATEMENT.getKey()), CoreMatchers.<Object>is("SELECT * FROM failure_tbl;"));
        assertThat(actualTags.get(ShardingTags.DB_BIND_VARIABLES.getKey()), CoreMatchers.<Object>is(""));
        assertSpanError(RuntimeException.class, "SQL execution error");
        verify(activeSpan, times(0)).deactivate();
    }
    
    private RouteUnit createRouteUnit(final String dataSourceName, final String sql, final List<Object> parameters) {
        SQLUnit sqlUnit = new SQLUnit(sql, parameters);
        return new RouteUnit(dataSourceName, sqlUnit);
    }
}
