import Item from "./item";

class BusinessViewRelation {
    constructor({ref, linkData, data, linkClick}) {
        this.d3 = require('d3');
        this.ref = ref;
        /*this._data = {
            0: {
                children: [
                    {
                        name: 'table1',
                        id: 'table1',
                        children: [
                            {
                                name: 'column1',
                                id: 'column1',
                                isHighlight: true
                            },
                            {
                                name: 'column13',
                                id: 'column13',
                            },
                        ]
                    },
                    {
                        name: 'table2',
                        id: 'table2',
                        children: [
                            {
                                name: 'column2',
                                id: 'column2',
                                isHighlight: null
                            }
                        ]
                    },
                ],
            },
            1: {
                children: [
                    {
                        name: 'table3',
                        id: 'table3',
                        children: [
                            {
                                name: 'column3',
                                id: 'column3',
                            }
                        ]
                    },
                    {
                        name: 'table4',
                        id: 'table4',
                        children: [
                            {
                                name: 'column4',
                                id: 'column4',
                            }
                        ]
                    },
                ],
            }
        };*/
        this._linkData = linkData;
        this._data = data;
        this._linkClick = linkClick || null;
        /*this._linkData = [
            {
                from: 'table4',
                to: 'table1'
            },
            {
                from: 'table2',
                to: 'table3'
            },
        ];*/
        this._id = 'id'; // link 查询到from的唯一标识
        this._width = 0;
        this._height = 0;
        this._svg = null;
        this._container = null;
        this._containerTran = null;
        this._rowSpace = 200;
        this._itemHeight = 30;
        this._itemWidth = 300;
        this._margin = 10;
        this._uuid = null;
        this.itemClass = [];
        this._init();
        this._matchLinkToNode();
        this.renderItem();
        this.render();
        this._calcAllNodesLoc();
        this._renderLink();
    }

    _init() {
        {
            this._uuid = Math.random() * 1000000;
            const {width, height} = this.ref.getBoundingClientRect();
            this._width = width;
            this._height = height;
            this.d3.select(this.ref).select('svg').remove();
            this._svg = this.d3.select(this.ref).append('svg')
                .attr('width', width)
                .attr('height', height - 10);
            this._container = this._svg.append('g').attr('class', 'container' + this._uuid);
        }
        {
            this._svg.append('svg:defs').append('svg:marker')
                .attr('id', 'arrow' + this._uuid)
                .attr('viewBox', '0 -5 10 10')
                .attr('refX', 9)
                .attr('markerWidth', 3)
                .attr('markerHeight', 3)
                .attr('orient', 'auto')
                .append('svg:path')
                .attr('d', 'M0,-5L10,0L0,5')
                .attr('fill', '#559EF2');
            this._svg.append('defs').append('marker')
                .attr('id', 'endCircle' + this._uuid)
                .attr('markerWidth', 3)
                .attr('markerHeight', 3)
                .attr('refX', 1.5)
                .attr('refY', 1.5)
                .attr('orient', 'auto')
                .append('circle')
                .attr('r', 1.5)
                .attr('cx', 1.5)
                .attr('cy', 1.5)
                .attr('fill', '#559EF2');

            this._svg.append('svg:defs').append('svg:marker')
                .attr('id', 'activeArrow' + this._uuid)
                .attr('viewBox', '0 -5 10 10')
                .attr('refX', 9)
                .attr('markerWidth', 3)
                .attr('markerHeight', 3)
                .attr('orient', 'auto')
                .append('svg:path')
                .attr('d', 'M0,-5L10,0L0,5')
                .attr('fill', 'red');
            this._svg.append('defs').append('marker')
                .attr('id', 'activeEndCircle' + this._uuid)
                .attr('markerWidth', 3)
                .attr('markerHeight', 3)
                .attr('refX', 1.5)
                .attr('refY', 1.5)
                .attr('orient', 'auto')
                .append('circle')
                .attr('r', 1.5)
                .attr('cx', 1.5)
                .attr('cy', 1.5)
                .attr('fill', 'red');
        }

    }

