package com.clinbrain.bd.mdm.MetadataManage.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;


/**
 * 数据库信息
 */
@Data
public class BaseKeys {
    @TableId
    private Integer id;

    /**
     *
     */
    private static final long serialVersionUID = 5336766680317040531L;

    /**
     * identifier field
     */
    private String keyName = "MDM";


    /**
     * persistent field
     */
    private String keyValue = "0";

    /**
     * persistent field
     */
    private Integer keyLength = 50;

    /**
     * persistent field
     */
    private Integer keyCache = 200;

    /**
     * nullable persistent field
     */
    private String keyPrefix = "MDM${date6}";

    /**
     * nullable persistent field
     */
    private String keySuffix;

    private String keyDigit = "10";

    private Integer version = 0;
}
