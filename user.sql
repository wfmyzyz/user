/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80016
 Source Host           : localhost:3306
 Source Schema         : user

 Target Server Type    : MySQL
 Target Server Version : 80016
 File Encoding         : 65001

 Date: 23/02/2020 20:47:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for authority
-- ----------------------------
DROP TABLE IF EXISTS `authority`;
CREATE TABLE `authority`  (
  `authority_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '权限ID',
  `name` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限名',
  `url` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限地址',
  `type` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限类型：页面，按钮',
  `f_authority_id` int(11) NULL DEFAULT NULL COMMENT '父权限ID',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `display` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否显示：显示，隐藏',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `tb_status` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '正常' COMMENT '状态:正常，删除',
  PRIMARY KEY (`authority_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 45 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of authority
-- ----------------------------
INSERT INTO `authority` VALUES (5, '角色列表', '/back/admin/adminUser/roleList.html', '页面', 0, 0, '显示', '2020-01-18 15:28:39', '2020-02-12 23:14:23', '删除');
INSERT INTO `authority` VALUES (6, '获取角色列表接口', '/user/back/role/getRoleTreeList', '按钮', 5, 0, '显示', '2020-01-18 15:31:06', '2020-02-12 23:14:31', '删除');
INSERT INTO `authority` VALUES (7, '用户列表', '/back/admin/adminUser/userList.html', '页面', 0, 0, '显示', '2020-01-18 17:24:27', '2020-02-12 23:14:31', '删除');
INSERT INTO `authority` VALUES (8, '权限列表', '/back/admin/adminUser/authorityList.html', '页面', 0, 0, '显示', '2020-01-18 17:25:21', '2020-02-12 23:14:32', '删除');
INSERT INTO `authority` VALUES (9, '测试页面', '', '页面', 7, 0, '显示', '2020-02-08 15:38:50', '2020-02-12 23:14:32', '删除');
INSERT INTO `authority` VALUES (10, '测试子页面', 'testSon.html', '页面', 9, 0, '显示', '2020-02-08 15:43:04', '2020-02-12 23:14:33', '删除');
INSERT INTO `authority` VALUES (11, '管理员用户管理', '', '页面', 0, 1, '显示', '2020-02-10 14:09:54', '2020-02-17 17:14:41', '正常');
INSERT INTO `authority` VALUES (12, '用户列表', '/backstage/admin/adminUser/userList.html', '页面', 11, 0, '显示', '2020-02-10 14:11:39', '2020-02-17 18:27:59', '正常');
INSERT INTO `authority` VALUES (13, '权限列表', '/backstage/admin/adminUser/authorityList.html', '页面', 11, 0, '显示', '2020-02-10 14:24:43', '2020-02-17 18:28:07', '正常');
INSERT INTO `authority` VALUES (14, '角色列表', '/backstage/admin/adminUser/roleList.html', '页面', 11, 0, '显示', '2020-02-10 14:25:32', '2020-02-17 18:28:11', '正常');
INSERT INTO `authority` VALUES (15, '测试', '', '页面', 0, 0, '隐藏', '2020-02-12 23:27:10', '2020-02-12 23:39:56', '删除');
INSERT INTO `authority` VALUES (16, '后台主页面', '', '页面', 0, 0, '隐藏', '2020-02-17 14:47:47', '2020-02-21 21:59:37', '正常');
INSERT INTO `authority` VALUES (17, '菜单列表接口', '/user/back/authority/getMenuList', '按钮', 16, 0, '显示', '2020-02-17 15:02:28', '2020-02-17 15:03:27', '正常');
INSERT INTO `authority` VALUES (18, '分页获取用户列表接口', '/user/back/user/getUserList', '按钮', 12, 0, '显示', '2020-02-17 15:25:31', '2020-02-17 15:25:31', '正常');
INSERT INTO `authority` VALUES (19, '获取树状权限列表接口', '/user/back/authority/getAuthorityTreeList', '按钮', 13, 0, '显示', '2020-02-17 15:27:09', '2020-02-17 15:27:09', '正常');
INSERT INTO `authority` VALUES (20, '获取树状角色列表接口', '/user/back/role/getRoleTreeList', '按钮', 14, 0, '显示', '2020-02-17 15:27:50', '2020-02-17 15:27:50', '正常');
INSERT INTO `authority` VALUES (21, '添加新用户接口', '/user/back/user/addUser', '按钮', 12, 0, '显示', '2020-02-17 15:30:12', '2020-02-17 15:30:12', '正常');
INSERT INTO `authority` VALUES (22, '修改用户密码接口', '/user/back/user/updatePasswordByUserId', '按钮', 12, 0, '显示', '2020-02-17 15:31:26', '2020-02-17 15:31:26', '正常');
INSERT INTO `authority` VALUES (23, '修改用户冻结状态接口', '/user/back/user/updateUserStatus', '按钮', 12, 0, '显示', '2020-02-17 15:32:34', '2020-02-17 15:32:34', '正常');
INSERT INTO `authority` VALUES (24, '删除用户接口', '/user/back/user/deleteUser', '按钮', 12, 0, '显示', '2020-02-17 15:33:15', '2020-02-17 15:33:15', '正常');
INSERT INTO `authority` VALUES (25, '添加新权限接口', '/user/back/authority/addAuthority', '按钮', 13, 0, '显示', '2020-02-17 15:39:25', '2020-02-17 15:39:25', '正常');
INSERT INTO `authority` VALUES (26, '修改权限接口', '/user/back/authority/updateAuthority', '按钮', 13, 0, '显示', '2020-02-17 15:43:33', '2020-02-17 15:43:33', '正常');
INSERT INTO `authority` VALUES (27, '删除权限接口', '/user/back/authority/deleteAuthority', '按钮', 13, 0, '显示', '2020-02-17 15:46:18', '2020-02-17 15:46:18', '正常');
INSERT INTO `authority` VALUES (28, '根据ID获取权限详细接口', '/user/back/authority/getAuthorityById', '按钮', 13, 0, '显示', '2020-02-17 15:48:49', '2020-02-17 15:48:49', '正常');
INSERT INTO `authority` VALUES (29, '添加新角色接口', '/user/back/role/addRole', '按钮', 14, 0, '显示', '2020-02-17 15:54:52', '2020-02-17 15:55:06', '正常');
INSERT INTO `authority` VALUES (30, '修改角色接口', '/user/back/role/updateRoleName', '按钮', 14, 0, '显示', '2020-02-17 15:55:40', '2020-02-17 15:55:40', '正常');
INSERT INTO `authority` VALUES (31, '删除角色接口', '/user/back/role/deleteRole', '按钮', 14, 0, '显示', '2020-02-17 15:56:15', '2020-02-17 16:35:55', '正常');
INSERT INTO `authority` VALUES (32, '获取用户可操作角色列表接口', '/user/back/role/getRoleTreeListByUserId', '按钮', 12, 0, '显示', '2020-02-17 15:59:25', '2020-02-17 16:01:06', '正常');
INSERT INTO `authority` VALUES (33, '用户绑定角色接口', '/user/back/userRole/userBindRole', '按钮', 12, 0, '显示', '2020-02-17 16:00:27', '2020-02-17 16:00:27', '正常');
INSERT INTO `authority` VALUES (34, '获取角色可操作权限列表接口', '/user/back/authority/getAuthorityTreeByUserId', '按钮', 14, 0, '显示', '2020-02-17 16:02:46', '2020-02-17 16:02:46', '正常');
INSERT INTO `authority` VALUES (35, '角色绑定权限接口', 'user/back/roleAuthority/roleBindAuthority', '按钮', 14, 0, '显示', '2020-02-17 16:37:00', '2020-02-17 16:37:00', '正常');
INSERT INTO `authority` VALUES (36, '书籍管理', '', '页面', 0, 2, '显示', '2020-02-17 16:55:32', '2020-02-17 16:55:32', '正常');
INSERT INTO `authority` VALUES (37, '标签列表', '/backstage/admin/administrators/label/labelList.html', '页面', 36, 1, '显示', '2020-02-17 16:57:33', '2020-02-17 18:28:16', '正常');
INSERT INTO `authority` VALUES (38, '书籍列表', '/backstage/admin/book/bookList.html', '页面', 36, 2, '显示', '2020-02-17 17:05:41', '2020-02-17 18:28:21', '正常');
INSERT INTO `authority` VALUES (39, '章回列表', '/backstage/admin/book/serialList.html', '页面', 36, 3, '隐藏', '2020-02-17 17:06:25', '2020-02-17 18:39:31', '正常');
INSERT INTO `authority` VALUES (40, '前台管理', '', '页面', 0, 3, '显示', '2020-02-17 17:07:13', '2020-02-17 17:07:13', '正常');
INSERT INTO `authority` VALUES (41, '轮播图列表', '/backstage/admin/index/rotationList.html', '页面', 40, 1, '显示', '2020-02-17 17:10:42', '2020-02-17 18:28:31', '正常');
INSERT INTO `authority` VALUES (42, 'qqq', '', '页面', 0, 4, '显示', '2020-02-21 21:05:55', '2020-02-21 21:11:28', '删除');
INSERT INTO `authority` VALUES (43, 'zzz', 'www.baidu.com', '页面', 42, 0, '显示', '2020-02-21 21:09:56', '2020-02-21 21:11:28', '删除');
INSERT INTO `authority` VALUES (44, '菜单接口', '', '按钮', 11, 0, '显示', '2020-02-21 21:38:01', '2020-02-21 21:58:41', '删除');
INSERT INTO `authority` VALUES (45, '百度', 'http://www.baidu.com', '页面', 40, 0, '显示', '2020-02-21 21:47:16', '2020-02-21 21:47:27', '删除');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `name` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名',
  `f_role_id` int(11) NULL DEFAULT NULL COMMENT '父角色ID',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `tb_status` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '正常' COMMENT '状态:正常,删除',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, '超级管理员', 0, '2020-01-03 17:05:11', '2020-01-03 17:05:11', '正常');