    _matchLinkToNode() {
        let keys = Object.keys(this._data);
        let nodes = [];
        let index = 1;
        let firstGroup = this._data[keys[0]];
        firstGroup.children && firstGroup.children.forEach(node => {
            node.index = index;
            index++;
            node.children && node.children.forEach(child => {
                child.index = index;
                index++;
            })
        });
        keys.forEach(key => {
            let group = this._data[key];
            group.children && group.children.forEach(node => {
                nodes.push(node);
                node.children && node.children.forEach(child => {
                    nodes.push(child);
                })
            })
        });
        nodes.forEach(node => {
            let links = this._linkData.filter(link => link.to === node[this._id]);
            if (links && links.length) {
                links.forEach(link => {
                    let fromNode = nodes.find(nod => link.from === nod[this._id]);
                    if (fromNode) {
                        fromNode.index = node.index;
                        link.from = fromNode;
                        link.to = node;
                    }
                });
            }
        });
        keys.forEach(key => {
            let group = this._data[key];
            group.children && group.children.sort((pre, next) => {
                !pre.index ? pre.index = -1 : null;
                !next.index ? next.index = -1 : null;
                return pre.index - next.index;
            });
            group.children && group.children.forEach(node => {
                node.children && node.children.sort((pre, next) => {
                    !pre.index ? pre.index = -1 : null;
                    !next.index ? next.index = -1 : null;
                    return pre.index - next.index;
                });
            })
        });
    }


    /**
     * 渲染每大组的元素
     */
    renderItem() {
        this.itemClass = [];
        Object.keys(this._data).forEach(key => {
            const itemContainer = this._container.append('g').attr('class', 'item-container' + this._uuid);
            let group = this._data[key];
            group.container = itemContainer;
            this.itemClass.push(new Item({container: itemContainer, data: group}));
        });
    }

    /**
     * 渲染大组位置
     * @private
     */
    _renderGroupLoc() {
        // 计算每大组的水平和垂直位移
        let groupKeys = Object.keys(this._data);
        let allWidth = groupKeys.length * this._itemWidth + (groupKeys.length - 1) * this._rowSpace + this._margin * 2;
        let dx = 0;
        if (this._width <= allWidth) {
            dx = this._margin;
        } else {
            dx = (this._width - allWidth) / 2
        }
        // 存储每大组的高度
        let groupsHeights = [];
        groupKeys.forEach(key => {
            let group = this._data[key];
            groupsHeights.push(group.height);
        });
        let maxHeight = Math.max(...groupsHeights);
        groupKeys.forEach(key => {
            let group = this._data[key];
            groupsHeights.push(group.height);
            group.dx = dx;
            dx += this._itemWidth + this._rowSpace;
            group.dy = this._margin + (maxHeight - group.height) / 2;
            group.container
                .attr('transform', `translate(${group.dx} ${group.dy})`)
        });

        let {height: containerHeight} = this._container.node().getBoundingClientRect();
        this._svg.attr('height', Math.max(containerHeight + 30, this._height) - 10);
    }

    /**
     * 渲染container的位置，使整个居中
     */
    renderContainerLoc() {
        let {height: containerHeight, width: containerWidth} = this._container.node().getBoundingClientRect();
        const {width, height} = this.ref.getBoundingClientRect();
        this._width = width;
        this._height = height;
        this._svg.attr('height', Math.max(containerHeight + 30, height) - 10);
        this._svg.attr('width', Math.max(containerWidth, width) - 10);
        if (this._height >= containerHeight) {
            if (this._containerTran) {
                this._containerTran = this._container.transition().duration(1000);
                this._containerTran
                    .attr('transform', `translate(0 ${(this._height - containerHeight) / 2})`)
            } else {
                this._container
                    .attr('transform', `translate(0 ${(this._height - containerHeight) / 2})`)
            }
        } else {
            this._containerTran = this._container.transition().duration(1000);
            this._containerTran
                .attr('transform', `translate(0 0)`)
        }
        this._containerTran || (this._containerTran = this._container.transition().duration(1000));
    }


