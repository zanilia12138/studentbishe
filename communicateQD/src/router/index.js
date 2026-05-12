import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import UserHome from '../views/UserHome.vue'
import AdminHome from '../views/AdminHome.vue'
import InfoList from '../views/InfoList.vue'
import PostList from '../views/PostList.vue'
import PostDetail from '../views/PostDetail.vue'
import Register from '../views/Register.vue'
import Announcement from '../views/Announcement.vue'
import Guide from '../views/Guide.vue'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: Login },
  { path: '/user-home', component: UserHome },
  { path: '/admin-home', component: AdminHome },
  { path: '/post-detail/:id', 
    name: 'PostDetail', 
    component: PostDetail 
  }, 
  { path: '/info-list', component: InfoList },
  { path: '/post-list', component: PostList },
  { path: '/register', component: Register},
  { path: '/announcement', component: Announcement},
  { path: '/guide', component: Guide}

]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  if (to.meta.requireAuth) {
    const token = localStorage.getItem('token')
    const role = localStorage.getItem('role')
    if (!token) {
      next('/login')
    } else if (to.meta.role && to.meta.role !== role) {
      next('/login')
    } else {
      next()
    }
  } else {
    next()
  }
})

export default router