<template>
    <div class="cb-tab" :class="{'cb-closes':closes}" ref="cbTab">
        <el-tabs v-model.trim="elTabActiveName"
                 :type="type"
                 :closable="closable"
                 @tab-click="tabClick"
                 @tab-remove="tabRemove">
            <el-tab-pane v-for="item in tabData"
                         :key="item.label + item.name"
                         :label="item.label"
                         :name="item.name">
                <slot :name="item.name" :data="item" :tabData="tabData"></slot>
            </el-tab-pane>
        </el-tabs>
        <div class="cb-tab-operation"
             v-if="closes">
            <el-dropdown>
                <div class="el-dropdown-link">
                    <i class="el-icon-arrow-down el-icon--right" style="cursor: pointer;"></i>
                </div>
                <el-dropdown-menu slot="dropdown" class="cb-tab-dropdown">
                    <el-dropdown-item @click.native.prevent.stop="closeTabCurrent">关闭标签页</el-dropdown-item>
                    <el-dropdown-item @click.native.prevent.stop="closeTabOther">关闭其他标签页</el-dropdown-item>
                    <el-dropdown-item @click.native.prevent.stop="closeTavRight">关闭右侧标签页</el-dropdown-item>
                    <el-dropdown-item @click.native.prevent.stop="closeTavLeft">关闭左侧标签页</el-dropdown-item>
                    <el-dropdown-item @click.native.prevent.stop="closeTavAll">关闭全部标签页</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
        </div>
    </div>
</template>

<script>
    export default {
        name: "CbTab",

        // 定义v-mode
        model: {
            prop: 'activeName',
            event: 'change'
        },
        props: {
            activeName: {   // 绑定值，选中选项卡的 name
                type: String,
                default: 'name'
            },
            type: {  // 风格类型
                type: String,
                default: 'card'
            },
            closable: { // 标签是否可关闭
                type: Boolean,
                default: true
            },
            'tab-key': String | Number,
            // tab数据
            tabData: {
                type: Array,
                default() {
                    return [
                        {
                            label: '选项卡标题',  // 选项卡标题
                            name: 'name' // 与选项卡绑定值 value 对应的标识符，表示选项卡别名
                        }
                    ]
                }
            },
            maxTabs: {  // 展示tabs最大个数
                type: Number,
                default: 20
            },
            closes: { // 是否需要功能删除
                type: Boolean,
                default() {
                    return true;
                }
            }

        },
        data() {
            return {
                tabsData: [],// tabs的数据
                oldActiveName: '', // 存储被激活的name，这里用于回滚activeName
                elTabActiveName: '' // element 的tab激活名称
            }
        },
        mounted() {
            this.elTabActiveName = this.activeName;
        },
        watch: {
            tabData(newVal) {
                // 控制tab不能有重复的数据
                const flag = newVal.filter(item => item.name === newVal[newVal.length - 1].name);
                if (flag.length >= 2) {
                    // this.$message.warning(`不能重复展示${newVal[newVal.length - 1].label}`);
                    newVal.splice(newVal.length - 1, 1);
                    return;
                }
                // 控制tab个数
                if (newVal.length > this.maxTabs) {
                    this.$message.warning(`最多展示${this.maxTabs}个tabs`);
                    newVal.splice(newVal.length - 1, 1);
                    this.$emit('change', this.oldActiveName);
                    return;
                }
                this.tabsData.splice(0, this.tabsData.length, ...newVal);
                this.oldActiveName = this.activeName;
            },
            activeName(newVal, oldVal) {
                if (newVal === oldVal) return;
                this.elTabActiveName = newVal;
            },
            /*activeName(){
                if (this.closes) {
                    console.log(this.$refs.cbTab);
                    this.$refs.cbTab.querySelector('.el-tabs__header').style.marginRight = '55px';
                } else {
                    this.$refs.cbTab.querySelector('.el-tabs__header').style.marginRight = '0';
                }
            }*/

        },
        methods: {
            /**
             * tab 被选中时触发
             * @param tab 被选中的标签 tab 实例
             */
            tabClick(tab) {
                this.$emit('change', tab['name']);
                this.$emit('tab-click', tab);
            },

            /**
             * 关闭全部标签页
             */
            closeTavAll() {
                this.tabData.splice(0, this.tabData.length);
            },
            /**
             * 关闭左侧标签页
             */
            closeTavLeft() {
                let index = -1;
                let tabs = this.tabData;
                tabs.forEach((tab, i) => {
                    if (tab['name'] === this.elTabActiveName) {
                        index = i;
                    }
                });
                this.tabData.splice(0, index);
            },
            /**
             * 关闭右侧标签页
             */
            closeTavRight() {
                let index = -1;
                let tabs = this.tabData;
                tabs.forEach((tab, i) => {
                    if (tab['name'] === this.elTabActiveName) {
                        index = i;
                    }
                });
                this.tabData.splice(index + 1, this.tabData.length);
            },
            /**
             * 关闭其他标签页
             */
            closeTabOther() {
                let tabs = this.tabData;
                const tabData = tabs.filter(tab => tab.name === this.elTabActiveName);
                this.tabData.splice(0, this.tabData.length, ...tabData);
            },

            /**
             * 删除当前标签页
             */
            closeTabCurrent() {
                this.tabRemove(this.elTabActiveName)
            },

            /**
             * 点击 tab 移除按钮后触发
             * @param name 被删除的标签的 name
             */
            tabRemove(targetName) {
                let tabs = this.tabData;
                let activeName = this.activeName;
                if (activeName === targetName) {
                    tabs.forEach((tab, index) => {
                        if (tab.name === targetName) {
                            let nextTab = tabs[index + 1] || tabs[index - 1];
                            if (nextTab) {
                                activeName = nextTab.name;
                            }
                        }
                    });
                }
                const tabData = tabs.filter(tab => tab.name !== targetName);
                this.tabData.splice(0, this.tabData.length, ...tabData);
                this.$emit('change', activeName);
                this.$emit('tab-remove', targetName);
            }
        }
    }
