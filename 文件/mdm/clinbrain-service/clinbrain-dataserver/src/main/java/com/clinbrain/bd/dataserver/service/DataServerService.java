package com.clinbrain.bd.dataserver.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.clinbrain.bd.dataserver.dto.DataServer;
import com.clinbrain.bd.dataserver.entity.ConnectionParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

public interface DataServerService extends IService<DataServer> {
    List<DataServer> getDataServer();
    DataServer edit(DataServer model);
    IPage<DataServer> getDataServerPage(Page<DataServer> page, DataServer dataServer);

}
