<template>
    <div>
        <div style="margin: 5px">
            <el-input v-model="name" placeholder="商品名を入力してください" suffix-icon="el-icon-search" style="width: 180px;margin-left: -4px"
                      @keyup.enter.native="loadPost"></el-input>
            <el-select v-model="storage" placeholder="倉庫を選択してください" style="margin-left: 5px;">
                <el-option
                        v-for="item in storageData"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                </el-option>
            </el-select>
            <el-select v-model="goodstype" placeholder="分類を選択してください" style="margin-left: 5px;">
                <el-option
                        v-for="item in goodstypeData"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                </el-option>
            </el-select>

            <el-button type="primary" style="margin-left: 18px;margin-right: 6px" @click="loadPost">照会</el-button>
            <el-button type="success" @click="resetParam">リセット</el-button>
            <el-button type="primary" style="margin-left: 16px;margin-right: 6px" @click="add" v-if="user.roleId!=2">追加</el-button>

            <el-button type="primary" style="margin-left: 5px;" @click="inGoods" v-if="user.roleId!=2">入庫</el-button>
            <el-button type="primary" style="margin-left: 5px;" @click="outGoods" v-if="user.roleId!=2">出庫</el-button>

        </div>
        <el-table :data="tableData"
                :header-cell-style="{ background: '#f2f5fc', color: '#555555' }"
                border
                highlight-current-row
                @current-change="selectCurrentChange"
        >
        <el-table-column prop="id" label="ID" width="60">
        </el-table-column>
        <el-table-column prop="name" label="商品名" width="120">
        </el-table-column>
        <el-table-column prop="storage" label="倉庫" width="120" :formatter="formatStorage">
        </el-table-column>
        <el-table-column prop="goodstype" label="分類" width="120" :formatter="formatGoodstype">
        </el-table-column>
        <el-table-column prop="count" label="数量" width="120">
        </el-table-column>
        <el-table-column prop="remark" label="備考" >
        </el-table-column>
        <el-table-column prop="operate" label="操作" v-if="user.roleId!=2">
            <template slot-scope="scope">
                <el-button size="small" type="success" @click="modify(scope.row) ">編集</el-button>
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
            title="商品のメンテナンス"
            :visible.sync="centerDialogVisible"
            width="30%"
            center>

            <el-form ref="form" :rules="rules" :model="form" label-width="80px">
                <el-form-item label="商品名" prop="name">
                    <el-col :span="20">
                        <el-input v-model="form.name"></el-input>
                    </el-col>
                </el-form-item>
                <el-form-item label="倉庫" prop="storage">
                    <el-col :span="20">
                        <el-select v-model="form.storage" placeholder="倉庫を選択してください" style="margin-left: 5px;">
                            <el-option
                                    v-for="item in storageData"
                                    :key="item.id"
                                    :label="item.name"
                                    :value="item.id">
                            </el-option>
                        </el-select>
                    </el-col>
                </el-form-item>
                <el-form-item label="分类" prop="goodstype">
                    <el-col :span="20">
                        <el-select v-model="form.goodstype" placeholder="分類を選択してください" style="margin-left: 5px;">
                            <el-option
                                    v-for="item in goodstypeData"
                                    :key="item.id"
                                    :label="item.name"
                                    :value="item.id">
                            </el-option>
                        </el-select>
                    </el-col>
                </el-form-item>
                <el-form-item label="数量" prop="count">
                    <el-col :span="20">
                        <el-input v-model="form.count"></el-input>
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


        <el-dialog
                title="入出庫"
                :visible.sync="inDialogVisible"
                width="30%"
                center>

            <el-dialog
                    width="75%"
                    title="用者選択"
                    :visible.sync="innerVisible"
                    append-to-body>
                <SelectUser @doSelectUser="doSelectUser"></SelectUser>
                <span slot="footer" class="dialog-footer">
                    <el-button @click="innerVisible = false">キャンセル</el-button>
                    <el-button type="primary" @click="confirmUser">確定</el-button>
                </span>
            </el-dialog>

            <el-form ref="form1" :rules="rules1" :model="form1" label-width="80px">
                <el-form-item label="商品名">
                    <el-col :span="20">
                        <el-input v-model="form1.goodsname" readonly></el-input>
                    </el-col>
                </el-form-item>
                <el-form-item label="申請者">
                    <el-col :span="20">
                        <el-input v-model="form1.username" readonly @click.native="selectUser"></el-input>
                    </el-col>
                </el-form-item>
                <el-form-item label="数量" prop="count">
                    <el-col :span="20">
                        <el-input v-model="form1.count"></el-input>
                    </el-col>
                </el-form-item>
                <el-form-item label="備考" prop="remark">
                    <el-col :span="20">
                        <el-input type="textarea" v-model="form1.remark"></el-input>
                    </el-col>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="inDialogVisible = false">キャンセル</el-button>
                <el-button type="primary" @click="doInGoods">確定</el-button>
            </span>
        </el-dialog>

    </div>
