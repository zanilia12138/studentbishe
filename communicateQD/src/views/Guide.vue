<template>
  <div class="announcement-container">
    <div class="announcement-box">
      <h2>📚 备考学生交流平台使用指南</h2>
      
      <div class="guide-section">
        <h3>一、账号与登录</h3>
        <ul>
          <li>新用户请点击「没有登录账户？请注册！」完成账号注册，默认角色为学生。</li>
          <li>登录时请输入正确的用户名和密码，登录成功后会根据角色跳转至对应首页。</li>
          <li>管理员账号拥有后台管理权限，可管理用户、帖子及学习资料。</li>
        </ul>
      </div>

      <div class="guide-section">
        <h3>二、核心功能使用</h3>
        <div class="sub-section">
          <h4>1. 社区帖子交流</h4>
          <ul>
            <li>在「社区帖子」页面可浏览、发布考研/考公相关经验分享、问题求助。</li>
            <li>发布帖子时请填写标题、内容，选择相关标签便于分类检索。</li>
            <li>可对他人帖子进行评论、点赞，共同交流备考心得。</li>
          </ul>
        </div>
        <div class="sub-section">
          <h4>2. 学习资料共享</h4>
          <ul>
            <li>「学习资料」板块提供考研公共课、专业课及考公行测/申论资料下载。</li>
            <li>用户可上传优质资料，经管理员审核后展示给全体用户。</li>
            <li>请尊重知识产权，仅分享合法合规的学习资源。</li>
          </ul>
        </div>
        <div class="sub-section">
          <h4>3. 打卡与自律</h4>
          <ul>
            <li>每日打卡功能记录你的学习时长，形成个人学习日历。</li>
            <li>查看排行榜与其他考生比拼学习进度，互相监督共同进步。</li>
          </ul>
        </div>
      </div>

      <div class="guide-section">
        <h3>三、备考方向分类</h3>
        <div class="direction-grid">
          <div class="direction-card">
            <h4>🎓 考研专区</h4>
            <p>覆盖英语、政治、数学公共课及各专业专业课，提供院校信息、真题解析、复试经验。</p>
          </div>
          <div class="direction-card">
            <h4>📝 考公专区</h4>
            <p>聚焦行测技巧、申论写作、岗位选择，分享国考/省考备考规划与上岸经验。</p>
          </div>
        </div>
      </div>

      <div class="guide-section">
        <h3>四、社区规范</h3>
        <ul>
          <li>禁止发布涉政、低俗、广告等违规内容，违者将被封禁账号。</li>
          <li>交流时请理性友善，尊重不同备考方向的考生，避免引战争吵。</li>
          <li>若发现违规内容，可点击举报按钮，管理员将及时处理。</li>
        </ul>
      </div>

      <div class="guide-section">
        <h3>五、常见问题</h3>
        <ul>
          <li><strong>Q：忘记密码怎么办？</strong><br>A：请联系管理员重置密码，后续将开放自助找回功能。</li>
          <li><strong>Q：如何修改个人信息？</strong><br>A：进入「个人首页」可修改昵称、头像等信息，用户名不可修改。</li>
          <li><strong>Q：资料上传后为何不显示？</strong><br>A：新上传资料需管理员审核通过后才会展示，请耐心等待。</li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup>
// 无需额外逻辑，纯展示页面
</script>

<style scoped>
/* 与登录/注册页保持视觉一致的容器样式 */
.announcement-container {
  width: 100%;
  min-height: calc(100vh - 60px); /* 减去导航栏高度 */
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
  max-width: 900px;
  width: 100%;
}

.announcement-box h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
  font-size: 24px;
}

.guide-section {
  margin-bottom: 30px;
}

.guide-section h3 {
  color: #409eff;
  margin-bottom: 15px;
  font-size: 20px;
  border-left: 4px solid #409eff;
  padding-left: 10px;
}

.sub-section h4 {
  color: #333;
  margin: 15px 0 10px;
  font-size: 18px;
}

.guide-section ul {
  padding-left: 20px;
  line-height: 1.8;
  color: #666;
}

.guide-section li {
  margin-bottom: 8px;
}

.direction-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  margin-top: 15px;
}

.direction-card {
  background: #f5f7fa;
  padding: 20px;
  border-radius: 8px;
  border-left: 4px solid #409eff;
}

.direction-card h4 {
  margin-top: 0;
  color: #409eff;
}

.direction-card p {
  color: #666;
  line-height: 1.6;
  margin: 10px 0 0;
}
</style>