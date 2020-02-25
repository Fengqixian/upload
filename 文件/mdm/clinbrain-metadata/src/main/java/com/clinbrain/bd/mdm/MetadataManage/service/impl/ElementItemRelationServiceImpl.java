package com.clinbrain.bd.mdm.MetadataManage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clinbrain.bd.mdm.MetadataManage.entity.ElementItemRelation;
import com.clinbrain.bd.mdm.MetadataManage.mapper.ElementItemRelationMapper;
import com.clinbrain.bd.mdm.MetadataManage.service.ElementItemRelationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author：ligen
 * @Date: Created:11:46  2019/7/17
 * @Description: 元素项service实现类
 **/
@Slf4j
@Service
public class ElementItemRelationServiceImpl extends ServiceImpl<ElementItemRelationMapper,ElementItemRelation> implements ElementItemRelationService {
    @Override
    public List<ElementItemRelation> getListByIds(List ids){
        return null;
    }

}
