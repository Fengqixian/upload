<template>
    <div class="cb-card-relation">
        <el-dialog :title="infoRelationInfoData.nameCn"
                   :visible.sync="infoRelationVisible"
                   :close-on-click-modal="false"
                   width="90%"
                   class="info-relation">
            <div class="info-relation-wrapper">
                <div class="info-wrapper">
                    <template v-if="viewType === 'technology'">
                        <ul>
                            <li>
                                <span class="label">中文名称：</span>
                                <span class="value">{{infoRelationInfoData.nameCn}}</span>
                            </li>
                            <li>
                                <span class="label">英文名称：</span>
                                <span class="value">{{infoRelationInfoData.nameEn}}</span>
                            </li>
                            <li>
                                <span class="label">类型：</span>
                                <span class="value">{{infoRelationInfoData.dataType}}</span>
                            </li>
                            <li>
                                <span class="label">备注：</span>
                                <span class="value">{{infoRelationInfoData.comment}}</span>
                            </li>
                        </ul>
                    </template>
                    <template v-else>
                        <ul>
                            <li>
                                <span class="label">名称：</span>
                                <span class="value">{{infoRelationInfoData.nameCn}}</span>
                            </li>
                            <li>
                                <span class="label">资源编码：</span>
                                <span class="value">{{infoRelationInfoData.resourceCode}}</span>
                            </li>
                            <li>
                                <span class="label">数据元分类：</span>
                                <span class="value">{{infoRelationInfoData.category}}</span>
                            </li>
                            <li>
                                <span class="label">定义：</span>
                                <span class="value">{{infoRelationInfoData.definition}}</span>
                            </li>
                        </ul>
                    </template>
                    <div class="table" v-if="infoRelationTableData.length">
                        <el-table :data="infoRelationTableData" border>
                            <el-table-column
                                    prop="Code"
                                    label="编号"
                                    width="60">
                            </el-table-column>
                            <el-table-column
                                    prop="Name"
                                    label="允许值">
                            </el-table-column>
                        </el-table>
                    </div>
                </div>
                <div class="relation-wrapper" ref="relationWrapper">
                    <relation-cb v-if="infoRelationVisible"
                                 :arg="hxArg"
                                 :linkClick="relationClick"
                                 :viewType="viewType"/>
                </div>
            </div>
        </el-dialog>

        <el-dialog title="ETL"
                   :visible.sync="infoRelationETLVisible"
                   :close-on-click-modal="false"
                   style="z-index: 3000"
                   class="relation-wrapper-ETL">
            <relation-cb v-if="infoRelationETLVisible"
                         :arg="ETLhxArg"
                         :detail="true"/>
        </el-dialog>
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component, Watch, Model, Prop} from 'vue-property-decorator'
    import config from "../../config";
    import relationCb from "./relationCb.vue";
    import BusinessViewRelationClass from '../../common/js/BusinessViewRelationClass'

    @Component({
        components: {relationCb},
    })
    export default class CbCardRelation extends Vue {
        name: string = 'CbCardRelation';
        @Model('change') relationData;
        @Prop()
        viewType;
        infoRelationInfoData: object = {};
        infoRelationVisible: boolean = false;
        infoRelationTableData: Array<any> = [];

        hxArg: object = {};
        ETLhxArg: object = {};


        infoRelationETLVisible: boolean = false;

        @Watch('relationData')
        relationDataChange(data) {
            if (data) {
                this.hxArg = data;
                this.getInfoRelationInfoData(data)
            }
        }

        @Watch('infoRelationVisible')
        infoRelationVisibleChange(data) {
            if (!data) {
                this.$emit('change', null)
            }
        }

        /**
         * 获取血缘详情找中的详情
         */
        getInfoRelationInfoData(data) {
            if (this.viewType === 'technology') {
                let url = config.port('technologyView') + '/treeNodeInfo';
                let params = {
                    resourceId: data.resourceId,
                    type: 'column'
                };
                this.$http.get(url, {params}).then(response => {
                    if (response.data.code === 0) {
                        this.infoRelationVisible = true;
                        let res = response.data.data;
                        this.infoRelationInfoData = res;
                        if (res.rangeList && res.rangeList.Members) {
                            this.infoRelationTableData = res.rangeList.Members;
                        }

                    }
                })
            } else {
                let url = config.port('businessView') + '/elementInfo';
                let params = {
                    id: data.id,
                    current: 1,
                    size: 1000,
                };
                this.$http.get(url, {params}).then(response => {
                    if (response.data.code === 0) {
                        this.infoRelationVisible = true;
                        let res = response.data.data;
                        this.infoRelationInfoData = res;
                        if (res.rangeList && res.rangeList.Members) {
                            this.infoRelationTableData = res.rangeList.Members;
                        }

                    }
                })
            }
        }

        /**
         * 华西血缘线点击
         */
        relationClick(data) {
            if (data.type === 'ETL') {
                this.infoRelationETLVisible = true;
                this.ETLhxArg = data;
            }
        }

    }
</script>

<style lang="less">
    .cb-card-relation {


        .el-dialog {
            background-color: #F0F0F0;

            .el-dialog__header {
                background-color: #0E183F;

                .el-dialog__title {
                    color: #fff;
                }
            }

            .el-dialog__body {
                overflow: auto;
            }
        }

        .info-relation {
            .el-dialog {
                display: flex;
                flex-direction: column;
                height: 90%;
                margin-top: 2% !important;

                .el-dialog__body {
                    flex: 1;
                    background-color: #0e183f;
                }
            }

            .info-relation-wrapper {
                width: 100%;
                height: 100%;
                padding: 10px;
                position: relative;

                .info-wrapper {
                    position: absolute;
                    top: 10px;
                    bottom: 0;
                    left: 0;
                    width: 250px;
                    overflow: auto;

                    .title {
                        text-align: center;
                        font-size: 2rem;
                        font-weight: 600;
                        margin-bottom: 20px;
                    }

                    li {
                        margin-bottom: 10px;
                        display: flex;

                        span {
                            display: flex;
                            align-items: center;
                            color: #fff;
                        }

                        .label {
                            width: 73px;
                            margin-right: 2px;
                        }

                        .value {
                            flex: 1;
                        }
                    }
                }

                .table {
                    width: 233px;

                    .el-table {
                        .el-table__header-wrapper {

                        }

                        th {
                            background-color: #20294c;
                            color: #ffffff;
                            border-color: #5D6587;
                        }

                        tr {
                            background-color: #0E183F;

                            td {
                                color: #ffffff;
                                border-color: #5D6587;
                            }
                        }

                        .el-table__body tr:hover {
                            & > td {
                                background-color: #21294C;
                            }
                        }

                        &.el-table--border,
                        &.el-table--group,
                        &.el-table--border::after,
                        &.el-table--group::after,
                        &.el-table::before {
                            border-color: #5D6587;
                            background-color: #5D6587;
                        }

                    }
                }

                .relation-wrapper {
                    position: absolute;
                    top: 10px;
                    right: 0;
                    bottom: 0;
                    left: 250px;
                }
            }
        }


        .relation-wrapper-ETL {
            .etl-wrapper {
                width: 100%;
                height: 100%;
                overflow: auto;
            }

            .el-dialog {
                display: flex;
                flex-direction: column;
                height: 80%;
                width: 870px;
                margin-top: 5% !important;

                .el-dialog__body {
                    flex: 1;
                    background-color: #0e183f;
                }
            }
        }
    }
</style>
