<template>
    <transition name="fade">
        <div class="cb-slide-side-info" v-if="isShow" @click.stop.prevent="">
            <div class="title">{{title}}</div>
            <slot></slot>
        </div>
    </transition>
</template>

<script>
    export default {
        name: "CbSideInfo",
        model: {
            prop: 'isShow',
            event: 'change'
        },
        props: {
            isShow: Boolean,
            title: String
        },
        mounted() {
            this.listenKeyword();
        },
        methods: {
            listenKeyword() {
                window.addEventListener('click', () => {
                    this.$emit('change', false)
                })
            }
        }
    }
</script>

<style lang="less">
    .cb-slide-side-info {
        position: fixed;
        top: 81px;
        right: 0;
        bottom: 36px;
        width: 250px;
        z-index: 100;
        padding: 10px;


        &.fade-enter-active, &.fade-leave-active {
            transition: all 0.5s;
        }

        &.fade-enter, &.fade-leave-to {
            transform: translateX(100%);
        }

        .title {
            text-align: center;
            font-size: 2rem;
            font-weight: 600;
            margin-bottom: 20px;
        }

        li {
            margin-bottom: 10px;
            display: flex;

            span {
                display: flex;
                align-items: center;
            }

            .label {
                width: 73px;
                margin-right: 2px;
            }

            .value {
                flex: 1;
            }
        }
    }
</style>
