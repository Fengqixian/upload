<template>
    <div class="cb-table">
        <div class="cb-table-wrapper"
             :style="{bottom:`${cbPaginationWrapper}px`}">
            <el-table :data="tableData"
                      v-if="headerData.length"
                      style="width: 100%"
                      :highlight-current-row="highlightCurrentRow"
                      border
                      :row-class-name="handleRowClassName"
                      @selection-change="selectionChange"
                      @current-change="handleTableCurrentChange"
                      @row-click="rowClick"
                      ref="cbTable">
                <el-table-column type="selection"
                                 width="55"
                                 v-if="selection">
                </el-table-column>

                <!-- 具有输入框的table 这里 后续可能需要添加各种输入框 暂定 -->
                <template v-if="tableInput">
                    <template v-for="item in headerData">
                        <el-table-column
                                :prop="item.prop"
                                :label="item.label"
                                :width="item.width"
                                :key="item.prop + item.label">
                            <template slot-scope="{row}">
                                <div v-if="item.type === 'select'">
                                    <el-select v-model.trim="row[item.prop]" :placeholder="`请选择${item.label}`">
                                        <el-option
                                                v-for="item in item.options"
                                                :key="item.value"
                                                :label="item.label"
                                                :value="item.value">
                                        </el-option>
                                    </el-select>
                                </div>
                                <div v-else-if="item.type === 'switch'">
                                    <el-switch
                                            v-model.trim="row[item.prop]"
                                            active-color="#13ce66"
                                            inactive-color="#ff4949">
                                    </el-switch>
                                </div>
                                <el-input v-else
                                          :placeholder="item.label"
                                          v-model.trim="row[item.prop]"
                                          clearable>
                                </el-input>
                            </template>
                        </el-table-column>
                    </template>
                </template>

                <!-- 没有任何要求的column -->
                <template v-else>
                    <template v-for="item in headerData">
                        <el-table-column
                                :prop="item.prop"
                                :label="item.label"
                                :width="item.width"
                                :key="item.prop + item.label">
                            <template slot-scope="{row}">
                                <div v-if="item.type === 'switch'">{{row[item.prop] === true?'是':'否'}}</div>
                                <template v-else-if="item.type === 'tag'">
                                    <el-tag :type="item.styles[row[item.prop]]">
                                        {{item.translations[row[item.prop]]}}
                                    </el-tag>
                                </template>
                                <template v-else-if="item.type === 'tagList'">
                                    <el-tag type="success" v-for="ite in row[item.prop]" :key="ite">
                                        {{ite}}
                                    </el-tag>
                                </template>
                                <template v-else-if="item.type === 'select'">
                                    <template v-for="(opt,index) in item.options">
                                        <span v-if="opt.itemsCode === row[item.prop]"
                                              :key="index">{{opt.itemsName}}</span>
                                    </template>
                                </template>
                                <div v-else>
                                    <el-tooltip class="item"
                                                effect="light"
                                                :content="row[item.prop] + ''"
                                                placement="top-start">
                                        <div class="row-content">
                                            {{row[item.prop]}}
                                        </div>
                                    </el-tooltip>
                                </div>
                            </template>
                        </el-table-column>
                    </template>
                </template>
                <slot name="column"></slot>
            </el-table>
        </div>
        <!-- 分页 -->
        <div class="cb-pagination-wrapper" v-if="isOperation || isPagination"
             :style="{height:`${cbPaginationWrapper}px`}">
            <slot name="operation"></slot>
            <el-pagination
                    background
                    v-if="isPagination"
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="paginationData.currentPage"
                    :page-sizes="pageSizes"
                    :page-size="paginationData.pageSize"
                    :layout="paginationLayout"
                    :prev-text="prevText"
                    :next-text="nextText"
                    :total="paginationData.total">
            </el-pagination>
        </div>
    </div>
</template>

