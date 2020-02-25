package com.clinbrain.bd.mdm.MetadataManage.technologyView.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.EtlSource;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.EtlTarget;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.vo.EtlDetailVo;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.vo.EtlInfoVo;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.service.TechnologyEtlService;
import com.clinbrain.bd.mdm.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import java.util.List;

/**
 * 数据ETL
 */
@RestController
@Api(value = "etl")
@RequestMapping("/technologyEtl")
@CrossOrigin
public class TechnologyEtlController {
    @Autowired
    private TechnologyEtlService technologyEtlService;


    @ApiOperation(response = R.class, value = "调用etl")
    @GetMapping("/etl")
    public R getTableListByTreeNode(Integer sourceId, Integer targetId) {
        return new R(technologyEtlService.selectEtlInfo(1, 1));
    }

    @ApiOperation(response = R.class, value = "添加对应数据库")
    @PostMapping
    public R save(@RequestBody EtlDetailVo detailVo) {
        EtlSource source = new EtlSource();
        BeanUtil.copyProperties(detailVo, source);
        EtlTarget target = new EtlTarget();
        BeanUtil.copyProperties(detailVo, target);
        return technologyEtlService.saveEtlDatabase(source, target);
    }

    @ApiOperation(response = R.class, value = "修改对应数据库")
    @PutMapping
    public R update(@RequestBody EtlDetailVo detailVo) {
        EtlSource source = new EtlSource();
        BeanUtil.copyProperties(detailVo, source);
        source.setId(detailVo.getSourceId());
        EtlTarget target = new EtlTarget();
        target.setId(detailVo.getTargetId());
        BeanUtil.copyProperties(detailVo, target);
        return technologyEtlService.updateEtlDatabase(source, target);
    }

    @PostMapping("/show")
    public void getShow(@RequestBody EtlInfoVo infoVo) {
        System.out.printf(infoVo.toString());
    }

    @ApiOperation(response = R.class, value = "查询etl脚本")
    @GetMapping("/getEtlSql")
    public R EtlDatabaseList() {
        return new R(technologyEtlService.EtlDatabaseList());
    }

}
