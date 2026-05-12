<template>
  <div class="login-container">
    <div class="login-box">
      <h2>用户登录</h2>
      <el-form :model="loginForm" :rules="rules" ref="loginFormRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名" prefix-icon="User" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" prefix-icon="Lock" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleLogin" class="login-btn">登录</el-button>
        </el-form-item>
        <!-- 新增：注册跳转提示 -->
        <el-form-item style="text-align: center; margin: 0;">
          <span>没有登录账户？</span>
          <el-button type="text" @click="$router.push('/register')" style="padding: 0; margin-left: 4px; color: #409eff;">
            请注册！
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
// 原有代码保持不变，无需修改
import axios from 'axios'
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../utils/request'

const router = useRouter()
const loginFormRef = ref()
const loading = ref(false)

const loginForm = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  await loginFormRef.value.validate()
  loading.value = true
  try {
    const res = await request.post('/login', {
      username: loginForm.username,
      password: loginForm.password
    })
    console.log(res);
    
    localStorage.setItem('token', res.data.token)
    localStorage.setItem('role', res.data.role)
    localStorage.setItem('nickname', res.data.nickname)
    localStorage.setItem('userId', res.data.userId)
    
    ElMessage.success('登录成功')
    
    if (res.data.role === 2) {
      router.push('/admin/users')
    } else {
      router.push('/user-home')
    }
  } catch (err) {
    if (!axios.isAxiosError(err)) {
      ElMessage.error(err.message || '登录失败')
    }
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
/* 原有样式保持不变 */
.login-container {
  width: 100vw;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
}
.login-box {
  background: white;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0,0,0,0.15);
  width: 400px;
}
.login-box h2 {
  text-align: center;
  margin-bottom: 20px;
  color: #333;
}
.login-btn {
  width: 100%;
  background: #409eff;
}
</style>