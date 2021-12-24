<template>
  <div>
  <div>
    <el-button style="margin: 10px 0" @click="add">增加</el-button>
  </div>
    <div style="margin: 10px">
      <el-input v-model="search" placeholder="请输入" style="width: 25%" clearable/>
      <el-button style="margin-left: 5px" @click="load">搜索</el-button>
    </div>
    <el-table :data="tableData"  boder stripe style="width: 100%">
      <el-table-column prop="id" label="ID" sortable/>
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="nickName" label="昵称" />
      <el-table-column prop="age" label="年龄" />
      <el-table-column prop="sex" label="性别" />
      <el-table-column prop="address" label="地址" />
      <el-table-column label="Operations">
        <template #default="scope">
          <el-button size="mini" @click="handleEdit( scope.row)">编辑</el-button>
          <el-popconfirm title="确定要删除吗？" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button size="mini" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div style="margin: 10px 0">
      <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currenPage"
      :page-sizes="[10,15,20]"
      :page-size="pageSize"
      layout="total,sizes,prev,pager,next,jumper"
      :total="total"
      >

      </el-pagination>

      <el-dialog
          v-model="dialogVisible"
          title="Tips"
          width="30%">
       <el-form :model="form" label-width="120px">
         <el-form-item label="用户名">
           <el-input v-model="form.username" style="width: 80%"></el-input>
         </el-form-item>
         <el-form-item label="昵称">
           <el-input v-model="form.nickName" style="width: 80%"></el-input>
         </el-form-item>
         <el-form-item label="年龄">
           <el-input v-model="form.age" style="width: 80%"></el-input>
         </el-form-item>
         <el-form-item label="性别">
           <el-radio v-model="form.sex" label="男">男</el-radio>
           <el-radio v-model="form.sex" label="女">女</el-radio>
         </el-form-item>
         <el-form-item label="地址">
           <el-input type="textarea" v-model="form.address" style="width: 80%"></el-input>
         </el-form-item>
       </el-form>
        <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">确定</el-button>
      </span>
        </template>
      </el-dialog>
    </div>
  </div>
</template>


<script>



import request from "@/utils/request";
import {ElMessage} from "element-plus"
import {nextTick} from "vue";
export default {
  name: 'Home',
  components: {},
  data() {
    return {
      form: {},
      dialogVisible: false,
      search: '',
      currenPage: 1,
      pageSize:10,
      total: 0,
      user:{},
      tableData: []
    }
  },
  created() {
    let userStr= sessionStorage.getItem("user")||"{}"
    this.user= JSON.parse((userStr))
    if(this.user.role===1)
    {
      this.$router.push("/book")
    }
    this.load()
  },
  methods: {
    load(){
      //传入参数要用 {}
      request.get("/user",{
        params:{
        pageNum:this.currenPage,
        pageSize:this.pageSize,
        search:this.search}
      }).then(res=>{

        this.tableData=res.data.records
        this.total=res.data.total
      })
    },
    save() {

      if(this.form.id)
      {
        request.put("/user",this.form).then(res=>{
          if(res.code==='0'){ElMessage({message:"更新成功",type:"success"})}
          else{ElMessage({message:"更新失败",type:"error"})}
        })
      }
      else {
        request.post("/user", this.form).then(res => {
          if(res.code==='0'){ElMessage({message:"添加成功",type:"success"})}
          else{ElMessage({message:"添加失败",type:"error"})}
        })
      }
      this.dialogVisible=false
      this.load()
    },
    add() {
      this.dialogVisible = true
      this.form = {}
    },
    handleEdit(row) {
      //创建一个与表格分离的对象
      this.form=JSON.parse(JSON.stringify(row))
      this.dialogVisible=true
      nextTick(()=>this.load())
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.currenPage=pageNum
      this.load()
    },
    handleDelete(id){
      request.delete("/user/"+id)
      this.load()
    }
  }
}
</script>
