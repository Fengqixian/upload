package com.clinbrain.bd.mdm.MetadataManage.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.clinbrain.bd.mdm.MetadataManage.entity.DictInfo;
import com.clinbrain.bd.mdm.MetadataManage.entity.DictItems;

import java.util.List;
import java.util.Map;


public interface DictItemService  extends IService<DictItems> {

    /**
     * 分页查询常量信息
     * @param dictItems 常量对象
     * @return 数据信息
     */
    IPage<DictItems> getDictItems(Page<DictItems> page, DictItems dictItems);

    /**
     * 根据id查询常量
     * @param id
     * @return
     */
    List<Map<String, Object>> getDictItemsInfo(int dictId);

    /**
     * 根据字段获取字段固定下拉框值
     * @param fieldName 查询字段
     * @return
     */
    List<DictItems> getSelectItemsValues(String fieldName);

}
