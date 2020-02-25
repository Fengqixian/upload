package com.clinbrain.bd.mdm.MetadataManage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataReference;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataReferenceInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MetadataReferenceMapper extends BaseMapper<MetadataReference> {
    IPage<MetadataReference> getMetadataReferencePage(Page page, @Param("metadataReference") MetadataReference metadataReference);

    List<Map<String,Object>> getMetaDataReferenceList(Page page, @Param("sourceParentId") String sourceParentId, @Param("targetParentId") String targetParentId);

    List<Map<String,Object>> getMetaDataReferenceListByID(Page page, @Param("sourceParentId") String sourceParentId, @Param("targetParentId") String targetParentId);

    List<Map<String,Object>> getMetaDataReferenceListByIDList(@Param("sourceIdList") List<String> sourceIdList);

    void deleteReference(@Param("metadataReference")MetadataReference metadataReference);

    IPage<MetadataReference> getMetadataReferenceChildPage(Page page, @Param("ids") String ids);

    List<Map<String,Object>> getMetaDataConnectListByID(Page page, @Param("sourceId") String sourceId, @Param("targetId") String targetId);

    List<Map<String,Object>> getMetaDataConnectListByIDList(@Param("sourceIdList") List<String> sourceIdList);

    List<Map<String,Object>> getMetaDataConnectListFromConnect(@Param("sourceId") String sourceId, @Param("targetId") String targetId);

    List<Map<String,Object>> getMetadataInfoWithParentInfoByID(@Param("resourceId") String resourceId);
}
