package com.clinbrain.bd.mdm.genid.server;

import com.clinbrain.bd.mdm.genid.cache.CacheUpdateJob;
import com.clinbrain.bd.mdm.genid.cache.KeyInfo;
import com.clinbrain.bd.mdm.genid.entity.BaseKeys;
import com.clinbrain.bd.mdm.genid.service.KeyInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * server
 */
@Component
@Slf4j
public class ServerManager implements InitializingBean {
    @Autowired
    private KeyInfoService keyInfoService;
    private static CopyOnWriteArrayList<KeyInfo> failList = new CopyOnWriteArrayList<>();

    private void runJob() {

        try {
            if (cacheInitData()) {
                //启动异步更新线程
                CacheUpdateJob.getInstance().start();
            }
        } catch (Exception e) {
            log.error("启动IDserver出错",e);
            //异常情况下 关闭异步更新线程
            CacheUpdateJob.getInstance().shutDown();
        }
    }

    private boolean cacheInitData() throws Exception {
        log.info("cacheInitData()...");
        List<BaseKeys> keyList = keyInfoService.selectList(new BaseKeys());
        //初始化缓存
        String keyName;
        for (BaseKeys basekey : keyList) {
            keyName = basekey.getKeyName();
            log.info("cacheInitData()... keyName: {}", keyName);
            KeyInfo keyInfo = new KeyInfo(keyName, keyInfoService, basekey);
            if (!keyInfo.call()) {
                failList.add(keyInfo);
                log.info("cacheInitData()... keyName: {} 初始化失败", keyName);
            }
            //重试失败的初始化缓存 
            int loopCount = 0;
            while (!failList.isEmpty() && loopCount++ <= 3) {
                Thread.sleep(loopCount * 100);
                for (KeyInfo key : failList) {
                    if (!key.isNotInit()) {
                        failList.remove(key);
                    }
                }
            }
            if (!failList.isEmpty()) {
                throw new Exception("Key : " + keyName + " 初始化失败");
            }
            keyInfoService.putKeyInfo(keyName, keyInfo);
        }
        return true;
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        runJob();
    }
}
