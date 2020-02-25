package com.clinbrain.bd.mdm.strategy.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.clinbrain.bd.mdm.strategy.dto.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: 梁乐乐
 * @Date: 2019/9/18 11:17
 * @Description: 装配SQL
 */
public class PackagingUtil {


    /**
     * 获取组装字段条件
     *
     * @param colJson 字段json字符串
     * @return
     */
    public static ColumnsDto getCloumn(String colJson) {

        ColumnsDto colDto = new ColumnsDto();

        JSONObject obj1 = JSON.parseObject(colJson);
//        JSONArray arrayf = JSONArray.parseArray(obj1.getString("fields"));

        //解析字段条件
        List<FieldDto> fieldList = JSONArray.parseArray(obj1.getString("fields"), FieldDto.class);
//        if (arrayf != null && arrayf.size() > 0) {
//            arrayf.stream().forEach(e -> {
////                Map<String, Object> map = (Map<String, Object>) e;
//                FieldDto fieldDto = BeanUtils.mapToBean((Map<String, Object>) e, FieldDto.class);
//                fieldList.add(fieldDto);
//            });
//        }
        colDto.setFieldList(fieldList);

        //解析关系
//        JSONArray arrayr = JSONArray.parseArray(obj1.getString("relation"));
        List<RelationDto> relList = JSONArray.parseArray(obj1.getString("relation"), RelationDto.class);
//        if (arrayr != null && arrayr.size() > 0) {
//            arrayr.stream().forEach(r -> {
//                RelationDto relationDto = BeanUtils.mapToBean((Map<String, Object>) r, RelationDto.class);
//                relList.add(relationDto);
//            });
//        }
        colDto.setRelationList(relList);
        return colDto;
    }


    /**
     * 获取表
     *
     * @return 表信息
     */
    public static List<TableDto> getTables(String tableJson) {
        JSONArray arrayt = JSONArray.parseArray(tableJson);
        List<TableDto> tableList = new ArrayList<>();
        if (arrayt != null && arrayt.size() > 0) {
            arrayt.stream().forEach(t -> {
                TableDto tableDto = BeanUtils.mapToBean((Map<String, Object>) t, TableDto.class);
                tableList.add(tableDto);
            });
        }
        return tableList;
    }


    /**
     * 获取条件对象
     *
     * @param condJson 条件
     * @return 对象
     */
    public static List<ConditionsDto> getConditions(String condJson) {
//        JSONArray arrayc = JSONArray.parseArray(condJson, ConditionsDto.class);
        List<ConditionsDto> condList = JSONArray.parseArray(condJson, ConditionsDto.class);
//        if (arrayc != null && arrayc.size() > 0) {
//            arrayc.stream().forEach(c -> {
//                ConditionsDto condDto = BeanUtils.mapToBean((Map<String, Object>) c, ConditionsDto.class);
//                condList.add(condDto);
//            });
//        }
        return condList;
    }


    /**
     * 获取字段字符串
     *
     * @param col 字段对象
     * @return
     */
    public static String getBeanToSqlField(ColumnsDto col) {

        //字段
        List<FieldDto> fieldList = col.getFieldList();

        //关系
        List<RelationDto> relList = col.getRelationList();
        StringBuffer resultSql = null;
        int fieldIn = 1;
        if (fieldList != null && fieldList.size() > 0) {
            //记录from的表以防连接错误
            String fromT = "";
            for (FieldDto f : fieldList) {
                String reuslt = "";
                if (StrUtil.isNotEmpty(f.getAlias())) {
                    reuslt += f.getAlias() + "." + f.getField();
                } else {
                    reuslt += f.getTable() + "." + f.getField();
                }

                //判断关系
                if (relList != null && relList.size() > 0) {
                    for (RelationDto r : relList) {
                        if (f.getField().equalsIgnoreCase(r.getFrom())) {
                            fromT = f.getFromTable();
                            if (EnumReckon.getEnumReckon(r.getRelat().toUpperCase())) {
                                reuslt = r.getRelat() + "(" + reuslt + ")";
                            } else {
                                reuslt += " " + r.getRelat();
                            }
                            break;
                        }

                        //防止错配
                        if ((StrUtil.isNotEmpty(fromT) && f.getFromTable().equalsIgnoreCase(r.getTo()))) {
                            fromT = "";
                        }
                    }

                }

                if (StrUtil.isEmpty(resultSql)) {
                    resultSql = new StringBuffer(reuslt);
                } else {
                    resultSql.append("," + reuslt);
                    if (fieldIn < fieldList.size() - 1)
                        resultSql.append(" , ");
                }

                fieldIn++;
            }
        }
        return resultSql.toString();
    }

