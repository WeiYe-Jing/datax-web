/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.1.150-3306
 Source Server Type    : MySQL
 Source Server Version : 50723
 Source Host           : 192.168.1.150:3306
 Source Schema         : datax-web

 Target Server Type    : MySQL
 Target Server Version : 50723
 File Encoding         : 65001

 Date: 27/06/2019 20:59:03
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for datax_plugin
-- ----------------------------
DROP TABLE IF EXISTS `datax_plugin`;
CREATE TABLE `datax_plugin`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `plugin_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '插件类型，reader writer',
  `plugin_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '插件名，用作主键',
  `template_json` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT 'json模板',
  `comments` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '注释',
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
  `user_id` int(11) DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '作业名',
  `job_group` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '分组',
  `config_json` varchar(5000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '作业描述信息',
  `label` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '标签（读插件、写插件)',
  `update_date` timestamp(0) DEFAULT NULL,
  `status` int(1) DEFAULT 1,
  `create_by` int(11) DEFAULT NULL,
  `create_date` timestamp(0) DEFAULT NULL,
  `update_by` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '作业配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of job_config
-- ----------------------------
INSERT INTO `job_config` VALUES (1, NULL, 'test', 'test', NULL, NULL, NULL, '2019-06-17 21:10:31', 0, NULL, '2019-06-17 21:10:31', NULL);
INSERT INTO `job_config` VALUES (2, NULL, 'testt', 'test', '{\n	\"abc\": \"def\"\n}', NULL, NULL, '2019-06-17 22:22:03', 0, NULL, '2019-06-17 21:14:30', NULL);
INSERT INTO `job_config` VALUES (3, NULL, '示例', 'test', '{\n  \"job\": {\n    \"setting\": {\n      \"speed\": {\n        \"channel\": 3\n      },\n      \"errorLimit\": {\n        \"record\": 0,\n        \"percentage\": 0.02\n      }\n    },\n    \"content\": [\n      {\n        \"reader\": {\n          \"name\": \"mysqlreader\",\n          \"parameter\": {\n            \"username\": \"root\",\n            \"password\": \"root\",\n            \"column\": [\n              \"id\",\n              \"name\"\n            ],\n            \"splitPk\": \"db_id\",\n            \"connection\": [\n              {\n                \"table\": [\n                  \"table\"\n                ],\n                \"jdbcUrl\": [\n                  \"jdbc:mysql://127.0.0.1:3306/database\"\n                ]\n              }\n            ]\n          }\n        },\n        \"writer\": {\n          \"name\": \"streamwriter\",\n          \"parameter\": {\n            \"print\": true\n          }\n        }\n      }\n    ]\n  }\n}', NULL, NULL, '2019-06-17 22:28:21', 1, NULL, '2019-06-17 22:05:31', NULL);
INSERT INTO `job_config` VALUES (4, NULL, 'test', '123', '{\n  \"job\": {\n    \"setting\": {\n      \"speed\": {\n        \"channel\": 3\n      },\n      \"errorLimit\": {\n        \"record\": 0,\n        \"percentage\": 0.02\n      }\n    },\n    \"content\": [\n      {\n        \"reader\": {\n          \"name\": \"mysqlreader\",\n          \"parameter\": {\n            \"username\": \"root\",\n            \"password\": \"root\",\n            \"column\": [\n              \"id\",\n              \"name\"\n            ],\n            \"splitPk\": \"db_id\",\n            \"connection\": [\n              {\n                \"table\": [\n                  \"table\"\n                ],\n                \"jdbcUrl\": [\n                  \"jdbc:mysql://127.0.0.1:3306/database\"\n                ]\n              }\n            ]\n          }\n        },\n        \"writer\": {\n          \"name\": \"streamwriter\",\n          \"parameter\": {\n            \"print\": true\n          }\n        }\n      }\n    ]\n  }\n}', NULL, NULL, '2019-06-17 22:28:31', 1, NULL, '2019-06-17 22:28:31', NULL);
INSERT INTO `job_config` VALUES (5, NULL, NULL, NULL, '{}', NULL, NULL, '2019-06-25 10:28:51', 1, NULL, '2019-06-25 10:28:51', NULL);
INSERT INTO `job_config` VALUES (6, NULL, NULL, NULL, '{}', NULL, NULL, '2019-06-25 10:47:59', 1, NULL, '2019-06-25 10:47:59', NULL);

-- ----------------------------
-- Table structure for job_log
-- ----------------------------
DROP TABLE IF EXISTS `job_log`;
CREATE TABLE `job_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_id` bigint(20) NOT NULL COMMENT '抽取任务，主键ID',
  `log_file_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '日志文件路径',
  `update_date` datetime(0) DEFAULT NULL,
  `status` int(1) DEFAULT 1,
  `create_by` int(11) DEFAULT NULL,
  `create_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `update_by` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '抽取日志记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of job_log
-- ----------------------------
INSERT INTO `job_log` VALUES (1, 1, '/data/applogs/datax-web/1_1561638145701', NULL, 1, NULL, '2019-06-27 08:22:26', NULL);
INSERT INTO `job_log` VALUES (2, 1, '/data/applogs/datax-web/1_1561638359975.log', NULL, 1, NULL, '2019-06-27 08:26:00', NULL);
INSERT INTO `job_log` VALUES (3, 1, '/data/applogs/datax-web/1_1561638760584.log', NULL, 1, NULL, '2019-06-27 08:32:41', NULL);

SET FOREIGN_KEY_CHECKS = 1;
