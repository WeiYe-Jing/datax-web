/*
Navicat MySQL Data Transfer

Source Server         : 192.168.1.189_3306
Source Server Version : 50724
Source Host           : 192.168.1.189:3306
Source Database       : datax-web

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2019-06-17 22:50:00
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for datax_plugin
-- ----------------------------
DROP TABLE IF EXISTS `datax_plugin`;
CREATE TABLE `datax_plugin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `plugin_type` varchar(32) DEFAULT NULL COMMENT '插件类型，reader writer',
  `plugin_name` varchar(255) NOT NULL COMMENT '插件名，用作主键',
  `template_json` text COMMENT 'json模板',
  `comments` varchar(1000) DEFAULT NULL COMMENT '注释',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='datax插件信息';

-- ----------------------------
-- Records of datax_plugin
-- ----------------------------
INSERT INTO `datax_plugin` VALUES ('1', 'reader', 'streamreader', '', '内存读取');
INSERT INTO `datax_plugin` VALUES ('2', 'writer', 'streamwriter', null, '内存写');
INSERT INTO `datax_plugin` VALUES ('3', 'reader', 'mysqlreader', null, 'mysql读取');
INSERT INTO `datax_plugin` VALUES ('4', 'writer', 'mysqlwriter', null, 'myysql写');
INSERT INTO `datax_plugin` VALUES ('5', 'reader', 'oraclereader', null, 'oracle读取');
INSERT INTO `datax_plugin` VALUES ('6', 'reader', 'sdfasdf', 'test', 'test');
INSERT INTO `datax_plugin` VALUES ('12', 'reader', 'test', null, null);

-- ----------------------------
-- Table structure for job_config
-- ----------------------------
DROP TABLE IF EXISTS `job_config`;
CREATE TABLE `job_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL COMMENT '作业名',
  `job_group` varchar(255) DEFAULT NULL COMMENT '分组',
  `config_json` varchar(5000) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL COMMENT '作业描述信息',
  `label` varchar(255) DEFAULT NULL COMMENT '标签（读插件、写插件)',
  `update_date` timestamp NULL DEFAULT NULL,
  `status` int(1) DEFAULT '1',
  `create_by` int(11) DEFAULT NULL,
  `create_date` timestamp NULL DEFAULT NULL,
  `update_by` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='作业配置表';

-- ----------------------------
-- Records of job_config
-- ----------------------------
INSERT INTO `job_config` VALUES ('1', null, 'test', 'test', null, null, null, '2019-06-17 21:10:31', '0', null, '2019-06-17 21:10:31', null);
INSERT INTO `job_config` VALUES ('2', null, 'testt', 'test', '{\n	\"abc\": \"def\"\n}', null, null, '2019-06-17 22:22:03', '0', null, '2019-06-17 21:14:30', null);
INSERT INTO `job_config` VALUES ('3', null, '示例', 'test', '{\n  \"job\": {\n    \"setting\": {\n      \"speed\": {\n        \"channel\": 3\n      },\n      \"errorLimit\": {\n        \"record\": 0,\n        \"percentage\": 0.02\n      }\n    },\n    \"content\": [\n      {\n        \"reader\": {\n          \"name\": \"mysqlreader\",\n          \"parameter\": {\n            \"username\": \"root\",\n            \"password\": \"root\",\n            \"column\": [\n              \"id\",\n              \"name\"\n            ],\n            \"splitPk\": \"db_id\",\n            \"connection\": [\n              {\n                \"table\": [\n                  \"table\"\n                ],\n                \"jdbcUrl\": [\n                  \"jdbc:mysql://127.0.0.1:3306/database\"\n                ]\n              }\n            ]\n          }\n        },\n        \"writer\": {\n          \"name\": \"streamwriter\",\n          \"parameter\": {\n            \"print\": true\n          }\n        }\n      }\n    ]\n  }\n}', null, null, '2019-06-17 22:28:21', '1', null, '2019-06-17 22:05:31', null);
INSERT INTO `job_config` VALUES ('4', null, 'test', '123', '{\n  \"job\": {\n    \"setting\": {\n      \"speed\": {\n        \"channel\": 3\n      },\n      \"errorLimit\": {\n        \"record\": 0,\n        \"percentage\": 0.02\n      }\n    },\n    \"content\": [\n      {\n        \"reader\": {\n          \"name\": \"mysqlreader\",\n          \"parameter\": {\n            \"username\": \"root\",\n            \"password\": \"root\",\n            \"column\": [\n              \"id\",\n              \"name\"\n            ],\n            \"splitPk\": \"db_id\",\n            \"connection\": [\n              {\n                \"table\": [\n                  \"table\"\n                ],\n                \"jdbcUrl\": [\n                  \"jdbc:mysql://127.0.0.1:3306/database\"\n                ]\n              }\n            ]\n          }\n        },\n        \"writer\": {\n          \"name\": \"streamwriter\",\n          \"parameter\": {\n            \"print\": true\n          }\n        }\n      }\n    ]\n  }\n}', null, null, '2019-06-17 22:28:31', '1', null, '2019-06-17 22:28:31', null);

-- ----------------------------
-- Table structure for job_log
-- ----------------------------
DROP TABLE IF EXISTS `job_log`;
CREATE TABLE `job_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_id` bigint(20) NOT NULL COMMENT '抽取任务，主键ID',
  `log_file_path` varchar(255) DEFAULT NULL COMMENT '日志文件路径',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='抽取日志记录表';

-- ----------------------------
-- Records of job_log
-- ----------------------------
