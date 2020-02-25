package com.clinbrain.bd.mdm.genid.cache;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.clinbrain.bd.mdm.genid.dto.Constant;
import com.clinbrain.bd.mdm.genid.entity.BaseKeys;
import com.clinbrain.bd.mdm.genid.mapper.KeyInfoMapper;
import com.clinbrain.bd.mdm.genid.service.KeyInfoService;
import com.clinbrain.bd.mdm.genid.util.ConversionUtil;
import com.clinbrain.bd.mdm.genid.util.StringDealUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 缓存对象
 */
@Slf4j
public class KeyInfo implements Callable<Boolean> {
    /**
     * keyName
     */
    private String name;
    /**
     * 数据库信息
     */
    private BaseKeys baseKeys = null;
    /**
     * 数据库操作
     */
    private KeyInfoService keyInfoService;
    /**
     * 当前缓存最大值
     */
    private long keyMax;
    /**
     * 更新最大值缓存锁
     */
    private Lock updateMaxLock = new ReentrantLock();
    private BlockingQueue<Long> seqQueue = new ArrayBlockingQueue<>(1000);
    private SeqGenerator seqGenerator = null;

    public KeyInfo(String name, KeyInfoService keyInfoService) {
        this.name = name;
        this.keyInfoService = keyInfoService;
    }

    public KeyInfo(String name, KeyInfoService keyInfoService, BaseKeys baseKeys) {
        this.name = name;
        this.baseKeys = baseKeys;
        this.keyInfoService = keyInfoService;
    }

    public String createKey() throws Exception {
        return createKey(baseKeys.getKeyPrefix(), baseKeys.getKeySuffix(), baseKeys.getKeyLength(), 1);
    }

    public String createKey(String prefix, String suffix, int length, int flag) throws Exception {
        String strKey = String.valueOf(createKeyAsLong());
        log.info("createKeyAsLong return key = {}" , strKey);
        if (Constant.KEY_DIGIT_36.equals(baseKeys.getKeyDigit()) && flag == 1) {
            strKey = ConversionUtil._10_to_36(Long.parseLong(strKey));
        }

        if (Constant.KEY_DIGIT_62.equals(baseKeys.getKeyDigit()) && flag == 1) {
            strKey = ConversionUtil._10_to_62(Long.parseLong(strKey));
        }

        log.info("类型处理后 key = {} ", strKey);
        prefix = prefix == null ? Constant.EMPTY : prefix.trim();
        suffix = suffix == null ? Constant.EMPTY : suffix.trim();
        prefix = StringDealUtil.parseValue(prefix);
        suffix = StringDealUtil.parseValue(suffix);
        int maxLen = length - prefix.length() - suffix.length();
        if (strKey.length() > maxLen) {
            strKey = strKey.substring(strKey.length() - maxLen);
        }
        log.info("长度处理后 key = {}" , strKey);
        int zeroLen = length - prefix.length() - strKey.length() - suffix.length();
        StringBuffer sb = new StringBuffer(length);
        sb.append(prefix);
        for (int i = 0; i < zeroLen; i++) {
            sb.append("0");
        }
        sb.append(strKey);
        sb.append(suffix);
        log.info("createKey返回值:{}" , sb.toString());
        return sb.toString();
    }

    public long createKeyAsLong() throws Exception {
        log.info("KeyInfoServiceImpl.createKeyAsLong begin");
        //获取序列号
        Long seq = getSeq();
        log.info("createKeyAsLong -- seq:{}", seq);
        if (seq == null) {
            throw new Exception("当前无可用的序列号！");
        }
        //判断是否需要更新DB 预分配序列号
        if (needUpdate(seq)) {
            //提交更新DB任务
            log.info("KeyInfoServiceImpl提交更新任务");
            CacheUpdateJob.getInstance().submitJob(this);
        }
        log.info("KeyInfoServiceImpl.createKeyAsLong end. seq = {}" , seq);
        return seq;
    }


