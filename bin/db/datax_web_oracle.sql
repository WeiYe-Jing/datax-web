CREATE TABLE job_group (
                           id NUMBER(11) NOT NULL,
                           app_name VARCHAR2(64) NOT NULL,
                           title VARCHAR2(32) NOT NULL,
                           "order" NUMBER(11) DEFAULT 0 NOT NULL,
                           address_type NUMBER(4) DEFAULT 0 NOT NULL,
                           address_list VARCHAR2(512),
                           CONSTRAINT job_group_pk PRIMARY KEY (id)
);

COMMENT ON COLUMN job_group.app_name IS '执行器AppName';
COMMENT ON COLUMN job_group.title IS '执行器名称';
COMMENT ON COLUMN job_group."order" IS '排序';
COMMENT ON COLUMN job_group.address_type IS '执行器地址类型：0=自动注册、1=手动录入';
COMMENT ON COLUMN job_group.address_list IS '执行器地址列表，多地址逗号分隔';

--------------

CREATE TABLE job_info (
                          id NUMBER(11) NOT NULL,
                          job_group NUMBER(11) NOT NULL,
                          job_cron VARCHAR2(128) NOT NULL,
                          job_desc VARCHAR2(255) NOT NULL,
                          add_time TIMESTAMP,
                          update_time TIMESTAMP,
                          author VARCHAR2(64),
                          alarm_email VARCHAR2(255),
                          executor_route_strategy VARCHAR2(50),
                          executor_handler VARCHAR2(255),
                          executor_param VARCHAR2(512),
                          executor_block_strategy VARCHAR2(50),
                          executor_timeout NUMBER(11) DEFAULT 0 NOT NULL,
                          executor_fail_retry_count NUMBER(11) DEFAULT 0 NOT NULL,
                          glue_type VARCHAR2(50) NOT NULL,
                          glue_source CLOB,
                          glue_remark VARCHAR2(128),
                          glue_updatetime TIMESTAMP,
                          child_jobid VARCHAR2(255),
                          trigger_status NUMBER(4) DEFAULT 0 NOT NULL,
                          trigger_last_time NUMBER(13) DEFAULT 0 NOT NULL,
                          trigger_next_time NUMBER(13) DEFAULT 0 NOT NULL,
                          job_json CLOB,
                          PRIMARY KEY (id)
);

COMMENT ON TABLE job_info IS '任务信息表';

COMMENT ON COLUMN job_info.id IS '主键ID';
COMMENT ON COLUMN job_info.job_group IS '执行器主键ID';
COMMENT ON COLUMN job_info.job_cron IS '任务执行CRON';
COMMENT ON COLUMN job_info.job_desc IS '任务描述';
COMMENT ON COLUMN job_info.add_time IS '添加时间';
COMMENT ON COLUMN job_info.update_time IS '更新时间';
COMMENT ON COLUMN job_info.author IS '作者';
COMMENT ON COLUMN job_info.alarm_email IS '报警邮件';
COMMENT ON COLUMN job_info.executor_route_strategy IS '执行器路由策略';
COMMENT ON COLUMN job_info.executor_handler IS '执行器任务handler';
COMMENT ON COLUMN job_info.executor_param IS '执行器任务参数';
COMMENT ON COLUMN job_info.executor_block_strategy IS '阻塞处理策略';
COMMENT ON COLUMN job_info.executor_timeout IS '任务执行超时时间，单位秒';
COMMENT ON COLUMN job_info.executor_fail_retry_count IS '失败重试次数';
COMMENT ON COLUMN job_info.glue_type IS 'GLUE类型';
COMMENT ON COLUMN job_info.glue_source IS 'GLUE源代码';
COMMENT ON COLUMN job_info.glue_remark IS 'GLUE备注';
COMMENT ON COLUMN job_info.glue_updatetime IS 'GLUE更新时间';
COMMENT ON COLUMN job_info.child_jobid IS '子任务ID，多个逗号分隔';
COMMENT ON COLUMN job_info.trigger_status IS '调度状态：0-停止，1-运行';
COMMENT ON COLUMN job_info.trigger_last_time IS '上次调度时间';
COMMENT ON COLUMN job_info.trigger_next_time IS '下次调度时间';
COMMENT ON COLUMN job_info.job_json IS 'datax运行脚本';

