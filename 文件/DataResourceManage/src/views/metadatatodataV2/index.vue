<template>
  <el-row class="sheets-container meta-data-to-data-v2">
    <div class="tree-container">
      <div class="tree">
        <span class="title">元数据管理</span>
        <div style="outline: white 0px solid;height: 90%;width:100%; border: 0;" ref="myPaletteDiv"
             id="myPaletteDiv"></div>
      </div>
      <div class="tabs">
        <el-tabs
          v-model.trim="rightTabsActiveId"
          type="card"
          closable
          @tab-remove="removeRightTab">
          <el-tab-pane
            :label="item.name"
            :name="item.module_instance_id"
            v-for="(item,index) in rightTabsAllData" :key="item.module_instance_id">
            <db-model v-if="item.module_id==30" :currentModuleInstance="item"></db-model>
            <table-model v-else-if="item.module_id==34" :currentModuleInstance="item"></table-model>
            <field-model v-else-if="item.module_id==38" :currentModuleInstance="item"></field-model>
            <!--<target-model v-else-if="item.module_id==1769" :currentModuleInstance="item"></target-model>-->
            <ship v-else-if="item.module_id==1769" :currentModuleInstance="item" :id="item.module_instance_id"></ship>
            <tab-model v-else :currentModuleInstance="item"></tab-model>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>

    <!--===============================建库弹框-->
    <el-dialog :title="moduleInfo.name"
               :visible.sync="createOrEditorModuleInstence"
               width="70%"
               :close-on-click-modal="false">
      <el-form ref="" label-width="120px" v-if="moduleInfo.show_type === 'form'">
        <el-form-item v-if="attr.attrExt.is_show !== '2'" :label="attr.att_name_cn"
                      v-for="(attr,index) in moduleInfo.attrs" :key="index">
          <el-input v-if="attr.attrExt.show_type!=='select'" :type="attr.attrExt.show_type"
                    v-model.trim="dataOrFormDatas[attr.att_name_en].meta_value"></el-input>
          <el-select filterable v-if="attr.attrExt.show_type === 'select'"
                     v-model.trim="dataOrFormDatas[attr.att_name_en].meta_value">
            <el-option
              v-for="x in attr.attrExt.selections"
              :key="x.code"
              :label="x.name"
              :value="x.code"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <!--建表的一次弹框--添加字段-->
      <div v-if="moduleInfo.show_type==='table'">
        <el-row class="row-bg" justify="start">
          <el-col :span="2">
            <el-button @click="handleAddColumn" icon="el-icon-plus" align="left">新增</el-button>
          </el-col>
          <!--<el-col :span="2"><el-button @click="deleteDictInfo" icon="el-icon-delete" align="left">删除</el-button></el-col>-->
        </el-row>
        <el-table
          :data="dataOrFormDatas"
          highlight-current-row
          row-key="id"
          @select=""
          @select-all=""
          @row-click=""
          ref="">
          <el-table-column width="55" type="index" align="center" label="序号"></el-table-column>
          <el-table-column
            v-if="attr.attrExt.is_show !== '2'"
            :prop="attr.att_name_en"
            :label="attr.att_name_cn"
            v-for="(attr,index) in moduleInfo.attrs" :key="index">
            <template slot-scope="scope">
              <el-input
                v-if="attr.attrExt.show_type !== 'select'"
                :type="attr.attrExt.show_type"
                v-model.trim="scope.row[attr.att_name_en].meta_value"></el-input>
              <el-select filterable
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
      <span slot="footer" class="dialog-footer">
          <el-button @click="createOrEditorModuleInstence = false">取 消</el-button>
          <el-button type="primary" @click="submitModuleInstence">确 定</el-button>
        </span>
    </el-dialog>
  </el-row>
</template>

