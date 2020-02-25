<template>
    <div class="metadata-to-data cb-tree-show-operation-wrapper">
        <div class="tree-wrapper">
            <cb-tree lazy
                     searchFromHttp
                     @search="searchTree"
                     v-show="searchVal === ''"
                     @load-node="loadNode"
                     ref="tree1">
                <template slot-scope="{data,node}">
                    <div class="tree-name" @click="treeNodeClick(data,node)">
                        <span>{{data.nameCn || data.nameEn || 'unknown'}}</span>
                    </div>
                </template>
            </cb-tree>
            <cb-tree @search="searchTree"
                     searchFromHttp
                     v-show="searchVal !== ''"
                     :data="treeData"
                     ref="tree2">
                <template slot-scope="{data,node}">
                    <div class="tree-name" @click="treeNodeClick(data,node)">
                        <span>{{data.nameCn || data.nameEn || 'unknown'}}</span>
                    </div>
                </template>
            </cb-tree>
        </div>
        <div class="tab-wrapper show-operation-wrapper">
            <cb-tab :tabData="tabsData"
                    v-model.trim="tabsActiveName">
                <template :slot="tab.name"
                          v-for="tab in tabsData">
                    <cb-operation-container>
                        <template slot="toOperation">
                            <el-button icon="iconfont icon-daoru" class="confirm-text" type="text"
                                       @click="importMateData(tab.tabNode)">导 入
                            </el-button>

                        </template>
                        <template slot="cbContainer">
                            <div class="tab-container-wrapper">
                                <div class="tab-info"
                                     :style="{bottom:tab.bottomTabsData && tab.bottomTabsData.length?'70%':'0'}">
                                    <el-row :gutter="10">
                                        <el-col :span="8"
                                                v-for="info in tab.tabInfo"
                                                :key="info.value + info.label"
                                                class="item-col">
                                            <div class="item-wrapper">
                                                <span class="label">{{info.label}}：</span>
                                                <el-tooltip class="item" effect="light" :content="info.value + ''"
                                                            placement="top-start">
                                                    <span class="value">
                                                          {{info.value}}
                                                    </span>
                                                </el-tooltip>
                                            </div>
                                        </el-col>
                                    </el-row>
                                </div>
                                <div class="children-data">
                                    <cb-tab :closable="false"
                                            v-model.trim="tab.bottomActiveName"
                                            :tabData="tab.bottomTabsData"
                                            v-if="tab.bottomTabsData && tab.bottomTabsData.length"
                                            :closes="false">
                                        <template :slot="bottomTab.name" v-for="bottomTab in tab.bottomTabsData">
                                            <cb-table :headerData="bottomTab.headerData"
                                                      :tableData="bottomTab.tableData"
                                                      :paginationData="bottomTab.paginationData"
                                                      @size-change="handleSizeChange"
                                                      @current-change="handleCurrentChange">
                                            </cb-table>
                                        </template>
                                    </cb-tab>
                                </div>
                            </div>
                        </template>
                    </cb-operation-container>
                </template>
            </cb-tab>
        </div>
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component} from 'vue-property-decorator'
    import {Action, State, Mutation} from 'vuex-class'
    import config from "../../config";

    @Component({})
    export default class Index extends Vue {
        name: string = 'Index';
        @State baseField;   // 基本的字段
        @State baseTableHeaderData;   // table 头部基本的展示字段
        @Action getMetadataProperties;  // 查寻字段的属性
        @Mutation letFieldsAssignments; // 字段赋值  这个用于tab头部详情展示  将值和字段名组装在一起


        /**
         * tree 树区域
         */
        treeData: Array<object> = []; // 用于存储查询的数据
        searchVal: string = ''; // 搜索的值

        /**
         * tab成员变量区域
         */
            // 存储每个tab的所有数据
        public tabsData: Array<object> = [];
        // tab被激活
        public tabsActiveName: string = '';


        /**
         * 创建修改元数据
         */
        public createEditMetadataVisible: boolean = false;  // 创建修改
        public mateModelData: object = {}; // 被创建修改元数据上级的元模型数据
        public mateModelNode: object = {}; // 被创建修改元数据上级的元模型node节点

        public excelImportMetadataVisible: boolean = false;  // excel导入
        public updateModelIsVisibal: boolean = false;
        public importModel: string = 'addModel';
        public updateModel: string = 'updateByCn';
        public fileList: Array<object> = [];
        public excelTitleNum: string = "1";

        public get uploadUrl() {
            return "/clinbrain/api" + config.port('metadataValueImportService');
        }

        public formData: object = {
            importModel: '',
            updateModel: '',
            excelTitleNum: '',
            parentId: '',
            modelId: '',
            modelName: '',
            modelType: ''
        };


        /**
         * 执行导入
         * @param node 此tab对用tree的node节点
         */
        public importMateData(node): void {
            // 默认数据库导入
            let params = {
                tableName: '',
                modelId: node.data.modelId,
                resourceId: node.data.resourceId,
                databaseModelId: node.parent.data.modelId,
                databaseResourceId: node.parent.data.resourceId,
                metaDataType: 'database',
                metaDataId: '',
                parentMetaDataId: '',
                size: -1
            };
            // 表导入
            if (node.parent.data.modelType === 'table') {
                params = {
                    tableName: node.data.nameEn,
                    modelId: node.parent.parent.data.modelId,
                    resourceId: node.parent.parent.data.resourceId,
                    databaseModelId: node.parent.parent.parent.data.modelId,
                    databaseResourceId: node.parent.parent.parent.data.resourceId,
                    metaDataType: 'table',
                    metaDataId: '',
                    parentMetaDataId: '',
                    size: -1
                };
            }
            let url = config.port('metadataImport') + '/importMetaData';
            this.$http.post(url, params).then(response => {
                if (response.data.data === false) {
                    this.$message.error('导入失败')
                } else {
                    this.$message.success('导入成功')
                }
            })

        }

        /**
         * 第二版创建修改元数据区域
         */
        public addEditMetadataVisible: boolean = false;
        public addEditMetadataLabels: Array<object> = [];
        public addEditMetaDataValues: object = {};
        public addEditMetaDataOperation: string = 'add'; // 创建元数据为add 修改为edit
        public addEditMetaDataNode: any = ''; // 当前修改或者创建节点的node

        /**
         * 创建修改元数据
         */
        public submitAddEditMetadata(): void {
            const data = this.addEditMetaDataOperation === 'add' ?
                Object.assign(this.addEditMetaDataValues, {
                    parentId: this.addEditMetaDataNode['data'].parentResourceId,
                    modelId: this.addEditMetaDataNode['data'].modelId
                }) : this.addEditMetaDataValues;
            this.$http({
                url: config.port('metadatavalue'),
                method: this.addEditMetaDataOperation === 'add' ? 'post' : 'put',
                data
            }).then(response => {
                this.$message.success(this.addEditMetaDataOperation === 'add' ? '创建成功！' : '修改成功！');
                this.addEditMetadataVisible = false;
                if (this.addEditMetaDataOperation === 'edit') {
                    this.getTreeDataToRefresh(this.addEditMetaDataNode['parent'].data.uuid).then(res => {
                        this.$refs.tree['refreshChildNode'](this.addEditMetaDataNode['parent'], res);
                    })
                } else {
                    this.getTreeDataToRefresh(this.addEditMetaDataNode['data'].uuid).then(res => {
                        this.$refs.tree['refreshChildNode'](this.addEditMetaDataNode, res);
                    })
                }
            })
        };

        /**
         * 获取某个节点的子节点
         * @param parentUuid 父节点的uuid
         */
        public async getTreeDataToRefresh(parentUuid): Promise<Array<object>> {
            // 获取被创建成功的元数据
            let url = config.port('metadatavalue') + '/tree/lazyTree';
            let params: object = {parentUuid};
            this.createEditMetadataVisible = false;
            let ret: Array<object> = [];
            await this.$http.get(url, {params}).then(response => {
                ret = response.data.data;
            });
            return ret;
        }

        /**
         * 编辑元数据
         * @param data node节点数据
         * @param node node节点
         */
        public deleteMetadata(data, node): void {
            this.$confirm(`删除时将删除【${data.nameCn || data.nameEn || 'unknown'}】和其所有子数据，确定删除吗？`, '提示', {type: 'warning'}).then(() => {
                let URL = config.port('metadatavalue') + '/' + data.id;
                this.$http.delete(URL).then((response) => {
                    this.$refs.tree['remove'](node, data)
                })
            })
        }

        /**
         * Excel导入元数据
         */
        public importMetadata(data, node): void {
            this.mateModelData = data;
            this.mateModelNode = node;
            this.excelImportMetadataVisible = true;

        }

        /**
         * 移除已选文件
         */
        public handleExceed(files, fileList) {
            this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
        }

        public beforeRemove(file, fileList) {
            return this.$confirm(`确定移除 ${file.name}？`);
        }

        /**
         * 切换导入模式
         */
        public changeModel(model) {
            this.updateModelIsVisibal = (model === 'updateModel') ? true : false;
            this.importModel = model;
        }

        /**
         * 切换更新模式
         */
        public changeUpdateModel(model) {
            this.updateModel = model;
        }

        public beforeUpload(file) {
        }

        /**
         * 导入Excel提交
         */
        public importSubmit() {
            this.formData["parentId"] = this.mateModelData['parentResourceId'];
            this.formData["modelId"] = this.mateModelData['modelId'];
            this.formData["modelName"] = this.mateModelData['nameCn'];
            this.formData["modelType"] = this.mateModelData['modelType'];
            this.formData["importModel"] = this.importModel;
            this.formData["updateModel"] = this.updateModel;
            this.formData["excelTitleNum"] = this.excelTitleNum;

            if (this.excelTitleNum === '' || !this.isPositiveInteger(this.excelTitleNum)) {
                this.$message.warning('标题行号必须是正整数!');
                return;
            }
            this.$refs.fileUpload['submit']();
        }

        public onSuccess(response) {
            this.$message.success('上传成功!');
            this.excelImportMetadataVisible = false;
            return
        }

        public onError(response) {
            this.$message.error('上传失败!');
            return
        }

        public isPositiveInteger(s) {//是否为正整数
            var re = /^[0-9]+$/;
            return re.test(s)
        }

        /**
         * 点击树节点
         * @param data node节点数据
         * @param node node节点
         */
        public async treeNodeClick(data, node): Promise<void> {
            if ((data.modelType === 'database' && data.type === 'INS') || (data.modelType === 'table' && data.type === undefined)) {
                // tabs个数不能超过20个
                if (this.tabsData.length > 20) {
                    this.$message.warning('最多能展示20个tabs！');
                    return
                }
                // tabs 不能有重复 根据uuid判断
                const flag = this.tabsData.filter(item => item['uuid'] === data['uuid']);
                if (flag.length >= 1) {
                    this.tabsActiveName = data.uuid;
                    return;
                }

                // 点击的节点不是实体库、表提示“只支持数据库和表导入”
                if (!(Boolean(node.parent)
                    && Boolean(node.parent.data)
                    && (node.parent.data.modelType === 'database'
                        || node.parent.data.modelType === 'table'))) {
                    this.$message.warning('只支持数据库和表导入');
                    return;
                }

                // label tab展示的名称 name 是tab的标识
                data['label'] = data.nameCn || data.nameEn || 'unknown';
                data['name'] = data.uuid;

                // 获取每个tab的属性详情
                if (data['modelId']) {
                    const attrs: Array<object> = await this.getMetadataProperties(data['modelId']);
                    const values: object = await this.getInsDbTableValue(data);
                    // 声明每个tab存储的tabInfo
                    const tabInfo: Array<object> = [...JSON.parse(JSON.stringify(this.baseField)), ...attrs];
                    this.letFieldsAssignments({fields: tabInfo, values});
                    // 将每个tabInfo存储到对应的tab中
                    data['tabInfo'] = tabInfo;

                    if (node.parent.data.modelType === 'database') {
                        // 获取底部tabs的信息
                        let bottomTabsData: Array<object> = await this.getBottomTabsInfo(data);
                        for (let tab of bottomTabsData) {
                            // label tab显示名字  name tab的标识 也是tab激活的标识
                            tab['label'] = tab['nameCn'] || tab['nameEn'] || 'unknown';
                            tab['name'] = tab['nameEn'];

                            // 获取每个tab下table的头部信息
                            const headerData: Array<object> = await this.getMetadataProperties(tab['resourceId']);
                            tab['headerData'] = [...JSON.parse(JSON.stringify(this.baseTableHeaderData)), ...headerData];
                            tab['paginationData'] = {
                                total: 1,
                                pageSize: 10,
                                currentPage: 1,
                                // 将tab的信息存储在table的分页信息中，因为table分页中需要使用到tab的信息 和此被点击的node 节点信息
                                tabInfo: tab,
                                insInfo: data,
                            };
                            // 获取每个tab下table的体数据
                            tab['tableData'] = await this.getTableData(tab, data, tab['paginationData']);
                        }
                        data['bottomTabsData'] = bottomTabsData;
                        data['bottomActiveName'] = bottomTabsData.length ? bottomTabsData[0]['name'] : '';
                    }
                }
                // tabs被激活的tab
                this.tabsActiveName = data.uuid;
                data['tabNode'] = node;
                this.tabsData.push(data);
            } else {
                this.$message.warning('只有能导入数据库、表')
            }


        }

        /**
         * 获取实例数据库、实例表value值
         */
        public async getInsDbTableValue(data): Promise<object> {
            let url = config.port('metadatavalue') + '/page';
            let params = {
                resourceId: data['resourceId'],
                size: -1
            };
            let retArr: Array<object> = [];
            await this.$http.get(url, {params}).then(response => {
                retArr = response.data.data.records[0];
            });
            return retArr;
        }


        /**
         * pageSize 改变时会触发
         * @param val 每页条数
         * @param headerData 当table的头部信息
         * @param data 当table的体数据
         */
        public handleSizeChange(val, paginationData, headerData, data): void {
            this.handleCurrentChange(val, paginationData, headerData, data);
        }

        /**
         * currentPage 改变时会触发
         * @param val 当前页
         * @param headerData 当table的头部信息
         * @param data 当table的体数据
         */
        public async handleCurrentChange(val, paginationData, headerData, data): Promise<void> {
            const {insInfo, tabInfo} = paginationData;
            const tableData = await this.getTableData(tabInfo, insInfo, paginationData);
            data.splice(0, data.length, ...tableData);
        }

        /**
         * 获取table体数据
         * @param tabInfo 当前tab的数据
         * @param insInfo 当前查看显示实例下数据的实例数据，就是被点击card的数据
         * return table 体数据
         */
        public async getTableData(tabInfo, insInfo, paginationData) {
            let url = config.port('metadatavalue') + '/page';
            let params = {
                parentId: insInfo['resourceId'],
                modelId: tabInfo['resourceId'],
                current: paginationData['currentPage'], // 当前显示的页数
                size: paginationData['pageSize'], // 当前页数显示的条数
            };
            let tableData: Array<object> = [];
            await this.$http.get(url, {params}).then(response => {
                const res = response.data.data;
                // 更新分页信息
                paginationData['currentPage'] = res.current;
                paginationData['total'] = res.total;

                tableData = res.records;
            });
            return tableData;
        }

        /**
         * 获取当前tab先底部tabs的信息
         * @param data 当前被点击的tree node节点数据
         * return获取当前tab先底部tabs的信息数据集
         */
        public async getBottomTabsInfo(data): Promise<Array<object>> {
            let url = config.port('metadatamodel') + '/page';
            let params: object = {
                parentResourceId: data.modelId
            };
            let bottomTabsData: Array<object> = [];
            await this.$http.get(url, {params}).then((response) => {
                const res = response.data.data;
                bottomTabsData = res.records;
            });
            return bottomTabsData;
        }


        /**
         * 树搜索
         */
        public searchTree(queryString): void {
            this.$refs.tree1['emitSearch'](queryString);
            this.$refs.tree2['emitSearch'](queryString);
            if (queryString === '') {
                this.treeData = [];
                this.searchVal = queryString;
            } else {
                let url = config.port('metadatavalue') + '/tree/lazyTree';
                let params: object = {
                    queryString
                };
                this.$http.get(url, {params}).then(response => {
                    this.treeData = response.data.data;
                    this.searchVal = queryString;
                });
            }
        }

        /**
         * 懒加载树
         * @param node node节点
         * @param resolve
         */
        public loadNode(node, resolve): void {
            // 加载实例表时候，调用其他接口
            let params: object = {};
            if (node.data && node.data.modelType === 'table') {
                params = {
                    tableName: '',
                    modelId: node.parent.data.modelId,
                    resourceId: node.parent.data.resourceId,
                    metaDataType: '',
                    metaDataId: '',
                    parentMetaDataId: '',
                    size: -1
                };
                let url = config.port('metadataImport') + '/getTablePageList';
                this.$http.get(url, {params}).then(response => {
                    // this.$http.get(url).then(response => {
                    resolve(response.data.data.records)
                }).catch(err => resolve([]));
                return;
            }


            // level = 0 代表tree刚加载
            if (node.level !== 0) {
                // level大于等于1时候，需要传递参数获取子级数据
                params['parentUuid'] = node.data.uuid;
            }
            let url = config.port('metadatavalue') + '/tree/lazyTree';
            this.$http.get(url, {params}).then(response => {
                resolve(response.data.data)
            }).catch(err => resolve([]));
        }
    }
