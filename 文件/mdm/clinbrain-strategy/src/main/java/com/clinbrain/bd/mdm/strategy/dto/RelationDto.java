package com.clinbrain.bd.mdm.strategy.dto;

import lombok.Data;

/**
 * @Auther: 梁乐乐
 * @Date: 2019/9/18 11:11
 * @Description:
 * from":"A字段",
 * "to":"B字段",
 * "relat":"+,-,*,/,count,avg"
 */
@Data
public class RelationDto {

    /**
     * 计算第一个表
     */
    private String from;

    /**
     * 计算end表
     */
    private String to;

    /**
     * 计算符
     */
    private String relat;
}
