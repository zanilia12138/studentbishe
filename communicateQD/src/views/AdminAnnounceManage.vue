<template>
  <div class="admin-page">
    <h2>公告管理</h2>
    <p class="sub">发布与查看系统公告</p>
    <el-button type="primary" @click="showAnnounceDialog = true">+ 发布公告</el-button>
    <el-table :data="announceList" stripe style="margin-top: 16px" v-loading="loading">
      <el-table-column prop="title" label="标题" min-width="160" />
      <el-table-column prop="content" label="内容" min-width="220" show-overflow-tooltip />
      <el-table-column label="发布时间" width="180">
        <template #default="{ row }">{{ formatTime(row.createTime) }}</template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="showAnnounceDialog" title="发布公告" width="50%">
      <el-form :model="announceForm" label-width="80px">
        <el-form-item label="公告标题">
          <el-input v-model="announceForm.title" />
        </el-form-item>
        <el-form-item label="公告内容">
          <el-input type="textarea" v-model="announceForm.content" rows="4" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAnnounceDialog = false">取消</el-button>
        <el-button type="primary" @click="publishAnnounce">发布</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../utils/request'

const announceList = ref([])
const showAnnounceDialog = ref(false)
const loading = ref(false)

const adminId = () => Number(localStorage.getItem('userId')) || 1

const announceForm = reactive({
  title: '',
  content: '',
  adminId: adminId()
})

const formatTime = (t) => (t ? String(t).replace('T', ' ') : '')

const loadAnnounceList = async () => {
  loading.value = true
  try {
    const res = await request.get('/announcement/list')
    announceList.value = res.data
  } catch {
    ElMessage.error('加载公告失败')
  } finally {
    loading.value = false
  }
}

const publishAnnounce = async () => {
  if (!announceForm.title || !announceForm.content) {
    return ElMessage.warning('请填写标题和内容')
  }
  announceForm.adminId = adminId()
  try {
    await request.post('/announcement/publish', announceForm)
    ElMessage.success('发布成功')
    showAnnounceDialog.value = false
    Object.assign(announceForm, { title: '', content: '', adminId: adminId() })
    loadAnnounceList()
  } catch {
    ElMessage.error('发布失败')
  }
}

onMounted(loadAnnounceList)
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
