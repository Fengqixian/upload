<template>
    <div class="rule">
        <cb-operation-container>
            <template slot="toOperation">
                <div class="operation">
                    <div class="top">
                        <div class="input-wrapper">
                            <div class="input">
                                <span>规则名称：</span>
                                <el-input
                                        placeholder="请输入"
                                        v-model.trim="ruleVal"
                                        clearable
                                        @keydown.enter.native.stop.prevent="getData">
                                </el-input>
                            </div>
                            <div class="input">
                                <span>状态：</span>
                                <el-select v-model.trim="statusVal"
                                           clearable
                                           placeholder="请选择"
                                           @keydown.enter.native.stop.prevent="getData">
                                    <el-option label="启用" value="1"></el-option>
                                    <el-option label="禁用" value="2"></el-option>
                                </el-select>
                            </div>
                            <el-button type="primary" @click="getData" style="margin-left: 10px;">查 询</el-button>

                        </div>
                        <div class="btn">
                            <el-button type="primary" @click="dialogDesensitizationVisible=true">创建加密/脱敏</el-button>
                            <!--<el-button type="primary" @click="$router.push('/edit-rule')">-->
                            <el-button type="primary" @click="dialogSQLVisible=true">
                                创建标签
                            </el-button>
                        </div>
                    </div>
                </div>
            </template>
            <template slot="cbContainer">
                <div class="table">
                    <cb-table :table-data="tableData"
                              :header-data="headerData"
                              :pagination-data="paginationData"
                              @size-change="getData"
                              @current-change="getData">
                        <template slot="column">
                            <el-table-column
                                    label="操作"
                                    width="100">
                                <template slot-scope="{row}">
                                    <!--<el-button @click="handleClick(scope.row)" type="text" size="small">查看</el-button>-->
                                    <el-button type="text"
                                               size="small"
                                               @click="editRule(row)">编辑
                                    </el-button>
                                </template>
                            </el-table-column>
                        </template>
                    </cb-table>
                </div>
            </template>
        </cb-operation-container>


        <el-dialog title="加密/脱敏" :visible.sync="dialogDesensitizationVisible">
            <el-form :model="desensitizationForm"
                     label-width="100px"
                     :rules="desensitizationFormRules"
                     ref="desensitizationFormRef">
                <el-form-item label="名称" prop="ruleName">
                    <el-input v-model.trim="desensitizationForm.ruleName" placeholder="名称"
                              @keydown.enter.native.stop.prevent="submitDesensitization"></el-input>
                </el-form-item>
                <el-form-item label="状态" prop="status">
                    <!--<el-select v-model="desensitizationForm.status" placeholder="请选择状态">
                        <el-option label="启用" value="1"></el-option>
                        <el-option label="禁用" value="2"></el-option>
                    </el-select>-->
                    <el-radio v-model.trim="desensitizationForm.status" label="1">启用</el-radio>
                    <el-radio v-model.trim="desensitizationForm.status" label="2">禁用</el-radio>
                </el-form-item>
                <el-form-item label="加密/脱敏方式" prop="encryptionType">
                    <el-select v-model.trim="desensitizationForm.encryptionType" placeholder="请选择加密/脱敏方式">
                        <el-option label="正则表达式" value="REG"></el-option>
                        <el-option label="AES方式" value="AES"></el-option>
                        <el-option label="MD5方式" value="MD5"></el-option>
                        <el-option label="BASE64方式" value="BASE64"></el-option>
                        <el-option label="DES方式" value="DES"></el-option>
                    </el-select>
                </el-form-item>
                <template v-if="desensitizationForm.encryptionType === 'REG'">
                    <el-form-item label="正则表达式" prop="ruleExpression">
                        <el-input v-model.trim="desensitizationForm.ruleExpression" placeholder="正则表达式"
                                  @keydown.enter.native.stop.prevent="submitDesensitization"></el-input>
                    </el-form-item>
                    <el-form-item label="替换符" prop="sign">
                        <el-input v-model.trim="desensitizationForm.sign" placeholder="替换符"
                                  @keydown.enter.native.stop.prevent="submitDesensitization"></el-input>
                    </el-form-item>
                </template>
                <template v-if="desensitizationForm.encryptionType.match(/ES$/)">
                    <el-form-item label="秘钥" prop="sign">
                        <el-input v-model.trim="desensitizationForm.sign" placeholder="秘钥"
                                  @keydown.enter.native.stop.prevent="submitDesensitization"></el-input>
                    </el-form-item>
                </template>
                <el-form-item label="描述">
                    <el-input type="textarea" v-model.trim="desensitizationForm.remarks" placeholder="描述"
                              @keydown.enter.native.stop.prevent="submitDesensitization"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogDesensitizationVisible = false">取 消</el-button>
                <el-button type="primary" @click="submitDesensitization">确 定</el-button>
            </div>
        </el-dialog>

        <el-dialog title="标签" :visible.sync="dialogSQLVisible">
            <el-form ref="sqlFormRef"
                     :model="sqlForm"
                     label-width="80px"
                     :rules="sqlFormRules"
                     class="info-form">
                <el-form-item label="规则名称" prop="ruleName">
                    <el-input v-model.trim="sqlForm.ruleName" placeholder="规则名称"
                              @keydown.enter.native.stop.prevent="submitSql"></el-input>
                </el-form-item>
                <el-form-item label="状态" prop="status">
                    <el-radio v-model.trim="sqlForm.status" label="1">启用</el-radio>
                    <el-radio v-model.trim="sqlForm.status" label="2">禁用</el-radio>
                </el-form-item>
                <el-form-item label="sql语句" prop="sqlInfo">
                    <el-input type="textarea" :rows="5" v-model="sqlForm.sqlInfo" placeholder="sql语句"
                              @keydown.enter.native.stop.prevent="submitSql"></el-input>
                </el-form-item>
                <el-form-item label="规则描述">
                    <el-input type="textarea" v-model.trim="sqlForm.remarks" placeholder="规则描述"
                              @keydown.enter.native.stop.prevent="submitSql"></el-input>
                </el-form-item>
                <!--<el-form-item label="">-->
                <!--<el-button @click="dialogSQLVisible = false" style="float: right;" class="del-nobg">取 消</el-button>-->
                <!--<el-button @click="submitSql" style="float: right;" class="add-bg">确 定</el-button>-->
                <!--</el-form-item>-->
            </el-form>

            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogSQLVisible = false">取 消</el-button>
                <el-button type="primary" @click="submitSql">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component,Watch} from 'vue-property-decorator'

    @Component({})
    export default class Index extends Vue {
        name: string = 'Index';

        ruleVal = '';
        statusVal = '';
        headerData = [
            {
                prop: 'ruleName',
                label: '名称'
            },
            {
                prop: 'ruleType', //  规则类型 0：脱敏 1:标签
                label: '类型',
                type: 'tag',
                translations: {
                    0: '脱敏加密',
                    1: '标签'
                },
                styles: {
                    0: 'default',
                    1: 'success'
                }
            },
            {
                prop: 'status', //  状态 1：启用 2：停用
                label: '状态',
                type: 'tag',
                translations: {
                    1: '启用',
                    2: '禁用'
                },
                styles: {
                    1: 'success',
                    2: 'error'
                }
            },
            {
                prop: 'remarks',
                label: '备注'
            },
        ];
        tableData = [];
        paginationData = {
            total: 1, // 总共数据
            pageSize: 10, // 每页显示条数
            currentPage: 1 // 当前页数
        };
        dialogDesensitizationVisible = false;
        desensitizationForm = {
            ruleName: '', // 名称
            remarks: '', // 描述
            status: '1', // 状态
            encryptionType: 'REG', // 加密脱敏方式
            ruleExpression: '', // 正则表达式
            sign: '', // 替换符 或者 秘钥

        };
        // 创建修改组合规则校验
        desensitizationFormRules = {
            ruleName: [{required: true, message: ' ', trigger: 'change'}],
            status: [{required: true, message: ' ', trigger: 'change'}],
            encryptionType: [{required: true, message: ' ', trigger: 'change'}],
            ruleExpression: [{required: true, message: ' ', trigger: 'change'}],
            sign: [{required: true, message: ' ', trigger: 'change'}],
        };
        id: any = null; // 脱敏规则的索引id
        ruleId = null; // 脱敏规则的 ruleId

        /**
         * 手动输入标签SQL
         */
        dialogSQLVisible = false;
        // sql form表单数据
        sqlForm = {
            id: '', // 主键
            ruleId: '', // sql规则id
            ruleName: '', // 规则名称
            status: '1', // 状态
            remarks: '',// 备注
            sqlInfo: '' // sql语句
        };
        // sql form 规则
        sqlFormRules = {
            ruleName: [{required: true, message: ' ', trigger: 'change'}],
            status: [{required: true, message: ' ', trigger: 'change'}],
            sqlInfo: [{required: true, message: ' ', trigger: 'change'}],
        };
        mounted() {
            this.getData();
        };
        @Watch('dialogDesensitizationVisible')
        dialogDesensitizationVisibleChange(newVal) {
            if (!newVal) {
                this.desensitizationForm = {
                    ruleName: '', // 名称
                    remarks: '', // 描述
                    status: '1', // 状态
                    encryptionType: 'REG', // 加密脱敏方式
                    ruleExpression: '', // 正则表达式
                    sign: '', // 替换符 或者 秘钥

                };
                this.$refs.desensitizationFormRef['resetFields']();
                this.id = null;
                this.ruleId = null;
            }
        };
        @Watch('dialogSQLVisible')
        dialogSQLVisibleChange(newVal) {
            if (!newVal) {
                this.sqlForm = {
                    id: '', // 主键
                    ruleId: '', // sql规则id
                    ruleName: '', // 规则名称
                    status: '1', // 状态
                    remarks: '',// 备注
                    sqlInfo: '' // sql语句
                };
                this.$refs.sqlFormRef['resetFields']();
            }
        };

        /**
         * 获取规则数据
         */
        getData() {
            let url = '/strategy/ruleInfo/page';
            let params = {
                size: this.paginationData.pageSize,
                current: this.paginationData.currentPage,
            };
            if (this.ruleVal) {
                params['ruleName'] = this.ruleVal;
            }
            if (this.statusVal) {
                params['status'] = this.statusVal;
            }
            this.$http.get(url, {params}).then(response => {
                if (response.data.code === 0) {
                    let data = response.data.data;
                    this.tableData = data.records;
                    this.paginationData.total = data.total;
                }
            })

        };
        /**
         * 编辑加密脱敏 按钮
         */
        editRule(data) {
            if (data.ruleType + '' === '1') {
                // this.$router.push({path: '/edit-rule', query: {id: data.id}});
                this.getSqlData(data.id);
            } else {
                this.getDesensitizationData(data.id)
            }
        };
        /**
         * 获取sql标签数据
         */
        getSqlData(id) {
            let ruleType = '1';
            let url = '/strategy/ruleInfo/getTypeRule';
            let params = {id, ruleType};
            this.$http.get(url, {params}).then(response => {
                if (response.data.code === 0) {
                    let data = response.data.data;
                    this.sqlForm.id = data.id;
                    this.sqlForm.ruleId = data.ruleId;
                    this.sqlForm.ruleName = data.ruleName;
                    this.sqlForm.remarks = data.remarks;
                    this.sqlForm.sqlInfo = data.sqlInfo;
                    this.sqlForm.status = data.status + '';
                    this.dialogSQLVisible = true;
                }
            })
        };
        /**
         * 提交创建加密脱敏表单
         */
        submitDesensitization() {
            this.$refs.desensitizationFormRef['validate']((valid) => {
                if (valid) {
                    let data = {
                        ruleName: this.desensitizationForm.ruleName, // 规则名称
                        remarks: this.desensitizationForm.remarks, // 备注
                        status: parseInt(this.desensitizationForm.status), // 状态 1：启用 2：停用 int
                        encryptionType: this.desensitizationForm.encryptionType, // 加密脱敏方式
                        ruleExpression: this.desensitizationForm.encryptionType === 'REG' ?
                            this.desensitizationForm.ruleExpression : undefined, // 正则表达式
                        sign: this.desensitizationForm.encryptionType.match(/\d/ig) ? undefined : this.desensitizationForm.sign, //  替换符 或者 秘钥
                        id: this.id,
                        ruleId: this.ruleId,
                        ruleType: '0', //  规则类型 0：脱敏 1:标签
                    };
                    let url = `/strategy/ruleInfo/${this.ruleId ? 'update' : 'save'}`;
                    this.$http({
                        url,
                        method: this.id ? 'put' : 'post',
                        data
                    }).then(response => {
                        this.id = null;
                        this.ruleId = null;
                        let res = response.data;
                        if (res.code === 0) {
                            this.$message.success("操作成功");
                            this.dialogDesensitizationVisible = false;
                            this.getData();
                        }
                    });
                } else {
                    return false;
                }
            });

        };
        /**
         * 获取加密脱敏数据
         * @param id
         */
        getDesensitizationData(id) {
            this.id = id;
            let ruleType = '1';
            let url = '/strategy/ruleInfo/getTypeRule';
            let params = {id, ruleType};
            this.$http.get(url, {params}).then(response => {
                if (response.data.code === 0) {
                    // this.analysisGetData(response.data.data);
                    let data = response.data.data;
                    data.status = data.status.toString();
                    this.desensitizationForm = {...data};
                    this.ruleId = data.ruleId;
                    this.dialogDesensitizationVisible = true;
                }
            })
        };
        /**
         * 提交标签SQL表单
         */
        submitSql() {
            this.$refs.sqlFormRef['validate']((valid) => {
                if (valid) {
                    let data = {
                        ruleName: this.sqlForm.ruleName, // 规则名称
                        sqlInfo: this.sqlForm.sqlInfo, // sql语句
                        status: parseInt(this.sqlForm.status), // 状态 1：启用 2：停用 int
                        ruleType: '1', //  规则类型 0：脱敏 1:标签
                        remarks: this.sqlForm.remarks, // 备注
                        type: 1, //   0：组装类型  1:sql类型 int
                        id: this.sqlForm.id,
                        ruleId: this.sqlForm.ruleId
                    };
                    let url = `/strategy/ruleInfo/${this.sqlForm.id ? 'update' : 'save'}`;
                    this.$http({
                        url,
                        method: this.sqlForm.id ? 'put' : 'post',
                        data
                    }).then(response => {
                        let res = response.data;
                        if (res.code === 0) {
                            this.$message.success("操作成功");
                            this.dialogSQLVisible = false;
                            this.getData();
                        }
                    });
                } else {
                    return false;
                }
            });
        }
    }
</script>

<style scoped lang="less">
    .rule {
        .operation {
            display: flex;
            flex-direction: column;

            .top {
                display: flex;
                align-items: center;
                justify-content: space-between;

                .input-wrapper {
                    display: flex;
                    align-items: center;

                    .input {
                        display: flex;
                        align-items: center;

                        & > span {
                            width: 68px;
                            text-align: end;
                        }

                        & > div {
                            flex: 1;
                        }
                    }
                }

                .btn {
                    margin-left: 10px;
                }
            }
        }

        .table {
            width: 100%;
            height: 100%;
        }
    }
</style>
