# DynamicForm使用
##  Form Attributes
| 参数	| 说明	| 类型	| 可选值	| 默认值| 
| :---: | :---: | :---: | :---: | :---: | 
| label | 标签文本 | string | — | — |
| key | 键 | string | — | — |
| value | 值 |  string | — | — |
| placeholder | input占位符 |  string | — | — |
| rules | 表单校验规则 |  Array | — | — |
| type | input类型 |  text，textarea，select 和[其他 原生 input 的 type 值](https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input#Form_%3Cinput%3E_types) | text | 
| options | type为select的选项 |  Array | — | — |

## options
| 参数	| 说明	| 类型	| 可选值	| 默认值| 
| :---: | :---: | :---: | :---: | :---: | 
| label | 标签文本 | string | — | — |
| key | 键 | string | — | — |
| value | 值 |  string | — | — |

## Form Events
| 事件名称 | 说明 | 回调参数 |
| :---: | :---: | :---: |
| dynamicSubmit | 表单提交 | 别校验通过的form表单数据 |

## example
```vue
<template>
  <div>
    <dynamic-form :form-data="formData" @submit="dynamicSubmit"></dynamic-form>
</div>
</template>
<script>
  export default {
    data(){
      return {
        formData: [{
                      label: '活动名称',
                      key: 'bbb',
                      value: '活动',
                      rules: [
                        {required: true, message: '请输入活动名称', trigger: 'blur'},
                        {min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur'}
                      ],
                      type: 'password'
                    }, {
                      label: 'select',
                      key: 'select',
                      value: '选项1',
                      rules: [
                        {required: true, message: '请输入活动名称', trigger: 'blur'},
                        {min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur'}
                      ],
                      type: 'select',
                      options: [
                        {
                          value: '选项1',
                          label: '黄金糕'
                        }, {
                          value: '选项2',
                          label: '双皮奶'
                        }, {
                          value: '选项3',
                          label: '蚵仔煎'
                        }, {
                          value: '选项4',
                          label: '龙须面'
                        }, {
                          value: '选项5',
                          label: '北京烤鸭'
                        }]
                    }]
      }
    },
    methods:{
      // 动态form表单提交
     dynamicSubmit(data) {
     }
    }
  }
</script>
```
