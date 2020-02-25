<template>
    <el-row>
        <div class="operate">
              <span
                      class="btn"
                      @click.prevent.stop="handleAddColumnRight">
                <i class="iconfont icon-xinzeng"></i> 新增</span>
            <span
                    class="btn"
                    @click.prevent.stop="submitModuleInstence">
                <i class="iconfont icon-baocun"></i> 保存</span>
        </div>
        <div class="tabs-top-content">
            <ul class="info-container">
                <el-row>
                    <el-col :span="8"
                            v-if="attr.att_name_en||attr.att_name_cn"
                            :label="attr.att_name_cn"
                            v-for="(attr,index) in currentModuleInstance"
                            :key="index">
                        <li class="item">
                            <span class="label">{{attr.att_name_cn}}：</span>
                            <span class="value">{{attr.meta_value}}</span>
                        </li>
                    </el-col>
                </el-row>
            </ul>
        </div>
        <!--右侧下方Tab选项卡-->
        <div class="tabs-bottom-tabs">
            <el-tabs
                    type="card"
                    v-model.trim="activeName"
                    v-show="bottomTabs.length"
                    @tab-click="handleTabClick">
                <el-tab-pane
                        :label="tab.name"
                        :name="tab.module_id"
                        v-for="tab in bottomTabs"
                        :key="tab.module_id">
                    <!--<el-row class="row-bg" justify="start">-->
                    <!--<el-col :span="4">-->
                    <!--<el-button-->
                    <!--@click="handleAddColumnRight"-->
                    <!--icon="el-icon-plus"-->
                    <!--align="left">新增-->
                    <!--</el-button>-->
                    <!--</el-col>-->
                    <!--<el-col :span="4">-->
                    <!--<el-button-->
                    <!--@click="submitModuleInstence"-->
                    <!--type="primary"-->
                    <!--icon="el-icon-save"-->
                    <!--align="left">保存-->
                    <!--</el-button>-->
                    <!--</el-col>-->
                    <!--</el-row>-->
                    <el-table
                            :data="tabFormOrTableDatas"
                            highlight-current-row
                            row-key="id"
                            @select=""
                            @select-all=""
                            @row-click=""
                            v-if="loaded"
                            ref="">
                        <el-table-column width="100" type="index" align="center" label="序号"></el-table-column>
                        <el-table-column
                                v-if="attr.attrExt.is_show!=='2'"
                                :label="attr.att_name_cn"
                                v-for="(attr,index) in tabModuleInfo.attrs"
                                :key="index">
                            <template slot-scope="scope">
                                <el-input
                                        v-if="attr.attrExt.show_type!=='select'"
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
                    <el-pagination background
                                   layout="prev, pager, next,total"
                                   @current-change="child_current_change"
                                   :total="pageInfo.total"
                                   :pageSize="pageInfo.pageSize">
                    </el-pagination>
                </el-tab-pane>
            </el-tabs>
        </div>
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
                pageInfo: config.page(),
                activeName: '',
                tabModuleInfo: {},
                tabFormOrTableDatas: [],
                allTabFormOrTableDatas: [],
                bottomTabs: [],
                // 右侧外围的所有tabs的数据
                loaded: false
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
            this.init();
        },
        methods: {
            child_current_change(currentPage) {
                this.pageInfo.currentPage = currentPage;
                this.tabFormOrTableDatas = this.allTabFormOrTableDatas.slice((this.pageInfo.currentPage - 1) * this.pageInfo.pageSize, this.pageInfo.currentPage * this.pageInfo.pageSize);
            },
            init() {
                //准备下边tab的数据
                //加载树首次加载顶级模型传入参数 parentId
                let URL = config.port('metadataManage') + 'getMetadataModelTree';
                var p = {
                    parentId: this.currentModuleInstance.module_id,
                    moduleInstanceId: this.currentModuleInstance.module_instance_id,
                    treeType: this.currentModuleInstance.tree_type
                };
                var param = new Object();
                param.metaJson = JSON.stringify(p);
                this.$http.post(URL, Qs.stringify(param)).then((response) => {
                    this.bottomTabs = response.data.data;
                    if (this.bottomTabs && this.bottomTabs.length > 0) {
                        this.activeName = this.bottomTabs[0].module_id
                        this.handleTabClick(this.bottomTabs[0]);
                    }
                }).catch(function (response) {
                })
            },
            /*提交建库建表元数据*/
            submitModuleInstence() {
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
            },
            /*右边table方式添加元数据时，添加行*/
            handleAddColumnRight() {
                var row = {};
                row = JSON.parse(JSON.stringify(this.tabModuleInfo.attrValueJson));
                this.tabFormOrTableDatas.push(row);
            },
            /*tab下方切换时执行*/
            handleTabClick(tab) {
                let data = this.bottomTabs[tab.index] || tab;
                this.loaded = false;
                // 调用后端接口
                let model = {
                    id: data.module_id,
                    parentId: data.module_id,
                    moduleInstanceId: data.module_instance_id,
                    treeType: data.tree_type
                };
                let URL = config.port('metadataManage') + 'getCreateModelByModelAndMetaData';
                this.$http.post(URL, model).then((response) => {
                    this.tabModuleInfo = response.data.data.moduleInfo;
                    this.allTabFormOrTableDatas = response.data.data.datas;
                    this.pageInfo.total = response.data.data.datas.length || 0;
                    //分出第一页
                    this.tabFormOrTableDatas = this.allTabFormOrTableDatas.slice((this.pageInfo.currentPage - 1) * this.pageInfo.pageSize, this.pageInfo.currentPage * this.pageInfo.pageSize)
                    this.loaded = true;
                }).catch(function (response) {
                });
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
