package com.clinbrain.bd.mdm.MetadataManage.controller;

import com.clinbrain.bd.mdm.MetadataManage.entity.ElementItemInfo;
import com.clinbrain.bd.mdm.MetadataManage.service.ElementItemService;
import com.clinbrain.bd.mdm.common.core.util.R;
import com.clinbrain.bd.mdm.common.log.annotation.SysLog;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @Author：ligen
 * @Date: Created:14:44  2019/7/16
 * @Description: 元素项控制类
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/elementItem")
public class ElementItemController{
    private ElementItemService elementItemService;

    @SysLog("手动新增元素项")
    @PostMapping("add")
    //@PreAuthorize("@pms.hasPermission('MetadataManage_elementItem_add')")
    public R save(@RequestBody ElementItemInfo info){
        /*if(elementItemService.getListByName(info.getElementName()).size()>0){
            return new R<>(info,"元素项已存在");
        }*/
        info.setResourceId(UUID.randomUUID().toString());
        if(info.getParentResourceId()==null) info.setParentResourceId("");
        return new R<>(elementItemService.save(info));
    }

}
