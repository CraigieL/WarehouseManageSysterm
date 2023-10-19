<template>
    <div>
        <div style="margin-bottom: 5px;">
            <el-input v-model="name" placeholder="名前を入力してください" suffix-icon="el-icon-search" style="width: 180px;margin-left: -4px"
                      @keyup.enter.native="loadPost"></el-input>
            <el-select v-model="sex" filterable placeholder="性別を洗濯してください" style="margin-left: 6px;">
                <el-option
                    v-for="item in sexs"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                </el-option>
            </el-select>
            <el-button type="primary" style="margin-left: 18px;margin-right: 6px" @click="loadPost">照会</el-button>
            <el-button type="success" @click="resetParam">リセット</el-button>

        </div>
        <el-table :data="tableData"
                  :header-cell-style="{ background: '#f2f5fc', color: '#555555' }"
                  border
                  highlight-current-row
                  @current-change="selectCurrentChange"
        >
            <el-table-column prop="id" label="ID" width="60">
            </el-table-column>
            <el-table-column prop="account" label="アカウント" width="120">
            </el-table-column>
            <el-table-column prop="name" label="氏名" width="120">
            </el-table-column>
            <el-table-column prop="age" label="年齢" width="60">
              </el-table-column>
            <el-table-column prop="sex" label="性別" width="80">
                <template slot-scope="scope">
                    <el-tag
                        :type="scope.row.sex === 1 ? 'primary' : 'success'"
                        disable-transitions>{{scope.row.sex === 1 ? '男性' : '女性'}}</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="roleId" label="ロール" width="120">
                <template slot-scope="scope">
                    <el-tag
                            :type="scope.row.roleId === 0 ? 'danger' : (scope.row.roleId === 1 ? 'primary' : 'success')"
                            disable-transitions>{{scope.row.roleId === 0 ? 'スーパー管理者' :
                        (scope.row.roleId === 1 ? '管理者' : '用者')}}</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="phone" label="電話">
            </el-table-column>
        </el-table>
        <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="pageNum"
                :page-sizes="[5, 10, 20,30]"
                :page-size="pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="total">
        </el-pagination>

    </div>
</template>

<script>
    export default {
        name: "SelectUser",
        data() {
            return {
                tableData: [],
                pageSize:10,
                pageNum:1,
                total:0,
                name:'',
                sex:'',
                sexs:[
                    {
                        value: '1',
                        label: '男性'
                    }, {
                        value: '0',
                        label: '女性'
                    }
                ],
                centerDialogVisible:false,
                form:{
                    id:'',
                    no:'',
                    name:'',
                    password:'',
                    age:'',
                    phone:'',
                    sex:'0',
                    roleId:'2'
                },
                rules: {
                }
            }
        },
        methods:{
            resetForm() {
                this.$refs.form.resetFields();
            },
            add(){

                this.centerDialogVisible = true
                this.$nextTick(()=>{
                    this.resetForm()
                })

            },
            save(){
                this.$refs.form.validate((valid) => {
                    if (valid) {
                        if(this.form.id){
                            this.doMod();
                        }else{
                            this.doSave();
                        }
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });

            },
            handleSizeChange(val) {
                console.log(`ページごと ${val} 項目`);
                this.pageNum=1
                this.pageSize=val
                this.loadPost()
            },
            handleCurrentChange(val) {
                console.log(`現在のページ: ${val}`);
                this.pageNum=val
                this.loadPost()
            },
            loadGet(){
                this.$axios.get(this.$httpUrl+'/user/list').then(res=>res.data).then(res=>{
                    console.log(res)
                })
            },
            resetParam(){
                this.name=''
                this.sex=''
            },
            selectCurrentChange(val) {
                //this.currentRow = val;
                this.$emit("doSelectUser",val)
            },
            loadPost(){
                this.$axios.post(this.$httpUrl+'/user/listPageC1',{
                    pageSize:this.pageSize,
                    pageNum:this.pageNum,
                    param:{
                        name:this.name,
                        sex:this.sex,
                        roleId:'2'
                    }
                }).then(res=>res.data).then(res=>{
                    console.log(res)
                    if(res.code==200){
                        this.tableData=res.data
                        this.total=res.total
                    }else{
                        alert('データの取得に失敗しました')
                    }

                })
            }
        },
        beforeMount() {
            //this.loadGet();
            this.loadPost()
        }
    }
</script>

<style scoped>

</style>
