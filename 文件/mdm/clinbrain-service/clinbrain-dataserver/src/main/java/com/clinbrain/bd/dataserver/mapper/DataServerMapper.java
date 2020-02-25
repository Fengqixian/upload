package com.clinbrain.bd.dataserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clinbrain.bd.dataserver.dto.DataServer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface DataServerMapper extends BaseMapper<DataServer> {
    List<DataServer> selectAll();
    IPage<DataServer> getDataServerPage(Page page, @Param("dataServer") DataServer dataServer);
}