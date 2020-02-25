package com.clinbrain.bd.mdm.common.core.util;

import java.util.List;
import java.util.Map;

/**
 * @Auther: 梁乐乐
 * @Date: 2018/11/2 09:10
 * @Description:
 */
public class CreateSqlUtil {

    public static void main(String[] args) {
        System.out.println("/r/n".length());
    }

    /**
     * 根据dbname创建数据库sql
     *
     * @param dbName db名称
     * @return sql
     */
    public static String CreateDbSql(String dbName, Map<String, Map<String, Object>> dbMap) {
        StringBuffer dbsql = new StringBuffer("create database").append(dbName).append("<br/>");
        try {
            if (dbMap != null && dbMap.isEmpty() == false) {

                //primary默认就属于primary文件组,可省略
                Map<String, Object> priMap = dbMap.get("primary");

                if (priMap != null && priMap.isEmpty() == false) {
                    dbsql.append("on  primary  -- 默认就属于primary文件组,可省略").append("<br/>");
                    //内容/*--数据文件的具体描述--*/
                    StringBuffer priSql = new StringBuffer("(").append("<br/>");
                    for (String key : priMap.keySet()) {
                        priSql.append(key).append(" = '").append(priMap.get(key)).append("',").append("<br/>");
                    }
                    dbsql.append(priSql.toString().substring(0, priSql.toString().length() - 6)).append("<br/>").append(")").append("<br/>");

                    Map<String, Object> logMap = dbMap.get("log");

                    if (logMap != null && logMap.isEmpty() == false) {
                        dbsql.append("log on  -- 默认就属于primary文件组,可省略").append("<br/>");
                        //内容 log on
                        StringBuffer logSql = new StringBuffer("(").append("<br/>");
                        for (String key : logMap.keySet()) {
                            logSql.append(key).append(" = '").append(logMap.get(key)).append("',").append("<br/>");
                        }
                        dbsql.append(logSql.toString().substring(0, logSql.toString().length() - 6)).append("<br/>").append(")");
                    }
                }
            }
            return dbsql.toString();
        } catch (Exception e) {
            return "";
        }

    }


    /**
     * 根据参数，创建数据表结构
     *
     * @param tableName 表名
     * @param tabMap    表字段以及约束
     * @param pkMaps    主外键关系
     * @param feedSign  换行符
     * @return sql语句
     */
    public static String createTableSql(String tableName, Map<String, List<String>> tabMap, Map<String, Object> pkMaps, String feedSign) {

        String toSign = "<br/>";
        if ("".equals(feedSign) == false) {
            toSign = feedSign;
        }
//        StringBuffer dbsql = new StringBuffer(" if exists(select * from information_schema.TABLES where table_name='")
//                .append(tableName).append("') ").append(toSign)
//                .append(" drop table ").append(tableName).append(";").append(toSign)
//                .append(" create table ").append(tableName).append(toSign);

        StringBuffer dbsql = new StringBuffer(" drop table if exists ")
                .append(tableName).append(";").append(toSign)
                .append("  ").append(tableName).append(";").append(toSign)
                .append(" create table ").append(tableName).append(toSign);
        try {
            dbsql.append(" (").append(toSign);
            if (tabMap != null && tabMap.isEmpty() == false) {
                for (String key : tabMap.keySet()) {
                    dbsql.append(key).append(" ");
                    //获取其它属性约束
                    List<String> colList = tabMap.get(key);
                    if (colList != null && colList.size() > 0) {
                        for (String val : colList) {
                            dbsql.append(val).append(" ");
                        }
                        dbsql.append(",").append(toSign);
                    }

                }
                //生成主外键sql
                if (pkMaps != null && !pkMaps.isEmpty()) {
                    //是否有主键
                    if (pkMaps.containsKey("primary") && pkMaps.get("primary") != null) {
                        Map<String, Object> pkMap = (Map<String, Object>) pkMaps.get("primary");
                        dbsql.append(CreateSqlUtil.getPrimarySql(pkMap, toSign));
                    }
                    //是否有外键
                    if (pkMaps.containsKey("foreign") && pkMaps.get("foreign") != null) {
                        List<Map<String, Object>> fkMap = (List<Map<String, Object>>) pkMaps.get("foreign");
                        dbsql.append(CreateSqlUtil.getForeignSql(fkMap, toSign));
                    }
                }
            }

            return dbsql.toString().substring(0, dbsql.toString().length() - (toSign.length() + 1)) + "); ";
        } catch (Exception e) {
            return "";
        }
    }


