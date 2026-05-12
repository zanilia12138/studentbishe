<template>
  <div class="admin-home">
    <h2>🔧 管理员后台</h2>
    <el-tabs>
      <el-tab-pane label="用户管理">
        <el-table :data="userList" stripe>
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
              <el-button size="small" :type="row.status === 1 ? 'warning' : 'success'"
                @click="toggleStatus(row)">
                {{ row.status === 1 ? '禁用' : '启用' }}
              </el-button>
              <el-button type="danger" size="small" @click="deleteUser(row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="公告管理">
        <el-button type="primary" @click="showAnnounceDialog = true">+ 发布公告</el-button>
        <el-table :data="announceList" stripe style="margin-top:16px">
          <el-table-column prop="title" label="标题" />
          <el-table-column prop="content" label="内容" />
          <el-table-column prop="createTime" label="发布时间" width="180" />
        </el-table>
      </el-tab-pane>
    </el-tabs>

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

const userList = ref([])
const announceList = ref([])
const showAnnounceDialog = ref(false)

const announceForm = reactive({
  title: '',
  content: '',
  adminId: 1
})

const loadUserList = async () => {
  try {
    const res = await request.get('/user/list?pageNum=1&pageSize=100')
    userList.value = res.data.records || res.data
  } catch (err) {
    ElMessage.error('加载用户失败')
  }
}

const loadAnnounceList = async () => {
  try {
    const res = await request.get('/announcement/list')
    announceList.value = res.data
  } catch (err) {
    ElMessage.error('加载公告失败')
  }
}

const toggleStatus = (row) => {
  row.status = row.status === 1 ? 0 : 1
  request.post('/user/updateStatus', row).then(() => {
    ElMessage.success('状态更新成功')
    loadUserList()
  })
}

const deleteUser = (id) => {
  ElMessage.warning('确认删除该用户？').then(() => {
    request.delete(`/user/delete/${id}`).then(() => {
      ElMessage.success('删除成功')
      loadUserList()
    })
  })
}

const publishAnnounce = async () => {
  if (!announceForm.title || !announceForm.content)
    return ElMessage.warning('请填写标题和内容')
  try {
    await request.post('/announcement/publish', announceForm)
    ElMessage.success('发布成功')
    showAnnounceDialog.value = false
    Object.assign(announceForm, { title: '', content: '' })
    loadAnnounceList()
  } catch (err) {
    ElMessage.error('发布失败')
  }
}

onMounted(() => {
  loadUserList()
  loadAnnounceList()
})
</script>

<style scoped>
.admin-home {
  padding: 20px;
  max-width: 1400px;
  margin: 0 auto;
  background: #f5f7fa;
  min-height: calc(100vh - 60px);
}
</style>