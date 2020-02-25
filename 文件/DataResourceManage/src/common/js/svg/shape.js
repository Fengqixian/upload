const isIE = () => {
    return !!window.ActiveXObject || "ActiveXObject" in window;
};
const CONFIG = {
    fuwuqi: 'data:image/png;base64,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',
    zhibiao: 'data:image/png;base64,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',
    biao: 'data:image/png;base64,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',
    ziduan: 'data:image/png;base64,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',
    shujuku: 'data:image/png;base64,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'

};

class Shape {
    /*
    * @params
    * @svg 存放shape的svg盒子
    * @loc line的起点和重点坐标
    * @icon shape 图形
    * @iconSize  图标的尺寸
    * @iconColor 图形颜色
    * @textStyle 字体样式
    * */
    constructor(arg) {
        this.svg = arg.svg;
        this.icon = arg.icon;
        this.ballRadius = arg.ballRadius;
        this.loc = arg.loc;
        this.name = arg.name || '';
        this.iconSize = arg.iconSize || 50;
        this.iconColor = arg.iconColor || '#2c3e50';
        this.textStyle = arg.textStyle;
        this.titleSize = arg.titleSize;
        this.data = arg.data;
        this.d3 = arg.d3;
        this.diffx = 0;
        this.diffy = 0;
        this.allData = arg.allData;
        this.groupPadding = arg.groupPadding;
        this.groupMargin = arg.groupMargin;
        this.groupTitleSize = arg.groupTitleSize;
        this.select = arg.select;
        this.lineFunc = arg.d3.svg.line()
            .x(function (d) {
                return d.x
            })
            .y(function (d) {
                return d.y;
            });
    }

    createShape() {
        if (isIE()) {
            return this.svg.append('image')
                .attr('id', this.data.id)
                .attr('x', this.loc.x)
                .attr('y', this.loc.y)
                .attr('data', `${JSON.stringify(this.data)}`)
                .attr("width", this.iconSize)
                .attr("height", this.iconSize)
                .attr("style", 'cursor: pointer')
                .attr('xlink:href', CONFIG[this.icon])
                // .call(this.d3.drag()
                .call(this.d3.behavior.drag()
                    .on('dragstart', (d) => {
                        const el = this.d3.select(`#${this.data.id}`);
                        const event = this.d3.event;
                        this.diffx = event.sourceEvent.x -  parseInt(el.attr('x'));
                        this.diffy = event.sourceEvent.y -  parseInt(el.attr('y'));
                    })
                    .on('drag', (d) => {
                        this.dragShape();
                    }))
        } else {
            return this.svg.append('svg:foreignObject')
                .attr('id', this.data.id)
                .attr('x', this.loc.x)
                .attr('y', this.loc.y)
                .attr("width", this.iconSize)
                .attr("height", this.iconSize)
                .append("xhtml:body")
                .html(`<i class="iconfont icon-${this.icon}" data='${JSON.stringify(this.data)}' style="font-size: ${this.iconSize}px;cursor: pointer;color:${this.iconColor};"></i>`)
                .call(this.d3.behavior.drag()
                    .on('dragstart', (d) => {
                        const el = this.d3.select(`#${this.data.id}`);
                        const event = this.d3.event;
                        this.diffx = event.sourceEvent.x - parseInt(el.attr('x'));
                        this.diffy = event.sourceEvent.y - parseInt(el.attr('y'));
                    })
                    .on('drag', (d) => {
                        this.dragShape();
                    })
                )
        }
    }

    // 拖拽shape
    dragShape() {
        const el = this.d3.select(`#${this.data.id}`);
        // const el = this.d3.select(document.getElementById(`#${this.data.id}`));
        const event = this.d3.event;
        let x = event.sourceEvent.x - this.diffx;
        let y = event.sourceEvent.y - this.diffy;
        const svg = this.d3.select(`#${this.select}_container`);
        const svgWidth = svg.attr('width') - this.iconSize;
        const svgHeight = svg.attr('height') - this.iconSize;
        if (x < 0) {
            x = 0
        }
        if (y < 0) {
            y = 0
        }
        if (x > svgWidth) {
            x = svgWidth
        }
        if (y > svgHeight) {
            y = svgHeight
        }
        this.loc.x = x;
        this.loc.y = y;
        el.attr('x', x);
        el.attr('y', y);
        const text = this.d3.select(`#text_${this.data.id}`);
        // const text = this.d3.select(document.getElementById(`#${this.data.id}`));
        text.attr('x', x + this.iconSize / 2)
            .attr('y', y + this.iconSize + 10);
        this.dragLine(x, y);
        this.dragGroup();
    }

    // 寻找ball对应的keyFrames
    getkeyFrames(name) {
        var animation = {};
        var ss = document.styleSheets;
        for (var i = 0; i < ss.length; ++i) {
            const item = ss[i];
            if (item.cssRules[0] && item.cssRules[0].name && item.cssRules[0].name === name) {
                animation.cssRule = item.cssRules[0];
                animation.styleSheet = ss[i];
                animation.index = 0;
            }
        }
        return animation;
    }

