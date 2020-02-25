package com.clinbrain.bd.mdm.MetadataManage.util.lineage;

import com.clinbrain.bd.mdm.MetadataManage.entity.EtlWorkflow;
import com.clinbrain.bd.mdm.MetadataManage.entity.LineageLink;
import com.clinbrain.bd.mdm.MetadataManage.entity.LineageNode;
import com.clinbrain.bd.mdm.MetadataManage.entity.LineageTable;
import com.clinbrain.bd.mdm.MetadataManage.util.cost.MetaModuleConst;
import com.clinbrain.parser.common.entity.Column;
import com.clinbrain.parser.common.entity.Table;
import com.clinbrain.parser.datax.utils.DataXParseUtil;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TableParser extends AbstractParser<LineageTable>{
    public static final LineageTable END_NODE = new LineageTable();
    @Override
    LineageTable accept() {
        try {
            return enviroment.getTableQueue().take();
        } catch (InterruptedException e) {
            LOGGER.error("获取任务时发生错误：",e);
        }
        return null;
    }

    @Override
    void doParse(LineageTable t) {
        List<LineageNode> nodes = new ArrayList<LineageNode>();
        Set<String> columntables = new HashSet<String>();
            List<Table> etlTables = parseTableEtlLineage(t);
            for(LineageNode column:t.getColumns()){
                //解析etlTables
                for(Table etl:etlTables){
                    if(!etl.getColumns().isEmpty()){
                        for(Column c: etl.getColumns()){
                            LineageNode node = null;
                            if(StringUtils.isEmpty(c.getExpr())&&(column.getName().equalsIgnoreCase(c.getEtlTargetColumn().getfName()))){
                                //将列转化为元数据中的列
                                node = enviroment.transferColumn2MetadataColumn(c);
                                if(node.getId()==null)continue;
                                node.setModuleId(MetaModuleConst.COLUMN_MODULE_ID);
                                enviroment.getNode().add(node);
                                nodes.add(node);
                                LineageLink link= new LineageLink();
                                link.setFrom(column.getId());
                                link.setTo(node.getId());
                                link.setColor("lightblue");
                                enviroment.getLink().add(link);
                                break;
                            }else if((column.getName().equalsIgnoreCase(c.getEtlTargetColumn().getfName()))){
                                //添加一个临时节点
                                LineageNode tempnode =null;
                                if(c.getColumns().size()>=1){
                                    tempnode = enviroment.addTempNode(column,c);
                                }
                                for(Column col:c.getColumns()){
                                    node = enviroment.transferColumn2MetadataColumn(col);
                                    if(node==null||node.getId()==null)continue;
                                    node.setModuleId(MetaModuleConst.COLUMN_MODULE_ID);
                                    nodes.add(node);
                                    enviroment.getNode().add(node);
                                    LineageLink link= new LineageLink();
                                    link.setFrom(tempnode==null?column.getId():tempnode.getId());
                                    link.setColor(tempnode==null?"lightblue":null);
                                    link.setTo(node.getId());
                                    if(tempnode==null) link.setComment(c.getExpr());
                                    enviroment.getLink().add(link);
                                }
                                break;
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
            //enviroment.getTableQueue().addAll(parsedTables);
        }
    }

    @Override
    boolean isEndNode(LineageTable lineageTable) {
        return lineageTable == END_NODE;
    }

    /**
     * 解析ETL血缘关系
     */
    private List<Table> parseTableEtlLineage(LineageTable lineageTable){
        EtlWorkflow workflow = new EtlWorkflow();
        //查询目标表为当前表的workflow 解析出workflow的源表
        /**
         * 1.查找目标为table的workflow
         * 2.解析json得到源  Node
         * 3.递归源
         */
        if("DataXCompoment".equalsIgnoreCase(workflow.excutor)){
            try {
                return DataXParseUtil.parseDataxJson(workflow.parama);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //组装成节点，添加连线
        //再次将源表作为目标表解析。。。
        return null;
    }
}
