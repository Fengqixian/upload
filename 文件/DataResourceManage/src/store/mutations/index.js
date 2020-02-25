import types from '../types'
import store from '../index'

const mutations = {
    setLoadingFlag(state, value) {
        // 加载
        if (value) {
            // 记录加载次数
            state.loadingNum < 0 ? state.loadingNum = 1 : state.loadingNum++;
        } else {
            // 取消加载
            state.loadingNum < 0 ? state.loadingNum = 0 : state.loadingNum--;
        }
        // state.loadingFlag = value
    },
    upDateMenuWidth(state, value) { // 设置菜单的宽度
        state.menuWidth = value
    },
    changeWindowSize(state, value) { // 屏幕宽度 这个值右window.onresize控制
        state.windowSize = value
    },
    setAccessToken(state, value) {
        state.access_token = value; // token
    },
    setRandomStr(state, value) { // 设置验证码
        state.randomStr = value;
    },
    /**
     * 修改baseField的options
     * @param state
     * @param field
     * @param options
     */
    setBaseField(state, {field, options}) {
        // 更新字段基本数据
        let baseFiled = state.baseField;
        baseFiled.map(item => {
            if (item.prop === field) {
                item.options = options;
            }
        });
        state.baseField.splice(0, baseFiled.length, ...baseFiled);
        // 更新 table 头部基本的展示字段
        let baseTableHeaderData = [
            {
                prop: 'createTime',
                label: '创建时间',
                value: ''
            },
            {
                prop: 'updateTime',
                label: '更新时间',
                value: ''
            }
        ];
        state.baseTableHeaderData.splice(0, state.baseTableHeaderData.length, ...state.baseField, ...baseTableHeaderData);
    },

    /**
     * 字段赋值  这个用于tab头部详情展示  将值和字段名组装在一起
     * @param state
     * @param fields 字段
     * @param values 值
     */
    letFieldsAssignments(state, {fields, values}) {
        fields.forEach(item => {
            if (item['type'] === 'select'/* || item['type'] === 'radio' || item['type'] === 'switch'*/) {
                // 如果type是选择类型的，将值映射为options里面的解释型数据
                item['options'].forEach(ite => {
                    if (ite['itemsCode'] === values[item['prop']]) {
                        item['value'] = ite['itemsName']
                    }
                });
            } else {
                item['value'] = values[item['prop']]
            }
        })
    },
    /**
     * 设置主题
     * @param state
     * @param theme
     */
    setTheme(state, theme) {
        state.theme = theme;
        store.commit('writeNewStyle', theme);
    },
    /**
     * 生成新的主题样式
     * @param state
     * @param theme  新的主题颜色集合
     */
    writeNewStyle(state, theme) {
        let cssText = state.themeStyle;
        let themes = state[theme];
        Object.keys(themes).forEach(key => {
            cssText = cssText.replace(new RegExp(key, 'ig'), themes[key])
        });

        cssText = `<style id="themeStyle">${cssText}</style>`;

        var x = document.createElement('div');
        x.innerHTML = cssText;
        var head = document.getElementsByTagName('head')[0];
        let themeStyle = document.querySelector("#themeStyle");
        // themeStyle ? themeStyle.remove() : null; // 删除已经存在的 theme 样式
        themeStyle ? themeStyle.parentNode.removeChild(themeStyle) : null; // 删除已经存在的 theme 样式
        head.appendChild(x.lastChild);
    }
};

export default mutations
