package com.clinbrain.bd.mdm.common.datasource.util;

/**
 * @author lianglele
 * @date 2019-12-26 11:34
 */
public enum DataDBType {
    GREENPLUM("gp","com.pivotal.jdbc.GreenplumDriver"),
    MYSQL("mysql","com.mysql.cj.jdbc.Driver"),
    HIVE("hive","com.mysql.cj.jdbc.Driver"),
    SQLSERVER("sqlserver","com.microsoft.jdbc.sqlserver.SQLServerDriver"),
    ORACLE("oracle","oracle.jdbc.driver.OracleDriver");
    private String value;
    private String driverClass;
    private DataDBType(String value,String driverClass){
        this.value=value;
        this.driverClass=driverClass;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public String getValue() {
        return value;
    }

    public static DataDBType parseValue(String value){
        for(DataDBType e:DataDBType.values()){
            if(e.value.equalsIgnoreCase(value))
                return e;
        }
        return null;
    }
}
