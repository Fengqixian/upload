package com.clinbrain.bd.dataserver.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clinbrain.bd.dataserver.dto.DataServer;
import com.clinbrain.bd.dataserver.entity.ConnectionParam;
import com.clinbrain.bd.dataserver.mapper.DataServerMapper;
import com.clinbrain.bd.dataserver.service.DataServerService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("dataServerService")
public class DataServerServiceImpl extends ServiceImpl<DataServerMapper, DataServer> implements DataServerService {

    public List<DataServer> getDataServer() {
        return baseMapper.selectAll();
    }
    public DataServer edit(DataServer model){
        if(model.getId() == null){// 新增
            baseMapper.insert(model);
        }else{//修改
            baseMapper.updateById(model);
        }
        return model;
    }

    public IPage<DataServer> getDataServerPage(Page<DataServer> page, DataServer dataServer){
        return baseMapper.getDataServerPage(page,dataServer);
    }

}
