package com.clinbrain.bd.mdm.MetadataManage.metadataVersion.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clinbrain.bd.mdm.MetadataManage.businessView.entity.DataElement;
import com.clinbrain.bd.mdm.MetadataManage.metadataVersion.entity.EditionInfo;
import com.clinbrain.bd.mdm.MetadataManage.metadataVersion.mapper.EditionMapper;
import com.clinbrain.bd.mdm.MetadataManage.metadataVersion.mapper.VersionControlMapper;
import com.clinbrain.bd.mdm.MetadataManage.metadataVersion.service.VersionControlService;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.Column;
import com.clinbrain.bd.mdm.common.core.util.R;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Getter
@Service
public class VersionControlServiceImpl implements VersionControlService {

    private VersionControlMapper versionControlMapper;

    private EditionMapper editionMapper;

    @Override
    public R createNewVersion(EditionInfo editionInfo){
        //生成并记录版本信息
        Integer versionId;
        if(editionInfo!=null){
            editionMapper.insert(editionInfo);
        }
        if(editionInfo.getId()==null||editionInfo.equals(0)){
            return new R<>(false,"保存版本信息失败。");
        }
        versionId = editionInfo.getId();

        if(versionId==null||versionId.equals(0)){
            return new R<>(false,"保存版本信息，未获取到版本号。");
        }

        //调用存储过程执行全部过程
        Integer resultCode = versionControlMapper.callReleaseVersion(versionId);
        if(resultCode.equals(0)){
            return new R<>(true,"版本发布成功。");
        }else{
            return new R<>(false,"版本发布失败。");
        }

    /*
        //master数据复制到edition 并记录版本号
        versionControlMapper.createColumnMasterToEdition(versionId);
        versionControlMapper.createMetadataMasterToEdition(versionId);

        //删除master数据，将host数据完整复制到master表
        versionControlMapper.clearMetadataFromMaster();
        versionControlMapper.clearColumnFromMaster();

        versionControlMapper.createColumnHostToMaster();
        versionControlMapper.createMetadataHostToMaster();
        */

    }

    @Override
    public R rollBackMaster(){
//        versionControlMapper.clearColumnFromHost();
//        versionControlMapper.clearMetadataFromHost();
//
//        versionControlMapper.rollbackColumnMasterToHost();
//        versionControlMapper.rollbackElementMasterToHost();

        //调用存储过程执行全部过程
        Integer resultCode = versionControlMapper.rollbackMasterVersion();
        if(resultCode.equals(0)){
            return new R<>(true,"版本回滚成功。");
        }else{
            return new R<>(false,"版本回滚失败。");
        }
    }

    @Override
    public R rollBackEdition(Integer versionId){
        if(versionId==null||versionId.equals(0)){
            return new R<>(false,"未获取到要回滚的版本号。");
        }
//        versionControlMapper.clearColumnFromHost();
//        versionControlMapper.clearMetadataFromMaster();
//
//        versionControlMapper.rollbackColumnEditionToHost(versionId);
//        versionControlMapper.rollbackElementEditionToHost(versionId);

        //调用存储过程执行全部过程
        Integer resultCode = versionControlMapper.rollbackEditionVersion(versionId);
        if(resultCode.equals(0)){
            return new R<>(true,"版本回滚成功。");
        }else{
            return new R<>(false,"版本回滚失败。");
        }
    }

    @Override
    public  IPage<EditionInfo> getVersionList(Page<EditionInfo> page,String queryString){
        return versionControlMapper.getVersionList(page,queryString);
    }

    @Override
    public IPage<Column> getColumnInfo(Page<Column> page,Integer versionId,Integer tableId,String queryString){
       try{
           IPage<Column> list;
           if(versionId==null||versionId.equals(0)){
               //获取master版本的表列集合
               list = versionControlMapper.getMasterColumnInfo(page,tableId,queryString);
           }else{
               //获取指定版本的表列集合
               list = versionControlMapper.getEditionColumnInfo(page,versionId,tableId,queryString);
           }
           return list;
       }catch(Exception e){
           e.printStackTrace();
           return null;
       }
    }

    @Override
    public IPage<DataElement> getElementInfo(Page<DataElement> page, Integer versionId, Integer datasetId, String queryString){
        try{
            IPage<DataElement> list;
            if(versionId==null||versionId.equals(0)){
                //获取master版本的数据集信息
                list = versionControlMapper.getMasterElementInfo(page,datasetId,queryString);
            }else{
                //获取指定版本的数据集信息
                list = versionControlMapper.getEditionElementInfo(page,versionId,datasetId,queryString);
            }
            return list;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
