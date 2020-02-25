<template>
    <div class="technology-view cb-tree-show-operation-wrapper">
        <div class="tree-wrapper">
            <cb-tree
                    :data="treeData"
                    nodeKey="id"
                    :default-expanded-keys="defaultExpandedKeys"
                    ref="tree">
                <template slot="searchAppend">
                    <el-tooltip effect="dark" content="新增数据库" placement="top-start">
                        <el-button type="text"
                                   icon="el-icon-plus"
                                   class="add-text"
                                   style="margin-left: 5px;"
                                   @click.prevent.stop="addEditDatabase(null,'database')">
                        </el-button>
                    </el-tooltip>
                </template>
                <template slot-scope="{data,node}">
                    <div class="tree-name" @click="handleNodeClick(data,node)">
                        <span>{{data.nameCn || data.nameEn || 'unknown'}}</span>
                        <div class="operation-wrapper">
                            <el-tooltip effect="dark" content="编辑" placement="top-start">
                                <el-button type="text"
                                           class="edit-text"
                                           icon="el-icon-edit"
                                           @click.prevent.stop="addEditDatabase(node,data.nodeType)">
                                </el-button>
                            </el-tooltip>
                            <el-tooltip effect="dark"
                                        content="导入"
                                        placement="top-start"
                                        v-if="data.nodeType === 'database'">
                                <el-button type="text"
                                           icon="iconfont icon-daoru1"
                                           class="add-text"
                                           style="margin-left: 5px;"
                                           @click.prevent.stop="getImportData(data,node)">
                                </el-button>
                            </el-tooltip>
                        </div>
                    </div>
                </template>
            </cb-tree>
        </div>
        <div class="tab-wrapper show-operation-wrapper">
            <cb-tab :tabData="tabsAllData"
                    v-model.trim="tabsActiveId">
                <template :slot="tab.name"
                          v-for="tab in tabsAllData">
                    <div class="tab-container-wrapper">
                        <technology-tab :arg="tab"/>
                    </div>
                </template>
            </cb-tab>
        </div>
        <el-dialog
                title="数据库"
                :visible.sync="databaseDialogVisible"
                :close-on-click-modal="false"
                width="30%">
            <el-form :model="databaseForm" :rules="databaseFormRules" ref="databaseFormRef" label-width="100px">
                <el-form-item label="中文名称" prop="nameCn">
                    <el-input v-model.trim="databaseForm.nameCn" placeholder="中文名称"/>
                </el-form-item>
                <el-form-item label="英文名称" prop="nameEn">
                    <el-input v-model.trim="databaseForm.nameEn" placeholder="英文名称"/>
                </el-form-item>
                <el-form-item label="IP" prop="connectIp">
                    <el-input v-model.trim="databaseForm.connectIp" placeholder="IP"/>
                </el-form-item>
                <el-form-item label="端口号" prop="connectHost">
                    <el-input v-model.trim="databaseForm.connectHost" placeholder="端口号"/>
                </el-form-item>
                <el-form-item label="用户名" prop="connectUser">
                    <el-input v-model.trim="databaseForm.connectUser" placeholder="用户名"/>
                </el-form-item>
                <el-form-item label="密码" prop="connectPassword">
                    <el-input v-model.trim="databaseForm.connectPassword" placeholder="密码"/>
                </el-form-item>
                <el-form-item label="数据库类型" prop="databaseType">
                    <el-select v-model.trim="databaseForm.databaseType" placeholder="数据库类型">
                        <el-option label="mysql" value="mysql"/>
                        <el-option label="oracle" value="oracle"/>
                        <el-option label="gp" value="gp"/>
                        <el-option label="hive" value="hive"/>
                    </el-select>
                </el-form-item>
                <el-form-item label="描述" prop="remark">
                    <el-input type="textarea" v-model.trim="databaseForm.remark" placeholder="描述"/>
                </el-form-item>
                <el-form-item>
                    <div class="btn-right">
                        <el-button class="del-nobg" @click="databaseDialogVisible = false">取 消</el-button>
                        <el-button class="confirm-bg" @click="submitDatabaseForm()">确 定</el-button>
                    </div>
                </el-form-item>
            </el-form>

        </el-dialog>
        <el-dialog
                title="表"
                :visible.sync="tableDialogVisible"
                :close-on-click-modal="false"
                width="30%">
            <el-form :model="tableForm" :rules="tableFormRules" ref="tableFormRef" label-width="100px">
                <el-form-item label="中文名称" prop="nameCn">
                    <el-input v-model.trim="tableForm.nameCn" placeholder="中文名称"/>
                </el-form-item>
                <el-form-item label="英文名称" prop="nameEn">
                    <el-input v-model.trim="tableForm.nameEn" placeholder="英文名称"/>
                </el-form-item>
                <!--<el-form-item label="描述" prop="remark">
                    <el-input type="textarea" v-model.trim="tableForm.remark" placeholder="描述"/>
                </el-form-item>-->
                <el-form-item>
                    <div class="btn-right">
                        <el-button class="del-nobg" @click="tableDialogVisible = false">取 消</el-button>
                        <el-button class="confirm-bg" @click="submitTableForm()">确 定</el-button>
                    </div>
                </el-form-item>
            </el-form>
        </el-dialog>

        <el-dialog title="元数据采集"
                   :visible.sync="importVisible"
                   :close-on-click-modal="false"
                   width="60%"
                   class="import-dialog">
            <el-tabs v-model="activeName" v-if="importVisible">
                <el-tab-pane label="未导入" name="neverImport">
                    <cb-check-box :data="neverImportAllData"
                                  :defaultChecked="neverImportChecked"
                                  checkName="nameEn"
                                  ref="neverImportRef"/>
                </el-tab-pane>
                <el-tab-pane label="导入后变更字段信息" name="changeImport">
                    <cb-check-box :data="changeImportAllData"
                                  :defaultChecked="changeImportChecked"
                                  checkName="nameEn"
                                  ref="changeImportRef">
                        <template slot-scope="{data}">
                            <el-popover
                                    placement="right"
                                    width="500"
                                    trigger="click">
                                <el-table :data="data.changeColList">
                                    <el-table-column property="nameCn"
                                                     label="中文名"/>
                                    <el-table-column property="nameEn"
                                                     label="英文名"/>
                                </el-table>
                                <span slot="reference"
                                      class="iconfont icon-tree-column"
                                      style="float: right;margin-right: 10px;"
                                      @click.stop.prevent="void(0)"/>
                            </el-popover>
                        </template>
                    </cb-check-box>
                </el-tab-pane>
                <el-tab-pane label="已导入" name="alreadyImport">
                    <cb-check-box :data="checkedImportAllData"
                                  checkName="nameEn"
                                  :allDisabled="true"
                                  ref="checkedImportRef"/>
                </el-tab-pane>
            </el-tabs>
            <div class="btn-right">
                <el-button class="del-nobg" @click="importVisible = false">取 消</el-button>
                <el-button class="confirm-bg" @click="submitImport">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component, Watch} from 'vue-property-decorator'
    import config from '../../config'
    import {State, Mutation} from 'vuex-class'
    import TechnologyTab from './technologyTab.vue'
    import {isIE} from '../../common/util/IE'
    import CbCheckBox from '../../components/cbCheckbox/index'

    @Component({
        components: {TechnologyTab, CbCheckBox}
    })
    export default class BloodRelation extends Vue {
        myThis: any = this
        @State pageSizes
        @State pageSize
        @State loadingFlag
        @Mutation setLoadingFlag
        name: string = 'MetaImport'
        // 元模型tree数据
        treeData: Array<object> = []
        defaultExpandedKeys = [];
        defaultProps: object = {
            children: 'children',
            label: 'label'
        }
        // form表单其他数据
        otherData: any = null
        // tab标签
        tabsActiveId: string = ''
        //  tab内所有的数据
        tabsAllData: Array<object> = [
            /*{
                label: 1111,
                name: '1'
            }*/
        ];
        // tree搜索值
        searchModelValue: string = ''

        /**
         * 新增编辑数据库
         */
        databaseDialogVisible: boolean = false;
        databaseForm: {
            id: number,
            nameEn: string,
            nameCn: string,
            connectIp: string,
            connectHost: string,
            connectPassword: string,
            connectUser: string,
            remark: string,
            databaseType: string,
        } = {
            id: null,
            nameEn: '',
            nameCn: '',
            connectIp: '',
            connectHost: '',
            connectPassword: '',
            connectUser: '',
            remark: '',
            databaseType: '',
        };
        databaseFormRules = {
            nameCn: [{required: true, message: ' ', trigger: 'blur'}],
            nameEn: [{required: true, message: ' ', trigger: 'blur'}],
            connectIp: [{required: true, message: ' ', trigger: 'blur'}],
            connectHost: [{required: true, message: ' ', trigger: 'blur'}],
            connectPassword: [{required: true, message: ' ', trigger: 'blur'}],
            connectUser: [{required: true, message: ' ', trigger: 'blur'}],
            databaseType: [{required: true, message: ' ', trigger: 'blur'}]
        };
        tableDialogVisible: boolean = false;
        tableForm: {
            id: number,
            databaseId: number,
            resourceId: string,
            resourceCode: string,
            nameCn: string,
            nameEn: string,
            remark: string,
        } = {
            id: null,
            databaseId: null,
            resourceId: null,
            resourceCode: '',
            nameCn: '',
            nameEn: '',
            remark: '',
        };
        tableFormRules = {
            nameCn: [{required: true, message: ' ', trigger: 'blur'}],
            nameEn: [{required: true, message: ' ', trigger: 'blur'}]
        };


        /**
         * 导入
         */
        importVisible: boolean = false;
        neverImportChecked = [];
        neverImportIsIndeterminate = false;
        neverImportCheckAll = false;
        neverImportAllDataId = [];
        neverImportAllData = [
            /*{
                nameCn: 'add',
                nameEn: 'add',
                id: '1'
            }*/
        ];


        changeImportChecked = [];
        changeImportIsIndeterminate = false;
        changeImportCheckAll = false;
        changeImportAllDataId = [];
        changeImportAllData = [
            /*{
                nameCn: 'add',
                nameEn: 'add',
                id: '1'
            }*/
        ];

        checkedImportAllData = [];
        importCurrentTreeData = null;
        activeName: string = 'neverImport';

        // 监听删选框数据变化
        @Watch('searchModelValue')
        onChangeSearchModelValue(val: string, oldVal: string) {
            this.myThis.$refs.tree.filter(val);
        }

        // 监听删选框数据变化
        @Watch('databaseDialogVisible')
        onChangeDatabaseDialogVisible(val: boolean) {
            if (!val) {
                this.databaseForm = {
                    id: null,
                    nameEn: '',
                    nameCn: '',
                    connectIp: '',
                    connectHost: '',
                    connectPassword: '',
                    connectUser: '',
                    remark: '',
                    databaseType: '',
                }
                this.$refs.databaseFormRef['resetFields']();
            }
        }

        // 监听删选框数据变化
        @Watch('importVisible')
        onChangeImportVisible(val: boolean) {
            if (!val) {
                this.activeName = 'neverImport';
                this.neverImportChecked = [];
                this.neverImportIsIndeterminate = false;
                this.neverImportCheckAll = false;
                this.neverImportAllDataId = [];
                this.neverImportAllData = [
                    /*{
                        nameCn: 'add',
                        nameEn: 'add',
                        id: '1'
                    }*/
                ];

                this.changeImportChecked = [];
                this.changeImportIsIndeterminate = false;
                this.changeImportCheckAll = false;
                this.changeImportAllDataId = [];
                this.changeImportAllData = [
                    /*{
                        nameCn: 'add',
                        nameEn: 'add',
                        id: '1'
                    }*/
                ];

                this.checkedImportAllData = [];
                this.importCurrentTreeData = null;
            }
        }

        mounted() {
            this.getTreeData();
        }

        /**
         * 提交导入
         */
        submitImport() {
            const {data: neverImportAllData, checkedItem: neverImportChecked} = this.$refs.neverImportRef['getData']();
            const {data: changeImportAllData, checkedItem: changeImportChecked} = this.$refs.changeImportRef['getData']();
            if (!neverImportChecked.length && !changeImportChecked.length) {
                this.$message.warning('请选择导入数据再提交');
                return;
            }
            let tableList = [];
            neverImportAllData.forEach(item => {
                if (neverImportChecked.includes(item.nameEn)) tableList.push(item)
            });

            let changeList = [];
            changeImportAllData.forEach(item => {
                if (changeImportChecked.includes(item.nameEn)) changeList.push(item)
            });

            const params = {
                type: this.importCurrentTreeData.databaseType, // gp,hive,mysql,sqlserver
                databaseId: this.importCurrentTreeData.id, //数据库标识
                dbName: this.importCurrentTreeData.nameEn, // 数据库名
                tableList, // 数据库名
                changeList, // 数据库名
            };
            const url = config.port('technologyView') + '/importDataBase';
            this.$http.post(url, params).then(response => {
                if (response.data.code === 0) {
                    this.$message.success('操作成功');
                    this.importVisible = false;
                    this.getTreeData();
                } else {
                    this.$message.error('操作失败')
                }
            })
        }

        /**
         * 从未导入 全选
         */
        neverImportCheckAllChange(val) {
            this.neverImportChecked = val ? this.neverImportAllDataId : [];
            this.neverImportIsIndeterminate = false;
        }

        /**
         * 从未导入 单选
         */
        neverImportChange(val) {
            let checkedCount = val.length;
            this.neverImportCheckAll = checkedCount === this.neverImportAllData.length;
            this.neverImportIsIndeterminate = checkedCount > 0 && checkedCount < this.neverImportAllData.length;
        }

        /**
         * 从未导入 全选
         */
        changeImportCheckAllChange(val) {
            this.changeImportChecked = val ? this.changeImportAllDataId : [];
            this.changeImportIsIndeterminate = false;
        }

        /**
         * 从未导入 单选
         */
        changeImportChange(val) {
            let checkedCount = val.length;
            this.changeImportCheckAll = checkedCount === this.changeImportAllData.length;
            this.changeImportIsIndeterminate = checkedCount > 0 && checkedCount < this.changeImportAllData.length;
        }

        // 获取tree树模型数据集合
        getTreeData(): void {
            let url = config.port('technologyView') + '/tree';
            this.$http.get(url).then(response => {
                const res = response.data;
                if (res.code === 0) {
                    this.treeData = res.data;
                }
            })
        }


        // tree节点被点击时的回调
        async handleNodeClick(data, node) {
            if (data.nodeType !== 'table') return;
            let dataId = data.id.toString();
            // 限制20个tabs
            if (this.tabsAllData.length >= 20) {
                // 查看tab是否已经存在tabsAllData中
                const repetition = this.tabsAllData.find(item => item['id'] === dataId);
                if (repetition !== undefined) {
                    this.tabsActiveId = dataId
                } else {
                    this.$message.warning('tabs标签不能大于20个')
                }
                return
            } else {
                this.tabsActiveId = dataId
            }
            // 禁止出现第二个相同的tabs
            for (let i = 0; i < this.tabsAllData.length; i++) {
                this.tabsActiveId = dataId;
                if (this.tabsAllData[i]['id'] === dataId) return
            }
            this.tabsAllData.map(item => item['activateFlag'] = false);
            // 给新加入到tab中的成员添加一个标记active  代表被激活 ，因为bloodTab组件需要用这个来刷新svg(解决非激活的tab svg被刷新的问题)
            data.activateFlag = true;
            // label tab展示的名称 name 是tab的标识
            data['label'] = data.nameCn || data.nameEn || 'unknown';
            data['name'] = dataId;
            this.tabsAllData.push(data);
            this.tabsActiveId = dataId;
        }

        // tree节点过滤
        treeFilterNode(value, data) {
            if (!value) return true;
            return data.name.indexOf(value) !== -1;
        }

        /**
         * 新增编辑数据库 表按钮
         * @param data
         * @param dataType
         */
        addEditDatabase(node?, dataType?) {
            if (dataType === 'database') {
                this.databaseDialogVisible = true;
                if (!node) return;
                const data = node['data'];
                this.databaseForm.id = data['id'];
                this.databaseForm.nameCn = data['nameCn'];
                this.databaseForm.nameEn = data['nameEn'];
                this.databaseForm.connectIp = data['connectIp'];
                this.databaseForm.connectHost = data['connectHost'];
                this.databaseForm.connectPassword = data['connectPassword'];
                this.databaseForm.connectUser = data['connectUser'];
                this.databaseForm.remark = data['remark'];
                this.databaseForm.databaseType = data['databaseType'];
            } else if (dataType === 'table') {
                this.tableDialogVisible = true;
                if (!node) return;
                const data = node['data'];
                this.tableForm.id = data['id'];
                this.tableForm.databaseId = data['databaseId'];
                this.tableForm.resourceId = data['resourceId'];
                this.tableForm.resourceCode = data['resourceCode'];
                this.tableForm.nameCn = data['nameCn'];
                this.tableForm.nameEn = data['nameEn'];
                this.tableForm.remark = data['remark'];
            }
        }

        /**
         * 提交表操作
         */
        submitDatabaseForm() {
            const url = config.port('technologyView');
            const data = this.databaseForm;
            this.$http({
                method: this.databaseForm.id ? 'put' : 'post',
                url: url + (this.databaseForm.id ? 'updateDataBase' : 'saveDataBase'),
                data
            }).then(response => {
                if (response.data.code === 0) {
                    this.$message.success('提交成功');
                    this.getTreeData();
                    this.databaseDialogVisible = false;
                }
            })
        }

        /**
         * 提交数据库操作
         */
        submitTableForm() {
            const url = config.port('technologyView') + 'updateTable';
            const data = this.tableForm;
            this.$http({
                method: 'put',
                url,
                data
            }).then(response => {
                if (response.data.code === 0) {
                    this.$message.success('提交成功');
                    this.getTreeData();
                    this.tableDialogVisible = false;
                }
            })
        }

        /**
         * 获取导入的数据
         */
        getImportData(data, node) {
            const url = config.port('technologyView') + 'getDataTableList';
            const params = {
                databaseId: data.id,
                type: data.databaseType,
                dbName: data.nameEn
            };
            this.importCurrentTreeData = data;
            this.$http.get(url, {params}).then(response => {
                if (response.data.code === 0) {
                    this.neverImportAllDataId = [];
                    this.neverImportAllData = [];
                    this.changeImportAllDataId = [];
                    this.changeImportAllData = [];
                    response.data.data.forEach(item => {
                        if (!item['changeType']) {
                            this.neverImportAllData.push(item);
                            this.neverImportAllDataId.push(item.nameEn);
                        } else {
                            this.changeImportAllData.push(item);
                            this.changeImportAllDataId.push(item.nameEn);
                        }
                    });
                    this.checkedImportAllData = [];
                    node.childNodes.forEach(child => {
                        this.checkedImportAllData.push(child.data)
                    });
                    this.importVisible = true;
                }
            })
        }

    }
