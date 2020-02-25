<template>
    <div class="cb-tree">
        <slot name="header" style="color: #ffffff;"/>
        <div class="search-wrapper">
            <el-input
                    placeholder="输入关键字进行过滤"
                    v-model.trim="searchVal"
                    ref="inputRef"
                    @keyup.native.enter.stop.prevent="search(searchVal)">
                <el-button v-if="searchFromHttp" slot="append" icon="el-icon-search"
                           @click="search(searchVal)"></el-button>
            </el-input>
            <slot name="searchAppend"/>
        </div>
        <div class="el-tree-container">
            <el-tree
                    class="filter-tree"
                    :node-key="nodeKey"
                    :data="data"
                    :props="defaultProps"
                    :expand-on-click-node="false"
                    :load="loadNode"
                    :filter-node-method="filterNode"
                    :lazy="lazy"
                    :default-expanded-keys="defaultExpandedKeys"
                    :default-checked-keys="defaultCheckedKeys"
                    @node-expand="nodeExpand"
                    @node-collapse="nodeCollapse"
                    ref="tree"
                    v-if="!searchFromHttp">
                <div slot-scope="{node,data}" @click="nodeClick(data,node,arguments)">
                    <template v-if="(data.modelType + data.nodeType).toString().includes('omaha')"> <!-- omaha -->
                        <i class="cb-tree-icon iconfont icon-tree-omaha"></i>
                    </template>
                    <template v-else-if="(data.modelType + data.nodeType).toString().includes('Identification')">
                        <!-- 标识 -->
                        <i class="cb-tree-icon iconfont icon-tree-Identification"></i>
                    </template>
                    <template v-else-if="(data.modelType + data.nodeType).toString().includes('flag')"> <!-- 指标 -->
                        <i class="cb-tree-icon iconfont icon-tree-flag"></i>
                    </template>
                    <template v-else-if="(data.modelType + data.nodeType).toString().includes('database')"> <!-- 数据库 -->
                        <i class="cb-tree-icon iconfont icon-tree-database"></i>
                    </template>
                    <template v-else-if="(data.modelType + data.nodeType).toString().includes('view')"> <!-- 视图 -->
                        <i class="cb-tree-icon iconfont icon-tree-view"></i>
                    </template>
                    <template v-else-if="(data.modelType + data.nodeType).toString().includes('element_item')">
                        <!-- 元素项 -->
                        <i class="cb-tree-icon iconfont icon-yuansu"></i>
                    </template>
                    <template v-else-if="(data.modelType + data.nodeType).toString().includes('table')"> <!-- 表 -->
                        <i class="cb-tree-icon iconfont icon-tree-table"></i>
                    </template>
                    <template v-else-if="(data.modelType + data.nodeType).toString().includes('element_set')">
                        <!-- 元素项集合 -->
                        <i class="cb-tree-icon iconfont icon-tree-element_set"></i>
                    </template>
                    <template v-else-if="(data.modelType + data.nodeType).toString().includes('column')"> <!-- 列 -->
                        <i class="cb-tree-icon iconfont icon-tree-column"></i>
                    </template>
                    <template v-else-if="(data.modelType + data.nodeType).toString().includes('primary_key')">
                        <!-- 主键 -->
                        <i class="cb-tree-icon iconfont icon-tree-primary_key"></i>
                    </template>
                    <template v-else-if="(data.modelType + data.nodeType).toString().includes('index')"> <!-- 索引 -->
                        <i class="cb-tree-icon iconfont icon-tree-index"></i>
                    </template>
                    <template v-else-if="(data.modelType + data.nodeType).toString().includes('ETL')"> <!-- ETL -->
                        <i class="cb-tree-icon iconfont icon-tree-ETL"></i>
                    </template>
                    <template v-else-if="(data.modelType + data.nodeType).toString().includes('foreign_key')">
                        <!-- 外键 -->
                        <i class="cb-tree-icon iconfont icon-tree-foreign_key"></i>
                    </template>
                    <template v-else-if="(data.modelType + data.nodeType).toString().includes('category')"> <!-- 分类 -->
                        <i class="cb-tree-icon iconfont icon-category"></i>
                    </template>
                    <template v-else-if="(data.modelType + data.nodeType).toString().includes('dataset')"> <!-- 数据集 -->
                        <i class="cb-tree-icon iconfont icon-shujuji"></i>
                    </template>
                    <template v-else-if="(data.modelType + data.nodeType).toString().includes('project')"> <!-- 工程 -->
                        <i class="cb-tree-icon iconfont icon-xiangmu1"></i>
                    </template>
                    <template v-else> <!-- 默认 -->
                        <i class="cb-tree-icon iconfont icon-morenfenlei"></i>
                    </template>
                    <slot v-bind:node="node" :data="data">
                        {{data[defaultProps.label]}}
                    </slot>
                </div>
            </el-tree>
            <el-tree
                    class="filter-tree"
                    :node-key="nodeKey"
                    :data="data"
                    :props="defaultProps"
                    :expand-on-click-node="false"
                    :load="loadNode"
                    :filter-node-method="filterNode"
                    :lazy="lazy"
                    :default-expanded-keys="defaultExpandedKeys"
                    :default-checked-keys="defaultCheckedKeys"
                    @node-expand="nodeExpand"
                    @node-collapse="nodeCollapse"
                    v-if="searchFromHttp"
                    ref="tree">
                <div slot-scope="{node,data}" @click="nodeClick(data,node,arguments)">
                    <template v-if="(data.modelType + data.nodeType).toString().includes('omaha')"> <!-- omaha -->
                        <i class="cb-tree-icon iconfont icon-tree-omaha"></i>
                    </template>
                    <template v-else-if="(data.modelType + data.nodeType).toString().includes('Identification')">
                        <!-- 标识 -->
                        <i class="cb-tree-icon iconfont icon-tree-Identification"></i>
                    </template>
                    <template v-else-if="(data.modelType + data.nodeType).toString().includes('flag')"> <!-- 指标 -->
                        <i class="cb-tree-icon iconfont icon-tree-flag"></i>
                    </template>
                    <template v-else-if="(data.modelType + data.nodeType).toString().includes('database')"> <!-- 数据库 -->
                        <i class="cb-tree-icon iconfont icon-tree-database"></i>
                    </template>
                    <template v-else-if="(data.modelType + data.nodeType).toString().includes('view')"> <!-- 视图 -->
                        <i class="cb-tree-icon iconfont icon-tree-view"></i>
                    </template>
                    <template v-else-if="(data.modelType + data.nodeType).toString().includes('element_item')">
                        <!-- 元素项 -->
                        <i class="cb-tree-icon iconfont icon-yuansu"></i>
                    </template>
                    <template v-else-if="(data.modelType + data.nodeType).toString().includes('table')"> <!-- 表 -->
                        <i class="cb-tree-icon iconfont icon-tree-table"></i>
                    </template>
                    <template v-else-if="(data.modelType + data.nodeType).toString().includes('element_set')">
                        <!-- 元素项集合 -->
                        <i class="cb-tree-icon iconfont icon-tree-element_set"></i>
                    </template>
                    <template v-else-if="(data.modelType + data.nodeType).toString().includes('column')"> <!-- 列 -->
                        <i class="cb-tree-icon iconfont icon-tree-column"></i>
                    </template>
                    <template v-else-if="(data.modelType + data.nodeType).toString().includes('primary_key')">
                        <!-- 主键 -->
                        <i class="cb-tree-icon iconfont icon-tree-primary_key"></i>
                    </template>
                    <template v-else-if="(data.modelType + data.nodeType).toString().includes('index')"> <!-- 索引 -->
                        <i class="cb-tree-icon iconfont icon-tree-index"></i>
                    </template>
                    <template v-else-if="(data.modelType + data.nodeType).toString().includes('ETL')"> <!-- ETL -->
                        <i class="cb-tree-icon iconfont icon-tree-ETL"></i>
                    </template>
                    <template v-else-if="(data.modelType + data.nodeType).toString().includes('foreign_key')">
                        <!-- 外键 -->
                        <i class="cb-tree-icon iconfont icon-tree-foreign_key"></i>
                    </template>
                    <template v-else-if="(data.modelType + data.nodeType).toString().includes('category')"> <!-- 分类 -->
                        <i class="cb-tree-icon iconfont icon-category"></i>
                    </template>
                    <template v-else-if="(data.modelType + data.nodeType).toString().includes('dataset')"> <!-- 数据集 -->
                        <i class="cb-tree-icon iconfont icon-shujuji"></i>
                    </template>
                    <template v-else-if="(data.modelType + data.nodeType).toString().includes('project')"> <!-- 工程 -->
                        <i class="cb-tree-icon iconfont icon-xiangmu1"></i>
                    </template>
                    <template v-else> <!-- 默认 -->
                        <i class="cb-tree-icon iconfont icon-morenfenlei"></i>
                    </template>
                    <slot v-bind:node="node" :data="data">
                        {{data[defaultProps.label]}}
                    </slot>
                </div>
            </el-tree>
        </div>
    </div>
