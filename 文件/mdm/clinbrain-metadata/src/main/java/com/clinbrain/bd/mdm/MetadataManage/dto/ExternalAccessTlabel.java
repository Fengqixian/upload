package com.clinbrain.bd.mdm.MetadataManage.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author WANGYI
 * @className com.clinbrain.bd.mdm.MetadataManage.entity.ExternalAccessTlabel
 * @createdDate 2019/9/11 17:00
 * @description TODO
 * @e-mail WANGYI@clinbrain.com
 * @group bigdata develop group (mdm)
 */
@Data
public class ExternalAccessTlabel implements Serializable {
    private Integer tlabelPoolid;
    private Integer tlabelParentid;
    private String tlabelName;
    private String tlabelValue;
    private Integer tlabelType;
    private Integer tlabelTagType;
    private String tlabelTagValue;
    private String tlabelOperator;
    private String tlabelOperatorAccount;
    private String tlabelOperatorRole;
    private String tlabelOperatorTime;
    private Integer tlabelStatus;
    private Integer tlabelOrClass;
    private String tlabelClassName;
    private Integer tlabelPoolType;
    private Integer  tlabelCount;
    private Integer tlabelAllowRemove;
}
