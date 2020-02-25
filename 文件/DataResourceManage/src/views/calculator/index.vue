<template>
    <div class="calculator header-operate-btn">
        <div class="operate">
            <div class="operate-special">
                <!--<el-input v-model.trim="searchVal" placeholder="请输入服务ID" class="search"></el-input>-->
                <!--<el-button type="default" @click="search">查询</el-button>-->
                <el-button type="primary" @click="addEditDataServer('add')">新建</el-button>
            </div>
        </div>
        <div class="table">
            <DynamicTable
                    :headerData="headerData"
                    :tableData="tableData"
                    :paginationData="paginationData"
                    operate-width="100px"
                    :operate="true"
                    @currentChange="currentChange"
                    @sizeChange="sizeChange">
                <template slot-scope="{row}">
                    <div class="table-operate">
                        <span class="edit" @click="addEditDataServer('edit',row)">编辑</span>
                        <span class="edit" @click="delDataServer(row)">删除</span>
                    </div>
                </template>
            </DynamicTable>
        </div>

        <!-- sql编辑 -->
        <el-dialog :title="dataServiceForm.id === null?'新增': '编辑'"
                   :visible.sync="sqlVisible"
                   width="80%"
                   :close-on-click-modal="false">
            <el-form :model="dataServiceForm" :rules="dataServiceRules" ref="dataServiceFormRef" label-width="110px">
                <el-form-item label="名称" prop="serverName">
                    <el-input v-model.trim="dataServiceForm.serverName" placeholder="请输入名称"></el-input>
                </el-form-item>
                <el-form-item label="中文名称" prop="serverNameCn">
                    <el-input v-model.trim="dataServiceForm.serverNameCn" placeholder="请输入中文名称"></el-input>
                </el-form-item>
                <el-form-item label="返回类型" prop="returnType">
                    <el-select v-model.trim="dataServiceForm.returnType" placeholder="请选择返回类型">
                        <el-option label="XML" value="XML"></el-option>
                        <el-option label="JSON" value="JSON"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="数据源连接信息" prop="datasourceId">
                    <el-select v-model.trim="dataServiceForm.datasourceId" placeholder="请选择数据源连接信息">
                        <el-option label="1" value="1"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="执行SQL语句" prop="querySql">
                    <el-input type="textarea" :minRows="3" :maxRows="6"
                              v-model.trim="dataServiceForm.querySql" placeholder="请输入执行SQL语句"></el-input>
                </el-form-item>
                <el-form-item label="描述" prop="serverDesc">
                    <el-input type="textarea" :minRows="6" :maxRows="10"
                              v-model.trim="dataServiceForm.serverDesc" placeholder="请输入描述"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="sqlVisible = false">取 消</el-button>
                <el-button type="primary" @click="submit" v-if="dataServiceForm.id !== null">修 改</el-button>
                <el-button type="primary" @click="submit = true" v-else>确 认</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component, Watch} from 'vue-property-decorator'

    @Component({})
    export default class Index extends Vue {
        name: string = 'Index';
        // 搜索内容
        searchVal: string = '';
        headerData: Array<object> = [
            {
                prop: 'serverNameCn',
                label: '中文名称',
            },
            {
                prop: 'serverName',
                label: '名称',
            },
            {
                prop: 'datasourceId',
                label: '数据源连接信息',
            },
            {
                prop: 'querySql',
                label: '执行SQL语句',
            },
            {
                prop: 'returnType',
                label: '返回类型',
            },
            {
                prop: 'serverDesc',
                label: '查询所有的数据',
            },
        ];
        tableData: Array<object> = [{
            nameCn: '服务1',
            nameEn: 'server1',
            desc: '查询num大于10 的所有数据',
            sql: 'SELECT * FROM table1 WHERE num > 10;'
        }];
        paginationData: object = {
            currentPage: 1,
            pageSizes: [10, 20, 30, 40, 50, 100],
            pageSize: 10,
            total: 1
        };

        // sql对话框
        sqlVisible: boolean = false;  // sql对话框是否展示
        dataServiceForm: object = {
            id: null,  // 主键
            serverName: '', // 服务名称
            serverDesc: '', // 服务描述
            serverNameCn: '', // 服务中文名称
            querySql: '', // 执行SQL语句
            datasourceId: '1', // 数据源连接信息
            returnType: 'JSON' // 返回类型
        }; // form表单数据
        dataServiceRules: object = {
            serverName: [{required: true, message: '请输入服务名称', trigger: 'blur'}],
            serverNameCn: [{required: true, message: '请输入服务中文名称', trigger: 'blur'}],
            querySql: [{required: true, message: '请输入执行sql语句', trigger: 'blur'}],
            datasourceId: [{required: true, message: '请选择数据源连接信息', trigger: 'blur'}],
            returnType: [{required: true, message: '请选择返回类型', trigger: 'blur'}],
        }; // form表单校验

        @Watch('sqlVisible')
        public sqlVisibleChange(val): void {
            if (!val) this.resetataServiceForm();
        }

        public mounted(): void {
            this.getDataServerPage();
        }

        // 删除服务
        public delDataServer(data): void {
            this.$confirm(`此操作将永久删除"${data.serverName}", 是否继续?`, '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                let url = '/clinbrainDataserver/dataServer/' + data.id;
                this.$http.delete(url).then(response => {
                    const res = response.data;
                    if (res.code === 0) {
                        this.$message.success('删除成功');
                        this.getDataServerPage();
                    }
                })
            });
        }

        // 获取服务列表
        public getDataServerPage(): void {
            let params = {
                current: this.paginationData['currentPage'],
                size: this.paginationData['pageSize']
            }
            let url = '/clinbrainDataserver/dataServer/page';
            this.$http.get(url, {params}).then(response => {
                const data = response.data;
                if (data.code === 0) {
                    this.tableData = data.data.records;
                    this.paginationData['currentPage'] = data.data.current;
                    this.paginationData['total'] = data.data.pages;
                }
            })
        }

        // 分页
        public sizeChange(): void {
            this.getDataServerPage();
        }

        public currentChange(): void {
            this.getDataServerPage();
        }


        // 重置表单
        public resetataServiceForm(): void {
            this.$refs.dataServiceFormRef['resetFields']();
        }

        // 提交对话框form表单
        public submit(): void {
            this.$refs.dataServiceFormRef['validate']((valid) => {
                if (valid) {
                    let url = '/clinbrainDataserver/dataServer/edit';
                    this.$http.post(url, this.dataServiceForm).then(response => {
                        this.sqlVisible = false;
                        if (response.data.status === 1) {
                            this.$message.success(this.dataServiceForm['id'] === null ? '新增成功' : '修改成功')
                        }
                        this.paginationData['currentPage'] = 1;
                        this.getDataServerPage();
                    }).catch(err => {
                        this.sqlVisible = false;
                        this.$message.error(this.dataServiceForm['id'] === null ? '新增失败' : '修改失败')
                    })
                } else {
                    return false;
                }
            })
        }

        // 搜索
        public search(): void {

        }

        // 编辑
        public addEditDataServer(type, data): void {
            if (type === 'edit') {
                this.dataServiceForm = JSON.parse(JSON.stringify(data));
            }
            this.sqlVisible = true;
        }

    }
</script>

<style scoped lang="less">
    .calculator {
        overflow: auto;

        .operate {
            .operate-special {
                width: 30%;
                display: flex;
            }

            .search {
                margin-right: 10px;
            }
        }

        .table {
            margin: 46px 10px 10px 10px;
            box-shadow: 0 0 10px #cccccc;
            padding: 20px;

            .table-operate {
                display: flex;

                span {
                    cursor: pointer;
                    margin-right: 10px;
                }

                .look {
                    &:hover {
                        color: rgba(0, 0, 0, 0.3) !important;
                    }
                }

                .edit {
                    color: #188bf5;

                    &:hover {
                        color: rgba(24, 139, 245, 0.7);
                    }
                }
            }
        }
    }
</style>