----------------
CREATE TABLE job_jdbc_datasource (
                                     id NUMBER(20) NOT NULL,
                                     datasource_name VARCHAR2(200) NOT NULL,
                                     datasource_group VARCHAR2(200) DEFAULT 'Default',
                                     jdbc_username VARCHAR2(100) NOT NULL,
                                     jdbc_password VARCHAR2(200) NOT NULL,
                                     jdbc_url VARCHAR2(500) NOT NULL,
                                     jdbc_driver_class VARCHAR2(200),
                                     status NUMBER(1) DEFAULT 1 NOT NULL,
                                     create_by VARCHAR2(20),
                                     create_date TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP,
                                     update_by VARCHAR2(20),
                                     update_date TIMESTAMP(0),
                                     comments VARCHAR2(1000),
                                     CONSTRAINT job_jdbc_datasource_pk PRIMARY KEY (id)
);

COMMENT ON TABLE job_jdbc_datasource IS 'jdbc数据源配置';

COMMENT ON COLUMN job_jdbc_datasource.id IS '自增主键';
COMMENT ON COLUMN job_jdbc_datasource.datasource_name IS '数据源名称';
COMMENT ON COLUMN job_jdbc_datasource.datasource_group IS '数据源分组';
COMMENT ON COLUMN job_jdbc_datasource.jdbc_username IS '用户名';
COMMENT ON COLUMN job_jdbc_datasource.jdbc_password IS '密码';
COMMENT ON COLUMN job_jdbc_datasource.jdbc_url IS 'jdbc url';
COMMENT ON COLUMN job_jdbc_datasource.jdbc_driver_class IS 'jdbc驱动类';
COMMENT ON COLUMN job_jdbc_datasource.status IS '状态：0删除 1启用 2禁用';
COMMENT ON COLUMN job_jdbc_datasource.create_by IS '创建人';
COMMENT ON COLUMN job_jdbc_datasource.create_date IS '创建时间';
COMMENT ON COLUMN job_jdbc_datasource.update_by IS '更新人';
COMMENT ON COLUMN job_jdbc_datasource.update_date IS '更新时间';
COMMENT ON COLUMN job_jdbc_datasource.comments IS '备注';

-----------------
CREATE TABLE job_lock (
                          lock_name VARCHAR2(50) NOT NULL,
                          CONSTRAINT job_lock_pk PRIMARY KEY (lock_name)
);

COMMENT ON TABLE job_lock IS '锁名称';

COMMENT ON COLUMN job_lock.lock_name IS '锁名称';


-----------------
CREATE TABLE job_log (
                         id NUMBER(20) NOT NULL,
                         job_group NUMBER(11) NOT NULL,
                         job_id NUMBER(11) NOT NULL,
                         job_desc VARCHAR2(255 CHAR),
                         executor_address VARCHAR2(255 CHAR),
                         executor_handler VARCHAR2(255 CHAR),
                         executor_param VARCHAR2(512 CHAR),
                         executor_sharding_param VARCHAR2(20 CHAR),
                         executor_fail_retry_count NUMBER(11) DEFAULT 0,
                         trigger_time TIMESTAMP(0),
                         trigger_code NUMBER(11) NOT NULL,
                         trigger_msg CLOB,
                         handle_time TIMESTAMP(0),
                         handle_code NUMBER(11) NOT NULL,
                         handle_msg CLOB,
                         alarm_status NUMBER(4) DEFAULT 0,
                         process_id VARCHAR2(20 CHAR),
                         max_id NUMBER(20),
                         CONSTRAINT job_log_pk PRIMARY KEY (id)
);

COMMENT ON TABLE job_log IS '任务日志';

