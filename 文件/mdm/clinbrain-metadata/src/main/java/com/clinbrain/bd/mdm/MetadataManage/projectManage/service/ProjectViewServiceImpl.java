package com.clinbrain.bd.mdm.MetadataManage.projectManage.service;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.poi.excel.ExcelFileUtil;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clinbrain.bd.mdm.MetadataManage.businessView.entity.DataElement;
import com.clinbrain.bd.mdm.MetadataManage.businessView.mapper.DataElementMapper;
import com.clinbrain.bd.mdm.MetadataManage.projectManage.entity.ProjectDataSetCategory;
import com.clinbrain.bd.mdm.MetadataManage.projectManage.entity.ProjectInfo;
import com.clinbrain.bd.mdm.MetadataManage.projectManage.entity.ProjectViewTree;
import com.clinbrain.bd.mdm.MetadataManage.projectManage.mapper.ProjectInfoMapper;
import com.clinbrain.bd.mdm.MetadataManage.projectManage.entity.RelationView;
import com.clinbrain.bd.mdm.MetadataManage.projectManage.mapper.ProjectViewMapper;
import com.clinbrain.bd.mdm.common.core.factory.ConnectionFactory;
import com.clinbrain.bd.mdm.common.core.util.ConnectionParam;
import com.clinbrain.bd.mdm.common.core.util.DbTypeEnum;
import com.clinbrain.bd.mdm.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@Slf4j
@Service
public class ProjectViewServiceImpl implements ProjectViewService {
    @Value("${project.data.main-table-name:BaseInfo}")
    private String mainTableName;
    @Value("${project.data.main-join-column:persid}")
    private String joinColumn;
    @Value("${project.data.limit}")
    private String limit;

    @Value("${project.data.db-type}")
    private String dbType;
    @Value("${project.data.ip-addr}")
    private String ipAddr;
    @Value("${project.data.port}")
    private String port;
    @Value("${project.data.username}")
    private String username;
    @Value("${project.data.password}")
    private String password;
    @Value("${project.data.database}")
    private String database;


    @Autowired
    private DataElementMapper dataElementMapper;
    @Autowired
    private ProjectViewMapper projectViewMapper;

    @Autowired
    private ProjectInfoMapper projectInfoMapper;

    /**
     * 加载分类/数据集树
     *
     * @return
     * @author yjt
     * @date 2019/10/12 18:10
     */
    @Override
    public R getProjectViewTree() {
        /*查询所有的分类  以及分类下的数据集*/
        List<ProjectViewTree> treeList = projectViewMapper.getProjectViewTreeList();
        /*循环将数据集放到子节点中 按照 id */
        List<ProjectViewTree> trees = new ArrayList<>();
        ProjectViewTree tree = null;
        Integer categoryId = null;
        for (ProjectViewTree t : treeList) {
            if (!t.getId().equals(categoryId)) {
                categoryId = t.getId();
                tree = new ProjectViewTree();

                tree.setId(t.getId());
                tree.setParentId(t.getParentId());
                tree.setNameCn(t.getNameCn());
                tree.setNameEn(t.getNameEn());
                tree.setNodeType("category");
                tree.setResourceId(t.getResourceId());

                trees.add(tree);
            }
            if (t.getProjectId() != null) { //说明是父分类或者没有子集
                ProjectViewTree node = new ProjectViewTree();

                node.setResourceId(t.getProjectResid());
                node.setId(t.getProjectId());
                node.setNodeType("project");
                node.setNameCn(t.getProjectNameCn());
                node.setNameEn(t.getProjectNameEn());

                tree.getChildren().add(node);
            }
        }
        //双重循环构造树
        List<ProjectViewTree> topTrees = new ArrayList<>();
        for (ProjectViewTree t : trees) {
            for (ProjectViewTree n : trees) {
                if (t.getId().equals(n.getParentId())) {
                    t.getChildren().add(n);
                }
            }
            if (t.getParentId() == null) {
                topTrees.add(t);
            }
        }

        return new R(topTrees);
    }

    /**
     * 加载分类/数据集树
     *
     * @return
     * @author yjt
     * @date 2019/10/12 18:10
     */
    @Override
    public R getProjectViewTree(String roleId) {
        /*查询所有的分类  以及分类下的数据集*/
        List<ProjectViewTree> treeList = projectViewMapper.getProjectViewTreeListByRoleId(roleId);
        /*循环将数据集放到子节点中 按照 id */
        List<ProjectViewTree> trees = new ArrayList<>();
        ProjectViewTree tree = null;
        Integer categoryId = null;
        for (ProjectViewTree t : treeList) {
            if (!t.getId().equals(categoryId)) {
                categoryId = t.getId();
                tree = new ProjectViewTree();

                tree.setId(t.getId());
                tree.setParentId(t.getParentId());
                tree.setNameCn(t.getNameCn());
                tree.setNameEn(t.getNameEn());
                tree.setNodeType("category");
                tree.setResourceId(t.getResourceId());

                trees.add(tree);
            }
            if (t.getProjectId() != null) { //说明是父分类或者没有子集
                ProjectViewTree node = new ProjectViewTree();

                node.setResourceId(t.getProjectResid());
                node.setId(t.getProjectId());
                node.setNodeType("project");
                node.setNameCn(t.getProjectNameCn());
                node.setNameEn(t.getProjectNameEn());

                tree.getChildren().add(node);
            }
        }
        //双重循环构造树
        List<ProjectViewTree> topTrees = new ArrayList<>();
        for (ProjectViewTree t : trees) {
            for (ProjectViewTree n : trees) {
                if (t.getId().equals(n.getParentId())) {
                    t.getChildren().add(n);
                }
            }
            if (t.getParentId() == null) {
                topTrees.add(t);
            }
        }

        return new R(topTrees);
    }

