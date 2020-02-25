<template>
    <div class="metadata-presentation-tree-map"
         :style="{
                   transform: `scale3d(${scale}, ${scale}, 1)`,
                   opacity:opacity
         }">
        <div class="left" :style="{right:detailData.length?'290px':'0'}">
            <el-row :gutter="12" class="table-wrapper">
                <el-col
                        :xs="24" :sm="12" :md="8" :lg="8" :xl="6"
                        v-for="item in data"
                        :key="item.uuid">
                    <div class="item" @click="cardClick(item)">
                        <div class="title">{{item.nameCn}}</div>
                        <ul>
                            <li>
                                <span class="label"><i>英</i><i>文</i><i>名</i><i>称</i><i>：</i></span>
                                <el-tooltip class="item" effect="light" :content="item.nameEn"
                                            placement="top-start">
                                    <span class="value">{{item.nameEn}}</span>
                                </el-tooltip>
                            </li>
                            <li>
                                <span class="label"><i>资</i><i>源</i><i>ID</i><i>：</i></span>
                                <el-tooltip class="item" effect="light" :content="item.resourceId"
                                            placement="top-start">
                                    <span class="value">{{item.resourceId}}</span>
                                </el-tooltip>
                            </li>
                            <li>
                                <span class="label"><i>状</i><i>态</i><i>：</i></span>
                                <el-tooltip class="item" effect="light" :content="item.status"
                                            placement="top-start">
                                    <span class="value">{{item.status}}</span>
                                </el-tooltip>
                            </li>
                        </ul>
                        <div class="button btn-right" v-if="item.type === 'INS'">
                            <el-button style="margin-left: 10px;"
                                       class="add-bg"
                                       @click.stop.prevent="showChildrenData(item)">查 看
                            </el-button>
                            <el-button style="margin-left: 10px;"
                                       class="add-nobg"
                                       @click.stop.prevent="detailClick(item)">详 情
                            </el-button>
                        </div>
                    </div>
                </el-col>
            </el-row>
        </div>
        <transition name="right">
            <div class="right" v-if="detailData.length">
                <h3 class="title"
                    v-for="(item,index) in detailData"
                    :key="index"
                    v-if="item.key === 'nameCn'">{{item.value}}详情</h3>
                <ul class="form-list">
                    <li class="header">
                        <span class="attr">属 性</span>
                        <span class="desc"><i>描 述</i></span>
                    </li>
                    <li class="data"
                        v-for="(item,index) in detailData"
                        :key="index">
                        <span class="attr">{{item.label}}</span>
                        <span class="desc">
                            {{item.value}}
                        </span>
                    </li>
                </ul>
            </div>
        </transition>
        <!-- 底部展示下级模型和实例数据 -->
        <transition name="fade">
            <div class="fade-wrapper" v-if="isShowFade && tabsData.length">
                <i class="el-icon-close icon" @click.stop.prevent="closeFade"></i>
                <cb-tab :closable="false"
                        v-if="tabsData.length"
                        :closes="false"
                        :tabData="tabsData"
                        v-model.trim="activeName">
                    <template :slot="tab.name" v-for="tab in tabsData">
                        <cb-table :headerData="tab.headerData"
                                  :tableData="tab.tableData"
                                  :paginationData="tab.paginationData"
                                  @size-change="handleSizeChange"
                                  @current-change="handleCurrentChange">
                        </cb-table>
                    </template>
                </cb-tab>
            </div>
        </transition>
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component, Prop, Watch} from 'vue-property-decorator'
    import {Action, State, Mutation} from 'vuex-class'
    import config from "../../config";

    @Component({})
    export default class TreeMap extends Vue {
        name: string = 'TreeMap';
        isShowFade: boolean = false;  // 是否展示下级模型和实例数据
        @State baseField;   // 基本的字段
        @State baseTableHeaderData;   // table 头部基本的展示字段
        @Action getMetadataProperties;  // 查寻字段的属性
        @Mutation letFieldsAssignments; // 字段赋值  这个用于tab头部详情展示  将值和字段名组装在一起

        /**
         * X轴移动
         */
        @Prop({type: String, default: '0'})
        translateX;
        @Prop({type: Boolean, default: false})
        transition;
        @Prop({type: String, default: 1})
        opacity;
        @Prop({type: String, default: 1})
        scale;
        @Prop()
        data; // card数据

        /**
         * 存储详情数据
         */
        detailData: Array<object> = [];
        detailCardData: object = {};
        isRefresh: boolean = false; // 属性是不是需要刷新


        /**
         * tabs数据
         */
        tabsData: Array<object> = [];
        activeName: string = ''; // 激活tab

        @Watch('data')
        public dataChange(): void {
            if (this.isRefresh) {
                this.detailClick(this.detailCardData); // 刷新属性详情
                this.isRefresh = false;
            } else {
                this.detailData = [];
                this.tabsData = [];
                this.isShowFade = false;
            }
        }

        /**
         * 获取详情事件
         * @param data
         */
        public async detailClick(data) {
            const attrs: Array<object> = await this.getMetadataProperties(data['modelId']);
            const values: object = await this.getDetailValue(data);
            this.detailData = [...JSON.parse(JSON.stringify(this.baseField)), ...attrs];
            this.letFieldsAssignments({fields: this.detailData, values});
            this.detailCardData = data;
        }

        /**
         * 获取详情的值
         * @param data
         * return 获取详情值的数据集
         */
        public async getDetailValue(data): Promise<object> {
            let url = config.port('metadatavalue') + '/page';
            let params = {
                resourceId: data['resourceId']
            };
            let retArr: Array<object> = [];
            await this.$http.get(url, {params}).then(response => {
                retArr = response.data.data.records[0];
            });
            return retArr;
        }

        /**
         * 关闭tab
         */
        public closeFade(): void {
            this.isShowFade = false;
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
            const {insInfo, tabInfo} = paginationData;
            const tableData = await this.getTableData(tabInfo, insInfo, paginationData);
            data.splice(0, data.length, ...tableData);
        }

        /**
         * 展示下级模型和实例数据
         * @param data 当前被点击的card数据
         */
        async showChildrenData(data) {
            const tabs: Array<object> = await this.getTabsData(data);
            if (tabs.length === 0) return this.$message.warning('暂无数据！');
            for (let tab of tabs) {
                // label tab显示名字  name tab的标识 也是tab激活的标识
                tab['label'] = tab['nameCn'];
                tab['name'] = tab['nameEn'];

                // 获取每个tab下table的头部信息
                const headerData: Array<object> = await this.getMetadataProperties(tab['resourceId']);
                tab['headerData'] = [...JSON.parse(JSON.stringify(this.baseTableHeaderData)), ...headerData];
                tab['paginationData'] = {
                    total: 1,
                    pageSize: 10,
                    currentPage: 1,
                    // 将tab的信息存储在table的分页信息中，因为table分页中需要使用到tab的信息 和此被点击的card信息
                    tabInfo: tab,
                    insInfo: data,
                };

                // 获取每个tab下table的体数据
                tab['tableData'] = await this.getTableData(tab, data, tab['paginationData']);
            }
            this.activeName = tabs[0]['name'];
            this.tabsData = tabs;
            // 展示显示下方的tabs
            this.isShowFade = true;
        }

        /**
         * 获取头tabs信息
         * @param data 当前被点击的card数据
         * return tabs的数据
         */
        public async getTabsData(data): Promise<Array<object>> {
            let params = {
                parentResourceId: data.modelId
            };
            let url = config.port('metadatamodel') + '/page';
            let tabs: Array<object> = [];
            await this.$http.get(url, {params}).then(response => {
                tabs = response.data.data.records;
            });
            return tabs;
        }

        /**
         * 获取table头部信息
         * @param data 当前tab的数据
         * return table 头部信息
         */
        public async getTableHeaderData(data): Promise<Array<object>> {
            let URL = config.port('metadataproperties') + '/page';
            const params = {
                modelResourceId: data['modelId']
            };
            let headerData: Array<object> = [];
            await this.$http.get(URL, {params}).then((response) => {
                headerData = response.data.data.records;
            });
            return headerData;
        }

        /**
         * 获取table体数据
         * @param tabInfo 当前tab的数据
         * @param insInfo 当前查看显示实例下数据的实例数据，就是被点击card的数据
         * return table 体数据
         */
        public async getTableData(tabInfo, insInfo, paginationData) {
            let url = config.port('metadatavalue') + '/page';
            let params = {
                parentId: insInfo['resourceId'],
                modelId: tabInfo['resourceId'],
                current: paginationData['currentPage'], // 当前显示的页数
                size: paginationData['pageSize'], // 当前页数显示的条数
            };
            let tableData: Array<object> = [];
            await this.$http.get(url, {params}).then(response => {
                const res = response.data.data;
                // 更新分页信息
                paginationData['currentPage'] = res.current;
                paginationData['total'] = res.total;

                tableData = res.records;
            });
            return tableData;
        }


        /**
         * 每个卡片被点击
         * @param data 当前被点击的card数据
         */
        public cardClick(data): void {
            this.$emit('card-click', data)
        }

        /**
         * 提交详情中属性修改
         */
        public submitAttr(): void {
            let params: Object = {};
            this.detailData.forEach(item => {
                params[item['key']] = item['value']
            });
            let url = config.port('metadatavalue');
            this.$http.put(url, params).then(response => {
                this.$message.success('保存成功！');
                this.$emit('submit-attr', this.detailCardData);
                this.isRefresh = true;
            })

        }
    }
