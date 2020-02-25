package com.clinbrain.bd.mdm.strategy.dto;

import lombok.Data;

/**
 * @Auther: 梁乐乐
 * @Date: 2019/9/18 11:09
 * @Description:[ {
 * "field":"字段A",
 * "table":"表A",
 * "alias":"别名A"
 * }
 */

@Data
public class FieldDto {

    /**
     * 字段
     */
    private String field;

    /**
     * 所属表
     */
    private String table;

    /**
     * 别名
     */
    private String alias;

    private String fromTable;

    private String toTable;

}
