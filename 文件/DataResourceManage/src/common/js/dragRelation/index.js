/**
 * 拖拽关系图
 */
export default class DragRelationClass {
    constructor({idLoc, textLoc, nodes, links, width, height, containerClassName, d3}) {
        /**
         * 每个节点的id在node的什么位置
         */
        this.idLoc = idLoc;
        /**
         * 每个节点的文字在node的什么位置
         */
        this.textLoc = textLoc;
        /**
         * 每个关系图的节点
         */
        this.nodes = nodes;

        /**
         * 关系图的关系
         */
        this.links = links;

        /**
         * 容纳svg的宽高
         */
        this.width = width;
        this.height = height;

        /**
         * d3插件
         */
        this.d3 = d3;

        /**
         * 容器的class类名
         */
        this.containerClassName = containerClassName;

        /**
         * 鼠标操作的变量
         */
        this.selected_node = null;
        this.selected_link = null;
        this.mousedown_link = null;
        this.mousedown_node = null;
        this.mouseup_node = null;
        /**
         * 每个按键只响应一次
         */
        this.lastKeyDown = -1;
        /**
         * 存放力初始化之后的对象
         */
        this.force = null;
        /**
         * 存放放缩对象
         */
        this.zoom = null;
        /**
         * svg的id
         */
        this.id = 'svg' + Math.random()

        // this.render();
    };


    /**
     * 创建node节点
     * @param ex 距离浏览器左边距离
     * @param ey 距离浏览器头部距离
     */
    createNode(node, ex, ey) {
        // svg 距离浏览器左边距离  距离浏览器头部距离
        const {x, y, height, width} = document.getElementById(this.id).getBoundingClientRect();
        // 判断，只有当鼠标ex ey 在 svg区域内才能添加node节点
        if (ex >= x && ex <= (x + width) && ey >= y && ey <= (y + height)) {
            const _this = this;
            _this.svg.classed('active', true);
            node = {...node, reflexive: false};
            node['x'] = ex - x;
            node['y'] = ey - y;
            _this.nodes.push(node);
            _this.restart();
        }
    }

    /**
     * 清除所有的node和link
     */
    clearNode() {
        const  _this = this;
        _this.nodes.splice(0,_this.nodes.length)
        _this.links.splice(0,_this.links.length)
        _this.restart();
    }

    /**
     * 向外暴露nodes 和links
     */
    getNodes() {
        return this.nodes;
    };

    getLinks() {
        return this.links;
    };

    /**
     *
     */
    colors(color) {
        return "#000";
    };

    /**
     * 初始化鼠标操作的变量
     */
    resetMouseVars() {
        this.mousedown_node = null;
        this.mouseup_node = null;
        this.mousedown_link = null;
    }

    /**
     * 初始化渲染
     */
    render() {
        const _this = this;
        this.svg = _this.d3.select(this.containerClassName)
            .attr('id', this.id)
            // .call(_this.zoomFun(_this))
            // .append('svg')
            // .attr('width', this.width)
            // .attr('height', this.height);


            // .call(_this.zoomFun(_this))
            .append('svg')
            .attr('width', this.width)
            .attr('height', this.height)
            .append('g')
            // .call(_this.zoomFun(_this))
            // .append('g');
        this.svg.append('rect')
            .attr('width', this.width)
            .attr('height', this.height)
            .attr('fill', '#fff');


        this.linkNodeGroups();
        this.forceInit();
        this.defineArrowMarkers();
        this.dragLine();
        this.svg
        // .on('mousedown', function () {
        // })
            .on('mousemove', function () {
                _this.mousemove(_this, this)
            })
            .on('mouseup', function () {
                _this.mouseup(_this)
            });
        this.d3.select(window)
            .on('keydown', function () {
                _this.keydown(_this)
            })
            .on('keyup', function () {
                _this.keyup(_this)
            });
        _this.restart();
    }

    /**
     * 缩放
     */
    zoomFun(arg) {
        const _this = arg;
        _this.zoom = _this.d3.behavior.zoom().scaleExtent([0, 20]).on("zoom", function () {
            _this.zoomed(_this)
        });
        return _this.zoom;
    };

