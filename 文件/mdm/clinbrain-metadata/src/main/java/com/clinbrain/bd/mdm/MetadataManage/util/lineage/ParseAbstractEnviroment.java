package com.clinbrain.bd.mdm.MetadataManage.util.lineage;

import com.clinbrain.bd.mdm.MetadataManage.entity.*;
import com.clinbrain.bd.mdm.MetadataManage.service.impl.DataLineageServiceImpl;
import com.clinbrain.parser.common.entity.Column;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedTransferQueue;

public class ParseAbstractEnviroment {
    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractParser.class);
    protected DataLineageServiceImpl dataLineageService;
    protected ConcurrentLinkedQueue<String> parentIds = new ConcurrentLinkedQueue<String>();
    //任务队列
    private LinkedTransferQueue<MetadataValue> indexQueue = new LinkedTransferQueue<MetadataValue>();
    private LinkedTransferQueue<LineageSql> sqlQueue = new LinkedTransferQueue<LineageSql>();
    private LinkedTransferQueue<LineageTable> tableQueue = new LinkedTransferQueue<LineageTable>();
    //存放node link
    private ConcurrentLinkedQueue<LineageNode> node = new ConcurrentLinkedQueue<LineageNode>();
    private ConcurrentLinkedQueue<LineageLink> link = new ConcurrentLinkedQueue<LineageLink>();
    //注册的解析器
    private List<AbstractParser> registedParsers = new ArrayList<AbstractParser>();
    public ParseAbstractEnviroment(DataLineageServiceImpl dataLineageService){
        this.dataLineageService = dataLineageService;
    }

    protected void registedParser(AbstractParser parser){
        registedParsers.add(parser);
    }
    //启动运行
    public void enviromentStart() throws InterruptedException {
        //开启线程做任务
        for(AbstractParser parser:registedParsers) {
            parser.start();
        }
        //队列监控，只要不是三个队列都在阻塞的情况，都等待执行。
        while(!(indexQueue.hasWaitingConsumer()&&sqlQueue.hasWaitingConsumer()&&tableQueue.hasWaitingConsumer())){
            LOGGER.info("indexQueue watting count :"+indexQueue.getWaitingConsumerCount());
            LOGGER.info("sqlQueue watting count :"+sqlQueue.getWaitingConsumerCount());
            LOGGER.info("tableQueue watting count :"+tableQueue.getWaitingConsumerCount());
            Thread.sleep(1000);
        }

        LOGGER.info("indexQueue watting count :"+indexQueue.getWaitingConsumerCount());
        LOGGER.info("sqlQueue watting count :"+sqlQueue.getWaitingConsumerCount());
        LOGGER.info("tableQueue watting count :"+tableQueue.getWaitingConsumerCount());

        //终止命令发出
        indexQueue.transfer(IndexParser.END_NODE);
        sqlQueue.transfer(SqlParser.END_NODE);
        tableQueue.transfer(TableParser.END_NODE);
    }
    public <T extends AbstractParser> AbstractParser create(Class<T> clazz) throws InstantiationException, IllegalAccessException{
        T t = clazz.newInstance();
		t.init(this,clazz.getName());
		this.registedParser(t);
    	return t;
    }

    public LinkedTransferQueue<MetadataValue> getIndexQueue() {
        return indexQueue;
    }

    public LinkedTransferQueue<LineageSql> getSqlQueue() {
        return sqlQueue;
    }

    public LinkedTransferQueue<LineageTable> getTableQueue() {
        return tableQueue;
    }

    public ConcurrentLinkedQueue<LineageNode> getNode() {
        return node;
    }

    public ConcurrentLinkedQueue<LineageLink> getLink() {
        return link;
    }

    protected  <T> T transferMetadataValue2Node(MetadataValue metadataValue,Class<T> clazz) throws Exception{
        T obj  = clazz.newInstance();
        if(obj instanceof LineageNode){
            ((LineageNode) obj).setId(metadataValue.getResourceId());
            ((LineageNode) obj).setName(metadataValue.getNameCn());
            if(metadataValue.getNameCn() == null){
                ((LineageNode) obj).setName(metadataValue.getNameEn());
            }
            ((LineageNode) obj).setModuleId(metadataValue.getModelId());
            ((LineageNode) obj).setParentId(metadataValue.getParentId());
        }else if(obj instanceof LineageLink){

        }else{
            throw new Exception();
        }
        return obj;
    }

    protected LineageNode transferColumn2MetadataColumn(Column c) {
        return dataLineageService.transferColumn2MetadataColumn(c);
    }
    protected LineageNode addTempNode(LineageNode rootNode, Column c){
        LineageNode tempnode = new LineageNode();

        tempnode.setId(Base64Utils.encodeToString(c.getExpr().getBytes()));
        //tempnode.setModuleId(rootNode.getModuleId());
        tempnode.setParentId(rootNode.getParentId());
        tempnode.setType("temp");
        node.add(tempnode);
        //由节点指向临时节点
        LineageLink l= new LineageLink();
        l.setFrom(rootNode.getId());
        l.setTo(tempnode.getId());
        l.setComment(c.getExpr());
        l.setType("temp");
        link.add(l);
        return tempnode;
    }

    protected MetadataValue getObjectInfo(LineageNode node) {
        MetadataValue metadataValue = new MetadataValue();
        metadataValue.setResourceId(node.getId());
        metadataValue.setParentId(node.getParentId());
        return dataLineageService.getMetadataValue(metadataValue);
    }

    protected List<LineageNode> getLineageTableNode(Set<String> columntables) {
        return dataLineageService.getLineageTableNode(columntables);
    }
}
