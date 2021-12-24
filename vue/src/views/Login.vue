<template>
  <div style="width: 100%; height: 100vh; overflow: hidden">
  <div style="width: 400px; margin:150px auto">
    <div style="font-size: 30px;text-align: center;padding:25px">欢迎登录</div>
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item label="用户名" size="normal" prop="username">
        <el-input v-model="form.username"></el-input>
      </el-form-item>
      <el-form-item label="密码" size="normal" prop="password">
        <el-input v-model="form.password" show-password></el-input>
      </el-form-item>
      <el-form-item size="normal" style="align-content: center">
        <el-button style="width: 30%;" @click="login('form')">登录</el-button>
        <p style="font-size: 20px" @click="gotoRegist">没有账号？前往注册</p>
      </el-form-item>
    </el-form>
  </div>
  </div>
</template>

<script>
import request from "@/utils/request";
import {ElMessage} from "element-plus"
export default {
  name: "Login",
  data(){
    return{
      form:{},
      rules:{
        username: [
          {
            required: true,
            message: '请输入用户名',
            trigger: 'blur',
          }],
        password: [
          {
            required: true,
            message: '请输入密码',
            trigger: 'blur',
          }],
      }
    }
  },
  methods:{
    gotoRegist()
    {
      this.$router.push('/regist')
    },
    login(formName){
      this.$refs[formName].validate((valid) => {
        if (valid) {
          request.post("user/login",this.form).then(res=>{
            if(res.code==='0'){
              sessionStorage.setItem("user",JSON.stringify(res.data))
              this.$router.push("/user")//登录成功后进行跳转
            }
            else{
              ElMessage({message:"登录失败",type:"error"})
            }
          })
        } else {
          ElMessage({message:"请输入内容",type:"error"})
          return false
        }
      })

    }
  }
}
</script>

<style scoped>

</style>