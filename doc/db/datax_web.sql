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
INSERT INTO `job_config` VALUES (3, NULL, '示例', 'test', '{\n  \"job\": {\n    \"setting\": {\n      \"speed\": {\n        \"channel\": 3\n      },\n      \"errorLimit\": {\n        \"record\": 0,\n        \"percentage\": 0.02\n      }\n    },\n    \"content\": [\n      {\n        \"reader\": {\n          \"name\": \"mysqlreader\",\n          \"parameter\": {\n            \"username\": \"root\",\n            \"password\": \"root\",\n            \"column\": [\n              \"id\",\n              \"name\"\n            ],\n            \"splitPk\": \"db_id\",\n            \"connection\": [\n              {\n                \"table\": [\n                  \"table\"\n                ],\n                \"jdbcUrl\": [\n                  \"jdbc:mysql://127.0.0.1:3306/com.com.wugui.datax.admin.tool.database\"\n                ]\n              }\n            ]\n          }\n        },\n        \"writer\": {\n          \"name\": \"streamwriter\",\n          \"parameter\": {\n            \"print\": true\n          }\n        }\n      }\n    ]\n  }\n}', NULL, NULL, '2019-06-17 22:28:21', 1, NULL, '2019-06-17 22:05:31', NULL);
INSERT INTO `job_config` VALUES (4, NULL, 'test', '123', '{\n  \"job\": {\n    \"setting\": {\n      \"speed\": {\n        \"channel\": 3\n      },\n      \"errorLimit\": {\n        \"record\": 0,\n        \"percentage\": 0.02\n      }\n    },\n    \"content\": [\n      {\n        \"reader\": {\n          \"name\": \"mysqlreader\",\n          \"parameter\": {\n            \"username\": \"root\",\n            \"password\": \"root\",\n            \"column\": [\n              \"id\",\n              \"name\"\n            ],\n            \"splitPk\": \"db_id\",\n            \"connection\": [\n              {\n                \"table\": [\n                  \"table\"\n                ],\n                \"jdbcUrl\": [\n                  \"jdbc:mysql://127.0.0.1:3306/com.com.wugui.datax.admin.tool.database\"\n                ]\n              }\n            ]\n          }\n        },\n        \"writer\": {\n          \"name\": \"streamwriter\",\n          \"parameter\": {\n            \"print\": true\n          }\n        }\n      }\n    ]\n  }\n}', NULL, NULL, '2019-06-17 22:28:31', 1, NULL, '2019-06-17 22:28:31', NULL);
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
INSERT INTO `job_log` VALUES (12, 4, 'D:\\temp\\logs\\com.wugui.datax-web\\4_1573628574883.log', '88612', '2019-11-13 15:02:55', 0, NULL, NULL);
INSERT INTO `job_log` VALUES (13, 4, 'D:\\temp\\logs\\com.wugui.datax-web\\4_1573628575201.log', '88772', '2019-11-13 15:02:55', 0, NULL, NULL);
INSERT INTO `job_log` VALUES (14, 4, 'D:\\temp\\logs\\com.wugui.datax-web\\4_1573628576097.log', '88984', '2019-11-13 15:02:56', 0, NULL, NULL);
INSERT INTO `job_log` VALUES (15, 4, 'D:\\temp\\logs\\com.wugui.datax-web\\4_1573628589106.log', '89224', '2019-11-13 15:03:09', 0, NULL, NULL);
INSERT INTO `job_log` VALUES (16, 4, 'D:\\temp\\logs\\com.wugui.datax-web\\4_1573628589293.log', '89364', '2019-11-13 15:03:09', 0, NULL, NULL);
INSERT INTO `job_log` VALUES (17, 4, 'D:\\temp\\logs\\com.wugui.datax-web\\4_1573628589509.log', '89520', '2019-11-13 15:03:10', 0, NULL, NULL);
INSERT INTO `job_log` VALUES (18, 4, 'D:\\temp\\logs\\com.wugui.datax-web\\4_1573628589670.log', '89652', '2019-11-13 15:03:10', 0, NULL, NULL);
INSERT INTO `job_log` VALUES (19, 4, 'D:\\temp\\logs\\com.wugui.datax-web\\4_1573628589859.log', '89720', '2019-11-13 15:03:10', 0, NULL, NULL);
INSERT INTO `job_log` VALUES (20, 4, 'D:\\temp\\logs\\com.wugui.datax-web\\4_1573628590201.log', '89832', '2019-11-13 15:03:10', 0, NULL, NULL);
INSERT INTO `job_log` VALUES (21, 4, 'D:\\temp\\logs\\com.wugui.datax-web\\4_1573628590325.log', '89912', '2019-11-13 15:03:10', 0, NULL, NULL);
INSERT INTO `job_log` VALUES (22, 6, 'D:\\temp\\logs\\com.wugui.datax-web\\6_1573630200227.log', '18352', '2019-11-13 15:30:00', 0, NULL, NULL);
INSERT INTO `job_log` VALUES (23, 6, 'D:\\temp\\logs\\com.wugui.datax-web\\6_1573630558990.log', '74672', '2019-11-13 15:35:59', 0, NULL, NULL);
INSERT INTO `job_log` VALUES (24, 6, 'D:\\temp\\logs\\com.wugui.datax-web\\6_1573635226093.log', '93744', '2019-11-13 16:53:46', 0, NULL, NULL);
INSERT INTO `job_log` VALUES (25, 4, 'D:\\temp\\logs\\com.wugui.datax-web\\4_1573635586309.log', '94472', '2019-11-13 16:59:46', 200, NULL, NULL);
INSERT INTO `job_log` VALUES (26, 4, 'D:\\temp\\logs\\com.wugui.datax-web\\4_1573635777426.log', '94460', '2019-11-13 17:02:57', 500, NULL, NULL);
INSERT INTO `job_log` VALUES (27, 3, 'D:\\temp\\logs\\com.wugui.datax-web\\3_1573637050199.log', '97044', '2019-11-13 17:24:10', 200, NULL, NULL);
INSERT INTO `job_log` VALUES (28, 3, 'D:\\temp\\logs\\com.wugui.datax-web\\3_1573637194736.log', '89716', '2019-11-13 17:26:35', 200, NULL, NULL);
INSERT INTO `job_log` VALUES (29, 3, 'D:\\temp\\logs\\com.wugui.datax-web\\3_1573638319908.log', '97816', '2019-11-13 17:45:20', 200, NULL, NULL);
INSERT INTO `job_log` VALUES (30, 3, 'D:\\temp\\logs\\com.wugui.datax-web\\3_1573638524001.log', '97888', '2019-11-13 17:48:44', 200, NULL, NULL);
INSERT INTO `job_log` VALUES (31, 3, 'D:\\temp\\logs\\com.wugui.datax-web\\3_1573638646217.log', '97884', '2019-11-13 17:50:46', 500, NULL, NULL);
INSERT INTO `job_log` VALUES (32, 3, 'D:\\temp\\logs\\com.wugui.datax-web\\3_1573638967908.log', '96844', '2019-11-13 17:56:08', 500, NULL, NULL);
INSERT INTO `job_log` VALUES (33, 3, 'D:\\temp\\logs\\com.wugui.datax-web\\3_1573639056074.log', '92228', '2019-11-13 17:57:36', 500, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;



CREATE TABLE `xxl_job_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_group` int(11) NOT NULL COMMENT '执行器主键ID',
  `job_cron` varchar(128) NOT NULL COMMENT '任务执行CRON',
  `job_desc` varchar(255) NOT NULL,
  `add_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `author` varchar(64) DEFAULT NULL COMMENT '作者',
  `alarm_email` varchar(255) DEFAULT NULL COMMENT '报警邮件',
  `executor_route_strategy` varchar(50) DEFAULT NULL COMMENT '执行器路由策略',
  `executor_handler` varchar(255) DEFAULT NULL COMMENT '执行器任务handler',
  `executor_param` varchar(512) DEFAULT NULL COMMENT '执行器任务参数',
  `executor_block_strategy` varchar(50) DEFAULT NULL COMMENT '阻塞处理策略',
  `executor_timeout` int(11) NOT NULL DEFAULT '0' COMMENT '任务执行超时时间，单位秒',
  `executor_fail_retry_count` int(11) NOT NULL DEFAULT '0' COMMENT '失败重试次数',
  `glue_type` varchar(50) NOT NULL COMMENT 'GLUE类型',
  `glue_source` mediumtext COMMENT 'GLUE源代码',
  `glue_remark` varchar(128) DEFAULT NULL COMMENT 'GLUE备注',
  `glue_updatetime` datetime DEFAULT NULL COMMENT 'GLUE更新时间',
  `child_jobid` varchar(255) DEFAULT NULL COMMENT '子任务ID，多个逗号分隔',
  `trigger_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '调度状态：0-停止，1-运行',
  `trigger_last_time` bigint(13) NOT NULL DEFAULT '0' COMMENT '上次调度时间',
  `trigger_next_time` bigint(13) NOT NULL DEFAULT '0' COMMENT '下次调度时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `xxl_job_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_group` int(11) NOT NULL COMMENT '执行器主键ID',
  `job_id` int(11) NOT NULL COMMENT '任务，主键ID',
  `executor_address` varchar(255) DEFAULT NULL COMMENT '执行器地址，本次执行的地址',
  `executor_handler` varchar(255) DEFAULT NULL COMMENT '执行器任务handler',
  `executor_param` varchar(512) DEFAULT NULL COMMENT '执行器任务参数',
  `executor_sharding_param` varchar(20) DEFAULT NULL COMMENT '执行器任务分片参数，格式如 1/2',
  `executor_fail_retry_count` int(11) NOT NULL DEFAULT '0' COMMENT '失败重试次数',
  `trigger_time` datetime DEFAULT NULL COMMENT '调度-时间',
  `trigger_code` int(11) NOT NULL COMMENT '调度-结果',
  `trigger_msg` text COMMENT '调度-日志',
  `handle_time` datetime DEFAULT NULL COMMENT '执行-时间',
  `handle_code` int(11) NOT NULL COMMENT '执行-状态',
  `handle_msg` text COMMENT '执行-日志',
  `alarm_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '告警状态：0-默认、1-无需告警、2-告警成功、3-告警失败',
  PRIMARY KEY (`id`),
  KEY `I_trigger_time` (`trigger_time`),
  KEY `I_handle_code` (`handle_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `xxl_job_logglue` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_id` int(11) NOT NULL COMMENT '任务，主键ID',
  `glue_type` varchar(50) DEFAULT NULL COMMENT 'GLUE类型',
  `glue_source` mediumtext COMMENT 'GLUE源代码',
  `glue_remark` varchar(128) NOT NULL COMMENT 'GLUE备注',
  `add_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `xxl_job_registry` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `registry_group` varchar(255) NOT NULL,
  `registry_key` varchar(255) NOT NULL,
  `registry_value` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `i_g_k_v` (`registry_group`,`registry_key`,`registry_value`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `xxl_job_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_name` varchar(64) NOT NULL COMMENT '执行器AppName',
  `title` varchar(12) NOT NULL COMMENT '执行器名称',
  `order` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `address_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '执行器地址类型：0=自动注册、1=手动录入',
  `address_list` varchar(512) DEFAULT NULL COMMENT '执行器地址列表，多地址逗号分隔',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `xxl_job_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '账号',
  `password` varchar(50) NOT NULL COMMENT '密码',
  `role` tinyint(4) NOT NULL COMMENT '角色：0-普通用户、1-管理员',
  `permission` varchar(255) DEFAULT NULL COMMENT '权限：执行器ID列表，多个逗号分割',
  PRIMARY KEY (`id`),
  UNIQUE KEY `i_username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `xxl_job_lock` (
  `lock_name` varchar(50) NOT NULL COMMENT '锁名称',
  PRIMARY KEY (`lock_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO `xxl_job_group`(`id`, `app_name`, `title`, `order`, `address_type`, `address_list`) VALUES (1, 'xxl-job-executor-sample', '示例执行器', 1, 0, NULL);
INSERT INTO `xxl_job_info`(`id`, `job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (1, 1, '0 0 0 * * ? *', '测试任务1', '2018-11-03 22:21:31', '2018-11-03 22:21:31', 'XXL', '', 'FIRST', 'demoJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2018-11-03 22:21:31', '');
INSERT INTO `xxl_job_user`(`id`, `username`, `password`, `role`, `permission`) VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', 1, NULL);
INSERT INTO `xxl_job_lock` ( `lock_name`) VALUES ( 'schedule_lock');

