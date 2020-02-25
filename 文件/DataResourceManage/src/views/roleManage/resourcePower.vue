<template>
    <div class="resource-power">
        <div class="resource-power-container">
            <div class="model-tree">
                <div class="checkout-tree">
                    <el-button-group>
                        <el-button :type="checkoutTreeValue==='technology'?'primary':''"
                                   @click.stop.prevent="checkoutTreeChange('technology')">技术视图
                        </el-button>
                        <el-button :type="checkoutTreeValue==='business'?'primary':''"
                                   @click.stop.prevent="checkoutTreeChange('business')">业务视图
                        </el-button>
                        <el-button :type="checkoutTreeValue==='project'?'primary':''"
                                   @click.stop.prevent="checkoutTreeChange('project')">项目视图
                        </el-button>
                    </el-button-group>
                </div>
                <div class="tree">
                    <cb-tree :data="technologyTreeData"
                             ref="technologyTree"
                             @node-click="nodeClick"
                             v-show="checkoutTreeValue === 'technology'">
                        <template slot-scope="{data,node}">
                            <div class="tree-name">
                                <span>{{data.nameCn || data.nameEn || 'unknown'}}</span>
                            </div>
                        </template>
                    </cb-tree>
                    <cb-tree :data="businessTreeData"
                             ref="businessTree"
                             @node-click="nodeClick"
                             v-show="checkoutTreeValue === 'business'">
                        <template slot-scope="{data,node}">
                            <div class="tree-name">
                                <span>{{data.nameCn || data.nameEn || 'unknown'}}</span>
                            </div>
                        </template>
                    </cb-tree>
                    <cb-tree :data="projectTreeData"
                             ref="projectTree"
                             @node-click="nodeClick"
                             v-show="checkoutTreeValue === 'project'">
                        <template slot-scope="{data,node}">
                            <div class="tree-name">
                                <span>{{data.nameCn || data.nameEn || 'unknown'}}</span>
                            </div>
                        </template>
                    </cb-tree>
                </div>
            </div>
            <div class="instance-manage">
                <add-and-cancel-power
                        :checkoutTreeValue="checkoutTreeValue"
                        :roleId="roleId"
                        ref="addAndCancelPower"
                        :treeCurrentNode="treeCurrentNode"
                        @change="handleCheckBox"/>
            </div>
            <div class="btn-right">
                <el-button class="del-nobg" @click="cancelDialog">取 消
                </el-button>
                <el-button class="confirm-bg" @click="submitResourcePower">保 存</el-button>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component, Prop, Watch} from 'vue-property-decorator'
    import config from "../../config";
    import AddAndCancelPower from './addAndCancelPower'

    @Component({components: {AddAndCancelPower}})
    export default class ResourcePower extends Vue {
        name: string = 'ResourcePower';
        @Prop()
        roleId;
        /**
         * 树
         */
        public technologyTreeData: Array<object> = []; // 存储tree 数据
        public businessTreeData: Array<object> = []; // 存储tree 数据
        public projectTreeData: Array<object> = []; // 存储tree 数据
        public treeCurrentNode: object = null; // 当前被点击的node节点数据

        public isOperation: boolean = false; // 判断是否操作了

        // 选择配置的树
        checkoutTreeValue = 'technology';

        @Watch('checkoutTreeValue')
        checkoutTreeValueChange() {
            // 切换树的时候，将当前的tree高亮node设置为空
            this.treeCurrentNode = null;
            // 取消tree高亮
            this.$refs['technologyTree'] && this.$refs['technologyTree']['setCurrentKey'](null);
            this.$refs['addAndCancelPower'] && this.$refs['addAndCancelPower']['setCheckboxNull']();
        }

        public mounted() {
            this.getTechnologyTreeData();
            this.getBusinessTreeData();
            this.getProjectTreeData();
        }


        /**
         * 点击checkbox的
         * @param ins 每一项 的instance数据
         */
        public handleCheckBox(ins): void {
            this.isOperation = true;  // 需要提示保存再执行其他的
        }

        /**
         * 保存
         */
        public async submitResourcePower() {
            const {noCheckedAllData, noCheckedItem, checkedAllData, checkedItem} = this.$refs.addAndCancelPower['getCheckboxData']();
            noCheckedItem.length && await this.submitAddPower(noCheckedItem);
            checkedItem.length && await this.submitCanclePower(checkedItem);
            this.cancelDialog();
            if (!noCheckedItem.length && !checkedItem.length) {
                this.$message.warning('请选择再保存')
            }
        }

        public async submitAddPower(parentResourceIds) {
            let url = config.port('roleresourcepermission') + `saveOrUpdateRole`;
            this.getParentsResourceId(this.treeCurrentNode, parentResourceIds);
            let projectType = null; //  0：技术 1：业务 2：项目
            switch (this.checkoutTreeValue) {
                case 'technology':
                    projectType = 0;
                    break;
                case 'business':
                    projectType = 1;
                    break;
                case 'project':
                    projectType = 2;
                    break;
            }
            let params = {
                roleId: this.roleId,
                parentResourceIds,
                projectType
            };
            await this.$http.put(url, params).then(response => {
                if (response.data.code === 0) {
                    this.$message.success('新增授权成功');
                    this.isOperation = false
                }
            })
        }

        public async submitCanclePower(parentResourceIds) {
            let url = config.port('roleresourcepermission') + `cancel`;
            this.getParentsResourceId(this.treeCurrentNode, parentResourceIds);
            let projectType = null; //  0：技术 1：业务 2：项目
            switch (this.checkoutTreeValue) {
                case 'technology':
                    projectType = 0;
                    break;
                case 'business':
                    projectType = 1;
                    break;
                case 'project':
                    projectType = 2;
                    break;
            }
            let params = {
                roleId: this.roleId,
                parentResourceIds,
                projectType
            };
            await this.$http.put(url, params).then(response => {
                if (response.data.code === 0) {
                    this.$message.success('取消授权成功');
                    this.isOperation = false
                }
            })
        }

        getParentsResourceId(node, resourceIds) {
            if (node.data) {
                resourceIds.push(node.data.resourceId);
                if (node.parent && node.parent.level > 0 && node.parent.data) {
                    this.getParentsResourceId(node.parent, resourceIds)
                }
            }
        }


        /**
         * 选择资产目录的 tree 树
         * @param val
         */
        checkoutTreeChange(val) {
            if (this.isOperation) {
                this.$confirm(`您已更改内容，需要保存吗？`, '提示', {type: 'warning'}).then(() => {
                    this.submitResourcePower()['then'](() => {
                        this.reset();
                        this.checkoutTreeValue = val;
                    });
                }).catch(() => {
                    this.reset();
                    this.checkoutTreeValue = val;
                });
            } else {
                this.reset();
                this.checkoutTreeValue = val;
            }
        }

        /**
         * 重置资源权限的数据
         */
        reset() {
            this.isOperation = false;
        }

        /**
         * 节点被点击时的回调
         * @param data  新数据
         * @param node 需要刷新的node节点
         * @param component 当前组件
         * */
        public nodeClick(data, node, component): void {
            if(data.nodeType === 'table' || data.nodeType === 'dataset' || data.nodeType === 'project'){
                if (this.treeCurrentNode !== node) {
                    if (this.isOperation) {
                        this.$confirm(`您已更改内容，需要保存吗？`, '提示', {type: 'warning'}).then(() => {
                            this.submitResourcePower()['then'](() => {
                                this.reset();
                                this.treeCurrentNode = node;
                                this.$refs.addAndCancelPower['getDataAndChecked']();
                            });
                        }).catch(() => {
                            this.reset();
                            this.treeCurrentNode = node;
                            this.$refs.addAndCancelPower['getDataAndChecked']();
                        });
                    } else {
                        this.reset();
                        this.treeCurrentNode = node;
                        this.$refs.addAndCancelPower['getDataAndChecked']();
                    }
                }
            }
        }


        // 获取 技术视图 tree 树模型数据集合
        getTechnologyTreeData(): void {
            let url = config.port('technologyView') + 'tree';
            this.$http.get(url).then(response => {
                const res = response.data;
                if (res.code === 0) {
                    this.technologyTreeData = res.data;
                }
            });
        }

        // 获取 业务视图 tree 树模型数据集合
        getBusinessTreeData(): void {
            let url = config.port('businessView') + 'tree';
            this.$http.get(url).then(response => {
                const res = response.data;
                if (res.code === 0) {
                    this.businessTreeData = res.data;
                }
            });
        }

        // 获取 项目视图 tree 树模型数据集合
        getProjectTreeData(): void {
            let url = config.port('projectView') + 'tree';
            this.$http.get(url).then(response => {
                const res = response.data;
                if (res.code === 0) {
                    this.projectTreeData = res.data;
                }
            });
        }

        /**
         * 取消模态框
         */
        cancelDialog() {
            this.$emit('cancelDialog')
        }
    }
