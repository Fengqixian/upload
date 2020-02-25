package com.clinbrain.bd.mdm.MetadataManage.util.uuid;

import com.clinbrain.bd.mdm.MetadataManage.dto.BaseKeys;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author WANGYI
 * @className com.clinbrain.bd.mdm.MetadataManage.util.uuid.UuidGenerator
 * @createdDate 2019/7/24 15:25
 * @description TODO
 * @e-mail WANGYI@clinbrain.com
 * @group bigdata develop group (mdm)
 */
@FeignClient(name = "clinbrain-genid")
public interface UuidGenerator {
    @RequestMapping(value="/{keyName}/{keyType}",method = RequestMethod.GET)
    public String uuid(@PathVariable("keyName") String modelType,@PathVariable("keyType") String keyType);
    @PutMapping(value="/createKey")
    public String createKey(@RequestBody BaseKeys baseKeys);
}
