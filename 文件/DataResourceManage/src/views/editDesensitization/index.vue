<template>
    <div class="edit-desensitization">
        <div class="tree-wrapper" :class="{'add-column':isColumnRelation}">
            <cb-tree lazy
                     searchFromHttp
                     @search="searchTree"
                     v-show="searchVal === ''"
                     @load-node="loadNode"
                     :searchHidden="false"
                     node-key="uuid"
                     @node-click="dropColumn"
                     ref="tree1">
                <template slot-scope="{data,node}">
                    <span style="cursor: pointer;"
                          :class="cacheTreeCurrentData.find(item=>item === (data.nameEn + data.id)) !== undefined?'active':''">{{data.nameCn || data.nameEn}}</span>
                </template>
            </cb-tree>
            <!--<cb-tree @search="searchTree"
                     searchFromHttp
                     v-show="searchVal !== ''"
                     :data="treeData"
                     node-key="uuid"
                     ref="tree2">
                <template slot-scope="{data,node}">
                    <span style="cursor: pointer;" @click="dropColumn(data,node,arguments)">{{data.nameCn || data.nameEn}}</span>
                </template>
            </cb-tree>-->
        </div>
        <div class="rule" :class="{'add-column':isColumnRelation}">
            <div class="top">
                <el-breadcrumb separator-class="el-icon-arrow-right">
                    <el-breadcrumb-item :to="{ path: '/desensitization' }">加密脱敏策略</el-breadcrumb-item>
                    <el-breadcrumb-item>脱敏策略编辑</el-breadcrumb-item>
                </el-breadcrumb>
                <!-- <div class="search">
                     <el-input placeholder="请输入内容" v-model.trim="ruleVal" class="input-with-select">
                         <el-button slot="append" icon="el-icon-search"></el-button>
                     </el-input>
                 </div>-->
            </div>
            <div class="table-user">
                <div class="table">
                    <cb-table :table-data="tableData"
                              :header-data="headerData"
                              :pagination-data="paginationData"
                              paginationLayout="total, prev, next"
                              prevText="上一页"
                              nextText="下一页"
                              @row-click="ruleRowClick"
                              @size-change="getDesensitizationData"
                              @current-change="getDesensitizationData">
                        <template slot="column">
                            <el-table-column
                                    label="操作"
                                    width="100">
                                <template slot-scope="{row}">
                                    <i class="iconfont icon-yuanquan"
                                       v-if="row.status === 1"
                                       @click.stop.prevent="addRule(row,arguments)"
                                       :style="{'cursor':isColumnRelation?'not-allowed':'pointer'}"></i>
                                </template>
                            </el-table-column>
                        </template>
                    </cb-table>
                </div>
                <div class="user">
                    <el-table
                            :data="roleData"
                            style="width: 100%"
                            @row-click="roleRowClick"
                            height="100%">
                        <el-table-column
                                prop="userName"
                                label="角色名">
                        </el-table-column>
                        <el-table-column
                                label="操作"
                                width="100">
                            <template slot-scope="{row}">
                                <i class="iconfont icon-yuanquan"
                                   :style="{'cursor':isColumnRelation?'not-allowed':'pointer'}"
                                   @click.stop.prevent="addRole(row,arguments)"></i>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>
            </div>
            <div class="opration-wrapper">
                <div class="drag-to-relation">
                    <div class="drag-to-relation-content" ref="dragToRelationContent"></div>
                    <transition name="fade">
                        <div class="column-relation"
                             v-if="isColumnRelation"
                             @click.stop.prevent="">
                            <div class="title">
                                <span>{{ruleMapColumnRuleData.name}}关系的字段</span>
                                <span class="el-icon-circle-close"
                                      @click="closeColumnRelationTable"></span>
                            </div>
                            <div class="table-wrapper">
                                <cb-table :table-data="columnRelationTableData"
                                          :header-data="columnRelationHeaderData"
                                          :pagination-data="columnRelationPaginationData"
                                          highlight-current-row
                                          @current-change="paginationColumnRelationTable"
                                          @size-change="paginationColumnRelationTable"
                                          paginationLayout="total, prev, next"
                                          prevText="上一页"
                                          nextText="下一页"
                                          ref="columnTable">
                                    <template slot="column">
                                        <el-table-column label="操作"
                                                         width="50">
                                            <template slot-scope="{row}">
                                                <el-tooltip content="删除" placement="top" effect="dark">
                                                    <el-button type="text"
                                                               class="del-text"
                                                               icon="el-icon-delete"
                                                               @click.prevent.stop="delColumn(row)">
                                                    </el-button>
                                                </el-tooltip>
                                            </template>
                                        </el-table-column>
                                    </template>
                                </cb-table>
                            </div>
                        </div>
                    </transition>
                </div>
                <div class="form-wrapper">
                    <el-form ref="infoForm"
                             :model="infoForm"
                             label-width="80px" class="info-form">
                        <el-form-item label="策略名称">
                            <el-input v-model.trim="infoForm.name" placeholder="请填策略名称"></el-input>
                        </el-form-item>
                        <el-form-item label="策略状态">
                            <el-select v-model.trim="infoForm.status" placeholder="请选择">
                                <el-option label="启用" value="1"></el-option>
                                <el-option label="禁用" value="2"></el-option>
                                <!--<el-option label="其他" value="2"></el-option>-->
                            </el-select>
                        </el-form-item>
                        <el-form-item label="应用">
                            <el-select v-model.trim="infoForm.systemId" :disabled="this.id !== undefined"
                                       placeholder="请选择">
                                <el-option label="患者360" value="360"></el-option>
                                <el-option label="搜索引擎" value="SS"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="秘钥">
                            <el-input v-model.trim="infoForm.secretKey" placeholder="请填写秘钥"></el-input>
                        </el-form-item>
                        <el-form-item label="策略描述">
                            <el-input type="textarea" v-model.trim="infoForm.remarks" placeholder="请填策略描述"></el-input>
                        </el-form-item>
                        <el-form-item label="">
                            <el-button @click="submit" class="add-bg" style="float: right;">提 交</el-button>
                        </el-form-item>
                    </el-form>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component, Watch} from 'vue-property-decorator'
    import parabolaBall from '../../common/util/parabolaBall';
    import TreeRelation from '../../common/util/TreeRelation'

    let dragTreeRelation = null;

    @Component({})
    export default class editDesensitization extends Vue {
        name: string = 'editDesensitization';

        ruleVal = ''; // 搜索规则的名称
        searchVal = ''; // 树搜索
        treeData = [];
        // 角色
        roleData = [];
        /**
         *策略 区域
         */
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
                    0: '脱敏',
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


        // 规则form表单数据
        infoForm = {
            name: '',
            status: '1',
            systemId: '',
            secretKey: '',
            remarks: ''
        };

        // 编辑脱敏策略的id
        id: any = '';
        isCondition = true; // 是否展示条件关系配置区域
        /**
         * 规则点击 添加链接
         */
        isColumnRelation = false; // 是否展示操作字段
        ruleMapColumnRuleData = {}; // 存储规则名称
        columnRelationTableData = [];
        columnRelationHeaderData = [
            {
                prop: 'columnNameCn',
                label: '中文名'
            },
            {
                prop: 'columnNameEn',
                label: '英文名'
            },
            {
                prop: 'tableNameCn',
                label: '所属表中文名'
            },
            {
                prop: 'tableNameEn',
                label: '所属表英文名'
            },
        ];
        columnRelationPaginationData = {
            total: 1, // 总共数据
            pageSize: 10, // 每页显示条数
            currentPage: 1 // 当前页数
        };
        ruleMapColumn = {}; // 规则对应字段
        showMask = false;
        dragPassStyle = 'box-shadow:0 0 5px 1px #67C23A;';
        dragNoPassStyle = 'box-shadow:0 0 5px 1px #F56C6C;';

        // 缓存树高亮数据
        cacheTreeCurrentData = [];

        mounted() {
            setTimeout(() => {
                this.initSvg();
                this.getRole();
                this.getDesensitizationData();
                this.getData();
            }, 20)
        };

        /**
         * 搜索树
         */
        searchTree(queryString) {
            this.$refs.tree1['emitSearch'](queryString);
            this.$refs.tree2['emitSearch'](queryString);
            if (queryString === '') {
                this.treeData = [];
                this.searchVal = queryString;
            } else {
                let url = '/strategy/strategyInfo/modelTree';
                let params = {
                    queryString
                };
                this.$http.get(url, {params}).then(response => {
                    this.treeData = response.data.data;
                    this.searchVal = queryString;
                });
            }
        };

        /**
         * 获取脱敏规则数据
         */
        getDesensitizationData() {
            let url = '/strategy/ruleInfo/page';
            let params = {
                size: this.paginationData.pageSize,
                current: this.paginationData.currentPage,
                ruleType: 0
            };
            this.$http.get(url, {params}).then(response => {
                if (response.data.code === 0) {
                    let data = response.data.data;
                    this.tableData = data.records;
                    this.paginationData.total = data.total;
                }
            })
        };

        /**
         * 树懒加载
         */
        loadNode(node, resolve) {
            let params = {};
            switch (node.level) {
                case 0:
                    params = {
                        nodeType: 'database'
                    };
                    break;
                case 1:
                    params = {
                        databaseId: node.data.id
                    };
                    break;
                case 2:
                    params = {
                        tableId: node.data.id
                    }
            }
            let url = '/strategy/strategyInfo/tree';
            this.$http.get(url, {params}).then(response => {
                let res = response.data;
                resolve(res.data);
            });
        };

        /**
         * 获取角色
         */
        getRole() {
            let url = '/strategy/strategyInfo/rolePage';
            const params = {
                size: -1
            };
            this.$http.get(url, {params}).then(response => {
                const res = response.data;
                if (res.code === 0) {
                    this.roleData = res.data;
                    this.roleData.forEach(role => {
                        role.type = '2'; // 角色
                        role.userName = role.roleName; // 名称
                        role.userId = role.roleId; // 角色id
                        role.userId = role.userId.toString();
                        role.roleId = role.roleId.toString(); // 角色id
                    })
                }
            })
        };

        /**
         * 提交
         */
        submit() {
            let relationData = dragTreeRelation.getRelationData();
            this.alansyncTreeData(relationData);
            relationData.systemId = this.infoForm.systemId;
            relationData.secretKey = this.infoForm.secretKey;
            let isHasCoulmn = false; // 存储是否有匹配的字段
            if (relationData['nodeId'] && relationData.children && relationData.children.length) {
                relationData.children.forEach(item => {
                    let column = this.ruleMapColumn[item['nodeId']];
                    if (column && column.length) {
                        isHasCoulmn = true;
                        item.children = column
                    }
                });
                for (let i = 0; i < relationData.children.length; i++) {
                    let item = relationData.children[i];
                    let column = this.ruleMapColumn[item['nodeId']];
                    if (column && column.length) {
                        isHasCoulmn = true;
                        item.children = column
                    } else {
                        this.$message.warning(`请双击${item.name || item.ruleName || item.roleName || item.nameCn || item.nameEn}添加对应的字段`);
                        return;
                    }
                }
            } else {
                this.$message.warning('添加关系');
                return
            }
            if (!isHasCoulmn) {
                this.$message.warning('双击规则添加规则与字段之间的关系');
                return
            }
            let data = {
                ...this.infoForm,
                children: [relationData],
                id: this.id ? this.id : undefined,
                type: 1, // 1 脱敏 2 标签
            };
            let url = `/strategy/strategyInfo/${this.id ? 'update' : 'save'}`;
            this.$http({
                method: this.id ? 'put' : 'post',
                url,
                data,
            }).then(response => {
                if (response.data.code === 0) {
                    this.$message.success("保存成功");
                    this.$router.push('/desensitization');
                }
            })
        };

        /**
         * 分析关系树
         * @param relationData
         */
        alansyncTreeData(relationData) {
            delete relationData.parent;
            if (relationData.children) {
                relationData.children.forEach(child => {
                    this.alansyncTreeData(child);
                })
            }
        };

        /**
         * 初始化svg
         */
        initSvg() {
            dragTreeRelation = new TreeRelation({
                selectName: '.drag-to-relation-content',
                callbackNodeClick: this.nodeClick,
                linkRules: [{
                    fromNodeType: 'user',
                    toNodeType: 'desensitization'
                }],
                enterDirector: {
                    user: 'top',
                    desensitization: 'top',
                    column: 'left',
                }
            });
        };

        /**
         * node节点点击
         */
        async nodeClick(data) {
            if (data.nodeType === 'desensitization') {
                this.ruleMapColumnRuleData = data;
                if (!this.ruleMapColumnRuleData['nodeId']) {
                    this.ruleMapColumnRuleData['nodeId'] = this.ruleMapColumnRuleData['id']
                }
                if (!this.id) {
                    if (!this.ruleMapColumn[data['nodeId']]) {
                        this.ruleMapColumn[data['nodeId']] = [];
                    }
                } else {
                    if (!this.ruleMapColumn[data['nodeId']]) {
                        await this.getColumnData(data.id, this.id);
                    }
                }
                this.isColumnRelation = true;
                this.paginationColumnRelationTable();
            }
        };

        /**
         * 获取策略规则对应的字段
         */
        async getColumnData(ruleId, strategyId) {
            let url = '/strategy/strategyValue/page';
            let params = {
                ruleId,
                strategyId,
                size: -1,
            };
            await this.$http.get(url, {params}).then(response => {
                if (response.data.code === 0) {
                    this.ruleMapColumn[this.ruleMapColumnRuleData['nodeId']] = response.data.data.records;
                }
            })

        };

        /**
         * js手动分页
         */
        paginationColumnRelationTable() {
            // 处理tree column高亮
            {
                this.cacheTreeCurrentData = [];
                this.ruleMapColumn[this.ruleMapColumnRuleData['nodeId']].forEach(column => {
                    this.cacheTreeCurrentData.push(column.sourceId)
                });
            }
            this.columnRelationPaginationData.total = this.ruleMapColumn[this.ruleMapColumnRuleData['nodeId']].length;
            this.columnRelationTableData = this.ruleMapColumn[this.ruleMapColumnRuleData['nodeId']]['splice'](this.columnRelationPaginationData.pageSize * (this.columnRelationPaginationData.currentPage - 1), this.columnRelationPaginationData.pageSize);
            if (this.columnRelationTableData.length === 0 && this.columnRelationPaginationData.currentPage > 1) {
                this.columnRelationPaginationData.currentPage--;
                this.columnRelationTableData = this.ruleMapColumn[this.ruleMapColumnRuleData['nodeId']]['splice'](this.columnRelationPaginationData.pageSize * (this.columnRelationPaginationData.currentPage - 1), this.columnRelationPaginationData.pageSize);
            }
            this.ruleMapColumn[this.ruleMapColumnRuleData['nodeId']]['splice'](this.columnRelationPaginationData.currentPage - 1, 0, ...this.columnRelationTableData);
        };

        /**
         * 删除被选中的字段
         */
        delColumn(row) {
            this.cacheTreeCurrentData = this.cacheTreeCurrentData.filter(item => item !== row['nodeId']);
            this.ruleMapColumn[this.ruleMapColumnRuleData['nodeId']] = this.ruleMapColumn[this.ruleMapColumnRuleData['nodeId']].filter(item => {
                return item.id !== row.id;
            });
            this.paginationColumnRelationTable();
        };

        closeColumnRelationTable() {
            this.isColumnRelation = false;
        };

        /**
         * 获取已存在的关系数据
         */
        getData() {
            this.id = this.$route.query.id;
            if (!this.id) return;

            let url = `/strategy/strategyInfo/pages`;
            let params = {
                id: this.id,
                type: 1,
                pageType: 1  // 规则对应的标签不分页
            };
            this.$http.get(url, {params}).then(response => {
                if (response.data.code === 0) {
                    let data = response.data.data;
                    // this.tableData = data.records;
                    const treeData = data.records[0];
                    this.infoForm.name = treeData.name;
                    this.infoForm.status = treeData.status;
                    this.infoForm.remarks = treeData.remarks;
                    this.infoForm.systemId = treeData.children[0].systemId;
                    this.infoForm.secretKey = treeData.children[0].secretKey;
                    treeData.children[0]['nodeId'] = treeData.children[0].userId;
                    treeData.children[0].children.forEach(item => {
                        item['nodeId'] = item.ruleId;
                        item.children.forEach(column => {
                            column['nodeId'] = column.sourceId;
                        });
                        this.ruleMapColumn[item['nodeId']] = item.children;
                        delete item.children
                    });
                    dragTreeRelation.addTreeData(treeData.children[0]);
                }
            })
        };

        /**
         * 添加规则
         */
        addRule(data, event) {
            if (this.isColumnRelation) return;
            const {x: x1, y: y1} = event[0];
            const {x: x2, y: y2, width} = this.$refs.dragToRelationContent['getBoundingClientRect']();
            parabolaBall({startLoc: [x1 - 10, y1 - 10], endLoc: [x2 + width / 2, y2]});


            data.sourceId = data.ruleId;
            let nodeId = data.ruleId;
            let nodeType = 'desensitization';
            let name = data.ruleName;

            let dropData = {...data, name, nodeId, nodeType};
            setTimeout(() => {
                let addResult = dragTreeRelation.addNode(dropData);
                if (addResult) {
                    this.$message.warning(addResult)
                }
            }, 500)
        };

        /**
         * 添加角色
         */
        addRole(data, event) {
            if (this.isColumnRelation) return;
            const {x: x1, y: y1} = event[0];
            const {x: x2, y: y2, width} = this.$refs.dragToRelationContent['getBoundingClientRect']();
            parabolaBall({startLoc: [x1 - 10, y1 - 10], endLoc: [x2 + width / 2, y2]});


            data.sourceId = data.roleId;
            let nodeId = data.roleId;
            let nodeType = 'user';
            let name = data.roleName;

            let dropData = {...data, name, nodeId, nodeType};
            setTimeout(() => {
                let addResult = dragTreeRelation.addNode(dropData);
                if (addResult) {
                    this.$message.warning(addResult)
                }
            }, 500)
        };

        dropColumn(data, node, event) {
            if (!this.isColumnRelation || data.nodeType !== 'column') return;
            let tableData = node.parent.data;
            data.tableNameCn = tableData.nameCn;
            data.tableNameEn = tableData.nameEn;
            data.columnNameEn = data.nameEn;
            data.columnNameCn = data.nameCn;
            data.sourceId = data.nameEn + data.id;
            let nodeId = data.nameEn + data.id; // 字段的nodeId 根据字段名 + id 组合的
            let nodeType = 'column';
            let name = data.nameCn || data.nameEn;
            const dropData = {...data, name, nodeId, nodeType};
            const {x: x1, y: y1} = event;
            const {x: x2, y: y2, height} = this.$refs.dragToRelationContent['getBoundingClientRect']();
            parabolaBall({startLoc: [x1 - 10, y1 - 10], endLoc: [x2, y2 + height / 2]});

            setTimeout(() => {
                let table = this.ruleMapColumn[this.ruleMapColumnRuleData['nodeId']];
                if (table.length !== 0 &&
                    table.find(item => item['nodeId'] === dropData['nodeId'])) {
                    this.$message.warning(`不能重复添加“${dropData.columnNameCn || dropData.columnNameEn}”`)
                } else {
                    table.unshift(dropData);
                    this.cacheTreeCurrentData.push(data.sourceId);
                    this.columnRelationPaginationData.currentPage = 1;
                    this.paginationColumnRelationTable();
                    setTimeout(() => {
                        this.$refs.columnTable['setCurrentRow'](this.columnRelationTableData[0])
                    }, 1)
                }
            }, 500)
        };

        /**
         * 当某一行被点击时会触发该事件
         * @param row
         * @param column
         * @param event
         */
        ruleRowClick(row, column, event) {
            this.addRule(row, [event]);
        };

        /**
         * 当某一行被点击时会触发该事件
         * @param row
         * @param column
         * @param event
         */
        roleRowClick(row, column, event) {
            this.addRole(row, [event]);
        };
    }
