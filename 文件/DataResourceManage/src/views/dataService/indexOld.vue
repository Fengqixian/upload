<template>
  <div class="data-service">
    <div class="data-service-wrapper  sheets-container header-no-operate">
      <div class="tree-container server-list">
        <div class="tree">
          <span class="title top">服务列表<span class="el-icon-plus" @click="addEditServer"></span></span>
          <el-input
            v-model.trim="searchServerValue"
            placeholder="请输入关键词"
            suffix-icon="el-icon-search"></el-input>
          <div class="tree-scroll-wrapper">
            <el-tree
              class="filter-tree"
              :data="serverData"
              :filter-node-method="serverFilterNode"
              ref="serverTree"
              :props="defaultProps"
              @node-click="serverNodeClick"
            >
              <div class="tree-slot-div" slot-scope="{ node, data }">
                <span>{{data.name_cn}}</span>
                <span class="tree-icon">
                    <i class="edit-model el-icon-edit" @click.stop.prevent="addEditServer(data)"></i>
                    <i class="edit-model iconfont icon-shanchu" @click.stop.prevent="submitServer('delete',data)"></i>
                  </span>
              </div>
            </el-tree>
          </div>
        </div>
      </div>
      <transition name="fade">
        <data-tree
          :title="serverTreeActiveData.name_cn"
          @node-click="treeNodeClick"
          @cb-drag="handleDrag"
          @cb-drag-end="handleDragEnd"
          class="model-tree"
          v-if="serverTreeActiveData.id !== undefined"
        >
          <!--<template slot="content" v-if="serverTreeActiveData !== ''">-->
          <template slot="content" v-if="serverTreeActiveData !== ''">
            <div class="operate" style="display: flex;justify-content: space-between">
              <span class="btn" @click.stop.prevent="submitSQL"><i class="iconfont icon-xinzeng"></i>确认</span>
              <el-radio-group v-model.trim="SQLMode" size="mini">
                <el-radio-button label="拖拽拼接SQL"></el-radio-button>
                <el-radio-button label="手动输入SQL脚本"></el-radio-button>
              </el-radio-group>
            </div>
            <div class="table-wrapper" v-if="SQLMode === '手动输入SQL脚本'">
              <div class="sqlInfo">
                <div class="database-wrapper" v-if="sqlInfoDatabase.name !== ''">操作的数据库：{{sqlInfoDatabase.name}}</div>
                <el-input
                  type="textarea"
                  placeholder="请先选择数据库再输入内容SQL脚本"
                  v-model.trim="sqlInfo"
                  :rows="8"
                  show-word-limit>
                </el-input>
              </div>
            </div>
            <div class="table-wrapper" v-if="SQLMode === '拖拽拼接SQL'">
              <div class="column">
                <el-table
                  :data="columnData"
                  style="height: 100%"
                  border>
                  <el-table-column
                    prop="anotherName"
                    label="字段(中文)">
                  </el-table-column>
                  <el-table-column
                    prop="fieldName"
                    label="字段名">
                  </el-table-column>
                  <el-table-column
                    prop="alias"
                    label="別名"
                    width="120px">
                    <template slot-scope="scope">
                      <el-input v-model.trim="scope.row.alias" placeholder="请输入"></el-input>
                    </template>
                  </el-table-column>
                  <el-table-column
                    prop="tableName"
                    label="所属表">
                  </el-table-column>
                  <!--<el-table-column-->
                  <!--prop="calcWays"-->
                  <!--label="计算方式">-->
                  <!--</el-table-column>-->
                  <el-table-column
                    fixed="right"
                    label="操作"
                    width="120">
                    <template slot-scope="scope">
                      <el-button
                        @click.native.prevent="columnData=columnData.filter(item=>item.fieldName === scope.row.fieldName)"
                        type="text"
                        size="small">
                        移除
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
              <div class="table">
                <el-table
                  :data="tableData"
                  style="height: 100%"
                  border>
                  <el-table-column
                    prop="fieldNameM"
                    label="主表字段">
                  </el-table-column>
                  <el-table-column
                    prop="tableNameM"
                    label="主表">
                  </el-table-column>
                  <el-table-column
                    prop="aliasM"
                    label="别名"
                    width="120px">
                    <template slot-scope="scope">
                      <el-input v-model.trim="scope.row.aliasM" placeholder="请输入"></el-input>
                    </template>
                  </el-table-column>
                  <el-table-column
                    prop="fieldNameS"
                    label="关联表字段">
                  </el-table-column>
                  <el-table-column
                    prop="tableNameS"
                    label="关联表">
                  </el-table-column>
                  <el-table-column
                    prop="aliasS"
                    label="关联别名"
                    width="120px">
                    <template slot-scope="scope" v-if="scope.row.aliasS !== undefined">
                      <el-input v-model.trim="scope.row.aliasS" placeholder="请输入"></el-input>
                    </template>
                  </el-table-column>
                  <el-table-column
                    prop="linkMode"
                    label="链接方式"
                    width="120px">
                    <template slot-scope="scope" v-if="scope.row.linkMode !== undefined">
                      <el-select v-model.trim="scope.row.linkMode" filterable placeholder="请选择">
                        <el-option label="JOIN" value="JOIN"/>
                        <el-option label="INNER JOIN" value="INNER JOIN"/>
                        <el-option label="LEFT JOIN" value="LEFT JOIN"/>
                        <el-option label="RIGHT JOIN" value="RIGHT JOIN"/>
                      </el-select>
                    </template>
                  </el-table-column>
                  <el-table-column
                    fixed="right"
                    label="操作"
                    width="120">
                    <template slot-scope="scope">
                      <el-button
                        @click.native.prevent="tableData=tableData.filter(item=>item.fieldNameM === scope.row.fieldNameM)"
                        type="text"
                        size="small">
                        移除
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
              <div class="reference">
                <el-table
                  :data="referenceData"
                  style="height: 100%"
                  border>
                  <el-table-column
                    prop="fieldName"
                    label="参数字段">
                  </el-table-column>
                  <el-table-column
                    prop="parameterType"
                    label="参数类型">
                  </el-table-column>
                  <el-table-column
                    prop="parameterLength"
                    label="参数大小">
                  </el-table-column>
                  <el-table-column
                    prop="tableName"
                    label="参数所在表">
                  </el-table-column>
                  <el-table-column
                    prop="seatValue"
                    label="占位符"
                    width="120px">
                    <template slot-scope="scope">
                      <el-input type="text" v-model.trim="scope.row.seatValue" placeholder="请输入"></el-input>
                    </template>
                  </el-table-column>
                  <el-table-column
                    prop="queryMode"
                    label="查询方式"
                    width="120px">
                    <template slot-scope="scope">
                      <el-select v-model.trim="scope.row.queryMode" filterable placeholder="请选择">
                        <el-option value=">" label="大于"></el-option>
                        <el-option value="<" label="小于"></el-option>
                        <el-option value="<=" label="小于等于"></el-option>
                        <el-option value=">=" label="大于等于"></el-option>
                        <el-option value="!=" label="不等于"></el-option>
                        <el-option value="=" label="等于"></el-option>
                      </el-select>
                    </template>
                  </el-table-column>
                  <el-table-column
                    prop="linkMode"
                    label="链接方式"
                    width="120px">
                    <template slot-scope="scope">
                      <el-select v-model.trim="scope.row.linkMode" filterable placeholder="请选择">
                        <el-option label="AND" value="AND"/>
                        <el-option label="OR" value="OR"/>
                      </el-select>
                    </template>
                  </el-table-column>
                  <!--<el-table-column-->
                  <!--prop="groupRelation"-->
                  <!--label="组关联">-->
                  <!--</el-table-column>-->
                  <el-table-column
                    fixed="right"
                    label="操作"
                    width="120">
                    <template slot-scope="scope">
                      <el-button
                        @click.native.prevent="referenceData=referenceData.filter(item=>item.fieldName === scope.row.fieldName)"
                        type="text"
                        size="small">
                        移除
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </div>
          </template>
        </data-tree>
      </transition>
    </div>
    <el-dialog :title="serverForm.id?'修改'+ serverForm.nameCn:'新增服务'"
               :visible.sync="serverDialogTableVisible"
               :close-on-click-modal="false">
      <el-form :model="serverForm">
        <el-form-item label="中文" label-width="80px">
          <el-input v-model.trim="serverForm.nameCn" autocomplete="off" placeholder="中文"></el-input>
        </el-form-item>
        <el-form-item label="英文名" label-width="80px">
          <el-input v-model.trim="serverForm.nameEn" autocomplete="off" placeholder="英文"></el-input>
        </el-form-item>
        <el-form-item label="描述" label-width="80px">
          <el-input v-model.trim="serverForm.description" autocomplete="off" placeholder="描述"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="serverDialogTableVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitServer(serverForm?'edit': 'add',serverForm)">{{serverForm.id?'修 改': '确 定'}}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts">
  import Vue from 'vue'
  import {Component, Watch} from 'vue-property-decorator'
  import {State, Mutation} from 'vuex-class'
  import config from '../../config'
  import Qs from 'qs'
  import DataTree from '../../components/dataTree/index.vue';

  @Component({
    components: {DataTree}
  })
  export default class Index extends Vue {
    name: string = 'DataService';
    myThis: any = this
    @Mutation setLoadingFlag;
    columnData: Array<any> = [];
    tableData: Array<any> = [];
    referenceData: Array<any> = [];
    sqlInfo: string = '';
    // 手动输入SQL脚本时候选择的数据库
    sqlInfoDatabase: object = {
      name: '',
      id: ''
    };
    dragData: any = '';
    node: any = '';
    dropTable: any = '';
    // 服务tree搜索
    searchServerValue: string = '';
    // 服务数据
    serverData: Array<object> = [];
    // 服务新增对话框
    serverDialogTableVisible: boolean = false;
    // 服务新增修改form表单
    serverForm: object = {
      nameCn: '服务1',
      nameEn: 'server1',
      description: '描述1',
    };
    // 设置服务tree展示props
    defaultProps: object = {
      label: 'name_cn'
    };

    // 存储被点击的服务列表tree树id
    serverTreeActiveData: object = {};
    // SQL 切换
    SQLMode: string = '拖拽拼接SQL';


    public mounted(): void {
      this.getServerTreeData();
    }

    @Watch('searchServerValue')
    public searchServerValueChange(val): void {
      this.myThis.$refs.serverTree.filter(val);
    }

    @Watch('serverDialogTableVisible')
    public serverDialogTableVisibleChange(val): void {
      if (!val) {
        this.serverForm = {
          nameCn: '',
          nameEn: '',
          description: '',
        };
      }
    }

    // 获取SQL数据
    public getSQLData(): void {
      this.columnData = [];
      this.tableData = [];
      this.referenceData = [];
      this.sqlInfo = '';
      this.serverTreeActiveData['operationMode'] = 'add';
      this.serverTreeActiveData['operationModeSqlInfo'] = 'add';
      let url = 'http://192.168.6.91:8077/dataServer/selectDataServerDetail';
      var param = {
        jsonParam: JSON.stringify({
          serverId: this.serverTreeActiveData['id']
        })
      };

      this.myThis.$http.post(url, Qs.stringify(param)).then(res => {

        if (res.data === '') return;
        this.serverTreeActiveData['operationMode'] = 'edit';
        this.serverTreeActiveData['operationModeSqlInfo'] = 'edit';
        const {computeTable, parameter, tableField, sqlInfo} = res.data;
        computeTable.forEach(item => {
          this.tableData.push({
            aliasM: item.alias_m,
            aliasS: item.alias_s,
            dataSource: item.data_source,
            fieldId: item.field_id,
            fieldNameM: item.field_name_m,
            fieldNameS: item.field_name_s,
            id: item.id,
            linkMode: item.link_mode,
            serverId: item.server_id,
            sqlInfo: item.sql_info,
            status: item.status,
            tableNameM: item.table_name_m,
            tableNameS: item.table_name_s,
          })
        });
        parameter.forEach(item => {
          this.referenceData.push({
            fieldName: item.field_name,
            id: item.id,
            linkMode: item.link_mode,
            parameterLength: item.parameter_length,
            parameterType: item.parameter_type,
            queryMode: item.query_mode,
            seatValue: item.seatValue,
            serverId: item.server_id,
            tableId: item.table_id,
            tableName: item.table_name,
          })
        });
        tableField.forEach(item => {
          this.columnData.push({
            alias: item.alias,
            anotherName: item.another_name,
            fieldName: item.field_name,
            id: item.id,
            serverId: item.server_id,
            tableName: item.table_name,
          })
        })
        this.sqlInfo = sqlInfo;
        if (this.sqlInfo === '') {
          this.serverTreeActiveData['operationModeSqlInfo'] = 'add';
        }
        // this.serverData = res.data;
      })
    }

    // 服务列表tree点击事件
    public serverNodeClick(data): void {
      if (data.id === this.serverTreeActiveData['id']) {
        return;
      }
      // 将被点击的node的id存储到serverTreeActiveData上
      this.serverTreeActiveData = data;
      this.SQLMode = '拖拽拼接SQL';
      // this.sqlInfoDatabase = {};
      this.getSQLData();
    }


    // SQL組裝確認
    public submitSQL(): void {
      /*
      {
        tableField:[{
          anotherName:'',
          fieldName:'',
          tableName:'',
          serverId:''
        },{
          anotherName:'',
          fieldName:'',
          tableName:'',
          serverId:''
        }],
        computeTable:[{
          fieldNameM:'',
          tableNameM:'',
          aliasM:'',
          fieldNameS:'',
          tableNameS:'',
          aliasS:'',
          linkMode:'',
          dataSource:'',
          serverId:''
        }],
        parameter:[{
          fieldName:'',
          parameterType:'',
          parameterLength:'',
          tableName:'',
          seatValue:'',
          queryMode:'',
          linkMode:''
        }],
        operationMode:''
      }
       */
      if (this.SQLMode === '拖拽拼接SQL') {
        let data: object = {
          tableField: [],
          computeTable: [],
          parameter: [],
          operationMode: this.serverTreeActiveData['operationMode']
        };
        this.columnData.forEach(item => {
          item.serverId = this.serverTreeActiveData['id'];
          data['tableField'].push(item)
        });
        this.tableData.forEach(item => {
          item.serverId = this.serverTreeActiveData['id'];
          data['computeTable'].push(item)
        });
        this.referenceData.forEach(item => {
          item = JSON.parse(JSON.stringify(item));
          item.serverId = this.serverTreeActiveData['id'];
          // item.seatValue = item.seatValue;
          data['parameter'].push(item)
        });
        let url = 'http://192.168.6.91:8077/dataServer/saveOrUpdateParam';

        var param = {};
        param['jsonParam'] = JSON.stringify(data);
        this.myThis.$http.post(url, Qs.stringify(param)).then(res => {

          if (res.data === 'success') {
            this.getSQLData();
            this.myThis.$message.success('提交成功')
          } else {
            this.myThis.$message.error('提交失败')
          }
        })
      } else if (this.SQLMode === '手动输入SQL脚本') {
        if (this.sqlInfoDatabase['id'] === '') {
          this.myThis.$message.warning('请先选择数据库再提交');
          return;
        }
        let data: object = {
          sqlInfo: this.sqlInfo,
          serverId: this.serverTreeActiveData['id'],
          operationMode: this.serverTreeActiveData['operationModeSqlInfo'],
          dataSource: this.sqlInfoDatabase['id']
        };
        // sqlInfo
        let url = 'http://192.168.6.91:8077/dataServer/saveOrUpdateSql';

        var param = {};
        param['jsonParam'] = JSON.stringify(data);
        this.myThis.$http.post(url, Qs.stringify(param)).then(res => {

          // if (res.data.status === '200') {
          if (res.data === 'success') {
            this.myThis.$message.success('提交成功');
            this.getSQLData();
          } else {
            this.myThis.$message.error('提交失败')
          }
        })
      }

    }

    // 获取服务数据
    public getServerTreeData(): void {
      let url = 'http://192.168.6.91:8077/dataServer/selectDataServer';

      this.myThis.$http.post(url).then(res => {

        this.serverData = res.data;
      })
    }

    // 提交server名称
    public submitServer(operation?, data?): void {
      // let URL = config.port('dataServer') + 'saveOrUpdateServer';
      let URL = 'http://192.168.6.91:8077/dataServer/saveOrUpdateServer';
      const pageAjax: object = {
        operationMode: operation,
        dataServer: this.serverForm
      };
      if (operation !== 'add') {
        pageAjax['dataServer']['id'] = data['id']
      }


      var param = {};
      param['jsonParam'] = JSON.stringify(pageAjax);
      this.myThis.$http.post(URL, Qs.stringify(param)).then(res => {

        this.serverDialogTableVisible = false;
        if (res.data === 'success') {
          this.myThis.$message.success('操作成功');
          this.getServerTreeData()
        } else {
          this.myThis.$message.error('操作失败')
        }
      })
    }

    // 刪除服務
    public delServer(data): void {

    }

    // 修改或者新增服务
    public addEditServer(data): void {
      this.serverDialogTableVisible = !this.serverDialogTableVisible;
      // 新增
      if (data === undefined) {

      } else {
        this.serverForm = {
          description: data.description,
          id: data.id,
          nameCn: data.name_cn,
          nameEn: data.name_en,
        };
      }
    }

    // 服务搜索过滤
    public serverFilterNode(value, data): boolean {
      if (!value) return true;
      return data.name_en.indexOf(value) !== -1;
    }

    // 拖动
    public handleDrag(node, nodeData, cbDragDom, el): void {
      this.dragData = nodeData;
      this.node = node;
      const columnDom: any = document.querySelector('.column');
      const referenceDom: any = document.querySelector('.reference');
      const tableDom: any = document.querySelector('.table');
      this.dragToTableStyle(columnDom, el, cbDragDom);
      this.dragToTableStyle(referenceDom, el, cbDragDom);
      this.dragToTableStyle(tableDom, el, cbDragDom);

    }

    // 拖动进入table中 样式变换
    public dragToTableStyle(dom, el, cbDragDom?): void {
      if (dom !== null) {
        if (this.tableBoxPosition(dom, el)) {
          this.addTableDropStyle(dom);
          setTimeout(_ => {
            cbDragDom.addEventListener('mouseup', this.dragDataToTable, false)
            this.dropTable = dom
          }, 1)
        } else {
          dom.removeAttribute('style');
          cbDragDom.removeEventListener('mouseup', this.dragDataToTable, false)
          this.dropTable = ''
        }
      }
    }

    // 拖动将数据添加到table中
    public dragDataToTable(): void {
      const name: string = this.dropTable.getAttribute('class')
      switch (name) {
        case 'column':
          this.dragDataToColumnTable();
          break;
        case 'table':
          this.dragDataToTableTable();
          break;
        case 'reference':
          this.dragDataToReferenceTable();
          break;
      }
    }

    // 将数据添加到字段table
    public dragDataToColumnTable(): void {
      const data = this.node.data;
      /*
      if (this.repeatData(this.columnData, data.tree_node_key)) {
        this.myThis.$message.warning('数据添加重复');
        return;
      }
       */
      this.columnData.push({
        // 字段中文名
        anotherName: data.remarks ? data.remarks.meta_value : '',
        // 字段名
        fieldName: data.name,
        // 所属表
        tableName: this.node.parent.parent.data.name,
        // 计算方式
        // calcWays: '',
        // 别名
        alias: '',
        tree_node_key: data.tree_node_key,
      });

    }

    // 将数据添加到主表table
    public dragDataToTableTable(): void {
      const data = this.node.data;
      /*
      if (this.repeatData(this.tableData, data.tree_node_key)) {
        this.myThis.$message.warning('数据添加重复');
        return;
      }
       */
      // 添加主表信息
      const tableData = this.tableData[this.tableData.length - 1];
      if (tableData === undefined || (tableData['fieldNameS'] !== undefined && tableData['fieldNameM'] !== undefined) || (tableData['fieldNameM'] === undefined)) {
        this.tableData.push({
          // 主表字段
          fieldNameM: data.name,
          // 主表
          tableNameM: this.node.parent.parent.data.name,
          // 别名
          aliasM: '',
          aliasS: undefined,
          // 链接方式
          linkMode: '',
          tree_node_key: data.tree_node_key,
          dataSource: this.node.parent.parent.parent.parent.data.database_id.meta_value_id
        })
      } else if (tableData['fieldNameS'] === undefined) {
        // 判断关联表和主表不能在一个表中
        if (tableData.tableNameM === this.node.parent.parent.data.name) {
          this.myThis.$message.warning('主表和关联表不能是同一张表');
          return;
        }
        // 关联表字段
        tableData.fieldNameS = data.name;
        // 关联表
        tableData.tableNameS = this.node.parent.parent.data.name;
        // 关联别名
        tableData.aliasS = '';
        tableData.tree_node_key = tableData.tree_node_key + data.tree_node_key;
        this.tableData.splice(this.tableData.length - 1, 1, tableData)
      }
    }

    // 将数据添加到参数table
    public dragDataToReferenceTable(): void {
      const data = this.node.data;
      /*
       if (this.repeatData(this.referenceData, data.tree_node_key)) {
         this.myThis.$message.warning('数据添加重复');
         return;
       }
       */
      let dat: Array<object> = [...this.tableData, ...this.columnData];
      const flag = dat.find(item => {
        return item['tableNameM'] === this.node.parent.parent.data.name || item['tableNameS'] === this.node.parent.parent.data.name || item['tableName'] === this.node.parent.parent.data.name;
      });
      if (flag === undefined) {
        this.myThis.$message.warning('请从字段表和主表关联表选取');
        return;
      }
      this.referenceData.push({
        // 参数字段
        fieldName: data.name,
        // 参数类型
        parameterType: data.type_name.meta_value,
        // 参数大小
        parameterLength: data.column_size.meta_value,
        // 参数所在表
        tableName: this.node.parent.parent.data.name,
        // 占位符
        seatValue: '',
        // 查询方式
        queryMode: '',
        // 链接方式
        linkMode: '',
        // 组关联
        // groupRelation: '',
        tree_node_key: data.tree_node_key,
      });
    }

    // 判断数据是否重复添加
    public repeatData(tableData: Array<any>, key): boolean {
      const flag = tableData.find(item => {
        return item.tree_node_key.indexOf(key) !== -1
      });
      return flag !== undefined
    }

    // 拖动结束
    public handleDragEnd(el): void {
      const columnDom: any = document.querySelector('.column');
      const referenceDom: any = document.querySelector('.reference');
      const tableDom: any = document.querySelector('.table');
      this.removeTableDropStyle(columnDom);
      this.removeTableDropStyle(referenceDom);
      this.removeTableDropStyle(tableDom);
      this.dragData = '';
      this.node = '';
    }

    // tree点击
    public treeNodeClick(data, node): void {
      if (this.SQLMode === '手动输入SQL脚本' && node.level === 4) {
        this.sqlInfoDatabase['id'] = data.database_id.meta_value_id;
        this.sqlInfoDatabase['name'] = data.name
      }
    }

    // 判断拖动是否进入table中
    public tableBoxPosition(dom, mousePosition): boolean {
      const rect: object = dom.getBoundingClientRect();
      const x: number = rect['x'] || rect['left'];
      const y: number = rect['y'] || rect['top'];
      const width: number = rect['width'];
      const height: number = rect['height'];
      const mousex = mousePosition['clientX'];
      const mousey = mousePosition['clientY'];
      // const mousex = mousePosition['x'];
      // const mousey = mousePosition['y'];
      return mousex >= x && mousex <= (width + x) && mousey >= y && mousey <= (y + height)
    }

    // table添加样式
    public addTableDropStyle(dom): void {
      const style = 'transform: scale(1.02);box-shadow: 0 0 10px #000;';
      if (dom !== null) {
        dom.setAttribute('style', style);
      }
    }

    // table移除样式
    public removeTableDropStyle(dom): void {
      if (dom !== null) {
        dom.removeAttribute('style');
      }
    }


  }