    /**
     * 放缩执行
     */
    zoomed(arg) {
        const _this = arg;
        // 获取class
        const targetClassName = _this.d3.event['sourceEvent'].target.getAttribute('class');

        if (_this.mousedown_node) return;
        _this.svg.attr("transform", `
                    translate(${_this.zoom.translate()}) scale(${_this.zoom.scale()}) `);

        // if (targetClassName === 'node') {
        // } else {
        //     if (_this.mousedown_node) return;
        //     _this.svg.attr("transform", `
        //             translate(${_this.zoom.translate()}) scale(${_this.zoom.scale()}) `);
        // }
    }

    /**
     * 初始化力关系图
     */
    forceInit() {
        const _this = this;
        this.force = this.d3.layout.force()
            .nodes(this.nodes)
            .links(this.links)
            .size([this.width, this.height])
            .linkDistance(150)
            .charge(-500)
            .on('tick', function () {
                _this.tick(_this)
            });
    }

    /**
     * 定义箭头
     */
    defineArrowMarkers() {
        this.svg.append('svg:defs').append('svg:marker')
            .attr('id', 'end-arrow')
            .attr('viewBox', '0 -5 10 10')
            .attr('refX', 6)
            .attr('markerWidth', 3)
            .attr('markerHeight', 3)
            .attr('orient', 'auto')
            .append('svg:path')
            .attr('d', 'M0,-5L10,0L0,5')
            .attr('fill', '#000');

        this.svg.append('svg:defs').append('svg:marker')
            .attr('id', 'start-arrow')
            .attr('viewBox', '0 -5 10 10')
            .attr('refX', 4)
            .attr('markerWidth', 3)
            .attr('markerHeight', 3)
            .attr('orient', 'auto')
            .append('svg:path')
            .attr('d', 'M10,-5L0,0L10,5')
            .attr('fill', '#000');
    }

    /**
     * 拖拽新节点时显示line
     */
    dragLine() {
        this.drag_line = this.svg.append('svg:path')
            .attr('class', 'link dragline hidden')
            .attr('d', 'M0,0L0,0');
    }

    /**
     * link 和 node 分组
     */
    linkNodeGroups() {
        this.path = this.svg.append('svg:g').selectAll('path');
        this.circle = this.svg.append('svg:g').selectAll('g');
    }

    /**
     * 更新力布局（每次迭代自动调用）
     */
    tick(arg) {
        const _this = arg;
        // draw directed edges with proper padding from node centers
        _this.path.attr('d', function (d) {
            var deltaX = d.target.x - d.source.x,
                deltaY = d.target.y - d.source.y,
                dist = Math.sqrt(deltaX * deltaX + deltaY * deltaY),
                normX = deltaX / dist,
                normY = deltaY / dist,
                sourcePadding = d.left ? 17 : 12,
                targetPadding = d.right ? 17 : 12,
                sourceX = d.source.x + (sourcePadding * normX),
                sourceY = d.source.y + (sourcePadding * normY),
                targetX = d.target.x - (targetPadding * normX),
                targetY = d.target.y - (targetPadding * normY);
            return 'M' + sourceX + ',' + sourceY + 'L' + targetX + ',' + targetY;
        });

        this.circle.attr('transform', function (d) {
            return 'translate(' + d.x + ',' + d.y + ')';
        });
    }

