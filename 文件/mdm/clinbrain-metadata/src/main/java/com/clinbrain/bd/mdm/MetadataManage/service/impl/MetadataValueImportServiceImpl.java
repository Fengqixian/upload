/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */
package com.clinbrain.bd.mdm.MetadataManage.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataModel;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataProperties;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataValue;
import com.clinbrain.bd.mdm.MetadataManage.mapper.MetadataValueMapper;
import com.clinbrain.bd.mdm.MetadataManage.service.*;
import com.clinbrain.bd.mdm.common.core.util.R;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 元数据Excel导入
 *
 * @author
 */
@Service("metadataValueImportService")
public class MetadataValueImportServiceImpl extends ServiceImpl<MetadataValueMapper, MetadataValue> implements MetadataValueImportService {
    @Autowired
    private MetadataValueService metadataValueService;

    @Autowired
    private MetadataPropertiesService metadataPropertiesService;

    @Autowired
    private MetadataModelService metadataModelService;

    @Autowired
    private MetadataReferenceService metadataReferenceService;

    public MetadataValueImportServiceImpl(MetadataValueService metadataValueService, MetadataPropertiesService metadataPropertiesService) {
        this.metadataValueService = metadataValueService;
        this.metadataPropertiesService = metadataPropertiesService;
    }

