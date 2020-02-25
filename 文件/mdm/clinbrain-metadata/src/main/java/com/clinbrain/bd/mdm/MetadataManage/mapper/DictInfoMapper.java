package com.clinbrain.bd.mdm.MetadataManage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clinbrain.bd.mdm.MetadataManage.entity.DictInfo;
import org.apache.ibatis.annotations.Param;

/**
 * @Auther: 梁乐乐
 * @Date: 2019/6/11 11:42
 * @Description:
 */
public interface DictInfoMapper extends BaseMapper<DictInfo> {
    IPage<DictInfo> selectDictInfoList(Page page, @Param("dictInfo") DictInfo dictInfo);

    Integer validateCommand(@Param("dictInfo") String dictInfo);

    int deleteDictItemsByDictInfo(@Param("dictId")  int dictId);
}
