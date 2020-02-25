# CbTable文档
## 1、table 属性
| 参数 | 说明 | 类型 | 可选 | 默认 |备注 |
|:-|:-|:-|:-|:-|:-|
| headerData | 表头 | array | - | - | 看下面的headerData详情 | 
| tableData | 表数据 | array | - |- |  看下面的tableData详情 |
| paginationData | 分页数据 | object | - | - | 看下面的paginationData详情 |
| highlightCurrentRow | 是否支持单选 | boolean | - | false |- | 
| selection | 是否展示多选框 | boolean | - | false |- | 
| row-class-name | 行的 className 的回调方法，也可以使用字符串为所有行设置一个固定的 className。 | Function({row, rowIndex})/String | - | - |- | 
| isPagination | 是否使用分页 | boolean | - | true | - |
| isOperation | 是否底部添加操作按钮 | boolean | - | false | - |
---


### 1-1、headerDate 属性
| 参数 | 说明 | 类型 | 可选 | 备注 |
|:-|:-|:-|:-|:-|
| prop | 字段英文名 | string | - | - |
| label | 字段中文名 | string | - | - |
| width | 对应列的宽度 | string | - | - |
| type | 简单翻译 | string | switch、tag| 看下面type详情 |
---

### 1-2、type 属性使用
```js
headerData = [
    {
        prop: 'nameCn', // 字段英文名
        label:'中文名', // 字段中文名
        type: 'switch' // 数据为true 翻译成是，数据为false 翻译成否
    },
    {
        prop: 'status', // 字段英文名
        label:'状态', // 字段中文名
        type: 'tag', // 数据为true 翻译成是，数据为false 翻译成否
        translations: { // 将状态翻译成中文
            0: '正常', 
            1: '异常'
            2： "其他"
        },
        styles: {
            0: 'success',  // 可选值有： success/info/warning/danger
            1: 'error',
            2: 'default'
        }
    },
]
tableData = [
    {
        nameCn: true, // 被翻译成是
        status: 0, // 被翻译成 正常
    },
    {
        nameCn: false, // 被翻译成否
        status: 1, // 被翻译成 异常
    }
]
```
---

### 1-3、paginationData 属性
| 参数 | 说明 | 类型 | 可选 | 备注 |
|:-|:-|:-|:-|:-|
| currentPage | 当前页数 | number | - | - |
| pageSize | 每页显示个数 | number | - | - |
| total | 总条数 | number | - | - |
---



## 2、table 事件
| 参数 | 说明 | 参数 |
|:-|:-|:-|
| selection-change | 当选择项发生变化时会触发该事件 | selection |
| table-current-change | 当表格的当前行发生变化的时候会触发该事件，如果要高亮当前行，请打开表格的 highlight-current-row 属性 | currentRow, oldCurrentRow |
| size-change | 每页展示个数发生变化 | pageSize,paginationData,headerData,tableData |
| current-change | 当前页数发生变化 | currentPage,paginationData,headerData,tableData |
---


## 3、table 方法
| 参数 | 说明 | 参数 |
|:-|:-|:-|
| getSelectData | 获取被选择的值 | - |
| setCurrentRow | 用于单选表格，设定某一行为选中行，如果调用时不加参数，则会取消目前高亮行的选中状态。 | row |

## 4、table Scoped Slot
| 名称 | 说明 |
|:-|:-|
| column | 最后一行添加自定义列，参考element 添加类方式 |
| operation | 底部添加自定义按钮 |


## 4、demo
```html
<template>
    <cb-table headerData="headerData"
              tableData="taleData"
              paginationData="paginationData"
              selection
              @selection-change="selectionChange"
              @size-change="getData"
              @current-change="getData"
              ref="tableDemo"
              >
    
    </cb-table>
</template>
```
```js
<script>
    export default{
        data(){
            return {
                headerData: [
                    {
                        prop: 'nameCn', // 字段英文名
                        label:'中文名',  // 字段中文名
                        width: '100px'  // 设置这列宽度为 100px
                    },
                    {
                        prop: 'status', // 字段英文名
                        label:'中文名', // 字段中文名
                        type: 'switch' // 数据为true 翻译成是，数据为false 翻译成否
                    },
                    {
                        prop: 'tag', // 字段英文名
                        label:'状态', // 字段中文名
                        type: 'tag', // 数据为true 翻译成是，数据为false 翻译成否
                        translations: { // 将状态翻译成中文
                            0: '正常', 
                            1: '异常'
                            2： "其他"
                        },
                        styles: {
                            0: 'success',  // 可选值有： success/info/warning/danger
                            1: 'error',
                            2: 'default'
                        }
                    },
                ],
                tableData = [
                    {
                        nameCn: '张三', 
                        status: true, // 被翻译成是
                        tag: 0, // 被翻译成 正常
                    },
                    {
                        nameCn: '李四', 
                        status: false, // 被翻译成否
                        tag: 1, // 被翻译成 异常
                    }
                ],
                paginationData:{
                    currentPage: 1, // 当前页数
                    pageSize: 10, // 每页显示个数
                    total: 0, // 总条数
                }
            }
        },
        methods:{
            /**
            * 获取数据
            */
            getData(){
                let url = '/get/data';
                let params = {
                    current: this.paginationData.currentPage,
                    pageSize: this.paginationData.pageSize,
                }
                this.$http.get(url,{params}).then(response=>{
                    let res = response.data;
                    this.tableData = res.data;
                    this.paginationData.total = res.total;
                })
            },
            /**
            * 获取被选择的值
            */
            getSelectData(){
                this.$refs.tableDemo.getSelectData()
            },
            /**
            * 当选择项发生变化时会触发该事件
            */
            selectionChange(selection){
                console.log(selection)
            }
        }
    }

</script>
```