</script>

<style lang="less">
  .data-service {
    .model-tree {
      flex: 1;

      .tree-container .tree {
        background-color: #474f6b;

        .el-input input {
          background-color: #3c4258;
        }
      }
    }

    .sqlInfo {
      .database-wrapper {
        padding: 10px;
        border-radius: 4px;
        box-shadow: 0 0 2px #333;
        font-size: 16px;
        font-weight: 400;
        margin-bottom: 20px;
        background-color: #fff;
      }
    }

    .data-service-wrapper {
      display: flex;

      .fade-enter-active, .fade-leave-active {
        transition: opacity .5s;
      }

      .fade-enter, .fade-leave-to /* .fade-leave-active below version 2.1.8 */ {
        opacity: 0;
      }

      .server-list {
        height: 100%;
        position: relative;
        width: 20rem;

        .tree {
          right: 0 !important;
          width: initial;
        }

      }

      .top {
        height: 3rem;
        width: 100%;
        display: flex !important;
        align-items: center;
        justify-content: space-between;
        padding: 0 1rem;

        .el-icon-plus {
          cursor: pointer;
          width: 2rem;
          height: 2rem;
          line-height: 2rem;
          text-align: center;
        }
      }
    }

    .table-wrapper {
      height: 100%;
      width: 100%;
      padding: 10px;
      /*display: flex;*/
      /*flex-direction: column;*/

      & > div {
        flex: 1;
        margin: 10px;
      }

      .column, .reference, .table {
        transition: all 0.2s;
      }
    }
  }


</style>
