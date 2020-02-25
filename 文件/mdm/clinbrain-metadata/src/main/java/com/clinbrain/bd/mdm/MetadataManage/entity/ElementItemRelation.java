package com.clinbrain.bd.mdm.MetadataManage.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author：ligen
 * @Date: Created:11:27  2019/7/17
 * @Description: 元素项关系表实体
 **/
@Data
@TableName("mdm_element_item_relation")
public class ElementItemRelation {
    @TableId
    private Integer id;
    private String elementId;
    private String sourceId;
    private String parentSourceId;
    private String targetId;
    private String parentTargetId;
}
