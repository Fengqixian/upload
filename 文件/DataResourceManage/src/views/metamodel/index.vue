<template>
    <div class="meta-model cb-tree-show-operation-wrapper">
        <div class="tree-wrapper">
            <cb-tree :data="treeData"
                     ref="tree">
                <template slot="header">
                    <div class="tree-header">
                        <div>元模型</div>
                        <el-button type="text"
                                   icon="add-model el-icon-plus"
                                   class="add-text"
                                   @click.prevent.stop="addEditModel('add')">新增
                        </el-button>
                    </div>
                </template>
                <template slot-scope="{data,node}">
                    <div class="tree-name" @click="treeNodeClick(data,node)">
                        <span>{{data.nameCn || data.nameEn || 'unknown'}}</span>
                        <div class="operation-wrapper">
                            <el-tooltip effect="dark" content="新增" placement="top-start">
                                <el-button type="text"
                                           icon="el-icon-plus"
                                           class="add-text"
                                           @click.prevent.stop="addEditModel('add',data,node)">
                                </el-button>
                            </el-tooltip>
                            <el-tooltip effect="dark" content="编辑" placement="top-start">
                                <el-button type="text"
                                           class="edit-text"
                                           icon="el-icon-edit"
                                           @click.prevent.stop="addEditModel('edit',data,node)">
                                </el-button>
                            </el-tooltip>
                            <el-tooltip effect="dark" content="删除" placement="top-start">
                                <el-button type="text"
                                           class="del-text"
                                           icon="el-icon-delete"
                                           @click.prevent.stop="delModel(data,node)">
                                </el-button>
                            </el-tooltip>
                        </div>
                    </div>
                </template>
            </cb-tree>
        </div>
        <div class="tab-wrapper show-operation-wrapper">
            <cb-tab :tab-data="tabsData"
                    v-model.trim="tabsActiveName">
                <template :slot="tab.name"
                          v-for="tab in tabsData">
                    <div class="tab-container-wrapper">
                        <cb-operation-container>
                            <template slot="toOperation">
                                <el-button type="text" icon="el-icon-edit" class="edit-text"
                                           @click.prevent.stop="editAttr(tab)">编辑
                                </el-button>
                            </template>
                            <template slot="cbContainer">
                                <div class="tab-info">
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
                                    <cb-table :headerData="headerData"
                                              :tableData="tab.tableData"
                                              :paginationData="tab.paginationData"
                                              @size-change="handleSizeChange"
                                              @current-change="handleCurrentChange">
                                    </cb-table>
                                </div>
                            </template>
                        </cb-operation-container>
                    </div>
                </template>

            </cb-tab>
        </div>


        <!-- 新增、编辑模型弹框 -->
        <el-dialog :title="addEditModelType === 'add'? '新增模型' : '编辑模型'"
                   :close-on-click-modal="false"
                   :visible.sync="addEditModelVisible"
                   width="30%">
            <el-form ref="addEditModelFromRef"
                     :model="addEditModelFrom"
                     label-width="120px"
                     :rules="addEditModelRules">
                <el-form-item label="模型英文名称" prop="nameEn">
                    <el-input v-model.trim="addEditModelFrom.nameEn" placeholder="模型英文名称"
                              @keydown.native.enter="submitForm('ruleForm')"></el-input>
                </el-form-item>
                <el-form-item label="模型中文名称" prop="nameCn">
                    <el-input v-model.trim="addEditModelFrom.nameCn" placeholder="模型中文名称"
                              @keydown.native.enter="submitForm('ruleForm')"></el-input>
                </el-form-item>
                <el-form-item label="模型状态" prop="status">
                    <el-select v-model.trim="addEditModelFrom.status" placeholder="请选择"
                               @keydown.native.enter="submitForm('ruleForm')">
                        <template v-for="field in baseField">
                            <el-option
                                    v-if="field.prop === 'status'"
                                    v-for="item in field.options"
                                    :key="item.itemsCode"
                                    :label="item.itemsName"
                                    :value="item.itemsCode.toString()">
                            </el-option>
                        </template>
                    </el-select>
                </el-form-item>
                <el-form-item label="模型类型" prop="modelType">
                    <el-select v-model.trim="addEditModelFrom.modelType" placeholder="请选择模型类型"
                               @keydown.native.enter="submitForm('ruleForm')">
                        <el-option
                                v-for="item in modelTypeOptions"
                                :key="item.itemsCode"
                                :label="item.itemsName"
                                :value="item.itemsCode.toString()">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="描述" prop="description">
                    <el-input v-model.trim="addEditModelFrom.description" placeholder="描述"
                              @keydown.native.enter="submitForm('ruleForm')"></el-input>
                </el-form-item>
                <!--<el-form-item label="根模型" prop="description">
                    <el-switch
                            v-model.trim="addEditModelFrom.isStandard"
                            active-color="#13ce66"
                            inactive-color="#ff4949">
                    </el-switch>
                </el-form-item>-->
                <el-form-item>
                    <div class="btn-right">
                        <el-button class="del-nobg" @click="addEditModelVisible = false">取 消</el-button>
                        <el-button class="confirm-bg" @click="submitAddEditModelForm('ruleForm')">保 存
                        </el-button>
                    </div>
                </el-form-item>
            </el-form>
        </el-dialog>

        <el-dialog title="编辑元模型"
                   :visible.sync="editAttrVisible"
                   v-if="editAttrVisible"
                   width="80%"
                   :close-on-click-modal="false">
            <edit-meta-model
                    v-model.trim="editAttrVisible"
                    :metaModelData="metaModelData"
                    :tabData="tabData"
                    @success="editMetaModelSuccess">
            </edit-meta-model>
        </el-dialog>
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component, Watch} from 'vue-property-decorator'
    import {State, Action} from 'vuex-class'
    import config from "../../config";
    import EditMetaModel from './editMetaModel'


    @Component({components: {EditMetaModel}})
    export default class MetaModel extends Vue {
        name: 'MetaModel';
        @State baseField;   // 基本的字段
        @Action getBaseFiledOptions; // 获取基础字段中options
        public treeData: Array<object> = []; // 存储tree 数据
        public tabsData: Array<object> = []; // 存储tabs数据
        public tabsActiveName: string = ''; // tab激活的名称
        // 表头信息
        public headerData: Array<any> = [
            {
                label: 'ID',
                prop: 'id',
            },
            {
                label: '资源标识',
                prop: 'resourceId'
            },
            {
                label: '模型标识',
                prop: 'modelResourceId'
            },
            {
                label: '中文名称',
                prop: 'nameCn'
            },
            {
                label: '英文名称',
                prop: 'nameEn'
            },
            {
                label: '数据类型',
                prop: 'dataType'
            },
            {
                label: '数据长度',
                prop: 'dataLength'
            },
            {
                label: '展示方式',
                prop: 'showType'
            },
            {
                label: '展示格式',
                prop: 'showFormat'
            },
            {
                label: '映射列',
                prop: 'mappingColumn'
            },
            {
                label: '值域说明',
                prop: 'valueScope'
            },
            {
                label: '创建时间',
                prop: 'createTime'
            },
            {
                label: '更新时间',
                prop: 'updateTime'
            },
        ];

        /**
         * 新增 编辑模型
         */
        public addEditModelType: string = ''; // 新增 编辑模型类型 add edit
        public addEditModelVisible: boolean = false; // 对话框
        public addEditModelNode: object = null; // 操作节点的数据
        public modelTypeOptions: Array<object> = []; // 模型下拉框的选项
        // form表单
        public addEditModelFrom: object = {
            nameEn: '', // 模型英文名称
            nameCn: '', // 模型中文名称
            description: '', // 描述
            status: '',  // 描述
            modelType: '',  // 模型类型
            isStandard: false  // 根模型
        };
        // 正则校验
        public addEditModelRules: object = {
            nameEn: [{required: true, message: ' ', trigger: 'change'}],
            nameCn: [{required: true, message: ' ', trigger: 'change'}],
            modelType: [{required: true, message: ' ', trigger: 'change'}],
            status: [{required: true, message: ' ', trigger: 'change'}],
        };

        /**
         * 编辑元模型
         */
        public editAttrVisible: boolean = false;
        public metaModelData: object = {};
        public tabData: object = null; // 缓存当前被编辑的tab数据 用于编辑属性后 修改table值

        public async mounted(): Promise<void> {
            this.getTreeData();
            this.modelTypeOptions = await this.getBaseFiledOptions("modelType");
        }

        // 关闭新增编辑对话框时候 初始化form表单数据
        @Watch('addEditModelVisible')
        public addEditModelVisibleChange(newVal): void {
            if (!newVal) {
                this.addEditModelFrom = {
                    nameEn: '', // 模型英文名称
                    nameCn: '', // 模型中文名称
                    description: '', // 描述
                    status: '',  // 描述
                    modelType: '',  // 模型类型
                    isStandard: false  // 根模型
                };
                this.addEditModelNode = null;
                this.addEditModelType = '';
            }
        }

        /**
         * 删除元模型
         */
        public delModel(data: object, node: Node) {
            this.$confirm('删除时将删除模型和所有属性约束，确定删除吗？', '提示', {type: 'warning'}).then(() => {
                let URL = config.port('metadatamodel') + '/' + node['data'].id;
                this.$http.delete(URL).then((response) => {
                    this.$message.success('删除成功');
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
                    this.getTreeData()
                })
            }).catch(() => {
            });
        }

        /**
         * 编辑元模型成功回调
         */
        public async editMetaModelSuccess(tabData): Promise<void> {
            this.getTableData(tabData.paginationData.modelData,
                tabData.paginationData).then(tableData => {
                tabData.tableData.splice(0, tabData.tableData.length, ...tableData);
            })
        }

        /**
         * 编辑属性
         * @param tabData tab数据
         */
        public editAttr(tabData): void {
            this.metaModelData = tabData.paginationData.modelData;
            this.tabData = tabData;
            this.editAttrVisible = true;

        }

        /**
         * 新增 编辑模型提交
         */
        public submitAddEditModelForm() {
            this.$refs.addEditModelFromRef['validate']((valid) => {
                if (valid) {
                    let data: object = {...this.addEditModelFrom};
                    let method: string = '';
                    // 新增
                    if (this.addEditModelType === 'add') {
                        data['parentResourceId'] = this.addEditModelNode ?
                            this.addEditModelNode['data'].resourceId : null;
                        method = 'post';
                    }
                    // 编辑
                    else if (this.addEditModelType === 'edit') {
                        data['resourceId'] = this.addEditModelNode['data']['resourceId'];
                        data['id'] = this.addEditModelNode['data']['id'];
                        data['parentResourceId'] = this.addEditModelNode['data']['parentResourceId'];
                        method = 'put';
                    }
                    if (method !== '') {
                        let url: string = config.port('metadatamodel');
                        this.$http({url, data, method}).then(response => {
                            //保存成功
                            this.$message({
                                message: '操作成功',
                                type: 'success'
                            });
                            // 重新加载tree数据
                            this.getTreeData();
                            this.addEditModelVisible = false;
                        })
                    }


                } else {
                    return false;
                }
            });
        }

        /**
         * 新增 编辑模型
         * @param type 操作类型 add edit
         * @param data tree node 节点data数据
         * @param node tree node 数据
         */
        public addEditModel(type, data, node): void {
            this.addEditModelNode = node; // 缓存node数据
            this.addEditModelType = type;  // 缓存操作类型
            this.addEditModelVisible = true;
            if (type === 'edit') {
                this.addEditModelFrom = {
                    nameEn: data['nameEn'], // 模型英文名称
                    nameCn: data['nameCn'], // 模型中文名称
                    description: data['description'], // 描述
                    status: data['status'],  // 描述
                    modelType: data['modelType'],  // 模型类型
                    isStandard: data['isStandard']  // 根模型
                }
            }
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
            const {modelData} = paginationData;
            const tableData = await this.getTableData(modelData, paginationData);
            data.splice(0, data.length, ...tableData);
        }

        /**
         * 点击node节点
         * @param data tree node 节点data数据
         * @param node tree node 数据
         */
        public async treeNodeClick(data, node): Promise<void> {
            this.tabsActiveName = data['uuid'];
            // 组装tab详情数据
            let tabInfo: Array<object> = [
                {
                    label: '模型编号',
                    value: data['id'],
                    key: 'id'
                },
                {
                    label: '资源编号',
                    value: data['resourceId'],
                    key: 'resourceId'
                },
                {
                    label: '模型英文名称',
                    value: data['nameEn'],
                    key: 'nameEn'
                },
                {
                    label: '模型中文名称',
                    value: data['nameCn'],
                    key: 'nameCn'
                },
                {
                    label: '模型状态',
                    value: data['status'],
                    key: 'status'
                },
                {
                    label: '描述',
                    value: data['description'],
                    key: 'description'
                },
            ];


            // 组装底部table数据
            let paginationData: object = {
                total: 1,
                pageSize: 10,
                currentPage: 1,
                modelData: data // 存储每个节点模型的数据
            };
            // 到你table数据
            let tableData: Array<object> = [];
            await this.getTableData(data, paginationData).then(response => {
                tableData = response;
            });

            let tabData = {
                name: data.uuid,
                label: data['nameCn'] || data['nameEn'] || 'unknown',
                tabInfo,
                tableData,
                paginationData
            };
            this.tabsData.push(tabData)
        }

        /**
         * 获取当前模型下table数据
         * @param data 模型数据
         * @param paginationData 分页信息
         */
        public async getTableData(data, paginationData): Promise<Array<object>> {
            let url = config.port('metadataproperties') + '/page';
            let params: object = {
                current: paginationData['currentPage'], // 当前显示的页数
                size: paginationData['pageSize'], // 当前页数显示的条数
                modelResourceId: data.resourceId
            };
            let tableData: Array<object> = [];
            await this.$http.get(url, {params}).then(response => {
                const res = response.data;
                if (res.code === 0) {
                    paginationData['currentPage'] = res.data['current'];
                    paginationData['total'] = res.data['total'];
                    paginationData['pageSize'] = res.data['size'];
                    tableData = res.data.records;
                }
            });
            return tableData;
        }


        /**
         * 获取tree树的数据
         */
        public getTreeData() {
            let url = config.port('metadatamodel') + '/tree';
            this.$http.get(url).then(response => {
                this.treeData = response.data.data;
            })
        }

    }


</script>

<style lang="less">
    .meta-model {
        display: flex;

        //  tree盒子
        .tree-wrapper {
            width: 300px;
            height: 100%;

            .title-btn {
                cursor: pointer;

                &:hover {
                    color: #188bf5;
                }
            }

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
                    top: 31%;
                    right: 0;
                    left: 0;
                    bottom: 0;
                    padding-top: 1px;
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
