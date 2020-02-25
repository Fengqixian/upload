package com.clinbrain.bd.mdm.MetadataManage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.clinbrain.bd.mdm.MetadataManage.entity.DictInfo;
import com.clinbrain.bd.mdm.common.core.util.R;


public interface DictInfoService extends IService<DictInfo> {
    /**
     * 分页查询常量信息
     * @param dictInfo 常量对象
     * @return 数据信息
     */
    IPage<DictInfo> getDictInfo(Page<DictInfo> page, DictInfo dictInfo);

    /**
     * 根据id删除常量和常量值
     * @param id 常量标识
     * @return
     */
    boolean deleteDictInfoByid(int id);

    /**
     * 修改常量
     * @param dictInfo
     * @return 返回对象
     */
    R updateByDict(DictInfo dictInfo);
}
