<template>
    <div class="el-presentation" ref="elPresentation" @mousemove="listenTreeDragBallMove">
        <div class="tree-wrapper">
            <cb-tree lazy
                     @load-node="loadNode"
                     ref="tree">
                <template slot-scope="{data,node}">
                    <div class="tree-name">
                        <span @mousedown="treeNodeClick(data,node,arguments)">
                            {{data.nameCn || data.nameEn || 'unknown'}}
                        </span>
                        <div class="operation-wrapper" v-if="data.type === 'MOD'">
                            <el-tooltip effect="dark" content="新增" placement="top-start">
                                <el-button type="text"
                                           icon="el-icon-plus"
                                           class="add-text"
                                           @click.prevent.stop="addEle(data,node)">
                                </el-button>
                            </el-tooltip>
                        </div>
                    </div>
                </template>
            </cb-tree>
        </div>
        <div class="presentation-wrapper" ref="dragContainer">
            <cb-operation-container>
                <template slot="toOperation">
                    <div class="operation">
                        <el-button type="primary" @click="submitRelation">确认</el-button>
                    </div>
                </template>
                <template slot="cbContainer">
                    <div class="drag-relation-wrapper"></div>
                </template>
            </cb-operation-container>
        </div>
        <transition name="fade">
            <div class="tree-drag-ball" v-if="isShowBall"
                 :style="{top:`${treeDragBallY}px`,left:`${treeDragBallX}px`}">
                {{treeDragBallData && (treeDragBallData.data.nameCn ||
                treeDragBallData.data.nameEn || 'unknown')}}
            </div>
        </transition>
        <el-dialog title="创建元素项header"
                   :visible.sync="dialogAddVisible"
                   :close-on-click-modal="false">
            <el-form :model="eleForm" :rules="eleFormRules" ref="eleFormRef" label-width="100px">
                <el-form-item label="名称" prop="nameCn">
                    <el-input v-model.trim="eleForm.nameCn"></el-input>
                </el-form-item>
                <el-form-item>
                    <div style="display: flex;justify-content: flex-end;">
                        <el-button @click="resetEleFormForm">重 置</el-button>
                        <el-button type="primary" @click="submitEleForm">确 认</el-button>
                    </div>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component, Watch} from 'vue-property-decorator'
    import config from "../../config";
    // import DragRelation from './dragRelation'
    import DragRelationClass from './DragRelationClass'
    import d3 from 'd3'

    @Component({})
    export default class ElPresentation extends Vue {
        name: string = 'ElPresentation';
        /**
         * tree拖拽小球
         */
        public isShowBall: boolean = false; // 是否展示tree拖拽小球
        public treeDragBallX: string = '0'; // tree拖拽小球 x坐标
        public treeDragBallY: string = '0'; // tree拖拽小球 y坐标
        public treeDragBallData: any = null; // 拖拽小球的数据
        public dragRelationSvg: any = null; // 拖拽区域svg
        public nodes: Array<object> = [
            // {id: "A", reflexive: false, nameEn: 'name1'},
            // {id: "B", reflexive: false, nameEn: 'name2'},
            // {id: "C", reflexive: false, nameEn: 'name3'}
        ];
        public links = [
            // {source: 0, target: 1, left: false, right: true},
            // {source: 0, target: 2, left: false, right: true}
        ];

        /**
         * 创建元素项header
         */
        public currentEleModNode: object = null; // 存放被创建元素项的node节点 用于刷新节点的
        public dialogAddVisible: boolean = false;
        public eleForm: object = {
            nameCn: ''
        };
        // form表单规则
        public eleFormRules: object = {nameCn: [{required: true, message: '请输入元素项header名称', trigger: 'blur'}]};

        public mounted() {
            this.listenMouseUp();
            setTimeout(() => {
                const dragContainerHeight = this.$refs.dragContainer['clientHeight'];
                const dragContainerWidth = this.$refs.dragContainer['clientWidth'];
                this.dragRelationSvg = new DragRelationClass({
                    nodes: this.nodes,
                    links: this.links,
                    width: dragContainerWidth,
                    height: dragContainerHeight,
                    containerClassName: '.drag-relation-wrapper',
                    d3: d3,
                });
                this.dragRelationSvg.render();
            }, 20)
        }

        @Watch('dialogAddVisible')
        public dialogAddVisibleChange(newVal): void {
            if (!newVal) {
                this.resetEleFormForm();
            }
        }

        /**
         * 创建元书香header
         * @param data 创建元素项模型的数据
         */
        public addEle(data, node): void {
            this.currentEleModNode = node;
            this.dialogAddVisible = true;
        }

        /**
         * 重置创建元素项header表单
         */
        resetEleFormForm() {
            this.$refs.eleFormRef['resetFields']();
        }

        /**
         * 提交创建元素项header表单
         */
        public submitEleForm(): void {
            this.$refs.eleFormRef['validate']((valid) => {
                if (valid) {
                    let url = config.port('metadatavalue') + '/createHeader';
                    const params = {
                        nameCn: this.eleForm['nameCn']
                    };
                    this.$http.post(url, params).then(response => {
                        if (response.data.code === 0) {
                            this.dialogAddVisible = false;
                            this.getTreeChildrenNode(this.currentEleModNode).then((childrenNodeData) => {
                                this.$refs.tree['refreshChildNode'](this.currentEleModNode, childrenNodeData)
                            })
                        }
                    })
                } else {
                    return false;
                }
            });
        }

        /**
         * 获取tree子级数据 用于刷新
         */
        public async getTreeChildrenNode(node): Promise<Array<object>> {
            let url = config.port('metadatavalue') + '/tree/lazyTree';
            let params: object = {
                parentUuid: node.data.uuid
            };
            let childrenNodeData: Array<object> = [];
            await this.$http.get(url, {params}).then(response => {
                childrenNodeData = response.data.data
            });
            return childrenNodeData;
        }

        /**
         * 获取拖拽的nodes  和links
         */
        public getDragRelation(): object {
            return {
                links: this.dragRelationSvg.getLinks(),
                nodes: this.dragRelationSvg.getNodes()
            }
        }

        /**
         * 提交关联关系
         */
        public submitRelation(): void {
            const linksAndNodes = this.getDragRelation();
            const links = linksAndNodes['links'];
            const nodes = linksAndNodes['nodes'];
            let params: Array<object> = [];
            links.forEach(link => {
                params.push({
                    fromResourceId: link['source'].data.resourceId,
                    toResourceId: link['target'].data.resourceId,
                    toId: link['target'].data.id,
                });
            });
            const url = config.port('metadatavalue') + '/createHeaderRelation';
            this.$http.post(url, params).then(response => {
                if (response.data.code === 0) {
                    this.$message.success('保存成功！')
                } else {
                    this.$message.error('保存失败！')
                }
            })
        }


        /**
         * 监听 鼠标up事件 创建node
         */
        public listenMouseUp(): void {
            this.$refs.elPresentation['addEventListener']('mouseup', e => {
                this.isShowBall = false;
                if (this.treeDragBallData !== null) {
                    this.dragRelationSvg.createNode(this.treeDragBallData, e.x, e.y);
                    this.treeDragBallData = null;
                }
            })

        }

        /**
         * 监听在小球展示是，鼠标移动事件数据，tree拖拽小球跟着鼠标一起运动
         */
        public listenTreeDragBallMove(e) {
            if (this.isShowBall) {
                this.treeDragBallX = e.x;
                this.treeDragBallY = e.y;
            }
        }

        /**
         * 点击tree节点
         * @param data 点击节点数据
         * @param node 点击node节点数据
         * @param MouseEvent 点击事件
         */
        public treeNodeClick(data, node, [MouseEvent]): void {
            if (data.type === 'MOD') return;
            this.treeDragBallData = {data, node};
            this.isShowBall = true;
            // const treeDragBall = this.$refs.treeDragBall;
            this.treeDragBallX = MouseEvent['x'] + '';
            this.treeDragBallY = MouseEvent['y'] + '';
        }

        /**
         * 懒加载树
         * @param node node节点
         * @param resolve
         */
        public loadNode(node, resolve): void {
            let url = config.port('metadatavalue') + '/tree/lazyTree';
            let params: object = {
                parentUuid: ''
            };
            if (node.level !== 0) {
                params['parentUuid'] = node.data.uuid
            }
            // 只有根模型过滤已审批数据
            if (node.data && node.data.isStandard === true) {
                params['status'] = 1
            }
            this.$http.get(url, {params}).then(response => {
                let res = response.data;
                /* if (node.level === 0) {
                     resolve(res.data.filter(item => item.nameEn === 'element_item'));
                 } else {
                     resolve(res.data);
                 }*/
                resolve(res.data);
            });
        }
    }
