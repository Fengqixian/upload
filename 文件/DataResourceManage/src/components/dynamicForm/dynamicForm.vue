<template>
  <div class="dynamic-form" :form1="form1">
    <el-form ref="ruleForm" :model="form" label-width="110px" :rules="rules" v-show="formData.length">
      <el-row class="info-container">
        <el-col :span="6" v-for="(item, index) in formData" :key="index" class="item-wrapper">
          <el-form-item :label="item.label" :prop="item.key">
            <el-select
              v-model.trim="form[item.key]"
              :placeholder="item.placeholder"
              v-if="item.type === 'select'">
              <el-option
                v-for="optVal in item.options"
                :key="optVal.value"
                :label="optVal.label"
                :value="optVal.value">
              </el-option>
            </el-select>
            <el-date-picker
              v-model.trim="form[item.key]"
              type="date"
              :placeholder="item.placeholder"
              v-else-if="item.type === 'date'"
              value-format="yyyy-MM-dd">
            </el-date-picker>
            <el-date-picker
              v-model.trim="form[item.key]"
              type="datetime"
              :placeholder="item.placeholder"
              v-else-if="item.type === 'datetime'"
              value-format="yyyy-MM-dd HH:mm:ss">
            </el-date-picker>
            <el-input
              v-model.trim.number="form[item.key]"
              :placeholder="item.placeholder"
              type="text"
              :disabled="item.disabled"
              v-else-if="item.type === 'number'">
            </el-input>
            <el-input
              v-model.trim="form[item.key]"
              :placeholder="item.placeholder"
              :type="item.type?item.type:'text'"
              :disabled="item.disabled"
              v-else>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item v-if="type !== null && type !== undefined">
        <div style="float: right;">
          <el-button type="primary" @click="submitForm('ruleForm')">{{type === 'create'?'立即创建':'立即修改'}}</el-button>
          <el-button @click="resetForm('ruleForm')">重置</el-button>
        </div>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
  export default {
    name: 'DynamicForm',
    data() {
      return {
        rules: {},  // 规则
        form: {}, // form 表单数据
      }
    },
    props: {
      // form表单配置+数据
      formData: {
        type: Array,
        default: []
      },
      otherData: null, // 其他数据
      type: null,  // 判断button.submit展示立即创建create/立即修改edit
    },
    computed: {
      form1() {  // 计算form属性，将formData解析为form表单需要的格式
        let ret = {}
        this.formData.forEach(item => {
          ret[item.key] = item.value
          this.$set(this.form, item.key, item.value)
          this.$set(this.rules, item.key, item.rules)
        })
        return ret;
      }
    },
    methods: {
      submit() {
        const ret = {
          formData: this.form,
          otherData: this.otherData,
          type: this.type
        };
        this.$emit('submit', ret)
      },

      // 表单提交
      submitForm(formName) {
        const ref = this.$refs['ruleForm']
        ref.validate((valid) => {
          if (valid) {
            this.submit()
          } else {
            return false;
          }
        });
      },

      // 重置
      resetForm(formName) {
        const ref = this.$refs['ruleForm']
        ref.resetFields()
      }
    }

  }
</script>
<style scoped lang="less">

</style>


