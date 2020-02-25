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
    this.lineFunc = arg.d3.line()
      .x(function (d) {
        return d.x
      })
      .y(function (d) {
        return d.y;
      });

  }

  createShape() {
    return this.svg.append('svg:foreignObject')
      .attr('id', this.data.id)
      .attr('x', this.loc.x)
      .attr('y', this.loc.y)
      .attr("width", this.iconSize)
      .attr("height", this.iconSize)
      .append("xhtml:body")
      .html(`<i class="iconfont ${this.icon}" data='${JSON.stringify(this.data)}' style="font-size: ${this.iconSize}px;cursor: pointer;color:${this.iconColor};"></i>`)
      .call(this.d3.behavior.drag()
        .on('dragstart', (d) => {
          const el = this.d3.select(`#${this.data.id}`);
          // const el = this.d3.select(document.getElementById(`#${this.data.id}`));
          const event = this.d3.event;
          this.diffx = event.sourceEvent.x -  parseInt(el.attr('x'));
          this.diffy = event.sourceEvent.y -  parseInt(el.attr('y'));
        })
        .on('drag', (d) => {
          this.dragShape();
        }))
  }

  // 拖拽shape
  dragShape() {
    const el = this.d3.select(`#${this.data.id}`);
    // const el = this.d3.select(document.getElementById(`#${this.data.id}`));
    const event = this.d3.event;
    let x = event.x - this.diffx;
    let y = event.y - this.diffy;
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

  // 拖拽line
  dragLine(x, y) {
    const {shapes, links, groups, linksArr} = this.allData;
    const event = this.d3.event;
    const linkForms = linksArr.filter(item => item.linkInfo.from === this.data.id);
    const linkTos = linksArr.filter(item => item.linkInfo.to === this.data.id);
    linkForms.forEach(item => {
      const path = this.d3.select(`#${item.linkInfo.from}_${item.linkInfo.to}`);
      // const path = this.d3.select(document.getElementById(`#${item.linkInfo.from}_${item.linkInfo.to}`));
      const ball = this.d3.select(`#ball_${item.linkInfo.from}_${item.linkInfo.to}`);
      // const ball = this.d3.select(document.getElementById(`#ball_${item.linkInfo.from}_${item.linkInfo.to}`));
      const data = item.data;
      data[0].x = x;
      data[0].y = y;
      path.attr('d', this.lineFunc(this.analysisLinksData(data)));
      ball.attr('path', this.lineFunc(this.analysisLinksData(data)));
      ball.attr('dur', `${this.runTime(this.analysisLinksData(data))}s`);
    })
    linkTos.forEach(item => {
      const path = this.d3.select(`#${item.linkInfo.from}_${item.linkInfo.to}`);
      // const path = this.d3.select(document.getElementById(`#${item.linkInfo.from}_${item.linkInfo.to}`));
      const ball = this.d3.select(`#ball_${item.linkInfo.from}_${item.linkInfo.to}`);
      // const ball = this.d3.select(document.getElementById(`#ball_${item.linkInfo.from}_${item.linkInfo.to}`));
      const data = item.data;
      data[1].x = x;
      data[1].y = y;
      path.attr('d', this.lineFunc(this.analysisLinksData(data)));
      ball.attr('path', this.lineFunc(this.analysisLinksData(data)));
      ball.attr('dur', `${this.runTime(this.analysisLinksData(data))}s`);
    })
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
      const x = minX - this.groupPadding.left- this.groupMargin.left;
      const y = minY - this.groupPadding.top  - this.groupMargin.top ;
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
      .html(this.name)
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
