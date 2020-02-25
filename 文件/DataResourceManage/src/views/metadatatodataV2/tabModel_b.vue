<template>
  <el-row>
    <div ref="myDiagramDiv" :id="myDiagramDivId" style="width: 100%;height: 300px;border: 0;"></div>
  </el-row>
</template>

<script>
  import Vue from 'vue'
  import Component from 'vue-class-component'
  import config from '../../config.ts'
  import Qs from 'qs'
  import {Watch} from 'vue-property-decorator'

  export default {
    data() {
      return {
        myDiagram:'',
        goMake:'',
        myDiagramDivId:''
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
    },
    mounted:function(){
      this.initDiagram();
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
              allowDrop: true,
              initialContentAlignment: _this.$go.Spot.TopLeft,
              validCycle: _this.$go.Diagram.CycleNotDirected,
              "undoManager.isEnabled": true,
            });
        this.myDiagram.nodeTemplate =this.goMake(_this.$go.Node,
          {
            deletable: false,
          },
          this.goMake(_this.$go.Panel, "Auto",
              new _this.$go.Binding("key", "module_instance_id"),
              new _this.$go.Binding("groupKey", "group"),
              this.goMake(_this.$go.Shape, "Rectangle", {
                row:1,
                fill: "white",
                stroke: null,
                width:200,
                height:25
              }),
              this.goMake(_this.$go.TextBlock, {
                  font: "bold 9pt Helvetica, Arial, sans-serif",
                  stroke: "#5b5b5b",
                  width:180,
                  editable: true,
               },
                new _this.$go.Binding("text","name").makeTwoWay())
          )
        );

        this.myDiagram.linkTemplate =
          this.goMake(_this.$go.Link,
            {
              curve: _this.$go.Link.JumpOver,
              routing: _this.$go.Link.AvoidsNodes,
              relinkableFrom: true,
              relinkableTo: true,
              selectionAdorned: false
            },
            this.goMake(_this.$go.Shape, {strokeWidth: 1.5}),
            this.goMake(_this.$go.Shape, {toArrow: "Standard", stroke: null})
          );

        this.myDiagram.model =
          this.goMake(_this.$go.GraphLinksModel,
            {
              linkFromPortIdProperty: "module_instance_id",
              linkToPortIdProperty: "module_instance_id",
              "Changed": function (e) {
              },
              nodeDataArray: [],
              linkDataArray: []
            });
        this.myDiagram.groupTemplate =
          this.goMake(_this.$go.Group, "Vertical", {
              ungroupable: false,
              layout: this.goMake(_this.$go.GridLayout,
                {
                  wrappingColumn: 1,
                }),
            },
            new _this.$go.Binding("key", "module_instance_id"),
            this.goMake(_this.$go.Panel, "Table",
              {
                alignment:_this.$go.Spot.Top,
              },
              this.goMake(_this.$go.Shape, "Rectangle", {
                row:0,
                fill: "#00A9C9",
                stroke: null,
                width:200,
                height:30,
              }),
              this.goMake(_this.$go.TextBlock, {
                  row:0,
                  stroke: 'gray',
                  textAlign: "left",
                  width:180,
                  editable: true,
                },
                new _this.$go.Binding("text","name").makeTwoWay()),
                this.goMake(_this.$go.Shape, "Rectangle", {
                  row:1,
                  fill: "white",
                  stroke: null,
                  width:200,
                }),
                this.goMake(_this.$go.Placeholder, {
                  padding: 0,
                  alignment: go.Spot.TopLeft,
                  row:1,
                }),
            ));
        this.initData();
      },

      initData() {
        let URL = config.port('metadataManage') + 'getChildInstanceForGojs';
        var p = {
          parentId: this.currentModuleInstance.module_id,
          moduleInstanceId: this.currentModuleInstance.module_instance_id,
          treeType: this.currentModuleInstance.tree_type
        };
        this.$http.post(URL, p).then((response) => {
          let nodes = [];
          let groupNode = JSON.parse(JSON.stringify(this.currentModuleInstance));
          groupNode.isGroup=true;
          groupNode.key=this.currentModuleInstance.module_instance_id;
          nodes.push(groupNode);

          let fields = response.data.data;
          if(fields&&fields.length>0){
            for(var i=0;i<fields.length;i++){
              let node = fields[i];
              node.group = this.currentModuleInstance.module_instance_id;
              node.key = node.module_instance_id;
              nodes.push(node);
            }
          }
          this.myDiagram.model.nodeDataArray = nodes;
        }).catch(function (response) {
        })
      },
      /*提交建库建表元数据*/
      /*submitModuleInstence() {
        let data = [];
        //将列的元数据保存
        for (var j = 0; j < this.tabFormOrTableDatas.length; j++) {
          let val = this.tabFormOrTableDatas[j];
          let metaVules = [];
          for (var i = 0; i < this.tabModuleInfo.attrs.length; i++) {
            let attr = this.tabModuleInfo.attrs[i];
            if (val[attr.att_name_en]) {
              var metaVule = {
                id: val[attr.att_name_en]['meta_value_id'] || '',
                att_id: attr.id,
                parent_id: this.tabModuleInfo.meta_value_pid || this.tabModuleInfo.module_instance_id,
                row_id: val['module_instance_id'],
                meta_value: val[attr.att_name_en]['meta_value']
              };
              metaVules.push(metaVule);
            }
          }
          data.push(metaVules);
        }

        let URL = config.port('metadata') + 'saveOrUpdateMetaData';
        var param = new Object();
        param.metaJson = JSON.stringify({
          table: 'meta_value',
          data: data
        });
        this.$http.post(URL, Qs.stringify(param)).then((response) => {
        }).catch(function (response) {
        })
      },*/
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
