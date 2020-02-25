package com.clinbrain.bd.dataserver.service;

import com.clinbrain.bd.dataserver.entity.ConnectionParam;
import com.clinbrain.bd.mdm.common.core.util.R;
import org.apache.ibatis.annotations.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "clinbrain-MetadataManage")
@RequestMapping("/metadatavalue")
public interface ConnectionService {
    @RequestMapping(value="/getConnection",method=RequestMethod.GET)
    List<Map> getConnection(@RequestParam("resourceId") String resourceId);
}
