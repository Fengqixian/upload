package com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.vo;

import lombok.Data;

/**
 * @author lianglele
 * @date 2019-11-11 15:11
 */
@Data
public class EtlConnection {

    private String connectionCode;
    private String url;
    private String user;
    private String password;

    private Integer engineIdw =6;

    private String dataflowReader= "mysqlreader";

    private String dataflowWriter = "mysqlreader";

    private long createdAt;

    private long updatedAt;

}
