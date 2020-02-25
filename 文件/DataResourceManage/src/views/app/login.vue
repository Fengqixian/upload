<template>
    <div id="login">
      <div class="el-header" style="height:50px;font-size: 12px;width:100%">
       <div style="padding-left: 5px;width:40%;float: left;text-align: left;"> <i class="el-icon-arrow-left"></i></div>
        <div style="padding-right: 5px;width:50%;float: right;">
        <el-dropdown>
          <i class="el-icon-setting" style="margin-right: 15px"></i>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item>查看</el-dropdown-item>
            <el-dropdown-item>新增</el-dropdown-item>
            <el-dropdown-item>删除</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <span>{{userName}}ddd</span>
        </div>
      </div>
      <el-container style="height: 100%; border: 1px solid #eee">
        <el-aside style="background-color: rgb(238, 241, 246)">
        啊发发
        </el-aside>

        <el-main>
         时代感啊是否是否
        </el-main>

      </el-container>

    </div>

</template>

<script lang="ts">
  import Vue from 'vue'
  import Component from 'vue-class-component'

  @Component({
    name: "login",
  })
  export default class login extends Vue {
    myThis:any = this
    mounted(){
      // alert(this.$route.params.userId);
    }
  }
</script>

<style scoped>

</style>
