<template>
    <div class="layout">
        <!-- 头部 -->
        <layout-header ref="layoutHeader" id="layoutHeader"></layout-header>
        <div class="menu-content" :style="{'height':`${menuContentHeight}px`}">
            <!-- 菜单 -->
            <layout-menu></layout-menu>
            <!-- 主体部分 -->
            <div class="content" :style="{'left':`${menuWidth}px`}">
                <router-view
                        class="container"
                        v-loading="loadingNum>0"
                        element-loading-text="拼命加载中"
                        :style="{'height':`${contentHeight}px`}"></router-view>
                <footer class="layout-footer" ref="layout_footer">
                    <img :src="`/static/images/${theme}-footer-logo.png`">
                    <!--<div class="layout-footer-img"></div>-->
                    <span>上海柯林布瑞信息技术有限公司Copyright@2018-2020</span>
                </footer>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component} from 'vue-property-decorator'
    import {State, Mutation} from 'vuex-class'
    import LayoutHeader from '../header'
    import LayoutMenu from '../menu/index'

    @Component({
        components: {
            LayoutHeader,
            LayoutMenu
        }
    })
    export default class Index extends Vue {
        name: string = 'index';
        @State loadingNum;
        @State menuWidth;
        @State theme; // 主题
        @Mutation changeWindowSize;
        // 除去头部底部的高度
        menuContentHeight: number = 0;
        // 主体高度
        contentHeight: number = 0;

        created() {
            this.$nextTick(() => {
                this.getStyle();
                this.getClientHeight()
            });
        }

        getStyle() {
            const docHeight = document.documentElement.clientHeight || document.body.clientHeight
            const docWidth = document.documentElement.clientWidth || document.body.clientWidth
            const headerHeight = this.$refs['layoutHeader']['$el'].offsetHeight
            this.menuContentHeight = docHeight - headerHeight
            this.contentHeight = docHeight - headerHeight - 36
            this.changeWindowSize(docWidth + docHeight)
        }

        // 获取整个屏幕的高度
        getClientHeight() {
            window.onresize = () => {
                this.getStyle()
            }
        }
    }
</script>

<style scoped lang="less">
    @import "~@/common/css/_variables";

    .layout {
        overflow: hidden;
    }

    .container {
        position: relative;
    }

</style>
