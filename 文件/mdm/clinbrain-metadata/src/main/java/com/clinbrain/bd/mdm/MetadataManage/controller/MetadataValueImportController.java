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

import com.clinbrain.bd.mdm.MetadataManage.service.MetadataValueImportService;
import com.clinbrain.bd.mdm.MetadataManage.service.MetadataValueService;
import com.clinbrain.bd.mdm.common.core.util.R;
import com.clinbrain.bd.mdm.common.log.annotation.SysLog;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 导入Excel元数据
 *
 * @author yjt
 * @date 2019-06-25 14:41:40
 */
@RestController
@AllArgsConstructor
@RequestMapping("/metadatavalueimport")
public class MetadataValueImportController {
    private final MetadataValueImportService importService;

    private MetadataValueService metadataValueService;
    /**
     * 新增记录
     *
     * @param
     * @return R
     */
    @SysLog("导入元数据")
    @PostMapping
    public R importValue(@RequestParam Map<String,Object> paramMap,@RequestBody MultipartFile file, HttpServletRequest request) throws IOException {
        try{
            InputStream fileStream = file.getInputStream();
            String fileName = file.getOriginalFilename();
            boolean result = importService.importValue(paramMap,fileStream,fileName);
            return  new R<>(result);
        }catch(Exception e){
            e.printStackTrace();
            return new R<>(0,"上传出错。");
        }
    }
}
