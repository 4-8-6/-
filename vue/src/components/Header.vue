<template>
  <div style="height: 50px;line-height: 50px;border-bottom: 1px solid #ccc;display: flex">
    <div style="width: 200px; padding-left: 30px;font-weight: bold">商城</div>
    <div style="flex:1"></div>
    <div style="width:100px;font-weight: bold" >
      <el-dropdown>
    <span class="el-dropdown-link">
      用户：{{this.user.nickName}}
      <el-icon class="el-icon--right">
        <arrow-down />
      </el-icon>
    </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="$router.push('/person')">个人系统</el-dropdown-item>
            <el-dropdown-item @click="$router.push('/')">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
    export default {
        name:"Header",

      data() {
        return {
          user:{},
        }
      },
      created() {
        let userStr = sessionStorage.getItem("user")||"{}"
        this.user= JSON.parse((userStr))
      }
    }
</script>

<style scoped>

</style>
