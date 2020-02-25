<template>
    <div class="cascade-flow">
        <header>
            <div class="left">
                <span class="line"></span>
                <el-breadcrumb separator-class="el-icon-arrow-right">
                    <el-breadcrumb-item v-for="(item,index) in breadcrumb"
                                        @click.native="getPreData(item,index)"
                                        :key="index">
                        {{item.nameCn || item.nameEn }}
                    </el-breadcrumb-item>
                </el-breadcrumb>

            </div>
            <div class="right">
                <el-input placeholder="请输入内容"
                          v-model="searchVal"
                          class="input-with-select"
                          @keyup.native.enter.stop.prevent="search">
                    <el-button slot="append" icon="el-icon-search" @click="search"></el-button>
                </el-input>
            </div>
        </header>
        <div class="flow-wrapper"
             v-infinite-scroll="load"
             :infinite-scroll-disabled="disabled">
            <ul class="flow">
                <template v-for="(item) in flowData">
                    <li class="flow-item" :style="`background-color:${item.color}`"
                        :key="item.id"
                        @click.stop.prevent="showHxRelation(item)">
                        <span class="name">{{item.nameCn}}</span>
                    </li>
                </template>
            </ul>
            <footer v-if="noMore">
                加载完成
            </footer>
        </div>
        <cb-card-relation v-model="cbCardRelationData" viewType="project"/>
        <!--视图类别  业务视图:business  项目视图:project  技术视图:technology-->
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component, Prop, Watch} from 'vue-property-decorator'
    import config from "../../config";
    import CbCardRelation from '../../components/cbCardRelation/index.vue'

    @Component({
        components: {CbCardRelation},
    })
    export default class CascadeFlow extends Vue {
        name: string = 'CascadeFlow';
        // 定义v-mode
        model = {
            prop: 'breadcrumb',
            event: 'change'
        };
        @Prop({type: Array, default: []})
        breadcrumb;
        searchVal: string = ''; // 搜索的值
        column: number = 2;
        windowWidth: number = 0; // 界面尺寸

        /**
         * 获取数据元参数
         */

        paginationData = {
            currentPage: 1,
            pageSize: 50,
            total: 1
        };
        flowData = []; // 存放瀑布流数据
        noMore = false; // 没有更多数据
        loading: boolean = false; // 开始加载
        flowColors: Array<string> = ['#27AE60', '#16A085', '#E67E22', '#8E44AD', '#3498DB', '#F39C12'];


        /**
         * 华西血缘
         */
        cbCardRelationData: object = {};

        /**
         * 华西血缘展示
         */
        showHxRelation(data) {
            data['projectId'] = this.breadcrumb[this.breadcrumb.length - 1].id;
            this.cbCardRelationData = data;
        }


        @Watch("breadcrumb")
        breadcrumbChange(newVal) {
            this.initData();
            this.searchVal = '';
            this.getDataMetaTableData();
        }

        /**
         * 禁止瀑布流加载
         */
        get disabled(): boolean {
            return this.loading || this.noMore;
        }

        mounted() {
            this.listen();
            this.resize();
        }

        /**
         * 获取页面尺寸
         */
        resize() {
            this.windowWidth = document.documentElement.clientWidth || document.body.clientWidth;
        }

        /**
         * 初始化数据
         */
        initData() {
            this.loading = false;
            this.noMore = false;
            this.flowData = [];
            this.resize();
            this.paginationData['currentPage'] = 1;
        }

        /**
         * 监听resize变化
         */
        listen() {
            window['addEventListener']("resize", () => {
                this.resize();
            })
        }

        /**
         * 瀑布流加载数据
         */
        load(): void {
            this.getDataMetaTableData();
        }

        /**
         * 获取 数据集引用数据元
         */
        getDataMetaTableData() {
            let params = {
                current: this.paginationData['currentPage'],
                size: this.paginationData['pageSize'],
                id: this.breadcrumb[this.breadcrumb.length - 1].id,
                nodeType: this.breadcrumb[this.breadcrumb.length - 1].nodeType,
                queryString: this.searchVal
            };
            this.loading = true;
            let url = config.port('projectView') + 'info';
            this.$http.get(url, {params}).then(response => {
                const res = response.data;
                if (res.code === 0) {
                    const data = res.data;
                    this.paginationData['currentPage'] = data.current + 1;
                    this.paginationData['pageSize'] = data.size;
                    this.paginationData['total'] = data.total;
                    data.records.map(item => {
                        let index = Math.floor(Math.random() * 6);
                        item['color'] = this.flowColors[index];
                    });
                    this.flowData.push(...data.records);
                    this.loading = false;
                    if (data.records.length === 0) {
                        this.noMore = true;
                    }
                }
            }).catch(() => {
                this.noMore = true;
            })
        }


        /**
         * 获取面包屑上的数据
         * @param data
         * @param index
         */
        getPreData(data, index) {
            this.breadcrumb.splice(index + 1, this.breadcrumb.length - index);
            this.$emit("change", this.breadcrumb);
        }

        /**
         * 搜索
         */
        search() {
            this.initData();
            this.getDataMetaTableData()
        }
    }
</script>

<style lang="less">
    .cascade-flow {
        width: 100%;
        height: 100%;
        position: relative;

        header {
            height: 32px;
            background: rgba(255, 255, 255, 1);
            box-shadow: 0 0 1px 0 rgba(6, 0, 1, 0.26);
            display: flex;
            align-items: center;
            padding: 10px;
            justify-content: space-between;

            .left {
                display: flex;
                align-items: center;

                .line {
                    display: inline-block;
                    width: 4px;
                    height: 14px;
                    background: #3498DB;
                    margin-right: 9px;
                }
            }
        }

        .flow-wrapper {
            position: absolute;
            left: 0;
            right: 0;
            bottom: 0;
            top: 32px;
            overflow: auto;

            .flow {
                width: 100%;
                display: flex;
                flex-wrap: wrap;
                padding: 10px 0 10px 10px;

                .flow-item {
                    height: 90px;
                    line-height: 90px;
                    padding: 0 30px;
                    margin-right: 10px;
                    margin-bottom: 10px;
                    text-align: center;
                    white-space: nowrap;
                    flex: 1;
                    font-size: 20px;
                    position: relative;
                    color: #fff;
                    cursor: pointer;

                    .icon {
                        position: absolute;
                        top: 65%;
                        left: 50%;
                        line-height: 16px;
                        transform: translateX(-50%);
                        font-size: 16px;
                    }

                    &.string {
                        background-color: #E67E22;
                    }

                    &.number {
                        background-color: #16A085;
                    }

                    &.boolean {
                        background-color: #8E44AD;
                    }

                    &.image {
                        background-color: #F39C12;
                    }

                    &.date {
                        background-color: #27AE60;
                    }
                }
            }

            footer {
                text-align: center;
                line-height: 20px;
            }

        }

        .el-breadcrumb {
            .el-breadcrumb__item {
                cursor: pointer;
                font-size: 12px;

                &:hover {
                    .el-breadcrumb__inner {
                        color: #1F7FDF;
                    }
                }
            }

            .el-breadcrumb__item:last-child {

                .el-breadcrumb__inner {
                    color: #1F7FDF;
                }
            }
        }
    }
</style>
