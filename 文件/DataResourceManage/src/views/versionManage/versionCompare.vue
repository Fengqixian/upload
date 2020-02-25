<template>
  <el-row :gutter="20">
    <el-col :span="6" style="min-height: 500px;">
      <el-tree
        :default-expanded-keys="defaultExpandedKeys"
        :props="defaultProps"
        @current-change="currentChange"
        ref="metaDataTree"
        :highlight-current="true"
        node-key="tree_node_key"
        :expand-on-click-node="false"
        :load="loadNode"
      lazy>
      </el-tree>
    </el-col>
    <!-- 右侧外围tabs -->
    <el-col :span="18">
      <el-tabs v-model.trim="activeName">
        <el-tab-pane
          :label="formTabName"
          name="formTab"
          key="formTab"
          v-if="formTabName">
          <el-row  :gutter="20">
            <el-col :span="12">
              <el-form ref="" label-width="120px" style="margin-bottom: 1rem;">
                <el-form-item label="系统版本">
                  <el-select  filterable v-model.trim="leftPettyCode" disabled >
                    <el-option
                      v-for="x in pettyVersions"
                      :key="x.petty_code"
                      :label="x.vs_name"
                      :value="x.petty_code"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-form>
              <div v-scroll="formScrollChange"  style="overflow:auto;">
                <el-form ref="" label-width="120px"  v-if="dataOrFormDatas">
                  <el-form-item v-if="attr.attrExt.is_show !== '2'" :label="attr.att_name_cn"
                                v-for="(attr,index) in moduleInfo.attrs" :key="index">
                    <el-input v-if="attr.attrExt.show_type!=='select'" :type="attr.attrExt.show_type" disabled
                              v-model.trim="dataOrFormDatas[attr.att_name_en].meta_value"></el-input>
                    <el-select  disabled filterable v-if="attr.attrExt.show_type === 'select'" v-model.trim="dataOrFormDatas[attr.att_name_en].meta_value">
                      <el-option
                        v-for="x in attr.attrExt.selections"
                        :key="x.code"
                        :label="x.name"
                        :value="x.code"
                      ></el-option>
                    </el-select>
                  </el-form-item>
                </el-form>
              </div>
            </el-col>
            <el-col :span="12">
              <el-form ref="" label-width="120px"  style="margin-bottom: 1rem;">
                <el-form-item label="系统版本">
                  <el-select  filterable v-model.trim="rightPettyCode" @change="rightPettyCodeChange">
                    <el-option
                      v-for="x in pettyVersions"
                      :key="x.petty_code"
                      :label="x.vs_name"
                      :value="x.petty_code"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-form>
              <div id="rightForm" style="overflow: auto;">
                <el-form ref="" label-width="120px" v-if="hisDataOrFormDatas">
                  <el-form-item v-if="attr.attrExt.is_show !== '2'" :label="attr.att_name_cn"
                                v-for="(attr,index) in hisModuleInfo.attrs" :key="index">
                    <el-input v-if="attr.attrExt.show_type!=='select'" :type="attr.attrExt.show_type" disabled
                              v-model.trim="hisDataOrFormDatas[attr.att_name_en].meta_value"></el-input>
                    <el-select disabled filterable v-if="attr.attrExt.show_type === 'select'" v-model.trim="hisDataOrFormDatas[attr.att_name_en].meta_value">
                      <el-option
                        v-for="x in attr.attrExt.selections"
                        :key="x.code"
                        :label="x.name"
                        :value="x.code"
                      ></el-option>
                    </el-select>
                  </el-form-item>
                </el-form>
              </div>
            </el-col>
          </el-row>
        </el-tab-pane>
        <el-tab-pane
          :label="tableTabName"
          v-if="tableTabName"
          name="tableTab"
          key="tableTab">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form ref="" label-width="120px">
                <el-form-item label="系统版本">
                  <el-select  filterable v-model.trim="leftPettyCode" disabled >
                    <el-option
                      v-for="x in pettyVersions"
                      :key="x.petty_code"
                      :label="x.vs_name"
                      :value="x.petty_code"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-form>
              <div>
                <!--表格展示-->
                <el-table
                  ref="mainTable"
                  :data="tabFormOrTableDatas"
                  highlight-current-row
                  row-key="id"
                  @select=""
                  v-scroll="scrollChange"
                  @select-all=""
                  @row-click=""
                  v-if="loaded"
                  height="400">
                  <el-table-column width="55" type="index" align="center" label="序号"></el-table-column>
                  <el-table-column width="200"
                                   v-if="attr.attrExt.is_show!=='2'"
                                   :label="attr.att_name_cn"
                                   v-for="(attr,index) in tabModuleInfo.attrs"
                                   :key="index">
                    <template slot-scope="scope">
                      <el-input disabled
                                v-if="attr.attrExt.show_type!=='select'"
                                :type="attr.attrExt.show_type"
                                v-model.trim="scope.row[attr.att_name_en].meta_value"></el-input>
                      <el-select filterable disabled
                                 v-if="attr.attrExt.show_type === 'select'"
                                 v-model.trim="scope.row[attr.att_name_en].meta_value">
                        <el-option
                          v-for="x in attr.attrExt.selections"
                          :key="x.code"
                          :label="x.name"
                          :value="x.code"
                        ></el-option>
                      </el-select>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-col>
            <el-col :span="12">
              <el-form ref="" label-width="120px">
                <el-form-item label="系统版本">
                  <el-select  filterable v-model.trim="rightPettyCode"  @change="rightPettyCodeChange">
                    <el-option
                      v-for="x in pettyVersions"
                      :key="x.petty_code"
                      :label="x.vs_name"
                      :value="x.petty_code"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-form>
              <div>
                <!--表格展示-->
                <el-table
                  :data="hisTabFormOrTableDatas"
                  highlight-current-row
                  row-key="id"
                  @select=""
                  @select-all=""
                  @row-click=""
                  v-if="loaded"
                  height="400"
                  ref="rightTable">
                  <el-table-column width="55" type="index" align="center" label="序号"></el-table-column>
                  <el-table-column width="200"
                                   v-if="attr.attrExt.is_show!=='2'"
                                   :label="attr.att_name_cn"
                                   v-for="(attr,index) in hisTabModuleInfo.attrs"
                                   :key="index">
                    <template slot-scope="scope">
                      <el-input disabled
                                v-if="attr.attrExt.show_type!=='select'"
                                :type="attr.attrExt.show_type"
                                v-model.trim="scope.row[attr.att_name_en].meta_value"></el-input>
                      <el-select filterable disabled
                                 v-if="attr.attrExt.show_type === 'select'"
                                 v-model.trim="scope.row[attr.att_name_en].meta_value">
                        <el-option
                          v-for="x in attr.attrExt.selections"
                          :key="x.code"
                          :label="x.name"
                          :value="x.code"
                        ></el-option>
                      </el-select>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-col>
          </el-row>
        </el-tab-pane>
      </el-tabs>
    </el-col>
  </el-row>