    /**
     * 根据参数，创建数据表结构 （方法重载）
     *
     * @param tableName 表名
     * @param tabMap    表字段以及约束
     * @param pkMaps    主外键关系
     * @return sql语句
     */
    public static String createTableSql(String tableName, Map<String, List<String>> tabMap, Map<String, Object> pkMaps) {
        return createTableSql(tableName, tabMap, pkMaps, "");
    }

    /**
     * 组装生成主键的sql
     *
     * @param pMap     主键信息
     * @param feedSign 换行换行符
     * @return sql
     */
    private static String getPrimarySql(Map<String, Object> pMap, String feedSign) {
        if (pMap != null && pMap.isEmpty() == false) {
            StringBuffer pksql = new StringBuffer("constraint ").append(pMap.get("pkName"))
                    .append(" primary key (").append(pMap.get("columns")).append("),").append(feedSign);
            return pksql.toString();
        }
        return "";

    }


    /**
     * 生成外键sql
     *
     * @param fList    外键信息map中的key{fkName 外键名,columns：当前表外键对应字段,fkTableName：外键对应表名, fkColumns:外键表字段}
     * @param feedSign 换行符
     * @return
     */
    private static String getForeignSql(List<Map<String, Object>> fList, String feedSign) {
        StringBuffer fksql = new StringBuffer(" ");
        if (fList != null && fList.size() > 0) {
            for (Map<String, Object> map : fList) {
                fksql.append("constraint ").append(map.get("fkName")).append(" foreign key (").append(map.get("columns"))
                        .append(") ").append("references ").append(map.get("fkTableName (")).append(map.get("fkColumns")).append("),").append(feedSign);
            }
            return fksql.toString();
        }
        return "";
    }


    /**
     * 根据参数，创建唯一索引
     *
     * @param tableName 表名
     * @param indexName 索引名称
     * @param indexList 索引字段
     * @return sql语句
     */
    public static String createUniqueIndexSql(String indexName, String tableName, List<String> indexList) {
        try {
            return CreateSqlUtil.CreateIndexSql("UNIQUE", indexName, tableName, indexList);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 根据参数，创建一般索引
     *
     * @param tableName 表名
     * @param indexName 索引名称
     * @param indexList 索引字段
     * @return sql语句
     */
    public static String CreateSortIndexSql(String indexName, String tableName, List<String> indexList) {
        try {
            return CreateSqlUtil.CreateIndexSql("", indexName, tableName, indexList);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 根据参数，创建索引
     *
     * @param indexKey  索引关键字
     * @param tableName 表名
     * @param indexName 索引名称
     * @param indexList 索引字段
     * @return sql语句
     */
    private static String CreateIndexSql(String indexKey, String indexName, String tableName, List<String> indexList) {
        StringBuffer dbsql = new StringBuffer("CREATE ").append(indexKey).append(" INDEX ").append(indexName).append("<br/>");
        try {
            dbsql.append("ON ").append("<br/>").append(tableName).append("(");
            if (indexList != null && indexList.size() > 0) {
                for (String ind : indexList) {
                    dbsql.append(ind).append(",");
                }
            }
            return dbsql.toString().substring(0, dbsql.toString().length() - 1) + ") ";
        } catch (Exception e) {
            return "";
        }
    }

}
