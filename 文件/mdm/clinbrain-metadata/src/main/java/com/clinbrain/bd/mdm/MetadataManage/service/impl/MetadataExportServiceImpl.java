package com.clinbrain.bd.mdm.MetadataManage.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataProperties;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataValue;
import com.clinbrain.bd.mdm.MetadataManage.service.MetaDataExportService;
import com.clinbrain.bd.mdm.MetadataManage.service.MetadataPropertiesService;
import com.clinbrain.bd.mdm.MetadataManage.service.MetadataValueService;
import com.clinbrain.bd.mdm.common.core.util.CreateSqlUtil;
import com.clinbrain.bd.mdm.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.beans.BeanMap;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MetadataExportServiceImpl implements MetaDataExportService {

    @Autowired
    private MetadataValueService metadataValueService;

    @Autowired
    private MetadataPropertiesService propertiesService;

    /**
     * 导出表或库的脚本
     *
     * @param paramMap
     * @return
     * @author yjt
     * @date 2019/9/3 15:34
     */
    public String exportMetaData(Map<String, Object> paramMap) {
        String exportType = paramMap.get("exportType").toString();
        String id = paramMap.get("id").toString();
        String dbType = paramMap.get("dbType").toString();

        String result = "table".equalsIgnoreCase(exportType) == true ? getTableScript(id, dbType) : getDBScript(id, dbType);
        return result;
    }

    /**
     * 将表或库的脚本执行到指定目标
     *
     * @param param
     * @return
     * @author yjt
     * @date 2019/9/3 16:25
     */
    public R exportMetaDataToTarget(Map<String, Object> param) {

        //param参数项  database_type/server_name/port/database_name/login_name/login_pwd  要导入的指定数据库信息
        //exportType 导出数据类型(table/database)、 databaseID(导出数据库id)、tableID(导出表id)
        String exportType = param.get("exportType").toString();
        return "table".equalsIgnoreCase(exportType) == true ? exportMetaDataTableToTarget(param) : exportMetaDataDBToTarget(param);
    }

    /**
     * 将表执行到指定库
     *
     * @param param
     * @return
     * @author yjt
     * @date 2019/9/3 15:34
     */
    private R exportMetaDataTableToTarget(Map<String, Object> param) {

        R r = new R();
        try {

            //要导出的表id集合
            String ids = param.get("tableIDList") != null ? param.get("tableIDList").toString() : "";
            //要导出的数据库类型
            String exportType = param.get("database_type") != null ? param.get("database_type").toString() : "";
            if (!"".equalsIgnoreCase(ids)) {
                String[] idList = ids.split(";");

                for (int i = 0; i < idList.length; i++) {
                    String id = idList[i];

                    //校验当前要导的表在目标库中是否存在，如果存在，则忽略
                    MetadataValue value = new MetadataValue();
                    value.setResourceId(id);
                    Page<MetadataValue> page = new Page<>();
                    page.setSize(-1);

                    IPage<MetadataValue> valueList = metadataValueService.getMetadataValuePage(page, value);
                    String tableName = valueList.getRecords().get(0).getNameEn();
                    if ("".equalsIgnoreCase(tableName)) {
                        continue;
                    }
                    String checkSql = "select count(*) from " + tableName;
                    ResultSet resultSet = getExcuteResult(param, checkSql);
                    while (resultSet != null && resultSet.next()) {
                        continue;
                    }
                    //获取创建语句脚本
                    String sql = getTableScript(id, exportType);

                    //将脚本执行到指定的数据库
                    boolean result = excuteSqlToDB(param, sql);
                    if (!result) {
                        r.setCode(1);
                        r.setMsg("执行创建脚本失败。");
                        return r;
                    }
                }
            }
            r.setCode(0);
            r.setMsg("执行成功。");
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("导出表结构出错。", e);
            r.setCode(1);
            r.setMsg("执行创建脚本失败。");
            return r;
        }
    }

    /**
     * 将库执行到指定实例
     *
     * @param param
     * @return
     * @author yjt
     * @date 2019/9/3 15:34
     */
    private R exportMetaDataDBToTarget(Map<String, Object> param) {
        R r = new R();
        try {
            if (param == null || param.size() < 1) {
                r.setCode(-1);
                r.setMsg("未获取到参数。");
                return r;
            }

            param = setDriverType(param);
            //校验当前要连接的数据库是否存在
            String checkResult = checkHasInstanceAndDataBase(param);
            if ("当前数据库连接不存在。".equalsIgnoreCase(checkResult)) {
                r.setCode(-1);
                r.setMsg("目标数据库不存在，请先创建数据库。");
                return r;
            }

            //要导出的库id
            String dataBaseId = param.get("dataBaseID") != null ? param.get("dataBaseID").toString() : "";

            //目标数据库名称
            String dbName = param.get("database_name") != null ? param.get("database_name").toString() : "";
            //创建数据库
            String createDbSql = "create database " + dbName;

            //执行创建数据库脚本
//            boolean excute = excuteSqlToDB(param, createDbSql);
//
//            if (excute) {
//                r.setCode(0);
//                r.setMsg("执行成功");
//            } else {
//                r.setCode(-1);
//                r.setMsg("执行失败");
//            }


            //向目标数据库新增表
            //执行创建所有表脚本
            String exportDBType = param.get("database_type") != null ? param.get("database_type").toString() : "";
            String result = getDBScript(dataBaseId, exportDBType);
            boolean excute = excuteSqlToDB(param, result);
            if (excute) {
                r.setCode(0);
                r.setMsg("执行成功");
            } else {
                r.setCode(-1);
                r.setMsg("执行失败");
            }
            return r;
        } catch (Exception e) {
            log.error("执行导出数据库时出错。", e);
            r.setCode(-1);
            r.setMsg("执行失败");
            return r;
        }
    }

    /**
     * 获取一个表的脚本语句
     *
     * @param id         表id
     * @param exportType 导出的数据库类型（mysq/server/oracle/gp）
     * @return
     * @author yjt
     * @date 2019/9/2 10:47
     */
    private String getTableScript(String id, String exportType) {
        try {
            MetadataValue value = new MetadataValue();
            value.setResourceId(id);

            Page<MetadataValue> page = new Page<>();
            page.setSize(-1);
            IPage<MetadataValue> valueList = metadataValueService.getMetadataValuePage(page, value);
            String tableName = valueList.getRecords().get(0).getNameEn();
            if ("".equalsIgnoreCase(tableName)) {
                return "";
            }

            //列集合
            value = new MetadataValue();
            value.setParentId(id);
            value.setModelType("column");
            List<MetadataValue> columnList = metadataValueService.getMetadataValuePage(page, value).getRecords();

            //主键集合
            value.setModelType("primary_key");
            List<MetadataValue> pkList = metadataValueService.getMetadataValuePage(page, value).getRecords();

            //外键集合
            value.setModelType("foreign_key");
            List<MetadataValue> fkList = metadataValueService.getMetadataValuePage(page, value).getRecords();

            //获取一个表的创建语句
            String sql = getTableScriptStr(exportType, tableName, columnList, pkList, fkList);
            return sql;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取表的导出脚本出错", e);
            return "";
        }
    }

    /**
     * 获取一个数据库所有表的脚本
     *
     * @param id
     * @param exportType
     * @return
     * @author yjt
     * @date 2019/9/3 9:40
     */
    private String getDBScript(String id, String exportType) {
        try {
            String result = "";

            //根据数据库id获取数据库信息
            MetadataValue value = new MetadataValue();
            value.setResourceId(id);

            Page<MetadataValue> page = new Page<>();
            page.setSize(-1);

            IPage<MetadataValue> dbInfo = metadataValueService.getMetadataValuePage(page, value);

            if (dbInfo == null || dbInfo.getRecords().size() < 1) {
                return null;
            }

            //数据库所属实例id
            String instanceId = dbInfo.getRecords().get(0).getParentId() != null ? dbInfo.getRecords().get(0).getParentId() : "";
            value.setResourceId(instanceId);
            IPage<MetadataValue> instanceInfo = metadataValueService.getMetadataValuePage(page, value);

            if (instanceInfo == null || instanceInfo.getRecords().size() < 1) {
                return null;
            }

            value.setParentId(id);
            value.setModelType("table");
            IPage<MetadataValue> tables = metadataValueService.getMetadataValuePage(page, value);

            if (tables == null || tables.getRecords().size() < 1) {
                return result;
            }

            for (MetadataValue table : tables.getRecords()) {
                String tableid = table.getResourceId();
                if ("".equalsIgnoreCase(result)) {
                    result = getTableScript(tableid, exportType);
                } else {
                    result = result + ";" + getTableScript(tableid, exportType);
                }
            }

            return result;

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取一个表的脚本
     *
     * @param dbtype     导出数据库类型
     * @param tableName  表名称
     * @param columnList 列集合
     * @param pkList     主键集合
     * @param fkList     外键集合
     * @return
     * @author yjt
     * @date 2019/9/3 17:06
     */
    private String getTableScriptStr(String dbtype, String tableName, List<MetadataValue> columnList, List<MetadataValue> pkList, List<MetadataValue> fkList) {
        try {
            if (columnList == null || columnList.size() < 1) {
                return null;
            }

            //将columns根据导出数据库类型进行字段对应转换
            List<Map<String, Object>> columnListMap = getListMapFromListObject(columnList);
            columnListMap = getFieldMappingList(columnListMap, dbtype);
            //拼接列信息
            Map<String, List<String>> tabMap = new HashMap<>();
            for (Map<String, Object> column : columnListMap) {

                String columnName = column.get("column_name") != null ? column.get("column_name").toString() : "";
                String columnType = column.get("type_name") != null ? column.get("type_name").toString() : "";
                String columnSize = column.get("column_size") != null ? column.get("column_size").toString() : "";
                String nullable = column.get("nullable") != null ? column.get("nullable").toString() : "";
                List<String> list = new ArrayList<>();
                List<String> fieldWithNoSizeList = new ArrayList<>();
                fieldWithNoSizeList.add("date");
                fieldWithNoSizeList.add("datetime");
                fieldWithNoSizeList.add("int");
                fieldWithNoSizeList.add("bigint");
                fieldWithNoSizeList.add("smallint");
                fieldWithNoSizeList.add("tinyint");
                fieldWithNoSizeList.add("timestamp");
                fieldWithNoSizeList.add("bit");

                //不拼接长度的字段
                if (fieldWithNoSizeList.contains(columnType.toLowerCase()) || columnSize == null || "".equalsIgnoreCase(columnSize) || "0".equalsIgnoreCase(columnSize)) {
                    list.add(columnType);
                } else {
                    list.add(columnType + "(" + columnSize + ")");
                }

                if ("0".equalsIgnoreCase(nullable)) {
                    list.add("not null");
                } else {
                    list.add("null");
                }

                tabMap.put(columnName, list);
            }

            //拼接主键信息
            Map<String, Object> pkMap = new HashMap<>();

            if (pkList != null && pkList.size() > 0) {
                String pknames = "";
                String pkName = "";
                List<Map<String, Object>> pkListMap = getListMapFromListObject(pkList);
                pkListMap = getPKMappingList(pkListMap, "pk");

                for (Map<String, Object> pk : pkListMap) {
                    pkName = pk.get("pk_name") != null ? pk.get("pk_name").toString() : "";
                    String pkColumnName = pk.get("column_name") != null ? pk.get("column_name").toString() : "";

                    if ("".equalsIgnoreCase(pknames)) {
                        pknames = pkColumnName;
                    } else {
                        pknames = pknames + "," + pkColumnName;
                    }

                    //pkMap.put("pkName", pkName);
                    pkMap.put("pkName", "");
                    pkMap.put("columns", pknames);
                }
            }

            //获取外键集合
            List<Map<String, Object>> fks = new ArrayList<>();
            if (fkList != null && fkList.size() > 0) {
                String fkcolumns = "";
                String pkcolumns = "";
                List<Map<String, Object>> fkListMap = getListMapFromListObject(fkList);
                fkListMap = getPKMappingList(fkListMap, "fk");
                for (Map<String, Object> fk : fkListMap) {

                    Map<String, Object> fkinfo = new HashMap<String, Object>();
                    //外键名和外键列名
                    String fkName = fk.get("fk_name") != null ? fk.get("fk_name").toString() : "";
                    String fkColumnName = fk.get("fkcolumn_name") != null ? fk.get("fkcolumn_name").toString() : "";

                    if ("".equalsIgnoreCase(fkcolumns)) {
                        fkcolumns = fkColumnName;
                    } else {
                        fkcolumns = pkcolumns + "," + fkColumnName;
                    }

                    //关联表名和主键名
                    String pkTableName = fk.get("pktable_name") != null ? fk.get("pktable_name").toString() : "";
                    String pkColumnName = fk.get("pkcolumn_name") != null ? fk.get("pkcolumn_name").toString() : "";

                    if ("".equalsIgnoreCase(pkcolumns)) {
                        pkcolumns = pkColumnName;
                    } else {
                        pkcolumns = pkcolumns + "," + pkColumnName;
                    }

                    fkinfo.put("fkName", fkName);
                    fkinfo.put("columns", fkcolumns);
                    fkinfo.put("fkTableName", pkTableName);
                    fkinfo.put("fkColumns", pkcolumns);

                    fks.add(fkinfo);
                }
            }

            //主外键对象
            Map<String, Object> pkMaps = new HashMap<>();
            pkMaps.put("primary", pkMap);
            pkMaps.put("foreign", fks);
            CreateSqlUtil util = new CreateSqlUtil();

            String sqlText = util.createTableSql(tableName, tabMap, pkMaps, "\r\n");
            return sqlText;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取数据字段映射
     *
     * @param sourceList 原始字段集合
     * @return 转换后的字段集合
     */
    private List<Map<String, Object>> getFieldMappingList(List<Map<String, Object>> sourceList, String outType) {
        try {
            if (sourceList == null || sourceList.size() < 1) {
                return null;
            }

            String modelId = sourceList.get(0).get("modelId").toString();
            MetadataProperties properties = new MetadataProperties();
            properties.setModelResourceId(modelId);
            Page<MetadataProperties> page = new Page<>();
            page.setSize(-1);
            IPage<MetadataProperties> propertiesList = propertiesService.getMetadataPropertiesPage(page, properties);
            if (propertiesList == null || propertiesList.getRecords().size() < 1) {
                return null;
            }

            String type_nameField = "";
            String column_sizeField = "";
            String nullableField = "";
            String table_idField = "";
            for (MetadataProperties p : propertiesList.getRecords()) {
                switch (p.getNameEn().toLowerCase()) {
                    case "type_name":
                        type_nameField = p.getMappingField();
                        break;
                    case "column_size":
                        column_sizeField = p.getMappingField();
                        break;
                    case "nullable":
                        nullableField = p.getMappingField();
                        break;
                    case "table_id":
                        table_idField = p.getMappingField();
                        break;
                }
            }

            //转换后的字段集合
            List<Map<String, Object>> exchangeFieldList = new ArrayList<>();

            //遍历原始字段集合开始转换
            for (Map<String, Object> sourceField : sourceList) {

                //原始字段信息
                String sourceFieldType = sourceField.get(type_nameField) != null ? sourceField.get(type_nameField).toString() : "";
                String sourceFieldSize = sourceField.get(column_sizeField) != null ? sourceField.get(column_sizeField).toString() : "";
                String sourceFieldName = sourceField.get("nameEn") != null ? sourceField.get("nameEn").toString() : "";
                String sourceNullAble = sourceField.get(nullableField) != null ? sourceField.get(nullableField).toString() : "";

                Map<String, Object> field = new HashMap<>();
                if ("oracle".equalsIgnoreCase(outType)) {
                    field = getOracleFieldMappingList(sourceFieldType, sourceFieldSize);
                } else if ("sqlserver".equalsIgnoreCase(outType)) {
                    field = getSqlServerFieldMappingList(sourceFieldType, sourceFieldSize);
                } else if ("mysql".equalsIgnoreCase(outType)) {
                    field = getMySqlFieldMappingList(sourceFieldType, sourceFieldSize);
                }

                Map<String, Object> exchangefield = new HashMap<>();
                exchangefield.put("type_name", field.get("fieldType") != null ? field.get("fieldType").toString() : "");
                exchangefield.put("column_size", field.get("fieldSize") != null ? field.get("fieldSize").toString() : "");
                exchangefield.put("column_name", sourceFieldName);
                exchangefield.put("nullable", sourceNullAble);
                exchangefield.put("table_id", sourceField.get(table_idField) != null ? sourceField.get(table_idField).toString() : "");

                exchangeFieldList.add(exchangefield);
            }

            return exchangeFieldList;
        } catch (Exception e) {
            log.error("getFieldMappingList_获取数据字段映射异常", e);
        }

        return null;
    }

    private List<Map<String, Object>> getPKMappingList(List<Map<String, Object>> sourceList, String type) {
        try {
            if (sourceList == null || sourceList.size() < 1) {
                return null;
            }

            String modelId = sourceList.get(0).get("modelId").toString();
            MetadataProperties properties = new MetadataProperties();
            properties.setModelResourceId(modelId);
            Page<MetadataProperties> page = new Page<>();
            page.setSize(-1);
            IPage<MetadataProperties> propertiesList = propertiesService.getMetadataPropertiesPage(page, properties);
            if (propertiesList == null || propertiesList.getRecords().size() < 1) {
                return null;
            }

            //转换后的字段集合
            List<Map<String, Object>> exchangeFieldList = new ArrayList<>();
            String pk_nameField = "";
            String column_nameField = "";

            String fk_nameField = "";
            String fkcolumn_nameField = "";
            String pktable_nameField = "";
            String pkcolumn_nameField = "";
            for (MetadataProperties p : propertiesList.getRecords()) {
                switch (p.getNameEn().toLowerCase()) {
                    case "pk_name":
                        pk_nameField = p.getMappingField();
                        break;
                    case "column_name":
                        column_nameField = p.getMappingField();
                        break;
                    case "fk_name":
                        fk_nameField = p.getMappingField();
                        break;
                    case "fkcolumn_name":
                        fkcolumn_nameField = p.getMappingField();
                        break;
                    case "pktable_name":
                        pktable_nameField = p.getMappingField();
                        break;
                    case "pkcolumn_name":
                        pkcolumn_nameField = p.getMappingField();
                        break;
                }
            }

            //遍历原始字段集合开始转换
            for (Map<String, Object> sourceField : sourceList) {

                Map<String, Object> exchangefield = new HashMap<>();
                if ("pk".equalsIgnoreCase(type)) {
                    String sourcePKName = sourceField.get(pk_nameField) != null ? sourceField.get(pk_nameField).toString() : "";
                    String sourceColumnName = sourceField.get(column_nameField) != null ? sourceField.get(column_nameField).toString() : "";
                    exchangefield.put("pk_name", sourcePKName);
                    exchangefield.put("column_name", sourceColumnName);
                } else if ("fk".equalsIgnoreCase(type)) {
                    String sourceFKName = sourceField.get(fk_nameField) != null ? sourceField.get(fk_nameField).toString() : "";
                    String sourceFkColumnName = sourceField.get(fkcolumn_nameField) != null ? sourceField.get(fkcolumn_nameField).toString() : "";
                    String sourcePkTableName = sourceField.get(pktable_nameField) != null ? sourceField.get(pktable_nameField).toString() : "";
                    String sourcePkColumnName = sourceField.get(pkcolumn_nameField) != null ? sourceField.get(pkcolumn_nameField).toString() : "";
                    exchangefield.put("fk_name", sourceFKName);
                    exchangefield.put("fkcolumn_name", sourceFkColumnName);
                    exchangefield.put("pktable_name", sourcePkTableName);
                    exchangefield.put("pkcolumn_name", sourcePkColumnName);
                }

                exchangeFieldList.add(exchangefield);
            }
            return exchangeFieldList;

        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取主、外键转换集合出错", e);
            return null;
        }
    }

    /**
     * 将对象集合转换为map集合
     *
     * @param sourceList
     * @return
     * @author yjt
     * @date 2019/9/3 17:06
     */
    private List<Map<String, Object>> getListMapFromListObject(List<MetadataValue> sourceList) {
        try {
            List<Map<String, Object>> list = sourceList.stream()
                    .map(value -> {
                        Map<String, Object> map = new HashMap<>();
                        //map = BeanUtil.beanToMap(value,false,true);
                        BeanMap beanMap = BeanMap.create(value);
                        for (Object key : beanMap.keySet()) {
                            map.put(key + "", beanMap.get(key));
                        }
                        return map;
                    }).collect(Collectors.toList());

            return list;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 转换为Oracle字段类型
     *
     * @param fieldType 字段类型
     * @param fieldSize 字段长度
     * @return 转换好的字段
     */
    private Map<String, Object> getOracleFieldMappingList(String fieldType, String fieldSize) {
        try {

            Map<String, Object> exchangeField = new HashMap<>();
            String exchangeFieldType;
            switch (fieldType.toLowerCase().trim()) {
                case "bigint":
                    exchangeFieldType = "number";
                    fieldSize = "20";
                    break;
                case "tinyint":
                case "bit":
                    exchangeFieldType = "number";
                    fieldSize = "2";
                    break;
                case "smallint":
                    exchangeFieldType = "number";
                    fieldSize = "5";
                    break;
                case "mediumint":
                case "integer":
                case "int":
                    exchangeFieldType = "number";
                    break;
                case "float":
                    exchangeFieldType = "real";
                    break;
                case "numeric":
                case "decimal":
                case "decimal(10)":
                case "double":
                case "int(8)":
                case "mediumini(10)":
                case "smallint(2)":
                    exchangeFieldType = "number";
                    break;
                case "char":
                case "char(2)":
                    exchangeFieldType = "char";
                    break;
                case "varchar":
                case "text":
                    exchangeFieldType = "varchar2";
                    break;
                case "blob":
                case "binary":
                case "longblob":
                case "img":
                case "uniqueidentifier":
                case "geometry":
                case "geometrycollection":
                case "multilinestring":
                case "linestring":
                case "multipoint":
                case "multipolygon":
                case "polygon":
                case "blob(10)":
                case "mediumblob":
                case "tinyblob":
                    exchangeFieldType = "blob";
                    break;
                case "datetime":
                case "date":
                case "time":
                    exchangeFieldType = "date";
                    break;
                case "timestamp":
                    exchangeFieldType = "timestamp";
                    break;
                case "binary(10)":
                case "varbinary(10)":
                    exchangeFieldType = "raw";
                    break;
                case "year":
                    exchangeFieldType = "number(3,0)";
                    break;
                case "tinytext":
                case "longtext":
                case "mediumtext":
                    exchangeFieldType = "clob";
                    break;
                default:
                    exchangeFieldType = fieldType;
                    break;

            }

            exchangeField.put("fieldType", exchangeFieldType);
            exchangeField.put("fieldSize", fieldSize);

            return exchangeField;
        } catch (Exception e) {
            //LOGGER.error("getOracleFieldMappingList_获取Oracle数据字段映射异常",e);
        }


        return null;
    }

    /**
     * 转换为SqlServer字段类型
     *
     * @param fieldType 字段类型
     * @param fieldSize 字段长度
     * @return 转换好的字段
     */
    private Map<String, Object> getSqlServerFieldMappingList(String fieldType, String fieldSize) {
        try {

            Map<String, Object> exchangeField = new HashMap<>();
            String exchangeFieldType;
            switch (fieldType.toUpperCase().trim()) {
                case "binary_double":
                case "float":
                    exchangeFieldType = "float";
                    break;
                case "binary_float":
                case "double":
                case "real":
                    exchangeFieldType = "real";
                    break;
                case "blob":
                case "long raw":
                case "longblob":
                case "mediumblob":
                case "tinyblob":
                    exchangeFieldType = "image";
                    break;
                case "clob":
                case "long":
                case "nclob":
                    exchangeFieldType = "ntext";
                    break;
                case "char":
                    exchangeFieldType = "nchar";
                    break;
                case "number":
                    exchangeFieldType = "nemeric";
                    break;
                case "nvarchar2":
                case "varchar2":
                case "interval":
                    exchangeFieldType = "nvarchar";
                    break;
                case "decimal":
                    exchangeFieldType = "decimal";
                    break;
                case "binary":
                    exchangeFieldType = "binary";
                    break;
                case "date":
                    exchangeFieldType = "date";
                    break;
                case "time":
                    exchangeFieldType = "time";
                    break;
                case "timestamp":
                    exchangeFieldType = "timestamp";
                    break;
                case "year":
                case "int":
                case "int(8)":
                case "mediumint(10)":
                case "smallint(2)":
                case "tinyint(2)":
                    exchangeFieldType = "int";
                    break;
                case "text":
                case "tinytext":
                case "geometry":
                case "geometrycollection":
                case "multilinestring":
                case "linestring":
                case "multipoint":
                case "multipolygon":
                case "point":
                case "polygon":
                case "longtext":
                case "mediumtext":
                    exchangeFieldType = "text";
                    break;
                case "bit":
                    exchangeFieldType = "bit";
                    break;
                case "enum":
                case "set":
                    exchangeFieldType = "varchar2";
                    break;
                case "varchar":
                    exchangeFieldType = "varchar";
                    break;
                case "datetime":
                case "datetime(6)":
                    exchangeFieldType = "datetime";
                    break;
                default:
                    exchangeFieldType = fieldType;
                    break;
            }

            exchangeField.put("fieldType", exchangeFieldType);
            exchangeField.put("fieldSize", fieldSize);
            return exchangeField;
        } catch (Exception e) {
            //LOGGER.error("getOracleFieldMappingList_获取Oracle数据字段映射异常",e);
        }
        return null;
    }

    /**
     * 转换为MySql类型
     *
     * @param fieldType 字段类型
     * @param fieldSize 字段长度
     * @return 转换好的字段
     */
    private Map<String, Object> getMySqlFieldMappingList(String fieldType, String fieldSize) {
        try {
            Map<String, Object> exchangeField = new HashMap<>();
            String exchangeFieldType;
            switch (fieldType.toUpperCase().trim()) {
                case "binary(50)":
                    exchangeFieldType = "binary";
                    break;
                case "bit":
                    exchangeFieldType = "bit";
                    break;
                case "char(10)":
                case "char":
                    exchangeFieldType = "char";
                    break;
                case "date":
                    exchangeFieldType = "date";
                    break;
                case "datetime":
                case "datetime2(7)":
                case "datetimeoffset(7)":
                    exchangeFieldType = "datetime";
                    break;
                case "decimal(18, 0)":
                case "smallmoney":
                    exchangeFieldType = "decimal";
                    break;
                case "float":
                case "binary_float":
                    exchangeFieldType = "float";
                    break;
                case "nchar":
                case "nvarchar":
                    exchangeFieldType = "nvarchar";
                    break;
                case "numeric":
                    exchangeFieldType = "binary";
                    break;
                case "real":
                    exchangeFieldType = "real";
                    break;
                case "text":
                case "sql_variant":
                    exchangeFieldType = "text";
                    break;
                case "time(7)":
                    exchangeFieldType = "time";
                    break;
                case "timestamp":
                    exchangeFieldType = "timestamp";
                    break;
                case "varbinary(50)":
                case "varbinary(max)":
                    exchangeFieldType = "varbinary";
                    break;
                case "varchar(max)":
                    exchangeFieldType = "varchar";
                    break;
                case "uniqueidentifier":
                    exchangeFieldType = "timestamp";
                    break;
                case "geography":
                case "geometry":
                case "hierarchyid":
                case "image":
                    exchangeFieldType = "longblob";
                    break;
                case "int":
                case "smallint":
                case "tinyint":
                    exchangeFieldType = "int";
                    break;
                case "money":
                    exchangeFieldType = "decimal(19,4)";
                    break;
                case "bigint":
                    exchangeFieldType = "bigint";
                    break;
                case "ntext":
                case "xml":
                    exchangeFieldType = "mediumtext";
                    break;
                case "smalldatetime":
                    exchangeFieldType = "datetime";
                    break;
                case "binary_double":
                    exchangeFieldType = "double";
                    break;
                case "blob":
                    exchangeFieldType = "blob";
                    break;
                case "interval day tosecond":
                case "tinytext":
                case "long":
                    exchangeFieldType = "tinytext";
                    break;
                case "number":
                    exchangeFieldType = "number";
                    break;
                case "nvarchar2":
                    exchangeFieldType = "nvarchar2";
                    break;
                case "clob":
                case "nclob":
                    exchangeFieldType = "mediumtext";
                    break;
                case "varchar2":
                    exchangeFieldType = "varchar2";
                    break;
                default:
                    exchangeFieldType = fieldType;
                    break;
            }

            exchangeField.put("fieldType", exchangeFieldType);
            exchangeField.put("fieldSize", fieldSize);
            return exchangeField;
        } catch (Exception e) {
            //LOGGER.error("getOracleFieldMappingList_获取Oracle数据字段映射异常",e);
        }

        return null;
    }

    /**
     * 将一条语句执行到指定数据库
     *
     * @param param
     * @param sql
     * @return
     * @author yjt
     * @date 2019/9/3 17:07
     */
    private boolean excuteSqlToDB(Map<String, Object> param, String sql) {
        try {
            //添加数据库驱动参数
            Connection conn = null;
            param = setDriverType(param);
            conn = getConnections(param);

            Statement state = conn.createStatement();
            state.executeUpdate(sql);

            return true;
        } catch (Exception e) {
            log.error("向指定数据源执行SQL脚本出错", e);
            return false;
        }
    }

    /**
     * 从指定数据库获取执行结果
     *
     * @param param
     * @param sql
     * @return
     * @author yjt
     * @date 2019/9/3 17:07
     */
    private ResultSet getExcuteResult(Map<String, Object> param, String sql) {
        try {
            Connection conn = null;
            param = setDriverType(param);
            conn = getConnections(param);

            Statement state = conn.createStatement();
            ResultSet resultSet = state.executeQuery(sql);
            return resultSet;
        } catch (Exception e) {
            log.error("向指定数据源执行SQL脚本出错", e);
            return null;
        }
    }

    /**
     * 获取数据库连接
     *
     * @param param
     * @return
     * @author yjt
     * @date 2019/9/3 17:07
     */
    private Connection getConnections(Map<String, Object> param) throws Exception {
        try {
            Connection conn = null;
            String url = getConnectionUrl(param);

            Properties props = new Properties();

            String serverType = param.get("database_type") != null ? param.get("database_type").toString() : "";
            if (serverType.equalsIgnoreCase("oracle")) {
                props.put("remarks", "true");
                props.put("remarksReporting", "true");
                props.put("user", param.get("login_name") + " as sysdba");
            } else if (serverType.equalsIgnoreCase("mysql")) {
                props.put("useSSL", "false");
                props.put("remarks", "true");
                props.put("remarksReporting", "true");
                props.put("useInformationSchema", "true");
                props.put("user", param.get("login_name"));
            } else if (serverType.equalsIgnoreCase("sqlserver")) {
                props.put("remarks", "true");
                props.put("remarksReporting", "true");
                props.put("useInformationSchema", "true");
                props.put("user", param.get("login_name"));
            } else if ("gp".equalsIgnoreCase(serverType)) {
                props.put("user", param.get("login_name"));
            }

            props.put("password", param.get("login_pwd"));
            Class.forName(param.get("driver").toString());
            conn = DriverManager.getConnection(url, props);
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("getConnections_获取数据库连接出错", e);
            throw e;
        }
    }

    /**
     * 根据数据库类型获取连接字符串
     *
     * @param param
     * @return
     */
    private String getConnectionUrl(Map<String, Object> param) {
        try {
            String url = "";

            String server_name = param.get("server_name") != null ? param.get("server_name").toString().trim() : "";
            String port = param.get("port") != null ? param.get("port").toString().trim() : "";

            String database_name = param.get("database_name") != null ? param.get("database_name").toString().trim() : "";
            server_name += ":" + port;
            // Oracle数据库
            if ("oralce".equalsIgnoreCase(param.get("database_type").toString())) {
                url += "jdbc:oracle:thin:@" + server_name;
                if (!"".equalsIgnoreCase(database_name)) {
                    url += ":" + database_name;
                }

                String url2 = "jdbc:oracle:thin:@(DESCRIPTION = (ADDRESS_LIST = (ADDRESS = (PROTOCOL = TCP)(HOST = "
                        + server_name + ")))(CONNECT_DATA = (SERVICE_NAME =" + database_name +
                        ")(FAILOVER_MODE = (TYPE = SELECT)(METHOD = BASIC)(RETRIES = 180)(DELAY = 5))))";
            } else if ("mysql".equalsIgnoreCase(param.get("database_type").toString())) {
                url += "jdbc:mysql://" + server_name;
                if (!"".equalsIgnoreCase(database_name)) {
                    url += "/" + database_name;
                }
            } else if ("sqlserver".equalsIgnoreCase(param.get("database_type").toString())) {
                url += "jdbc:sqlserver://" + server_name;
                if (!"".equalsIgnoreCase(database_name)) {
                    url += ";DatabaseName=" + database_name;
                }
            } else if ("gp".equalsIgnoreCase(param.get("database_type").toString())) {
                url = "jdbc:pivotal:greenplum://" + server_name + ":" + port + ";DatabaseName=" + database_name;
            } else {
                throw new RuntimeException("不支持的数据库类型!");
            }

            return url;
        } catch (Exception e) {
            log.error("getConnectionUrl_获取数据库连接URL出错", e);
            return "";
        }
    }

    private String checkHasInstanceAndDataBase(Map<String, Object> param) {
        try {

            if (param == null || param.size() < 1) {
                return "未获取到数据库连接参数。";
            }
            param = setDriverType(param);
            boolean result;
            try {
                Connection conn = null;
                conn = getConnections(param);
                result = conn != null ? true : false;
            } catch (Exception e) {
                e.printStackTrace();
                log.error("getConnectionResult_获取数据库连接信息出错", e);
                result = false;
            }

            if (result) {
                return "";
            } else {
                return "当前数据库连接不存在。";
            }

        } catch (Exception e) {
            log.error("验证是否存在数据库时出错。", e);
            return "验证是否存在数据库时出错。";
        }
    }

    private Map<String, Object> setDriverType(Map<String, Object> paramMap) {
        try {
            String serverType = "";
            serverType = paramMap.get("database_type") != null ? paramMap.get("database_type").toString() : "";
            if (serverType.equals("oracle")) {
                paramMap.put("driver", "oracle.jdbc.driver.OracleDriver");
            } else if (serverType.equals("sqlserver")) {
                paramMap.put("driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
            } else if (serverType.equals("mysql")) {
                paramMap.put("driver", "com.mysql.jdbc.Driver");
            } else if ("gp".equalsIgnoreCase(serverType)) {
                paramMap.put("driver", "com.pivotal.jdbc.GreenplumDriver");
            } else {
                paramMap.put("driver", "com.mysql.jdbc.Driver");
            }

            return paramMap;
        } catch (Exception e) {
            return null;
        }
    }
}
