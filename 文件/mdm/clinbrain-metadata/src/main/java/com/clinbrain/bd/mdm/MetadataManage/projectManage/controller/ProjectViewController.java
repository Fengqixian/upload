package com.clinbrain.bd.mdm.MetadataManage.projectManage.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clinbrain.bd.mdm.MetadataManage.businessView.entity.DataElement;
import com.clinbrain.bd.mdm.MetadataManage.projectManage.entity.*;
import com.clinbrain.bd.mdm.MetadataManage.projectManage.service.ProjectDataService;
import com.clinbrain.bd.mdm.MetadataManage.projectManage.service.ProjectViewService;
import com.clinbrain.bd.mdm.common.core.util.R;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 业务视图
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectView")
@CrossOrigin
public class ProjectViewController {

    private ProjectViewService projectViewService;

    private ProjectDataService projectDataService;

    @GetMapping("/tree")
    public R getBusinessViewTree() throws Exception {
        return projectViewService.getProjectViewTree();
    }

    @GetMapping("/public/tree")
    public R getBusinessViewTree(String client) throws Exception {
        return projectViewService.getProjectViewTree(client);
    }

    @GetMapping("/info")
    public R<IPage<DataElement>> getProjectDataElement(Page<DataElement> page, ProjectViewTree tree, String queryString) throws Exception {
        return projectViewService.getProjectDataElement(page, tree, queryString);
    }

    @GetMapping("/public/info")
    public R<IPage<DataElement>> getProjectDataElement(Page<DataElement> page, ProjectViewTree tree, String queryString, String client) throws Exception {
        return projectViewService.getProjectDataElement(page, tree, queryString, client);
    }

    @PostMapping("/excel/{projectId}")
    public void getProjectDataElementWithData(HttpServletResponse response, @PathVariable Integer projectId, @RequestBody(required = false) List<Integer> ids) throws Exception {
        //八进制输出流
        response.setContentType("application/octet-stream");
        //这后面可以设置导出Excel的名称，此例中名为student.xls
        response.setHeader("Content-disposition", "attachment;filename=dataexcel.xls");
        //刷新缓冲
        response.flushBuffer();

        projectViewService.getProjectDataElementWithData(response, projectId, ids);
    }


    @GetMapping("/getElementRelation")
    public R<List<RelationView>> selectRelationView(Integer elementId) throws Exception {
        return projectViewService.selectRelationView(elementId);
    }

    /**
     * 保存分类详情
     *
     * @param project
     * @return
     * @author yjt
     * @date 2019/11/13 16:53
     */
    @PutMapping("/saveOrUpdate")
    public R saveOrUpdateBatch(@RequestBody ProjectDataSetCategory project) {
        if(project.getResourceId()==null||"".equalsIgnoreCase(project.getResourceId())){
            project.setResourceId(UUID.randomUUID().toString());
        }
        return new R<>(projectDataService.saveOrUpdate(project));
    }

    /**
     * 获取分类或工程详情
     *
     * @param id
     * @return
     * @author yjt
     * @date 2019/11/13 16:53
     */
    @GetMapping("/projectInfo/{id}")
    public R getProjectInfo(@PathVariable("id") Integer id) {
        ProjectDataSetCategory project = new ProjectDataSetCategory();
        project.setId(id);
        Wrapper wrapper = Wrappers.query(project);
        return new R<>(projectDataService.getOne(wrapper));
    }

    /**
     * 获取数据元的所有字段关系集合
     *
     * @param ids 数据元id集合
     * @return
     * @author yjt
     * @date 2019/11/13 16:54
     */
    @PutMapping("/elementColumnRefInfo")
    public R getElementColumnRefInfo(@RequestBody List<Integer> ids) {
        return projectDataService.getElementColumnRefInfo(ids);
    }

