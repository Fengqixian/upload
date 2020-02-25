class DragRelation {
    constructor({d3, select, width, height, nodesData, linksData, id, nodeColor, activeNodeColor, linkColor, activeLinkColor, textColor, activeTextColor, nodeR, textField, scaleExtent, dragLink, deleteLink, deleteNode}) {
        // 参数
        {
            this.d3 = d3;                                               // d3对象
            this.select = select;                                       // 选择对象
            this.width = width;                                         // 容器的宽度
            this.height = height;                                       // 容器的高度
            this.id = id;                                               // 唯一标识，用于标记每个DragRelation的class是唯一的
            this.nodesData = nodesData || [];                           // 连接的node节点数据
            this.linksData = linksData || [];                           // 连接关系
            this.nodeColor = nodeColor || '#0b96dc';                    // 节点的颜色
            this.activeNodeColor = activeNodeColor || '#3366cc';        // 被激活连接线的颜色
            this.nodeR = nodeR || 20;                                   // node节点的半径
            this.linkColor = linkColor || '#0b96dc';                    // 连接线的颜色
            this.activeLinkColor = activeLinkColor || '#3366cc';        // 被激活连接线的颜色
            this.textColor = textColor || '#0b96dc';                    // 文字的颜色
            this.activeTextColor = activeTextColor || '#3366cc';        // 被激活文字的颜色
            this.textField = textField || 'name';                       // 文字展示名字字段
            this.scaleExtent = scaleExtent || [0.3, 20];                // 放缩比例
            this.dragLink = dragLink === false ? dragLink : true;       // 是否允许拖拽连接
            this.deleteLink = deleteLink === false ? deleteLink : true; // 是否允许删除连接关系
            this.deleteNode = deleteNode === false ? deleteNode : true; // 是否允许删除node节点
        }
        // 全局变量
        {
            this.nodes = null;                                          // 所有node节点对象
            this.links = null;                                          // 所有连接对象
            this.texts = null;                                          // 所有文字对象
            this.svg = null;                                            // 容器对象#
            this.force = null;                                          // 力导向图对象
            this.linePath = null;                                       // 路径生成器方法
            this.activeNode = null;                                     // 被激活的node节点数据
            this.activeLink = null;                                     // 被激活的 links
            this.dragIng = false;                                       // 全局变量 是执行了拖动
            this.dragstartPx = null;                                    // force 拖拽node节点开始位置x坐标
            this.dragstartPy = null;                                    // force 拖拽node节点开始位置y坐标

            /**
             * 鼠标操作连接区域
             */
            this.mouseLinkLine = null;                                  // 鼠标操作连接的连接线
            this.mouseLinkLineStartLoc = null;                          // 鼠标操作连接的连接线的开始位置
            this.mouseLinkLineSourceIndex = null;                       // 鼠标操作连接的连接线起索引
            this.mouseLinkLineTargetIndex = null;                       // 鼠标操作连接的连接线终索引
            this.addResultFlag = false;                                 // 添加nodesData 或者linksData 是否成功
        }
        this.init();
    }

    /**
     * 初始化
     */
    init() {
        const _this = this;
        // 初始化 路径生成器方法
        _this.linePath = d3.svg.line();

        // 初始化 容器对象
        {
            _this.svg = _this.d3.select(_this.select)
                .append('svg')
                .attr('id', 'svg' + _this.id)
                .attr('width', _this.width)
                .attr('height', _this.height)
                .call(this.d3.behavior.zoom()
                    .scaleExtent(_this.scaleExtent)
                    .on('zoom', function () {
                        _this.zoomed()
                    }))
                .on('dblclick.zoom', null)
                .append('g')
                .attr('id', 'wrapper' + _this.id)
        }

        // 定义箭头
        {
            // 正常状态下的箭头
            _this.svg.append('svg:defs').append('svg:marker')
                .attr('id', 'arrow' + _this.id)
                .attr('viewBox', '0 -5 10 10')
                .attr('refX', _this.nodeR + 6)
                .attr('markerWidth', 3)
                .attr('markerHeight', 3)
                .attr('orient', 'auto')
                .append('svg:path')
                .attr('d', 'M0,-5L10,0L0,5')
                .attr('fill', _this.linkColor);
            // 激活的箭头
            _this.svg.append('svg:defs').append('svg:marker')
                .attr('id', 'activeArrow' + _this.id)
                .attr('viewBox', '0 -5 10 10')
                .attr('refX', _this.nodeR + 6)
                .attr('markerWidth', 3)
                .attr('markerHeight', 3)
                .attr('orient', 'auto')
                .append('svg:path')
                .attr('d', 'M0,-5L10,0L0,5')
                .attr('fill', _this.activeLinkColor);
            // 拖拽连接线箭头
            _this.svg.append('svg:defs').append('svg:marker')
                .attr('id', 'dragArrow' + _this.id)
                .attr('viewBox', '0 -5 10 10')
                .attr('refX', 3)
                .attr('markerWidth', 3)
                .attr('markerHeight', 3)
                .attr('orient', 'auto')
                .append('svg:path')
                .attr('d', 'M0,-5L10,0L0,5')
                .attr('fill', _this.activeLinkColor);
        }

        // 初始化 鼠标操作连接的连接线
        {
            _this.mouseLinkLine = _this.svg.append('path')
                .attr('id', 'mouseLinkLine')
                .attr('stroke', _this.activeLinkColor)
                .attr('stroke-width', 4)
                .attr('marker-end', 'url(#dragArrow' + _this.id + ')');
        }

        // 初始化 所有node节点对象 所有连接对象
        {
            _this.links = _this.svg.append('g')
                .attr('id', 'links' + _this.id)
                .selectAll('.link' + _this.id)
                .append('path')
                .attr('class', 'link' + _this.id);
            _this.nodes = _this.svg.append('g')
                .attr('id', 'nodes' + _this.id)
                .selectAll('.node' + _this.id)
                .append('circle')
                .attr('class', 'node' + _this.id);
            _this.texts = _this.svg.append('g')
                .attr('id', 'texts' + _this.id)
                .selectAll('.text' + _this.id)
                .append('text')
                .attr('class', 'text' + _this.id)

        }


        // 初始化 力导向图对象
        {
            _this.force = _this.d3.layout.force()
                .nodes(_this.nodesData)
                .links(_this.linksData)
                .size([_this.width, _this.height])
                .charge(-400)
                .linkDistance(200)
                .on('tick', function () { // 添加监听运动每一步
                    _this.tick();
                });
        }

        _this.renderForce();

        // 监听键盘 window 添加事件
        {
            _this.d3.select(window)
                .on('keydown', function () {
                    const keyCode = _this.d3.event.keyCode;
                    // delete键
                    if (keyCode === 46) {
                        // 删除被激活的node和link节点
                        if (_this.activeLink !== null && _this.deleteLink) {
                            _this.linksData.splice(_this.linksData.indexOf(_this.activeLink), 1);
                        } else if (_this.activeNode !== null && _this.deleteNode) {
                            _this.nodesData.splice(_this.nodesData.indexOf(_this.activeNode), 1);
                            // 将没有被选中的过滤出来
                            let linksData = _this.linksData.filter(link => {
                                return link.target !== _this.activeNode && link.source !== _this.activeNode;
                            });

                            // 删除原数据 将过滤出来的数据重新插入进来
                            _this.linksData.splice(0, _this.linksData.length, ...linksData);
                        }

                        if ((_this.activeLink !== null && _this.deleteLink)
                            || (_this.activeNode !== null && _this.deleteNode)) {
                            // 初始化 activeNode
                            _this.activeNode = null;
                            _this.activeLink = null;
                            let nodesData = _this.nodesData.splice(0, _this.nodesData.length);
                            let linksData = _this.linksData.splice(0, _this.linksData.length);
                            _this.linksData.splice(0, _this.linksData.length);
                            _this.renderForce();
                            _this.nodesData.push(...nodesData);
                            _this.linksData.push(...linksData);
                            _this.renderForce();
                        }
                    }
                })
                .on('mouseup', function () {
                    _this.windowMouseup()
                })
        }

    }

    /**
     * 监听力导向图运动每一步
     */
    tick() {
        const _this = this;
        _this.links
            .attr('d', d => {
                return _this.linePath([[d.source.x, d.source.y], [d.target.x, d.target.y]])
            });
        _this.nodes
            .attr('cx', d => {
                return d.x
            })
            .attr('cy', d => {
                return d.y
            });
        _this.texts
            .attr('x', d => {
                return d.x;
            })
            .attr('y', d => {
                return d.y;
            });
    }

    /**
     * 渲染force
     */
    renderForce() {
        const _this = this;
        // 使用这种方式，刷新 nodesData linksData 数据产生更新的效果
        {
            _this.linksData.splice(0, _this.linksData.length, ..._this.linksData);
            _this.nodesData.splice(0, _this.nodesData.length, ..._this.nodesData);
        }

        // 从 force 中获取对象的 nodes 和 links 数据
        {
            _this.links = _this.links.data(_this.force.links());
            _this.nodes = _this.nodes.data(_this.force.nodes());
            _this.texts = _this.texts.data(_this.force.nodes());
        }

        // 生成 link 线条 并添加样式
        {
            _this.links.enter()
                .append('path')
                .attr('class', 'link' + _this.id)
                .attr("stroke", _this.linkColor)
                .attr('stroke-width', 4)     // 描边宽度
                .attr('stroke-dasharray', '5 5')
                .style('cursor', 'pointer');
            // 多余的 link线条 给移除
            _this.links.exit().remove();

            _this.links.append('animate') // 添加 让连线运动
                .attr('attributeType', 'XML')
                .attr('attributeName', 'stroke-dashoffset')
                .attr('from', '0')
                .attr('to', '-10')
                .attr('dur', '1s')
                .attr('repeatCount', 'indefinite');   // 将线段分割为一段一段

            // 连接线添加事件
            _this.links.on('mouseover', function () {
                _this.force.stop();
            }).on('mouseout', function () {
                // _this.force.resume();
            }).on('mousedown', function (d) {
                _this.linkMouseup(d)
            });

            // 添加箭头
            _this.links.attr('marker-end', 'url(#arrow' + _this.id + ')');
        }

        // 生成 node 节点 并添加样式
        {
            _this.nodes.enter()
                .append('circle')
                .attr('class', 'node' + _this.id)
                .attr('r', _this.nodeR)
                .attr('fill', _this.nodeColor)
                .style('cursor', 'pointer')
                .on('mouseover', function () {
                    _this.force.stop();
                })
                .on('mouseout', function () {
                    _this.force.resume();
                })
                .on('mouseup', function (d, i) {
                    _this.nodesMouseUp(this, d, i)
                })
                .call(_this.force.drag()
                    .on('dragstart', function (d, i) {
                        _this.forceDragstart(d, i);
                    })
                    .on('drag', function (d) {
                        _this.forceDrag(this, d);
                    })
                    .on('dragend', function (d) {
                        _this.forceDragend(this, d);
                    }));
            _this.nodes.exit().remove();
        }

        // 生成 文字 并添加样式
        {
            _this.texts.enter()
                .append('text')
                .attr('class', 'text' + _this.id)
                .attr('dy', _this.nodeR + 13)
                .attr('fill', _this.textColor)
                .style('font-weight', 600)
                .style('font-size', 14)
                .style('text-anchor', 'middle')
                .text(d => {
                    return d[_this.textField];
                });
            _this.texts.exit().remove();
        }

        // 鼠标操作连接的连接线 隐藏
        _this.mouseLinkLine.style('opacity', '0');
        // 开始计算力导向图
        _this.force.start();
    }

    /**
     * force拖拽开始
     * @param d 被操作的node数据
     * @param i 被操作的node在 nodesData数组中的索引
     */
    forceDragstart(d, i) {
        const _this = this;
        // 阻止冒泡，拖拽node时候，不影响zoom放缩
        _this.d3.event.sourceEvent.stopPropagation();
        // 处理拖拽node节点禁止不动
        {

            // 判断操作的dom 是不是被激活的dom
            if (_this.activeNode !== null && d === _this.activeNode) {
                // 将操作的node dom 缓存起来
                _this.dragstartToElement = _this.d3.event.sourceEvent.toElement;
                // 将node的索引值存储起来
                _this.mouseLinkLineSourceIndex = i;
                // 是的话 将所有的小球停止运动
                _this.force.stop();
                // 缓存拖拽前小球的位置 用于拖拽之后 node回退到拖拽前的位置
                _this.dragstartPx = d.px;
                _this.dragstartPy = d.py;
            }
        }

        // 拖拽连线
        {
            if (_this.activeNode !== null && d === _this.activeNode) {
                // 将拖拽连线的起始值缓存下来
                _this.mouseLinkLineStartLoc = [d.x, d.y];
            }
        }
    }

    /**
     * force拖拽中
     * @param $this fun 对象
     * @param d 被操作的node数据
     */
    forceDrag($this, d) {
        const _this = this;
        // 处理拖拽node节点禁止不动
        {
            // 判断操作的dom 是不是被激活的dom
            if (_this.activeNode !== null && d === _this.activeNode) {
                // 是的话 将所有的小球停止运动
                _this.force.stop();
            }
            // 缓存 node在执行拖拽操作
            _this.dragIng = true;
        }
        // 拖拽连线
        if (_this.dragLink) {
            if (_this.activeNode !== null && d === _this.activeNode) {
                // 拖拽时候的位置
                _this.mouseLinkLine.style('opacity', '1');
                _this.mouseLinkLine.attr('d', function () {
                    return _this.linePath([_this.mouseLinkLineStartLoc, _this.d3.mouse($this)]);
                })
            }
        }
    }

    /**
     * force 拖拽结束
     * @param $this fun 对象
     * @param d 被操作的node数据
     */
    forceDragend($this, d) {
        // 判断操作的dom 是不是被激活的dom
        const _this = this;
        if (_this.activeNode !== null && d === _this.activeNode) {
            // 将node位置回退到拖拽之前的位置
            d.px = _this.dragstartPx;
            d.py = _this.dragstartPy;
            // 重新加热冷却参数，并重启模拟
            _this.force.resume();
            // 将关于node节点的缓存置为 null
            _this.dragstartToElement = null;
            _this.dragstartPx = null;
            _this.dragstartPy = null;
        }
    }

    /**
     * 放缩中功能实现
     */
    zoomed() {
        const _this = this;
        _this.d3.select('#wrapper' + _this.id)
            .attr('transform',
                'translate(' + _this.d3.event.translate + ') scale(' + _this.d3.event.scale + ')')
    }


    /**
     * node节点鼠标按下up去执行的事件
     * @param $this fun 对象
     * @param d 被操作的node数据
     * @param i 被操作的node在 nodesData数组中的索引
     */
    nodesMouseUp($this, d, i) {
        const _this = this;
        // 连线操作
        if (_this.dragLink) {
            // node被激活  且 鼠标操作连接的连接线起索引 不等于 鼠标操作连接的连接线终索引
            _this.mouseLinkLineTargetIndex = i;
            if (_this.activeNode
                && _this.mouseLinkLineSourceIndex !== null
                && _this.mouseLinkLineTargetIndex !== null
                && _this.mouseLinkLineSourceIndex !== _this.mouseLinkLineTargetIndex) {
                // 添加连接关系
                // _this.linksData.map(item => item.activeLink = false);
                _this.linksData.push({
                    source: _this.mouseLinkLineSourceIndex,
                    target: _this.mouseLinkLineTargetIndex
                });

                // 清除 鼠标操作连接的连接线起索引 和 鼠标操作连接的连接线终索引
                _this.mouseLinkLineSourceIndex = null;
                _this.mouseLinkLineTargetIndex = null;

                // 设置新创建 link 为激活状态
                if (_this.deleteLink || _this.dragLink) {
                    setTimeout(() => {
                        _this.cacheActiveNodeOrLink('link', _this.linksData[_this.linksData.length - 1]);
                    }, 1);
                }

                // 重新加载force
                _this.renderForce();
            }
        }


        // 激活node操作
        if (_this.deleteNode || _this.dragLink) {
            // 防止 拖拽node之后 node被激活
            if (_this.dragIng) {
                // 关闭拖拽中变量
                _this.dragIng = false;
            }
            // 激活 node
            else {
                // 判断此节点有木有被激活，没有的话，将其激活，并保存激活对象，有的话，将其失去激活，并其原保存的数据置为null
                setTimeout(() => {
                    _this.cacheActiveNodeOrLink('node', d);
                }, 1)
            }
        }
    }

    /**
     * link鼠标up事件
     * @param d 被操作的link数据
     */
    linkMouseup(d) {
        const _this = this;
        if (_this.deleteLink || _this.dragLink) {
            _this.cacheActiveNodeOrLink('link', d);
        }
    }

    /**
     * 缓存被激活的 node 或者 link 请更改样式
     * @param type 激活类型 node 或者 link
     * @param d 被操作的node数据
     */
    cacheActiveNodeOrLink(type, d) {
        const _this = this;
        // 将所有的样式给初始化
        _this.nodes.attr('fill', _this.nodeColor);
        _this.texts.attr('fill', _this.textColor);
        _this.links.attr('stroke', _this.linkColor).attr('marker-end', 'url(#arrow' + _this.id + ')');
        if (d === _this.activeNode || d === _this.activeLink) {
            // 初始化 被缓存 node 和 link
            _this.activeNode = null;
            _this.activeLink = null;
            return
        }
        // 初始化 被缓存 node 和 link
        _this.activeNode = null;
        _this.activeLink = null;
        if (type === 'node') {
            _this.nodes[0].forEach(node => {
                if (node.__data__ === d) {
                    _this.d3.select(node).attr('fill', _this.activeNodeColor);
                    _this.activeNode = d;
                }
            });
            _this.texts[0].forEach(text => {
                if (text.__data__ === d) {
                    _this.d3.select(text).attr('fill', _this.activeTextColor);
                }
            });
        } else if (type === 'link') {
            _this.links[0].forEach(link => {
                if (link.__data__ === d) {
                    _this.d3.select(link).attr('stroke', _this.activeLinkColor).attr('marker-end', 'url(#activeArrow' + _this.id + ')');
                    _this.activeLink = d;
                }
            });
        }
    }

    /**
     * window添加鼠标按键抬起事件
     */
    windowMouseup() {
        const _this = this;
        // 关闭拖拽中变量 适用异步，避免此操作在nodesMouseUp之前操作
        setTimeout(() => {
            _this.dragIng = false;
        }, 1)

        // 防止鼠标操作连接的连接线 连接数鼠标在非node节点上弹起
        {
            if (_this.d3.event.srcElement.nodeName !== 'circle' && _this.mouseLinkLineSourceIndex !== null) {
                _this.mouseLinkLineSourceIndex = null;
                _this.mouseLinkLineTargetIndex = null;
                // 鼠标操作连接的连接线 隐藏
                _this.mouseLinkLine.style('opacity', '0');
            }
        }
    }

    /**
     * 添加是结果
     * @return boolean值
     */
    getAddResult() {
        return this.addResultFlag;
    }

    /**
     * 添加 node 节点
     * @param node 添加的node节点
     * @param ex 鼠标触发事件的位置
     * @param ey 鼠标触发事件的位置
     */
    addNode(node, ex, ey) {
        const _this = this;
        _this.addResultFlag = false;
        // svg 距离浏览器左边距离  距离浏览器头部距离
        const {x, y, height, width} = document.getElementById('svg' + _this.id).getBoundingClientRect();
        // 判断，只有当鼠标ex ey 在 svg区域内才能添加node节点
        if (ex !== undefined && ey !== undefined) {
            if (ex >= x && ex <= (x + width) && ey >= y && ey <= (y + height)) {
                node['x'] = ex - x;
                node['y'] = ey - y;
                _this.nodesData.push(node);
                _this.renderForce();
                if (_this.deleteNode || _this.dragLink) {
                    _this.cacheActiveNodeOrLink('node', _this.nodesData[_this.nodesData.length - 1]);
                }
                _this.addResultFlag = true;
            }
        } else {
            _this.nodesData.push(node);
            _this.renderForce();
            if (_this.deleteNode || _this.dragLink) {
                _this.cacheActiveNodeOrLink('node', _this.nodesData[_this.nodesData.length - 1]);
            }
            _this.addResultFlag = true;
        }
    }

    /**
     * 批量添加node节点
     * @param nodesData node节点数据
     * @param linksData link数据
     * @param ex
     * @param ey
     * @param clear 是否需要先清屏再添加
     */
    addNodesLinks(nodesData, linksData, ex, ey, clear) {
        const _this = this;
        // 设置添加结果
        _this.addResultFlag = false;
        // svg 距离浏览器左边距离  距离浏览器头部距离
        const {x, y, height, width} = document.getElementById('svg' + _this.id).getBoundingClientRect();
        // 判断，只有当鼠标ex ey 在 svg区域内才能添加node节点
        if (ex !== undefined && ey !== undefined) {
            if (ex >= x && ex <= (x + width) && ey >= y && ey <= (y + height)) {
                // 清 屏
                if (clear) {
                    _this.clear();
                }
                _this.nodesData.push(...nodesData);
                _this.linksData.push(...linksData);
                _this.renderForce();
                // 设置添加结果
                _this.addResultFlag = true;
            }
        }
        // 没有添加初始位置
        else {
            // 清 屏
            if (clear) {
                _this.clear();
            }
            _this.nodesData.push(...nodesData);
            _this.linksData.push(...linksData);
            _this.renderForce();
            // 设置添加结果
            _this.addResultFlag = true;
        }
    }

    /**
     * 获取已有的nodes数据
     * @returns {*|Array}
     */
    getNodesData() {
        return this.nodesData
    }

    /**
     * 获取已经连接的连接数据
     * @returns {*|Array}
     */
    getLinksData() {
        return this.linksData
    }

    /**
     * 清除所有的node和link数据
     */
    clear() {
        const _this = this;
        _this.nodesData.splice(0, _this.nodesData.length);
        _this.linksData.splice(0, _this.linksData.length);
        _this.renderForce();
    }

    /**
     * 只清空link数据
     */
    clearLinksData() {
        const _this = this;
        _this.linksData.splice(0, _this.linksData.length);
        _this.renderForce();
    }
}

export default DragRelation;
