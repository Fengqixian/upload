package com.clinbrain.bd.mdm.strategy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Auther: 梁乐乐
 * @Date: 2019/9/17 11:38
 * @Description: 策略信息值表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("label_labelpool")
public class LabelLabelpool extends Model<LabelLabelpool> {
    private static final long serialVersionUID = 1L;
    /**
     * 标签池唯一ID
     */
    private Integer id;

    /**
     * 标签池唯一ID
     */
    private Integer tlabelPoolId;

    /**
     * 父级ID
     */
    private Integer tlabelParentId;

    /**
     * 分类名称 / 标签名称
     */
    private String tlabelName;
    /**
     * 标签值
     */
    private String tlabelValue;
    /**
     * 标签类型 0=普通标签 1=有值标签 2=分类
     */
    private int tlabelType;

    /**
     * 标签类型 0=input 1=radio 3=selected
     */
    private int tlabelTagType;
    /**
     * 多个radio/selected 选项
     */
    private String tlabelTagValue;
    /**
     * 操作人
     */
    private String tlabelOperator;
    /**
     * 操作人账号
     */
    private String tlabelOperatorAccount;

    /**
     * 操作人角色
     */
    private String tlabelOperatorRole;
    /**
     * 操作时间
     */
    private String tlabelOperatorTime;
    /**
     * 标签状态0=正常 1=暂停使用 2=删除 3= 待审核 4= 驳回
     */
    private int tlabelStatus;
    /**
     * 0 = 分类 1=标签
     */
    private int tlabelorClass;
    /**
     * 分类名称
     */
    private String tlabelClassName;
    /**
     * 1= 个性标签 2=全局标签 3=科研标签
     */
    private int tlabelPoolType;
    /**
     * 使用次数
     */
    private Integer tlabelCount;
    /**
     * 是否允许删除0=是 1= 否
     */
    private Integer tlabelAllowRemove;
    /**
     * 备注
     */
    private String tlabelRemark;
}
