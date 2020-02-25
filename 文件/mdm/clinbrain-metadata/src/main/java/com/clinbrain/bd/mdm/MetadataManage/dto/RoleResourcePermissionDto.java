package com.clinbrain.bd.mdm.MetadataManage.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 资源权限
 *
 * @author lianglele
 * @date 2019-12-25 15:48:56
 */
@Data
public class RoleResourcePermissionDto {
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
     * 视图类型 0：技术 1：业务 2：项目
     */
    private Integer projectType;

    /**
     * 资源ID
     */
    private String resourceId;
    /**
     * 资源对应模型
     */
    private String modelResourceId;

    /**
     * 资源父实例
     */
    private List<String> parentResourceIds;

}