</template>

<script>
    import SelectUser from "@/components/user/SelectUser.vue";
    export default {
        name: "GoodsManage",
      components: {SelectUser},
            data() {
                let checkCount = (rule, value, callback) => {
                    if(value>9999){
                        callback(new Error('数量の入力が多きすぎます'));
                    }else{
                        callback();
                }
                };

                return {
                    user : JSON.parse(sessionStorage.getItem('CurUser')),
                    storageData:[],
                    goodstypeData:[],
                    tableData: [],
                    pageSize:10,
                    pageNum:1,
                    total:0,
                    name:'',
                    storage:'',
                    goodstype:'',
                    centerDialogVisible: false,
                    inDialogVisible:false,
                    innerVisible:false,
                    currentRow:{},
                    tempUser:{},
                    form:{
                        id:'',
                        name:'',
                        storage:'',
                        goodstype:'',
                        count:'',
                        remark:''
                    },
                    form1:{
                        goods:'',
                        goodsname:'',
                        count:'',
                        username:'',
                        userid:'',
                        adminId:'',
                        remark:'',
                        action:'1'
                    },
                    rules1:{

                    },
                    rules: {
                        name: [
                            {required: true, message: '商品名を入力してください', trigger: 'blur'}
                        ],
                        storage:[
                            {required: true, message: '倉庫を選択してください', trigger: 'blur'}
                        ],
                        goodstype:[
                            {required: true, message: 'カテゴリを選択してください', trigger: 'blur'}
                        ],
                        count: [
                            {required: true, message: '数量を入力してください', trigger: 'blur'},
                            {pattern: /^([1-9][0-9]*){1,4}$/,message: '数量は正の整数でなければなりません',trigger: "blur"},
                            {validator:checkCount,trigger: 'blur'}
                        ],
                    }
                }
            },
        methods:{
            doSelectUser(val){
                console.log(val)
                this.tempUser = val
            },
            confirmUser(){
                this.form1.username = this.tempUser.name
                this.form1.userid = this.tempUser.id

                this.innerVisible = false
            },
            selectCurrentChange(val) {
                this.currentRow = val;
            },
            formatStorage(row){
                let temp =  this.storageData.find(item=>{
                    return item.id == row.storage
                })

                return temp && temp.name
            },
            formatGoodstype(row){
                let temp =  this.goodstypeData.find(item=>{
                    return item.id == row.goodstype
                })

                return temp && temp.name
            },
            resetForm() {
                this.$refs.form.resetFields();
            },
            resetInForm() {
              this.$refs.form1.resetFields();
            },
            del(id){
                console.log(id)

                this.$axios.get(this.$httpUrl+'/goods/del?id='+id).then(res=>res.data).then(res=>{
                    console.log(res)
                    if(res.code==200){

                        this.$message({
                            message: '編集成功!',
                            type: 'success'
                        });
                        this.loadPost()
                    }else{
                        this.$message({
                            message: '編集失敗！',
                            type: 'error'
                        });
                    }

                })
            },
            add(){
                this.centerDialogVisible = true
                this.$nextTick(()=>{
                    this.resetForm()

                    this.form.id = ''
                })
            },
            inGoods(){
                if(!this.currentRow.id){
                    alert('レコードを選択してください');
                    return;
                }
                this.inDialogVisible = true
                this.$nextTick(()=>{
                    this.resetInForm()
                })

                this.form1.goodsname = this.currentRow.name
                this.form1.goods = this.currentRow.id
                this.form1.adminId=this.user.id
                this.form1.action='1'
            },

            outGoods(){
                if(!this.currentRow.id){
                    alert('レコードを選択してください');
                    return;
                }
                this.inDialogVisible = true
                this.$nextTick(()=>{
                    this.resetInForm()
                })

                this.form1.goodsname = this.currentRow.name
                this.form1.goods = this.currentRow.id
                this.form1.adminId=this.user.id
                this.form1.action='2'

            },
            selectUser(){
                this.innerVisible=true
            },
            modify(row){
              console.log(row)

              this.centerDialogVisible = true //先打开对话框，表单中的值为form的默认值，后赋值
              this.$nextTick(()=>{
                  //赋值到表单
                  this.form.id = row.id
                  this.form.name = row.name
                  this.form.storage = row.storage
                  this.form.goodstype = row.goodstype
                  this.form.count = row.count
                  this.form.remark = row.remark
              })
            },
            doMod(){
                this.$axios.post(this.$httpUrl+'/goods/update',this.form).then(res=>res.data).then(res=>{
                    console.log(res)
                    if(res.code==200){
                        this.$message({
                            message: '编辑成功！',
                            type: 'success'
                        });
                        this.centerDialogVisible = false
                        this.loadPost()
                        this.resetForm()
                    }else{
                        this.$message({
                            message: '编辑失败！',
                            type: 'error'
                        });
                    }
                })
            },
            doSave(){
                this.$axios.post(this.$httpUrl+'/goods/save',this.form).then(res=>res.data).then(res=>{
                    console.log(res)
                    if(res.code==200){
                        this.$message({
                            message: '追加が成功しました！',
                            type: 'success'
                        });
                        this.centerDialogVisible = false
                        this.loadPost()
                        this.resetForm()
                    }else{
                        this.$message({
                            message: '追加が失敗しました！',
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
            doInGoods(){
                this.$axios.post(this.$httpUrl+'/record/save',this.form1).then(res=>res.data).then(res=>{
                    console.log(res)
                    if(res.code==200){

                        this.$message({
                            message: '編集成功!',
                            type: 'success'
                        });
                        this.inDialogVisible = false
                        this.loadPost()
                        this. resetInForm()
                    }else{
                        this.$message({
                            message: '編集失敗！',
                            type: 'error'
                        });
                    }

                })
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
                this.$axios.get(this.$httpUrl+'/goods/list').then(res=>res.data).then(res=>{
                  console.log(res)
                })
            },
            resetParam(){
                this.name=''
                this.storage=''
                this.goodstype=''
            },
            loadPost(){
                this.$axios.post(this.$httpUrl+'/goods/listPage',{
                    pageSize:this.pageSize,
                    pageNum:this.pageNum,
                    param:{
                        name:this.name,
                        goodstype:this.goodstype+'',
                        storage:this.storage+''
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
            },
            loadStorage(){
                this.$axios.get(this.$httpUrl+'/storage/list').then(res=>res.data).then(res=>{
                    console.log(res)
                    if(res.code==200){
                        this.storageData=res.data
                    }else{
                        alert('データの取得に失敗しました')
                    }

                })
            },
            loadGoodstype(){
                this.$axios.get(this.$httpUrl+'/goodstype/list').then(res=>res.data).then(res=>{
                    console.log(res)
                    if(res.code==200){
                        this.goodstypeData=res.data
                    }else{
                        alert('データの取得に失敗しました')
                    }

                })
            }
        },
        beforeMount(){
            //this.loadGet();
            this.loadStorage()
            this.loadGoodstype()
            this.loadPost()
        }
}
</script>

<style scoped>

</style>
