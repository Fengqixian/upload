package com.clinbrain.bd.mdm.MetadataManage.util.cron;

import com.clinbrain.bd.mdm.MetadataManage.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author lianglele
 * @date 2019-11-07 11:18
 */
@Component
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class SchedulerTask {
    int count = 1;

    @Autowired
    private IndexService indexService;

    @Scheduled(cron = "${index.data.cron}")
    protected void process() {
        try {
            indexService.getIndexCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("this is scheduler task runing  " + (count++));
    }
}