<script>
    import {mapState} from 'vuex'

    export default {
        name: "CbTable",
        props: {
            // table头部数据
            headerData: {
                type: Array,
                // required:true,
                default() {
                    return [
                        {
                            prop: 'date',
                            label: '日期',
                            width: '180'
                        },
                        {
                            prop: 'name',
                            label: '姓名',
                            width: '180'
                        },
                        {
                            prop: 'address',
                            label: '地址',
                            // width: '180'
                        },
                    ]
                }
            },
            // table数据
            tableData: {
                type: Array,
                default() {
                    return [{
                        date: '2016-05-02',
                        name: '王小虎',
                        address: '上海市普陀区金沙江路 1518 弄'
                    }]
                }
            },
            // 是否展示多选框
            selection: {
                type: Boolean,
                default: false
            },
            // 是否支持单选
            highlightCurrentRow: {
                type: Boolean,
                default: false
            },
            // 是否使用分页 默认使用分页
            isPagination: {
                type: Boolean,
                default: true
            },
            // 是否底部添加操作按钮
            isOperation: {
                type: Boolean,
                default: false
            },
            // 分页数据
            paginationData: {
                type: Object,
                default() {
                    return {
                        total: 1, // 总共数据
                        pageSize: 1, // 每页显示条数
                        currentPage: 1 // 当前页数
                    }
                }
            },
            paginationLayout: {
                type: String,
                default: 'total, sizes, prev, pager, next, jumper'
            },
            prevText: {
                type: String,
                default: ''
            },
            nextText: {
                type: String,
                default: ''
            },
            // table是否为输入框形式
            tableInput: {
                type: Boolean,
                default: false
            },
            // 行的 className 的回调方法，也可以使用字符串为所有行设置一个固定的 className。
            rowClassName: Function
        },
        data() {
            return {
                /**
                 * table节点被选择
                 */
                selectData: [],
                tabHeight: '',
                cbPaginationWrapper: 0 // 分页区域的高度
            }
        },
        computed: {
            ...mapState(['pageSizes'])
        },
        mounted() {
            this.initStyle();
        },
        methods: {
            /**
             * 初始化样式
             */
            initStyle() {
                // 组件没有操作 没有分页 将分页区域高度设置为0 否则设置为40
                if (!this.isOperation && !this.isPagination) {
                    this.cbPaginationWrapper = 0;
                } else {
                    this.cbPaginationWrapper = 40;
                }
            },
            /**
             * 用于多选表格，清空用户的选择
             */
            clearSelection() {
                this.$refs.cbTable.clearSelection();
            },

            /**
             * 当选择项发生变化时会触发该事件
             * @param selection
             */
            selectionChange(selection) {
                this.selectData = selection;
                this.$emit('selection-change', selection)
            }
            ,
            /**
             * 当用户手动勾选数据行的 Checkbox 时触发的事件
             * @param selection
             * @param row
             */
            select(selection, row) {
                this.$emit('select', selection, row)
            }
            ,

            /**
             * 当用户手动勾选全选 Checkbox 时触发的事件
             * @param selection
             */
            selectAll(selection) {
                this.$emit('select-all', selection)
            }
            ,

            /**
             * 获取被选择的
             * @returns {Array}
             */
            getSelectData() {
                return this.selectData;
            }
            ,

            /**
             * pageSize 改变时会触发
             * @param val 每页条数
             */
            handleSizeChange(val) {
                this.paginationData['pageSize'] = val;
                this.paginationData['currentPage'] = 1;
                // 将事件派发出去，将改变的页数 当前table的头部信息 table体数据返回出去
                this.$emit('size-change', val, this.paginationData, this.headerData, this.tableData)
            }
            ,

            /**
             * currentPage 改变时会触发
             * @param val 当前页
             */
            handleCurrentChange(val) {
                this.paginationData['currentPage'] = val;
                // 将事件派发出去，将改变的页数 当前table的头部信息 table体数据返回出去
                this.$emit('current-change', val, this.paginationData, this.headerData, this.tableData)
            },
            /**
             * 当表格的当前行发生变化的时候会触发该事件，如果要高亮当前行，请打开表格的 highlight-current-row 属性
             * @param currentRow 当前行
             * @param oldCurrentRow 之前高亮那行
             */
            handleTableCurrentChange(currentRow, oldCurrentRow) {
                this.selectData.splice(0);
                this.selectData.push(currentRow);
                this.$emit('table-current-change', currentRow, oldCurrentRow)
            },
            handleRowClassName({row, rowIndex}) {
                if (this.rowClassName === undefined) {
                    return '';
                }
                return this.rowClassName({row, rowIndex})
            },
            /**
             * 用于单选表格，设定某一行为选中行，如果调用时不加参数，则会取消目前高亮行的选中状态。
             */
            setCurrentRow(row) {
                this.$refs.cbTable.setCurrentRow(row);
            },
            /**
             * 当某一行被点击时会触发该事件
             * @param row
             * @param column
             * @param event
             */
            rowClick(row, column, event) {
                this.$emit('row-click', row, column, event)
            }
        }
    }
</script>

<style lang="less">
    .cb-table {
        width: 100%;
        height: 100%;
        /*display: flex;*/
        /*flex-direction: column;*/
        position: relative;


        .cb-table-wrapper {
            /*flex: 1;*/
            /*!*overflow: auto;*!*/
            position: absolute;
            top: 0;
            right: 0;
            left: 0;
            bottom: 40px;
        }

        .cb-pagination-wrapper {
            position: absolute;
            left: 0;
            right: 0;
            bottom: 0;
            padding-right: 10px;
            height: 40px;
            display: flex;
            justify-content: flex-end;
            align-items: center;
            background-color: #ffffff;

            .el-pagination {
                margin: 0 0 0 10px;
            }
        }

        /**
        输入框样式
         */

        .el-input {
            input {
                border: 0;
            }
        }

        .el-table {
            width: 100%;
            height: 100%;
            display: flex;
            flex-direction: column;

            .el-table__body-wrapper {
                flex: 1;
                margin-bottom: 3px;
                overflow-y: auto;
            }
        }

        .row-content {
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }
    }
</style>
