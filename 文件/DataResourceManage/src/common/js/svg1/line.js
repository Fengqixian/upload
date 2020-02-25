class Line {
  /*
  * @params
  * @d3 d3
  * @svg 存放line的svg盒子
  * @data line的起点和重点坐标
  * @iconSize  图标的尺寸
  * @lineColor  line的颜色
  * @strokeWidth  line的宽度
  * @shapeHeight  被链接物体的高度
  * @ballRadius  // 小球的半径
  * @ballColor  // 小球的颜色
  * @ballSpeed  // 小球的运行速度
  * */

  /*
  * @data 格式
  * [{x,y},{x,y}]
  * */
  constructor(arg) {
    // 线的起点和终点的位置
    this.lineFunc = arg.d3.line()
      .x(function (d) {
        return d.x
      })
      .y(function (d) {
        return d.y;
      });
    this.svg = arg.svg;
    this.data = arg.data;
    this.iconSize = parseFloat(arg.iconSize) || 50;
    this.lineColor = arg.lineColor || '#262f52';
    this.strokeWidth = arg.strokeWidth || 1;
    this.ballRadius = arg.ballRadius || 2;
    this.ballSpeed = arg.ballSpeed || 30;
    this.ballColor = arg.ballColor || 'red';
    this.linkInfo = arg.linkInfo;
    this.analysisData()
  }

  // 创建g并将g放到svg中
  g() {
    return this.svg.append('g')
  }

  // 创建line线
  line() {
    const path = this.g().append('path')
      .attr('id', `${this.linkInfo.from}_${this.linkInfo.to}`)
      .attr('d', this.lineFunc(this.data))
      .attr('stroke', this.lineColor)
      .attr('stroke-width', this.strokeWidth)
      .attr('fill', 'none')
      .attr('stroke-dasharray', `5 5`);
    path.append('animate')
      .attr('attributeType', 'XML')
      .attr('attributeName', 'stroke-dashoffset')
      .attr('from', '0')
      .attr('to', '-10')
      .attr('dur', '1s')
      .attr('repeatCount', 'indefinite')
  }

  // 创建小球并且运动
  ball() {
    this.svg.append('circle')
      .attr('cx', 0)
      .attr('cy', 0)
      .attr('r', this.ballRadius)
      .attr('fill', this.ballColor)
      .append('animateMotion')
      .attr('id', `ball_${this.linkInfo.from}_${this.linkInfo.to}`)
      .attr('path', this.lineFunc(this.data))
      .attr('begin', 0)
      .attr('dur', `${this.runTime()}s`)
      .attr('rotate', 'auto')
      .attr('repeatCount', 'indefinite')

  }

  // 小球运行时间
  runTime() {
    const [{x: x1, y: y1}, {x: x2, y: y2}] = this.data
    const x = x2 - x1
    const y = y2 - y1
    const z = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2))
    return z / this.ballSpeed
  }

  analysisData() {
    const [{x: x1, y: y1}, {x: x2, y: y2}] = this.data;
    if (Math.abs(x2 - x1) < (this.iconSize * 1.5) && Math.abs(y2 - y1) > (this.iconSize * 1.5)) {
      if (y2 >= y1) {
        this.data = [{
          x: x1 + this.iconSize / 2,
          y: y1 + this.iconSize,
        }, {
          x: x2 + this.iconSize / 2,
          y: y2
        }]
      } else {
        this.data = [{
          x: x1 + this.iconSize / 2,
          y: y1
        }, {
          x: x2 + this.iconSize / 2,
          y: y2 + this.iconSize,
        }]
      }
    } else if (x2 > x1) {
      this.data = [{
        x: x1 + this.iconSize,
        y: y1 + this.iconSize / 2,
      }, {
        x: x2,
        y: y2 + this.iconSize / 2
      }]
    } else if (x2 < x1) {
      this.data = [{
        x: x1,
        y: y1 + this.iconSize / 2
      }, {
        x: x2 + this.iconSize,
        y: y2 + this.iconSize / 2
      }]
    }
  }
}

export default Line
