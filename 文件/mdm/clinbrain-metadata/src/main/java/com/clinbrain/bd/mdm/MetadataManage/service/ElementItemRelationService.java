package com.clinbrain.bd.mdm.MetadataManage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clinbrain.bd.mdm.MetadataManage.entity.ElementItemRelation;

import java.util.List;

public interface ElementItemRelationService extends IService<ElementItemRelation> {
    List<ElementItemRelation> getListByIds(List ids);
}
