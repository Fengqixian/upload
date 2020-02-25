<template>
    <div class="log-manage sheets-container header-operate-input">
        <cb-operation-container>
            <template slot="toOperation">
                <div class="operation">
                    <el-input placeholder="请输入日志标题" v-model.trim="searchModle.title" style="width:300px;"
                              @keydown.native.enter="search">
                        <el-button slot="append" icon="el-icon-search" @click.stop.prevent="search"></el-button>
                    </el-input>
                </div>
            </template>
            <template slot="cbContainer">
                <div class="table-wrapper ">
                    <cb-table :header-data="headerData"
                              :table-data="tableData"
                              :pagination-data="paginationData"
                              @size-change="handleSizeChange"
                              @current-change="handleCurrentChange">
                    </cb-table>
                </div>
            </template>
        </cb-operation-container>
    </div>


</template>

<script lang="ts">
    import Vue from 'vue'
    import Component from 'vue-class-component'
    import {Mutation, State} from 'vuex-class'

    @Component({
        name: 'logManage'
    })
    export default class LogManage extends Vue {
        @Mutation setLoadingFlag;
        @State pageSizes;
        searchModle = {
            title: ''
        };

        paginationData: object = {
            currentPage: 1,
            pageSize: 10,
            total: 1
        };
        tableData = [];
        headerData: Array<object> = [
            {
                label: '日志标题',
                prop: 'title'
            },
            {
                label: '创建者',
                prop: 'createBy'
            },
            {
                label: '操作提交的数据',
                prop: 'params'
            },
            {
                label: '服务ID',
                prop: 'serviceId'
            },
            {
                label: '执行时间',
                prop: 'time'
            },
            {
                label: '删除标记',
                prop: 'delFlag'
            },
            {
                label: '异常信息',
                prop: 'exception'
            },
            {
                label: '创建时间',
                prop: 'createTime'
            },
            {
                label: '更新时间',
                prop: 'updateTime'
            },
            {
                label: '操作IP地址',
                prop: 'remote_addr'
            },
            {
                label: '请求URI',
                prop: 'requestUri'
            },
            {
                label: '备注',
                prop: 'remark'
            },
            {
                label: '操作方式',
                prop: 'method'
            }
        ];

        created() {
            this.getData();
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
            // const {insInfo, tabInfo} = paginationData;
            // const tableData = await this.getTableData(tabInfo, insInfo, paginationData);
            // data.splice(0, data.length, ...tableData);
            this.getData();
        }

        /*   currentChange(currentPage) {
               this.paginationData['currentPage'] = currentPage;
               this.getData();
           }*/

        search() {
            this.paginationData['currentPage'] = 1;
            this.paginationData['pageSize'] = 10;
            this.paginationData['total'] = 1;
            this.getData();
        }

        public getData(): void {
            let url = '/MetadataManage/log/page';
            let params: object = {
                current: this.paginationData['currentPage'],
                size: this.paginationData['pageSize'],
            };
            if (this.searchModle['title'] !== '') {
                params = {...params, ...this.searchModle}
            }
            this.$http.get(url, {params}).then((response) => {
                const res = response.data.data
                this.tableData = res.records;
                this.paginationData['currentPage'] = res.current;
                this.paginationData['pageSize'] = res.size;
                this.paginationData['total'] = res.total;
            })
        }

    }
</script>

<style scoped lang="less">
    .header-operate-input {
        .operate {
            flex-direction: column;
            align-items: flex-start;
        }

        .input-search-wrapper {
            display: flex;
        }

        .container-wrapper {
            top: 35px;
        }
    }

    .log-manage {
        .table-wrapper {
            width: 100%;
            height: 100%;
            padding: 10px;
        }
    }

</style>