    /**
     * 获取表sql段
     *
     * @param tabList 表
     * @return
     */
    public static String getBeanToTable(List<TableDto> tabList) {
        String tableFrom = "";
        if (tabList != null && tabList.size() > 0) {
            String join = "";
            String fField = "";
            String eField = "";
            Boolean isToJson = false;
            String newFrom = "";
            for (TableDto t : tabList) {
                if (StrUtil.isEmpty(newFrom)) {
                    if (StrUtil.isEmpty(tableFrom)) {
                        newFrom = t.getTable() + " " + t.getAlias();
                    }
                    if (StrUtil.isNotEmpty(t.getJoin())) {
                        join = t.getJoin();
                        isToJson = true;
                    }
                } else {
                    if (StrUtil.isNotEmpty(join)) {
                        newFrom += " " + join + " ";
                        join = "";
                    }
                    newFrom += t.getTable() + " " + t.getAlias();
                }

                if (isToJson) {
                    if (StrUtil.isNotEmpty(join)) {
                        if (StrUtil.isNotEmpty(t.getAlias())) {
                            fField = t.getAlias() + ".";
                        } else {
                            fField = t.getTable() + ".";
                        }
                        fField += t.getJoinField();
                    } else {
                        if (StrUtil.isNotEmpty(t.getAlias())) {
                            eField = t.getAlias() + ".";
                        } else {
                            eField = t.getTable() + ".";
                        }
                        eField += t.getJoinField();

                        newFrom += " ON " + fField + "=" + eField;
                        isToJson = false;
                        tableFrom += newFrom;
                        newFrom = "";
                    }
                } else {
                    tableFrom = newFrom;
                }

            }
        }
        return tableFrom;
    }


    /**
     * 获取条件sql
     *
     * @param condList 条件
     * @param valMap   值
     * @return
     */
    public static String getBeanToWhere(List<ConditionsDto> condList, Map<String, Object> valMap) {

        String resultSql = "";
        if (condList != null && condList.size() > 0) {
            int i = 0;
            for (ConditionsDto cond : condList) {
                i++;
                if (StrUtil.isEmpty(resultSql) && StrUtil.isNotEmpty(cond.getAlias())) {
                    resultSql = cond.getAlias() + ".";
                } else {
                    resultSql += cond.getAlias() + ".";
                }
                if (cond.getType() == 3) {
                    resultSql += "to_char(" + cond.getFiled() + ", yyyy-mm-dd hh24:mi:ss) ";
                } else {
                    resultSql += cond.getFiled() + " ";
                }
                resultSql += cond.getOper();

                //查询值
                String value = "";
                if (cond.getStatus() == 2 && valMap != null) {
                    String key = cond.getValue().replace("${", "").replace("}", "");
                    value = valMap.get(key) != null ? valMap.get(key) + "" : "";
                } else {
                    value = cond.getValue();
                }
                // 拼接
                if (cond.getType() == 1) {
                    if ("like".equalsIgnoreCase(cond.getOper())) {
                        resultSql += " '%" + value + "%'";
                    } else {
                        resultSql += " '" + value + "'";
                    }

                } else if (cond.getType() == 2) {
                    resultSql += value;
                } else {
                    resultSql += " '" + value + "'";
                }

                //判断是否有后续
                if (i != condList.size() && StrUtil.isNotEmpty(cond.getAndOr())) {
                    resultSql += " " + cond.getAndOr() + " ";
                }
            }
        }
        return resultSql;
    }

