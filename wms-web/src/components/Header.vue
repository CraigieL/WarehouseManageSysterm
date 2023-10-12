<template>
    <div style="display: flex;line-height: 60px">
      <div style="display: flex; align-items: center;">
        <i :class="icon" style="font-size: 26px" @click="collapse"></i>
      </div>
      <div style="flex: 1;text-align: center;font-size: 34px">
        <span>欢迎来到仓库管理系统</span>
      </div>
      <el-dropdown style="line-height: 60px">
          <span>{{user.name}}</span>
          <i class="el-icon-arrow-down" style="margin-left: 15px"></i>
            <el-dropdown-menu slot="dropdown">
                <el-dropdown-item @click.native="toUser">个人中心</el-dropdown-item>
                <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
      </el-dropdown>
    </div>
</template>

<style scoped>

</style>

<script>
    export default {
        name: "Header",
        data(){
            return{
              //直接从sessionStorage调用的CurUser是字符串，转化成Json格式才能调用user.name
              user : JSON.parse(sessionStorage.getItem('CurUser'))
            }
        },
        props:{
            icon:String
        },
        methods:{
            toUser(){
                console.log('to_user')

                this.$router.push("/Home")
            },
            logout(){
                console.log('log_out')

                this.$confirm('您确定要退出登录吗?', '提示', {
                    confirmButtonText: '确定',  //确认按钮的文字显示
                    type: 'warning',
                    center: true, //文字居中显示

                })
                    .then(() => {
                        this.$message({
                          type:'success',
                          message:'退出登录成功'
                        })

                        this.$router.push("/")
                        sessionStorage.clear()
                    })
                    .catch(() => {
                        this.$message({
                          type:'info',
                          message:'已取消退出登录'
                        })
                    })

            },
            collapse(){
                this.$emit('doCollapse')
            }
        },
        created() {
            this.$router.push("/Home")

        }
    }
</script>