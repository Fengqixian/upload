package com.clinbrain.bd.mdm.strategy.dto;

import lombok.Data;

/**
 * @Auther: 梁乐乐
 * @Date: 2019/9/18 11:02
 * @Description: where条件{
 * "filed":"字段A",
 * "oper":"等于",
 * "value":"${替代标签}"
 * }
 */
@Data
public class ConditionsDto {

    /**
     * 条件字段
     */
    private String filed;

    /**
     * 条件连接符
     */
    private String oper;

    /**
     * 替换标签或者值
     */
    private String value;

    /**
     * 1: 普通值， 2：标签
     */
    private int status;
    /**
     * 1：字符串类型 2：int 类型 3：时间类型
     */
    private int type;

    /**
     * 拼接符 and 或者 or
     */
    private String andOr;

    /**
     * 别名
     */
    private String alias;
}
