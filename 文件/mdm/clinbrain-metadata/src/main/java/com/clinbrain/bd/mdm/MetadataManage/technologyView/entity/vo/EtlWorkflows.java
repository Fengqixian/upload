package com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.vo;

import lombok.Data;

import java.util.Map;

/**
 * @author lianglele
 * @date 2019-11-11 15:05
 */
@Data
public class EtlWorkflows {

    private String loc = "33.140625 -63";

    /**
     * 是否可用
     */
    private Integer runnable =1;

    private boolean isGroup = true;

    private String group;

    private String color = "#00A9C9";

    /**
     *  目标库
     */
    private String targetSchema;

    private String targetTable;

    private EtlExtraData extraData;

    private String key;

    private String text;

    private String code;

    private Integer sequenceCustomized;

    private String paramDefine;

    private String componentCategory;

    private String category;

    private Map<String, Object> parameterJson;

    private String desc;

    private Integer flowType;
}
