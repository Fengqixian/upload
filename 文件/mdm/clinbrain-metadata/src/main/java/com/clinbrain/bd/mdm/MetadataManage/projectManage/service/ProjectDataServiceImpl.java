package com.clinbrain.bd.mdm.MetadataManage.projectManage.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clinbrain.bd.mdm.MetadataManage.businessView.entity.DataElement;
import com.clinbrain.bd.mdm.MetadataManage.businessView.entity.MetadataElement;
import com.clinbrain.bd.mdm.MetadataManage.projectManage.entity.*;
import com.clinbrain.bd.mdm.MetadataManage.projectManage.mapper.ProjectDataMapper;
import com.clinbrain.bd.mdm.common.core.util.R;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Getter
@Service
public class ProjectDataServiceImpl extends ServiceImpl<ProjectDataMapper, ProjectDataSetCategory> implements ProjectDataService {

    private ProjectDataMapper projectDataMapper;

    @Override
    public boolean saveProjectDataSetRef(List<ProjectDataSetRef> projectDataSetRefList) {
        try {
            projectDataMapper.saveProjectDataSetRef(projectDataSetRefList);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("保存分类、数据集关系异常。", e);
            return false;
        }
    }

    @Override
    public boolean saveProjectElementRef(List<ProjectElementRef> projectElementRefList) {
        try {
            projectDataMapper.saveProjectElementRef(projectElementRefList);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("保存分类、数据集关系异常。", e);
            return false;
        }
    }

    @Override
    public boolean deleteCategoryProjectRef(Integer id){
        try{
            if(id ==null){
                return false;
            }

            projectDataMapper.deleteCategoryProjectRef(id);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteElementRef(Integer datasetId) {
        try {
            if (datasetId == null) {
                return false;
            }
            projectDataMapper.deleteElementRef(datasetId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("删除数据集、数据元关系异常。", e);
            return false;
        }
    }

    @Override
    public boolean deleteProjectElementColumnRef(Integer datasetId) {
        try {
            if (datasetId == null) {
                return false;
            }
            projectDataMapper.deleteProjectElementColumnRef(datasetId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean saveProjectElementColumnRef(List<ProjectElementColumnRef> projectElementColumnRefList) {
        try {
            if (projectElementColumnRefList == null || projectElementColumnRefList.size() < 1) {
                return false;
            }
            projectDataMapper.saveProjectElementColumnRef(projectElementColumnRefList);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public R getElementColumnRefInfo(List<Integer> ids) {
        try {
            if (ids == null || ids.size() < 1) {
                return null;
            }
            List<ProjectElementColumnRef> list = projectDataMapper.getElementColumnRefInfo(ids);
            Map<Integer, List<ProjectElementColumnRef>> refList = list.stream().collect(Collectors.groupingBy(ProjectElementColumnRef::getElementId));
            return new R(refList);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public R getProjectElementInfo(Integer id) {
        try {
            //获取一个工程的所有关联元数据和每个元数据对应的列关系
            ProjectDataSetCategory dataset = new ProjectDataSetCategory();
            dataset = projectDataMapper.selectById(id);

            //该数据集下的所有数据元
            List<MetadataElement> elementList = projectDataMapper.getProjectElementList(id);

            //每个数据元对应的所有字段集合
            List<ProjectElementColumnRef> allRefList = projectDataMapper.getElementColumnList(id);
            Map<Integer, List<ProjectElementColumnRef>> allRefMap = allRefList.stream().collect(Collectors.groupingBy(ProjectElementColumnRef::getElementId));

            //该数据集数据元选择的字段来源
            List<ProjectElementColumnRef> refList = projectDataMapper.getProjectElementColumnDetail(id);

            for (MetadataElement element : elementList) {
                if (allRefMap.containsKey(element.getId()) && allRefMap.get(element.getId()) != null) {
                    element.setOptions(allRefMap.get(element.getId()));
                }

                for (ProjectElementColumnRef ref : refList) {
                    if (ref.getElementId().equals(element.getId())) {
                        element.setRefId(ref.getRefId());
                        break;
                    }
                }
            }

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("dataset", dataset);
            resultMap.put("elementList", elementList);

            return new R(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取工程信息异常。",e);
            return null;
        }
    }

    @Override
    public R getElementList(String queryString, String type) {
        try {
            if ("element".equalsIgnoreCase(type)) {
                //直接搜索所有数据元
                List<MetadataElement> list = projectDataMapper.getElementList(queryString, null);
                return new R(list);
            } else if ("dataset".equalsIgnoreCase(type)) {
                //搜索的是数据集下的数据元
                List<MetadataElement> list = projectDataMapper.getElementListByDataSet(queryString);
                return new R(list);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("搜索数据元时异常", e);
            return null;
        }
    }
}
