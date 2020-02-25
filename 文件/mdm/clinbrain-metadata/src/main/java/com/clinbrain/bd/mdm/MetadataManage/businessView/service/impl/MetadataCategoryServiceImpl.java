package com.clinbrain.bd.mdm.MetadataManage.businessView.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clinbrain.bd.mdm.MetadataManage.businessView.entity.*;
import com.clinbrain.bd.mdm.MetadataManage.businessView.mapper.MetadataSetCategoryMapper;
import com.clinbrain.bd.mdm.MetadataManage.businessView.service.MetadataCategoryService;
import com.clinbrain.bd.mdm.common.core.util.R;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


@Slf4j
@AllArgsConstructor
@Getter
@Service
public class MetadataCategoryServiceImpl extends ServiceImpl<MetadataSetCategoryMapper, MetadataSetCategory> implements MetadataCategoryService {

    private MetadataSetCategoryMapper metadataSetCategoryMapper;

    /**
     * 加载分类/数据集树
     *
     * @return
     * @author yjt
     * @date 2019/10/12 18:10
     */
    @Override
    public R getMetadataSetCategoryTree() {
        /*查询所有的分类  以及分类下的数据集*/
        List<BusinessViewTree> treeList = metadataSetCategoryMapper.getBusinessViewTreeList();
        /*循环将数据集放到子节点中 按照 id */
        List<BusinessViewTree> trees = new ArrayList<>();
        BusinessViewTree tree = null;
        Integer categoryId = null;
        for (BusinessViewTree t : treeList) {
            if (!t.getId().equals(categoryId)) {
                categoryId = t.getId();
                tree = new BusinessViewTree();

                tree.setId(t.getId());
                tree.setParentId(t.getParentId());
                tree.setNameCn(t.getNameCn());
                tree.setNameEn(t.getNameEn());
                tree.setNodeType("category");
                tree.setResourceId(t.getResourceId());
                tree.setSubResourceId(t.getSubResourceId());

                trees.add(tree);
            }
            if (t.getDatasetId() != null) { //说明是父分类或者没有子集
                BusinessViewTree node = new BusinessViewTree();
                node.setDatasetResid(t.getDatasetResid());
                node.setResourceId(t.getSubResourceId());
                node.setSubResourceId(t.getSubResourceId());
                node.setId(t.getDatasetId());
                node.setNodeType("dataset");
                node.setNameCn(t.getDatasetNameCn());
                node.setNameEn(t.getDatasetNameEn());

                tree.getChildren().add(node);
            }
        }
        //双重循环构造树
        List<BusinessViewTree> topTrees = new ArrayList<>();
        for (BusinessViewTree t : trees) {
            for (BusinessViewTree n : trees) {
                if (t.getId().equals(n.getParentId())) {
                    n.setResourceId(n.getSubResourceId());
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
    public R getMetadataSetCategoryTree(String roleId) {
        /*查询所有的分类  以及分类下的数据集*/
        List<BusinessViewTree> treeList = metadataSetCategoryMapper.getBusinessViewTreeListByRoleId(roleId);
        /*循环将数据集放到子节点中 按照 id */
        List<BusinessViewTree> trees = new ArrayList<>();
        BusinessViewTree tree = null;
        Integer categoryId = null;
        for (BusinessViewTree t : treeList) {
            if (!t.getId().equals(categoryId)) {
                categoryId = t.getId();
                tree = new BusinessViewTree();

                tree.setId(t.getId());
                tree.setParentId(t.getParentId());
                tree.setNameCn(t.getNameCn());
                tree.setNameEn(t.getNameEn());
                tree.setNodeType("category");
                tree.setResourceId(t.getResourceId());

                trees.add(tree);
            }
            if (t.getDatasetId() != null) { //说明是父分类或者没有子集
                BusinessViewTree node = new BusinessViewTree();

                node.setResourceId(t.getDatasetResid());
                node.setId(t.getDatasetId());
                node.setNodeType("dataset");
                node.setNameCn(t.getDatasetNameCn());
                node.setNameEn(t.getDatasetNameEn());

                tree.getChildren().add(node);
            }
        }
        //双重循环构造树
        List<BusinessViewTree> topTrees = new ArrayList<>();
        for (BusinessViewTree t : trees) {
            for (BusinessViewTree n : trees) {
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
    public IPage<MetadataSetCategory> getChildCategoryList(Page<MetadataSetCategory> page, MetadataSetCategory metadataSetCategory, String queryString) {
        try {
            if (metadataSetCategory == null) {
                return null;
            }

            Integer categoryId = metadataSetCategory.getId();
            IPage<MetadataSetCategory> list = metadataSetCategoryMapper.getChildCategoryList(page, categoryId, queryString);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取子集异常。", e);
            return null;
        }
    }

    /**
     * 获取数据元
     *
     * @param page
     * @param metadataSetCategory
     * @return
     * @author yjt
     * @date 2019/10/12 18:11
     */
    @Override
    public IPage<MetadataElement> getPageMetaDataElement(Page<MetadataElement> page, MetadataSetCategory metadataSetCategory, String queryString) {
        try {
            if (metadataSetCategory == null) {
                return null;
            }
            Integer id = metadataSetCategory.getId();
            String type = metadataSetCategory.getCategory();
            IPage<MetadataElement> list = new Page<>();
            List<Integer> ids = new ArrayList<>();
            if ("dataset".equalsIgnoreCase(type)) {
                //根据数据集id获取数据元
                ids.add(id);
                list = metadataSetCategoryMapper.getPageMetaDataElement(page, ids, queryString);
            } else if ("category".equalsIgnoreCase(type)) {
                /*查询所有的分类  以及分类下的数据集*/
                List<BusinessViewTree> treeList = metadataSetCategoryMapper.getBusinessViewTreeList();
                /*循环将数据集放到子节点中 按照 id */
                List<BusinessViewTree> trees = new ArrayList<>();
                BusinessViewTree tree = null;
                Integer categoryId = null;
                for (BusinessViewTree t : treeList) {
                    if (!t.getId().equals(categoryId)) {
                        categoryId = t.getId();
                        tree = new BusinessViewTree();

                        tree.setId(t.getId());
                        tree.setParentId(t.getParentId());
                        tree.setNodeType("category");
                        trees.add(tree);
                    }
                    if (t.getDatasetId() != null) { //说明是父分类或者没有子集
                        BusinessViewTree node = new BusinessViewTree();

                        node.setId(t.getDatasetId());
                        node.setNodeType("dataset");
                        tree.getChildren().add(node);
                    }
                }
                //双重循环构造树并截取子树
                List<BusinessViewTree> topTrees = new ArrayList<>();
                for (BusinessViewTree t : trees) {
                    for (BusinessViewTree n : trees) {
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

               /*

                //根据分类获取数据元
                List<BusinessViewTree> treeList = metadataSetCategoryMapper.getBusinessViewTreeList();
                for(BusinessViewTree node : treeList){
                    if(node.getId()==id||node.getParentId()==id){
                        if(!ids.contains(node.getDatasetId())){
                            ids.add(node.getDatasetId());
                        }
                    }
                }

               List<BusinessViewTree> treeListTemp= treeList.stream().filter(t -> t.getParentId() == id).collect(Collectors.toList());
               for (BusinessViewTree n : treeListTemp) {
                   for(BusinessViewTree node : treeList){
                       if(n.getId()==node.getParentId()){
                           if (!ids.contains(node.getDatasetId())) {
                               ids.add(node.getDatasetId());
                           }
                       }
                   }
               }*/
                list = metadataSetCategoryMapper.getPageMetaDataElement(page, ids, queryString);
            } else {
                return null;
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取子集或数据元异常。", e);
            return null;
        }
    }

    /**
     * 获取数据元
     *
     * @param page
     * @param metadataSetCategory
     * @return
     * @author yjt
     * @date 2019/10/12 18:11
     */
    @Override
    public IPage<MetadataElement> getPageMetaDataElement(Page<MetadataElement> page, MetadataSetCategory metadataSetCategory, String queryString, String roleId) {
        if (metadataSetCategory == null) {
            return null;
        }
        Integer id = metadataSetCategory.getId();
        String type = metadataSetCategory.getCategory();
        IPage<MetadataElement> list = new Page<>();
        List<Integer> ids = new ArrayList<>();
        if ("dataset".equalsIgnoreCase(type)) {
            //根据数据集id获取数据元
            ids.add(id);
            list = metadataSetCategoryMapper.getPageMetaDataElementByRoleId(page, ids, queryString, roleId);
        } else if ("category".equalsIgnoreCase(type)) {
            /*查询所有的分类  以及分类下的数据集*/
            List<BusinessViewTree> treeList = metadataSetCategoryMapper.getBusinessViewTreeListByRoleId(roleId);
            /*循环将数据集放到子节点中 按照 id */
            List<BusinessViewTree> trees = new ArrayList<>();
            BusinessViewTree tree = null;
            Integer categoryId = null;
            for (BusinessViewTree t : treeList) {
                if (!t.getId().equals(categoryId)) {
                    categoryId = t.getId();
                    tree = new BusinessViewTree();

                    tree.setId(t.getId());
                    tree.setParentId(t.getParentId());
                    tree.setNodeType("category");
                    trees.add(tree);
                }
                if (t.getDatasetId() != null) { //说明是父分类或者没有子集
                    BusinessViewTree node = new BusinessViewTree();

                    node.setId(t.getDatasetId());
                    node.setNodeType("dataset");
                    tree.getChildren().add(node);
                }
            }
            //双重循环构造树并截取子树
            List<BusinessViewTree> topTrees = new ArrayList<>();
            for (BusinessViewTree t : trees) {
                for (BusinessViewTree n : trees) {
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
            list = metadataSetCategoryMapper.getPageMetaDataElementByRoleId(page, ids, queryString, roleId);
        } else {
            return null;
        }

        return list;
    }

    private void loopTreeGetDatasetIds(List<BusinessViewTree> topTrees, List<Integer> ids) {
        for (BusinessViewTree top : topTrees) {
            if (StringUtils.equalsIgnoreCase(top.getNodeType(), "dataset")) {
                ids.add(top.getId());
            }
            if (top.getChildren() != null && !top.getChildren().isEmpty()) {
                loopTreeGetDatasetIds(top.getChildren(), ids);
            }
        }
    }

    /**
     * @param categoryId
     * @return
     * @author yjt
     * @date 2019/10/14 17:26
     */
    @Override
    public R getCategoryInfo(Integer categoryId) {
        try {
            MetadataSetCategory category = new MetadataSetCategory();
            category.setId(categoryId);
            Wrapper wrapper = Wrappers.query(category);
            return new R(metadataSetCategoryMapper.selectOne(wrapper));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取分类、数据集信息异常。", e);
            return null;
        }
    }

    @Override
    public R getCategoryInfo(MetadataSetCategory category) {
        try {
            Wrapper wrapper = Wrappers.query(category);
            return new R(metadataSetCategoryMapper.selectOne(wrapper));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取分类、数据集信息异常");
            return null;
        }
    }

    @Override
    public R getElementList(String queryString, String type) {
        try {
            if ("element".equalsIgnoreCase(type)) {
                //直接搜索所有数据元
                List<MetadataElement> list = metadataSetCategoryMapper.getElementList(queryString, null);
                return new R(list);
            } else if ("dataset".equalsIgnoreCase(type)) {
                //搜索的是数据集下的数据元
                List<MetadataElement> list = metadataSetCategoryMapper.getElementListByDataSet(queryString);
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

    @Override
    public boolean saveCategoryRef(List<CategoryDataSetRef> categoryDataSetRefList) {
        try {
            metadataSetCategoryMapper.saveCatgoryRef(categoryDataSetRefList);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("保存分类、数据集关系异常。", e);
            return false;
        }
    }

    @Override
    public boolean saveDataSetRef(List<DataSetElementRef> dataSetElementRefList) {
        try {
            metadataSetCategoryMapper.saveDataSetRef(dataSetElementRefList);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("保存数据集、数据元关系异常。", e);
            return false;
        }
    }

    @Override
    public boolean deleteElementRef(Integer datasetId) {
        try {
            if (datasetId == null) {
                return false;
            }
            metadataSetCategoryMapper.deleteElementRef(datasetId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("删除数据集、数据元关系异常。", e);
            return false;
        }
    }

    @Override
    public R getElementInfo(Page<ElementRange> page, MetadataElement element, String url) {
        try {
            List<MetadataElement> list = metadataSetCategoryMapper.getElementList(null, element.getId());
            if (list == null || list.size() < 1) {
                return null;
            }

            //值域编码，需要据此获rangeMap取主数据中的值域信息
            MetadataElement ele = list.get(0);
            if (ele.getAllowableCode() == null || "".equalsIgnoreCase(ele.getAllowableCode().trim())) {
                return new R(ele);
            }

            Map<String, Object> rangeMap = getRangeInfoFromService(page, ele, url);
            ele.setRangeList(rangeMap);
            return new R(ele);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取数据元详情异常。", e);
            return null;
        }
    }

    @Override
    public R getElementRangeList(Page<ElementRange> page, MetadataElement element, String url) {
        try {
            Map<String, Object> rangeList = getRangeInfoFromService(page, element, url);
            return new R(rangeList);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("通过接口获取值域信息异常。", e);
            return null;
        }
    }

    @Override
    public R getMetaCategoryLazzyTree(BusinessViewTree treeNode, String queryString) {
        try {

            if ("category".equalsIgnoreCase(treeNode.getNodeType())) {
                //传参是分类时，获取子类，数据集
                List<BusinessViewTree> list = new ArrayList<>();
                list = metadataSetCategoryMapper.getCategotyLazzyTree(treeNode.getId(), queryString);
                for (BusinessViewTree node : list) {
                    node.setResourceId(UUID.randomUUID() + "");
                }
                return new R(list);
            } else if ("dataset".equalsIgnoreCase(treeNode.getNodeType())) {
                //传参是数据集时，获取数据元
                return new R(metadataSetCategoryMapper.getElementListLazzyTree(treeNode.getId(), queryString));
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("懒加载获取分类数据集、数据元异常。", e);
            return null;
        }
    }

    /**
     * 从主数据接口获取值域信息
     *
     * @param page    分页
     * @param element 数据元
     * @return
     * @author yjt
     * @date 2019/10/22 13:51
     */
    private Map<String, Object> getRangeInfoFromService(Page<ElementRange> page, MetadataElement element, String serverUrl) {
        try {
            String rangeId = element.getAllowableCode();
            String uri = serverUrl;
            String fromFlag = "MDM";
            String defaultVersion = "1.0";
            Long pageNum = page.getCurrent();
            Long pageSize = page.getSize();
            String checkSum = "";
            String dataFormat = "JSON";
            StringBuffer builder = new StringBuffer();
            builder.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            builder.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
            builder.append("<soap:Body>");
            builder.append("<DictionaryQuery xmlns=\"http://tempuri.org/\">");
            builder.append("<From>" + fromFlag + "</From>");
            builder.append("<DataFormat >" + dataFormat + "</DataFormat>");
//            builder.append("<Query>");
            builder.append("<Dictionary>");
            builder.append("<Code>" + rangeId + "</Code>");
            builder.append("<Version>" + defaultVersion + "</Version>");
            builder.append("</Dictionary>");
            builder.append("<PageInfo>");
            builder.append("<Page>" + pageNum + "</Page>");
            builder.append("<Size>" + pageSize + "</Size>");
            builder.append("</PageInfo>");
//            builder.append("</Query>");
            //builder.append("<Timestamp>" + LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli() + "</Timestamp>");
            builder.append("<Timestamp>" + 10 + "</Timestamp>");
            builder.append("<Checksum>" + checkSum + "</Checksum>");
            builder.append("</DictionaryQuery>");
            builder.append("</soap:Body>");
            builder.append("</soap:Envelope>");

            URL url = new URL(uri);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.write(builder.toString().getBytes("utf-8"));
            dos.flush();

//            URLConnection conn = url.openConnection();
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            OutputStreamWriter dos = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
//            dos.write(builder.toString());
//            dos.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line = null;
            StringBuffer strBuf = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                strBuf.append(line);
            }

            dos.close();
            reader.close();
            String rs = strBuf.toString();
            Map<String, Object> resultMap = new HashMap<>();
            if (rs != null) {
                resultMap = getRangeList(rs);
            }
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("通过接口获取值域信息异常。", e);
            return null;
        }
    }

    /**
     * 解析接口返回值，获取值域信息
     *
     * @param resultStr
     * @return
     * @author yjt
     * @date 2019/10/22 11:45
     */
    private Map<String, Object> getRangeList(String resultStr) {
        try {
            Map<String, Object> dataMap = new HashMap<>();
            Document doc = DocumentHelper.parseText(resultStr);//报文转成doc对象
            Element root = doc.getRootElement();
            if (root != null) {
                Element dataElement = doc.getRootElement().element("Body").element("DictionaryQueryResponse").element("DictionaryQueryResult").element("Members");
                if (dataElement != null) {
                    String dataStr = dataElement.getText();
                    log.debug("接口返回的数据：" + dataStr);
                    JSONArray jsonArray = JSONObject.parseArray(dataStr);
                    if (jsonArray != null && jsonArray.size() > 0) {
                        dataMap.put("Members", jsonArray);
                    }
                }
            }

            return dataMap;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("解析主数据接口返回值异常。", e);
            return null;
        }
    }

    @Override
    public boolean deleteCategoryDataSetRef(Integer id) {
        try {
            if (id == null) {
                return false;
            }
            metadataSetCategoryMapper.deleteCategoryDataSetRef(id);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
