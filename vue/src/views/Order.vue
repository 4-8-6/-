<template>
<div>
  <el-table :data="tableData"  boder stripe style="width: 100%">
    <el-table-column prop="id" label="ID" sortable/>
    <el-table-column prop="userid" label="用户id"  v-if="authority===2"/>
    <el-table-column prop="cost" label="金额" />
    <el-table-column prop="date" label="日期" />
    <el-table-column label="Operations">
      <template #default="scope">
        <el-popconfirm title="确定要删除吗？" @confirm="handleDelete(scope.row.id)" v-if="authority===2">
          <template #reference>
            <el-button size="mini" type="danger" >删除</el-button>
          </template>
        </el-popconfirm>
      </template>
    </el-table-column>
  </el-table>
</div>
</template>

<script>
import request from "@/utils/request";
import {nextTick} from "vue";

export default {
  name: "Order",
  data(){
    return{
      user:{},
      authority:1,
      tableData:[]
    }
  },
  created() {
    let userStr = sessionStorage.getItem("user")||"{}"
    this.user= JSON.parse((userStr))
    if(this.user.role===2)
    {
      this.authority = 2
    }
    this.load()
  },
  methods:{
    load(){
      if(this.authority===1) {
        request.get("/order/" + this.user.id).then(res => {
          this.tableData = res.data
        })
      }
      else{
        request.get("/order/").then(res => {
          this.tableData = res.data
        })
      }
    },
    handleDelete(id)
    {
        request.delete("/order/" +id)
       this.$nextTick(()=>{
         this.load()
       })
    }
  }
}

</script>

<style scoped>

</style>