<template>
    <div class="constant">
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
                    <el-input placeholder="请输入内容" v-model.trim="dataMetaSearchVal"
                              @keydown.native.enter.stop.prevent="getDataMetaTableData"
                              @change="dataMetaSearchValFlag=true">
                        <el-button slot="append" icon="el-icon-search"
                                   @click.stop.prevent="getDataMetaTableData"></el-button>
                    </el-input>
                </div>
                <div class="table-wrapper">
                    <cb-table :header-data="elementHeaderData"
                              :table-data="dataMetaTableData"
                              :pagination-data="dataMetaPaginationData"
                              @size-change="getDataMetaTableData"
                              @current-change="getDataMetaTableData"
                              ref="dataMetaTable">
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
                    <cb-table :header-data="childDataHeaderData"
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
            <div class="child-data-meta" v-if="childDataMetaTableData.length && childDataTableData.length || childDataMetaSearchValFlag">
                <div class="title">
                    子集数据元
                    <el-input placeholder="请输入内容" v-model.trim="childDataMetaSearchVal"
                              @keydown.native.enter="getChildDataMetaTableData"
                              @change="childDataMetaSearchValFlag=true">
                        <el-button slot="append" icon="el-icon-search"
                                   @click.stop.prevent="getChildDataMetaTableData"></el-button>
                    </el-input></div>
                <div class="table-wrapper">
                    <cb-table :header-data="elementHeaderData"
                              :table-data="childDataMetaTableData"
                              :pagination-data="childDataMetaPaginationData"
                              @size-change="getChildDataMetaTableData"
                              @current-change="getChildDataMetaTableData"
                              ref="childDataMetaTable">
                    </cb-table>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component, Prop, Watch} from 'vue-property-decorator'
    import {Mutation, State} from 'vuex-class'
    import config from '../../config.ts'

    @Component({})
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


        mounted() {
            this.getInfo();
            this.getDataMetaTableData();
            this.getChildDataTableData();
        }

        // 获取数据

        /**
         * 获取 数据集引用数据元
         */
        getDataMetaTableData() {
            let params = {
                current: this.dataMetaPaginationData['currentPage'],
                size: this.dataMetaPaginationData['pageSize'],
                id: this.arg.id,
                queryString: this.dataMetaSearchVal
            };
            let url = config.port('businessView') + '/elementList';
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
         * 获取 数据集子级
         */
        getChildDataTableData() {
            let params = {
                current: this.childDataPaginationData['currentPage'],
                size: this.childDataPaginationData['pageSize'],
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
                    const data = res.data[0];
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
    }
</style>
