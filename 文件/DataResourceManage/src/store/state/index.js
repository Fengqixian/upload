const state = {
    pageSizes: [5, 10, 20, 30, 50, 100],  // 分页查询每页显示个数选择器的选项设置
    pageSize: 5, // 分页查询 每页显示条目个数
    loadingFlag: false,
    menuWidth: 200, // 菜单的宽度
    // menuWidth: 64, // 菜单的宽度
    windowSize: 0, // 屏幕宽度 这个值右window.onresize控制
    access_token: '', // token
    randomStr: 0, // 控制登录验证图片的
    loadingNum: 0, // 记录loading加载次数
    // 基本的字段
    baseField: [
        {
            prop: 'nameCn', // 字段英文名 --- 字段标识
            label: '中文名称',  // 字段中文名  -- 字段解释
            value: ''   // 部分需求 需要展示值 --- 字段的值
        },
        {
            prop: 'nameEn',
            label: '英文名称',
            value: ''
        },
        {
            prop: 'status',
            label: '状态',
            type: 'select',  // 这个子弹使用什么类型属于什么类型
            value: '',
            options: [{      // 字段值得选项
                itemsName: '正常',
                itemsCode: '0'
            }, {
                itemsName: '删除',
                itemsCode: '1'
            }]
        },
        {
            prop: 'modelType',
            label: '模型类型',
            value: ''
        },
        /*{
            prop: 'isStandard',
            label: '是否根模型',
            type: 'switch',
            value: ''
        }*/
    ],
    // table 头部基本的展示字段
    baseTableHeaderData: [],
    theme: '', // 主题
    // 默认主题颜色
    defaultTheme: {
        /**
         背景颜色
         */
        // 网页外观背景颜色
        '--WEB-HEADER-BG-COLOR': '#0e183f', // 网页头部背景颜色

        '--WEB-FOOTER-BG-COLOR': 'rgba(14, 24, 63, 0.9)', // 网页底部背景颜色

        '--WEB-CONTENT-BG-COLOR': '#e5e5e5', // 展示内容区域背景颜色


        '--FOOTER-FONT-COLOR': 'rgb(204, 204, 204)', // 底部字体颜色
        '--FOOTER-SHADOW-COLOR': 'transparent', // 底部阴影颜色

        '--WEB-CONTAINER-BG-COLOR': '#f5f5f5', // 主体区域背景颜色

        // 菜单
        '--WEB-MENU-BG-COLOR': '#293152', // 网页菜单背景颜色
        '--WEB-MENU-ACTIVE-HOVER-BG-COLOR': '#39436a', // 网页菜单HOVER背景颜色
        '--WEB-MENU-ACTIVE-FONT-COLOR': 'rgba(255, 255, 255, .85)', // 网页菜单被激活的字体颜色
        '--WEB-MENU-FONT-COLOR': 'rgba(255, 255, 255, .85)', // 网页菜单字体颜色
        '--WEB-MENU-BORDER-RIGHT-COLOR': '#293152', // 菜单右侧边框线条


        // tree
        '--TREE-BG-COLOR': '#39436a', // 树背景颜色
        '--TREE-HOVER-BG-COLOR': '#313a61', // 树背景颜色
        '--TREE-ACTIVE-FONT-COLOR': '#8de567', // 树被激活的字体颜色
        '--TREE-FONT-COLOR': 'rgba(255, 255, 255, .85)', // 树字体颜色
        '--TREE-BORDER-RIGHT-COLOR': '#39436a', // 树右侧边框线条
        '--TREE-ICON-COLOR': '#8de567', // tree每个节点的标识icon

        '--TREE-INPUT-BG-COLOR': '#0f183f', // 树输入框景颜色


        // tab
        '--TAB-BG-COLOR': 'rgb(40, 55, 87)', // tab 背景颜色
        '--TAB-ITEM-BG-COLOR': '#545a75', // tab 未被激活梯形背景杨塞
        '--TAB-ACTIVE-BG-COLOR': '#ffffff', // tab 激活背景颜色
        '--TAB-FONT-COLOR': '#f5f5f5', // tab 未被激活的字体颜色
        '--TAB-ACTIVE-FONT-COLOR': 'rgba(0, 0, 0, 0.65)', // tab 被激活的字体颜色
        '--TAB-BOTTOM-SHADOW-COLOR': 'rgb(40, 55, 87)', // tab 被激活的字体颜色
        '--TAB-CLOSE-BG-COLOR': '#f5f5f5', // tab close 背景颜色
        '--TAB-CLOSE-ACTIVE-BG-COLOR': '#e0eaf8', // tab close 背景颜色
        '--TAB-CLOSE-SHADOW-COLOR': '#e0eaf8', // 盒子阴影颜色
        '--TAB-CLOSE-FONT-COLOR': '#808080', // tab 文字颜色
        '--TAB-CLOSE-ACTIVE-FONT-COLOR': '#3366cc', // tab 文字激活颜色

        /**
         表格
         */
        '--TABLE-HOVER-ROW-BG-COLOR': '#F2F6FC', // table row hove背景颜色
        '--TABLE-ACTIVE-ROW-BG-COLOR': '#E4E7ED', // 当前row被激活的背景颜色

        /**
         按钮
         */
        // 含有边框和背景颜色的按钮
        '--BTN-ADD-COLOR': '#409eff', // 新增背景颜色

        '--BTN-DEL-COLOR': '#f56c6c', // 删除背景颜色

        '--BTN-EDIT-COLOR': '#909399', // 修改背景颜色

        '--BTN-CONFIRM-COLOR': '#3366cc', // 确认背景颜色

        /**
         血缘背景图颜色
         */
        '--RELATION-BG-COLOR': '#0e183f',
        /**
         自己开发的主键的血缘关系背景图颜色
         */
        '--BLOOD-RELATION-BG-COLOR': '#0e183f',
        /**
         血缘关系详情展示区域的样式
         */
        '--RELATION-INFO-BG-COLOR': 'rgb(57, 67, 106, 0.7)',
        '--RELATION-INFO-SHADOW-COLOR': 'rgb(57, 67, 106, 0.7)',
        '--RELATION-INFO-TITLE-COLOR': '#ffffff',
        '--RELATION-INFO-LABEL-COLOR': '#ffffff',
        '---RELATION-INFO-VALUE-COLOR': '#ffffff',

        /**
         滚动条颜色
         */
        '--SCROLLBAR-THUMB-BG-COLOR': '#ccc',
        '--SCROLLBAR-THUMB-HOVER-BG-COLOR': '#3366cc',
    },
    blueTheme: {
        /**
         背景颜色
         */
        // 网页外观背景颜色
        '--WEB-HEADER-BG-COLOR': '#3366cc', // 网页头部背景颜色

        '--WEB-FOOTER-BG-COLOR': '#fafafa', // 网页底部背景颜色

        '--WEB-CONTENT-BG-COLOR': '#e5e5e5', // 展示内容区域背景颜色


        '--FOOTER-FONT-COLOR': 'rgba(0, 0, 0, 0.8)', // 底部字体颜色
        '--FOOTER-SHADOW-COLOR': 'rgba(0, 0, 0, 0.2)', // 底部阴影颜色

        '--WEB-CONTAINER-BG-COLOR': '#f3f3f3', // 主体区域背景颜色

        // 菜单
        '--WEB-MENU-BG-COLOR': '#ffffff', // 网页菜单背景颜色
        '--WEB-MENU-ACTIVE-HOVER-BG-COLOR': '#3366cc', // 网页菜单HOVER背景颜色
        '--WEB-MENU-ACTIVE-FONT-COLOR': '#f2f5fc', // 网页菜单被激活的字体颜色
        '--WEB-MENU-FONT-COLOR': '#0099ff', // 网页菜单字体颜色
        '--WEB-MENU-BORDER-RIGHT-COLOR': '#e0e0e0', // 菜单右侧边框线条


        // tree
        '--TREE-BG-COLOR': '#fcfcfc', // 树背景颜色
        '--TREE-HOVER-BG-COLOR': '#e6edf7', // 树背景颜色
        '--TREE-ACTIVE-FONT-COLOR': '#8de567', // 树被激活的字体颜色
        '--TREE-FONT-COLOR': 'rgba(0, 0, 0, 0.8)', // 树字体颜色
        '--TREE-BORDER-RIGHT-COLOR': '#e0e0e0', // 树右侧边框线条
        '--TREE-ICON-COLOR': '#8de567', // tree每个节点的标识icon

        '--TREE-INPUT-BG-COLOR': '#eee', // 树输入框景颜色


        // tab
        '--TAB-BG-COLOR': '#f5f5f5', // tab 背景颜色
        '--TAB-ITEM-BG-COLOR': '#eeeeee', // tab 未被激活梯形背景杨塞
        '--TAB-ACTIVE-BG-COLOR': '#4d8cee', // tab 激活背景颜色
        '--TAB-FONT-COLOR': 'rgba(0, 0, 0, 0.8)', // tab 未被激活的字体颜色
        '--TAB-ACTIVE-FONT-COLOR': '#dfe5f6', // tab 被激活的字体颜色
        '--TAB-BOTTOM-SHADOW-COLOR': '#ccc', // tab 被激活的字体颜色
        '--TAB-CLOSE-BG-COLOR': '#f5f5f5', // tab close 背景颜色
        '--TAB-CLOSE-ACTIVE-BG-COLOR': '#e0eaf8', // tab close 背景颜色
        '--TAB-CLOSE-SHADOW-COLOR': '#e0eaf8', // 盒子阴影颜色
        '--TAB-CLOSE-FONT-COLOR': '#808080', // tab 文字颜色
        '--TAB-CLOSE-ACTIVE-FONT-COLOR': '#3366cc', // tab 文字激活颜色

        /**
         表格
         */
        '--TABLE-HOVER-ROW-BG-COLOR': '#ccc', // table row hove背景颜色
        '--TABLE-ACTIVE-ROW-BG-COLOR': '#3366cc', // 当前row被激活的背景颜色

        /**
         按钮
         */
        // 含有边框和背景颜色的按钮
        '--BTN-ADD-COLOR': '#409eff', // 新增背景颜色

        '--BTN-DEL-COLOR': '#f56c6c', // 删除背景颜色

        '--BTN-EDIT-COLOR': '#909399', // 修改背景颜色

        '--BTN-CONFIRM-COLOR': '#67c23a', // 确认背景颜色

        /**
         血缘背景图颜色
         */
        '--RELATION-BG-COLOR': '#E5E5E5',
        /**
         自己开发的主键的血缘关系背景图颜色
         */
        '--BLOOD-RELATION-BG-COLOR': '#636c80',
        /**
         血缘关系详情展示区域的样式
         */
        '--RELATION-INFO-BG-COLOR': 'rgb(245, 245, 245 )',
        '--RELATION-INFO-SHADOW-COLOR': '#666',
        '--RELATION-INFO-TITLE-COLOR': '#666',
        '--RELATION-INFO-LABEL-COLOR': '#777',
        '---RELATION-INFO-VALUE-COLOR': '#36C',
        /**
         滚动条颜色
         */
        '--SCROLLBAR-THUMB-BG-COLOR': '#dedfe2',
        '--SCROLLBAR-THUMB-HOVER-BG-COLOR': '#acacad',
    },
    // 主题样式模板
    themeStyle: 'body{overflow:hidden}body,h1,h2,h3,h4,h5,h6,hr,p,blockquote,dl,dt,dd,ul,ol,li,pre,form,fieldset,legend,button,input,textarea,th,td{margin:0;padding:0}html{font-size:62.5%}button,input,select,textarea{font:1.2rm/1.5tahoma,arial,\\5 b8b \\4 f53}h1,h2,h3,h4,h5,h6{font-size:100%}address,cite,dfn,em,var{font-style:normal}code,kbd,pre,samp{font-family:couriernew,courier,monospace}small{font-size:12px}ul,ol{list-style:none}a{text-decoration:none}a:hover{text-decoration:underline}i{font-style:normal}*{box-sizing:border-box}body,div,dl,dt,dd,ul,ol,li,h1,h2,h3,h4,h5,h6,pre,code,form,fieldset,legend,button,p,blockquote,th,td{user-select:none}::-webkit-scrollbar{width:8px;height:8px;cursor:pointer}::-webkit-scrollbar-track{border-radius:4px}::-webkit-scrollbar-thumb{border-radius:4px;background:--SCROLLBAR-THUMB-BG-COLOR;cursor:pointer}::-webkit-scrollbar-thumb:hover{background-color:--SCROLLBAR-THUMB-HOVER-BG-COLOR}.fade-enter-active,.fade-leave-active{transition:opacity .3s}.fade-enter,.fade-leave-to{opacity:0}.el-loading-mask{z-index:200008;background-color:rgba(255,255,255,0.1);cursor:wait}.cb-tree-show-operation-wrapper{position:relative}.cb-tree-show-operation-wrapper .tree-wrapper{width:300px;height:100%}.cb-tree-show-operation-wrapper .show-operation-wrapper{position:absolute;top:0;right:0;bottom:0;left:300px}.drag-relation svg{background-color:#FFF}.drag-relation svg path.link{fill:none;stroke:#888;stroke-width:3px;cursor:default}.drag-relation svg:not(.active):not(.ctrl) path.link{cursor:pointer}.drag-relation svg path.link.selected{stroke-dasharray:10,2}.drag-relation svg path.link.dragline{pointer-events:none}.drag-relation svg path.link.hidden{stroke-width:0}.drag-relation svg circle.node{stroke-width:0;cursor:pointer;fill:#fff}.drag-relation svg text{font:14px sans-serif;pointer-events:none}.drag-relation svg text.id{text-anchor:middle;font-weight:bold}.cb-relation-wrapper{background-color:--RELATION-BG-COLOR}.cb-blood-relation-wrapper{background-color:--BLOOD-RELATION-BG-COLOR}.cb-relation-info{background-color:--RELATION-INFO-BG-COLOR;box-shadow:1px 1px 1px --RELATION-INFO-SHADOW-COLOR}.cb-relation-info .info-title{color:--RELATION-INFO-TITLE-COLOR}.cb-relation-info .label{color:--RELATION-INFO-LABEL-COLOR}.cb-relation-info .value{color:---RELATION-INFO-VALUE-COLOR}.add-bg{color:#fff}.add-bg.el-button{background-color:--BTN-ADD-COLOR;border-color:--BTN-ADD-COLOR;color:#fff}.add-bg:focus,.add-bg:hover{opacity:.7}.add-bg.is-active,.add-bg:active{opacity:1}.edit-bg{color:#fff}.edit-bg.el-button{background-color:--BTN-EDIT-COLOR;border-color:--BTN-EDIT-COLOR;color:#fff}.edit-bg:focus,.edit-bg:hover{opacity:.7}.edit-bg.is-active,.edit-bg:active{opacity:1}.del-bg{color:#fff}.del-bg.el-button{background-color:--BTN-DEL-COLOR;border-color:--BTN-DEL-COLOR;color:#fff}.del-bg:focus,.del-bg:hover{opacity:.7}.del-bg.is-active,.del-bg:active{opacity:1}.confirm-bg{color:#fff}.confirm-bg.el-button{background-color:--BTN-CONFIRM-COLOR;border-color:--BTN-CONFIRM-COLOR;color:#fff}.confirm-bg:focus,.confirm-bg:hover{opacity:.7}.confirm-bg.is-active,.confirm-bg:active{opacity:1}.add-nobg{color:#606266}.add-nobg.el-button{background-color:#fff}.add-nobg:focus,.add-nobg:hover{opacity:.7;color:#fff;border-color:--BTN-ADD-COLOR;background-color:--BTN-ADD-COLOR}.add-nobg.is-active,.add-nobg:active{opacity:1;color:#fff;border-color:--BTN-ADD-COLOR}.del-nobg{color:#606266}.del-nobg.el-button{background-color:#fff}.del-nobg:focus,.del-nobg:hover{opacity:.7;color:#fff;border-color:--BTN-DEL-COLOR;background-color:--BTN-DEL-COLOR}.del-nobg.is-active,.del-nobg:active{opacity:1;color:#fff;border-color:--BTN-DEL-COLOR}.edit-nobg{color:#606266}.edit-nobg.el-button{background-color:#fff}.edit-nobg:focus,.edit-nobg:hover{opacity:.7;color:#fff;border-color:--BTN-EDIT-COLOR;background-color:--BTN-EDIT-COLOR}.edit-nobg.is-active,.edit-nobg:active{opacity:1;color:#fff;border-color:--BTN-EDIT-COLOR}.confirm-nobg{color:#606266}.confirm-nobg.el-button{background-color:#fff}.confirm-nobg:focus,.confirm-nobg:hover{opacity:.7;color:#fff;border-color:--BTN-CONFIRM-COLOR;background-color:--BTN-CONFIRM-COLOR}.confirm-nobg.is-active,.confirm-nobg:active{opacity:1;color:#fff;border-color:--BTN-CONFIRM-COLOR}.add-text{color:--BTN-ADD-COLOR}.add-text:focus,.add-text:hover{opacity:.7;color:--BTN-ADD-COLOR}.add-text.is-active,.add-text:active{opacity:1;color:--BTN-ADD-COLOR}.del-text{color:--BTN-DEL-COLOR}.del-text:focus,.del-text:hover{opacity:.7;color:--BTN-DEL-COLOR}.del-text.is-active,.del-text:active{opacity:1;color:--BTN-DEL-COLOR}.edit-text{color:--BTN-EDIT-COLOR}.edit-text:focus,.edit-text:hover{opacity:.7;color:--BTN-EDIT-COLOR}.edit-text.is-active,.edit-text:active{opacity:1;color:--BTN-EDIT-COLOR}.confirm-text{color:--BTN-CONFIRM-COLOR}.confirm-text:focus,.confirm-text:hover{opacity:.7;color:--BTN-CONFIRM-COLOR}.confirm-text.is-active,.confirm-text:active{opacity:1;color:--BTN-CONFIRM-COLOR}.el-button{font-size:12px;padding:5px 10px;border-radius:0}.el-button--text{padding:0;margin-left:0}\n' +
        '.el-dialog__wrapper .el-dialog .el-dialog__header{padding:0 20px;height:34px;display:flex;justify-content:space-between;align-items:center;border-bottom:1px solid #f1f1f1}.el-dialog__wrapper .el-dialog .el-dialog__header .el-dialog__title{line-height:33px;font-size:14px;font-weight:700}.el-dialog__wrapper .el-dialog .el-dialog__header .el-dialog__headerbtn{position:initial}.el-dialog__wrapper .el-dialog .el-dialog__body{padding-bottom:15px}.el-select-dropdown .el-select-dropdown__item{font-size:12px;line-height:26px;height:26px}.el-input .el-input__inner,.el-select .el-input__inner{font-size:12px;border-radius:0;height:24px;line-height:24px}.el-input .el-input__prefix,.el-input .el-input__suffix,.el-select .el-input__prefix,.el-select .el-input__suffix{top:1px}.el-form .el-form-item__label{font-size:12px}.el-form .el-form-item{margin-bottom:0}.el-form .el-form-item .el-form-item__content .el-textarea{margin-bottom:6px}.el-form .el-form-item .el-form-item__content .el-input .el-input__prefix,.el-form .el-form-item .el-form-item__content .el-input .el-input__suffix,.el-form .el-form-item .el-form-item__content .el-select .el-input__prefix,.el-form .el-form-item .el-form-item__content .el-select .el-input__suffix{top:-1px}.el-textarea .el-textarea__inner{font-size:12px;border-radius:0}.el-checkbox,.el-checkbox .el-checkbox__label,.el-checkbox .el-radio__label,.el-radio,.el-radio .el-checkbox__label,.el-radio .el-radio__label{font-size:12px}.el-select{display:block}.btn-right{display:flex;justify-content:flex-end;margin-top:25px}.el-input--medium .el-input__icon,.el-input--small .el-input__icon{line-height:24px}.el-dialog .el-dialog__body{padding-top:0;font-size:12px}.el-input-group__prepend,.el-textarea__inner{border-radius:0}.el-input-group__append,.el-input-group__prepend{border-radius:0}.el-tag{height:24px;line-height:24px;padding:0 4px;font-size:12px;margin-right:5px}.el-popover{font-size:12px;padding:5px 10px}.el-tooltip__popper{max-width:200px}.el-checkbox .el-checkbox__label{padding-left:5px}.layout{overflow:hidden}.layout .menu-content{position:relative}.layout .menu-content .content{transition:all .35s;position:absolute;top:0;right:0;bottom:0;display:flex;flex-direction:column}.layout .menu-content .content .container{position:relative;background-color:--WEB-CONTAINER-BG-COLOR}.layout .menu-content .content>div{flex:1}.layout .menu-content .content .sheets-container{width:100%;height:100%;position:relative;background-color:#e5e5e5}.layout .menu-content .content .sheets-container:after{content:"";display:block;clear:both}.layout .menu-content .content .sheets-container .father{margin-bottom:50px}.layout .menu-content .content .layout-footer{background-color:--WEB-FOOTER-BG-COLOR;z-index:100;line-height:3rem;padding:0 1rem;display:flex;flex-direction:row;justify-content:space-between;align-items:center;color:--FOOTER-FONT-COLOR;font-size:1.2rem;box-shadow:0 0 6px --FOOTER-SHADOW-COLOR}.layout .menu-content .content .layout-footer img{width:7.6rem}.layout .layout-header{height:4rem;display:flex;justify-content:space-between;align-items:center;background-color:--WEB-HEADER-BG-COLOR;padding:0 1rem 0 1.8rem;overflow:hidden}.layout .layout-header .logo{font-size:2rem;font-weight:600;display:flex;align-items:center;color:#fbfdfd}.layout .layout-header .logo .company{margin-right:2rem}.layout .layout-header .user span{color:rgba(255,255,255,0.85)}.el-pagination{display:flex;align-items:center;justify-content:flex-end;font-weight:400}.el-pagination button,.el-pagination span:not([class*=suffix]){font-size:12px;height:24px;line-height:28px;border-radius:0}.el-pagination .el-pager{display:flex;align-items:center}.el-pagination .el-pager .number{height:24px;line-height:24px;border-radius:0}.el-pagination .el-pagination__jump{margin-left:10px;display:flex;align-items:center;justify-content:flex-end}.el-pagination .el-pagination__jump .el-input{width:40px}.el-pagination .el-pagination__jump .el-input .el-input__inner{font-size:12px;border-radius:0;height:24px;line-height:24px;margin-top:2px}.el-pagination .el-select .el-input{width:86px}.cb-tab .el-tabs__nav-wrap{background-color:--TAB-BG-COLOR;border-bottom:1px solid --TAB-BOTTOM-SHADOW-COLOR}.cb-tab .el-tabs__nav-wrap .el-tabs__item{border:0!important;border-radius:0!important;color:--TAB-FONT-COLOR!important;background-color:--TAB-ITEM-BG-COLOR;font-size:1.2rem;line-height:2rem;height:2rem;min-width:100px;text-align:center;position:relative}.cb-tab .el-tabs__nav-wrap .el-tabs__item:before,.cb-tab .el-tabs__nav-wrap .el-tabs__item:after{content:\'\';display:block!important;position:absolute!important;border-top:1rem solid;border-bottom:1rem solid;border-left:.6rem solid;border-right:.6rem solid;z-index:1000;bottom:0}.cb-tab .el-tabs__nav-wrap .el-tabs__item:before{left:0;border-top-color:--TAB-BG-COLOR;border-right-color:--TAB-ITEM-BG-COLOR;border-bottom-color:--TAB-ITEM-BG-COLOR;border-left-color:--TAB-BG-COLOR}\n' +
        '.cb-tab .el-tabs__nav-wrap .el-tabs__item:after{right:0;border-top-color:--TAB-BG-COLOR;border-right-color:--TAB-BG-COLOR;border-bottom-color:--TAB-ITEM-BG-COLOR;border-left-color:--TAB-ITEM-BG-COLOR}.cb-tab .el-tabs__nav-wrap .el-tabs__item .el-icon-close{color:--TAB-FONT-COLOR}.cb-tab .el-tabs__nav-wrap .el-tabs__item.is-active .el-icon-close{color:--TAB-ACTIVE-FONT-COLOR}.cb-tab .el-tabs__nav-wrap .el-tabs__item.is-active{background-color:--TAB-ACTIVE-BG-COLOR!important;color:--TAB-ACTIVE-FONT-COLOR!important}.cb-tab .el-tabs__nav-wrap .el-tabs__item.is-active:before{border-top-color:--TAB-BG-COLOR;border-right-color:--TAB-ACTIVE-BG-COLOR;border-bottom-color:--TAB-ACTIVE-BG-COLOR;border-left-color:--TAB-BG-COLOR}.cb-tab .el-tabs__nav-wrap .el-tabs__item.is-active:after{border-top-color:--TAB-BG-COLOR;border-right-color:--TAB-BG-COLOR;border-bottom-color:--TAB-ACTIVE-BG-COLOR;border-left-color:--TAB-ACTIVE-BG-COLOR}.cb-tab .tabs-top-content{background-color:#fff;border-bottom:.1rem solid #ccc}.cb-tab .cb-tab-operation{background-color:--TAB-BG-COLOR;border-bottom:1px solid --TAB-BOTTOM-SHADOW-COLOR}.cb-tab .cb-tab-operation .el-icon-arrow-down{color:--TAB-CLOSE-ACTIVE-FONT-COLOR}.cb-tab-dropdown.el-dropdown-menu .el-dropdown-menu__item,.layout-header-dropdown.el-dropdown-menu .el-dropdown-menu__item{line-height:24px;font-size:12px}.cb-tab-dropdown .el-dropdown,.layout-header-dropdown .el-dropdown{color:--TAB-CLOSE-FONT-COLOR}.cb-tab-dropdown.el-dropdown-menu,.layout-header-dropdown.el-dropdown-menu{background-color:--TAB-CLOSE-BG-COLOR;border-color:--TAB-CLOSE-BG-COLOR;color:--TAB-CLOSE-FONT-COLOR;font-size:12px;box-shadow:0 0 1px --TAB-CLOSE-SHADOW-COLOR}.cb-tab-dropdown .el-dropdown-menu__item:focus,.cb-tab-dropdown .el-dropdown-menu__item:not(.is-disabled):hover,.layout-header-dropdown .el-dropdown-menu__item:focus,.layout-header-dropdown .el-dropdown-menu__item:not(.is-disabled):hover{background-color:--TAB-CLOSE-ACTIVE-BG-COLOR;color:--TAB-CLOSE-ACTIVE-FONT-COLOR!important}.cb-tab-dropdown.el-dropdown-menu .el-dropdown-menu__item,.layout-header-dropdown.el-dropdown-menu .el-dropdown-menu__item{color:--TAB-CLOSE-FONT-COLOR!important}.cb-tab-dropdown .popper__arrow::after,.layout-header-dropdown .popper__arrow::after{border-bottom-color:--TAB-CLOSE-BG-COLOR!important}.el-table{font-size:12px}.el-table tr.hover-row td{background-color:--TABLE-HOVER-ROW-BG-COLOR}.el-table tr.current-row td{background-color:--TABLE-ACTIVE-ROW-BG-COLOR}.el-table tr th .cell,.el-table tr td .cell{line-height:14px}.el-table tr th{padding:10px 0;font-weight:700;font-size:14px;color:#595959}.el-table tr td{padding:8px 0;color:#818181}.cb-tree{background-color:--TREE-BG-COLOR;border-right:1px solid --TREE-BORDER-RIGHT-COLOR}.cb-tree .search-wrapper .el-input__inner{background-color:--TREE-INPUT-BG-COLOR;border-color:--TREE-INPUT-BG-COLOR;color:--TREE-FONT-COLOR}.cb-tree .search-wrapper .el-input-group__append{background-color:--TREE-INPUT-BG-COLOR;border-color:--TREE-INPUT-BG-COLOR;color:--TREE-FONT-COLOR}.cb-tree .el-tree-container .el-tree{color:--TREE-FONT-COLOR}.cb-tree .el-tree-container .el-tree .el-tree-node.is-current>.el-tree-node__content{background-color:--TREE-HOVER-BG-COLOR;color:--TREE-ACTIVE-FONT-COLOR}.cb-tree .el-tree-container .el-tree .el-tree-node .el-tree-node__content:hover{background-color:--TREE-HOVER-BG-COLOR}.cb-tree .el-tree-container .el-tree .el-tree-node__expand-icon{color:--TREE-FONT-COLOR}.cb-tree .el-tree-container .el-tree .el-tree-node__expand-icon.is-leaf{color:transparent;cursor:default}.cb-tree .cb-tree-icon{color:--TREE-ICON-COLOR}.layout-menu{transition:all .35s;position:absolute;top:0;bottom:0;left:0;overflow-y:auto;overflow-x:hidden;background-color:--WEB-MENU-BG-COLOR;color:--WEB-MENU-FONT-COLOR;border-right:1px solid --WEB-MENU-BORDER-RIGHT-COLOR}.layout-menu .btn{display:inline-block;text-align:left;margin-left:18px;height:30px;padding-top:10px}.layout-menu .btn>.iconfont{font-size:20px;cursor:pointer}.layout-menu .el-menu-vertical-demo:not(.el-menu--collapse){width:200px;min-height:400px}.el-submenu__title,.el-menu-item{height:46px;line-height:46px}.el-menu{background-color:--WEB-MENU-BG-COLOR;border:0}.el-menu .el-menu-item{background-color:--WEB-MENU-BG-COLOR}.el-menu .el-menu-item i,.el-menu .el-menu-item span{color:--WEB-MENU-FONT-COLOR}.el-menu .el-menu-item.is-active,.el-menu .el-menu-item>.el-submenu__title{background-color:--WEB-MENU-ACTIVE-HOVER-BG-COLOR}.el-menu .el-menu-item.is-active i,.el-menu .el-menu-item.is-active span,.el-menu .el-menu-item>.el-submenu__title i,.el-menu .el-menu-item>.el-submenu__title span{color:--WEB-MENU-ACTIVE-FONT-COLOR}.el-menu .el-menu-item:hover{background-color:--WEB-MENU-ACTIVE-HOVER-BG-COLOR}.el-menu .el-menu-item:hover i,.el-menu .el-menu-item:hover span{color:--WEB-MENU-ACTIVE-FONT-COLOR}.el-menu .el-submenu .el-submenu__title{background-color:--WEB-MENU-BG-COLOR}.el-menu .el-submenu .el-submenu__title i,.el-menu .el-submenu .el-submenu__title span{color:--WEB-MENU-FONT-COLOR}\n' +
        '.el-menu .el-submenu.is-active .el-submenu__title{background-color:--WEB-MENU-ACTIVE-HOVER-BG-COLOR}.el-menu .el-submenu.is-active .el-submenu__title i,.el-menu .el-submenu.is-active .el-submenu__title span{color:--WEB-MENU-ACTIVE-FONT-COLOR}.el-step__title{font-size:14px;line-height:28px}.el-transfer .el-transfer-panel .el-transfer-panel__header .el-checkbox .el-checkbox__label{font-size:14px}.cb-slide-side-info{background-color:rgb(57,67,106,0.7);box-shadow:1px 1px 1px rgb(57,67,106,0.7);color:#fff}',


};
export default state
