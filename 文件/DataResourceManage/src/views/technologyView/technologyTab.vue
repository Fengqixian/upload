<template>
    <div class="constant">
        <div class="classification">
            <div class="data-meta">
                <div class="search-wrapper">
                    <div class="right">
                        <el-input placeholder="请输入内容"
                                  v-model="queryString"
                                  class="input-with-select"
                                  @keyup.native.enter.stop.prevent="getDataMetaTableData('search')">
                            <el-button slot="append" icon="el-icon-search"
                                       @click="getDataMetaTableData('search')"></el-button>
                        </el-input>
                    </div>
                </div>
                <!--<div class="title">数据集引用数据元</div>-->
                <div class="table-wrapper">
                    <cb-table :header-data="tableHeaderData"
                              :table-data="dataMetaTableData"
                              :pagination-data="dataMetaPaginationData"
                              @size-change="getDataMetaTableData"
                              @current-change="getDataMetaTableData"
                              ref="dataMetaTable">
                        <template slot="column">
                            <el-table-column label="操作"
                                             fixed="right"
                                             width="80">
                                <template slot-scope="{row}">
                                    <el-tooltip content="血缘关系" placement="top" effect="dark">
                                        <el-button type="text"
                                                   class="add-text"
                                                   icon="iconfont icon-xueyuanguanxi"
                                                   @click="showHxRelation(row)">
                                        </el-button>
                                    </el-tooltip>
                                    <el-tooltip content="编辑" placement="top" effect="dark">
                                        <el-button type="text"
                                                   class="edit-text"
                                                   icon="el-icon-edit"
                                                   @click="editColumn(row)">
                                        </el-button>
                                    </el-tooltip>
                                </template>
                            </el-table-column>
                        </template>
                    </cb-table>
                </div>
            </div>
        </div>

        <cb-card-relation v-model="cbCardRelationData" viewType="technology"/>
        <!--视图类别  业务视图:business  项目视图:project  技术视图:technology-->
        <el-dialog
                title="字段"
                :visible.sync="columnDialogVisible"
                :close-on-click-modal="false"
                width="30%">
            <el-form :model="columnForm" :rules="columnFormRules" ref="columnFormRef" label-width="100px">
                <el-form-item label="中文名称" prop="nameCn">
                    <el-input v-model.trim="columnForm.nameCn" placeholder="中文名称"/>
                </el-form-item>
                <el-form-item label="英文名称" prop="nameEn">
                    <el-input v-model.trim="columnForm.nameEn" placeholder="英文名称"/>
                </el-form-item>
                <el-form-item label="描述" prop="comment">
                    <el-input type="textarea" v-model.trim="columnForm.comment" placeholder="描述"/>
                </el-form-item>
                <el-form-item>
                    <div class="btn-right">
                        <el-button class="del-nobg" @click="columnDialogVisible = false">取 消</el-button>
                        <el-button class="confirm-bg" @click="submitColumnForm()">确 定</el-button>
                    </div>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component, Prop} from 'vue-property-decorator'
    import {Mutation, State} from 'vuex-class'
    import config from '../../config.ts'
    import CbCardRelation from '../../components/cbCardRelation/index.vue'

    @Component({
        components: {CbCardRelation},
    })
    export default class TechnologyTab extends Vue {
        name: string = 'index';
        @Mutation setLoadingFlag;
        @State windowSize;
        @State menuWidth;
        @Prop()
        id;
        // tree节点的数据
        @Prop()
        arg;
        // 搜索
        queryString: String = '';

        /**
         * 数据集引用数据元
         */
        tableHeaderData = [
            {
                prop: 'nameCn',
                label: '中文名'
            },
            {
                prop: 'nameEn',
                label: '英文名'
            },
            // {
            //     prop: 'resourceCode',
            //     label: '资源编码'
            // },
            {
                prop: 'dataType',
                label: '数据类型'
            },
            {
                prop: 'comment',
                label: '备注'
            },
        ];
        dataMetaTableData = [];

        dataMetaPaginationData = {
            currentPage: 1,
            pageSize: 10,
            total: 1
        };
        cbCardRelationData: object = {};


        columnDialogVisible: boolean = false;
        columnForm: {
            id: number,
            tableId: number,
            resourceId: string,
            resourceCode: string,
            nameCn: string,
            nameEn: string,
            dataType: string,
            dataLength: number,
            primaryKey: boolean,
            nullAllow: boolean,
            comment: string,
        } = {
            id: null,
            tableId: null,
            resourceId: '',
            resourceCode: '',
            nameCn: '',
            nameEn: '',
            dataType: '',
            dataLength: null,
            primaryKey: null,
            nullAllow: null,
            comment: '',
        };
        columnFormRules = {
            nameCn: [{required: true, message: ' ', trigger: 'blur'}],
            nameEn: [{required: true, message: ' ', trigger: 'blur'}],
            dataType: [{required: true, message: ' ', trigger: 'blur'}],
        };

        mounted() {
            this.getDataMetaTableData();
        }


        /**
         * 华西血缘展示
         */
        showHxRelation(data) {
            this.cbCardRelationData = data;
        }

        /**
         * 获取 数据集引用数据元
         */
        getDataMetaTableData(search?) {
            if (search === 'search') this.dataMetaPaginationData['currentPage'] = 1;
            let params = {
                current: this.dataMetaPaginationData['currentPage'],
                size: this.dataMetaPaginationData['pageSize'],
                id: this.arg.id,
                queryString: this.queryString
            };
            let url = config.port('technologyView') + '/info';
            this.$http.get(url, {params}).then(response => {
                const res = response.data;
                if (res.code === 0) {
                    const data = res.data;
                    this.dataMetaPaginationData['currentPage'] = data.current;
                    this.dataMetaPaginationData['pageSize'] = data.size;
                    this.dataMetaPaginationData['total'] = data.total;
                    this.dataMetaTableData = data.records
                }

            })
        }

        /**
         * 编辑字典按钮
         * @param data
         */
        editColumn(data) {
            this.columnForm.nameCn = data['nameCn'];
            this.columnForm.nameEn = data['nameEn'];
            this.columnForm.comment = data['comment'];
            this.columnForm.id = data['id'];
            this.columnDialogVisible = true;
        }

        /**
         * 提交数据库操作
         */
        submitColumnForm() {
            const url = config.port('technologyView') + 'updateDataColumn';
            const data = this.columnForm;
            this.$http({
                method: 'put',
                url,
                data
            }).then(response => {
                if (response.data.code === 0) {
                    this.$message.success('提交成功');
                    this.getDataMetaTableData();
                    this.columnDialogVisible = false;
                }
            })
        }
    }
</script>

<style scoped lang="less">
    .constant {
        position: relative;
        width: 100%;
        height: 100%;

        .classification {
            position: absolute;
            left: 10px;
            right: 10px;
            top: 10px;
            bottom: 10px;
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
                    width: 100px;
                    font-size: 12px;
                }

                .label {
                    display: inline-block;
                    text-align: right;
                }

                .value {
                    display: inline-block;
                }
            }

            .data-meta {
                flex: 1;
                display: flex;
                flex-direction: column;
                min-height: 300px;

                .search-wrapper {
                    height: 32px;
                    background: rgba(255, 255, 255, 1);
                    margin-bottom: 5px;
                    display: flex;
                    justify-content: flex-end;
                    align-items: center;
                }

                .table-wrapper {
                    flex: 1;
                }
            }
        }
    }
</style>
