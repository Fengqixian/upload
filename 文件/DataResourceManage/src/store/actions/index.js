import types from '../types'
import config from '../../config.ts'
import http from '../../server/index'
import store from '../index'


const actions = {
    /**
     * 获取常量方法
     * @param dictId  常量分类的id
     * @returns {Promise<Array>} 分类下的常量
     */
    async getConstant(state, dictId) {
        let params = {
            dictId,
            size: -1
        };
        let url = config.port('dictinfo') + 'getDictDetailPage';
        let ret = [];
        await http.get(url, {params}).then(response => {
            const res = response.data;
            if (res.code === 0) {
                ret = res.data.records;
            }
        });
        return ret;
    },


    /**
     * 获取基础字段中options
     * @param field 字段名称nameEn
     */
    async getBaseFiledOptions(state, field) {
        // url 中status代表获取status的options
        let url = config.port('dictinfo') + 'selectItemsValue/' + field;
        let options = [];
        await http.get(url).then(response => {
            const res = response.data;
            if (res.code === 0) {
                 options = res.data;
                if (field === 'status') {
                    options.map(item => {
                        item['itemsCode'] = parseInt(item['itemsCode']);
                    });
                    store.commit('setBaseField', {field: 'status', options});
                }
            }
        });
        return options;
    },

    /**
     * 查寻字段的属性
     * @param modelResourceId
     * @returns {Promise<Array>}
     */
    async getMetadataProperties(state, modelResourceId) {
        let url = config.port('metadataproperties') + '/itemsPage';
        let params = {
            modelResourceId,   // 根据元数据查询元数据的属性 传modelId
            size: -1 // 查询所有
        };
        let retArr = [];
        await http.get(url, {params}).then(response => {
            retArr = response.data.data.records;
            retArr.map(item => {
                item['prop'] = item['mappingField'];    // 字段英文名 --- 字段标识
                item['label'] = item['nameCn'];         // 字段中文名  -- 字段解释
                item['value'] = '';                     // 部分需求 需要展示值 --- 字段的值
                item['type'] = item['showType'];        // 这个子弹使用什么类型属于什么类型
                item['options'] = item['itemsList'];    // 字段值得选项
            })
        });
        return retArr;
    }

};

export default actions
