<template>
  <div class="model-tree">
    <div class="tree-content">
      <span class="title">{{title}}</span>
      <el-input
        v-model.trim="searchModelValue"
        suffix-icon="el-icon-search"></el-input>
      <el-tree
        :default-expand-all="defaultExpandAll"
        :props="defaultProps"
        :load="loadNode"
        @node-click="handleNodeClick"
        ref="metaDataTree"
        node-key="tree_node_key"
        :filter-node-method="treeFilterNode"
        :expand-on-click-node="false"
        lazy>
      </el-tree>
    </div>
  </div>
</template>

<script>
  import config from '../../config.ts'
  import Qs from 'qs'

  export default {
    name: 'ModelTree',
    data() {
      return {
        // tree搜索值
        searchModelValue: '',
        /*树形结构属性*/
        defaultProps: {
          children: 'children',
          label: 'name'
        },
      }
    },
    props: {
      // 树的标题
      title: String,
      // 树是否自动展开
      defaultExpandAll: {
        type: Boolean,
        default: false
      },
      // 树默认展开到哪一级
      defaultExpandNodeLevel: Number,
      ignoreNodes: {  // 不展示某个节点
        type: Array
      },
    },
    methods: {
      /*树形节点加载子节点函数*/
      loadNode(node, resolve) {
        //加载树首次加载顶级模型传入参数 parentId
        let URL = config.port('metadataManage') + 'getMetadataModelTree';
        var p = {
          parentId: null,
          moduleInstanceId: null,
          treeType: null
        };
        if (this.defaultExpandNodeLevel !== undefined && node.level === this.defaultExpandNodeLevel) {
          resolve([]);
          return
        }
        //如果首次加载
        if (node.level === 0) {
          p = {
            parentId: null,
            moduleInstanceId: null,
            treeType: null
          };
        } else {  //非首次加载
          p = {
            parentId: node.data.module_id,
            moduleInstanceId: node.data.module_instance_id,
            treeType: node.data.tree_type
          };
        }
        var param = new Object();
        param['metaJson'] = JSON.stringify(p);
        // this.loading = true
        this.$http.post(URL, Qs.stringify(param)).then((response) => {
          // this.loading = false
          let data = !response.data.data ? null : response.data.data.filter(item => {
            return !this.ignoreNodes ? true : !this.ignoreNodes.includes(item.tree_node_key)
          });
          resolve(data || []);
        }).catch(function (response) {
        })
      },
      /*元数据树形结构节点点击回调函数*/
      handleNodeClick(data, node) {
        this.$emit('node-click', data, node)
      },
      // tree节点过滤
      treeFilterNode(value, data) {
        if (!value) return true;
        return data.name.indexOf(value) !== -1;
      }
    }
  }
</script>

<style scoped lang="less">
  .tree-content {
    z-index: 11;
    position: absolute;
    top: 0;
    left: 0;
    bottom: 0;
    width: 23rem;
    overflow: auto;
    padding: 0 1rem;
    background: #fff;
    border: 1px solid #ccc;

    span {
      font-size: 1.2rem !important;
    }

    .title {
      display: inline-block;
      line-height: 3rem;
      font-weight: 600;
    }
  }
</style>
