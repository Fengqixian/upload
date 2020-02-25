<template>
    <div class="cb-operation-container">
        <div class="cb-top-operation">
            <slot name="toOperation" class="cb-top-operation"></slot>
        </div>
        <div class="cb-container">
            <slot name="cbContainer"></slot>
        </div>
    </div>
</template>

<script>
    export default {
        name: "CbOperationContainer",

        mounted() {
            // this.initStyle();
            setTimeout(() => {
                // this.initStyle();
            }, 20)
        },


        methods: {
            initStyle() {
                const cbOperationContainer = document.querySelector('.cb-operation-container');
                const cbTopOperation = cbOperationContainer.querySelector('.cb-top-operation');
                const cbTopOperationHeight = cbTopOperation.offsetHeight;
                const cbContainer = cbOperationContainer.querySelector('.cb-container');
                cbContainer.style.setProperty('top', `${cbTopOperationHeight}px`)
            }
        }
    }
</script>

<style scoped lang="less">
    .cb-operation-container {
        position: relative;
        width: 100%;
        height: 100%;
        overflow: hidden;
        display: flex;
        flex-direction: column;

        // 头部操作
        .cb-top-operation {
            padding: 10px;
            box-shadow: 0 1px 0 #ccc;
            background-color: #fff;
            z-index: 10;
        }

        // 底部操作
        .cb-container {
            /*position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;*/
            position: relative;
            flex: 1;
            overflow: auto;
        }
    }
</style>