    /**
     * 保存工程数据详情，包括工程详情，所属数据元，每个数据元选择的对应列
     *
     * @param param
     * @return
     * @author yjt
     * @date 2019/11/13 16:54
     */
    @PutMapping("/saveProjectSetInfo")
    public R saveProjectSetInfo(@RequestBody Map<String, Object> param) {
        try {

            boolean result = true;
            boolean editFlag = false;
            if (param.get("categoryId") == null) {
                return new R<>(1, "未获取到分类ID", null);
            }
            Integer categoryId = Integer.parseInt(param.get("categoryId").toString());
            ProjectDataSetCategory dataset = JSON.parseObject(JSON.toJSONString(param.get("projectset")), ProjectDataSetCategory.class);

            List<Map<String, Object>> elementList = (List<Map<String, Object>>) param.get("elementList");

            Integer datasetId = dataset.getId();
            //有id表示是更新数据集
            if (datasetId != null) {
                editFlag = true;
            }
            //保存数据集
            if(dataset.getResourceId()==null||"".equalsIgnoreCase(dataset.getResourceId())){
                dataset.setResourceId(UUID.randomUUID().toString());
            }
            result = projectDataService.saveOrUpdate(dataset);

            //保存分类和数据集关系
            if (result && !editFlag) {
                //保存后的数据集ID
                datasetId = dataset.getId();
                ProjectDataSetRef categoryDataSetRef = new ProjectDataSetRef();
                categoryDataSetRef.setCategoryId(categoryId);
                categoryDataSetRef.setDatasetId(datasetId);
                List<ProjectDataSetRef> categoryDataSetRefList = new ArrayList<>();
                categoryDataSetRefList.add(categoryDataSetRef);
                result = projectDataService.saveProjectDataSetRef(categoryDataSetRefList);
            }

            //保存数据集与数据元关系
            if (result) {
                //更新前先删除关系
                if (editFlag) {
                    result = projectDataService.deleteElementRef(datasetId);
                    if (result) projectDataService.deleteProjectElementColumnRef(datasetId);
                }

                if (result) {
                    List<ProjectElementRef> projectElementRefList = new ArrayList<>();

                    List<ProjectElementColumnRef> elementColumnList = new ArrayList<>();
                    if (elementList != null && elementList.size() > 0) {
                        for (Map<String, Object> f : elementList) {
                            ProjectElementColumnRef refObject = new ProjectElementColumnRef();
                            refObject = BeanUtils.mapToBean(f, ProjectElementColumnRef.class);
                            ProjectElementRef ref = new ProjectElementRef();
                            ref.setDatasetId(datasetId);
                            ref.setElementId(refObject.getElementId());
                            projectElementRefList.add(ref);

                            ProjectElementColumnRef c = new ProjectElementColumnRef();
                            c.setDatasetId(datasetId);
                            c.setRefId(refObject.getRefId());
                            c.setCondition(refObject.getCondition() != null ? refObject.getCondition() : "");
                            elementColumnList.add(c);
                        }
                        result = projectDataService.saveProjectElementRef(projectElementRefList);
                        if (result) result = projectDataService.saveProjectElementColumnRef(elementColumnList);
                    }
                }
            }

            return new R<>(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new R<>(1, "保存数据集异常", null);
        }
    }

    /**
     * 获取一个工程的所属数据元和每个数据元对应的列
     *
     * @param id 工程id
     * @return
     * @author yjt
     * @date 2019/11/13 16:55
     */
    @GetMapping("/projectElementInfo/{id}")
    public R getProjectElementInfo(@PathVariable("id") Integer id) {
        return projectDataService.getProjectElementInfo(id);
    }

    @PutMapping("/deleteProjectInfo")
    public R deleteProjectInfo(@RequestBody ProjectDataSetCategory project) {
        boolean result = true;
        result = projectDataService.removeById(project.getId());
        if (!result) {
            return new R(result);
        }

        if ("caetgory".equalsIgnoreCase(project.getCategory())) {
            //删除分类和工程关系
            result = projectDataService.deleteCategoryProjectRef(project.getId());
        } else if ("project".equalsIgnoreCase(project.getCategory())) {
            //删除工程和数据元关系、工程和数据元对应字段关系
            result = projectDataService.deleteElementRef(project.getId());
            if (result) {
                result = projectDataService.deleteProjectElementColumnRef(project.getId());
            }
        }

        return new R(result);
    }


    /**
     * 根据项目标识和数据元表示查询字段和表
     *
     * @param datasetId 项目标识
     * @param elementId 数据元表示
     * @return 字段信息
     */
    @GetMapping("/getProjectCol")
    public R selectProjectColumn(Integer datasetId, Integer elementId) {
        return projectViewService.selectProjectColumn(datasetId, elementId);
    }

    /**
     * 根据项目标识和数据元表示查询字段和表
     *
     * @param datasetId 项目标识
     * @param elementId 数据元表示
     * @return 字段信息
     */
    @GetMapping("/public/getProjectCol")
    public R selectProjectColumn(Integer datasetId, Integer elementId, String client) {
        return projectViewService.selectProjectColumn(datasetId, elementId, client);
    }

    @GetMapping("/getElementList")
    public R getElementList(String queryString, String type) {
        return projectDataService.getElementList(queryString, type);
    }
}
