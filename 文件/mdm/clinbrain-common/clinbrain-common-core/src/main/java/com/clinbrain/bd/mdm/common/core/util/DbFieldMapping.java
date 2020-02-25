package com.clinbrain.bd.mdm.common.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbFieldMapping {

    private final static Logger LOGGER = LoggerFactory.getLogger(DbFieldMapping.class);

    /**
     * 获取数据字段映射
     * @param sourceList 原始字段集合
     * @return 转换后的字段集合
     */
    public static List<Map<String,Object>> getFieldMappingList(List<Map<String,Object>> sourceList,String outType){
        try{
            if(sourceList==null||sourceList.size()<1){
                return null;
            }

            //转换后的字段集合
            List<Map<String,Object>> exchangeFieldList = new ArrayList<>();

            //遍历原始字段集合开始转换
            for(Map<String,Object> sourceField : sourceList){
                //原始字段信息
                String sourceFieldType = sourceField.get("type_name")!=null? sourceField.get("type_name").toString():"";
                String sourceFieldSize = sourceField.get("column_size")!=null?sourceField.get("column_size").toString():"";
                String sourceFieldName = sourceField.get("column_name")!=null? sourceField.get("column_name").toString():"";
                String sourceNullAble =  sourceField.get("nullable")!=null?sourceField.get("nullable").toString():"";

                Map<String,Object> field = new HashMap<>();
                if(outType.equalsIgnoreCase("oracle")){
                    field = getOracleFieldMappingList(sourceFieldType,sourceFieldSize);
                }else if(outType.equalsIgnoreCase("sqlserver")){
                    field = getSqlServerFieldMappingList(sourceFieldType,sourceFieldSize);
                }else if(outType.equalsIgnoreCase("mysql")){
                    field = getMySqlFieldMappingList(sourceFieldType,sourceFieldSize);
                }

                Map<String,Object> exchangefield = new HashMap<>();
                exchangefield.put("type_name",field.get("fieldType")!=null?field.get("fieldType").toString():"");
                exchangefield.put("column_size",field.get("fieldSize")!=null?field.get("fieldSize").toString():"");
                exchangefield.put("column_name",sourceFieldName);
                exchangefield.put("nullable",sourceNullAble);
                exchangefield.put("table_id",sourceField.get("table_id")!=null?sourceField.get("table_id").toString():"");

                exchangeFieldList.add(exchangefield);
            }

            return exchangeFieldList;
        }catch(Exception e){
            LOGGER.error("getOracleFieldMappingList_获取Oracle数据字段映射异常",e);
        }

        return null;
    }

    /**
     * 转换为Oracle字段类型
     * @param fieldType 字段类型
     * @param fieldSize 字段长度
     * @return  转换好的字段
     */
    private static Map<String,Object> getOracleFieldMappingList(String fieldType,String fieldSize){
        try{

            Map<String,Object> exchangeField = new HashMap<>();
            String exchangeFieldType;
            switch(fieldType.toLowerCase().trim()){
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

            exchangeField.put("fieldType",exchangeFieldType);
            exchangeField.put("fieldSize",fieldSize);

            return exchangeField;
        }catch(Exception e){
            LOGGER.error("getOracleFieldMappingList_获取Oracle数据字段映射异常",e);
        }


        return null;
    }

    /**
     * 转换为SqlServer字段类型
     * @param fieldType 字段类型
     * @param fieldSize 字段长度
     * @return  转换好的字段
     */
    private static Map<String,Object> getSqlServerFieldMappingList(String fieldType,String fieldSize){
        try{

            Map<String,Object> exchangeField = new HashMap<>();
            String exchangeFieldType;
            switch(fieldType.toUpperCase().trim()){
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
                case "varchar2" :
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
                    exchangeFieldType ="bit";
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

            exchangeField.put("fieldType",exchangeFieldType);
            exchangeField.put("fieldSize",fieldSize);
            return exchangeField;
        }catch(Exception e){
            LOGGER.error("getOracleFieldMappingList_获取Oracle数据字段映射异常",e);
        }
        return null;
    }

    /**
     * 转换为MySql类型
     * @param fieldType 字段类型
     * @param fieldSize 字段长度
     * @return  转换好的字段
     */
    private static Map<String,Object> getMySqlFieldMappingList(String fieldType,String fieldSize){
        try{
            Map<String,Object> exchangeField = new HashMap<>();
            String exchangeFieldType;
            switch(fieldType.toUpperCase().trim()) {
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
                    exchangeFieldType ="double";
                    break;
                case "blob":
                    exchangeFieldType ="blob";
                    break;
                case "interval day tosecond":
                case "tinytext":
                case "long":
                    exchangeFieldType ="tinytext";
                    break;
                case "number":
                    exchangeFieldType ="number";
                    break;
                case "nvarchar2":
                    exchangeFieldType ="nvarchar2";
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

            exchangeField.put("fieldType",exchangeFieldType);
            exchangeField.put("fieldSize",fieldSize);
            return exchangeField;
        }catch(Exception e){
            LOGGER.error("getOracleFieldMappingList_获取Oracle数据字段映射异常",e);
        }

        return null;
    }
}
