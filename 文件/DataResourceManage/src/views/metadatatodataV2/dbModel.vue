<template>
  <el-row>
    <el-row>
      <div class="operate">
        <span class="btn" @click.prevent.stop="submitModuleInstence"> <i class="iconfont icon-baocun"></i> 保存</span>
      </div>
    </el-row>
    <el-row>
      <el-col :span="createOrEditorModuleInstence?18:24">
        <div ref="myDiagramDiv" :id="myDiagramDivId" style="width: 100%;height: 500px;border: 0;"></div>
      </el-col>
      <el-col :span="6"  style="display: inline-block; vertical-align: top;" v-show="createOrEditorModuleInstence">
        <div :id="myInspectorNodeDivId" class="inspector" style="height: 500px;border: 0;" v-show="createOrEditorModuleInstence=='node'"></div>
        <div :id="myInspectorLinkDivId" class="inspector" style="height: 500px;border: 0;" v-show="createOrEditorModuleInstence=='link'"></div>
        <div :id="myInspectorGroupDivId" class="inspector" style="height: 500px;border: 0;" v-show="createOrEditorModuleInstence=='group'"></div>
      </el-col>
    </el-row>
  </el-row>
</template>

<script>
  import Vue from 'vue'
  import Component from 'vue-class-component'
  import config from '../../config.ts'
  import Qs from 'qs'
  import {Watch} from 'vue-property-decorator'
  import {Inspector} from "../../common/dataInspector/DataInspector.js"
  import "../../common/dataInspector/DataInspector.css";
  export default {
    data() {
      return {
        createOrEditorModuleInstence:"",
        moduleInfo:null,
        dataOrFormDatas:null,
        myDiagram:'',
        goMake:'',
        myDiagramDivId:'',
        myInspectorLinkDivId:'',
        myInspectorNodeDivId:'',
        myInspectorGroupDivId:'',

        groupInspector:null,
        nodeInspector:null,
        linkInspector:null,
        /*拦截器共有属性*/
        inspectorProperties: {
          "name": {readOnly: true, show: true, display: '名称'},
          "nodeGroup": {readOnly: true, show: false, display: "所属表ID"},
          "nodeIsGroup": {readOnly: true, show: false, type: 'checkbox', display: ""},
          "parent_module_instance_id": {readOnly: true, show: false},
          "show_type": {readOnly: true, show: false},
          "module_id": {readOnly: true, show: false},
          "module_instance_key": {readOnly: true, show: false},
          "tree_node_key": {readOnly: true, show: false},
          "tree_type": {readOnly: true, show: false},
          "module_instance_id": {readOnly: true, show: true, display: '节点ID'},
          /*"from": {readOnly: true, show: Inspector.showIfLink},
          "to": {readOnly: true, show: Inspector.showIfLink},*/
          "parent": {readOnly: true, show: false},
          "isUpdated": {readOnly: false, show: false,type:"checkbox"},
        },
        /*存储被删除的对象*/
        deletedObjects:[]
      }
    },
    props: {
      currentModuleInstance: {
        type: Object,
        default: function () {
          return {};
        }
      }
    },
    created: function () {
      this.myDiagramDivId = "myDiagramDiv-" + this.currentModuleInstance.module_instance_id;
      this.myInspectorNodeDivId = "myInspectorNodeDivId-" + this.currentModuleInstance.module_instance_id;
      this.myInspectorGroupDivId = "myInspectorGroupDivId-" + this.currentModuleInstance.module_instance_id;
      this.myInspectorLinkDivId = "myInspectorLinkDivId-" + this.currentModuleInstance.module_instance_id;
    },
    mounted:function(){
      this.initDiagram();
      this.initData();
    },
    methods: {
      showPorts(node, show) {
        var diagram = node.diagram;
        if (!diagram || diagram.isReadOnly || !diagram.allowLink) return;
        node.ports.each(function (port) {
          port.stroke = (show ? "blue" : null);
        });
      },
      makePort(name, spot, output, input) {
        return this.goMake(this.$go.Shape, "Circle",
          {
            fill: "transparent",
            stroke: null,
            desiredSize: new this.$go.Size(4, 4),
            alignment: spot,
            alignmentFocus: spot,
            portId: name,
            fromSpot: spot, toSpot: spot,
            fromLinkable: output,
            toLinkable: input,
            cursor: "pointer"
          });
      },
      initDiagram() {
        const _this = this
        this.goMake = _this.$go.GraphObject.make;
        this.myDiagram =
          this.goMake(_this.$go.Diagram, document.getElementById(this.myDiagramDivId),
            {
              allowDrop:false,
              allowDelete:false,
              initialContentAlignment: _this.$go.Spot.TopLeft,
              validCycle: _this.$go.Diagram.CycleNotDirected,
              "undoManager.isEnabled": true,
              click: function (e) {
                _this.createOrEditorModuleInstence = "";
              }
            });
        /*鼠标释放时间*/
        this.myDiagram.mouseDrop=function(e,node){
          let tool =_this.myDiagram.currentTool;
          /*如果是拖加，处理拖加逻辑 ToolManager  "Dragging"*/
          if( tool.name != "Dragging"){
            tool.doCancel();
          }
        }
        // // This template represents a whole "record".
        this.myDiagram.nodeTemplate =this.goMake(_this.$go.Node, "Auto",
          {
            mouseEnter: function (e, obj) {
              _this.showPorts(obj.part, true);
            },
            mouseLeave: function (e, obj) {
              _this.showPorts(obj.part, false);
            },
            click: function (e, obj) {
              _this.showProp(obj);
            },
            cursor:"pointer",
          },
          this.goMake(_this.$go.Shape,
            {
              figure:'Square',
              fill: "white",
              width:140,
              strokeWidth: 0,
              fromLinkable: true,
              toLinkable: true,
              cursor: "pointer",
            },
            new _this.$go.Binding("fill", "color")),
          this.goMake(_this.$go.Panel, "Vertical",
            //this.goMake(_this.$go.Picture, { source: "assets/images/servericon_blue.jpg", background: "gray", width: 90, height: 90 }),
            this.goMake(_this.$go.Shape,
              {
                figure:'Database',
                fill: "blue",
                width:90,
                strokeWidth: 2,
                fromLinkable: false,
                toLinkable: false,
                cursor: "pointer",
              }),
            this.goMake(_this.$go.TextBlock,
              {
                margin: 8,
                editable: false,
                stroke:'gray',
                textAlign:'center',
                width:120,
              },
              new _this.$go.Binding("text","name").makeTwoWay())),
          this.makePort("T", _this.$go.Spot.Top, true, true),
          this.makePort("L", _this.$go.Spot.Left, true, true),
          this.makePort("R", _this.$go.Spot.Right, true, true),
          this.makePort("B", _this.$go.Spot.Bottom, true, true)
        )
        this.myDiagram.linkTemplate =this.goMake(_this.$go.Link,
          {
            routing: _this.$go.Link.AvoidsNodes,
            //curve: _this.$go.Link.JumpOver,
            corner: 3,
            toShortLength: 4,
            relinkableFrom: true,
            relinkableTo: true,
            reshapable: true,
            resegmentable: true,
          },
          this.goMake(_this.$go.Shape),
          this.goMake(_this.$go.Shape,{ toArrow: "Circle"})
        );
        this.myDiagram.model =
          this.goMake(_this.$go.GraphLinksModel,
            {
              linkFromPortIdProperty: "module_instance_key",
              linkToPortIdProperty: "module_instance_key",
              nodeKeyProperty: "module_instance_key",
              nodeDataArray: [],
              linkDataArray: []
            });
      },
      initData() {
        const _this=this;
        let URL1 = config.port('metadataManage') + 'getChildInstanceForGojs';
        var param = {
          parentId: this.currentModuleInstance.module_id,
          moduleInstanceId: this.currentModuleInstance.module_instance_id,
          treeType: this.currentModuleInstance.tree_type
        };
        this.$http.post(URL1,param).then((response) => {
          this.myDiagram.model.nodeDataArray=response.data.data.nodeData;
          this.myDiagram.model.linkDataArray=response.data.data.linkData.fks;
        }).catch(function (response) {
        })

        let URL = config.port('metadataManage') + 'getCreateModelByModelName';
        var p = {
          parentId: this.currentModuleInstance.module_id,
          moduleInstanceId: this.currentModuleInstance.module_instance_id,
          treeType: this.currentModuleInstance.tree_type
        };
        /*初始化field模板*/
        p["id"]=config.FIXED_VARIABLE.CONSTANT_DATAABASE_MODEL_ID;
        this.$http.post(URL, JSON.parse(JSON.stringify(p))).then((response) => {
          let moduleInfo = response.data.data.moduleInfo;
          let attrs = moduleInfo.attrs;
          let properties = JSON.parse(JSON.stringify(_this.inspectorProperties));
          for(var i=0;i<attrs.length;i++){
            let attr = attrs[i];
            properties[("_self_"+attr.att_name_en)]={
              readOnly: false,
              show:attr.attrExt.is_show!=0,
              type: attr.attrExt.show_type,
              display:attr.att_name_cn,
              //choices:attr.attrExt.selections,
              choices: function(node, propName) {
                return attr.attrExt.selections;
              }
            }
          }
          /*添加拦截器--当前选中节点*/
          this.nodeInspector = new Inspector(_this.myInspectorNodeDivId, _this.myDiagram,
            {
              includesOwnProperties: true,
              inspectSelection:false,
              properties:properties
            });
        }).catch(function (response) {
        })
      },
      showProp(obj){
        if(Inspector.showIfGroup(obj)){
          this.createOrEditorModuleInstence="group";
          this.groupInspector.inspectObject(obj);
        }else if(Inspector.showIfLink(obj)){
          this.createOrEditorModuleInstence="link";
          this.linkInspector.inspectObject(obj);
        }else{
          this.createOrEditorModuleInstence="node";
          this.nodeInspector.inspectObject(obj);
        }
      },
      /*提交建库建表元数据*/
      submitModuleInstence() {
        //将node link 以及delete全部提交
        let URL = config.port('metadataManage') + 'saveOrUpdateMetaDataGojs';
        var param = {
          deletedObjects: this.deletedObjects,
          nodeArray: this.myDiagram.model.nodeDataArray,
          linkArray: this.myDiagram.model.linkDataArray
        }
        this.$http.post(URL, param).then((response) => {
          this.$message({
            message: response.data.message,
            type: response.data.status==1?'info':"error",
            showClose: true
          });
        }).catch(function (response) {
        })
      }
    }
  }
</script>
<style scoped lang="less">

  .item {
  .label {
    display: inline-block;
    text-align: left;
  }
  }
</style>
