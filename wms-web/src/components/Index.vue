<template>
    <el-container style="height: 100%; border: 1px solid #eee">
        <el-aside :width="aside_width" style="height: 100vh; background-color: rgb(238, 241, 246);margin-left: -1px;margin-top: -1px">
            <Aside :isCollapse="isCollapse"></Aside>
        </el-aside>

        <el-container style="height: 100%">
            <el-header style="text-align: right; font-size: 12px; height: 100%;border-bottom: rgba(169,169,169,0.3)  1px solid">
                <Header @doCollapse="doCollapse" :icon="icon"></Header>
            </el-header>

            <el-main style="height: 100%">
                <!--<Main></Main>-->
                <router-view/>
            </el-main>
        </el-container>
    </el-container>
</template>

<style scoped>
    .el-header {
      //background-color: #B3C0D1;
      color: #333;
      line-height: 60px;
    }

    .el-aside {
      color: #333;
    }
    .el-main {
      padding: 5px;
    }
</style>

<script>
    import Aside from "@/components/Aside.vue";
    import Header from "@/components/Header.vue";

    export default {
      name: "Index",
      components: {Header, Aside},
      data(){
        return{
            isCollapse:false,
            aside_width:'200px',
            icon:'el-icon-s-fold'
        }
      },
      methods:{
          doCollapse(){
            console.log(1111)

            this.isCollapse = !this.isCollapse
            if(!this.isCollapse){//展开，isCollapse为false，执行这条语句
                this.aside_width='200px'
                this.icon='el-icon-s-fold'
            }else{//折叠，isCollapse为true，执行这条语句
                this.aside_width='64px'
                this.icon='el-icon-s-unfold'
            }
          }
      }
    };
</script>