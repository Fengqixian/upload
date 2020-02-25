# DynamicTable 使用
## Table Attributes
| 参数	| 说明	| 类型	| 可选值	| 默认值| 
| :---: | :---: | :---: | :---: | :---: | 
| tableData | 显示的数据	 | array | — | — |
| headerData | 表格头部信息	 | array | — | — |
| paginationData | 分页配置	 | object | — | — |
| otherData | 父子间携带的其他属性	 | any | — | — |
| index | 代表此组件在遍历中的index下表，便于vue对Array<object>深层次的相应	 | number | — | — |
| operate | 操作	 | boolean | true/false | false |
| operateWidth | 操作宽度	 | string | - | - |
| checkBox | checkBox	 | boolean | - | false |
| headerCheckBox | 头部选择框	 | boolean | - | false |
| radio | table行单选	 | boolean | - | false |

### headerData
| 参数	| 说明	| 类型	| 可选值	| 默认值| 
| :---: | :---: | :---: | :---: | :---: | 
| prop | 对应列内容的字段名 | string | — | — |
| label | 显示的标题 | string | — | — |
| width | 对应列的宽度 | string | — | — |
| value | headerCheckBox为true，存放被选的值 | string | — | — |

## paginationData
| 参数	| 说明	| 类型	| 可选值	| 默认值| 
| :---: | :---: | :---: | :---: | :---: | 
| currentPage | 当前页数	 | number | — | 1 |
| pageSizes | 每页显示个数选择器的选项设置	 | number[] | — | [10, 20, 30, 40, 50, 100] |
| pageSize | 每页显示条目个数	 | number | — | 10 |
| total | 总条目数	 | number | — | — |

## Table Events
| 事件名称 | 说明 | 回调参数 |
| :---: | :---: | :---: |
| sizeChange | 每页显示条数发生变化 | 父组件传给子组件数据({tableData,paginationData,index,otherData}) |
| currentChange | 页数发生变化 | 父组件传给子组件数据({tableData,paginationData,index,otherData}) |

## Table 事件
| 事件名称 | 说明 | 回调参数 |
| :---: | :---: | :---: |
| sizeChange | 每页显示条数发生变化 | 父组件传给子组件数据({tableData,paginationData,index,otherData}) |
| currentChange | 页数发生变化 | 父组件传给子组件数据({tableData,paginationData,index,otherData}) |

## Table Scoped Slot

| 事件名称 | 说明 |
| :---: | :---: | 
| -- | 自定义列的内容，参数为 { row, column, $index } | 

