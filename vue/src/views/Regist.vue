<template>
  <div style="width: 100%; height: 100vh; overflow: hidden">
    <div style="width: 400px; margin:150px auto">
      <div style="font-size: 30px;text-align: center;padding:25px">欢迎来到注册界面</div>
      <el-form ref="form"
               :model="form"
               label-width="120px"
               :rules="rules"
      >
        <el-form-item label="用户名" size="normal" prop="username">
          <el-input v-model="form.username"></el-input>
        </el-form-item>
        <el-form-item label="密码" size="normal" prop="password">
          <el-input v-model="form.password" show-password></el-input>
        </el-form-item>
        <el-form-item label="确认密码" size="normal" prop="confirm">
          <el-input v-model="form.confirm" show-password></el-input>
        </el-form-item>
        <el-form-item size="normal">
          <el-button style="width: 30%" @click="regist('form')">注册</el-button>
          <p style="font-size: 20px" @click="$router.push('/')">已有账号，前往登录</p>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";
import {ElMessage} from "element-plus"
export default {
  name: "Regist",
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
        confirm: [
          {
            required: true,
            message: '请再次输入密码',
            trigger: 'blur',
          }],
      }
    }
  },
  methods:{
    regist(formName){
      if(this.form.password !== this.form.confirm){
        ElMessage({message:"两次输入密码不一致",type:"error"})
        return
      }
      //表单校验部分
      this.$refs[formName].validate((valid) => {
        if (valid) {
          request.post("/user/register",this.form).then(res=>{
            if(res.code==='0'){
              ElMessage({message:"注册成功",type:"success"})
              this.$router.push("/login")//登录成功后进行跳转
            }
            else{
              ElMessage({message:"注册失败，可能是用户名存在",type:"error"})
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