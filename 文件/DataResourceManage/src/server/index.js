import axios from 'axios';
import api from './api';
import { Message } from 'element-ui';
axios.defaults.withCredentials = true;
// 创建实例时设置配置的默认值
var instance = axios.create({
    baseURL: api.baseUrl,
    withCredentials: true
});
// @ts-ignore
// 请求拦截器
instance.interceptors.request.use(function (config) {
    return config;
}, function (error) {
    Message.warning('操作失败');
    return Promise.reject(error);
});
// @ts-ignore
// 响应拦截器
instance.interceptors.response.use(function (response) {
    // const status = response.data.status
    // if (status === 200 || status === 1) {
    //   return response
    // } else {
    //   Message.warning('操作失败')
    //   return status
    // }
    return response;
}, function (error) {
    Message.warning('操作失败');
    return Promise.reject(error);
});
export default instance;
