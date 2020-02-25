///*
// *    Copyright (c) 2018-2025, lengleng All rights reserved.
// *
// * Redistribution and use in source and binary forms, with or without
// * modification, are permitted provided that the following conditions are met:
// *
// * Redistributions of source code must retain the above copyright notice,
// * this list of conditions and the following disclaimer.
// * Redistributions in binary form must reproduce the above copyright
// * notice, this list of conditions and the following disclaimer in the
// * documentation and/or other materials provided with the distribution.
// * Neither the name of the pig4cloud.com developer nor the names of its
// * contributors may be used to endorse or promote products derived from
// * this software without specific prior written permission.
// * Author: lengleng (wangiegie@gmail.com)
// */
package com.clinbrain.bd.mdm.MetadataManage.controller;

import com.clinbrain.bd.mdm.MetadataManage.service.MetaDataExportService;
import com.clinbrain.bd.mdm.common.core.util.R;
import com.clinbrain.bd.mdm.common.log.annotation.SysLog;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * 导出
 *
 * @author yjt
 */
@RestController
@AllArgsConstructor
@RequestMapping("/metadataexport")
public class MetaDataExportController {

    private final MetaDataExportService exportService;

    /**
     * 导出元数据（表或数据库）
     * @param
     * @return R
     */
    @SysLog("导出表")
    @PostMapping("exportMetaData")
    public String exportMetaData(@RequestBody Map<String,Object> paramMap) {
        return exportService.exportMetaData(paramMap);
    }

    /**
     * 导出元数据（表或数据库执行到某个库或实例上）
     * @param
     * @return R
     */
    @SysLog("导出并执行脚本")
    @PostMapping("exportMetaDataToTarget")
    public R exportMetaDataToTarget(@RequestBody Map<String,Object> paramMap) {
        return exportService.exportMetaDataToTarget(paramMap);
    }
}
