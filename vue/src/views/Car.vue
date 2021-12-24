<template>

  <div>
    <el-table :data="tableData"  boder stripe style="width: 100%">
      <el-table-column prop="id" label="ID" sortable/>
      <el-table-column prop="name" label="名称" />
      <el-table-column prop="price" label="单价" />
      <el-table-column prop="num" label="数量" />
        <el-table-column label="Operations">
        <template #default="scope">
          <el-button size="mini" @click="handleEdit( scope.row)" >编辑</el-button>
          <el-popconfirm title="确定要删除吗？" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button size="mini" type="danger" >删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div style="margin: 10px 0">
      <el-dialog
          v-model="dialogVisible"
          title="Tips"
          width="30%">
        <el-form :model="form" label-width="120px">
          <el-form-item label="数量">
            <el-input v-model="form.num" style="width: 80%"></el-input>
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
    <div style="color: #409EFF;font-size: larger">
        <span style="position:absolute; right:10%; bottom:10%;">
      总共价格:{{this.totalamount}}
      <el-button @click="buyitems" size="normal" type="danger">生成订单</el-button>
        </span>
    </div>

  </div>
</template>


<script>



import request from "@/utils/request";
import {ElMessage} from "element-plus"
export default {
  name: 'Car',
  components: {},
  data() {
    return {
      form: {},
      dialogVisible: false,
      user:{},
      search: '',
      tableData: [],
      totalamount:0
    }
  },
  created() {
    let userStr = sessionStorage.getItem("user")||"{}"
    this.user= JSON.parse((userStr))
    if(this.user===null){this.$router.push("/")}
    request.get("/user/"+this.user.id).then(res=>{
      if(res.code===0)
      {
        this.user=res.data
      }
    })
    this.load()
  },
  methods: {
    uploadsuccess(res){
      console.log(res)
      this.form.cover=res.data
    },
    load(){
      //传入参数要用 {}
      this.tableData=[]
      //初始化两个写的
      let that = this.tableData
      this.totalamount = 0
      let thst = this
      request.get("/car/"+this.user.id).then(res=>{
        let mm = res.data
        mm.forEach((item)=>{
          if(item.userid){
            request.get("/book/"+item.bookid).then(rest=>{
                      if(rest.code==='0') {
                        that.push({
                          id: item.id,
                          name: rest.data.name,
                          price: rest.data.price,
                          num: item.num
                        })
                        //计算总的价格
                       thst.totalamount = thst.totalamount+ item.num * rest.data.price
                      }
                      else{
                        thst.handleDelete(item.id)
                      }
            })
          }
        })
        //this.tableData=res.data.records
      })
    },
    //生成订单
    buyitems(){
      if(this.totalamount === 0)
      {
        this.$message({
          type: "error",
          message: "没有物品"
        })
        return
      }
      let foo = {
        userid:this.user.id,
        cost:this.totalamount
      }
      console.log(foo)
      request.post("/order",foo).then(res=>{
        if(res.code === '0')
        {
          this.$message({
            type: "success",
            message: "生成成功"
          })
        }
      })
      this.tableData.forEach((item)=>{
              request.delete("/car/"+ item.id)
            })

            this.$nextTick(()=>{

              this.load()
      })
    },
    save() {
      if (this.form.id) {  // 更新
        let fo = {
            id: this.form.id,
            userid:this.user.id,
          num:this.form.num
        }
        request.put("/car", fo).then(res => {
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
      request.delete("/car/"+id)
      this.$nextTick(()=>{
        this.load()
      })

    }
  }
}
</script>
