<template>
  <el-container>
    <el-header class="navbar-header">
      <div class="nav-left">
        <el-icon><School /></el-icon>
        <span class="nav-title">备考学生交流系统</span>
      </div>
      <div class="nav-right">
        <el-menu
          :default-active="activeMenu"
          mode="horizontal"
          class="nav-menu"
          @select="handleMenuSelect"
        >
          <el-menu-item index="/user-home" v-if="role === '1'">
            <el-icon><HomeFilled /></el-icon>
            <span>个人首页</span>
          </el-menu-item>
          <el-menu-item index="/post-list" v-if="role === '1'">
            <el-icon><Document /></el-icon>
            <span>社区帖子</span>
          </el-menu-item>
          <el-menu-item index="/info-list" v-if="role === '1'">
            <el-icon><Files /></el-icon>
            <span>学习资料</span>
          </el-menu-item>
          <el-menu-item index="/admin/posts" v-if="role === '2'">
            <el-icon><Document /></el-icon>
            <span>帖子管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/users" v-if="role === '2'">
            <el-icon><User /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
        </el-menu>
        <div class="nav-user">
          <el-dropdown v-if="token" @command="handleCommand">
            <span class="user-info">
              <el-avatar size="small" :src="avatar" />
              <span class="username">{{ nickname }}</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
          <el-button v-else type="primary" size="small" @click="$router.push('/login')">
            登录
          </el-button>
        </div>
      </div>
    </el-header>
  </el-container>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { School, HomeFilled, Document, Files, User } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()

const token = ref(localStorage.getItem('token') || '')
const role = ref(localStorage.getItem('role') || '1')
const nickname = ref(localStorage.getItem('nickname') || '用户')
const avatar = ref('https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png')

const activeMenu = computed(() => route.path)

// 菜单点击跳转
const handleMenuSelect = (index) => {
  router.push(index)
}

const handleCommand = (command) => {
  if (command === 'logout') {
    localStorage.removeItem('token')
    localStorage.removeItem('role')
    localStorage.removeItem('nickname')
    ElMessage.success('退出登录成功')
    router.push('/login')
  }
}
</script>

<style scoped>
.navbar-header {
  background-color: #409eff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  color: white;
}

.nav-left {
  display: flex;
  align-items: center;
  gap: 10px;
}

.nav-title {
  font-size: 20px;
  font-weight: bold;
}

.nav-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.nav-menu {
  border-bottom: none;
  background-color: transparent;
  color: white;
}

.nav-menu .el-menu-item {
  color: white;
  transition: all 0.3s;
}

/* 鼠标 hover 效果 */
.nav-menu .el-menu-item:hover {
  background-color: rgba(255, 255, 255, 0.3) !important;
}

/* 激活态高亮效果（核心修改） */
.nav-menu .el-menu-item.is-active {
  background-color: rgba(255, 255, 255, 0.4) !important;
  font-weight: bold;
  color: #fff !important;
}

.nav-user {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.username {
  font-size: 14px;
}
</style>