</script>

<style lang="less">
    // 右边添加关闭所有 当前 右侧 左侧
    .el-tabs__header {
        margin-right: 0;
    }

    .cb-closes > .el-tabs > .el-tabs__header {
        margin-right: 55px;
    }

    .cb-tab {
        width: 100%;
        height: 100%;
        position: absolute;

        .el-tabs {
            width: 100%;
            height: 100%;
            display: flex;
            flex-direction: column;

            .el-tabs__content {
                flex: 1;

                .el-tab-pane {
                    width: 100%;
                    height: 100%;
                }
            }
        }

        .el-tabs__nav-wrap {
            height: 2.5rem;
            padding-top: 0.5rem;
            border-bottom: none;

            .el-tabs__item {
                border: 0 !important;
                border-radius: 0 !important;
                font-size: 1.2rem;
                line-height: 2rem;
                height: 2rem;
                min-width: 100px;
                text-align: center;
                position: relative;


            }
        }

        .el-tabs__nav-next, .el-tabs__nav-prev {
            line-height: 25px;
            padding: 0 3px;
        }

        .el-tabs__nav {
            border: 0 !important;
            border-radius: 0 !important;
        }

        .el-tabs {
            .el-tabs__header {
                margin-bottom: 0;
            }

            // tabs 内容区域
            .el-tabs__content:first-child {
                overflow: auto;
                position: absolute;
                padding: 0;
                top: 2.5rem;
                bottom: 0;
                left: 0;
                right: 0;

            }
        }

        // 上部信息展示
        .tabs-top-content {
            padding: 2.5rem;
            line-height: 3rem;

            .item {
                display: flex;

                .value {
                    flex: 1;
                    line-height: 1.6rem;
                    word-break: break-all;
                    padding: 0 10px;
                }

                .label, .value {
                    display: flex;
                    align-items: center;
                }
            }
        }

        .cb-tab-operation {
            position: absolute;
            right: 0;
            top: 0;
            width: 55px;
            height: 2.5rem;
            line-height: 2.5rem;
            display: flex;
            justify-content: center;
            align-items: center;

            .el-dropdown {
                flex: 1;
                text-align: center;
                color: rgba(255, 255, 255, .6);
                cursor: pointer;
            }
        }
    }


</style>
