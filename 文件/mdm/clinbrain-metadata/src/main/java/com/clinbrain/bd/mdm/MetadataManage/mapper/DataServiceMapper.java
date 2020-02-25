package com.clinbrain.bd.mdm.MetadataManage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clinbrain.bd.mdm.MetadataManage.dto.DataService;
import com.clinbrain.bd.mdm.MetadataManage.entity.LineageNode;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataValue;
import com.clinbrain.parser.common.entity.Column;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DataServiceMapper extends BaseMapper {
    DataService selectOneByCondition(@Param("projectName") String projectName,
                                     @Param("modelName") String modelName,
                                     @Param("serviceName") String serviceName,
                                     @Param("version") String version);
}