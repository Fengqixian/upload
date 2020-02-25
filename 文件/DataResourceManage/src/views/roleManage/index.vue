<template>
    <div class="role header-operate-btn">
        <cb-operation-container>
            <template slot="toOperation">
                <div class="operate">
                    <el-button class="add-bg" @click="addEditRole('add')">新 建</el-button>
                </div>
            </template>
            <template slot="cbContainer">
                <div class="table-wrapper">
                    <cb-table :header-data="headerData"
                              :table-data="tableData"
                              :paginationData="paginationData"
                              @size-change="handleSizeChange"
                              @current-change="handleCurrentChange">
                        <template slot="column">
                            <el-table-column label="操作" width="280">
                                <template slot-scope="{row}">
                                    <el-tooltip content="查看" placement="top" effect="dark">
                                        <el-button type="text"
                                                   class="add-text"
                                                   icon="iconfont icon-info"
                                                   @click="addEditRole('look',row)"></el-button>
                                    </el-tooltip>
                                    <el-tooltip content="删除" placement="top" effect="dark">
                                        <el-button type="text"
                                                   class="del-text"
                                                   icon="iconfont icon-shanchu1"
                                                   @click="delRole(row)"></el-button>
                                    </el-tooltip>
                                    <el-tooltip content="编辑" placement="top" effect="dark">
                                        <el-button type="text"
                                                   class="edit-text"
                                                   icon="iconfont icon-bianji1"
                                                   @click="addEditRole('edit',row)"></el-button>
                                    </el-tooltip>

                                    <el-tooltip content="菜单权限" placement="top" effect="dark">
                                        <el-button type="text"
                                                   class="confirm-text"
                                                   icon="iconfont icon-caidanquanxianfenpeiquanxian"
                                                   @click="editMenuPowerBtnClick(row)"></el-button>
                                    </el-tooltip>

                                    <el-tooltip content="资源权限" placement="top" effect="dark">
                                        <el-button type="text"
                                                   class="edit-text"
                                                   icon="iconfont icon-ziyuanfenpei"
                                                   @click="editResourcePowerBtnClick(row)"></el-button>
                                    </el-tooltip>
                                </template>
                            </el-table-column>
                        </template>
                    </cb-table>
                </div>
            </template>
        </cb-operation-container>

        <!--对话框-->

        <el-dialog :title="`${roleType==='add'?'新增':roleType==='edit'?'编辑':'查看'}角色`"
                   :visible.sync="dialogRoleFromVisible"
                   :close-on-click-modal="false"
                   class="new-role"
                   width="40%">
            <el-form ref="newRoleRef" :model="newRoleForm" label-width="100px" :rules="newRoleRules">
                <el-form-item label="中文名称" prop="roleName">
                    <el-input v-model.trim="newRoleForm.roleName" placeholder="请输入中文名称"
                              @keydown.native.enter="roleFormSubmit" :disabled="roleType === 'look'"></el-input>
                </el-form-item>
                <el-form-item label="英文名称" prop="roleCode">
                    <el-input v-model.trim="newRoleForm.roleCode" placeholder="请输入英文名称"
                              @keydown.native.enter="roleFormSubmit" :disabled="roleType === 'look'"></el-input>
                </el-form-item>
                <el-form-item label="描述" prop="roleDesc">
                    <el-input v-model.trim="newRoleForm.roleDesc" placeholder="请输入描述"
                              @keydown.native.enter="roleFormSubmit" :disabled="roleType === 'look'"></el-input>
                </el-form-item>
                <el-form-item label="创建时间" prop="createTime" v-if="roleType === 'look'">
                    <el-input v-model.trim="newRoleForm.createTime"
                              @keydown.native.enter="roleFormSubmit" :disabled="roleType === 'look'"></el-input>
                </el-form-item>
                <el-form-item v-if="roleType !== 'look'">
                    <div class="btn-right">
                        <el-button class="del-nobg" @click="dialogRoleFromVisible=false">取 消</el-button>
                        <el-button class="confirm-bg" @click="roleFormSubmit">保 存</el-button>
                    </div>
                </el-form-item>
            </el-form>
        </el-dialog>

        <!--配置菜单权限-->
        <el-dialog title="配置菜单权限"
                   :close-on-click-modal="false"
                   :visible.sync="dialogMenuPowerFromVisible">
            <el-input
                    placeholder="输入关键字进行过滤"
                    v-model.trim="menuPowerTreeFilterText">
            </el-input>
            <el-tree
                    class="filter-tree"
                    :data="menuPowerTreeData"
                    show-checkbox
                    :props="menuPowerTreeDefaultProps"
                    :default-checked-keys="menuPowerDefaultCheckedKeys"
                    node-key="id"
                    default-expand-all
                    :expand-on-click-node="false"
                    :filter-node-method="menuPowerTreeFilterNode"
                    ref="powerTree">
                <div class="tree-slot-div" slot-scope="{ node, data }">
                    <span>{{data.name || data.label || 'unknown'}}</span>
                </div>
            </el-tree>
            <div class="btn-right">
                <el-button class="del-nobg" @click="dialogMenuPowerFromVisible=false">取 消
                </el-button>
                <el-button class="confirm-bg" @click="submitMenuPowerMenu">保 存</el-button>
            </div>
        </el-dialog>

        <!--配置资源权限-->
        <el-dialog title="配置资源权限"
                   :visible.sync="dialogResourcePowerFromVisible"
                   class="resource-dialog scroll-dialog"
                   width="80%"
                   :close-on-click-modal="false">
            <resource-power :roleId="currentRoleId"
                            v-if="dialogResourcePowerFromVisible"
                            @cancelDialog="dialogResourcePowerFromVisible=false"></resource-power>
        </el-dialog>
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component, Watch} from 'vue-property-decorator'
    import EditRole from './editRole'
    import {Mutation} from 'vuex-class'
    import config from '../../config'
    import ResourcePower from './resourcePower'

    @Component({components: {EditRole, ResourcePower}})
    export default class Role extends Vue {
        name: string = 'Role';
        @Mutation setLoadingFlag;
        searchValue: string = '';
        // 创建角色
        dialogRoleFromVisible: boolean = false;
        newRoleForm: object = {
            roleName: '', // 角色中文名称
            roleCode: '', // 角色英文名称
            roleDesc: '', // 角色描述
            createTime: '', // 创建时间
        };
        // 创建角色名称的规则判断
        newRoleRules: object = {
            roleName: [
                {required: true, message: '请输入角色中文名称', trigger: 'blur'}
            ],
            roleCode: [
                {required: true, message: '请输入角色英文名称', trigger: 'blur'}
            ],
            roleDesc: [
                {required: true, message: '请输输入角色描述', trigger: 'blur'}
            ]
        };

        // table表格数据
        headerData: Array<object> = [
            {
                prop: 'roleName',
                label: '角色中文名称'
            },
            {
                prop: 'roleCode',
                label: '角色英文名称'
            },
            {
                prop: 'roleDesc',
                label: '角色描述'
            },
            {
                prop: 'createTime',
                label: '创建时间'
            },
        ];
        tableData: Array<object> = [];
        paginationData: object = {
            currentPage: 1,
            pageSizes: [10, 20, 30, 40, 50, 100],
            pageSize: 10,
            total: 1
        }
        // 角色操作编辑类型
        roleType: string = 'add';  // edit

        // 配置权限
        dialogMenuPowerFromVisible: boolean = false;
        public menuPowerTreeFilterText: string = '';
        public menuPowerTreeData: Array<Object> = [];
        menuPowerDefaultCheckedKeys: Array<string> = [];
        public menuPowerTreeDefaultProps: Object = {
            children: 'children',
            label: 'label'
        };
        // 当前操作的角色id
        currentRoleId: string = '';

        // 资源配置变量
        dialogResourcePowerFromVisible: boolean = false;
        resourcePowerTreeFilterText: string = '';

        // 资源权限被配置的角色


        @Watch('dialogRoleFromVisible')
        public dialogRoleFromVisibleChange(newVal, oldVal) {
            if (!newVal) {
                this.resetRoleForm();
            }
        }

        // 过滤检测
        @Watch('menuPowerTreeFilterText')
        public menuPowerTreeFilterTextChange(val) {
            this.$refs.powerTree['filter'](val);
        }

        // 过滤资源配置检测
        @Watch('resourcePowerTreeFilterText')
        public resourcePowerTreeFilterTextChange(val) {
            this.$refs.sourcePowerTree['filter'](val);
        }

        // 配置菜单权限对话框是否显示监测
        @Watch('dialogMenuPowerFromVisible')
        public dialogMenuPowerFromVisibleChange(val) {
            if (!val) {
                // 菜单选中制空
                this.menuPowerDefaultCheckedKeys = [];
                // this.menuPowerTreeData = [];
                this.$refs.powerTree['setCheckedKeys']([]);
            }
        }

        public mounted(): void {
            this.getRolePage();
            this.getMenuPowerTreeData(); // 获取授权菜单
            // this.getResourcePowerTreeData(); // 获取资源配置tree树
        }


        // 递归循环，将已经选中的tree的uuid遍历出来
        public recursionCheckedResourceTree(checkedTree, checkedTreeUuidArr) {
            checkedTree.forEach(item => {
                checkedTreeUuidArr.push(item.uuid);
                if (item.children.length) {
                    this.recursionCheckedResourceTree(item.children, checkedTreeUuidArr)
                }
            });
        }


        // 资源权限按钮
        public async editResourcePowerBtnClick(data) {
            this.currentRoleId = data.roleId;
            this.dialogResourcePowerFromVisible = true;
        }


        // 获取用户对一个的菜单权限
        public getRoleResource(data): void {
            let url = `/MetadataManage/menu/tree/${data.roleId}`;
            this.$http.get(url).then(response => {
                this.menuPowerDefaultCheckedKeys = response.data;
            })
        }


        // 提交菜单权限按钮
        public submitMenuPowerMenu(): void {
            // 获取被选的node（包含全选 半选）
            const checkedNodesData = this.$refs.powerTree['getCheckedNodes'](false, true);
            let checkedKey: Array<string> = [];
            checkedNodesData.forEach(item => {
                checkedKey.push(item.id);
            });
            this.$http({
                method: 'put',
                url: '/MetadataManage/role/menu',
                params: {
                    roleId: this.currentRoleId,
                    menuIds: checkedKey.join(',')
                }
            }).then(response => {
                const res = response.data;
                if (res.code === 0) {
                    this.$message.success('更新菜单权限成功');
                    this.dialogMenuPowerFromVisible = false;
                } else {
                    this.$message.error(res.msg);
                }
            })
        }

        // 删除角色
        public delRole(data): void {
            this.$confirm(`此操作将永久删除"${data.roleName}", 是否继续?`, '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                let url = `/MetadataManage/role/${data.roleId}`;
                this.$http.delete(url).then(response => {
                    if (response.data.code === 0) {
                        this.$message.success('删除成功');
                        this.getRolePage();
                    } else {
                        this.$message.error(response.data.msg)
                    }
                })
            })
        }

        // 编辑菜单权限按钮
        public editMenuPowerBtnClick(data): void {
            this.currentRoleId = data.roleId;
            this.getRoleMenu(data);
            this.dialogMenuPowerFromVisible = true;
        }

        // 获取用户对一个的菜单权限
        public getRoleMenu(data): void {
            let url = `/MetadataManage/menu/tree/${data.roleId}`;
            this.$http.get(url).then(response => {
                // this.menuPowerDefaultCheckedKeys = response.data;
                response.data.forEach(item => {
                    this.$refs.powerTree['setChecked'](item, true);
                })
            })
        }

        // 获取权限菜单tree树数据
        public getMenuPowerTreeData(): void {
            let url = '/MetadataManage/menu/tree/';
            this.$http.get(url).then(response => {
                if (response.data.code === 0) {
                    this.menuPowerTreeData = response.data.data;
                }
            })
        }

        // 过滤配置权限搜索
        public menuPowerTreeFilterNode(value, data) {
            if (!value) return true;
            return data.label.indexOf(value) !== -1;
        }

        // 搜索角色
        public search(): void {

        }

        // 修改角色
        public editRole(): void {
            this.$refs.newRoleRef['validate']((valid) => {
                if (valid) {
                    let url = '/MetadataManage/role';
                    this.$http.put(url, this.newRoleForm).then(response => {
                        if (response.data.code === 0) {
                            this.$message.success('修改角色成功');
                            this.dialogRoleFromVisible = false;
                            this.getRolePage();
                        }
                    })
                } else {
                    return false;
                }
            });
        }

        // 创建角色
        public addRole(): void {
            this.$refs.newRoleRef['validate']((valid) => {
                if (valid) {
                    let url = '/MetadataManage/role';
                    delete this.newRoleForm['createTime'];
                    this.$http.post(url, this.newRoleForm).then(response => {
                        if (response.data.code === 0) {
                            this.$message.success('创建角色成功');
                            this.dialogRoleFromVisible = false;
                            this.getRolePage();
                        }
                    })
                } else {
                    return false;
                }
            });
        }


        // 创建编辑角色按钮事件
        addEditRole(type, data?) {
            this.roleType = type;
            if (type === 'edit' || type === 'look') {
                this.newRoleForm = JSON.parse(JSON.stringify(data));
            }
            this.dialogRoleFromVisible = true
        }

        // table分页
        public handleCurrentChange(): void {
            this.getRolePage()
        }

        public handleSizeChange(): void {
            this.getRolePage()
        }

        // 查询角色列表
        public getRolePage(): void {
            let url = '/MetadataManage/role/page';
            const params = {
                current: this.paginationData['currentPage'],
                size: this.paginationData['pageSize']
            };
            this.$http({
                url,
                method: 'get',
                params
            }).then(response => {
                const res = response.data;
                if (res.code === 0) {
                    const data = res.data;
                    this.paginationData['currentPage'] = data.current;
                    this.paginationData['pageSize'] = data.size;
                    this.paginationData['total'] = data.total;
                    this.tableData = data.records
                } else {
                    this.$message.error(res.msg);
                }
            })
        }

        //  角色表创建修改提交
        public roleFormSubmit(): void {
            if (this.roleType === 'add') {
                this.addRole();
            } else if (this.roleType === 'edit') {
                this.editRole();
            }
        }

        // 重置角色表
        public resetRoleForm(): void {
            this.$refs.newRoleRef['resetFields']();
            this.newRoleForm = {
                roleName: '', // 角色中文名称
                roleCode: '', // 角色英文名称
                roleDesc: '', // 角色描述
            }
        }
    }