</script>

<style lang="less">

    .technology-view {
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

        .tab-container-wrapper {
            width: 100%;
            height: 100%;
        }

        .import-dialog {
            .el-dialog {

                margin-top: 49px !important;
                height: 82% !important;
                position: relative;

                .import {
                    border-bottom: 1px solid #cccccc;
                    display: flex;
                    flex-direction: column;

                    &:last-child {
                        border: none;
                    }

                    .title-wrapper {
                        height: 20px;
                        display: flex;
                        align-items: center;
                        margin-top: 10px;
                        padding-bottom: 5px;
                        margin-bottom: 5px;
                        border-bottom: 1px dotted #ccc;

                        .decorate {
                            height: 16px;
                            border-right: 5px solid #1679da;
                            margin-right: 5px;
                        }

                        .title {
                            font-size: 14px;
                            font-weight: 600;
                            margin-right: 10px;
                        }
                    }

                    .import-container {
                        flex: 1;
                        overflow: auto;
                        display: flex;
                        flex-wrap: wrap;
                        max-height: 300px;

                        .item {
                            height: 30px;
                            width: 200px;
                            background-color: #ddd;
                            margin-right: 10px;
                            margin-bottom: 10px;
                            display: flex;
                            align-items: center;
                            padding: 0 10px;

                            .el-checkbox {
                                flex: 1;
                                display: flex;
                                overflow: hidden;
                                align-items: center;

                                .el-checkbox__label {
                                    flex: 1;
                                    overflow: hidden; //超出的文本隐藏
                                    text-overflow: ellipsis; //溢出用省略号显示
                                    white-space: nowrap; //溢出不换行
                                }
                            }

                            .iconfont {
                                cursor: pointer;

                                &:hover {
                                    color: #1679da;
                                }
                            }
                        }
                    }
                }

                .el-dialog__body {
                    position: absolute;
                    left: 0;
                    right: 0;
                    bottom: 0;
                    top: 43px;
                    display: flex;
                    flex-direction: column;
                    overflow: hidden;

                    .el-tabs {
                        flex: 1;
                        display: flex;
                        flex-direction: column;
                        overflow: hidden;

                        .el-tabs__header {
                            .el-tabs__nav-wrap {
                                .el-tabs__item {
                                    font-size: 12px;
                                    line-height: 26px;
                                    height: 29px;
                                }
                            }
                        }

                        .el-tabs__content {
                            flex: 1;

                            & > div {
                                width: 100%;
                                height: 100%;
                            }
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
