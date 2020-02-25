<template>
    <div class="item">
        <ul class="info">
            <el-tooltip
                    effect="dark"
                    :content="`名称：${treeData.name}`"
                    placement="top-start">
                <li class=" text-overflow">
                    名称：{{treeData.name}}
                </li>
            </el-tooltip>
            <el-tooltip
                    effect="dark"
                    :content="`状态：${treeData.statusCn}`"
                    placement="top-start">
                <li class=" text-overflow">状态：{{treeData.statusCn}}</li>
            </el-tooltip>
            <el-tooltip
                    effect="dark"
                    :content="`单位：${treeData.systemName}`"
                    placement="top-start">
                <li class=" text-overflow">单位：{{treeData.systemName}}</li>
            </el-tooltip>
            <li>描述：{{treeData.remarks}}</li>
        </ul>
        <div class="svg-wrapper" ref="item">
            <div class="btn">
                <!--<el-button class="del-bg"
                           @click="$router.push({path:'/edit-desensitization',query:{id: treeData.id}})">
                    修 改
                </el-button>-->
                <i class="el-icon-edit edit"
                   @click="$router.push({path:'/editDesensitization',query:{id: treeData.id}})"></i>
            </div>
            <div class="scale"><i class="el-icon-zoom-in" @click="fullscreen"></i></div>
            <div class="zoom" v-if="zoomVisible">
                <el-dialog :title="treeData.name" :visible.sync="zoomVisible" fullscreen>
                    <div class="fullscreenRef" ref="fullscreenRef"></div>
                </el-dialog>
            </div>
        </div>
        <div class="operation">
            <el-switch
                    v-model.trim="treeData.status"
                    active-value="1"
                    inactive-value="2"
                    @change="statusSwitchChange"
                    active-color="#13ce66"
                    inactive-color="#ff4949"
                    inactive-text="禁用"
                    active-text="启用">
            </el-switch>
        </div>
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component, Prop} from 'vue-property-decorator'
    import d3 from 'd3'

    @Component({})
    export default class Index extends Vue {
        name: string = 'Index';
        @Prop({required: true, type: Object})
        treeData;
        zoomVisible: Boolean = false;
        status: Boolean = true; // 状态
        mounted() {
            this.treeData.statusCn = this.treeData.status === '1' ? '启用' : '禁用';
            this.treeData.systemName = this.treeData.children[0].systemId === '360' ? '患者360' : '搜索引擎';
            this.initSvg();
        };

        /**
         * 初始化svg
         */
        initSvg(ref?) {
            const wrapper = ref ? this.$refs[ref] : this.$refs.item;
            // const {clientHeight: width, clientWidth: height} = wrapper;
            const width = wrapper['clientHeight'];
            const height = wrapper['clientWidth'];
            const _this = this;
            let svg = d3.select(wrapper)
                .append('svg')
                .attr('width', '100%')
                .attr('height', '100%');

            let treeWrapper = svg.append('g')
                .attr('class', '')
                .attr('transform', function () {
                    return `translate(25,${width / 2 - 10}) scale(0.8)`
                });

            svg.call(d3.behavior.zoom()
                .scaleExtent([0.1, 10])
                .translate([25, width / 2 - 10])
                .scale([0.8])
                .on('zoom', function () {
                    treeWrapper.attr('transform',
                        `translate(${d3.event.translate})
                            scale(${d3.event.scale})`)
                }))
                .on('dblclick.zoom', null);
            // 树布局
            let treeLayout = d3.layout.tree()
            // .size([width, height])
                .nodeSize([60, height / 3])
                .separation(function (a, b) {
                    return (a.parent === b.parent ? 1 : 1);
                });
            // 对角线生成器
            let diagonal = d3.svg.diagonal()
                .projection(function (d) {
                    return [d.y, d.x];
                });
            let nodesData = treeLayout.nodes(this.treeData);
            let linksData = treeLayout.links(nodesData);

            let line = treeWrapper.selectAll('g.line').data(linksData);
            let lineEntry = line.enter()
                .append('g')
                .attr('class', 'line');
            lineEntry.append('path')
                .attr('d', diagonal)
                .attr('fill', 'transparent')
                .attr('stroke', '#47e4bb')
                .attr('stroke-dasharray', '5 5')
                .append('animate')
                .attr('attributeType', 'XML')
                .attr('attributeName', 'stroke-dashoffset')
                .attr('from', '0')
                .attr('to', '-10')
                .attr('dur', '1s')
                .attr('repeatCount', 'indefinite');

            let node = treeWrapper.selectAll('g.node').data(nodesData);
            let nodeEntry = node.enter()
                .append('g')
                .attr('class', 'node')
                .on('click', function (d) {
                    d3.event.stopPropagation();
                    _this.handleNodeClick(d)
                })
                .attr('cursor', 'pointer')
                .attr('transform', function (d) {
                    return `translate(${d.y},${d.x})`;
                });
            nodeEntry.append('circle')
                .attr('r', 20)
                .attr('fill', function (d) {
                    switch (d.nodeType) {
                        case 'column': // 字段
                            return d.color = '#E6A23C' || '#E6A23C';
                        case 'desensitization': // 脱敏
                            return d.color = '#509cfc' || '#509cfc';
                        case 'sign': // 标签
                            return d.color = '#64b63d' || '#64b63d';
                        case 'user': // 用户
                            return d.color = '#386cd4' || '#386cd4';
                        case 'label': // 标签
                            return d.color = '#409EFF' || '#409EFF';
                        default:
                            return d.color = '#64b63d' || '#64b63d';
                    }
                });
            nodeEntry.append('text')
                .attr('font-family', 'iconfont')
                .attr('font-size', 20)
                .attr('dy', 2)
                .attr('fill', '#fff')
                .style('text-anchor', 'middle')
                .style('dominant-baseline', 'middle')
                .text(function (d) {
                    switch (d.nodeType) {
                        case 'column': // 字段
                            return '\ue600';
                        case 'desensitization': // 脱敏
                            return '\ue73e';
                        case 'sign': // 标签
                            return '\ue603';
                        case 'role': // 角色
                            return '\uea62';
                        case 'user': // 用户
                            return '\ue7e9';
                        case 'label': // 标签
                            return '\uebb7';
                        default:
                            return '\ue6c0';
                    }
                });
            nodeEntry.append('text')
                .attr('font-size', 14)
                .attr('dy', 32)
                .style('text-anchor', 'middle')
                .style('dominant-baseline', 'middle')
                .style('font-weight', 400)
                .text(function (d) {
                    return d.name || d.userName || d.roleName || d.ruleName || d.columnNameCn || d.columnNameEn || d.tableNameCn || d.tableNameEn;
                }).attr('fill', function (d) {
                switch (d.nodeType) {
                    case 'column': // 字段
                        return d.color = '#E6A23C' || '#E6A23C';
                    case 'desensitization': // 脱敏
                        return d.color = '#509cfc' || '#509cfc';
                    case 'sign': // 标签
                        return d.color = '#64b63d' || '#64b63d';
                    case 'user': // 用户
                        return d.color = '#386cd4' || '#386cd4';
                    case 'label': // 标签
                        return d.color = '#409EFF' || '#409EFF';
                    default:
                        return d.color = '#64b63d' || '#64b63d';
                }
            });


        }

        fullscreen() {
            this.zoomVisible = true;
            setTimeout(() => {
                this.initSvg('fullscreenRef');
            }, 1)
        };

        handleNodeClick(d) {
            this.$emit('node-click', d);
            // this.isShowSlideSideInfo = !this.isShowSlideSideInfo;
        };

        /**
         * 状态修改
         * @param status
         */
        statusSwitchChange(status) {
            let url = '/strategy/strategyInfo/updateStatus';
            let params = {
                id: this.treeData.id,
                status,
                type: 1
            };
            this.$http.put(url, params).then(response => {
                if (response.data.code === 0) {
                    this.$message.success("修改状态成功")
                } else {
                    this.$message.success("修改状态失败")
                }
            })
        }
    }
