<template>
  <div class="metadata-to-data cb-tree-show-operation-wrapper">
    <div class="tree-wrapper">
      <cb-tree lazy
               @load-node="loadNode"
               ref="tree">
        <template slot-scope="{data,node}">
          <div class="tree-name" @click="treeNodeClick(data,node)">
            <span>{{data.nameCn || data.nameEn || 'unknown'}}</span>
          </div>
        </template>
      </cb-tree>
    </div>
    <div class="tab-wrapper show-operation-wrapper">
      <cb-operation-container>
        <template slot="toOperation">
          <el-button icon="iconfont icon-daoru" class="confirm-text" type="text"
                     @click="importMateData(tab.tabNode)">导 入
          </el-button>
        </template>
        <template slot="cbContainer">
        </template>
      </cb-operation-container>
    </div>
  </div>
</template>

<script lang="ts">
  import Vue from 'vue'
  import {Component} from 'vue-property-decorator'
  import config from "../../config";

  @Component({})
  export default class Index extends Vue {
    name: string = 'Index';


    /**
     * 懒加载树
     * @param node node节点
     * @param resolve
     */
    public loadNode(node, resolve): void {
      // 加载实例表时候，调用其他接口
      let params: object = {};
      if (node.data && node.data.modelType === 'table') {
        params = {
          tableName: '',
          modelId: node.parent.data.modelId,
          resourceId: node.parent.data.resourceId,
          metaDataType: '',
          metaDataId: '',
          parentMetaDataId: '',
          size: -1
        };
        let url = config.port('metadataImport') + '/getTablePageList';
        this.$http.get(url, {params}).then(response => {
          // this.$http.get(url).then(response => {
          resolve(response.data.data.records)
        }).catch(err => resolve([]));
        return;
      }


      // level = 0 代表tree刚加载
      if (node.level !== 0) {
        // level大于等于1时候，需要传递参数获取子级数据
        params['parentUuid'] = node.data.uuid;
      }
      let url = config.port('metadatavalue') + '/tree/lazyTree';
      this.$http.get(url, {params}).then(response => {
        resolve(response.data.data)
      }).catch(err => resolve([]));
    }

  }
</script>

<style lang="less">
</style>
