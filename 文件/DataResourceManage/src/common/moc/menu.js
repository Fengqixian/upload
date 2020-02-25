const menuList = [
    {
        id: 1,
        title: '主页',
        name: 'home',
        routerPath: '/home',
        icon: 'iconfont icon-shouye',
        description: '系统元元模型菜单',
        subMenus: []
    },

    {
        id: 4,
        title: '资产目录',
        name: 'assets',
        routerPath: '/assets',
        icon: 'iconfont icon-shujuzichanmulu',
        description: '资产目录',
        subMenus: [
            {
                id: 41,
                title: '技术视图',
                name: 'technologyView',
                routerPath: '/technologyView',
                icon: 'iconfont icon-jishushitu',
                description: '技术视图',
                subMenus: []
            },
            {
                id: 42,
                title: '业务视图',
                name: 'businessView',
                routerPath: '/businessView',
                icon: 'iconfont icon-yewushiutu',
                description: '业务视图',
                subMenus: []
            },
            {
                id: 43,
                title: '项目视图',
                name: 'myCollection',
                routerPath: '/myCollection',
                icon: 'iconfont icon-xiangmu1',
                description: '项目视图',
                subMenus: []
            },
            /*{
                id: 44,
                title: '数据元关系',
                name: 'businessViewRelation',
                routerPath: '/businessViewRelation',
                icon: 'iconfont icon-yuanshujuxueyuanguanxi',
                description: '数据元关系',
                subMenus: []
            }*/
        ]
    },
    {
        id: 1000,
        title: '策略',
        name: 'elPresentation',
        routerPath: '/elPresentation',
        icon: 'iconfont icon-jiayajiami',
        description: '基础元数据',
        subMenus: [
            {
                id: 1010,
                title: '加密脱敏策略',
                name: 'desensitization',
                routerPath: '/desensitization',
                icon: 'iconfont icon-jiayajiami',
                description: '加密脱敏策略',
                subMenus: [{
                    id: 1011,
                    title: '编辑脱敏策略',
                    name: 'editDesensitization',
                    routerPath: '/editDesensitization',
                    icon: 'iconfont icon-jiayajiami',
                    description: '编辑脱敏策略',
                    NMenu: true, // 这是子页面，用于当前页面编辑 新增 功能 不在菜单内
                    subMenus: []
                }]
            },
            {
                id: 1020,
                title: '组合策略',
                name: 'label',
                routerPath: '/label',
                icon: 'iconfont icon-biaoqian',
                description: '组合策略',
                subMenus: [{
                    id: 1021,
                    title: '编辑脱敏策略',
                    name: 'editLabel',
                    routerPath: '/editLabel',
                    icon: 'iconfont icon-biaoqian',
                    description: '编辑脱敏策略',
                    NMenu: true, // 这是子页面，用于当前页面编辑 新增 功能 不在菜单内
                    subMenus: []
                }]
            },
            {
                id: 1030,
                title: '规则字典维护',
                name: 'rule',
                routerPath: '/rule',
                icon: 'iconfont icon-guize',
                description: '规则字典维护',
                subMenus: [/*{
            id: 1031,
            title: '编辑规则字典维护',
            name: 'editRule',
            routerPath: '/editRule',
            icon: 'iconfont icon-guize',
            description: '编辑规则字典维护',
            NMenu: true, // 这是子页面，用于当前页面编辑 新增 功能 不在菜单内
            subMenus: []
        }*/]
            }
        ]
    },
    {
        id: 11,
        title: '权限管理',
        name: 'powerManage',
        routerPath: '/powerManage',
        icon: 'iconfont icon-quanxianguanli',
        description: '权限管理',
        subMenus: [{
            id: 110,
            title: '用户管理',
            name: 'userManage',
            routerPath: '/userManage',
            icon: 'iconfont icon-yonghu',
            description: '用户管理的菜单',
            subMenus: []
        }, {
            id: 111,
            title: '角色管理',
            name: 'iconfont roleManage',
            routerPath: '/roleManage',
            icon: 'iconfont icon-juesegaunli',
            description: '角色管理的菜单',
            subMenus: []
        }, {
            id: 112,
            title: '菜单管理',
            name: 'menuManage',
            routerPath: '/menuManage',
            icon: 'iconfont icon-bendicaidanguanli',
            description: '菜单管理',
            subMenus: []
        },
            // {
            //     id: 113,
            //     title: '系统资源管理',
            //     name: 'SysResourceManage',
            //     routerPath: '/SysResourceManage',
            //     icon: 'iconfont icon-userMange',
            //     description: '系统资源管理',
            //     subMenus: []
            // }
        ]
    },
    {
        id: 2000,
        title: '系统管理',
        name: 'set',
        routerPath: '/set',
        icon: 'iconfont icon-xitongshezhi',
        description: '系统管理',
        subMenus: [
            {
                id: 14,
                title: '数据服务',
                name: 'dataService',
                routerPath: '/dataService',
                icon: 'iconfont icon-shujufuwu',
                description: '数据服务',
                subMenus: []
            },
            {
                id: 10,
                title: '日志管理',
                name: 'logManage',
                routerPath: '/logManage',
                icon: 'iconfont icon-rizhiguanli',
                description: '日志查看',
                subMenus: []
            },
            {
                id: 3,
                title: '常量管理',
                name: 'constant',
                routerPath: '/constant',
                icon: 'iconfont icon-changliangguanli',
                description: '常量管理',
                subMenus: []
            },
        ]
    }

    /*{
        id: 13,
        title: '关系图',
        name: 'relation',
        routerPath: '/relation',
        icon: 'iconfont icon-relation',
        description: '关系图',
        subMenus: []
    },*/
    /*{
        id: 19,
        title: 'tree树',
        name: 'tree',
        routerPath: '/tree',
        icon: 'iconfont icon-examine',
        description: 'tree树',
        subMenus: []
    },*/

    /*{
      id: 15,
      title: '计算器',
      name: 'calculator',
      routerPath: '/calculator',
      icon: 'iconfont icon-shujufuwu',
      description: '数据服务',
      subMenus: []
    },*/
    /*{
        id: 16,
        title: '主数据映射',
        name: 'calculator1',
        routerPath: '/calculator1',
        icon: 'iconfont icon-mapping',
        description: '主数据映射',
        subMenus: [{
            id: 17,
            title: '主数据映射',
            name: 'mappingMethod',
            routerPath: '/mappingMethod',
            icon: 'iconfont icon-mapping',
            description: '主数据映射',
        }, {
            id: 18,
            title: '映射审核',
            name: 'mappingExamine',
            routerPath: '/mappingExamine',
            icon: 'iconfont icon-examine',
            description: '映射审核',
        }]
    },*/

    /*{
        id: 23,
        title: '基础元数据',
        name: 'elPresentation',
        routerPath: '/elPresentation',
        icon: 'iconfont icon-jichuyuanshuju',
        description: '基础元数据',
        subMenus: [
            {
                id: 231,
                title: '基础元数据',
                name: 'elRelation',
                routerPath: '/elRelation',
                icon: 'iconfont icon-xueyuanguanxi',
                description: '基础元数据',
                subMenus: []
            },
            {
                id: 232,
                title: '衍生元数据',
                name: 'metadataMerge',
                routerPath: '/metadataMerge',
                icon: 'iconfont icon-metadata-mapping',
                description: '衍生元数据',
                subMenus: []
            },
            // {
            //     id: 233,
            //     title: '元素项审批',
            //     name: 'elPresentation',
            //     routerPath: '/elPresentation',
            //     icon: 'el-icon-eleme',
            //     description: '元素审批',
            //     subMenus: []
            // }
        ]
    },*/


    /*{
        id: 5,
        title: '元数据',
        name: 'metadatatodata1',
        routerPath: '/metadatatodata1',
        icon: 'iconfont icon-yuanshujuguanli',
        description: '元数据',
        subMenus: [
            {
                id: 5,
                title: '元数据管理',
                name: 'metadatatodata',
                routerPath: '/metadatatodata',
                icon: 'iconfont icon-yuanshujuguanli',
                description: '用于系统元数据管理的菜单',
                subMenus: []
            },
            {
                id: 8,
                title: '元数据导入',
                name: 'metaimport',
                routerPath: '/metaimport',
                icon: 'iconfont icon-daoru',
                description: '元模型导入功能',
                subMenus: []
            },
            {
                id: 20,
                title: '元数据展示',
                name: 'metadataPresentation',
                routerPath: '/metadataPresentation',
                icon: 'iconfont icon-showMetadata',
                description: '元数据展示',
                subMenus: []
            },
            /!*{
                id: 21,
                title: '元数据审批',
                name: 'examinationApprovalMetadata',
                routerPath: '/examinationApprovalMetadata',
                icon: 'iconfont icon-examine',
                description: '元数据审批',
                subMenus: []
            },*!/
            // {
            //     id: 22,
            //     title: '元数据匹配',
            //     name: 'metadataMapping',
            //     routerPath: '/metadataMapping',
            //     icon: 'iconfont icon-metadata-mapping',
            //     description: '元数据匹配',
            //     subMenus: []
            // },
            {
                id: 51,
                title: '元数据血缘关系',
                name: 'metadataRelation',
                routerPath: '/metadataRelation',
                icon: 'iconfont icon-xueyuanguanxi',
                description: '元数据血缘关系',
                subMenus: []
            },
        ]
    },*/

    /*{
      id: 52,
      title: '血缘关系',
      name: 'metadatatodataV2',
      routerPath: '/metadatatodataV2',
      icon: 'iconfont icon-xueyuanguanxi',
      description: '用于系统元数据管理的菜单',
      subMenus: []
    },*/
    /*{
        id: 6,
        title: '血缘关系',
        name: 'bloodRelation',
        routerPath: '/bloodRelation',
        icon: 'iconfont icon-xueyuanguanxi',
        description: '血缘关系',
        subMenus: []
    },*/
    /*{
      id: 7,
      title: '版本管理',
      name: 'versionManage',
      routerPath: '/versionManage',
      icon: 'iconfont icon-banbenguanli',
      description: '用于系统元数据版本管理的菜单',
      subMenus: []
    },*/
    /*{
      id: 9,
      title: '元数据导出',
      name: 'metaexport',
      routerPath: '/metaexport',
      icon: 'iconfont icon-daochu',
      description: '元模型导入功能',
      subMenus: []
    },*/
    /*{
        id: 2,
        title: '元模型',
        name: 'metamodel',
        routerPath: '/metamodel',
        icon: 'iconfont icon-yuanmoxing1',
        description: '元模型',
        subMenus: []
    },*/
    // {
    //   id: 41,
    //   title: '元数据血缘关系',
    //   name: 'bloodRelationship',
    //   routerPath: '/bloodRelationship',
    //   icon: 'iconfont icon-yuanshujuxueyuanguanxi',
    //   description: '元数据血缘关系',
    //   subMenus: []
    // },,
    // {
    //   id: 1,
    //   title: '元元模型',
    //   name: 'metatometa',
    //   routerPath: '/metatometa',
    //   icon: 'iconfont icon-yuanyuanmoxing',
    //   description: '系统元元模型菜单',
    //   subMenus: []
    // },

];


menuList.forEach(menu => {
    if (menu.subMenus && menu.subMenus.length) {
        const hasChild = menu.subMenus.filter(item => !item.NMenu);
        menu['hasChild'] = hasChild.length ? true : false;
        menu.subMenus.forEach(sub => {
            const hasChild = sub.subMenus.filter(item => !item.NMenu);
            sub['hasChild'] = hasChild.length ? true : false;
        })
    }
});
sessionStorage.setItem('menuList', JSON.stringify(menuList));
