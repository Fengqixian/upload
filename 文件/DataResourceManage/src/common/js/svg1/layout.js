import * as d3 from "d3";

const defaultTypes = new Set(['server', 'database', 'table', 'column'])  // 默认层级顺序的types
const layout = (data, lineLength) => {
  const {shapes, links} = data
  const svg = d3.select('svg')
  const {width, height} = svg.node().getBoundingClientRect();
  let types = new Set()  // 存放数据中types
  let serializeTypes = new Set() // 序列化的types

  shapes.forEach(item => types.add(item.type))
  // 序列化的types
  defaultTypes.forEach(item => {
    if (types.has(item)) {
      serializeTypes.add(item)
    }
  });
  // 记录每一行之间的间距
  const typesMap = new Map()
  serializeTypes.forEach(item => {
    typesMap.set(item, 0)
  })

  // 序列化的types转为数组
  const serializeTypesArray = [...serializeTypes]
  const lineNum = types.size - 1
  const baseLocX = (width - (lineLength * lineNum)) / 2
  shapes.forEach(item => {
    // 初始化每个shape的位置
    item.loc = {x: baseLocX + serializeTypesArray.indexOf(item.type) * lineLength, y: typesMap.get(item.type) * 100}
    typesMap.set(item.type, typesMap.get(item.type) + 1)
    switch (item.type) {
      case 'server':
        item.icon = 'icon-fuwuqi'
        break
      case 'database':
        item.icon = 'icon-shujuku'
        break
      case 'table':
        item.icon = 'icon-biao'
        break
      case 'column':
        item.icon = 'icon-ziduan'
        break
    }
  });
  let linksArr = []
  links.forEach(linkItem => {
    const fromShapes = shapes.find(shape => shape.name === linkItem.from)
    const toShapes = shapes.find(shape => shape.name === linkItem.to)
    if (!toShapes || !fromShapes) return
    linksArr.push([{
      x: fromShapes.loc.x,
      y: fromShapes.loc.y
    }, {
      x: toShapes.loc.x,
      y: toShapes.loc.y
    }])
  })
  return {shapes, links: linksArr}
}

export default layout
