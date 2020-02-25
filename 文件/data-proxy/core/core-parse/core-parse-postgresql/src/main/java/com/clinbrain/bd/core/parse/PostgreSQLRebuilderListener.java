package com.clinbrain.bd.core.parse;

import com.alibaba.fastjson.JSONObject;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import org.antlr.v4.runtime.tree.Trees;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import com.clinbrain.bd.core.parse.autogen.PostgreSQLStatementBaseListener;
import com.clinbrain.bd.core.parse.autogen.PostgreSQLStatementParser;
import com.clinbrain.bd.core.parse.util.PostgreSQLParseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * @author WANGYI
 * @className com.clinbrain.bd.core.parse.PostgreSQLRebuilderListener
 * @createdDate 2019/6/12 11:17
 * @description TODO
 * @e-mail WANGYI@clinbrain.com
 * @group bigdata develop group (shardingsphere)
 */
public class PostgreSQLRebuilderListener extends PostgreSQLStatementBaseListener{
    private static final String CONFIG_PATH_DIR = "/opt/complements/metadatajars/data-proxy-config";
    private String username;
    public PostgreSQLRebuilderListener(){}
    public PostgreSQLRebuilderListener(String username){this.username=username;}

    Logger LOGGER = LoggerFactory.getLogger(PostgreSQLRebuilderListener.class);
    /******************************************************************************************************
     * TODO 以下区域为处理行级权限（1）和脱敏加密区域公用区域（2）
     *@author WANGYI
     *@createdDate 2019/3/14 14:13
     *@description TODO 进入TableReferenceContext 时，判断这个table是不是真是的表，如果是就加入list
     *@class MySqlParserBaseListenerAdapter
     *@group bigdata develop group ()
     *@params
     *@return
     *@updatedDate
     *@updatedBy
     ******************************************************************************************************/
    @Override
    public void enterTableReference(PostgreSQLStatementParser.TableReferenceContext ctx){
        /*判断是不是真是的表*/
        if(ctx.tableFactor()==null || ctx.tableFactor().isEmpty()){
            tableSourceItems.peek().add(ctx);
        }else if(ctx.tableFactor(0).subquery()==null||ctx.tableFactor(0).subquery().isEmpty()){
            tableSourceItems.peek().add(ctx);
        }
    }


    /******************************************************************************************************
     * TODO 以下区域为处理行级权限区域
     *@author WANGYI
     *@createdDate 2019/3/14 14:14
     *@description TODO
     *@class MySqlParserBaseListenerAdapter
     *@group bigdata develop group ()
     *@params
     *@return
     *@updatedDate
     *@updatedBy
     ******************************************************************************************************/
    /**
     * TODO 存放每一个from里面的原表
     */
    private Stack<List<PostgreSQLStatementParser.TableReferenceContext>> tableSourceItems = new Stack<List<PostgreSQLStatementParser.TableReferenceContext>>();

