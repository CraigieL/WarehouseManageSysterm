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
        </div>
        <el-table :data="tableData"
                :header-cell-style="{ background: '#f2f5fc', color: '#555555' }"
                border>
            <el-table-column prop="id" label="ID" width="60">
            </el-table-column>
            <el-table-column prop="goodsname" label="商品名" width="80">
            </el-table-column>
            <el-table-column prop="storagename" label="倉庫" width="80">
            </el-table-column>
            <el-table-column prop="goodstypename" label="分類" width="80">
            </el-table-column>
            <el-table-column prop="adminname" label="操作者" width="160">
            </el-table-column>
            <el-table-column prop="username" label="申請者" width="120">
            </el-table-column>
            <el-table-column prop="count" label="数量" width="80">
            </el-table-column>
            <el-table-column prop="createtime" label="操作時間" width="180">
            </el-table-column>
            <el-table-column prop="remark" label="備考" >
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
        name: "RecordManage",
        data() {

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
                form:{
                    id:'',
                    name:'',
                    storage:'',
                    goodstype:'',
                    count:'',
                    remark:''
                },
            }
        },

        methods:{
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
            resetParam(){
                this.name=''
                this.storage=''
                this.goodstype=''
            },
            loadPost(){
                this.$axios.post(this.$httpUrl+'/record/listPage',{
                    pageSize:this.pageSize,
                    pageNum:this.pageNum,
                    param:{
                        name:this.name,
                        goodstype:this.goodstype+'',
                        storage:this.storage+'',
                        roleId:this.user.roleId+'',
                        userId:this.user.id+''
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
            this.loadStorage()
            this.loadGoodstype()
            this.loadPost()
        }
    }


</script>

<style scoped>

</style>
