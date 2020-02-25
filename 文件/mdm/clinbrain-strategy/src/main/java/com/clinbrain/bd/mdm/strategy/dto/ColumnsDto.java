package com.clinbrain.bd.mdm.strategy.dto;

import lombok.Data;

import java.util.List;

/**
 * @Auther: 梁乐乐
 * @Date: 2019/9/18 11:02
 * @Description:
 */
@Data
public class ColumnsDto {
   private List<FieldDto> fieldList;

   private List<RelationDto> relationList;
}
