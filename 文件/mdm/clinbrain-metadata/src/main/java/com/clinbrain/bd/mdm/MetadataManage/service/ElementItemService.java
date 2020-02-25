package com.clinbrain.bd.mdm.MetadataManage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.clinbrain.bd.mdm.MetadataManage.entity.ElementItemInfo;

import java.util.List;

public interface ElementItemService extends IService<ElementItemInfo> {
    IPage<ElementItemInfo> getPageData(Page<ElementItemInfo> page, ElementItemInfo elementItemInfo);
    List<ElementItemInfo> getListByName(String elementName);
    int add(ElementItemInfo elementItemInfo);
}
