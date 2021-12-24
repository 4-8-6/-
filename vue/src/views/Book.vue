<template>
  <div>
    <div>
      <el-button style="margin: 10px 0" @click="add" v-if="user.role===2">增加</el-button>
    </div>
    <div style="margin: 10px">
      <el-input v-model="search" placeholder="请输入" style="width: 25%" clearable/>
      <el-button style="margin-left: 5px" @click="load">搜索</el-button>
    </div>
    <el-table :data="tableData"  boder stripe style="width: 100%">
      <el-table-column prop="id" label="ID" sortable/>
      <el-table-column prop="name" label="名称" />
      <el-table-column prop="price" label="单价" />
      <el-table-column prop="author" label="作者" />
      <el-table-column prop="createTime" label="出版日期" />
      <el-table-column prop="address" label="地址" />
      <el-table-column label="封面">
        <template #default="scope">
          <el-image
              style="width: 100px; height: 100px"
              :src="scope.row.cover"
              :preview-src-list="[scope.row.cover]">
          </el-image>
        </template>
      </el-table-column>
        <el-table-column label="Operations">
        <template #default="scope">
          <el-button size="mini" @click="additem(scope.row)" v-if="user.role===1">加入购物车</el-button>
          <el-button size="mini" @click="handleEdit( scope.row)" v-if="user.role===2">编辑</el-button>
          <el-popconfirm title="确定要删除吗？" @confirm="handleDelete(scope.row.id)" v-if="user.role===2">
            <template #reference>
              <el-button size="mini" type="danger" >删除</el-button>
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
          <el-form-item label="名称">
            <el-input v-model="form.name" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="价格">
            <el-input v-model="form.price" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="作者">
            <el-input v-model="form.author" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="出版时间">
            <el-date-picker v-model="form.createTime" value-format="YYYY-MM-DD" type="date" style="width: 80%" clearable></el-date-picker>
          </el-form-item>
          <el-form-item label="封面">
            <el-upload ref="upload" :action="filesUploadUrl" :on-success="uploadsuccess" :on-error="uploadfail">
              <el-button size="small" type="primary">Click to upload</el-button>
            </el-upload>
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
export default {
  name: 'Book',
  components: {},
  data() {
    return {
      form: {},
      dialogVisible: false,
      user:{},
      search: '',
      currenPage: 1,
      pageSize:10,
      total: 0,
      tableData: [],
      filesUploadUrl:   "http://localhost:9090/api/files/upload",
    }
  },
  created() {
    let userStr = sessionStorage.getItem("user")||"{}"
    this.user= JSON.parse((userStr))
    request.get("/user/"+this.user.id).then(res=>{
      if(res.code===0)
      {
        this.user=res.data
      }
    })

    this.load()
  },
  methods: {
    uploadfail(res){
      this.$message({
        type: "error",
        message: "上传失败，图片过大"
      })
    },
    uploadsuccess(res){
      console.log(res)
      this.form.cover=res.data
    },
    load(){
      //传入参数要用 {}
      request.get("/book",{
        params:{
          pageNum:this.currenPage,
          pageSize:this.pageSize,
          search:this.search}
      }).then(res=>{
        console.log(res)
        this.tableData=res.data.records
        this.total=res.data.total
      })
    },
    save() {
      if (this.form.id) {  // 更新
        request.put("/book", this.form).then(res => {
          console.log(res)
          if (res.code === '0') {
            this.$message({
              type: "success",
              message: "更新成功"
            })
          } else {
            this.$message({
              type: "error",
              message: res.msg
            })
          }
          this.load() // 刷新表格的数据
          this.dialogVisible = false  // 关闭弹窗
        })
      }  else {  // 新增
        request.post("/book", this.form).then(res => {
          console.log(res)
          if (res.code === '0') {
            this.$message({
              type: "success",
              message: "新增成功"
            })
          } else {
            this.$message({
              type: "error",
              message: res.msg
            })
          }

          this.load() // 刷新表格的数据
          this.dialogVisible = false  // 关闭弹窗
        })
      }
    },
    add() {
      this.dialogVisible = true
      this.form = {}
      if(this.$refs["upload"])
      {
        this.$refs["upload"].clearFiles() //清空
      }
    },
    handleEdit(row) {
      //创建一个与表格分离的对象
      this.form=JSON.parse(JSON.stringify(row))
      this.dialogVisible=true
      this.$nextTick(()=>{
        if(this.$refs["upload"])
        {
          this.$refs["upload"].clearFiles() //清空
        }
      })
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
      request.delete("/book/"+id)
      this.load()
    },
    additem(row)
    {
        let fo = {
          userid:this.user.id,
          bookid:row.id
        }
        request.post("/car",fo).then(res=>{
          if(res.code==='0'){
            this.$message({
              type: "success",
              message: "新增成功"
            })
          }
        })
    }
  }
}
</script>
