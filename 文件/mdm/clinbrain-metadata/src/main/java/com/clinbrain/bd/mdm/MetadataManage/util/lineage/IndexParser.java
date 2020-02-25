package com.clinbrain.bd.mdm.MetadataManage.util.lineage;

import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.clinbrain.bd.mdm.MetadataManage.entity.*;
import com.clinbrain.bd.mdm.MetadataManage.util.cost.MetaModuleConst;
import com.clinbrain.parser.common.entity.Column;
import com.clinbrain.parser.common.entity.Table;
import com.clinbrain.parser.mysql.utils.ParseUtil;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class IndexParser extends AbstractParser<MetadataValue>{
    //模型信息
    private MetadataModel metadataModel ;
    private List<MetadataProperties> properties ;
    private String definationProp;
    private String calcMeasureTypeProp;

    public static final MetadataValue END_NODE= new MetadataValue();
    @Override
    MetadataValue accept() {
        try {
            return enviroment.getIndexQueue().take();
        } catch (InterruptedException e) {
            LOGGER.error("获取任务时发生错误：",e);
        }
        return null;
    }

    @Override
    void doParse(MetadataValue metadataValue) throws Exception {
        if(metadataModel == null){
            metadataModel = enviroment.dataLineageService.getMetadataModel(metadataValue);
            properties = enviroment.dataLineageService.getMetadataProperties(metadataValue);
            properties.stream().forEach(e -> {
                if("defination".equalsIgnoreCase(e.getNameEn())){
                    this.definationProp = e.getMappingField();
                }
                if("calc_measure_type".equalsIgnoreCase(e.getNameEn())||"calcMeasureType".equalsIgnoreCase(e.getNameEn())){
                    calcMeasureTypeProp = e.getMappingField();
                }
            });
        }
        LineageNode rootNode =enviroment.transferMetadataValue2Node(metadataValue,LineageNode.class);
        enviroment.getNode().add(rootNode);
        Map<String,Object> row = BeanUtils.beanToMap(metadataValue);
        String defination = row.get(definationProp)+"";
        String calcMeasureType = row.get(calcMeasureTypeProp)+"";
        if(StringUtils.isEmpty(defination)){
            return;
        }
        //如果是sql类型的，由sql处理
        if(MetaModuleConst.CALC_MEASURE_TYPE_SQL.equalsIgnoreCase(calcMeasureType)){
            //作为sql需要降指标的信息带过去否则无法连线
            LineageSql lineageSql = new LineageSql();
            lineageSql.setRootNode(rootNode);
            lineageSql.setSql(defination);
            enviroment.getSqlQueue().add(lineageSql);
            return ;
        }else if(MetaModuleConst.CALC_MEASURE_TYPE_MTH.equalsIgnoreCase(calcMeasureType)){
            List<Table> tables = null;
            try {
                tables = ParseUtil.parseSelect("select "+defination);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(tables==null||tables.isEmpty()){
                return;
            }
            for(Table t:tables){
                if(!t.getColumns().isEmpty()){
                    for(Column c: t.getColumns()){
                        LineageNode node = null;
                        if(StringUtils.isEmpty(c.getExpr())){
                            //将列转化为元数据中的列
                            node = enviroment.transferColumn2MetadataColumn(c);
                            if(node.getId()==null)continue;
                            //node.setModuleId(MetaModuleConst.PK_TARGETMODEL_ID);
                            //enviroment.getNode().add(node);
                            LineageLink link= new LineageLink();
                            link.setFrom(rootNode.getId());
                            link.setTo(node.getId());
                            link.setColor("lightblue");
                            enviroment.getLink().add(link);
                            doParseLinege(node);
                        }else{
                            //添加一个临时节点
                            LineageNode tempnode = null;
                            if(c.getColumns().size()>1){
                                tempnode = enviroment.addTempNode(rootNode,c);
                            }
                            for(Column col:c.getColumns()){
                                node = enviroment.transferColumn2MetadataColumn(col);
                                if(node.getId()==null)continue;
                                //node.setModuleId(MetaModuleConst.PK_TARGETMODEL_ID);
                                //enviroment.getNode().add(node);
                                LineageLink link= new LineageLink();
                                link.setFrom(tempnode==null?rootNode.getId():tempnode.getId());
                                link.setColor(tempnode==null?"lightblue":null);
                                link.setTo(node.getId());
                                enviroment.getLink().add(link);
                                if(tempnode==null) link.setComment(c.getExpr());
                                doParseLinege(node);
                            }
                        }

                    }
                }
            }
            return;
        }

    }

    @Override
    boolean isEndNode(MetadataValue stringObjectMap) {
        return stringObjectMap == END_NODE;
    }

    private void  doParseLinege(LineageNode node){
        MetadataValue metadataValue = enviroment.getObjectInfo(node);
        enviroment.getIndexQueue().add(metadataValue);
    }
}
