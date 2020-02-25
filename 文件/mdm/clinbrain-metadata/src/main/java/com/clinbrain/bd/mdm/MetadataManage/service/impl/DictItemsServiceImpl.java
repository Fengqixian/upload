package com.clinbrain.bd.mdm.MetadataManage.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clinbrain.bd.mdm.MetadataManage.entity.DictInfo;
import com.clinbrain.bd.mdm.MetadataManage.entity.DictItems;
import com.clinbrain.bd.mdm.MetadataManage.mapper.DictItemsMapper;
import com.clinbrain.bd.mdm.MetadataManage.service.DictItemService;
import jdk.nashorn.internal.runtime.Debug;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class DictItemsServiceImpl extends ServiceImpl<DictItemsMapper, DictItems> implements DictItemService {

    @Autowired
    private Environment env;

    public IPage<DictItems> getDictItems(Page<DictItems> page, DictItems dictItems) {
        try {
            IPage<DictItems> ss = baseMapper.selectDictItemList(page, dictItems);
            return ss;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * 根据id
     *
     * @param dictId
     * @return
     */
    public List<Map<String, Object>> getDictItemsInfo(int dictId) {
        try {
            return baseMapper.selectDictItemsByDictId(dictId);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 根据字段获取字段固定下拉框值
     * @param fieldName 查询字段
     * @return
     */
    @Override
    public List<DictItems> getSelectItemsValues(String fieldName) {
        int dictId = Integer.valueOf(env.getProperty("dict.selectId"));
        return baseMapper.getSelectItemsValues(dictId, fieldName);
    }
}