    /**
     * 更新图表（需要时调用）
     */
    restart() {
        const _this = this;
        // path (link) group
        _this.path = _this.path.data(_this.links);

        // update existing links
        _this.path.classed('selected', function (d) {
            return d === _this.selected_link;
        })
            .style('marker-start', function (d) {
                return d.left ? 'url(#start-arrow)' : '';
            })
            .style('marker-end', function (d) {
                return d.right ? 'url(#end-arrow)' : '';
            });


        // add new links
        _this.path.enter().append('svg:path')
            .attr('class', 'link')
            .classed('selected', function (d) {
                return d === _this.selected_link;
            })
            .style('marker-start', function (d) {
                return d.left ? 'url(#start-arrow)' : '';
            })
            .style('marker-end', function (d) {
                return d.right ? 'url(#end-arrow)' : '';
            })
            .on('mousedown', function (d) {
                if (_this.d3.event.ctrlKey) return;

                // select link
                _this.mousedown_link = d;
                if (_this.mousedown_link === _this.selected_link) _this.selected_link = null;
                else _this.selected_link = _this.mousedown_link;
                _this.selected_node = null;
                _this.restart();
            });

        // remove old links
        _this.path.exit().remove();


        // circle (node) group
        // NB: the function arg is crucial here! nodes are known by id, not by index!
        _this.circle = _this.circle.data(_this.nodes, function (d) {
            const arr = _this.idLoc.split('.');
            let id = d;
            arr.forEach(item => {
                id = id[item]
            });
            return id;
        });
        // _this.circle = _this.circle.data(_this.nodes);

        // update existing nodes (reflexive & selected visual states)
        _this.circle.selectAll('circle')
            .style('fill', function (d) {
                return (d === _this.selected_node) ? '#188bf5' : "#0e183fe6";
            })
            .classed('reflexive', function (d) {
                return d.reflexive;
            });

        // add new nodes
        var g = _this.circle.enter().append('svg:g');

        g.append('svg:circle')
            .attr('class', 'node')
            .attr('r', 12)
            .style('fill', function (d) {
                return (d === _this.selected_node) ? _this.d3['rgb'](_this.colors(d.id)).brighter().toString() : "#0e183fe6";
            })
            .style('stroke', function (d) {
                return _this.d3['rgb'](_this.colors(d.id)).darker().toString();
            })
            .classed('reflexive', function (d) {
                return d.reflexive;
            })
            .on('mouseover', function (d) {
                if (!_this.mousedown_node || d === _this.mousedown_node) return;
                // enlarge target node
                _this.d3.select(this).attr('transform', 'scale(1.1)');
            })
            .on('mouseout', function (d) {
                if (!_this.mousedown_node || d === _this.mousedown_node) return;
                // unenlarge target node
                _this.d3.select(this).attr('transform', '');
            })
            .on('mousedown', function (d) {
                if (_this.d3.event.ctrlKey) return;

                // select node
                _this.mousedown_node = d;
                if (_this.mousedown_node === _this.selected_node) _this.selected_node = null;
                else _this.selected_node = _this.mousedown_node;
                _this.selected_link = null;

                // reposition drag line
                _this.drag_line
                    .style('marker-end', 'url(#end-arrow)')
                    .classed('hidden', false)
                    .attr('d', 'M' + _this.mousedown_node.x + ',' + _this.mousedown_node.y + 'L' + _this.mousedown_node.x + ',' + _this.mousedown_node.y);
                _this.restart();
            })
            .on('mouseup', function (d) {
                if (!_this.mousedown_node) return;

                // needed by FF
                _this.drag_line
                    .classed('hidden', true)
                    .style('marker-end', '');

                // check for drag-to-self
                _this.mouseup_node = d;
                if (_this.mouseup_node === _this.mousedown_node) {
                    _this.resetMouseVars();
                    return;
                }

                // unenlarge target node
                _this.d3.select(this).attr('transform', '');

                // add link to graph (update if exists)
                // NB: links are strictly source < target; arrows separately specified by booleans
                var source, target, direction;
                // if (_this.mousedown_node.id < _this.mouseup_node.id) {
                //     source = _this.mousedown_node;
                //     target = _this.mouseup_node;
                //     direction = 'right';
                // } else {
                //     source = _this.mouseup_node;
                //     target = _this.mousedown_node;
                //     direction = 'left';
                // }

                source = _this.mousedown_node;
                target = _this.mouseup_node;
                direction = 'right';
                var link;
                link = _this.links.filter(function (l) {
                    return (l.source === source && l.target === target);
                })[0];

                if (link) {
                    link[direction] = true;
                } else {
                    link = {source: source, target: target, left: false, right: false};
                    link[direction] = true;
                    _this.links.push(link);
                }

                // select new link
                _this.selected_link = link;
                _this.selected_node = null;
                _this.restart();
            });

        // show node IDs
        g.append('svg:text')
            .attr('x', 0)
            .attr('y', 25)
            .attr('class', 'id')
            .text(function (d) {
                let text = d;
                if (_this.textLoc instanceof Array) {
                    for (let i = 0; i < _this.textLoc.length; i++) {
                        text = d;
                        try {
                            const item = _this.textLoc[i];
                            const arr = item.split('.');
                            arr.forEach(ite => {
                                text = text[ite]
                            });
                            if (typeof text === 'string') return text
                        } catch (e) {
                            text = d;
                        }
                    }
                    if (text === d) return 'unknown';

                } else {
                    _this.textLoc.forEach(item => {
                        const arr = item.split('.');
                    });
                    arr.forEach(item => {
                        text = text[item]
                    });
                    return text;
                }

            });

        // remove old nodes
        _this.circle.exit().remove();
        _this.force.start();
    }


