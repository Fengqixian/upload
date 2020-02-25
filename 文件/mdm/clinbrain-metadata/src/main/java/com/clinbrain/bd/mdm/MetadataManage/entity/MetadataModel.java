/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */
package com.clinbrain.bd.mdm.MetadataManage.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;

import java.time.LocalDateTime;

/**
 * 模型
 *
 * @author wangyi
 * @date 2019-05-28 09:56:48
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mdm_metadata_model")
public class MetadataModel extends Model<MetadataModel> {
private static final long serialVersionUID = 1L;

    /**
   * 
   */
    @TableId
    private Integer id;
    /**
   * 资源标识
   */
    private String resourceId;
    /**
   * 资源标识
   */
    private String parentResourceId;
    /**
   * 英文名称
   */
    private String nameEn;
    /**
   * 中文名称
   */
    private String nameCn;
    /**
   * 描述
   */
    private String description;
    /**
   * 状态
   */
    private String status;
    /**
   * 创建时间
   */
    private LocalDateTime createTime;
    /**
   * 更新时间
   */
    private LocalDateTime updateTime;

    /**
      * 是否是值域
      * @date  2019/6/12 10:37 
     */
    private Boolean isRange;
    /**
      * 是否是标准模型
      * @date  2019/6/12 10:37
     */
    private Boolean isStandard;

    /**
      * 值域id
      * @date  2019/6/12 10:38 
     */
    private String rangeId;

    /*
      * 模型类型
      * @author yjt
      * @date  2019/6/13 18:30
     */
    private String modelType;
  
}
