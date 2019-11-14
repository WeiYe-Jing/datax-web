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
  `pid` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务进程ID',
  `handle_time` datetime(0) NULL DEFAULT NULL COMMENT '执行-时间',
  `handle_code` int(11) NULL DEFAULT 0 COMMENT '执行-状态',
  `handle_msg` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '执行-日志',
  `create_by` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '抽取日志记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of job_log
-- ----------------------------
INSERT INTO `job_log` VALUES (12, 4, 'D:\\temp\\logs\\datax-web\\4_1573628574883.log', '88612', '2019-11-13 15:02:55', 0, NULL, NULL);
INSERT INTO `job_log` VALUES (13, 4, 'D:\\temp\\logs\\datax-web\\4_1573628575201.log', '88772', '2019-11-13 15:02:55', 0, NULL, NULL);
INSERT INTO `job_log` VALUES (14, 4, 'D:\\temp\\logs\\datax-web\\4_1573628576097.log', '88984', '2019-11-13 15:02:56', 0, NULL, NULL);
INSERT INTO `job_log` VALUES (15, 4, 'D:\\temp\\logs\\datax-web\\4_1573628589106.log', '89224', '2019-11-13 15:03:09', 0, NULL, NULL);
INSERT INTO `job_log` VALUES (16, 4, 'D:\\temp\\logs\\datax-web\\4_1573628589293.log', '89364', '2019-11-13 15:03:09', 0, NULL, NULL);
INSERT INTO `job_log` VALUES (17, 4, 'D:\\temp\\logs\\datax-web\\4_1573628589509.log', '89520', '2019-11-13 15:03:10', 0, NULL, NULL);
INSERT INTO `job_log` VALUES (18, 4, 'D:\\temp\\logs\\datax-web\\4_1573628589670.log', '89652', '2019-11-13 15:03:10', 0, NULL, NULL);
INSERT INTO `job_log` VALUES (19, 4, 'D:\\temp\\logs\\datax-web\\4_1573628589859.log', '89720', '2019-11-13 15:03:10', 0, NULL, NULL);
INSERT INTO `job_log` VALUES (20, 4, 'D:\\temp\\logs\\datax-web\\4_1573628590201.log', '89832', '2019-11-13 15:03:10', 0, NULL, NULL);
INSERT INTO `job_log` VALUES (21, 4, 'D:\\temp\\logs\\datax-web\\4_1573628590325.log', '89912', '2019-11-13 15:03:10', 0, NULL, NULL);
INSERT INTO `job_log` VALUES (22, 6, 'D:\\temp\\logs\\datax-web\\6_1573630200227.log', '18352', '2019-11-13 15:30:00', 0, NULL, NULL);
INSERT INTO `job_log` VALUES (23, 6, 'D:\\temp\\logs\\datax-web\\6_1573630558990.log', '74672', '2019-11-13 15:35:59', 0, NULL, NULL);
INSERT INTO `job_log` VALUES (24, 6, 'D:\\temp\\logs\\datax-web\\6_1573635226093.log', '93744', '2019-11-13 16:53:46', 0, NULL, NULL);
INSERT INTO `job_log` VALUES (25, 4, 'D:\\temp\\logs\\datax-web\\4_1573635586309.log', '94472', '2019-11-13 16:59:46', 200, NULL, NULL);
INSERT INTO `job_log` VALUES (26, 4, 'D:\\temp\\logs\\datax-web\\4_1573635777426.log', '94460', '2019-11-13 17:02:57', 500, NULL, NULL);
INSERT INTO `job_log` VALUES (27, 3, 'D:\\temp\\logs\\datax-web\\3_1573637050199.log', '97044', '2019-11-13 17:24:10', 200, NULL, NULL);
INSERT INTO `job_log` VALUES (28, 3, 'D:\\temp\\logs\\datax-web\\3_1573637194736.log', '89716', '2019-11-13 17:26:35', 200, NULL, NULL);
INSERT INTO `job_log` VALUES (29, 3, 'D:\\temp\\logs\\datax-web\\3_1573638319908.log', '97816', '2019-11-13 17:45:20', 200, NULL, NULL);
INSERT INTO `job_log` VALUES (30, 3, 'D:\\temp\\logs\\datax-web\\3_1573638524001.log', '97888', '2019-11-13 17:48:44', 200, NULL, NULL);
INSERT INTO `job_log` VALUES (31, 3, 'D:\\temp\\logs\\datax-web\\3_1573638646217.log', '97884', '2019-11-13 17:50:46', 500, NULL, NULL);
INSERT INTO `job_log` VALUES (32, 3, 'D:\\temp\\logs\\datax-web\\3_1573638967908.log', '96844', '2019-11-13 17:56:08', 500, NULL, NULL);
INSERT INTO `job_log` VALUES (33, 3, 'D:\\temp\\logs\\datax-web\\3_1573639056074.log', '92228', '2019-11-13 17:57:36', 500, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
