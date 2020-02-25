<template>
    <div class="detail-info">
        <el-row :gutter="20">
            <el-col :xs="24" :md="12" v-for="(item , index) in data" :key="index">
                <div class="item">
                    <span class="label">{{item.label}}：</span>
                    <span class="value">{{item.value}}</span>
                </div>
            </el-col>
        </el-row>
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component, Prop} from 'vue-property-decorator'

    @Component({})
    export default class Index extends Vue {
        name: string = 'DetailInfo';
        @Prop()
        data
    }
</script>

<style scoped lang="less">
    .detail-info {
        padding: 10px;
        background-color: #fff;
        line-height: 3rem;
        border-bottom: 0.1rem solid #ccc;

        .item {
            line-height: 36px;
            color: #2c3e50;
            font-size: 1.2rem;
        }
    }
</style>
