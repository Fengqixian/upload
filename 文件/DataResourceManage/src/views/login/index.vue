<template>
    <div class="login">
        <div class="login-wrapper">
            <header class="header">
                <img src="./images/logo.png" alt="" class="logo">
                <span class="company">上海柯林布瑞信息技术有限公司</span>
            </header>
            <div class="login-body">
                <div class="cbb-bg"></div>
                <div class="login-input-wrapper">
                    <div class="login-input">
                        <h2 class="title">元数据系统</h2>
                        <el-form ref="loginFormRef" :model="loginForm" :rules="loginRules" class="login-form">
                            <el-form-item prop="username">
                                <el-input v-model.trim="loginForm.username" autofocus placeholder="请输入用户名"
                                          @keydown.native.enter="loginSubmit">
                                    <i slot="prefix" class="iconfont icon-username"></i>
                                </el-input>
                            </el-form-item>
                            <el-form-item prop="password">
                                <el-input type="password" v-model.trim="loginForm.password" placeholder="请输入密码"
                                          @keydown.native.enter="loginSubmit">
                                    <i slot="prefix" class="iconfont icon-password"></i>
                                </el-input>
                            </el-form-item>
                        </el-form>
                        <div class="login-form">
                            <button class="login-btn" @click.stop.prevent="loginSubmit">登 陆</button>
                        </div>
                    </div>
                </div>
            </div>
            <footer class="footer">
                <img src="./images/logo_no_color.png" alt="" class="logo" height="20">
                <span class="text">上海柯林布瑞信息技术有限公司Copyright@2018-2020</span>
            </footer>
        </div>
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component, Watch} from 'vue-property-decorator'
    import api from '../../server/api.js'
    import {encryption} from '../../common/util/utils.js'
    import {Mutation, Getter} from 'vuex-class'

    @Component({})
    export default class Index extends Vue {
        name: string = 'Login';
        @Mutation setAccessToken;
        @Mutation setRandomStr;
        @Getter('getRandomStr') randomStr;
        baseUrl: string = api.baseUrl;
        // randomStr: number = 0; // 验证码
        loginForm: any = {
            username: 'admin',
            password: '123456',
            code: ''
        };
        loginRules: object = {
            username: [
                {required: true, message: '请输入用户名', trigger: 'change'}
            ],
            password: [
                {required: true, message: '请输入密码', trigger: 'change'}
            ],
        };
        showTk: any = ''; // 参数


        @Watch('randomStr')
        public randomStrChange(): void {
            this.loginForm.code = '';
        }

        public created(): void {
            this.setRandomStr(Math.random());
            this.$nextTick(() => {
                this.loginSubmit();
            })
        }

        // 登录
        public loginSubmit(): void {
            this.$refs.loginFormRef['validate']((valid) => {
                if (valid) {
                    let {username, password, code} = encryption({
                        data: this.loginForm,
                        key: 'thanks,pig4cloud',
                        param: ['password']
                    });
                    let url = '/auth/oauth/token';
                    const grant_type = 'password';
                    const scope: string = 'server';
                    this.$http({
                        method: 'POST',
                        url,
                        headers: {
                            isToken: false,
                            'Authorization': 'Basic cGlnOnBpZw=='
                        },
                        params: {username, password, code, grant_type, scope, randomStr: this.randomStr}
                    }).then(response => {
                        const {access_token} = response.data;
                        this.setAccessToken(access_token);
                        sessionStorage.setItem('access_token', access_token);
                        sessionStorage.setItem('username', this.loginForm.username);
                        this['$router'].push({path: '/home'})
                    })
                } else {
                    return false;
                }
            });
        }

        // 更改验证码
        public changeCode(): void {
            this.setRandomStr(Math.random());
        }
    }
</script>

<style lang="less">
    .login {
        position: fixed;
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;

        background-color: #0a112d;

        .el-input, .el-select {
            max-width: 100%;
            width: 100%;
        }

        .login-wrapper {
            width: 100%;
            height: 100%;
            background: url("./images/left.png") no-repeat;
            display: flex;
            flex-direction: column;

            .header {
                height: 56px;
                background-color: rgba(7, 14, 41, 0.6);
                display: flex;
                align-items: center;

                .logo {
                    margin: 0 18px;
                }

                .company {
                    font-size: 24px;
                    color: #e6e6e6;
                    font-family: "Microsoft YaHei UI", serif;
                }
            }

            .login-body {
                flex: 1;
                display: flex;

                .cbb-bg {
                    flex: 3;
                    background: url("./images/cbb.png") no-repeat center center;
                }

                .login-input-wrapper {
                    flex: 2;
                    display: flex;
                    align-items: center;
                    justify-content: center;

                    .login-input {
                        min-height: 340px;
                        width: 400px;
                        border-radius: 4px;
                        background-color: rgba(5, 66, 167, 0.1);
                        border: 2px solid rgba(170, 233, 251, 0.1);
                        display: flex;
                        flex-direction: column;
                        align-items: center;

                        .title {
                            margin-top: 30px;
                            font-size: 36px;
                            color: #fff;
                            font-family: "Microsoft YaHei UI", serif;
                            font-weight: bold;
                        }

                        .login-form {
                            width: 350px;

                            .el-form-item {
                                margin: 30px 0;

                                .el-input {
                                    input {
                                        height: 40px !important;
                                        line-height: 40px !important;
                                        background-color: #e6e6e6;
                                        border-radius: 3px;
                                        font-size: 14px;
                                        color: #666666;
                                    }

                                    .el-input__prefix {
                                        line-height: 40px;
                                        left: 11px;
                                    }
                                }
                            }

                            .code-wrapper {
                                .el-form-item__content {
                                    display: flex;

                                    .el-input {
                                        margin-right: 10px;
                                    }
                                }
                            }
                        }
                    }

                    .login-btn {
                        width: 100%;
                        margin-bottom: 30px;
                        height: 40px;
                        line-height: 40px;
                        font-size: 14px;
                        color: #ffffff;
                        border-radius: 4px;
                        background-color: #3278eb;
                        border: 1px solid #1a88b1;
                        cursor: pointer;

                        &:hover {
                            opacity: 0.8;
                        }
                    }
                }
            }

            .footer {
                height: 40px;
                width: 100%;
                background-color: rgba(9, 22, 57, 0.64);
                padding: 0 23px;
                display: flex;
                justify-content: space-between;
                align-items: center;

                .text {
                    font-size: 12px;
                    color: #cccccc;
                }
            }
        }
    }

</style>
