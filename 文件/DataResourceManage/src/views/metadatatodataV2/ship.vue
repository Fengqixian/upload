<template>
  <div class="ship" :sizeChange="sizeChange">
    <div class="ship-container">
      <div :id='"svg_"+id' @click="handleSvgClick" class="svg-container"></div>
      <transition name="fade">
        <div class="info-container" :id='"infoContainer_"+id' v-show="showInfo">
          <header class="info-title">属 性</header>
          <ul>
            <li class="info-item" v-for="(item,key) in infoData" :key="key">
              <span class="label">{{item.label}}</span>
              <span class="value">{{item.value}}</span>
            </li>
          </ul>
        </div>
      </transition>
    </div>
  </div>
</template>

<script lang="ts">
  import Vue from 'vue'
  import {Component, Prop, Watch} from 'vue-property-decorator'
  import {Mutation} from 'vuex-class'
  import {State} from 'vuex-class'
  import Svg from '../../common/js/svg'
  import * as d3 from 'd3'
  import config from '../../config.ts'

  const ICON_FONT = {
    34: 'shujuku',
    38: 'biao',
    46: 'ziduan',
    1769: 'zhibiao',
  };

  @Component({})
  export default class Index extends Vue {
    name: string = 'index';
    @Mutation setLoadingFlag;
    @State windowSize;
    @Prop({type: Object})
    currentModuleInstance;
    @Prop()
    id;
    // 展示属性侧栏
    showInfo: Boolean = false;
    // 属性数据
    infoData: Array<any> = [];
    // 阻止节点重复点击重复请求属性数据
    disableCliked: string = '';

    container: any;
    svg: any;

    mounted() {
      this.$nextTick(() => {
        // this.initShip()
        this.getDataLineage()
      })
    }

    linear(num) {
      const {width} = d3.select('#svg').node().getBoundingClientRect()
      const linear = d3.scaleLinear()
        .domain([0, 1800])
        .range([0, width])
      return linear(num)
    }

    initShip(data) {
      this.svg = new Svg({
        select: `#svg_${this.id}`,
        data,
        shapeHInterval: 100,
        shapeVInterval: 25,
        iconColor: '#559ef2',
        iconSize: 50,
        ballColor: '#fff',
        lineColor: '#31fd6c',
        textStyle: {
          fill: '#fff'
        }
      });
      this.svg.render();
    }

    // 页面首次加载时不执行this.svg.render()
    flag: boolean = true;

    get sizeChange() {
      if (!this.flag) {
        this.svg.resize();
      }
      this.flag = false;
      return this.windowSize
    }

    // 获取点击shape的数据
    handleSvgClick(e) {
      const target = e.target;
      if (target.tagName.toLowerCase() === 'i' || target.tagName.toLowerCase() === 'image') {
        const data = JSON.parse(target.getAttribute('data'));
        if (this.disableCliked === data.oldId) return;
        this.getMetadataModelTreeForEdit(data);
      } else {
        this.showInfo = false;
      }
    }

    @Watch('showInfo')
    showInfoChange(val, oldVal) {
      if (val) {
        d3.select(`#svg_${this.id}_container`).attr('style', `margin-right:${301}px`)
      } else {
        d3.select(`#svg_${this.id}_container`).attr('style', `margin-right:${0}px`);
        this.disableCliked = ''
      }
    }


    // 获取数据
    getDataLineage() {

      const _this = this;
      let model = {
        id: this.currentModuleInstance.module_id,
        parentId: this.currentModuleInstance.module_id,
        moduleInstanceId: this.currentModuleInstance.module_instance_id,
        treeType: this.currentModuleInstance.tree_type
      };
      let URL1 = config.port('dataLineage') + '/getDataLineage';
      this.$http.post(URL1, model).then((response) => {
        if (response.data.status === 1) {
          const data = response.data.data;
          this.analysisData(data, this.currentModuleInstance.module_instance_id)
        } else {
          this.$message.error(response.data.message);
        }

      }).catch(function (response) {
      });
    }

    // 解析符合创建的svg的数据
    analysisData(data, id) {
      const va = 'a' + id;
      let {nodeDataArray, linkDataArray: links} = data;
      let temp = nodeDataArray.filter(item => item.type === 'temp');
      nodeDataArray = nodeDataArray.filter(item => item.type !== 'temp');
      let newLInks: Array<any> = [];
      temp.forEach(item => {
        // link中to指向这个node
        const from = links.find(link => link.to === item.id);
        // link中from指向多个node
        const to = links.filter(link => link.from === item.id);
        if (to.length >= 2) {
          to.map(it => it.from = from.from);
          // newLInks.push(to)
          newLInks = [...newLInks, ...to];
        } else {
          newLInks.push(from)
        }
      });
      links = links.filter(item => {
        return temp.find(tem => item.from === tem.id || item.to === tem.id) === undefined
      });

      // 给每个id、moduleId、parentId 前面添加a，便于d3的select选择节点
      const regExp = /[`~!@#$^&*()=|{}':;',\[\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？]/ig;
      nodeDataArray.map(item => {
        item.oldId = item.id;
        item.oldModuleId = item.moduleId;
        item.oldParentId = item.parentId;
        item.id = (va + item.id).replace(regExp, '');
        item.moduleId = (item.parentId === 'null' ? 'null' : va + item.moduleId).replace(regExp, '');
        item.parentId = (item.parentId === 'null' ? 'null' : va + item.parentId).replace(regExp, '');
      });
      // 给每个links的from、to 前面添加a
      links.map(item => {
        item.oldFrom = item.from;
        item.oldTo = item.to;
        item.from = (va + item.from).replace(regExp, '');
        item.to = (va + item.to).replace(regExp, '');
      });
      let groups = nodeDataArray.filter(item => item.isGroup);
      groups.map(item => {
        item.group = item.id;
        item.title = item.name;
        item.parent = va + '库'
      });
      groups.push({
        group: va + '指标',
        title: '指标',
      });
      groups.push({
        group: va + '库',
        title: '库',
      });
      let zhibiao = nodeDataArray.filter(item => item.parentId === 'null');
      zhibiao.map(item => item.parentId = va + '指标');
      let shapes = nodeDataArray.filter(item => !item.isGroup);
      shapes.map(item => {
        item.group = item.parentId;
        item.type = 'column';
        item.icon = ICON_FONT[item.oldModuleId];
      });

      this.initShip({groups, shapes, links})

      // let data1 = {
      //   groups: [
      //     {
      //       group: 'a2634',
      //       title: 'AB_IP_FeeAccount',
      //       parent: '库',
      //     },
      //     {
      //       group: 'a5194',
      //       title: 'AB_IP_FeeList',
      //       parent: '库',
      //     },
      //     {
      //       group: '指标',
      //       title: '指标',
      //     },
      //     {
      //       group: '库',
      //       title: '库',
      //     },
      //   ],
      //   shapes: [
      //     {
      //       // name: 'TotalOweMoney',
      //       name: 'a3706',
      //       id: 'a3706',
      //       type: 'column',
      //       group: 'a2634',
      //       icon: 'icon-fuwuqi'
      //     },
      //     {
      //       // name: 'TotalYBPaidMoney',
      //       name: 'a3610',
      //       id: 'a3610',
      //       type: 'column',
      //       group: 'a2634',
      //       icon: 'icon-fuwuqi'
      //     },
      //     {
      //       name: 'a9776',
      //       // name: '手术费用',
      //       id: 'a9776',
      //       type: 'column',
      //       group: '指标',
      //       icon: 'icon-fuwuqi'
      //     },
      //     {
      //       name: 'a3754',
      //       // name: 'TotalSpecialMoney',
      //       id: 'a3754',
      //       type: 'column',
      //       group: 'a2634',
      //       icon: 'icon-fuwuqi'
      //     },
      //     {
      //       // name: 'TotalMoney',
      //       name: 'a7082',
      //       id: 'a7082',
      //       type: 'column',
      //       group: 'a5194',
      //       icon: 'icon-fuwuqi'
      //     },
      //     {
      //       // name: 'SerialNo',
      //       name: 'a2746',
      //       id: 'a2746',
      //       type: 'column',
      //       group: 'a2634',
      //       icon: 'icon-fuwuqi'
      //     },
      //     {
      //       // name: '门急诊人次',
      //       name: 'a2226',
      //       id: 'a2226',
      //       type: 'column',
      //       group: '指标',
      //       icon: 'icon-fuwuqi'
      //     },
      //     {
      //       name: 'a9996',
      //       // name: '均次费用',
      //       id: 'a9996',
      //       type: 'column',
      //       group: '指标',
      //       icon: 'icon-fuwuqi'
      //     },
      //     {
      //       // name: '门急诊均次费用',
      //       name: 'a2386',
      //       id: 'a2386',
      //       type: 'column',
      //       group: '指标',
      //       icon: 'icon-fuwuqi'
      //     },
      //     {
      //       // name: '均次门急诊费用占比',
      //       name: 'a10076',
      //       id: 'a10076',
      //       type: 'column',
      //       group: '指标',
      //       icon: 'icon-fuwuqi'
      //     },
      //     {
      //       // name: 'TotalSelfPaidMoney',
      //       name: 'a3658',
      //       id: 'a3658',
      //       type: 'column',
      //       group: 'a2634',
      //       icon: 'icon-fuwuqi'
      //     },
      //     {
      //       // name: '门急诊费用',
      //       name: 'a2306',
      //       id: 'a2306',
      //       type: 'column',
      //       group: '指标',
      //       icon: 'icon-fuwuqi'
      //     },
      //   ],
      //   links: [
      //     {
      //       from:'a9776',
      //       to:'a7082',
      //     },
      //     {
      //       from:'a2226',
      //       to:'a2746',
      //     },
      //     {
      //       from:'a2306',
      //       to:'a3610',
      //     },
      //     {
      //       from:'a2306',
      //       to:'a3754',
      //     },
      //     {
      //       from:'a2306',
      //       to:'a3706',
      //     },
      //     {
      //       from:'a2306',
      //       to:'a3658',
      //     },
      //     {
      //       from:'a10076',
      //       to:'a2386',
      //     },
      //     {
      //       from:'a10076',
      //       to:'a9996',
      //     },
      //     {
      //       from:'a2386',
      //       to:'a2306',
      //     },
      //     {
      //       from:'a2386',
      //       to:'a2226',
      //     },
      //     {
      //       from:'a9996',
      //       to:'a2226',
      //     },
      //     {
      //       from:'a9996',
      //       to:'a2306',
      //     },
      //     {
      //       from:'a9996',
      //       to:'a9776',
      //     },
      //   ]
      // };
      // this.initShip(data1)
    }

    // 获取节点属性
    getMetadataModelTreeForEdit(obj) {

      let URL = config.port('metadataManage') + 'getMetadataModelTreeForEdit';
      var p = {
        parentId: obj.oldModuleId,
        moduleInstanceId: obj.oldId,
        treeType: "instance",
        id: obj.oldModuleId
      };
      this.$http.post(URL, p).then((response) => {

        if (response.data.status === 1) {
          this.disableCliked = obj.oldId
          const data = response.data.data;
          this.analysisNodeData(data)
        } else {
          this.$message.error(response.data.message)
        }
      })
    }

    // 解析节点属性
    analysisNodeData(data) {
      this.showInfo = true;
      const {datas, moduleInfo} = data;
      const {attrs} = moduleInfo;
      let infoData: Array<any> = [];
      attrs.forEach(item => {
        const label = item.att_name_cn;
        const value = datas[item.att_name_en] ? datas[item.att_name_en].meta_value : '';
        infoData.push({label, value});
      });
      this.infoData = infoData;
    }
  }
</script>

<style scoped lang="less">
  .ship {
    width: 100%;
    height: 100%;
    position: relative;
    background: url("../../../static/images/ship-bg.png") center center;

    .ship-container {
      width: 100%;
      height: 100%;
      overflow-y: auto;

      .svg-container {
        width: 100%;
        height: 100%;
      }

      .info-container {
        position: absolute;
        right: 0;
        top: 0;
        bottom: 0;
        width: 301px;
        /*background-color: rgba(255, 255, 255, .4);*/
        background-color: rgba(14, 24, 63, 0.9);
        transform: translateX(0px);

        &.fade-enter-active, &.fade-leave-active {
          transition: transform 0.4s;
        }

        &.fade-enter, &.fade-leave-to {
          transform: translate3D(100%, 0, 0);
        }

        .info-title {
          text-align: center;
          font-size: 2rem;
          font-weight: 600;
          color: #ffffff;
          margin-top: 1rem;
        }

        ul {
          width: 100%;
          color: #fff;
          font-size: 2rem;
          padding: 1.5rem;

          .info-item {
            display: flex;
            width: 100%;
            margin-bottom: 1rem;

            &:last-child {
              margin-bottom: 0;
            }

            .label {
              width: 5rem;
              margin-right: 1rem;
            }

            .value {
              flex: 1;
              color: rgba(255, 255, 255, 0.7);
            }

            .label, .value {
              display: flex;
              align-items: center;
              word-break: break-all;
            }
          }
        }
      }
    }
  }
</style>


