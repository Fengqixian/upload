package com.clinbrain.bd.mdm.genid.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clinbrain.bd.mdm.genid.cache.KeyInfo;
import com.clinbrain.bd.mdm.genid.entity.BaseKeys;
import com.clinbrain.bd.mdm.genid.mapper.KeyInfoMapper;
import com.clinbrain.bd.mdm.genid.service.KeyInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class KeyInfoServiceImpl  extends ServiceImpl<KeyInfoMapper,BaseKeys> implements KeyInfoService {
    private static final ConcurrentHashMap<String, KeyInfo> CACHEKEYBANK = new ConcurrentHashMap<String, KeyInfo>();
    /**
     * 首次初始化锁（启动时未初始化的）
     */
    private static Lock initLock = new ReentrantLock();

    @Override
    public List<BaseKeys> selectList(BaseKeys baseKeys) {
        return baseMapper.selectList(Wrappers.query(baseKeys));
    }

    @Override
    public String createKey(String name) throws Exception {
        return getKeyInfo(name).createKey();
    }

    @Override
    public String createKey(String name, String prefix, String suffix, int length) throws Exception {
        return getKeyInfo(name).createKey(prefix, suffix, length, 0);
    }

    @Override
    public long createKeyAsLong(String name) throws Exception {
        return getKeyInfo(name).createKeyAsLong();
    }

    /**
     * 从缓存取得KeyInfo。如果没有则初始化并添加到缓存。
     *
     * @param name keyName
     * @return keyInfo
     */
    protected KeyInfo getKeyInfo(String name) throws Exception {
        log.info("KeyInfoServiceImpl.getKeyInfo name: {}", name);
        KeyInfo result = CACHEKEYBANK.get(name);
        if (result == null) {
            log.info("应用初始化无此数据，需要初始化keyInfo：name:{}", name);
            KeyInfo keyInfo = null;
            initLock.lock();
            if (CACHEKEYBANK.get(name) == null) {
                keyInfo = new KeyInfo(name, this);
                CACHEKEYBANK.put(name, keyInfo);
            } else {
                keyInfo = CACHEKEYBANK.get(name);
            }
            initLock.unlock();
            log.info("初始化keyInfo：name:{}", name);
            synchronized (keyInfo) {
                if (keyInfo.isNotInit()) {
                    if (keyInfo.call()) {
                        result = keyInfo;
                    } else {
                        throw new Exception("初始化错误");
                    }
                }
            }
        }
        log.info("result :{} ", result);
        return result;
    }

    @Override
    public void putKeyInfo(String key, KeyInfo keyInfo) {
        CACHEKEYBANK.put(key, keyInfo);
    }

    @Override
    public int updateKeyInfoValue(long keyValue, String keyName, Integer version) {
        return baseMapper.updateKeyInfoValue(keyValue,keyName,version);
    }
}
