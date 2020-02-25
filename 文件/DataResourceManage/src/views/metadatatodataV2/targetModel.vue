<template>
  <el-row>
    <el-row>
      <el-col :span="createOrEditorModuleInstence?18:24">
        <div ref="myDiagramDiv" :id="myDiagramDivId" style="outline: white 0px solid;width: 100%;height: 543px;border: 0;background: #4D4D4D;"></div>
      </el-col>
      <el-col :span="6"  style="display: inline-block; vertical-align: top;" v-show="createOrEditorModuleInstence">
        <div :id="myInspectorNodeDivId" class="inspector" style="height: 543px;border: 0;" v-show="createOrEditorModuleInstence=='node'"></div>
        <div :id="myInspectorLinkDivId" class="inspector" style="height: 543px;border: 0;" v-show="createOrEditorModuleInstence=='link'"></div>
        <div :id="myInspectorGroupDivId" class="inspector" style="height: 543px;border: 0;" v-show="createOrEditorModuleInstence=='group'"></div>
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

        opacity : 1,
        down : true,
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
      initDiagram() {
        const _this = this
        this.goMake = _this.$go.GraphObject.make;
        this.myDiagram =
          this.goMake(_this.$go.Diagram, document.getElementById(this.myDiagramDivId),
            {
              isReadOnly:true,
              initialContentAlignment: go.Spot.TopLeft,
              //"textEditingTool.starting": go.TextEditingTool.SingleClick,
              layout: this.goMake(_this.$go.TreeLayout, {
                layerSpacing:83,
                nodeSpacing:83,
              }),
              "undoManager.isEnabled": true,
              click: function (e) {
                _this.createOrEditorModuleInstence = "";
              }
            });
        this.myDiagram.nodeTemplate =
          this.goMake(_this.$go.Node, "Auto",
            {
              margin:0,
              cursor:"pointer",
              click: function (e, obj) {
                _this.showProp(obj);
              }
            },
            this.goMake(_this.$go.Shape, "Rectangle",
              { fill: '#8C8C8C', stroke: null },
              new _this.$go.Binding("fill", "color")),
            this.goMake(_this.$go.Panel, "Table",
              this.goMake(_this.$go.RowColumnDefinition, { column: 0, separatorStroke: "black" }),
              this.goMake(_this.$go.RowColumnDefinition, { column: 1, separatorStroke: "black", background: "#BABABA" }),
              this.goMake(_this.$go.RowColumnDefinition, { row: 0, separatorStroke: "black" }),
              this.goMake(_this.$go.TextBlock, "",
                { row: 0,
                  wrap: _this.$go.TextBlock.None, margin: 5, width: 90,
                  isMultiline: false, textAlign: 'left',
                  font: '10pt  Segoe UI,sans-serif', stroke: 'white' },
                new _this.$go.Binding("text", "name").makeTwoWay()),
              this.goMake(_this.$go.TextBlock, "",
                { column: 1, row: 0,
                  wrap: _this.$go.TextBlock.None, margin: 2, width: 25,
                  isMultiline: false, editable: true, textAlign: 'center',
                  font: '10pt  Segoe UI,sans-serif', stroke: 'black' },
                new _this.$go.Binding("text", "score1").makeTwoWay()),
            )
          );
        this.myDiagram.groupTemplate =
          this.goMake(_this.$go.Group,"Vertical",{
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
            this.goMake(_this.$go.Panel,"Table",
              this.goMake(_this.$go.Shape, "Rectangle",
                {
                  row:0,
                  fill: "rgba(128,128,128,0.8)",
                  stroke: "gray",
                  strokeWidth: 3,
                  height:25,
                  width:128,
                  margin:0
                }),
              this.goMake(_this.$go.TextBlock,
                {
                  wrap: _this.$go.TextBlock.None,
                  row:0,
                  stroke: 'white',
                  font: "bold 15px sans-serif",
                  isMultiline: true,
                  height:25,
                  width:120,
                  editable: false
                },
                new _this.$go.Binding("text", "name").makeTwoWay(),
                new _this.$go.Binding("stroke", "color")),
              this.goMake(_this.$go.Placeholder, { padding: 0,margin:0,row:1})
            )
          );


        this.myDiagram.nodeTemplateMap.add("temp",this.goMake(_this.$go.Node, "Auto",
          { selectable: false },
          this.goMake(_this.$go.Shape, "Circle",
            { fill: '#8C8C8C', stroke: null ,width:5,height:5},
            new _this.$go.Binding("fill", "color")),
        ))
        this.myDiagram.linkTemplateMap.add("",this.goMake(_this.$go.Link,
          this.goMake(_this.$go.Shape,{
              toArrow: "standard",
              fill:"#33D6A2",
              width:0,
              height:0,
              strokeWidth:0,
              segmentOrientation:_this.$go.Link.OrientAlong,
            },new _this.$go.Binding("width","arrowSize"),
            new _this.$go.Binding("height","arrowSize")),
          this.goMake(_this.$go.Shape, {
            isPanelMain: true,
            stroke: "#33D6A2",
            strokeWidth: 1,
            name: "PIPE",
            strokeDashArray: [3,4]
          },new _this.$go.Binding("stroke","color",function(value){if(value) return "yellow";
              return "#33D6A2";
          })),
          this.goMake(_this.$go.Shape,"Circle",{
            name:"CIRCLE",
            segmentIndex:1,
            segmentOffset:new _this.$go.Point(0,0),
            segmentOrientation:_this.$go.Link.OrientAlong,
            fill:"#33D6A2",
            strokeWidth:0,
            width:6,
            height:6,
          }),
        ));
        this.myDiagram.linkTemplateMap.add("temp",this.goMake(_this.$go.Link,
          {
            routing: _this.$go.Link.Normal,
            fromShortLength:1,
            toShortLength:1,
          },
          this.goMake(_this.$go.Shape,{
              toArrow: "standard",
              fill:"#33D6A2",
              width:0,
              height:0,
              strokeWidth:0,
              segmentOrientation:_this.$go.Link.OrientAlong,
            },new _this.$go.Binding("width","arrowSize"),
            new _this.$go.Binding("height","arrowSize")),
          this.goMake(_this.$go.Shape, {
            isPanelMain: true,
            stroke: "#33D6A2",
            strokeWidth: 1,
            name: "PIPE",
            strokeDashArray: [3,4]
          }),
          /*this.goMake(_this.$go.TextBlock, {
                    stroke: "#33D6A2",
                    margin:new _this.$go.Size(40),
                    height:20
                },new _this.$go.Binding("text","comment")),*/
          this.goMake(_this.$go.Shape,"Circle",{
            name:"CIRCLE",
            segmentIndex:1,
            segmentOffset:new _this.$go.Point(0,0),
            segmentOrientation:_this.$go.Link.OrientAlong,
            fill:"#33D6A2",
            strokeWidth:0,
            width:6,
            height:6,
          }),
        ));
        this.myDiagram.model =
          this.goMake(_this.$go.GraphLinksModel,
            {
              nodeGroupKeyProperty:"parentId",
              nodeCategoryProperty:"type",
              linkCategoryProperty:"type",
              nodeKeyProperty:"id",
              //linkFromKeyProperty:"from",
              //linkToKeyProperty:"to",
              nodeDataArray: [],
              linkDataArray: []
            });
      },
      initData() {
        const _this=this;
        let model = {
          id: this.currentModuleInstance.module_id,
          parentId: this.currentModuleInstance.module_id,
          moduleInstanceId: this.currentModuleInstance.module_instance_id,
          treeType: this.currentModuleInstance.tree_type
        };
        let URL1 = config.port('dataLineage') + '/getDataLineage';
        this.$http.post(URL1, model).then((response) => {
          this.myDiagram.model.nodeDataArray=response.data.data.nodeDataArray;
          this.myDiagram.model.linkDataArray=response.data.data.linkDataArray;
          this.startAnomation();
        }).catch(function (response) {
        });
      },
   startAnomation(){
        const _this =this;
    if(this.timer){
      clearInterval(this.timer);
    }
     this.timer=setInterval(function(){
       _this.loop();
    },50);
  },
  loop(){
        const _this = this;
    let diagram = _this.myDiagram;
    var oldskips = diagram.skipsUndoManager;
    diagram.skipsUndoManager = true;

    if (_this.down) _this.opacity = _this.opacity - 0.02;
    else _this.opacity = _this.opacity + 0.05;
    if (_this.opacity <= 0.3) { _this.down = !_this.down; _this.opacity = 0.3;}
    if (_this.opacity > 1) { _this.down = !_this.down; _this.opacity = 1;}

    if(diagram.links.count>0){
      diagram.links.each(function(link) {
        var shape = link.findObject("PIPE");
        let rec =shape.naturalBounds;
        let max =Math.sqrt(Math.pow(rec.width,2)+Math.pow(rec.height,2));
        let circle = link.findObject("CIRCLE");

        /*if(link.category){
            circle.segmentOffset=new go.Point(max/2,0);
            circle.opacity = opacity;
            return;
        }*/

        var off = shape.strokeDashOffset + 1;
        shape.strokeDashOffset = (off >=8) ? 0 : off;
        circle.opacity = _this.opacity;

        /*let current = circle.segmentOffset.x+1;//(max/100);
        if(current>(0.9*max)){
            current=0.1*max;
        }*/
        let current = circle.segmentOffset.x-1;//(max/100);
        if(current<(0.1*max)){
          current=0.9*max;
        }
        circle.segmentOffset=new go.Point(current,0);
        circle.opacity = _this.opacity;
      });
    }

    diagram.skipsUndoManager = oldskips;
  },
      showProp(obj){
        var _this=this;
        //获取Obj的信息
        let URL = config.port('metadataManage') + 'getMetadataModelTreeForEdit';
        var p = {
          parentId: obj.data.moduleId,
          moduleInstanceId: obj.data.id,
          treeType: "instance",
          id:obj.data.moduleId
        };
        this.$http.post(URL, p).then((response) => {
          let moduleInfo = response.data.data.moduleInfo;
          let attrs = moduleInfo.attrs;
          let properties = JSON.parse(JSON.stringify(_this.inspectorProperties));
          for(var i=0;i<attrs.length;i++){
            let attr = attrs[i];
            properties[attr.att_name_en]={
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
          _this.nodeInspector = new Inspector(_this.myInspectorNodeDivId, _this.myDiagram,
            {
              includesOwnProperties: false,
              inspectSelection:false,
              properties:properties
            });
          _this.createOrEditorModuleInstence="node";
          let datas = response.data.data.datas[0]||response.data.data.datas;
          for(var key in obj.data){
            datas[key] = obj.data[key];
          }
          _this.nodeInspector.inspectObject(datas);
        }).catch(function (response) {
        })
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
