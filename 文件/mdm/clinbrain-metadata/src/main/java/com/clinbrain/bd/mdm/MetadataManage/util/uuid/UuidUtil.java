package com.clinbrain.bd.mdm.MetadataManage.util.uuid;

import com.clinbrain.bd.mdm.MetadataManage.dto.BaseKeys;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataModel;
import org.apache.commons.lang3.StringUtils;

/**
 * @author WANGYI
 * @className com.clinbrain.bd.mdm.MetadataManage.util.uuid.UuidUtil
 * @createdDate 2019/7/24 17:34
 * @description TODO
 * @e-mail WANGYI@clinbrain.com
 * @group bigdata develop group (mdm)
 */
public class UuidUtil {
    public static BaseKeys transferModel2Basekey(MetadataModel metadataModel) throws Exception {
        BaseKeys baseKeys = new BaseKeys();
        if(StringUtils.isBlank(metadataModel.getModelType())){
            throw new Exception();
        }
        baseKeys.setKeyName(StringUtils.upperCase(metadataModel.getModelType()));
        baseKeys.setKeyPrefix(baseKeys.getKeyPrefix()+StringUtils.upperCase(metadataModel.getModelType()));
        return baseKeys;
    }
}