</script>

<style lang="less">
    .role {
        .table-wrapper {
            width: 100%;
            height: 100%;
        }

        .new-role {
            .select-wrapper {
                margin-top: 20px;

                .left, .right {
                    display: flex;

                    .label {
                        width: 80px;
                        text-align: right;
                        padding-right: 12px;
                        font-size: 1.2rem;
                    }

                    .select {
                        flex: 1;

                        .tree-label {
                            display: flex;
                            justify-content: space-between;
                            align-items: center;
                        }
                    }
                }
            }
        }

        .el-tree-node__content {
            display: flex;
            align-items: center;

            .custom-tree-node {
                flex: 1;

                .checkbox-wrapper {
                    display: flex;
                    align-items: center;
                }
            }
        }
    }

    .resource-dialog {
        /*.wrapper {
            box-shadow: 0 0 10px #cccccc;

            .tree-wrapper {
                margin: 10px;

            }

            .table-column {
                background-color: #d1dbe5;
                padding: 15px;
                !*&:nth-child(>0)*!
                border-bottom: 3px dashed #fff;

                &:last-child {
                    border-bottom: 0;
                }

                .table-title {
                    font-size: 16px;
                    font-weight: 700;
                    line-height: 20px;
                }

                .column-wrapper {
                    padding: 10px;
                    background-color: #fff;
                    border-radius: 4px;
                    margin-top: 10px;

                    .column-title {
                        font-size: 14px;
                        font-weight: 600;
                        margin-bottom: 15px;
                    }
                }

                // 自定义区域
                .line {
                    display: flex;
                    align-items: center;
                    margin-bottom: 10px;

                    span {
                        font-size: 12px;
                        margin: 0 10px;
                    }

                    i {
                        flex: 1;
                        border-bottom: 1px dotted #cccccc;
                    }
                }

                .custom-column {
                    display: flex;
                    align-items: center;
                    margin-bottom: 15px;

                    .column {
                        margin-right: 5px;
                    }

                    // and or
                    .connection {
                        flex: 1;
                    }

                    // 值
                    .value {
                        flex: 3;
                        margin: 0 5px;
                    }

                    // 比较
                    .condition {
                        flex: 1;
                    }
                }
            }
        }*/


        .el-dialog {
            margin-top: 49px !important;
            height: 82% !important;
            position: relative;
            overflow: hidden;

            .el-dialog__body {
                position: absolute;
                left: 0;
                right: 0;
                bottom: 0;
                top: 43px;
            }
        }
    }

</style>