</script>

<style lang="less">
    .edit-desensitization {
        width: 100%;
        height: 1100%;
        position: relative;

        .tree-wrapper, .rule {
            transition: all 0.5s;
        }

        .tree-wrapper {
            position: absolute;
            top: 0;
            left: 0;
            bottom: 0;
            width: 0;

            .active {
                color: #e0712f;
            }

            &.add-column {
                width: 300px;
            }
        }

        .rule {
            position: absolute;
            top: 0;
            right: 0;
            left: 0;
            bottom: 0;
            background-color: #e5e5e5;

            &.add-column {
                left: 300px;
            }

            .top {
                display: flex;
                justify-content: space-between;
                background-color: #fff;
                padding: 0 10px;
                align-items: center;
                height: 32px;

                .input-name {
                    display: flex;
                    align-items: center;
                    width: 40%;

                    span {
                        width: 67px;
                    }

                    & > div {
                        flex: 1;
                    }
                }

                .search {
                    width: 30%;
                }
            }

            .table-user {
                position: absolute;
                left: 10px;
                bottom: 50%;
                right: 10px;
                top: 42px;
                margin-bottom: 10px;

                .table {
                    position: absolute;
                    top: 0;
                    right: 310px;
                    left: 0;
                    bottom: 0;
                }

                .user {
                    position: absolute;
                    top: 0;
                    right: 0;
                    width: 300px;
                    bottom: 0;
                    background-color: #fff;
                }
            }

            .opration-wrapper {
                position: absolute;
                bottom: 10px;
                top: 50%;
                left: 10px;
                right: 10px;

                .drag-to-relation {
                    position: absolute;
                    top: 0;
                    right: 310px;
                    left: 0;
                    bottom: 0;

                    .drag-to-relation-content {
                        background-color: #fff;
                        position: absolute;
                        top: 0;
                        left: 0;
                        right: 0;
                        bottom: 0;
                    }

                    .condition {
                        position: absolute;
                        top: 0;
                        bottom: 0;
                        right: 0;
                        width: 300px;
                        display: flex;
                        flex-direction: column;
                        background-color: #fff;
                        transform: translateX(0);

                        .el-icon-circle-close {
                            position: absolute;
                            top: 30px;
                            right: 5px;
                            font-size: 20px;
                            color: #cccccc;
                            cursor: pointer;

                            &:hover {
                                color: #F56C6C;
                            }
                        }

                        .title {
                            font-size: 16px;
                            font-weight: 500;
                            text-align: center;
                            line-height: 25px;
                            border-bottom: 1px solid #cccccc;
                        }

                        .content {
                            flex: 1;
                            overflow: auto;
                        }

                        .signle {
                            height: 50px;
                            line-height: 50px;
                        }

                        &.fade-enter-active, &.fade-leave-active {
                            transition: all .5s;
                        }

                        &.fade-enter, &.fade-leave-to {
                            transform: translateX(100%);
                        }
                    }

                    .column-relation {
                        &.fade-enter-active, &.fade-leave-active {
                            transition: all .5s;
                        }

                        &.fade-enter, &.fade-leave-to {
                            transform: translateY(100%);
                        }

                        position: absolute;
                        top: 0;
                        bottom: 0;
                        left: 0;
                        right: 0;
                        background-color: #E4E7ED;
                        border-right: none;
                        display: flex;
                        flex-direction: column;

                        .title {
                            padding: 10px;
                            display: flex;
                            justify-content: space-between;

                            .el-icon-circle-close {
                                font-size: 16px;
                                cursor: pointer;

                                &:hover {
                                    color: #409EFF;
                                }
                            }

                        }

                        .table-wrapper {
                            flex: 1;
                            background-color: red;
                        }

                        .mask {
                            position: absolute;
                            top: 0;
                            right: 0;
                            bottom: 0;
                            left: 0;
                            z-index: 99999999;
                        }
                    }
                }

                .form-wrapper {
                    position: absolute;
                    top: 0;
                    right: 0;
                    width: 300px;
                    bottom: 0;
                    background-color: #fff;
                    padding: 0 10px;
                }
            }
        }

        .user {
            position: absolute;
            top: 0;
            width: 300px;
            right: 0;
            bottom: 0;
            background-color: #EBEEF5;
        }


        .ball {
            position: fixed;
            top: 0;
            left: 0;
            line-height: 35px;
            background-color: red;
            text-align: center;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            border-radius: 50%;
            transform: translate(-17.5px, -17.5px,);
            z-index: -1;
            transition: opacity 0.3s;
            width: 35px;
            height: 35px;
            opacity: 0;
            cursor: pointer;

            &.active {
                opacity: 0.8;
                z-index: 1000;
            }
        }

        .el-table tr.current-row td {
            background-color: #909399;
            color: #fff;
        }
    }
</style>