INSERT INTO `role` VALUES (28, '开发管理员', 1, '2020-01-18 15:25:46', '2020-01-18 15:25:46', '正常');
INSERT INTO `role` VALUES (29, '普通管理员', 28, '2020-02-21 21:13:27', '2020-02-21 21:15:45', '删除');
INSERT INTO `role` VALUES (30, '普通管理员', 28, '2020-02-21 21:15:53', '2020-02-21 21:15:53', '正常');

-- ----------------------------
-- Table structure for role_authority
-- ----------------------------
DROP TABLE IF EXISTS `role_authority`;
CREATE TABLE `role_authority`  (
  `role_authority_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色权限ID',
  `role_id` int(11) NULL DEFAULT NULL COMMENT '角色ID',
  `authority_id` int(11) NULL DEFAULT NULL COMMENT '权限ID',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `tb_status` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '正常' COMMENT '状态:正常，删除',
  PRIMARY KEY (`role_authority_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 40 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_authority
-- ----------------------------
INSERT INTO `role_authority` VALUES (9, 28, 6, '2020-01-18 15:31:20', '2020-02-10 14:33:47', '删除');
INSERT INTO `role_authority` VALUES (10, 28, 5, '2020-02-08 17:17:42', '2020-02-10 14:25:23', '删除');
INSERT INTO `role_authority` VALUES (11, 28, 11, '2020-02-10 14:33:48', '2020-02-10 14:33:48', '正常');
INSERT INTO `role_authority` VALUES (12, 28, 12, '2020-02-10 14:33:48', '2020-02-10 14:33:48', '正常');
INSERT INTO `role_authority` VALUES (13, 28, 13, '2020-02-10 14:33:48', '2020-02-10 14:33:48', '正常');
INSERT INTO `role_authority` VALUES (14, 28, 14, '2020-02-10 14:33:48', '2020-02-10 14:33:48', '正常');
INSERT INTO `role_authority` VALUES (15, 28, 16, '2020-02-17 15:10:00', '2020-02-17 15:10:00', '正常');
INSERT INTO `role_authority` VALUES (16, 28, 17, '2020-02-17 15:10:01', '2020-02-17 15:10:01', '正常');
INSERT INTO `role_authority` VALUES (17, 28, 18, '2020-02-17 16:37:44', '2020-02-17 16:37:44', '正常');
INSERT INTO `role_authority` VALUES (18, 28, 19, '2020-02-17 16:37:45', '2020-02-17 16:37:45', '正常');
INSERT INTO `role_authority` VALUES (19, 28, 20, '2020-02-17 16:37:45', '2020-02-17 16:37:45', '正常');
INSERT INTO `role_authority` VALUES (20, 28, 21, '2020-02-17 16:37:45', '2020-02-17 16:37:45', '正常');
INSERT INTO `role_authority` VALUES (21, 28, 22, '2020-02-17 16:37:45', '2020-02-17 16:37:45', '正常');
INSERT INTO `role_authority` VALUES (22, 28, 23, '2020-02-17 16:37:45', '2020-02-17 16:37:45', '正常');
INSERT INTO `role_authority` VALUES (23, 28, 24, '2020-02-17 16:37:45', '2020-02-17 16:37:45', '正常');
INSERT INTO `role_authority` VALUES (24, 28, 25, '2020-02-17 16:37:45', '2020-02-17 16:37:45', '正常');
INSERT INTO `role_authority` VALUES (25, 28, 26, '2020-02-17 16:37:45', '2020-02-17 16:37:45', '正常');
INSERT INTO `role_authority` VALUES (26, 28, 27, '2020-02-17 16:37:45', '2020-02-17 16:37:45', '正常');
INSERT INTO `role_authority` VALUES (27, 28, 28, '2020-02-17 16:37:46', '2020-02-17 16:37:46', '正常');
INSERT INTO `role_authority` VALUES (28, 28, 29, '2020-02-17 16:37:46', '2020-02-17 16:37:46', '正常');
INSERT INTO `role_authority` VALUES (29, 28, 30, '2020-02-17 16:37:46', '2020-02-17 16:37:46', '正常');
INSERT INTO `role_authority` VALUES (30, 28, 31, '2020-02-17 16:37:46', '2020-02-17 16:37:46', '正常');
INSERT INTO `role_authority` VALUES (31, 28, 32, '2020-02-17 16:37:46', '2020-02-17 16:37:46', '正常');
INSERT INTO `role_authority` VALUES (32, 28, 33, '2020-02-17 16:37:46', '2020-02-17 16:37:46', '正常');
INSERT INTO `role_authority` VALUES (33, 28, 34, '2020-02-17 16:37:46', '2020-02-17 16:37:46', '正常');
INSERT INTO `role_authority` VALUES (34, 28, 35, '2020-02-17 16:37:46', '2020-02-17 16:37:46', '正常');
INSERT INTO `role_authority` VALUES (35, 30, 36, '2020-02-21 21:19:00', '2020-02-21 21:19:00', '正常');
INSERT INTO `role_authority` VALUES (36, 30, 16, '2020-02-21 22:22:53', '2020-02-21 22:22:53', '正常');
INSERT INTO `role_authority` VALUES (37, 30, 17, '2020-02-21 22:22:54', '2020-02-21 22:22:54', '正常');
INSERT INTO `role_authority` VALUES (38, 30, 37, '2020-02-21 22:22:54', '2020-02-21 22:22:54', '正常');
INSERT INTO `role_authority` VALUES (39, 30, 38, '2020-02-21 22:22:54', '2020-02-21 22:22:54', '正常');
INSERT INTO `role_authority` VALUES (40, 30, 39, '2020-02-21 22:22:54', '2020-02-21 22:22:54', '正常');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户账号',
  `password` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `salt` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '盐值',
  `status` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '正常' COMMENT '账号状态：正常，冻结',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `tb_status` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '正常' COMMENT '状态:正常,删除',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', '0296ad1f8b74ea72b6cf7f619b221600', 'qAa_u.QgOK&3 &;~\\r3TvdXdi_', '正常', '2019-12-20 14:51:37', '2020-01-07 17:44:58', '正常');
INSERT INTO `user` VALUES (4, 'test', '1e694172146bc67613a2d793098fa56e', '~*`,S:+:ibD(1qe84Pp9M(1[4', '正常', '2020-01-18 14:32:47', '2020-01-18 14:32:47', '正常');
INSERT INTO `user` VALUES (5, 'test1', '594a81ad83d303230f31006212e04fd4', 'zYFa\'#k{xEc~Hm6WE8aE[3qmRg', '正常', '2020-01-18 14:34:38', '2020-01-18 14:34:38', '正常');
INSERT INTO `user` VALUES (6, 'mgc', '73d5b77528007256b9a8201a4a29e8c1', '!&Ys\'86\"|(5Er$]&THSL7AM 0;*', '正常', '2020-02-21 20:45:18', '2020-02-21 21:57:42', '正常');

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `user_role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户角色ID',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户ID',
  `role_id` int(11) NULL DEFAULT NULL COMMENT '角色ID',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `tb_status` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '正常' COMMENT '状态:正常，删除',
  PRIMARY KEY (`user_role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (1, 1, 1, '2020-01-05 11:21:38', '2020-01-05 11:54:06', '正常');
INSERT INTO `user_role` VALUES (14, 4, 28, '2020-01-18 15:25:54', '2020-01-18 15:25:54', '正常');
INSERT INTO `user_role` VALUES (15, 6, 30, '2020-02-21 22:13:43', '2020-02-21 22:13:43', '正常');

SET FOREIGN_KEY_CHECKS = 1;
