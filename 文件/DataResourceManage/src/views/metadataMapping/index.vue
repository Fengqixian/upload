<template>
    <div class="metadata-mapping">
        <div class="tree-wrapper">
            <cb-tree lazy
                     @load-node="loadNode"
                     @node-click="nodeClick">
                <template slot-scope="{data,node}">
                    {{data.nameCn || data.nameEn || 'unknown'}}
                </template>
                <template slot="header">
                    <div class="select-target">
                        <el-radio-group v-model.trim="target" text-color="blue" fill="red">
                            <el-radio label="resource">来 源</el-radio>
                            <el-radio label="target">目 标</el-radio>
                        </el-radio-group>
                    </div>
                </template>
            </cb-tree>
        </div>
        <div class="mapping-wrapper">
            <!-- 未匹配 -->
            <div class="no-mapping">
                <!-- 来源区域 -->
                <div class="resource-wrapper">
                    <div class="title-wrapper">来 源{{resourceCurrentTreeNode.data?`：${resourceCurrentTreeNode.data.nameCn
                        || resourceCurrentTreeNode.data.nameEn || 'unknown'}`: null}}
                    </div>
                    <div class="table-wrapper">
                        <cb-table highlight-current-row
                                  :header-data="resourceHeaderData"
                                  :table-data="resourceTableData"
                                  :is-pagination="false"
                                  :is-operation="false"
                                  :pagination-data="resourcePaginationData"
                                  ref="resourceTable">
                        </cb-table>
                    </div>
                </div>
                <!-- 目标区域 -->
                <div class="target-wrapper">
                    <div class="title-wrapper">目 标{{targetCurrentTreeNode.data?`：${targetCurrentTreeNode.data.nameCn
                        || targetCurrentTreeNode.data.nameEn || 'unknown'}`: null}}
                    </div>
                    <div class="table-wrapper">
                        <cb-table highlight-current-row
                                  :header-data="targetHeaderData"
                                  :table-data="targetTableData"
                                  :is-pagination="false"
                                  :is-operation="false"
                                  :pagination-data="targetPaginationData"
                                  ref="targetTable">
                        </cb-table>
                    </div>
                </div>
            </div>
            <!-- 已匹配 -->
            <div class="yes-mapping">
                <cb-table selection
                          :header-data="matchHeaderData"
                          :table-data="matchTableData"
                          :is-pagination="false"
                          :is-operation="true"
                          :pagination-data="matchPaginationData"
                          :row-class-name="matchRowClassName"
                          ref="matchTable">
                    <template slot="operation">
                        <!--<el-button>取 消</el-button>-->
                        <el-button @click="delMatch">移 除</el-button>
                        <!--<el-button>自动匹配</el-button>-->
                        <el-button @click="handleMatch">手动匹配</el-button>
                        <el-button @click="saveMatch" type="primary">保 存</el-button>
                    </template>
                </cb-table>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component, Watch} from 'vue-property-decorator'
    import config from "../../config";

    @Component({})
    export default class MetaDataMapping extends Vue {
        name: string = 'MetaDataMapping';
        /**
         * 共同头部信息
         */
        public baseTableHeaderData: Array<object> = [
            {
                prop: 'nameCn',
                label: '中文名称'
            },
            {
                prop: 'nameEn',
                label: '英文名称'
            },
            {
                prop: 'status',
                label: '状态'
            },
            {
                prop: 'createTime',
                label: '创建时间'
            },
            {
                prop: 'updateTime',
                label: '更新时间'
            }
        ];
        /**
         * tree 成员变量
         */
        public target: string = 'resource'; // 映射变量  默认选择来源  resource：来源   target：目标

        /**
         * 来源成员变量
         */
        public resourceCurrentTreeNode: object = {}; // 当前被点击的tree节点信息
        public resourceHeaderData: Array<object> = []; // 头部信息
        public resourceTableData: Array<object> = []; // 体数据
        public resourcePaginationData: object = {currentPage: 1, pageSize: 10, total: 1}; // 体数据

        /**
         * 目标成员变量
         */
        public targetCurrentTreeNode: object = {}; // 当前被点击的tree节点信息
        public targetHeaderData: Array<object> = []; // 头部信息
        public targetTableData: Array<object> = []; // 体数据
        public targetPaginationData: object = {currentPage: 1, pageSize: 10, total: 1}; // 体数据

        /**
         * 已匹配
         */
        public matchHeaderData: Array<object> = [
            {
                label: '来源字段名称',
                prop: 'sourceNameEn'
            },
            {
                label: '来源字段中文',
                prop: 'sourceNameCn'
            },
            {
                label: '来源表',
                prop: 'sourceParentCn'
            },
            {
                label: '目标字段名称',
                prop: 'targetNameEn'
            },
            {
                label: '目标字段中文',
                prop: 'targetNameCn'
            },
            {
                label: '目标表',
                prop: 'targetParentCn'
            },
        ]; // 头部信息
        public matchTableData: Array<object> = []; // 体数据
        public matchPaginationData: object = {currentPage: 1, pageSize: 10, total: 1}; // 体数据
        public handleMatchTableData: Array<object> = []; // 未匹配的数据
        public matchedTableData: Array<object> = []; // 已经匹配上的数据

        /**
         * 检测未匹配数据发生变化，重新将handleMatchTableData和matchedTableData组装到matchTableData中
         */
        @Watch('handleMatchTableData')
        public handleMatchTableDataChange(newVal, oldVal) {
            // 将选中的数据push到匹配区域的table中
            this.matchTableData.splice(0, this.matchTableData.length, ...this.handleMatchTableData, ...this.matchedTableData);
        }

        /**
         * 检测已经匹配上的数据发生变化，重新将handleMatchTableData和matchedTableData组装到matchTableData中
         */
        @Watch('matchedTableData')
        public async matchedTableDataChange(newVal): Promise<void> {
            /**
             * 已经匹配成功的table数据发生变化时候，重新获取来源table数据 和目标 table数据
             */
            // 获取来源table数据
            {
                const {tableData} = await this.getResourceAndTargetTableData(this.resourceCurrentTreeNode);
                this.resourceTableData.splice(0, this.resourceTableData.length, ...tableData);
            }
            // 获取目标table数据
            {
                const {tableData} = await this.getResourceAndTargetTableData(this.targetCurrentTreeNode);
                this.targetTableData.splice(0, this.targetTableData.length, ...tableData);
            }
            // 将选中的数据push到匹配区域的table中
            this.matchTableData.splice(0, this.matchTableData.length, ...this.handleMatchTableData, ...this.matchedTableData);

            // 过滤去除 在已经匹配的table中的来源table 和目标 table
            this.matchTableData.forEach(item => {
                this.filterResourceAndTargetTableData(item);
            })
        }


        /**
         * 删除已经匹配的数据
         */
        public delMatch(): void {
            const matchTable = this.$refs.matchTable;
            // 获取勾选删除的数据
            const delMatchData: Array<object> = matchTable['getSelectData']();
            if (delMatchData.length === 0) {
                this.$message.warning('请选择删除的数据！');
                return;
            }

            // 把即将删除的数据分离为 数据库中已经匹配的 和手动匹配的
            // 手动匹配的
            const handleMatchData: Array<object> = delMatchData.filter(item => item['handleMatchFlag']);
            // 数据库存在的
            const matchedData: Array<object> = delMatchData.filter(item => !item['handleMatchFlag']);
            // 将手动匹配的数据分离出来 来源数据 和 目标数据 并unshift对应的table中
            handleMatchData.forEach(item => {
                this.targetTableData.unshift(item['targetData']);
                this.resourceTableData.unshift(item['resourceData']);
            });

            // 将手动匹配的数据移除
            handleMatchData.forEach(matchData => {
                this.handleMatchTableData.splice(0,
                    this.handleMatchTableData.length,
                    ...this.handleMatchTableData.filter(item =>
                        item['sourceNameEn'] !== matchData['sourceNameEn']
                        && item['targetNameEn'] !== matchData['targetNameEn']));
            });

            // 删除的没有数据库中的匹配数据，不需要执行删除http
            if (matchedData.length === 0) return;
            let url = config.port('metadataReference') + '/removedReferences';
            this.$http.put(url, matchedData).then(response => {
                if (response.data.code === 0) {
                    this.$message.success('删除成功！');
                    this.matchingData();
                    // 将已经匹配的数据会写到上面
                } else {
                    this.$message.error('删除失败！');
                }
            })
        }

        /**
         * 匹配保存
         */
        public saveMatch(): void {
            let url = config.port('metadataReference') + '/saveBatch';
            if (this.handleMatchTableData.length === 0) return;
            this.handleMatchTableData.map(item => {
                delete item['targetData'];
                delete item['resourceData'];
            });
            this.$http.put(url, this.handleMatchTableData).then(response => {
                this.handleMatchTableData.splice(0, this.handleMatchTableData.length);
                if (response.data.code === 0) {
                    this.$message.success('匹配成功！');
                    this.matchingData();
                } else {
                    this.$message.error('匹配失败！');
                }
            })
        }

        /**
         * 手动匹配
         */
        public handleMatch(): void {
            // 来源table
            const resourceTable = this.$refs.resourceTable;
            // 目标table
            const targetTable = this.$refs.targetTable;
            // 获取被选择的数据
            const resourceSelectedTableData: Array<object> = resourceTable['getSelectData']();
            const targetSelectedTableData: Array<object> = targetTable['getSelectData']();
            // 判断来源 目标都有被选择的数据 则才能手动匹配
            if (!resourceSelectedTableData.length || !targetSelectedTableData.length) {
                this.$message.warning('请选择来源和目标！');
                return;
            }

            // 选中的来源数据和目标数据
            let resourceData: object = resourceSelectedTableData[0];
            let targetData: object = targetSelectedTableData[0];
            // 选中来源tree数据  目标tree数据
            const resourceCurrentTreeData: object = this.resourceCurrentTreeNode['data'];
            const targetCurrentTreeData: object = this.targetCurrentTreeNode['data'];

            /*
                将被选择的数据组装成这样的数据
                String sourceNameEn;
                String sourceNameCn;
                String sourceParentEn;
                String sourceParentCn;
                String targetNameEn;
                String targetNameCn;
                String targetParentEn;
                String targetParentCn;
             */

            // 将选中的数据组装成匹配table需要的数据格式
            // 来源组装
            resourceData['sourceNameEn'] = resourceData['nameEn'];
            resourceData['sourceNameCn'] = resourceData['nameCn'];
            resourceData['sourceId'] = resourceData['resourceId'];
            resourceData['sourceParentEn'] = resourceCurrentTreeData['nameEn'];
            resourceData['sourceParentCn'] = resourceCurrentTreeData['nameCn'];
            resourceData['sourceParentId'] = resourceCurrentTreeData['resourceId'];
            resourceData['resourceData'] = resourceData; // 手动匹配时，将来源数据单独存储起来 方便后面执行删除是 容易寻找到来源数据回写到来源table中

            // 目标组装
            targetData['targetNameEn'] = targetData['nameEn'];
            targetData['targetNameCn'] = targetData['nameCn'];
            targetData['targetId'] = targetData['resourceId'];
            targetData['targetParentEn'] = targetCurrentTreeData['nameEn'];
            targetData['targetParentCn'] = targetCurrentTreeData['nameCn'];
            targetData['targetParentId'] = targetCurrentTreeData['resourceId'];
            targetData['targetData'] = targetData; // 手动匹配时，将目标数据单独存储起来 方便后面执行删除是 容易寻找到目标数据回写到目标table中

            // 标记是手动匹配的数据
            const handleMatchFlag = {handleMatchFlag: true};
            this.handleMatchTableData.push({...resourceData, ...targetData, ...handleMatchFlag});
            // 将已经手动匹配的数据从来源table和目标table中移除
            this.resourceTableData.splice(0, this.resourceTableData.length, ...this.resourceTableData.filter(item => {
                return item['nameEn'] !== resourceData['nameEn'];
            }));
            this.targetTableData.splice(0, this.targetTableData.length, ...this.targetTableData.filter(item => {
                return item['nameEn'] !== targetData['nameEn'];
            }));
        }

        /**
         * 获取已经匹配的数据
         * @param paginationData 分页查询数据
         */
        public matchingData(): void {
            let url = config.port('metadataReference') + '/getReferenceList';
            let params = {
                sourceParentId: this.resourceCurrentTreeNode['data']['resourceId'],
                targetParentId: this.targetCurrentTreeNode['data']['resourceId']
            };
            this.$http.get(url, {params}).then(response => {
                this.matchedTableData.splice(0, this.matchedTableData.length, ...response.data.data);
            })
        }

        /**
         * @param matchData 已经匹配table中column数据
         */
        public filterResourceAndTargetTableData(matchData): void {
            // 过滤来源table
            this.resourceTableData.splice(0,
                this.resourceTableData.length,
                ...this.resourceTableData.filter(item => item['nameEn'] !== matchData['sourceNameEn']));
            // 过滤目标table
            this.targetTableData.splice(0,
                this.targetTableData.length,
                ...this.targetTableData.filter(item => item['nameEn'] !== matchData['targetNameEn']));
        }


        /**
         * 节点被点击时的回调
         * @param data  新数据
         * @param node 需要刷新的node节点
         * */
        public async nodeClick(data, node): Promise<void> {

            // if ((this.resourceCurrentTreeNode['data'] && this.resourceCurrentTreeNode['data'].uuid === node['data'].uuid) || (this.targetCurrentTreeNode['data'] && this.targetCurrentTreeNode['data'].uuid === node['data'].uuid)) {
            //     return;
            // }
            // if(this.resourceCurrentTreeNode['data'] && this.resourceCurrentTreeNode['data'].uuid === node['data'].uuid) return;
            //
            // if(this.targetCurrentTreeNode['data'] && this.targetCurrentTreeNode['data'].uuid === node['data'].uuid)return;

            // radio选中的是来源
            if (this.target === 'resource') {
                if (this.targetCurrentTreeNode['data'] && this.targetCurrentTreeNode['data'].uuid === node['data'].uuid) {
                    this.$message.warning('来源和目标不能相同！');
                    return
                }
                if (this.resourceCurrentTreeNode['data'] && this.resourceCurrentTreeNode['data'].uuid === node['data'].uuid) return;
                this.resourceCurrentTreeNode = node;
            }

            // 选中的是目标
            else {
                if (this.resourceCurrentTreeNode['data'] && this.resourceCurrentTreeNode['data'].uuid === node['data'].uuid) {
                    this.$message.warning('来源和目标不能相同！');
                    return
                }
                if (this.targetCurrentTreeNode['data'] && this.targetCurrentTreeNode['data'].uuid === node['data'].uuid) return;
                this.targetCurrentTreeNode = node;
            }
            // 判断来源和目标是否相同
            if (this.resourceCurrentTreeNode['data'] && this.targetCurrentTreeNode['data']) {
                if (this.resourceCurrentTreeNode['data'].uuid === this.targetCurrentTreeNode['data'].uuid) {
                    this.$message.warning('来源和目标不能相同！');
                    return;
                }
            }


            // 存放被获取的table  获取下一节点的数据
            const {childData, tableData} = await this.getResourceAndTargetTableData(node);
            let header = await this.getTableHeaderData(childData);
            header.map(item => {
                // 头部展示数据的字段
                item['prop'] = item['mappingField'];
                item['label'] = item['nameCn']
            });
            const headerData = [...JSON.parse(JSON.stringify(this.baseTableHeaderData)), ...header];
            if (this.target === 'resource') {
                this.resourceHeaderData = headerData;
                this.resourceTableData.splice(0, this.resourceTableData.length, ...tableData);
            } else {
                this.targetHeaderData = headerData;
                this.targetTableData.splice(0, this.targetTableData.length, ...tableData);
            }

            // 判断来源和目标都有数据时候，请求已经匹配上的数据
            if (this.resourceTableData.length && this.targetTableData.length) {
                this.matchingData();
            }
        }

        /**
         * 获取当前被匹配的元数据
         * @param node 当前被点击的node节点数据
         * @return object类型 childData：下一节点的数据  tableData：table数据
         */
        public async getResourceAndTargetTableData(node): Promise<any> {
            /**
             * 获取下一节点的数据
             * 判断当前节点有木有子节点数据
             */
            let childData: object = {};
            if (node.childNodes.length) { // 有子节点
                childData = node.childNodes[0].data;
            } else {
                // 调用懒加载接口回去子级数据
                childData = (await this.getTreeChildData(node['data']))[0];
            }
            // 获取table数据并将数据存放到对应的table中
            let tableData: Array<object> = [];
            if (node.level === 1) {
                // 点击是第一级模型时候，匹配底下的元数据，将被匹配的数据传递过去
                tableData = await this.getTableData(node['data'], node['data']);
            } else {
                tableData = await this.getTableData(node['data'], childData);
            }

            // 将下一节点的数据return出去
            return {childData, tableData};
        }

        /**
         * 获取table头部信息
         * @param data 获取元数据属性的模型
         * return table 头部信息
         */
        public async getTableHeaderData(data): Promise<Array<object>> {
            let URL = config.port('metadataproperties') + '/page';
            const params = {
                modelResourceId: data['modelId'] // 根据模型查询元数据的属性 传resourceId
            };
            let headerData: Array<object> = [];
            await this.$http.get(URL, {params}).then((response) => {
                headerData = response.data.data.records;
            });
            return headerData;
        }

        /**
         * 获取table体数据
         * @param modelInfo 模型数据
         * @param insInfo 元数据数据
         * @param paginationData 分页
         * return table 体数据
         */
        public async getTableData(insInfo, modelInfo) {
            let url = config.port('metadatavalue') + '/page';
            let params = {
                // insInfo['nameEn'] === modelInfo['nameEn']代表给顶级元数据匹配，parentId被匹配元数据本身的parentId  否则床元数据的resourceId
                parentId: insInfo['uuid'] === modelInfo['uuid'] ? undefined : insInfo['resourceId'],
                modelId: modelInfo['resourceId'],
                isStandard: modelInfo['isStandard'],
                status: modelInfo['isStandard'] === true ? 1 : undefined
            };
            let tableData: Array<object> = [];
            await this.$http.get(url, {params}).then(response => {
                const res = response.data.data;
                tableData = res.records;
            });
            return tableData;
        }

        /**
         * 获取当前下一节点数据
         * @param data 当前节点数据
         */
        public async getTreeChildData(data): Promise<Array<object>> {
            let url = config.port('metadatavalue') + '/tree/lazyTree';
            let params: object = {
                parentUuid: data.uuid
            };
            let ret: Array<object> = [];
            await this.$http.get(url, {params}).then(response => {
                ret = response.data.data;
            });
            return ret
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
            this.$http.get(url, {params}).then(response => {
                let res = response.data;
                resolve(res.data);
            });
        }

        public matchRowClassName({row, rowIndex}): string {
            if (row['handleMatchFlag']) {
                return 'match-column'
            } else {
                return '';
            }
        }
    }
