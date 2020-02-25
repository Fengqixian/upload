<template>
  <div class="ship" :sizeChange="sizeChange">
    <div class="ship-container">
      <div id="svg" @click="handleSvgClick"></div>
    </div>
  </div>
</template>

<script lang="ts">
  import Vue from 'vue'
  import {Component} from 'vue-property-decorator'
  import {State} from 'vuex-class'
  import Svg from '../../common/js/svg'
  import * as d3 from 'd3'

  @Component({})
  export default class Index extends Vue {
    name: string = 'index'
    @State windowSize

    container: any;
    svg: any;

    mounted() {
      this.$nextTick(() => {
        this.initShip()
      })
    }

    initShip() {
      const data = {
        shapes: [
          {
            name: '指标0',
            id: '指标0',
            type: 'server',
            group: '指标',
            icon: 'fuwuqi'
          },
          {
            name: '指标1',
            id: '指标1',
            type: 'server',
            group: '指标',
            icon: 'fuwuqi'
          },
          {
            name: '指标2',
            id: '指标2',
            type: 'database',
            group: '指标',
            icon: 'fuwuqi'
          },
          {
            name: '指标3',
            id: '指标3',
            type: 'database',
            group: '指标',
            icon: 'fuwuqi'
          },
          {
            name: '指标4',
            id: '指标4',
            type: 'database',
            group: '指标',
            icon: 'fuwuqi'
          },
          {
            name: '指标5',
            id: '指标5',
            type: 'database',
            group: '指标',
            icon: 'fuwuqi'
          },
          {
            name: 'table1字段1',
            id: 'table1字段1',
            type: 'column',
            group: '库table1',
            icon: 'fuwuqi'
          },
          {
            name: 'table1字段2',
            id: 'table1字段2',
            type: 'column',
            group: '库table1',
            icon: 'fuwuqi'
          },
          {
            name: 'table1字段3',
            id: 'table1字段3',
            type: 'column',
            group: '库table1',
            icon: 'fuwuqi'
          },
          {
            name: 'table2字段1',
            id: 'table2字段1',
            type: 'column',
            group: '库table2',
            icon: 'fuwuqi'
          },
          {
            name: 'table2字段2',
            id: 'table2字段2',
            type: 'column',
            group: '库table2',
            icon: 'fuwuqi'
          },
          {
            name: 'table2字段3',
            id: 'table2字段3',
            type: 'column',
            group: '库table2',
            icon: 'fuwuqi'
          },

          {
            name: 'table3字段1',
            id: 'table3字段1',
            type: 'column',
            group: '库table3',
            icon: 'fuwuqi'
          }, {
            name: 'table3字段2',
            id: 'table3字段2',
            type: 'column',
            group: '库table3',
            icon: 'fuwuqi'
          }, {
            name: 'table3字段3',
            id: 'table3字段3',
            type: 'column',
            group: '库table3',
            icon: 'fuwuqi'
          }, {
            name: 'table3字段4',
            id: 'table3字段4',
            type: 'column',
            group: '库table3',
            icon: 'fuwuqi'
          }, {
            name: 'table4字段1',
            id: 'table4字段1',
            type: 'column',
            group: '库table4',
            icon: 'fuwuqi'
          }, {
            name: 'table4字段2',
            id: 'table4字段2',
            type: 'column',
            group: '库table4',
            icon: 'fuwuqi'
          }, {
            name: 'table4字段3',
            id: 'table4字段3',
            type: 'column',
            group: '库table4',
            icon: 'fuwuqi'
          }, {
            name: 'table4字段4',
            id: 'table4字段4',
            type: 'column',
            group: '库table4',
            icon: 'fuwuqi'
          },
          {
            name: '其他table1字段1',
            id: '其他table1字段1',
            type: 'column',
            group: '其他table1',
            icon: 'fuwuqi'
          },

          {
            name: '库1table1字段1',
            id: '库1table1字段1',
            type: 'column',
            group: '库1table1',
            icon: 'fuwuqi'
          },
          {
            name: '库1table1字段2',
            id: '库1table1字段2',
            type: 'column',
            group: '库1table1',
            icon: 'fuwuqi'
          },
          {
            name: '库1table1字段3',
            id: '库1table1字段3',
            type: 'column',
            group: '库1table1',
            icon: 'fuwuqi'
          },
        ],
        links: [
          {
            from: '指标0',
            to: '指标1'
          },
          {
            from: '指标0',
            to: '指标4'
          },
          {
            from: '指标4',
            to: '指标5'
          },
          {
            from: '指标5',
            to: '库1table1字段1'
          },
          {
            from: '指标4',
            to: '指标2'
          },
          {
            from: '指标4',
            to: '指标3'
          },
          {
            from: '指标1',
            to: '指标2'
          },
          {
            from: '指标1',
            to: '指标3'
          },
          {
            from: '指标3',
            to: 'table1字段2'
          },
          {
            from: '指标3',
            to: 'table2字段1'
          },
          {
            from: 'table2字段1',
            to: 'table3字段1'
          },
          {
            from: 'table2字段1',
            to: 'table3字段2'
          },
          {
            from: 'table1字段1',
            to: 'table3字段2'
          },
          {
            from: 'table3字段1',
            to: 'table4字段1'
          },
          {
            from: 'table4字段1',
            to: 'table4字段2'
          },
          {
            from: 'table4字段2',
            to: 'table4字段3'
          },
          {
            from: 'table4字段2',
            to: 'table4字段4'
          },
          {
            from: 'table4字段3',
            to: '其他table1字段1'
          },
          {
            from: 'table2字段2',
            to: 'table2字段3'
          },
          {
            from: 'table2字段3',
            to: 'table3字段4'
          },
        ],
        groups: [
          {
            group: '指标',
            title: '指标',
            // serializeTypes: ['server', 'database', 'table', 'column'],
          },
          {
            group: '库',
            title: '库',
            serializeTypes: ['server', 'database', 'table', 'column'],
          },
          {
            group: '库table1',
            title: '库table1',
            parent: '库',
            // serializeTypes: ['server', 'database', 'table', 'column'],
          },
          {
            group: '库table2',
            title: '库table2',
            parent: '库',
            serializeTypes: ['server', 'database', 'table', 'column'],
          },
          {
            group: '库table3',
            title: '库table3',
            parent: '库',
            // serializeTypes: ['server', 'database', 'table', 'column'],
          },
          {
            group: '库table4',
            title: '库table4',
            parent: '库',
            // serializeTypes: ['server', 'database', 'table', 'column'],
          },
          {
            group: '其他',
            title: '其他',
            serializeTypes: ['server', 'database', 'table', 'column']
          },
          {
            group: '其他table1',
            title: '其他table1',
            parent: '其他',
            serializeTypes: ['server', 'database', 'table', 'column'],
          },
          {
            group: '库1',
            title: '库1',
            serializeTypes: ['server', 'database', 'table', 'column'],
          },
          {
            group: '库1table1',
            title: '库1table1',
            parent: '库1',
            // serializeTypes: ['server', 'database', 'table', 'column'],
          },
        ]
      };
      this.svg = new Svg({
        select: '#svg',
        data,
        // shapeHInterval: this.linear(100),
        shapeHInterval: 100,
        // shapeVInterval: this.linear(60),
        shapeVInterval: 25,
        iconColor: '#559ef2',
        // iconSize: this.linear(70),
        iconSize: 50,
        ballColor: '#fff',
        lineColor: '#31fd6c',
        textStyle: {
          fill: '#fff'
        }
      });
      this.svg.render();
      // 获取底部infoContainer高度作为svg盒子的margin-bottom
      // const {height} = d3.select('#infoContainer').node().getBoundingClientRect()
      // d3.select('svg').attr('style', `margin-bottom:${height + 10}px`)
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
      const target = e.target
      if (target.tagName.toLowerCase() === 'i') {
      }
    }
  }
</script>

<style scoped lang="less">
  .ship {
    overflow-x: auto;
    position: relative;
    background: url("../../../static/images/ship-bg.png") center center;

    .ship-container {
      width: 100%;
      height: 100%;
      overflow-y: auto;

      #svg {
        width: 100%;
        height: 100%;
      }

      .info-container {
        position: absolute;
        bottom: 0;
        left: 0;
        right: 0;
        height: 40%;
        background-color: rgba(255, 255, 255, 0.5);
        padding: 10px;

        .el-row {
          width: 100%;
          height: 100%;
          margin: 0 !important;

          .el-col {
            height: 100%;

            .info-item {
              height: 100%;
              background-color: #fff;
              overflow: auto;
              padding: 1rem;

              .title {
                font-size: 2rem;
                font-weight: 600;
              }
            }
          }
        }
      }
    }
  }
</style>


