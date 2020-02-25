package com.clinbrain.bd.mdm.MetadataManage.technologyView.service;


import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.EtlSource;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.EtlTarget;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.vo.EtlDetailVo;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.vo.EtlInfoVo;
import com.clinbrain.bd.mdm.common.core.util.R;

import java.util.List;

/**
 * @author LIANGLELE
 * @className com.clinbrain.bd.mdm.MetadataManage.service.TechnologyViewService
 * @createdDate 2019/11/11 11:25
 * @description TODO
 * @e-mail lianglele@clinbrain.com
 * @group bigdata develop group (mdm)
 */
public interface TechnologyEtlService {

    /**
     * 根据来源id和表id查询ETL详情
     * @param sourceId 来源标识
     * @param targetId 目标标识
     * @return
     */
    EtlInfoVo selectEtlInfo(Integer sourceId, Integer targetId);


    R saveEtlDatabase(EtlSource source, EtlTarget target);
    R updateEtlDatabase(EtlSource source, EtlTarget target);

    List<EtlDetailVo> EtlDatabaseList();
}
