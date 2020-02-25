<template>
    <div class="business-view-relation cb-tree-show-operation-wrapper">
        <div class="tree-wrapper">
            <cb-tree lazy
                     @load-node="loadNode"
                     nodeKey="id"
                     ref="tree">
                <template slot-scope="{data,node}">
                    <div class="tree-name" @click="handleNodeClick(data,node)">
                        <span>{{data.nameCn || data.nameEn || 'unknown'}}</span>
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
                        <business-tab :arg="tab"/>
                    </div>
                </template>
            </cb-tab>
        </div>
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component, Watch} from 'vue-property-decorator'
    import config from '../../config'
    import {State, Mutation} from 'vuex-class'
    import BusinessTab from './tab.vue'
    import {isIE} from '../../common/util/IE'

    @Component({
        components: {BusinessTab}
    })
    export default class BloodRelation extends Vue {
        myThis: any = this;
        @State pageSizes;
        @State pageSize;
        @State loadingFlag;
        @Mutation setLoadingFlag;
        name: string = 'MetaImport';
        // 元模型tree数据
        treeData: Array<object> = [];
        defaultProps: object = {
            children: 'children',
            label: 'label'
        };
        // form表单其他数据
        otherData: any = null;
        // tab标签
        tabsActiveId: string = '';
        //  tab内所有的数据
        tabsAllData: Array<object> = [
            // {
            //     label: '1111',
            //     name: '1'
            // }
        ];
        // tree搜索值;
        searchModelValue: string = '';

        // 监听删选框数据变化
        @Watch('searchModelValue')
        onChangeSearchModelValue(val: string, oldVal: string) {
            // this.myThis.$refs.tree.filter(val);
        }


        mounted() {
            // this.getTreeData();
        }

        // 获取tree树模型数据集合
        loadNode(node, resolve): void {
            let url = config.port('businessView') + '/getCategoryLazzyTree';
            if (node.data && node.data.nodeType === 'element_item') {
                resolve([])
            } else {
                let params = null;
                if (node.level === 0) {
                    params = {
                        nodeType: 'category'
                    };
                } else {
                    params = {
                        nodeType: node.data.nodeType,
                        id: node.data.id
                    };
                }
                this.$http.get(url, {params}).then(response => {
                    const res = response.data;
                    if (res.code === 0) {
                        resolve(res.data);
                    }
                });
            }
        }


        // tree节点被点击时的回调
        async handleNodeClick(data, node) {
            if (data.nodeType !== 'element_item') {
                return
            }
            // 限制20个tabs
            let dataId = data.id.toString();
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

    }
</script>

<style lang="less">

    .business-view-relation {
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
            position: relative;
        }

        .el-dialog__body {
            display: flex;
            flex-direction: column;
            align-items: center;

            .el-form {
                width: 576px;
            }

            .search {
                width: 500px;
                margin-bottom: 10px;

                .el-input {
                    width: 200px;
                }
            }


            .el-steps {
                display: flex;
                justify-content: center;
                align-items: center;
                width: 500px;
            }

            .el-transfer {
                display: inline-block;
            }

            .el-transfer-panel__body {
                height: 300px;
            }

            .el-transfer-panel__list {
                height: 100%;
            }
        }

        .elementInfo {
            width: 576px;
            display: flex;
            flex-direction: column;
            overflow: auto;

            .title {
                display: flex;
                align-items: center;
                line-height: 30px;
                background-color: #fff;
                font-size: 14px;
                font-weight: 700;
                padding-left: 10px;
                border-bottom: 1px dashed #ccc;
            }

            .info {
                display: flex;
                font-size: 14px;
                flex-wrap: wrap;
                padding: 10px;
                background-color: #fff;

                span {
                    overflow: hidden; //超出的文本隐藏
                    text-overflow: ellipsis; //溢出用省略号显示
                    white-space: nowrap; //溢出不换行
                    width: 90px;
                    font-size: 12px;
                    margin-bottom: 5px;
                }

                .label {
                    display: inline-block;
                    text-align: right;
                }

                .value {
                    display: inline-block;
                }
            }

            .table-wrapper {
                margin-top: 10px;
                height: 200px;
                background-color: red;
            }
        }
    }
</style>