    // ie拖拽移动小球
    dragBall(item, data) {
        const ball = this.d3.select(`#ball_${item.linkInfo.from}_${item.linkInfo.to}`);
        const animation = this.getkeyFrames(`ball-run-ball_${item.linkInfo.from}_${item.linkInfo.to}`);
        animation.styleSheet.deleteRule(animation.index);
        var keyFrames = `
        @keyframes ball-run-ball_${item.linkInfo.from}_${item.linkInfo.to} {
          0% {
            left: ${this.analysisLinksData(data)[0].x}px;
            top: ${this.analysisLinksData(data)[0].y}px;
          }
          100% {
            left: ${this.analysisLinksData(data)[1].x}px;
            top: ${this.analysisLinksData(data)[1].y}px;
          }
        }
    `;
        animation.styleSheet.insertRule(keyFrames, animation.index);
        ball.attr('style', `animation:ball-run-ball_${item.linkInfo.from}_${item.linkInfo.to}1 ${this.runTime(this.analysisLinksData(data))}s infinite linear;width:${this.ballRadius * 2}px;height:${this.ballRadius * 2}px;`);
        setTimeout(_ => {
            ball.attr('style', `animation:ball-run-ball_${item.linkInfo.from}_${item.linkInfo.to} ${this.runTime(this.analysisLinksData(data))}s infinite linear;width:${this.ballRadius * 2}px;height:${this.ballRadius * 2}px;`);
        }, 1);
    }

    // 拖拽line
    dragLine(x, y) {
        const {shapes, links, groups, linksArr} = this.allData;
        const event = this.d3.event;
        const linkForms = linksArr.filter(item => item.linkInfo.from === this.data.id);
        const linkTos = linksArr.filter(item => item.linkInfo.to === this.data.id);
        if (isIE()) {
            linkForms.forEach(item => {
                const path = this.d3.select(`#${item.linkInfo.from}_${item.linkInfo.to}`);
                const data = item.data;
                data[0].x = x;
                data[0].y = y;
                this.dragBall(item, data);
                path.attr('d', this.lineFunc(this.analysisLinksData(data)));
            });
            linkTos.forEach(item => {
                const path = this.d3.select(`#${item.linkInfo.from}_${item.linkInfo.to}`);
                const data = item.data;
                data[1].x = x;
                data[1].y = y;
                this.dragBall(item, data);
                path.attr('d', this.lineFunc(this.analysisLinksData(data)));
            })
        } else {
            linkForms.forEach(item => {
                const path = this.d3.select(`#${item.linkInfo.from}_${item.linkInfo.to}`);
                const ball = this.d3.select(`#ball_${item.linkInfo.from}_${item.linkInfo.to}`);
                const data = item.data;
                data[0].x = x;
                data[0].y = y;
                path.attr('d', this.lineFunc(this.analysisLinksData(data)));
                ball.attr('path', this.lineFunc(this.analysisLinksData(data)));
                ball.attr('dur', `${this.runTime(this.analysisLinksData(data))}s`);
            });
            linkTos.forEach(item => {
                const path = this.d3.select(`#${item.linkInfo.from}_${item.linkInfo.to}`);
                const ball = this.d3.select(`#ball_${item.linkInfo.from}_${item.linkInfo.to}`);
                const data = item.data;
                data[1].x = x;
                data[1].y = y;
                path.attr('d', this.lineFunc(this.analysisLinksData(data)));
                ball.attr('path', this.lineFunc(this.analysisLinksData(data)));
                ball.attr('dur', `${this.runTime(this.analysisLinksData(data))}s`);
            })
        }

    }

    // 拖拽group
    dragGroup() {
        const {links, groups, linksArr} = this.allData;
        const wrapperGroup = groups.find(group => group.group === this.data.group);
        const {shapes} = wrapperGroup;
        let shapeLocX = [];
        let shapeLocY = [];
        shapes.forEach(item => {
            shapeLocX.push(item.loc.x);
            shapeLocY.push(item.loc.y);
        });
        const maxX = Math.max(...shapeLocX);
        const maxY = Math.max(...shapeLocY);
        const minX = Math.min(...shapeLocX);
        const minY = Math.min(...shapeLocY);
        const groupEl = this.d3.select(`#${wrapperGroup.group}`);
        // const groupEl = this.d3.select(document.getElementById(`#${wrapperGroup.group}`));
        const x = minX - this.groupPadding.left;
        const y = minY - this.groupPadding.top;
        const width = maxX - minX + this.groupPadding.left + this.groupPadding.right + this.iconSize;
        const height = maxY - minY + this.groupPadding.top + this.groupPadding.bottom + this.iconSize;
        groupEl.attr('x', x);
        groupEl.attr('y', y);
        groupEl.attr('width', width);
        groupEl.attr('height', height);
        const titleEl = this.d3.select(`#text_${wrapperGroup.group}`);
        // const titleEl = this.d3.select(document.getElementById(`#text_${wrapperGroup.group}`));
        titleEl.attr('x', x + width / 2);
        titleEl.attr('y', y + 5 + this.groupTitleSize);
        wrapperGroup.horizontalMaxWidth = width;
        wrapperGroup.verticalMaxHeight = height;
        wrapperGroup.x = x;
        wrapperGroup.y = y;
        this.dragFatherGroup(wrapperGroup)
    }

