<template>
  <div>
    <div style="margin: 5px">
      <el-input v-model="name" placeholder="名前を入力してください" suffix-icon="el-icon-search" style="width: 180px;margin-left: -4px"
                @keyup.enter.native="loadPost"></el-input>
      <el-select v-model="sex" filterable placeholder="性別を選択してください" style="margin-left: 6px;">
        <el-option
            v-for="item in sexs"
            :key="item.value"
            :label="item.label"
            :value="item.value">
        </el-option>
      </el-select>
      <el-button type="primary" style="margin-left: 18px;margin-right: 6px" @click="loadPost">照会</el-button>
      <el-button type="success" @click="resetParam">リセット</el-button>
      <el-button type="primary" style="margin-left: 16px;margin-right: 6px" @click="add">追加</el-button>
    </div>
    <el-table :data="tableData"
              :header-cell-style="{ background: '#f2f5fc', color: '#555555' }"
              border>
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
      <el-table-column prop="phone" label="電話" width="120">
      </el-table-column>
      <el-table-column prop="roleId" label="ロール" width="120">
        <template slot-scope="scope">
          <el-tag
              :type="scope.row.roleId === 0 ? 'danger' : (scope.row.roleId === 1 ? 'primary' : 'success')"
              disable-transitions>{{scope.row.roleId === 0 ? 'スーパー管理者' :
              (scope.row.roleId === 1 ? '管理者' : '用者')}}</el-tag>
        </template>
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
        <el-form-item label="アカウント" prop="account">
          <el-col :span="20">
            <el-input v-model="form.account"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="氏名" prop="name">
          <el-col :span="20">
            <el-input v-model="form.name"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="パスワード" prop="password">
          <el-col :span="20">
            <el-input v-model="form.password"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="年齢" prop="age">
          <el-col :span="20">
            <el-input v-model="form.age"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="性別" prop="sex">
          <el-radio-group v-model="form.sex">
            <el-radio label="1">男性</el-radio>
            <el-radio label="0">女性</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="電話" prop="phone">
          <el-col :span="20">
            <el-input v-model="form.phone"></el-input>
          </el-col>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
      <el-button @click="centerDialogVisible = false">キャンセル</el-button>
      <el-button type="primary" @click="save">確認</el-button>
    </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "UserManage",
  data() {
    let checkAge = (rule, value, callback) => {
      if(value>100){
        callback(new Error('年齢入力が大きすぎます'));
      }else{
        callback();
      }
    }
    let checkDuplicate =(rule,value,callback)=>{
      if(this.form.id){
        return callback();
      }
      this.$axios.get(this.$httpUrl+"/user/findByAccount?account="+this.form.account).then(res=>res.data).then(res=>{
        if(res.code!=200){
          callback()
        }else{
          callback(new Error('アカウントは既に存在しています'));
        }
      })
    }

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
      centerDialogVisible: false,
      form:{
        id:'',
        account:'',
        name:'',
        password:'',
        age:'',
        phone:'',
        sex:'1',
        roleId:'2'
      },
      rules: {
        account: [
          {required: true, message: 'アカウントを入力してください', trigger: 'blur'},
          {min: 3, max: 8, message: '文字数は3から8文字の間である必要があります', trigger: 'blur'},
          {validator:checkDuplicate,trigger: 'blur'}
        ],
        name: [
          {required: true, message: '名前を入力してください', trigger: 'blur'}
        ],
        password: [
          {required: true, message: 'パスワードを入力してください', trigger: 'blur'},
          {min: 3, max: 8, message: '文字数は3から8文字の間である必要があります', trigger: 'blur'}
        ],
        age: [
          {required: true, message: '年齢を入力してください', trigger: 'blur'},
          {min: 1, max: 3, message: '桁数は1から3桁である必要があります', trigger: 'blur'},
          {pattern: /^([1-9][0-9]*){1,3}$/,message: '年齢は正の整数でなければなりません',trigger: "blur"},
          {validator:checkAge,trigger: 'blur'}
        ],
        phone: [
          {required: true,message: "電話番号は空にできません",trigger: "blur"},
          {pattern: /^080\d{8}$/, message: "正しい電話番号を入力してください", trigger: "blur"}
        ]
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

      this.$axios.get(this.$httpUrl+'/user/del?id='+id).then(res=>res.data).then(res=>{
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
        this.form.account = row.account
        this.form.name = row.name
        this.form.password = ''
        this.form.age = row.age +''
        this.form.sex = row.sex +''
        this.form.phone = row.phone
        this.form.roleId = row.roleId
      })
    },
    doMod(){
      this.$axios.post(this.$httpUrl+'/user/update',this.form).then(res=>res.data).then(res=>{
        console.log(res)
        if(res.code==200){
          this.$message({
            message: '編集成功！',
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
      this.$axios.post(this.$httpUrl+'/user/save',this.form).then(res=>res.data).then(res=>{
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
      this.$axios.get(this.$httpUrl+'/user/list').then(res=>res.data).then(res=>{
        console.log(res)
      })
    },
    resetParam(){
      this.name=''
      this.sex=''
      this.loadPost();
    },
    loadPost(){
      this.$axios.post(this.$httpUrl+'/user/listPageC1',{
        pageSize:this.pageSize,
        pageNum:this.pageNum,
        param:{
          name:this.name,
          sex:this.sex,
          roleId: 2
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
  beforeMount(){
    //this.loadGet();
    this.loadPost()
  }
}
</script>

<style scoped>

</style>

