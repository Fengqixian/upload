package com.clinbrain.bd.mdm.MetadataManage.businessView.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clinbrain.bd.mdm.MetadataManage.businessView.entity.DataElement;
import com.clinbrain.bd.mdm.common.core.util.R;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author WANGYI
 * @className com.clinbrain.bd.mdm.MetadataManage.businessView.mapper.DataElementMapper
 * @createdDate 2019/10/22 15:33
 * @description TODO
 * @e-mail WANGYI@clinbrain.com
 * @group bigdata develop group (mdm)
 */
public interface DataElementMapper extends BaseMapper<DataElement> {
    IPage<DataElement> getProjectElementList(Page page, @Param("ids") List<Integer> ids, @Param("queryString") String queryString);

    IPage<DataElement> getProjectElementListByRoleId(Page page, @Param("ids") List<Integer> ids, @Param("queryString") String queryString, @Param("roleId") String roleId);
}
