<template>
    <div class="home">
        <div class="field">
            <div class="title"><i class="circle"></i>数据领域</div>
            <div class="field-container key-value">
                <ul>
                    <li>
                        <i class="iconfont icon-first-renyuan"></i>
                        <span class="label">人员</span>
                        <span class="value">{{dataArea.renYuan}}</span>
                    </li>
                    <li>
                        <i class="iconfont icon-first-zhenduan"></i>
                        <span class="label">诊断</span>
                        <span class="value">{{dataArea.yuanQianZhenDuan}}</span>
                    </li>
                    <li>
                        <i class="iconfont icon-first-guahao"></i>
                        <span class="label">挂号</span>
                        <span class="value">{{dataArea.guaHao}}</span>
                    </li>
                </ul>
                <ul>
                    <li>
                        <i class="iconfont icon-first-jiuzhen"></i>
                        <span class="label">就诊</span>
                        <span class="value">{{dataArea.jiuZhen}}</span>
                    </li>
                    <li>
                        <i class="iconfont icon-first-yuyue"></i>
                        <span class="label">预约</span>
                        <span class="value">{{dataArea.yuYue}}</span>
                    </li>
                    <li>
                        <i class="iconfont icon-first-shenqing"></i>
                        <span class="label">申请</span>
                        <span class="value">{{dataArea.shenQing}}</span>
                    </li>
                </ul>
                <ul>
                    <li>
                        <i class="iconfont icon-first-yizhu"></i>
                        <span class="label">医嘱</span>
                        <span class="value">{{dataArea.yiZu}}</span>
                    </li>
                    <li>
                        <i class="iconfont icon-first-chufang"></i>
                        <span class="label">处方</span>
                        <span class="value">{{dataArea.chuFang}}</span>
                    </li>
                    <li>
                        <i class="iconfont icon-first-bingli"></i>
                        <span class="label">病历</span>
                        <span class="value">{{dataArea.bingLi}}</span>
                    </li>
                </ul>
                <ul>

                    <li>
                        <i class="iconfont icon-first-shouma"></i>
                        <span class="label">手麻</span>
                        <span class="value">{{dataArea.shouMa}}</span>
                    </li>
                    <li>
                        <i class="iconfont icon-first-huli"></i>
                        <span class="label">护理</span>
                        <span class="value">{{dataArea.binAnHuLi}}</span>
                    </li>
                    <li>
                        <i class="iconfont icon-first-yiji"></i>
                        <span class="label">医技</span>
                        <span class="value">{{dataArea.yiJi}}</span>
                    </li>
                </ul>
                <ul>

                    <li>
                        <i class="iconfont icon-first-feiyong"></i>
                        <span class="label">费用</span>
                        <span class="value">{{dataArea.feiYong}}</span>
                    </li>
                    <li>
                        <i class="iconfont icon-first-ziyuan"></i>
                        <span class="label">资源</span>
                        <span class="value">{{dataArea.ziChanZiYuan}}</span>
                    </li>
                    <li>
                        <i class="iconfont icon-bingan"></i>
                        <span class="label">病案</span>
                        <span class="value">{{dataArea.binAnHuLi}}</span>
                    </li>
                </ul>
            </div>
        </div>
        <div class="standard">
            <div class="title"><i class="circle"></i>数据标准</div>
            <div class="standard-container">
                <div class="key-value standard-content">
                    <div>
                        <span class="standard-title">omaha</span>
                        <ul>
                            <li>
                                <i class="iconfont icon-gainian"></i>
                                <span class="label">概念</span>
                                <span class="value">{{omaha.concept}}万</span>
                            </li>
                            <li>
                                <i class="iconfont icon-shuyu"></i>
                                <span class="label">术语</span>
                                <span class="value">{{omaha.term}}万</span>
                            </li>
                            <li>
                                <i class="iconfont icon-guanxi"></i>
                                <span class="label">关系</span>
                                <span class="value">{{omaha.relationship}}万</span>
                            </li>
                            <li>
                                <i class="iconfont icon-yingshe"></i>
                                <span class="label">映射</span>
                                <span class="value">{{omaha.mapping}}万</span>
                            </li>
                        </ul>
                        <span class="standard-title" style="margin-top: 20px;">snomedct</span>
                        <ul>
                            <li>
                                <i class="iconfont icon-gainian"></i>
                                <span class="label">概念</span>
                                <span class="value">{{snomedct.concept}}万</span>
                            </li>
                            <li>
                                <i class="iconfont icon-shuyu"></i>
                                <span class="label">术语</span>
                                <span class="value">{{snomedct.term}}万</span>
                            </li>
                            <li>
                                <i class="iconfont icon-guanxi"></i>
                                <span class="label">关系</span>
                                <span class="value">{{snomedct.relationship}}万</span>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="pie standard-content">
                    <div>
                        <span class="standard-title">国标</span>
                        <div class="charts charts-pie" ref="pie"></div>
                    </div>
                </div>
                <div class="tree-map standard-content">
                    <div>
                        <span class="standard-title">行标</span>
                        <div class="charts tree-map-pie" ref="treeMap"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component} from 'vue-property-decorator'
    import config from "../../config";

    @Component({})
    export default class Home extends Vue {
        name: string = 'Home';
        dataArea = {
            /*'renYuan': 88,
            'ziYuan': 88,
            'yiJi': 88,
            'yuanGan': 88,
            'yuanQianZhenDuan': 88,
            'shenQing': 88,
            'zhiLiao': 88,
            'linChuangLuJing': 88,
            'guaHao': 88,
            'yiZu': 88,
            'shouMa': 88,
            'suiFang': 88,
            'houZhen': 88,
            'chuFang': 88,
            'binAnHuLi': 88,
            'ziChanZiYuan': 88,
            'jiuZhen': 88,
            'yaoShi': 88,
            'yiBao': 88
       */
        };
        omaha = {
            /* concept: 79,
             term: 101,
             relationship: 265,
             mapping: 54*/
        };
        snomedct = {
            /* concept: 53,
             term: 156,
             relationship: 479*/
        };
        pieData = [];
        treeMapData = [];
        pieChart = null;
        treeMapChart = null;

        async mounted() {
            setTimeout(() => {
                this.getData();
                window.addEventListener('resize', () => {
                    this.pieChart.resize();
                    this.treeMapChart.resize();
                })
            }, 20)
        }

        /**
         * 获取首页数据
         */
        getData() {
            let url = config.port('index');
            this.$http.get(url).then(response => {
                if (response.data.code === 0) {
                    const {dataArea, omaha, snomedct, pieData, treeMapData} = response.data.data;
                    this.dataArea = dataArea;
                    this.omaha = omaha;
                    this.snomedct = snomedct;
                    this.pieData = pieData;
                    this.treeMapData = treeMapData;
                    this.initPie();
                    this.initTreeMap(treeMapData);
                }
            })

        }

        /**
         * 初始化饼状图
         */
        initPie() {
            this.pieChart = this.$echarts.init(this.$refs.pie);
            let pieColor = ['#0057B4', '#1890FF', '#0071FF', '#6EBBFE', '#92182A', '#FF85C0', '#FF5750', '#FF9E55', '#FFF566', '#B89A6A', '#52C41A', '#00E575', '#13C2C2', '#B37FEB', '#A3BAFF', '#0057B4', '#1890FF', '#6EBBFE'];
            let options = {
                color: pieColor,
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b}: {c} ({d}%)"
                },
                legend: {
                    type: 'scroll',
                    pageIconColor: '#fff',
                    orient: 'vertical',
                    right: 0,
                    top: 'middle',
                    itemWidth: 15,
                    textStyle: {
                        color: '#D3D3D3',
                        fontSize: 12,
                        fontFamily: 'MicrosoftYaHeiLight',
                    },
                    pageTextStyle: {
                        color: '#fff'
                    },
                    data: this.pieData['legendData']
                },
                series: [
                    {
                        name: '国标',
                        type: 'pie',
                        radius: ['0', '60%'],
                        center: ['33%', '50%'],
                        label: {
                            show: false
                        },
                        data: this.pieData['data']
                    }
                ]
            };

            this.pieChart.setOption(options);
        }

        /**
         * 初始化tree-map
         */
        initTreeMap(treeMapData) {
            let {data} = treeMapData;
            let formatUtil = this.$echarts.format;
            this.treeMapChart = this.$echarts.init(this.$refs.treeMap);
            let options = {
                tooltip: {
                    formatter: function (info) {
                        var value = info.value;
                        var treePathInfo = info.treePathInfo;
                        var treePath = [];

                        for (var i = 1; i < treePathInfo.length; i++) {
                            treePath.push(treePathInfo[i].name);
                        }

                        return [
                            '<div class="tooltip-title">' + formatUtil.encodeHTML(treePath.join('/')) + '</div>',
                            '标准数量: ' + formatUtil.addCommas(value) + ' 条',
                        ].join('');
                    }
                },

                series: [
                    {
                        type: 'treemap',
                        top: '10%',
                        left: '10%',
                        bottom: '10%',
                        right: '10%',
                        leafDepth: 1,
                        roam: false,
                        breadcrumb: {
                            show: false,
                        },
                        visibleMin: 300,
                        nodeClick: false,
                        drillDownIcon: '',
                        label: {
                            show: true,
                            formatter: '{b}'
                        },
                        levels: [{
                            color: ['#0057B4', '#52C41A', '#FF9E55', '#1890FF',
                                '#FFF566', '#4783CF', '#FAAD14', '#13C2C2',
                                '#00E575', '#26A2FF', '#FF5750', '#FF9E55',
                                '#FFF566', '#FF5750', '#5271FF', '#A3BAFF', '#92182A', '#B37FEB']
                        }],
                        data
                    }
                ]
            };
            this.treeMapChart.setOption(options);
        }
    }
