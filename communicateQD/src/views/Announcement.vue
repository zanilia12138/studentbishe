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
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="showDetailDialog(row)">查看详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 公告详情弹窗 -->
    <el-dialog v-model="showDetail" :title="currentAnnounce.title" width="60%">
      <div class="announce-detail">
        <p class="detail-time">发布时间：{{ currentAnnounce.createTime }}</p>
        <div class="detail-content">{{ currentAnnounce.content }}</div>
      </div>
      <template #footer>
        <el-button @click="showDetail = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../utils/request'

const announceList = ref([])
const showDetail = ref(false)
const currentAnnounce = reactive({
  id: '',
  title: '',
  content: '',
  createTime: ''
})

// 加载公告列表
const loadAnnounceList = async () => {
  try {
    const res = await request.get('/announcement/list')
    announceList.value = res.data
  } catch (err) {
    ElMessage.error('加载公告失败')
  }
}

// 显示详情弹窗
const showDetailDialog = (row) => {
  Object.assign(currentAnnounce, row)
  showDetail.value = true
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

.announce-detail {
  padding: 10px 0;
}

.detail-time {
  color: #999;
  font-size: 14px;
  margin-bottom: 15px;
}

.detail-content {
  line-height: 1.8;
  color: #333;
  white-space: pre-wrap;
}
</style>