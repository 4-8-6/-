<template>
  <div>
    <div>
      <el-button style="margin: 10px 0" @click="add">增加</el-button>
      <el-button>删除</el-button>
    </div>
    <div style="margin: 10px">
      <el-input v-model="search" placeholder="请输入" style="width: 25%" clearable/>
      <el-button style="margin-left: 5px" @click="load">搜索</el-button>
    </div>
    <el-table :data="tableData"  boder stripe style="width: 100%">
      <el-table-column prop="id" label="ID" sortable/>
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="author" label="作者" />
      <el-table-column prop="time" label="时间" />
        <el-table-column label="Operations">
        <template #default="scope">
          <el-button size="mini" @click="details( scope.row)">预览</el-button>
          <el-button size="mini" @click="handleEdit( scope.row)" v-if="scope.row.userid===this.user.id">编辑</el-button>
          <el-popconfirm title="确定要删除吗？" @confirm="handleDelete(scope.row.id)" v-if="scope.row.userid===this.user.id">
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
          width="50%">
        <el-form :model="form" label-width="120px">
          <el-form-item label="标题">
            <el-input v-model="form.title" style="width: 80%"></el-input>
          </el-form-item>
          <div id="div1"></div>
<!--          <el-form-item label="内容">-->
<!--            <el-input v-model="form.content" style="width: 80%"></el-input>-->
<!--          </el-form-item>-->
        </el-form>
        <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">确定</el-button>
      </span>
        </template>
      </el-dialog>
      <el-dialog v-model="vis" title="详情" width="50%">
        <el-card>
          <div v-html="ditail.content" style="min-height: 80px"></div>
        </el-card>
      </el-dialog>
    </div>
  </div>
</template>


<script>


import E from 'wangeditor'
import request from "@/utils/request";
import {ElMessage} from "element-plus"

let editor;
export default {
  name: 'News',
  components: {},
  data() {
    return {
      form: {},
      dialogVisible: false,
      search: '',
      currenPage: 1,
      pageSize:10,
      total: 0,
      ditail:{},
      user:{},
      tableData: [],
      vis:false
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
  mounted() {
  },
  methods: {
    details(row){
      this.ditail=row
      this.vis=true
    },
    uploadsuccess(res){
      console.log(res)
      this.form.cover=res.data
    },
    load(){
      //传入参数要用 {}
      request.get("/news",{
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
      this.form.content=editor.txt.html()//h获取文本

      if (this.form.id) {  // 更新
        request.put("/news", this.form).then(res => {
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
        let Userstr = sessionStorage.getItem("user")||"{}"
        let user = JSON.parse(Userstr)
        this.form.author=user.username
        this.form.userid=user.id
        request.post("/news", this.form).then(res => {
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
      this.$nextTick(()=>
      {
        if(!editor) {
          editor = new E('#div1')

          //配置server接口地址
          editor.config.uploadImgServer = 'http://'+ window.server.filesUploadUrl +':9090/files/editor/upload'
          editor.config.uploadFileName = "file"
          editor.create()
        }
        editor.txt.html("")
      })

      //关闭弹窗的div，new一个editor

    },
    handleEdit(row) {
      //创建一个与表格分离的对象
      this.form=JSON.parse(JSON.stringify(row))
      this.dialogVisible=true
      this.$nextTick(()=>{
        if(!editor) {
          editor = new E('#div1')
          editor.create()
        }
          editor.txt.html(row.content)

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
      request.delete("/news/"+id)
      this.load()
    }
  }
}
</script>
