package com.clinbrain.bd.mdm.MetadataManage.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clinbrain.bd.mdm.MetadataManage.dto.ImportMetaData;
import com.clinbrain.bd.mdm.MetadataManage.service.MetaDataImportService;
import com.clinbrain.bd.mdm.common.core.util.R;
import com.clinbrain.bd.mdm.common.log.annotation.SysLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 元数据导入
 */
@RestController
@CrossOrigin
@RequestMapping("/metadataImport")
public class MetaDataImportController {

    @Autowired
    private MetaDataImportService metaImportService;

    /**
     * 获取数据库服务连接信息
     *
     * @param jsonParam 参数
     * @return
     */
    @GetMapping("getServerConnection")
    @ResponseBody
    public R getServerConnection(String jsonParam) {
        boolean con = metaImportService.getServerConnection(jsonParam);
        String msg = "连接失败，请重新再试";
        if (con) {
            msg = "连接成功";
        }
        return new R<>(con, msg);
    }

    /**
     * 导入数据库和表信息
     *
     * @param metaData 参数
     * @return
     */
    @SysLog("元数据导入库表信息")
    @PostMapping("importMetaData")
    public R importMetaData(@RequestBody ImportMetaData metaData) {
        boolean con = metaImportService.importMetaData(metaData);
        String msg = "导入失败";
        if (con) {
            msg = "导入成功";
        }
        return new R<>(con, msg);
    }

    /**
     * 分页查询表集合
     *
     * @param metaData 参数
     * @return
     */
    @GetMapping("getTablePageList")
    public R<IPage<List<Map<String, Object>>>> getTablePageList(Page page, ImportMetaData metaData) {
        return new R<IPage<List<Map<String, Object>>>>(metaImportService.getTablePageList(page, metaData));
    }

    /**
     * 分页查询视图集合
     *
     * @param metaData 参数
     * @return
     */
    @GetMapping("getViewPageList")
    @ResponseBody
    public R<IPage<List<Map<String, Object>>>> getViewPageList(Page page, ImportMetaData metaData) {
        return new R<IPage<List<Map<String, Object>>>>(metaImportService.getViewPageList(page, metaData));
    }


    /**
     * 分页查询字段集合
     *
     * @param metaData 参数
     * @return
     */
    @GetMapping("getColumnPageList")
    @ResponseBody
    public R<IPage<List<Map<String, Object>>>> getColumnPageList(Page page, ImportMetaData metaData) {
        return new R<IPage<List<Map<String, Object>>>>(metaImportService.getSubInfoPageList(page, metaData));
    }

    /**
     * 分页查询索引集合
     *
     * @param metaData 参数
     * @return
     */
    @GetMapping("getIndexPageList")
    @ResponseBody
    public R<IPage<List<Map<String, Object>>>> getIndexPageList(Page page, ImportMetaData metaData) {
        return new R<IPage<List<Map<String, Object>>>>(metaImportService.getSubInfoPageList(page, metaData));
    }

    /**
     * 分页查询主键集合
     *
     * @param metaData 参数
     * @return
     */
    @GetMapping("getPrimaryKeyPageList")
    @ResponseBody
    public R<IPage<List<Map<String, Object>>>> getPrimaryKeyPageList(Page page, ImportMetaData metaData) {
        return new R<IPage<List<Map<String, Object>>>>(metaImportService.getSubInfoPageList(page, metaData));
    }

    /**
     * 分页查询外键集合
     *
     * @param metaData 参数
     * @return
     */
    @GetMapping("getForeignKeyPageList")
    @ResponseBody
    public R<IPage<List<Map<String, Object>>>> getForeignKeyPageList(Page page, ImportMetaData metaData) {
        return new R<IPage<List<Map<String, Object>>>>(metaImportService.getSubInfoPageList(page, metaData));
    }


}
