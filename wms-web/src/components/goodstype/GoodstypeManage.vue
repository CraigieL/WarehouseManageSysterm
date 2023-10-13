<template>
    <div>
        <div style="margin: 5px">
            <el-input v-model="name" placeholder="カテゴリ名を入力してください" suffix-icon="el-icon-search" style="width: 180px;margin-left: -4px"
                      @keyup.enter.native="loadPost"></el-input>
            <el-button type="primary" style="margin-left: 18px;margin-right: 6px" @click="loadPost">照会</el-button>
            <el-button type="success" @click="resetParam">リセット</el-button>
            <el-button type="primary" style="margin-left: 16px;margin-right: 6px" @click="add">追加</el-button>
        </div>
        <el-table :data="tableData"
                :header-cell-style="{ background: '#f2f5fc', color: '#555555' }"
                border>
        <el-table-column prop="id" label="ID" width="60">
        </el-table-column>
        <el-table-column prop="name" label="カテゴリ名" width="120">
        </el-table-column>
        <el-table-column prop="remark" label="備考" width="300">
        </el-table-column>
        <el-table-column prop="operate" label="操作">
            <template slot-scope="scope">
                <el-button size="small" type="success" @click="modify(scope.row)">編集</el-button>
                <el-popconfirm
                    title="削除してもよろしいですか？"
                    @confirm="del(scope.row.id)"
                    style="margin-left: 10px;"
                >
                <el-button slot="reference" size="small" type="danger">削除</el-button>
                </el-popconfirm>
            </template>
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

        <el-dialog
            title="ヒント"
            :visible.sync="centerDialogVisible"
            width="32%"
            center>

            <el-form ref="form" :rules="rules" :model="form" label-width="100px">
                <el-form-item label="カテゴリ名" prop="name">
                    <el-col :span="20">
                        <el-input v-model="form.name"></el-input>
                    </el-col>
                </el-form-item>
                <el-form-item label="備考" prop="remark">
                    <el-col :span="20">
                        <el-input type="textarea" v-model="form.remark"></el-input>
                    </el-col>
                </el-form-item>

            </el-form>
            <span slot="footer" class="dialog-footer">
            <el-button @click="centerDialogVisible = false">キャンセル</el-button>
            <el-button type="primary" @click="save">確定</el-button>
        </span>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "GoodstypeManage",
            data() {
                return {
                    tableData: [],
                    pageSize:10,
                    pageNum:1,
                    total:0,
                    name:'',
                    centerDialogVisible: false,
                    form:{
                        id:'',
                        name:'',
                        remark:''
                    },
                    rules: {
                        name: [
                            {required: true, message: 'カテゴリ名を入力してください', trigger: 'blur'}
                        ],
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

                    this.form.id = ''
                })
            },
            del(id){
                console.log(id)

                this.$axios.get(this.$httpUrl+'/goodstype/del?id='+id).then(res=>res.data).then(res=>{
                    console.log(res)
                    if(res.code==200){
                        this.$message({
                            message: '削除成功！',
                            type: 'success'
                        });
                        this.loadPost()
                    }else{
                        this.$message({
                            message: '削除失敗！',
                            type: 'error'
                        });
                    }
                })
            },

            modify(row){
              console.log(row)

              this.centerDialogVisible = true //先打开对话框，表单中的值为form的默认值，后赋值
              this.$nextTick(()=>{
                  //赋值到表单
                  this.form.id = row.id
                  this.form.name = row.name
                  this.form.remark = row.remark
              })
            },
            doMod(){
                this.$axios.post(this.$httpUrl+'/goodstype/update',this.form).then(res=>res.data).then(res=>{
                    console.log(res)
                    if(res.code==200){
                        this.$message({
                            message: '編集成功!',
                            type: 'success'
                        });
                        this.centerDialogVisible = false
                        this.loadPost()
                        this.resetForm()
                    }else{
                        this.$message({
                            message: '編集失敗！',
                            type: 'error'
                        });
                    }
                })
            },
            doSave(){
                this.$axios.post(this.$httpUrl+'/goodstype/save',this.form).then(res=>res.data).then(res=>{
                    console.log(res)
                    if(res.code==200){
                        this.$message({
                            message: '追加成功！',
                            type: 'success'
                        });
                        this.centerDialogVisible = false
                        this.loadPost()
                        this.resetForm()
                    }else{
                        this.$message({
                            message: '追加失敗！',
                            type: 'error'
                        });
                    }
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
                this.$axios.get(this.$httpUrl+'/goodstype/list').then(res=>res.data).then(res=>{
                  console.log(res)
                })
            },
            resetParam(){
                this.name=''
            },
            loadPost(){
                this.$axios.post(this.$httpUrl+'/goodstype/listPage',{
                    pageSize:this.pageSize,
                    pageNum:this.pageNum,
                    param:{
                        name:this.name,
                    }
                }).then(res=>res.data).then(res=>{
                    console.log(res)
                    if(res.code==200){
                        this.tableData=res.data
                        this.total=res.total
                    }else{
                        alert('データの取得に失敗しまし')
                    }
                })
            }
        },
        beforeMount(){
            //this.loadGet();
            this.loadPost()
        }
}
</script>

<style scoped>

</style>
