<template>
    <div class="sheets-container header-operate-btn" id="sheets-container">
        <div class="operate">
            <!--<span class="btn"><i class="iconfont icon-bianji"></i>编辑</span>-->
            <span class="btn" @click="handleDeleteFK"><i class="iconfont icon-shanchu"></i>删除字段</span>
            <span class="btn" @click.stop.prevent="createTable"><i class="iconfont icon-xinzeng"></i>新增</span>
        </div>
        <div ref="myDiagramDiv" id="myDiagramDiv" class="container-wrapper" style="width: 100%;"></div>
        <!--弹出框区域-->
        <el-dialog title="编辑元元模型"
                   :visible.sync="dialogVisible"
                   v-if='dialogVisible'
                   width="80%"
                   :close-on-click-modal="false">
            <EditMetaToMetaModel :table="tableName" v-on:refreshbizlines="closeDialog"></EditMetaToMetaModel>
        </el-dialog>
        <el-dialog :close-on-click-modal="false"
                   title="提示"
                   :visible.sync="tipDialogVisible"
                   width="30%">
            <span v-if="tableOrFK==='table'">是否删除 : {{tipContent}} 表</span>
            <span v-else-if="tableOrFK==='FK'">是否删除 : {{tipContent}} 字段</span>
            <span slot="footer" class="dialog-footer">
                <el-button @click="tipDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="deleteTableOrFK">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import Component from 'vue-class-component'
    import {State, Mutation} from 'vuex-class'
    import config from '../../config.ts'
    import Qs from 'qs'
    import EditMetaToMetaModel from './editMetaToMetaModel'

    @Component({
        name: 'showMetaData',
        components: {
            EditMetaToMetaModel
        }
    })
    export default class showMetaData extends Vue {
        myThis: any = this
        @State loadingFlag
        @Mutation setLoadingFlag
        tables: any = []
        myDiagram: any = ''
        goMake: any = ''
        dialogVisible = false;
        tableName: any = "";
        // 存储group信息
        oldGroupDataArray: Array<any> = []
        // 删除字段或者表的提醒
        tipDialogVisible: boolean = false
        // 提示内容
        tipContent: string = ''
        // 删除表还是字段
        tableOrFK: string = ''
        // 删除表的名称
        deleteTableName: string = ''

        //初始化访问
        created() {
            this.$nextTick(() => {
                this.initDiagram()
                this.getTableAndCheck()
            })
        }

        // 提示删除[确认删除]
        deleteTableOrFK() {
            this.tipDialogVisible = false
            if (this.tableOrFK === 'FK') { // 删除字段
                this.deleteFK()
            } else if (this.tableOrFK === 'table') { // 删除表
                this.deleteTable(this.deleteTableName)
            }
        }

        // 点击删除字段按钮
        handleDeleteFK() {
            this.tableOrFK = 'FK'
            const checkedFK = this.analysisDeleteDK()
            this.tipContent = ''
            let deleteFKNameArr: Array<string> = []
            checkedFK.forEach(item => {
                deleteFKNameArr.push(item['name'])
            })
            // deleteFKNameArr.push(' 字段')
            if (deleteFKNameArr.length === 0) {
                this.$message.warning("请选择被删除的字段");
                return;
            }
            this.tipDialogVisible = true;
            this.tipContent = deleteFKNameArr.join('、')
        }

        // 点击删除表按钮
        handleDeleteTable(tableName) {
            this.tipDialogVisible = true
            this.tableOrFK = 'table'
            this.deleteTableName = tableName
            this.tipContent = tableName
        }

        // 解析出删除字段的data
        analysisDeleteDK() {
            const {nodeDataArray} = JSON.parse(this.myDiagram.model.toJson())
            let fields: Array<object> = []
            nodeDataArray.forEach(item => {
                if (item.fields) {
                    fields = [...fields, ...item.fields]
                }
            })
            let checkedFK: Array<object> = []
            checkedFK = fields.filter(item => {
                return item['checked']
            })
            return checkedFK
        }

        // 删除字段
        deleteFK() {
            const checkedFK = this.analysisDeleteDK()
            let submitData: Array<object> = []
            checkedFK.forEach(item => {
                submitData.push({
                    tableName: item['table'],
                    attid: item['tag_id'],
                })
            })
            let URL = config.port('metadata') + 'deleteMetaToMetaColumns';
            this.$http.post(URL, {
                data: submitData,
                pageAjax: {}
            }).then((response) => {
                if (response.data.status === '200') {
                    this.$message.success(response.data.message)
                } else {
                    this.$message.error(response.data.message)
                }
                this.isAllowLinkDel = false
                this.getTableAndCheck()
            })
        }

        // 删除表
        deleteTable(table) {
            let URL = config.port('metadata') + 'deleteMetaToMetaTableAndColumns';
            let param = {}
            const submitData = {
                tableName: table
            }
            param['metaJson'] = JSON.stringify(submitData);
            this.$http.post(URL, Qs.stringify(param)).then((response) => {
                if (response.data.status === '200') {
                    this.$message.success(response.data.message)
                } else {
                    this.$message.error(response.data.message)
                }
                this.isAllowLinkDel = false
                this.getTableAndCheck()
            })
        }

        // 新增
        createTable() {
            this.handleRowChange('')
        }

        // 编辑字段提交
        submitTextEdit(data, index, previousText, currentText, type): void {
            if (currentText === previousText) return
            let thisTextData = data.fields[index]
            let submitData = {
                table: data['key'],
                data: [{
                    parent_id: "0",
                    row_id: data['key'],
                    metatype: "column",
                    id: thisTextData.tag_id
                }]
            }
            if (type === 'name') {
                if (currentText) {
                    submitData.data[0]['name'] = currentText
                    submitData.data[0]['description'] = thisTextData['description']
                } else {
                    this.$message.error('字段名称不能为空')
                    return
                }
            } else if (type === 'description') {
                submitData.data[0]['description'] = currentText
                submitData.data[0]['name'] = thisTextData['name']
            }

            let URL = config.port('metadata') + 'saveOrUpdateMetaToMetaModule';
            let param = {}
            param['metaJson'] = JSON.stringify(submitData);
            this.$http.post(URL, Qs.stringify(param)).then((response) => {
                if (response.data.status === '200') {
                    this.$message.success('修改成功')

                } else {
                    if (type === 'name') {
                        thisTextData['name'] = previousText
                    } else if (type === 'description') {
                        thisTextData['description'] = previousText
                    }

                    this.isAllowLinkDel = false
                    this.getTableAndCheck()
                    this.$message.error(response.data.message)
                }
            }).catch((response) => {

            })
        }

        // 初始化goJs
        initDiagram() {
            const _this = this
            this.goMake = _this.$go.GraphObject.make;
            this.myDiagram =
                this.goMake(_this.$go.Diagram, document.getElementById('myDiagramDiv'),
                    {
                        initialContentAlignment: _this.$go.Spot.TopLeft,
                        validCycle: _this.$go.Diagram.CycleNotDirected,  // don't allow loops
                        "undoManager.isEnabled": true,
                        padding: 10,
                        layout: _this.goMake(_this.$go.GridLayout, {
                            comparer: _this.$go.GridLayout.smartComparer,//设置从小到大排序
                            spacing: _this.$go.Size.parse("60 60"),//设置节点间隔

                        })
                    }
                );

            var fieldTemplate =
                this.goMake(_this.$go.Panel, "TableRow",
                    new _this.$go.Binding("portId", "tag_id"),
                    {
                        background: "white",
                        fromSpot: _this.$go.Spot.Right,
                        toSpot: _this.$go.Spot.Left,
                        fromLinkable: true,
                        toLinkable: true
                    },
                    _this.goMake(_this.$go.TextBlock, {
                        column: 0,
                        text: '选择',
                        visible: false,
                        margin: new _this.$go.Margin(5, 3),
                        stretch: _this.$go.GraphObject.Horizontal,
                        font: "bold 14px sans-serif",
                        wrap: _this.$go.TextBlock.None,
                        overflow: _this.$go.TextBlock.OverflowEllipsis,
                        fromLinkable: true,
                        toLinkable: true,
                        editable: true
                    }, new _this.$go.Binding("visible", "isShowCheckedText")),
                    this.goMake("CheckBox", "checked", {
                            column: 0,
                            defaultAlignment: _this.$go.Spot.Left,
                            margin: new _this.$go.Margin(5, 10),
                            visible: true,
                            cursor: 'pointer',
                        },
                        { // _doClick is executed within a transaction by the CheckBoxButton click function
                            "_doClick": function (e, obj) {
                                obj.part.movable = !obj.part.movable;  // toggle the Part.movable flag
                            }
                        },
                        new _this.$go.Binding("visible", "visible"),),
                    this.goMake(_this.$go.TextBlock,
                        {
                            column: 1,
                            margin: new _this.$go.Margin(5, 10),
                            stretch: _this.$go.GraphObject.Horizontal,
                            font: "11px sans-serif",
                            width: 100,
                            wrap: _this.$go.TextBlock.None,
                            overflow: _this.$go.TextBlock.OverflowEllipsis,
                            fromLinkable: true,
                            toLinkable: true,
                            editable: true
                        },
                        {
                            textEdited: (TextBlock, previousText, currentText) => {
                                this.submitTextEdit(TextBlock.part.data, TextBlock.row, previousText, currentText, 'name')
                            },
                        },
                        new _this.$go.Binding("text", "name"),
                        new _this.$go.Binding("font", "font"),
                        new _this.$go.Binding("editable", "editable")),
                    this.goMake(_this.$go.TextBlock,
                        {
                            column: 2,
                            margin: new _this.$go.Margin(5, 10),
                            stretch: _this.$go.GraphObject.Horizontal,
                            font: "11px sans-serif",
                            alignment: _this.$go.Spot.Left,
                            overflow: _this.$go.TextBlock.OverflowEllipsis,
                            fromLinkable: true,
                            toLinkable: true,
                            editable: true,
                        },
                        {
                            textEdited: (TextBlock, previousText, currentText) => {
                                this.submitTextEdit(TextBlock.part.data, TextBlock.row, previousText, currentText, 'description')
                            },
                        },
                        new _this.$go.Binding("text", "description").makeTwoWay(),
                        new _this.$go.Binding("font", "font"),
                        new _this.$go.Binding("editable", "editable"))
                );

            this.myDiagram.nodeTemplate =
                this.goMake(_this.$go.Node, "Auto",
                    {
                        linkDisconnected: (e, node, GraphObject) => { // link连接删除执行的回调方法
                            this.linkDisconnectedData(node.data)
                        },
                        deletable: false
                    },
                    new _this.$go.Binding("location", "loc", _this.$go.Point.parse).makeTwoWay(_this.$go.Point.stringify),
                    this.goMake(_this.$go.Shape,
                        {stroke: "white", fill: "#EEEEEE"}),
                    this.goMake(_this.$go.Panel, "Vertical",
                        {stretch: _this.$go.GraphObject.Horizontal, alignment: _this.$go.Spot.TopLeft},
                        this.goMake(_this.$go.Panel, "Table",
                            {
                                // name: "TABLE",
                                stretch: _this.$go.GraphObject.Horizontal,
                                minSize: new _this.$go.Size(100, 10),
                                defaultAlignment: _this.$go.Spot.Left,
                                defaultStretch: _this.$go.GraphObject.Horizontal,
                                defaultColumnSeparatorStroke: "transparent",
                                defaultRowSeparatorStroke: "#f3f1f2",
                                itemTemplate: fieldTemplate,
                                width: 250
                            },
                            new _this.$go.Binding("itemArray", "fields")
                        )  // end Table Panel of items
                    ),  // end Vertical Panel
                    // { // 出发表格的click事件
                    //   click: (e, node) => {
                    //     _this.handleRowChange(node.data.key)
                    //   }
                    // },
                );  // end Node
            //
            this.myDiagram.linkTemplate =
                this.goMake(_this.$go.Link,
                    {
                        curve: _this.$go.Link.JumpOver,
                        routing: _this.$go.Link.AvoidsNodes,
                        relinkableFrom: true,
                        relinkableTo: true, selectable: true,
                    },
                    this.goMake(_this.$go.Shape, {strokeWidth: 1.5}),
                    this.goMake(_this.$go.Shape, {toArrow: "Standard", stroke: null}),
                    {
                        fromPortChanged: (newLinkData, oldNodeData, newNodeData) => {
                            if (this.fromLinkNum < this.initLinkNum) {
                                this.fromLinkNum++
                                return
                            }
                            if (oldNodeData === null) return // 这代表新增，新增方法在toPortChanged事件中调用
                            const data = {
                                newLinkData: newLinkData.data,
                                oldNodeData: oldNodeData ? oldNodeData.data : null,
                                newNodeData: newNodeData ? newNodeData.data : null
                            }
                            this.submitFromLinkData(data)
                        },
                        toPortChanged: (newLinkData, oldNodeData, newNodeData) => {
                            if (this.toLinkNum < this.initLinkNum) {
                                this.toLinkNum++
                                return
                            }
                            const data = {
                                newLinkData: newLinkData.data,
                                oldNodeData: oldNodeData ? oldNodeData.data : null,
                                newNodeData: newNodeData ? newNodeData.data : null
                            }
                            this.submitToLinkData(data)
                        },
                    },
                );
            this.myDiagram.model =
                this.goMake(_this.$go.GraphLinksModel,
                    {
                        linkFromPortIdProperty: "fromPort",
                        linkToPortIdProperty: "toPort",
                        nodeDataArray: [
                            // {
                            //   key: "Record1",
                            //   fields: [
                            //     {
                            //       name: "列名",
                            //       description: "描述",
                            //       color: "#F7B84B",
                            //       figure: "Ellipse",
                            //       font: "bold 11px sans-serif"
                            //     },
                            //     {name: "field2", description: "the second one", color: "#F25022", figure: "Ellipse"},
                            //     {name: "fieldThree", description: "3rd", color: "#00BCF2"}
                            //   ],
                            //   loc: "0 0"
                            // },
                            // {
                            //   key: "Record2",
                            //   widths: [NaN, NaN, NaN],
                            //   fields: [
                            //     {name: "fieldA", description: "", color: "#FFB900", figure: "Diamond"},
                            //     {name: "fieldB", description: "", color: "#F25022", figure: "Rectangle"},
                            //     {name: "fieldC", description: "", color: "#7FBA00", figure: "Diamond"},
                            //     {name: "fieldD", description: "fourth", color: "#00BCF2", figure: "Rectangle"}
                            //   ],
                            //   loc: "350 0"
                            // }
                        ],
                        linkDataArray: [
                            // {from: "Record1", fromPort: "field2", to: "Record2", toPort: "fieldD"}
                        ]
                    });

            // define the group template
            _this.myDiagram.groupTemplate =
                this.goMake(_this.$go.Group, "Auto",
                    {
                        isSubGraphExpanded: false,
                        deletable: false
                    },
                    {
                        subGraphExpandedChanged(Group) {
                            if (!_this.isAllowLinkDel) return
                            // 修改被展开或关闭组的isSubGraphExpanded值
                            Group.data.isSubGraphExpanded = !Group.data.isSubGraphExpanded
                            const oldNodeDataArray = JSON.parse(_this.myDiagram.model.toJson()).nodeDataArray
                            // 将所有的组信息缓存下载
                            _this.oldGroupDataArray = JSON.parse(JSON.stringify(oldNodeDataArray.filter(item => item.isGroup)))
                        }
                    },
                    new _this.$go.Binding("isSubGraphExpanded", "isSubGraphExpanded"),
                    this.goMake(_this.$go.Shape, "Rectangle",
                        {fill: '#0f183f', stroke: "transparent", strokeWidth: 2, width: 250}),
                    this.goMake(_this.$go.Panel, "Vertical",
                        {
                            defaultAlignment: _this.$go.Spot.Left,
                            width: 250
                        },
                        this.goMake(_this.$go.Panel, 'Position',
                            {defaultAlignment: _this.$go.Spot.Right, width: 250},
                            // 添加展开group按钮
                            this.goMake(_this.$go.Panel,
                                {margin: 4, padding: (6.5), cursor: 'pointer'},
                                this.goMake("SubGraphExpanderButton"),
                            ),
                            this.goMake(_this.$go.TextBlock,
                                {
                                    font: "200 18px Sans-Serif",
                                    stroke: '#fff',
                                    position: new _this.$go.Point(30, 6.5)
                                },
                                new _this.$go.Binding("text", "title"),),
                            this.goMake(_this.$go.TextBlock,
                                {
                                    font: "180 12px Sans-Serif",
                                    stroke: '#fff',
                                    text: '编辑',
                                    cursor: 'pointer',
                                    position: new _this.$go.Point(180, 11)
                                },
                                {
                                    click(e, node) {
                                        _this.handleRowChange(node.part.data.title)
                                    }
                                }),
                            this.goMake(_this.$go.TextBlock,
                                {
                                    font: "180 12px Sans-Serif",
                                    stroke: '#fff',
                                    text: '删除',
                                    cursor: 'pointer',
                                    position: new _this.$go.Point(215, 11)
                                },
                                {
                                    click(e, node) {
                                        _this.handleDeleteTable(node.part.data.title)
                                    }
                                }),
                            this.goMake(_this.$go.Panel, 'Position',
                                {margin: 6.5, cursor: 'pointer'}),
                        ),
                        // 展示group内的内容
                        this.goMake(_this.$go.Placeholder)
                    )
                );  // end Group
        }

        // 缓存连接时提交的数据
        submitLinkData: Array<any> = []
        // 缓存初始化连接线的个数，解决页面初始化时调用节点连接
        initLinkNum: number = 0
        // 缓存连接线箭头方向连接调用的次数
        toLinkNum: number = 0
        // 缓存连接线开始方向连接调用的次数
        fromLinkNum: number = 0

        // 连接时提交数据
        submitToLinkData(data) {
            const {newLinkData, oldNodeData, newNodeData} = JSON.parse(JSON.stringify(data))
            // oldNodeData === null 新增连接，这个submitLinkData放在toPortChanged方法里执行
            if (oldNodeData === null) {
                this.submitLinkData.push({
                    table: newNodeData.table, // 外键的table名称
                    data: [{
                        "name": newNodeData.name,  // 外键的字段名
                        "description": newNodeData.description,  // 外键的描述
                        "row_id": newNodeData.table, // 外键的表名
                        "metatype": "column",
                        "id": newNodeData.tag_id, // 外键的tag_id
                        "subdata": {
                            "table": "column_type", // 固定不变的
                            "data": [{ // 主键信息
                                "att_id": newNodeData.tag_id,// 外键tag_id
                                "codetype": "is_fk",
                                "typevalue": newLinkData.fromPort, // 主键tag_id
                                "description": "关联主键列"
                            }, {
                                "att_id": newNodeData.tag_id,// 外键tag_id
                                "codetype": "pk_tablename",
                                "typevalue": newLinkData.from,  // 主键表
                                "description": "主键表"
                            }]
                        }
                    }]
                })
            } else {

                //
                // oldNodeData、newLinkData都含有字段名，代表修改前后外键都是在一个表中
                if (oldNodeData.name && newNodeData.name) {
                    // 根据
                    this.submitLinkData.push({
                        table: newNodeData.table, // 外键的table名称
                        data: [
                            {
                                "name": newNodeData.name,  // 外键的字段名
                                "description": newNodeData.description,  // 外键的描述
                                "row_id": newNodeData.table, // 外键的表名
                                "metatype": "column",
                                "id": newNodeData.tag_id, // 外键的tag_id
                                "subdata": {
                                    "table": "column_type", // 固定不变的
                                    "data": [{ // 主键信息
                                        "att_id": newNodeData.tag_id,// 外键tag_id
                                        "codetype": "is_fk",
                                        "typevalue": newLinkData.fromPort, // 主键tag_id
                                        "description": "关联主键列"
                                    }, {
                                        "att_id": newNodeData.tag_id,// 外键tag_id
                                        "codetype": "pk_tablename",
                                        "typevalue": newLinkData.from,  // 主键表
                                        "description": "主键表"
                                    }]
                                }
                            }, {
                                "name": oldNodeData.name,  // 外键的字段名
                                "description": oldNodeData.description,  // 外键的描述
                                "row_id": oldNodeData.table, // 外键的表名
                                "metatype": "column",
                                "id": oldNodeData.tag_id, // 外键的tag_id
                                "subdata": {
                                    "table": "column_type", // 固定不变的
                                    "data": [{ // 主键信息
                                        "att_id": oldNodeData.tag_id,// 外键tag_id
                                        "codetype": "is_fk",
                                        "typevalue": '', // 主键tag_id
                                        "description": "关联主键列"
                                    }, {
                                        "att_id": oldNodeData.tag_id,// 外键tag_id
                                        "codetype": "pk_tablename",
                                        "typevalue": '',  // 主键表
                                        "description": "主键表"
                                    }]
                                }
                            }
                        ]
                    })
                }
                // 修改前后不在一个表中
                else {
                    // 修改前后不在一个表中，事件触发两次，
                    // 一次oldNodeData含有修改之前外键字段的数据newNodeData含有修改之后的表信息
                    // 一次newNodeData含有修改之后外键字段的数据oldNodeData含有修改之后的表信息
                    if (oldNodeData.name) {
                        // 将之前的外键字段关系置为空
                        this.submitLinkData.push({
                            table: oldNodeData.table, // 外键的table名称
                            data: [
                                {
                                    "name": oldNodeData.name,  // 外键的字段名
                                    "description": oldNodeData.description,  // 外键的描述
                                    "row_id": oldNodeData.table, // 外键的表名
                                    "metatype": "column",
                                    "id": oldNodeData.tag_id, // 外键的tag_id
                                    "subdata": {
                                        "table": "column_type", // 固定不变的
                                        "data": [{ // 主键信息
                                            "att_id": oldNodeData.tag_id,// 外键tag_id
                                            "codetype": "is_fk",
                                            "typevalue": '', // 主键tag_id
                                            "description": "关联主键列"
                                        }, {
                                            "att_id": oldNodeData.tag_id,// 外键tag_id
                                            "codetype": "pk_tablename",
                                            "typevalue": '',  // 主键表
                                            "description": "主键表"
                                        }]
                                    }
                                }
                            ]
                        })
                    } else if (newNodeData.name) {
                        this.submitLinkData.push({
                            table: newNodeData.table, // 外键的table名称
                            data: [
                                {
                                    "name": newNodeData.name,  // 外键的字段名
                                    "description": newNodeData.description,  // 外键的描述
                                    "row_id": newNodeData.table, // 外键的表名
                                    "metatype": "column",
                                    "id": newNodeData.tag_id, // 外键的tag_id
                                    "subdata": {
                                        "table": "column_type", // 固定不变的
                                        "data": [{ // 主键信息
                                            "att_id": newNodeData.tag_id,// 外键tag_id
                                            "codetype": "is_fk",
                                            "typevalue": newLinkData.fromPort, // 主键tag_id
                                            "description": "关联主键列"
                                        }, {
                                            "att_id": newNodeData.tag_id,// 外键tag_id
                                            "codetype": "pk_tablename",
                                            "typevalue": newLinkData.from,  // 主键表
                                            "description": "主键表"
                                        }]
                                    }
                                }
                            ]
                        })
                    }
                }
            }
            // 新增 或者是外键表内修改提交数据
            if (oldNodeData === null || oldNodeData.name && newNodeData.name) {
                const submitLinkData = JSON.parse(JSON.stringify(this.submitLinkData))
                this.submitLinkData = []
                this.submitLink(submitLinkData)
            } else {
                if (this.submitLinkData.length >= 2) {
                    const submitLinkData = JSON.parse(JSON.stringify(this.submitLinkData))
                    this.submitLinkData = []
                    this.submitLink(submitLinkData)
                }
            }


            /*
            const {linkDataArray, nodeDataArray} = data
            let this.submitLinkData: Array<any> = []
            linkDataArray.forEach(linkDataItem => {
              // 外键有连接字表信息
              const toLinkTableData = nodeDataArray.find(item => item.key === linkDataItem.to)
              // 主键有连接字表信息
              const fromLinkTableData = nodeDataArray.find(item => item.key === linkDataItem.from)
              // 外键有连接字段的信息
              const toLinkField = toLinkTableData.fields.find(item => {
                if (item.tag_id) return item.tag_id.toString() === linkDataItem.toPort.toString()
              })
              // 主键有连接字段的信息
              const fromLinkField = fromLinkTableData.fields.find(item => {
                if (item.tag_id) return item.tag_id.toString() === linkDataItem.fromPort.toString()
              })
              // 寻找subData中是否存在已经连接的表
              const isFindTable = this.submitLinkData.find(item => item['table'] === linkDataItem.to)
              if (isFindTable) {
                isFindTable.data.push({
                  "name": toLinkField.name,
                  "description": toLinkField.description,
                  // "parent_id": "0",
                  "row_id": linkDataItem.to, // 表名称
                  "metatype": "column",
                  "id": toLinkField.tag_id,
                  "subdata": {
                    "table": "column_type",
                    "data": [{
                      "att_id": toLinkField.tag_id,
                      "codetype": "is_fk",
                      "typevalue": fromLinkField.tag_id,
                      "description": "外键"
                    }, {
                      "att_id": toLinkField.tag_id,
                      "codetype": "pk_tablename",
                      "typevalue": fromLinkField.table,
                      "description": "主键表"
                    }]
                  }
                })
              } else {
                this.submitLinkData.push({
                  table: toLinkField.table,
                  data: [{
                    "name": toLinkField.name,
                    "description": toLinkField.description,
                    // "parent_id": "0",
                    "row_id": linkDataItem.to, // 表名称
                    "metatype": "column",
                    "id": toLinkField.tag_id,
                    "subdata": {
                      "table": "column_type",
                      "data": [{
                        "att_id": toLinkField.tag_id,
                        "codetype": "is_fk",
                        "typevalue": fromLinkField.tag_id,
                        "description": "外键"
                      }, {
                        "att_id": toLinkField.tag_id,
                        "codetype": "pk_tablename",
                        "typevalue": fromLinkField.table,
                        "description": "主键表"
                      }]
                    }
                  }]
                })
              }
            })
            this.submitLink(this.submitLinkData)

            */


        }

        // 连接时提交数据
        submitFromLinkData(data) {
            const {nodeDataArray} = JSON.parse(this.myDiagram.model.toJson())
            const {newLinkData, oldNodeData, newNodeData} = JSON.parse(JSON.stringify(data))
            // 根据newLinkData寻找出外键字段薪资
            const fkFieldTagId = newLinkData.toPort // 外键字段的tag_id
            const fkTable = newLinkData.to // 外键表名
            const table = nodeDataArray.find(item => item.key === fkTable)   // 获取外键表数据
            const fkField = table.fields.find(item => {   // 获取外键字段数据
                return item.tag_id ? item.tag_id.toString() === fkFieldTagId.toString() : false
            })
            if (oldNodeData === null) return // 这代表新增，新增方法在toPortChanged事件中调用
            //
            // oldNodeData、newLinkData都含有字段名，代表修改前后主键都是在一个表中
            if (oldNodeData.name && newNodeData.name) {
                this.submitLinkData.push({
                    table: fkField.table, // 外键的table名称
                    data: [
                        {
                            "name": fkField.name,  // 外键的字段名
                            "description": fkField.description,  // 外键的描述
                            "row_id": fkField.table, // 外键的表名
                            "metatype": "column",
                            "id": fkField.tag_id, // 外键的tag_id
                            "subdata": {
                                "table": "column_type", // 固定不变的
                                "data": [{ // 主键信息
                                    "att_id": fkField.tag_id,// 外键tag_id
                                    "codetype": "is_fk",
                                    "typevalue": '', // 主键tag_id
                                    "description": "关联主键列"
                                }, {
                                    "att_id": fkField.tag_id,// 外键tag_id
                                    "codetype": "pk_tablename",
                                    "typevalue": '',  // 主键表
                                    "description": "主键表"
                                }]
                            }
                        },
                        {
                            "name": fkField.name,  // 外键的字段名
                            "description": fkField.description,  // 外键的描述
                            "row_id": fkField.table, // 外键的表名
                            "metatype": "column",
                            "id": fkField.tag_id, // 外键的tag_id
                            "subdata": {
                                "table": "column_type", // 固定不变的
                                "data": [{ // 主键信息
                                    "att_id": fkField.tag_id,// 外键tag_id
                                    "codetype": "is_fk",
                                    "typevalue": newLinkData.fromPort, // 主键tag_id
                                    "description": "关联主键列"
                                }, {
                                    "att_id": fkField.tag_id,// 外键tag_id
                                    "codetype": "pk_tablename",
                                    "typevalue": newLinkData.from,  // 主键表
                                    "description": "主键表"
                                }]
                            }
                        }
                    ]
                })
            }
            // 修改前后不在一个表中
            else {
                // 修改前后不在一个表中，事件触发两次，
                // 一次oldNodeData含有修改之前外键字段的数据newNodeData含有修改之后的表信息
                // 一次newNodeData含有修改之后外键字段的数据oldNodeData含有修改之后的表信息
                if (oldNodeData.name) {
                    // 将之前的外键字段关系置为空
                    this.submitLinkData.push({
                        table: fkField.table, // 外键的table名称
                        data: [
                            {
                                "name": fkField.name,  // 外键的字段名
                                "description": fkField.description,  // 外键的描述
                                "row_id": fkField.table, // 外键的表名
                                "metatype": "column",
                                "id": fkField.tag_id, // 外键的tag_id
                                "subdata": {
                                    "table": "column_type", // 固定不变的
                                    "data": [{ // 主键信息
                                        "att_id": fkField.tag_id,// 外键tag_id
                                        "codetype": "is_fk",
                                        "typevalue": '', // 主键tag_id
                                        "description": "关联主键列"
                                    }, {
                                        "att_id": fkField.tag_id,// 外键tag_id
                                        "codetype": "pk_tablename",
                                        "typevalue": '',  // 主键表
                                        "description": "主键表"
                                    }]
                                }
                            }
                        ]
                    })
                } else if (newNodeData.name) {
                    this.submitLinkData.push({
                        table: fkField.table, // 外键的table名称
                        data: [
                            {
                                "name": fkField.name,  // 外键的字段名
                                "description": fkField.description,  // 外键的描述
                                "row_id": fkField.table, // 外键的表名
                                "metatype": "column",
                                "id": fkField.tag_id, // 外键的tag_id
                                "subdata": {
                                    "table": "column_type", // 固定不变的
                                    "data": [{ // 主键信息
                                        "att_id": fkField.tag_id,// 外键tag_id
                                        "codetype": "is_fk",
                                        "typevalue": newLinkData.fromPort, // 主键tag_id
                                        "description": "关联主键列"
                                    }, {
                                        "att_id": fkField.tag_id,// 外键tag_id
                                        "codetype": "pk_tablename",
                                        "typevalue": newLinkData.from,  // 主键表
                                        "description": "主键表"
                                    }]
                                }
                            }
                        ]
                    })
                }
            }


            // 新增 或者是外键表内修改提交数据
            if (oldNodeData === null || oldNodeData.name && newNodeData.name) {
                const submitLinkData = JSON.parse(JSON.stringify(this.submitLinkData))
                this.submitLinkData = []
                this.submitLink(submitLinkData)
            } else {
                if (this.submitLinkData.length >= 2) {
                    const submitLinkData = JSON.parse(JSON.stringify(this.submitLinkData))
                    this.submitLinkData = []
                    this.submitLink(submitLinkData)
                }
            }
        }

        // 调取删除功能是，自动调用两次，用次缓存，保证调取一次
        linkDelFlag: boolean = true
        // 是否展开允许执行del删除功能，在重新获取元元模型数据时候，gojs会自动执行linkDisconnectedData，这个变量控制是否允许连接删除功能
        isAllowLinkDel: boolean = true


        // 连接删除
        linkDisconnectedData(data) {
            // 是否展开允许执行del删除功能，在重新获取元元模型数据时候，gojs会自动执行linkDisconnectedData，这个变量控制是否允许连接删除功能
            if (!this.isAllowLinkDel) return;
            if (this.linkDelFlag) {
                this.linkDelFlag = false
                const {nodeDataArray} = JSON.parse(this.myDiagram.model.toJson())
                // 根据newLinkData寻找出外键字段薪资
                const fkFieldTagId = data.toPort // 外键字段的tag_id
                const fkTable = data.to // 外键表名
                const table = nodeDataArray.find(item => item.key === fkTable)   // 获取外键表数据
                const fkField = table.fields.find(item => {   // 获取外键字段数据
                    return item.tag_id ? item.tag_id.toString() === fkFieldTagId.toString() : false
                })
                this.submitLinkData.push({
                    table: fkField.table, // 外键的table名称
                    data: [
                        {
                            "name": fkField.name,  // 外键的字段名
                            "description": fkField.description,  // 外键的描述
                            "row_id": fkField.table, // 外键的表名
                            "metatype": "column",
                            "id": fkField.tag_id, // 外键的tag_id
                            "subdata": {
                                "table": "column_type", // 固定不变的
                                "data": [{ // 主键信息
                                    "att_id": fkField.tag_id,// 外键tag_id
                                    "codetype": "is_fk",
                                    "typevalue": '', // 主键tag_id
                                    "description": "关联主键列"
                                }, {
                                    "att_id": fkField.tag_id,// 外键tag_id
                                    "codetype": "pk_tablename",
                                    "typevalue": '',  // 主键表
                                    "description": "主键表"
                                }]
                            }
                        }
                    ]
                })
                const submitLinkData = JSON.parse(JSON.stringify(this.submitLinkData))
                this.submitLinkData = []
                this.submitLink(submitLinkData)
            } else {
                this.linkDelFlag = true
            }
        }

        // 连接提交
        submitLink(data) {

            let URL = config.port('metadata') + 'saveOrUpdateMetaToMetaModuleList'
            let param: any = {}
            param.metaJson = JSON.stringify(data)
            this.$http.post(URL, Qs.stringify(param)).then((response) => {

                if (response.data.status === '200') {
                    this.$message.success(response.data.message)
                } else {
                    // 是否展开允许执行del删除功能，在重新获取元元模型数据时候，gojs会自动执行linkDisconnectedData，这个变量控制是否允许连接删除功能
                    this.isAllowLinkDel = false
                    this.getTableAndCheck()
                    this.$message.error(response.data.message)
                }
            }).catch((err) => {

                this.$message.error(err)
            })
        }

//获取元元模型数据结构
        getTableAndCheck() {

            this.isAllowLinkDel = false;
            let URL = config.port('metadata') + 'getMetaTableList';
            //let URL = config.prototype.port('metadata_ribbon')+'getLogicalTableAndCheck'
            let table: any = "module";
            // params.table = "module";
            this.$http.get(URL).then((response) => {

                this.tables = JSON.parse(response.data.data)
                this.analysisTableAndCheckData(this.tables)
                this.isAllowLinkDel = true;
            }).catch((response) => {
                this.isAllowLinkDel = true;

            })
        }

        // 解析获取元元模型数据结构数据
        analysisTableAndCheckData(data) {
            let nodeDataArray: Array<object> = []
            let linkDataArray: Array<any> = []
            let groupDataArray: Array<object> = []
            if (this.oldGroupDataArray.length) {
                groupDataArray = this.oldGroupDataArray
            } else {
                data.forEach(item => {
                    // 将每个表设置为一个group
                    groupDataArray.push({
                        key: item.table + 1, // 组的名称 加1的原因是为了不与nodeDataArray中key重复
                        title: item.table,
                        isSubGraphExpanded: false,
                        isGroup: true
                    })
                })
            }
            data.forEach(item => {
                item.data.forEach(ite => {
                    ite['tag_id'] = ite['tag_id'].toString()
                })
                item.data.forEach(ite => {
                    ite.table = item.table
                    ite.checked = false
                })
                item.data.unshift({
                    name: "列名",
                    description: "描述",
                    figure: "Ellipse",
                    font: "bold 14px sans-serif",
                    editable: false,
                    visible: false,
                    isShowCheckedText: true
                })
                nodeDataArray.push({
                    key: item.table,
                    fields: item.data,
                    group: item.table + 1
                })
                item.data.forEach(ite => {
                    if (ite.pk_tablename !== '' && ite.pk_tablename) {
                        linkDataArray.push({
                            from: ite.pk_tablename,
                            fromPort: ite.is_fk,
                            to: item.table,
                            toPort: ite.tag_id.toString()
                        })
                    }
                })
            })
            // 缓存初始化连接线的个数，解决页面初始化时调用节点连接
            this.initLinkNum = linkDataArray.length
            this.toLinkNum = 0
            // 缓存连接线开始方向连接调用的次数
            this.fromLinkNum = 0
            nodeDataArray = groupDataArray.concat(nodeDataArray)
            this.myDiagram.model = this.goMake(this.$go.GraphLinksModel, {
                linkFromPortIdProperty: "fromPort",
                linkToPortIdProperty: "toPort", nodeDataArray, linkDataArray
            });

            // 是否展开允许执行del删除功能，在重新获取元元模型数据时候，gojs会自动执行linkDisconnectedData，这个变量控制是否允许连接删除功能
            this.isAllowLinkDel = true

        }

        //显示展示框
        handleRowChange(table) {
            this.tableName = table;
            this.dialogVisible = true;
            //this.EditMetaModel.init(table)
        }

        //关闭弹出框
        closeDialog(items) {
            // 保存成功刷新列表
            if (items) {
                this.getTableAndCheck()
            }
            this.dialogVisible = false;
            const a = {
                "table": "column_type",
                "data": [{
                    "name": "id",
                    "description": "属性类型编号",
                    "parent_id": "0",
                    "row_id": "column_type",
                    "metatype": "column",
                    "id": 15,
                    "subdata": {
                        "table": "column_type",
                        "data": [{
                            "att_id": 15,
                            "codetype": "is_fk",
                            "typevalue": "20",
                            "description": "外键"
                        }, {
                            "att_id": 15,
                            "codetype": "pk_tablename",
                            "typevalue": "dict",
                            "description": "主键表"
                        }]
                    }
                }, {
                    "name": "att_id",
                    "description": "属性编号",
                    "parent_id": "0",
                    "row_id": "column_type",
                    "metatype": "column",
                    "id": 16,
                    "subdata": {
                        "table": "column_type",
                        "data": [{
                            "att_id": 16,
                            "codetype": "is_fk",
                            "typevalue": "0",
                            "description": "外键"
                        }, {
                            "att_id": 16,
                            "codetype": "pk_tablename",
                            "typevalue": "module",
                            "description": "主键表"
                        }]
                    }
                }, {
                    "name": "codeType",
                    "description": "类型编码",
                    "parent_id": "0",
                    "row_id": "column_type",
                    "metatype": "column",
                    "id": 17,
                    "subdata": {
                        "table": "column_type",
                        "data": [{
                            "att_id": 17,
                            "codetype": "4",
                            "typevalue": "text",
                            "description": "展示框"
                        }, {
                            "att_id": 17,
                            "codetype": "3",
                            "typevalue": "4",
                            "description": "常量"
                        }, {
                            "att_id": 17,
                            "codetype": "type",
                            "typevalue": "varchar",
                            "description": "字符串类型"
                        }, {
                            "att_id": 17,
                            "codetype": "length",
                            "typevalue": "100",
                            "description": "字符串长度"
                        }, {
                            "att_id": 17,
                            "codetype": "is_pk",
                            "typevalue": "0",
                            "description": "是否主键"
                        }, {
                            "att_id": 17,
                            "codetype": "is_fk",
                            "typevalue": "20",
                            "description": "外键"
                        }, {
                            "att_id": 17,
                            "codetype": "pk_tablename",
                            "typevalue": "dict",
                            "description": "主键表"
                        }]
                    }
                }, {
                    "name": "typeValue",
                    "description": "类型值",
                    "parent_id": "0",
                    "row_id": "column_type",
                    "metatype": "column",
                    "id": 18,
                    "subdata": {
                        "table": "column_type"
                    }
                }, {
                    "name": "description",
                    "description": "描述",
                    "parent_id": "0",
                    "row_id": "column_type",
                    "metatype": "column",
                    "id": 19,
                    "subdata": {
                        "table": "column_type",
                        "data": [{
                            "att_id": 19,
                            "codetype": "4",
                            "typevalue": "text",
                            "description": "展示框"
                        }, {
                            "att_id": 19,
                            "codetype": "3",
                            "typevalue": "",
                            "description": "常量"
                        }, {
                            "att_id": 19,
                            "codetype": "type",
                            "typevalue": "varchar",
                            "description": "字符串类型"
                        }, {
                            "att_id": 19,
                            "codetype": "length",
                            "typevalue": "200",
                            "description": "字符串长度"
                        }, {
                            "att_id": 19,
                            "codetype": "is_pk",
                            "typevalue": "0",
                            "description": "是否主键"
                        }, {
                            "att_id": 19,
                            "codetype": "is_fk",
                            "typevalue": "4",
                            "description": "外键"
                        }, {
                            "att_id": 19,
                            "codetype": "is_null",
                            "typevalue": "1",
                            "description": "是否可空"
                        }, {
                            "att_id": 19,
                            "codetype": "is_show",
                            "typevalue": "1",
                            "description": "是否展示"
                        }, {
                            "att_id": 19,
                            "codetype": "pk_tablename",
                            "typevalue": "module",
                            "description": "主键表"
                        }]
                    }
                }]
            }
        }
    }

</script>

<style lang="less" scoped>
    .myDiagramDiv {
        width: 100%;
        height: 100%;
        background-color: #fff;
    }
</style>
