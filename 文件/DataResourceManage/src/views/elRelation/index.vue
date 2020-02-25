<template>
    <div class="el-relation cb-tree-show-operation-wrapper" ref="elPresentation" @mousemove="listenTreeDragBallMove">
        <div class="tree-wrapper">
            <cb-tree lazy
                     @search="searchTree"
                     searchFromHttp
                     v-show="searchVal === ''"
                     @load-node="loadNode"
                     ref="tree1">
                <template slot-scope="{data,node}">
                    <div class="tree-name">
                        <span @mousedown="treeNodeClick(data,node,arguments)">
                            {{data.nameCn || data.nameEn || 'unknown'}}
                        </span>
                        <div class="operation-wrapper"
                             v-if="data.type === 'MOD'
                             && data.modelType === 'element_set'">
                            <el-tooltip effect="dark" content="新增" placement="top-start">
                                <el-button type="text"
                                           icon="el-icon-plus"
                                           class="add-text"
                                           @click.prevent.stop="addEle(data,node)">
                                </el-button>
                            </el-tooltip>
                            <!--<i class="el-icon-plus" @click.stop.prevent="addEle(data,node)"></i>-->
                        </div>
                    </div>
                </template>
            </cb-tree>
            <cb-tree @search="searchTree"
                     searchFromHttp
                     v-show="searchVal !== ''"
                     :data="treeData"
                     ref="tree2">
                <template slot-scope="{data,node}">
                    <div class="tree-name">
                        <span @mousedown="treeNodeClick(data,node,arguments)">
                            {{data.nameCn || data.nameEn || 'unknown'}}
                        </span>
                        <div class="operation-wrapper"
                             v-if="data.type === 'MOD'
                             && data.modelType === 'element_set'">
                            <el-button type="text"
                                       icon="el-icon-plus"
                                       class="add-text"
                                       @click.prevent.stop="addEle(data,node)">
                            </el-button>
                            <!--<i class="el-icon-plus" @click.stop.prevent="addEle(data,node)"></i>-->
                        </div>
                    </div>
                </template>
            </cb-tree>
        </div>
        <div class="presentation-wrapper show-operation-wrapper" ref="dragContainer">
            <cb-operation-container>
                <template slot="toOperation">
                    <div class="operation">
                        <el-button class="confirm-bg" @click="submitRelation">确 认</el-button>
                        <el-button class="del-nobg" @click="clearRelation">清 屏</el-button>
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
                        <el-button @click="dialogAddVisible = false">取 消</el-button>
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
    import DragRelationClass from '../../common/js/DragRelationClass'
    import d3 from 'd3'

    let dragRelationClass;

    @Component({components: {}})
    export default class ElPresentation extends Vue {
        name: string = 'ElPresentation';


        /**
         * tree 树区域
         */
        treeData: Array<object> = []; // 用于存储查询的数据
        searchVal: string = ''; // 搜索的值

        /**
         * tree拖拽小球
         */
        public isShowBall: boolean = false; // 是否展示tree拖拽小球
        public treeDragBallX: string = '0'; // tree拖拽小球 x坐标
        public treeDragBallY: string = '0'; // tree拖拽小球 y坐标
        public treeDragBallData: any = null; // 拖拽小球的数据
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

        // 确认提交关系时候存储被拖拽的resourceId
        public resourceIds: Array<string> = [];
        // 存放被拖拽进入操作台的实例数据
        public dragFromInsDatabaseNode: string = '';

        public mounted() {
            this.listenMouseUp();
            setTimeout(() => {
                const dragContainerHeight = this.$refs.dragContainer['clientHeight'];
                const dragContainerWidth = this.$refs.dragContainer['clientWidth'];

                dragRelationClass = new DragRelationClass({
                    d3,
                    nodesData: this.nodes,
                    linksData: this.links,
                    id: 'elRelation',
                    width: dragContainerWidth,
                    height: dragContainerHeight,
                    select: '.drag-relation-wrapper'
                })
            }, 20)
        }

        @Watch('dialogAddVisible')
        public dialogAddVisibleChange(newVal): void {
            if (!newVal) {
                this.resetEleFormForm();
            }
        }

        /**
         * 创建元素项header
         * @param data 创建元素项模型的数据
         */
        public addEle(data, node): void {
            this.currentEleModNode = node;
            this.dialogAddVisible = true;
            this.eleForm['dbResourceId'] = node.parent.data.resourceId;
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
                        dbResourceId: this.eleForm['dbResourceId'],
                        nameCn: this.eleForm['nameCn']
                    };
                    this.$http.get(url, {params}).then(response => {
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
                links: dragRelationClass.getLinksData(),
                nodes: dragRelationClass.getNodesData()
            }
        }

        /**
         * 清屏
         */
        public clearRelation(): void {
            dragRelationClass['clear']();
            this.resourceIds = [];
        }

        /**
         * 提交关联关系
         */
        public submitRelation(): void {
            const linksAndNodes = this.getDragRelation();
            const links = linksAndNodes['links'];
            const nodes = linksAndNodes['nodes'];
            let element_set = nodes.filter(item => item.type === 'element_set');
            if (!nodes.length) {
                this.$message.warning('请添加元素项和元素项集合元素');
                return;
            }
            if (!element_set.length) {
                this.$message.warning('请添加元素项集合元素');
                return;
            }
            // const parentNode = nodes[0].node['parent'];
            // 存放关系list
            // 存放头list
            let resourceIds: Array<object> = [];
            let list: Array<object> = [];
            links.forEach(link => {
                let fromResourceId = link['source'].id;
                let toResourceId = link['target'].id;
                list.push({
                    fromResourceId,
                    toResourceId,
                });
                if (this.resourceIds.indexOf(fromResourceId) !== -1 && resourceIds.indexOf(fromResourceId) == -1) {
                    resourceIds.push(fromResourceId);
                }
                if (this.resourceIds.indexOf(toResourceId) !== -1 && resourceIds.indexOf(toResourceId) == -1) {
                    resourceIds.push(toResourceId);
                }
            });
            nodes.forEach(node => {
                if (this.resourceIds.indexOf(node['id']) !== -1 && resourceIds.indexOf(node['id']) == -1) {
                    resourceIds.push(node['id']);
                }
            });

            let params: object = {resourceIds, list};
            const url = config.port('metadatavalue') + '/createHeaderRelation';
            this.$http.post(url, params).then(response => {
                if (response.data.code === 0) {
                    this.$message.success('保存成功！可以到元数据血缘关系中查看创建的关系');
                    dragRelationClass.clear();
                    this.getTreeChildrenNode(this.dragFromInsDatabaseNode).then((childrenNodeData) => {
                        this.$refs.tree['refreshChildNode'](this.dragFromInsDatabaseNode, childrenNodeData)
                    })
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
                    this.treeDragBallData['name'] = this.treeDragBallData['data'].nameCn || this.treeDragBallData['data'].nameEn;
                    // 获取已有的node和link
                    let nodesData = JSON.parse(JSON.stringify(dragRelationClass.getNodesData()));
                    let linksData = JSON.parse(JSON.stringify(dragRelationClass.getLinksData()));

                    // 将 this.resourceId 中存在nodesData的给存储下来 重新赋值给this.resourceId ，
                    // 这里这么做 用于解决 插件将node给删除了 然后this.resourceId没有被删除
                    let resourceIds: Array<string> = [];
                    nodesData.forEach(node => {
                        if (this.resourceIds.indexOf(node['id']) !== -1 && resourceIds.indexOf(node['id']) == -1) {
                            resourceIds.push(node['id']);
                        }
                    });
                    // 将 this.resourceId 中存在nodesData的给存储下来 重新赋值给this.resourceId
                    this.resourceIds.splice(0, this.resourceIds.length, ...resourceIds);
                    // 禁止重复拖拽节点
                    if (this.resourceIds.indexOf(this.treeDragBallData['data'].resourceId) !== -1) {
                        this.$message.warning('不能重复拖拽！');
                        this.treeDragBallData = null;
                        return;
                    }


                    /**
                     * 经过上面的处理this.resourceIds
                     * 这里来处理元素项所在的实例数据库
                     * 如果this.resourceIds有数据，并且在同一个实例数据库下允许拖拽元素项
                     * 如果this.resourceIds没有数据  允许拖拽元素项
                     * 否则代表不是同一个实例数据库下 不能进行拖拽
                     */
                    if (this.resourceIds.length === 0) {
                        // 如果this.resourceIds没有数据  允许拖拽元素项 并将所属实例数据库的uuid给存储下来 用于之后判断所托的是不是在同一个实例数据库下
                        let databaseNode = [];
                        this.getParentDatabaseNode(databaseNode, this.treeDragBallData['node']);
                        this.dragFromInsDatabaseNode = databaseNode[0];
                    } else {
                        let databaseNode = [];
                        this.getParentDatabaseNode(databaseNode, this.treeDragBallData['node']);
                        // 操作台上有元数项 并且如果即将拖拽的元素项所属的实例数据库和操作台所属的实例数据部不是同一个 不能将此元数据拖到操作台上
                        if (this.dragFromInsDatabaseNode['data'].uuid !== databaseNode[0].data.uuid) {
                            this.treeDragBallData = null;
                            return;
                        }
                    }
                    let resourceId: string = this.treeDragBallData['data'].resourceId; // 存储拖拽node的resourceId
                    this.getEditHeaderRelation(this.treeDragBallData['data'].resourceId).then(response => {
                        const res = response['data'];
                        if (res.code === 0) {
                            // 将已有的node  link和新拖进来的整合在一起
                            let nodeDataArray = [...res.data.nodeDataArray, ...nodesData];
                            let linkDataArray = [...res.data.linkDataArray, ...linksData];
                            // nodes 去重
                            let addNodesData: Array<object> = [];
                            let flag: boolean = false;  // false 没有重复 添加到新数组里面  true 有重复 不添加
                            nodeDataArray.forEach(item => {
                                for (let i = 0; i < addNodesData.length; i++) {
                                    let node = addNodesData[i];
                                    if (node['id'] === item['id']) {
                                        flag = true;
                                        return;
                                    }
                                }
                                if (!false) {
                                    addNodesData.push(item);
                                    flag = true;
                                }
                            });

                            addNodesData.forEach((node, index) => {
                                for (let i = 0; i < linkDataArray.length; i++) {
                                    let link = linkDataArray[i];
                                    if (link['from'] === node['id'] || (link['source'] && link['source'].id === node['id'])) {
                                        link['source'] = index;
                                    }
                                    if (link['to'] === node['id'] || (link['target'] && link['target'].id === node['id'])) {
                                        link['target'] = index;
                                    }
                                }
                            });
                            // 新组装的关系关系呈现到页面中
                            dragRelationClass.addNodesLinks(addNodesData, linkDataArray, e.x, e.y, true);
                            // 添加成功 将被拖拽的node节点的resourceId给存储下来
                            if (dragRelationClass.getAddResult()) {
                                this.resourceIds.push(resourceId);
                            }
                        } else {
                            this.$message.error('获取数据异常')
                        }
                    });

                    this.treeDragBallData = null;
                }
            })

        }

        /**
         * 获取当前节点所属数据库的node
         * @param databaseNode 数据库节点node
         * @param currentNode 当前节点node
         */
        public getParentDatabaseNode(databaseNode, currentNode) {
            const currentData = currentNode.data;
            if (currentData.modelType == 'database' && currentData.type === 'INS') {
                databaseNode[0] = currentNode;
            } else {
                currentNode.parent ? this.getParentDatabaseNode(databaseNode, currentNode.parent) : null;
            }
        }

        public async getEditHeaderRelation(resourceId): Promise<object> {
            let url = config.port('metadatavalue') + '/editHeaderRelation';
            let params = {
                resourceId
            };
            let ret: object = {};
            await this.$http.get(url, {params}).then(response => {
                ret = response;
            });
            return ret;
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
            if (data.type === 'INS' && (data.modelType === 'element_item' || data.modelType === 'element_set')) {
                this.treeDragBallData = {data, node};
                this.isShowBall = true;
                // const treeDragBall = this.$refs.treeDragBall;
                this.treeDragBallX = MouseEvent['x'] + '';
                this.treeDragBallY = MouseEvent['y'] + '';
            }
        }


        /**
         * 树搜索
         */
        public searchTree(queryString): void {
            this.$refs.tree1['emitSearch'](queryString);
            this.$refs.tree2['emitSearch'](queryString);
            if (queryString === '') {
                this.treeData = [];
                this.searchVal = queryString;
            } else {
                let url = config.port('metadatavalue') + '/tree/lazyTree';
                let params: object = {
                    queryString
                };
                this.$http.get(url, {params}).then(response => {
                    this.treeData = response.data.data;
                    this.searchVal = queryString;
                });
            }
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
    .el-relation {
        display: flex;
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
        }
    }

</style>