COMMENT ON COLUMN job_log.id IS '自增主键';
COMMENT ON COLUMN job_log.job_group IS '执行器主键ID';
COMMENT ON COLUMN job_log.job_id IS '任务，主键ID';
COMMENT ON COLUMN job_log.job_desc IS '任务描述';
COMMENT ON COLUMN job_log.executor_address IS '执行器地址，本次执行的地址';
COMMENT ON COLUMN job_log.executor_handler IS '执行器任务handler';
COMMENT ON COLUMN job_log.executor_param IS '执行器任务参数';
COMMENT ON COLUMN job_log.executor_sharding_param IS '执行器任务分片参数，格式如 1/2';
COMMENT ON COLUMN job_log.executor_fail_retry_count IS '失败重试次数';
COMMENT ON COLUMN job_log.trigger_time IS '调度-时间';
COMMENT ON COLUMN job_log.trigger_code IS '调度-结果';
COMMENT ON COLUMN job_log.trigger_msg IS '调度-日志';
COMMENT ON COLUMN job_log.handle_time IS '执行-时间';
COMMENT ON COLUMN job_log.handle_code IS '执行-状态';
COMMENT ON COLUMN job_log.handle_msg IS '执行-日志';
COMMENT ON COLUMN job_log.alarm_status IS '告警状态：0-默认、1-无需告警、2-告警成功、3-告警失败';
COMMENT ON COLUMN job_log.process_id IS 'datax进程Id';
COMMENT ON COLUMN job_log.max_id IS '增量表max id';

CREATE INDEX I_trigger_time ON job_log (trigger_time);
CREATE INDEX I_handle_code ON job_log (handle_code);

---------------

CREATE TABLE job_log_report (
                                id NUMBER(11) NOT NULL,
                                trigger_day TIMESTAMP(0),
                                running_count NUMBER(11) DEFAULT 0 NOT NULL,
                                suc_count NUMBER(11) DEFAULT 0 NOT NULL,
                                fail_count NUMBER(11) DEFAULT 0 NOT NULL,
                                CONSTRAINT job_log_report_pk PRIMARY KEY (id),
                                CONSTRAINT i_trigger_day UNIQUE (trigger_day)
);

COMMENT ON TABLE job_log_report IS '任务日志报告';

COMMENT ON COLUMN job_log_report.id IS '自增主键';
COMMENT ON COLUMN job_log_report.trigger_day IS '调度-时间';
COMMENT ON COLUMN job_log_report.running_count IS '运行中-日志数量';
COMMENT ON COLUMN job_log_report.suc_count IS '执行成功-日志数量';
COMMENT ON COLUMN job_log_report.fail_count IS '执行失败-日志数量';

------------------

CREATE TABLE job_logglue (
                             id NUMBER(11) NOT NULL,
                             job_id NUMBER(11) NOT NULL,
                             glue_type VARCHAR2(50 CHAR),
                             glue_source CLOB,
                             glue_remark VARCHAR2(128 CHAR) NOT NULL,
                             add_time TIMESTAMP(0),
                             update_time TIMESTAMP(0),
                             CONSTRAINT job_logglue_pk PRIMARY KEY (id)
);

COMMENT ON TABLE job_logglue IS '任务日志GLUE信息';

COMMENT ON COLUMN job_logglue.id IS '自增主键';
COMMENT ON COLUMN job_logglue.job_id IS '任务，主键ID';
COMMENT ON COLUMN job_logglue.glue_type IS 'GLUE类型';
COMMENT ON COLUMN job_logglue.glue_source IS 'GLUE源代码';
COMMENT ON COLUMN job_logglue.glue_remark IS 'GLUE备注';
COMMENT ON COLUMN job_logglue.add_time IS '添加时间';
COMMENT ON COLUMN job_logglue.update_time IS '更新时间';

----------------

