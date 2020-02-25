<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script lang="ts">
  import Vue from 'vue'
  import {Component} from 'vue-property-decorator'
  import Layout from './views/layout'

  @Component({
    components: {
      Layout
    }
  })
  export default class App extends Vue {
    name: string = 'index'
  }
</script>

<style scoped lang="less">
  #app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    //text-align: center;
    color: #2c3e50;
    margin-top: 0px;
  }
</style>