    @Override
    public R<IPage<DataElement>> getProjectDataElement(Page<DataElement> page, ProjectViewTree treeNode, String queryString) {
        try {
            if (treeNode == null) {
                return null;
            }
            Integer id = treeNode.getId();
            String type = treeNode.getNodeType();
            IPage<DataElement> pageR = new Page<>();
            List<Integer> ids = new ArrayList<>();
            if ("project".equalsIgnoreCase(type)) {
                //根据数据集id获取数据元
                ids.add(id);
                pageR = dataElementMapper.getProjectElementList(page, ids, queryString);
            } else if ("category".equalsIgnoreCase(type)) {
                /*查询所有的分类  以及分类下的数据集*/
                List<ProjectViewTree> treeList = projectViewMapper.getProjectViewTreeList();
                /*循环将数据集放到子节点中 按照 id */
                List<ProjectViewTree> trees = new ArrayList<>();
                ProjectViewTree tree = null;
                Integer categoryId = null;
                for (ProjectViewTree t : treeList) {
                    if (!t.getId().equals(categoryId)) {
                        categoryId = t.getId();
                        tree = new ProjectViewTree();

                        tree.setId(t.getId());
                        tree.setParentId(t.getParentId());
                        tree.setNodeType("category");
                        trees.add(tree);
                    }
                    if (t.getProjectId() != null) { //说明是父分类或者没有子集
                        ProjectViewTree node = new ProjectViewTree();

                        node.setId(t.getProjectId());
                        node.setNodeType("project");
                        tree.getChildren().add(node);
                    }
                }
                //双重循环构造树并截取子树
                List<ProjectViewTree> topTrees = new ArrayList<>();
                for (ProjectViewTree t : trees) {
                    for (ProjectViewTree n : trees) {
                        if (t.getId().equals(n.getParentId())) {
                            t.getChildren().add(n);
                        }
                    }
                    if (t.getId() == id || t.getId().equals(id)) {
                        topTrees.add(t);
                    }
                }

                //遍历子树提取数据集id
                loopTreeGetDatasetIds(topTrees, ids);
                pageR = dataElementMapper.getProjectElementList(page, ids, queryString);
            } else {
                return null;
            }
            return new R(pageR);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取项目数据元异常。", e);
            return null;
        }
    }

    @Override
    public R<IPage<DataElement>> getProjectDataElement(Page<DataElement> page, ProjectViewTree treeNode, String queryString, String roleId) {
        try {
            if (treeNode == null) {
                return null;
            }
            Integer id = treeNode.getId();
            String type = treeNode.getNodeType();
            IPage<DataElement> pageR = new Page<>();
            List<Integer> ids = new ArrayList<>();
            if ("project".equalsIgnoreCase(type)) {
                //根据数据集id获取数据元
                ids.add(id);
                pageR = dataElementMapper.getProjectElementListByRoleId(page, ids, queryString, roleId);
            } else if ("category".equalsIgnoreCase(type)) {
                /*查询所有的分类  以及分类下的数据集*/
                List<ProjectViewTree> treeList = projectViewMapper.getProjectViewTreeList();
                /*循环将数据集放到子节点中 按照 id */
                List<ProjectViewTree> trees = new ArrayList<>();
                ProjectViewTree tree = null;
                Integer categoryId = null;
                for (ProjectViewTree t : treeList) {
                    if (!t.getId().equals(categoryId)) {
                        categoryId = t.getId();
                        tree = new ProjectViewTree();

                        tree.setId(t.getId());
                        tree.setParentId(t.getParentId());
                        tree.setNodeType("category");
                        trees.add(tree);
                    }
                    if (t.getProjectId() != null) { //说明是父分类或者没有子集
                        ProjectViewTree node = new ProjectViewTree();

                        node.setId(t.getProjectId());
                        node.setNodeType("project");
                        tree.getChildren().add(node);
                    }
                }
                //双重循环构造树并截取子树
                List<ProjectViewTree> topTrees = new ArrayList<>();
                for (ProjectViewTree t : trees) {
                    for (ProjectViewTree n : trees) {
                        if (t.getId().equals(n.getParentId())) {
                            t.getChildren().add(n);
                        }
                    }
                    if (t.getId() == id || t.getId().equals(id)) {
                        topTrees.add(t);
                    }
                }

                //遍历子树提取数据集id
                loopTreeGetDatasetIds(topTrees, ids);
                pageR = dataElementMapper.getProjectElementListByRoleId(page, ids, queryString, roleId);
            } else {
                return null;
            }
            return new R(pageR);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取项目数据元异常。", e);
            return null;
        }
    }

