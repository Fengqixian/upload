<template>
    <div class="project-constant">
        <div class="classification">
            <div class="info-title">
                <div class="title">详 情</div>
                <ul class="info">
                    <li v-for="(item,index) in infoData" :key="index">
                        <span class="label">{{item.label}}：</span>
                        <template v-if="item.prop === 'category'">
                            <span class="value" v-if="item.value === '1'">分类</span>
                            <span class="value" v-else-if="item.value === '2'">数据集</span>
                            <span class="value" v-else>其他</span>
                        </template>
                        <span class="value" v-else>{{item.value}}</span>
                    </li>
                </ul>
            </div>
            <div class="data-meta" v-if="dataMetaTableData.length || dataMetaSearchValFlag">
                <div class="title">
                    数据元
                    <div>
                        <el-input placeholder="请输入内容" v-model.trim="dataMetaSearchVal"
                                  @keydown.native.enter.stop.prevent="getDataMetaTableData"
                                  @change="dataMetaSearchValFlag=true">
                            <el-button slot="append" icon="el-icon-search"
                                       @click.stop.prevent="getDataMetaTableData"></el-button>
                        </el-input>
                        <el-button class="add-nobg" @click="showSql">展示SQL</el-button>
                        <el-button class="add-bg" @click="getMetaDataViewData">获取数据</el-button>
                    </div>
                </div>
                <div class="table-wrapper">
                    <cb-table selection :isPagination="false"
                              @selection-change="selectionChange"
                              :header-data="elementHeaderData"
                              :table-data="dataMetaTableData"
                              :pagination-data="dataMetaPaginationData"
                              @size-change="getDataMetaTableData"
                              @current-change="getDataMetaTableData"
                              ref="dataMetaTable">

                        <template slot="column">
                            <el-table-column label="操作"
                                             fixed="right"
                                             width="50">
                                <template slot-scope="{row}">
                                    <el-tooltip content="血缘关系" placement="top" effect="dark">
                                        <el-button type="text"
                                                   class="add-text"
                                                   icon="iconfont icon-xueyuanguanxi"
                                                   @click="showHxRelation(row)">
                                        </el-button>
                                    </el-tooltip>
                                </template>
                            </el-table-column>
                        </template>
                    </cb-table>
                </div>
            </div>
            <div class="child-data" v-if="childDataTableData.length || childDataSearchValFlag">
                <div class="title">
                    子集
                    <el-input placeholder="请输入内容" v-model.trim="childDataSearchVal"
                              @keydown.native.enter="getChildDataTableData"
                              @change="childDataSearchValFlag=true">
                        <el-button slot="append" icon="el-icon-search"
                                   @click.stop.prevent="getChildDataTableData"></el-button>
                    </el-input>
                </div>
                <div class="table-wrapper">
                    <cb-table selection
                              @selection-change="selectionChange"
                              :header-data="childDataHeaderData"
                              :table-data="childDataTableData"
                              :pagination-data="childDataPaginationData"
                              @size-change="getChildDataTableData"
                              @current-change="getChildDataTableData"
                              @table-current-change="handleTableCurrentChange"
                              :highlight-current-row="true"
                              ref="childDataTable">
                    </cb-table>
                </div>
            </div>
            <div class="child-data-meta"
                 v-if="childDataMetaTableData.length && childDataTableData.length || childDataMetaSearchValFlag">
                <div class="title">
                    子集数据元
                    <el-input placeholder="请输入内容" v-model.trim="childDataMetaSearchVal"
                              @keydown.native.enter="getChildDataMetaTableData"
                              @change="childDataMetaSearchValFlag=true">
                        <el-button slot="append" icon="el-icon-search"
                                   @click.stop.prevent="getChildDataMetaTableData"></el-button>
                    </el-input>
                </div>
                <div class="table-wrapper">
                    <cb-table selection
                              @selection-change="selectionChange"
                              :header-data="elementHeaderData"
                              :table-data="childDataMetaTableData"
                              :pagination-data="childDataMetaPaginationData"
                              @size-change="getChildDataMetaTableData"
                              @current-change="getChildDataMetaTableData"
                              ref="childDataMetaTable">
                    </cb-table>
                </div>
            </div>
        </div>
        <el-dialog title="展示SQL"
                   :close-on-click-modal="false"
                   :visible.sync="showSqlVisible"
                   class="showSqlDialog">
            <div class="table-wrapper">
                <cb-table :header-data="showSqlHeaderData"
                          :table-data="showSqlTableData"
                          :is-pagination="false"
                          @size-change="getShowSqlTableData"
                          @current-change="getShowSqlTableData"
                          ref="showSqlTable">
                    <template slot="column">
                        <el-table-column
                                label="操作"
                                fixed="right"
                                width="100">
                            <template slot-scope="{row}">
                                <el-tooltip content="编辑" placement="top" effect="dark">
                                    <el-button type="text"
                                               class="edit-text"
                                               icon="iconfont icon-bianji1"
                                               @click="editSql(row)"></el-button>
                                </el-tooltip>
                                <el-tooltip content="启用ETL" placement="top" effect="dark">
                                    <el-button type="text"
                                               class="add-text"
                                               icon="el-icon-attract"
                                               @click="enableETL(row)"></el-button>
                                </el-tooltip>
                                <!-- <el-tooltip content="删除" placement="top" effect="dark"
                                             v-if="row.id !== 16 && row.id !== 17 && row.id !== 18 && row.id !== 19 && row.id !== 22">
                                     <el-button type="text"
                                                class="del-text"
                                                icon="iconfont icon-shanchu1"
                                                @click="del('dict',row)"></el-button>
                                 </el-tooltip>-->
                            </template>
                        </el-table-column>
                    </template>
                </cb-table>
            </div>
            <div slot="footer" class="dialog-footer">
                <el-button class="del-nobg"
                           @click="showSqlVisible = false">取 消
                </el-button>
                <el-button class="add-bg"
                           @click="addSql">新 增
                </el-button>
            </div>

        </el-dialog>
        <el-dialog title="添加SQL"
                   :close-on-click-modal="false"
                   :visible.sync="addSqlVisible">
            <el-form ref="SqlFormRef"
                     :model="SqlForm"
                     label-width="120px"
                     :rules="SqlFormRules">
                <el-form-item label="来源数据库" prop="sourceDbId">
                    <el-select v-model="SqlForm.sourceDbId"
                               placeholder="请选择来源数据库"
                               @change="sourceDatabaseSelect">
                        <el-option
                                v-for="item in sqlDatabase"
                                :key="item.id"
                                :label="item.nameCn || item.nameEn"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>

                <el-form-item label="来源表" prop="sourceTableId" v-if="sqlSourceTable.length">
                    <el-select v-model="SqlForm.sourceTableId" placeholder="请选择来源表">
                        <el-option
                                v-for="item in sqlSourceTable"
                                :key="item.id"
                                :label="item.nameCn || item.nameEn"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="目标数据库" prop="targetDbId">
                    <el-select v-model="SqlForm.targetDbId"
                               placeholder="请选择目标数据库"
                               @change="targetDatabaseSelect">
                        <el-option
                                v-for="item in sqlDatabase"
                                :key="item.id"
                                :label="item.nameCn || item.nameEn"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>

                <el-form-item label="目标表" prop="targetTableId" v-if="sqlTargetTable.length">
                    <el-select v-model="SqlForm.targetTableId" placeholder="请选择目标表">
                        <el-option
                                v-for="item in sqlTargetTable"
                                :key="item.id"
                                :label="item.nameCn || item.nameEn"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="执行sql" prop="executorSql">
                    <el-input type="textarea"
                              v-model.trim="SqlForm.executorSql"
                              placeholder="请输入执行sql">
                    </el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button class="del-nobg" @click="addSqlVisible = false">取 消</el-button>
                <el-button class="confirm-bg" @click="submitSql()">确 定</el-button>
            </div>

        </el-dialog>

        <cb-card-relation v-model="cbCardRelationData" viewType="project"/>
        <!--视图类别  业务视图:business  项目视图:project  技术视图:technology-->
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component, Prop, Watch} from 'vue-property-decorator'
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

        // 详情
        infoData = [
            {
                prop: 'resourceCode',
                label: '资源编码',
                value: '',
            },
            {
                prop: 'category', //    1: '分类', 2: '数据集',  9: '其他'
                label: '类型',
                value: '',
                type: 'tag'
            },
            {
                prop: 'nameCn',
                label: '中文名称',
                value: '',
            },
            {
                prop: 'nameEn',
                label: '英文名称',
                value: '',
            },
            {
                prop: 'description',
                label: '备注',
                value: '',
            },
            {
                prop: 'sourceId',
                label: '来源标识',
                value: '',
            },
            {
                prop: 'createUser',
                label: '创建人',
                value: '',
            },
            {
                prop: 'createTime',
                label: '创建时间',
                value: '',
            },
        ];
        childDataSearchVal = ''; // 模糊查询值 数据元
        dataMetaSearchVal = ''; // 模糊查询值 子集
        childDataMetaSearchVal = ''; // 模糊查询值 子集数据元
        dataMetaSearchValFlag = false; // 执行模糊查询值 子集
        childDataSearchValFlag = false; // 执行模糊查询值 子集
        childDataMetaSearchValFlag = false; // 执行模糊查询值 子集数据元
        /**
         * 数据集引用数据元
         */
        dataMetaTableData = [];

        dataMetaPaginationData = {
            currentPage: 1,
            pageSize: 10,
            total: 1
        };
        /**
         * 数据集子级
         */
        childDataHeaderData = [
            {
                prop: 'resourceCode',
                label: '资源编码',
                width: '100px',
            },
            {
                prop: 'category', //  规则类型 0：脱敏 1:标签
                label: '类型',
                type: 'tag',
                translations: {
                    1: '分类',
                    2: '数据集',
                    9: '其他'
                },
                styles: {
                    1: 'default',
                    2: 'success',
                    9: 'warning'
                }
            },
            {
                prop: 'nameCn',
                label: '中文名称',
                width: '200px',
            },
            {
                prop: 'nameEn',
                label: '英文名称',
                width: '100px',
            },
            {
                prop: 'description',
                label: '备注'
            },
            {
                prop: 'sourceId',
                label: '来源标识'
            },
            {
                prop: 'createUser',
                label: '创建人'
            },
            {
                prop: 'createTime',
                label: '创建时间'
            },

        ];
        childDataTableData = [];


        childDataPaginationData = {
            currentPage: 1,
            pageSize: 10,
            total: 1
        };
        /**
         * 子级数据元
         */
        elementHeaderData = [
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
        childDataMetaTableData = [];


        childDataMetaPaginationData = {
            currentPage: 1,
            pageSize: 10,
            total: 1
        };
        currentRow = {};

        /**
         * 展示SQL
         */
        showSqlVisible: boolean = false;
        showSqlHeaderData: Array<object> = [
            {
                prop: 'executorSql',
                label: ' 执行sql',
                width: '150px'
            },
            {
                prop: 'sourceId',
                label: ' 来源id',
                width: '150px'
            },
            {
                prop: 'sourceDb',
                label: ' 来源数据库名称',
                width: '150px'
            },
            {
                prop: 'sourceDbId',
                label: ' 来源数据库id',
                width: '150px'
            },
            {
                prop: 'sourceTable',
                label: ' 来源表名称',
                width: '150px'
            },
            {
                prop: 'sourceTableId',
                label: ' 来源表ID',
                width: '150px'
            },
            {
                prop: 'targetId',
                label: ' 目标id',
                width: '150px'
            },
            {
                prop: 'targetDb',
                label: ' 目标数据库名称',
                width: '150px'
            },
            {
                prop: 'targetDbId',
                label: ' 目标数据库id',
                width: '150px'
            },
            {
                prop: 'targetTable',
                label: ' 目标表名称',
                width: '150px'
            },
            {
                prop: 'targetTableId',
                label: ' 目标表ID',
                width: '150px'
            },
            {
                prop: 'etlId',
                label: ' etlId',
                width: '150px'
            },
        ];
        showSqlTableData: Array<object> = [];

        /**
         * 增加SQL
         */
        addSqlVisible: boolean = false;
        SqlForm: object = {
            sourceId: '', // 来源id  修改传
            sourceDb: '', // 来源数据库名称
            sourceDbId: '', // 来源数据库id
            sourceTable: '', // 来源表名称
            sourceTableId: '', // 来源表ID
            targetId: '', // 目标id  修改传
            targetDb: '', // 目标数据库名称
            targetDbId: '', // 目标数据库id
            targetTable: '', // 目标表名称
            targetTableId: '', // 目标表ID
            executorSql: '', // 执行sql
            etlId: '', // etl Id
        };
        SqlFormRules: object = {
            sourceDbId: [
                {required: true, message: ' ', trigger: 'change'},
            ],
            sourceTableId: [
                {required: true, message: ' ', trigger: 'change'},
            ],
            targetDbId: [
                {required: true, message: ' ', trigger: 'change'},
            ],
            targetTableId: [
                {required: true, message: ' ', trigger: 'change'},
            ],
            executorSql: [
                {required: true, message: ' ', trigger: 'change'},
            ],
        };
        sqlDatabase: Array<object> = [];
        sqlSourceTable: Array<object> = [];
        sqlTargetTable: Array<object> = [];

        cbCardRelationData: object = {};


        mounted() {
            this.getInfo();
            this.getDataMetaTableData();
        }


        /**
         * 华西血缘展示
         */
        showHxRelation(data) {
            data['projectId'] = this.arg.id;
            this.cbCardRelationData = data;
        }

        // 获取数据

        /**
         * 获取 数据集引用数据元
         */
        getDataMetaTableData() {
            let params = {
                current: this.dataMetaPaginationData['currentPage'],
                size: -1,
                id: this.arg.id,
                // id: 27,
                queryString: this.dataMetaSearchVal
            };
            let url = config.port('projectView') + '/info';
            this.$http.get(url, {params}).then(response => {
                const res = response.data;
                if (res.code === 0) {
                    const data = res.data;
                    this.dataMetaPaginationData['currentPage'] = data.current;
                    this.dataMetaPaginationData['pageSize'] = 1000;
                    this.dataMetaPaginationData['total'] = data.total;
                    this.dataMetaTableData = data.records
                }

            })
        }

        /**
         * 获取 数据集子级
         */
        getChildDataTableData() {
            let params = {
                current: this.childDataPaginationData['currentPage'],
                size: -1,
                id: this.arg.id,
                queryString: this.childDataSearchVal
            };
            let url = config.port('businessView') + '/childCategory';
            this.$http.get(url, {params}).then(response => {
                const res = response.data;
                if (res.code === 0) {
                    const data = res.data;
                    this.childDataPaginationData['currentPage'] = data.current;
                    this.childDataPaginationData['pageSize'] = data.size;
                    this.childDataPaginationData['total'] = data.total;
                    this.childDataTableData = data.records
                }
            })
        }

        /**
         * 获取 子级数据元
         */
        getChildDataMetaTableData() {
            let params = {
                current: this.childDataMetaPaginationData['currentPage'],
                size: this.childDataMetaPaginationData['pageSize'],
                id: this.currentRow['id'],
                queryString: this.childDataMetaSearchVal
            };
            let url = config.port('businessView') + '/elementList';
            this.$http.get(url, {params}).then(response => {
                const res = response.data;
                if (res.code === 0) {
                    const data = res.data;
                    this.childDataMetaPaginationData['currentPage'] = data.current;
                    this.childDataMetaPaginationData['pageSize'] = data.size;
                    this.childDataMetaPaginationData['total'] = data.total;
                    this.childDataMetaTableData = data.records
                }
            })
        }

        /**
         * 获取详情数据
         */
        getInfo() {
            let url = config.port('businessView') + 'categoryInfo/' + this.arg.id;
            this.$http.get(url).then(response => {
                const res = response.data;
                if (res.code === 0) {
                    const data = res.data;
                    this.infoData.map(item => {
                        item.value = data[item.prop]
                    });
                }
            })
        }

        handleTableCurrentChange(data) {
            this.currentRow = data;
            this.getChildDataMetaTableData();
        }

        //获取选中的数据
        selectionChange(selection) {
            this.metaDataViewList = selection;

        }

        metaDataViewList = [];

        /**
         * 获取数据
         */
        getMetaDataViewData() {
            const showId = Array<number>();
            if (this.metaDataViewList == null || this.metaDataViewList.length <= 0) {
                this.$message.warning('请选择需要预览的数据源。')
                return;
            } else {
                for (let o = 0; o < this.metaDataViewList.length; o++) {
                    showId.push(this.metaDataViewList[o]['id']);
                }
            }
            let url = config.port('projectView') + 'excel/' + this.arg.id;
            this.$http.post(url, showId, {responseType: "arraybuffer"}).then(response => {
                const res = new Blob([response.data], {type: "application/vnd.excel"});
                // let objectUrl = URL.createObjectURL(res);
                var link = document.createElement('a');

                link.href = window.URL.createObjectURL(res);

                link.download = "数据列表.xls";

                link.click();
            })
        }


        /**
         * 添加SQL
         */
        addSql() {
            this.addSqlVisible = true;
            this.getDatabase();
        }

        /**
         * 编辑SQL
         */
        editSql(data) {
            this.addSqlVisible = true;
            Object.assign(this.SqlForm, data);
            this.sourceDatabaseSelect(data.sourceDbId);
            this.targetDatabaseSelect(data.targetDbId);
            this.getDatabase();
        }

        /**
         * 启用ETL
         */
        enableETL(data) {
            let params = {
                sourceId: data.sourceId,
                targetId: data.targetId
            };
            let url = config.port('technologyEtl') + '/etl';
            this.$http.get(url, {params}).then(response => {
                const res = response.data;
                if (res.code === 0) {
                    this.$message.success("启用成功")
                }

            })
        }

        /**
         * 展示SQL
         */
        showSql() {
            this.showSqlVisible = true;
            this.getShowSqlTableData();
        }

        /**
         * 获取数据库
         */
        getDatabase() {
            if (this.sqlDatabase.length) return;
            let params = {
                size: -1
            };
            let url = config.port('technologyView') + 'database';
            this.$http.get(url, {params}).then(response => {
                if (response.data.code === 0) {
                    this.sqlDatabase = response.data.data.records;
                }
            })
        }

        /**
         * 来源数据库 选择 change事件
         */
        sourceDatabaseSelect(databaseId) {
            let url = config.port('technologyView') + 'table';
            let params = {
                databaseId
            };
            this.$http.get(url, {params}).then(response => {
                if (response.data.code === 0) {
                    this.sqlSourceTable = response.data.data.records;
                }
            })
        }

        /**
         * 来源数据库 选择 change事件
         */
        targetDatabaseSelect(databaseId) {
            let url = config.port('technologyView') + 'table';
            let params = {
                databaseId
            };
            this.$http.get(url, {params}).then(response => {
                if (response.data.code === 0) {
                    this.sqlTargetTable = response.data.data.records;
                }
            })
        }

        /**
         * 提交sql
         */
        submitSql() {
            this.$refs.SqlFormRef['validate'](valid => {
                if (valid) {
                    // 根据id 将来源目标的数据库表中文名查询出来
                    this.SqlForm['sourceDb'] = this.sqlDatabase.find(item => item['id'] === this.SqlForm['sourceDbId'])['nameEn'];
                    this.SqlForm['sourceTable'] = this.sqlSourceTable.find(item => item['id'] === this.SqlForm['sourceTableId'])['nameEn'];
                    this.SqlForm['targetDb'] = this.sqlDatabase.find(item => item['id'] === this.SqlForm['targetDbId'])['nameEn'];
                    this.SqlForm['targetTable'] = this.sqlTargetTable.find(item => item['id'] === this.SqlForm['targetTableId'])['nameEn'];
                    let url = config.port('technologyEtl');
                    this.$http({
                        method: this.SqlForm['sourceId'].toString() ? 'put' : 'post',
                        url,
                        data: this.SqlForm
                    }).then(response => {
                        if (response.data.code === 0) {
                            this.$message.success("操作成功");
                            this.addSqlVisible = false;
                        }
                        this.getShowSqlTableData();
                    });
                } else {
                    return false;
                }
            });
        }

        /**
         * 获取SQL数据
         */
        getShowSqlTableData() {
            let params = {
                size: -1,
                id: this.arg.id
            };
            let url = config.port('technologyEtl') + '/getEtlSql';
            this.$http.get(url, {params}).then(response => {
                const res = response.data;
                if (res.code === 0) {
                    const data = res.data;
                    this.showSqlTableData = data
                }

            })
        }
    }
</script>

<style lang="less">
    .project-constant {
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
                justify-content: space-between;
                line-height: 30px;
                background-color: #fff;
                font-size: 14px;
                font-weight: 700;
                padding-left: 10px;
                border-bottom: 1px dashed #ccc;

                .el-input-group {
                    width: 200px;
                }
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

            .data-meta, .child-data, .child-data-meta {
                flex: 1;
                margin-top: 10px;
                display: flex;
                flex-direction: column;
                min-height: 300px;

                .table-wrapper {
                    flex: 1;
                }
            }
        }

        .showSqlDialog {
            .el-dialog {
                height: 60%;
                display: flex;
                flex-direction: column;

                .el-dialog__body {
                    flex: 1;

                    .table-wrapper {
                        margin-top: 10px;
                        width: 100%;
                        height: 100%;
                    }
                }
            }
        }
    }
</style>
