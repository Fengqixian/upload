<template>
    <div class="contraner">
        <div class="tree1">
            <cb-tree :data="treeData"
                     ref="tree"
                     nodeKey="uuid"
                     @node-click="nodeClick">
            </cb-tree>
        </div>
        <div class="tabss">
            <cb-tab :data="tabsData">
                <template v-for="(tab , index) in tabsData"
                          :slot="tab.name">
                    <div @click="change">change</div>
                    {{ tab.label}}
                    <hr>
                    <cb-table></cb-table>
                </template>
            </cb-tab>
        </div>
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component} from 'vue-property-decorator'

    @Component({})
    export default class Index extends Vue {
        name: string = 'Index';
        nodeKey: number = 0;
        treeData: Array<object> = [];

        // tab数据
        tabsData: Array<object> = [];

        public mounted() {
            this.tabsData = [{
                label: '选项卡标题',  // 选项卡标题
                name: 'name' // 与选项卡绑定值 value 对应的标识符，表示选项卡别名
            }, {
                label: '选项卡标题1',  // 选项卡标题
                name: '134123-fasf--rqr1234-asf-adsf-q234r213' // 与选项卡绑定值 value 对应的标识符，表示选项卡别名
            }];
            this.mockTreeData(0, this.treeData);
        }

        public mockTreeData(index, arr): void {
            if (index < 10) {
                for (let i = 0; i < 2; i++) {
                    arr.push({
                        label: `我是第${index}节点`,
                        uuid: Math.random(),
                        children: []
                    })
                }
                this.mockTreeData(++index, arr[0].children);
            }
        }

        public loadNode(node, resolve): void {
            if (node.level === 0) {
                return resolve([{label: 'region', uuid: 16}]);
            } else if (node.level >= 1) {
                setTimeout(_ => {
                    const data = [{
                        label: 'leaf',
                        uuid: this.nodeKey++
                    }, {
                        label: 'zone',
                        uuid: Math.random()
                    }];
                    return resolve(data);
                }, 500)
            } else {
                resolve([])
            }

        };

        public nodeClick(data1, node): void {
            let data = {
                label: 'newChild',
                uuid: '11111'
            }
            // this.$refs.tree['refreshTreeNode'](node, data);
        }

        public change(): void {
            this.$set(this.tabsData, 1, {
                label: '选项卡标题2',  // 选项卡标题
                name: 'sdafasdf' // 与选项卡绑定值 value 对应的标识符，表示选项卡别名
            })
        }
    }
</script>

<style scoped lang="less">
    .contraner {
        display: flex;

        .tabss {
            flex: 1;
            /*background-color: #b3d4fc;*/
        }
    }

    .tree1 {
        width: 300px;
        height: 100%;
    }
</style>
