<template>
  <div class="version-manage-tree">
    <div class="select-wrapper">
      <el-form ref="form" label-width="62px">
        <el-form-item label="版本选择">
          <el-select
            filterable
            ref="hisVersionSelect"
            v-model.trim="selectedVersion.vs_code"
            @change="onSelectedChange" v-if="selector">
            <el-option
              v-for="x in historyVersions"
              :key="x.vs_code"
              v-if="x.vs_code!==currentVersionCode"
              :label="x.vs_name"
              :value="x.vs_code"
            ></el-option>
          </el-select>
          <el-select filterable v-model.trim="selectedVersion.vs_code" disabled @change="onSelectedChange" v-else>
            <el-option
              v-for="x in historyVersions"
              :key="x.vs_code"
              :label="x.vs_name"
              :value="x.vs_code"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
    </div>
    <el-row>
      <el-col :span="24" class="version-tree-wrapper">
        <el-tree v-if="treeRefresh"
                 ref="versionManagerTree"
                 :props="defaultProps"
                 :load="loadNode"
                 @node-click="handleNodeClick"
                 node-key="tree_node_key"
                 :expand-on-click-node="false"
                 :default-expanded-keys="defaultExpandedKeys"
                 lazy>
        </el-tree>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import Vue from 'vue'
  import Component from 'vue-class-component'
  import config from '../../config.ts'
  import Qs from 'qs'
  import {mapMutations} from 'vuex'

  export default {
    data() {
      return {
        /*树形结构属性*/
        defaultProps: {
          children: 'children',
          label: 'name'
        },
        loaded: false,
        selectedVersion: {},
        treeRefresh: true,
        currentVersionCode: '',
        currentSelectionTreeNode: '',
        defaultExpandedKeys: [] // 设置默认展开节点
      }
    },
    props: {
      historyVersions: {
        type: Array,
        default: () => []
      },
      selector: {
        type: Boolean,
        default: () => true
      }
    },
    created: function () {
      this.currentVersionCode = localStorage.getItem("versionCode") || '';
      if (!this.selector) {
        this.selectedVersion.vs_code = this.currentVersionCode;
      }
    },
    methods: {
      ...mapMutations(['setLoadingFlag']),
      onSelectedChange(data) {
        this.treeRefresh = false;
        //通知父组件-当前下拉框值已经改变
        this.$emit('versionManageHisVersionChange', this.selectedVersion);

        this.$nextTick(() => {
          this.treeRefresh = true
        })
      },
      handleNodeClick(data) {
        this.currentSelectionTreeNode = this.$refs.versionManagerTree.getCurrentKey();
        this.$emit('currentSelectionTreeNode', this.currentSelectionTreeNode);
      },
      /*树形节点加载子节点函数*/
      loadNode(node, resolve) {
        //加载树首次加载顶级模型传入参数 parentId
        let URL = config.port('metadataManage') + 'getMetadataModelTree';
        var p = {
          parentId: null,
          moduleInstanceId: null,
          treeType: null,
          specifiedVersion: this.selectedVersion.vs_code || ''
        };
        //如果首次加载
        if (node.level === 0) {
          p = {
            parentId: null,
            moduleInstanceId: null,
            treeType: null,
            specifiedVersion: this.selectedVersion.vs_code || ''
          };
        } else {  //非首次加载
          p = {
            parentId: node.data.module_id,
            moduleInstanceId: node.data.module_instance_id,
            treeType: node.data.tree_type,
            specifiedVersion: this.selectedVersion.vs_code || ''
          };
        }
        var param = new Object();
        param.metaJson = JSON.stringify(p);

        this.$http.post(URL, Qs.stringify(param)).then((response) => {

          resolve(response.data.data || []);
          if (node.level === 0) {
            response.data.data.forEach(item => {
              this.defaultExpandedKeys.push(item.tree_node_key)
            })
          }
        }).catch(function (response) {
        })
      },

    }
  }
</script>

<style lang="less">

  .version-manage-tree {
    display: flex;
    flex-direction: column;

    .el-row {
      flex: 1;
      overflow-y: auto;
    }
    .select-wrapper {
      height: 4rem;
      border-bottom: 0.1rem dashed #ccc;
      display: flex;
      align-items: center;
      padding: 1rem;

      .label {
        margin-right: 1rem;
      }

      input {
        border-radius: 0;
        height: 2.2rem;
        line-height: 2.2rem;
      }

      .el-input__icon {
        height: 2.2rem !important;
        line-height: 2.2rem !important;
      }
    }
  }
</style>
