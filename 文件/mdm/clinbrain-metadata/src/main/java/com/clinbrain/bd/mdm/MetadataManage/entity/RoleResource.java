package com.clinbrain.bd.mdm.MetadataManage.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 资源权限
 *
 * @author lianglele
 * @date 2019-12-27 15:48:56
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mdm_role_resource")
public class RoleResource extends Model<RoleResource> {
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
   * 资源ID
   */
    private String resourceId;

    /**
     * 权限所属类型
     */
    private Integer roleType;
}
