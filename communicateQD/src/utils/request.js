import axios from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({
  // 关键：只写 /api，避免和 vite 代理重复
  baseURL: '/api',
  timeout: 10000
})

// 响应拦截：HTTP 2xx 但 Result.code != 200 时按失败处理（与后端业务码一致）
request.interceptors.response.use(
  (res) => {
    const data = res.data
    if (data && typeof data.code === 'number' && data.code !== 200) {
      const err = new Error(data.message || '操作失败')
      err.response = { status: res.status, data }
      return Promise.reject(err)
    }
    return data
  },
  (err) => {
    const d = err.response?.data
    const msg =
      d?.message ||
      d?.msg ||
      (err.response?.status ? `${err.response.status} ${err.response.statusText || ''}` : '') ||
      err.message ||
      '请求失败'
    ElMessage.error(String(msg).trim() || '请求失败')
    return Promise.reject(err)
  }
)

export default request
