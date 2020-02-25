<template>
  <div class="mappingMethod">
    <tree
      @node-click="handleNodeClick">
      <template slot="content">
        <div class="operate" style="display: flex;justify-content: space-between">
          <span class="btn" @click.stop.prevent="submit"><i class="iconfont icon-xinzeng"></i>确认</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="table-wrapper" :class="{'is-active':tableActive === 'source'}">
              <div class="header">
                <span class="title">映射源 {{sourceNodeData === null?'':'-'+sourceNodeData.name_cn}}</span>
                <el-button class="btn" type="text" @click="tableActive='source'">选择</el-button>
              </div>
              <div class="body">
                <dynamic-table
                  ref="sourceDynamicTable"
                  :radio="true"
                  :headerData="sourceHeaderData"
                  :tableData="sourceTableData"
                  :paginationData="sourcePaginationData"
                  @sizeChange="tableSizeChange('source',arguments)"
                  @currentChange="tableCurrentChange('source',arguments)"></dynamic-table>
              </div>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="table-wrapper" :class="{'is-active':tableActive === 'target'}">
              <div class="header">
                <span class="title">映射目标{{targetNodeData === null?'':'-'+targetNodeData.name_cn}}</span>
                <el-button class="btn" type="text" @click="tableActive='target'">选择</el-button>
              </div>
              <div class="body">
                <dynamic-table
                  ref="targetDynamicTable"
                  :checkBox="true"
                  :headerData="targetHeaderData"
                  :tableData="targetTableData"
                  :paginationData="targetPaginationData"
                  @sizeChange="tableSizeChange('target',arguments)"
                  @currentChange="tableCurrentChange('target',arguments)">
                </dynamic-table>
              </div>
            </div>
          </el-col>
        </el-row>
      </template>
    </tree>
  </div>
</template>

