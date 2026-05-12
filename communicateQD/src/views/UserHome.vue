<template>
  <div class="user-home">
    <div class="welcome-card">
      <h2>欢迎回来，{{ nickname }} 🎉</h2>
      <p>今天也要坚持学习哦！</p>
    </div>

    <div class="clockin-card">
      <h3>📝 今日学习打卡</h3>
      <el-form :model="clockForm" label-width="100px">
        <el-form-item label="学习内容">
          <el-input v-model="clockForm.content" placeholder="今天学了什么？" />
        </el-form-item>
        <el-form-item label="学习时长">
          <div class="duration-input">
            <el-button @click="clockForm.duration = Math.max(10, clockForm.duration - 10)">-</el-button>
            <el-input-number v-model="clockForm.duration" :min="10" :step="10" />
            <el-button @click="clockForm.duration += 10">+</el-button>
            <span class="unit">分钟</span>
          </div>
        </el-form-item>
        <el-form-item label="学习配图">
          <el-upload
            ref="clockUploadRef"
            action="#"
            :auto-upload="false"
            :limit="1"
            accept="image/jpeg,image/png,image/gif,image/webp"
            :on-change="onClockImageChange"
            :on-exceed="() => ElMessage.warning('最多选择 1 张图片')"
            list-type="picture-card"
          >
            <el-icon><Plus /></el-icon>
            <template #tip>
              <div class="el-upload__tip">可选，支持 jpg/png/gif/webp</div>
            </template>
          </el-upload>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="submitClock" class="submit-btn">提交打卡</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="record-card">
      <h3>📊 打卡记录</h3>
      <el-table :data="clockList" stripe>
        <el-table-column prop="content" label="学习内容" />
        <el-table-column prop="duration" label="时长(分钟)" width="120" />
        <el-table-column label="配图" width="100">
          <template #default="scope">
            <el-image
              v-if="scope.row.image_url"
              :src="publicImageUrl(scope.row.image_url)"
              :preview-src-list="[publicImageUrl(scope.row.image_url)]"
              fit="cover"
              class="thumb-img"
              preview-teleported
            />
            <span v-else class="no-img">—</span>
          </template>
        </el-table-column>
        <!-- 改用格式化后的时间 -->
        <el-table-column label="打卡时间" width="180">
          <template #default="scope">
            <span>{{ formatTime(scope.row.create_time) }}</span>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import request from '../utils/request'

const nickname = ref(localStorage.getItem('nickname') || '同学')
const userId = ref(Number(localStorage.getItem('userId')))
const loading = ref(false)
const clockList = ref([])

const clockForm = reactive({
  content: '',
  duration: 60
})
const clockUploadRef = ref(null)
const clockImageFile = ref(null)

const publicImageUrl = (path) => {
  if (!path) return ''
  if (path.startsWith('http://') || path.startsWith('https://')) return path
  return path.startsWith('/') ? path : `/${path}`
}

const onClockImageChange = (file, fileList) => {
  clockImageFile.value = fileList.length ? file.raw : null
}

// ==========================================
// ✅ 时间格式化函数（去掉 T）
// ==========================================
const formatTime = (timeStr) => {
  if (!timeStr) return ''
  // 把 2026-03-27T12:30:00 变成 2026-03-27 12:30:00
  return timeStr.replace('T', ' ')
}

const loadClockList = async () => {
  console.log(userId.value);
  console.log(clockList);
  try {
    const res = await request.get(`/clock/list/${userId.value}`)
    clockList.value = res.data
  } catch (err) {
    ElMessage.error('加载打卡记录失败')
  }
}

const submitClock = async () => {
  if (!clockForm.content) return ElMessage.warning('请输入学习内容')
  loading.value = true
  try {
    let imageUrl = null
    if (clockImageFile.value) {
      const fd = new FormData()
      fd.append('file', clockImageFile.value)
      const up = await request.post('/clock/uploadImage', fd)
      if (!up || up.code !== 200) {
        throw new Error(up?.message || '图片上传失败')
      }
      imageUrl = up.data
    }

    await request.post('/clock/add', {
      userId: userId.value,
      content: clockForm.content,
      duration: clockForm.duration,
      imageUrl
    })
    ElMessage.success('打卡成功')
    clockForm.content = ''
    clockImageFile.value = null
    if (clockUploadRef.value) {
      clockUploadRef.value.clearFiles()
    }
    loadClockList()
  } catch (err) {
    ElMessage.error(err.message || '打卡失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => loadClockList())
</script>

<style scoped>
.user-home {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
  background: #f5f7fa;
  min-height: calc(100vh - 60px);
}

.welcome-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 20px;
  border-radius: 12px;
  margin-bottom: 20px;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.clockin-card, .record-card {
  background: white;
  padding: 20px;
  border-radius: 12px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
}

.duration-input {
  display: flex;
  align-items: center;
  gap: 8px;
}
.unit {
  margin-left: 8px;
  color: #666;
}
.submit-btn {
  width: 100%;
  background: #409eff;
}

.thumb-img {
  width: 56px;
  height: 56px;
  border-radius: 6px;
}
.no-img {
  color: #c0c4cc;
}
</style>