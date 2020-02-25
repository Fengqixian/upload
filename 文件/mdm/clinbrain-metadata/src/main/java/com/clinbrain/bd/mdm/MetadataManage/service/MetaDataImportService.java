package com.clinbrain.bd.mdm.MetadataManage.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clinbrain.bd.mdm.MetadataManage.dto.ImportMetaData;

import java.util.Map;

public interface MetaDataImportService {
    boolean getServerConnection(String jsonParam);

    /**
     * 导入数据库和表信息
     *
     * @param metaData JSON格式的参数（Map）
     * @return
     */
    boolean importMetaData(ImportMetaData metaData);

    /**
     * 分页获取表集合
     *
     * @param metaData
     * @return
     */
    IPage getTablePageList(Page page, ImportMetaData metaData);

    /**
     * 分页获取视图集合
     *
     * @param metaData
     * @return
     */
    IPage getViewPageList(Page page, ImportMetaData metaData);

    /**
     * 字段，索引，主键查询
     * @param page
     * @param metaData
     * @return
     */
    IPage getSubInfoPageList(Page page, ImportMetaData metaData);
}
