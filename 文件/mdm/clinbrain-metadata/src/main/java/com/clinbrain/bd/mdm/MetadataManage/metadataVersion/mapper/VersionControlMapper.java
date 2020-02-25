package com.clinbrain.bd.mdm.MetadataManage.metadataVersion.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clinbrain.bd.mdm.MetadataManage.businessView.entity.DataElement;
import com.clinbrain.bd.mdm.MetadataManage.metadataVersion.entity.EditionInfo;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.Column;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

public interface VersionControlMapper extends BaseMapper<Column>{
    void createColumnMasterToEdition(@Param("versionId") Integer versionId);
    void createMetadataMasterToEdition(@Param("versionId") Integer versionId);

    void clearColumnFromMaster();
    void clearMetadataFromMaster();

    void createColumnHostToMaster();
    void createMetadataHostToMaster();

    void clearColumnFromHost();
    void clearMetadataFromHost();

    void rollbackColumnMasterToHost();
    void rollbackElementMasterToHost();

    void rollbackColumnEditionToHost(@Param("versionId") Integer versionId);
    void rollbackElementEditionToHost(@Param("versionId") Integer versionId);

    Integer callReleaseVersion(@Param("versionId") Integer versionId);

    Integer rollbackMasterVersion();

    Integer rollbackEditionVersion(@Param("versionId") Integer versionId);

    IPage<EditionInfo> getVersionList(Page page, @Param("queryString") String queryString);

    IPage<Column> getMasterColumnInfo(Page page,@Param("tableId") Integer tableId,@Param("queryString") String queryString);
    IPage<Column> getEditionColumnInfo(Page page,@Param("versionId") Integer versionId,@Param("tableId") Integer tableId,@Param("queryString") String queryString);

    IPage<DataElement> getMasterElementInfo(Page page,@Param("datasetId") Integer datasetId,@Param("queryString") String queryString);
    IPage<DataElement> getEditionElementInfo(Page page, @Param("versionId") Integer versionId, @Param("datasetId") Integer datasetId,@Param("queryString") String queryString);
}
