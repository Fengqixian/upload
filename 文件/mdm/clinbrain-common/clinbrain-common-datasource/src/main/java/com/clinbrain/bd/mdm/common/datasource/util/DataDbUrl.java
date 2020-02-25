package com.clinbrain.bd.mdm.common.datasource.util;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author lianglele
 * @date 2019-12-26 13:27
 */
@Component
public class DataDbUrl {

    @Value("${hive.dbName:hive}")
    private String hiveDbName;

    public String getDbUrl(String ip, String port, String dbName, DataDBType type) throws Exception {
        String url = "";
        switch (type) {
            case MYSQL:
                url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName + "?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
                break;
            case GREENPLUM:
                url = "jdbc:pivotal:greenplum://" + ip + ":" + port + ";DatabaseName=" + dbName;
                break;
            case SQLSERVER:
                url = "jdbc:sqlserver://" + ip + ":" + port + ";DatabaseName=" + dbName;
                break;
            case ORACLE:
                url = "jdbc:oracle:thin:@//" + ip + ":" + port + ";DatabaseName=" + dbName;
                break;
            case HIVE:
                url = "jdbc:mysql://" + ip + ":" + port + "/"+hiveDbName + "" + "?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
                break;
            default:
                throw new Exception("不支持的数据库类型");
        }
        return url;
    }
}