</script>

<style lang="less">
    .item {
        position: relative;
        width: 100%;
        height: 100%;

        .info {
            position: absolute;
            top: 0;
            bottom: 0;
            left: 0;
            width: 100px;
            background-color: #394160;
            padding: 30px 10px 0 10px;
            color: #ffffff;
            font-size: 12px;
            font-weight: 500;
            border-top-left-radius: 4px;
            border-bottom-left-radius: 4px;

            li {
                margin-bottom: 10px;

            }

        }

        .svg-wrapper {
            position: absolute;
            top: 0;
            bottom: 0;
            left: 100px;
            right: 0;

            .edit {
                position: absolute;
                top: 5px;
                right: 10px;
                color: #394160;
                cursor: pointer;
                font-size: 14px;

                &:hover {
                    opacity: 0.8;
                }
            }
        }

        .scale {
            position: absolute;
            top: 0;
            right: 35px;
            font-size: 16px;
            cursor: pointer;

            &:hover {
                color: #409EFF;
            }
        }

        .el-dialog {
            display: flex;
            flex-direction: column;

            .el-dialog__body {
                flex: 1;
            }
        }


        .fullscreenRef {
            width: 100%;
            height: 100%;
        }

        .operation {
            position: absolute;
            top: 0;
            left: 161px;
            transform: scale(0.7);

        }
    }
</style>
