package com.clinbrain.bd.dataserver.entity;

/**
 * @author WANGYI
 * @className com.clinbrain.bt.metadata.entity.AggFunctionEnum
 * @createdDate 2019/5/13 15:15
 * @description TODO
 * @e-mail WANGYI@clinbrain.com
 * @group bigdata develop group (DataServer)
 */
public enum AggFunctionEnum {
    SUM("sum"),COUNT("count"),AVG("avg"),MAX("max"),MIN("min");
    private String value;
    private AggFunctionEnum(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
