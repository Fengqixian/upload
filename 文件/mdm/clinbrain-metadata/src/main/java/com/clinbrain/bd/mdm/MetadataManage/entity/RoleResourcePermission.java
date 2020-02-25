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
 * 资源权限
 *
 * @author wangyi
 * @date 2019-06-04 15:48:56
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mdm_role_resource_permission")
public class RoleResourcePermission extends Model<RoleResourcePermission> {
private static final long serialVersionUID = 1L;

    /**
   * 自增长主键
   */
    @TableId
    private Integer id;
    /**
   * 角色ID
   */
    private String roleId;
    /**
   * 资源父实例
   */
    private String parentResourceId;
    /**
   * 授权类型
   */
    private String permissionType;
    /**
   * 资源ID
   */
    private String resourceId;
    /**
   * 资源对应模型
   */
    private String modelResourceId;

    /**
      * 模型关联的值域ID
      * @author yjt
      * @date  2019/6/10 16:05
     */
    private String rangeId;

    /**
      * 值域的允许访问值集合
      * @author yjt
      * @date  2019/6/10 16:06 
     */
    private String rangeValues;

    /**
      * 取消脱敏标识
      * @author yjt
      * @date  2019/6/10 16:09 
     */
    private Boolean cancelDataMasking;

    /**
      * 自定义条件
      * @author yjt
      * @date  2019/6/10 16:31 
     */
    private String customConditions;

    /**
   * 权限-新增
   */
    private Boolean permissionCreate;
    /**
   * 权限-删除
   */
    private Boolean permissionDelete;
    /**
   * 权限-修改
   */
    private Boolean permissionUpdate;
    /**
   * 创建时间
   */
    private LocalDateTime createTime;
    /**
   * 更新时间
   */
    private LocalDateTime updateTime;
  
}
