<template>
  <div class="sheets-container version-manage header-operate-input">
    <div v-if="refresh" class="version-container">
      <el-row class="switch-version operate">
        <el-form ref="form" label-width="62px">
          <el-form-item label="版本选择">
            <el-select filterable v-model.trim="selectionCode" @change="versionSelectionnChange">
              <el-option
                v-for="x in allVersions"
                :key="x.vs_code"
                :label="x.vs_name"
                :value="x.vs_code"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <el-button style="margin-left: 10px;" v-if="selectVersion.vs_status!==1" @click="submitSwitchVersion">切换版本
        </el-button>
        <el-button v-if="selectVersion.vs_status===1" @click="publishOrCloneDialog(0)">创建分支
        </el-button>
        <el-button v-if="selectVersion.vs_status!==1" @click="publishOrCloneDialog(1)">发布版本
        </el-button>
      </el-row>
      <el-row class="compare-version container-wrapper">
        <!--<el-button type="primary" @click="versionCompare" class="compare-version-btn">版本比较</el-button>-->
        <div class="compare-version-item-wrapper">
          <el-col :span="12">
            <version-manage-tree
              @currentSelectionTreeNode="currentSelectionTreeNode"
              ref="versionManagerTreeLeft"
              :selector="false"
              :historyVersions="allVersions">
            </version-manage-tree>
          </el-col>
          <el-col :span="12">
            <version-manage-tree
              @versionManageHisVersionChange="versionManageHisVersionChange"
              :historyVersions="allVersions">
            </version-manage-tree>
          </el-col>
        </div>
      </el-row>
    </div>
    <!--发布、分支弹出框-->
    <el-dialog title="信息"
               :visible.sync="publishOrCloneDialogShow"
               width="50%"
               :close-on-click-modal="false">
      <div v-if="dialogType == 1">
        <el-form label-width="120px">
          <el-form-item label="版本名称">
            <el-input v-model.trim="selectVersion.vs_name"></el-input>
          </el-form-item>
          <el-form-item label="备注">
            <el-input type="textarea" v-model.trim="selectVersion.remark"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <div v-if="dialogType == 0">
        <el-form label-width="120px">
          <el-form-item label="版本名称">
            <el-input v-model.trim="selectVersion.vs_name"></el-input>
          </el-form-item>
          <el-form-item label="备注">
            <el-input type="textarea" v-model.trim="selectVersion.vs_desc"></el-input>
          </el-form-item>
        </el-form>
      </div>

      <span slot="footer" class="dialog-footer">
            <el-button @click="publishOrCloneDialogShow = false">取 消</el-button>
            <el-button type="primary" @click="submitPublishOrClone">确 定</el-button>
          </span>
    </el-dialog>
    <!--发布、分支弹出框-->
    <el-dialog title="版本比较"
               :visible.sync="versionCompareDialogShow"
               width="90%"
               :close-on-click-modal="false">
      <version-compare v-if="versionCompareDialogShow" :defaultExpandedKeys="defaultExpandedKeys"
                       :hisVersionCode="hisVersionCode"
                       :selectedTreeNodeKey="currentSelectionTreeNodeKey"></version-compare>
    </el-dialog>
  </div>
