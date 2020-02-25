package com.clinbrain.bd.mdm.MetadataManage.businessView.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clinbrain.bd.mdm.MetadataManage.businessView.entity.DataElement;
import com.clinbrain.bd.mdm.MetadataManage.businessView.mapper.DataElementMapper;
import com.clinbrain.bd.mdm.MetadataManage.businessView.service.MetaDataElementService;
import org.springframework.stereotype.Service;

@Service
public class MetaDataElementServiceImpl extends ServiceImpl<DataElementMapper, DataElement> implements MetaDataElementService {
}
