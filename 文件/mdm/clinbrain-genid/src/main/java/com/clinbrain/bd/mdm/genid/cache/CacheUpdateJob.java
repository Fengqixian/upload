package com.clinbrain.bd.mdm.genid.cache;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 更新缓存任务
 */
@Slf4j
public class CacheUpdateJob extends Thread {

	private volatile static CacheUpdateJob job;
	/**
	 * 提交队列
	 */
	private static BlockingQueue<KeyInfo>  queue = new ArrayBlockingQueue<>(10);
	private static ExecutorService pool = Executors.newFixedThreadPool(2);
	/**
	 * 停止标志
	 */
	private volatile static Boolean shutDown = Boolean.FALSE;
	
	private CacheUpdateJob() {
	}
	
	 /**
	  * 获取更新缓存任务的单例
	 * @return jobInstance
	 */
	public static CacheUpdateJob getInstance() {  
		if (job == null) {
			//防止重复初始化
			 synchronized (CacheUpdateJob.class) {  
				 if (job == null) {
					 job = new CacheUpdateJob();
				 }
			 }
		} 
		return job;
	 }

	@Override
	public void run() {
		//循环获取队列中的数据
		while(!shutDown) {
			try {
				log.info("任务执行中。。。。。");
				pool.submit(queue.take());
			} catch (Exception e) {
				log.error("更新任务异常", e);
			}
		}
	}
	
	/**
	 * 提交
	 * @param keyInfo keyInfo
	 */
	public void submitJob(KeyInfo keyInfo) {
		log.info("提交更新任务:{}", keyInfo);
		if (keyInfo != null && !shutDown) {
			try {
				queue.put(keyInfo);
			} catch (Exception e) {
				log.error("提交更新任务异常", e);
			}
		}
	}
	
	/**
	 * 停止
	 */
	public void shutDown() {
		log.info("停止任务");
		//设置停止标志
		shutDown = true;
		//终止线程池
		pool.shutdown();
	}
}
