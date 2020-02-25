package com.clinbrain.bd.mdm.MetadataManage.dto;

import lombok.Data;

/**
 * @Author：ligen
 * @Date: Created:11:20  2019/7/23
 * @Description: 元素项头与元素项关系实体
 **/
@Data
public class HeaderRelation {
    String fromResourceId;
    String fromParentId;
    String toResourceId;
    String toParentId;
}
