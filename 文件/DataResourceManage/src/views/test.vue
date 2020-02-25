<template>
  <div class="edit-role" @click="boxClick">
    <!--tree树以及操作-->
    <tree
      :show-checkbox="true"
      @check="handleCheck"
      ref="tree"
      title="配置权限">
      <!--左侧选择权限区域-->
      <template slot="content">
        <!--<dynamic-table-->
        <!--:headerData="dynamicHeaderData"-->
        <!--:tableData="dynamicTableData"-->
        <!--:paginationData="dynamicPaginationData"-->
        <!--:headerCheckBox="true"-->
        <!--:checkBox="true"-->
        <!--&gt;</dynamic-table>-->


        <div class="operate" style="display: flex;justify-content: space-between">
          <span class="btn" @click.stop.prevent="submit"><i class="iconfont icon-xinzeng"></i>确认</span>
        </div>
      </template>
    </tree>
    <!--操作-->
    <div>
      <input type="text" @keydown.esc="close" style="position: absolute;top: -10000px;" autofocus="autofocus"
             ref="editRoleInput">
      <span class="close" @click="close">
          <i class="el-icon-close"></i>
        </span>
    </div>
  </div>
</template>

<script lang="ts">
  import Vue from 'vue'
  import {Component} from 'vue-property-decorator'
  import {State} from 'vuex-class'
  import Tree from './Tree'


  @Component({components: {Tree}})
  export default class EditRole extends Vue {
    name: string = 'EditRole';
    @State pageSizes;
    treeCheckedArr: Array<any> = [];
    dynamicTableData: Array<any> = [
      {
        date: '2016-05-02',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄'
      }, {
        date: '2016-05-04',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1517 弄'
      }, {
        date: '2016-05-01',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1519 弄'
      }, {
        date: '2016-05-03',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1516 弄'
      }];
    dynamicHeaderData: Array<any> = [
      {
        prop: 'date',
        label: '日期',
        value: ''
      }, {
        prop: 'name',
        label: '姓名',
        value: ''
      }, {
        prop: 'address',
        label: '地址',
        value: ''
      }];
    dynamicPaginationData: Object = {
      currentPage: 1,
      pageSizes: this.pageSizes,
      pageSize: 10,
      total: 1
    };

    public mounted(): void {
      this.boxClick();
    }

    // 提交
    public submit(): void {

    }

    // 关闭模态框
    public close(): void {
      this.$emit('close')
    }

    public handleCheck(): void {
      this.treeCheckedArr = this.$refs.tree['getCheckedNodes'](false, true);
    }

    // 将数据从tree树转成扁平化
    public treeToBian(data, dat: Array<any>): Array<any> {
      dat = dat ? dat : [];
      if (data instanceof Array) {
        data.forEach(item => {
          dat.push(item);
          if (item.children.length) {
            this.treeToBian(item.children, dat);
          }
        });
      } else {
        dat.push(data);
        if (data.children.length) {
          this.treeToBian(data.children, dat);
        }
      }

      return dat;
    }

    // 点击模态框，让input获取焦点
    public boxClick(): void {
      this.$refs.editRoleInput['focus']();
    }
  }
</script>

<style scoped lang="less">
  .edit-role {
    position: absolute;
    left: 0;
    top: 0;
    right: 0;
    bottom: 0;
    z-index: 1000;
    background-color: #fff;
    // 关闭
    .close {
      position: absolute;
      top: 20px;
      right: 20px;
      width: 20px;
      height: 20px;
      border-radius: 50%;
      // background-color: rgba(0, 0, 0, 0.3);
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: center;
      color: #909399;

      .el-icon-close {
        font-size: 18px;
      }

      &:hover {
        color: #188bf5;
      }
    }
  }
</style>
