import axios from 'axios'
import api from './api';
import {Message} from 'element-ui';
import store from '../store'
import router from '../router'

axios.defaults.withCredentials = true;
// 创建实例时设置配置的默认值
const instance = axios.create({
    baseURL: api.baseUrl,
    withCredentials: true
});
let url: string = '';  // 缓存请求的url，request的error中使用

// @ts-ignore
// 请求拦截器
instance.interceptors.request.use(config => {
    store.commit('setLoadingFlag', true);
    // @ts-ignore
    url = config['url'];
    // @ts-ignore
    if (!url.match('/auth/oauth/token')) {
        config.headers.Authorization = `Bearer ${store.getters.getAccessToken || sessionStorage.getItem('access_token')}`
    }
    return config
}, error => {
    Message.warning('操作失败');
    /*router.push('/login');*/
    store.commit('setLoadingFlag', false);
    return Promise.reject(error)
});


// @ts-ignore
// 响应拦截器
instance.interceptors.response.use(response => {
    store.commit('setLoadingFlag', false);
    const data = response.data;
    if (data.status == 200 || data.status == 1 || data.status == undefined) {
        return response
    } else {
        Message.warning(data.message);
        return response
    }

}, error => {
    // @ts-ignore
    if (url.match('/auth/oauth/token')) {
        Message.error('用户名或者密码不正确，请重新输入！');
    } else {
        Message.error('操作失败！');
        /*router.push('/login');*/
    }
    store.commit('setLoadingFlag', false);
    store.commit('setRandomStr', Math.random());
    return Promise.reject(error)
});

export default instance
