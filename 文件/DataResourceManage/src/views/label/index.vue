<template>
    <div class="label">
        <cb-operation-container>
            <template slot="toOperation">
                <div class="operation">
                    <div class="top">
                        <div class="input-wrapper">
                            <div class="input">
                                <span>策略名称：</span>
                                <el-input
                                        placeholder="请输入内容"
                                        v-model.trim="ruleVal"
                                        @keydown.native.enter.prevent="getData"
                                        clearable>
                                </el-input>
                            </div>
                            <!-- <div class="input">
                                 <span>状态：</span>
                                 <el-select v-model="statusVal"
                                            clearable
                                            placeholder="请选择"
                                            @keydown.native.enter.prevent="getData">
                                     <el-option
                                             v-for="item in statusOptions"
                                             :key="item.value"
                                             :label="item.label"
                                             :value="item.value">
                                     </el-option>
                                 </el-select>
                             </div>-->
                            <el-button type="primary" @click="getData" style="margin-left: 10px;">查 询</el-button>
                        </div>
                        <div class="btn">
                            <!--<el-button>重 置</el-button>-->
                            <el-button type="primary" @click="$router.push('/editLabel')">添 加</el-button>
                            <!--<el-button>批量操作</el-button>-->
                            <!--<el-button>删 除</el-button>-->
                            <!--<el-button>授 权</el-button>-->
                        </div>
                    </div>
                </div>
            </template>
            <template slot="cbContainer">
                <div class="table">
                    <cb-table :table-data="tableData"
                              :header-data="headerData"
                              :pagination-data="paginationData"
                              @size-change="getData"
                              @current-change="getData"> >
                        <template slot="column">
                            <el-table-column
                                    label="操作"
                                    width="100">
                                <template slot-scope="{row}">
                                    <!--<el-button @click="handleClick(scope.row)" type="text" size="small">查看</el-button>-->
                                    <el-button type="text" size="small"
                                               @click="$router.push({path:'/editLabel',query:{id:row.id}})">编辑
                                    </el-button>
                                </template>
                            </el-table-column>
                        </template>
                    </cb-table>
                </div>
            </template>
        </cb-operation-container>
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component} from 'vue-property-decorator'

    @Component({})
    export default class Label extends Vue {
        name: string = 'Label';

        ruleVal = '';
        statusVal = '';
        statusOptions = [
            {
                label: '禁用',
                value: 0
            },
            {
                label: '启用',
                value: 1
            },
        ];
        headerData = [
            {
                prop: 'id',
                label: '编号'
            },
            {
                prop: 'name',
                label: '名称'
            }, {
                prop: 'status', //  状态 1：启用 2：停用
                label: '状态',
                type: 'tag',
                translations: {
                    1: '启用',
                    2: '禁用'
                },
                styles: {
                    1: 'success',
                    2: 'error'
                }
            },
            {
                prop: 'createTime',
                label: '创建时间'
            },
            {
                prop: 'remarks',
                label: '描述'
            },
        ];
        tableData = [
            /* {
                 id: '1',
                 name: 'TradeCode1',
                 desc: '米哦按花素'
             },*/
        ];
        paginationData = {
            total: 1, // 总共数据
            pageSize: 10, // 每页显示条数
            currentPage: 1 // 当前页数
        };

        mounted() {
            this.getData();
        };

        getData() {
            let url = `/strategy/strategyInfo/page`;
            let params = {
                current:this.paginationData.currentPage,
                size:this.paginationData.pageSize,
                name: this.ruleVal,
                status: this.statusVal,
            };
            this.$http.get(url, {params}).then(response => {
                if (response.data.code === 0) {
                    let data = response.data.data;
                    this.tableData = data.records;

                    this.paginationData.total = data.total;
                    this.paginationData.currentPage = data.current;
                }
            })

        }
    }
</script>

<style scoped lang="less">
    .label {
        .operation {
            display: flex;
            flex-direction: column;

            .top {
                display: flex;
                align-items: center;
                justify-content: space-between;

                .input-wrapper {
                    display: flex;
                    align-items: center;

                    .input {
                        display: flex;
                        align-items: center;

                        & > span {
                            width: 68px;
                            text-align: end;
                        }

                        & > div {
                            flex: 1;
                        }
                    }
                }

                .btn {
                    margin-left: 10px;
                }
            }
        }

        .table {
            width: 100%;
            height: 100%;
        }
    }
</style>
