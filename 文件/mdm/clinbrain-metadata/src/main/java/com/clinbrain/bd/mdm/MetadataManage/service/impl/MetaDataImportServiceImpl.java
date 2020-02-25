package com.clinbrain.bd.mdm.MetadataManage.service.impl;

import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.clinbrain.bd.mdm.MetadataManage.dto.ImportMetaData;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataModel;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataProperties;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataValue;
import com.clinbrain.bd.mdm.MetadataManage.service.*;
import com.clinbrain.bd.mdm.common.core.factory.ConnectionFactory;
import com.clinbrain.bd.mdm.common.core.util.ConnectionParam;
import com.clinbrain.bd.mdm.common.core.util.DataJDBCUtil;
import com.clinbrain.bd.mdm.common.core.util.DbTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class MetaDataImportServiceImpl implements MetaDataImportService {

    @Autowired
    private MetadataModelService metadataModelService;

    /**
     * 模型属性
     */
    @Autowired
    private MetadataPropertiesService metadataPropertiesService;

    @Autowired
    private MetadataValueService metadataValueService;

    @Autowired
    private MetadataReferenceService referenceService;

    /**
     * 获取数据库连接结果
     *
     * @param jsonParam 参数集合
     * @return 数据库连接结果
     */
    public boolean getServerConnection(String jsonParam) {
        try {
            Map<String, Object> paramMap = JSON.parseObject(jsonParam);
            return DataJDBCUtil.getConnectionResult(paramMap);
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 导入元数据
     *
     * @return 导入元数据结果
     */
    @Transactional
    public boolean importMetaData(ImportMetaData metaData) {
        boolean toValue = false;
        try {
            //数据类型
            String metaDataType = metaData.getMetaDataType();
            String moduleID = metaData.getModelId();

            switch (metaDataType) {
                case "数据库":
                case "database":
                    //依次导出数据库下的所有表和视图
                    toValue = importMetaDataFromDataBase(metaData);
                    break;
                case "表":
                case "table":
                    //依次导出数据库下的所有表和视图
                    toValue = importMetaDataFromDataBase(metaData);
                    break;
                case "视图":
                case "view":
                    String tableName = metaData.getTableName();
                    toValue = importView(metaData, moduleID, tableName);
                    break;
            }
            return toValue;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 导入数据库的元数据
     *
     * @return 导入数据库和实例的结果
     */
    @Transactional
    public boolean importMetaDataFromDataBase(ImportMetaData metaData) {
        boolean result = false;
        try {
            List<MetadataModel> modelList = metadataModelService.list(Wrappers.<MetadataModel>query().lambda()
                    .eq(MetadataModel::getParentResourceId, metaData.getModelId()));
            String tableModelID = "";
            String viewModelID = "";

            for (MetadataModel model : modelList) {
                if (model.getNameCn().equals("表")) {
                    tableModelID = model.getResourceId();
                } else if (model.getNameCn().equals("视图")) {
                    viewModelID = model.getResourceId();
                }
            }
            //获取表的模型信息
            List<MetadataProperties> propertiesList = metadataPropertiesService.list(Wrappers.<MetadataProperties>query().lambda().eq(MetadataProperties::getModelResourceId, tableModelID));
            String tableName = metaData.getTableName();
            List<Map<String, Object>> tableList = getTableInfoList(metaData, null, "TABLE", propertiesList, tableName);

            if (propertiesList != null && propertiesList.size() > 0) {
                for (Map<String, Object> table : tableList) {
                    //循环调用导入表的方法，导入表的元数据
                    MetadataValue metadataValue = new MetadataValue();

                    metadataValue.setModelId(tableModelID);
                    metadataValue.setCreateTime(LocalDateTime.now());
                    metadataValue.setNameCn(table.get("table_name") + "");
                    metadataValue.setNameEn(table.get("table_name") + "");
                    metadataValue.setParentId(metaData.getResourceId());
                    metadataValue.setResourceId(UUID.randomUUID().toString());
                    metadataValue.setModelType("table");
                    metaData.setTableName(metadataValue.getNameEn());

                    result = importMetaDataFromTable(metaData, metadataValue);

                }
            }
            if (result && "database".equalsIgnoreCase(metaData.getMetaDataType())) {
                //暂不支持导入视图
                //return importView(metaData, viewModelID, tableName);
            }
            comParam = null;
            return result;
        } catch (Exception e) {
            return false;
        } finally {
        }
    }

    /**
     * 导入视图
     *
     * @param viewModelId 模型标识
     * @param tableName   表名
     * @return
     */
    private boolean importView(ImportMetaData metaData, String viewModelId, String tableName) {
        try {
            //导入视图
            List<MetadataProperties> propertiesList = metadataPropertiesService.list(Wrappers.<MetadataProperties>query().lambda().eq(MetadataProperties::getModelResourceId, viewModelId));
            List<Map<String, Object>> viewList = getTableInfoList(metaData, null, "VIEW", propertiesList, tableName);
            boolean toValue = true;
            if (viewList != null && viewList.size() > 0) {
                for (Map<String, Object> view : viewList) {
                    MetadataValue metadataValue = new MetadataValue();
                    //循环调用导入表的方法，导入视图的元数据
                    metadataValue.setModelId(viewModelId);
                    metadataValue.setCreateTime(LocalDateTime.now());
                    metadataValue.setNameCn(view.get("view_name") + "");
                    metadataValue.setNameEn(view.get("view_name") + "");
                    metadataValue.setParentId(metaData.getResourceId());
                    metadataValue.setResourceId(UUID.randomUUID().toString());
                    metadataValue.setModelType("view");
                    toValue = importMetaDataFromTable(metaData, metadataValue);

                }
            }
            return toValue;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取表信息集合
     *
     * @param type 类型 ，表或视图  TABLE  VIEW
     * @return Map集合
     */
    public List<Map<String, Object>> getTableInfoList(ImportMetaData metaData, Page page, String type, List<MetadataProperties> metaModelList, String tableName) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            if (metaModelList == null || metaModelList.size() < 1) {
                return null;
            }

            String modelId = metaData.getModelId();
            String resourceId = metaData.getResourceId();
            //if (comParam == null)
            this.showConn(modelId, resourceId);

            conn = ConnectionFactory.getConnectionFactory().createConnection(comParam);
            String sql = String.format("SELECT * from information_schema.TABLES t WHERE  t.TABLE_TYPE ='BASE TABLE'");
            if (type.equalsIgnoreCase("view")) {
                sql = String.format("SELECT * from information_schema.VIEWS t WHERE 1=1 ");
            }
            if (comParam.getDbType().getValue().equalsIgnoreCase("mysql")) {
                sql = String.format(sql + " and t.table_schema ='%s'", comParam.getDatabase());
            }

            if(comParam.getDbType().getValue().equalsIgnoreCase("gp")){
                sql = String.format(sql + " and t.table_schema ='%s'", "public");
            }

            if (metaData.getTableName() != null && !"".equalsIgnoreCase(metaData.getTableName())) {
                sql = String.format(sql + " and t.table_name='%s'", metaData.getTableName());
            }

            preparedStatement = conn.prepareStatement(sql);
            if (page != null && page.getSize() > -1) {
                //设置最大查询到第几条记录
                preparedStatement.setMaxRows((int) page.getCurrent() * (int) page.getSize());
            }

            rs = preparedStatement.executeQuery();
            if (page != null && page.getSize() > -1) {
                //游标移动到要输出的第一条记录
                rs.relative(((int) page.getCurrent() - 1) * (int) page.getSize());
            }
            int i = 0;
            while (rs.next()) {
                Map m = new HashMap();

                //从元模型中依次取名称，根据名称取列描述值
                for (MetadataProperties properties : metaModelList) {
                    //数据属性的属性名
                    try {
                        String value = "";
                        if (properties.getNameEn().equalsIgnoreCase("table_name") || properties.getNameEn().equalsIgnoreCase("view_name")) {
                            value = rs.getString("TABLE_NAME") != null ? rs.getString("TABLE_NAME") : "";

                            if (type.equalsIgnoreCase("table")) {
                                m.put("table_name", value);
                                m.put("nameCn", value);
                                m.put("nameEn", value);
                                m.put("uuid", Base64Utils.encodeToString((value + i + "-" + "table").getBytes()));
                            } else if (type.equalsIgnoreCase("view")) {
                                m.put("view_name", value);
                                m.put("nameCn", value);
                                m.put("nameEn", value);
                                m.put("uuid", Base64Utils.encodeToString((value + i + "-" + "view").getBytes()));
                            }
                            m.put("modelType",type.toLowerCase());
                        } else {
                            value = rs.getString(properties.getNameEn().toUpperCase()) != null ? rs.getString(properties.getNameEn().toUpperCase()) : "";
                            m.put(properties.getNameEn(), value);
                        }
                    } catch (Exception e) {

                    }
                }
                i++;
                m.put("database_id", comParam.getDatabase());
                result.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 导入表(视图)的元数据
     *
     * @return 保存表的结果
     */
    private boolean importMetaDataFromTable(ImportMetaData metaData, MetadataValue dataInfo) {
        try {
            //表名称
            String metaDataName = metaData.getTableName();
            if (metaDataName == null || metaDataName.equalsIgnoreCase("")) {
                return false;
            }

            //已经导入的表不再导入，根据英文名和父id查找
            MetadataValue checkValue = new MetadataValue();
            if (dataInfo.getNameEn() != null && !"".equalsIgnoreCase(dataInfo.getNameEn().trim())) {
                checkValue.setNameEn(dataInfo.getNameEn());
            }

            if (dataInfo.getParentId() != null && !"".equalsIgnoreCase(dataInfo.getParentId().trim())) {
                checkValue.setParentId(dataInfo.getParentId());
            }
            List<MetadataValue> checkList = metadataValueService.getMetaDataListByName(checkValue);
            if (checkList != null && checkList.size() > 0) {
                //将当前信息替换
                dataInfo = checkList.get(0);
            }else{
                //导入表信息
                boolean value = referenceService.saveMetaDataValueInfo(dataInfo);//  metadataValueService.save(dataInfo);
                if (!value) {
                    return value;
                }
            }

            //获取当前要导入的数据模型ID
            String moduleId = dataInfo.getModelId();

            //获取表的子模型
            List<MetadataModel> childModuleList = metadataModelService.list(Wrappers.<MetadataModel>query().lambda().eq(MetadataModel::getParentResourceId, moduleId));

            //导入子节点
            boolean toValue = false;
            if (childModuleList != null && childModuleList.size() > 0) {
                for (MetadataModel child : childModuleList) {

                    String childModelID = child.getResourceId();
                    String childModelName = child.getNameEn();
                    //将对象改为子模型
                    metaData.setModelId(childModelID);
                    metaData.setParentMetaDataId(moduleId);

                    List<Map<String, Object>> childList = new ArrayList<Map<String, Object>>();
                    List<MetadataProperties> propertiesList = metadataPropertiesService.list(Wrappers.<MetadataProperties>query().lambda().eq(MetadataProperties::getModelResourceId, childModelID));
                    switch (childModelName) {
                        case "column":
                            //获取表下面的列
                            childList = getTableBySubInfo(metaData, "column", metaData.getTableName(), propertiesList);

                            if (childList != null && childList.size() > 0) {
                                for (Map<String, Object> map : childList) {
                                    Map<String, Object> obMap = new HashMap<>();
                                    for (MetadataProperties properties : propertiesList) {

                                        if ("column_name".equalsIgnoreCase(properties.getNameEn())) {
                                            obMap.put("nameEn", map.get(properties.getNameEn()) != null ? map.get(properties.getNameEn()) + "" : "");
                                            obMap.put(properties.getMappingField(), map.get(properties.getNameEn()) != null ? map.get(properties.getNameEn()) + "" : "");
                                        } else if ("column_comment".equalsIgnoreCase(properties.getNameEn())) {
                                            obMap.put("nameCn", map.get(properties.getNameEn()) != null ? map.get(properties.getNameEn()) + "" : "");
                                        } else {
                                            obMap.put(properties.getMappingField(), map.get(properties.getNameEn()) != null ? map.get(properties.getNameEn()) + "" : "");
                                        }
                                    }

                                    MetadataValue metadataValue = BeanUtils.mapToBean(obMap, MetadataValue.class);

                                    //循环调用导入表的方法，导入表的元数据
                                    metadataValue.setModelId(childModelID);
                                    metadataValue.setCreateTime(LocalDateTime.now());
                                    metadataValue.setParentId(dataInfo.getResourceId());
                                    metadataValue.setResourceId(UUID.randomUUID().toString());
                                    metadataValue.setModelType("column");
                                    //导入表信息
                                    toValue = referenceService.saveMetaDataValueInfo(metadataValue);//  metadataValueService.save(metadataValue);
                                }
                            }
                            break;
                        case "index":
                            childList = getTableBySubInfo(metaData, "index", metaData.getTableName(), propertiesList);
                            if (childList != null && childList.size() > 0) {
                                for (Map<String, Object> map : childList) {
                                    Map<String, Object> obMap = new HashMap<>();
                                    for (MetadataProperties properties : propertiesList) {

                                        if ("index_name".equalsIgnoreCase(properties.getNameEn())) {
                                            obMap.put("nameEn", map.get(properties.getNameEn()) != null ? map.get(properties.getNameEn()) + "" : "");
                                            obMap.put(properties.getMappingField(), map.get(properties.getNameEn()) != null ? map.get(properties.getNameEn()) + "" : "");
                                        }
                                        if ("index_comment".equalsIgnoreCase(properties.getNameEn())) {
                                            obMap.put("nameCn", map.get(properties.getNameEn()) != null ? map.get(properties.getNameEn()) + "" : "");
                                        }

                                        obMap.put(properties.getMappingField(), map.get(properties.getNameEn()) != null ? map.get(properties.getNameEn()) + "" : "");
                                    }

                                    MetadataValue metadataValue = BeanUtils.mapToBean(obMap, MetadataValue.class);

                                    //循环调用导入表的方法，导入表的元数据
                                    metadataValue.setModelId(childModelID);
                                    metadataValue.setCreateTime(LocalDateTime.now());
                                    metadataValue.setParentId(dataInfo.getResourceId());
                                    metadataValue.setResourceId(UUID.randomUUID().toString());
                                    metadataValue.setModelType("index");
                                    //导入表信息
                                    toValue = referenceService.saveMetaDataValueInfo(metadataValue);//metadataValueService.save(metadataValue);
                                }
                            }
                            break;
                        case "primary_key":
                            childList = getTableBySubInfo(metaData, "primary_key", metaData.getTableName(), propertiesList);
                            if (childList != null && childList.size() > 0) {
                                for (Map<String, Object> map : childList) {
                                    Map<String, Object> obMap = new HashMap<>();
                                    for (MetadataProperties properties : propertiesList) {
                                        if ("pk_name".equalsIgnoreCase(properties.getNameEn())) {
                                            obMap.put("nameEn", map.get(properties.getNameEn()) != null ? map.get(properties.getNameEn()) + "" : "");
                                            obMap.put(properties.getMappingField(), map.get(properties.getNameEn()) != null ? map.get(properties.getNameEn()) + "" : "");
                                        }
                                        if ("pk_comment".equalsIgnoreCase(properties.getNameEn())) {
                                            obMap.put("nameCn", map.get(properties.getNameEn()) != null ? map.get(properties.getNameEn()) + "" : "");
                                        }
                                        obMap.put(properties.getMappingField(), map.get(properties.getNameEn()) != null ? map.get(properties.getNameEn()) + "" : "");
                                    }

                                    MetadataValue metadataValue = BeanUtils.mapToBean(obMap, MetadataValue.class);

                                    //循环调用导入表的方法，导入表的元数据
                                    metadataValue.setModelId(childModelID);
                                    metadataValue.setCreateTime(LocalDateTime.now());
                                    metadataValue.setParentId(dataInfo.getResourceId());
                                    metadataValue.setResourceId(UUID.randomUUID().toString());
                                    metadataValue.setModelType("primary_key");
                                    //导入表信息
                                    toValue = referenceService.saveMetaDataValueInfo(metadataValue);// metadataValueService.save(metadataValue);
                                }
                            }
                            break;
                        case "foreign_key":
                            childList = getTableBySubInfo(metaData, "foreign_key", metaData.getTableName(), propertiesList);
                            if (childList != null && childList.size() > 0) {
                                for (Map<String, Object> map : childList) {
                                    Map<String, Object> obMap = new HashMap<>();
                                    for (MetadataProperties properties : propertiesList) {
                                        if ("fk_name".equalsIgnoreCase(properties.getNameEn())) {
                                            obMap.put("nameEn", map.get(properties.getNameEn()) != null ? map.get(properties.getNameEn()) + "" : "");
                                            obMap.put(properties.getMappingField(), map.get(properties.getNameEn()) != null ? map.get(properties.getNameEn()) + "" : "");
                                        }
                                        if ("fk_comment".equalsIgnoreCase(properties.getNameEn())) {
                                            obMap.put("nameCn", map.get(properties.getNameEn()) != null ? map.get(properties.getNameEn()) + "" : "");
                                        }
                                        obMap.put(properties.getMappingField(), map.get(properties.getNameEn()) != null ? map.get(properties.getNameEn()) + "" : "");
                                    }

                                    MetadataValue metadataValue = BeanUtils.mapToBean(obMap, MetadataValue.class);

                                    //循环调用导入表的方法，导入表的元数据
                                    metadataValue.setModelId(childModelID);
                                    metadataValue.setCreateTime(LocalDateTime.now());
                                    metadataValue.setParentId(dataInfo.getResourceId());
                                    metadataValue.setResourceId(UUID.randomUUID().toString());
                                    metadataValue.setModelType("foreign_key");
                                    //导入表信息
                                    toValue = referenceService.saveMetaDataValueInfo(metadataValue);//metadataValueService.save(metadataValue);
                                }
                            }
                            break;
                        case "trigger":
                            break;
                    }
                }
            }
            return toValue;
        } catch (Exception e) {
            return false;
        }
    }

    private ConnectionParam comParam = null;

    private void showConn(String modelId, String resourceId) {
        Map<String, Object> showMap = this.getDatabaseInfo(modelId, resourceId);
        String serverType = showMap.get("database_type") != null ? showMap.get("database_type").toString() : "";
        ConnectionParam param1 = new ConnectionParam();
        param1.setDatabase(showMap.get("database_name") + "");
        param1.setIpAddr(showMap.get("server_name") + "");
        param1.setPort(showMap.get("port") + "");
        param1.setDbType(DbTypeEnum.parseValue(serverType));
        param1.setUsername(showMap.get("login_name") + "");
        param1.setPassword(showMap.get("login_pwd") + "");
        comParam = param1;
    }

    /**
     * 获取数据表的列信息集合
     *
     * @param type      表或视图  TABLE   VIEW
     * @param tableName 表或视图名
     * @return Map集合
     */
    public List<Map<String, Object>> getTableBySubInfo(ImportMetaData metaData, String type, String tableName, List<MetadataProperties> metaModelList) {

        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        DatabaseMetaData dbmd;
        Connection conn = null;
        if (metaModelList == null || metaModelList.size() < 1) {
            return null;
        }
        ResultSet rs = null;
        try {
            String serverType = comParam.getDbType().toString();
            conn = ConnectionFactory.getConnectionFactory().createConnection(comParam);
            dbmd = conn.getMetaData();


            if ("column".equalsIgnoreCase(type)) {
                rs = dbmd.getColumns(null, null, tableName, "%");
            } else if ("index".equalsIgnoreCase(type)) {
                rs = dbmd.getIndexInfo(null, null, tableName, false, true);
            } else if ("primary_key".equalsIgnoreCase(type)) {
                rs = dbmd.getPrimaryKeys(null, null, tableName);
            } else if ("foreign_key".equalsIgnoreCase(type)) {
                rs = dbmd.getImportedKeys(null, null, tableName);
            }
            //手动从数据库中获取列描述  sqlserver的jdbc获取columns时描述为null
            Map<String, Object> remarkList = new HashMap<>();
            if(serverType.equalsIgnoreCase("sqlserver")){
                String dbName = comParam.getDatabase();
                remarkList = getColumnRemraks(dbName,tableName);
            }

            while (rs.next()) {
                Map m = new HashMap();
                String columnName = "";

                if ("index".equalsIgnoreCase(type)) {
                    //排除空的索引
                    try {
                        String name = rs.getString("INDEX_NAME") != null ? rs.getString("INDEX_NAME") : "";
                        String column = rs.getString("COLUMN_NAME") != null ? rs.getString("COLUMN_NAME") : "";
                        if (name.equalsIgnoreCase("") && column.equalsIgnoreCase("")) {
                            continue;
                        }
                    } catch (Exception e) {
                    }
                }

                //从元模型中依次取名称，根据名称取列描述值
                for (MetadataProperties map : metaModelList) {
                    //数据属性的属性名
                    String attName = map.getNameEn();
                    try {
                        String value = rs.getString(attName.toUpperCase()) != null ? rs.getString(attName.toUpperCase()) : "";
                        if ("column".equalsIgnoreCase(type)) {
                            if (attName.equalsIgnoreCase("column_name")) {
                                columnName = value;
                            }
                            if (attName.equalsIgnoreCase("column_size") && !value.equalsIgnoreCase("")) {
                                if (Integer.parseInt(value) > 4000) {
                                    value = "max";
                                }
                            }
                        }
                        m.put(attName, value);
                    } catch (Exception e) {

                    }
                }

                if(serverType.equalsIgnoreCase("sqlserver")){
                    m.put("remarks",remarkList.get(columnName));
                }

                //所属表
                m.put("table_id", tableName);
                result.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    /**
     * 分页获取表集合
     *
     * @return 表集合
     * @Param [jsonParam] 分页参数集合
     * @Author yuanjuntao
     * @Date 13:50
     **/
    @Override
    public IPage getTablePageList(Page page, ImportMetaData metaData) {
        try {
            IPage ipage = null;
            MetadataModel model = metadataModelService.getOne(Wrappers.<MetadataModel>query().lambda().eq(MetadataModel::getParentResourceId, metaData.getModelId()).eq(MetadataModel::getModelType, "table"));
            List<MetadataProperties> propertiesList = metadataPropertiesService.list(Wrappers.<MetadataProperties>query().lambda().eq(MetadataProperties::getModelResourceId, model.getResourceId()));
            String tableName = metaData.getTableName();
            List<Map<String, Object>> tableList = getTableInfoList(metaData, page, "TABLE", propertiesList, tableName);
            ipage = getPageList(tableList, page);
            return ipage;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 获取分页信息
     *
     * @param list
     * @param page
     * @return
     */
    private IPage getPageList(List<Map<String, Object>> list, Page page) {
        try {
            IPage ipage = page;

            if (list == null || list.size() < 1) {
                return ipage;
            }
            ipage.setTotal(list.size());

            if (page == null || page.getCurrent() > 0) {
                ipage.setRecords(list);
                return ipage;
            } else {
                list = getListByPageAjax(list, page);
                ipage.setRecords(list);
                ipage.setSize(page.getSize());
                ipage.setCurrent(page.getCurrent());
                return ipage;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据分页信息将结果分页返回
     *
     * @param allList
     * @param page
     * @return
     */
    private List<Map<String, Object>> getListByPageAjax(List<Map<String, Object>> allList, Page page) {
        try {

            if (allList == null || allList.size() < 1 || page == null) {
                return null;
            }

            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

            Integer pageNumber = Integer.valueOf(page.getCurrent() + "");
            Integer pageSize = Integer.valueOf(page.getSize() + "");

            int currIdx = (pageNumber > 1 ? (pageNumber - 1) * pageSize : 0);
            for (int i = 0; i < pageSize && i < allList.size() - currIdx; i++) {
                Map<String, Object> map = allList.get(currIdx + i);
                list.add(map);
            }

            return list;

        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 分页返回view集合
     *
     * @param metaData 分页参数集合
     * @return 视图集合
     */
    @Override
    public IPage<List<Map<String, Object>>> getViewPageList(Page page, ImportMetaData metaData) {
        try {
            IPage ipage = null;
            List<MetadataProperties> propertiesList = metadataPropertiesService.list(Wrappers.<MetadataProperties>query().lambda().eq(MetadataProperties::getNameEn, "view"));
            String tableName = metaData.getTableName();
            List<Map<String, Object>> tableList = getTableInfoList(metaData, null, "view", propertiesList, tableName);
            ipage = getPageList(tableList, page);
            return ipage;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 分页获取字段集合
     *
     * @param metaData 分页参数集合
     * @return 列集合
     */
    public IPage getSubInfoPageList(Page page, ImportMetaData metaData) {
        try {
            IPage ipage = null;
            String moduleId = metaData.getModelId();
            if (moduleId == null || moduleId.equalsIgnoreCase("")) {
                return null;
            }

            List<MetadataModel> metaModelList = metadataModelService.list(Wrappers.<MetadataModel>query().lambda().eq(MetadataModel::getParentResourceId, moduleId));
            if (metaModelList == null || metaModelList.size() < 1) {
                return null;
            }

            String dataType = metaModelList.get(0).getNameEn() != null ? metaModelList.get(0).getNameEn().toLowerCase() : "";

            if (dataType.equalsIgnoreCase("")) {
                return null;
            }

            List<Map<String, Object>> columnList = new ArrayList<>();
            List<MetadataProperties> propertiesList = metadataPropertiesService.list(Wrappers.<MetadataProperties>query().lambda().eq(MetadataProperties::getModelResourceId, moduleId));
            switch (dataType) {
                case "column":
                    //获取表下面的列
                    columnList = getTableBySubInfo(metaData, "column", metaData.getTableName(), propertiesList);
                    break;
                case "index":
                    columnList = getTableBySubInfo(metaData, "index", metaData.getTableName(), propertiesList);
                    break;
                case "primary_key":
                    columnList = getTableBySubInfo(metaData, "primary_key", metaData.getTableName(), propertiesList);
                    break;
                case "foreign_key":
                    columnList = getTableBySubInfo(metaData, "foreign_key", metaData.getTableName(), propertiesList);
                    break;
            }

            ipage = getPageList(columnList, page);
            return ipage;
        } catch (Exception e) {
            return null;
        }
    }

    private Map<String, Object> getDatabaseInfo(String modelId, String resourceId) {
        List<MetadataProperties> propertiesList = metadataPropertiesService.list(Wrappers.<MetadataProperties>query().lambda().eq(MetadataProperties::getModelResourceId, modelId));
        MetadataValue value = metadataValueService.getOne(Wrappers.<MetadataValue>query().lambda().eq(MetadataValue::getResourceId, resourceId));
        Map<String, Object> map = new HashMap<>();

        for (MetadataProperties properties : propertiesList) {
            Map<String, Object> obj = (Map<String, Object>) JSON.toJSON(value);
            map.put(properties.getNameEn(), obj.get(properties.getMappingField()));
        }
        return map;
    }

    /**
     * 获取某一列的描述
     * @param dbName 数据库名
     * @param tableName 表名
     * @return
     * @author yjt
     * @date  2018/12/17 15:44
     */
    public Map<String,Object>  getColumnRemraks(String dbName,String tableName){
        String sql= " select columnname= convert(varchar(100), a.name)," +
                " tablename= convert(varchar(50), d.name )," +
                " type= convert(varchar(50),b.name)," +
                " dbname= '"+dbName+"'," +
                " remarks=convert(varchar(50), isnull(g.[value],''))" +
                " from dbo.syscolumns a" +
                " left join dbo.systypes b on a.xusertype=b.xusertype" +
                " inner join dbo.sysobjects d on a.id=d.id and d.xtype='U' and d.name<>'dtproperties'" +
                " left join sys.extended_properties g on a.id=g.major_id and a.colid=g.minor_id" +
                " where d.name ='"+tableName+"'";
        ResultSet res = null;
        PreparedStatement statement = null;
        Map<String,Object> columns = new HashMap<>();
        try {

            if(dbName.equalsIgnoreCase("")||tableName.equalsIgnoreCase("")){
                return null;
            }
            Connection conn = null;
            conn = ConnectionFactory.getConnectionFactory().createConnection(comParam);
            statement = conn.prepareStatement(sql);
            res = statement.executeQuery();
            while(res.next()) {
                String columnname = res.getString("columnname");
                String remark = res.getString("remarks");//获取test_name列的元素
                columns.put(columnname,remark);
            }

            return columns;
        } catch (Exception e) {
            return null;
        } finally{
            try {
                if(res != null) res.close();
                if(statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

