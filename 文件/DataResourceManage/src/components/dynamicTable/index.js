import Component from './dynamicTable'

// export default (Vue) => {
//   Vue.Component(Component.name, Component)
// }

/* istanbul ignore next */
Component.install = function(Vue) {
  Vue.component(Component.name, Component);
};

export default Component;
