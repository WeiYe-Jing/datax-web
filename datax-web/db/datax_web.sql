/*
 Navicat Premium Data Transfer

 Source Server         : z01-mysql
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : z01:3306
 Source Schema         : datax_web

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 01/08/2019 08:55:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for datax_plugin
-- ----------------------------
DROP TABLE IF EXISTS `datax_plugin`;
CREATE TABLE `datax_plugin`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `plugin_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '插件类型，reader writer',
  `plugin_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '插件名，用作主键',
  `template_json` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'json模板',
  `comments` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '注释',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'datax插件信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of datax_plugin
-- ----------------------------
INSERT INTO `datax_plugin` VALUES (1, 'reader', 'streamreader', '', '内存读取');
INSERT INTO `datax_plugin` VALUES (2, 'writer', 'streamwriter', NULL, '内存写');
INSERT INTO `datax_plugin` VALUES (3, 'reader', 'mysqlreader', NULL, 'mysql读取');
INSERT INTO `datax_plugin` VALUES (4, 'writer', 'mysqlwriter', NULL, 'myysql写');
INSERT INTO `datax_plugin` VALUES (5, 'reader', 'oraclereader', NULL, 'oracle读取');
INSERT INTO `datax_plugin` VALUES (6, 'reader', 'sdfasdf', 'test', 'test');
INSERT INTO `datax_plugin` VALUES (12, 'reader', 'test', NULL, NULL);

-- ----------------------------
-- Table structure for job_config
-- ----------------------------
DROP TABLE IF EXISTS `job_config`;
CREATE TABLE `job_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '作业名',
  `job_group` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分组',
  `config_json` varchar(5000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '作业描述信息',
  `label` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标签（读插件、写插件)',
  `update_date` datetime(0) NULL DEFAULT NULL,
  `status` int(1) NULL DEFAULT 1,
  `create_by` int(11) NULL DEFAULT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `update_by` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '作业配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of job_config
-- ----------------------------
INSERT INTO `job_config` VALUES (1, NULL, 'test', 'test', NULL, NULL, NULL, '2019-06-17 21:10:31', 0, NULL, '2019-06-17 21:10:31', NULL);
INSERT INTO `job_config` VALUES (2, NULL, 'testt', 'test', '{\n	\"abc\": \"def\"\n}', NULL, NULL, '2019-06-17 22:22:03', 0, NULL, '2019-06-17 21:14:30', NULL);
INSERT INTO `job_config` VALUES (3, NULL, '示例', 'test', '{\n  \"job\": {\n    \"setting\": {\n      \"speed\": {\n        \"channel\": 3\n      },\n      \"errorLimit\": {\n        \"record\": 0,\n        \"percentage\": 0.02\n      }\n    },\n    \"content\": [\n      {\n        \"reader\": {\n          \"name\": \"mysqlreader\",\n          \"parameter\": {\n            \"username\": \"root\",\n            \"password\": \"root\",\n            \"column\": [\n              \"id\",\n              \"name\"\n            ],\n            \"splitPk\": \"db_id\",\n            \"connection\": [\n              {\n                \"table\": [\n                  \"table\"\n                ],\n                \"jdbcUrl\": [\n                  \"jdbc:mysql://127.0.0.1:3306/com.wugui.database\"\n                ]\n              }\n            ]\n          }\n        },\n        \"writer\": {\n          \"name\": \"streamwriter\",\n          \"parameter\": {\n            \"print\": true\n          }\n        }\n      }\n    ]\n  }\n}', NULL, NULL, '2019-06-17 22:28:21', 1, NULL, '2019-06-17 22:05:31', NULL);
INSERT INTO `job_config` VALUES (4, NULL, 'test', '123', '{\n  \"job\": {\n    \"setting\": {\n      \"speed\": {\n        \"channel\": 3\n      },\n      \"errorLimit\": {\n        \"record\": 0,\n        \"percentage\": 0.02\n      }\n    },\n    \"content\": [\n      {\n        \"reader\": {\n          \"name\": \"mysqlreader\",\n          \"parameter\": {\n            \"username\": \"root\",\n            \"password\": \"root\",\n            \"column\": [\n              \"id\",\n              \"name\"\n            ],\n            \"splitPk\": \"db_id\",\n            \"connection\": [\n              {\n                \"table\": [\n                  \"table\"\n                ],\n                \"jdbcUrl\": [\n                  \"jdbc:mysql://127.0.0.1:3306/com.wugui.database\"\n                ]\n              }\n            ]\n          }\n        },\n        \"writer\": {\n          \"name\": \"streamwriter\",\n          \"parameter\": {\n            \"print\": true\n          }\n        }\n      }\n    ]\n  }\n}', NULL, NULL, '2019-06-17 22:28:31', 1, NULL, '2019-06-17 22:28:31', NULL);
INSERT INTO `job_config` VALUES (5, NULL, NULL, NULL, '{}', NULL, NULL, '2019-06-25 10:28:51', 1, NULL, '2019-06-25 10:28:51', NULL);
INSERT INTO `job_config` VALUES (6, NULL, NULL, NULL, '{}', NULL, NULL, '2019-06-25 10:47:59', 1, NULL, '2019-06-25 10:47:59', NULL);

-- ----------------------------
-- Table structure for job_jdbc_datasource
-- ----------------------------
DROP TABLE IF EXISTS `job_jdbc_datasource`;
CREATE TABLE `job_jdbc_datasource`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `datasource_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据源名称',
  `datasource_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'Default' COMMENT '数据源分组',
  `jdbc_username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `jdbc_password` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `jdbc_url` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'jdbc url',
  `jdbc_driver_class` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'jdbc驱动类',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：0删除 1启用 2禁用',
  `create_by` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `comments` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'jdbc数据源配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of job_jdbc_datasource
-- ----------------------------
INSERT INTO `job_jdbc_datasource` VALUES (1, 'z01', 'Default', 'root', 'root', 'jdbc:mysql://z01:3306/datax_web?serverTimezone=Asia/Shanghai&useLegacyDatetimeCode=false&useSSL=false&nullNamePatternMatchesAll=true&useUnicode=true&characterEncoding=UTF-8', 'com.mysql.jdbc.Driver', 1, NULL, '2019-07-31 20:30:11', NULL, '2019-07-31 20:30:11', '测试');

-- ----------------------------
-- Table structure for job_log
-- ----------------------------
DROP TABLE IF EXISTS `job_log`;
CREATE TABLE `job_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_id` bigint(20) NOT NULL COMMENT '抽取任务，主键ID',
  `log_file_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志文件路径',
  `update_date` datetime(0) NULL DEFAULT NULL,
  `status` int(1) NULL DEFAULT 1,
  `create_by` int(11) NULL DEFAULT NULL,
  `create_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `update_by` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '抽取日志记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of job_log
-- ----------------------------
INSERT INTO `job_log` VALUES (1, 1, '/data/applogs/datax-web/1_1561638145701', NULL, 1, NULL, '2019-06-27 08:22:26', NULL);
INSERT INTO `job_log` VALUES (2, 1, '/data/applogs/datax-web/1_1561638359975.log', NULL, 1, NULL, '2019-06-27 08:26:00', NULL);
INSERT INTO `job_log` VALUES (3, 1, '/data/applogs/datax-web/1_1561638760584.log', NULL, 1, NULL, '2019-06-27 08:32:41', NULL);
INSERT INTO `job_log` VALUES (4, 4, '/data/applogs/datax-web/4_1564480870563.log', '2019-07-30 18:01:11', 1, NULL, '2019-07-30 18:01:11', NULL);
INSERT INTO `job_log` VALUES (5, 3, '/data/applogs/datax-web/3_1564481419699.log', '2019-07-30 18:10:20', 1, NULL, '2019-07-30 18:10:20', NULL);
INSERT INTO `job_log` VALUES (6, 3, '/data/applogs/datax-web/3_1564485764734.log', '2019-07-30 19:22:45', 1, NULL, '2019-07-30 19:22:45', NULL);
INSERT INTO `job_log` VALUES (7, 3, '/data/applogs/datax-web/3_1564485918860.log', '2019-07-30 19:25:19', 1, NULL, '2019-07-30 19:25:19', NULL);
INSERT INTO `job_log` VALUES (8, 3, '/data/applogs/datax-web/3_1564486087223.log', '2019-07-30 19:28:07', 1, NULL, '2019-07-30 19:28:07', NULL);
INSERT INTO `job_log` VALUES (9, 3, '/data/applogs/datax-web/3_1564486152278.log', '2019-07-30 19:29:12', 1, NULL, '2019-07-30 19:29:12', NULL);
INSERT INTO `job_log` VALUES (10, 3, '/data/applogs/datax-web/3_1564486351631.log', '2019-07-30 19:32:32', 1, NULL, '2019-07-30 19:32:32', NULL);
INSERT INTO `job_log` VALUES (11, 3, '/data/applogs/datax-web/3_1564486375214.log', '2019-07-30 19:32:55', 1, NULL, '2019-07-30 19:32:55', NULL);
INSERT INTO `job_log` VALUES (12, 3, '/data/applogs/datax-web/3_1564486398393.log', '2019-07-30 19:33:18', 1, NULL, '2019-07-30 19:33:18', NULL);
INSERT INTO `job_log` VALUES (13, 3, '/data/applogs/datax-web/3_1564488007122.log', '2019-07-30 20:00:07', 1, NULL, '2019-07-30 20:00:07', NULL);
INSERT INTO `job_log` VALUES (14, 3, '/data/applogs/datax-web/3_1564489795800.log', '2019-07-30 20:29:56', 1, NULL, '2019-07-30 20:29:56', NULL);
INSERT INTO `job_log` VALUES (15, 3, '/data/applogs/datax-web/3_1564490723427.log', '2019-07-30 20:45:23', 1, NULL, '2019-07-30 20:45:23', NULL);
INSERT INTO `job_log` VALUES (16, 3, '/data/applogs/datax-web/3_1564490843863.log', '2019-07-30 20:47:24', 1, NULL, '2019-07-30 20:47:24', NULL);
INSERT INTO `job_log` VALUES (17, 3, '/data/applogs/datax-web/3_1564491445033.log', '2019-07-30 20:57:25', 1, NULL, '2019-07-30 20:57:25', NULL);
INSERT INTO `job_log` VALUES (18, 3, '/data/applogs/datax-web/3_1564491868935.log', '2019-07-30 21:04:29', 1, NULL, '2019-07-30 21:04:29', NULL);
INSERT INTO `job_log` VALUES (19, 3, '/data/applogs/datax-web/3_1564492047112.log', '2019-07-30 21:07:27', 1, NULL, '2019-07-30 21:07:27', NULL);
INSERT INTO `job_log` VALUES (20, 3, '/data/applogs/datax-web/3_1564492173290.log', '2019-07-30 21:09:33', 1, NULL, '2019-07-30 21:09:33', NULL);
INSERT INTO `job_log` VALUES (21, 3, '/data/applogs/datax-web/3_1564492308532.log', '2019-07-30 21:11:49', 1, NULL, '2019-07-30 21:11:49', NULL);
INSERT INTO `job_log` VALUES (22, 3, '/data/applogs/datax-web/3_1564492378732.log', '2019-07-30 21:12:59', 1, NULL, '2019-07-30 21:12:59', NULL);
INSERT INTO `job_log` VALUES (23, 3, '/data/applogs/datax-web/3_1564495260737.log', '2019-07-30 22:01:01', 1, NULL, '2019-07-30 22:01:01', NULL);
INSERT INTO `job_log` VALUES (24, 3, '/data/applogs/datax-web/3_1564574180078.log', '2019-07-31 19:56:20', 1, NULL, '2019-07-31 19:56:20', NULL);
INSERT INTO `job_log` VALUES (25, 3, '/data/applogs/datax-web/3_1564574319878.log', '2019-07-31 19:58:40', 1, NULL, '2019-07-31 19:58:40', NULL);

SET FOREIGN_KEY_CHECKS = 1;