</script>

<style lang="less">
    .metadata-presentation-tree-map {
        position: absolute;
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
        overflow: hidden;
        padding: 10px;
        /*background-color: #fff;*/
        transition: all 1.5s;
        transform: scale3d(0.5, 0.5, 1);


        .left {
            position: absolute;
            top: 0;
            left: 0;
            bottom: 0;
            display: flex;
            flex-direction: column;
            overflow: hidden;
            transition: all .5s;


            .table-wrapper {
                margin: 0 !important;
                padding-top: 10px;
                flex: 1;
                overflow: auto;
                // 每一样数据
                .item {
                    width: 100%;
                    background-color: #fff;
                    line-height: 30px;
                    position: relative;
                    overflow: hidden;
                    margin-bottom: 12px;
                    cursor: pointer;

                    .title {
                        text-align: center;
                        font-weight: 500;
                        background-color: #156bc4;
                        font-size: 14px;
                        line-height: 28px;
                        color: #ffffff;
                        overflow: hidden;
                        text-overflow: ellipsis;
                        white-space: nowrap;
                    }

                    ul {
                        padding: 10px;
                        /*border-bottom: 1px dotted #ccc;*/

                        li {
                            display: flex;
                            flex-wrap: wrap;

                            .label {
                                width: 65px;
                                display: flex;
                                justify-content: space-between;
                            }

                            .value {
                                margin-left: 5px;
                                flex: 1;
                                height: 30px;
                                overflow: hidden;
                                text-overflow: ellipsis;
                                white-space: nowrap;
                            }
                        }
                    }

                    .button {
                        border-top: 1px dotted #cccccc;
                        padding: 10px;
                        margin-top: 0;
                    }
                }
            }


            // 分页
            .pagination {
                position: absolute;
                right: 0;
                bottom: 10px;
            }
        }

        .right {
            position: absolute;
            top: 0;
            right: 0;
            bottom: 0;
            width: 280px;
            background-color: #fff;
            border: 1px solid #ccc;
            padding: 10px;
            display: flex;
            flex-direction: column;


            &.right-enter-active, &.right-leave-active {
                transition: all .5s;
            }

            &.right-enter, &.right-leave-to {
                width: 0;
                /*opacity: 0;*/
            }

            &.right-enter-to, &.right-leave {
                width: 280px;
                /*opacity: 1;*/
            }

            .title {
                text-align: center;
                font-size: 16px;
                margin-bottom: 10px;
            }

            .form-list {
                overflow: auto;
                flex: 1;
                margin-bottom: 10px;

                li {
                    border: 1px solid #cccccc;
                    border-bottom: 0;
                    line-height: 30px;
                    display: flex;
                    flex-wrap: wrap;

                    &:last-child {
                        border-bottom: 1px solid #cccccc;
                    }

                    &:first-child {
                        .desc i {
                            padding-left: 10px;
                        }
                    }

                    &.header {
                        background-color: #dedede;
                        font-weight: 600;
                    }

                    span {
                        padding-left: 10px;

                        &.attr {
                            border-right: 1px solid #cccccc;
                            flex: 1;
                            display: flex;
                            align-items: center;
                        }

                        &.desc {
                            flex: 2;
                        }
                    }
                }
            }

            .button {
                display: flex;
                justify-content: flex-end;
            }
        }

        .fade-wrapper {
            position: absolute;
            left: 0;
            right: 0;
            bottom: 0;
            background-color: #fff;
            height: 300px;

            &.fade-enter-active, &.fade-leave-active {
                transition: all 0.5s;
            }

            &.fade-enter-to, &.fade-leave {
                transform: translate3d(0, 0, 0);
            }

            &.fade-enter, &.fade-leave-to {
                transform: translate3d(0, 100%, 0);
            }

            .icon {
                position: absolute;
                top: 6px;
                right: 10px;
                z-index: 10000;
                color: #ffffff;
                cursor: pointer;
                font-size: 18px;
            }
        }

    }
</style>