</template>

<script>
    import defaultTheme from '../../common/moc/defaultTheme';

    export default {
        name: "cbTree",
        model: {
            prop: 'searchVal',
            event: 'change'
        },
        props: {
            searchTextColor: { // 搜索框文字颜色
                type: String,
                default: '#ccc'
            },
            nodeKey: { // 每个树节点用来作为唯一标识的属性，整棵树应该是唯一的
                default: 'uuid'
            },
            lazy: { // 是否懒加载子节点，需与 load 方法结合使用
                type: Boolean,
                default: false
            },
            searchFromHttp: { // 搜索框的搜索的数据需要通过http请求
                type: Boolean,
                default: false
            },
            defaultProps: { //  tree树配置
                type: Object,
                default() {
                    return {
                        children: 'children',
                        label: 'nameCn'
                    }
                }
            },
            searchHidden: {
                type: Boolean,
                default: true
            },
            icon: {
                type: Boolean,
                default: true
            },
            data: Array // tree数
        },
        data() {
            return {
                searchVal: '', // tree过滤内容
                defaultExpandedKeys: [], // 默认展开的节点的 key 的数组
                defaultExpandedRelation: [], // 存储展开节点父子关系
                defaultCheckedKeys: [1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 10, 11, 12, 13], // 默认展开的节点的 key 的数组
                clickFlag: false, // 点击之后为true onmouseleave之后设置为false；这个作用，是控制node节点被点击之后高亮
            };
        },
        mounted() {
            // this.initStyle();
        },

        watch: {
            searchVal(val) {
                this.emitSearch(val);
                if (!this.searchFromHttp) {
                    this.$refs.tree.filter(val)
                }
            },
            defaultExpandedRelation(val) {
                this.defaultExpandedKeys = [];
                val.forEach(item => {
                    this.defaultExpandedKeys.push(item.nodeKey);
                })
                console.log(this.defaultExpandedKeys);
            }
        },

        methods: {
            /**
             * emit v-model
             */
            emitSearch(val) {
                this.searchVal = val;
                this.$emit('change', val);
            },

            /**
             * 搜索
             * @param val 需要搜索的值
             */
            search(val) {
                this.$emit('search', val);
            },
            /**
             * 节点被点击时的回调
             * @param data  新数据
             * @param node 需要刷新的node节点
             * @param component 当前组件
             * */
            nodeClick(data, node, component) {
                this.clickFlag = true;
                this.$emit('node-click', data, node, component[0])
            },
            /**
             * 节点被展开时触发的事件 将
             */
            nodeExpand(data, Node, Component) {
                // this.defaultExpandedKeys.push(data[this.nodeKey]);
                if (Node.level === 1) {
                    this.defaultExpandedRelation.push({
                        parent: null,
                        nodeKey: data[this.nodeKey]
                    })
                } else {
                    this.defaultExpandedRelation.push({
                        parent: Node.parent.data[this.nodeKey],
                        nodeKey: data[this.nodeKey]
                    })
                }
                this.$emit('node-expand', data, Node, Component)
            },
            nodeCollapse(data, Node, Component) {
                let parentNodeKeys = [];
                this.defaultExpandedRelation.forEach(item => {
                    parentNodeKeys.push(item.parent)
                });
                let stayData = []; // 存储被删除后的数据
                // 删除 defaultExpandedRelation 中 nodeKey  为参数 nodeKey
                // 删除 parent  为参数 nodeKey
                this.defaultExpandedRelation.forEach(item => {
                    if (item.nodeKey !== data[this.nodeKey] && !parentNodeKeys.includes(item.parent)) {
                        stayData.push(item)
                    }
                });
                this.defaultExpandedRelation.splice(0, this.defaultExpandedRelation.length, ...stayData);
                this.$emit('node-collapse', data, Node, Component)
            },

            /**
             * tree树内容过滤
             * @param value 被过滤出来的模糊值
             * @param data tree树整体数据
             * @returns {boolean}
             */
            filterNode(value, data) {
                if (!value) return true;
                let name = (data['nameCn'] + data['nameEn']).toLowerCase();
                return name.indexOf(value.trim().toLowerCase()) !== -1


            },

            /**
             * 刷新tree树当前节点
             * @param node 需要刷新的node节点
             * @param data  新数据
             * @returns {boolean} 返回节点是否被刷新成功
             */
            refreshThisNode(node, data) {
                if (node && data) {
                    Object.assign(node.data, node.data, data);
                    return true
                } else {
                    return false
                }
            },
            /**
             * 刷新tree树子节点
             * @param node 需要父节点node
             * @param children  新数据
             * @returns {boolean} 返回节点是否被刷新成功
             */
            refreshChildNode(node, children) {
                try {
                    let theChildren = node.childNodes;
                    theChildren.splice(0, theChildren.length);
                    node.doCreateChildren(children);
                    return true;
                } catch (e) {
                    return false;
                }
            },
            /**
             * 删除节点
             * @param node 需要删除的节点
             * @returns {boolean} 返回节点是否被刷新成功
             */
            remove(node, data) {
                try {
                    const children = node.parent.childNodes;
                    const index = children.findIndex(d => d.data[this.nodeKey] === data[this.nodeKey]);
                    children.splice(index, 1);
                    return true;
                } catch (e) {
                    return false;
                }
            },
            /**
             * 懒加载树
             * @param node node节点
             * @param resolve
             */
            loadNode(node, resolve) {
                if (this.lazy) {
                    this.$emit('load-node', node, resolve);
                }
            },

            /**
             * 根据 key 拿到 Tree 组件中的 node
             * @param key
             */
            getNode(key) {
                return this.$refs.tree.getNode(key);
            },
            /**
             * 通过 key 设置某个节点的当前选中状态，使用此方法必须设置 node-key 属性
             * @param nodeKey
             */
            setCurrentKey(nodeKey) {
                this.$refs.tree.setCurrentKey('');
            }
        },

    }
</script>

<style lang="less">
    .cb-tree {
        width: 100%;
        height: 100%;
        display: flex;
        flex-direction: column;

        .search-wrapper {
            padding: 10px 5px;
            display: flex;
            align-items: center;
        }

        .el-tree-container {
            * {
                user-select: none;
            }

            overflow: auto;
            flex: 1;
            /*margin-top: 10px;*/

            .el-tree-node__content {
                position: relative;
                display: flex;

                & > div {
                    flex: 1;
                    position: relative;
                    display: flex;

                }
            }

            // 初始化tree中所有的背景颜色
            & > * {
                background-color: transparent;

                .el-tree-node__content:hover { // 鼠标hover上之后高亮
                    background-color: transparent;
                }

                .el-tree-node:focus > .el-tree-node__content { // 鼠标点击之后高亮
                    background-color: transparent;
                }
            }
        }

        .tree-header {
            padding: 10px;
            color: #fff;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }

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

        // tree icon
        .cb-tree-icon {
            margin-right: 2px;
            font-size: 16px;
        }


    }
</style>
