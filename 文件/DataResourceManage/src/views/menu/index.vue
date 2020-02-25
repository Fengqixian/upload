<template>
    <div class="layout-menu" :style="{'width':`${menuWidth}px`}">
        <div class="btn">
            <!--<i class="iconfont icon-shouqi" v-show="isCollapse" @click="isCollapse=false"></i>-->
            <i class="iconfont icon-zhankai" v-show="isCollapse" @click="isCollapseBtn(false)"></i>
            <i class="iconfont icon-shouqi" v-show="!isCollapse" @click="isCollapseBtn(true)"></i>
            <!--<span  v-show="!isCollapse">全部菜单</span>-->
        </div>
        <el-menu
                :default-active="defaultActive"
                class="el-menu-vertical-demo"
                @open="handleOpen"
                @close="handleClose"
                :collapse="isCollapse">
            <!-- 没有子菜单 -->
            <template v-for="menu in menuList">
                <el-menu-item v-if="!menu.hasChild" :index="menu.id.toString()"
                              @click="routerPush(menu.routerPath)">
                    <i :class="menu.icon"></i>
                    <span slot="title">{{menu.title}}</span>
                </el-menu-item>
                <el-submenu v-else :index="menu.id.toString()">
                    <template slot="title">
                        <i :class="menu.icon"></i>
                        <span slot="title">{{menu.title}}</span>
                    </template>
                    <el-menu-item v-for="item in menu.subMenus" :key="item.id" :index="item.id.toString()"
                                  @click="routerPush(item.routerPath)"
                                  v-if="!item.NMenu">
                        <i :class="item.icon"></i>
                        <span slot="title">{{item.title}}</span>
                    </el-menu-item>
                </el-submenu>
            </template>
        </el-menu>
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component} from 'vue-property-decorator'
    import {State, Mutation, Action} from 'vuex-class'
    import config from "../../config";

    @Component({})
    export default class LayoutMenu extends Vue {
        name: string = 'LayoutMenu';
        @Mutation setBaseField;
        @Action getBaseFiledOptions;
        @State menuWidth;  // 菜单的宽度
        @Mutation upDateMenuWidth;  // 设置菜单的宽度
        // 控制菜单收起展开
        isCollapse: boolean = true;
        // 菜单列表
        menuList = [];

        created() {
            this.getMenu();
            this.getBaseFiledOptions('status');
            this.isCollapse = this.menuWidth < 100 ? true : false;
        }

        // 计算属性获取菜单激活
        get defaultActive() {
            const fullPath = this.$route.fullPath;
            let defaultActive = '';
            this.menuList.forEach(menu => {
                if (menu['routerPath'] === fullPath) {
                    defaultActive = menu['id'].toString()
                }else if (menu['subMenus'] && menu['subMenus'].length) {
                    menu['subMenus'].forEach(sub => {
                        if (sub['routerPath'] === fullPath) {
                            if(!sub['NMenu']){
                                defaultActive = sub['id'].toString()
                            }else {
                                defaultActive = menu['id'].toString()
                            }
                        }
                        sub['subMenus'].forEach(item => {
                            if (item['routerPath'] === fullPath) {
                                if(!item['NMenu']){
                                    defaultActive = item['id'].toString()
                                }else {
                                    defaultActive = sub['id'].toString()
                                }
                            }
                        })
                    })
                }
            });
            // 根据URL获取激活菜单，如果是path = /  激活首个菜单
            return defaultActive || this.menuList[0]['id'].toString()
        }

        // 获取菜单
        getMenu() {
            this.menuList = JSON.parse(sessionStorage.getItem('menuList') || '[]')
        }

        // 路由跳转
        routerPush(routerPush) {

            this['$router'].push({path: routerPush})
        }

        handleOpen(key, keyPath) {
        }

        handleClose(key, keyPath) {
        }

        // 点击菜单展开按钮
        isCollapseBtn(flag) {
            this.isCollapse = flag
            if (!flag) {
                this.upDateMenuWidth(200)
            } else {
                this.upDateMenuWidth(64)
            }
        }

    }
</script>

<style scoped lang="less">
    .layout-menu {
        .btn {
            height: 40px;
        }
    }

</style>