</script>

<style lang="less">
    .resource-power {
        height: 100%;
        width: 100%;
        display: flex;
        flex-direction: column;

        .resource-power-container {
            flex: 1;
            display: flex;

            .model-tree {
                width: 209px;
                height: 100%;
                display: flex;
                flex-direction: column;

                .checkout-tree {
                    margin-bottom: 2px;

                    .el-radio-button__inner {
                        border-radius: 0;
                    }
                }

                .tree {
                    flex: 1;
                    overflow: auto;
                }
            }

            .instance-manage {
                flex: 1;
                /*background-color: #e5e5e5;*/
                display: flex;
                padding: 0 10px;

                .instance {
                    flex: 1;
                    /*background-color: #e5e5e5;*/
                    background-color: #fff;
                    border: 1px solid #cccccc;
                    border-right-style: dashed;
                    position: relative;

                    .search {
                        position: absolute;
                        top: 0;
                        left: 0;
                        right: 0;
                        height: 40px;
                        padding-left: 10px;
                        padding-right: 10px;
                        display: flex;
                        align-items: center;
                        border-bottom: 1px dotted #ccc;
                    }
                }

                .custom {
                    flex: 1;
                    /*background-color: #e5e5e5;*/
                    background-color: #fff;
                    border: 1px solid #cccccc;
                    border-left-width: 0;
                    position: relative;

                    & > .title {
                        position: absolute;
                        top: 0;
                        left: 0;
                        right: 0;
                        height: 40px;
                        padding-left: 10px;
                        padding-right: 10px;
                        display: flex;
                        align-items: center;
                        border-bottom: 1px dotted #ccc;
                        font-weight: 700;
                    }

                    .content {
                        position: absolute;
                        top: 40px;
                        left: 0;
                        right: 0;
                        bottom: 0;
                        padding: 10px;
                        overflow: auto;

                        li {
                            border: 1px solid #ccc;
                            margin-bottom: 10px;

                            .title {
                                line-height: 24px;
                                font-weight: 600;
                                border-bottom: 1px solid #ccc;
                                padding: 0 10px;
                                display: flex;
                                justify-content: space-between;
                            }

                            .adu-operation {
                                padding: 10px;
                                display: flex;
                                align-items: center;

                                .label {
                                    margin-left: 15px;
                                }
                            }

                            .custom-operation {
                                .custom-top {
                                    padding: 10px;
                                    display: flex;
                                    align-items: center;

                                    .label {
                                        margin-left: 15px;
                                    }
                                }

                                .custom-bottom {
                                    .line-wrapper {
                                        display: flex;
                                        align-items: center;
                                        margin-bottom: 10px;

                                        .line-left, .line-right {
                                            flex: 1;
                                            border-bottom: 1px dotted #cccccc;
                                        }

                                        .line-text {
                                            margin: 0 10px;
                                        }

                                    }

                                    .custom-wrapper {
                                        .custom-item {
                                            padding: 0 10px 10px 10px;

                                            .el-form {
                                                display: flex;
                                                align-items: center;
                                                justify-content: center;
                                            }

                                            .btn {
                                                width: 42px;
                                                text-align: center;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                .content {
                    position: absolute;
                    top: 40px;
                    left: 0;
                    right: 0;
                    bottom: 0;

                    .el-col {
                        overflow: hidden;
                        text-overflow: ellipsis;
                        white-space: nowrap;
                        margin-bottom: 10px;
                    }

                    .checkbox {
                        position: absolute;
                        top: 0;
                        left: 0;
                        right: 0;
                        bottom: 0;
                        padding: 10px;
                        overflow: auto;

                        .dataLoadEnd {
                            text-align: center;
                        }
                    }
                }
            }

            .btn-right {
                position: absolute;;
                bottom: 12px;
                right: 10px;
            }
        }


    }
</style>
