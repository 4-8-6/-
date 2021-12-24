import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/User.vue'
import login from "@/views/Login";
import consumer from "@/layout/consumer";

const routes = [
  {
    path: '/',
    name: 'consumer',
    component:consumer,
    redirect:'/login',
    children:[
      {
        path:'/user',
        name:'User',
        component:()=>import('../views/User')
      },
      {
        path:'/book',
        name:'Book',
        component:()=>import('../views/Book')
      },
      {
        path:'/news',
        name:'News',
        component:()=>import('../views/News')
      },
      {
        path:'/order',
        name:'Order',
        component:()=>import('../views/Order')
      },
      {
        path:'/car',
        name:'Car',
        component:()=>import('../views/Car')
      },
      {
        path:'/person',
        name:'Person',
        component:()=>import('../views/Person')
      }
    ]
  },
  {
    path:'/login',
    name:'Login',
    component: ()=>import('../views/Login')
  },
  {
    path: '/regist',
    name:'Regist',
    component: ()=>import('../views/Regist')
  },
  {
    path: '/about',
    name: 'About',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
