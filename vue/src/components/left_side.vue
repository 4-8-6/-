<template>

    <el-row class="tac">
      <el-col :span="30">

        <el-menu
            style="width: 140px"
            default-active="path"
            class="el-menu-vertical-demo"
            @open="handleOpen"
            @close="handleClose"
            router
        >

          <el-menu-item index="/user">
            <el-icon><setting /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
          <el-menu-item index="/book">
            <el-icon><setting /></el-icon>
            <span>书籍管理</span>
          </el-menu-item>
          <el-menu-item index="/news">
            <el-icon><setting /></el-icon>
            <span>评论管理</span>
          </el-menu-item>
       <!-- 这部分是测试 -->
          <el-menu-item index="/car">
            <el-icon><setting /></el-icon>
            <span>购物车管理</span>
          </el-menu-item>
          <el-menu-item index="/order">
            <el-icon><setting /></el-icon>
            <span>订单管理</span>
          </el-menu-item>
        </el-menu>
      </el-col>
    </el-row>
</template>

<script>
import request from "@/utils/request";

export default {
  name:"left_side",
  data(){
    return{
      user:{},
      path:this.$route.path,//设置默认高亮
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
  }
}
</script>

<style scoped>

</style>