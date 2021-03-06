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

package com.clinbrain.bd.shardingproxy.backend.communication.jdbc.wrapper;

import lombok.RequiredArgsConstructor;
import com.clinbrain.bd.core.SimpleQueryShardingEngine;
import com.clinbrain.bd.core.constant.DatabaseType;
import com.clinbrain.bd.core.constant.properties.ShardingPropertiesConstant;
import com.clinbrain.bd.core.optimize.OptimizeEngineFactory;
import com.clinbrain.bd.core.optimize.result.OptimizeResult;
import com.clinbrain.bd.core.parse.entry.MasterSlaveSQLParseEntry;
import com.clinbrain.bd.core.parse.sql.statement.SQLStatement;
import com.clinbrain.bd.core.rewrite.SQLRewriteEngine;
import com.clinbrain.bd.core.rewrite.rewriter.parameter.ParameterRewriter;
import com.clinbrain.bd.core.rewrite.rewriter.sql.EncryptSQLRewriter;
import com.clinbrain.bd.core.rewrite.rewriter.sql.SQLRewriter;
import com.clinbrain.bd.core.route.RouteUnit;
import com.clinbrain.bd.core.route.SQLRouteResult;
import com.clinbrain.bd.core.route.SQLUnit;
import com.clinbrain.bd.core.route.router.masterslave.MasterSlaveRouter;
import com.clinbrain.bd.shardingproxy.backend.schema.*;
import com.clinbrain.bd.shardingproxy.context.ShardingProxyContext;
import com.clinbrain.bd.spi.DatabaseTypes;
import com.clinbrain.bd.spi.DbType;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Executor wrapper for statement.
 *
 * @author zhangliang
 * @author pannjuan
 */
@RequiredArgsConstructor
public final class StatementExecutorWrapper implements JDBCExecutorWrapper {
    
    private static final ShardingProxyContext SHARDING_PROXY_CONTEXT = ShardingProxyContext.getInstance();
    
    private final LogicSchema logicSchema;
    
    @Override
    public SQLRouteResult route(final String sql, final DatabaseType databaseType) {
        if (logicSchema instanceof ShardingSchema) {
            return doShardingRoute(sql, DatabaseTypes.getActualDatabaseType(databaseType.name()));
        }
        if (logicSchema instanceof MasterSlaveSchema) {
            return doMasterSlaveRoute(sql);
        }
        if (logicSchema instanceof EncryptSchema) {
            return doEncryptRoute(sql);
        }
        return doTransparentRoute(sql,DatabaseTypes.getActualDatabaseType(databaseType.name()));
    }
    
    private SQLRouteResult doShardingRoute(final String sql, final DbType databaseType) {
        SimpleQueryShardingEngine shardingEngine = new SimpleQueryShardingEngine(logicSchema.getShardingRule(), 
                ShardingProxyContext.getInstance().getShardingProperties(), logicSchema.getMetaData(), databaseType, logicSchema.getParsingResultCache());
        return shardingEngine.shard(sql, Collections.emptyList());
    }
    
    private SQLRouteResult doMasterSlaveRoute(final String sql) {
        SQLStatement sqlStatement = ((MasterSlaveSchema) logicSchema).getParseEngine().parse(sql, false);
        SQLRewriteEngine sqlRewriteEngine = new SQLRewriteEngine(sqlStatement);
        sqlRewriteEngine.init(Collections.<ParameterRewriter>emptyList(), Collections.<SQLRewriter>emptyList());
        String rewriteSQL = sqlRewriteEngine.generateSQL().getSql();
        SQLRouteResult result = new SQLRouteResult(sqlStatement);
        for (String each : new MasterSlaveRouter(((MasterSlaveSchema) logicSchema).getMasterSlaveRule(), ((MasterSlaveSchema) logicSchema).getParseEngine(),
                SHARDING_PROXY_CONTEXT.getShardingProperties().<Boolean>getValue(ShardingPropertiesConstant.SQL_SHOW)).route(rewriteSQL, false)) {
            result.getRouteUnits().add(new RouteUnit(each, new SQLUnit(rewriteSQL, Collections.emptyList())));
        }
        return result;
    }
    
    private SQLRouteResult doEncryptRoute(final String sql) {
        EncryptSchema encryptSchema = (EncryptSchema) logicSchema;
        SQLStatement sqlStatement = encryptSchema.getParseEngine().parse(sql, false);
        SQLRewriteEngine sqlRewriteEngine = new SQLRewriteEngine(encryptSchema.getEncryptRule(), sqlStatement, Collections.emptyList());
        OptimizeResult optimizeResult = OptimizeEngineFactory.newInstance(encryptSchema.getEncryptRule(), sqlStatement, new LinkedList<>()).optimize();
        sqlRewriteEngine.init(Collections.<ParameterRewriter>emptyList(), 
                Collections.<SQLRewriter>singletonList(new EncryptSQLRewriter(encryptSchema.getEncryptRule().getEncryptorEngine(), sqlStatement, optimizeResult)));
        SQLRouteResult result = new SQLRouteResult(sqlStatement);
        result.getRouteUnits().add(new RouteUnit(logicSchema.getDataSources().keySet().iterator().next(), new SQLUnit(sqlRewriteEngine.generateSQL().getSql(), Collections.emptyList())));
        return result;
    }
    
    private SQLRouteResult doTransparentRoute(final String sql,final DbType databaseType) {
        //如果没有表名称的采用主从模式路由
        //new MasterSlaveSQLParseEntry(LogicSchemas.getInstance().getDatabaseType().name()).parse(sql,false);
        //SQLRouteResult result = new SQLRouteResult(((MasterSlaveSchema) logicSchema).getParseEngine().parse(sql, false));
        /*SQLRouteResult result = new SQLRouteResult(new MasterSlaveSQLParseEntry(LogicSchemas.getInstance().getDatabaseType().name()).parse(sql,false));
        result.getRouteUnits().add(new RouteUnit(logicSchema.getDataSources().keySet().iterator().next(), new SQLUnit(sql, Collections.emptyList())));
        return result;*/
        return doShardingRoute(sql,databaseType);
    }
    
    @Override
    public Statement createStatement(final Connection connection, final SQLUnit sqlUnit, final boolean isReturnGeneratedKeys) throws SQLException {
        return connection.createStatement();
    }
    
    @Override
    public boolean executeSQL(final Statement statement, final String sql, final boolean isReturnGeneratedKeys) throws SQLException {
        return statement.execute(sql, isReturnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);
    }
}
