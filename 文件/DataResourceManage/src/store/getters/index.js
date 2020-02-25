import types from '../types'

const getters = {
  getAccessToken(state) { // token
    return state.access_token;
  },
  getRandomStr(state) { // 验证码
    return state.randomStr;
  }
};
export default getters
