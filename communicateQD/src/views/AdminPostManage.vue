<template>
  <div class="admin-page">
    <h2>帖子管理</h2>
    <p class="sub">查看与删除社区帖子</p>
    <el-table :data="postList" stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="title" label="标题" min-width="140" show-overflow-tooltip />
      <el-table-column label="内容摘要" min-width="180" show-overflow-tooltip>
        <template #default="{ row }">{{ contentPreview(row.content) }}</template>
      </el-table-column>
      <el-table-column prop="user_name" label="发布人" width="110" />
      <el-table-column label="配图" width="80">
        <template #default="{ row }">
          <el-image
            v-if="row.image_url"
            :src="imgUrl(row.image_url)"
            fit="cover"
            class="thumb"
            preview-teleported
            :preview-src-list="[imgUrl(row.image_url)]"
          />
          <span v-else class="muted">—</span>
        </template>
      </el-table-column>
      <el-table-column label="发布时间" width="170">
        <template #default="{ row }">{{ formatTime(row.createTime || row.create_time) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="100" fixed="right">
        <template #default="{ row }">
          <el-button type="danger" size="small" @click="confirmDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../utils/request'

const postList = ref([])
const loading = ref(false)

const formatTime = (t) => (t ? String(t).replace('T', ' ') : '')
const contentPreview = (c) => {
  const s = c || ''
  return s.length > 60 ? `${s.slice(0, 60)}…` : s
}
const imgUrl = (path) => {
  if (!path) return ''
  if (path.startsWith('http')) return path
  return path.startsWith('/') ? path : `/${path}`
}

const loadList = async () => {
  loading.value = true
  try {
    const res = await request.get('/post/list')
    postList.value = res.data || []
  } catch {
    ElMessage.error('加载帖子失败')
  } finally {
    loading.value = false
  }
}

const confirmDelete = (row) => {
  ElMessageBox.confirm(`确定删除帖子「${row.title}」？`, '提示', {
    type: 'warning',
    confirmButtonText: '删除',
    cancelButtonText: '取消'
  })
    .then(async () => {
      await request.delete(`/post/delete/${row.id}`)
      ElMessage.success('已删除')
      loadList()
    })
    .catch(() => {})
}

onMounted(loadList)
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
.thumb {
  width: 44px;
  height: 44px;
  border-radius: 6px;
}
.muted {
  color: #c0c4cc;
}
</style>