    public boolean importValue(Map<String, Object> paramMap, InputStream fileStream, String fileName) {
        try {
            //String fileFullPath = paramMap.get("fileFullPath").toString();
            //fileFullPath = "E:\\Import\\DC-字典1.xlsx";
            //父级数据资源标识
            String parentid = paramMap.get("parentId").toString();
            //模型id
            String modelid = paramMap.get("modelId").toString();
            String modelType = paramMap.get("modelType").toString();
            //父级数据中文名称，用于查找sheet
            String modelName = paramMap.get("modelName").toString();

            String importModel = paramMap.get("importModel").toString();
            String updateModel = paramMap.get("updateModel").toString();
            int excelTitleNum = paramMap.get("excelTitleNum") != null ? Integer.parseInt(paramMap.get("excelTitleNum").toString()) : 0;
            //标题所在行数
            int titleRowNum = excelTitleNum - 1;

            Page<MetadataProperties> page = new Page<>();
            page.setSize(-1);
            MetadataProperties properties = new MetadataProperties();
            properties.setModelResourceId(modelid);
            R<IPage<MetadataProperties>> propertiesList = getMetadataPropertiesPage(page, properties);
            if (propertiesList == null || propertiesList.getData().getRecords().size() < 1) {
                return false;
            }
            //获取输入流
            Workbook xssfWorkbook = null;
            try {
                if (fileName.toLowerCase().endsWith(".xls")) {
                    xssfWorkbook = new HSSFWorkbook(fileStream);//2003版本
                } else if (fileName.toLowerCase().endsWith(".xlsx")) {
                    xssfWorkbook = new XSSFWorkbook(fileStream);//2007版本
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            } finally {
                fileStream.close();
            }

            Sheet sheet;
            if ("".equalsIgnoreCase(modelName.trim()) || modelName == null) {
                sheet = xssfWorkbook.getSheetAt(0);
            } else {
                sheet = xssfWorkbook.getSheet(modelName);
                if (sheet == null) {
                    sheet = xssfWorkbook.getSheetAt(0);
                }
            }

            int firstRowNum = titleRowNum + 1;
            //获得当前sheet的结束行
            int lastRowNum = sheet.getLastRowNum();

            //获得标题行
            Row row = sheet.getRow(titleRowNum);
            //获得当前行的开始列
            int firstCellNum = row.getFirstCellNum();
            //获得当前行的列数
            int lastCellNum = row.getLastCellNum();//为空列获取
            String[] cells = new String[row.getLastCellNum()];
            for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                Cell cell = row.getCell(cellNum);
                if (cell != null) {
                    cells[cellNum] = String.valueOf(cell.getStringCellValue());
                } else {
                    cells[cellNum] = "";
                }
            }

            String name_Cn = "";
            String name_En = "";
            for (int rowNum = firstRowNum; rowNum <= lastRowNum; rowNum++) {
                //获得当前行
                row = sheet.getRow(rowNum);
                Map<String, Object> map = new HashMap<>();
                for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                    if (cells[cellNum] == null || "".equalsIgnoreCase(cells[cellNum].trim())) {
                        continue;
                    }
                    Cell cell = row.getCell(cellNum);
                    String cellValue = "";
                    try {
                        switch (cell.getCellType()) {
                            case STRING:
                                cellValue = cell.getStringCellValue();
                                break;
                            case NUMERIC:
                                cellValue = getDoubleString(cell.getNumericCellValue());
                                break;
                            default:
                                cellValue = String.valueOf(cell.getStringCellValue());

                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    //map即为一行元数据，key为元数据属性名称，value为元数据属性值
                    if ("英文名称".equalsIgnoreCase(cells[cellNum].trim()) || "数据元英文名称".equalsIgnoreCase(cells[cellNum].trim()) || "元数据英文名称".equalsIgnoreCase(cells[cellNum].trim())) {
                        name_En = cellValue;
                    }
                    if ("中文名称".equalsIgnoreCase(cells[cellNum].trim()) || "数据元中文名称".equalsIgnoreCase(cells[cellNum].trim()) || "元数据中文名称".equalsIgnoreCase(cells[cellNum].trim())) {
                        name_Cn = cellValue;
                    }
                    if (!map.containsKey(cells[cellNum].trim())) {
                        map.put(cells[cellNum].trim().toLowerCase(), cellValue);
                    }
                }

                Map<String, Object> metaValueMap = new HashMap<>();
                for (MetadataProperties p : propertiesList.getData().getRecords()) {
                    String propertieField = p.getMappingField();
                    String propertieName = p.getNameCn().toLowerCase().trim();
                    if (map.get(propertieName) != null && !"".equalsIgnoreCase(map.get(propertieName).toString().trim())) {
                        metaValueMap.put(propertieField, map.get(propertieName));
                    }
                }

                if(!metaValueMap.containsKey("nameEn")){
                    metaValueMap.put("nameEn",name_En);
                }
                if(!metaValueMap.containsKey("nameCn")){
                    metaValueMap.put("nameCn",name_Cn);
                }

                if (metaValueMap.size() < 1) {
                    continue;
                }

                MetadataValue metaValue;
                metaValue = BeanUtils.mapToBean(metaValueMap, MetadataValue.class);
                metaValue.setStatus(0);
                metaValue.setModelId(modelid);
                metaValue.setModelType(modelType);

                metaValue.setResourceId(UUID.randomUUID().toString());
                metaValue.setParentId(parentid);

                //更新模式，可以根据中文名或英文名更新元数据
                if (importModel.equalsIgnoreCase("updateModel")) {
                    //已经导入的表不再导入，根据名称和父id查找
                    MetadataValue checkValue = new MetadataValue();
                    if (updateModel.equalsIgnoreCase("updateByCn")) {
                        if (metaValue.getNameCn() != null && !"".equalsIgnoreCase(metaValue.getNameCn().trim())) {
                            checkValue.setNameCn(metaValue.getNameCn());
                        }
                    } else if (updateModel.equalsIgnoreCase("updateByEn")) {
                        if (metaValue.getNameEn() != null && !"".equalsIgnoreCase(metaValue.getNameEn().trim())) {
                            checkValue.setNameEn(metaValue.getNameEn());
                        }
                    }

                    if (metaValue.getParentId() != null && !"".equalsIgnoreCase(metaValue.getParentId().trim())) {
                        checkValue.setParentId(metaValue.getParentId());
                    }
                    List<MetadataValue> checkList = metadataValueService.getMetaDataListByName(checkValue);
                    if (checkList != null && checkList.size() > 0) {
                        //如果数据库中已存在该数据，将数据更新
                        metaValue.setResourceId(checkList.get(0).getResourceId());
                        metaValue.setId(checkList.get(0).getId());
                        metadataValueService.updateById(metaValue);
                    }
                } else {
                    //导入模式，不进行更新
                    metadataReferenceService.saveMetaDataValueInfo(metaValue);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取属性
     *
     * @return
     * @author yjt
     * @date 2019/7/1 14:40
     */
    public R<IPage<MetadataProperties>> getMetadataPropertiesPage(Page<MetadataProperties> page, MetadataProperties metadataProperties) {
        return new R<>(metadataPropertiesService.getMetadataPropertiesPage(page, metadataProperties));
    }

    private String getDoubleString(double number) {
        String numberStr;
        if (((int) number * 1000) == (int) (number * 1000)) {
            //如果是一个整数
            numberStr = String.valueOf((int) number);
        } else {
            DecimalFormat df = new DecimalFormat("######0.00");
            numberStr = df.format(number);
        }
        return numberStr;
    }
}
