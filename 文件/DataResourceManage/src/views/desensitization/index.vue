<template>
    <div class="desensitization">
        <cb-operation-container>
            <template slot="toOperation">
                <div class="operation">
                    <div class="title">脱敏策略</div>
                    <div class="right">
                        <el-input placeholder="请输入内容"
                                  v-model.trim="searchVal"
                                  size="mini"
                                  @keydown.native.enter.prevent="search">
                            <el-button slot="append"
                                       icon="el-icon-search"
                                       size="mini"
                                       @click="search"></el-button>
                        </el-input>
                        <el-button type="primary" size="mini" style="margin-left: 10px;"
                                   @click="$router.push('/editDesensitization')">添 加
                        </el-button>
                    </div>
                </div>
            </template>
            <template slot="cbContainer">
                <div class="container">
                    <div class="tree">
                        <el-row :gutter="20">
                            <el-col :md="12" :lg="8" :xl="6" v-for="(item,index) in tableData" :key="index">
                                <div class="wrapper">
                                    <item :treeData="item" @node-click="handleNodeClick"/>
                                </div>
                            </el-col>
                        </el-row>
                    </div>
                    <div class="pagination">
                        <el-pagination
                                @size-change="sizeChange"
                                @current-change="currentChange"
                                :current-page="paginationData.currentPage"
                                :page-sizes="pageSizes"
                                :page-size="paginationData.pageSize"
                                layout="total, sizes, prev, pager, next, jumper"
                                :total="paginationData.total">
                        </el-pagination>
                    </div>
                </div>
            </template>
        </cb-operation-container>
        <cb-side-info v-model.trim="isShowSlideSideInfo" title="详情">
            <ul>
                <template v-for="item in infoData">
                    <li :key="item.prop">
                        <span class="label">{{item.label}}：</span>
                        <template v-if="item.type=== 'translation'">
                            <span class="value">{{item.translations[item.value]}}</span>
                        </template>
                        <template v-else>
                            <span class="value">{{item.value}}</span>
                        </template>
                    </li>
                </template>
            </ul>
        </cb-side-info>
        <el-dialog :title="columnStrategyName" :visible.sync="columnVisible" width="900px">
            <cb-table :table-data="columnTableData"
                      :header-data="columnHeaderData"
                      :pagination-data="columnPaginationData"
                      @size-change="getColumnData"
                      @current-change="getColumnData">
            </cb-table>
            <div slot="footer" class="dialog-footer">
                <el-button @click="columnVisible = false">取 消</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component, Watch} from 'vue-property-decorator'
    import {State} from 'vuex-class'
    import Item from './item';

    @Component({
        components: {Item}
    })
    export default class Desensitization extends Vue {
        name: string = 'Desensitization';
        @State pageSizes;
        searchVal: string = ''; // 搜索值
        currentPage = 1; // 当前页数
        dialogVisible = true; // 对话框变量
        paginationData = {
            total: 1, // 总共数据
            pageSize: 10, // 每页显示条数
            currentPage: 1 // 当前页数
        };
        tableData = [];
        isShowSlideSideInfo = false; // 是否展示侧边栏信息
        ruleInfo = [
            {
                label: '名称',
                prop: 'ruleName',
                value: '',
            },
            {
                label: '状态',
                prop: 'status',
                value: '',
                type: 'translation',
                translations: {
                    1: '启用',
                    2: '禁用'
                }
            },
            {
                label: '加密/脱敏方式',
                prop: 'encryptionType',
                value: '',
                type: 'translation',
                translations: {
                    REG: '正则表达式',
                    AES: 'AES方式',
                    MD5: 'MD5方式',
                    BASE64: 'BASE64方式',
                    DES: 'DES方式',
                }
            },
            {
                label: '描述',
                prop: 'remarks',
                value: '',
            }
        ];
        infoData = [];
        // 更多查看字段
        columnVisible = false;
        columnRuleId = '';  // 规则id
        columnStrategyId = ''; // 策略标识
        columnStrategyName = ''; // 策略名称
        columnHeaderData = [
            {
                prop: 'columnNameCn',
                label: '中文名'
            },
            {
                prop: 'columnNameEn',
                label: '英文名'
            },
            {
                prop: 'tableNameCn',
                label: '所属表中文名'
            },
            {
                prop: 'tableNameEn',
                label: '所属表英文名'
            }
        ]; // table头部
        columnTableData = []; // table数据
        // table 分页
        columnPaginationData = {
            total: 1, // 总共数据
            pageSize: 10, // 每页显示条数
            currentPage: 1 // 当前页数
        };

        @Watch('columnVisible')
        columnVisibleChange(newVal) {
            if (!newVal) {
                this.columnRuleId = '';
                this.columnStrategyId = '';
                this.columnStrategyName = '';
            }
        };

        mounted() {
            this.getData();
        };

        /**
         * 搜索
         */
        search() {
            this.paginationData.currentPage = 1;
            this.getData();
        };

        /**
         * 每页展示个数发生变化
         */
        sizeChange(size) {
            this.paginationData.pageSize = size;
            this.getData();
        };

        /**
         * 当前页数发生变化
         */
        currentChange(current) {
            this.paginationData.currentPage = current;
            this.getData();
        };

        /**
         * 获取脱敏策略数据
         */
        getData() {
            let url = `/strategy/strategyInfo/pages`;
            let params = {
                size: this.paginationData.pageSize,
                current: this.paginationData.currentPage,
                type: 1,
                name: this.searchVal
            };
            this.$http.get(url, {params}).then(response => {
                if (response.data.code === 0) {
                    let data = response.data.data;
                    this.tableData = data.records;
                    this.paginationData.total = data.total;
                }
            })

        };

        /**
         * 节点点击
         * @param d
         */
        handleNodeClick(d) {
            // 展示规则详情
            if (d.nodeType === 'desensitization') {
                this.getRuleInfoData(d.id);
            } else if (d.nodeType === 'more') {
                this.columnRuleId = d.id;
                this.columnStrategyId = d.strategicCond;
                this.columnStrategyName = d.columnNameEn;
                this.columnVisible = true;
                this.columnPaginationData.currentPage = 1;
                this.getColumnData();
            }
        };

        /**
         * 获取规则详情
         * @param id
         */
        getRuleInfoData(id) {
            let ruleType = '1';
            let url = '/strategy/ruleInfo/getTypeRule';
            let params = {id, ruleType};
            this.$http.get(url, {params}).then(response => {
                if (response.data.code === 0) {
                    this.isShowSlideSideInfo = true;
                    let data = response.data.data;
                    this.infoData = [...this.ruleInfo];
                    this.infoData.map((item) => {
                        item.value = data[item.prop];
                    });
                    if(!data.encryptionType) return;
                    if (data.encryptionType === 'REG') {
                        this.infoData.splice(this.ruleInfo.length - 2, 0,
                            {
                                label: '正则表达式',
                                prop: 'ruleExpression',
                                value: data.ruleExpression,
                            }, {
                                label: '替换符',
                                prop: 'sign',
                                value: data.sign,
                            })
                    } else if (data.encryptionType.match(/ES$/)) {
                        this.infoData.splice(this.ruleInfo.length - 2, 0, {
                            label: '秘钥',
                            prop: 'sign',
                            value: data.sign,
                        })
                    }
                }
            })
        };

        /**
         * 获取策略规则对应的字段
         */
        getColumnData() {
            let url = '/strategy/strategyValue/page';
            let params = {
                ruleId: this.columnRuleId,
                strategyId: this.columnStrategyId,
                size: this.columnPaginationData.pageSize,
                current: this.columnPaginationData.currentPage,
            };
            this.$http.get(url, {params}).then(response => {
                if (response.data.code === 0) {
                    let data = response.data.data;
                    this.columnTableData = data.records;
                    this.columnPaginationData.total = data.total;
                }
            })

        };
    }
</script>

<style lang="less">
    .desensitization {
        .operation {
            display: flex;
            justify-content: space-between;
            align-items: center;

            .title {
                font-size: 14px;
                font-weight: 600;
            }

            .right {
                display: flex;
            }
        }

        .container {
            width: 100%;
            height: 100%;
            position: relative;

            .tree {
                position: absolute;
                top: 0;
                bottom: 42px;
                right: 0;
                left: 0;
                padding: 10px;
                overflow: auto;

                .wrapper {
                    height: 200px;
                    margin-bottom: 10px;
                    border: 1px solid #cccccc;
                    border-radius: 4px;
                }
            }

            .pagination {
                position: absolute;
                bottom: 0;
                right: 0;
                left: 0;
                padding: 10px;
                background-color: #fff;
                display: flex;
                justify-content: flex-end;
                box-shadow: 0 0 1px #cccccc;
            }
        }

        .cb-slide-side-info {
            .label {
                width: 90px;
            }
        }

        .el-dialog__body {
            height: 350px;
        }
    }
</style>