<script lang="ts">
  import Vue from 'vue'
  import {Component, Watch} from 'vue-property-decorator'
  import Tree from './Tree'
  import config from '../../config'
  import Qs from 'qs'
  import {State, Mutation} from 'vuex-class'

  @Component({
    components: {Tree}
  })
  export default class Index extends Vue {
    name: string = 'Index';
    @State pageSizes;
    @Mutation setLoadingFlag;
    tableActive: string = 'source';
    public tableData: Array<any> = [];
    public sourceHeaderData: Array<any> = [];
    public sourceTableData: Array<any> = [];
    public sourcePageSize: number = 10;
    public sourceCurrentPage: number = 1;
    public sourceNodeData: any = null;
    public sourcePaginationData: object = {
      currentPage: this.sourceCurrentPage,
      pageSizes: this.pageSizes,
      pageSize: this.sourcePageSize,
      total: 1
    };
    public targetHeaderData: Array<any> = [];
    public targetTableData: Array<any> = [];
    public targetPageSize: number = 10;
    public targetCurrentPage: number = 1;
    public targetNodeData: any = null;
    public targetPaginationData: object = {
      currentPage: this.targetCurrentPage,
      pageSizes: this.pageSizes,
      pageSize: this.targetPageSize,
      total: 1
    };

    // 确认提交
    public submit(): void {
      let URL = config.port('metadatamapping') + 'saveMappingInfo';
      if (this.$refs.sourceDynamicTable['getSelect']().length === 0) {
        this.$message.warning('请选择映射源');
        return;
      } else if (this.$refs.targetDynamicTable['getSelect']().length === 0 || this.sourceNodeData === null) {
        this.$message.warning('请选择映射目标');
        return;
      } else if (this.$refs.targetDynamicTable['getSelect']().length === 0 || this.targetNodeData === null) {
        this.$message.warning('请选择映射目标');
        return;
      }
      let params = {
        data: [{
          sourceData: [this.$refs.sourceDynamicTable['getSelect']()],
          targetData: this.$refs.targetDynamicTable['getSelect'](),
          sourceModule: this.sourceNodeData,
          targetModule: this.targetNodeData
        }],
        pageAjax: null
      };

      this.$http.post(URL, params).then(response => {

        const res = response.data;
        if (res.status !== '200') {
          this.$message.error(res.message);
          return;
        }
        this.$message.success(res.message);
        this.$refs.targetDynamicTable['clearSelection']();
      })
    }

    // 当前页面发生变化
    public tableCurrentChange(target, data): void {
      const currentPage = data[0].paginationData.currentPage;
      // 映射源
      if (target === 'source') {
        this.sourceCurrentPage = currentPage;

      }
      // 映射目标
      else if (target === 'target') {
        this.targetCurrentPage = currentPage;
      }
      this.getSourceData(data, true, target)
    }

    // 当前页面展示数据量发生变化
    public tableSizeChange(target, data): void {
      const pageSize = data[0].paginationData.pageSize;
      // 映射源
      if (target === 'source') {
        this.sourcePageSize = pageSize
      }
      // 映射目标
      else if (target === 'target') {
        this.targetPageSize = pageSize
      }

      this.getSourceData(data, true, target)
    }

    // tree树点击节点
    public handleNodeClick(data, node, component): void {
      this.sourceCurrentPage = 1;
      this.targetCurrentPage = 1;
      if (this.tableActive === 'source') {
        this.sourceNodeData = data
      } else if (this.tableActive === 'target') {
        this.targetNodeData = data
      }
      this.getSourceData(data)
    }

    // 获取映射源数据数据
    public async getSourceData(data, page?, tableName?) {
      /*
      let pageAjax = {
        data: [data['id']],
        pageAjax: {
          currentPage: data.paginationData['currentPage'],
          pageSize: data.paginationData['pageSize'],
          total: data.paginationData['total'],
          pageSizes: data.paginationData['pageSizes'],
        }
      }
       */
      let pageAjax: any = {};
      let headerData: Array<any> = [];
      let tableData: Array<any> = [];
      let paginationData: object = {};

      if (tableName === 'source' || this.tableActive === 'source') {
        if (page) data = this.sourceNodeData;
        headerData = this.sourceHeaderData;
        tableData = this.sourceTableData;
        paginationData = this.sourcePaginationData;
        pageAjax = {
          pageAjax: {
            currentPage: this.sourceCurrentPage,
            pageSize: this.sourcePageSize,
            total: 1,
            pageSizes: this.pageSizes,
          }
        };
      } else if (tableName === 'target' || this.tableActive === 'target') {
        if (page) data = this.targetNodeData;
        headerData = this.targetHeaderData;
        tableData = this.targetTableData;
        paginationData = this.targetPaginationData;
        pageAjax = {
          pageAjax: {
            currentPage: this.targetCurrentPage,
            pageSize: this.targetPageSize,
            total: 1,
            pageSizes: this.pageSizes,
          }
        };
      } else {
        this.$message.warning('请选择映射类型');
        return;
      }
      const dat: any = {moduleName: data['name_en']};
      pageAjax['data'] = [dat];
      if (page) {
      } else {

        await this.getModuleAttList(data).then(response => {

          const res = response.data;
          if (res.status !== '200') {
            this.$message.error('获取头部信息出错');
            return
          }
          headerData.splice(0, headerData.length);
          const header = JSON.parse(res.data);
          header.forEach(item => {
            headerData.push({
              prop: item.att_name_en,
              label: item.att_name_cn,
            })
          });
        });
      }

      let URL = config.port('metadatamapping') + 'getMetaDataTree';

      this.$http.post(URL, pageAjax).then(response => {

        const {currentPage, pageSize, tableData: dataList, total} = response.data;
        tableData.splice(0, tableData.length);
        dataList.forEach(item => {
          tableData.push(item);
        });
        paginationData['currentPage'] = currentPage;
        paginationData['pageSize'] = pageSize;
        paginationData['total'] = total
      });
    }

    // 获取table头部信息
    public async getModuleAttList(data) {
      let URL = config.port('metadatamapping') + 'getModuleAttList';
      let dat = {
        moduleId: data.id
      };
      let pageAjax = {
        data: [dat],
        pageAjax: null
      };
      return this.$http.post(URL, pageAjax)
    }
  }
</script>

<style scoped lang="less">
  .mappingMethod {
    .el-row {
      margin: 0 !important;
    }

    .table-wrapper {
      margin: 10px;
      transition: all 0.3s;
      box-shadow: 0 0 5px #565656;
      background-color: #fff;
      border-radius: 4px;

      &.is-active {
        transform: scale(1.01);
        box-shadow: 0 0 5px #188bf5;
      }

      .header {
        padding: 10px;
        border-bottom: 1px solid #ebeef5;

        .btn {
          float: right;
        }

        .title {
          font-size: 17px !important;
          font-weight: 600;
        }
      }

      .body {
        padding: 10px;
      }
    }
  }
</style>
