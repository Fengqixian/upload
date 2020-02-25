<template>
    <div class="create-edit-metadata">
        <div class="create-edit-content">
            <div class="left">
                <div class="create-edit-operation">
                    <span class="label">元模型：</span>
                    <el-select v-model.trim="selectModel" clearable placeholder="请选择" style="margin-right:10px;">
                        <el-option
                                v-for="item in metadataOptions"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
                    <el-input placeholder="请输入内容" v-model.trim="searchModelValue" class="input-with-select">
                        <el-button slot="append" icon="el-icon-search"></el-button>
                    </el-input>
                </div>
                <div class="table-wrapper">
                    <cb-table selection></cb-table>
                </div>
            </div>
            <div class="center">
                <el-button class="icon-arrow-right"
                           type="primary"
                           disabled><i class="el-icon-arrow-right"></i>
                </el-button>
            </div>
            <div class="right">
                <div class="create-edit-operation">
                    <span class="label">元模型名称：</span>
                    <el-select v-model.trim="selectModel" clearable placeholder="请选择" style="margin-right:10px;">
                        <el-option
                                v-for="item in metadataOptions"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
                </div>
                <div class="table-wrapper">
                    <cb-table></cb-table>
                </div>
            </div>
        </div>
        <div class="create-edit-button">
            <el-button>审批</el-button>
            <el-button>确定</el-button>
            <el-button>取消</el-button>
        </div>
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component} from 'vue-property-decorator'

    /**
     * 创建、修改元数据
     */
    @Component({})
    export default class CreateEditMetadata extends Vue {
        name: string = 'CreateEditMetadata';
        public searchModelValue: string = ''; // 根据模型名称搜索模型
        public selectModel: string = ''; // 元模型选择
        // 元模型选择select options
        public metadataOptions: Array<object> = [
            {
                value: '选项1',
                label: '黄金糕'
            }, {
                value: '选项2',
                label: '双皮奶'
            }, {
                value: '选项3',
                label: '蚵仔煎'
            }, {
                value: '选项4',
                label: '龙须面'
            }, {
                value: '选项5',
                label: '北京烤鸭'
            }
        ]
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
                        width: 150px;
                        font-weight: 700;
                    }

                }

                .table-wrapper {
                    margin-top: 15px;
                    flex-grow: 1;
                    overflow: auto;
                    display: flex;
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