<script>
  import Vue from 'vue'
  import Component from 'vue-class-component'
  import config from '../../config.ts'
  import Qs from 'qs'
  import DbModel from './dbModel'
  import TableModel from './tableModel'
  import TabModel from './tabModel'
  import FieldModel from './fieldModel'
  import TargetModel from './targetModel'
  import Ship from './ship'
  import { mapMutations } from 'vuex'
  import {isIE} from '@/common/util/IE'

  export default {
    data() {
      return {
        /*树形结构属性*/
        defaultProps: {
          children: 'children',
          label: 'name'
        },
        loaded: false,
        /*建库弹框是否弹出*/
        createOrEditorModuleInstence: false,
        /*建库&建表的模型的相关信息*/
        moduleInfo: {},
        /*要展示出来的数据*/
        dataOrFormDatas: {},
        //当前模型的实例
        currentModuleInstance: {},
        // 右侧外围的tabs激活activeId
        rightTabsActiveId: '',
        // 右侧外围的所有tabs的数据
        rightTabsAllData: [],

        currentEditingNode: {},
        gojs: this.$go.GraphObject.make
      }
    },
    /*渲染完成之后开始画图*/
    mounted() {
      this.initPalette();
      if(isIE && sessionStorage.getItem('IE') === 'true'){
        sessionStorage.setItem('IE','true');
        this.$message.warning('您使用的是IE浏览器，为了更好的访问体验，请使用谷歌、火狐浏览器')
      }
    },
    methods: {
      ...mapMutations(['setLoadingFlag']),
      imageConverter(prop, picture) {
        var node = picture.part;
        if (node.isTreeLeaf) {
          return "images/document.png";
        } else {
          if (node.isTreeExpanded) {
            return "images/openFolder.png";
          } else {
            return "images/closedFolder.png";
          }
        }
      },
      /*初始化Palette*/
      initPalette() {
        const _this = this;
        var myPalette = _this.gojs(_this.$go.Palette, "myPaletteDiv",
          {
            //allowMove: false,
            //allowCopy: false,
            //allowDelete: false,
            //allowHorizontalScroll: false,
            contentAlignment: go.Spot.TopLeft,
            layout:
              _this.gojs(_this.$go.TreeLayout,
                {
                  alignment: go.TreeLayout.AlignmentStart,
                  angle: 0,
                  compaction: go.TreeLayout.CompactionNone,
                  layerSpacing: 16,
                  layerSpacingParentOverlap: 1,
                  nodeIndentPastParent: 1.0,
                  nodeSpacing: 5,
                  setsPortSpot: false,
                  setsChildPortSpot: false
                }),
            linkTemplate: _this.gojs(_this.$go.Link),
          });
        /*重写树展开命令*/
        myPalette.nodeTemplate = _this.gojs(_this.$go.Node,
          {
            selectionAdorned: false,
            cursor: "pointer",
            click: function (e, node) {
              if ("treeExpanderButton" == e.targetObject.name) {
                var cmd = myPalette.commandHandler;
                e.handled = true;
                if (node.isTreeExpanded && (!node.isTreeLeaf)) {
                  cmd.collapseTree(node);
                } else if (!node.isTreeLeaf) {
                  cmd.expandTree(node);
                } else {
                  _this.loadNode(node, function (data) {
                    if (data) {
                      for (var i = 0; i < data.length; i++) {
                        data[i].parent = node.data.tree_node_key;
                        node.diagram.model.addNodeData(data[i]);
                      }
                    }
                    cmd.expandTree(node);
                  })
                }
              } else {
                _this.handleNodeClick(node.data);
              }
            }
          },
          new _this.$go.Binding("key", "tree_node_key"),
          _this.gojs("SubGraphExpanderButton",
            {
              width: 14,
              "ButtonBorder.fill": null,
              "ButtonBorder.stroke": null,
              "_buttonFillOver": "rgba(255,255,255,0.55)",
              "_buttonStrokeOver": null,
              name: "treeExpanderButton",
            }),
          _this.gojs(_this.$go.Panel, "Horizontal",
            {position: new _this.$go.Point(16, 0)},
            new _this.$go.Binding("background", "isSelected", function (s) {
              return (s ? "#2F3E61" : null);
            }).ofObject(),
            _this.gojs(_this.$go.TextBlock,
              {
                font: '9pt Verdana, sans-serif',
                stroke: "white"
              },
              new _this.$go.Binding("text", "name", function (s) {
                return s || "unknow";
              }))
          )
        );
        this.loadNode(null, function (data) {

          myPalette.model = _this.gojs(_this.$go.TreeModel, {
            nodeKeyProperty: "tree_node_key",
            nodeParentKeyProperty: "parent",
            nodeDataArray: data
          });
        });
      },
      // 移除右边外围的tabs
      removeRightTab(targetId) {
        if (this.rightTabsAllData.length < 2) {
          this.rightTabsAllData = [];
          return;
        }
        let index = 0;
        for (var i = 0; i < this.rightTabsAllData.length; i++) {
          if (targetId == this.rightTabsAllData[i].module_instance_id) {
            index = i;
          }
        }
        if (targetId != this.rightTabsActiveId) {
          this.rightTabsAllData.splice(index, 1);
          return;
        }
        if (index == (this.rightTabsAllData.length - 1)) {
          this.rightTabsActiveId = this.rightTabsAllData[0].module_instance_id;
        } else {
          this.rightTabsActiveId = this.rightTabsAllData[index + 1].module_instance_id;
        }
        this.rightTabsAllData.splice(index, 1);
      },
      /*提交建库建表元数据*/
      submitModuleInstence() {
        this.createOrEditorModuleInstence = false;
        let data = [];
        //组装数据：
        if (this.moduleInfo.show_type == "form") {
          let metaVules = [];
          for (var i = 0; i < this.moduleInfo.attrs.length; i++) {
            let attr = this.moduleInfo.attrs[i];
            var metaVule = {
              id: this.dataOrFormDatas[attr.att_name_en]['meta_value_id'] || '',
              att_id: attr.id,
              parent_id: this.moduleInfo.meta_value_pid || this.moduleInfo.module_instance_id,
              row_id: this.dataOrFormDatas['module_instance_id'],
              meta_value: this.dataOrFormDatas[attr.att_name_en]['meta_value']
            };
            metaVules.push(metaVule);
          }
          data.push(metaVules);
        } else if (this.moduleInfo.show_type === "table") {
          //将列的元数据保存
          for (var j = 0; j < this.dataOrFormDatas.length; j++) {
            let val = this.dataOrFormDatas[j];
            let metaVules = [];
            for (var i = 0; i < this.moduleInfo.attrs.length; i++) {
              let attr = this.moduleInfo.attrs[i];
              if (val[attr.att_name_en]) {
                var metaVule = {
                  id: val[attr.att_name_en]['meta_value_id'] || '',
                  att_id: attr.id,
                  parent_id: this.moduleInfo.meta_value_pid || this.moduleInfo.module_instance_id,
                  row_id: val['module_instance_id'],
                  meta_value: val[attr.att_name_en]['meta_value']
                };
                metaVules.push(metaVule);
              }
            }
            data.push(metaVules);
          }
        }
        let URL = config.port('metadata') + 'saveOrUpdateMetaData';
        var param = new Object();
        param.metaJson = JSON.stringify({
          table: 'meta_value',
          data: data
        });

        this.$http.post(URL, Qs.stringify(param)).then((response) => {

          this.refreshCurrentNode();
        }).catch(function (response) {
        })
      },
      /*table方式添加元数据时，添加行*/
      handleAddColumn() {
        var row = {};
        row = JSON.parse(JSON.stringify(this.moduleInfo.attrValueJson));
        this.dataOrFormDatas.push(row);
      },
      /*元数据树形结构节点点击回调函数*/
      handleNodeClick(data) {
        //let allTabsData = JSON.parse(JSON.stringify(this.rightTabsAllData))
        if (data.tree_type === 'module') {
          return;
        }
        this.rightTabsActiveId = data.module_instance_id
        // 最多展示20个tabs
        if (this.rightTabsAllData.length >= 20) {
          return;
        }
        // 禁止重复tab
        for (let i = 0; i < this.rightTabsAllData.length; i++) {
          if (this.rightTabsAllData[i].module_instance_id === data.module_instance_id) {
            return;
          }
        }
        this.rightTabsAllData.push(data)
        this.currentModuleInstance = data;
        //准备右边tab的数据
        //加载树首次加载顶级模型传入参数 parentId
      },
      /*树形节点加载子节点函数*/
      loadNode(node, resolve) {
        //加载树首次加载顶级模型传入参数 parentId
        let URL = config.port('metadataManage') + 'getMetadataModelTreeGojs';
        var p = {
          parentId: null,
          moduleInstanceId: null,
          treeType: null
        };
        //如果首次加载
        if (node) {
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
        }).catch(function (response) {
        })
      },
      refreshCurrentNode() {
        let data = this.currentEditingNode;
        //加载树首次加载顶级模型传入参数 parentId
        let URL = config.port('metadataManage') + 'getMetadataModelTreeGojs';
        var p = {
          parentId: data.module_id,
          moduleInstanceId: data.module_instance_id,
          treeType: data.tree_type
        };
        var param = new Object();
        param.metaJson = JSON.stringify(p);

        this.$http.post(URL, Qs.stringify(param)).then((response) => {

          this.$refs.metaDataTree.updateKeyChildren(data['tree_node_key'], (response.data.data || []))
        }).catch(function (response) {
        })
      },
      /*添加元数据*/
      append(node, data) {
        //调用后端接口
        let model = {
          id: data.module_id,
          parentId: data.module_id,
          moduleInstanceId: data.module_instance_id,
          treeType: data.tree_type
        };
        let URL = config.port('metadataManage') + 'getCreateModelByModelName';

        this.$http.post(URL, model).then((response) => {

          this.createOrEditorModuleInstence = true;
          this.moduleInfo = response.data.data.moduleInfo;
          this.dataOrFormDatas = response.data.data.datas;
          this.currentEditingNode = data;
        }).catch(function (response) {
        })
      },
      /*系统升级*/
      systemUpgrade(node, data) {
        //调用后端接口
        let model = {
          id: data.module_id,
          parentId: node.data.module_id,
          moduleInstanceId: node.data.module_instance_id,
          treeType: node.data.tree_type
        };
        let URL = config.port('metadataManage') + 'systemUpgrade';

        this.$http.post(URL, model).then((response) => {

          /*this.moduleInfo = response.data.data.moduleInfo;
          this.dataOrFormDatas = response.data.data.datas;*/
        }).catch(function (response) {
        });
      },
      /*编辑元数据*/
      edit(node, data) {
        //调用后端接口
        let model = {
          id: data.module_id,
          parentId: node.data.module_id,
          moduleInstanceId: node.data.module_instance_id,
          treeType: node.data.tree_type
        };
        let URL = config.port('metadataManage') + 'getMetadataModelTreeForEdit';

        this.$http.post(URL, model).then((response) => {

          this.createOrEditorModuleInstence = true;
          this.moduleInfo = response.data.data.moduleInfo;
          this.dataOrFormDatas = response.data.data.datas;
        }).catch(function (response) {
        });
      }
    },
    components: {
      TableModel,
      DbModel,
      TabModel,
      FieldModel,
      TargetModel,
      Ship
    }
  }
</script>

<style>
  div {
    outline-width: 0;
  }

  /*div.el-tree-node__label{*/
  /*width: 100%;*/
  /*}*/
  /*.el-tree-node__label .el-tree-node__buuton{*/
  /*display: inline-block;*/
  /*}*/
  /*.el-tree-node__label .el-tree-node__buuton .btn {*/
  /*display: none;*/
  /*}*/

  /*.el-tree-node__label:hover .el-tree-node__buuton .btn {*/
  /*display: inline;*/
  /*}*/
</style>