</template>
<script lang="ts">
  import Vue from 'vue'
  import Component from 'vue-class-component'
  import config from '../../config.ts'
  import {State, Mutation} from 'vuex-class'
  import Qs from 'qs'
  import VersionManageTree from './versionManageTree.vue'
  import VersionCompare from './versionCompare.vue'

  @Component({
    name: 'versionManage',
    components: {VersionManageTree, VersionCompare}
  })
  export default class VersionManage extends Vue {
    @State loadingFlag
    @Mutation setLoadingFlag
    refresh = true
    selectionCode = ''
    selectVersion = {
      vs_code: '',
      vs_name: '',
      remark: ''
    };
    editingVersions = [];
    historyVersions = []
    allVersions = []

    hisVersionCode = ""

    publishOrCloneDialogShow = false;
    dialogType = 0;
    versionCompareDialogShow = false;

    //版本比较时左边展开过的树；
    defaultExpandedKeys = [];
    currentSelectionTreeNodeKey = "";

    created() {
      this.initSelectionOptions();
    }

    publishOrCloneDialog(type) {
      this.dialogType = type;
      this.publishOrCloneDialogShow = true;
    }

    /*初始化下拉框的值  历史版本historyVersions 、可编辑版本editingVersions、所有版本 allVersions*/
    initSelectionOptions() {
      let URL = config.port('versionManage') + 'getVersions';

      this.$http.post(URL, {}).then((response) => {

        this.editingVersions = response.data.data.editingVersions;
        this.historyVersions = response.data.data.historyVersions;
        this.allVersions = response.data.data.allVersions;
        this.selectionCode = localStorage.getItem("versionCode") || "";
      }).catch(function (response) {
      })
    }

    versionSelectionnChange() {
      for (var i = 0; i < this.allVersions.length; i++) {
        let v = this.allVersions[i];
        if (this.selectionCode == v['vs_code']) {
          this.selectVersion = v;
        }
      }
    }

    /*发布版本提交*/
    submitPublishOrClone() {
      this.publishOrCloneDialogShow = false;
      var param = {};
      if (this.dialogType == 1) {
        param = {
          vsName: this.selectVersion.vs_name,
          vsStatus: this.dialogType,
          version: this.selectVersion.vs_code,
          remark: this.selectVersion.remark
        }
      } else if (this.dialogType == 0) {
        param = {
          vsName: this.selectVersion.vs_name,
          vsStatus: this.dialogType,
          version: this.selectVersion.vs_code,
          remark: this.selectVersion.remark
        }
      }
      let URL = config.port('versionManage') + 'backUpVersion';

      this.$http.post(URL, param).then((response) => {

        this.$message({
          message: response.data.message,
          type: 'success',
          showClose: true
        });
        this.initSelectionOptions();
      }).catch(function (response) {
      })
    }

    /*切换版本*/
    submitSwitchVersion() {
      let URL = config.port('versionManage') + 'switchVersion';

      this.$http.post(URL, {versionCode: this.selectVersion.vs_code}).then((response) => {

        this.$message({
          message: response.data.message,
          type: 'success',
          showClose: true
        });
        //刷新页面
        this.refresh = false;
        this.$nextTick(() => {
          this.refresh = true
        })
        localStorage.setItem("versionCode", this.selectVersion.vs_code);
      }).catch(function (response) {
      })
    }

    /*版本比较*/
    versionCompare() {
      var root = this.$refs.versionManagerTreeLeft['$refs'].versionManagerTree.root;
      var childData = [];
      this.parseRoot(root.childNodes, childData);
      //树形结构的菜单
      this.defaultExpandedKeys = childData;
      this.$nextTick(() => {
        this.versionCompareDialogShow = true;
      })
    }

    parseRoot(root, childData) {
      if (root && root.length > 0) {
        for (var i = 0; i < root.length; i++) {
          let node = root[i].data;
          if (root[i].expanded) {
            childData.push(node.tree_node_key);

            if (root[i].childNodes && root[i].childNodes.length > 0) {
              node.children = [];
              this.parseRoot(root[i].childNodes, childData);
            }
          }
        }
      }
    }

    /*右边版本下拉框的值改变后回调函数*/
    versionManageHisVersionChange(res) {
      this.hisVersionCode = res.vs_code;
    }

    /*右边版本下拉框的值改变后回调函数*/
    currentSelectionTreeNode(res) {
      this.currentSelectionTreeNodeKey = res;
    }
  }
</script>

<style lang="less">

  .absolute(@top) {

    position: absolute;
    top: @top;
    left: 0;
    right: 0;
    bottom: 0;
  }

  .version-container {
    .absolute(0);

    .compare-version {
      .absolute(3.5rem);
      padding: 2rem;
      overflow: hidden;

      .compare-version-item-wrapper {
        .absolute(0);
        padding: 2rem;
        display: flex;
        justify-content: space-between;

        .el-col-12 {
          width: 49.5%;
          height: 100%;
          background-color: #fff;

          .version-manage-tree {
            width: 100%;
            height: 100%;
          }
        }
      }
    }

  }
</style>
