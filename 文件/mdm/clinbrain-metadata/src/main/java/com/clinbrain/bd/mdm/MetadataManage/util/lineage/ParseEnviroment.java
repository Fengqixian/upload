package com.clinbrain.bd.mdm.MetadataManage.util.lineage;

import com.clinbrain.bd.mdm.MetadataManage.entity.*;
import com.clinbrain.bd.mdm.MetadataManage.service.impl.DataLineageServiceImpl;
import com.clinbrain.parser.common.entity.Column;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ParseEnviroment {
    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractParser.class);
    /*环境监控用 总解析器数量  等待任务的解析器数量*/
    protected AtomicInteger parserCount = new AtomicInteger(0);
    protected AtomicInteger parserWaitingCount = new AtomicInteger(0);

    protected DataLineageServiceImpl dataLineageService;
    /*存放已经解析过的对象的UUID 防止造成循环解析*/
    protected List<String> hasParsedObjects = Collections.synchronizedList(new ArrayList<String>());

    protected ConcurrentLinkedQueue<String> parentIds = new ConcurrentLinkedQueue<String>();
    //存放任务的队列
    protected LinkedTransferQueue<MetadataValue> columns = new LinkedTransferQueue<MetadataValue>(); //列
    protected LinkedTransferQueue<MetadataValue> etls = new LinkedTransferQueue<MetadataValue>(); //etl
    protected LinkedTransferQueue<MetadataValue> items = new LinkedTransferQueue<MetadataValue>(); //元素

    //存放解析过的对象的UUID
    protected ConcurrentLinkedQueue<String> parsedObjects = new ConcurrentLinkedQueue<String>();
    //存放node link
    private ConcurrentLinkedQueue<LineageNode> node = new ConcurrentLinkedQueue<LineageNode>();
    private ConcurrentLinkedQueue<LineageLink> link = new ConcurrentLinkedQueue<LineageLink>();
    //注册的解析器
    private List<AbstractParserExecutor> registedParsers = new ArrayList<AbstractParserExecutor>();

    public ParseEnviroment(DataLineageServiceImpl dataLineageService){
        this.dataLineageService = dataLineageService;
    }

    protected void registedParser(AbstractParserExecutor parser){
        registedParsers.add(parser);
    }
    //启动运行
    public void enviromentStart() throws InterruptedException {
        parserCount.set(registedParsers.size());
        //开启线程做任务
        for(AbstractParserExecutor parser:registedParsers) {
            parser.start();
        }
        //只要等待数小于总数就是还有任务没有解析
        while (true){
            if(parserWaitingCount.get()==parserCount.get()){
                break;
            }
            LOGGER.info("columns watting count :"+columns.getWaitingConsumerCount()+"   current count:"+columns.size());
            LOGGER.info("etls watting count :"+etls.getWaitingConsumerCount()+"   current count:"+etls.size());
            LOGGER.info("items watting count :"+items.getWaitingConsumerCount()+"   current count:"+items.size());
            Thread.sleep(1000);
        }
        //所有任务完成
        LOGGER.info("columns watting count :"+columns.getWaitingConsumerCount());
        LOGGER.info("etls watting count :"+etls.getWaitingConsumerCount());
        LOGGER.info("items watting count :"+items.getWaitingConsumerCount());
        //终止命令发出
        columns.transfer(ColumnParserExecutor.SHUTDOWN_COMMAND);
        etls.transfer(EtlParserExecutor.SHUTDOWN_COMMAND);
        items.transfer(ItemParserExecutor.SHUTDOWN_COMMAND);
    }
    public <T extends AbstractParserExecutor> AbstractParserExecutor create(Class<T> clazz) throws InstantiationException, IllegalAccessException{
        T t = clazz.newInstance();
		t.init(this,clazz.getName());
		this.registedParser(t);
    	return t;
    }
    public List<AbstractParserExecutor> getRegistedParsers() {
        return registedParsers;
    }

    public LinkedTransferQueue<MetadataValue> getColumns() {
        return columns;
    }

    public LinkedTransferQueue<MetadataValue> getEtls() {
        return etls;
    }

    public LinkedTransferQueue<MetadataValue> getItems() {
        return items;
    }

    public ConcurrentLinkedQueue<LineageNode> getNode() {
        return node;
    }

    public ConcurrentLinkedQueue<LineageLink> getLink() {
        return link;
    }
}