    private void loopTreeGetDatasetIds(List<ProjectViewTree> topTrees, List<Integer> ids) {
        for (ProjectViewTree top : topTrees) {
            if (StringUtils.equalsIgnoreCase(top.getNodeType(), "project")) {
                ids.add(top.getId());
            }
            if (top.getChildren() != null && !top.getChildren().isEmpty()) {
                loopTreeGetDatasetIds(top.getChildren(), ids);
            }
        }
    }

    @Override
    public R<List<RelationView>> selectRelationView(Integer elementId) {
        return new R(projectViewMapper.selectRelationView(elementId));
    }

    @Override
    public void getProjectDataElementWithData(HttpServletResponse response, Integer projectId, List<Integer> ids) throws Exception {
        /*第一步，查询数据清单包含了哪些表哪些字段*/
        List<ProjectInfo> projectInfos = projectInfoMapper.getProjectInfoList(projectId, ids);
        String selectColumns = "";
        String fromTables = " from " + mainTableName;
        Integer tableId = null;
        for (ProjectInfo info : projectInfos) {
            if (info.getIsKeyValue() != null && info.getIsKeyValue()) continue;
            if (info.getIsCaseWhen() != null && info.getIsCaseWhen()) {
                if (StringUtils.isNotBlank(info.getCondition())) {
                    selectColumns = selectColumns + ", case when " + info.getCondition() + " then " + info.getTableName() + "." + info.getColumnName() + " end as \"" + info.getElementName() + "\"";
                }
            } else {
                selectColumns = selectColumns + "," + info.getTableName() + "." + info.getColumnName() + " as \"" + info.getElementName() + "\"";
            }
            if (!info.getTableId().equals(tableId)) {
                tableId = info.getTableId();
                if (!info.getTableName().equalsIgnoreCase(mainTableName)) {
                    fromTables = fromTables + " left join " + info.getTableName() + " on " + info.getTableName() + "." + joinColumn + " = " + mainTableName + "." + joinColumn + " ";
                }
            }
        }
        String sql = "select " + StringUtils.removeStart(selectColumns, ",") + fromTables + " " + limit + " order by " + mainTableName + "." + joinColumn;
        log.error("测试打印-故意报错：" + sql);
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            ConnectionParam comParam = new ConnectionParam();
            comParam.setDbType(DbTypeEnum.parseValue(dbType));
            comParam.setDatabase(database);
            comParam.setIpAddr(ipAddr);
            comParam.setPassword(password);
            comParam.setPort(port);
            comParam.setUsername(username);
            conn = ConnectionFactory.getConnectionFactory().createConnection(comParam);
            preparedStatement = conn.prepareStatement(sql);
            rs = preparedStatement.executeQuery();
            List<Map<String, Object>> mapList = new ArrayList<>();
            handleResultSet(rs, mapList, response);
        } finally {
            if (rs != null) rs.close();
            if (preparedStatement != null) preparedStatement.close();
            if (conn != null) conn.close();
        }
    }

    /*理论是直接往excel写*/
    private void handleResultSet(ResultSet rs, List<Map<String, Object>> mapList, HttpServletResponse response) throws IOException, SQLException {
        ResultSetMetaData columns = null;
        int columnCount = 0;
        if (rs != null) {
            columns = rs.getMetaData();
            columnCount = rs.getMetaData().getColumnCount();
        }
        Map<String, Object> row = null;
        int rowCount = 0;
        while (rs != null && rs.next() && rowCount <= 60000) {
            rowCount++;
            row = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                row.put(columns.getColumnLabel(i), rs.getObject(i));
            }
            mapList.add(row);
        }

        //表头数据
        String[] header = new String[columnCount];
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("sheet1");
        sheet.setDefaultColumnWidth(10);
        HSSFRow headrow = sheet.createRow(0);
        for (int i = 0; i < columnCount; i++) {
            HSSFRichTextString text = new HSSFRichTextString(columns.getColumnLabel(i + 1));
            headrow.createCell(i).setCellValue(text);
        }

        for (int i = 0; i < mapList.size(); i++) {
            headrow = sheet.createRow(i + 1);
            for (int j = 0; j < columnCount; j++) {
                HSSFRichTextString text = new HSSFRichTextString((String) mapList.get(i).get(columns.getColumnLabel(j + 1)));
                headrow.createCell(j).setCellValue(text);
            }
        }
        workbook.write(response.getOutputStream());
    }


    @Override
    public R<List<RelationView>> selectProjectColumn(Integer datasetId, Integer elementId) {
        return new R(projectViewMapper.selectProjectColumn(datasetId, elementId));
    }

    @Override
    public R<List<RelationView>> selectProjectColumn(Integer datasetId, Integer elementId, String roleId) {
        return new R(projectViewMapper.selectProjectColumnByRoleId(datasetId, elementId, roleId));
    }

    @Override
    public List<ProjectDataSetCategory> selectProjectCategory(String resourceId) {
        return projectViewMapper.selectProjectCategory(resourceId);
    }
}
