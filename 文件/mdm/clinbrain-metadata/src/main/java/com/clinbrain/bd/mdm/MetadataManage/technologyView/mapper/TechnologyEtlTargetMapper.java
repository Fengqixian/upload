package com.clinbrain.bd.mdm.MetadataManage.technologyView.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.EtlTarget;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.vo.EtlDetailVo;

import java.util.List;

public interface TechnologyEtlTargetMapper extends BaseMapper<EtlTarget> {

    List<EtlDetailVo> EtlDatabaseList();
}