    // 拖拽一级group
    dragFatherGroup(son) {
        const {links, groups, linksArr} = this.allData;
        const fatherGroup = groups.find(item => item.group === son.parent);
        if (fatherGroup) {
            const sonGroup = groups.filter(item => item.parent === fatherGroup.group);
            let groupLocX = [];
            let groupLocY = [];
            let group_widthLocX = [];
            let group_widthLocY = [];
            sonGroup.forEach(item => {
                groupLocX.push(item.x);
                groupLocY.push(item.y);
                group_widthLocX.push(item.x + item.horizontalMaxWidth);
                group_widthLocY.push(item.y + item.verticalMaxHeight);
            });
            const maxX = Math.max(...groupLocX);
            const maxY = Math.max(...groupLocY);
            const minX = Math.min(...groupLocX);
            const minY = Math.min(...groupLocY);
            const maxWX = Math.max(...group_widthLocX);
            const maxWY = Math.max(...group_widthLocY);
            const groupEl = this.d3.select(`#${fatherGroup.group}`);
            // const groupEl = this.d3.select(document.getElementById(`#${fatherGroup.group}`));
            const x = minX - this.groupPadding.left - this.groupMargin.left;
            const y = minY - this.groupPadding.top - this.groupMargin.top;
            const width = maxWX - minX + this.groupMargin.left + this.groupMargin.right + this.groupPadding.left + this.groupPadding.right;
            const height = maxWY - minY + this.groupMargin.top + this.groupMargin.bottom + this.groupPadding.top + this.groupPadding.bottom;
            groupEl.attr('x', x);
            groupEl.attr('y', y);
            groupEl.attr('width', width);
            groupEl.attr('height', height);
            const titleEl = this.d3.select(`#text_${fatherGroup.group}`);
            // const titleEl = this.d3.select(document.getElementById(`#text_${fatherGroup.group}`));
            titleEl.attr('x', x + width / 2);
            titleEl.attr('y', y + 5 + this.groupTitleSize);
            fatherGroup.horizontalMaxWidth = width;
            fatherGroup.verticalMaxHeight = height;
            fatherGroup.x = x;
            fatherGroup.y = y;


        }
    }


    // 分析link数据
    analysisLinksData(linkData) {
        const [{x: x1, y: y1}, {x: x2, y: y2}] = linkData;
        if (Math.abs(x2 - x1) < (this.iconSize * 1.5) && Math.abs(y2 - y1) > (this.iconSize * 1.5)) {
            if (y2 >= y1) {
                linkData = [{
                    x: x1 + this.iconSize / 2,
                    y: y1 + this.iconSize,
                }, {
                    x: x2 + this.iconSize / 2,
                    y: y2
                }]
            } else {
                linkData = [{
                    x: x1 + this.iconSize / 2,
                    y: y1
                }, {
                    x: x2 + this.iconSize / 2,
                    y: y2 + this.iconSize,
                }]
            }
        } else if (x2 > x1) {
            linkData = [{
                x: x1 + this.iconSize,
                y: y1 + this.iconSize / 2,
            }, {
                x: x2,
                y: y2 + this.iconSize / 2
            }]
        } else if (x2 < x1) {
            linkData = [{
                x: x1,
                y: y1 + this.iconSize / 2
            }, {
                x: x2 + this.iconSize,
                y: y2 + this.iconSize / 2
            }]
        }
        return linkData
    }

    // 小球运行时间
    runTime(linkData) {
        const [{x: x1, y: y1}, {x: x2, y: y2}] = linkData
        const x = x2 - x1
        const y = y2 - y1
        const z = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2))
        return z / 30
    }


    createName() {
        const text = this.svg.append('text')
            .attr('id', `text_${this.data.id}`)
            .text(this.name)
            .attr('x', this.loc.x + this.iconSize / 2)
            .attr('y', this.loc.y + this.iconSize + 10)
            .attr('text-anchor', 'middle')
        let style = ''
        const textStyle = this.textStyle;
        for (let k in textStyle) {
            style += `${k}:${textStyle[k]};`
        }
        style += `font-size:${this.titleSize}px;`
        text.attr('style', style)
        return text
    }
}

export default Shape
