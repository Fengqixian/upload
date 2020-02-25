<template>
    <div class="relation cb-relation-wrapper">
        <div :class="`md-relation-${id}`" :ref="`mdRelation${id}`" :style="mdRelationStyle"></div>
        <transition name="fade">
            <div class="cb-relation-info" v-show="showInfo">
                <header class="info-title">属 性</header>
                <ul>
                    <li class="info-item" v-for="(item,key) in infoData" :key="key">
                        <span class="label">{{item.label}}</span>
                        <span class="value">{{item.value}}</span>
                    </li>
                </ul>
            </div>
        </transition>
        <ul class="legend">
            <!--<li class="database">
                <span>
                    <i class="iconfont icon-tree-database"></i>
                </span>
                <div class="span">数据库</div>
            </li>-->
            <li class="table">
                <span>
                    <i class="iconfont icon-tree-table"></i>
                </span>
                <div class="span">表</div>
            </li>
            <li class="column">
                <span>
                    <i class="iconfont icon-tree-column"></i>
                </span>
                <div class="span">列</div>
            </li>
            <li class="element-item">
                <span>
                    <i class="iconfont icon-tree-element_item"></i>
                </span>
                <div class="span">数据元</div>
            </li>
            <li class="element-set">
                <span>
                    <i class="iconfont icon-tree-element_set"></i>
                </span>
                <div class="span">数据集</div>
            </li>
            <li class="etl">
                <span>
                    <i class="iconfont icon-tree-ETL"></i>
                </span>
                <div class="span">ETL</div>
            </li>
        </ul>
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component, Prop} from 'vue-property-decorator';
    import d3 from 'd3'
    import config from "../../config";

    @Component({})
    export default class Relation extends Vue {
        name: string = 'Relation';
        @Prop({required: true})
        id;
        @Prop({default: 0})
        containerWidth;
        @Prop({default: 0})
        containerHeight;
        @Prop({required: true})
        relationData;
        @Prop({required: true})
        headerId;
        @Prop()
        nodeClick;

        public mdRelationStyle: object = {
            width: '100%',
            height: '100%',
        };

        // 关系数据
        public linkDataArray: Array<object> = [];
        // 每组node节点数据
        public nodeDataArray: Array<object> = [];
        // 存放力导向图对象
        public force: object = null;
        // 设定作用范围
        public size: Array<any> = [];
        // svg
        svg = null;

        // 存放区域宽高
        public width: number = 0;
        public height: number = 0;

        public mounted(): void {
            setTimeout(() => {
                this.analysisData();
                this.renderRelation();
            }, 200)
        }

        /**
         * 右侧详情区域
         */
        public showInfo: boolean = false;
        public isGetInfoData: boolean = true;  // 控制是否允许获取详情  防止鼠标移动时候还获取数据
        public infoData: Array<object> = [];
        public baseInfoData: Array<object> = [
            {
                label: 'ID', // 展示的名称
                value: '', // 展示的值
                key: 'id' // 对应的字段
            },
            {
                label: '资源ID', // 展示的名称
                value: '', // 展示的值
                key: 'resourceId' // 对应的字段
            },
            {
                label: '上级资源', // 展示的名称
                value: '', // 展示的值
                key: 'parentId' // 对应的字段
            },
            {
                label: '模型ID', // 展示的名称
                value: '', // 展示的值
                key: 'modelId' // 对应的字段
            },
            {
                label: '中文名称', // 展示的名称
                value: '', // 展示的值
                key: 'nameCn' // 对应的字段
            },
            {
                label: '英文名称', // 展示的名称
                value: '', // 展示的值
                key: 'nameEn' // 对应的字段
            },
            {
                label: '状态', // 展示的名称
                value: '', // 展示的值
                key: 'status' // 对应的字段
            }
        ];

        /**
         * 获取详情数据
         */
        public async getinfoData(data): Promise<void> {
            this.nodeClick && this.nodeClick(data);

        }

        /**
         * 获取详情的属性
         * @param data 当前被点击的tree node节点数据
         * return 获取详情属性的数据集
         */
        public async getDetailAttr(data): Promise<Array<object>> {
            let url = config.port('metadataproperties') + '/page';
            let params = {
                modelResourceId: data['moduleId']  // 根据元数据查询元数据的属性 传modelId
            };
            let retArr: Array<object> = [];
            await this.$http.get(url, {params}).then(response => {
                retArr = response.data.data.records;
            });
            return retArr;
        }

        /**
         * 获取详情的值
         * @param data 当前被点击的tree node节点数据
         * return 获取详情值的数据集
         */
        public async getDetailValue(data): Promise<object> {
            let url = config.port('metadatavalue') + '/page';
            let params = {
                resourceId: data['id'],
                size: -1
            };
            let retArr: Array<object> = [];
            await this.$http.get(url, {params}).then(response => {
                retArr = response.data.data.records[0];
            });
            return retArr;
        }

        /**
         * 渲染关系图
         */
        public renderRelation(): void {
            const _this = this;
            if (!this.id) return;
            const color: Array<string> = ['#0b96dc', '#ff9f43', '#27ae60', '#c66ecd', '#0abde3', '#1dd1a1', '#0b96dc', '#7822d3', '#f368e0', '#9b59b6'];
            const linePath = d3.svg.line();
            const {width, height} = this.$refs[`mdRelation${this.id}`]['getBoundingClientRect']();
            this.width = this.containerWidth || width;
            this.height = this.containerHeight || height;
            const svg = d3.select(`.md-relation-${this.id}`)
                .append('svg')
                .attr('width', this.width)
                .attr('height', this.height)
                .call(d3.behavior.zoom()
                    .scaleExtent([0.2, 10])
                    .on('zoom', function () {
                        d3.select('#svgWrapper' + _this.id)
                            .attr('transform', `
                            translate(${d3.event.translate}) scale(${d3.event.scale})
                        `)
                    }))
                .on('click', () => {
                    this.showInfo = false;
                }).on('mousemove', (e) => {
                    if ((Math.abs(d3.event.movementX) > 5 || Math.abs(d3.event.movementY) > 5) && this.isGetInfoData) {
                        this.isGetInfoData = false;
                    }
                })
                /*.attr("preserveAspectRatio", "xMidYMid meet")
                .attr("viewBox", `-${this.width * (1 / 10)} -${this.height * (1 / 5)} ${this.width} ${this.height}`)*/
                .append('g')
                .attr('id', 'svgWrapper' + this.id);
            const force = d3.layout.force()
                .nodes(this.nodeDataArray)      // 设置节点数组
                .links(this.linkDataArray)      // 设置连线数组
                .size(this.size)                // 设置作用范围
                .linkDistance(100)              // 设置连线的距离
                .gravity(0)                     //没有重力
                .charge(-200);                  // 设置节点的电荷数
            force.start();
            this.nodeDataArray.forEach(node => {
                if (node['id'] === _this.headerId) {
                    node['fixed'] = true;
                    node['px'] = _this.width / 2;
                    node['py'] = _this.height / 2;
                }
            });

            // 绘制连线
            let lines = null;
            {
                lines = svg.append('g')
                    .attr('id', 'lines' + this.id)
                    .selectAll('.forceLine')
                    .data(this.linkDataArray)
                    .enter()
                    .append('path')
                    .attr('class', 'forceLine')
                    .attr('stroke-dasharray', '5 5')
                    .attr("stroke", d => {
                        switch (d.type) {
                            case 'ETL': // ETL
                                return color[5];
                            default:
                                return color[9];
                        }
                    })
                    .attr('stroke-width', d => {
                        let strokeWidth = 3;
                        if (d.type === 'ETL') {
                            strokeWidth = 5;
                        }
                        return strokeWidth;
                    });
                lines.append('animate')
                    .attr('attributeType', 'XML')
                    .attr('attributeName', 'stroke-dashoffset')
                    .attr('from', '0')
                    .attr('to', '-10')
                    .attr('dur', '1s')
                    .attr('repeatCount', 'indefinite');
            }


            // 添加运动的小球
            let balls = null;
            {
                balls = svg.append('g')
                    .attr('id', 'ball' + this.id)
                    .selectAll('.forceBall')
                    .data(this.linkDataArray)
                    .enter()
                    .append('circle')
                    .attr('r', 3)
                    .attr('class', 'forceBall')
                    .attr('fill', '#0efff8')
                    .attr('id', d => {
                        return (d.from + d.to).replace(new RegExp(/-/g), '')
                    });
            }
            let animateMotions = null;
            {
                animateMotions = balls
                    .append('animateMotion')
                    .attr('class', 'forceAnimateMotion')
                    .attr('begin', '0s')
                    .attr('dur', '4s')
                    .attr('repeatCount', 'indefinite')
                    .attr('path', (d, i) => {
                        let lines = [];
                        lines.push([d.source.x, d.source.y], [d.target.x, d.target.y]);
                        return linePath(lines);
                    });
            }

            let gNode = svg.selectAll('g.forceNode')
                .data(this.nodeDataArray);
            let gNodeEnter = gNode.enter()
                .append('g')
                .attr('class', 'forceNode')
                .call(force.drag()
                    .on('dragstart', function () {
                        d3.event.sourceEvent.stopPropagation();
                    })
                )
                .on('mousedown', (d) => {
                    this.isGetInfoData = true;
                })
                .on('mouseup', (d) => {
                    if (!this.isGetInfoData) return;
                    // this.getinfoData(d);
                    this.heightLightLine(d, gNodeEnter, lines, color);
                });
            {

                gNodeEnter.append('circle')
                    .attr('class', 'forceCircle')
                    .attr('r', 20)
                    .style('fill', d => {
                        switch (d.type) {
                            case 'table': // 表
                                return color[0];
                            case 'column': // 字段
                                return color[1];
                            case 'database': // 数据库
                                return color[2];
                            case 'element_item': // 基础元数据
                                return color[3];
                            case 'element_set': // 衍生元数据
                                return color[4];
                            case 'ETL': // ETL
                                return color[5];
                        }
                    })
                    .style('cursor', 'pointer')
                    .on('mousedown', (d) => {
                        this.isGetInfoData = true;
                    })
                    .on('mouseup', (d) => {
                        if (!this.isGetInfoData) return;
                        this.getinfoData(d);
                        this.heightLightLine(d, gNodeEnter, lines, color);
                    });
                gNodeEnter.append('text')
                    .attr('class', 'forceIcon')
                    .attr('font-family', 'iconfont')
                    .attr('font-size', '28px')
                    .text(function (d) {
                        switch (d.type) {
                            case 'table': // 表
                                return '\ue618';
                            case 'column': // 字段
                                return '\ue7d4';
                            case 'database': // 数据库
                                return '\ue617';
                            case 'element_item': // 基础元数据
                                return '\ue601';
                            case 'element_set': // 衍生元数据
                                return '\ue78f';
                            case 'ETL': // ETL
                                return '\ue61a';
                        }
                    })
                    .style('fill', d => {
                        return '#fff'
                    })
                    .style('cursor', 'pointer')
                    // .attr('transform', d => {
                    //     return `translate(-13,10)`;
                    // })

                    .style('font-weight', 'lighter')
                    .style('text-anchor', 'middle')
                    .attr('dy', 10)
                    .on('mousedown', (d) => {
                        this.isGetInfoData = true;
                    })
                    .on('mouseup', (d) => {
                        if (!this.isGetInfoData) return;
                        this.getinfoData(d);
                        this.heightLightLine(d, gNodeEnter, lines, color);
                    });


            }

            let texts = null;
            {
                texts = svg.append('g')
                    .attr('id', 'texts' + this.id)
                    .selectAll('.forceText')
                    .data(this.nodeDataArray)
                    .enter()
                    .append('text')
                    .attr('class', 'forceText')
                    .attr('dy', 34)
                    .text(d => {
                        return d.name;
                    })
                    .style('fill', d => {
                        switch (d.type) {
                            case 'table': // 表
                                return color[0];
                            case 'column': // 字段
                                return color[1];
                            case 'database': // 数据库
                                return color[2];
                            case 'element_item': // 基础元数据
                                return color[3];
                            case 'element_set': // 衍生元数据
                                return color[4];
                            case 'ETL': // ETL
                                return color[5];
                        }
                    })
                    .style('font-size', 12)
                    .style('font-weight', 'lighter')
                    .style('text-anchor', 'middle')
            }


            force.on('tick', function () {
                lines.attr('d', d => {
                    let lines = [];
                    lines.push([d.source.x, d.source.y], [d.target.x, d.target.y]);
                    return linePath(lines);
                });
                gNodeEnter.attr('transform', d => {
                    let transform = `translate(${d.x},${d.y}) scale(0.74)`;
                    if (d.current || d.id === _this.headerId) {
                        transform = `translate(${d.x},${d.y}) scale(1.2)`;
                    }
                    return transform;
                });
                texts
                    .attr('x', d => {
                        return d.x;
                    })
                    .attr('y', d => {
                        return d.y;
                    });

                animateMotions.attr('path', (d, i) => {
                    let lines = [];
                    lines.push([d.source.x, d.source.y], [d.target.x, d.target.y]);
                    return linePath(lines);
                });
            });
            force.start();
        }

        /**
         * 连接线高亮
         */
        public heightLightLine(currentNode, nodes, lines, color) {
            lines.attr("stroke", d => {
                let stroke = null;
                d.current = false;
                switch (d.type) {
                    case 'ETL': // ETL
                        stroke = color[5];
                        break;
                    default:
                        stroke = color[9];
                }
                if (d.source === currentNode || d.target === currentNode) {
                    stroke = 'red';
                    d.current = true;
                }
                return stroke;
            })
                .attr("stroke-width", d => {
                    let strokeWidth = 3;
                    if (d.source === currentNode || d.target === currentNode || d.type === 'ETL') {
                        strokeWidth = 5;
                    }
                    return strokeWidth;
                });
            nodes.attr('transform', d => {
                d.current = false;
                let transform = `translate(${d.x},${d.y}) scale(0.74)`;
                if (d === currentNode || d.id === this.headerId) {
                    d.current = true;
                    return transform = `translate(${d.x},${d.y}) scale(1.2)`;
                }
                return transform;
            });
        }

        /**
         * 分析数据
         */
        public analysisData(): void {
            if (!this.id) return;
            const {width, height} = this.$refs[`mdRelation${this.id}`]['getBoundingClientRect']();
            this.width = this.containerWidth || width;
            this.height = this.containerHeight || height;
            // this.size = [this.width * (4 / 5), this.height * (3 / 5)];
            this.size = [this.width, this.height];
            // this.linkDataArray = this.relationData['linkDataArray'];
            this.linkDataArray = this.relationData['linkDataArray'];
            this.nodeDataArray = this.relationData['nodeDataArray'];
            /*this.linkDataArray.forEach(item => {
                let from = this.nodeDataArray.find(node=>node['id'] === item['from']);
                let to = this.nodeDataArray.find(node=>node['id'] === item['to']);
                // console.log(from);
                console.log(to);
                // if(from === undefined){
                //     console.log('from',item);
                // }
                // if(to === undefined){
                //     console.log('to',item);
                // }
            })*/
        }
    }
