import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import UserHome from '../views/UserHome.vue'
import AdminPostManage from '../views/AdminPostManage.vue'
import AdminUserManage from '../views/AdminUserManage.vue'
import AdminAnnounceManage from '../views/AdminAnnounceManage.vue'
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
  { path: '/admin-home', redirect: '/admin/users' },
  { path: '/admin/posts', component: AdminPostManage },
  { path: '/admin/users', component: AdminUserManage },
  { path: '/admin/announcements', component: AdminAnnounceManage },
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

const ADMIN_ROLE = '2'
const studentOnlyPaths = ['/post-list', '/info-list', '/guide']

// 路由守卫
router.beforeEach((to, from, next) => {
  const role = String(localStorage.getItem('role') ?? '')
  const token = localStorage.getItem('token')

  if (to.path.startsWith('/admin/')) {
    if (!token) {
      next('/login')
      return
    }
    if (role !== ADMIN_ROLE) {
      next('/user-home')
      return
    }
  }

  if (role === ADMIN_ROLE) {
    if (studentOnlyPaths.includes(to.path) || to.path.startsWith('/post-detail')) {
      next('/admin/users')
      return
    }
  }

  if (to.meta.requireAuth) {
    const roleMeta = localStorage.getItem('role')
    if (!token) {
      next('/login')
    } else if (to.meta.role && to.meta.role !== roleMeta) {
      next('/login')
    } else {
      next()
    }
  } else {
    next()
  }
})

export default router