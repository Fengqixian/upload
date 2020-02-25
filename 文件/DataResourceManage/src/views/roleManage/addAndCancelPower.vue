<template>
    <div class="add-and-cancel-power">
        <el-tabs v-model="activeName">
            <el-tab-pane label="未授权" name="never">
                <cb-check-box :data="noCheckedAllData" ref="noCheckedRef" @change="change"/>
            </el-tab-pane>
            <el-tab-pane label="已授权" name="checked">
                <cb-check-box :data="checkedAllData" ref="checkedRef" @change="change"/>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component, Prop} from 'vue-property-decorator'
    import CbCheckBox from '../../components/cbCheckbox/index'
    import config from "../../config";

    @Component({
        components: {CbCheckBox},
    })
    export default class AddAndCancelPower extends Vue {
        name: string = 'AddAndCancelPower';
        // 当前被点击的树节点
        @Prop() treeCurrentNode;
        // 当前被选中的树类型(有技术视图的树 业务视图的树 项目视图的树)
        @Prop() checkoutTreeValue;
        // 操作角色的id
        @Prop() roleId;
        activeName: string = 'never';

        // 所有数据
        noCheckedAllData: Array<any> = [];
        checkedAllData: Array<string> = [];
        isOperation: boolean = false;

        change() {
            this.$emit('change')
        }

        /**
         * 获所有的数据
         */
        public async getData(): Promise<any> {
            if (!this.treeCurrentNode) return;
            let url = '';
            let params = {
                size: -1,
                id: this.treeCurrentNode['data'].id
            };
            if (this.checkoutTreeValue === 'technology') {
                url = config.port('technologyView') + '/info';
            } else if (this.checkoutTreeValue === 'business') {
                url = config.port('businessView') + '/elementList';
                params['category'] = 'dataset';
            } else if (this.checkoutTreeValue === 'project') {
                url = config.port('projectView') + '/info';
                params['nodeType'] = 'project';
            }
            let res;
            await this.$http.get(url, {params}).then(response => {
                res = response.data.data.records;
            });
            return res;
        }

        /**
         * 获取被选择的数据
         */
        public async getChecked(): Promise<any> {
            let url = config.port('MetadataManage') + 'roleresourcepermission/list';
            let params = {
                roleId: this.roleId, // 角色ID
                parentResourceIds: this.treeCurrentNode['data'].resourceId, /*被选中树的id*/
            };
            let res = [];
            await this.$http.get(url, {params}).then(response => {
                let data = response.data;
                if (data.code === 0) {
                    data.data.forEach(item => res.push(item.resourceId))
                }
            });
            return res;
        }


        /**
         * 获取节点对应的所有树和被选择的数据
         */
        public getDataAndChecked() {
            setTimeout(() => {
                Promise.all([this.getData(), this.getChecked()]).then(([allData, cehckedData]) => {
                    this.noCheckedAllData = [];
                    this.checkedAllData = [];
                    allData.forEach(item => {
                        if (cehckedData.includes(item['resourceId'])) {
                            this.checkedAllData.push(item);
                        } else {
                            this.noCheckedAllData.push(item);
                        }
                    });
                })
            }, 200)
        }

        /**
         * 给父组件获取被选择的数据
         */
        getCheckboxData() {
            let {data: noCheckedAllData, checkedItem: noCheckedItem} = this.$refs['noCheckedRef']['getData']();
            let {data: checkedAllData, checkedItem: checkedItem} = this.$refs['checkedRef']['getData']();
            return {noCheckedAllData, noCheckedItem, checkedAllData, checkedItem}
        }

        /**
         * 将所有备选的数据设置为空
         */
        setCheckboxNull(){
            this.noCheckedAllData = [];
            this.checkedAllData = [];
        }

    }
</script>

<style lang="less">
    .add-and-cancel-power {
        width: 100%;
        height: 100%;
        display: flex;
        flex-direction: column;

        .el-tabs {
            flex: 1;
            display: flex;
            flex-direction: column;

            .el-tabs__header {
                .el-tabs__nav-wrap {
                    .el-tabs__item {
                        font-size: 12px;
                        line-height: 26px;
                        height: 29px;
                    }
                }
            }

            .el-tabs__content {
                flex: 1;

                & > div {
                    width: 100%;
                    height: 100%;
                }
            }
        }
    }
</style>
