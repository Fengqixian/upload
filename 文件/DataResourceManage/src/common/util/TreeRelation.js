class TreeRelation {
    constructor({selectName, treeData, nodeSize, callbackNodeClick, linkRules, enterDirector, del}) {
        /**
         * d3插件，使用v3
         * @type {d3}
         */
        this.d3 = require('d3');
        this.selectName = selectName;                           // svg的父盒子class 或者id名
        this._treeData = treeData || {};                  // 树形数据
        this.nodeSize = nodeSize || [40, 40];
        this._callbackNodeClick = callbackNodeClick || null;
        this._freeNodes = [];
        this._del = del || null;
        // 连接规则
        this._linkRules = linkRules; /*|| [{
            fromNodeType: 'user',
            toNodeType: 'desensitization'
        }];*/
        // 节点进入位置
        this._enterDirector = enterDirector /*|| {
            user: 'top',
            desensitization: 'top',
            column: 'left',
        }*/;
        this._linePath = null;
        this._container = null;
        this._svg = null;
        this._layoutTree = null;
        this._diagonal = null;
        this._treeLinkContainer = null;
        this._treeNodeContainer = null;
        this._freeNodeContainer = null;
        this._width = null;
        this._height = null;
        this._warn = null;
        this._activeColor = '#e0712f';
        this.init();
        this.updateTree();
    }

    init() {
        this.svgWrapper = this.d3.select(this.selectName);
        const {height, width} = this.svgWrapper.node().getBoundingClientRect();
        this._width = width;
        this._height = height;
        {
            this._svg = this.svgWrapper.append('svg')
                .attr('width', '100%')
                .attr('height', '100%')
                // 添加缩放功能
                .call(this.d3.behavior.zoom()
                    .scaleExtent([0.1, 20])
                    .on('zoom', () => {
                        this._container.attr('transform',
                            `translate(${this.d3.event.translate}) scale(${this.d3.event.scale})`)
                    }))
                // 禁止双击放大
                .on("dblclick.zoom", null);
            this._warn = this._svg.append('rect')
                .attr('width', '100%')
                .attr('height', '100%')
                .attr('fill', 'transparent')
            // 操作空间
            this._container = this._svg.append('g')
                .attr('class', 'container')/*
                // 让 tree node 居中
                .attr('transform', `translate(10, ${height / 2})`);*/

            // tree操作空间
            this._treeLinkContainer = this._container.append('g')
                .attr('class', 'treeLinkContainer');
            this._treeNodeContainer = this._container.append('g')
                .attr('class', 'treeNodeContainer');
            this._freeNodeContainer = this._container.append('g')
                .attr('class', 'freeNodeContainer');
        }

        // 初始化生成器
        {
            // 线段生成器
            this._linePath = this.d3.svg.line();
            // 树布局
            this._layoutTree = this.d3.layout.tree()
            // 设置树布局 垂直距离和水平距离
                .nodeSize([this.nodeSize[1] * 2, 100]);

            // 对角线生成器
            this._diagonal = this.d3.svg.diagonal()
                .projection(d => {
                    // 相对数布局生成的节点坐标基础上 让节点下移 和左移动
                    return [d.x, d.y];
                });
        }
        // 监听
        {
            this.d3.select('body').on('keyup', () => {
                // 删除按钮
                if (this.d3.event.keyCode === 46) {
                    {
                        this._freeNodeContainer.selectAll('.free-node').each((node, index) => {
                            if (node.active) {
                                this._del && this._del(node);
                                this._freeNodes.splice(index, 1)
                            }
                        });
                    }
                    {
                        this._treeNodeContainer.selectAll('.tree-node').each(node => {
                            if (node.active && this._treeData === node) {
                                if (this._treeData.children) {
                                    this._freeNodes.push(...this._treeData.children);
                                }
                                this._del && this._del(node);
                                this._treeData = {};
                            } else {
                                let index = null;
                                this._treeData.children && this._treeData.children.forEach((item, ind) => {
                                    if (item.active) {
                                        this._del && this._del(item);
                                        index = ind;
                                    }
                                });
                                if (index !== null) this._treeData.children.splice(index, 1);
                            }
                        });
                    }
                    this.upDateFree();
                    this.updateTree();
                }
            })
        }
    }

    /**
     * 节点颜色
     */
    nodeColor(d) {
        if (d.active) {
            return this._activeColor
        }
        switch (d.nodeType) {
            case 'desensitization': // 脱敏
                return d.color = '#509cfc' || '#509cfc';
            case 'sign': // 标签
                return d.color = '#64b63d' || '#64b63d';
            case 'user': // 用户
                return d.color = '#386cd4' || '#386cd4';
            case 'label': // 标签
                return d.color = '#409EFF' || '#409EFF';
        }
    }

    nodeIcon(d) {
        switch (d.nodeType) {
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
        }
    }

    /**
     * 更新界面
     */
    updateTree() {
        // 清除页面所有的节点数据
        this._treeNodeContainer.selectAll('.tree-node').remove();
        this._treeLinkContainer.selectAll('.tree-link').remove();
        if (Object.keys(this._treeData).length <= 3) {
            return;
        }
        // 根据树布局生成树对应的node和link数据
        const treeNodeData = this._layoutTree.nodes(this._treeData);
        let maxDepth = 0;
        treeNodeData.forEach(node => {
            node.orginFree = false;
            maxDepth = Math.max(maxDepth, node.depth);
        });
        maxDepth++;
        const treeHeight = 12.7 + (maxDepth - 1) * (this.nodeSize[1] + 100);
        let dy = this.nodeSize[1] * 0.75;
        if (treeHeight < this._height) {
            dy += (this._height - treeHeight) / 2;
        }
        treeNodeData.forEach(node => {
            node.x += this._width / 2;
            node.y += dy;
        });
        const treeLinkData = this._layoutTree.links(treeNodeData);

        // link
        {
            const updateLink = this._treeLinkContainer.selectAll('.tree-link').data(treeLinkData);
            const enterLink = updateLink.enter()
                .append('path', 'g')
                .attr('class', 'tree-link')
                .attr('cursor', 'pointer')
                .attr('stroke-dasharray', '7 3')
                .attr('fill', 'transparent')
                .attr('stroke-width', '2px')
                .attr('stroke', '#59ba29');
            enterLink.append('animate')
                .attr('attributeType', 'XML')
                .attr('attributeName', 'stroke-dashoffset')
                .attr('from', '0')
                .attr('to', '-10')
                .attr('dur', '1s')
                .attr('repeatCount', 'indefinite');
            enterLink.attr('d', d => {
                let source = {};
                let target = {};
                {
                    if (d.source.x0 !== undefined && d.source.y0 !== undefined) {
                        source = {
                            x: d.source.x0 || this._height / 2,
                            y: d.source.y0 || 0,
                        };
                    } else {
                        source = {
                            x: this._width / 2,
                            y: 0
                        };
                    }
                }
                {
                    if (d.target.x0 !== undefined && d.target.y0 !== undefined) {
                        target = {
                            x: d.target.x0 || this._height / 2,
                            y: d.target.y0 || 0,
                        };
                    } else {
                        target = {
                            x: this._width / 2,
                            y: 0
                        };
                    }
                }
                return this._diagonal({
                    source,
                    target
                })
            });

            // 添加link
            updateLink
                .transition()
                .duration(750)
                .attr('d', this._diagonal);
            updateLink.exit().remove();
        }

        // node
        {
            const updateTreeNode = this._treeNodeContainer.selectAll('.tree-node').data(treeNodeData);
            const enterTreeNode = updateTreeNode.enter()
                .append('g')
                .attr('class', 'tree-node')
                .on('dblclick', (d) => {
                    this._nodeClick(d);
                    this._callbackNodeClick && this._callbackNodeClick(d, this);
                })
                .on('click', d => {
                    this._nodeClick(d)
                });
            // 添加node
            {
                enterTreeNode.append('circle')
                    .attr('r', this.nodeSize[0] / 2)
                    .attr('title', 'da')
                    .attr('fill', (d) => {
                        return this.nodeColor(d);
                    });
            }
            // 添加logo
            {
                enterTreeNode.append('text')
                    .attr('class', 'nodeIcon')
                    .attr('font-family', 'iconfont')
                    .attr('cursor', 'pointer')
                    .attr('font-size', this.nodeSize[0] * (3 / 5))
                    .style('text-anchor', 'middle')
                    .style('dominant-baseline', 'middle')
                    .text((d) => {
                        return this.nodeIcon(d)
                    })
                    .attr('y', 3)
                    .attr('fill', '#fff')
            }

            // 添加节点
            {
                enterTreeNode.append("text")
                    .attr('class', 'nameText')
                    .attr("x", 0)
                    .attr("y", this.nodeSize[0] / 2 + 10)
                    .style('font-weight', 600)
                    .attr("text-anchor", "middle")
                    .text(function (d) {
                        return d.name || d.userName || d.roleName || d.ruleName || d.columnNameCn || d.columnNameEn || d.tableNameCn || d.tableNameEn;
                    })
                    .attr('fill', (d) => {
                        return this.nodeColor(d);
                    });
            }

            // 添加动画
            {
                enterTreeNode
                    .attr("transform", d => {
                        let translate = '';
                        if (d.y0 !== undefined && d.x0 !== undefined) {
                            translate = `translate(${d.x0},${d.y0})`;
                        } else {
                            translate = `translate(${this._width / 2},${0})`
                        }
                        return translate;
                    });
                updateTreeNode.transition()
                    .duration(750)
                    .attr("transform", d => {
                        d.y0 = d.y;
                        d.x0 = d.x;
                        return `translate(${d.x},${d.y})`;
                    });
            }
            updateTreeNode.exit().remove();
        }

    }

    /**
     * 更新离散的节点
     */
    upDateFree() {
        this._freeNodeContainer.selectAll('.free-node').remove();
        const updateTreeNode = this._freeNodeContainer.selectAll('.free-node').data(this._freeNodes);
        const enterTreeNode = updateTreeNode.enter()
            .append('g')
            .attr('class', 'free-node')
            .on('click', d => {
                this._nodeClick(d)
            });
        // 添加node
        {
            enterTreeNode.append('circle')
                .attr('r', this.nodeSize[0] / 2)
                .attr('title', 'da')
                .attr('fill', (d) => {
                    return this.nodeColor(d);
                });
        }
        // 添加logo
        {
            enterTreeNode.append('text')
                .attr('class', 'nodeIcon')
                .attr('font-family', 'iconfont')
                .attr('cursor', 'pointer')
                .attr('font-size', this.nodeSize[0] * (3 / 5))
                .style('text-anchor', 'middle')
                .style('dominant-baseline', 'middle')
                .text((d) => {
                    return this.nodeIcon(d)
                })
                .attr('y', 3)
                .attr('fill', '#fff')
        }

        // 添加节点
        {
            enterTreeNode.append("text")
                .attr('class', 'nameText')
                .attr("x", 0)
                .attr("y", this.nodeSize[0] / 2 + 7)
                .attr("dy", ".35em")
                .style('font-weight', 600)
                .attr("text-anchor", "middle")
                .text(function (d) {
                    return d.name || d.userName || d.roleName || d.ruleName || d.columnNameCn || d.columnNameEn || d.tableNameCn || d.tableNameEn;
                })
                .attr('fill', (d) => {
                    return this.nodeColor(d);
                });
        }

        // 添加动画
        {
            enterTreeNode
                .attr("transform", d => {
                    return `translate(${d.x0},${d.y0})`;
                });
            updateTreeNode.transition()
                .duration(750)
                .attr("transform", d => {
                    if (!d.orginFree) {
                        d.orginFree = true;
                        d.x = (this._width - this.nodeSize[0] * 2) * Math.random() + this.nodeSize[0];
                        d.y = (this._height - this.nodeSize[0] * 2) * Math.random() + this.nodeSize[1];
                        d.x0 = d.x;
                        d.y0 = d.y;
                    }
                    return `translate(${d.x},${d.y})`;
                });
        }
        updateTreeNode.exit().remove();
    }

    /**
     * 节点点击
     * @param d
     * @private
     */
    _nodeClick(d) {
        const _this = this;
        {
            const nodes = this._treeNodeContainer.selectAll('.tree-node').each(node => {
                if (d === node) {
                    d.active = !d.active;
                } else {
                    node.active = false;
                }
            });
            nodes.attr('fill', function (d) {
                const circle = _this.d3.select(this).select('circle');
                const nameText = _this.d3.select(this).select('.nameText');
                if (d.active) {
                    circle.attr('fill', _this._activeColor);
                    nameText.attr('fill', _this._activeColor);
                } else {
                    circle.attr('fill', (d) => {
                        return _this.nodeColor(d);
                    });
                    nameText.attr('fill', (d) => {
                        return _this.nodeColor(d);
                    });
                }
            })
        }
        {
            const nodes = this._freeNodeContainer.selectAll('.free-node').each(node => {
                if (d === node) {
                    d.active = !d.active;
                } else {
                    node.active = false;
                }
            });
            nodes.attr('fill', function (d) {
                const circle = _this.d3.select(this).select('circle');
                const nameText = _this.d3.select(this).select('.nameText');
                if (d.active) {
                    circle.attr('fill', _this._activeColor);
                    nameText.attr('fill', _this._activeColor);
                } else {
                    circle.attr('fill', (d) => {
                        return _this.nodeColor(d);
                    });
                    nameText.attr('fill', (d) => {
                        return _this.nodeColor(d);
                    });
                }
            })
        }
    }

    /**
     * 添加树形数据
     * @param data
     */
    addTreeData(data) {
        this._treeData = data;
        this.updateTree();
    }

    /**
     * 添加节点
     * @param data
     */
    addNode(data) {
        if (!data) return;
        let isRepeat = false;
        this._treeNodeContainer.selectAll('.tree-node').each(node => {
            if ((node.nodeId + '') === (data.nodeId  + '')&& node.nodeType === data.nodeType) isRepeat = true;
        });
        this._freeNodeContainer.selectAll('.free-node').each(node => {
            if ((node.nodeId + '') === (data.nodeId  + '')&& node.nodeType === data.nodeType) isRepeat = true;
        });
        if (isRepeat) {
            this.warn();
            return "数据重复";
        }

        let parentNoeType = '';
        let parentNode = '';
        let childNode = '';
        let exist = false;
        this._linkRules.forEach(rule => {
            if (rule.toNodeType === data.nodeType) {
                exist = true;
                parentNoeType = rule.fromNodeType;
            }
            if (rule.fromNodeType === data.nodeType) {
                childNode = rule.toNodeType;
                exist = true;
            }
        });

        if (exist) {
            this.clearActive();
            data.active = true;
            this.setInitLoc(this._enterDirector[data.nodeType], data);
            /**
             * 这是树第一级
             */
            if (parentNoeType === '') {
                Object.assign(this._treeData, data);
                if (!this._treeData.children) this._treeData.children = [];
                let freeNodes = [];
                this._freeNodes.forEach(item => {
                    if (item.nodeType === childNode) {
                        this._treeData.children.push(item)
                    } else {
                        freeNodes.push(item)
                    }
                });
                this._freeNodes.splice(0, this._freeNodes.length, ...freeNodes);
            } else {
                if (this._treeData.nodeType === parentNoeType) {
                    if (!this._treeData.children) {
                        this._treeData.children = [];
                    }
                    this._treeData.children.splice(this._treeData.children.length / 2, 0, data);
                } else {
                    if (this._treeData.children) {
                        this.findNodeParentNode(this._treeData.children, parentNode, parentNoeType)
                    }
                    if (parentNode) {
                        if (!parentNode.children) {
                            parentNode.children = [];
                        }
                        parentNode.children.splice(parentNode.children.length / 2, 0, data);
                    } else {
                        this._freeNodes.push(data);
                    }
                }
            }
            this.updateTree();
            this.upDateFree();
            this.success();
        } else {
            this.warn();
        }
    }

    /**
     * 警告提示
     */
    warn() {
        this._warn.attr('stroke', 'red');
        setTimeout(() => {
            this._warn.attr('stroke', 'transparent');
        }, 500);
    }

    /**
     * 成功提示
     */
    success() {
        this._warn.attr('stroke', '#67C23A');
        setTimeout(() => {
            this._warn.attr('stroke', 'transparent');
        }, 500);
    }

    /**
     * 清除激活
     */
    clearActive() {
        this._nodeClick();
    }

    /**
     * 递归 寻找父节点
     * @param nodes
     * @param parentNode
     * @param parentNoeType
     */
    findNodeParentNode(nodes, parentNode, parentNoeType) {
        nodes.forEach(node => {
            if (node.nodeType === parentNoeType) {
                parentNode = node;
            } else {
                node.children && this.findNodeParentNode(node.children, parentNode, parentNoeType)
            }
        })
    }

    /**
     * 设置初始位置
     */
    setInitLoc(position, data) {
        let difLoc = this._container.attr('transform') && this._container.attr('transform').replace(/translate\(/, '').replace(/scale/, '').replace(/\)/, '').split(' ')[0].split(',');
        if (!difLoc) {
            difLoc = [0, 0]
        }
        if (position === 'top' || !position) {
            data.x0 = this._width / 2;
            data.y0 = 1 - difLoc[1];
        } else if (position === 'left') {
            data.y0 = this._height / 2;
            data.x0 = 1 - difLoc[0];
        }
    }

    getRelationData() {
        return this._treeData;
    }


}

export default TreeRelation;
