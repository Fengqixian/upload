<template>
  <div>
    <el-container>
      <div id="editModel">
        <el-row style="margin-bottom: 1rem;">
          <el-col :span="6">
            <el-form ref="tableForm" label-width="50px" v-if="table === ''">
              <el-form-item label="表名">
                <el-input v-model.trim="tableName" placeholder="请输入表名"></el-input>
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>
        <el-table :data="tableData" border stripe max-height="300" @row-click="rowClick">
          <el-table-column prop="列中文名" label="列中文名">
            <template slot-scope="scope">
              <el-input :value="scope.row.description" v-model.trim='scope.row.description'></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="列名" label="列名">
            <template slot-scope="scope">
              <el-input :value="scope.row.name" v-model.trim='scope.row.name'></el-input>
            </template>
          </el-table-column>
          <el-table-column label="数据类型">
            <template slot-scope="scope">
              <el-input placeholder="输入长度" v-model.trim="scope.row.length" class="input-with-select">
                <el-select placeholder="选择类型" slot="prepend" v-model.trim='scope.row.type'>
                  <template>
                    <el-option
                      v-for="(op ,index) in dictList"
                      :key="op.key"
                      :label="op.key"
                      :value="op.value">
                    </el-option>
                  </template>
                </el-select>
              </el-input>
            </template>
          </el-table-column>
          <el-table-column label="操作" :width="160">
            <template slot-scope="scope">
              <el-button size="small" type="success" plain @click="handleAdd()">增加</el-button>
              <el-button size="small" type="danger" plain @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div id="editForm" style="margin-top: 20px;">
        <el-form ref="form" border label-width="120px" height="150">
          <el-row :gutter="20">
            <template v-for="(form ,index) in formDataList">
              <el-col :span="8"
                      v-if="form.codeType!=='length' & form.codeType!=='type'" style="height: 30px;">
                <el-form-item
                  :label="form.description">
                  <el-select :placeholder="form.description" v-if="form.codeType!=='pk_tablename'"
                             v-model.trim="form.typeValue" filterable
                             style="width:200px">
                    <template v-for="(key,value) in form.dictValues">
                      <el-option :label="key" :value="value">
                      </el-option>
                    </template>
                  </el-select>

                  <el-select :placeholder="form.description" v-if="form.codeType==='pk_tablename'"
                             v-model.trim="form.typeValue" filterable
                             style="width:200px" @change="onSelectChange">
                    <template v-for="(key,value) in form.dictValues">
                      <el-option :label="key" :value="value">
                      </el-option>
                    </template>
                  </el-select>

                  <!--<div v-if="form.codeType=='length'">
                      <el-input placeholder="输入长度" v-model.trim="form.typeValue" style="width:130px"/>
                  </div>-->
                </el-form-item>
              </el-col>
            </template>
          </el-row>
        </el-form>
      </div>

      <el-footer>
        <div class="btn" style="float: right;">
          <el-button icon="el-icon-close" @click="close(false)" plain>取消</el-button>
          <el-button type="primary" icon="el-icon-check" @click="handleSave()">保存</el-button>
        </div>
      </el-footer>
    </el-container>
  </div>
</template>

