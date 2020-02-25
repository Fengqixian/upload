<template>
  <div class="mappingMethod">
    <tree
      @node-click="handleNodeClick"
      @tree-radio-change="treeRadioChange"
      ref="tree">
      <template slot="content">
        <div class="operate" v-if="treeRadio === '待审核'">
          <span class="btn" @click.stop.prevent="submit('1')" style="color: #67C23A;">通过</span>
          <span class="btn" @click.stop.prevent="submit('2')" style="color: #E6A23C">不通过</span>
          <span class="btn" @click.stop.prevent="deleteMappingInfo" style="color: #F56C6C">删除</span>
          <el-input v-model.trim="mappingSearchVal" placeholder="请输入搜索映射源" style="width: 150px;"
                    @keydown.native.enter="searchSource"></el-input>
          <el-button style="margin-left: 10px;" @click="searchSource">搜 索</el-button>
        </div>
        <el-row :gutter="20">
          <el-col :span="12">
            <transition name="el-fade-in">
              <div class="table-wrapper" :class="{'is-active':tableActive === 'source'}" v-if="sourceHeaderData.length">
                <div class="header">
                  <span class="title">映射源{{sourceNodeData === null?'':'-'+sourceNodeData.name_cn}}</span>
                  <!--<el-button class="btn" type="text" @click="tableActive='source'">选择</el-button>-->
                </div>
                <div class="body">
                  <dynamic-table
                    ref="sourceDynamicTable"
                    :radio="true"
                    :headerData="sourceHeaderData"
                    :tableData="sourceTableData"
                    :paginationData="sourcePaginationData"
                    @sizeChange="tableSizeChange('source',arguments)"
                    @currentChange="tableCurrentChange('source',arguments)"
                    @current-change="sourceCurrentChange"></dynamic-table>
                </div>
              </div>
            </transition>
          </el-col>
          <el-col :span="12">
            <div class="table-wrapper" :class="{'is-active':tableActive === 'target'}" v-if="targetResponseData">
              <div
                class="target-table"
                v-for="(table , index) in targetResponseData"
                :key="index"
                v-if="table.dataList.length">
                <div class="header">
                  <span class="title">映射目标-{{table.moduleInfo[0].name_cn}}</span>
                </div>
                <div class="body">
                  <el-table
                    :data="table.dataList"
                    @selection-change="targetSelectionChange(table.multipleSelection,arguments)">
                    <el-table-column
                      type="selection"
                      width="55"
                      v-if="treeRadio === '待审核'">
                    </el-table-column>
                    <el-table-column
                      v-for="header in table.attInfo"
                      :key="header.id"
                      :prop="header.att_name_en"
                      :label="header.att_name_cn">
                    </el-table-column>
                  </el-table>
                </div>
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
    tableActive: string = '';
    public tableData: Array<any> = [];
    public sourceHeaderData: Array<any> = [];
    public sourceTableData: Array<any> = [];
    public sourcePageSize: number = 10;
    public sourceCurrentPage: number = 1;
    public sourceNodeData: any = null;
    // 存放映射源table被点击的的行信息
    public sourceCurrentChangeData: any = null;
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
    public targetResponseData: any = null;
    public targetPaginationData: object = {
      currentPage: this.targetCurrentPage,
      pageSizes: this.pageSizes,
      pageSize: this.targetPageSize,
      total: 1
    };
    // tree 树 radio选择的值
    treeRadio: string = '';

    // 搜索映射源
    mappingSearchVal: string = '';

    // 搜索
    public searchSource(): void {
      if (this.sourceNodeData === null) {
        this.$message.warning('请选择映射源');
        return;
      }
      ;

      this.sourceCurrentPage = 1;
      this.targetCurrentPage = 1;
      // 将tree node节点数据缓存下来
      this.getMappingSourceDataList(this.sourceNodeData, false, this.mappingSearchVal);
    }

    // 删除
    public async deleteMappingInfo() {
      let targetData: Array<any> = [];
      this.targetResponseData.forEach(item => {
        if (item.multipleSelection.length) {
          let targetDataId: Array<string> = [];
          item.multipleSelection.forEach(ite => {
            targetDataId.push(ite.row_id);
          });
          targetData.push({
            targetModuleId: item.moduleInfo[0].id,
            targetDataId
          })
        }
      });

      if (this.sourceNodeData === null || this.sourceCurrentChangeData === null) {
        this.$message.warning('请选择映射源');
        return;
      } else if (targetData.length === 0) {
        this.$message.warning('请选择映射目标');
        return;
      }
      let URL = config.port('metadatamapping') + 'deleteMappingInfo';
      let params = {
        data: [{
          sourceModuleId: this.sourceNodeData.id,
          sourceModuleName: this.sourceNodeData.name_en,
          sourceDataId: this.sourceCurrentChangeData.row_id,
          targetData
        }],
        pageAjax: null
      };

      await this.$http.post(URL, params).then(response => {

        const res = response.data;
        if (res.status === '200') {
          this.$message.success(res.message);
          this.getMappingSourceDataList(this.sourceNodeData);
          this.getMappingTargetDataList(this.sourceCurrentChangeData);
          this.$refs.tree['getTreeData'](this.treeRadio);
        } else {
          this.$message.error(res.message)
        }
      });
    }


    // 确认提交
    public async submit(status) {
      let targetData: Array<any> = [];
      this.targetResponseData.forEach(item => {
        if (item.multipleSelection.length) {
          let targetDataId: Array<string> = [];
          item.multipleSelection.forEach(ite => {
            targetDataId.push(ite.row_id);
          });
          targetData.push({
            targetModuleId: item.moduleInfo[0].id,
            targetDataId
          })
        }
      });

      if (this.sourceNodeData === null || this.sourceCurrentChangeData === null) {
        this.$message.warning('请选择映射源');
        return;
      } else if (targetData.length === 0) {
        this.$message.warning('请选择映射目标');
        return;
      }
      let URL = config.port('metadatamapping') + 'auditMappingInfo';
      let params = {
        data: [{
          status,
          sourceModuleId: this.sourceNodeData.id,
          sourceModuleName: this.sourceNodeData.name_en,
          sourceDataId: this.sourceCurrentChangeData.row_id,
          targetData
        }],
        pageAjax: null
      };

      await this.$http.post(URL, params).then(response => {

        const res = response.data;
        if (res.status === '200') {
          this.$message.success(res.message);
          this.getMappingSourceDataList(this.sourceNodeData);
          this.getMappingTargetDataList(this.sourceCurrentChangeData);
          this.$refs.tree['getTreeData'](this.treeRadio);
        } else {
          this.$message.error(res.message)
        }
      });

    }

    // 点击映射目标checkbox
    public targetSelectionChange(multipleSelection, val): void {
      multipleSelection.splice(0, multipleSelection.length);
      val[0].forEach(item => {
        multipleSelection.push(item)
      });
    }

    // 点击映射源table
    public sourceCurrentChange(data): void {
      // 提交成功之后 单选table 数据重新刷新，导致当前被选中的table发生变化
      // 这里解决发生变化之后 传来data为null出现的bug
      if (data) {
        this.sourceCurrentChangeData = data;
        this.getMappingTargetDataList(data);
      }
    }


    // 获取映射目标数据
    public getMappingTargetDataList(data): void {
      let URL = config.port('metadatamapping') + 'getMappingTargetDataList';
      let status: string = '';
      switch (this.treeRadio) {
        case '待审核':
          status = '0';
          break;
        case '已审核':
          status = '1';
          break;
        case '全部':
          status = '';
          break;
      }
      const dat: any = {
        status,
        sourceModuleId: this.sourceNodeData.id,
        sourceModuleName: this.sourceNodeData.name_en,
        sourceDataId: data.row_id
      };
      let params = {
        data: [dat],
        // pageAjax: {
        //   currentPage: this.targetCurrentPage,
        //   pageSize: this.targetPageSize,
        //   total: 1,
        //   pageSizes: this.pageSizes,
        // }
        pageAjax: null
      };

      this.$http.post(URL, params).then(response => {

        const res = response.data;
        // if (res.code !== '200') {
        //   this.$message.error(res.message);
        //   return;
        // }
        // 将映射目标的数据缓存到targetResponseData
        this.targetResponseData = res.tableData;
        this.targetResponseData.map(item => {
          return item.multipleSelection = [];
        })
      })
    }

    // 当前页面发生变化
    public tableCurrentChange(target, data): void {
      const currentPage = data[0].paginationData.currentPage;
      // 映射源
      if (target === 'source') {
        this.sourceCurrentPage = currentPage;
        this.getMappingSourceDataList(data, true)
      }
      // 映射目标
      else if (target === 'target') {
        this.targetCurrentPage = currentPage;
      }
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

      this.getMappingSourceDataList(data, true)
    }

    // tree树点击节点
    public handleNodeClick(data, node, component): void {
      this.sourceCurrentPage = 1;
      this.targetCurrentPage = 1;
      // 将tree node节点数据缓存下来
      this.sourceNodeData = data;
      this.getMappingSourceDataList(data)
    }

    // tree 树 radio change
    public treeRadioChange(val): void {
      this.treeRadio = val;
      this.sourceCurrentPage = 1;
      this.targetCurrentPage = 1;
      this.sourceTableData = [];
      this.sourceHeaderData = [];
      this.targetResponseData = [];
    }

    // 获取映射源数据数据
    public async getMappingSourceDataList(data, page?, searchValue?) {
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
      let status: string = '';
      switch (this.treeRadio) {
        case '待审核':
          status = '0';
          break;
        case '已审核':
          status = '1';
          break;
        case '全部':
          status = '';
          break;
      }
      const dat: any = {
        status,
        sourceModuleId: data.id,
        sourceModuleName: data.name_en,
        searchValue: searchValue ? searchValue : ''
      };
      pageAjax['data'] = [dat];
      if (!page && !searchValue) {

        await this.getModuleAttList(data).then(response => {

          const res = response.data;
          if (res.status !== '200') {
            this.$message.error(res.message);
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

      let URL = config.port('metadatamapping') + 'getMappingSourceDataList';

      this.$http.post(URL, pageAjax).then(response => {

        const {currentPage, pageSize, tableData: dataList, total} = response.data;
        tableData.splice(0, tableData.length);
        dataList.forEach(item => {
          tableData.push(item);
        });
        paginationData['currentPage'] = currentPage;
        paginationData['pageSize'] = pageSize;
        paginationData['total'] = total
        // 选中提交之前单行table选择的一行
        if (this.sourceCurrentChangeData) {
          this.$refs.sourceDynamicTable['setCurrentRow'](this.sourceCurrentChangeData.row_id, 'row_id')
        }
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
