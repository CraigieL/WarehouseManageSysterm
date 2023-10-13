<template>
    <div class="loginBody">
        <div class="loginDiv">
            <div class="login-content">
                <h1 class="login-title">ユーザーログイン</h1>
                <el-form :model="loginForm" label-width="100px"
                         :rules="rules" ref="loginForm">
                    <el-form-item label="アカウント" prop="account">
                        <el-input style="width: 180px" type="text" v-model="loginForm.account"
                                  autocomplete="off" size="small"></el-input>
                    </el-form-item>
                    <el-form-item label="パスワード" prop="password">
                        <el-input style="width: 180px" type="password" v-model="loginForm.password"
                                  show-password autocomplete="off" size="small" @keyup.enter.native="confirm"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="confirm" :disabled="confirm_disabled">確 認</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </div>
    </div>
</template>


<script>
    export default {
        name: "Login",
        data(){
            return{
                confirm_disabled:false,
                loginForm:{
                    account:'',
                    password:''
                },
                rules:{
                    account: [
                      { required: true, message: 'アカウントを入力してください', trigger: 'blur' }
                    ],
                    password: [
                      { required: true, message: 'パスワードを入力してください', trigger: 'blur' }
                    ],
                }
            }
        },
        methods:{
            confirm(){
                this.confirm_disabled=true;
                this.$refs.loginForm.validate((valid) => {
                    if (valid) { //valid成功为true，失败为false
                        //去后台验证用户名密码
                        this.$axios.post(this.$httpUrl+'/user/login',this.loginForm).then(res=>res.data).then(res=>{
                            console.log(res)
                            if(res.code==200){
                                //存储
                                //sessionStorage.setItem("CurUser",JSON.stringify(res.data)) UserController修改后，CurUser中的值产生了变化
                                sessionStorage.setItem("CurUser",JSON.stringify(res.data.user))
                                //生成路由
                                console.log(res.data.menu)
                                this.$store.commit("setMenu",res.data.menu)

                                //跳转到主页
                                this.$router.replace('/Index');
                            }else{
                                this.confirm_disabled=false;
                                alert('認証に失敗しました、ユーザー名またはパスワードが間違っています！');
                                return false;
                            }
                        });
                    } else {
                        this.confirm_disabled=false;
                        console.log('認証に失敗');
                        return false;
                    }
                });
            }
        }
    }
</script>

<style scoped>
    .loginBody {
        position: absolute;
        width: 100%;
        height: 100%;
        background-color: #B3C0D1;
        display: flex;
        justify-content: center;
        align-items: center;
    }
    .loginDiv {
        width: 450px;
        height: 330px;
        background: #fff;
        border-radius: 5%;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
    }
    .login-title {
        margin: 20px 0;
        text-align: center;
    }
    .login-content {
        width: 400px;
        height: 250px;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
    }
    el-form, el-form-item {
        text-align: center;
    }
</style>