</script>

<style lang="less">
    .metadata-mapping {
        display: flex;

        // tree
        .tree-wrapper {
            width: 300px;
            height: 100%;

            .select-target {
                padding: 5px 10px;
                display: flex;

                .el-radio {
                    color: #ffffff;
                }
            }
        }

        // 匹配区域
        .mapping-wrapper {
            flex: 1;
            position: relative;

            .no-mapping {
                right: 0;
                left: 0;
                bottom: 50%;
                box-shadow: 0 2px 5px #ccc;
                position: absolute;
                top: 0;

                & > .resource-wrapper, & > .target-wrapper {
                    position: absolute;
                    top: 10px;
                    bottom: 10px;
                    box-shadow: 0 0 5px #cccccc;

                    .title-wrapper {
                        width: 100%;
                        height: 40px;
                        font-size: 18px;
                        font-weight: 600;
                        line-height: 40px;
                        padding-left: 10px;
                        border-bottom: 1px solid #cccccc;
                    }

                    .table-wrapper {
                        position: absolute;
                        top: 40px;
                        left: 0;
                        right: 0;
                        bottom: 0;
                    }
                }

                .resource-wrapper {
                    left: 10px;
                    right: 51%;
                }

                .target-wrapper {
                    right: 10px;
                    left: 51%;
                }
            }

            .yes-mapping {
                position: absolute;
                bottom: 0;
                top: 50%;
                right: 0;
                left: 0;
                padding-top: 10px;

                .match-column {
                    background-color: #f0f9eb;
                }
            }
        }
    }
</style>
