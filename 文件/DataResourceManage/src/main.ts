// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import Router from 'vue-router'
import Vuex from 'vuex'
import "babel-polyfill";
import '@/element-ui'
import 'element-ui/lib/theme-chalk/index.css'
// import './common/css/index.less'
import './common/css/font/iconfont.css'
import App from './App.vue'
import http from './server'
import DynamicForm from './components/dynamicForm'  // 动态form表单
import DynamicTable from './components/dynamicTable'  // 动态table表格
import ScreeningTree from './components/screeningTree'  // 筛选Tree
import ModelTree from './components/modelTree'  // 筛选Tree
import CbTree from './components/cbTree'  // 完整的tree树
import CbTab from './components/cbTab'
import CbTable from './components/cbTable'
import CbOperationContainer from './components/cbOperationContainer'
import CbSideInfo from './components/cbSideInfo'
import './common/moc/menu'
import router from './router'
// @ts-ignore
import store from '@/store'
import echarts from 'echarts';
// 原型上添加 axios 方法
Vue.prototype.$http = http;
Vue.prototype.$echarts = echarts;
// @ts-ignore

// import DynamicTable from './components/dynamicTable/dynamicTable'
// Vue.component(DynamicTable.name, DynamicTable)

Vue.use(Router);
// 动态form表单
Vue.use(DynamicForm);
// 动态table表格
Vue.use(DynamicTable);
// 筛选Tree
Vue.use(ScreeningTree);
// 模型tree
Vue.use(ModelTree);
Vue.use(CbTree);
Vue.use(CbTab);
Vue.use(CbTable);
Vue.use(CbOperationContainer);
Vue.use(CbSideInfo);
Vue.config.productionTip = false;
Vue.directive('scroll', {
    bind(el, binding) {
        const selectWrap = el.querySelector('.el-table__body-wrapper') || el
        selectWrap.addEventListener('scroll', function () {
            binding.value(selectWrap.scrollLeft, selectWrap.scrollTop);
        })
    }
})

/* eslint-disable no-new */
// @ts-ignore
const vm = new Vue({
    el: '#app',
    store,
    router,
    components: {App},
    template: '<App/>'
})
