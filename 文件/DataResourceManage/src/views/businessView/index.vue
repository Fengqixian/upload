<template>
    <div class="business-view cb-tree-show-operation-wrapper">
        <div class="tree-wrapper">
            <cb-tree
                    :data="treeData"
                    nodeKey="id"
                    :default-expanded-keys="defaultExpandedKeys"
                    ref="tree">
                <template slot="searchAppend">
                    <el-tooltip effect="dark" content="新增分类" placement="top-start">
                        <el-button type="text"
                                   icon="el-icon-plus"
                                   class="add-text"
                                   style="margin-left: 5px;"
                                   @click.prevent.stop="operationClick('addCategory',{id:''},null)">
                        </el-button>
                    </el-tooltip>
                </template>
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
                                <el-tooltip effect="dark" content="新增子类" placement="top-start">
                                    <el-button type="text"
                                               v-if="node.level <= 3"
                                               icon="el-icon-plus"
                                               class="add-text"
                                               @click.prevent.stop="operationClick('addCategory',data,node)">
                                    </el-button>
                                </el-tooltip>
                                <el-tooltip effect="dark" content="新增数据集" placement="top-start">
                                    <el-button type="text"
                                               icon="el-icon-plus"
                                               class="add-text"
                                               @click.prevent.stop="operationClick('addDataset',data,node)">
                                    </el-button>
                                </el-tooltip>
                            </template>
                            <template v-else>
                                <el-tooltip effect="dark" content="编辑" placement="top-start">
                                    <el-button type="text"
                                               class="edit-text"
                                               icon="el-icon-edit"
                                               @click.prevent.stop="operationClick('editDataset',data,node)">
                                    </el-button>
                                </el-tooltip>
                                <!--<el-tooltip effect="dark" content="新增数据元" placement="top-start">
                                    <el-button type="text"
                                               icon="el-icon-plus"
                                               class="add-text"
                                               @click.prevent.stop="operationClick('addElementSet',data,node)">
                                    </el-button>
                                </el-tooltip>-->
                            </template>
                        </div>
                    </div>
                </template>
            </cb-tree>
        </div>
        <div class="tab-wrapper show-operation-wrapper">
            <cascade-flow :breadcrumb="breadcrumb"
                          v-if="breadcrumb.length"
                          @edit-click="editElementSetClick"/>
        </div>

        <el-dialog title="分类"
                   :close-on-click-modal="false"
                   :visible.sync="categoryVisible">
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
                <el-form-item label="来源标识" prop="sourceId">
                    <el-input v-model.trim="categoryForm.sourceId" placeholder="来源标识"></el-input>
                </el-form-item>
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

        <el-dialog title="数据集"
                   :visible.sync="elementVisible"
                   :close-on-click-modal="false">
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
                    <el-form-item label="来源标识" prop="sourceId">
                        <el-input v-model.trim="categoryForm.sourceId" placeholder="来源标识"></el-input>
                    </el-form-item>
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
                <el-transfer v-model="transferValue"
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
                </div>
            </div>
        </el-dialog>


        <el-dialog title="数据元"
                   :visible.sync="elementSetVisible"
                   :close-on-click-modal="false">
            <el-steps :space="200"
                      :active="elementSetStep-1"
                      finish-status="success"
                      align-center
                      style="margin: 10px 0;">
                <el-step title="详情"></el-step>
                <el-step title="字段"></el-step>
                <el-step title="预览"></el-step>
            </el-steps>

            <!--第一步-->
            <template v-if="elementSetStep === 1">
                <el-form :model="elementSetForm"
                         :rules="elementSetFormRules"
                         label-width="100px"
                         ref="elementSetFormRef">
                    <el-form-item label="中文名称" prop="nameCn">
                        <el-input v-model.trim="elementSetForm.nameCn" placeholder="中文名称"></el-input>
                    </el-form-item>
                    <el-form-item label="英文名称" prop="nameEn">
                        <el-input v-model.trim="elementSetForm.nameEn" placeholder="英文名称"></el-input>
                    </el-form-item>
                    <el-form-item label="来源标识" prop="sourceId">
                        <el-input v-model.trim="elementSetForm.sourceId" placeholder="来源标识"></el-input>
                    </el-form-item>
                    <el-form-item label="数据元类别" prop="category">
                        <el-input v-model.trim="elementSetForm.category" placeholder="数据元类别"></el-input>
                    </el-form-item>
                    <el-form-item label="定义" prop="definition">
                        <el-input v-model.trim="elementSetForm.definition" placeholder="定义"></el-input>
                    </el-form-item>
                    <el-form-item label="数据类型" prop="dataType">
                        <el-select v-model="elementSetForm.dataType"
                                   clearable
                                   placeholder="请选择数据类型">
                            <el-option label="图片型" value="BY"/>
                            <el-option label="日期型" value="D"/>
                            <el-option label="时间型" value="T"/>
                            <el-option label="日期时间型" value="DT"/>
                            <el-option label="布尔型" value="L"/>
                            <el-option label="数值型" value="N"/>
                            <el-option label="字符串1型" value="S1"/>
                            <el-option label="字符串2型" value="S2"/>
                            <el-option label="字符串3型" value="S3"/>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="表示格式" prop="showFormat">
                        <el-input v-model.trim="elementSetForm.showFormat" placeholder="表示格式"></el-input>
                    </el-form-item>
                    <el-form-item label="允许值" prop="allowableValue">
                        <el-input v-model.trim="elementSetForm.allowableValue" placeholder="允许值"></el-input>
                    </el-form-item>
                    <el-form-item label="值域编码" prop="allowableCode">
                        <el-input v-model.trim="elementSetForm.allowableCode" placeholder="值域编码"></el-input>
                    </el-form-item>
                    <el-form-item label="允许值类型" prop="allowableType">
                        <el-select v-model="elementSetForm.allowableType" placeholder="请选择允许值类型">
                            <el-option label="不可枚举型" :value="0"/>
                            <el-option label="可枚举型" :value="1"/>
                        </el-select>
                    </el-form-item>
                </el-form>
            </template>


            <!--第二步-->
            <template v-if="elementSetStep === 2">
                <div class="search">
                    <el-input placeholder="请输入内容" v-model.trim="columnTransferSearch"
                              @keydown.native.enter.stop.prevent="getColumnList()">
                        <el-button slot="append" icon="el-icon-search" @click="getColumnList()"></el-button>
                    </el-input>
                </div>
                <el-transfer v-model="columnTransferValue"
                             :titles="['待添加', '已添加']"
                             :props="{
                                  key: 'id',
                                  label: 'nameCn'
                                }"
                             :data="columnTransferData">
                    <template slot-scope="{option}">
                        <el-tooltip class="item" effect="dark"
                                    :content="`${option.columnNameCn || option.columnNameEn}
                                → ${option.tableNameCn || option.tableNameEn}
                                → ${option.databaseNameCn || option.databaseNameEn}`"
                                    placement="top-start">
                            <span>
                                {{option.columnNameCn || option.columnNameEn}}
                                → {{option.tableNameCn || option.tableNameEn}}
                                → {{option.databaseNameCn || option.databaseNameEn}}
                            </span>
                        </el-tooltip>
                    </template>
                </el-transfer>
            </template>


            <!--第三步-->
            <template v-if="elementSetStep === 3">
                <div class="elementInfo">
                    <div class="title">详情：</div>
                    <ul class="info">
                        <li>
                            <span class="label">中文名称：</span>
                            <span class="value">{{elementSetForm.nameCn}}</span>
                        </li>
                        <li>
                            <span class="label">英文名称：</span>
                            <span class="value">{{elementSetForm.nameEn}}</span>
                        </li>
                        <li>
                            <span class="label">来源标识：</span>
                            <span class="value">{{elementSetForm.sourceId}}</span>
                        </li>
                        <li>
                            <span class="label">数据元类别：</span>
                            <span class="value">{{elementSetForm.category}}</span>
                        </li>
                        <li>
                            <span class="label">定义：</span>
                            <span class="value">{{elementSetForm.definition}}</span>
                        </li>
                        <li>
                            <span class="label">数据类型：</span>
                            <span class="value" v-if="elementSetForm.dataType === 'BY'">图片型</span>
                            <span class="value" v-else-if="elementSetForm.dataType === 'D'">日期型</span>
                            <span class="value" v-else-if="elementSetForm.dataType === 'T'">时间型</span>
                            <span class="value" v-else-if="elementSetForm.dataType === 'DT'">日期时间型</span>
                            <span class="value" v-else-if="elementSetForm.dataType === 'L'">布尔型</span>
                            <span class="value" v-else-if="elementSetForm.dataType === 'N'">数值型</span>
                            <span class="value" v-else-if="elementSetForm.dataType === 'N'">数值型</span>
                            <span class="value" v-else-if="elementSetForm.dataType === 'S1'">字符串1型</span>
                            <span class="value" v-else-if="elementSetForm.dataType === 'S2'">字符串2型</span>
                            <span class="value" v-else-if="elementSetForm.dataType === 'S3'">字符串3型</span>
                        </li>
                        <li>
                            <span class="label">表示格式：</span>
                            <span class="value">{{elementSetForm.showFormat}}</span>
                        </li>
                        <li>
                            <span class="label">允许值：</span>
                            <span class="value">{{elementSetForm.allowableValue}}</span>
                        </li>
                        <li>
                            <span class="label">值域编码：</span>
                            <span class="value">{{elementSetForm.allowableCode}}</span>
                        </li>
                        <li>
                            <span class="label">允许值类型：</span>
                            <span class="value" v-if="elementSetForm.allowableType === '1'">可枚举型</span>
                            <span class="value" v-else-if="elementSetForm.allowableType === '0'">不可枚举型</span>
                        </li>
                    </ul>
                </div>

                <div class="elementInfo">
                    <div class="title">字段信息：</div>
                    <div class="table-wrapper">
                        <cb-table :header-data="columnHeaderData"
                                  :table-data="columnTableData"
                                  :pagination-data="columnPaginationData"
                                  @size-change="getColumnTableData"
                                  @current-change="getColumnTableData"
                                  paginationLayout="total,prev, pager, next"
                                  ref="dataMetaTable">
                        </cb-table>
                    </div>
                </div>
            </template>
            <div slot="footer">
                <div class="btn-right">
                    <el-button class="del-nobg" @click="elementSetVisible = false">取 消</el-button>
                    <el-button class="add-bg" v-if="elementSetStep > 1"
                               @click="(elementSetStep>0)?elementSetStep--:null">上一步
                    </el-button>
                    <el-button class="confirm-bg" v-if="elementSetStep < 3" @click="elementSetNextStep">下一步</el-button>
                    <el-button class="confirm-bg" v-if="elementSetStep === 3" @click="submitElementSet">确 定</el-button>
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
    import CascadeFlow from './cascadeFlow'
    import Relation from './relation.vue'

    @Component({
        components: {BusinessTab, CascadeFlow, Relation}
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


        /**
         * 展示数据元
         */
        breadcrumb = [];


        /**
         * 新增 编辑 数据元
         */
        elementSetVisible = false;
        cacheNodeClickData = {}; // 缓存点击树数据
        elementSetStep = 1;
        elementSetForm = {
            sourceId: '', // 来源标识 来源国际编码
            category: '', // 数据元类别
            nameCn: '', // 数据元名称
            nameEn: '', // 数据元名称
            definition: '', // 定义
            dataType: '', // 数据类型  数据元的取值类型
            showFormat: '', // 表示格式
            allowableValue: ' ', // 允许值
            allowableType: null, // 允许值类型 0 不可枚举型  1 可枚举型
            allowableCode: ' ', // 值域编码 引用的值域编码

        };
        elementSetFormRules = {
            /*nameCn: [{required: true, message: ' ', trigger: 'change'}],
            nameEn: [{required: true, message: ' ', trigger: 'change'}],
            category: [{required: true, message: ' ', trigger: 'change'}],*/
        };
        columnTransferSearch = '';
        columnTransferData = [];
        columnTransferValue = [];
        cacheColumnTransferValue = [];
        columnHeaderData = [
            {
                prop: 'columnNameCn',
                label: '字段中文名',
                width: '100px',
            },
            {
                prop: 'columnNameEn',
                label: '字段英文名',
                width: '100px',
            },
            {
                prop: 'tableNameCn',
                label: '表中文名',
                width: '100px',
            },
            {
                prop: 'tableNameEn',
                label: '表英文名',
                width: '100px',
            },
            {
                prop: 'databaseNameCn',
                label: '表中文名',
                width: '100px',
            },
            {
                prop: 'databaseNameEn',
                label: '表英文名',
                width: '100px',
            },
        ];
        columnTableData = [];
        columnPaginationData = {
            currentPage: 1,
            pageSize: 10,
            total: 1
        };


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

        @Watch('columnTransferValue')
        onChangeColumnTransferValue(newVal) {
            console.log(newVal);
            this.cacheColumnTransferValue = [];
            this.columnTransferValue.forEach((id) => {
                let transferItem = this.columnTransferData.find(item => item.id === id);
                if (transferItem !== undefined) {
                    this.cacheColumnTransferValue.push(transferItem);
                }
            });
        }

        // 监听数据集创建修改
        @Watch('elementSetVisible')
        onChangeElementSetVisible(val) {
            if (val) return;
            this.resetElementSet();
        }

        mounted() {

            this.getTreeData();
            if (isIE()) {
                this.$message.warning('您使用的是IE浏览器，为了更好的访问体验，请使用谷歌、火狐浏览器')
            }
        }

        /**
         * 数据元新增修改下一步
         */
        elementSetNextStep() {
            // 校验form表单
            if (this.elementSetStep === 1) {
                this.$refs.elementSetFormRef['validate']((valid) => {
                    if (valid) {
                        this.elementSetStep++;
                    } else {
                        return false;
                    }
                })
            } else if (this.elementSetStep > 0 && this.elementSetStep < 3) {
                this.elementSetStep++;
                this.getColumnTableData();
            }
        }


        /**
         * 获取字段
         */
        async getColumnList(id?) {
            if (this.columnTransferSearch === '' && id === undefined) {
                this.$message.warning('搜索内容不能为空');
                return;
            }
            let params = {
                queryString: this.columnTransferSearch,
                size: -1,
                elementId: id !== undefined ? id : ''
            };
            let url = config.port('businessView') + 'columnList';
            await this.$http.get(url, {params}).then(response => {
                const res = response.data;
                if (res.code === 0) {
                    this.columnTransferData = [];
                    const data = res.data.records;
                    // 存在id 代表编辑时候获取已经关联的字段
                    if (id !== undefined) {
                        this.cacheColumnTransferValue = data;
                        data.forEach(item => {
                            this.columnTransferValue.push(item.id);
                        });
                    } else {
                        data.forEach(item => {
                            if (!this.columnTransferValue.find(id => id === item.id)) {
                                this.columnTransferData.push(item)
                            }
                        });
                    }
                    this.columnTransferData = [...this.cacheColumnTransferValue, ...this.columnTransferData];
                }
            })
        }


        /**
         * 展示table字段数据
         */
        getColumnTableData() {
            this.columnPaginationData.total = this.cacheColumnTransferValue.length;
            this.columnTableData = this.cacheColumnTransferValue['slice']((this.columnPaginationData.currentPage - 1) * this.columnPaginationData.pageSize, (this.columnPaginationData.currentPage) * this.columnPaginationData.pageSize);
        }

        /**
         * 提交 新增 编辑数据元
         */
        submitElementSet() {
            let url = config.port("businessView") + "/saveElement";
            let data = {
                element: this.elementSetForm,
                refList: [],
                datasetId: this.cacheNodeClickData['data'].id
            };
            this.cacheColumnTransferValue.forEach(item => {
                data.refList.push({
                    elementId: '',
                    columnId: item.id,
                })
            });
            this.$http({
                method: 'put',
                url,
                data,
            }).then(response => {
                if (response.data.code === 0) {
                    this.elementSetVisible = true;
                    this.$message.success("操作成功");
                    this.handleNodeClick(this.cacheNodeClickData['data'], this.cacheNodeClickData['node']);
                    this.resetElementSet();
                }
            });
        }


        /**
         * 初始化创建修改数据集有的数据
         */
        resetElementSet() {
            this.elementSetStep = 1;
            this.columnPaginationData = {
                currentPage: 1,
                pageSize: 10,
                total: 1
            };
            this.columnTableData = [];
            this.treeCurrentData = null;
            this.elementSetVisible = false;

            this.columnTransferData = [];
            this.columnTransferValue = [];
            this.cacheTransferValue = [];
            this.columnTransferSearch = ''; // 搜索内容
            this.elementSetForm = {
                sourceId: '', // 来源标识 来源国际编码
                category: '', // 数据元类别
                nameCn: '', // 数据元名称
                nameEn: '', // 数据元名称
                definition: '', // 定义
                dataType: '', // 数据类型  数据元的取值类型
                showFormat: '', // 表示格式
                allowableValue: '', // 允许值
                allowableType: null, // 允许值类型 0 不可枚举型  1 可枚举型
                allowableCode: ' ', // 值域编码 引用的值域编码

            };
            this.$refs.elementSetFormRef && this.$refs.elementSetFormRef['resetFields']();
        }

        /**
         * 编辑
         * @param data
         */
        async editElementSetClick(data) {
            await this.getElementSetInfo(data.id);
            await this.getColumnList(data.id);
            this.elementSetVisible = true;
        }

        /**
         * 获取数据元信息
         */
        async getElementSetInfo(id) {
            let url = config.port('businessView') + '/elementInfo';
            let params = {
                id,
                current: 1,
                size: 1000,
            };
            await this.$http.get(url, {params}).then(response => {
                if (response.data.code === 0) {
                    this.elementSetForm = response.data.data;
                }
            })
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
            this.breadcrumb = [];
            this.getParentData(node);

            // 缓存被点击的树节点
            this.cacheNodeClickData = {data, node};
        }

        getParentData(node,) {
            if (node.level >= 1) {
                this.breadcrumb.unshift(node.data);
                this.getParentData(node.parent)
            }
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
            } else if (type === 'addElementSet') {
                this.elementSetVisible = true;
                this.cacheNodeClickData = {data, node};
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
                    this.categoryForm = res.data;
                    this.categoryVisible = true;
                }
            })
        }

        /**
         * 获取数据元
         */
        getElementList() {
            if (this.transferSearch === '') {
                this.$message.warning('搜索内容不能为空');
                return;
            }
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
            this.dataMetaTableData = this.cacheTransferValue['slice']((this.dataMetaPaginationData.currentPage - 1) * this.dataMetaPaginationData.pageSize, (this.dataMetaPaginationData.currentPage) * this.dataMetaPaginationData.pageSize);
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
                    this.categoryForm = res.data;
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
                id,
                category: 'dataset'
            };
            let url = config.port('businessView') + 'elementList';
            await this.$http.get(url, {params}).then(response => {
                const res = response.data;
                if (res.code === 0) {
                    const data = res.data;
                    if (data) {
                        this.cacheTransferValue = data.records;
                        this.transferData = data.records;
                        this.cacheTransferValue.forEach(item => {
                            this.transferValue.push(item.id);
                        });
                    }
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

        .el-dialog {
            width: 680px;
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