    /**
     * 鼠标在svg上移动
     */
    mousemove(arg, mthis) {
        const _this = arg;
        if (!_this.mousedown_node) return;
        // update drag line
        _this.drag_line.attr('d', 'M' + _this.mousedown_node.x + ',' + _this.mousedown_node.y + 'L' + _this.d3.mouse(mthis)[0] + ',' + _this.d3.mouse(mthis)[1]);
        _this.restart();
    }

    /**
     * 鼠标在svg上弹起
     */
    mouseup(arg) {
        const _this = arg;
        if (_this.mousedown_node) {
            // hide drag line
            _this.drag_line
                .classed('hidden', true)
                .style('marker-end', '');
        }

        // because :active only works in WebKit?
        _this.svg.classed('active', false);

        // clear mouse event vars
        _this.resetMouseVars();
    }

    /**
     *
     * @param node
     */
    spliceLinksForNode(node) {
        const _this = this;
        var toSplice = _this.links.filter(function (l) {
            return (l.source === node || l.target === node);
        });
        toSplice.map(function (l) {
            _this.links.splice(_this.links.indexOf(l), 1);
        });
    }

    keydown(arg) {
        const _this = arg;
        // _this.d3.event.preventDefault();
        // return

        if (_this.lastKeyDown !== -1) return;
        _this.lastKeyDown = _this.d3.event.keyCode;

        // ctrl
        if (_this.d3.event.keyCode === 17) {
            _this.circle.call(force.drag);
            _this.svg.classed('ctrl', true);
        }

        if (!_this.selected_node && !_this.selected_link) return;
        switch (_this.d3.event.keyCode) {
            case 8: // backspace
            case 46: // delete
                if (_this.selected_node) {
                    _this.nodes.splice(_this.nodes.indexOf(_this.selected_node), 1);
                    _this.spliceLinksForNode(_this.selected_node);
                } else if (_this.selected_link) {
                    _this.links.splice(_this.links.indexOf(_this.selected_link), 1);
                }
                _this.selected_link = null;
                _this.selected_node = null;
                _this.restart();
                break;
            case 66: // B
                if (_this.selected_link) {
                    // set link direction to both left and right
                    _this.selected_link.left = true;
                    _this.selected_link.right = true;
                }
                _this.restart();
                break;
            case 76: // L
                if (_this.selected_link) {
                    // set link direction to left only
                    _this.selected_link.left = true;
                    _this.selected_link.right = false;
                }
                _this.restart();
                break;
            case 82: // R
                if (_this.selected_node) {
                    // toggle node reflexivity
                    _this.selected_node.reflexive = !_this.selected_node.reflexive;
                } else if (_this.selected_link) {
                    // set link direction to right only
                    _this.selected_link.left = false;
                    _this.selected_link.right = true;
                }
                _this.restart();
                break;
        }
    }

    keyup(arg) {
        const _this = arg;
        _this.lastKeyDown = -1;

        // ctrl
        if (_this.d3.event.keyCode === 17) {
            _this.circle
                .on('mousedown.drag', null)
                .on('touchstart.drag', null);
            _this.svg.classed('ctrl', false);
        }
    }
}