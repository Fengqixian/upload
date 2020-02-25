class Item {
    constructor({container, data}) {
        this.d3 = require('d3');
        this._container = container;
        this._data = data;


        this._itemHeight = 30;
        this._itemWidth = 300;
        this._columnSpace = 30;
        this._headerBgColor = '#39436A';
        this._bodyBgColor = '#fff';
        this._calcLoc();
    }

    _calcLoc() {
        let viceGroupDy = 0;
        this._data.width = this._itemWidth;
        this._data.height = (this._data.children.length - 1) * this._columnSpace;
        this._data.children.forEach(item => {
            // 每一个副组宽高
            item.width = this._itemWidth;
            item.height = this._itemHeight * ((item.children ? item.children.length : 0) + 1) + (item.children ? item.children.length - 1 : 0); //  (item.children ? item.children.length - 1 : 0) 实际是  (item.children ? item.children.length - 1 : 0) * 1，将每个字段抵消添加一个1单位的横线 最后一个column不添加横线  所以需要减1
            this._data.height += item.height;


            // 设置每列副组的y轴方向偏移量
            item.viceGroupDx = 0;
            item.viceGroupDy = viceGroupDy;
            viceGroupDy += this._itemHeight * ((item.children ? item.children.length : 0) + 1) + this._columnSpace;

            // 每副组 添加偏移量
            item.dy = 0;
            item.dx = 0;
            let dy = this._itemHeight;
            item.children && item.children.forEach(ite => {
                ite.dx = 0;
                ite.dy = dy;
                ite.width = this._itemWidth;
                ite.height = this._itemHeight;
                dy += this._itemHeight + 1; // 这个1 代表底线

            })

        })
    }

    render() {
        this._data.children.forEach(item => {
            const viceGroup = this._container.append('g').attr('class', 'vice-group');
            viceGroup.attr('transform', `translate(${item.viceGroupDx} ${item.viceGroupDy})`);
            let headerRect = null;
            // 画头部
            {
                const header = viceGroup.append('g')
                    .attr('class', 'header')
                    .attr('transform', `translate(${item.dx} ${item.dy})`);
                headerRect = header.append('rect')
                    .attr('width', this._itemWidth)
                    .attr('height', this._itemHeight)
                    .attr('fill', '#177FE4');
                header.append('text')
                    .text(item.name)
                    .style('fill', '#fff')
                    .style('font-size', '14px')
                    .attr('dx', this._itemWidth / 2)
                    .attr('dy', this._itemHeight / 2)
                    .attr('fill', this._headerBgColor)
                    .attr('text-anchor', 'middle')
                    .attr('dominant-baseline', 'middle');
            }

            // 画body
            {
                item.children && item.children.forEach((child, index) => {
                    const body = viceGroup.append('g')
                        .attr('class', 'body-item')
                        .attr('transform', `translate(${child.dx} ${child.dy})`);
                    body.append('rect')
                        .attr('width', this._itemWidth)
                        .attr('height', this._itemHeight)
                        .attr('fill', this._bodyBgColor);
                    /*// 添加每个item分割线
                    if (index < item.children.length - 1) {
                        body.append('line')
                            .attr('class', 'body-bottom-line')
                            .attr('x1', 0)
                            .attr('y1', 0)
                            .attr('x2', this._itemWidth)
                            .attr('y2', 0)
                            .attr('stroke', 'red')
                            .attr('transform', `translate(0 ${this._itemHeight + 1})`);
                    }*/
                    let text = body.append('text')
                        .text(child.name)
                        .style('fill', '#000')
                        .style('font-size', '12px')
                        .attr('dx', 30)
                        .attr('dy', this._itemHeight / 2)
                        .attr('fill', this._headerBgColor)
                        .attr('dominant-baseline', 'middle');
                    let icon = body.append('text')
                        .attr('font-family', 'iconfont')
                        .text('\ue7d4')
                        .style('fill', '#E39900')
                        .style('font-size', '12')
                        .attr('dx', 10)
                        .attr('dy', this._itemHeight / 2)
                        .attr('fill', this._headerBgColor)
                        .attr('dominant-baseline', 'middle');
                    if (child.isHighlight) {
                        text.style('fill', '#177FE4')
                            .style('font-weight', 600)
                            .append('animate')
                            .attr('begin', '0s')
                            .attr('dur', '1s')
                            .attr('attributeName', 'opacity')
                            .attr('from', '1')
                            .attr('to', '0.4')
                            .attr('repeatCount', 'indefinite');
                        icon.style('fill', '#177FE4')
                            .style('font-weight', 600)
                            .append('animate')
                            .attr('begin', '0s')
                            .attr('dur', '1s')
                            .attr('attributeName', 'opacity')
                            .attr('from', '1')
                            .attr('to', '0.4')
                            .attr('repeatCount', 'indefinite');
                        headerRect.attr('fill', this._headerBgColor)
                    }
                })

            }

        })
    }
}

export default Item;
