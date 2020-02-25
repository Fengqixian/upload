package com.clinbrain.bd.dataserver.entity;

/**
 * @author WANGYI
 * @className com.clinbrain.bt.metadata.entity.Column
 * @createdDate 2019/5/13 15:11
 * @description TODO
 * @e-mail WANGYI@clinbrain.com
 * @group bigdata develop group (DataServer)
 */
public class Column {
    private String name;
    private AggFunctionEnum aggFunction;
    private String orderType;//desc,asc

    public Column(String name, AggFunctionEnum aggFunction, String orderType) {
        this.name = name;
        this.aggFunction = aggFunction;
        this.orderType = orderType;
    }
    public Column() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AggFunctionEnum getAggFunction() {
        return aggFunction;
    }

    public void setAggFunction(AggFunctionEnum aggFunction) {
        this.aggFunction = aggFunction;
    }

    public String getSelectExp(){
        return this.aggFunction == null? name :this.aggFunction.getValue()+"("+ name +")";
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}
