package com.clinbrain.bd.mdm.MetadataManage.entity;

import lombok.Data;

/**
 * @Author：ligen
 * @Date: Created:10:34  2019/8/5
 * @Description: 元素项节点
 **/
@Data
public class ElementNode {
    private String id;
    private String name;
    private String modelId;
    private String modelType;
    private String parentId;

    private String sourceId;
    private String targetId;
}
