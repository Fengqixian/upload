package com.clinbrain.bd.mdm.genid.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.data.annotation.Transient;


/**
 * 数据库信息
 */
@Data
public class BaseKeys{
    @TableId
    private Integer id;

    /**
     *
     */
    private static final long serialVersionUID = 5336766680317040531L;

    /**
     * identifier field
     */
    private String keyName;


    /**
     * persistent field
     */
    private String keyValue;

    /**
     * persistent field
     */
    private Integer keyLength;

    /**
     * persistent field
     */
    private Integer keyCache;

    /**
     * nullable persistent field
     */
    private String keyPrefix;

    /**
     * nullable persistent field
     */
    private String keySuffix;

    private String keyDigit;

    private Integer version;
}
