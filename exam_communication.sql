/*
 Navicat Premium Dump SQL

 Source Server         : Myseif
 Source Server Type    : MySQL
 Source Server Version : 50727 (5.7.27)
 Source Host           : localhost:3306
 Source Schema         : exam_communication

 Target Server Type    : MySQL
 Target Server Version : 50727 (5.7.27)
 File Encoding         : 65001

 Date: 27/03/2026 18:12:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for announcement
-- ----------------------------
DROP TABLE IF EXISTS `announcement`;
CREATE TABLE `announcement`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
  `admin_id` int(11) NOT NULL COMMENT '发布管理员ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `admin_id`(`admin_id`) USING BTREE,
  CONSTRAINT `announcement_ibfk_1` FOREIGN KEY (`admin_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '公告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of announcement
-- ----------------------------
INSERT INTO `announcement` VALUES (1, '2025考研报名时间提醒', '2025年全国硕士研究生招生考试网上报名时间为2024年10月8日至10月25日，请各位考生及时完成报名', 1, '2026-03-25 10:42:12');
INSERT INTO `announcement` VALUES (2, '2024国考笔试时间通知', '2024年国家公务员考试笔试时间为2024年11月23日-24日，请各位考生提前打印准考证，做好考试准备', 1, '2026-03-25 10:42:12');
INSERT INTO `announcement` VALUES (3, '平台资料更新通知', '近期已上传2024年国考行测真题、考研政治核心笔记等资料，欢迎大家下载学习', 1, '2026-03-25 10:42:12');
INSERT INTO `announcement` VALUES (4, '纪念考研导师，张先生', '考研界少了一位重要人物', 1, '2026-03-25 22:30:23');

-- ----------------------------
-- Table structure for clock_in
-- ----------------------------
DROP TABLE IF EXISTS `clock_in`;
CREATE TABLE `clock_in`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '打卡ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `duration` int(11) NULL DEFAULT NULL COMMENT '学习时长（分钟）',
  `content` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '学习内容',
  `image_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '打卡配图相对路径',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '打卡时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `clock_in_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '学习打卡表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of clock_in
-- ----------------------------
INSERT INTO `clock_in` VALUES (11, 2, 120, '复习考研英语阅读，做2018年真题', NULL, '2026-03-25 10:42:06');
INSERT INTO `clock_in` VALUES (12, 2, 90, '背诵考研政治马原部分', NULL, '2026-03-25 10:42:06');
INSERT INTO `clock_in` VALUES (13, 3, 150, '刷计算机408数据结构选择题', NULL, '2026-03-25 10:42:06');
INSERT INTO `clock_in` VALUES (14, 3, 60, '整理考研政治错题本', NULL, '2026-03-25 10:42:06');
INSERT INTO `clock_in` VALUES (15, 4, 180, '学习教育学考研专业课教育学原理', NULL, '2026-03-25 10:42:06');
INSERT INTO `clock_in` VALUES (16, 5, 120, '刷行测言语理解100题', NULL, '2026-03-25 10:42:06');
INSERT INTO `clock_in` VALUES (17, 5, 90, '学习申论归纳概括题技巧', NULL, '2026-03-25 10:42:06');
INSERT INTO `clock_in` VALUES (18, 6, 150, '刷行测判断推理题', NULL, '2026-03-25 10:42:06');
INSERT INTO `clock_in` VALUES (19, 6, 60, '背诵申论金句', NULL, '2026-03-25 10:42:06');
INSERT INTO `clock_in` VALUES (20, 7, 120, '复习公务员常识判断', NULL, '2026-03-25 10:42:06');
INSERT INTO `clock_in` VALUES (21, 2, 60, 'java', NULL, '2026-03-25 22:34:29');
INSERT INTO `clock_in` VALUES (22, 2, 70, 'vue', NULL, '2026-03-26 15:32:41');
INSERT INTO `clock_in` VALUES (23, 2, 60, 'vue3', NULL, '2026-03-26 15:33:48');
INSERT INTO `clock_in` VALUES (24, 2, 30, 'vue4', NULL, '2026-03-27 11:48:13');
INSERT INTO `clock_in` VALUES (25, 5, 60, '321', NULL, '2026-03-27 12:16:31');

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `post_id` int(11) NOT NULL COMMENT '关联帖子ID',
  `user_id` int(11) NOT NULL COMMENT '评论人ID',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论内容',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_post_id`(`post_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '帖子评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES (1, 4, 3, '我也遇到了这个问题，同求解答！', '2026-03-25 18:04:58');
INSERT INTO `comment` VALUES (2, 9, 4, '可以试试定位原文，同义替换~', '2026-03-25 18:04:58');
INSERT INTO `comment` VALUES (3, 3, 2, '肖八只做选择题就行，主观题看思路', '2026-03-25 18:04:58');
INSERT INTO `comment` VALUES (4, 11, 5, '跨考计算机加油！我也是双非上岸的', '2026-03-25 18:04:58');
INSERT INTO `comment` VALUES (5, 12, 6, '教育学考研竞争很激烈，要早点准备', '2026-03-25 18:04:58');
INSERT INTO `comment` VALUES (6, 13, 2, '我也是越做越错，心态崩了...', '2026-03-25 18:04:58');
INSERT INTO `comment` VALUES (7, 14, 7, '开头可以用名言或者热点引入', '2026-03-25 18:04:58');
INSERT INTO `comment` VALUES (8, 15, 3, '可以同时准备，我身边有成功的例子', '2026-03-25 18:04:58');
INSERT INTO `comment` VALUES (9, 5, 2, '我也饿', '2026-03-25 22:49:06');
INSERT INTO `comment` VALUES (10, 19, 8, '?', '2026-03-27 17:53:21');

-- ----------------------------
-- Table structure for material
-- ----------------------------
DROP TABLE IF EXISTS `material`;
CREATE TABLE `material`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '资料ID',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资料标题',
  `category` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类（真题/笔记/课件）',
  `user_id` int(11) NOT NULL COMMENT '上传人ID',
  `file_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件存储路径',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  `download_count` int(11) NULL DEFAULT 0 COMMENT '下载次数',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `material_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '学习资料表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of material
-- ----------------------------
INSERT INTO `material` VALUES (11, '2025考研英语历年真题（2010-2024）.pdf', '考公', 2, '/files/kaoyan/english_2010_2024.pdf', '2026-03-25 10:41:47', 0);
INSERT INTO `material` VALUES (12, '考研政治核心考点背诵手册.docx', '笔记', 3, '/files/kaoyan/politics_core_notes.docx', '2026-03-25 10:41:47', 0);
INSERT INTO `material` VALUES (13, '计算机408考研历年真题及解析.pdf', '考公', 4, '/files/kaoyan/408_exam_analysis.pdf', '2026-03-25 10:41:47', 0);
INSERT INTO `material` VALUES (14, '教育学考研专业课重点笔记.pdf', '笔记', 4, '/files/kaoyan/education_notes.pdf', '2026-03-25 10:41:47', 0);
INSERT INTO `material` VALUES (15, '考研数学高数基础班课件.pptx', '课件', 2, '/files/kaoyan/math_basic_course.pptx', '2026-03-25 10:41:47', 0);
INSERT INTO `material` VALUES (16, '2024国考行测历年真题（地市级）.pdf', '考研', 5, '/files/kaogong/xingce_2024.pdf', '2026-03-25 10:41:47', 0);
INSERT INTO `material` VALUES (17, '申论高分范文100篇.docx', '题库', 6, '/files/kaogong/shenlun_essays.docx', '2026-03-25 10:41:47', 0);
INSERT INTO `material` VALUES (18, '公务员考试常识判断高频考点.pdf', '笔记', 7, '/files/kaogong/common_sense_notes.pdf', '2026-03-25 10:41:47', 0);
INSERT INTO `material` VALUES (19, '行测判断推理技巧总结.pptx', '课件', 5, '/files/kaogong/xingce_skills.pptx', '2026-03-25 10:41:47', 0);
INSERT INTO `material` VALUES (20, '国考岗位报考指南.docx', '笔记', 6, '/files/kaogong/post_guide.docx', '2026-03-25 10:41:47', 0);

-- ----------------------------
-- Table structure for post
-- ----------------------------
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '帖子ID',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '帖子标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '帖子内容',
  `user_id` int(11) NOT NULL COMMENT '发布人ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '社区帖子表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of post
-- ----------------------------
INSERT INTO `post` VALUES (1, '学习经验分享', '分享一下我的学习方法，大家一起进步！', 5, '2026-03-25 18:02:06');
INSERT INTO `post` VALUES (2, '考研资料求购', '有没有26届考研的同学，求数学资料~', 2, '2026-03-25 18:02:06');
INSERT INTO `post` VALUES (3, '四六级备考交流', '大家四六级准备得怎么样了，一起交流下', 3, '2026-03-25 18:02:06');
INSERT INTO `post` VALUES (4, '课程作业互助', '谁会做Java课程设计呀，求指导！', 2, '2026-03-25 18:02:06');
INSERT INTO `post` VALUES (5, '我好饿...', '好想吃金汤酸菜鱼...', 4, '2026-03-25 22:35:02');
INSERT INTO `post` VALUES (6, '?', '?', 2, '2026-03-27 11:04:12');
INSERT INTO `post` VALUES (7, '!', '!', 2, '2026-03-27 11:04:33');
INSERT INTO `post` VALUES (8, '一次性彻底解决', '一次性彻底解决', 2, '2026-03-27 11:32:05');
INSERT INTO `post` VALUES (9, '一次性彻底解决', '一次性彻底解决', 2, '2026-03-27 11:32:38');
INSERT INTO `post` VALUES (10, '一次性彻底解决', '一次性彻底解决', 1, '2026-03-27 11:33:06');
INSERT INTO `post` VALUES (11, '一次性彻底解决', '一次性彻底解决', 2, '2026-03-27 11:34:25');
INSERT INTO `post` VALUES (12, '一次性彻底解决', '一次性彻底解决', 1, '2026-03-27 11:34:25');
INSERT INTO `post` VALUES (13, '一次性彻底解决', '一次性彻底解决', 2, '2026-03-27 11:34:30');
INSERT INTO `post` VALUES (14, '一次性彻底解决', '一次性彻底解决', 1, '2026-03-27 11:34:51');
INSERT INTO `post` VALUES (15, 'localStorage', 'localStorage', 2, '2026-03-27 11:40:32');
INSERT INTO `post` VALUES (16, 'postForm', 'postForm', 2, '2026-03-27 11:43:19');
INSERT INTO `post` VALUES (17, 'userId', 'userId', 5, '2026-03-27 12:13:38');
INSERT INTO `post` VALUES (18, '1', '1', 4, '2026-03-27 13:01:08');
INSERT INTO `post` VALUES (19, '2', '2', 8, '2026-03-27 17:53:16');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '账号',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `role` tinyint(4) NOT NULL DEFAULT 1 COMMENT '角色 1=学生 2=管理员',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态 1=正常 0=禁用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', '123456', '系统管理员', 2, 1, '2026-03-25 10:41:41');
INSERT INTO `user` VALUES (2, 'student1', '123456', '考研小张', 1, 0, '2026-03-25 10:41:41');
INSERT INTO `user` VALUES (3, 'student2', '123456', '考研小李', 1, 1, '2026-03-25 10:41:41');
INSERT INTO `user` VALUES (4, 'student3', '123456', '教育学考研人', 1, 1, '2026-03-25 10:41:41');
INSERT INTO `user` VALUES (5, 'student4', '123456', '考公小王', 1, 1, '2026-03-25 10:41:41');
INSERT INTO `user` VALUES (6, 'student5', '123456', '行测刷题达人', 1, 1, '2026-03-25 10:41:41');
INSERT INTO `user` VALUES (7, 'student6', '123456', '申论写作能手', 1, 1, '2026-03-25 10:41:41');
INSERT INTO `user` VALUES (8, 'yyy', '123456', 'yy', 1, 1, '2026-03-27 17:52:51');

SET FOREIGN_KEY_CHECKS = 1;

-- 若数据库由旧版初始化、尚无配图列，可手动执行：
-- ALTER TABLE `clock_in` ADD COLUMN `image_url` varchar(512) NULL DEFAULT NULL COMMENT '打卡配图相对路径' AFTER `content`;
