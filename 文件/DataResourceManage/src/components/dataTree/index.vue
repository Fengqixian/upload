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
            :load="loadNode"
            :filter-node-method="treeFilterNode"
            :expand-on-click-node="false"
            lazy
            @node-click="nodeClick"
            ref="tree"
          >
            <div class="tree-slot-div" slot-scope="{ node, data }">
              <span>{{data.name}}</span>
              <slot :node="node" :data="data" class="tree-icon"></slot>
              <!--<span class="tree-icon iconfont icon-drag" @mousedown="handleMouseDown(data.name,data,node,arguments)"-->
              <!--v-if="node.level === 8"></span>-->
              <span class="tree-icon iconfont icon-drag"
                    @mousedown="handleMouseDown(data.name,data,node,arguments)"></span>
            </div>
          </el-tree>
        </div>
      </div>
      <div class="tabs">
        <slot name="content" class="tab"></slot>
      </div>
    </div>
    <!--
    <div class="tree dom" v-if="domHtml !== ''" id="dom">
      <div class="tree-scroll-wrapper">
        <div class="el-tree">
          <div class="el-tree-node is-focusable" v-html="domHtml">
          </div>
        </div>
      </div>
    </div>
    -->
    <div class="cbDragDom" v-if="dragShow">
      {{domHtml}}
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
    domHtml: any = '';
    @Mutation setLoadingFlag
    // tree搜索值
    searchModelValue: string = '';
    // 元模型tree数据
    treeData: Array<object> = []
    defaultProps: object = {
      children: 'children',
      label: 'label'
    }
    // 拖拽定位
    cbDragDomLeft: number = 0;
    cbDragDomTop: number = 0;
    // 是否显示dragdom
    dragShow: boolean = true;
    // node节点数据
    nodeData: any = '';
    // node节点
    node: any = '';
    @Prop()
    title

    // 监听删选框数据变化
    @Watch('searchModelValue')
    onChangeSearchModelValue(val: string, oldVal: string) {
      this.myThis.$refs.tree.filter(val);
    }

    public mounted(): void {
      const cbDragDom = document.querySelector('.cbDragDom');
      document.addEventListener('mouseup', (el) => {
        this.$emit('cb-drag-end', el)
        document.removeEventListener('mousemove', this.setDragStyle, false);
        cbDragDom === null ? null : cbDragDom.removeAttribute('style');
        this.dragShow = false;
        this.domHtml = '';
        this.nodeData = '';
        this.node = '';
        setTimeout(_ => {
          this.dragShow = true
        }, 1)
      })
    }

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

    public handleMouseDown(name, data, node, el): void {
      this.domHtml = name;
      this.nodeData = data;
      this.node = node;
      this.setDragStyle(el[0]);
      document.addEventListener('mousemove', this.setDragStyle, false);
    }

    public setDragStyle(el): any {
      const cbDragDom = document.querySelector('.cbDragDom');
      cbDragDom === null ? null : (cbDragDom.setAttribute('style', `left:${el.clientX}px;top:${el.clientY}px;width:70px;height:70px`) || this.$emit('cb-drag', this.node, this.nodeData, cbDragDom, el))
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