<script lang="ts">

  import Vue from 'vue'
  import {Component, Prop} from 'vue-property-decorator'
  import config from '../../config.ts'
  import Qs from 'qs'
  import {State, Mutation} from 'vuex-class'

  @Component({
    name: "editModel",
    // props: ['table']
  })

  export default class editModel extends Vue {
    myThis: any = this
    @State loadingFlag
    @Mutation setLoadingFlag
    @Prop() table

    tableName: any = ""

    //this.tableName=this.$route.params.tableName
    //常量集合
    dictList: any = [];

    //数据集合
    tableData: any = null

    //获取某一个字段的所有属性集合
    formDataList: any = null

    //存储所有默认属性集合 attid为空时的默认值
    defaultDataList: any

    //存储所有属性字段的id和属性集合 缓存,用于加载修改后的form表单
    metaDataList: any = []

    //切换选中时上一个选中的tagId
    lastTagId: any = null

    //记录当前点击的字段attid,用于将最后一次点击的字段，添加到缓存
    nowTagId: any = "";

    //初始化
    created() {
      this.tableName = this.table
      this.getMetaTypeList()
      this.getMetaDatatModelList()

    }

    //外键表选择事件
    onSelectChange(item) {

      let URL = config.port('metadata') + 'getfkTableColumnList?table=' + item
      this.$http.get(URL).then((response) => {

        for (let form of this.formDataList) {
          if (form.codeType === 'is_fk') {
            form.dictValues = JSON.parse(response.data.data)
          }
        }
      }).catch(function (response) {
      })
    }

    //获取数据类型集合
    getMetaTypeList() {

      let URL = config.port('metadata') + 'selectMetaTypeDictItems'
      this.$http.get(URL).then((response) => {

        this.dictList = JSON.parse(response.data.data)

      }).catch(function (response) {
      })
    }

    //获取当前数据集合
    getMetaDatatModelList() {
      if (this.table === '') {
        this.tableData = []
        this.handleAdd()
        //默认加载界面下方的属性
        this.loadMetaAttributes("")
        return
      }

      let URL = config.port('metadata') + 'getMetaTableList?table=' + this.tableName
      this.$http.get(URL).then((response) => {

        this.tableData = JSON.parse(response.data.data)[0].data
        //默认加载界面下方的属性
        this.loadMetaAttributes("")
      }).catch(function (response) {
      })
    }

    //行点击事件,加载界面下方的属性
    rowClick(row, event, column) {
      this.nowTagId = row.tag_id
      this.loadMetaAttributes(row.tag_id)
    }

    //加载界面下方的属性信息
    loadMetaAttributes(tagid: any) {

      if (this.lastTagId != "" && tagid != "" && tagid != "0") {
        this.setTempMemory(this.lastTagId, this.formDataList);
      }

      //已经修改过的属性信息需要重新加载出来
      let flag = false;
      if (this.metaDataList != null) {
        for (let meta of this.metaDataList) {
          //根据tagid,从临时缓存集合中取出已经修改过的值
          if (meta.lastTagId === tagid) {
            //取到缓存后不再从后台取值，直接返回
            this.formDataList = JSON.parse(meta.lastDataList);
            flag = true;
          }
        }
      }

      if (flag) {
        this.lastTagId = tagid;
        return;
      }

      //从后台获取每一个字段的所有属性，没有属性的加载默认值

      let URL = config.port('metadata') + 'selectMetaModuleTypeListByTable?attId=' + tagid+'&tableName='+this.tableName;
      this.$http.get(URL).then((response) => {

        //获取当前已经配置的属性信息
        this.formDataList = JSON.parse(response.data.data);

        if (tagid == "") {
          //第一次加载时获取的全部属性集合
          this.defaultDataList = JSON.parse(response.data.data);
        }

        let formDataStr = JSON.stringify(this.defaultDataList);
        let tempDataList = JSON.parse(formDataStr);

        //加载默认的属性信息
        for (let tempData of tempDataList) {
          for (let formData of this.formDataList) {
            if (formData.codeType === tempData.codeType) {
              tempData.dictValues = formData.dictValues;
              tempData.typeValue = formData.typeValue;
            }
          }
        }

        this.formDataList = tempDataList;
      }).catch(function (response) {
      })

      this.lastTagId = tagid;
    }

    //将数据存入缓存对象集合
    setTempMemory(attid, formList) {
      //将当前的数据临时存储
      let newMetaData: any = {}
      newMetaData.lastTagId = attid
      //下方属性集合
      newMetaData.lastDataList = JSON.stringify(formList);

      //循环当前临时数据集合，如果有的话将数据更新，没有的话添加进去
      let hasFormData = true;
      for (let i = 0; i < this.metaDataList.length; i++) {

        if (this.metaDataList[i].lastTagId + "" == attid + "") {
          this.metaDataList.splice(i, 1);
          hasFormData = false;
        }
      }

      this.metaDataList.push(newMetaData);
    }

    //新增的字段，临时记录ID
    newTagId: any = 1;

    //增加一行空行
    handleAdd() {
      var newdata = {
        name: '',
        description: '',
        type: '',
        tag_id: 'addNew_' + this.newTagId
      }
      this.tableData.push(newdata);
      this.newTagId += 1;
    }

    //保存信息
    handleSave() {
      if (this.nowTagId != "") {
        //将最后一次点击的字段添加到缓存
        this.setTempMemory(this.nowTagId, this.formDataList);
      }

      //将this.tableData的内容全部存储
      let dataList: any = new Object();
      dataList.table = this.tableName;

      let tempDataList: any = [];
      //循环拼接保存时规定的json格式数据
      for (let data of this.tableData) {

        let dataValue: any = new Object();
        dataValue.name = data.name;
        dataValue.description = data.description;
        dataValue.parent_id = '0';
        dataValue.row_id = this.tableName;
        dataValue.metatype = 'column';

        //后续添加的字段不传id,由后台生成
        if ((data.tag_id + "").indexOf("addNew_") != -1) {
          dataValue.id = "";
        } else {
          dataValue.id = data.tag_id;
        }

        let subdata: any = {};
        subdata.table = "column_type";
        for (let meta of this.metaDataList) {
          let formList: any = [];
          if (data.tag_id == meta.lastTagId) {

            let list = JSON.parse(meta.lastDataList);
            for (let form of list) {
              let formValue: any = new Object();

              //手动添加的字段不赋att_id值
              if ((meta.lastTagId + "").indexOf("addNew_") != -1) {
                formValue.att_id = "";
              } else {
                formValue.att_id = meta.lastTagId;
              }
              formValue.codetype = form.codeType

              //类型和长度两个属性固定从data里面取,其他类型从form里面循环取
              if (form.codeType == "type") {
                formValue.typevalue = data.type;
              } else if (form.codeType == "length") {
                formValue.typevalue = data.length;
              } else {
                formValue.typevalue = form.typeValue;
              }

              formValue.description = form.description;
              formList.push(formValue);
            }

            subdata.data = formList;
          }
        }
        dataValue.subdata = subdata;

        tempDataList.push(dataValue)
        dataList.data = tempDataList;
      }

      let param: any = new Object();
      param.metaJson = JSON.stringify(dataList);

      let URL = config.port('metadata') + 'saveOrUpdateMetaToMetaModule';

      this.$http.post(URL, Qs.stringify(param)).then((response) => {

        //将数据发送到后台存储
        let result = response.data.status;
        if (result == "200") {
          //保存成功
          this.$message({
            message: '保存成功。',
            type: 'success'
          });
          this.close(true);
        } else if (result == "-10000") {
          //保存失败
          this.$message.error('保存失败。');
        }

      }).catch(function (response) {
      })
    }

    handleDelete(row){

      let nowid = row.tag_id+"";
      this.nowTagId = nowid;
      if(nowid.indexOf("addNew_")>-1){
        this.tableData.pop();
        return;
      }
      this.$confirm('删除时将删除所有属性约束和关联关系，确定删除吗？', '提示', {type: 'warning'}).then(() => {
        
        let URL = config.port('metadata') + 'deleteMetaToMetaColumns';
        let param : any = [];
        let attInfo:any = new Object()
        attInfo.tableName = this.tableName
        attInfo.attid = this.nowTagId
        param.push(attInfo)

        let paramData : any = new Object()
        paramData.data = param
        paramData.pageAjax = null


        this.$http.post(URL,paramData).then((response) => {

          //将数据发送到后台进行删除
          let result = response.data.status;
          if (result == "200") {
            //删除成功
            this.$message({
              message: '删除成功。',
              type: 'success'
            });
            this.close(true);
          } else if (result == "-10000") {
            //删除失败
            this.$message.error(response.data.message);
          }  
        }).catch(function (response) {
        })
      }).catch(() => {
      }); 
    }
    
    // success 保存成功true 失败false或者为undefined
    close(success) {
      this.$emit("refreshbizlines", success);
    }
  }
</script>

<style scope='scoped'>
  .input-with-select {
    background-color: #fff;
  }

  .rowHeight td {
    height: 25px;
  }

</style>
