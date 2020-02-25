package com.clinbrain.bd.mdm.strategy.dto;

import lombok.Data;

/**
 * @Auther: 梁乐乐
 * @Date: 2019/9/17 11:38
 * @Description: 策略信息值表
 */
@Data
public class LabelpoolDto {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String label;
    private Integer tlabelPoolID;
    private String tlabelName; //标签名称
    private String tlabelValue; //标签值
    private Integer tlabelType; //标签类型
    private Integer tlabelTagType;//标签值类型
    private String tlabelTagValue;//标签类型为 radio/selected时的多选项
    private String tlabelOperator;//操作人
    private String tlabelOperatorAccount;//操作人账号
    private String tlabelOperatorRole;//操作人角色
    private String tlabelOperatorTime;//操作时间
    private Integer tlabelStatus;
    private Integer tlabelorclass;
    private String tlabelClassName;
    private Integer tlabelParentID;
//    private List<LabelpoolDto> children;
    private Integer tlabelpooltype;
    private Integer tlabelcount;
    private Integer tlabelallowremove; //是否允许删除
    private Integer value;
    private String tlabelremark;
    private String nodeType;
}