CREATE TABLE job_registry (
                              id NUMBER(11) NOT NULL,
                              registry_group VARCHAR2(50 CHAR) NOT NULL,
                              registry_key VARCHAR2(191 CHAR) NOT NULL,
                              registry_value VARCHAR2(191 CHAR) NOT NULL,
                              update_time TIMESTAMP(0),
                              CONSTRAINT job_registry_pk PRIMARY KEY (id),
                              CONSTRAINT i_g_k_v UNIQUE (registry_group, registry_key, registry_value)
);

COMMENT ON TABLE job_registry IS '注册表';

COMMENT ON COLUMN job_registry.id IS '自增主键';
COMMENT ON COLUMN job_registry.registry_group IS '注册组';
COMMENT ON COLUMN job_registry.registry_key IS '注册键';
COMMENT ON COLUMN job_registry.registry_value IS '注册值';
COMMENT ON COLUMN job_registry.update_time IS '更新时间';

--------------

CREATE TABLE job_user (
                          id NUMBER(11) NOT NULL,
                          username VARCHAR2(50 CHAR) NOT NULL,
                          password VARCHAR2(100 CHAR) NOT NULL,
                          role VARCHAR2(50 CHAR),
                          permission VARCHAR2(255 CHAR),
                          CONSTRAINT job_user_pk PRIMARY KEY (id),
                          CONSTRAINT i_username UNIQUE (username)
);

COMMENT ON TABLE job_user IS '用户表';

COMMENT ON COLUMN job_user.id IS '自增主键';
COMMENT ON COLUMN job_user.username IS '账号';
COMMENT ON COLUMN job_user.password IS '密码';
COMMENT ON COLUMN job_user.role IS '角色：0-普通用户、1-管理员';
COMMENT ON COLUMN job_user.permission IS '权限：执行器ID列表，多个逗号分割';


--------------
ALTER TABLE `job_info`
    ADD COLUMN `replace_param` VARCHAR(100) NULL DEFAULT NULL COMMENT '动态参数' AFTER `job_json`,
ADD COLUMN `jvm_param` VARCHAR(200) NULL DEFAULT NULL COMMENT 'jvm参数' AFTER `replace_param`,
ADD COLUMN `time_offset` INT(11) NULL DEFAULT '0'COMMENT '时间偏移量'  AFTER `jvm_param`;
ALTER TABLE job_info DROP COLUMN time_offset;

ALTER TABLE job_info ADD inc_start_time TIMESTAMP DEFAULT NULL ;
COMMENT ON COLUMN job_info.inc_start_time IS '增量初始时间';
--------------

CREATE TABLE job_template (
                              id NUMBER(11) NOT NULL,
                              job_group NUMBER(11) NOT NULL,
                              job_cron VARCHAR2(128),
                              job_desc VARCHAR2(255) NOT NULL,
                              add_time TIMESTAMP(0),
                              update_time TIMESTAMP(0),
                              user_id NUMBER(11) NOT NULL,
                              alarm_email VARCHAR2(255),
                              executor_route_strategy VARCHAR2(50),
                              executor_handler VARCHAR2(255),
                              executor_param VARCHAR2(512),
                              executor_block_strategy VARCHAR2(50),
                              executor_timeout NUMBER(11) DEFAULT 0 NOT NULL,
                              executor_fail_retry_count NUMBER(11) DEFAULT 0 NOT NULL,
                              glue_type VARCHAR2(50) NOT NULL,
                              glue_source CLOB,
                              glue_remark VARCHAR2(128),
                              glue_updatetime TIMESTAMP(0),
                              child_jobid VARCHAR2(255),
                              trigger_last_time NUMBER(13) DEFAULT 0 NOT NULL,
                              trigger_next_time NUMBER(13) DEFAULT 0 NOT NULL,
                              job_json CLOB,
                              jvm_param VARCHAR2(200),
                              project_id NUMBER(11),
                              CONSTRAINT job_template_pk PRIMARY KEY (id)
);

COMMENT ON TABLE job_template IS '任务模板';

