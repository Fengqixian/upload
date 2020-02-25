<template>
    <div class="role header-operate-btn">
        <cb-operation-container>
            <template slot="toOperation">
                <div class="operate">
                    <div class="operation">
                        <el-input placeholder="请输入用户名" v-model.trim="username" style="width:300px;"
                                  @keydown.native.enter="search">
                            <el-button slot="append" icon="el-icon-search" @click.stop.prevent="search"></el-button>
                        </el-input>
                        <el-button class="add-bg" @click="addEditUser('add')">新建</el-button>
                    </div>
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
                            <el-table-column label="操作" width="120">
                                <template slot-scope="{row}">
                                    <el-tooltip content="查看" placement="top" effect="dark">
                                        <el-button type="text"
                                                   class="add-text"
                                                   icon="iconfont icon-info"
                                                   @click="addEditUser('look',row)"></el-button>
                                    </el-tooltip>
                                    <el-tooltip content="删除" placement="top" effect="dark">
                                        <el-button type="text"
                                                   class="del-text"
                                                   icon="iconfont icon-shanchu1"
                                                   @click="delUser(row)"></el-button>
                                    </el-tooltip>
                                    <el-tooltip content="编辑" placement="top" effect="dark">
                                        <el-button type="text"
                                                   class="edit-text"
                                                   icon="iconfont icon-bianji1"
                                                   @click="addEditUser('edit',row)"></el-button>
                                    </el-tooltip>
                                </template>
                            </el-table-column>
                        </template>
                    </cb-table>
                </div>
            </template>
        </cb-operation-container>
        <!--对话框-->
        <el-dialog :title="userType==='add'?'新增用户':(userType==='edit'?'编辑用户':'查看用户')"
                   :visible.sync="dialogUserFromVisible"
                   class="new-role"
                   width="40%"
                   :close-on-click-modal="false">
            <el-form ref="roleForm" :model="newUserForm" label-width="80px" :rules="newUserRules">
                <el-form-item label="用户名" prop="username">
                    <el-input v-model.trim="newUserForm.username" placeholder="请输入用户名"
                              @keydown.native.enter="userFormSubmit" :disabled="userType!=='add'"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input v-model.trim="newUserForm.password" placeholder="请输入密码"
                              @keydown.native.enter="userFormSubmit" :disabled="userType==='look'"></el-input>
                </el-form-item>
                <el-form-item label="手机号" prop="phone">
                    <el-input v-model.trim="newUserForm.phone" placeholder="请输入手机号" @keydown.native.enter="userFormSubmit"
                              :disabled="userType==='look'"></el-input>
                </el-form-item>
                <el-form-item label="角色" prop="role">
                    <el-select v-model.trim="newUserForm.role" multiple placeholder="请选择角色"
                               @keydown.native.enter="userFormSubmit"
                               :disabled="userType==='look'">
                        <el-option
                                v-for="item in roleOPtions"
                                :key="item.roleId"
                                :label="item.roleName"
                                :value="item.roleId">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="状态" prop="lockFlag">
                    <el-select v-model.trim="newUserForm.lockFlag" placeholder="请选择状态" @keydown.native.enter="userFormSubmit"
                               :disabled="userType==='look'">
                        <el-option
                                v-for="item in lockFlagOptions"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <div class="btn-right" v-show="userType!=='look'">
                        <el-button class="del-nobg" @click="dialogUserFromVisible=false">取 消</el-button>
                        <el-button class="confirm-bg" @click="userFormSubmit">保 存</el-button>
                    </div>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component, Watch} from 'vue-property-decorator'
    import {Mutation} from 'vuex-class'
    // import DynamicTable from '../../components/dynamicTable/dynamicTable.vue'  // 动态table表格

    @Component({})
    export default class User extends Vue {
        name: string = 'User';
        @Mutation setLoadingFlag;
        username: string = ''; // 搜索名称
        dialogUserFromVisible: boolean = false;
        newUserForm: object = {
            username: '', // 用户名
            userId: '', // 用户ID
            password: '', // 密码
            phone: '', // 手机号
            role: [], // 角色
            lockFlag: '', // 是否锁定
            createTime: '', //创建时间
            deptId: '', //所属部门id
            deptName: '', //所属部门名称
        };
        // 创建用户是否锁定状态选项
        lockFlagOptions: Array<Object> = [
            {
                label: '有效',
                value: '0'
            },
            {
                label: '锁定',
                value: '9'
            }
        ];
        // 角色选项
        roleOPtions: Array<Object> = [];
        // 创建用户名称的规则判断
        newUserRules: object = {
            username: [
                {required: true, message: '请输入用户名', trigger: 'blur'}
            ],
            password: [
                {required: true, message: '请输入密码', trigger: 'blur'}
            ],
            role: [
                {required: true, message: '请输选择角色', trigger: 'change'}
            ],
            lockFlag: [
                {required: true, message: '请选择状态', trigger: 'change'}
            ],
            phone: [
                {required: true, validator: this.validatePhone, trigger: 'change'}
            ]
        };
        // 操作用户类型
        userType: string = '';

        // 检测手机号
        public validatePhone(rule, value, callback): void {
            if (value === '') {
                callback('请输入手机号');
            } else if (!(/^1[34578]\d{9}$/.test(value))) {
                callback('手机号有误，请重填');
            } else {
                callback();
            }
        }

        // table表格数据
        headerData: Array<object> = [
            {
                prop: 'username',
                label: '用户名',
            },
            {
                prop: 'phone',
                label: '手机号',
            },
            {
                prop: 'roles',
                label: '角色',
                type: 'tagList',
            },
            {
                prop: 'lockFlag',
                label: '状态',
                type: 'tag',
                translations: {
                    0: '有效',
                    9: '锁定'
                },
                styles: {
                    9: 'info',
                    0: 'success'
                }
            },
            {
                prop: 'createTime',
                label: '创建时间',
            },
        ];
        tableData: Array<object> = [];
        paginationData: object = {
            currentPage: 1,
            pageSizes: [10, 20, 30, 40, 50, 100],
            pageSize: 10,
            total: 1
        };

        @Watch('dialogUserFromVisible')
        public dialogUserFromVisibleChange(newVal, oldVal) {
            if (!newVal) {
                this.resetUserForm();
            }
        }

        public mounted(): void {
            this.getUser();
            this.getRolePage();
        }

        // 搜索
        public search(): void {
            this.paginationData['currentPage'] = 1;
            this.getUser();
        }

        // 分页
        public handleSizeChange(): void {
            this.getUser();
        }

        public handleCurrentChange(): void {
            this.getUser();
        }

        // 删除用户按钮
        public delUser(data): void {
            this.$confirm(`此操作将永久删除"${data.username}", 是否继续?`, '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                let url = `/MetadataManage/user/${data.userId}`;

                this.$http.delete(url).then(response => {

                    if (response.data.code === 0) {
                        this.$message.success('删除成功');
                        this.getUser();
                    } else {
                        this.$message.error(response.data.msg)
                    }
                })
            })
        }

        // 新建编辑按钮
        public addEditUser(type, data?): void {
            this.userType = type;
            if (type === 'edit' || type === 'look') {
                data = JSON.parse(JSON.stringify(data));
                data.role = [];
                data.password = '';
                data.roleList.forEach(item => {
                    data.role.push(item.roleId)
                });
                this.newUserForm = data;
            }
            this.dialogUserFromVisible = true
        }

        // 获取用户数据
        public getUser(): void {
            let url = '/MetadataManage/user/page';
            const params = {
                current: this.paginationData['currentPage'],
                size: this.paginationData['pageSize'],
                username: this.username
            };

            this.$http.get(url, {params}).then(response => {
                const res = response.data;
                if (res.code === 0) {
                    this.tableData = res.data.records;
                    this.tableData.map(item => {
                        let roles: Array<string> = [];
                        item['roleList'].forEach(ite => {
                            roles.push(ite['roleName']);
                        });
                        item['roles'] = roles;
                    });
                    this.paginationData['currentPage'] = res.data.current;
                    this.paginationData['pageSize'] = res.data.size;
                    this.paginationData['total'] = res.data.total;
                }

            })
        }

        // 获取角色列表
        public getRolePage(): void {
            let url = '/MetadataManage/role/list';

            this.$http({
                url,
                method: 'get',
            }).then(response => {

                const res = response.data;
                if (res.code === 0) {
                    const data = res.data;
                    this.roleOPtions = data;
                } else {
                    this.$message.error(res.msg);
                }
            })
        }

        //  用户表创建提交
        public userFormSubmit(): void {
            this.$refs.roleForm['validate']((valid) => {
                if (valid) {
                    if (this.userType === 'add') this.addUser();
                    else if (this.userType === 'edit') this.editUser();
                } else {
                    return false;
                }
            });
        }

        // 新增用户
        public addUser(): void {
            let URL = '/MetadataManage/user';

            this.$http.post(URL, this.newUserForm).then(response => {

                const res = response.data;
                if (res.code === 0) {
                    this.getUser();
                    this.$message.success('新增成功');
                    this.dialogUserFromVisible = false;
                }
            })
        }

        // 编辑用户
        public editUser(): void {
            let URL = '/MetadataManage/user';
            this.$http.put(URL, this.newUserForm).then(response => {
                const res = response.data;
                if (res.code === 0) {
                    this.getUser();
                    this.$message.success('修改成功');
                    this.dialogUserFromVisible = false;
                }
            })
        }

        // 重置用户表
        public resetUserForm(): void {
            this.$refs.roleForm['resetFields']();
            this.newUserForm = {
                username: '', // 用户名
                userId: '', // 用户ID
                password: '', // 密码
                phone: '', // 手机号
                role: [], // 角色
                lockFlag: '', // 是否锁定
                createTime: '', //创建时间
                deptId: '', //所属部门id
                deptName: '', //所属部门名称
            };
        }
    }
</script>

<style lang="less">
    .role {
        .operate {
            .operate-special {
                width: 30%;
                display: flex;
            }

            .search {
                margin-right: 10px;
            }
        }

        .table-wrapper {
            width: 100%;
            height: 100%;
            padding: 10px;
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
            }
        }
    }

</style>
