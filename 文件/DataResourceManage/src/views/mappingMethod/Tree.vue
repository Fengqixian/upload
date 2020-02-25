<template>
  <div class="data-service sheets-container header-no-operate">
    <div class="tree-container">
      <div class="tree">
        <span class="title" v-if="title">{{title}}</span>
        <el-input
          v-model.trim="searchModelValue"
          placeholder="请输入关键词"
          suffix-icon="el-icon-search"></el-input>
        <div class="tree-scroll-wrapper">
          <el-tree
            :data="treeData"
            :props="defaultProps"
            node-key="tree_node_key"
            :filter-node-method="treeFilterNode"
            :expand-on-click-node="false"
            @node-click="nodeClick"
            ref="tree">
            <div class="tree-slot-div" slot-scope="{ node, data }">
              <span>{{data.name_cn}}</span>
              <slot :node="node" :data="data" class="tree-icon"></slot>
            </div>
          </el-tree>
        </div>
      </div>
      <div class="tabs">
        <slot name="content" class="tab"></slot>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
  import Vue from 'vue'
  import {Component, Watch, Prop} from 'vue-property-decorator'
  import {State, Mutation} from 'vuex-class'
  import config from '../../config'
  import Qs from 'qs'

  @Component({})
  export default class Index extends Vue {
    myThis: any = this
    name: string = 'DataService';
    @Mutation setLoadingFlag
    // tree搜索值
    searchModelValue: string = '';
    // 元模型tree数据
    treeData: Array<object> = []
    defaultProps: object = {
      children: 'children',
      label: 'label'
    }
    // node节点数据
    nodeData: any = '';
    // node节点
    node: any = '';
    //模型分页参数
    paging: any = config.page();
    //元模型值
    modelData: any = [];
    @Prop()
    title


    public mounted(): void {
      this.getTreeData();
    }

    public getTreeData(): void {
      let URL = config.port('metamodel') + 'selectMetaModulePage';
      this.modelData.pageAjax = this.paging;
      let pageAjax = {pageAjax: {}}


      this.$http.post(URL, pageAjax).then((response) => {

        //要显示的元模型
        this.modelData = response.data.tableData;
        const treeData = response.data.tableData;
        this.treeData = this.arr2tree(treeData, {
          id: 'id',
          pid: 'parent_id',
          children: 'children'
        });
        //总记录数
        this.paging.total = response.data.total;
      }).catch(function (response) {
      })
    }


    // 扁平化数据转树状数据
    arr2tree(data, config) {
      var id = config.id || 'id';
      var pid = config.pid || 'pid';
      var children = config.children || 'children';
      var idMap = {};
      var jsonTree: Array<object> = [];
      data.forEach(function (v) {
        idMap[v[id]] = v;
      });
      data.forEach(function (v) {
        var parent = idMap[v[pid]];
        if (parent) {
          !parent[children] && (parent[children] = []);
          parent[children].push(v);
        } else {
          jsonTree.push(v);
        }
      });
      return jsonTree;
    };

    // 懒加载tree
    /*树形节点加载子节点函数*/
    loadNode(node, resolve) {
      //加载树首次加载顶级模型传入参数 parentId
      let URL = config.port('metadataManage') + 'getMetadataModelTree';
      var p = {
        parentId: null,
        moduleInstanceId: null,
        treeType: null
      };
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
      var param = {};
      param['metaJson'] = JSON.stringify(p);

      this.$http.post(URL, Qs.stringify(param)).then((response) => {

        resolve(response.data.data || []);
      }).catch(function (response) {
      })
    }

    // tree节点过滤
    treeFilterNode(value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    }


    // 将事件暴露出去
    public nodeClick(data, node, component, e): void {
      this.$emit('node-click', data, node, component)
    }

  }
</script>

<style lang="less">
  .data-service {
    .tree-slot-div {
      display: flex;
      justify-content: space-between;
      padding-right: 20px;
    }
  }

  .dom {
    position: fixed;
    left: 50%;
    bottom: 120px;
    /*background-color: red;*/
    /*width: 100px;*/
    /*height: 100px;*/

    &.tree {
      width: 23rem;
      display: flex;
      flex-direction: column;
    }

    .el-tree {
      background-color: #39436a;
      color: #ffffff;

      .el-tree-node__content {
        width: 100%;
        display: flex;

        &:hover {
          background-color: #39436a;
        }

        .tree-slot-div {
          flex: 1;
        }
      }
    }
  }

  .cbDragDom {
    position: fixed;
    width: 0;
    height: 0;
    background-color: rgba(0, 0, 0, 0.7);
    /*transition: all 0.12s linear;*/
    transform: translate3d(-50%, -50%, 0);
    cursor: move;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #ffffff;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    z-index: 10000;
  }
</style>
