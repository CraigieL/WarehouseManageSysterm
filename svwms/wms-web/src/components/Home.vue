<template>
    <div style="text-align: center;background-color: #f1f1f3;height: 100%;padding: 0px;margin: 0px;">
        <h1 style="font-size: 50px;">{{'ようこそ！'+user.name}}</h1>
        <el-descriptions  title="パーソナルセンター" :column="2" size="40" border>
            <el-descriptions-item>
                <template slot="label">
                  <i class="el-icon-s-custom"></i>
                  アカウント
                </template>
                {{user.account}}
            </el-descriptions-item>
            <el-descriptions-item>
                <template slot="label">
                  <i class="el-icon-mobile-phone"></i>
                  電話
                </template>
                {{user.phone}}
            </el-descriptions-item>
            <el-descriptions-item>
                <template slot="label">
                  <i class="el-icon-location-outline"></i>
                  性別
                </template>
                <el-tag
                    :type="user.sex === '1' ? 'primary' : 'danger'"
                    disable-transitions><i :class="user.sex==1?'el-icon-male':'el-icon-female'"></i>{{user.sex==1?"男性":"女性"}}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item>
                <template slot="label">
                  <i class="el-icon-tickets"></i>
                  ロール
                </template>
                <el-tag
                    type="success"
                    disable-transitions>{{user.roleId==0?"スーパー管理者":(user.roleId==1?"管理者":"用者")}}</el-tag>

            </el-descriptions-item>
        </el-descriptions>

        <DateUtils></DateUtils>
    </div>
</template>

<script>
    import DateUtils from "./DateUtils";
    export default {
        name: "Home",
        components: {DateUtils},
        data() {

            return {
              user:{}
            }
        },
        computed:{

        },
        methods:{
            init(){
              this.user = JSON.parse(sessionStorage.getItem('CurUser'))
            }
        },
        created(){
            this.init()
        }
    }
</script>

<style scoped>
    .el-descriptions{
        width:90%;

        margin: 0 auto;
        text-align: center;
    }
</style>
