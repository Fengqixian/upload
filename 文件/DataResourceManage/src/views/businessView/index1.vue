<template>
    <div class="business-view cb-tree-show-operation-wrapper">
        <div class="tree-wrapper">
            <cb-tree
                    :data="treeData"
                    nodeKey="id"
                    :default-expanded-keys="defaultExpandedKeys"
                    ref="tree">
                <template slot-scope="{data,node}">
                    <div class="tree-name" @click="handleNodeClick(data,node)">
                        <span>{{data.nameCn || data.nameEn || 'unknown'}}</span>
                        <div class="operation-wrapper" v-if="'categorydataset'.includes(data.nodeType)">
                            <template v-if="data.nodeType === 'category'">  <!-- category 1 分类  2 数据集 -->
                                <el-tooltip effect="dark" content="编辑" placement="top-start">
                                    <el-button type="text"
                                               class="edit-text"
                                               icon="el-icon-edit"
                                               @click.prevent.stop="operationClick('editCategory',data,node)">
                                    </el-button>
                                </el-tooltip>
                                <!--<i class="el-icon-edit"
                                   @click.stop.prevent="operationClick('editCategory',data,node)">编辑</i>-->
                                <el-tooltip effect="dark" content="新增子类" placement="top-start">
                                    <el-button type="text"
                                               v-if="node.level === 1"
                                               icon="el-icon-plus"
                                               class="add-text"
                                               @click.prevent.stop="operationClick('addCategory',data,node)">
                                    </el-button>
                                </el-tooltip>
                                <!--<i class="el-icon-circle-plus-outline"
                                   @click.stop.prevent="operationClick('addCategory',data,node)"
                                   v-if="node.level === 1">子类</i>-->
                                <el-tooltip effect="dark" content="新增数据集" placement="top-start">
                                    <el-button type="text"
                                               icon="el-icon-plus"
                                               class="add-text"
                                               @click.prevent.stop="operationClick('addDataset',data,node)">
                                    </el-button>
                                </el-tooltip>
                                <!--<i class="el-icon-circle-plus-outline"
                                   @click.stop.prevent="operationClick('addDataset',data,node)">数据集</i>-->
                            </template>
                            <template v-else>
                                <el-tooltip effect="dark" content="编辑" placement="top-start">
                                    <el-button type="text"
                                               class="edit-text"
                                               icon="el-icon-edit"
                                               @click.prevent.stop="operationClick('editDataset',data,node)">
                                    </el-button>
                                </el-tooltip>
                                <!--<i class="el-icon-edit"
                                   @click.stop.prevent="operationClick('editDataset',data,node)">编辑</i>-->
                            </template>
                        </div>
                    </div>
                </template>
            </cb-tree>
        </div>
        <div class="tab-wrapper show-operation-wrapper">
            <cb-tab :tabData="tabsAllData"
                    v-model.trim="tabsActiveId">
                <template :slot="tab.name"
                          v-for="tab in tabsAllData">
                    <div class="tab-container-wrapper">
                        <business-tab :arg="tab"/>
                    </div>
                </template>
            </cb-tab>
        </div>

        <el-dialog title="分类" :visible.sync="categoryVisible">
            <el-form :model="categoryForm"
                     :rules="categoryFormRules"
                     label-width="100px"
                     ref="categoryFormRef">
                <el-form-item label="中文名称" prop="nameCn">
                    <el-input v-model.trim="categoryForm.nameCn" placeholder="中文名称"></el-input>
                </el-form-item>
                <el-form-item label="英文名称" prop="nameEn">
                    <el-input v-model.trim="categoryForm.nameEn" placeholder="英文名称"></el-input>
                </el-form-item>
                <el-form-item label="资源编码" prop="resourceCode">
                    <el-input v-model.trim="categoryForm.resourceCode" placeholder="资源编码"></el-input>
                </el-form-item>
                <!--<el-form-item label="资源编号" prop="resourceId">
                    <el-input v-model="categoryForm.resourceId" placeholder="资源编号"></el-input>
                </el-form-item>-->
                <el-form-item label="来源标识" prop="sourceId">
                    <el-input v-model.trim="categoryForm.sourceId" placeholder="来源标识"></el-input>
                </el-form-item>
                <!--<el-form-item label="类别" prop="category">
                    <el-select v-model="categoryForm.category" placeholder="类别">
                        <el-option label="分类" value="1"></el-option>
                        <el-option label="数据集" value="2"></el-option>
                        <el-option label="其他" value="9"></el-option>
                    </el-select>
                </el-form-item>-->
                <el-form-item label="备注" prop="description">
                    <el-input type="textarea" v-model.trim="categoryForm.description" placeholder="备注"></el-input>
                </el-form-item>
                <el-form-item>
                    <div class="btn-right">
                        <el-button class="del-nobg" @click="categoryVisible = false">取 消</el-button>
                        <el-button class="confirm-bg" @click="submitCategory()">确 定</el-button>
                    </div>
                </el-form-item>
            </el-form>
        </el-dialog>

        <el-dialog title="数据集" :visible.sync="elementVisible">
            <el-steps :space="200"
                      :active="step-1"
                      finish-status="success"
                      align-center
                      style="margin: 10px 0;">
                <el-step title="详情"></el-step>
                <el-step title="数据元"></el-step>
                <el-step title="预览"></el-step>
            </el-steps>

            <!--第一步-->
            <template v-if="step === 1">
                <el-form :model="categoryForm"
                         :rules="categoryFormRules"
                         label-width="100px"
                         ref="elementFormRef">
                    <el-form-item label="中文名称" prop="nameCn">
                        <el-input v-model.trim="categoryForm.nameCn" placeholder="中文名称"></el-input>
                    </el-form-item>
                    <el-form-item label="英文名称" prop="nameEn">
                        <el-input v-model.trim="categoryForm.nameEn" placeholder="英文名称"></el-input>
                    </el-form-item>
                    <el-form-item label="资源编码" prop="resourceCode">
                        <el-input v-model.trim="categoryForm.resourceCode" placeholder="资源编码"></el-input>
                    </el-form-item>
                    <!--<el-form-item label="资源编号" prop="resourceId">
                        <el-input v-model="categoryForm.resourceId" placeholder="资源编号"></el-input>
                    </el-form-item>-->
                    <el-form-item label="来源标识" prop="sourceId">
                        <el-input v-model.trim="categoryForm.sourceId" placeholder="来源标识"></el-input>
                    </el-form-item>
                    <!--<el-form-item label="类别" prop="category">
                        <el-select v-model="categoryForm.category" placeholder="类别">
                            <el-option label="分类" value="1"></el-option>
                            <el-option label="数据集" value="2"></el-option>
                            <el-option label="其他" value="9"></el-option>
                        </el-select>
                    </el-form-item>-->
                    <el-form-item label="备注" prop="description">
                        <el-input type="textarea" v-model.trim="categoryForm.description" placeholder="备注"></el-input>
                    </el-form-item>
                </el-form>
            </template>


            <!--第二步-->
            <template v-if="step === 2">
                <div class="search">
                    <el-input placeholder="请输入内容" v-model.trim="transferSearch"
                              @keydown.native.enter.stop.prevent="getElementList">
                        <el-button slot="append" icon="el-icon-search" @click="getElementList"></el-button>
                    </el-input>
                </div>
                <el-transfer v-model.trim="transferValue"
                             :titles="['待添加', '已添加']"
                             :props="{
                                  key: 'id',
                                  label: 'nameCn'
                                }"
                             :data="transferData">
                </el-transfer>
            </template>


            <!--第三步-->
            <template v-if="step === 3">
                <div class="elementInfo">
                    <div class="title">详情：</div>
                    <ul class="info">
                        <li>
                            <span class="label">中文名称：</span>
                            <span class="value">{{categoryForm.nameCn}}</span>
                        </li>
                        <li>
                            <span class="label">英文名称：</span>
                            <span class="value">{{categoryForm.nameEn}}</span>
                        </li>
                        <li>
                            <span class="label">资源编码：</span>
                            <span class="value">{{categoryForm.resourceCode}}</span>
                        </li>
                        <li v-if="categoryForm.resourceId">
                            <span class="label">资源编号：</span>
                            <span class="value">{{categoryForm.resourceId}}</span>
                        </li>
                        <li>
                            <span class="label">来源标识：</span>
                            <span class="value">{{categoryForm.sourceId}}</span>
                        </li>
                        <li>
                            <span class="label">类别：</span>
                            <span class="value" v-if="categoryForm.category === '1'">分类</span>
                            <span class="value" v-else-if="categoryForm.category === '2'">数据集</span>
                            <span class="value" v-else>其他</span>
                        </li>
                        <li>
                            <span class="label">备注：</span>
                            <span class="value">{{categoryForm.description}}</span>
                        </li>
                    </ul>
                </div>

                <div class="elementInfo">
                    <div class="title">数据元信息：</div>
                    <div class="table-wrapper">
                        <cb-table :header-data="dataMetaHeaderData"
                                  :table-data="dataMetaTableData"
                                  :pagination-data="dataMetaPaginationData"
                                  @size-change="getDataMetaTableData"
                                  @current-change="getDataMetaTableData"
                                  ref="dataMetaTable">
                        </cb-table>
                    </div>
                </div>
            </template>
            <div slot="footer">
                <div class="btn-right">
                    <el-button class="del-nobg" @click="elementVisible = false">取 消</el-button>
                    <el-button class="add-bg" v-if="step > 1" @click="(step>0)?step--:null">上一步</el-button>
                    <el-button class="confirm-bg" v-if="step < 3" @click="nextStep">下一步</el-button>
                    <el-button class="confirm-bg" v-if="step === 3" @click="submitElement">确 定</el-button>
                    <!--
                    <div class="btn-right">
                        <el-button class="del-nobg" @click="categoryVisible = false">取 消</el-button>
                        <el-button class="confirm-bg" @click="submitCategory()">确 定</el-button>
                    </div>
                    -->
                </div>
            </div>
        </el-dialog>
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component, Watch} from 'vue-property-decorator'
    import config from '../../config'
    import {State, Mutation} from 'vuex-class'
    import BusinessTab from './businessTab.vue'
    import {isIE} from '../../common/util/IE'

    @Component({
        components: {BusinessTab}
    })
    export default class BloodRelation extends Vue {
        myThis: any = this;
        @State pageSizes;
        @State pageSize;
        @State loadingFlag;
        @Mutation setLoadingFlag;
        name: string = 'MetaImport';
        // 元模型tree数据
        treeData: Array<object> = [];
        defaultExpandedKeys = [];
        defaultProps: object = {
            children: 'children',
            label: 'label'
        };
        // form表单其他数据
        otherData: any = null;
        // tab标签
        tabsActiveId: string = '';
        //  tab内所有的数据
        tabsAllData: Array<object> = [
            /*{
                label: 1111,
                name: '1'
            }*/
        ];
        // tree搜索值;
        searchModelValue: string = '';

        /**
         * 子分类创建
         */
        categoryVisible = false;
        categoryForm: object = {
            id: undefined,
            parentId: '',
            nameCn: '',
            nameEn: '',
            resourceCode: '',
            resourceId: '',  // 资源编号
            sourceId: '',
            category: 1, // 类别：1 分类  2 数据集  9 其他
            description: ''
        };
        categoryFormRules = {
            nameCn: [{required: true, message: ' ', trigger: 'change'}],
            nameEn: [{required: true, message: ' ', trigger: 'change'}],
            // resourceCode: [{required: true, message: ' ', trigger: 'change'}],
            // resourceId: [{required: true, message: ' ', trigger: 'change'}],
            // sourceId: [{required: true, message: ' ', trigger: 'change'}],
            category: [{required: true, message: ' ', trigger: 'change'}],
        };

        /**
         * 数据元
         */
        elementVisible = false;
        transferData = [];
        transferValue = [];
        cacheTransferValue = [];
        transferSearch = ''; // 搜索内容
        treeCurrentData = null; // 缓存当前操作tree树的node节点数据


        /**
         * 数据集引用数据元
         */
        dataMetaHeaderData = [
            {
                prop: 'resourceCode',
                label: '资源编码',
                width: '100px',
            },
            {
                prop: 'category',
                label: '类别',
                width: '200px',
            },
            {
                prop: 'nameCn',
                label: '中文名称',
                width: '200px'
            },
            {
                prop: 'nameEn',
                label: '英文名称',
                width: '100px'
            },
            {
                prop: 'definition',
                label: '定义',
                width: '200px'
            },
            {
                prop: 'dataType',
                label: '数据类型'
            },
            {
                prop: 'showFormat',
                label: '表示格式'
            },
            {
                prop: 'allowableType', // 0  不可枚举型  1 可枚举型（一般引用值域）
                label: '允许值类型',
                width: '100px',
                type: 'tag',
                translations: {
                    0: '不可枚举型',
                    1: '可枚举型'
                },
                styles: {
                    0: 'default',
                    1: 'success'
                }
            },
            {
                prop: 'allowableCode',
                label: '值域编码'
            },
            {
                prop: 'sourceId',
                label: '来源标识'
            },
        ];
        dataMetaTableData = [];
        dataMetaPaginationData = {
            currentPage: 1,
            pageSize: 10,
            total: 1
        };

        /**
         * 编辑创建数据集步伐
         */
        step = 1;


        // 监听删选框数据变化
        @Watch('searchModelValue')
        onChangeSearchModelValue(val: string, oldVal: string) {
            this.myThis.$refs.tree.filter(val);
        }

        // 监听分类创建修改
        @Watch('categoryVisible')
        onChangeCategoryVisible(val) {
            if (val) return;
            this.categoryForm = {
                id: '',
                parentId: '',
                nameCn: '',
                nameEn: '',
                resourceCode: '',
                resourceId: '',  // 资源编号
                sourceId: '',
                category: '', // 类别：1 分类  2 数据集  9 其他
                description: ''
            };
            this.$refs.categoryFormRef['resetFields']();
        }

        // 监听数据集创建修改
        @Watch('elementVisible')
        onChangeElementVisible(val) {
            if (val) return;
            this.resetElement();
        }

        @Watch('transferValue')
        onChangeTransferValue() {
            this.cacheTransferValue = [];
            this.transferValue.forEach((id) => {
                let transferItem = this.transferData.find(item => item.id === id);
                if (transferItem !== undefined) {
                    this.cacheTransferValue.push(transferItem);
                }
            });
        }

        mounted() {

            this.getTreeData();
            if (isIE()) {
                this.$message.warning('您使用的是IE浏览器，为了更好的访问体验，请使用谷歌、火狐浏览器')
            }
        }

        // 获取tree树模型数据集合
        getTreeData(): void {
            let url = config.port('businessView') + 'tree';
            this.$http.get(url).then(response => {
                const res = response.data;
                if (res.code === 0) {
                    this.treeData = res.data;
                }
            });
        }


        // tree节点被点击时的回调
        async handleNodeClick(data, node) {
            // 限制20个tabs
            let dataId = data.id.toString();
            if (this.tabsAllData.length >= 20) {
                // 查看tab是否已经存在tabsAllData中
                const repetition = this.tabsAllData.find(item => item['id'] === dataId);
                if (repetition !== undefined) {
                    this.tabsActiveId = dataId
                } else {
                    this.$message.warning('tabs标签不能大于20个')
                }
                return
            } else {
                this.tabsActiveId = dataId
            }
            // 禁止出现第二个相同的tabs
            for (let i = 0; i < this.tabsAllData.length; i++) {
                this.tabsActiveId = dataId;
                if (this.tabsAllData[i]['id'] === dataId) return
            }
            this.tabsAllData.map(item => item['activateFlag'] = false);
            // 给新加入到tab中的成员添加一个标记active  代表被激活 ，因为bloodTab组件需要用这个来刷新svg(解决非激活的tab svg被刷新的问题)
            data.activateFlag = true;
            // label tab展示的名称 name 是tab的标识
            data['label'] = data.nameCn || data.nameEn || 'unknown';
            data['name'] = dataId;
            this.tabsAllData.push(data);
            this.tabsActiveId = dataId;
        }

        // tree节点过滤
        treeFilterNode(value, data) {
            if (!value) return true;
            return data.name.indexOf(value) !== -1;
        }

        /**
         * 提交分类
         */
        submitCategory() {
            this.$refs.categoryFormRef['validate']((valid) => {
                if (valid) {
                    let url = config.port('businessView') + 'saveOrUpdate';
                    this.categoryForm['category'] = '1';
                    let params = {...this.categoryForm};
                    this.$http.put(url, params).then(response => {
                        const res = response.data;
                        if (res.code === 0) {
                            this.$message.success('提交成功');
                            this.getTreeData();
                            this.categoryVisible = false;
                        }
                    });
                } else {
                    return false;
                }
            });
        }

        /**
         * 点击树节点上的操作
         * @param type
         * @param data
         * @param node
         */
        operationClick(type, data, node) {
            this.treeCurrentData = data;
            if (type === 'addCategory') {
                this.categoryForm['parentId'] = data['id'];
                this.categoryVisible = true;
            } else if (type === 'editCategory') {
                this.getInfo(data['id']);
            } else if (type === 'addDataset') {
                this.elementVisible = true;
                this.categoryForm['parentId'] = data['id'];
            } else if (type === 'editDataset') {
                this.editElementGetData(data, node)
            }
        }

        /**
         * 获取分类数据
         */
        getInfo(id) {
            let url = config.port('businessView') + 'categoryInfo/' + id;
            this.$http.get(url).then(response => {
                const res = response.data;
                if (res.code === 0) {
                    this.categoryForm = res.data[0];
                    this.categoryVisible = true;
                }
            })
        }

        /**
         * 获取数据元
         */
        getElementList() {
            let params = {
                queryString: this.transferSearch,
                type: 'element',
            };
            let url = config.port('businessView') + 'getElementList';
            this.$http.get(url, {params}).then(response => {
                const res = response.data;
                if (res.code === 0) {
                    this.transferData = [];
                    res.data.forEach(item => {
                        if (!this.transferValue.find(id => id === item.id)) {
                            this.transferData.push(item)
                        }
                    });
                    this.transferData = [...this.cacheTransferValue, ...this.transferData];
                }
            })
        }

        /**
         * 展示数据集数据
         */
        getDataMetaTableData() {
            this.dataMetaPaginationData.total = this.cacheTransferValue.length;
            this.dataMetaTableData = this.cacheTransferValue['splice'](this.dataMetaPaginationData.currentPage - 1, this.dataMetaPaginationData.pageSize);
            this.cacheTransferValue['splice'](this.dataMetaPaginationData.currentPage - 1, 0, ...this.dataMetaTableData);
        }

        /**
         * 下一步
         */
        nextStep() {
            // 校验form表单
            if (this.step === 1) {
                this.$refs.elementFormRef['validate']((valid) => {
                    if (valid) {
                        this.step++;
                    } else {
                        return false;
                    }
                })
            } else if (this.step > 0 && this.step < 3) {
                this.step++;
                this.getDataMetaTableData();
            }
        }

        /**
         * 提交创建修改数据集
         */
        submitElement() {
            this.categoryForm['category'] = '2';
            let params = {
                categoryId: this.treeCurrentData.id,
                dataset: this.categoryForm,
                elementList: this.transferValue
            };
            let url = config.port('businessView') + 'saveDataSetAndRef';
            this.$http.put(url, params).then(response => {
                const res = response.data;
                if (res.code === 0) {
                    this.$message.success('提交成功');
                    this.getTreeData();
                    this.resetElement();
                }
            });
        }

        /**
         * 初始化创建修改数据集有的数据
         */
        resetElement() {
            this.step = 1;
            this.dataMetaPaginationData = {
                currentPage: 1,
                pageSize: 10,
                total: 1
            };
            this.dataMetaTableData = [];
            this.treeCurrentData = null;
            this.elementVisible = false;

            this.transferData = [];
            this.transferValue = [];
            this.cacheTransferValue = [];
            this.transferSearch = ''; // 搜索内容
            this.categoryForm = {
                id: '',
                parentId: '',
                nameCn: '',
                nameEn: '',
                resourceCode: '',
                resourceId: '',  // 资源编号
                sourceId: '',
                category: '', // 类别：1 分类  2 数据集  9 其他
                description: ''
            };
            this.$refs.elementFormRef && this.$refs.elementFormRef['resetFields']();
        }

        /**
         * 编辑数据集获取数据
         */
        async editElementGetData(data, node) {
            let category = await this.getCategory(data.id);
            let element = await this.getElementData(data.id);
            this.elementVisible = true;

        }

        /**
         * 获取分类详情
         */
        async getCategory(id) {
            let url = config.port('businessView') + 'categoryInfo/' + id;
            this.$http.get(url).then(response => {
                const res = response.data;
                if (res.code === 0) {
                    this.categoryForm = res.data[0];
                    return true;
                }
            })
        }


        /**
         * 获取 数据集引用数据元
         */
        async getElementData(id) {
            let params = {
                size: -1,
                id
            };
            let url = config.port('businessView') + '/elementList';
            await this.$http.get(url, {params}).then(response => {
                const res = response.data;
                if (res.code === 0) {
                    const data = res.data;
                    this.cacheTransferValue = data.records;
                    this.transferData = data.records;
                    this.cacheTransferValue.forEach(item => {
                        this.transferValue.push(item.id);
                    });
                    return true;
                }

            })
        }

    }
