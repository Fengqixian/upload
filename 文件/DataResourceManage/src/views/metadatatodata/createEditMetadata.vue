<template>
    <div class="create-edit-metadata">
        <div class="create-edit-content">
            <div class="left">
                <div class="create-edit-operation">
                    <span class="label">根元模型：</span>
                    <div class="input-wrapper">
                        <el-select v-model.trim="selectModel"
                                   placeholder="请选择"
                                   style="margin-right:10px;"
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
                                  class="input-with-select"
                                  @keydown.native.enter="searchStandard">
                            <el-button slot="append" icon="el-icon-search"
                                       @click.stop.prevent="searchStandard"></el-button>
                        </el-input>
                    </div>
                </div>
                <div class="table-wrapper">
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
            </div>
            <div class="center">
                <el-button class="icon-arrow-right"
                           type="primary"
                           :disabled="moveDisabled"
                           @click="moveStandardMetadataToCreateMetadata"><i class="el-icon-arrow-right"></i>
                </el-button>
            </div>
            <div class="right">
                <div class="create-edit-operation">
                    创建元数据
                </div>
                <div class="table-wrapper">
                    <cb-table
                            :headerData="createHeaderData"
                            :tableData="createTableData"
                            :is-operation="true"
                            tableInput>
                        <template slot="column">
                            <el-table-column
                                    fixed="right"
                                    label="操作"
                                    width="120">
                                <template slot-scope="{row,$index}">
                                    <el-button
                                            type="text"
                                            size="small"
                                            @click="deleteCreateMetaData(row,$index)">
                                        移除
                                    </el-button>
                                </template>
                            </el-table-column>
                        </template>
                        <template slot="operation">
                            <el-button @click="addRowMetadata">新增一行</el-button>
                        </template>
                    </cb-table>
                </div>
            </div>
        </div>
        <div class="create-edit-button">
            <el-button @click="cancelCreateMetadata">取消</el-button>
            <el-button type="primary" @click="submit">确定</el-button>
        </div>
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component, Prop} from 'vue-property-decorator'
    import config from "../../config";

    /**
     * 创建、修改元数据
     */
    @Component({})
    export default class CreateEditMetadata extends Vue {
        name: string = 'CreateEditMetadata';
        /**
         * 父组件传递来的数据
         */
        @Prop() // 被创建的元数据的元模型数据
        mateModelData;
        @Prop() // 被创建的元数据的元模型数据
        mateModelNode;


        /**
         * 公共区域
         */
        public moveDisabled: boolean = true; // 能否将选中的根模型中的元数据移动到创建的元模型中
        public selectedData: Array<object> = []; // 已经选择的根模型中的数据


        /**
         * 根模型区域
         */
        public standardSearchVal: string = ''; // 根据模型名称搜索模型
        public selectModel: string = ''; // 元模型选择
        public standardSelectOptions: Array<object> = [];  // 存放根模型数据
        // public headerData
        // 存放根模型分页
        public standardPaginationData: object = {
            total: 1,
            pageSize: 10,
            currentPage: 1
        };
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
            // {
            //     prop: 'modelType',
            //     label: '模型类型'
            // },
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

        /**
         * 被创建元数据区域
         */
            // 存放跟模型table头信息
        public createHeaderData: Array<object> = [];
        public createTableData: Array<object> = [];
        public baseCreateTableHeaderData: Array<object> = [
            {
                prop: 'nameCn',
                label: '中文名称'
            },
            {
                prop: 'nameEn',
                label: '英文名称'
            },
            // {
            //     prop: 'modelType',
            //     label: '模型类型'
            // },
            /*{
                prop: 'isStandard',
                label: '是否根模型',
                type: 'switch'
            }*/
        ];

        public mounted(): void {
            this.getStandardModel();
            // this.getCreatedMetadata();
        }

        /**
         * @param row 移出那一行的数据
         * @param index 移出那一行数据在createTableData的索引值
         */
        public deleteCreateMetaData(row, index): void {
            this.createTableData.splice(index, 1);
        }

        /**
         * 搜索框搜索根模型元数据
         */
        public searchStandard(): void {
            this.standardPaginationData['currentPage'] = 1;
            this.getTableData(this.selectModel, this.standardSearchVal).then(res => this.standardTableData = res);
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

        /**
         * 取消创建
         */
        public cancelCreateMetadata(): void {
            this.$emit('cancel')
        }

        /**
         * 清空根模型所有的选择
         */
        public clearSelectionStandardTable(): void {
            this.$refs.standardTable['clearSelection']();
        }


        /**
         * 创建元数据
         */
        public submit(): void {
            // 处理参数
            let submitData: Array<object> = JSON.parse(JSON.stringify(this.createTableData));
            if (submitData[submitData.length - 1]['nameEn'] === '') {
                submitData.splice(submitData.length - 1);
            }
            submitData.forEach(item => {
                item['status'] = 0;
                item['modelType'] = this.mateModelData['modelType'];
                item['modelId'] = typeof this.selectModel === 'string' ? this.selectModel : this.selectModel['resourceId'];
                // item['isStandard'] = false;
            });
            let url = config.port('metadatavalue') + '/saveBath';
            this.$http.put(url, submitData).then(response => {
                // 创建元数据成功，将成功事件派发出去，并将创建的数据一并带出去
                this.$emit('created-success', submitData)
            })
        }

        /**
         * 在创建元数据table中新增一行空数据
         */
        public addRowMetadata(): void {
            let newMetadata = {
                parentId: this.mateModelNode.level > 2 ? this.mateModelNode.parent.data.resourceId : ''
            };
            this.standardHeaderData.forEach(item => item['type'] === 'switch' ? false : newMetadata[item['prop']] = '');
            for (let i = 0; i < this.standardHeaderData.length; i++) {
                if (this.standardHeaderData[i]['nameEn'] === '') {
                    this.$message.warning('填写完数据后再新增！');
                }
            }
            this.createTableData.push(newMetadata);
        }

        /**
         * 将选择的根模型元数据移到创建的元数据table中
         */
        moveStandardMetadataToCreateMetadata() {
            for (let column of this.selectedData) {
                const flag = this.createTableData.find(item => item['nameEn'] === column['nameEn']);
                if (flag === undefined) {
                    this.createTableData.push(JSON.parse(JSON.stringify(column)));
                }
            }
            // 清空根模型所有的选择
            this.clearSelectionStandardTable();
        }

        /**
         * 当选择项发生变化时会触发该事件
         * @param selection
         */
        public selectionChange(selection): void {
            this.selectedData = selection;
            if (selection.length) {
                this.moveDisabled = false;
            } else {
                this.moveDisabled = true;
            }
        }


        /**
         * 获取已经添加的元数据
         */
        public async getCreatedMetadata(): Promise<void> {
            // // 获取根模型头部信息
            // this.createHeaderData = [...this.baseStandardTableHeaderData, ...await this.getTableHeaderData(this.mateModelData)];
            // // 获取根模型table体数据
            // this.createTableData = await this.getTableData(this.mateModelData);
        }


        /**
         * 获取根模型数据
         */
        public getStandardModel(): void {
            let url = config.port('metadatamodel') + '/getModelInfo';
            let params = {
                isStandard: true, // 获取根模型的参数
                modelType: this.mateModelData['modelType'],
                size: -1
            };
            this.$http.get(url, {params}).then(response => {
                this.standardSelectOptions = response.data.data.records;
                if (this.standardSelectOptions.length === 0) return;
                this.selectModel = this.standardSelectOptions[0]['resourceId'];
                this.standardSelectChange(this.selectModel);
            })
        }

        /**
         * 选择根模型选择框选中值发生变化时触发
         * @param data 目前的选中值
         */
        public async standardSelectChange(data): Promise<void> {
            // 获取根模型头部信息
            this.standardHeaderData = [...JSON.parse(JSON.stringify(this.baseStandardTableHeaderData)), ...await this.getTableHeaderData(data)];
            this.standardHeaderData.map(item => {
                item['width'] = '200';
                item['standardData'] = data;
            });
            this.createHeaderData = [...JSON.parse(JSON.stringify(this.baseCreateTableHeaderData)), ...await this.getTableHeaderData(data)];
            this.createHeaderData.map(item => item['width'] = '200');
            // 获取根模型table体数据
            this.standardPaginationData['currentPage'] = 1;
            this.standardSearchVal = '';
            this.standardTableData = await this.getTableData(data);
            this.createTableData = [];
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
         * @param data 当前node节点的数据
         * return table 体数据
         */
        public async getTableData(data, nameEn?) {
            let url = config.port('metadatavalue') + '/page';
            let params = {
                modelId: typeof data === 'string' ? data : data['resourceId'],
                isStandard: true,
                status: 1,
                current: this.standardPaginationData['currentPage'], // 当前显示的页数
                size: this.standardPaginationData['pageSize'], // 当前页数显示的条数
            };
            let tableData: Array<object> = [];
            // 参数含有 nameEn 代表根据 nameEn 搜索
            if (nameEn) {
                params['nameEn'] = nameEn;
            }

            await this.$http.get(url, {params}).then(response => {
                const res = response.data.data;
                tableData = res.records;
                // 更新分页信息
                this.standardPaginationData['currentPage'] = res.current;
                this.standardPaginationData['total'] = res.total;
            });
            return tableData;
        }

    }
</script>

<style scoped lang="less">
    .create-edit-metadata {
        height: 100%;
        width: 100%;
        display: flex;
        flex-direction: column;

        // 按钮
        .create-edit-button {
            display: flex;
            justify-content: flex-end;
        }

        // 新增修改内容
        .create-edit-content {
            flex-grow: 1;
            margin-bottom: 10px;
            display: flex;

            .left, .right {
                display: flex;
                flex-direction: column;
                box-shadow: 0 0 3px #606060;
                position: absolute;
                top: 0;
                bottom: 50px;

                .create-edit-operation {
                    display: flex;
                    align-items: center;
                    justify-content: flex-start;
                    margin-bottom: 10px;
                    background-color: #EBEEF5;
                    height: 50px;
                    padding: 10px;

                    .label {
                        width: 67px;
                        font-weight: 700;
                    }

                    .input-wrapper {
                        display: flex;
                        align-items: center;
                        justify-content: flex-start;

                        .el-input-group {
                            /*width: 70%;*/
                        }
                    }

                    /*.el-select, .input-with-select {*/
                    /*flex: 1;*/
                    /*}*/

                }

                .table-wrapper {
                    margin-top: 15px;
                    position: absolute;
                    left: 0;
                    right: 0;
                    bottom: 0;
                    top: 50px;
                    padding: 10px;
                }
            }


            .left {
                left: 20px;
                right: 53%;
            }

            .center {
                left: 47%;
                right: 47%;
                position: absolute;
                top: 0;
                bottom: 10px;
                display: flex;
                align-items: center;
                justify-content: center;

                .icon-arrow-right {
                    height: 50px !important;
                    width: 50px !important;

                    i {
                        font-size: 25px;
                    }

                    border-radius: 50%;
                }
            }

            .right {
                right: 20px;
                left: 53%;

                .create-edit-operation {
                    .label {
                        width: 100px;
                    }
                }
            }
        }


    }
</style>