</script>

<style lang="less">
    .metadata-to-data {
        display: flex;

        //  tree盒子
        .tree-wrapper {
            width: 300px;
            height: 100%;
            // tree树展示名称区域
            .tree-name {
                width: 100%;

                &:hover {
                    .operation-wrapper {
                        visibility: visible;
                    }
                }

                .operation-wrapper {
                    visibility: hidden;
                    position: absolute;
                    right: 10px;
                    top: 0;
                    bottom: 0;
                    display: flex;
                    align-items: center;

                    i {
                        margin: 0 5px;

                        &:hover {
                            color: #188bf5;
                        }
                    }
                }
            }
        }

        // tab盒子
        .tab-wrapper {
            // 每个tab的区域
            .tab-container-wrapper {
                position: relative;
                overflow: auto;
                width: 100%;
                height: 100%;
                // 存放此tab信息区域
                .tab-info {
                    position: absolute;
                    top: 0;
                    right: 0;
                    left: 0;
                    bottom: 70%;
                    padding: 10px;
                    background-color: #fff;
                    box-shadow: 0 1px 0 #ccc;
                    overflow: auto;

                    .item-col {
                        margin-bottom: 10px;
                    }

                    .item-wrapper {
                        margin: 10px 0;
                        display: flex;

                        .value {
                            flex: 1;
                            overflow: hidden;
                            text-overflow: ellipsis;
                            white-space: nowrap;
                        }
                    }

                }

                // 存放此tab对应元数据底下模型和元数据
                .children-data {
                    position: absolute;
                    padding-top: 1px;
                    top: 30%;
                    right: 0;
                    left: 0;
                    bottom: 0;
                }
            }

        }
    }
</style>
