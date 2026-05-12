<template>
  <div class="announcement-list-container">
    <div class="announcement-box">
      <h2>📢 平台公告</h2>
      <el-table :data="announceList" stripe style="width: 100%">
        <el-table-column prop="title" label="公告标题" min-width="200" />
        <el-table-column prop="content" label="内容摘要" min-width="400">
          <template #default="{ row }">
            {{ row.content.length > 50 ? row.content.slice(0, 50) + '...' : row.content }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="发布时间" width="180" />
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../utils/request'

const announceList = ref([])

// 加载公告列表
const loadAnnounceList = async () => {
  try {
    const res = await request.get('/announcement/list')
    announceList.value = res.data
  } catch (err) {
    ElMessage.error('加载公告失败')
  }
}

onMounted(() => {
  loadAnnounceList()
})
</script>

<style scoped>
.announcement-list-container {
  width: 100%;
  min-height: calc(100vh - 60px);
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
  display: flex;
  justify-content: center;
}

.announcement-box {
  background: white;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0,0,0,0.15);
  max-width: 1000px;
  width: 100%;
}

.announcement-box h2 {
  text-align: center;
  margin-bottom: 20px;
  color: #333;
}
</style>