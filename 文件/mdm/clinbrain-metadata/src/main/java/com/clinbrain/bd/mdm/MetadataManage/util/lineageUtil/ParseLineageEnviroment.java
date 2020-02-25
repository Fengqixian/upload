package com.clinbrain.bd.mdm.MetadataManage.util.lineageUtil;

import com.clinbrain.bd.mdm.MetadataManage.entity.LineageLink;
import com.clinbrain.bd.mdm.MetadataManage.entity.LineageMetaValue;
import com.clinbrain.bd.mdm.MetadataManage.entity.LineageNode;
import com.clinbrain.bd.mdm.MetadataManage.service.impl.MetaDataLineageServiceImpl;
import com.clinbrain.bd.mdm.MetadataManage.util.lineage.AbstractParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ParseLineageEnviroment {
    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractParser.class);
    /*环境监控用 总解析器数量  等待任务的解析器数量*/
    protected AtomicInteger parserCount = new AtomicInteger(0);
    protected AtomicInteger parserWaitingCount = new AtomicInteger(0);

    protected MetaDataLineageServiceImpl metaDataLineageService;
    /*存放已经解析过的对象的UUID 防止造成循环解析*/
    protected List<String> hasParsedObjects = Collections.synchronizedList(new ArrayList<String>());

    protected ConcurrentLinkedQueue<String> parentIds = new ConcurrentLinkedQueue<String>();

    //数据元
    protected LinkedTransferQueue<LineageMetaValue> lineageItems = new LinkedTransferQueue<>();
    //字段
    protected LinkedTransferQueue<LineageMetaValue> lineageColumns = new LinkedTransferQueue<>();
    //表
    protected LinkedTransferQueue<LineageMetaValue> lineageTables = new LinkedTransferQueue<>();

    //存放解析过的对象的UUID
    protected ConcurrentLinkedQueue<String> parsedObjects = new ConcurrentLinkedQueue<String>();
    //存放node link
    private ConcurrentLinkedQueue<LineageNode> node = new ConcurrentLinkedQueue<LineageNode>();
    private ConcurrentLinkedQueue<LineageLink> link = new ConcurrentLinkedQueue<LineageLink>();
    //注册的解析器
    private List<AbstractLineageParserExecutor> registedParsers = new ArrayList<AbstractLineageParserExecutor>();

    public ParseLineageEnviroment(MetaDataLineageServiceImpl metaDataLineageService){
        this.metaDataLineageService = metaDataLineageService;
    }

    protected void registedParser(AbstractLineageParserExecutor parser){
        registedParsers.add(parser);
    }
    //启动运行
    public void enviromentStart() throws InterruptedException {
        parserCount.set(registedParsers.size());
        //开启线程做任务
        for(AbstractLineageParserExecutor parser:registedParsers) {
            parser.start();
        }
        //只要等待数小于总数就是还有任务没有解析
        while (true){
            if(parserWaitingCount.get()==parserCount.get()){
                break;
            }
            LOGGER.info("columns watting count :"+lineageColumns.getWaitingConsumerCount()+"   current count:"+lineageColumns.size());
            LOGGER.info("items watting count :"+lineageItems.getWaitingConsumerCount()+"   current count:"+lineageItems.size());
            Thread.sleep(1000);
        }
        //所有任务完成
        LOGGER.info("columns watting count :"+lineageColumns.getWaitingConsumerCount());
        LOGGER.info("items watting count :"+lineageItems.getWaitingConsumerCount());
        //终止命令发出
        lineageColumns.transfer(ColumnLineageParserExecutor.SHUTDOWN_COMMAND);
        lineageColumns.transfer(ColumnLineageParserExecutorHX.SHUTDOWN_COMMAND);
        //etls.transfer(EtlParserExecutor.SHUTDOWN_COMMAND);
        lineageItems.transfer(ItemLineageParserExecutor.SHUTDOWN_COMMAND);
        lineageItems.transfer(ItemLineageParserExecutorHX.SHUTDOWN_COMMAND);
    }
    public <T extends AbstractLineageParserExecutor> AbstractLineageParserExecutor create(Class<T> clazz) throws InstantiationException, IllegalAccessException{
        T t = clazz.newInstance();
        t.init(this,clazz.getName());
        this.registedParser(t);
        return t;
    }
    public List<AbstractLineageParserExecutor> getRegistedParsers() {
        return registedParsers;
    }

    public ConcurrentLinkedQueue<LineageNode> getNode() {
        return node;
    }

    public ConcurrentLinkedQueue<LineageLink> getLink() {
        return link;
    }

    public LinkedTransferQueue<LineageMetaValue> getLineageItems(){
        return lineageItems;
    }

    public LinkedTransferQueue<LineageMetaValue> getLineageColumns(){
        return lineageColumns;
    }
}
