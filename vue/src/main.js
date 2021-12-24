import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementPlus from "element-plus"
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import JsCookie from "js-cookie";
import 'element-plus/dist/index.css'
import '@/assets/css/global.css'
//这个地方的option可以对全局的文件（Elementplus）进行过滤
/*
做一点最基础的笔记：
flex布局的使用在外部容器进行使用
flex：1可以让这个div之中的内容占满剩下的整个页面

* */
const app = createApp(App).use(store).use(router).use(JsCookie)
app.use(ElementPlus,{locale:zhCn, size:'mini'})
app.mount('#app')

