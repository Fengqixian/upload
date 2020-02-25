// import Vue from 'vue'
// export default class config extends Vue {
let config: any = [];
config.port = function (portname: string) {
    let el = "";
    let data = [
        {
            name: "menu",
            url: "/menu"
        }, {
            name: "metadata",//本地元元模型查询
            url: "/metadata/"
        }, {
            name: "dictinfo",//微服务本地元元模型查询
            url: "MetadataManage/dictinfo/"
        }, {
            name: "metadata_ribbon",//微服务访问查询元元模型
            url: "/clinbrain-ribbon/"
        }, {
            name: "metadatamodel",//微服务访问查询元模型
            url: "/MetadataManage/metadatamodel"
        }, {
            name: "metadataproperties",//微服务访问查询元模型
            url: "/MetadataManage/metadataproperties"
        }, {
            name: "metadatavalue",//微服务访问查询元模型
            url: "/MetadataManage/metadatavalue"
        }, {
            name: "metadataReference",//微服务访问元数据匹配
            url: "/MetadataManage/metadataReference"
        }, {
            name: "metadataManage",//微服务访问元数据
            url: "/metadataManage/"
        }, {
            name: "versionManage",//微服务访问版本
            url: "/versionManage/"
        }, {
            name: "metadataImport",// 元模型导入
            url: "/MetadataManage/metadataImport"
        }, {
            name: "logManage",// 元模型导入
            url: "/logManage/"
        }, {
            name: "metadataexport",// 元模型导出
            url: "/metadataexport/"
        }, {
            name: "dataLineage",// 微服务元模型导出
            url: "/MetadataManage/dataLineage"
        }, {
            name: "dataServer",// 数据服务
            url: "/dataServer/"
        }, {
            name: "metadatamapping",// 数据服务
            url: "/metadatamapping/"
        }, {
            name: "metadatamapping",// 数据服务
            url: "/metadatamapping/"
        }, {
            name: "MetadataManage",// 角色管理
            url: "/MetadataManage/"
        }, {
            name: "metadataValueImportService",// Excel导入
            url: "/MetadataManage/metadatavalueimport"
        }, {
            name: "dataLineage",// 元数据血缘
            url: "/MetadataManage/dataLineage"
        }, {
            name: "technologyView",// 元数据血缘
            url: "/MetadataManage/technologyView/"
        }, {
            name: "businessView",// 元数据血缘
            url: "/MetadataManage/businessView/"
        }, {
            name: "projectView",// 我的收藏
            url: "/MetadataManage/projectView/"
        }, {
            name: "metaDataLineage",// 技术视图血缘分析
            url: "/MetadataManage/metaDataLineage/"
        }, {
            name: "index",// 首页
            url: "/MetadataManage/index/"
        }, {
            name: "technologyEtl",// 技术ETL
            url: "/MetadataManage/technologyEtl/"
        }, {
            name: "roleresourcepermission",// 技术ETL
            url: "/MetadataManage/roleresourcepermission/"
        }
    ];
    for (let i = 0; i < data.length; i++) {
        if (portname === data[i].name) {
            el = data[i].url;
        }
    }
    return el;
}
//设置默认分页
config.page = function () {
    return {
        current: 1,//当前是第currentPage页
        total: 0,//共total页
        size: 10,//每页sizes条
        pageSizes: [10, 20, 50, 100]
    };
}
config.FIXED_VARIABLE = {
    CONSTANT_TYPE_ID: 5,  // 特殊类型的常量类别
    CONSTANT_TYPE_SPECIAL_CODE: 2,  // 特殊类型的常量类别
    CONSTANT_DATAABASE_MODEL_ID: 34, // 数据库
    CONSTANT_TABLE_MODEL_ID: 38, // 表
    CONSTANT_COLUMN_MODEL_ID: 46, // 字段
    CONSTANT_FK_MODEL_ID: 54,  // 外键
    CONSTANT_TARGET_MODEL_ID: 1769 // 指标
}


export default config;