</template>

<script>
  import Vue from 'vue'
  import Component from 'vue-class-component'
  import config from '../../config.ts'
  import Qs from 'qs'
  import { mapMutations } from 'vuex'

  export default {
    data() {
      return {
        /*树形结构属性*/
        defaultProps: {
          children: 'children',
          label: 'name'
        },
        /*建库&建表的模型的相关信息*/
        moduleInfo: {},
        hisModuleInfo:{},
        /*要展示出来的数据*/
        dataOrFormDatas: {},
        hisDataOrFormDatas: {},

        tabModuleInfo:{},
        tabFormOrTableDatas:[],
        hisTabModuleInfo:{},
        hisTabFormOrTableDatas:[],
        loaded:false,
        activeName:"",
        formTabName:'',
        tableTabName:'',

        pettyVersions:[],
        leftPettyCode:'',
        rightPettyCode:'',
    }
    },
    props:{
      defaultExpandedKeys:{
        type:Array,
        default:function () {
         return [];
        }
      },
      hisVersionCode:{
        type:String,
        default:function () {
          return "";
        }
      },
      selectedTreeNodeKey:{
        type:String,
        default:function () {
          return "";
        }
      }
    },
    methods: {
      ...mapMutations(['setLoadingFlag']),
      rightPettyCodeChange(){
        var data = this.$refs.metaDataTree.getCurrentNode();
        this.handleNodeClick(data);
      },
      currentChange(data,node){
        //点击顶级节点，直接展示
        if(node.level<2){
          this.handleNodeClick(data);
          return;
        }
        var level = node.level
        for(var i=level;i>2;i--){
          node = node.parent;
        }
        var param = {
          id:node.data.module_id,
          parentId: node.data.module_id,
          moduleInstanceId: node.data.module_instance_id,
          treeType: node.data.tree_type,
          //specifiedVersion:this.hisVersionCode //指定版本
        }
        /*获取小版本*/
        let URL = config.port('versionManage') + 'getPettyVersions';

        this.$http.post(URL, param).then((response) => {

          this.pettyVersions = response.data.data||[];
          //如果换了系统将默认选项归为第一项
          let first = (this.pettyVersions[0]||{}).petty_code;
          if(!first){
            this.rightPettyCode='';
          }else if(first.substring(16) == this.rightPettyCode.substring(16)){

          }else{
            this.rightPettyCode=first;
          }
          this.handleNodeClick(data);
        }).catch(function (response) {

        })
      },
      scrollChange(scrollLeft,scrollTop){
        let table = this.$refs.rightTable.$el.querySelector('.el-table__body-wrapper');
        if(scrollLeft+table.clientWidth<table.scrollWidth){
          table.scrollLeft = scrollLeft;
        }else{
          table.scrollLeft = table.scrollWidth-table.clientWidth;
        }
        if(scrollTop+table.clientHeight<table.scrollHeight){
          table.scrollTop = scrollTop;
        }else{
          table.scrollTop = table.scrollHeight-table.clientHeight;
        }
      },
      formScrollChange(scrollLeft,scrollTop){
        let form = this.$el.querySelector('#rightForm');
        if(scrollLeft+form.clientWidth<form.scrollWidth){
          form.scrollLeft = scrollLeft;
        }else{
          form.scrollLeft = form.scrollWidth-form.clientWidth;
        }
        if(scrollTop+form.clientHeight<form.scrollHeight){
          form.scrollTop = scrollTop;
        }else{
          form.scrollTop = form.scrollHeight-form.clientHeight;
        }
      },
      /*元数据树形结构节点点击回调函数*/
      handleNodeClick(data) {
        var param = {
          id:data.module_id,
          parentId: data.module_id,
          moduleInstanceId: data.module_instance_id,
          treeType: data.tree_type,
          specifiedVersion:this.hisVersionCode,
          //currentPettyCode:this.leftPettyCode,
          historyPettyCode:this.rightPettyCode
        }
        if(data.tree_type == "instance"){
          //加载树首次加载顶级模型传入参数 parentId
          let URL = config.port('versionManage') + 'compareVersionModuleInstance';
          //获取后端的当前节点的历史版本信息

          this.$http.post(URL, param).then((response) => {

            this.moduleInfo = response.data.data.current.moduleInfo;
            if(this.moduleInfo.show_type=='table'){
              this.dataOrFormDatas = response.data.data.current.datas[0];
            }else{
              this.dataOrFormDatas = response.data.data.current.datas;
            }
            this.hisModuleInfo = response.data.data.history.moduleInfo;
            if(this.hisModuleInfo.show_type=='table'){
              this.hisDataOrFormDatas = response.data.data.history.datas[0];
            }else{
              this.hisDataOrFormDatas = response.data.data.history.datas;
            }
            this.activeName="formTab";
            this.formTabName=data.name;
          }).catch(function (response) {
          })
        }else if(data.tree_type == "module"){
          this.loaded = false;
          // 调用后端接口
          let URL = config.port('versionManage') + 'compareVersionModule';

          this.$http.post(URL, param).then((response) => {

            this.tabModuleInfo = response.data.data.current.moduleInfo;
            this.tabFormOrTableDatas = response.data.data.current.datas;
            this.hisTabModuleInfo = response.data.data.history.moduleInfo;
            this.hisTabFormOrTableDatas = response.data.data.history.datas;
            this.loaded = true;
            this.activeName="tableTab";
            this.tableTabName=data.name;
          }).catch(function (response) {
          });
        }
      },
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
        var param = new Object();
        param.metaJson = JSON.stringify(p);

        this.$http.post(URL, Qs.stringify(param)).then((response) => {

          resolve(response.data.data || []);
          let nodes = response.data.data||[];
          for(var i=0;i<nodes.length;i++){
            if(nodes[i].tree_node_key == this.selectedTreeNodeKey){
              this.$refs.metaDataTree.setCurrentKey(this.selectedTreeNodeKey);
              this.currentChange(nodes[i],this.$refs.metaDataTree.getNode(this.selectedTreeNodeKey));
            }
          }
        }).catch(function (response) {
        })
      }
    }
  }
</script>

<style>
  div.el-tree-node__label{
    width: 100%;
  }
  .el-tree-node__label .el-tree-node__buuton{
    display: inline-block;
  }
  .el-tree-node__label .el-tree-node__buuton .btn {
    display: none;
  }

  .el-tree-node__label:hover .el-tree-node__buuton .btn {
    display: inline;
  }
</style>
