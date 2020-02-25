package com.clinbrain.bd.mdm.MetadataManage.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author：ligen
 * @Date: Created:11:40  2019/7/16
 * @Description: 元素项实体
 **/
@Data
@TableName("mdm_element_item")
public class ElementItemInfo {
    @TableId
    private Integer id;
    private String elementName;
    private String elementDatatype;
    private Integer elementLength;
    private String elementRemarks;
    private Date createTime;
    private Date updateTime;
    private String resourceId;
    private String parentResourceId;
}
