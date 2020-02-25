<template>
    <div class="cb-card-relation-constant">
        <div class="element-set" v-if="viewType !== 'technology'">
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
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component, Prop, Watch} from 'vue-property-decorator'
    import {Mutation, State} from 'vuex-class'
    import config from '../../config.ts'
    import BusinessViewRelationClass from '../../common/js/BusinessViewRelationClass'

    @Component({})
    export default class TechnologyTab extends Vue {
        name: string = 'index';
        @Mutation setLoadingFlag;
        @State windowSize;
        @State menuWidth;
        // tree节点的数据
        @Prop()
        arg;
        @Prop()
        viewType;
        @Prop() // 线点击
        linkClick;
        expanded: boolean = true;
        expandedHeight: String = '0px';
        elementItem: Object = null;
        elementSetArray: Array<object> = null;
        relation;

        mounted() {
            let url = config.port('metaDataLineage') + 'getDataLineage';
            let params = {
                id: this.arg.id,
                version: 'HX',
                resourceId: this.arg.resourceId,
                viewType: this.viewType, // 视图类别  业务视图:business  项目视图:project  技术视图:technology
            };
            if (this.viewType === 'technology') {
                params['dataType'] = 'column';
                params['nameCn'] = this.arg.nameCn;
                params['nameEn'] = this.arg.nameEn;
            } else {
                params['dataType'] = 'element_item';
            }

            if (this.viewType === 'project') {
                params['projectId'] = this.arg.projectId;
            }
            this.$http.get(url, {params}).then(response => {
                if (response.data.code === 0) {
                    const {elementItemArray, elementSetArray, linkDataArray, nodeDataArray} = response.data.data;
                    this.elementItem = elementItemArray[0];
                    this.elementSetArray = elementSetArray;
                    setTimeout(() => {
                        const {height} = this.$refs.elementSetWrapper['getBoundingClientRect']();
                        this.expandedHeight = height + 'px';
                    }, 20);
                    if (!Object.keys(nodeDataArray).length) return;
                    setTimeout(() => {
                        this.relation = new BusinessViewRelationClass({
                            ref: this.$refs.relation,
                            data: nodeDataArray,
                            linkData: linkDataArray,
                            linkClick: this.relationLinkClick
                        });
                    }, 20);
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

        relationLinkClick(link) {
            this.linkClick(link);
        }
    }
</script>

<style lang="less">
    .cb-card-relation-constant {
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
    }
</style>
