<template>
    <div class="edit-label">
        <div class="tree-wrapper">
            <cb-tree lazy
                     searchFromHttp
                     @search="searchTree"
                     v-show="searchVal === ''"
                     @load-node="loadNode"
                     :searchHidden="false"
                     node-key="id"
                     @node-click="dropSign"
                     ref="tree1">
                <template slot-scope="{data,node}">
                    <span style="cursor: pointer;"
                          :class="cacheTreeCurrentData.find(item=>item === (data.tlabelName + data.id)) !== undefined?'active':''">
                        {{data.label || data.tlabelClassName || data.tlabelName || 'unknow'}}
                    </span>
                </template>
            </cb-tree>
            <!--<cb-tree @search="searchTree"
                     searchFromHttp
                     v-show="searchVal !== ''"
                     :data="treeData"
                     node-key="id"
                     ref="tree2">
                <template slot-scope="{data,node}">
                    <span style="cursor: pointer;"
                          @click="dropSign(data,node,arguments)">
                        {{data.label || data.tlabelClassName || data.tlabelName || 'unknow'}}
                    </span>
                </template>
            </cb-tree>-->
        </div>
        <div class="rule">
            <div class="top">
                <el-breadcrumb separator-class="el-icon-arrow-right">
                    <el-breadcrumb-item :to="{ path: '/label' }">组合策略</el-breadcrumb-item>
                    <el-breadcrumb-item>组合策略编辑</el-breadcrumb-item>
                </el-breadcrumb>
                <!--<div class="search">
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
                              @size-change="getDesensitizationData"
                              @row-click="rowClick"
                              @current-change="getDesensitizationData">
                        <template slot="column">
                            <el-table-column
                                    label="操作"
                                    width="100">
                                <template slot-scope="{row}">
                                    <i class="iconfont icon-yuanquan"
                                       v-if="row.status === 1"
                                       style="cursor: pointer;" @click.stop.prevent="addLabel(row,arguments)"></i>
                                </template>
                            </el-table-column>
                        </template>
                    </cb-table>
                </div>
            </div>
            <div class="opration-wrapper">
                <div class="drag-to-relation">
                    <div class="drag-to-relation-content" ref="dragToRelationContent"></div>
                </div>
                <div class="form-wrapper">
                    <el-form ref="infoForm" :model="infoForm" label-width="80px" class="info-form">
                        <el-form-item label="策略名称">
                            <el-input v-model.trim="infoForm.name" placeholder="策略名称"></el-input>
                        </el-form-item>
                        <el-form-item label="策略状态">
                            <el-select v-model.trim="infoForm.status" placeholder="请选择">
                                <el-option label="启用" value="1"></el-option>
                                <el-option label="禁用" value="2"></el-option>
                                <!--<el-option label="其他" value="2"></el-option>-->
                            </el-select>
                        </el-form-item>
                        <el-form-item label="策略描述">
                            <el-input type="textarea" v-model.trim="infoForm.remarks" placeholder="策略描述"></el-input>
                        </el-form-item>
                        <el-form-item label="">
                            <el-button @click="submit" style="float: right;">提 交</el-button>
                        </el-form-item>
                    </el-form>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component} from 'vue-property-decorator'
    import parabolaBall from '../../common/util/parabolaBall';
    import TreeRelation from '../../common/util/TreeRelation'

    let dragTreeRelation = null;

    @Component({})
    export default class Index extends Vue {
        name: string = 'editLabel';

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
            pageSize: 1, // 每页显示条数
            currentPage: 1 // 当前页数
        };


        // 规则form表单数据
        infoForm = {
            name: '',
            status: '1',
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
                ...this.paginationData,
                ruleType: 1
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
            let url = '/strategy/labelPool/getLabel';
            let nodeData = node.data;
            let params = {
                tlabelParentID: node.level !== 0 ? nodeData.id : 0,
                size: -1
            };

            this.$http.get(url, {params}).then(response => {
                let res = response.data;
                resolve(res.data);
            });
        };

        /**
         * 提交
         */
        submit() {
            let relationData = dragTreeRelation.getRelationData();
            this.alansyncTreeData(relationData);
            if (Object.keys(relationData).length <= 3) {
                this.$message.warning('请添加“标签规则与标签的关系”');
                return;
            }
            if (this.infoForm.name === '' || this.infoForm.status === '') {
                this.$message.warning('请完成“策略信息”');
                return;
            }
            let data = {
                ...this.infoForm,
                type: 2, // 1 脱敏 2 标签
                children: [relationData],
                id: this.id ? this.id : undefined,
            };
            let url = `/strategy/strategyInfo/${this.id ? 'updateLabel' : 'saveLabel'}`;
            this.$http({
                method: this.id ? 'put' : 'post',
                url,
                data,
            }).then(response => {
                if (response.data.code === 0) {
                    this.$message.success("保存成功");
                    this.$router.push('/label');
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
                linkRules: [{
                    fromNodeType: 'label',
                    toNodeType: 'sign'
                }],
                enterDirector: {
                    label: 'top',
                    sign: 'left',
                },
                del: this.delSign
            });
        };

        /**
         * 删除sign 回调
         */
        delSign(node) {
            if (node.nodeType === 'sign') {
                this.cacheTreeCurrentData = this.cacheTreeCurrentData.filter(item => item !== node.sourceId);
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
            this.columnRelationPaginationData.total = this.ruleMapColumn[this.ruleMapColumnRuleData['nodeId']].length;
            this.columnRelationTableData = this.ruleMapColumn[this.ruleMapColumnRuleData['nodeId']]['splice'](this.columnRelationPaginationData.currentPage - 1, this.columnRelationPaginationData.pageSize);
            this.ruleMapColumn[this.ruleMapColumnRuleData['nodeId']]['splice'](this.columnRelationPaginationData.currentPage - 1, 0, ...this.columnRelationTableData);
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
                type: 2,
                pageType: 1  // 规则对应的标签不分页
            };
            this.$http.get(url, {params}).then(response => {
                if (response.data.code === 0) {
                    let data = response.data.data;
                    const treeData = data.records[0];
                    this.infoForm.name = treeData.name;
                    this.infoForm.status = treeData.status;
                    this.infoForm.remarks = treeData.remarks;
                    this.cacheTreeCurrentData = [];

                    treeData.children[0]['nodeId'] = treeData.children[0].ruleId;
                    treeData.children[0].nodeType = 'label';
                    treeData.children[0].children.forEach(item => {
                        item['nodeId'] = item.sourceId;
                        item.nodeType = 'sign';
                        this.cacheTreeCurrentData.push(item.sourceId);
                        delete item.children;
                    });
                    dragTreeRelation.addTreeData(treeData.children[0]);
                }
            })
        };

        /**
         * 添加规则
         */
        addLabel(data, event) {
            const {x: x1, y: y1} = event[0];
            const {x: x2, y: y2, width} = this.$refs.dragToRelationContent['getBoundingClientRect']();
            parabolaBall({startLoc: [x1 - 10, y1 - 10], endLoc: [x2 + width / 2, y2]});


            data.sourceId = data.ruleId;
            let nodeId = data.ruleId;
            let nodeType = 'label';
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
         * 标签
         * @param data
         * @param node
         * @param event
         */
        dropSign(data, node, event) {
            if (data.tlabelorclass === 1) {
                data.sourceId = data.id;
                data.columnNameEn = data.tlabelName;
                data.columnNameCn = data.tlabelName;
                data.tableNameCn = data.tlabelName;
                data.tableNameEn = data.tlabelName;
                data.sourceId = data.tlabelName + data.id;
                let nodeId = data.tlabelName + data.id;
                let nodeType = 'sign';
                let name = data.label || data.tlabelClassName || data.tlabelName;
                const dropData = {...data, name, nodeId, nodeType};
                const {x: x1, y: y1} = event;
                const {x: x2, y: y2, height} = this.$refs.dragToRelationContent['getBoundingClientRect']();
                parabolaBall({startLoc: [x1 - 10, y1 - 10], endLoc: [x2, y2 + height / 2]});
                setTimeout(() => {
                    let addResult = dragTreeRelation.addNode(dropData);
                    if (addResult) {
                        this.$message.warning(addResult)
                    } else {
                        this.cacheTreeCurrentData.push(data.sourceId);
                    }
                }, 500)
            }
        };


        /**
         * 当某一行被点击时会触发该事件
         * @param row
         * @param column
         * @param event
         */
        rowClick(row, column, event) {
            this.addLabel(row, [event]);
        };
    }
</script>

<style scoped lang="less">
    .edit-label {
        width: 100%;
        height: 1100%;
        position: relative;

        .tree-wrapper {
            position: absolute;
            top: 0;
            left: 0;
            bottom: 0;
            width: 300px;

            .active {
                color: #e0712f;
            }
        }

        .rule {
            position: absolute;
            top: 0;
            left: 300px;
            right: 0;
            bottom: 0;
            background-color: #e5e5e5;

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
                    right: 0px;
                    left: 0;
                    bottom: 0;
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
    }
</style>
