<template>
    <div class="blood-relation cb-tree-show-operation-wrapper">
        <div class="tree-wrapper">
            <cb-tree
                    :data="treeData"
                    ref="tree">
                <template slot-scope="{data,node}">
                    <div class="tree-name" @click="handleNodeClick(data,node)">
                        <span>{{data.nameCn || data.nameEn || 'unknown'}}</span>
                    </div>
                </template>
            </cb-tree>
        </div>
        <div class="tab-wrapper cb-blood-relation-wrapper show-operation-wrapper">
            <cb-tab :tabData="tabsAllData"
                    v-model.trim="tabsActiveId">
                <template :slot="tab.name"
                          v-for="tab in tabsAllData">
                    <blood-tab :arg="tab" :activateFlag="tab.activateFlag"/>
                </template>
            </cb-tab>
        </div>
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import Qs from 'qs'
    import {Component, Watch} from 'vue-property-decorator'
    import config from '../../config'
    import {State, Mutation} from 'vuex-class'
    import BloodTab from './bloodTab.vue'
    import {isIE} from '../../common/util/IE'

    // 用于创建module时防止id重复
    let CREATE_MODULE_ID = 999.1;

    @Component({
        components: {BloodTab}
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
        defaultProps: object = {
            children: 'children',
            label: 'label'
        }
        // form表单其他数据
        otherData: any = null
        // tab标签
        tabsActiveId: string = ''
        //  tab内所有的数据
        tabsAllData: Array<object> = []
        // tree搜索值
        searchModelValue: string = ''


        // 监听删选框数据变化
        @Watch('searchModelValue')
        onChangeSearchModelValue(val: string, oldVal: string) {
            this.myThis.$refs.tree.filter(val);
        }

        mounted() {

            this.getTreeData();
            if (isIE()) {
                this.$message.warning('您使用的是IE浏览器，为了更好的访问体验，请使用谷歌、火狐浏览器')
            }
        }

        // 获取tree树模型数据集合
        getTreeData(): void {
            let url = config.port('MetadataManage') + 'metadatavalue/tree/modelTree/889553a9-fea1-46be-90db-77888f521c47';
            this.$http.get(url).then(response => {
                const res = response.data;
                if (res.code === 0) {
                    this.treeData = res.data;
                }
            })
        }


        // tree节点被点击时的回调
        async handleNodeClick(data, node) {
            if (node.level % 2 !== 0) return;
            // 限制20个tabs
            if (this.tabsAllData.length >= 20) {
                // 查看tab是否已经存在tabsAllData中
                const repetition = this.tabsAllData.find(item => item['uuid'] === data.uuid);
                if (repetition !== undefined) {
                    this.tabsActiveId = data.uuid
                } else {
                    this.$message.warning('tabs标签不能大于20个')
                }
                return
            } else {
                if (data.tree_type !== 'module' || node.level === 6) {
                    this.tabsActiveId = data.uuid
                }
            }
            // 禁止出现第二个相同的tabs
            for (let i = 0; i < this.tabsAllData.length; i++) {
                this.tabsActiveId = data.uuid;
                if (this.tabsAllData[i]['uuid'] === data.uuid) return
            }
            this.tabsAllData.map(item => item['activateFlag'] = false);
            // 给新加入到tab中的成员添加一个标记active  代表被激活 ，因为bloodTab组件需要用这个来刷新svg(解决非激活的tab svg被刷新的问题)
            data.activateFlag = true;
            // label tab展示的名称 name 是tab的标识
            data['label'] = data.nameCn || data.nameEn || 'unknown';
            data['name'] = data.uuid;
            this.tabsAllData.push(data);
            this.tabsActiveId = data.uuid;
        }

        // tree节点过滤
        treeFilterNode(value, data) {
            if (!value) return true;
            return data.name.indexOf(value) !== -1;
        }

        // tab 被选中时触发
        handleTabsClick(tab, event) {
            this.tabsAllData.map(item => {
                if (item['uuid'] === tab.name) {
                    item['activateFlag'] = true;
                } else {
                    item['activateFlag'] = false;
                }
            })
        }

        /*
        * ===============================================================================
        * */

        // tab 移除标签
        removeTab(targetId) {
            let tabs = this.tabsAllData;
            let activeName = this.tabsActiveId;
            if (activeName === targetId) {
                tabs.forEach((tab, index) => {
                    if (tab['uuid'] === targetId) {
                        let nextTab = tabs[index + 1] || tabs[index - 1];
                        if (nextTab) {
                            activeName = nextTab['uuid'];
                        }
                    }
                });
            }
            this.tabsActiveId = activeName;
            this.tabsAllData = tabs.filter(tab => tab['uuid'] !== targetId);
        }


    }
</script>

<style scoped lang="less">

    .blood-relation {
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
    }
</style>