</script>

<style scoped lang="less">
    .relation {
        width: 100%;
        height: 100%;
        position: relative;

        .md-relation {
            width: 100%;
            height: 100%;
            background-color: #293152;

            svg {
                width: 100%;
                height: 100%;
            }
        }

        .cb-relation-info {
            font-size: 12px;
            position: absolute;
            right: 0;
            top: 0;
            bottom: 0;
            width: 301px;
            /*background-color: rgba(14, 24, 63, 0.9);*/
            transform: translateX(0);
            overflow: auto;

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
                margin-top: 1rem;
            }

            ul {
                width: 100%;
                font-size: 12px;
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
                    }

                    .label, .value {
                        display: flex;
                        align-items: center;
                        word-break: break-all;
                    }
                }
            }
        }

        .legend {
            position: absolute;
            top: 0;
            left: 0;
            background-color: transparent;
            display: flex;
            flex-direction: column;
            /*width: 380px;*/
            justify-content: space-between;
            padding: 5px;
            color: #ffffff;

            & > li {
                text-align: center;
                display: flex;
                align-items: center;

                span {
                    width: 40px;
                    height: 40px;
                    line-height: 40px;
                    text-align: center;
                    display: inline-block;
                    border-radius: 50%;
                    transform: scale(0.618);

                    .iconfont {
                        font-size: 28px;
                        color: #fff;
                    }
                }

                &.table {
                    & > span {
                        background: #0b96dc;
                    }

                    color: #0b96dc;
                }

                &.column {
                    & > span {
                        background: #ff9f43;
                    }

                    color: #ff9f43;
                }

                &.database {
                    & > span {
                        background: #27ae60;
                    }

                    color: #27ae60;
                }

                &.element-item {
                    & > span {
                        background: #c66ecd;
                    }

                    color: #c66ecd;
                }

                &.element-set {
                    & > span {
                        background: #0abde3;
                    }

                    color: #0abde3;
                }

                &.etl {
                    & > span {
                        background: #1dd1a1;
                    }

                    color: #1dd1a1;
                }
            }
        }

    }
</style>
