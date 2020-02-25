<template>
    <div class="examination-approval-metadata">
        <cb-operation-container>
            <template slot="toOperation">
                <div class="operation">
                    <el-select v-model.trim="selectModel"
                               style="width: 300px !important;"
                               placeholder="请选择"
                               @change="standardSelectChange">
                        <el-option
                                v-for="item in standardSelectOptions"
                                :key="item.nameEn"
                                :label="item.nameCn"
                                :value="item.resourceId">
                        </el-option>
                    </el-select>
                    <el-input placeholder="请输入内容"
                              v-model.trim="standardSearchVal"
                              style="width: 300px;margin-left: 10px;"
                              @keydown.native.enter="searchStandard">
                        <el-button slot="append" icon="el-icon-search" @click.stop.prevent="searchStandard"></el-button>
                    </el-input>
                    <el-button class="del-nobg"
                               style="margin-left: 10px;"
                               @click.stop.prevent="examinationMetadata(2)">
                        不通过
                    </el-button>
                    <el-button
                            class="confirm-bg"
                            style="margin-left: 10px;"
                            type="primary"
                            @click.stop.prevent="examinationMetadata(1)">通 过
                    </el-button>
                </div>
            </template>
            <template slot="cbContainer">
                <div class="container">
                    <cb-table selection
                              :headerData="standardHeaderData"
                              :tableData="standardTableData"
                              :paginationData="standardPaginationData"
                              @selection-change="selectionChange"
                              @size-change="handleSizeChange"
                              @current-change="handleCurrentChange"
                              ref="standardTable">
                    </cb-table>
                </div>
            </template>
        </cb-operation-container>
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component} from 'vue-property-decorator'
    import config from "../../config";

    @Component({})
    export default class ExaminationApprovalMetadata extends Vue {
        name: string = 'ExaminationApprovalMetadata';
        /**
         * 搜索区域成员变量
         */
        public selectModel: string = ''; // select选择的根模型
        public standardSelectOptions: Array<object> = []; // select选择的选项
        public standardSearchVal: string = ''; // 搜索框搜索值


        // 存放跟模型table头信息
        public standardHeaderData: Array<object> = [];
        public standardTableData: Array<object> = [];
        // 存放根模型基本的头部信息
        public baseStandardTableHeaderData: Array<object> = [
            {
                prop: 'nameCn',
                label: '中文名称'
            },
            {
                prop: 'nameEn',
                label: '英文名称'
            },
            {
                prop: 'status',
                label: '状态',
                type: 'tag',
                translations: {
                    0: '待审批',
                    1: '正常'
                },
                styles: {
                    0: 'info',
                    1: 'success'
                }
            },
            {
                prop: 'modelType',
                label: '模型类型'
            },
            /*{
                prop: 'isStandard',
                label: '是否根模型',
                type: 'switch'
            },*/
            {
                prop: 'createTime',
                label: '创建时间'
            },
            {
                prop: 'updateTime',
                label: '更新时间'
            }
        ];
        // 存放根模型分页
        public standardPaginationData: object = {
            total: 1,
            pageSize: 10,
            currentPage: 1
        };
        public selectedData: Array<object> = []; // 已经选择的根模型中的数据


        public mounted(): void {
            this.getStandardModel();
        }


        /**
         * 搜索框搜索根模型元数据
         */
        public searchStandard(): void {
            this.standardPaginationData['currentPage'] = 1;
            this.getTableData(this.selectModel, this.standardSearchVal).then(res => this.standardTableData = res);
        }

        /**
         * 审批
         * @param status 审批状态 1为审批通过 2 为审批不通过
         */
        public examinationMetadata(status): void {
            if (!this.selectedData.length) {
                this.$message.warning('请选择审批对象')
                return;
            }
            let url = config.port('metadatavalue') + '/auditMetadata';
            const selectedData = JSON.parse(JSON.stringify(this.selectedData));
            selectedData.map(item => item['status'] = status);
            this.$http.put(url, selectedData).then(response => {
                this.$message.success('审批成功！');
                this.standardPaginationData['currentPage'] = 1;
                this.getTableData(this.selectModel).then(res => {
                    this.standardTableData = res;
                })
            })
        }


        /**
         * 获取根模型数据
         */
        public getStandardModel(): void {
            let url = config.port('metadatamodel') + '/getModelInfo';
            let params = {
                isStandard: true, // 获取根模型的参数
                size: -1
            };
            this.$http.get(url, {params}).then(response => {
                this.standardSelectOptions = response.data.data.records;
                if (this.standardSelectOptions[0]) {
                    this.selectModel = this.standardSelectOptions[0]['resourceId'];
                    this.standardSelectChange(this.selectModel);
                }
            })
        }

        /**
         * 选择根模型选择框选中值发生变化时触发
         * @param data 目前的选中值
         */
        public async standardSelectChange(data): Promise<void> {
            // 获取根模型头部信息
            this.standardHeaderData = [...this.baseStandardTableHeaderData, ...await this.getTableHeaderData(data)];
            this.standardHeaderData.map(item => item['standardData'] = data);
            // 获取根模型table体数据
            this.standardPaginationData['currentPage'] = 1;
            this.standardSearchVal = '';
            this.standardTableData = await this.getTableData(data);
        }


        /**
         * 获取根模型table头部信息(根模型下的元数据)
         * @param data 当前node节点的数据
         * return 获取根模型table头部信息(根模型下的元数据)
         */
        public async getTableHeaderData(data): Promise<Array<object>> {
            let URL = config.port('metadataproperties') + '/page';
            const params = {
                modelResourceId: typeof data === 'string' ? data : data['modelId']
            };
            let headerData: Array<object> = [];
            await this.$http.get(URL, {params}).then((response) => {
                headerData = response.data.data.records;
                headerData.map(item => {
                    item['label'] = item['nameCn'];
                    item['prop'] = item['mappingField'];
                })
            });
            return headerData;
        }

        /**
         * 获取table体数据
         * @param data 当前node节点的数据nameCn
         * @param nameCn 根据当nameCn搜索
         * return table 体数据
         */
        public async getTableData(data, nameEn?) {
            let url = config.port('metadatavalue') + '/page';
            let params = {
                modelId: typeof data === 'string' ? data : data['resourceId'],
                isStandard: true,
                status: 0,
                current: this.standardPaginationData['currentPage'], // 当前显示的页数
                size: this.standardPaginationData['pageSize'], // 当前页数显示的条数
            };
            // 参数含有nameCn 代表根据 nameCn 搜索
            if (nameEn) {
                params['nameEn'] = nameEn;
            }
            let tableData: Array<object> = [];

            await this.$http.get(url, {params}).then(response => {
                const res = response.data.data;
                tableData = res.records;
                // 更新分页信息
                this.standardPaginationData['currentPage'] = res.current;
                this.standardPaginationData['total'] = res.total;
            });
            return tableData;
        }


        /**
         * 当选择项发生变化时会触发该事件
         * @param selection
         */
        public selectionChange(selection): void {
            this.selectedData = selection;
        }

        /**
         * 分页查询 pageSize 改变时会触发
         * @param paginationData 分页的数据
         * @param headerData table头部数据
         * @param tableData table数据
         */
        public handleSizeChange(num, paginationData, headerData, tableData): void {
            this.handleCurrentChange(num, paginationData, headerData, tableData);
        }

        /**
         * 分页查询 currentPage 改变时会触发
         * @param paginationData 分页的数据
         * @param headerData table头部数据
         * @param tableData table数据
         */
        public handleCurrentChange(num, paginationData, headerData, tableData): void {
            if (this.standardSearchVal === '') {
                this.getTableData(headerData[0]['standardData']).then(res => this.standardTableData = res);
            } else {
                this.getTableData(headerData[0]['standardData'], this.standardSearchVal).then(res => this.standardTableData = res);
            }
        }
    }
</script>

<style scoped lang="less">
    .examination-approval-metadata {
        .operation {
            display: flex;
        }

        .container {
            padding: 10px;
            width: 100%;
            height: 100%;
        }
    }
</style>
