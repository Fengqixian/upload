package com.clinbrain.bd.dataserver.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clinbrain.bd.dataserver.common.ResponseData;
import com.clinbrain.bd.dataserver.common.factory.ConnectionFactory;
import com.clinbrain.bd.dataserver.dto.DataServer;
import com.clinbrain.bd.dataserver.entity.ConnectionParam;
import com.clinbrain.bd.dataserver.entity.DataSet;
import com.clinbrain.bd.dataserver.entity.DbTypeEnum;
import com.clinbrain.bd.dataserver.service.ConnectionService;
import com.clinbrain.bd.dataserver.service.DataProviderService;
import com.clinbrain.bd.dataserver.service.DataServerService;
import com.clinbrain.bd.mdm.common.core.util.R;
import com.clinbrain.bd.mdm.common.log.annotation.SysLog;
import com.clinbrain.parser.common.entity.Column;
import com.clinbrain.parser.mysql.utils.ParseUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author WANGYI
 * @className com.clinbrain.bd.dataserver.controller.DataServerController
 * @createdDate 2019/5/8 14:48
 * @description TODO
 * @e-mail WANGYI@clinbrain.com
 * @group bigdata develop group (dataserver)
 */
@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/dataServer")
public class DataServerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataServerController.class);
    @Autowired
    private DataProviderService dataProviderService;

    private final DataServerService dataServerService;
    @Autowired
    private ConnectionService connectionService;

    @RequestMapping("/s/{key}")
    @SysLog("数据服务调用")
    public ResponseData<List<Map<String,Object>>> dataServer(@PathVariable String key,
                                                             @RequestParam (required = false) Map param) throws Exception {
        if(!StringUtils.isBlank(key)){
            List<DataServer> dataServerList = dataServerService.getDataServer();
            for(DataServer dataServer:dataServerList){
                if(dataServer.getServerName().equals(key)){
                    String sql = dataServer.getQuerySql();
                    String connectionId = dataServer.getDatasourceId();
                    String returnType = dataServer.getReturnType();
                    //region
                    /*
                    Map<String, Object> map = new HashMap<String, Object>();
                    map = JSONObject.parseObject(param);
                    if(map!=null && map.size()>1){
                        Pattern p = Pattern.compile("(\\#\\{)([\\w]+)(\\})");
                        Matcher m = p.matcher(sql);
                        for(Map.Entry<String, Object> vo : map.entrySet()){
                            String pkey = vo.getKey();
                            String pvalue = (String)vo.getValue();
                            if(m.find()){
                                if(sql.indexOf(pkey)>0){
                                    m.appendReplacement(buff, "'"+pvalue+"'");
                                }
                            }
                        }
                        m.appendTail(buff);
                    }else {
                        buff.append(sql);
                    }
                    */
                    //endregion
                    //Map<String,Object> whereP = JSONObject.parseObject(param,Map.class);
                    Map<String,Object> whereP = param;
                    Map<String,Object> whereParams = new HashMap<>();
                    //转换为大写
                    if(whereP!=null && !whereP.isEmpty()){
                        Set<String> keySet = whereP.keySet();
                        for(String k:keySet){
                            whereParams.put(k.toUpperCase(),whereP.get(k));
                        }
                    }
                    //释放内存空间
                    if(whereP!=null){
                        whereP.clear();
                        whereP=null;
                    }
                    Map<Integer,Object> paramIndex = new HashMap<>();
                    char[] sqlChar = sql.toCharArray();
                    //遍历char[]
                    int startIndex = -1;
                    int endIndex = -1;
                    char startChar11 = '$';
                    char startChar12 = '#';
                    char startChar2 = '{';
                    char endChar = '}';
                    int charLen = sqlChar.length;
                    int pIndex = 1;
                    StringBuffer strBuf = new StringBuffer();
                    for(int i = 0;i<charLen;i++){
                        //找开始
                        if(i<charLen-1&&(sqlChar[i]==startChar11||sqlChar[i]==startChar12)&&sqlChar[i+1]==startChar2){
                            startIndex=i;
                        }
                        //找结束
                        if(startIndex>=0&&sqlChar[i]==endChar){
                            endIndex = i;
                        }
                        //不在开始结束之间的字符原样拼接
                        if(startIndex==-1&&endIndex==-1){
                            strBuf.append(sqlChar[i]);
                        }
                        //找到开始和结束
                        if(startIndex>=0&&endIndex>=0){
                            String paramName = new String(sqlChar,startIndex+2,endIndex-startIndex-2);
                            Object value = whereParams.get(paramName.toUpperCase());
                            if(value ==null){
                                //将SQL处理成 ？ 占位符
                                strBuf.append('\'');
                                strBuf.append('?');
                                strBuf.append('\'');
                                paramIndex.put(pIndex++,value);
                                //下一个
                                startIndex = -1;
                                endIndex = -1;
                                continue;
                            }
                            if(value instanceof Collection){
                                value = ((Collection) value).toArray();
                            }
                            if(value.getClass().isArray()){
                                Object[] v = (Object[])value;
                                int len = v.length;
                                for(int n=0;n<len;n++){
                                    if(n!=0) strBuf.append(',');
                                    strBuf.append('\'');
                                    strBuf.append('?');
                                    strBuf.append('\'');
                                    paramIndex.put(pIndex++,v[n]);
                                }
                                if(len == 0){
                                    strBuf.append('\'');
                                    strBuf.append('?');
                                    strBuf.append('\'');
                                    paramIndex.put(pIndex++,null);
                                }
                            }else{
                                //将SQL处理成 '？' 占位符
                                strBuf.append('\'');
                                strBuf.append('?');
                                strBuf.append('\'');

                                paramIndex.put(pIndex++,value);
                            }
                            //下一个
                            startIndex = -1;
                            endIndex = -1;
                        }
                    }
                    System.out.println("合并参数的SQL："+strBuf.toString());
                    String strs = ParseUtil.trimSql(strBuf.toString(),paramIndex);
                    //String strs = strBuf.toString();
                    strs= strs.replace("\'?\'", "?");
                    System.out.println("转换后："+strs);
                    Connection connection =null;
                    PreparedStatement preparedStatement =null;
                    ResultSet resultSet = null;
                    List<Map<String,Object>>  result = new ArrayList<Map<String, Object>>();
                    String str = "";
                    try{
                        ConnectionParam connectionParam = getConnection(connectionId);
                        /**
                         * GP测试环境
                         */
                        /*connectionParam.setDatabase("psotgres");
                        connectionParam.setUsername("gpadmin");
                        connectionParam.setPassword("gpadmin");
                        connectionParam.setIpAddr("172.21.230.33");
                        connectionParam.setPort("5432");
                        connectionParam.setDbType(DbTypeEnum.GREENPLUM);*/


                        connection = ConnectionFactory.getConnectionFactory().createConnection(connectionParam);//("jdbc:pivotal:greenplum://192.168.0.113:5432;DatabaseName=cdr","gpadmin","gpadmin");
                        preparedStatement = connection.prepareStatement(strs);
                        Map<Integer,Object> paramIndexNew = new HashMap<>();
                        int index = 0;
                        for(Integer pKey:paramIndex.keySet()){
                            if(paramIndex.get(pKey)!=null){
                                index++;
                                paramIndexNew.put(index,paramIndex.get(pKey));
                            }
                        }
                        for(Integer pKey:paramIndexNew.keySet()){
                            preparedStatement.setObject(pKey,paramIndexNew.get(pKey));
                        }
                        System.out.println("参数 ："+paramIndexNew.toString());
                        long startTime = System.currentTimeMillis();
                        resultSet = preparedStatement.executeQuery();

                        if("JSON".equals(returnType.toUpperCase())){
                            LOGGER.info("总共耗时间："+(System.currentTimeMillis()-startTime)+"ms");
                            tranfResultSet2Map(resultSet,result);
                            LOGGER.info("总共耗时间："+(System.currentTimeMillis()-startTime)+"ms");
                            return new ResponseData.Builder<List<Map<String,Object>>>().data(result).success();
                        }else if("XML".equals(returnType.toUpperCase())){
                            str = tranfResultSet2Xml(resultSet);
                            return new ResponseData.Builder<String>().data(str).success();
                        }else if("DATASET".equals(returnType.toUpperCase())){
                            DataSet dataSet = tranToDataSet(resultSet);
                            return new ResponseData.Builder<DataSet>().data(dataSet).success();
                        }
                    } finally {
                        if(resultSet!=null) resultSet.close();
                        if(preparedStatement!=null) preparedStatement.close();
                        if(connection!=null) connection.close();
                    }


                }
            }
        }else {
            System.out.println("非法服务！");
            return new ResponseData.Builder<List<Map<String,Object>>>().message("非法服务！").success();
        }
        return new ResponseData.Builder<List<Map<String,Object>>>().message("无此数据服务"+key+"，请稍后重试。").success();
    }
    @GetMapping("/page")
    public R<IPage<DataServer>> getDataServerPage(Page<DataServer> page, DataServer dataServer){
        return new R<>(dataServerService.getDataServerPage(page,dataServer));
    }

    public ConnectionParam getConnection(String connectionId){
        ConnectionParam connectionParam = new ConnectionParam();
        List<Map> list = connectionService.getConnection(connectionId);
        Map map = (Map)list.get(0);
        connectionParam.setDatabase(map.get("DATABASE_NAME")+"");
        connectionParam.setDbType(DbTypeEnum.parseValue(map.get("DATABASE_TYPE")+""));
        connectionParam.setIpAddr(map.get("SERVER_NAME")+"");
        connectionParam.setUsername(map.get("LOGIN_NAME")+"");
        connectionParam.setPassword(map.get("LOGIN_PWD")+"");
        connectionParam.setPort(map.get("PORT")+"");
        return connectionParam;
    }
    /**
     * @param  model
     * @return
     * @content 新增 修改 通用方法
     */
    @PostMapping("edit")
    @SysLog("编辑数据服务")
    @ResponseBody
    //@PreAuthorize("@pms.hasPermission('DataServer_dataServer_add,DataServer_dataServer_update')")
    public R edit(@RequestBody DataServer model){
        DataServer o = dataServerService.edit(model);
        return new R<>(o);
    }

    /**
     * 查询 通过id单挑记录
     * @param id
     * @return
     * \
     */
    @GetMapping("/{id}")
    public R<DataServer> getById(@PathVariable("id") Integer id){
        return new R<>(dataServerService.getById(id));
    }

    @SysLog("删除数据服务")
    @DeleteMapping("/{id}")
    //@PreAuthorize("@pms.hasPermission('DataServer_dataServer_del')")
    public R removeById(@PathVariable Integer id){
        return new R<>(dataServerService.removeById(id));
    }

    // region  ***注释：王益之前的方法***
    /*
    @RequestMapping("/{key}")
    public ResponseData<List<Map<String,Object>>> dataServer(@PathVariable String key,
                                                             @RequestBody(required = false) String param) throws Exception {
        //Map<String,Object> param = new HashMap<String, Object>();
        JSONObject jsonObject = JSONObject.parseObject(param);
        List<Column> selectColumns = JSONArray.parseArray(jsonObject.get("selectColumns").toString(),Column.class);
        Map<String,Object> whereP = JSONObject.parseObject(jsonObject.get("whereParams").toString(),Map.class);
        Map<String,Object> whereParams = new HashMap<String, Object>();
        //转换为大写
        if(whereP!=null && !whereP.isEmpty()){
            Set<String> keySet = whereP.keySet();
            for(String k:keySet){
                whereParams.put(k.toUpperCase(),whereP.get(k));
            }
        }
        //释放内存空间
        whereP.clear();
        whereP=null;

        List<Column> orderBy = JSONArray.parseArray(jsonObject.get("orderBy").toString(),Column.class);;
        List<String> groupBy = JSONArray.parseArray(jsonObject.get("groupBy").toString(),String.class);;

        //selectColumns.add(new Column("hospitalno",null,null));
        //selectColumns.add(new Column("hospitalno",null,null));
        //selectColumns.add(new Column("hospitalno",null,null));

        //whereParams.put("deptCode","209");
        //whereParams.put("hospitalNo","12450000498500618J");

        //param.put("selectColumns",selectColumns);
        //param.put("whereParams",whereParams);
        //param.put("orderBy",orderBy);
        //param.put("groupBy",groupBy);

        *//*
         *{
         *       selectColumns:[
         *           {name:"id",agFunction:"count"},
         *           {name:"username",agFunction:""},
         *           {name:"amount",agFunction:"sum"}
         *       ],
         *       whereParams:{
         *           key1:value1,
         *           key2:value2,
         *           key3:value3,
         *       },
         *       orderBy:[
         *           {name:"id",type:"asc"},
         *           {name:"name",type:"desc"},
         *       ],
         *       groupBy:["username"]
         * }
         *//*
        //应用调用参数
        Map<Integer,Object> paramIndex = new HashMap<Integer, Object>();
        //1、根据id获取SQL
        //String sql = dataProviderService.getExecuteSqlWithOutParam(key);
        //JSONObject sqlObj = JSONObject.parseObject(employeeService.selectSqlInfoById(key,new ArrayList<String>(whereParams.keySet())));
        JSONObject sqlObj = JSONObject.parseObject("{\n" +
                "  \"database_type\": \"gp\",\n" +
                "  \"server_name\": \"192.168.0.113\",\n" +
                "  \"login_name\": \"gpadmin\",\n" +
                "  \"database_name\": \"cdr\",\n" +
                "  \"port\": \"5432\",\n" +
                "  \"sqlInfo\": \" SELECT hospitalno, hospitalname, serialno, feeno, invoiceno, returnfeeno, patientid, visitnumber, orderno, deptcode, regdeptname, doctorcode, doctorname, doctordeptcode, doctordeptname, execdeptcode, execdeptname, confirmoperatorcode, confirmoperatorname, confirmdeptcode, confirmdeptname, confirmdatetime, confirmflag, itemcode, itemname, chargecategorycode, chargecategoryname, itemtypecode, itemtypename, itemgroupcode, itemgroupname, itemspec, drugoritem, unit, price, quantity, herbalnumber, totalmoney, accountingclass, accountingclassname, subjectcode, subjectname, ybratio, ybmoney, ybselfpaidmoney, ybselfmoney, specialmoney, registeredfee, clinicfee, receivableamount, recstatus, recstatusname, executeddate, regkindname, ybtransno, chargeflag, applyno, visitnumberid, str1, str2, str3, str4, str5, str6, num1, num2, date1, date2, resourcetable, resourcetablekey, resourcetablekeyvalue, isdeleted, lastupdatedttm, lastimportdttm FROM public.ab_op_feelist where deptcode = ${deptCode} and hospitalno = ${hospitalNo}\",\n" +
                "  \"login_pwd\": \"gpadmin\"\n" +
                "}");
        String sql = sqlObj.getString("sqlInfo");
        if(StringUtils.isBlank(sql)||sql.length()<5) return null;
        char[] sqlChar = sql.toCharArray();
        //遍历char[]
        int startIndex = -1;
        int endIndex = -1;
        char startChar11 = '$';
        char startChar12 = '#';
        char startChar2 = '{';
        char endChar = '}';
        int charLen = sqlChar.length;
        int pIndex = 1;
        StringBuffer strBuf = new StringBuffer();
        for(int i = 0;i<charLen;i++){
            //找开始
            if(i<charLen-1&&(sqlChar[i]==startChar11||sqlChar[i]==startChar12)&&sqlChar[i+1]==startChar2){
                startIndex=i;
            }
            //找结束
            if(startIndex>=0&&sqlChar[i]==endChar){
                endIndex = i;
            }
            //不在开始结束之间的字符原样拼接
            if(startIndex==-1&&endIndex==-1){
                strBuf.append(sqlChar[i]);
            }
            //找到开始和结束
            if(startIndex>=0&&endIndex>=0){
                String paramName = new String(sqlChar,startIndex+2,endIndex-startIndex-2);
                Object value = whereParams.get(paramName.toUpperCase());
                if(value ==null){
                    //将SQL处理成 ？ 占位符
                    //strBuf.append('\'');
                    strBuf.append('?');
                    //strBuf.append('\'');
                    paramIndex.put(pIndex++,value);
                    //下一个
                    startIndex = -1;
                    endIndex = -1;
                    continue;
                }
                if(value instanceof Collection){
                    value = ((Collection) value).toArray();
                }
                if(value.getClass().isArray()){
                    Object[] v = (Object[])value;
                    int len = v.length;
                    for(int n=0;n<len;n++){
                        if(n!=0) strBuf.append(',');
                        //strBuf.append('\'');
                        strBuf.append('?');
                        //strBuf.append('\'');
                        paramIndex.put(pIndex++,v[n]);
                    }
                    if(len == 0){
                        //strBuf.append('\'');
                        strBuf.append('?');
                        //strBuf.append('\'');
                        paramIndex.put(pIndex++,null);
                    }
                }else{
                    //将SQL处理成 '？' 占位符
                    //strBuf.append('\'');
                    strBuf.append('?');
                    //strBuf.append('\'');

                    paramIndex.put(pIndex++,value);
                }
                //下一个
                startIndex = -1;
                endIndex = -1;
            }
        }

        if(selectColumns==null||selectColumns.isEmpty()){
            strBuf = strBuf.insert(0,"select * from ( ");
            strBuf.append(") __t ");
        }else{
            //将groupBy找出来
            List<String> grpBy = new ArrayList<>();
            boolean isNeedGroupBy = false;
            StringBuffer buffer = new StringBuffer("select ");
            for(int i =0;i<selectColumns.size();i++){
                Column c = selectColumns.get(i);
                if(i!=0){
                    buffer.append(',');
                }
                buffer.append(c.getSelectExp());
                if(c.getAggFunction() ==null&&!groupBy.contains(c.getName())&&!grpBy.contains(c.getName())){
                    grpBy.add(c.getName());
                }
                if(c.getAggFunction() !=null){
                    isNeedGroupBy = true;
                }
            }
            if(isNeedGroupBy) groupBy.addAll(grpBy);
            buffer.append(" from (");
            strBuf = strBuf.insert(0,buffer);
            strBuf.append(") __t ");
        }
        if(groupBy!=null&&!groupBy.isEmpty())
            for(int i =0;i<groupBy.size();i++){
                String c = groupBy.get(i);
                if(i==0){
                    strBuf.append(" group by ");
                }else{
                    strBuf.append(',');
                }
                strBuf.append(c);
            }

        if(orderBy!=null&&!orderBy.isEmpty())
            for(int i =0;i<orderBy.size();i++){
                Column c = orderBy.get(i);
                if(i==0){
                    strBuf.append(" order by ");
                }else{
                    strBuf.append(',');
                }
                strBuf.append(c.getName());
                strBuf.append(' ');
                strBuf.append(c.getOrderType());
            }


        LOGGER.info(paramIndex.toString());
        LOGGER.info(strBuf.toString());

        Connection connection =null;
        PreparedStatement preparedStatement =null;
        ResultSet resultSet = null;
        List<Map<String,Object>>  result = new ArrayList<Map<String, Object>>();
        try{
            ConnectionParam connectionParam = new ConnectionParam();
            connectionParam.setDatabase(sqlObj.getString("database_name"));
            connectionParam.setUsername(sqlObj.getString("login_name"));
            connectionParam.setPassword(sqlObj.getString("login_pwd"));
            connectionParam.setIpAddr(sqlObj.getString("server_name"));
            connectionParam.setPort(sqlObj.getString("port"));
            connectionParam.setDbType(DbTypeEnum.parseValue(sqlObj.getString("database_type")));

            connection = ConnectionFactory.getConnectionFactory().createConnection(connectionParam);//("jdbc:pivotal:greenplum://192.168.0.113:5432;DatabaseName=cdr","gpadmin","gpadmin");
            preparedStatement = connection.prepareStatement(strBuf.toString());
            for(Integer pKey:paramIndex.keySet()){
                preparedStatement.setObject(pKey,paramIndex.get(pKey));
            }
            long startTime = System.currentTimeMillis();
            resultSet = preparedStatement.executeQuery();
            LOGGER.info("总共耗时间："+(System.currentTimeMillis()-startTime)+"ms");
            tranfResultSet2Map(resultSet,result);
            LOGGER.info("总共耗时间："+(System.currentTimeMillis()-startTime)+"ms");
            //Template template = new Template(key,sql,null);
            //Writer out = new StringWriter();
            //template.process(param,out);
            //LOGGER.info("out : -->" + out.toString());
        }finally {
            if(resultSet!=null) resultSet.close();
            if(preparedStatement!=null) preparedStatement.close();
            if(connection!=null) connection.close();
        }
        return new ResponseData.Builder<List<Map<String,Object>>>().data(result).success();
    }
    */
    // endregion

    private static void tranfResultSet2Map(ResultSet resultSet, List<Map<String,Object>> result) throws SQLException {
        ResultSetMetaData  metaData = resultSet.getMetaData();
        int count = metaData.getColumnCount();
        while (resultSet.next()){
            Map<String,Object> r = new HashMap<String, Object>();
            for (int i =1 ;i<=count;i++){
                r.put(metaData.getColumnName(i),resultSet.getObject(i));
            }
            result.add(r);
        }
    }

    private String tranfResultSet2Xml(ResultSet resultSet)throws SQLException{
        ResultSetMetaData  metaData = resultSet.getMetaData();
        int count = metaData.getColumnCount();
        StringBuffer xml= new StringBuffer("<?xml version='1.0' encoding='UTF-8'?>");
        int index=0;
        xml.append("<root>");
        while(resultSet.next()){
            index++;
            xml.append("<data_"+index+">");
            for (int i =1 ;i<=count;i++){
                xml.append("<"+metaData.getColumnName(i)+">"+resultSet.getObject(i)+"</"+metaData.getColumnName(i)+">");
            }
            xml.append("</data_"+index+">");
        }
        xml.append("</root>");
        return  xml.toString();
    }


    public static DataSet tranToDataSet(ResultSet resultSet) throws SQLException {
        DataSet dataSet = new DataSet();
        dataSet.setName("dataSet");
        ResultSetMetaData  metaData = resultSet.getMetaData();
        int count = metaData.getColumnCount();
        int rowId=0;
        DataSet.DataTable table = new DataSet.DataTable();
        List<DataSet.DataRow> rows = new ArrayList<DataSet.DataRow>();
        while (resultSet.next()){
            DataSet.DataRow row = new DataSet.DataRow();
            rowId++;
            Map map = new HashMap();
            for (int i =1 ;i<=count;i++){
                map.put(metaData.getColumnName(i),resultSet.getObject(i));
                row.setRow(map);
            }
            row.setRowId(rowId);
            rows.add(row);
            table.setTableId("");
            table.setDataRow(rows);
            table.setRowSize(rows.size());
        }
        dataSet.setDataTable(table);
        return dataSet;
    }

    /*public static void main(String[] args) {
        Pattern p = Pattern.compile("(\\#\\{)([\\w]+)(\\})");
        Matcher m = p.matcher("WHERE\n" +
                "\tdict_info.dict_type = #{typeValue} and dict_info.dict_name like #{nameValue}");
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String group = m.group(2);//规则中#{值}中的 值 一样 的数据
            System.out.println("符合规则中第二个的值"+group);
            //下一步是替换并且把替换好的值放到sb中
            m.appendReplacement(sb, "dog");
        }
        //把符合的数据追加到sb尾
        m.appendTail(sb);
        System.out.println(sb.toString());
    }*/
}
