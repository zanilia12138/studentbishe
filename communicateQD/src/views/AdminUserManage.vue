<template>
  <div class="admin-page">
    <h2>用户管理</h2>
    <p class="sub">启用 / 禁用 / 删除用户</p>
    <el-table :data="userList" stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="nickname" label="昵称" />
      <el-table-column prop="role" label="角色" width="100">
        <template #default="{ row }">
          <el-tag :type="row.role === 2 ? 'danger' : 'primary'">
            {{ row.role === 2 ? '管理员' : '学生' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">
            {{ row.status === 1 ? '正常' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button
            size="small"
            :type="row.status === 1 ? 'warning' : 'success'"
            @click="toggleStatus(row)"
          >
            {{ row.status === 1 ? '禁用' : '启用' }}
          </el-button>
          <el-button type="danger" size="small" @click="confirmDeleteUser(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../utils/request'

const userList = ref([])
const loading = ref(false)

const loadUserList = async () => {
  loading.value = true
  try {
    const res = await request.get('/user/list?pageNum=1&pageSize=100')
    userList.value = res.data.records || res.data
  } catch {
    ElMessage.error('加载用户失败')
  } finally {
    loading.value = false
  }
}

const toggleStatus = (row) => {
  row.status = row.status === 1 ? 0 : 1
  request.post('/user/updateStatus', row).then(() => {
    ElMessage.success('状态更新成功')
    loadUserList()
  })
}

const confirmDeleteUser = (row) => {
  ElMessageBox.confirm(`确定删除用户「${row.username}」？`, '提示', {
    type: 'warning',
    confirmButtonText: '删除',
    cancelButtonText: '取消'
  })
    .then(async () => {
      await request.delete(`/user/delete/${row.id}`)
      ElMessage.success('删除成功')
      loadUserList()
    })
    .catch(() => {})
}

onMounted(loadUserList)
</script>

<style scoped>
.admin-page {
  padding: 20px;
  max-width: 1400px;
  margin: 0 auto;
  background: #f5f7fa;
  min-height: calc(100vh - 60px);
}
.sub {
  color: #666;
  margin: -8px 0 16px;
  font-size: 14px;
}
</style>
