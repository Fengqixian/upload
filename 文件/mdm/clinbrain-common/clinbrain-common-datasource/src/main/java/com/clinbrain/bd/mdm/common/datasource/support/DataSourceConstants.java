package com.clinbrain.bd.mdm.common.datasource.support;

/**
 * @author lianglele
 * @date 2019-12-26 10:24
 * 数据源相关常量
 */
public interface DataSourceConstants {
    /**
     * 查询数据源的SQL
     */
    String QUERY_DS_SQL = "SELECT *  FROM mdm_technology_database";

    /**
     * 动态路由KEY
     */
    String DS_ROUTE_KEY = "id";

    /**
     * 数据源名称
     */
    String DS_NAME = "name_en";

    /**
     * jdbcurl IP
     */
    String DS_JDBC_IP = "connect_ip";

    /**
     * PROT
     */
    String DS_JDBC_PROT = "connect_host";

    /**
     * 用户名
     */
    String DS_USER_NAME = "connect_user";

    /**
     * 密码
     */
    String DS_USER_PWD = "connect_password";
    /**
     * 数据库类型
     */
    String DS_DB_TYPE = "database_type";

}