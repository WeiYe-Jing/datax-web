/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.1.150-3308
 Source Server Type    : MySQL
 Source Server Version : 50724
 Source Host           : 192.168.1.150:3308
 Source Schema         : datax-web

 Target Server Type    : MySQL
 Target Server Version : 50724
 File Encoding         : 65001

 Date: 16/05/2019 18:36:04
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for datax_content
-- ----------------------------
DROP TABLE IF EXISTS `datax_content`;
CREATE TABLE `datax_content`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `plugin_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '插件类型，区分reader writer',
  `params_json` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '插件配置参数json字符串',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'datax job content 信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for datax_job
-- ----------------------------
DROP TABLE IF EXISTS `datax_job`;
CREATE TABLE `datax_job`  (
  `id` int(11) NOT NULL COMMENT '主键',
  `job_id` int(11) DEFAULT NULL COMMENT '记录datax job id',
  `setting_json` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT 'setting json字符串',
  `content_ids` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '关联datax_content表，用逗号分隔',
  `job_log_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '记录日志文件路径',
  `extra_json` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '除了setting content外的一些配置',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'data job配置' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'datax插件信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of datax_plugin
-- ----------------------------
INSERT INTO `datax_plugin` VALUES (1, 'reader', 'streamreader', '', '内存读取');
INSERT INTO `datax_plugin` VALUES (2, 'writer', 'streamwriter', NULL, '内存写');
INSERT INTO `datax_plugin` VALUES (3, 'reader', 'mysqlreader', NULL, 'mysql读取');
INSERT INTO `datax_plugin` VALUES (4, 'writer', 'mysqlwriter', NULL, 'myysql写');
INSERT INTO `datax_plugin` VALUES (5, 'reader', 'oraclereader', NULL, 'oracle读取');

SET FOREIGN_KEY_CHECKS = 1;
