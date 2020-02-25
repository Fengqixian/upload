<template>
    <div class="dynamic-table">
        <el-table
                :data="tableData"
                :highlight-current-row="radio"
                @selection-change="selectChange"
                @current-change="handleCurrentChange"
                ref="dynamicTable"
                style="width: 100%">
            <el-table-column
                    type="selection"
                    width="55"
                    v-if="checkBox && headerData.length">
            </el-table-column>
            <el-table-column
                    width="55"
                    v-else-if="radio && headerData.length">
            </el-table-column>
            <template v-for="item in headerData">
                <el-table-column
                        :key="item.prop"
                        :prop="item.prop"
                        :label="item.label"
                        :width="item.width?item.width:''"
                        v-if="headerCheckBox || item.tag || (item.status)">
                    <template slot="header" slot-scope="{row,column, $index}">
                        <el-checkbox v-model.trim="item.value" v-if="headerCheckBox">{{item.label}}</el-checkbox>
                        <span v-model.trim="item.value" v-else>{{item.label}}</span>
                    </template>
                    <template slot-scope="{ row, column, $index }">
                        <template v-if="item.tag">
                            <el-tag v-for="(ite,index) in row[item.prop]" :key="index" style="margin: 2px">
                                {{ite[item.tagName]}}
                            </el-tag>
                        </template>
                        <template v-else-if="item.status">
                            <span>{{item.status[row[item.prop]]}}</span>
                        </template>
                    </template>
                </el-table-column>
                <el-table-column
                        v-else-if="!(headerCheckBox || item.tag)"
                        :key="item.prop"
                        :prop="item.prop"
                        :label="item.label"
                        :width="item.width?item.width:''">
                </el-table-column>
            </template>
            <el-table-column
                    v-if="operate"
                    fixed="right"
                    label="操作"
                    :width="operateWidth">
                <template slot-scope="scope">
                    <slot :row="scope.row" :column="scope.column" :$index="scope.$index"></slot>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination background
                       @size-change="sizeChange"
                       @current-change="currentChange"
                       :current-page="paginationData.currentPage"
                       :page-sizes="paginationData.pageSizes"
                       :page-size="paginationData.pageSize"
                       layout="total, sizes, prev, pager, next, jumper"
                       :total="paginationData.total">
        </el-pagination>
    </div>
</template>

<script>
    export default {
        name: 'DynamicTable',
        data() {
            return {
                // 存储被选中的data
                selectData: []
            }
        },
        props: {
            tableData: null,  // table展示数据
            paginationData: {},  // 分页数据
            index: 0,  // index代表此组件在遍历中的index下表，便于vue对Array<object>深层次的相应
            headerData: null,  // table头部信息
            otherData: null,  // 其他数据
            operate: {default: false}, // 操作
            checkBox: {default: false}, // checkBox
            operateWidth: null, // 操作宽度
            radio: {default: false}, // 单选
            headerCheckBox: {default: false} // 头部选择框
        },
        methods: {
            // 清除多选表格用户的多选
            clearSelection() {
                this.$refs.dynamicTable.clearSelection();
            },
            // checkBox change
            selectChange(selection) {
                this.selectData = selection;
                this.$emit('select-change', selection);
            },
            handleCurrentChange(selection) {
                this.selectData = selection;
                this.$emit('current-change', selection);
            },
            // 获取被选中的data
            getSelect() {
                return this.selectData;
            },
            setCurrentRow(id, row_id) {
                this.tableData.forEach((item, index) => {
                    if (item[row_id] === id) {
                        this.$refs.dynamicTable.setCurrentRow(this.tableData[index]);
                    }
                })
            },
            sizeChange(val) {  // 每页显示条数发生变化
                this.paginationData.pageSize = val
                this.paginationData.currentPage = 1
                const ret = {
                    tableData: this.tableData,
                    paginationData: this.paginationData,
                    index: this.index,
                    otherData: this.otherData
                }
                this.$emit('sizeChange', ret)
            },
            currentChange(val) {
                this.paginationData.currentPage = val
                const ret = {
                    tableData: this.tableData,
                    paginationData: this.paginationData,
                    index: this.index,
                    otherData: this.otherData
                }
                this.$emit('currentChange', ret)
            }
        }
    }
</script>
<style scoped lang="less">
    .dynamic-table {
        background-color: #fff;
    }
</style>
