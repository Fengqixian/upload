<template>
  <div class="index">
    <div id='main' style='width: 100%;height:400px;'></div>
  </div>
</template>

<script lang="ts">
  import Vue from 'vue'
  import {Component} from 'vue-property-decorator'

  @Component({})
  export default class Index extends Vue {
    name: string = 'Index';
    public myChart: any = '';
    public myChartNodeData: Array<object> = [
      {
        id: '1',
        name: '糖尿病',
        value: 10,
        category: 0,
      }, {
        id: '2',
        name: '心脏病',
        value: 20,
        category: 0,
      }, {
        id: '3',
        name: '高血压',
        value: 10,
        category: 1,
      }, {
        id: '4',
        name: '高血脂',
        value: 100,
        category: 1,
      }, {
        id: '5',
        name: '肥胖症',
        value: 20,
        category: 2,
      }, {
        id: '6',
        name: '高度近视',
        value: 60,
        category: 2,
      }, {
        id: '7',
        name: '皮肤癌',
        value: 70,
        category: 3,
      }, {
        id: '8',
        name: '鼻炎',
        value: 80,
        category: 3,
      }, {
        id: '9',
        name: '秃头',
        value: 90,
        category: 4,
      }, {
        id: '10',
        name: '血友病',
        value: 100,
        category: 4,
      }, {
        id: '11',
        name: '抑郁症',
        value: 110,
        category: 5,
      }, {
        id: '12',
        name: '超重',
        value: 320,
        category: 5,
      }, {
        id: '13',
        name: '骨质酥松',
        value: 130,
        category: 6,
      }
    ];
    public myChartNodeLink: Array<object> = [
      {
        source: '1',
        target: '2',
        value: 10
      }, {
        source: '1',
        target: '3',
      }, {
        source: '1',
        target: '4',
        value: 10000,
      }, {
        source: '1',
        target: '5',
      }, {
        source: '3',
        target: '4',
      }, {
        source: '3',
        target: '5',
      }, {
        source: '3',
        target: '6',
      }, {
        source: '3',
        target: '7',
      }, {
        source: '3',
        target: '8',
      }, {
        source: '3',
        target: '9',
      }, {
        source: '3',
        target: '10',
      }, {
        source: '3',
        target: '11',
      }, {
        source: '3',
        target: '12',
      }, {
        source: '3',
        target: '13',
      }, {
        source: '1',
        target: '13',
      }, {
        source: '2',
        target: '13',
      }, {
        source: '5',
        target: '13',
      }, {
        source: '4',
        target: '13',
      }, {
        source: '6',
        target: '13',
      }, {
        source: '7',
        target: '13',
      }, {
        source: '8',
        target: '13',
      }, {
        source: '9',
        target: '13',
      }, {
        source: '10',
        target: '13',
      }, {
        source: '11',
        target: '13',
      },];
    public charNodeDataScale: number = 0;
    // 这是数据最小值
    public valuesMin: number = 0;
    // 比例数据最小值
    public scaleMin: number = 8;
    // 比例数据最大值
    public scaleMax: number = 50;

    public mounted(): void {
      this.charNodeDataScaleCalc();
      this.myChartNodeLink.forEach(link => {
        const sourceId: string = link['source'];
        const targetId: string = link['target'];
        let sourceData: any = this.myChartNodeData.find(item => item['id'] === sourceId);
        let targetData: any = this.myChartNodeData.find(item => item['id'] === targetId);
        link['value'] = ((sourceData['value'] + targetData['value'])) * 100;
      });
      this.initEcharts()
    }

    // 计算实际值 虚拟值大小差的比例
    public charNodeDataScaleCalc(): void {
      const values: Array<number> = [];
      this.myChartNodeData.forEach(item => values.push(item['value']))
      const valuesMax = Math.max(...values)
      this.valuesMin = Math.min(...values)
      this.charNodeDataScale = (valuesMax - this.valuesMin) / (this.scaleMax - this.scaleMin);
    }

    // 计算实际值对应的虚拟值
    public charNodeDataScaleValue(item): number {
      return (item - this.valuesMin) / this.charNodeDataScale + this.scaleMin;
    }

    public initEcharts(): void {
      const _this = this;
      this.myChart = this.$echarts.init(document.getElementById('main'));
      // this.myChart.showLoading();
      // this.myChart.hideLoading();
      const categories: Array<object> = [];
      for (var i = 0; i < 9; i++) {
        categories.push({name: '类目' + i})
      }
      const option: object = {
        title: {
          text: '关系图',
          subtext: '我是关系图',
          top: '0',
          left: '0'
        },
        legend: [{
          show: true,
          data: categories.map(a => a['name'])
        }],
        tooltip:{},
        animationDuration: 1500,
        animationEasingUpdate: 'quinticInOut',
        series: [
          {
            name: 'Les Miserables',
            type: 'graph',
            symbolSize: function (num) {
              return _this.charNodeDataScaleValue(num);
            },
            layout: 'force',
            data: this.myChartNodeData,
            links: this.myChartNodeLink,
            categories: categories,
            roam: true,
            hoverAnimation: true,
            nodeScaleRatio: 0.9,
            draggable: true,
            focusNodeAdjacency: true,
            force: {
              repulsion: 300,
              gravity: 0.2,
              edgeLength: [10, 300]
            },
            itemStyle: {
              normal: {
                borderColor: '#fff',
                borderWidth: 1,
                shadowBlur: 10,
                shadowColor: 'rgba(0, 0, 0, 0.3)'
              }
            },
            label: {
              position: 'right',
              formatter: '{b}{c}'
            },
            lineStyle: {
              color: 'source',
              curveness: 0.1
            },
            emphasis: {
              lineStyle: {
                width: 10
              }
            }
          }]
      }
      this.myChart.setOption(option)
    }
  }
</script>

<style scoped lang="less">

</style>
