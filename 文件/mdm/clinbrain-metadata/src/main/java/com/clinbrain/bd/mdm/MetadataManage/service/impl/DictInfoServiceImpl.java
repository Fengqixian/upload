package com.clinbrain.bd.mdm.MetadataManage.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clinbrain.bd.mdm.MetadataManage.entity.DictInfo;
import com.clinbrain.bd.mdm.MetadataManage.entity.DictItems;
import com.clinbrain.bd.mdm.MetadataManage.mapper.DictInfoMapper;
import com.clinbrain.bd.mdm.MetadataManage.mapper.DictItemsMapper;
import com.clinbrain.bd.mdm.MetadataManage.service.DictInfoService;
import com.clinbrain.bd.mdm.common.core.constant.CommonConstants;
import com.clinbrain.bd.mdm.common.core.util.DictInfoConst;
import com.clinbrain.bd.mdm.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Transactional
@Service
public class DictInfoServiceImpl extends ServiceImpl<DictInfoMapper, DictInfo> implements DictInfoService {

    @Override
    public IPage<DictInfo> getDictInfo(Page<DictInfo> page, DictInfo dictInfo) {
        try {
            return baseMapper.selectDictInfoList(page, dictInfo);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }


//    @Override
//    public boolean save(DictInfo dictInfo) {
//        try {
//            //验证特殊类型的长量
//            if (DictInfoConst.CONSTANT_DICTINFO_TYPE_SPECIAL_CODE.equalsIgnoreCase(dictInfo.getDictType())) {
//                if (StringUtils.isEmpty(dictInfo.getSqlCommand())) {
//                    return false;
//                }
//                // 如果报错直接返回false
//                baseMapper.validateCommand(dictInfo.getSqlCommand());
//            }
//            //如果是新增 id 为null
//            if (dictInfo.getId() > -1) {
//                return baseMapper.insert(dictInfo) > 0;
//            } else {
//                return baseMapper.updateById(dictInfo) > 0;
//            }
//        } catch (Exception e) {
//            return false;
//        }
//    }

    @Override
    public R updateByDict(DictInfo dictInfo) {
        R resutl = new R();
        try {
            //验证特殊类型的长量
            if (DictInfoConst.CONSTANT_DICTINFO_TYPE_SPECIAL_CODE.equalsIgnoreCase(dictInfo.getDictType())) {
                if (StringUtils.isEmpty(dictInfo.getSqlCommand())) {
                    resutl.setCode(CommonConstants.FAIL);
                    resutl.setMsg("sql类型，请填写sql语句。");
                }
                // 如果报错直接返回false
                baseMapper.validateCommand(dictInfo.getSqlCommand());
            }
            if (dictInfo != null && dictInfo.getId() > -1&&baseMapper.updateById(dictInfo) > 0) {
                resutl.setCode(CommonConstants.SUCCESS);
                resutl.setData(dictInfo);
                resutl.setMsg("修改成功。");
            } else {
                resutl.setCode(CommonConstants.FAIL);
                resutl.setMsg("修改失败，请重新再试。");
            }
            return resutl;
        } catch (Exception e) {
            resutl.setCode(CommonConstants.FAIL);
            resutl.setMsg("修改失败，系统错误。");
            return resutl;
        }
    }

    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    @Override
    public boolean deleteDictInfoByid(int id) {
        try {
            //首先将子元素全部删除
            baseMapper.deleteDictItemsByDictInfo(id);
            //再将此项删除
            baseMapper.deleteById(id);
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
