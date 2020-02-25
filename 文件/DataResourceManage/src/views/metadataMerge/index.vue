<template>
    <div class="metadata-merge cb-tree-show-operation-wrapper" ref="metadataMergeRef"
         @mousemove="listenTreeDragBallMove">
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
                        <div class="operation-wrapper" v-if="data.modelType === 'element_item'">
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
                        <div class="operation-wrapper" v-if="data.modelType === 'element_item'">
                            <!--<i class="el-icon-plus" @click.stop.prevent="addEle(data,node)"></i>-->
                        </div>
                    </div>
                </template>
            </cb-tree>
        </div>
        <div class="operation-wrapper show-operation-wrapper">
            <cb-operation-container>
                <template slot="toOperation">
                    <div class="operation">
                        <el-form :model="tbForm"
                                 :rules="tbRules"
                                 ref="tbRulesRef"
                                 label-width="120px"
                                 style="display: flex;">
                            <el-form-item label="所属库" label-width="80px" prop="resourceId">
                                <el-select v-model.trim="tbForm.resourceId" placeholder="请选择">
                                    <el-option
                                            v-for="item in databaseOptions"
                                            :key="item.resourceId"
                                            :label="item.nameCn"
                                            :value="item.resourceId">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item label="技术元数据名称" prop="tbName" label-width="120px">
                                <el-input v-model.trim="tbForm.tbName"
                                          placeholder="技术元数据名称"
                                          @keydown.enter.stop.prevent.native="submitMetadataRelation">
                                </el-input>
                            </el-form-item>
                            <div class="btn" style="display: flex; align-items: center;margin-left: 20px;">
                                <el-button class="confirm-bg"
                                           @click="submitMetadataRelation">创 建
                                </el-button>
                            </div>
                        </el-form>
                    </div>
                </template>
                <template slot="cbContainer">
                    <div class="merge-wrapper" ref="mergeWrapper"></div>
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
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component} from 'vue-property-decorator'
    import config from "../../config";
    import DragRelationClass from '../../common/js/DragRelationClass'
    import d3 from 'd3'


    let dragRelationClass;
    @Component({})
    export default class MetadataMerge extends Vue {
        name: string = 'MetadataMerge';


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

        /**
         * svg区域
         */
        force = null;
        nodes: Array<object> = [];

        /**
         * 提交的form表单
         */
        tbForm: object = {
            tbName: '',
            resourceId: ''
        };
        tbRules: object = {
            tbName: [{required: true, message: '请输入技术元数据名称', trigger: 'blur'}],
            resourceId: [{required: true, message: '请选择所属库', trigger: 'change'}],
        };

        // 存储所有实例数据库的node用于创建某个数据库下的技术元数据 然后刷新该数据库的tree树
        cacheInsDatabase: Array<any> = [];


        public mounted(): void {
            this.listenMouseUp();
            setTimeout(() => {
                this.renderSvg();
            }, 20)
        }

        /**
         * 选择库
         */
        public databaseOptions: Array<object> = [];
        public saveQueryDataBaseNode: Object = {}; // 缓存获取数据库选择的node节点数据

        /**
         * 获取选择库的下拉选择框
         */
        public getInsDbTableValue(): void {
            let url = config.port('metadatavalue') + '/checkMetadata';
            let params = {
                modelType: 'database'
            };
            this.$http.put(url, params).then(response => {
                this.databaseOptions = response.data.data;
            });
        }

        /**
         * 提交
         */
        public submitMetadataRelation(): void {
            if (!this.nodes.length) {
                this.$message.warning('请选择元素项拖到下方操作区域');
                return;
            }
            this.$refs.tbRulesRef['validate']((valid) => {
                let nodes = dragRelationClass.getNodesData();
                if (valid && nodes.length) {
                    let url: string = config.port('metadatavalue') + '/createMetaDataByElementItem';
                    let list: Array<object> = [];
                    nodes.forEach(item => {
                        let data = item['data'];
                        list.push({
                            id: data['id'],
                            resourceId: data['resourceId'],
                            parentId: data['parentResourceId'],
                            nameEn: data['nameEn'],
                            modelId: data['modelId'],
                            modelType: data['modelType'],
                            rangeId: data['rangeId'],
                            status: data['status'],
                            isStandard: data['isStandard']
                        })
                    });
                    let params = {
                        tbName: this.tbForm['tbName'],
                        list,
                        dbResourceId: this.tbForm['resourceId']

                    };
                    this.$http.post(url, params).then(response => {
                        if (response.data.code === 0) {
                            this.$message.success("创建成功");

                            // 通过界面上所属库 被选中的数据库的resourceId从 cacheInsDatabase 寻找出对应的node用于刷新节点
                            dragRelationClass.clear();
                            let insDatabaseNode: any = null;
                            for (let i = 0; i < this.cacheInsDatabase.length; i++) {
                                let node = this.cacheInsDatabase[i];
                                if (node['data'].resourceId === this.tbForm['resourceId']) {
                                    insDatabaseNode = node;
                                }
                            }
                            this.getTreeChildrenNode(insDatabaseNode).then((childrenNodeData) => {
                                this.$refs.tree['refreshChildNode'](insDatabaseNode, childrenNodeData)
                            })
                        } else {
                            this.$message.error("创建失败")
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
         * 渲染svg
         */
        public renderSvg() {
            const boundingClientRect = this.$refs.mergeWrapper['getBoundingClientRect']();
            dragRelationClass = new DragRelationClass({
                d3,
                nodesData: this.nodes,
                id: 'elRelation',
                width: boundingClientRect['width'],
                height: boundingClientRect['height'],
                select: '.merge-wrapper',
                dragLink: false
            })
        }

        /**
         * 点击tree节点
         * @param data 点击节点数据
         * @param node 点击node节点数据
         * @param MouseEvent 点击事件
         */
        public treeNodeClick(data, node, [MouseEvent]): void {
            if (data.type === 'MOD' || data.modelType !== 'element_item') return;
            this.treeDragBallData = {data, node};
            this.isShowBall = true;
            // const treeDragBall = this.$refs.treeDragBall;
            this.treeDragBallX = MouseEvent['x'] + '';
            this.treeDragBallY = MouseEvent['y'] + '';
        }

        /**
         * 监听 鼠标up事件 创建node
         */
        public listenMouseUp(): void {
            this.$refs.metadataMergeRef['addEventListener']('mouseup', e => {
                this.isShowBall = false;
                if (this.treeDragBallData !== null) {
                    const flagArr = this.nodes.filter(item => {
                        const node = this.treeDragBallData['node'];
                        return node['parent'].parent.data.id !== item['node']['parent'].parent.data.id
                    });
                    if (flagArr.length) {
                        this.$message.warning('请选择同一个数据库下的元素项！');
                        this.treeDragBallData = null;
                        return;
                    }


                    this.treeDragBallData['name'] = this.treeDragBallData['data'].nameCn || this.treeDragBallData['data'].nameEn;
                    dragRelationClass.addNode(this.treeDragBallData, e.x, e.y);

                    if (!this.saveQueryDataBaseNode['data']
                        || this.saveQueryDataBaseNode['node'].parent.parent.parent.data.uuid
                        !== this.treeDragBallData['node'].parent.parent.parent.data.uuid) {
                        // 获取选择框数据
                        this.getInsDbTableValue();
                    }
                    this.saveQueryDataBaseNode = this.treeDragBallData;
                }
                this.treeDragBallData = null;
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
                resolve(res.data);

                res.data.forEach(item => {
                    let newNode = this.$refs.tree1['getNode'](item['uuid']) || this.$refs.tree2['getNode'](item['uuid']);
                    // 存储所有实例数据库的node用于创建某个数据库下的技术元数据 然后刷新该数据库的tree树
                    if (newNode.data && newNode.data.type === 'INS' && newNode.data.modelType === 'database') {
                        this.cacheInsDatabase.push(newNode);
                    }
                });
            });
        }
    }
</script>

<style lang="less">
    .metadata-merge {
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

        .operation-wrapper {
            .merge-wrapper {
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

        .el-form .el-col {
            margin-bottom: 0;
        }
    }
</style>