</script>

<style lang="less">

    .business-view {
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


        .tab-container-wrapper {
            width: 100%;
            height: 100%;
        }

        .el-dialog__body {
            display: flex;
            flex-direction: column;
            align-items: center;

            .el-form {
                width: 576px;
            }

            .search {
                width: 500px;
                margin-bottom: 10px;

                .el-input {
                    width: 200px;
                }
            }


            .el-steps {
                display: flex;
                justify-content: center;
                align-items: center;
                width: 500px;
            }

            .el-transfer {
                display: inline-block;
            }

            .el-transfer-panel__body {
                height: 300px;
            }

            .el-transfer-panel__list {
                height: 100%;
            }
        }

        .elementInfo {
            width: 576px;
            display: flex;
            flex-direction: column;
            overflow: auto;

            .title {
                display: flex;
                align-items: center;
                line-height: 30px;
                background-color: #fff;
                font-size: 14px;
                font-weight: 700;
                padding-left: 10px;
                border-bottom: 1px dashed #ccc;
            }

            .info {
                display: flex;
                font-size: 14px;
                flex-wrap: wrap;
                padding: 10px;
                background-color: #fff;

                span {
                    overflow: hidden; //超出的文本隐藏
                    text-overflow: ellipsis; //溢出用省略号显示
                    white-space: nowrap; //溢出不换行
                    width: 90px;
                    font-size: 12px;
                    margin-bottom: 5px;
                }

                .label {
                    display: inline-block;
                    text-align: right;
                }

                .value {
                    display: inline-block;
                }
            }

            .table-wrapper {
                margin-top: 10px;
                height: 200px;
                background-color: red;
            }
        }
    }
</style>
