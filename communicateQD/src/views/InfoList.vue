<template>
  <div class="info-list">
    <div class="header-bar">
      <h2>📚 学习资料中心</h2>
      <el-button type="success" @click="showUploadDialog = true">+ 上传资料</el-button>
    </div>

    <!-- 搜索栏 -->
    <div class="search-bar">
      <el-input
        v-model="searchKeyword"
        placeholder="搜索资料标题关键词"
        clearable
        @clear="handleSearch"
        @keyup.enter="handleSearch"
        class="search-input"
      >
        <template #append>
          <el-button @click="handleSearch">
            <el-icon><Search /></el-icon>
          </el-button>
        </template>
      </el-input>
    </div>

    <!-- 筛选栏（默认全部，不换行） -->
    <div class="filter-bar">
      <span class="filter-label">按分类筛选：</span>
      <el-select 
        v-model="filterCategory" 
        placeholder="全部资料" 
        @change="filterMaterialList" 
        class="filter-select"
      >
        <el-option label="全部" value="" />
        <el-option label="真题" value="真题" />
        <el-option label="笔记" value="笔记" />
        <el-option label="课件" value="课件" />
        <el-option label="题库" value="题库" />
        <el-option label="考研" value="考研" />
        <el-option label="考公" value="考公" />
      </el-select>
    </div>

    <el-table :data="showMaterialList" stripe>
      <el-table-column prop="title" label="资料名称" min-width="200" />
      <el-table-column prop="category" label="分类" width="100" />
      <el-table-column prop="userName" label="上传人" width="120" />
      <el-table-column label="下载次数" width="100" sortable prop="downloadCount">
        <template #default="scope">
          <el-tag type="success" size="small">{{ scope.row.downloadCount || 0 }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="上传时间" width="180">
        <template #default="scope">
          <span>{{ formatTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="downloadMaterial(row)">下载</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="showUploadDialog" title="上传资料" width="50%">
      <el-form :model="materialForm" label-width="80px">
        <el-form-item label="资料名称">
          <el-input v-model="materialForm.title" placeholder="请输入资料名称" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="materialForm.category" placeholder="请选择分类">
            <el-option label="真题" value="真题" />
            <el-option label="笔记" value="笔记" />
            <el-option label="课件" value="课件" />
            <el-option label="题库" value="题库" />
            <el-option label="考研" value="考研" />
            <el-option label="考公" value="考公" />
          </el-select>
        </el-form-item>
        <el-form-item label="选择文件">
          <el-upload
            ref="uploadRef"
            action="#"
            :auto-upload="false"
            :on-change="handleFileChange"
            :limit="1"
          >
            <el-button type="primary">选择文件</el-button>
            <template #tip>
              <div class="el-upload__tip">请选择要上传的文件（最大50MB）</div>
            </template>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showUploadDialog = false">取消</el-button>
        <el-button type="primary" :loading="uploading" @click="uploadMaterial">上传</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Search } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import request from '../utils/request'

const materialList = ref([])        // 原始全部资料
const showMaterialList = ref([])    // 表格显示的资料
const showUploadDialog = ref(false)
const filterCategory = ref('')      // 默认值为空字符串，对应"全部"选项
const searchKeyword = ref('')       // 搜索关键词
const uploading = ref(false)        // 上传状态
const selectedFile = ref(null)      // 选中的文件
const uploadRef = ref(null)

const materialForm = reactive({
  title: '',
  category: '',
  userId: Number(localStorage.getItem('userId')) || 1
})

// 搜索处理
const handleSearch = () => {
  loadMaterialList()
}

// 文件选择处理
const handleFileChange = (file) => {
  selectedFile.value = file.raw
}

// 时间格式化（去掉T）
const formatTime = (timeStr) => {
  if (!timeStr) return ''
  return timeStr.replace('T', ' ')
}

// 加载全部资料
const loadMaterialList = async () => {
  try {
    const params = searchKeyword.value ? { keyword: searchKeyword.value } : {}
    const res = await request.get('/material/list', { params })
    materialList.value = res.data || []
    filterMaterialList()
  } catch (err) {
    ElMessage.error('加载资料失败')
  }
}

// 纯前端筛选
const filterMaterialList = () => {
  if (!filterCategory.value) {
    showMaterialList.value = materialList.value
    return
  }
  showMaterialList.value = materialList.value.filter(item => {
    return item.category === filterCategory.value
  })
}

// 上传资料
const uploadMaterial = async () => {
  if (!materialForm.title || !materialForm.category) {
    return ElMessage.warning('请填写完整资料信息')
  }
  if (!selectedFile.value) {
    return ElMessage.warning('请选择要上传的文件')
  }

  uploading.value = true
  try {
    const formData = new FormData()
    formData.append('file', selectedFile.value)
    formData.append('title', materialForm.title)
    formData.append('category', materialForm.category)
    formData.append('userId', materialForm.userId)

    const res = await request.post('/material/upload', formData)
    if (res && res.code && res.code !== 200) {
      throw new Error(res.message || '上传失败')
    }

    ElMessage.success('上传成功')
    showUploadDialog.value = false
    materialForm.title = ''
    materialForm.category = ''
    selectedFile.value = null
    if (uploadRef.value) {
      uploadRef.value.clearFiles()
    }
    loadMaterialList()
  } catch (err) {
    ElMessage.error('上传失败：' + (err.response?.data?.msg || err.message))
  } finally {
    uploading.value = false
  }
}

// 下载文件
const downloadMaterial = (row) => {
  const downloadUrl = `/api/material/download/${row.id}`
  const link = document.createElement('a')
  link.href = downloadUrl
  link.download = row.title
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
  ElMessage.success(`开始下载：${row.title}`)
  // 刷新列表以更新下载次数
  setTimeout(() => loadMaterialList(), 1000)
}

onMounted(() => loadMaterialList())
</script>

<style scoped>
.info-list {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
  background: #f5f7fa;
  min-height: calc(100vh - 60px);
}

.header-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

/* 筛选栏：不换行 + 美观 */
.filter-bar {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
  padding: 10px 14px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.06);
  flex-wrap: nowrap;
}

.filter-label {
  white-space: nowrap;
  color: #333;
  font-size: 14px;
}

.filter-select {
  min-width: 160px;
}

.search-bar {
  margin-bottom: 16px;
  padding: 12px 16px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.06);
}

.search-input {
  max-width: 400px;
}
</style>