COMMENT ON COLUMN job_template.id IS '自增主键';
COMMENT ON COLUMN job_template.job_group IS '执行器主键ID';
COMMENT ON COLUMN job_template.job_cron IS '任务执行CRON';
COMMENT ON COLUMN job_template.job_desc IS '任务描述';
COMMENT ON COLUMN job_template.add_time IS '添加时间';
COMMENT ON COLUMN job_template.update_time IS '更新时间';
COMMENT ON COLUMN job_template.user_id IS '修改用户';
COMMENT ON COLUMN job_template.alarm_email IS '报警邮件';
COMMENT ON COLUMN job_template.executor_route_strategy IS '执行器路由策略';
COMMENT ON COLUMN job_template.executor_handler IS '执行器任务handler';
COMMENT ON COLUMN job_template.executor_param IS '执行器参数';
COMMENT ON COLUMN job_template.executor_block_strategy IS '阻塞处理策略';
COMMENT ON COLUMN job_template.executor_timeout IS '任务执行超时时间，单位秒';
COMMENT ON COLUMN job_template.executor_fail_retry_count IS '失败重试次数';
COMMENT ON COLUMN job_template.glue_type IS 'GLUE类型';
COMMENT ON COLUMN job_template.glue_source IS 'GLUE源代码';
COMMENT ON COLUMN job_template.glue_remark IS 'GLUE备注';
COMMENT ON COLUMN job_template.glue_updatetime IS 'GLUE更新时间';
COMMENT ON COLUMN job_template.child_jobid IS '子任务ID，多个逗号分隔';
COMMENT ON COLUMN job_template.trigger_last_time IS '上次调度时间';
COMMENT ON COLUMN job_template.trigger_next_time IS '下次调度时间';
COMMENT ON COLUMN job_template.job_json IS 'datax运行脚本';
COMMENT ON COLUMN job_template.jvm_param IS 'jvm参数';
COMMENT ON COLUMN job_template.project_id IS '所属项目Id';

-----------------------

ALTER TABLE job_jdbc_datasource
    ADD datasource VARCHAR2(45) NOT NULL ;

COMMENT ON COLUMN job_jdbc_datasource.datasource IS '数据源';

ALTER TABLE job_info
    ADD partition_info VARCHAR2(100);
COMMENT ON COLUMN job_info.partition_info IS '分区信息';

ALTER TABLE job_info
    ADD last_handle_code NUMBER(11) DEFAULT 0 ;

COMMENT ON COLUMN job_info.last_handle_code IS '最近一次执行状态';

ALTER TABLE job_jdbc_datasource
    ADD zk_adress VARCHAR2(200) DEFAULT NULL;

ALTER TABLE job_info
    MODIFY (executor_timeout NUMBER(11) DEFAULT 0);

COMMENT ON COLUMN job_info.executor_timeout IS '任务执行超时时间，单位分钟';


ALTER TABLE job_jdbc_datasource
    MODIFY (jdbc_username VARCHAR2(100 CHAR) DEFAULT NULL,
    jdbc_password VARCHAR2(200 CHAR) DEFAULT NULL);

COMMENT ON COLUMN job_jdbc_datasource.jdbc_username IS '用户名';
COMMENT ON COLUMN job_jdbc_datasource.jdbc_password IS '密码';

ALTER TABLE job_jdbc_datasource
    ADD database_name VARCHAR2(45 CHAR) DEFAULT NULL;

COMMENT ON COLUMN job_jdbc_datasource.database_name IS '数据库名';


ALTER TABLE job_registry
    ADD (cpu_usage NUMBER,
     memory_usage NUMBER,
     load_average NUMBER);

COMMENT ON COLUMN job_registry.cpu_usage IS 'CPU 使用率';
COMMENT ON COLUMN job_registry.memory_usage IS '内存使用率';
COMMENT ON COLUMN job_registry.load_average IS '平均负载';

---------------
CREATE TABLE job_permission (
                                id NUMBER(11) NOT NULL,
                                name VARCHAR2(50 CHAR) NOT NULL,
                                description VARCHAR2(11 CHAR),
                                url VARCHAR2(255 CHAR),
                                pid NUMBER(11),
                                PRIMARY KEY (id)
);

