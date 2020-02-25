<template>
  <div class="resource-role">
    <div class="outer-wrapper">
      <div class="wrapper">
        <div class="left">
          <div class="menu-btn">
            <el-button-group>
              <el-button type="primary" icon="el-icon-plus" @click="handlerSubmit">提交</el-button>
              <!--<el-button type="primary" icon="el-icon-resource" @click="handlerResource">修改</el-button>-->
              <!--<el-button type="primary" icon="el-icon-delete" @click="handleDelete">删除</el-button>-->
            </el-button-group>
          </div>
          <div class="menu-tree">
            <el-input
              placeholder="输入关键字进行过滤"
              v-model.trim="filterText">
            </el-input>
            <el-tree
              node-key="id"
              highlight-current
              :expand-on-click-node="false"
              class="filter-tree"
              :data="menuTreedata"
              :props="defaultProps"
              show-checkbox
              :filter-node-method="filterNode"
              ref="resourceTree">
            </el-tree>

          </div>
        </div>
        <div class="right">
          <div class="form-wrapper">
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
  import Vue from 'vue'
  import {Component, Watch} from 'vue-property-decorator'
  import config from '../../config'


  @Component({
    filters: {
      typeFilter(type) {
        const typeMap = {
          0: '菜单',
          1: '按钮'
        };
        return typeMap[type]
      }
    }
  })
  export default class ResourceRole extends Vue {
    name: string = 'ResourceRole';

    filterText: string = '';
    menuTreedata: Array<Object> = [];
    defaultProps: Object = {
      children: 'children',
      label: 'nameCn'
    };


    @Watch('filterText')
    public filterTextChange(val): void {
      this.$refs.tree['filter'](val);
    }

    public mounted(): void {
      this.getMenuTreeData();
    }

    // 资源树确认按钮
    public handlerSubmit(): void {
      return;

      let url = config.port('MetadataManage') + 'roleresourcepermission/save';
      this.$http.get(url).then(response => {
        const res = response.data;
        if (res.code === 0) {
          this.menuTreedata = res.data;
        }
      })
    }

    // 获取菜单tree树数据
    public getMenuTreeData(): void {
      let url = config.port('MetadataManage') + 'metadatavalue/tree';
      this.$http.get(url).then(response => {
        const res = response.data;
        if (res.code === 0) {
          this.menuTreedata = res.data;
        }
      })
    }

    // 搜索框过滤
    public filterNode(value, data): boolean {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    }


  }
</script>

<style scoped lang="less">
  .resource-role {
    position: absolute;
    left: 0;
    top: 0;
    right: 0;
    bottom: 0;
    z-index: 1000;
    /*padding: 15px;*/
    background-color: #fff;

    // 授权区域wrapper
    .outer-wrapper {
      width: 100%;
      height: 100%;
      display: flex;
      flex-direction: column;

      .wrapper {
        margin: 15px;
        flex: 1;
        box-shadow: 0 0 10px #cccccc;
        overflow: auto;
        display: flex;
        // 左侧菜单区域
        .left {
          flex: 2;
          background-color: #fff;
          padding: 15px;
          position: relative;

          .menu-btn {
            position: absolute;
            top: 15px;
            left: 15px;
            right: 0;
            z-index: 10;
            background-color: #fff;
          }

          // 菜单tree
          .menu-tree {
            height: 100%;
            width: 100%;
            padding-top: 40px;
            overflow: auto;
          }
        }

        // 右侧授权区域
        .right {
          flex: 9;
          background-color: #d1dbe5;
          padding: 15px;
          display: flex;

          .form-wrapper {
            flex: 1;
            box-shadow: 0 0 10px #ccc;
            padding: 15px;
            background-color: #fff;
            overflow: auto;
          }
        }

      }
    }
  }
</style>
