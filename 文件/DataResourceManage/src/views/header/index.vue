<template>
    <div class="layout-header">
        <div class="logo">
            <img class="company" :src="`/static/images/defaultTheme-header-logo.png`" height="22px">
            <i class="platform">健康医疗大数据统一工作平台&nbsp;&nbsp;数据资产</i>
        </div>
        <!--<div class="user">-->
        <!--<el-switch-->
        <!--v-model.trim="checkTheme"-->
        <!--active-color="#0e183f"-->
        <!--inactive-color="#3366cc"-->
        <!--active-value="blueTheme"-->
        <!--inactive-value="defaultTheme"-->
        <!--inactive-text="切换主题"-->
        <!--@change="changeTheme"-->
        <!--style="margin-right: 15px;">-->
        <!--</el-switch>-->
        <!--<el-dropdown>-->
        <!--<span>{{userName}} <i class="el-icon-caret-bottom"></i></span>-->
        <!--<el-dropdown-menu slot="dropdown" class="layout-header-dropdown">-->
        <!--<el-dropdown-item @click.native.prevent.stop="dropdownMenuClick('exit')">退出</el-dropdown-item>-->
        <!--</el-dropdown-menu>-->
        <!--</el-dropdown>-->
        <!--</div>-->
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component} from 'vue-property-decorator'
    import {State, Mutation} from 'vuex-class'

    @Component({})
    export default class Index extends Vue {
        name: string = 'index';
        checkTheme: string = 'defaultTheme';
        @State theme; // 主题
        @Mutation setTheme; // 修改主题

        public mounted(): void {
            let storageTheme = localStorage.getItem('theme');
            if (storageTheme) {
                this.checkTheme = storageTheme;
            } else {
                localStorage.setItem('theme', 'defaultTheme');
                this.checkTheme = 'defaultTheme';
            }
            this.setTheme(this.checkTheme)
        }

        /**
         * 切换主题
         * @param theme 新状态的值
         */
        public changeTheme(theme): void {
            localStorage.setItem('theme', theme);
            this.setTheme(theme);
        }

        get userName() {
            return sessionStorage.getItem('username')
        }

        public dropdownMenuClick(params): void {
            if (params === 'exit') {
                this['$router'].push({path: '/login'})
            }
        }
    }
</script>
