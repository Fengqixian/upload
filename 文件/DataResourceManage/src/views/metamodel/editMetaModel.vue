<template>
    <div>
        <el-container>
            <el-main>
                <div id="editModel">
                    <el-table :data="tableData" border stripe max-height="300"
                              @selection-change="handleSelectChange">
                        <el-table-column
                                type="selection"
                                width="55">
                        </el-table-column>
                        <el-table-column v-for="(header ,index) in tableHeaderList" :key="index" :label="header.label">
                            <template slot-scope="scope">
                                <el-select
                                        v-model.trim="scope.row[header.prop]"
                                        placeholder="请选择"
                                        v-if="header.dicData && header.dicData.length">
                                    <el-option
                                            v-for="item in header.dicData"
                                            :key="item.value"
                                            :label="item.itemsName"
                                            :value="item.itemsCode">
                                    </el-option>
                                </el-select>
                                <el-input :value="scope.row[header.prop]" v-model.trim="scope.row[header.prop]"
                                          v-else></el-input>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>
            </el-main>
            <el-footer>
                <div class="btn-right">
                    <el-button class="del-nobg" @click="$emit('change', false)">取 消</el-button>
                    <el-button class="add-bg" @click="handleAdd">新增一列</el-button>
                    <el-button class="del-bg" @click="handleDelete">删 除</el-button>
                    <el-button class="confirm-bg" @click="handleSave">保 存</el-button>
                </div>
            </el-footer>
        </el-container>
    </div>
</template>

<script lang="ts">

    import Vue from 'vue'
    import {Component, Prop, Model} from 'vue-property-decorator'
    import {Action} from 'vuex-class'
    import config from '../../config.ts'


    @Component({})

    export default class EditMetaModel extends Vue {
        name: string = 'EditMetaModel';

        // 设置v-model.trim
        @Model('change') attrVisible;

        // 模型node数据
        @Prop({type: Object, required: true})
        metaModelData;
        // 编辑的属性所属的tab下的所有数据  用于编辑属性后 修改table值
        @Prop({type: Object, required: true})
        tabData;

        @Action getConstant; // 获取常量方法


        //常量集合
        dictList: any = [];

        //数据集合
        tableData: any = [];
        //table的列头集合
        tableHeaderList: any = [
            {
                label: '中文名称',
                span: 12,
                prop: 'nameCn'
            },
            {
                label: '英文名称',
                span: 12,
                prop: 'nameEn'
            },
            {
                label: '数据类型',
                span: 12,
                prop: 'dataType',
                type: 'select',
                solt: true,
                dicData: [{
                    itemsName: '文本',
                    itemsCode: 'string'
                }, {
                    itemsName: '时间日期',
                    itemsCode: 'datetime'
                }]
            },
            {
                label: '数据长度',
                span: 12,
                prop: 'dataLength',
                type: 'select',
                solt: true,
                dicData: [{
                    itemsName: '50',
                    itemsCode: 50
                }, {
                    itemsName: '100',
                    itemsCode: 100
                }, {
                    itemsName: '6000',
                    itemsCode: 6000
                }]
            },
            {
                label: '展示方式',
                prop: 'showType',
                type: 'select',
                span: 12,
                solt: true,
                dicData: [{
                    itemsName: '文本框',
                    itemsCode: 'input'
                }, {
                    itemsName: '下拉框',
                    itemsCode: 'select'
                }, {
                    itemsName: '日期框',
                    itemsCode: 'datetime'
                }, {
                    itemsName: '大文本',
                    itemsCode: 'textarea'
                }]
            },
            {
                label: '展示格式',
                prop: 'showFormat'
            },
            {
                label: '值域说明',
                span: 24,
                prop: 'valueScope'
            }
        ];

        // table被选中
        tableCheckedArr: Array<object> = [];


        public mounted(): void {
            this.geMetaDataModelList();
            this.getDataTypeAndShowType();
        }

        /**
         * 获取展示方式和字段数据类型
         */
        public async getDataTypeAndShowType() {
            const dataType: Array<object> = await this.getConstant(16);
            const showType: Array<object> = await this.getConstant(17);
            this.tableHeaderList.map(item=> {
                if(item.prop === 'dataType'){
                    item.dicData = dataType;
                }else if(item.prop === 'showType'){
                    item.dicData = showType;
                }
            })

        }

        /**
         * 增加一行空行
         */
        handleAdd() {
            if (this.tableData[this.tableData.length - 1].nameEn !== '') {
                this.addNewMetaModel();
            } else {
                this.$message.warning('请完成新增一行的数据再新增')
            }
        }

        /**
         * 保存信息
         */
        handleSave() {
            let URL = config.port('metadataproperties');
            this.$http.post(URL, this.tableData.filter(item => item.nameEn !== '')).then((response) => {
                //将数据发送到后台存储
                let result = response.data.code;
                if (result == 0) {
                    //保存成功
                    this.$message({
                        message: '保存成功。',
                        type: 'success'
                    });
                    this.$emit('change', false);
                    this.$emit('success', this.tabData)
                }

            }).catch((response) => {
            })
        }


        /**
         * 批量删除属性
         */
        handleDelete() {
            if(this.tableCheckedArr.length === 0){
                this.$message.warning('请选择删除的属性!');
                return;
            }
            let deleteId: Array<number> = [];
            let deleteName: Array<string> = [];
            this.tableCheckedArr.forEach(item => {
                if (item['id']) {
                    deleteId.push(item['id']);
                    deleteName.push(item['nameCn'])
                }
            });
            this.$confirm(`此操作将永久删除"${deleteName.join(',')}", 是否继续?`, '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                let url = config.port('metadataproperties');
                this.$http.delete(url, {data: deleteId}).then(response => {
                    if (response.data.code === 0) {
                        this.$message.success('删除成功');
                        this.$emit('change', false);
                        this.$emit('success', this.tabData)
                    }
                })
            })
        }

        /**
         * table选择
         * @param selection
         */
        public handleSelectChange(selection): void {
            this.tableCheckedArr = selection;
        }

        /*//获取数据类型集合
        getMetaTypeList() {
            let URL = config.port('dictinfo') + 'getDictDetailPage';
            this.$http.get(URL, {params: {dictId: 1}}).then((response) => {
                this.dictList = response.data.data.records;
                // 添加数据类型
                this.tableHeaderList.push({
                    label: '数据类型',
                    span: 12,
                    prop: 'dataType',
                    type: 'select',
                    solt: true,
                    dicData: response.data.data.records
                })
            }).catch(function (response) {
            })
        }*/

        /**
         * 获取当前数据集合
         */
        geMetaDataModelList() {
            let URL = config.port('metadataproperties') + '/page';
            let param = {params: {modelResourceId: this.metaModelData.resourceId, size: -1}};
            this.$http.get(URL, param).then((response) => {
                //要显示的元模型
                this.tableData = response.data.data.records;
                if (this.tableData.length < 1 || this.tableData == null) {
                    this.addNewMetaModel()
                }
            }).catch(function (response) {
            })
        }


        /**
         * 新增一行
         */
        addNewMetaModel() {
            var newdata = {
                id: null,
                resourceId: null,  // 资源标识
                modelResourceId: this.metaModelData.resourceId, // 模型标识
                nameCn: '',
                nameEn: '',
                dataType: '',
                dataLength: '',
                showType: '',
                showFormat: '',
                mappingColumn: '',
                valueScope: '', // 取值范围说明
            };
            this.tableData.push(newdata);
        }

    }
</script>

<style scoped>
    .input-with-select .el-input-group__prepend {
        background-color: #fff;
    }

    .rowHeight td {
        height: 25px;
    }

</style>
