<template>
  <div class="post-detail">
    <!-- 加载状态 -->
    <div v-if="loading" class="loading">
      <el-spin size="large" tip="加载中..." />
    </div>

    <!-- 帖子内容 -->
    <div v-else class="post-container">
      <!-- 帖子头部 -->
      <div class="post-card">
        <h1 class="post-title">{{ post.title }}</h1>
        <div class="post-info">
          <span class="author">{{ post.user_name }}</span>
          <span class="time">{{ post.createTime }}</span>
        </div>
      </div>

      <!-- 帖子内容 -->
      <div class="content-card">
        <p class="post-content">{{ post.content }}</p>
      </div>

      <!-- 评论区 -->
      <div class="comment-card">
        <h3>💬 评论 ({{ commentList.length }})</h3>

        <!-- 发布评论 -->
        <div class="comment-publish">
          <el-input
            v-model="newComment"
            type="textarea"
            :rows="3"
            placeholder="分享你的观点..."
          />
          <el-button
            type="primary"
            :loading="submitting"
            @click="sendComment"
            class="submit-btn"
          >
            发布评论
          </el-button>
        </div>

        <!-- 评论列表 -->
        <div class="comment-list">
          <div
            v-for="item in commentList"
            :key="item.id"
            class="comment-item"
          >
            <div class="comment-user">{{ item.user_name }}</div>
            <div class="comment-text">{{ item.content }}</div>
            <div class="comment-time">{{ item.createTime }}</div>
          </div>

          <div v-if="commentList.length === 0" class="empty">
            暂无评论，快来抢沙发吧～
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../utils/request'

const route = useRoute()
const postId = route.params.id // 自动获取路由里的帖子ID

const post = ref({})
const commentList = ref([])
const newComment = ref('')
const loading = ref(true)
const submitting = ref(false)

// 加载帖子详情
const getPostDetail = async () => {
  try {
    const res = await request.get(`/post/detail/${postId}`)
    post.value = res.data || {}
  } catch (err) {
    ElMessage.error('帖子加载失败')
  }
}

// 加载评论
const getComments = async () => {
  try {
    const res = await request.get(`/comment/list/${postId}`)
    commentList.value = res.data || []
  } catch (err) {
    ElMessage.error('评论加载失败')
  } finally {
    loading.value = false
  }
}

// 发布评论
const sendComment = async () => {
  if (!newComment.value.trim()) {
    return ElMessage.warning('请输入评论内容')
  }

  submitting.value = true
  try {
    await request.post('/comment/add', {
      postId: postId,
      userId: localStorage.getItem('userId') || 2,
      content: newComment.value
    })
    ElMessage.success('评论成功')
    newComment.value = ''
    getComments()
  } catch (err) {
    ElMessage.error('评论失败')
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  getPostDetail()
  getComments()
})
</script>

<style scoped>
.post-detail {
  width: 100%;
  min-height: calc(100vh - 60px);
  background: #f5f7fa;
  padding: 30px 20px;
}

.loading {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 50vh;
}

.post-container {
  max-width: 900px;
  margin: 0 auto;
}

.post-card {
  background: #fff;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  margin-bottom: 20px;
}

.post-title {
  font-size: 24px;
  color: #333;
  margin: 0 0 12px 0;
  line-height: 1.4;
}

.post-info {
  color: #999;
  font-size: 14px;
  display: flex;
  gap: 16px;
}

.author {
  color: #409eff;
  font-weight: 500;
}

.content-card {
  background: #fff;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  margin-bottom: 20px;
}

.post-content {
  font-size: 16px;
  color: #555;
  line-height: 1.8;
  margin: 0;
}

.comment-card {
  background: #fff;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.comment-card h3 {
  font-size: 18px;
  color: #333;
  margin-bottom: 16px;
}

.comment-publish {
  margin-bottom: 24px;
}

.submit-btn {
  margin-top: 10px;
  background: #409eff;
  float: right;
}

.comment-item {
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.comment-user {
  font-size: 14px;
  color: #409eff;
  font-weight: 500;
  margin-bottom: 4px;
}

.comment-text {
  font-size: 15px;
  color: #555;
  line-height: 1.6;
  margin-bottom: 4px;
}

.comment-time {
  font-size: 12px;
  color: #bbb;
}

.empty {
  text-align: center;
  padding: 40px 0;
  color: #999;
}
</style>