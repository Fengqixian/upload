package com.clinbrain.bd.mdm.strategy.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.clinbrain.bd.mdm.strategy.dto.RegDto;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: 梁乐乐
 * @Date: 2019/9/18 09:30
 * @Description: 正则表达式替换
 */
public class ReplaceUtil {


    /**
     * 根据正则表达式替换值
     *
     * @param data      数据
     * @param fieldName 替换值对应列
     * @param reg       正则
     * @param sign      替换符
     * @return 脱敏数据
     */
    public static Map<String, Object> dataToReg(Map<String, Object> data, String fieldName, String reg, String sign) {

        Object value = null;
        if (data.get(fieldName) == null) {
            if (data.get(fieldName.toUpperCase()) == null) {
                if (data.get(fieldName.toLowerCase()) == null) {

                } else {
                    value = data.get(fieldName.toLowerCase());
                    fieldName = fieldName.toLowerCase();
                }
            } else {
                value = data.get(fieldName.toUpperCase());
                fieldName = fieldName.toUpperCase();
            }
        } else {
            value = data.get(fieldName);
        }

        //递归查询下级觉得影响性能
        if (value instanceof List) {
            ReplaceUtil.dataListToReg((List) value, fieldName, reg, sign);
        } else if (value instanceof Map) {
            ReplaceUtil.dataToReg((Map) value, fieldName, reg, sign);
        }

        if (value != null) {
            data.put(fieldName, value.toString().replaceAll(reg, sign));
        }
        return data;
    }


    /**
     * list数据脱敏
     *
     * @param dataList  数据集合
     * @param fieldName 字段名
     * @param reg       正则
     * @param sign      替换符
     * @return 数据
     */
    public static List<Map<String, Object>> dataListToReg(List<Map<String, Object>> dataList, String fieldName, String reg, String sign) {
        dataList.stream().forEach(item -> {
            dataToReg(item, fieldName, reg, sign);
        });
        return dataList;
    }


    /**
     * 解析正则表达式集合
     *
     * @param dataList
     * @param regDtos
     * @return
     */
    public static List<Map<String, Object>> dataListToRegList(List<Map<String, Object>> dataList, List<RegDto> regDtos) {
        regDtos.stream().forEach(r -> {
            dataListToReg(dataList, r.getFieldName(), r.getReg(), r.getSign());
        });

        return dataList;
    }


    /**
     * @param dataList  数据集合
     * @param fieldName 字段集合
     * @param reg       正则
     * @param sign      替换符
     * @return
     */
    private static List<Map<String, Object>> dataListToRegFieldList(List<Map<String, Object>> dataList, List<String> fieldName, String reg, String sign, String encryption, boolean type) {
        if (dataList != null && dataList.size() > 0) {
            dataList.forEach(d -> {
                dataListToRegFieldMap(d, fieldName, reg, sign, encryption, type);
            });
        }
        return dataList;
    }

    private static void dataListToRegFieldMap(Map<String, Object> dataMap, List<String> fieldName, String reg, String sign, String encryption, boolean type) {

        boolean toListStr = false;
        for (String key : dataMap.keySet()) {
            Object value = dataMap.get(key);
            //递归查询下级觉得影响性能
            if (value instanceof List) {
                if (value != null && ((List) value).size() > 0 && ((List) value).get(0) instanceof Map) {
                    dataListToRegFieldList((List) value, fieldName, reg, sign, encryption, type);
                    toListStr = false;
                    continue;
                } else {
                    toListStr = true;
                }
            } else if (value instanceof Map) {
                dataListToRegFieldMap((Map) value, fieldName, reg, sign, encryption, type);
                continue;
            }
            if (fieldName.stream().filter(f -> (StrUtil.isNotEmpty(f) && f.equalsIgnoreCase(key))).count() > 0 && value != null) {
                Object toValue = null;
                if (toListStr) {
                    toValue = dataValueListToCode((List<String>) value, reg, sign, encryption, type);
                } else {
                    String strValue = value.toString();
                    toValue = dataValueToCode(strValue, reg, sign, encryption, type);
                }

                dataMap.put(key, toValue);
            }
        }
    }

    /**
     * 调用加密解密方法
     *
     * @param valueList  值集合
     * @param reg        加密规则
     * @param sign       替换符
     * @param encryption 加密方式
     * @param type       true 加密 false 解密
     * @return
     */
    private static List<String> dataValueListToCode(List<String> valueList, String reg, String sign, String encryption, boolean type) {
        List<String> dataValueList = new ArrayList<>();
        valueList.forEach(v -> {
            dataValueList.add(dataValueToCode(v, reg, sign, encryption, type));
        });
        return dataValueList;
    }

    private static String dataValueToCode(String value, String reg, String sign, String encryption, boolean type) {
        String strValue = value;
        if (type) {
            if ("REG".equalsIgnoreCase(encryption)) {
                strValue = ValueToReg(value, reg, sign);
            } else if ("BASE".equalsIgnoreCase(encryption)) {
                strValue = Base64Util.DataToBase64Enc(value);
            } else if ("AES".equalsIgnoreCase(encryption)) {
                strValue = AESBaseDataUtil.AESEncode(sign, value);
            } else if ("DES".equalsIgnoreCase(encryption)) {
                strValue = EncryUtil.encrypt(value, sign);
            } else {
                MD5Util.encode(value);
            }
        } else {
            if ("REG".equalsIgnoreCase(encryption)) {
//                        strValue = ValueToReg(value, reg, sign);
            } else if ("BASE".equalsIgnoreCase(encryption)) {
                strValue = Base64Util.Base64ToDataDec(value);
            } else if ("AES".equalsIgnoreCase(encryption)) {
                strValue = AESBaseDataUtil.AESDncode(sign, value);
            } else if ("DES".equalsIgnoreCase(encryption)) {
                strValue = EncryUtil.decrypt(value, sign);
            } else {
//                        strValue = MD5Util.encode(strValue);
            }
        }
        return strValue;
    }

