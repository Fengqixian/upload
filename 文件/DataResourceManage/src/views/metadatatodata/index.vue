<template>
    <div class="metadata-to-data cb-tree-show-operation-wrapper">
        <div class="tree-wrapper">
            <cb-tree lazy
                     searchFromHttp
                     @search="searchTree"
                     v-if="searchVal === ''"
                     @load-node="loadNode"
                     ref="tree1">
                <template slot-scope="{data,node}">
                    <div class="tree-name" @click="treeNodeClick(data,node)">
                        <span>{{data.nameCn || data.nameEn || 'unknown'}}</span>
                        <div class="operation-wrapper">
                            <el-tooltip effect="dark" content="新增" placement="top-start">
                                <el-button icon="el-icon-plus"
                                           type="text"
                                           class="add-text"
                                           v-if="data.type === 'MOD'"
                                           @click.prevent.stop="addMetadata(data,node)">
                                </el-button>
                            </el-tooltip>
                            <el-tooltip effect="dark" content="导入" placement="top-start">
                                <el-button icon="el-icon-download"
                                           type="text"
                                           class="confirm-text"
                                           v-if="data.type === 'MOD'
                                           && (data.modelType === 'table'
                                           || data.modelType === 'primary_key'
                                            || data.modelType === 'column'
                                             || data.modelType === 'index'
                                             || data.modelType === 'foreign_key')"
                                           @click.prevent.stop="importMetadata(data,node)">
                                </el-button>
                            </el-tooltip>
                            <el-tooltip effect="dark" content="删除" placement="top-start">
                                <el-button icon="el-icon-delete"
                                           type="text"
                                           class="del-text"
                                           v-if="data.type === 'INS'"
                                           @click.prevent.stop="deleteMetadata(data,node)">
                                </el-button>
                            </el-tooltip>
                            <el-tooltip effect="dark" content="编辑" placement="top-start">
                                <el-button icon="el-icon-edit"
                                           type="text"
                                           class="edit-text"
                                           v-if="data.type === 'INS'"
                                           @click.prevent.stop="editMetadata(data,node)">
                                </el-button>
                            </el-tooltip>
                            <el-tooltip effect="dark" content="生成基础元数据" placement="top-start">
                                <el-button icon="iconfont icon-jichuyuanshuju"
                                           type="text"
                                           class="add-text"
                                           v-if="data.type === 'INS' && data.modelType === 'database'"
                                           @click.prevent.stop="createElementItem(data,node)">
                                </el-button>
                            </el-tooltip>
                        </div>
                    </div>
                </template>
            </cb-tree>
            <cb-tree @search="searchTree"
                     searchFromHttp
                     v-if="searchVal !== ''"
                     :data="treeData"
                     ref="tree2">
                <template slot-scope="{data,node}">
                    <div class="tree-name" @click="treeNodeClick(data,node)">
                        <span>{{data.nameCn || data.nameEn || 'unknown'}}</span>
                        <div class="operation-wrapper">
                            <el-tooltip effect="dark" content="新增" placement="top-start">
                                <el-button icon="el-icon-plus"
                                           type="text"
                                           class="add-text"
                                           v-if="data.type === 'MOD'"
                                           @click.prevent.stop="addMetadata(data,node)">
                                </el-button>
                            </el-tooltip>
                            <el-tooltip effect="dark" content="导入" placement="top-start">
                                <el-button icon="el-icon-download"
                                           type="text"
                                           class="confirm-text"
                                           v-if="data.type === 'MOD'
                                           && (data.modelType === 'table'
                                           || data.modelType === 'primary_key'
                                            || data.modelType === 'column'
                                             || data.modelType === 'index'
                                             || data.modelType === 'foreign_key')"
                                           @click.prevent.stop="importMetadata(data,node)">
                                </el-button>
                            </el-tooltip>
                            <el-tooltip effect="dark" content="删除" placement="top-start">
                                <el-button icon="el-icon-delete"
                                           type="text"
                                           class="del-text"
                                           v-if="data.type === 'INS'"
                                           @click.prevent.stop="deleteMetadata(data,node)">
                                </el-button>
                            </el-tooltip>
                            <el-tooltip effect="dark" content="编辑" placement="top-start">
                                <el-button icon="el-icon-edit"
                                           type="text"
                                           class="edit-text"
                                           v-if="data.type === 'INS'"
                                           @click.prevent.stop="editMetadata(data,node)">
                                </el-button>
                            </el-tooltip>
                            <el-tooltip effect="dark" content="生成基础元数据" placement="top-start">
                                <el-button icon="iconfont icon-jichuyuanshuju"
                                           type="text"
                                           class="add-text"
                                           v-if="data.type === 'INS' && data.modelType === 'database'"
                                           @click.prevent.stop="createElementItem(data,node)">
                                </el-button>
                            </el-tooltip>
                        </div>
                    </div>
                </template>
            </cb-tree>
        </div>
        <div class="tab-wrapper show-operation-wrapper">
            <cb-tab :tabData="tabsData"
                    v-model.trim="tabsActiveName">
                <template :slot="tab.name"
                          v-for="tab in tabsData">
                    <div class="tab-container-wrapper">
                        <div class="tab-info" :style="{bottom:tab.bottomTabsData.length?'70%':'0'}">
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
            </cb-tab>
        </div>

        <!-- Excel导入元数据 -->
        <el-dialog :close-on-click-modal="false"
                   title="Excel导入元数据"
                   width="30%"
                   :visible.sync="excelImportMetadataVisible">
            <el-form ref="excelImportForm" label-width="80px" v-if="excelImportMetadataVisible">
                <el-form-item>
                    <template>
                        <el-radio-group v-model.trim="importModel" @change="changeModel">
                            <el-radio label="addModel">新增模式</el-radio>
                            <el-radio label="updateModel">更新模式</el-radio>
                            <template>
                                <el-radio-group v-model.trim="updateModel" v-if="updateModelIsVisibal"
                                                @change="changeUpdateModel">
                                    <el-radio label="updateByCn">根据中文名更新</el-radio>
                                    <el-radio label="updateByEn">根据英文名更新</el-radio>
                                </el-radio-group>
                            </template>
                        </el-radio-group>
                    </template>
                </el-form-item>
                <el-form-item>
                    标题所在行：
                    <el-input v-model.trim.number="excelTitleNum" style="width:100px;"></el-input>
                </el-form-item>
                <el-form-item prop="excelImport">
                    <el-upload
                            class="upload-demo"
                            ref="fileUpload"
                            :action="uploadUrl"
                            :auto-upload="false"
                            :on-exceed="handleExceed"
                            :file-list="fileList"
                            :before-remove="beforeRemove"
                            :before-upload="beforeUpload"
                            :drag='false'
                            :limit="1"
                            accept=".xls,.xlsx"
                            :data="formData"
                            :on-success="onSuccess"
                            :on-error="onError">
                        <el-button class="add-bg">选择文件</el-button>
                        <div slot="tip" class="el-upload__tip">只能导入*.xls/*.xlsx文件</div>
                    </el-upload>
                </el-form-item>
                <el-form-item>
                    <div class="btn-right">
                        <el-button class="del-nobg" @click="excelImportMetadataVisible=false">取 消</el-button>
                        <el-button class="confirm-bg" @click="importSubmit">导 入</el-button>
                    </div>
                </el-form-item>
            </el-form>
        </el-dialog>


        <el-dialog width="30%"
                   :title="addEditMetaDataOperation=== 'add'?'创建元数据':'修改元数据'"
                   :visible.sync="addEditMetadataVisible"
                   :close-on-click-modal="false">
            <el-form ref="editMetadataForm"
                     :model="addEditMetaDataValues"
                     label-width="80px"
                     v-if="addEditMetadataVisible"
                     :rules="editMetadataFormRules">
                <template v-for="item in addEditMetadataLabels">
                    <el-form-item :label="item.label" :prop="item.prop">
                        <el-switch v-if="item['type'] === 'switch'"
                                   v-model.trim="addEditMetaDataValues[item.prop]"></el-switch>
                        <el-select v-else-if="item['type'] === 'select'"
                                   v-model.trim="addEditMetaDataValues[item.prop]"
                                   clearable
                                   placeholder="请选择">
                            <el-option
                                    v-for="ite in item['options']"
                                    :key="ite.itemsCode"
                                    :label="ite.itemsName"
                                    :value="ite.itemsCode">
                            </el-option>
                        </el-select>
                        <el-input v-else
                                  v-model.trim="addEditMetaDataValues[item.prop]"
                                  :placeholder="item.label" :disabled="item.prop === 'modelType'"></el-input>
                    </el-form-item>
                </template>
                <el-form-item>
                    <div class="btn-right">
                        <el-button class="del-nobg" @click="addEditMetadataVisible=false">取 消</el-button>
                        <el-button class="confirm-bg"
                                   @click.stop.prevent="submitAddEditMetadata">
                            {{addEditMetaDataOperation=== 'add'?'创 建':'修 改'}}
                        </el-button>
                    </div>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component} from 'vue-property-decorator'
    import {State, Action, Mutation} from 'vuex-class'
    import config from "../../config";
    import CreateEditMetadata from './createEditMetadata'

    @Component({components: {CreateEditMetadata}})
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
         * 第二版创建修改元数据区域
         */
        public addEditMetadataVisible: boolean = false;
        public addEditMetadataLabels: Array<object> = [];
        public editMetadataFormRules: object = {};
        public addEditMetaDataValues: object = {};
        public addEditMetaDataOperation: string = 'add'; // 创建元数据为add 修改为edit
        public addEditMetaDataNode: any = ''; // 当前修改或者创建节点的node

        /**
         * 创建元素项
         */
        public createElementItem(data, node): void {
            let url = config.port('metadatavalue') + '/createElementItem';
            let params = {
                dbResouceId: data.resourceId
            };
            this.$http.get(url, {params}).then(response => {
                if (response.data.code === 0) {
                    this.$message.success('创建元素项成功')
                    this.getTreeDataToRefresh(node['data'].uuid).then(res => {
                        this.$refs.tree1 && this.$refs.tree1['refreshChildNode'](node, res);
                        this.$refs.tree2 && this.$refs.tree2['refreshChildNode'](node, res);
                    })
                } else {
                    this.$message.error('创建元素项失败')
                }
            })
        }

        /**
         * 创建修改元数据
         */
        public submitAddEditMetadata(): void {

            this.$refs.editMetadataForm['validate'](valid => {
                if (valid) {
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
                        this.$refs.editMetadataForm['resetFields']();
                        if (this.addEditMetaDataOperation === 'edit') {
                            this.getTreeDataToRefresh(this.addEditMetaDataNode['parent'].data.uuid).then(res => {
                                this.$refs.tree2['refreshChildNode'](this.addEditMetaDataNode['parent'], res);
                                this.$refs.tree1['refreshChildNode'](this.addEditMetaDataNode['parent'], res);
                            })
                        } else {
                            this.getTreeDataToRefresh(this.addEditMetaDataNode['data'].uuid).then(res => {
                                this.$refs.tree1['refreshChildNode'](this.addEditMetaDataNode, res);
                                this.$refs.tree2['refreshChildNode'](this.addEditMetaDataNode, res);
                            })
                        }
                    })
                } else {
                    return false;
                }
            });

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
         * 创建元数据
         * @param data node节点数据
         * @param node node节点
         */
        public async addMetadata(data, node): Promise<void> {
            this.addEditMetaDataNode = node;
            this.addEditMetadataVisible = true;
            // 设置为添加模式
            this.addEditMetaDataOperation = 'add';
            // 获取元数据属性
            await this.getAddEditMetadataLabels(data, node);
            // 将值置为空
            this.addEditMetadataLabels.forEach(item => {
                if (item['type'] === 'switch') {
                    this.addEditMetaDataValues[item['prop']] = false;
                } else if (item['prop'] === 'modelType') {
                    this.addEditMetaDataValues[item['prop']] = data['modelType'];
                } else {
                    this.addEditMetaDataValues[item['prop']] = ''
                }
            });
            // 重置校验
            setTimeout(() => {
                this.$refs.editMetadataForm['resetFields']();
                this.addEditMetaDataValues['modelType'] = data['modelType'];
            }, 10);
            this.addEditMetaDataValues = JSON.parse(JSON.stringify(this.addEditMetaDataValues));
        }

        /**
         * 编辑元数据
         * @param data node节点数据
         * @param node node节点
         */
        public async editMetadata(data, node): Promise<void> {
            this.addEditMetaDataNode = node;
            this.addEditMetadataVisible = true;
            // 设置为编辑模式
            this.addEditMetaDataOperation = 'edit';
            // 获取元数据属性
            await this.getAddEditMetadataLabels(node.parent.data, node.parent);
            // 获取元数据数据
            this.getAddEditMetaDataValues(data, node);
        }

        /**
         * 获取元数据属性
         * @param data node节点数据
         * @param node node节点
         */
        public async getAddEditMetadataLabels(data, node): Promise<void> {
            // 获取label
            let labels = await this.getMetadataProperties(data.resourceId);
            this.addEditMetadataLabels = [...JSON.parse(JSON.stringify(this.baseField)), ...labels];
            // 动态添加校验
            this.editMetadataFormRules = {};
            this.addEditMetadataLabels.forEach(item => {
                this.editMetadataFormRules[item['prop']] = [{required: true, message: ' ', trigger: 'change'}];
            });

        }

        /**
         * 获取元数据数据
         * @param data node节点数据
         * @param node node节点
         */
        public async getAddEditMetaDataValues(data, node): Promise<void> {
            this.addEditMetaDataValues = await this.getDetailValue(data);
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
                    this.$refs.tree1 && this.$refs.tree1['remove'](node, data);
                    this.$refs.tree2 && this.$refs.tree2['remove'](node, data);
                    let stayTabsData = []; // 没有被删除的tabs
                    let deleteIndex = null;
                    this.tabsData.forEach((item, index) => {
                        if (item['name'] !== data['uuid']) {
                            stayTabsData.push(item);
                        } else {
                            deleteIndex = index;
                        }
                    });
                    this.tabsData.splice(0, this.tabsData.length, ...stayTabsData);
                    if (deleteIndex !== null) {
                        this.tabsActiveName = this.tabsData[deleteIndex] ? this.tabsData[deleteIndex]['name'] : this.tabsData[deleteIndex - 1] ? this.tabsData[deleteIndex - 1]['name'] : '';
                    }
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
            // 点击的节点是模型 不做任何处理
            if (data.type === 'MOD') return;

            // label tab展示的名称 name 是tab的标识
            data['label'] = data.nameCn || data.nameEn || 'unknown';
            data['name'] = data.uuid;

            // 获取每个tab的属性详情
            // const attrs: Array<object> = await this.getDetailAttr(data);
            const attrs: Array<object> = await this.getMetadataProperties(data['modelId']);
            const values: object = await this.getDetailValue(data);
            // 声明每个tab存储的tabInfo
            const tabInfo: Array<object> = [...JSON.parse(JSON.stringify(this.baseField)), ...attrs];
            this.letFieldsAssignments({fields: tabInfo, values});
            // 将每个tabInfo存储到对应的tab中
            data['tabInfo'] = tabInfo;
            // 获取底部tabs的信息
            let bottomTabsData: Array<object> = await this.getBottomTabsInfo(data);
            for (let tab of bottomTabsData) {
                // label tab显示名字  name tab的标识 也是tab激活的标识
                tab['label'] = tab['nameCn'] || tab['nameEn'] || 'unknown';
                tab['name'] = tab['nameEn'];

                // 获取每个tab下table的头部信息
                // const headerData: Array<object> = await this.getTableHeaderData(tab);
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
            // tabs被激活的tab
            this.tabsActiveName = data.uuid;
            this.tabsData.push(data);
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
         * 获取详情的值
         * @param data 当前被点击的tree node节点数据
         * return 获取详情值的数据集
         */
        public async getDetailValue(data): Promise<object> {
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
            let url = config.port('metadatavalue') + '/tree/lazyTree';
            let params: object = {
                parentUuid: ''
            };
            if (node.level !== 0) {
                params['parentUuid'] = node.data.uuid
            }
            // 只有根模型过滤已审批数据
            if (node.data && node.data.isStandard === true) {
                params['status'] = 1
            }
            this.$http.get(url, {params}).then(response => {
                let res = response.data;
                resolve(res.data);
            });
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
                width: 100%;
                height: 100%;
                /*display: flex;*/
                /*flex-direction: column;*/
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

        // 创建修改模态框
        .createEditMetadata {
            .el-dialog {
                height: 75%;
                display: flex;
                flex-direction: column;

                .el-dialog__body {
                    flex: 1;
                    position: relative;
                }
            }
        }
    }
</style>
