package com.clinbrain.bd.mdm.genid.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.clinbrain.bd.mdm.genid.dto.Constant;
import com.clinbrain.bd.mdm.genid.entity.BaseKeys;
import com.clinbrain.bd.mdm.genid.service.CounterService;
import com.clinbrain.bd.mdm.genid.service.KeyInfoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 数据血缘关系分析
 */
@RestController
@AllArgsConstructor
@RequestMapping
public class GenIdController {
    @Autowired
    private CounterService counterService;
    @Autowired
    private KeyInfoService keyInfoService;

    @GetMapping("/{keyName}/{keyType}")
    public String doWork(@PathVariable String keyType,@PathVariable String keyName) throws Exception{
        String keyValue = keyValue = counterService.generator(keyType.trim(), keyName.trim());
        return keyValue;
    }
    @PutMapping("/createKey")
    public String createKey(@RequestBody BaseKeys baseKeys) throws Exception {
        /*判断key是否存在，存在就直接返回*/
        BaseKeys keys = new BaseKeys();
        keys.setKeyName(baseKeys.getKeyName());
        keys = keyInfoService.getOne(Wrappers.query(keys));
        if(keys == null){
            keys = baseKeys;
            keyInfoService.save(baseKeys);
        }
        return counterService.generator(Constant.KEY_TYPE_STRING,keys.getKeyName());
    }
}
