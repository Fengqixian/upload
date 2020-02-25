package com.clinbrain.bd.mdm.MetadataManage.util.lineage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 解析流程
 * *启动一个线程开始
 * 1.检查环境是否有待解析任务（更新为同步阻塞队列，无需检查）
 * 2.申请一个对象来解析
 *
 * @param <T>
 */
public abstract class AbstractParser<T> extends Thread{
    protected final static Logger LOGGER = LoggerFactory.getLogger(AbstractParser.class);
    /**
     * 解析器的运行环境
     */
    protected volatile ParseAbstractEnviroment enviroment;
    /**
     * 解析器当前解析对象
     */
    protected T parsingObject;

    public void init(ParseAbstractEnviroment enviroment,String name){
        this.setName(name);
        this.enviroment = enviroment;
    }
    protected boolean hasTask(){
        return true;
    }
    /**
     * 从环境中申请一个对象来解析
     * @return
     */
    abstract T accept();

    /**
     * 解析
     */
    abstract void doParse(T t) throws Exception;

    /**
     * 开启线程开始解析流程
     * 每一轮循环首先申请对象 如果为空，报告一个等待
     */
    @Override
    public void run() {
        while (true){
            T t =accept();//会阻塞
            if(isEndNode(t)) break;
            LOGGER.info("accept object :",t);
            parsingObject = t;
            try {
                doParse(t);
            } catch (Exception e) {
                e.printStackTrace();
            }
            doParseEnd();
        }
        LOGGER.info("解析任务结束。");
    }

    abstract boolean isEndNode(T t);

    /**
     * 解析完成后的函数
     * @return
     */
    protected T doParseEnd(){
        return parsingObject;
    }

    /**
     * 添加顶点
     */
    protected void addNode(){

    }

    /**
     * 添加边
     */
    protected void addLink(){

    }
}