</script>

<style lang="less">
    .el-presentation {
        display: flex;

        svg {
            background-color: #FFF;
            /*cursor: default;*/
            /*-webkit-user-select: none;*/
            /*-moz-user-select: none;*/
            /*-ms-user-select: none;*/
            /*-o-user-select: none;*/
            /*user-select: none;*/

            &:not(.active):not(.ctrl) {
                /*cursor: crosshair;*/
            }

            path.link {
                fill: none;
                stroke: #888;
                stroke-width: 3px;
                cursor: default;
            }

            &:not(.active):not(.ctrl) path.link {
                cursor: pointer;
            }

            path.link.selected {
                stroke-dasharray: 10, 2;
            }

            path.link.dragline {
                pointer-events: none;
            }

            path.link.hidden {
                stroke-width: 0;
            }

            circle.node {
                stroke-width: 0px;
                cursor: pointer;
                fill: #fff;
            }

            /*circle.node.reflexive {
              stroke: #000 !important;
              stroke-width: 2.5px;
            }*/

            text {
                font: 14px sans-serif;
                pointer-events: none;
            }

            text.id {
                text-anchor: middle;
                font-weight: bold;
            }
        }

        //  tree盒子
        .tree-wrapper {
            width: 300px;
            height: 100%;
            // tree树展示名称区域
            .tree-name {
                width: 100%;

                &:hover {
                    .operation-wrapper {
                        visibility: visible;
                    }
                }

                .operation-wrapper {
                    visibility: hidden;
                    position: absolute;
                    right: 10px;
                    top: 0;
                    bottom: 0;
                    display: flex;
                    align-items: center;

                    i {
                        margin: 0 5px;

                        &:hover {
                            color: #188bf5;
                        }
                    }
                }
            }
        }

        .presentation-wrapper {
            flex: 1;

            .drag-relation-wrapper {
                width: 100%;
                height: 100%;
                overflow: hidden;
            }
        }

        // tree拖拽小球
        .tree-drag-ball {
            background-color: #0b96dc;
            position: fixed;
            width: 50px;
            height: 50px;
            border-radius: 50%;
            transform: translate3d(-50%, -50%, 0);
            opacity: .8;
            line-height: 50px;
            text-align: center;
            color: #ffffff;
            font-size: 12px;
            overflow: hidden;
            font-weight: 600;
            cursor: pointer;

            /*&.fade-enter-active, &.fade-leave-active {*/
            /*transition: opacity .5s;*/
            /*}*/

            /*&.fade-enter, .fade-leave-to !* .fade-leave-active below version 2.1.8 *! {*/
            /*opacity: 0;*/
            /*}*/
        }
    }

</style>
