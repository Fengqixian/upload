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
          this.goMake(_this.$go.Diagram, this.myDiagramDivId,
            {
              allowDrop: true,
              initialContentAlignment: _this.$go.Spot.TopLeft,
              "undoManager.isEnabled": true,
              click: function (e) {
                _this.createOrEditorModuleInstence = "";
              }
            });
        this.myDiagram.nodeTemplate = this.goMake(_this.$go.Node, "Auto",
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
            margin: 0,
            movable: false,
          },
          this.goMake(_this.$go.Shape,
            {
              fill: "white",
              width: 200,
              strokeWidth: 0,
              fromLinkable: true,
              toLinkable: true,
              cursor: "pointer",
              margin: 0,
            },
            new _this.$go.Binding("fill", "isUpdated",function(value){return value?"lightgray":"white";})),
          this.goMake(_this.$go.TextBlock,
            {
              margin: 5,
              editable: false,
              stroke: 'gray',
              textAlign: 'left',
              width: 100,
              maxLines:1,
              alignment: _this.$go.Spot.TopLeft,
            },
            new _this.$go.Binding("text", "name").makeTwoWay()),
          this.goMake(_this.$go.TextBlock,
            {
              margin: 5,
              editable: false,
              stroke: 'lightgreen',
              textAlign: 'right',
              maxLines:1,
              width: 80,
              alignment: _this.$go.Spot.TopRight,
            },
            new _this.$go.Binding("text", "_self_data_type",function(value){
              return (value===undefined||value["meta_value"]===undefined)?value:value["meta_value"];
            }).makeTwoWay()),
          this.makePort("T", _this.$go.Spot.Top, true, true),
          this.makePort("L", _this.$go.Spot.Left, true, true),
          this.makePort("R", _this.$go.Spot.Right, true, true),
          this.makePort("B", _this.$go.Spot.Bottom, true, true)
        );
        /*鼠标释放时间*/
        this.myDiagram.mouseDrop = function (e, node) {
          let tool = _this.myDiagram.currentTool;
          /*如果是拖加，处理拖加逻辑 ToolManager  "Dragging"*/
          if (tool.name != "Dragging") {
            /*拖拽增加节点 如果某一个节点不满足条件，全部不可以拖*/
            tool = _this.myDiagram.toolManager.draggingTool;
            var it = tool.draggingParts.iterator;
            let nodeDatas = _this.myDiagram.model.nodeDataArray;
            while (it.next()) {
              let data = it.value.data;
              if (data.module_id != _this.currentModuleInstance.module_id) {
                _this.myDiagram.currentTool.doCancel();
                break;
              }
              var count = 0;
              for (var k = 0; k < nodeDatas.length; k++) {
                if (nodeDatas[k].tree_node_key == data.tree_node_key) {
                  count++;
                }
              }
              if (count > 1) {
                _this.myDiagram.currentTool.doCancel();
                break;
              }
              /*加载字段*/
              _this.loadData(data,function(res){
                //过滤掉与该表不相关的表，只要该表和连线
                let fields = res.nodeData||[];
                if (fields && fields.length > 0) {
                  for (var i = 0; i < fields.length; i++) {
                    if(fields[i].module_id==config.FIXED_VARIABLE.CONSTANT_COLUMN_MODEL_ID&&fields[i].parent_module_instance_id==data.module_instance_id){
                      _this.myDiagram.model.addNodeData(fields[i]);
                    }
                  }
                }
                let links = res.linkData.fks||[];
                if (links && links.length > 0) {
                  for (var i = 0; i < links.length; i++) {
                    _this.myDiagram.model.addLinkData(links[i]);
                  }
                }
              });
            }
          }
        }
        this.myDiagram.linkTemplate =
          this.goMake(_this.$go.Link,
            {
              toEndSegmentLength: 10,
              fromEndSegmentLength: 10,
              curve: _this.$go.Link.JumpOver,
              routing: _this.$go.Link.Orthogonal,
              relinkableFrom: false,
              relinkableTo: false,
              selectionAdorned: true,
              cursor:"pointer",
              click: function (e, obj) {
                _this.showProp(obj);
              },
            },
            this.goMake(_this.$go.Shape, {strokeWidth: 1.5,stroke:"black"},
              new _this.$go.Binding("stroke", "isUpdated",function(value){return value?"lightgray":"black";})
            ),
            this.goMake(_this.$go.Shape, {toArrow: "Standard", stroke: null})
          );

        this.myDiagram.model =
          this.goMake(_this.$go.GraphLinksModel,
            {
              /*linkFromPortIdProperty: "module_instance_id",
              linkToPortIdProperty: "module_instance_id",*/
              nodeGroupKeyProperty: "nodeGroup",
              nodeIsGroupProperty: "nodeIsGroup",
              nodeKeyProperty: "module_instance_id",
              "Changed": function (e) {
              },
              nodeDataArray: [],
              linkDataArray: []
            });
        this.myDiagram.addDiagramListener("LinkDrawn", function (obj, param) {
          let link = obj.subject;//新加的连线
          let data = link.data;
          let from = link.fromNode.data;
          let to = link.toNode.data;
          //更新关联表，关联字段，所属表，所属字段，name，fk_name
          let fkName="FK_"+to.nodeGroup+"_"+from.nodeGroup+"_"+new Date().getTime();
          _this.myDiagram.model.setDataProperty(data, "name", fkName);
          _this.myDiagram.model.setDataProperty(data, "isUpdated", true);
          _this.myDiagram.model.setDataProperty(data, "_self_fk_name",{meta_value:fkName, meta_value_id:""});
          _this.myDiagram.model.setDataProperty(data, "_self_table_name",{meta_value:to.nodeGroup, meta_value_id:""});
          _this.myDiagram.model.setDataProperty(data, "_self_column_name",{meta_value:to.module_instance_id, meta_value_id:""});
          _this.myDiagram.model.setDataProperty(data, "_self_association_column",{meta_value:from.module_instance_id, meta_value_id:""});
          _this.myDiagram.model.setDataProperty(data, "_self_association_table",{meta_value:from.nodeGroup, meta_value_id:""});
          _this.myDiagram.model.setDataProperty(data, "module_id",config.FIXED_VARIABLE.CONSTANT_FK_MODEL_ID);
          _this.showProp(link);
        });
        this.myDiagram.addDiagramListener("SelectionDeleted", function (obj, param) {
          let objs = obj.subject;
          var it = objs.iterator;
          while (it.next()) {
            var item = it.value.data;
            _this.deletedObjects.push(item);
          }
        });
        this.myDiagram.groupTemplate =
          this.goMake(_this.$go.Group, "Vertical", {
              ungroupable: true,
              layout: this.goMake(_this.$go.GridLayout,
                {
                  wrappingColumn: 1,
                  spacing: new _this.$go.Size(0),
                }),
              click: function (e, obj) {
                _this.showProp(obj);
              },
              cursor:"pointer",
            },
            this.goMake(_this.$go.Panel, "Table",
              {
                alignment: _this.$go.Spot.Top,
              },
              this.goMake(_this.$go.Shape, "Rectangle", {
                row: 0,
                fill: "gray",
                stroke: null,
                width: 200,
                height: 30,
              },new _this.$go.Binding("fill", "isUpdated",function(value){return value?"lightgray":"white";})),
              this.goMake(_this.$go.TextBlock, {
                  row: 0,
                  stroke: 'white',
                  textAlign: "left",
                  width: 180,
                  editable: false,
                },
                new _this.$go.Binding("text", "name").makeTwoWay()),
              this.goMake(_this.$go.Shape, "Rectangle", {
                row: 1,
                fill: "white",
                stroke: null,
                width: 200,
              }),
              this.goMake(_this.$go.Placeholder, {
                padding: 0,
                alignment: go.Spot.TopLeft,
                row: 1,
              }),
            ));
      },
      /*右边属性框*/
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
      initData() {
        const _this=this;
        //将当前节点复制过来
        let groupNode = JSON.parse(JSON.stringify(this.currentModuleInstance));
        let nodeDatas=[];
        let linkDatas=[];
        nodeDatas.push(groupNode);
        this.myDiagram.model.addNodeData(groupNode);
        _this.loadData(_this.currentModuleInstance,function (res) {
          let fields = res.nodeData||[];
          nodeDatas = nodeDatas.concat(fields);
          let links = res.linkData.fks||[];
          linkDatas = linkDatas.concat(links);
          _this.myDiagram.model.nodeDataArray=nodeDatas;
          _this.myDiagram.model.linkDataArray=linkDatas;
        });

        let URL = config.port('metadataManage') + 'getCreateModelByModelName';
        var p = {
          parentId: this.currentModuleInstance.module_id,
          moduleInstanceId: this.currentModuleInstance.module_instance_id,
          treeType: this.currentModuleInstance.tree_type
        };
        /*初始化field模板*/
        p["id"]=config.FIXED_VARIABLE.CONSTANT_COLUMN_MODEL_ID;
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
        /*初始化table模板*/
        p["id"]=config.FIXED_VARIABLE.CONSTANT_TABLE_MODEL_ID;
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
          this.groupInspector = new Inspector(_this.myInspectorGroupDivId, _this.myDiagram,
            {
              includesOwnProperties: true,
              inspectSelection:false,
              properties:properties
            });
        }).catch(function (response) {
        })
        /*初始化link模板*/
        p["id"]=config.FIXED_VARIABLE.CONSTANT_FK_MODEL_ID;
        this.$http.post(URL, JSON.parse(JSON.stringify(p))).then((response) => {
          let moduleInfo = response.data.data.moduleInfo;
          let attrs = moduleInfo.attrs;
          let properties = JSON.parse(JSON.stringify(_this.inspectorProperties));
          properties["from"] = {readOnly: true, show:true}
          properties["to"] = {readOnly: true, show:true}
          for(var i=0;i<attrs.length;i++){
            let attr = attrs[i];
            properties[("_self_"+attr.att_name_en)]={
              readOnly: false,
              show:attr.attrExt.is_show!=0,
              type: attr.attrExt.show_type,
              display:attr.att_name_cn,
              choices: function(node, propName) {
                return attr.attrExt.selections;
              }
            }
          }
          /*添加拦截器--当前选中节点*/
          this.linkInspector = new Inspector(_this.myInspectorLinkDivId, _this.myDiagram,
            {
              includesOwnProperties: true,
              inspectSelection:false,
              properties:properties
            });
        }).catch(function (response) {
        })

      },
      /*加载子节点*/
      loadData(data,callback) {
        let URL = config.port('metadataManage') + 'getChildInstanceForGojs';
        var p = {
          parentId: data.module_id,
          moduleInstanceId: data.module_instance_id,
          treeType: data.tree_type
        };
        this.$http.post(URL, p).then((response) => {
          if(callback){
            callback(response.data.data);
            return false;
          }
        }).catch(function (response) {
        })
      },
      submitModuleInstence(){
        //将node link 以及delete全部提交
        let URL = config.port('metadataManage') + 'saveOrUpdateMetaDataGojs';
        var param ={
          deletedObjects:this.deletedObjects,
          nodeArray:this.myDiagram.model.nodeDataArray,
          linkArray:this.myDiagram.model.linkDataArray
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