    public static String getDataToSql(String colJson, String tabJson, String whereJson, Map<String, Object> map) {
        //获取解析后的字段
        ColumnsDto cold = PackagingUtil.getCloumn(colJson);
        //表
        List<TableDto> tabList = PackagingUtil.getTables(tabJson);
        //条件
        List<ConditionsDto> condList = PackagingUtil.getConditions(whereJson);
        String ss = PackagingUtil.getBeanToSqlField(cold);
        String tt = PackagingUtil.getBeanToTable(tabList);
        String ww = PackagingUtil.getBeanToWhere(condList, map);

        return "SELECT " + ss + " FROM " + tt + " WHERE " + ww;
    }

    public static void main(String[] args) {
//        String json = "{\"columns\":{ \"fields\":[{\"field\":\"字段A\",\"table\":\"表A\", \"alias\":\"别名A\" },{\"field\":\"字段B\", \"table\":\"表B\",\"alias\":\"别名B\"}],\"relation\":[{\"from\":\"字段A\",\"to\":\"字段B\",\"relat\":\"+,-,*,/,count,avg\"}]},\"from\":[{\"table\":\"表A\", \"alias\":\"别名A\",\"join\":\"左连接\"},{\"table\":\"表B\",\"alias\":\"别名B\"}],\"where\":[{\"filed\":\"字段A\",\"oper\":\"等于\",\"value\":\"${替代标签}\"}]}";
//        String json = "{\"columns\":{ \"fields\":[{\"field\":\"字段A\",\"table\":\"表A\", \"alias\":\"别名A\" }],\"relation\":[{\"from\":\"字段A\",\"to\":\"\",\"relat\":\"avg\"}]},\"from\":[{\"table\":\"表A\", \"alias\":\"别名A\",\"join\":\"左连接\",},{\"table\":\"表B\",\"alias\":\"别名B\"}],\"where\":[{\"filed\":\"字段A\",\"alias\":\"别名A\",\"oper\":\"等于\",\"value\":\"${替代标签}\",\"type\":2,\"status\":2,\"andOr\":\"and\"},{\"filed\":\"字段B\",\"alias\":\"别名B\",\"oper\":\"等于\",\"value\":\"1\",\"type\":1,\"status\":1,\"andOr\":\"\"}]}";
        String json = "{\"columns\":{ \"fields\":[{\"field\":\"字段A\",\"table\":\"表A\", \"alias\":\"别名A\" },{\"field\":\"字段B\", \"table\":\"表B\",\"alias\":\"别名B\"}],\"relation\":[{\"from\":\"字段A\",\"to\":\"字段B\",\"relat\":\"+,-,*,/,count,avg\"}]},\n" +
                "\"from\":[{\"table\":\"表A\", \"alias\":\"别名A\",\"join\":\"左连接\",\"joinField\":\"字段A\"},{\"table\":\"表B\",\"alias\":\"别名B\",\"join\":\"\",\"joinField\":\"字段B\"}],\n" +
                "\"where\":[{\"filed\":\"字段A\",\"alias\":\"别名A\",\"oper\":\"等于\",\"value\":\"${替代标签}\",\"type\":2,\"status\":2,\"andOr\":\"\"}]}";
        JSONObject obj = JSON.parseObject(json);

        String col = obj.getString("columns");
        String from = obj.getString("from");
        String where = obj.getString("where");

        //获取解析后的字段
        ColumnsDto cold = PackagingUtil.getCloumn(col);
        //表
        List<TableDto> tabList = PackagingUtil.getTables(from);
        //条件
        List<ConditionsDto> condList = PackagingUtil.getConditions(where);

        String ss = PackagingUtil.getBeanToSqlField(cold);
        System.out.println(ss);
        String tt = PackagingUtil.getBeanToTable(tabList);
        System.out.println(tt);
        Map<String, Object> map = new HashMap<>();
        map.put("替代标签", "ss");
        String ww = PackagingUtil.getBeanToWhere(condList, map);
        System.out.println(PackagingUtil.getDataToSql(col, from, where, map));
    }

    /**
     * 根据不同计算
     */
    public enum EnumReckon {
        AVG, COUNT, SUM, MAX, MIN;

        public static boolean getEnumReckon(String enumName) {

            switch (enumName) {
                case "AVG":
                    return true;
                case "COUNT":
                    return true;
                case "SUM":
                    return true;
                case "MAX":
                    return true;
                case "MIN":
                    return true;
                default:
                    return false;
            }

        }
    }
}