    /**
     * 得到结果
     *
     * @param value 带加密的值
     * @param reg   脱敏方式
     * @param sign  替换符
     * @return 值
     */
    private static String ValueToReg(Object value, String reg, String sign) {
        if (value != null) {
            return value.toString().replaceAll(reg, sign);
        }
        return null;
    }


    /**
     * 数据加密
     *
     * @param dataList   数据
     * @param fieldName  名称
     * @param reg        加密方式
     * @param sign       替换符
     * @param encryption 加密类型
     * @return 加密数据
     */
    public static List<Map<String, Object>> dataListToEncode(List<Map<String, Object>> dataList, List<String> fieldName, String reg, String sign, String encryption) {
        return dataListToRegFieldList(dataList, fieldName, reg, sign, encryption, true);
    }

    public static void dataMapToEncode(Map<String, Object> dataMap, List<String> fieldName, String reg, String sign, String encryption) {
        dataListToRegFieldMap(dataMap, fieldName, reg, sign, encryption, true);
    }

    /**
     * 数据解密
     *
     * @param dataList   带解密数据
     * @param fieldName  解密字段
     * @param reg        解密表达式
     * @param sign       替换符
     * @param encryption 加密方式
     * @return
     */
    public static List<Map<String, Object>> dataListToDncode(List<Map<String, Object>> dataList, List<String> fieldName, String reg, String sign, String encryption) {
        return dataListToRegFieldList(dataList, fieldName, reg, sign, encryption, false);
    }

    public static void dataMapToDncode(Map<String, Object> dataMap, List<String> fieldName, String reg, String sign, String encryption) {
        dataListToRegFieldMap(dataMap, fieldName, reg, sign, encryption, false);
    }


    public static void main(String[] args) throws UnsupportedEncodingException {
//        List<Map<String, Object>> datalist = new ArrayList<>();
//        List datass = new ArrayList();
//        List datass1 = new ArrayList();
//        datass1.add("asdfasdfasfddas1111");
//        datass1.add("asdfasdfasfddas222432322");
//        datass1.add("asdfasdfasfddas22255666");
//        datass1.add("");
//        Map<String, Object> data = new HashMap<>();
//        data.put("nAme", "13123456789");
//        datalist.add(data);
//        data = new HashMap<>();
//        data.put("nAme", "13123456788");
//        data = new HashMap<>();
//        datalist.add(data);
//        data.put("nAme", "13123456787中文");
//        datalist.add(data);
//        datass.add(data);
//        data = new HashMap<>();
//        data.put("nAme", "13123456785");
//        datalist.add(data);
//        datass.add(data);
//        data = new HashMap<>();
//        data.put("nAme", datass1);
//        datalist.add(data);
//
//        RegDto regDto = new RegDto();
//        regDto.setFieldName("nAme");
////        regDto.setReg("(\\d{3})\\d{4}(\\d{4})");
//        regDto.setSign("*");
//        regDto.setReg(Base64.getEncoder().encodeToString("[\\u4e00-\\u9fa5]".getBytes()));
//        //替换所有字母"[a-zA-z]"
//
//        //匹配中文字母数字[\u4e00-\u9fa5_a-zA-Z0-9]
//        //展示部分信息 ([\u4e00-\u9fa5_a-zA-Z0-9]{1})[\u4e00-\u9fa5_a-zA-Z0-9]
//
//        List<String> fiel = new ArrayList<>();
//        fiel.add("nAme");
//        System.out.println(regDto.getReg());
//
//        String reg = new String(Base64.getDecoder().decode(regDto.getReg().getBytes()), "utf-8");
//        List<Map<String, Object>> datas = ReplaceUtil.dataListToEncode(datalist, fiel, reg, regDto.getSign(), "BASE");
//        datas.stream().forEach(System.out::println);
//        datas = ReplaceUtil.dataListToDncode(datalist, fiel, reg, regDto.getSign(), "BASE");
//        datas.stream().forEach(System.out::println);

        //验证姓名
//        String nameReg = "([\\u2E80-\\u9FFF]|\\w|[`~!@#$%^&*()+=|{}:;\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？])";
//                "[`~!@#$%^&*()+=|{}:;\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]"; 特殊字符
//                "[\\u4e00-\\u9fa5]"; 汉字
//                "[\\d]"; 数字
//                "(?<=.{1}).*(?=.{1})"; 保留前一位和后一位的字符串
//                "(\\d{3})\\d{4}(\\d{4})"; 电话号码 保留前3和后4位
//                "([\u4e00-\u9fa5.·\u36c3\u4DAE]{1})([\u4e00-\u9fa5.·\u36c3\u4DAE])"; 姓名保留前1位和后1位
//        String name = "中国121212中国!@#$%^asdf&*()AZa";
//
//        byte[] base64Token = "Basic cGlnOnBpZw==".substring(6).getBytes("UTF-8");
        byte[] base64Token = "Basic bXl3dXd1Om15d3V3dQ==".substring(6).getBytes("UTF-8");
        System.out.println(new String(Base64.decode(base64Token), CharsetUtil.UTF_8));
        System.out.print(Base64.encode("mywuwu:mywuwu".getBytes()).toString());
    }
}
