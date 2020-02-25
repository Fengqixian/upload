package com.clinbrain.bd.mdm.common.core.util;

import com.clinbrain.bd.mdm.common.core.factory.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.*;

/**
 * 获取外部数据库元模型信息
 */
@Slf4j
public class DataJDBCUtil {

    /**
     * 数据库类型,枚举
     */
    public enum DataBaseType {
        oracle,
        mysql,
        sqlserver,
    }

    //数据库连接参数集合
    public Map<String, Object> param = new HashMap<String, Object>();

    //数据库连接
    private static Connection conn = null;


    /**
     * 获取数据库连接结果
     *
     * @param param 参数集合
     * @return
     */
    public static boolean getConnectionResult(Map<String, Object> param) {
        boolean result;

        try {
            conn = getConnections(param);
            result = conn != null ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("getConnectionResult_获取数据库连接信息出错", e);
            result = false;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * 获取数据库信息
     *
     * @param param 参数集合
     * @return
     */
    public List<Map<String, Object>> getDataBaseInfo(Map<String, Object> param) {
        return null;
    }


    /**
     * 获取一个表的信息
     *
     * @param param
     * @param tableName
     * @return
     * @author yjt
     * @date 2019/1/3 15:18
     */
    public static List<Map<String, Object>> getTableInfo(Map<String, Object> param, String tableName) {
        try {
            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
            DatabaseMetaData dbmd;

            conn = getConnections(param);
            dbmd = conn.getMetaData();

            ResultSet rs = dbmd.getTables(null, "%", tableName, new String[]{"TABLE"});

            String serverType = param.get("database_type") != null ? param.get("database_type").toString() : "";
            String databaseName = param.get("database_name") != null ? param.get("database_name").toString() : "";
            while (rs.next()) {
                Map m = new HashMap();

                if (serverType.equalsIgnoreCase("sqlserver")) {
                    String tableSchem = rs.getString("TABLE_SCHEM");
                    if (!tableSchem.equalsIgnoreCase("dbo")) {
                        continue;
                    }
                }
                String value = rs.getString("TABLE_NAME") != null ? rs.getString("TABLE_NAME") : "";
                m.put("table_name", value);
                result.add(m);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("getTableInfo_获取表信息集合出错", e);
            return null;
        }
    }


    /**
     * 获取表的主键集合
     *
     * @param param     参数
     * @param tableName 表名称
     * @return
     */
    public List<Map<String, Object>> getTablePrimaryKeysInfo(Map<String, Object> param, String tableName, List<Map<String, Object>> metaModelList) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        DatabaseMetaData dbmd = null;

        try {

            if (metaModelList == null || metaModelList.size() < 1) {
                return null;
            }

            conn = getConnections(param);
            dbmd = conn.getMetaData();

            //获取表的主键
            ResultSet rs = dbmd.getPrimaryKeys(null, null, tableName);

            while (rs.next()) {

                Map m = new HashMap();

                for (Map<String, Object> map : metaModelList) {

                    String attName = map.get("att_name_en") != null ? map.get("att_name_en").toString() : "";
                    try {
                        String value = rs.getString(attName.toUpperCase()) != null ? rs.getString(attName.toUpperCase()) : "";

                        m.put(attName, value);

                    } catch (Exception e) {
                    }
                }
                result.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("getTablePrimaryKeysInfo_获取表主键集合出错", e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * 获取标的外键集合
     *
     * @param param     参数
     * @param tableName 表名称
     * @return
     */
    public List<Map<String, Object>> getTableForeignKeysInfo(Map<String, Object> param, String tableName, List<Map<String, Object>> metaModelList) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        DatabaseMetaData dbmd = null;
        try {

            if (metaModelList == null || metaModelList.size() < 1) {
                return null;
            }

            conn = getConnections(param);
            dbmd = conn.getMetaData();

            ResultSet rs = dbmd.getImportedKeys(null, null, tableName);
            while (rs.next()) {
                Map m = new HashMap();

                for (Map<String, Object> map : metaModelList) {

                    String attName = map.get("att_name_en") != null ? map.get("att_name_en").toString() : "";
                    try {
                        String value = rs.getString(attName.toUpperCase()) != null ? rs.getString(attName.toUpperCase()) : "";

                        m.put(attName, value);

                    } catch (Exception e) {

                    }
                }
                result.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("getTableForeignKeysInfo_获取表外键集合出错", e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 获取索引属性信息
     *
     * @param param
     * @param tableName
     * @return
     */
    public List<Map<String, Object>> getTableIndexInfo(Map<String, Object> param, String tableName, List<Map<String, Object>> metaModelList) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        DatabaseMetaData dbmd = null;
        try {
            if (metaModelList == null || metaModelList.size() < 1) {
                return null;
            }

            conn = getConnections(param);
            dbmd = conn.getMetaData();
            ResultSet rs = dbmd.getIndexInfo(null, null, tableName, false, true);

            while (rs.next()) {
                Map m = new HashMap();

                //排除空的索引
                try {
                    String name = rs.getString("INDEX_NAME") != null ? rs.getString("INDEX_NAME") : "";
                    String column = rs.getString("COLUMN_NAME") != null ? rs.getString("COLUMN_NAME") : "";
                    if (name.equalsIgnoreCase("") && column.equalsIgnoreCase("")) {
                        continue;
                    }
                } catch (Exception e) {
                }

                for (Map<String, Object> map : metaModelList) {
                    String attName = map.get("att_name_en") != null ? map.get("att_name_en").toString() : "";
                    try {
                        String value = rs.getString(attName.toUpperCase()) != null ? rs.getString(attName.toUpperCase()) : "";
                        m.put(attName, value);
                    } catch (Exception e) {
                    }
                }
                result.add(m);
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("getTableIndexInfo_获取表索引集合出错", e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 获取某一列的描述
     *
     * @param dbName    数据库名
     * @param tableName 表名
     * @return
     * @author yjt
     * @date 2018/12/17 15:44
     */
    public static Map<String, Object> getColumnRemraks(String dbName, String tableName) {
        String sql = " select columnname= convert(varchar(100), a.name)," +
                " tablename= convert(varchar(50), d.name )," +
                " type= convert(varchar(50),b.name)," +
                " dbname= '" + dbName + "'," +
                " remarks=convert(varchar(50), isnull(g.[value],''))" +
                " from dbo.syscolumns a" +
                " left join dbo.systypes b on a.xusertype=b.xusertype" +
                " inner join dbo.sysobjects d on a.id=d.id and d.xtype='U' and d.name<>'dtproperties'" +
                " left join sys.extended_properties g on a.id=g.major_id and a.colid=g.minor_id" +
                " where d.name ='" + tableName + "'";
        ResultSet res = null;
        PreparedStatement statement = null;
        Map<String, Object> columns = new HashMap<>();
        try {

            if (dbName.equalsIgnoreCase("") || tableName.equalsIgnoreCase("")) {
                return null;
            }

            statement = conn.prepareStatement(sql);
            res = statement.executeQuery();
            while (res.next()) {
                String columnname = res.getString("columnname");
                String remark = res.getString("remarks");//获取test_name列的元素
                columns.put(columnname, remark);
            }

            return columns;
        } catch (Exception e) {
            log.error("获取列描述出错。", e);
            return null;
        } finally {
            try {
                if (res != null) res.close();
                if (statement != null) statement.close();
                //if(conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据数据源信息和查询条件获取数据值
     *
     * @return
     * @author yjt
     * @date 2018/12/20 10:49
     */
    public List<Map<String, Object>> getMetaDataFromDB(Map<String, Object> dbSource, Map<String, Object> param, String aggregation_function) {

        ResultSet res = null;
        PreparedStatement statement = null;
        List<Map<String, Object>> values = new ArrayList<>();
        try {
            dbSource = setDriverType(dbSource);
            conn = getConnections(dbSource);

            String tableName = dbSource.get("table_name") != null ? dbSource.get("table_name").toString() : "";
            String columnName = dbSource.get("column_name") != null ? dbSource.get("column_name").toString() : "";
            String sql = aggregation_function;

            //添加条件  TODO  未明确定义查询数据的范围
            if (param != null && param.size() < 1) {
                sql += "";
            }

            statement = conn.prepareStatement(sql);
            res = statement.executeQuery();
            ResultSetMetaData columns = res.getMetaData();
            while (res.next()) {

                Map<String, Object> column = new HashMap<>();

                for (int i = 1; i <= columns.getColumnCount(); i++) {
                    String fieldName = columns.getColumnName(i);
                    String value = res.getString(fieldName);
                    if (fieldName.equalsIgnoreCase("")) {
                        fieldName = i + "";
                    }
                    column.put(fieldName, value);
                }

                values.add(column);
            }

            return values;

        } catch (Exception e) {
            log.error("从数据源获取模型数据出错", e);
            return null;
        } finally {
            try {
                if (res != null) res.close();
                if (statement != null) statement.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取数据库连接
     *
     * @param param 参数
     * @return
     * @throws Exception
     */
    public static Connection getConnections(Map<String, Object> param) throws Exception {
        try {


            String server_name = param.get("server_name") != null ? param.get("server_name").toString().trim() : "";
            String port = param.get("port") != null ? param.get("port").toString().trim() : "";

            String database_name = param.get("database_name") != null ? param.get("database_name").toString().trim() : "";
//            String url = getConnectionUrl(param);

            Properties props = new Properties();

            String serverType = param.get("database_type") != null ? param.get("database_type").toString() : "";


            ConnectionParam param1 = new ConnectionParam();
            param1.setDatabase(database_name);
            param1.setIpAddr(server_name);
            param1.setPort(port);
            param1.setDbType(DbTypeEnum.parseValue(serverType));
            param1.setUsername(param.get("login_name") + "");
            param1.setPassword(param.get("login_pwd") + "");
            return ConnectionFactory.getConnectionFactory().createConnection(param1);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("getConnections_获取数据库连接出错", e);
            throw e;
        }
    }

//    /**
//     * 根据数据库类型获取连接字符串
//     *
//     * @param param
//     * @return
//     */
//    private static String getConnectionUrl(Map<String, Object> param) {
//        try {
//            String url = "";
//
//            String server_name = param.get("server_name") != null ? param.get("server_name").toString().trim() : "";
//            String port = param.get("port") != null ? param.get("port").toString().trim() : "";
//
//            String database_name = param.get("database_name") != null ? param.get("database_name").toString().trim() : "";
//
//            ConnectionParam param1 = new ConnectionParam();
//            param1.setDatabase(database_name);
//            param1.setIpAddr(server_name);
//            param1.setPort(port);
//
//            server_name += ":" + port;
//            // Oracle数据库
//            if (DataBaseType.oracle.toString().equals(param.get("database_type").toString())) {
//                //
//                url += "jdbc:oracle:thin:@";
//                url += server_name;
//                if (!database_name.equalsIgnoreCase("")) {
//                    url += ":" + database_name;
//                }
//
//                String url2 = "";
//                url2 = url2 + "jdbc:oracle:thin:@(DESCRIPTION = (ADDRESS_LIST = (ADDRESS = (PROTOCOL = TCP)(HOST = "
//                        + server_name + ")))(CONNECT_DATA = (SERVICE_NAME =" + database_name +
//                        ")(FAILOVER_MODE = (TYPE = SELECT)(METHOD = BASIC)(RETRIES = 180)(DELAY = 5))))";
//                //
//                // url = url2;
//            } else if (DataBaseType.mysql.toString().equals(param.get("database_type").toString())) {
//                //
//                url += "jdbc:mysql://";
//                url += server_name;
//                if (!database_name.equalsIgnoreCase("")) {
//                    url += "/" + database_name;
//                }
//
//            } else if (DataBaseType.sqlserver.toString().equals(param.get("database_type").toString())) {
//                //
//                url += "jdbc:sqlserver://";
//                url += server_name;
//                if (!database_name.equalsIgnoreCase("")) {
//                    url += ";DatabaseName=" + database_name;
//                }
//            } else {
//                throw new RuntimeException("不支持的数据库类型!");
//            }
//
//            return url;
//        } catch (Exception e) {
//            log.error("getConnectionUrl_获取数据库连接URL出错", e);
//            return "";
//        }
//    }

    //其他数据库不需要这个方法 oracle和db2需要
    private String getSchema(Connection conn) throws Exception {
        try {
            String schema;
            schema = conn.getMetaData().getUserName();
            if ((schema == null) || (schema.length() == 0)) {
                throw new Exception("ORACLE数据库模式不允许为空");
            }
            return schema.toUpperCase().toString();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 添加数据库驱动参数
     *
     * @param paramMap
     * @return
     */
    public static Map<String, Object> setDriverType(Map<String, Object> paramMap) {
        try {
            String serverType = "";
            serverType = paramMap.get("database_type") != null ? paramMap.get("database_type").toString() : "";
            if (serverType.equals("oracle")) {
                paramMap.put("driver", "oracle.jdbc.driver.OracleDriver");
            } else if (serverType.equals("sqlserver")) {
                paramMap.put("driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
            } else if (serverType.equals("mysql")) {
                paramMap.put("driver", "com.mysql.jdbc.Driver");
            } else {
                paramMap.put("driver", "com.mysql.jdbc.Driver");
            }

            return paramMap;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取脚本
     *
     * @param tableName      表名称
     * @param exportFileType 导出的文件类型  script或空
     * @param exportDBType   导出的目的数据库类型
     * @param columns        列集合
     * @param pkList         住建集合
     * @param fkList         外键集合
     * @return
     * @author yjt
     * @date 2018/12/25 14:11
     */
    public static String getScriptStr(String tableName, String exportFileType, String exportDBType, List<Map<String, Object>> columns, List<Map<String, Object>> pkList, List<Map<String, Object>> fkList) {
        try {
            if (columns == null || columns.size() < 1) {
                return null;
            }

            //将columns根据导出数据库类型进行字段对应转换
            columns = DbFieldMapping.getFieldMappingList(columns, exportDBType);

            //拼接列信息
            Map<String, List<String>> tabMap = new HashMap<>();
            for (Map<String, Object> column : columns) {
                String columnName = column.get("column_name") != null ? column.get("column_name").toString() : "";
                String columnType = column.get("type_name") != null ? column.get("type_name").toString() : "";
                String columnSize = column.get("column_size") != null ? column.get("column_size").toString() : "";
                String nullable = column.get("nullable") != null ? column.get("nullable").toString() : "";

                List<String> list = new ArrayList<>();
                List<String> fieldWithNoSizeList = new ArrayList<>();
                fieldWithNoSizeList.add("date");
                fieldWithNoSizeList.add("datetime");
                fieldWithNoSizeList.add("int");
                fieldWithNoSizeList.add("bigint");
                fieldWithNoSizeList.add("smallint");
                fieldWithNoSizeList.add("tinyint");
                fieldWithNoSizeList.add("timestamp");

                //不拼接长度的字段
                if (fieldWithNoSizeList.contains(columnType.toLowerCase()) || columnSize == null || columnSize.equalsIgnoreCase("") || columnSize.equalsIgnoreCase("0")) {
                    list.add(columnType);
                } else {
                    list.add(columnType + "(" + columnSize + ")");
                }

                if (nullable.equalsIgnoreCase("0")) {
                    list.add("not null");
                } else {
                    list.add("null");
                }

                tabMap.put(columnName, list);

            }

            //拼接主键信息
            Map<String, Object> pkMap = new HashMap<>();
            if (pkList != null && pkList.size() > 0) {
                String pknames = "";
                String pkName = "";
                for (Map<String, Object> pk : pkList) {
                    pkName = pk.get("pk_name") != null ? pk.get("pk_name").toString() : "";
                    String pkColumnName = pk.get("column_name") != null ? pk.get("column_name").toString() : "";

                    if (pknames.equalsIgnoreCase("")) {
                        pknames = pkColumnName;
                    } else {
                        pknames = pknames + "," + pkColumnName;
                    }

                    pkMap.put("pkName", pkName);
                    pkMap.put("columns", pknames);
                }
            }

            //获取外键集合
            List<Map<String, Object>> fks = new ArrayList<>();
            if (fkList != null && fkList.size() > 0) {
                String fkcolumns = "";
                String pkcolumns = "";
                for (Map<String, Object> fk : fkList) {

                    Map<String, Object> fkinfo = new HashMap<String, Object>();
                    //外键名和外键列名
                    String fkName = fk.get("fk_name") != null ? fk.get("fk_name").toString() : "";
                    String fkColumnName = fk.get("fkcolumn_name") != null ? fk.get("fkcolumn_name").toString() : "";

                    if ("".equalsIgnoreCase(fkcolumns)) {
                        fkcolumns = fkColumnName;
                    } else {
                        fkcolumns = pkcolumns + "," + fkColumnName;
                    }

                    //关联表名和主键名
                    String pkTableName = fk.get("pktable_name") != null ? fk.get("pktable_name").toString() : "";
                    String pkColumnName = fk.get("pkcolumn_name") != null ? fk.get("pkcolumn_name").toString() : "";

                    if ("".equalsIgnoreCase(pkcolumns)) {
                        pkcolumns = pkColumnName;
                    } else {
                        pkcolumns = pkcolumns + "," + pkColumnName;
                    }

                    fkinfo.put("fkName", fkName);
                    fkinfo.put("columns", fkcolumns);
                    fkinfo.put("fkTableName", pkTableName);
                    fkinfo.put("fkColumns", pkcolumns);

                    fks.add(fkinfo);
                }
            }

            //主外键对象
            Map<String, Object> pkMaps = new HashMap<>();
            pkMaps.put("primary", pkMap);
            pkMaps.put("foreign", fks);
            CreateSqlUtil util = new CreateSqlUtil();

            String sqlText;
            //导出脚本文件时换行符为\r\n
            if (exportFileType.equalsIgnoreCase("script")) {
                sqlText = CreateSqlUtil.createTableSql(tableName, tabMap, pkMaps, "\r\n");
            } else {
                sqlText = CreateSqlUtil.createTableSql(tableName, tabMap, pkMaps, "");
            }
            return sqlText;
        } catch (Exception e) {
            log.error("", e);
            return "";
        }
    }

    /**
     * 向指定数据源执行sql语句
     *
     * @param param 数据源信息
     * @param sql   要执行的sql语句
     * @return
     * @author yjt
     * @date 2018/12/21 15:07
     */
    public static boolean excuteSqlToDB(Map<String, Object> param, String sql) {
        try {
            //添加数据库驱动参数
            param = setDriverType(param);
            conn = getConnections(param);

            Statement state = conn.createStatement();
            state.executeUpdate(sql);

            return true;
        } catch (Exception e) {
            log.error("向指定数据源执行SQL脚本出错", e);
            return false;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据sql,数据库连接参数,模型属性获取数据集
     *
     * @param sql     要查询的sql
     * @param param   数据库连接
     * @param attList 模型属性集合
     * @return
     * @author yjt
     * @date 2019/1/23 13:49
     */
    public List<Map<String, Object>> getDataList(String sql, Map<String, Object> param, List<Map<String, Object>> attList) {
        ResultSet res = null;
        param = setDriverType(param);

        PreparedStatement statement = null;
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            conn = getConnections(param);
            statement = conn.prepareStatement(sql);
            res = statement.executeQuery();
            while (res.next()) {
                Map<String, Object> map = new HashMap<>();
                for (Map<String, Object> att : attList) {
                    String attName = att.get("att_name_en") != null ? att.get("att_name_en").toString() : "";
                    String value = "";
                    try {
                        value = res.getString(attName);
                    } catch (Exception e) {

                    }
                    map.put(attName.toLowerCase(), value);
                }
                result.add(map);
            }

            return result;
        } catch (Exception e) {
            log.error("根据SQL获取数据集出错。", e);
            return null;
        }
    }

    /**
     * 获取数据集合
     *
     * @param sql
     * @param param
     * @return
     * @author yjt
     * @date 2019/1/22 13:49
     */
    public ResultSet getResultList(String sql, Map<String, Object> param) {
        ResultSet res = null;
        param = setDriverType(param);

        PreparedStatement statement = null;
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            conn = getConnections(param);
            statement = conn.prepareStatement(sql);
            res = statement.executeQuery();
            return res;
        } catch (Exception e) {
            log.error("获取数据集出错。", e);
            return null;
        }
    }

    /**
     * 根据url和参数获取etl_workflow中目标表的字段集合
     *
     * @param url       jdbc连接
     * @param type      数据库类型
     * @param user      用户名
     * @param pwd       密码
     * @param tableName 表名称
     * @return
     * @author yjt
     * @date 2019/1/25 13:57
     */
    public List<String> getTableColumns(String url, String type, String user, String pwd, String tableName) {
        try {
            Properties props = new Properties();

            String serverType = type;
            if (serverType.equalsIgnoreCase("oracle")) {
                props.put("remarks", "true");
                props.put("remarksReporting", "true");
                props.put("user", user + " as sysdba");
            } else if (serverType.equalsIgnoreCase("mysql")) {
                props.put("useSSL", "false");
                props.put("remarks", "true");
                props.put("remarksReporting", "true");
                props.put("useInformationSchema", "true");
                props.put("user", user);
            } else if (serverType.equalsIgnoreCase("sqlserver")) {
                props.put("remarks", "true");
                props.put("remarksReporting", "true");
                props.put("useInformationSchema", "true");
                props.put("user", user);
            }

            props.put("password", pwd);
            Map<String, Object> p = new HashMap<>();
            p.put("database_type", type);
            p = setDriverType(p);
            Class.forName(p.get("driver").toString());
            conn = DriverManager.getConnection(url, props);
            DatabaseMetaData dbmd = conn.getMetaData();

            ResultSet rs = dbmd.getColumns(null, null, tableName, "%");

            List<String> columnList = new ArrayList<>();
            while (rs.next()) {
                String columnName = rs.getString("column_name");
                columnList.add(columnName);
            }

            return columnList;
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 从jdbc连接中解析数据库类型
     *
     * @param url jdbc连接
     * @return
     * @author yjt
     * @date 2019/1/25 13:58
     */
    public String getDataBaseTypeFromUrl(String url) {
        try {
            if (url.toLowerCase().contains(DataBaseType.oracle.toString())) {
                return DataBaseType.oracle.toString();
            } else if (url.toLowerCase().contains(DataBaseType.mysql.toString())) {
                return DataBaseType.mysql.toString();
            } else if (url.toLowerCase().contains(DataBaseType.sqlserver.toString())) {
                return DataBaseType.sqlserver.toString();
            } else {
                return null;
            }
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 拼接url地址和数据库名
     *
     * @param url      jdbc连接地址
     * @param database 数据库名
     * @return
     * @author yjt
     * @date 2019/1/25 13:59
     */
    public String concatJdbcUrl(String url, String database) {
        if (url.toLowerCase().contains(DataBaseType.oracle.toString())) {
            return url;
        } else if (url.toLowerCase().contains(DataBaseType.mysql.toString())) {
            return url + "/" + database;
        } else if (url.toLowerCase().contains(DataBaseType.sqlserver.toString())) {
            return url + ";DatabaseName=" + database;
        } else {
            //hive  url + "/" + database;
            //ES  url
            //GREENPLUM  url
            //IMPALA url + "/" + database;
            return null;
        }
    }

    /**
     * 获取一个表的所有字段名称集合
     *
     * @param param
     * @param tableName
     * @return
     * @author yjt
     * @date 2019/2/15 14:02
     */
    public List<String> getTableColumnList(Map<String, Object> param, String tableName) {
        List<String> result = new ArrayList<>();
        DatabaseMetaData dbmd = null;

        try {
            conn = getConnections(param);
            dbmd = conn.getMetaData();

            ResultSet rs = dbmd.getColumns(null, null, tableName, "%");
            while (rs.next()) {
                Map m = new HashMap();

                String columnName = rs.getString("COLUMN_NAME");
                result.add(columnName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("getTableColumnList_获取表字段集合出错", e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


//    /**
//     * 添加数据库驱动参数
//     *
//     * @param paramMap
//     * @return
//     */
//    public static Map<String, Object> setDriverType(Map<String, Object> paramMap) {
//        try {
//            String serverType = "";
//            serverType = paramMap.get("database_type") != null ? paramMap.get("database_type").toString() : "";
//            if (serverType.equals("oracle")) {
//                paramMap.put("driver", "oracle.jdbc.driver.OracleDriver");
//            } else if (serverType.equals("sqlserver")) {
//                paramMap.put("driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
//            } else if (serverType.equals("mysql")) {
//                paramMap.put("driver", "com.mysql.jdbc.Driver");
//            } else {
//                paramMap.put("driver", "com.mysql.jdbc.Driver");
//            }
//
//            //是否导入子模型数据  0:不导入子模型   1:导入子模型
//            paramMap.put("importChildMetaData", "1");
//            return paramMap;
//        } catch (Exception e) {
//            return null;
//        }
//    }
}
