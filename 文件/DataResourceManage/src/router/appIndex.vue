<template>
  <div class="appIndex">

  </div>
</template>

<script lang="ts">
  import Vue from 'vue'
  import Component from 'vue-class-component'

  @Component({
    name: "appIndex",
    components: {}
  })
  export default class appIndex extends Vue {
    myThis:any = this
    item: any = {
      // img: require('../assets/defaultTheme-footer-logo.png'),
      // img:require("E:/avfms/upload/14946621131274ebc23cb-d65e-4614-9d4f-5be20406c53a_5.jpg"),
      /*img:require("C:/Users/ThinkPad/Desktop/TIM图片20180809131319.png"),*/
      text: "图片"

    }
    items: any = Array(6).fill(this.item)
    userName:any=""
    mounted(){
      this.showUser()
    }
    //显示用户
    showUser(){
      let person:any =  sessionStorage.getItem("person")
      const obj = JSON.parse(person)
      if( JSON.parse(person) !=null &&  JSON.parse(person) != ""){
        this.userName = JSON.parse(person).userName.trim()
      }
    }
    imgClick(index):void{
      alert(index);
      //页面跳转加传参
      //页面跳转加传参
      this.myThis.$router.push({ name: 'login', params: { userId: 'asdfdsafsadf' }})
    }
  }
</script>
<style slot-scope='scoped'>

</style>

