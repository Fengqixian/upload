<template>
    <div class="business-view-relation-constant">
        <div class="element-set">
            <ul class="element-set-wrapper"
                :style="{height:expanded?expandedHeight=== '0px'?'auto':expandedHeight:0}"
                ref="elementSetWrapper">
                <li v-if="elementSetArray" v-for="(item, index) in elementSetArray" :key="index">
                    <i class="iconfont icon-shujuji"></i>
                    <span class="name">{{item.name}}</span>
                </li>
            </ul>
            <div class="btn-wrapper">
                <div class="btn"
                     @click="expandedElementSet">数据集
                    <i class="el-icon-d-arrow-right"
                       :class="{expanded}"></i></div>
            </div>
        </div>
        <div class="element-item">
            <span class="line"></span>
            <span class="name">{{this.arg.nameCn || this.arg.nameEn || 'unknown'}}</span>
        </div>
        <div class="relation-table" ref="relation"></div>
        <el-dialog :close-on-click-modal="false"
                   title="ETL"
                   class="relation-wrapper-ETL"
                   :visible.sync="tableVisible">
            <div class="table-relation" ref="tableRelation"></div>
        </el-dialog>
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component, Prop, Watch} from 'vue-property-decorator'
    import {Mutation, State} from 'vuex-class'
    import config from '../../config.ts'
    import BusinessViewRelationClass from '../../common/js/BusinessViewRelationClass'

    let relation;

    @Component({})
    export default class TechnologyTab extends Vue {
        name: string = 'index';
        @Mutation setLoadingFlag;
        @State windowSize;
        @State menuWidth;
        // tree节点的数据
        @Prop()
        arg;
        expanded: boolean = true;
        expandedHeight: String = '0px';
        tableVisible: Boolean = false;
        elementItem: Object = null;
        elementSetArray: Array<object> = null;
        relation;

        mounted() {
            let url = config.port('metaDataLineage') + 'getDataLineage';
            let params = {
                id: this.arg.id,
                resourceId: this.arg.resourceId,
                dataType: 'element_item',
                viewType: 'business',
                version: 'HX',
            };
            this.$http.get(url, {params}).then(response => {
                if (response.data.code === 0) {
                    const {elementItemArray, elementSetArray, linkDataArray, nodeDataArray} = response.data.data;
                    this.elementItem = elementItemArray[0];
                    this.elementSetArray = elementSetArray;
                    setTimeout(() => {
                        this.relation = new BusinessViewRelationClass({
                            ref: this.$refs.relation,
                            data: nodeDataArray,
                            linkData: linkDataArray,
                            linkClick: this.linkClick
                        });
                    }, 20);
                    setTimeout(() => {
                        const {height} = this.$refs.elementSetWrapper['getBoundingClientRect']();
                        this.expandedHeight = height + 'px';
                    }, 20)
                }
            })
        }

        /**
         * 展开数据集
         */
        expandedElementSet() {
            this.expanded = !this.expanded;
            setTimeout(() => {
                this.relation.renderContainerLoc()
            }, 250)
        }

        linkClick(link) {
            this.tableVisible = true;
            let url = config.port('metaDataLineage') + '/getDataLineageETLDetail';
            let params = {
                resourceId: link.to.id,
                version: 'HX',
                nameEn: link.to.nameEn,
            };
            this.$http.get(url, {params}).then(response => {
                if (response.data.code === 0) {
                    const {linkDataArray, sourceDataArray, targetDataArray} = response.data.data;
                    let data = {0: {children: targetDataArray}, 1: {children: sourceDataArray}};
                    new BusinessViewRelationClass({
                        ref: this.$refs.tableRelation,
                        data,
                        linkData: linkDataArray
                    });
                }
            });
        }
    }
</script>

<style lang="less">
    .business-view-relation-constant {
        width: 100%;
        height: 100%;
        background-color: #21294C;
        display: flex;
        flex-direction: column;

        .element-set {
            background-color: #1B2347;
            overflow: auto;

            .element-set-wrapper {
                display: flex;
                flex-wrap: wrap;
                transition: all 0.5s;
                background-color: #21294C;
                max-height: 300px;
                overflow: auto;

                li {
                    display: flex;
                    flex-direction: column;
                    align-items: center;
                    justify-content: center;
                    margin-left: 27px;
                    margin-top: 10px;
                    margin-bottom: 10px;

                    .iconfont {
                        font-size: 26px;
                        color: #559EF2;
                    }

                    .name {
                        color: #fff;
                    }
                }
            }

            .btn-wrapper {
                height: 24px;
                display: flex;
                align-items: center;
                justify-content: center;

                .btn {
                    cursor: pointer;
                    color: #fff;

                    &:hover {
                        color: #409EFF;
                    }

                    .el-icon-d-arrow-right {
                        margin-left: 5px;
                        transform: rotate(90deg);
                        transition: all 0.2s;

                        &.expanded {
                            transform: rotate(270deg);
                        }
                    }
                }

            }
        }

        .element-item {
            height: 30px;
            display: flex;
            align-items: center;
            padding-left: 10px;

            .line {
                width: 2px;
                height: 12px;
                background: rgba(23, 127, 228, 1);
                margin-right: 6px;
            }

            .name {
                color: #fff;
            }
        }

        .relation-table {
            overflow: auto;
            flex: 1;
        }

        .el-dialog__wrapper {
            .el-dialog {
                height: 60%;
                background-color: #F0F0F0;
                display: flex;
                flex-direction: column;

                .el-dialog__header {
                    background-color: #0E183F;

                    .el-dialog__title {
                        color: #fff;
                    }
                }

                .el-dialog__body {
                    flex: 1;

                    .table-relation {
                        width: 100%;
                        height: 100%;
                        overflow: auto;
                    }
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
