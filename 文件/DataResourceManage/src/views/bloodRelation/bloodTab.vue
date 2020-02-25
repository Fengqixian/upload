<template>
    <div class="ship cb-blood-relation-wrapper" :sizeChange="sizeChange">
        <div class="ship-container">
            <div :id='"svg_"+arg.resourceId' @click="handleSvgClick" class="svg-container"></div>
            <transition name="fade">
                <div class="cb-relation-info" :id='"infoContainer_"+arg.resourceId' v-show="showInfo">
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
    import {Mutation, State} from 'vuex-class'
    import Svg from '../../common/js/svg'
    import * as d3 from 'd3'
    import config from '../../config.ts'

    const ICON_FONT = {
        34: 'shujuku',
        'b5e5b682-cb6a-4b8b-a4a6-92f380b3e9ab': 'biao',
        'c22d1da2-afbb-4604-9f0e-54b2a1bd829a': 'ziduan',
        '889553a9-fea1-46be-90db-77888f521c47': 'zhibiao',
    };

    @Component({})
    export default class BloodTab extends Vue {
        name: string = 'index';
        @Mutation setLoadingFlag;
        @State windowSize;
        @State menuWidth;
        @Prop({type: Object})
        currentModuleInstance;
        @Prop()
        id;
        // tree节点的数据
        @Prop()
        arg;
        // 标记此tab被激活，用于检测是否需要刷新svg
        @Prop()
        activateFlag;
        // 缓存页面宽度，用于检测是否需要刷新svg
        originalWidth;
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
            });
            this.originalWidth = this.windowSize + this.menuWidth
        }

        linear(num) {
            const {width} = d3.select('#svg').node().getBoundingClientRect()
            const linear = d3.scale.linear()
                .domain([0, 1800])
                .range([0, width])
            return linear(num)
        }

        initShip(data) {
            this.svg = new Svg({
                select: `#svg_${this.arg.resourceId}`,
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
            if (!this.flag && this.activateFlag && (this.windowSize + this.menuWidth) != this.originalWidth) {
                this.resizeSvg();
            }
            this.flag = false;
            return this.windowSize + this.menuWidth;
        }

        @Watch('activateFlag')
        activateFlag1(val) {
            if ((this.windowSize + this.menuWidth) != this.originalWidth && val === true) {
                this.originalWidth = this.windowSize + this.menuWidth;
                this.resizeSvg();
            }
        }

        resizeSvg() {
            setTimeout(_ => {
                this.svg.resize();
            }, 10)
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
                d3.select(`#svg_${this.arg.resourceId}_container`).attr('style', `margin-right:${301}px`)
            } else {
                d3.select(`#svg_${this.arg.resourceId}_container`).attr('style', `margin-right:${0}px`);
                this.disableCliked = ''
            }
        }


        // 获取数据
        getDataLineage() {
            let URL1 = config.port('dataLineage') + '/getDataLineage';
            this.$http.post(URL1, this.arg).then((response) => {
                const data = response.data.data;
                this.analysisData(data, this.arg.resourceId)

            }).catch(function (response) {
            });
        }

        // 解析符合创建的svg的数据
        analysisData(data, id) {
            const va = 'a' + id;
            let {nodeDataArray, linkDataArray} = data;
            let links = JSON.parse(JSON.stringify(linkDataArray));
            let temp = nodeDataArray.filter(item => item.type === 'temp');
            nodeDataArray = nodeDataArray.filter(item => item.type !== 'temp');

            // 存储转接点的id
            let tempIds: Array<string> = [];
            temp.forEach(item => tempIds.push(item.id));
            // 将links中to或者from不属于temp的links存放在一起
            let hasNoTempLinks: Array<object> = links.filter(item => tempIds.indexOf(item.from) === -1 && tempIds.indexOf(item.to) === -1);

            // 将temp对应links的to和from分别存储下来
            let tempFromTos: Array<object> = [];
            tempIds.forEach(id => {
                let froms = links.filter(item => item.from === id);
                let tos = links.filter(item => item.to === id);
                tempFromTos.push({froms, tos});
            });

            // 将含有temp的links合并在一起
            let mergeToNoTempLinks: Array<object> = [];
            tempFromTos.forEach(item => {
                let froms = JSON.parse(JSON.stringify(item['froms']));
                let tos = item['tos'];
                tos.forEach(toLink => {
                    froms.map(fromLink => {
                        fromLink['from'] = toLink['from'];
                        mergeToNoTempLinks.push(fromLink)
                    });
                })
            });
            links = [...hasNoTempLinks, ...mergeToNoTempLinks];
            links.map(item => {
                item['oldFrom'] = item['from'];
                item['oldTo'] = item['to'];
                item['from'] = (va + item['from']).replace(regExp, '');
                item['to'] = (va + item['to']).replace(regExp, '');
            });

            // 给每个id、moduleId、parentId 前面添加a，便于d3的select选择节点
            const regExp = /[`~!@#$^&*()=|{}':;',\[\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？]/ig;
            nodeDataArray.map(item => {
                item.oldId = item.id;
                item.oldModuleId = item.moduleId;
                item.oldParentId = item.parentId;
                item.id = (va + item.id).replace(regExp, '');
                item.moduleId = (item.parentId === '' ? '' : va + item.moduleId).replace(regExp, '');
                item.parentId = (item.parentId === '' ? '' : va + item.parentId).replace(regExp, '');
            });
            // 给每个links的from、to 前面添加a
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
            let zhibiao = nodeDataArray.filter(item => item.parentId === '');
            zhibiao.map(item => item.parentId = va + '指标');
            let shapes = nodeDataArray.filter(item => !item.isGroup);
            shapes.map(item => {
                item.group = item.parentId;
                item.type = 'column';
                item.icon = ICON_FONT[item.oldModuleId];
            });
            this.initShip({groups, shapes, links})
        }

        // 获取节点属性
        async getMetadataModelTreeForEdit(obj) {
            /*加载当前实例的模型的属性*/
            let url = config.port('metadataproperties') + '/page';
            let attrs: any = [];
            let values: any = {};
            await this.$http.get(url, {params: {modelResourceId: obj.oldModuleId}}).then((response) => {
                attrs = response.data.data.records;
            });
            /*加载当前实例的数据*/
            url = config.port('metadatavalue') + '/page';
            await this.$http.get(url, {params: {resourceId: obj.oldId}}).then((response) => {
                values = response.data.data.records[0];
            });
            this.analysisNodeData({attrs, values});
        }


        // 解析节点属性
        analysisNodeData(data) {
            this.showInfo = true;
            const {attrs, values} = data;
            let infoData: Array<any> = [
                {
                    label: 'ID',
                    value: values['id']
                }, {
                    label: '资源ID',
                    value: values['resourceId']
                }, {
                    label: '上级资源',
                    value: values['parentId']
                }, {
                    label: '模型ID',
                    value: values['modelId']
                }, {
                    label: '中文名称',
                    value: values['nameCn']
                }, {
                    label: '英文名称',
                    value: values['nameEn']
                }, {
                    label: '状态',
                    value: values['status']
                }];
            attrs.forEach(item => {
                const label = item['nameCn'];
                const value = values[item['mappingField']];
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

        .ship-container {
            width: 100%;
            height: 100%;
            overflow-y: auto;

            .svg-container {
                width: 100%;
                height: 100%;
            }

            .cb-relation-info {
                position: absolute;
                right: 0;
                top: 0;
                bottom: 0;
                width: 301px;
                transform: translateX(0);

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
        }
    }
</style>


