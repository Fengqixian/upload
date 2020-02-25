package com.clinbrain.bd.mdm.MetadataManage.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clinbrain.bd.mdm.MetadataManage.entity.DictItems;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DictItemsMapper extends BaseMapper<DictItems> {
    IPage<DictItems> selectDictItemList(Page page, @Param("itemsInfo") DictItems itemsInfo);

    List<Map<String, Object>> selectDictItemsByDictId(@Param("dictId") int dictId);


    List<DictItems> getSelectItemsValues(@Param("dictId") int dictId, @Param("itemName") String itemName);

}