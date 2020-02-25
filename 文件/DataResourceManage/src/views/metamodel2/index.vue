<template>
    <div class="sheets-container header-no-operate">
        <div class="tree-container">
            <div class="tree">
                <span class="title">元模型</span>
                <span class="title-btn" @click.prevent.stop="addModel">
                  <i class="add-model el-icon-plus"></i>
                  新增
                </span>
                <el-input
                        v-model.trim="searchModelValue"
                        placeholder="请输入模型名称"
                        suffix-icon="el-icon-search">
                </el-input>
                <div class="tree-scroll-wrapper">
                    <el-tree
                            :data="treeData"
                            node-key="resourceId"
                            :props="defaultProps"
                            :default-expanded-keys="defaultExpandedKeys"
                            @node-click="handleNodeClick"
                            :filter-node-method="treeFilterNode"
                            :expand-on-click-node="false"
                            ref="tree"
                            @node-expand="nodeExpand">
                        <div class="tree-slot-div" slot-scope="{ node, data }">
                            <span>{{data.nameCn}}</span>
                            <div class="tree-icon">
                                <i class="add-model el-icon-plus" @click.prevent.stop="addModel(data,node)"></i>
                                <i class="edit-model el-icon-edit" @click.prevent.stop="editModel(data,node)"></i>
                                <i class="iconfont icon-shanchu" @click.prevent.stop="delModel(data,node)"></i>
                            </div>
                        </div>
                    </el-tree>
                </div>
            </div>
        </div>

        <!-- 新增、编辑 模型 -->
        <el-dialog :title="Boolean(this.addEditModelFormData)?'新增模型': '编辑模型'"
                   :visible.sync="addEditModelVisible"
                   :close-on-click-modal="false"
                   width="80%">
            <el-form ref="addEditModelFormRef" :model="addEditModelFormData" label-width="120px">
                <el-form-item label="模型英文名称" prop="nameEn">
                    <el-input v-model.trim="addEditModelFormData.nameEn" placeholder="模型英文名称"
                              @keydown.native.enter="submitForm('ruleForm')"></el-input>
                </el-form-item>
                <el-form-item label="模型中文名称" prop="nameCn">
                    <el-input v-model.trim="addEditModelFormData.nameCn" placeholder="模型中文名称"
                              @keydown.native.enter="submitForm('ruleForm')"></el-input>
                </el-form-item>
                <el-form-item label="模型状态" prop="status">
                    <el-input v-model.trim="addEditModelFormData.status" placeholder="模型状态"
                              @keydown.native.enter="submitForm('ruleForm')"></el-input>
                </el-form-item>
                <el-form-item label="模型类型" prop="modelType">
                    <el-input v-model.trim="addEditModelFormData.modelType" placeholder="模型类型"
                              @keydown.native.enter="submitForm('ruleForm')"></el-input>
                </el-form-item>
                <el-form-item label="描述" prop="description">
                    <el-input v-model.trim="addEditModelFormData.description" placeholder="描述"
                              @keydown.native.enter="submitForm('ruleForm')"></el-input>
                </el-form-item>
                <el-form-item>
                    <div class="dialog-btn">
                        <el-button type="primary" @click="submitAddEditForm">确 认
                        </el-button>
                        <el-button @click="resetForm('addEditModelFormRef')">重置</el-button>
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

    @Component({})
    export default class Index extends Vue {
        name: string = 'Index';
        /*
        * tree树
        * */
        treeData: Array<object> = []; // tree树数据
        searchModelValue: string = ''; // 搜索词
        defaultProps: object = { // tree树配置
            label: "nameCn",
            children: 'children'
        };
        defaultExpandedKeys: Array<string> = []; // tree默认展开节点

        /*
        * 新增、编辑模型
        * */
        public addEditModelVisible: boolean = true;
        public addEditModelFormData: object = {
            id: '', // 模型ID
            nameEn: '', // 模型英文名称
            nameCn: '', // 模型中文名称
            description: '', // 描述
            modelType: '', // 描述
            status: ''  // 描述
        }

        public mounted(): void {
            this.getTreeData();
        }

        // 检测tree树上的input变化
        @Watch('searchModelValue')
        public searchModelValueChange(val): void {
            this.$refs.tree['filter'](val);
        }

        // 检测新增、编辑展示隐藏变化
        @Watch('addEditModelVisible')
        public addEditModelVisibleChange(val): void {
            if (!val) {
                this.resetForm('addEditModelFormRef')
            }

        }

        // 提交新增、编辑模型
        public submitAddEditForm(): void {

        }

        // 初始化form表单
        public resetForm(formName): void {
            this.$refs[formName]['resetFields']();
        }

        // tree树新增模型
        addModel(data, node) {
            this.addEditModelVisible = true;
        }

        // 编辑模型
        public editModel(data, node): void {
            this.addEditModelVisible = true;
            this.addEditModelFormData = data;
        }


        // tree节点展开方法
        public nodeExpand(data): void {
            this.defaultExpandedKeys.push(data.uuid)
        }

        // tree节点被点击时的回调
        public handleNodeClick(data, node): void {
        };

        // 获取tree树方法
        public getTreeData(): void {
            let URL = config.port('metadatamodel') + '/tree';
            this.$http.get(URL).then((response) => {
                //要显示的元模型
                this.treeData = response.data.data;
            }).catch(function (response) {
            })
        }


        // tree树节点过滤
        treeFilterNode(value, data) {
            if (!value) return true;
            return data.nameCn.indexOf(value) !== -1;
        }
    }
</script>

<style scoped lang="less">

</style>
