import axios from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({
  // 关键：只写 /api，避免和 vite 代理重复
  baseURL: '/api',
  timeout: 10000
})

// 响应拦截
request.interceptors.response.use(
  res => res.data,
  err => {
    ElMessage.error(err.response?.data?.msg || '请求失败')
    return Promise.reject(err)
  }
)

export default request