    @Override
    public Boolean call() {
        log.info("KeyInfoServiceImpl.call keyMax = {}", keyMax);
        updateMaxLock.lock();

        // 乐观锁获取数据库记录,并更新
        log.info("KeyInfoServiceImpl.call 获取数据库信息");
        List<BaseKeys> baseKeys = null;
        try {
            BaseKeys baseKeysQueryName = new BaseKeys();
            baseKeysQueryName.setKeyName(name);
            baseKeys = keyInfoService.selectList(baseKeysQueryName);
        } catch (Exception e) {
            // 出现异常
            log.error("获取数据库记录异常", e);
        }

        if (CollectionUtils.isEmpty(baseKeys)) {
            updateMaxLock.unlock();
            log.info("KeyInfoServiceImpl.call baseKeys is Empty");
            return true;
        }

        log.info("KeyInfoServiceImpl.call 获取数据库信息完毕 keyName:{},baseKeys size:{}", name, baseKeys.size());

        try {

            BaseKeys baseKey = baseKeys.get(0);
            long currentMax = Long.parseLong(baseKey.getKeyValue());
            log.info("KeyInfoServiceImpl.call currentMax：{}", currentMax);
            long max;
            if (Long.MAX_VALUE - currentMax < baseKey.getKeyCache()) {
                // 超过了最大值 需要重新设置值
                max = baseKey.getKeyCache();
            } else {
                max = currentMax + baseKey.getKeyCache();
            }
            log.info("KeyInfoServiceImpl.call 准备更新数据库信息 max: {}", max);
            int updateNum = keyInfoService.updateKeyInfoValue(max, name, baseKey.getVersion());
            log.info("KeyInfoServiceImpl.call 记录更新条数 updateNum: {}", updateNum);
            if (updateNum == 1) {
                // 更新成功 设置对应的值
                updateFields(max, baseKey);
                updateMaxLock.unlock();
                log.info("KeyInfoServiceImpl.call 记录更新成功");
                return true;
            }

        } catch (Exception e) {
            // 出现异常
            log.error("乐观锁更新数据库异常", e);
        }
        updateMaxLock.unlock();
        log.info("KeyInfoServiceImpl.call 重新加入提交队列 keyName: {}", this.name);
        // 重新的加入队列
        CacheUpdateJob.getInstance().submitJob(this);

        return false;
    }

    public boolean isNotInit() {
        return this.baseKeys == null;
    }

    public Long getSeq() {
        try {
            return seqQueue.poll(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.error("seqQueue.poll",e);
        }
        return null;
    }

    private void updateFields(long max, BaseKeys baseKey) {
        // 首次初始化
        if (seqGenerator == null) {
            seqGenerator = new SeqGenerator(max - baseKey.getKeyCache(), max);
            seqGenerator.start();
        }
        keyMax = max;
        baseKeys = baseKey;
        baseKeys.setKeyValue(String.valueOf(max));
        baseKeys.setVersion(baseKey.getVersion() + 1);
        synchronized (seqGenerator) {
            try {
                seqGenerator.notifyAll();
            } catch (Exception e) {
            }
        }
    }

    // 根据步长计算应该提前多少预申请序列号
    private long getSize(Integer size) {
        if (size == 1) {
            return 0;
        }
        if (size <= 100) {
            return size / 2;
        }
        if (size <= 1000) {
            return size / 4;
        }
        if (size <= 10000) {
            return size / 8;
        }
        return size / 10;
    }

    private boolean needUpdate(Long seq) {
        return seq == seqGenerator.currentKeyMax - getSize(baseKeys.getKeyCache());
    }

    private class SeqGenerator extends Thread {
        /**
         * // 当前缓存值
         */
        private long keyCurrent;
        /**
         * // 当前缓存最大值
         */
        private long currentKeyMax;

        public SeqGenerator(long keyCurrent, long currentKeyMax) {
            this.keyCurrent = keyCurrent;
            this.currentKeyMax = currentKeyMax;
        }

        @Override
        public void run() {
            log.info("启动生成序列号线程=====:{}" , name);
            if (currentKeyMax != 0) {
                while (true) {
                    log.info("生成序列号线程 name: {} keyCurrent: {} currentKeyMax: {}" , name , keyCurrent , currentKeyMax);
                    if (keyCurrent < currentKeyMax) {
                        try {
                            keyCurrent++;
                            seqQueue.put(keyCurrent);
                        } catch (InterruptedException e) {
                        }
                    }
                    //更新缓存最大值
                    if (keyCurrent == currentKeyMax) {
                        if (currentKeyMax != keyMax) {
                            currentKeyMax = keyMax;
                            keyCurrent = keyMax - baseKeys.getKeyCache();
                            log.info("生成序列号线程 name: {} keyMax: {} currentKeyMax :{} keyCurrent: {}" , name , keyMax , currentKeyMax , keyCurrent);
                        } else {
                            synchronized (this) {
                                try {
                                    this.wait();
                                } catch (InterruptedException e) {
                                }
                            }
                        }
                    }
                }
            }
            throw new RuntimeException("序号生成线程启动错误: " + name);
        }
    }
}