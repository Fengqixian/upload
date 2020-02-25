package com.clinbrain.bd.mdm.MetadataManage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clinbrain.bd.mdm.MetadataManage.entity.ElementItemInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author：ligen
 * @Date: Created:11:46  2019/7/16
 * @Description:
 **/
public interface ElementItemMapper extends BaseMapper<ElementItemInfo> {
    IPage<ElementItemInfo> selectPageData(Page page, @Param("elementItemInfo") ElementItemInfo info,@Param("roleIds") List roleIds);
}
