/*
 *  Copyright (c) 2019-2020, 冷冷 (wangiegie@gmail.com).
 *  <p>
 *  Licensed under the GNU Lesser General Public License 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  <p>
 * https://www.gnu.org/licenses/lgpl.html
 *  <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.clinbrain.bd.mdm.common.core.datascope;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.handlers.AbstractSqlParserHandler;
import com.clinbrain.bd.mdm.common.core.constant.SecurityConstants;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * @author lengleng
 * @date 2019/2/1
 * <p>
 * mybatis 数据权限拦截器
 */
@Slf4j
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class DataScopeInterceptor extends AbstractSqlParserHandler implements Interceptor {

    @Override
    @SneakyThrows
    public Object intercept(Invocation invocation) {
        StatementHandler statementHandler = (StatementHandler) PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        this.sqlParser(metaObject);
        // 先判断是不是SELECT操作
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        if (!SqlCommandType.SELECT.equals(mappedStatement.getSqlCommandType())) {
            return invocation.proceed();
        }

        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        String originalSql = boundSql.getSql();
        Object parameterObject = boundSql.getParameterObject();

        //查找参数中包含DataScope类型的参数
        DataScope dataScope = findDataScopeObject(parameterObject);
        //查找参数中包含DataScope类型的参数
        SeourceDataScope sdataScope = findSeourceDataScopeObject(parameterObject);

        if (dataScope == null && sdataScope == null) {
            return invocation.proceed();
        } else if (sdataScope != null && dataScope == null) {
            String scopeName = sdataScope.getScopeName();
            List<Integer> roleIds = sdataScope.getRoleIds();
            if (StrUtil.isNotBlank(scopeName) && CollectionUtil.isNotEmpty(roleIds)) {
                String join = CollectionUtil.join(roleIds, ",");
                originalSql = "select * from (" + originalSql + ") temp_data_scope where resource_id in(select resource_id from mdm_role_resource_permission where " + scopeName + " in (" + join + "))";
                metaObject.setValue("delegate.boundSql.sql", originalSql);
            }
            return invocation.proceed();
        } else if (dataScope != null && sdataScope == null) {
            String scopeName = dataScope.getScopeName();
            List<Integer> deptIds = dataScope.getDeptIds();
            if (StrUtil.isNotBlank(scopeName) && CollectionUtil.isNotEmpty(deptIds)) {
                String join = CollectionUtil.join(deptIds, ",");
                originalSql = "select * from (" + originalSql + ") temp_data_scope where temp_data_scope." + scopeName + " in (" + join + ")";
                metaObject.setValue("delegate.boundSql.sql", originalSql);
            }
            return invocation.proceed();
        } else {
            String scopeName = dataScope.getScopeName();
            List<Integer> deptIds = dataScope.getDeptIds();
            if (StrUtil.isNotBlank(scopeName) && CollectionUtil.isNotEmpty(deptIds)) {
                String join = CollectionUtil.join(deptIds, ",");
                originalSql = "select * from (" + originalSql + ") temp_data_scope where temp_data_scope." + scopeName + " in (" + join + ")";
                metaObject.setValue("delegate.boundSql.sql", originalSql);
            }

            String sscopeName = sdataScope.getScopeName();
            List<Integer> roleIds = sdataScope.getRoleIds();
            if (StrUtil.isNotBlank(scopeName) && CollectionUtil.isNotEmpty(deptIds)) {
                String join = CollectionUtil.join(roleIds, ",");
                originalSql = "select * from (" + originalSql + ") temp_data_scope where resource_id in(select resource_id from mdm_role_resource_permission where " + sscopeName + " in (" + join + "))";
                metaObject.setValue("delegate.boundSql.sql", originalSql);
            }
            return invocation.proceed();
        }

    }

    /**
     * 生成拦截对象的代理
     *
     * @param target 目标对象
     * @return 代理对象
     */
    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    /**
     * mybatis配置的属性
     *
     * @param properties mybatis配置的属性
     */
    @Override
    public void setProperties(Properties properties) {

    }

    /**
     * 查找参数是否包括DataScope对象
     *
     * @param parameterObj 参数列表
     * @return DataScope
     */
    private DataScope findDataScopeObject(Object parameterObj) {
        if (parameterObj instanceof DataScope) {
            return (DataScope) parameterObj;
        } else if (parameterObj instanceof Map) {
            for (Object val : ((Map<?, ?>) parameterObj).values()) {
                if (val instanceof DataScope) {
                    return (DataScope) val;
                }
            }
        }
        return null;
    }

    /**
     * 查找参数是否包括SeourceDataScope对象
     *
     * @param parameterObj 参数列表
     * @return DataScope
     */
    private SeourceDataScope findSeourceDataScopeObject(Object parameterObj) {
        if (parameterObj instanceof SeourceDataScope) {
            return (SeourceDataScope) parameterObj;
        } else if (parameterObj instanceof Map) {
            SeourceDataScope scope = new SeourceDataScope();
            for (Object key : ((Map<?, ?>) parameterObj).keySet()) {
                if (key.equals("roleIds")) {
                    scope.setRoleIds((List<Integer>) ((Map) parameterObj).get(key));
                    scope.setIsOnly(true);
                }
            }
            if (scope != null && scope.getRoleIds() != null && scope.getRoleIds().size() > 0) {
                return scope;
            }
        }
        return null;
    }

}
