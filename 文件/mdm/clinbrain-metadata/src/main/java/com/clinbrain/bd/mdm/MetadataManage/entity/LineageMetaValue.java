package com.clinbrain.bd.mdm.MetadataManage.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
  * 用于血缘分析的通用实体
  * @return 
  * @author yjt
  * @date  2019/10/25 10:07
 */
@Data
@EqualsAndHashCode
public class LineageMetaValue {
    private Integer id;

    private String resourceId;
    private String nameEn;
    private String nameCn;
    private String dataType;
    private String parentResourceId;

    private Integer tableId;
    private String dbName;
    private Boolean isHighlight;

    //视图类别  业务视图:business  项目视图:project  技术视图:technology
    private String viewType;
    //工程视图时，数据元的所属工程id
    private Integer projectId;

    //查看的血缘版本 (HX或其他)
    private String version;

    private Integer etlId;
}
