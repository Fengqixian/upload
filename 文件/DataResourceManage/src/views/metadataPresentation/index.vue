<template>
    <div class="metadata-presentation">
        <cb-operation-container>
            <!-- 搜索区域 -->
            <template slot="toOperation">
                <div class="operation">
                    <div class="breadcrumb">
                        <span v-for="(item,index) in breadcrumbData"
                              :key="item.uuid"
                              @click="breadcrumbClick(item)">
                            <i class="title">{{item.nameCn}}</i>
                            <i class="el-icon-arrow-right" v-if="index !== (breadcrumbData.length- 1)"></i>
                        </span>
                    </div>
                    <el-input placeholder="请输入内容" v-model.trim="searchVal" style="width:300px;"
                              @keydown.native.enter="search">
                        <el-button slot="append" icon="el-icon-search" @click.stop.prevent="search"></el-button>
                    </el-input>
                </div>
            </template>
            <!-- 存放主体 -->
            <template slot="cbContainer">
                <div class="breadcrumb-container">
                    <div class="container">
                        <tree-map :translateX="translateX1"
                                  :transition="transition1"
                                  :opacity="opacity1"
                                  :scale="scale1"
                                  :data="data1"
                                  @card-click="cardClick"
                                  @submit-attr="submitAttr">
                        </tree-map>
                        <tree-map :translateX="translateX2"
                                  :transition="transition2"
                                  :opacity="opacity2"
                                  :scale="scale2"
                                  :data="data2"
                                  @card-click="cardClick"
                                  @submit-attr="submitAttr">
                        </tree-map>
                    </div>
                </div>
            </template>
        </cb-operation-container>
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component} from 'vue-property-decorator'
    import {State} from 'vuex-class'
    import TreeMap from './treeMap'
    import config from "../../config";

    /**
     * 元数据展示
     */
    @Component({components: {TreeMap}})
    export default class MetadataPresentation extends Vue {
        name: string = 'MetadataPresentation';
        @State baseField;   // 基本的字段

        searchVal: string = ''; // 搜索内容
        translateX1: string = '0';
        translateX2: string = '100';
        transition1: boolean = false;
        transition2: boolean = false;
        scale1: string = '1';
        scale2: string = '0';
        opacity1: string = '1';
        opacity2: string = '0';
        data1: Array<object> = []; // 存放第一个页面的数据
        data2: Array<object> = []; // 存放第二个页面的数据
        isStorageData1: boolean = true; // 默认情况下降card数据存储到data1中

        /**
         * 面包屑导航数据
         */
        breadcrumbData: Array<object> = [];

        public mounted(): void {
            /**
             * 获取每层卡片的数据
             */
            this.getCardData()
        }

        /**
         * 搜索
         */
        public search(): void {
            this.searchGetCardData(this.searchVal)

        }

        /**
         * 每个卡片被点击
         * @param data 卡片的数据
         */
        public cardClick(data): void {
            this.getCardData(data, 'left');
        }

        /**
         * 修改属性成功，重新获取数据
         * @param data 卡片的数据
         */
        public submitAttr(data): void {
            this.getCardData(this.breadcrumbData[this.breadcrumbData.length - 1], 'refresh');
        }

        /**
         * @param direction 页面切换方向
         */
        public moveCard(direction) {
            if (this.opacity1 === '0') {
                this.opacity1 = '1';
                this.scale1 = '1';
                this.opacity2 = '0';
                this.scale2 = '0';
            } else {
                this.opacity1 = '0';
                this.scale1 = '0';
                this.opacity2 = '1';
                this.scale2 = '1';
            }
            /*
             let translatex = '100';
            if (direction === 'left') {
                translatex = '-100';
            }
            if (this.translateX1 === '0') {
                this.transition1 = true;
                this.translateX1 = translatex;
                this.transition2 = false;
                // this.translateX2 = (-parseFloat(translatex)).toString();
                this.translateX2 = translatex;
                setTimeout(_ => {
                    this.transition2 = true;
                    this.translateX2 = '0';
                }, 10)
            }
            else if (this.translateX2 === '0') {
                this.transition2 = true;
                this.translateX2 = translatex;
                this.transition1 = false;
                // this.translateX1 = (-parseFloat(translatex)).toString();
                this.translateX1 = translatex;
                setTimeout(_ => {
                    this.transition1 = true;
                    this.translateX1 = '0';
                }, 10)
            }
             */
        }

        /**
         * 面包屑点击
         * @param data
         */
        public breadcrumbClick(data): void {
            this.getCardData(data, 'right');
            // this.moveCard('right');
        }

        /**
         * 获取每层卡片的数据
         * @param data 父card数据
         * @param direction 页面切换方向 是否是刷新 可选值：left right refresh
         */
        public getCardData(data?, direction?): void {
            if (direction !== 'refresh' && this.breadcrumbData.length && data && this.breadcrumbData[this.breadcrumbData.length - 1]['uuid'] === data['uuid']) {
                return;
            }
            let url = config.port('metadatavalue') + '/tree/lazyTree';
            let params = {
                parentUuid: data && data.nameCn !== '首页' ? data.uuid : '',
            };
            this.$http.get(url, {params}).then(response => {
                let res = response.data;
                // 如果返回的数据为空，代表没有card了 页面不做任何展示
                if (!res.data.length) {
                    this.$message.warning('没有下一级数据');
                    return
                }
                if (data) {
                    /**
                     * 查看面包屑中是不存在此data，不存在将data作为面包屑
                     */
                    let index: number = -1; // 存储data在this.breadcrumbData中的索引
                    this.breadcrumbData.forEach((item, i) => {
                        if (item['uuid'] === data['uuid']) {
                            index = i;
                        }
                    });
                    if (index == -1) {
                        this.breadcrumbData.push(data);
                    } else {
                        this.breadcrumbData.splice(index + 1)
                    }
                }


                /**
                 * 如果data存在的话，代表切换页面了，执行切换页面动画
                 */
                    // 获取状态的options
                let statusOptions = [];
                for (let i = 0; i < this.baseField.length; i++) {
                    let item = this.baseField[i];
                    if (item['prop'] === 'status') {
                        statusOptions = item['options'];
                    }
                }
                // 映射status的中文意思
                res.data.map(item => {
                    statusOptions.forEach(opt => {
                        if (opt['itemsCode'] === item['status']) {
                            item['status'] = opt['itemsName']
                        }
                    })
                });
                if (data && this.breadcrumbData.length) {
                    // 刷新 只刷新当前页面数据，不是刷新 更新另一面数据
                    if (direction === 'refresh') {
                        // 判断返回的数据应该存储在哪个变量上
                        if (this.isStorageData1) {
                            this.data2 = res.data;
                        } else {
                            this.data1 = res.data;
                        }
                    } else {
                        // 判断返回的数据应该存储在哪个变量上
                        if (this.isStorageData1) {
                            this.data1 = res.data;
                        } else {
                            this.data2 = res.data;
                        }
                        /**
                         * 切换页面
                         */
                        this.moveCard('left');
                    }
                } else {
                    this.breadcrumbData.push({
                        nameCn: '首页',
                        uuid: 'home'
                    });
                    this.data1 = res.data;
                }
                // 刷新不切换isStorageData1值，不是刷新，切换isStorageData1值
                direction === 'refresh' ? null : this.isStorageData1 = !this.isStorageData1;
            })
        }

        /**
         * 通过搜索获取卡片数据
         * @param searchVal
         */
        public searchGetCardData(searchVal): void {
            let url = config.port('metadatavalue') + '/tree/lazyTree';
            let params = {
                queryString: searchVal
            };
            this.$http.get(url, {params}).then(response => {
                const data = response.data.data;
                let statusOptions = [];
                for (let i = 0; i < this.baseField.length; i++) {
                    let item = this.baseField[i];
                    if (item['prop'] === 'status') {
                        statusOptions = item['options'];
                    }
                }
                // 映射status的中文意思
                data.map(item => {
                    statusOptions.forEach(opt => {
                        if (opt['itemsCode'] === item['status']) {
                            item['status'] = opt['itemsName']
                        }
                    })
                });
                this.data1 = data;
                this.data2 = data;
                this.breadcrumbData = [];
                this.breadcrumbData.push({
                    nameCn: '首页',
                    uuid: 'home'
                });
            })
        }
    }
</script>

<style lang="less">
    .metadata-presentation {
        .operation {
            display: flex;
            justify-content: space-between;
            align-items: center;

            .breadcrumb {
                cursor: pointer;

                .title:hover {
                    color: #188bf5;
                }

                span:last-child {
                    .title {
                        color: #188bf5;
                    }
                }
            }
        }

        .breadcrumb-container {
            width: 100%;
            height: 100%;
            display: flex;
            flex-direction: column;

            .container {
                flex: 1;
                position: relative;
                overflow: hidden;

            }
        }
    }
</style>
