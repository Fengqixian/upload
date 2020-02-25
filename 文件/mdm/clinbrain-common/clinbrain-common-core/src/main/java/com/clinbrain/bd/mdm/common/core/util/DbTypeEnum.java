package com.clinbrain.bd.mdm.common.core.util;

/**
 * @author WANGYI
 * @className com.clinbrain.bt.metadata.entity.DbTypeEnum
 * @createdDate 2019/5/14 10:45
 * @description TODO
 * @e-mail WANGYI@clinbrain.com
 * @group bigdata develop group (DataServer)
 */
public enum DbTypeEnum {
    GREENPLUM("gp","com.pivotal.jdbc.GreenplumDriver"),
    MYSQL("mysql","com.mysql.cj.jdbc.Driver"),
    SQLSERVER("sqlserver","com.microsoft.jdbc.sqlserver.SQLServerDriver"),
    ORACLE("oracle","oracle.jdbc.driver.OracleDriver");
    private String value;
    private String driverClass;
    private DbTypeEnum(String value, String driverClass){
        this.value=value;
        this.driverClass=driverClass;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public String getValue() {
        return value;
    }

    public static DbTypeEnum parseValue(String value){
        for(DbTypeEnum e: DbTypeEnum.values()){
            if(e.value.equalsIgnoreCase(value))
            return e;
        }
        return null;
    }
}
