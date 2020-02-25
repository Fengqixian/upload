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
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 属性
 *
 * @author wangyi
 * @date 2019-05-29 10:05:46
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mdm_metadata_properties")
public class MetadataProperties extends Model<MetadataProperties> {
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
   * 模型标识
   */
    private String modelResourceId;
    /**
   * 中文名称
   */
    private String nameCn;
    /**
   * 英文名称
   */
    private String nameEn;
    /**
   * 数据类型
   */
    private String dataType;
    /**
   * 数据长度
   */
    private Integer dataLength;
    /**
   * 级联属性
   */
    private String cascadeProperties;
    /**
   * 展示方式
   */
    private String showType;
    /**
   * 展示格式
   */
    private String showFormat;
    /**
   * 取值范围说明
   */
    private String valueScope;
    /**
   * 
   */
    private String mappingColumn;
    /**
   *
   */
    private String mappingField;
    /**
   * 
   */
    private LocalDateTime createTime;
    /**
   * 
   */
    private LocalDateTime updateTime;
  
}
