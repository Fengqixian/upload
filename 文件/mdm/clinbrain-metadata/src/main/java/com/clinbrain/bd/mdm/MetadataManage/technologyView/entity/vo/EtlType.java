package com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.vo;

import lombok.Data;

import java.util.Map;

/**
 * @author lianglele
 * @date 2019-11-11 15:21
 */
@Data
public class EtlType {
    private String id;
    private String dataflowCode;
    private String dataflowName;
    private Integer dataflowType;
    private Integer jdbcType;
    private String dataflowParam;
    private Map<String, String> parameter;

}
