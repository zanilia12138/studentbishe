<template>
  <div id="app">
    <!-- 非登录页面才显示导航栏 -->
    <nav v-if="!['/login', '/register'].includes(route.path)" class="navbar">
      <div class="nav-title">备考学生交流系统</div>
      <div class="nav-menu">
        <router-link v-if="role === '1'" to="/user-home" class="nav-item">个人首页</router-link>
        <router-link v-if="role === '1'" to="/post-list" class="nav-item">社区帖子</router-link>
        <router-link v-if="role === '1'" to="/info-list" class="nav-item">学习资料</router-link>
        <router-link v-if="role === '1'" to="/guide" class="nav-item">使用指南</router-link>
        <router-link to="/announcement" class="nav-item">公告</router-link>
        <template v-if="role === '2'">
          <router-link to="/admin/posts" class="nav-item">帖子管理</router-link>
          <router-link to="/admin/users" class="nav-item">用户管理</router-link>
        </template>
        
        <!-- 优化后的用户下拉框 -->
        <el-dropdown trigger="hover" class="user-dropdown">
          <span class="nav-item user-info">
            {{ nickname }} <el-icon><ArrowDown /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu class="user-dropdown-menu">
              <el-dropdown-item class="dropdown-item" @click="logout">
                <el-icon><SwitchButton /></el-icon>
                退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </nav>
    <router-view />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ArrowDown, SwitchButton } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()

// 判断当前是否是登录页
const isLoginPage = computed(() => route.path === '/login')

// 从 localStorage 实时获取用户信息
const nickname = ref(localStorage.getItem('nickname') || '访客')
const role = ref(localStorage.getItem('role') || '1')

// 监听路由变化，更新用户信息
router.afterEach((to) => {
  nickname.value = localStorage.getItem('nickname') || '访客'
  role.value = localStorage.getItem('role') || '1'
})

const logout = () => {
  localStorage.clear()
  ElMessage.success('退出成功')
  router.push('/login')
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
body {
  background: #f0f2f5;
}
.navbar {
  height: 60px;
  background: linear-gradient(90deg, #409eff 0%, #667eea 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}
.nav-title {
  font-size: 20px;
  font-weight: bold;
}
.nav-menu {
  display: flex;
  align-items: center;
  gap: 0; /* 让导航项紧密排列 */
}
/* 统一导航项样式 */
.nav-item {
  color: white;
  font-size: 18px; /* 和其他导航项保持一致大小 */
  font-weight: 500;
  padding: 0 16px;
  height: 60px;
  line-height: 60px;
  text-decoration: none;
  display: inline-flex;
  align-items: center;
  transition: all 0.2s;
  cursor: pointer;
}
.nav-item:hover {
  background-color: rgba(255, 255, 255, 0.15); /*  hover 高亮效果 */
}
/* 用户下拉框样式 */
.user-dropdown {
  display: inline-flex;
  align-items: center;
}
.user-info {
  display: inline-flex;
  align-items: center;
  gap: 4px;
}
/* 下拉菜单样式优化 */
.user-dropdown-menu {
  min-width: 120px !important;
  padding: 8px 0;
}
.dropdown-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  padding: 8px 16px;
  color: #333;
}
.dropdown-item:hover {
  background-color: #f0f7ff !important;
  color: #409eff;
}
</style>