package com.clinbrain.bd.mdm.MetadataManage.technologyView.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.Column;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.Database;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.Table;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.TechnologyViewTree;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.vo.FromVo;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.service.TechnologyViewService;
import com.clinbrain.bd.mdm.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据血缘关系分析
 */
@RestController
@Api(value = "技术视图")
@RequestMapping("/technologyView")
@CrossOrigin
public class TechnologyViewController {
    @Autowired
    private TechnologyViewService technologyViewService;

    @ResponseBody
    @ApiOperation(response = R.class, value = "查询数据库表Tree")
    @GetMapping("/tree")
    public R getTechnologyViewTree() throws Exception {
        return technologyViewService.getTechnologyViewTree();
    }

    @ResponseBody
    @ApiOperation(response = R.class, value = "根据表查询表的字段")
    @GetMapping("/info")
    public R getTableInfoByTreeNode(Page<Column> page, TechnologyViewTree tree, String queryString) throws Exception {
        return technologyViewService.getTableInfoByTreeNode(page, tree, queryString);
    }

    @ResponseBody
    @ApiOperation(response = R.class, value = "根据库id查询表")
    @GetMapping("/tableList")
    public R getTableListByTreeNode(Page<Table> page, TechnologyViewTree tree) throws Exception {
        return technologyViewService.getTableListByTreeNode(page, tree);
    }

    @ResponseBody
    @ApiOperation(response = R.class, value = "根据条件查询数据库")
    @GetMapping("/database")
    public R getDatabase(Page<Database> page, Database database) throws Exception {
        return technologyViewService.getDatabase(page, database);
    }

    @ResponseBody
    @ApiOperation(response = R.class, value = "根据条件查询数据表")
    @GetMapping("/table")
    public R getColumn(Page<Table> page, Table table) throws Exception {
        return technologyViewService.getColumn(page, table);
    }

    @ResponseBody
    @ApiOperation(response = R.class, value = "根据列id查询列信息")
    @GetMapping("/treeNodeInfo")
    public R getTreeNodeInfo(String resourceId, String type) throws Exception {
        return technologyViewService.getTechnologyViewNodeInfo(resourceId, type);
    }

    @ResponseBody
    @ApiOperation(response = R.class, value = "根据数据元查询列信息")
    @GetMapping("/getTableColumns")
    public R getTechnologyColumnList(Page<TechnologyViewTree> page, @RequestParam(required = false) String queryString, @RequestParam(value = "elementIds", required = false) List<Integer> elementIds) {
        return technologyViewService.getTechnologyColumnList(page, queryString, elementIds);
    }


    @ApiOperation(response = R.class, value = "查询数据库下表结构")
    @GetMapping("/getDataTableList")
    @ResponseBody
    public R getDataTableList(FromVo fromVo) {
        return technologyViewService.getDataTableList(fromVo);
    }


    @ApiOperation(response = R.class, value = "查询列信息")
    @GetMapping("/getTableColumnList")
    public R getTechnologyColumnList(FromVo fromVo) {
        return technologyViewService.getTableColumnList(fromVo);
    }


    /**
     * 导入数据表结构
     *
     * @param fromVo 参数
     * @return R 成功失败信息
     */
    @PostMapping("/importDataBase")
    public R importDataBase(@RequestBody FromVo fromVo) {
        return technologyViewService.importDataBase(fromVo);
    }

    /**
     * 创建数据库
     *
     * @param database
     * @return
     */
    @PostMapping("/saveDataBase")
    public R saveDataBase(@RequestBody Database database) {
        return technologyViewService.saveDataBase(database);
    }

    /**
     * 修改数据库
     *
     * @param database
     * @return
     */
    @PutMapping("/updateDataBase")
    public R updateDataBase(@RequestBody Database database) {
        return technologyViewService.updateDataBase(database);
    }

    /**
     * 创建数据表
     *
     * @param table
     * @return
     */
    @PostMapping("/saveDataTable")
    public R saveDataTable(@RequestBody Table table) {
        List<Table> tables = new ArrayList<>();
        tables.add(table);
        return technologyViewService.saveDataTable(tables);
    }

    /**
     * 创建数据表
     *
     * @param table
     * @return
     */
    @PutMapping("/updateTable")
    public R updateDataTable(@RequestBody Table table) {
        List<Table> tables = new ArrayList<>();
        tables.add(table);
        return technologyViewService.updateDataTable(tables);
    }

    /**
     * 创建数据字段
     *
     * @param column
     * @return
     */
    @PostMapping("/saveDataColumn")
    public R saveDataColumn(@RequestBody Column column) {
        List<Column> columns = new ArrayList<>();
        columns.add(column);
        return technologyViewService.saveDataColumn(columns);
    }

    /**
     * 修改数据字段
     *
     * @param column
     * @return
     */
    @PutMapping("/updateDataColumn")
    public R updateDataColumn(@RequestBody Column column) {
        List<Column> columns = new ArrayList<>();
        columns.add(column);
        return technologyViewService.updateDataColumn(columns);
    }


    @ApiOperation(response = R.class, value = "导出字段")
    @GetMapping("/downTemplate")
    public void downTemplate(HttpServletResponse response, TechnologyViewTree tree) throws UnsupportedEncodingException {
        Page page = new Page();
        page.setSize(-1);
        R rcols = technologyViewService.getTableInfoByTreeNode(page, tree, "");
        String codedFileName = "表对应字段信息";
        if (rcols.getData() != null) {
            IPage<Column> iPage = (IPage<Column>) rcols.getData();
            if (iPage.getRecords() != null && iPage.getRecords().size() > 0) {

            }

        }
    }
}