COMMENT ON TABLE job_permission IS '主键';
COMMENT ON COLUMN job_permission.id IS '主键';
COMMENT ON COLUMN job_permission.name IS '权限名';
COMMENT ON COLUMN job_permission.description IS '权限描述';

------------

ALTER TABLE job_info
    ADD replace_param_type VARCHAR2(255 CHAR);

COMMENT ON COLUMN job_info.replace_param_type IS '增量时间格式';

ALTER TABLE job_info
    ADD project_id NUMBER(11);

COMMENT ON COLUMN job_info.project_id IS '所属项目id';

ALTER TABLE job_info
    ADD reader_table VARCHAR2(255 CHAR);

COMMENT ON COLUMN job_info.reader_table IS 'reader表名称';

ALTER TABLE job_info
    ADD primary_key VARCHAR2(50 CHAR);

COMMENT ON COLUMN job_info.primary_key IS '增量表主键';

ALTER TABLE job_info
    ADD inc_start_id VARCHAR2(20 CHAR);

COMMENT ON COLUMN job_info.inc_start_id IS '增量初始id';

ALTER TABLE job_info
    ADD increment_type NUMBER(4);

COMMENT ON COLUMN job_info.increment_type IS '增量类型';

ALTER TABLE job_info
    ADD datasource_id NUMBER(11);

COMMENT ON COLUMN job_info.datasource_id IS '数据源id';

------------

CREATE TABLE job_project (
                             id NUMBER(11) NOT NULL,
                             name VARCHAR2(100 CHAR),
                             description VARCHAR2(200 CHAR),
                             user_id NUMBER(11),
                             flag NUMBER(4) DEFAULT 1,
                             create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             CONSTRAINT job_project_pk PRIMARY KEY (id)
);

COMMENT ON TABLE job_project IS 'key';

COMMENT ON COLUMN job_project.id IS 'key';
COMMENT ON COLUMN job_project.name IS 'project name';
COMMENT ON COLUMN job_project.user_id IS 'creator id';
COMMENT ON COLUMN job_project.flag IS '0 not available, 1 available';
COMMENT ON COLUMN job_project.create_time IS 'create time';
COMMENT ON COLUMN job_project.update_time IS 'update time';

----------
ALTER TABLE job_info
    RENAME COLUMN author TO user_id;

COMMENT ON COLUMN job_info.user_id IS '修改用户';

ALTER TABLE job_info
    MODIFY increment_type NUMBER(4) DEFAULT 0;
COMMENT ON COLUMN job_info.increment_type IS '增量类型';
--------------
INSERT INTO job_group VALUES (1, 'datax-executor', 'datax执行器', 1, 0, NULL);
INSERT INTO job_lock VALUES ('schedule_lock');

INSERT INTO job_log_report VALUES (20, TIMESTAMP '2019-12-07 00:00:00', 0, 0, 0);
INSERT INTO job_log_report VALUES (21, TIMESTAMP '2019-12-10 00:00:00', 77, 52, 23);
INSERT INTO job_log_report VALUES (22, TIMESTAMP '2019-12-11 00:00:00', 9, 2, 11);
INSERT INTO job_log_report VALUES (23, TIMESTAMP '2019-12-13 00:00:00', 9, 48, 74);
INSERT INTO job_log_report VALUES (24, TIMESTAMP '2019-12-12 00:00:00', 10, 8, 30);
INSERT INTO job_log_report VALUES (25, TIMESTAMP '2019-12-14 00:00:00', 78, 45, 66);
INSERT INTO job_log_report VALUES (26, TIMESTAMP '2019-12-15 00:00:00', 24, 76, 9);
INSERT INTO job_log_report VALUES (27, TIMESTAMP '2019-12-16 00:00:00', 23, 85, 10);
INSERT INTO job_user VALUES (1, 'admin', '$2a$10$2KCqRbra0Yn2TwvkZxtfLuWuUP5KyCWsljO/ci5pLD27pqR3TV1vy', 'ROLE_ADMIN', NULL);