</script>

<style scoped lang="less">
    .home {
        background-color: #39436A !important;
        display: flex;
        flex-direction: column;
        color: rgba(255, 255, 255, 1);
        position: relative;

        .title {
            font-size: 16px;
            font-weight: bold;
            line-height: 30px;
            display: flex;
            align-items: center;

            .circle {
                display: inline-block;
                width: 6px;
                height: 6px;
                background: rgba(255, 158, 85, 1);
                border-radius: 50%;
                margin-right: 10px;
            }
        }

        .field {
            position: absolute;
            left: 0;
            right: 0;
            top: 0;
            height: 180px;
            padding: 10px;
            display: flex;
            flex-direction: column;

            .field-container {
                display: flex;
                flex: 1;

                ul {
                    flex: 1;
                    border-right: 1px dotted rgba(255, 255, 255, 0.3);

                    &:last-child {
                        border: none;
                    }
                }
            }
        }

        .standard {
            position: absolute;
            top: 180px;
            left: 0;
            right: 0;
            bottom: 0;
            padding: 10px;
            display: flex;
            flex-direction: column;

            .standard-container {
                position: absolute;
                left: 10px;
                top: 30px;
                right: 10px;
                bottom: 10px;

                .standard-content {
                    position: absolute;
                    top: 10px;
                    bottom: 10px;
                    padding: 0 5px;

                    & > div {
                        width: 100%;
                        height: 100%;
                        background: rgba(14, 24, 63, 0.1);
                        border: 1px solid rgba(20, 30, 68, 0.6);
                        box-shadow: 0 0 3px 0 rgba(0, 0, 0, 0.34);
                        padding: 10px;
                    }


                    .standard-title {
                        font-size: 16px;
                        font-weight: 500;
                        margin-bottom: 10px;

                        &:last-child {
                            margin-top: 10px;
                        }
                    }

                }

                .key-value {
                    left: 0;
                    width: 200px;

                    & > div {
                        overflow: auto;
                        height: 100%;
                        width: 100%;
                        display: flex;
                        flex-direction: column;

                        ul {
                            /*flex: 1;*/
                            display: flex;
                            flex-direction: column;
                            min-height: 92px;

                            li {
                                flex: 1;
                                max-height: 26px;


                                .iconfont {
                                    font-size: 16px;
                                }

                                .label {
                                    font-size: 14px;
                                    margin: 0 10px;
                                }

                                .value {
                                    font-size: 18px;
                                }
                            }
                        }
                    }

                }

                .pie {
                    left: 200px;
                    width: 40%;


                }

                .tree-map {
                    right: 0;
                    width: calc(100% - 40% - 200px);
                }

                .pie, .tree-map {
                    .charts {
                        position: absolute;
                        top: 40px;
                        left: 10px;
                        bottom: 10px;
                        right: 10px;
                    }
                }
            }

        }

        .key-value {
            li {
                display: flex;
                align-items: center;
                margin-left: 10px;
                margin-bottom: 20px;

                &:first-child {
                    margin-left: 10px;
                }

                &:last-child {
                    margin-bottom: 0;
                }
            }

            .iconfont {
                color: #1890ff;
                font-size: 24px;
            }

            .label {
                font-size: 16px;
                font-weight: 400;
                color: rgba(255, 255, 255, 0.65);
                margin: 0 20px;
                min-width: 32px;
            }

            .value {
                font-size: 22px;
                font-weight: bold;
                color: #ff6d00;
            }

            &.standard-content {
                li{
                    margin-bottom: 10px;
                }
            }
        }
    }
</style>

