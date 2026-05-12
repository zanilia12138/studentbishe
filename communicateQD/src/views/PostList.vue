<template>
  <div class="post-list">
    <div class="header-bar">
      <h2>💬 备考社区 · 帖子列表</h2>
      <el-button type="primary" @click="showAddDialog = true">+ 发布帖子</el-button>
    </div>

    <div class="search-bar">
      <el-input
        v-model="searchKeyword"
        placeholder="搜索帖子标题关键词"
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

    <div class="post-items">
      <div v-for="item in filteredPostList" :key="item.id" class="post-item">
        <div class="post-item-head">
          <h3>{{ item.title }}</h3>
          <el-image
            v-if="item.image_url"
            :src="publicImageUrl(item.image_url)"
            :preview-src-list="[publicImageUrl(item.image_url)]"
            fit="cover"
            class="list-thumb"
            preview-teleported
          />
        </div>
        <p class="meta">
          <span>{{ item.user_name }}</span> ·
          <span>{{ item.createTime }}</span>
        </p>
        <p class="content">{{ contentPreview(item) }}</p>
        <el-button type="text" @click="goToDetail(item.id)">查看详情 →</el-button>
      </div>
      <div v-if="filteredPostList.length === 0" class="empty-tip">暂无帖子</div>
    </div>

    <el-dialog v-model="showAddDialog" title="发布帖子" width="50%" @closed="onPostDialogClosed">
      <el-form :model="postForm" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="postForm.title" />
        </el-form-item>
        <el-form-item label="内容">
          <el-input type="textarea" v-model="postForm.content" rows="6" />
        </el-form-item>
        <el-form-item label="配图">
          <el-upload
            ref="postUploadRef"
            action="#"
            :auto-upload="false"
            :limit="1"
            accept="image/jpeg,image/png,image/gif,image/webp"
            :on-change="onPostImageChange"
            :on-exceed="() => ElMessage.warning('最多选择 1 张图片')"
            list-type="picture-card"
          >
            <el-icon><Plus /></el-icon>
            <template #tip>
              <div class="el-upload__tip">可选，支持 jpg/png/gif/webp</div>
            </template>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="submitPost">发布</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { Search, Plus } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../utils/request'

const router = useRouter()
const postList = ref([])
const showAddDialog = ref(false)
const submitting = ref(false)
const searchKeyword = ref('')
const postUploadRef = ref(null)
const postImageFile = ref(null)

const defaultUserId = () => Number(localStorage.getItem('userId')) || 2

const publicImageUrl = (path) => {
  if (!path) return ''
  if (path.startsWith('http://') || path.startsWith('https://')) return path
  return path.startsWith('/') ? path : `/${path}`
}

const contentPreview = (item) => {
  const t = item.content || ''
  if (t.length <= 80) return t
  return `${t.slice(0, 80)}...`
}

const onPostImageChange = (file, fileList) => {
  postImageFile.value = fileList.length ? file.raw : null
}

const onPostDialogClosed = () => {
  postImageFile.value = null
  postUploadRef.value?.clearFiles()
}

const filteredPostList = computed(() => {
  if (!searchKeyword.value.trim()) {
    return postList.value
  }
  const keyword = searchKeyword.value.toLowerCase()
  return postList.value.filter(item =>
    item.title && item.title.toLowerCase().includes(keyword)
  )
})

const handleSearch = () => {
  // do nothing
}

const postForm = ref({
  title: '',
  content: '',
  userId: defaultUserId()
})

const loadPostList = async () => {
  try {
    const res = await request.get('/post/list')
    postList.value = res.data
  } catch (err) {
    ElMessage.error('加载帖子失败')
  }
}

const submitPost = async () => {
  if (!postForm.value.title || !postForm.value.content)
    return ElMessage.warning('请填写标题和内容')
  submitting.value = true
  try {
    let imageUrl = null
    if (postImageFile.value) {
      const fd = new FormData()
      fd.append('file', postImageFile.value)
      const up = await request.post('/post/uploadImage', fd)
      if (!up || up.code !== 200) {
        throw new Error(up?.message || '图片上传失败')
      }
      imageUrl = up.data
    }

    await request.post('/post/add', {
      ...postForm.value,
      imageUrl
    })
    ElMessage.success('发布成功')
    showAddDialog.value = false
    postForm.value = { title: '', content: '', userId: defaultUserId() }
    postImageFile.value = null
    postUploadRef.value?.clearFiles()
    loadPostList()
  } catch (err) {
    ElMessage.error(err.message || '发布失败')
  } finally {
    submitting.value = false
  }
}

const goToDetail = (id) => router.push(`/post-detail/${id}`)

onMounted(() => loadPostList())
</script>

<style scoped>
.post-list {
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

.post-item {
  background: white;
  padding: 20px;
  border-radius: 12px;
  margin-bottom: 16px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
  transition: all 0.3s;
}
.post-item:hover {
  box-shadow: 0 4px 16px rgba(0,0,0,0.12);
}
.post-item-head {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 12px;
}
.post-item-head h3 {
  flex: 1;
  min-width: 0;
  margin: 0 0 8px;
  color: #333;
}
.list-thumb {
  width: 72px;
  height: 72px;
  border-radius: 8px;
  flex-shrink: 0;
}
.meta {
  color: #999;
  font-size: 13px;
  margin-bottom: 8px;
}
.content {
  color: #666;
  line-height: 1.6;
}
</style>