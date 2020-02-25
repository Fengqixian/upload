package com.clinbrain.bd.mdm.strategy.dto;

import lombok.Data;

/**
 * @Auther: 梁乐乐
 * @Date: 2019/9/18 14:03
 * @Description: 表关系
 */
@Data
public class TableDto {
    /**
     * 查询表
     */
    private String table;

    /**
     * 表别名
     */
    private String alias;

    /**
     * 连接符 left  rigt
     */
    private String join;

    /**
     * 连接字段
     */
    private String joinField;

}
