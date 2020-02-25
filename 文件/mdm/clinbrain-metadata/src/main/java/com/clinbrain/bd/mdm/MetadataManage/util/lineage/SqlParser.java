package com.clinbrain.bd.mdm.MetadataManage.util.lineage;

import com.clinbrain.bd.mdm.MetadataManage.entity.LineageLink;
import com.clinbrain.bd.mdm.MetadataManage.entity.LineageNode;
import com.clinbrain.bd.mdm.MetadataManage.entity.LineageSql;
import com.clinbrain.bd.mdm.MetadataManage.entity.LineageTable;
import com.clinbrain.bd.mdm.MetadataManage.util.cost.MetaModuleConst;
import com.clinbrain.parser.common.entity.Column;
import com.clinbrain.parser.common.entity.Table;
import com.clinbrain.parser.mysql.utils.ParseUtil;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SqlParser extends AbstractParser<LineageSql>{
    public static final LineageSql END_NODE= new LineageSql();
    @Override
    LineageSql accept() {
        try {
            return enviroment.getSqlQueue().take();
        } catch (InterruptedException e) {
            LOGGER.error("获取任务时发生错误：",e);
        }
        return null;
    }

    @Override
    void doParse(LineageSql lineageSql) throws IOException {
        List<LineageNode> nodes = new ArrayList<LineageNode>();
        Set<String> columntables = new HashSet<String>();
        LineageNode rootNode = lineageSql.getRootNode();
        List<Table> tables = ParseUtil.parseSelect(lineageSql.getSql());
        if(tables==null||tables.isEmpty()){
            return ;
        }
        for(Table t:tables){
            if(!t.getColumns().isEmpty()){
                for(Column c: t.getColumns()){
                    LineageNode node = null;
                    if(StringUtils.isEmpty(c.getExpr())){
                        //将列转化为元数据中的列
                        node = enviroment.transferColumn2MetadataColumn(c);
                        if(node.getId()==null)continue;
                        //node.setModuleId(MetaModuleConst.COLUMN_MODULE_ID);
                        enviroment.getNode().add(node);
                        nodes.add(node);
                        enviroment.parentIds.add(node.getParentId());
                        LineageLink link= new LineageLink();
                        link.setFrom(rootNode.getId());
                        link.setTo(node.getId());
                        link.setColor("lightblue");
                        enviroment.getLink().add(link);
                    }else{
                        //添加一个临时节点
                        LineageNode tempnode =null;
                        if(c.getColumns().size()>=1){
                            tempnode = enviroment.addTempNode(rootNode,c);
                        }
                        for(Column col:c.getColumns()){
                            node = enviroment.transferColumn2MetadataColumn(col);
                            if(node==null||node.getId()==null)continue;
                            //node.setModuleId(MetaModuleConst.COLUMN_MODULE_ID);
                            enviroment.parentIds.add(node.getParentId());
                            enviroment.getNode().add(node);
                            nodes.add(node);
                            LineageLink link= new LineageLink();
                            link.setFrom(tempnode==null?rootNode.getId():tempnode.getId());
                            link.setColor(tempnode==null?"lightblue":null);
                            link.setTo(node.getId());
                            if(tempnode==null) link.setComment(c.getExpr());
                            enviroment.getLink().add(link);
                        }
                    }

                }
            }
        }
        //补全table
        for(LineageNode n:nodes){
            if(MetaModuleConst.COLUMN_MODULE_ID.equalsIgnoreCase(n.getModuleId()))
                columntables.add(n.getParentId());
        }
        if(!columntables.isEmpty()){
            List<LineageNode> lineageNodes = enviroment.getLineageTableNode(columntables);
            if(lineageNodes!=null&&!lineageNodes.isEmpty())
                enviroment.getNode().addAll(lineageNodes);
                nodes.addAll(lineageNodes);
        }

        List<LineageTable> parsedTables = new ArrayList<LineageTable>();
        LineageTable table = null;
        for(LineageNode n:nodes){
            if(MetaModuleConst.TABLE_MODULE_ID.equalsIgnoreCase(n.getModuleId())){
                table=new LineageTable();
                table.setTableNode(n);
                //寻找字段
                for(LineageNode f:nodes){
                    if(MetaModuleConst.COLUMN_MODULE_ID.equalsIgnoreCase(f.getModuleId())&&
                            n.getId().equalsIgnoreCase(f.getParentId())){
                        table.getColumns().add(f);
                    }
                }
                parsedTables.add(table);
            }
        }
        if(!parsedTables.isEmpty()){
            enviroment.getTableQueue().addAll(parsedTables);
        }
    }

    @Override
    boolean isEndNode(LineageSql lineageSql) {
        return lineageSql == END_NODE;
    }
}
