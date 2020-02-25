<template>
    <div class="constant">
        <div class="classification">
            <div class="title">常量分类</div>
            <div class="operation">
                <div class="btn">
                    <el-button class="add-text" type="text" @click="classificationDialogVisible=true">新 增</el-button>
                </div>
                <el-input placeholder="请输入常量名称" style="width:300px;" v-model.trim="searchClassificationVal"
                          @keydown.native.enter="getClassificationTableData('search')">
                    <el-button slot="append" icon="el-icon-search"
                               @click.stop.prevent="getClassificationTableData('search')"></el-button>
                </el-input>
            </div>
            <div class="table-wrapper">
                <cb-table :header-data="classificationHeaderData"
                          :table-data="classificationTableData"
                          :pagination-data="classificationPaginationData"
                          @size-change="getClassificationTableData()"
                          @current-change="getClassificationTableData()"
                          @table-current-change="handleTableCurrentChange"
                          :highlight-current-row="true"
                          ref="classificationTable">
                    <template slot="column">
                        <el-table-column
                                label="操作"
                                width="100">
                            <template slot-scope="{row}">
                                <el-tooltip content="编辑" placement="top" effect="dark"
                                            v-if="row.id !== 16 && row.id !== 17 && row.id !== 18 && row.id !== 19 && row.id !== 22">
                                    <el-button type="text"
                                               class="edit-text"
                                               icon="iconfont icon-bianji1"
                                               @click="edit('dict',row)"></el-button>
                                </el-tooltip>
                                <el-tooltip content="删除" placement="top" effect="dark"
                                            v-if="row.id !== 16 && row.id !== 17 && row.id !== 18 && row.id !== 19 && row.id !== 22">
                                    <el-button type="text"
                                               class="del-text"
                                               icon="iconfont icon-shanchu1"
                                               @click="del('dict',row)"></el-button>
                                </el-tooltip>
                            </template>
                        </el-table-column>
                    </template>
                </cb-table>
            </div>
        </div>
        <div class="detailed">
            <div class="title">常量明细</div>
            <div class="operation">
                <div class="btn">
                    <el-button class="add-text" type="text" @click="detailedDialogVisible=true">新 增</el-button>
                </div>
                <el-input placeholder="请输入常量值标识 / 常量代码" style="width:300px;" v-model.trim="searchDetailedVal"
                          @keydown.native.enter="getDetailedData('search')">
                    <el-button slot="append" icon="el-icon-search"
                               @click.stop.prevent="getDetailedData('search')"></el-button>
                </el-input>
            </div>
            <div class="table-wrapper">
                <cb-table :header-data="detailedHeaderData"
                          :table-data="detailedTableData"
                          :pagination-data="detailedPaginationData"
                          @size-change="getDetailedData()"
                          @current-change="getDetailedData()">
                    <template slot="column">
                        <el-table-column
                                label="操作"
                                width="100">
                            <template slot-scope="{row}">
                                <el-tooltip content="编辑" placement="top" effect="dark">
                                    <el-button type="text"
                                               class="edit-text"
                                               icon="iconfont icon-bianji1"
                                               @click="edit('items',row)"></el-button>
                                </el-tooltip>
                                <el-tooltip content="删除"
                                            placement="top"
                                            effect="dark">
                                    <el-button type="text"
                                               class="del-text"
                                               icon="iconfont icon-shanchu1"
                                               @click="del('items',row)"></el-button>
                                </el-tooltip>
                            </template>
                        </el-table-column>
                    </template>
                </cb-table>
            </div>
        </div>


        <!--弹出框区域-->
        <el-dialog :close-on-click-modal="false"
                   title="常量分类"
                   :visible.sync="classificationDialogVisible"
                   width="30%">
            <el-form ref="classificationFormRef"
                     :model="classificationForm"
                     label-width="120px"
                     :rules="classificationFormRules">
                <el-input type="hidden" v-model.trim="classificationForm.id"></el-input>
                <el-form-item label="常量名称" prop="dictName">
                    <el-input v-model.trim="classificationForm.dictName" placeholder="请输入常量名称"></el-input>
                </el-form-item>
                <el-form-item label="常量类别" prop="dictType">
                    <el-select filterable v-model.trim="classificationForm.dictType" placeholder="请选择常量类别">
                        <el-option v-for="item in typeSelections" :key="item.itemsCode" :value="item.itemsCode"
                                   :label="item.itemsName"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="获取常量命令" prop="sqlCommand" v-if="classificationForm.dictType === '2'">
                    <el-input type="textarea" v-model.trim="classificationForm.sqlCommand"
                              placeholder="请输入获取常量命令"></el-input>
                </el-form-item>
                <el-form-item label="显示顺序" prop="showOrder">
                    <el-input type="number" min="0" v-model.trim="classificationForm.showOrder"
                              placeholder="显示顺序"></el-input>
                </el-form-item>
                <el-form-item label="常量描述" prop="description">
                    <el-input type="textarea" v-model.trim="classificationForm.description"
                              placeholder="请输入常量描述"></el-input>
                </el-form-item>
                <el-form-item>
                    <div class="btn-right">
                        <el-button class="del-nobg" @click="classificationDialogVisible = false">取 消</el-button>
                        <el-button class="confirm-bg" @click="submitClassificationForm()">确 定</el-button>
                    </div>
                </el-form-item>
            </el-form>
        </el-dialog>

        <!--===============================常量值弹框-->
        <el-dialog title="常量明细"
                   :visible.sync="detailedDialogVisible"
                   :close-on-click-modal="false"
                   width="40%">
            <el-form ref="detailedFormRef"
                     :model="detailedForm"
                     label-width="120px"
                     :rules="detailedFormRules">
                <el-form-item label="常量标识" prop="itemsName">
                    <el-input v-model.trim="detailedForm.itemsName" placeholder="请输入常量标识"></el-input>
                </el-form-item>
                <el-form-item label="常量代码" prop="itemsCode">
                    <el-input v-model.trim="detailedForm.itemsCode" placeholder="请输入常量代码"></el-input>
                </el-form-item>
                <el-form-item label="显示顺序" prop="showOrder">
                    <el-input type="number" min="0" v-model.trim="detailedForm.showOrder"
                              placeholder="显示顺序"></el-input>
                </el-form-item>
                <el-form-item label="描述" prop="description">
                    <el-input type="textarea" v-model.trim="detailedForm.description" placeholder="请输入描述"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
          <el-button class="del-nobg" @click="detailedDialogVisible = false">取 消</el-button>
          <el-button class="confirm-bg" @click="submitDetailedForm()">确 定</el-button>
        </span>
        </el-dialog>
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component, Watch} from 'vue-property-decorator'
    import {Action} from 'vuex-class'
    import config from "../../config";

    @Component({})
    export default class Constant extends Vue {
        name: string = 'Constant';
        @Action getBaseFiledOptions; // 获取基础字段中options;
        /**
         * 常量分类变量
         */
        classificationHeaderData: Array<object> = [
            {
                label: '常量名称',
                prop: 'dictName'
            },
            {
                label: '常量类别',
                prop: 'dictType',
                type: 'select',
                options: []
            },
            {
                label: '获取常量命令',
                prop: 'sqlCommand'
            },
            {
                label: '显示顺序',
                prop: 'showOrder'
            },
            {
                label: '常量描述',
                prop: 'description'
            },
        ];
        classificationTableData: Array<object> = [];
        classificationPaginationData: object = {
            total: 1,
            pageSize: 10,
            currentPage: 1,
        };
        // 跟库常量名称搜索的变量
        searchClassificationVal: string = '';
        // 常量分类缓存被选中的
        classificationCurrentRow: object = {};
        // 常量类别
        typeSelections: Array<object> = [];
        /**
         * 常量明细变量
         */
        detailedHeaderData: Array<object> = [
            {
                label: '常量ID',
                prop: 'dictId'
            },
            {
                label: '常量值标识',
                prop: 'itemsName'
            },
            {
                label: '常量代码',
                prop: 'itemsCode'
            },
            {
                label: '显示顺序',
                prop: 'showOrder'
            },
            {
                label: '描述',
                prop: 'description'
            },
        ];
        detailedTableData: Array<object> = [];
        detailedPaginationData: object = {
            total: 1,
            pageSize: 10,
            currentPage: 1,
        };
        searchDetailedVal: string = '';


        /**
         * 弹框区域
         */
        classificationDialogVisible: boolean = false;
        detailedDialogVisible: boolean = false;
        classificationForm: object = {
            id: '',
            dictName: '',
            sqlCommand: '',
            dictType: '',
            showOrder: '',
            description: ''
        };
        detailedForm: object = {
            id: '',
            dictId: '',
            itemsName: '',
            itemsCode: '',
            showOrder: '',
            description: ''
        };
        classificationFormRules: object = {
            dictName: [
                {required: true, message: ' ', trigger: 'change'}
            ],
            sqlCommand: [
                {required: true, message: ' ', trigger: 'change'}
            ],
            dictType: [
                {required: true, message: ' ', trigger: 'change'}
            ],
            showOrder: [
                {required: true, message: ' ', trigger: 'change'}
            ],
            description: [
                {required: true, message: ' ', trigger: 'change'}
            ]
        };
        detailedFormRules: object = {
            itemsName: [
                {required: true, message: ' ', trigger: 'change'}
            ],
            itemsCode: [
                {required: true, message: ' ', trigger: 'change'}
            ],
            showOrder: [
                {required: true, message: ' ', trigger: 'change'}
            ],
            description: [
                {required: true, message: ' ', trigger: 'change'}
            ]
        };

        @Watch('classificationDialogVisible')
        classificationDialogVisibleChange(newVal) {
            if (!newVal) {
                this.$refs.classificationFormRef['resetFields']();
                this.classificationForm = {
                    id: '',
                    dictName: '',
                    sqlCommand: '',
                    dictType: '',
                    showOrder: '',
                    description: ''
                };
                this.classificationForm['showOrder'] = this.classificationPaginationData['total'] + 1;
            }
        };

        @Watch('detailedDialogVisible')
        detailedDialogVisibleChange(newVal) {
            if (!newVal) {
                this.$refs.detailedFormRef['resetFields']();
                this.detailedForm = {
                    id: '',
                    dictId: '',
                    itemsName: '',
                    itemsCode: '',
                    showOrder: '',
                    description: ''
                };
                this.detailedForm['showOrder'] = this.detailedPaginationData['total'] + 1;
            }
        };

        public mounted(): void {
            this.getClassificationTableData();
            this.getTypeSelections();
        }

        public edit(type, data) {
            if (type === 'dict') {
                this.classificationForm['id'] = data['id'];
                this.classificationForm['dictName'] = data['dictName'];
                this.classificationForm['sqlCommand'] = data['sqlCommand'];
                this.classificationForm['dictType'] = data['dictType'];
                this.classificationForm['showOrder'] = data['showOrder'];
                this.classificationForm['description'] = data['description'];
                this.classificationDialogVisible = true;
            } else if (type === 'items') {
                this.detailedForm['id'] = data['id'];
                this.detailedForm['dictId'] = data['dictId'];
                this.detailedForm['itemsName'] = data['itemsName'];
                this.detailedForm['itemsCode'] = data['itemsCode'];
                this.detailedForm['showOrder'] = data['showOrder'];
                this.detailedForm['description'] = data['description'];
                this.detailedDialogVisible = true;
            }
        }

        /**
         * 提交常量分类
         */
        public submitClassificationForm(): void {
            this.$refs.classificationFormRef['validate']((valid) => {
                if (valid) {
                    let URL = '';
                    if (this.classificationForm['id'] === '') {
                        URL = config.port('dictinfo') + 'saveDict';
                        this.$http.post(URL, this.classificationForm).then((response) => {
                            if (response.data.code === 0) {
                                this.$message({
                                    message: '添加成功',
                                    type: 'success',
                                    showClose: true
                                });
                                this.getClassificationTableData();
                                this.classificationDialogVisible = false;
                            } else {
                                this.$message.error(response.data.msg)
                            }
                        })
                    } else {
                        URL = config.port('dictinfo') + 'updateDict';
                        this.$http.put(URL, this.classificationForm).then((response) => {
                            if (response.data.code === 0) {
                                this.$message({
                                    message: '编辑成功',
                                    type: 'success',
                                    showClose: true
                                });
                                this.getClassificationTableData();
                                this.classificationDialogVisible = false;
                            } else {
                                this.$message.error(response.data.msg)
                            }
                        })
                    }
                } else {
                    return false;
                }
            });
        }

        /**
         * 提交常量明细
         */
        public submitDetailedForm(): void {
            this.$refs.detailedFormRef['validate']((valid) => {
                if (valid) {
                    this.detailedForm['dictId'] = this.classificationCurrentRow['id'];
                    let URL = '';
                    if (this.detailedForm['id'] === '') {
                        URL = config.port('dictinfo') + 'saveItems';
                        this.$http.post(URL, this.detailedForm).then((response) => {
                            if (response.data.code === 0) {
                                this.$message({
                                    message: '添加成功',
                                    type: 'success',
                                    showClose: true
                                });
                                // 19代表操作的是状态
                                if (this.classificationCurrentRow['id'] === 19) {
                                    this.getBaseFiledOptions('status');  // 更新store缓存的status的options值
                                }
                                this.getDetailedData();
                                this.detailedDialogVisible = false;
                            } else {
                                this.$message.error(response.data.msg)
                            }
                        })
                    } else {
                        URL = config.port('dictinfo') + 'updateItems';
                        this.$http.put(URL, this.detailedForm).then((response) => {
                            if (response.data.code === 0) {
                                this.$message({
                                    message: '编辑成功',
                                    type: 'success',
                                    showClose: true
                                });
                                // 19代表操作的是状态
                                if (this.classificationCurrentRow['id'] === 19) {
                                    this.getBaseFiledOptions('status');  // 更新store缓存的status的options值
                                }
                                this.getDetailedData();
                                this.detailedDialogVisible = false;
                            } else {
                                this.$message.error(response.data.msg)
                            }
                        })
                    }
                } else {
                    return false;
                }
            });
        }

        /**
         * 常量类别数据获取接口
         */
        getTypeSelections() {
            let queryParam = {dictId: config.FIXED_VARIABLE.CONSTANT_TYPE_ID};
            let URL = config.port('dictinfo') + 'getDictDetailPage';

            this.$http.get(URL, {params: queryParam}).then((response) => {
                this.typeSelections = response.data.data.records;
                this.classificationHeaderData[1]['options'] = this.typeSelections;
            }).catch(function (response) {
            })
        }

        /**
         * 常量分类当前行选择触发事件
         */
        public handleTableCurrentChange(currentRow): void {
            this.classificationCurrentRow = currentRow;
            this.getDetailedData();
        }


        /**
         * 删除常量
         */
        public del(type, data): void {
            this.$confirm(`删除时将删除【${data.dictName || data.itemsName || 'unknown'}】常量，确定删除吗？`, '提示', {type: 'warning'}).then(() => {
                let URL = '';
                if (type === 'dict') {
                    URL = config.port('dictinfo') + 'deleteDict/' + data.id;

                } else if (type === 'items') {
                    URL = config.port('dictinfo') + 'deleteItems/' + data.id;
                }
                this.$http.delete(URL).then((response) => {
                    this.$message({
                        message: '删除成功',
                        type: 'success',
                        showClose: true
                    });

                    // 19代表操作的是状态
                    if (this.classificationCurrentRow['id'] === 19) {
                        this.getBaseFiledOptions('status');  // 更新store缓存的status的options值
                    }

                    if (type === 'dict') {
                        this.classificationPaginationData['currentPage'] = 1;
                        this.getClassificationTableData();

                    } else if (type === 'items') {
                        this.detailedPaginationData['currentPage'] = 1;
                        this.getClassificationTableData();
                    }
                })
            })
        }

        /**
         * 获取常量分类的table数据
         */
        public getClassificationTableData(seearch?): void {
            if (seearch) {
                this.detailedPaginationData['currentPage'] = 1;
                this.classificationPaginationData['currentPage'] = 1;
            }
            let params = {
                current: this.classificationPaginationData['currentPage'],
                size: this.classificationPaginationData['pageSize'],
                dictName: this.searchClassificationVal
            };
            let url = config.port('dictinfo') + 'getDictPage';
            this.$http.get(url, {params}).then(response => {
                const res = response.data;
                if (res.code === 0) {
                    const data = res.data;
                    this.classificationPaginationData['total'] = data['total'];
                    this.classificationPaginationData['currentPage'] = data['current'];
                    this.classificationTableData = data['records'];
                    this.classificationForm['showOrder'] = data['total'] + 1;
                    // 将常量明细的数据置为空
                    this.detailedTableData = [];
                    // 常量分类缓存被选中的置为空
                    this.classificationCurrentRow = {};

                    // 常量分类有数据时 查看常量明细
                    if (data['records'].length) {
                        this.classificationCurrentRow = this.classificationTableData[0];
                        // 默认选中常量分类第一行
                        this.setClassificationTableCurrentRow();
                        // 查询常量明细
                        this.getDetailedData();
                    }
                }

            })
        }

        /**
         * 获取常量分类的table数据
         */
        public getDetailedData(search?): void {
            if (search) this.detailedPaginationData['currentPage'] = 1;
            let params = {
                current: this.detailedPaginationData['currentPage'],
                size: this.detailedPaginationData['pageSize'],
                dictId: this.classificationCurrentRow['id'],
                itemsName: this.searchDetailedVal,
                itemsCode: this.searchDetailedVal,
            };
            let url = config.port('dictinfo') + 'getDictDetailPage';
            this.$http.get(url, {params}).then(response => {
                const res = response.data;
                if (res.code === 0) {
                    const data = res.data;
                    this.detailedPaginationData['total'] = data['total'];
                    this.detailedPaginationData['currentPage'] = data['current'];
                    this.detailedTableData = data['records'];
                    this.detailedForm['showOrder'] = data['total'] + 1;
                }

            })
        }

        public setClassificationTableCurrentRow(): void {
            this.$refs.classificationTable['setCurrentRow'](this.classificationCurrentRow);
        }
    }
</script>

<style scoped lang="less">
    .constant {
        position: relative;

        .classification, .detailed {
            position: absolute;
            left: 10px;
            right: 10px;
            display: flex;
            flex-direction: column;
            border: 1px solid #ccc;

            .title {
                line-height: 30px;
                background-color: #fff;
                font-size: 14px;
                font-weight: 700;
                padding-left: 10px;
                border-bottom: 1px dashed #ccc;
            }

            .operation {
                background-color: #fff;
                line-height: 40px;
                padding-left: 10px;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }

            .table-wrapper {
                flex: 1;
            }
        }

        .classification {
            top: 10px;
            bottom: 52%;
        }

        .detailed {
            top: 52%;
            bottom: 10px;
        }
    }
</style>
