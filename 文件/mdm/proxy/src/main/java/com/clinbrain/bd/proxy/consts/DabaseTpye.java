package com.clinbrain.bd.proxy.consts;

/**
 * @author WANGYI
 * @className com.clinbrain.bd.proxy.consts.DabaseTpye
 * @createdDate 2019/8/23 9:41
 * @description TODO
 * @e-mail WANGYI@clinbrain.com
 * @group bigdata develop group (mdm)
 */
public enum DabaseTpye {
    GP("Greenplum"),PG("Postgres"),MYSQL("Mysql"),ORACLE("Oracle"),SQLSERVER("SqlServer");
    private String value;
    private DabaseTpye(String value){
        this.value = value;
    }
    public String getValue(){
        return this.value;
    }

}
