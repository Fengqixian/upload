package com.clinbrain.bd.mdm.MetadataManage.technologyView.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author WANGYI
 * @className com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.Column
 * @createdDate 2019/10/13 11:37
 * @description TODO
 * @e-mail WANGYI@clinbrain.com
 * @group bigdata develop group (mdm)
 */
@Data
@TableName("mdm_technology_column")
@ExcelTarget("column")
public class Column {
    @TableId
    @Excel(name = "编号", width = 30)
    private Integer id;
    @Excel(name = "对应表编号", width = 30)
    private Integer tableId;
    @Excel(name = "字段内部唯一编码", width = 50)
    private String resourceId;
    @Excel(name = "内部唯一编号", width = 50)
    private String resourceCode;
    @Excel(name = "字段名", width = 50)
    private String nameEn;
    @Excel(name = "字段中文说明", width = 50)
    private String nameCn;
    @Excel(name = "字段类型", width = 20)
    private String dataType;
    @Excel(name = "字段长度", width = 20)
    private Integer dataLength;
    @Excel(name = "字段是否不为空", replace = {"是_true", "否_false"}, width = 20)
    private Boolean nullAllow;
    @Excel(name = "字段是否主见", replace = {"是_true", "否_false"}, width = 20)
    private Boolean primaryKey;
    @Excel(name = "字段描述", width = 50)
    private String comment;
    @Excel(name = "字段类型分组", width = 20)
    private String dataTypeGroup;
}