    /**
     *@author WANGYI
     *@createdDate 2019/3/11 16:08
     *@description TODO 进入from语句块时压栈，压入一个list用来存放from的表
     *@class MySqlRebuildListener
     *@group bigdata develop group ()
     *@params [ctx]
     *@return void
     *@updatedDate
     *@updatedBy
     */
    @Override
    public void enterSelectClause(PostgreSQLStatementParser.SelectClauseContext ctx) {
        tableSourceItems.push(new ArrayList<PostgreSQLStatementParser.TableReferenceContext>(16));
    }
    /**
     *@author WANGYI
     *@createdDate 2019/3/11 16:10
     *@description TODO 退出from语句块时弹栈,并且将from所牵涉到的表根据权限增加where限制，实现数据的行级权限
     *@class MySqlRebuildListener
     *@group bigdata develop group ()
     *@params [ctx]
     *@return void
     *@updatedDate
     *@updatedBy
     */
    @Override
    public void exitSelectClause(PostgreSQLStatementParser.SelectClauseContext ctx) {
        List<PostgreSQLStatementParser.TableReferenceContext> tables = tableSourceItems.peek();
        PostgreSQLStatementParser.WhereClauseContext whereExpr = ctx.whereClause();
        PostgreSQLStatementParser.ExprContext exprCtx = whereExpr == null?null:ctx.whereClause().expr();
        //为每一个表获取权限策略
        List<String> filterStrings = new ArrayList<String>();
        String [] temp = new String[2];
        for(PostgreSQLStatementParser.TableReferenceContext tab:tables) {
            int childLen = tab.getChildCount();
            if(childLen<1){continue;}
            String tableName = tab.getChild(0).getText();
            /*如果是符合条件的系统表，以及明确指定的不受控制的表直接跳过*/
            if(tableName.toLowerCase().startsWith("pg_catalog.")||
                    "INFORMATION_SCHEMA.SCHEMATA".equalsIgnoreCase(tableName)||
                    "INFORMATION_SCHEMA".equalsIgnoreCase(tableName)||
                    "SCHEMATA".equalsIgnoreCase(tableName)){
                continue;
            }

            String tableAlias = childLen==1?null:tab.getChild(childLen-1).getText();
            String preString = tableAlias != null?tableAlias+".":tableName+".";
            Map<String,List<String>> privileges = getUserPrivileges(username,tableName,tableAlias);
            temp[0] = null;
            temp[1] = null;
            //如果权限为空
            if(privileges==null||privileges.isEmpty()){
                filterStrings.add(" 1 = 0 ");
                break;
            }

            List<String> andfilters = privileges.get("andfilters");
            List<String> orfilters = privileges.get("orfilters");
            if((andfilters ==null || andfilters.isEmpty())&&(orfilters==null || orfilters.isEmpty())){
                filterStrings.add(" 1 = 0 ");
                break;
            }
            //TODO 添加别名,后面需要调整添加别名的时机
            if(andfilters!=null&&!andfilters.isEmpty()){
                for(int i=0;i<andfilters.size();i++){
                    String t = andfilters.get(i);
                    andfilters.set(i,preString+t);
                }
            }
            if(orfilters!=null&&!orfilters.isEmpty()){
                for(int i=0;i<orfilters.size();i++){
                    String t = orfilters.get(i);
                    orfilters.set(i,preString+t);
                }
            }

            temp[0] = StringUtils.join(andfilters," AND ");
            temp[1] = StringUtils.join(orfilters," OR ");
            String filterString = null;
            if(StringUtils.isNotBlank(temp[0])&&StringUtils.isNotBlank(temp[1])){
                filterString = StringUtils.join(temp," OR ");
            }
            if(StringUtils.isBlank(temp[0])){
                filterString = temp[1];
            }
            if(StringUtils.isBlank(temp[1])){
                filterString = temp[0];
            }
            //TODO 根据实际是否取消下面这个if （如果既没有and 条件也没有or条件 权限的处理策略 从严/从宽  这里暂时从严）
            /*if(StringUtils.isBlank(temp[0])&&StringUtils.isBlank(temp[1])){
                filterString = " 1 = 0 ";
            }*/
            if(StringUtils.isNotBlank(filterString)){
                filterStrings.add(filterString);
            }
        }
        String filterString = StringUtils.join(filterStrings," AND ");
        PostgreSQLStatementParser.ExprContext exprContext = PostgreSQLParseUtil.createOrAppendExpressionContext(exprCtx,filterString);
        if(whereExpr==null&&StringUtils.isNotBlank(filterString)){
            ctx.fromClause().addChild(new TerminalNodeImpl(new CommonToken(0,"WHERE")));
            ctx.fromClause().addChild(exprContext);
        }
        tableSourceItems.pop();
    }
    /**
     *@author WANGYI
     *@createdDate 2019/3/13 14:12
     *@description TODO 获取用户的表访问权限（后期待升级为从缓存中生成）
     *@class MySqlRebuildListener
     *@group bigdata develop group ()
     *@params [user, sysUserId, tableName, tableAlias]
     *@return java.util.Map<java.lang.String,java.util.List<java.lang.String>>
     *@updatedDate
     *@updatedBy
     */
    private Map<String,List<String>> getUserPrivileges(String username, String tableName, String tableAlias) {
        try {
            String realTable = new String (tableName);
            realTable = org.apache.commons.lang3.StringUtils.replaceChars(realTable,"`'","");
            if(StringUtils.contains(realTable,".")){
                realTable = StringUtils.substringAfter(realTable,".");
            }
            String input = FileUtils.readFileToString(new File(
                    CONFIG_PATH_DIR+"/power/"+username+"_"+"power.json"
            ));
            JSONObject jsonObject = JSONObject.parseObject(input);
            Map<String,List<String>> powers = (Map<String,List<String>>)jsonObject.get(realTable);
            return powers;
        } catch (Exception e) {
            LOGGER.error("获取权限出现错误",e);
            return null;
        }
    }

}