    render() {
        this.itemClass.forEach(item => {
            item.render();
        });
        this._renderGroupLoc();
        this.renderContainerLoc();
    }


    /**
     * 计算每个node的坐标
     * @private
     */
    _calcAllNodesLoc() {
        let keys = Object.keys(this._data);
        keys.forEach(key => {
            let group = this._data[key];
            group.x = group.dx;
            group.y = group.dy;
            group.leftPort = [group.x, group.y + group.height / 2];
            group.rightPort = [group.x + group.width, group.y + group.height / 2];
            group.children && group.children.forEach(viceGroup => {
                viceGroup.x = group.x + viceGroup.viceGroupDx;
                viceGroup.y = group.y + viceGroup.viceGroupDy;
                viceGroup.leftPort = [viceGroup.x, viceGroup.y + viceGroup.height / 2];
                viceGroup.rightPort = [viceGroup.x + viceGroup.width, viceGroup.y + viceGroup.height / 2];
                viceGroup.children && viceGroup.children.forEach(node => {
                    node.x = viceGroup.x + node.dx;
                    node.y = viceGroup.y + node.dy;
                    node.leftPort = [node.x, node.y + node.height / 2];
                    node.rightPort = [node.x + node.width, node.y + node.height / 2];
                })
            })
        });
    }

    /**
     * 渲染link
     */
    _renderLink() {
        const _this = this;
        const linePath = d3.svg.line();
        const linkContainer = this._container.append('g').attr('class', 'link-container' + this._uuid);
        this._linkData.forEach(link => {
            let fromPort = null;
            let toPort = null;
            if (link.from.x > link.to.x) {
                fromPort = link.from.leftPort;
                toPort = link.to.rightPort;
            } else if (link.from.x < link.to.x) {
                fromPort = link.from.rightPort;
                toPort = link.to.leftPort;
            }
            if (fromPort instanceof Array && toPort instanceof Array) {
                let linkLine = linkContainer.append('path')
                    .attr('stroke', '#559EF2')
                    .attr('stroke-width', 3)
                    .attr('fill', 'transparent')
                    .attr('stroke-dasharray', '8 2')
                    .attr('marker-end', `url(#arrow${this._uuid})`)
                    .attr('marker-start', `url(#endCircle${this._uuid})`)
                    .attr('d', linePath([fromPort, toPort]));
                if (this._linkClick) {
                    linkLine.attr('cursor', 'pointer')
                        .on('click', () => {
                            this._linkClick && this._linkClick(link)
                        })
                        .on('mouseenter', function () {
                            linkLine.attr('stroke', 'red')
                                .attr('marker-start', `url(#activeEndCircle${_this._uuid})`)
                                .attr('marker-end', `url(#activeArrow${_this._uuid})`);
                            linkLine.selectAll('animate').remove();
                        })
                        .on('mouseleave', function () {
                            linkLine.attr('stroke', '#559EF2')
                                .attr('marker-end', `url(#arrow${_this._uuid})`)
                                .attr('marker-start', `url(#endCircle${_this._uuid})`);
                            linkLine.select('animate').remove();
                            linkLine.append('animate')
                                .attr('attributeType', 'XML')
                                .attr('attributeName', 'stroke-dashoffset')
                                .attr('from', '0')
                                .attr('to', '-10')
                                .attr('dur', '2s')
                                .attr('repeatCount', 'indefinite')
                        })
                }
                linkLine.append('animate')
                    .attr('attributeType', 'XML')
                    .attr('attributeName', 'stroke-dashoffset')
                    .attr('from', '0')
                    .attr('to', '-10')
                    .attr('dur', '2s')
                    .attr('repeatCount', 'indefinite')
            }
        })
    }
}

export default BusinessViewRelation;
