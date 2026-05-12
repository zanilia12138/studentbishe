<template>
  <div class="register-container">
    <div class="register-box">
      <h2>用户注册</h2>
      <el-form :model="registerForm" :rules="rules" ref="registerFormRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="registerForm.username" placeholder="请输入用户名" prefix-icon="User" />
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="registerForm.nickname" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="registerForm.password" type="password" placeholder="请输入密码" prefix-icon="Lock" show-password />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="registerForm.confirmPassword" type="password" placeholder="请再次输入密码" prefix-icon="Lock" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleRegister" class="register-btn">立即注册</el-button>
        </el-form-item>
        <!-- 已有账号跳转登录 -->
        <el-form-item style="text-align: center; margin: 0;">
          <span>已有账号？</span>
          <el-button type="text" @click="$router.push('/login')" style="padding: 0; margin-left: 4px; color: #409eff;">
            去登录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../utils/request'

const router = useRouter()
const registerFormRef = ref()
const loading = ref(false)

const registerForm = reactive({
  username: '',
  nickname: '',
  password: '',
  confirmPassword: ''
})

// 自定义校验：确认密码与密码一致
const validateConfirmPassword = (rule, value, callback) => {
  if (value !== registerForm.password) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' },
    { min: 2, max: 20, message: '昵称长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

const handleRegister = async () => {
  await registerFormRef.value.validate()
  loading.value = true
  try {
    // 后端默认设置 role=1、status=1，前端只传递必填字段
    const res = await request.post('/register', {
      username: registerForm.username,
      nickname: registerForm.nickname,
      password: registerForm.password
    })
    ElMessage.success('注册成功')
    // 注册成功后跳转回登录页
    router.push('/login')
  } catch (err) {
    ElMessage.error(err.response?.data?.msg || '注册失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
/* 与登录页完全一致的样式 */
.register-container {
  width: 100vw;
  height: 100vh;
  /* 修正：让容器占满视口，覆盖导航栏的影响 */
  position: fixed;
  top: 0;
  left: 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 10; /* 确保在导航栏之上 */
}
.register-box {
  background: white;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0,0,0,0.15);
  width: 400px;
}
.register-box h2 {
  text-align: center;
  margin-bottom: 20px;
  color: #333;
}
.register-btn {
  width: 100%;
  background: #409eff;
}
</style>