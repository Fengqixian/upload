package com.clinbrain.bd.mdm.MetadataManage.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.clinbrain.bd.mdm.MetadataManage.entity.DictInfo;
import com.clinbrain.bd.mdm.MetadataManage.entity.DictItems;
import com.clinbrain.bd.mdm.MetadataManage.service.DictInfoService;
import com.clinbrain.bd.mdm.MetadataManage.service.DictItemService;
import com.clinbrain.bd.mdm.common.core.util.R;
import com.clinbrain.bd.mdm.common.log.annotation.SysLog;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.sql.Wrapper;
import java.util.UUID;

/**
 * Created by 梁乐乐 on 2018/9/21
 */
@RestController
@AllArgsConstructor
@RequestMapping("/dictinfo")
public class DictInfoController {

   private final DictInfoService dictInfoService;
   private final DictItemService dictItemService;

    @GetMapping("/getDictPage")
    public R<IPage<DictInfo>> getDictPage(Page<DictInfo> page, DictInfo dictInfo){
        return new R<>(dictInfoService.getDictInfo(page, dictInfo));
    }

    @GetMapping("/getDictDetailPage")
    public R<IPage<DictItems>> getDictDetailPage(Page<DictItems> page, DictItems dictItems){
        return new R<>(dictItemService.getDictItems(page, dictItems));
    }

    @GetMapping("/getDictDetailInfo")
    public R getDictDetailInfo(int dictId){
        return new R<>(dictItemService.getDictItemsInfo(dictId));
    }

    @SysLog("新增常量")
    @PostMapping("/saveDict")
//    @PreAuthorize("@pms.hasPermission('MetadataManage_dictInfo_add')")
    public R saveDict(@RequestBody DictInfo dictInfo){
        return new R<>(dictInfoService.save(dictInfo));
    }
    @SysLog("修改常量")
    @PutMapping("/updateDict")
//    @PreAuthorize("@pms.hasPermission('MetadataManage_dictInfo_update')")
    public R updateDict(@RequestBody DictInfo dictInfo){
        return dictInfoService.updateByDict(dictInfo);
    }

    /**
     * 通过id删除一条记录
     * @param id
     * @return R
     */
    @SysLog("删除常量")
    @DeleteMapping("/deleteDict/{id}")
//    @PreAuthorize("@pms.hasPermission('MetadataManage_dictInfo_del')")
    public R removeById(@PathVariable Integer id){
        return new R<>(dictInfoService.deleteDictInfoByid(id));
    }

    @SysLog("新增常量值")
    @PostMapping("/saveItems")
//    @PreAuthorize("@pms.hasPermission('MetadataManage_dictItems_add')")
    public R saveItems(@RequestBody DictItems dictItems){
        return new R<>(dictItemService.save(dictItems));
    }
    @SysLog("修改常量值")
    @PutMapping("/updateItems")
//    @PreAuthorize("@pms.hasPermission('MetadataManage_dictItems_update')")
    public R updateItems(@RequestBody DictItems dictItems){
        return new R<>(dictItemService.updateById(dictItems));
    }

    /**
     * 通过id删除一条记录
     * @param id
     * @return R
     */
    @SysLog("删除常量值")
    @DeleteMapping("/deleteItems/{id}")
//    @PreAuthorize("@pms.hasPermission('MetadataManage_dictItems_del')")
    public R removeItemsById(@PathVariable Integer id){
        return new R<>(dictItemService.removeById(id));
    }

    @GetMapping("/selectItemsValue/{fieldName}")
    public R getSelectItemsValues(@PathVariable String fieldName){
        return new R<>(dictItemService.getSelectItemsValues(fieldName));
    }
}
