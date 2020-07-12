![GitHub contributors](https://img.shields.io/github/contributors/WeiYe-Jing/datax-web)
![GitHub issues](https://img.shields.io/github/issues/WeiYe-Jing/datax-web)
![GitHub](https://img.shields.io/github/license/WeiYe-Jing/datax-web)
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/WeiYe-Jing/datax-web)
![](https://img.shields.io/badge/qq%E7%BE%A4-795380631-green.svg)

# DataX-Web

DataX Web是在DataX之上开发的分布式数据同步工具，提供简单易用的
操作界面，降低用户使用DataX的学习成本，缩短任务配置时间，避免配置过程中出错。用户可通过页面选择数据源即可创建数据同步任务，RDBMS数据源可批量创建数据同步任务，支持实时查看数据同步进度及日志并提供终止同步功能，集成并二次开发xxl-job可根据时间、自增主键增量同步数据。

任务"执行器"支持集群部署，支持执行器多节点路由策略选择，支持超时控制、失败重试、失败告警、任务依赖，执行器CPU.内存.负载的监控等等。后续还将提供更多的数据源支持、数据转换UDF、表结构同步、数据同步血缘等更为复杂的业务场景。

# System Requirements

- Language: Java 8（jdk版本建议1.8.201以上）<br>
  Python2.7(支持Python3需要修改替换datax/bin下面的三个python文件，替换文件在doc/datax-web/datax-python3下)
- Environment: MacOS, Windows,Linux
- Database: Mysql5.7



# Features

- 1、通过Web构建DataX Json；
- 2、DataX Json保存在数据库中，方便任务的迁移，管理；
- 3、Web实时查看抽取日志，类似Jenkins的日志控制台输出功能；
- 4、DataX运行记录展示，可页面操作停止DataX作业；
- 5、支持DataX定时任务，支持动态修改任务状态、启动/停止任务，以及终止运行中任务，即时生效；
- 6、调度采用中心式设计，支持集群部署；
- 7、任务分布式执行，任务"执行器"支持集群部署；
- 8、执行器会周期性自动注册任务, 调度中心将会自动发现注册的任务并触发执行；
- 9、路由策略：执行器集群部署时提供丰富的路由策略，包括：第一个、最后一个、轮询、随机、一致性HASH、最不经常使用、最近最久未使用、故障转移、忙碌转移等；
- 10、阻塞处理策略：调度过于密集执行器来不及处理时的处理策略，策略包括：单机串行（默认）、丢弃后续调度、覆盖之前调度；
- 11、任务超时控制：支持自定义任务超时时间，任务运行超时将会主动中断任务；
- 12、任务失败重试：支持自定义任务失败重试次数，当任务失败时将会按照预设的失败重试次数主动进行重试；
- 13、任务失败告警；默认提供邮件方式失败告警，同时预留扩展接口，可方便的扩展短信、钉钉等告警方式；
- 14、用户管理：支持在线管理系统用户，存在管理员、普通用户两种角色；
- 15、任务依赖：支持配置子任务依赖，当父任务执行结束且执行成功后将会主动触发一次子任务的执行, 多个子任务用逗号分隔；
- 16、运行报表：支持实时查看运行数据，以及调度报表，如调度日期分布图，调度成功分布图等；
- 17、指定增量字段，配置定时任务自动获取每次的数据区间，任务失败重试，保证数据安全；
- 18、页面可配置DataX启动JVM参数；
- 19、数据源配置成功后添加手动测试功能；
- 20、可以对常用任务进行配置模板，在构建完JSON之后可选择关联模板创建任务；
- 21、jdbc添加hive数据源支持，可在构建JSON页面选择数据源生成column信息并简化配置；
- 22、优先通过环境变量获取DataX文件目录，集群部署时不用指定JSON及日志目录；
- 23、通过动态参数配置指定hive分区，也可以配合增量实现增量数据动态插入分区；
- 24、任务类型由原来DataX任务扩展到Shell任务、Python任务、PowerShell任务；
- 25、添加HBase数据源支持，JSON构建可通过HBase数据源获取hbaseConfig，column；
- 26、添加MongoDB数据源支持，用户仅需要选择collectionName即可完成json构建；
- 27、添加执行器CPU、内存、负载的监控页面；
- 28、添加24类插件DataX JSON配置样例
- 29、公共字段（创建时间，创建人，修改时间，修改者）插入或更新时自动填充
- 30、对swagger接口进行token验证
- 31、任务增加超时时间，对超时任务kill datax进程，可配合重试策略避免网络问题导致的datax卡死。
- 32、添加项目管理模块，可对任务分类管理；
- 33、对RDBMS数据源增加批量任务创建功能，选择数据源，表即可根据模板批量生成DataX同步任务；
- 34、JSON构建增加ClickHouse数据源支持；
- 35、执行器CPU.内存.负载的监控页面图形化；
- 36、RDBMS数据源增量抽取增加主键自增方式并优化页面参数配置；
- 37、更换MongoDB数据源连接方式,重构HBase数据源JSON构建模块；
- 38、脚本类型任务增加停止功能；
- 39、rdbms json构建增加postSql，并支持构建多个preSql，postSql；
- 40、数据源信息加密算法修改及代码优化；
- 41、日志页面增加DataX执行结果统计数据；

# Quick Start：

##### 请点击：[Quick Start](https://github.com/WeiYe-Jing/datax-web/blob/master/userGuid.md)
##### Linux：[一键部署](https://github.com/WeiYe-Jing/datax-web/blob/master/doc/datax-web/datax-web-deploy.md)


# Introduction：

### 1.执行器配置(使用开源项目xxl-job)

![](https://datax-web.oss-cn-hangzhou.aliyuncs.com/doc/executor.png)

- 1、"调度中心OnLine:"右侧显示在线的"调度中心"列表, 任务执行结束后, 将会以failover的模式进行回调调度中心通知执行结果, 避免回调的单点风险;
- 2、"执行器列表" 中显示在线的执行器列表, 可通过"OnLine 机器"查看对应执行器的集群机器;

#### 执行器属性说明

![](https://datax-web.oss-cn-hangzhou.aliyuncs.com/doc/add_executor.png)

```
1、AppName: （与datax-executor中application.yml的datax.job.executor.appname保持一致）
   每个执行器集群的唯一标示AppName, 执行器会周期性以AppName为对象进行自动注册。可通过该配置自动发现注册成功的执行器, 供任务调度时使用;
2、名称: 执行器的名称, 因为AppName限制字母数字等组成,可读性不强, 名称为了提高执行器的可读性;
3、排序: 执行器的排序, 系统中需要执行器的地方,如任务新增, 将会按照该排序读取可用的执行器列表;
4、注册方式：调度中心获取执行器地址的方式；
    自动注册：执行器自动进行执行器注册，调度中心通过底层注册表可以动态发现执行器机器地址；
    手动录入：人工手动录入执行器的地址信息，多地址逗号分隔，供调度中心使用；
5、机器地址："注册方式"为"手动录入"时有效，支持人工维护执行器的地址信息；
```

### 2.创建数据源

![](https://datax-web.oss-cn-hangzhou.aliyuncs.com/doc/add_datasource.png)

第四步使用

### 3.创建任务模版

![](https://datax-web.oss-cn-hangzhou.aliyuncs.com/doc/template_list.png)

第四步使用



### 4. 构建JSON脚本

- 1.步骤一，步骤二，选择第二步中创建的数据源，JSON构建目前支持的数据源有hive,mysql,oracle,postgresql,sqlserver,hbase,mongodb,clickhouse 其它数据源的JSON构建正在开发中,暂时需要手动编写。

![](https://datax-web.oss-cn-hangzhou.aliyuncs.com/doc/build.png)

- 2.字段映射


![](https://datax-web.oss-cn-hangzhou.aliyuncs.com/doc/mapping.png)

- 3.点击构建，生成json,此时可以选择复制json然后创建任务，选择datax任务，将json粘贴到文本框。也可以点击选择模版，直接生成任务。

![](https://datax-web.oss-cn-hangzhou.aliyuncs.com/doc/select_template.png)

### 5.批量创建任务

![](https://datax-web.oss-cn-hangzhou.aliyuncs.com/doc/batch_build_r.png)
![](https://datax-web.oss-cn-hangzhou.aliyuncs.com/doc/batch_build_w.png)

### 6.任务创建介绍（关联模版创建任务不再介绍，具体参考4. 构建JSON脚本）

#### DataX任务

![](https://datax-web.oss-cn-hangzhou.aliyuncs.com/doc/datax.png)

#### Shell任务

![](https://datax-web.oss-cn-hangzhou.aliyuncs.com/doc/shell.png)

#### Python任务

![](https://datax-web.oss-cn-hangzhou.aliyuncs.com/doc/python.png)

#### PowerShell任务

![](https://datax-web.oss-cn-hangzhou.aliyuncs.com/doc/powershell.png)

- 任务类型：目前支持DataX任务、Shell任务、Python任务、PowerShell任务；
- 阻塞处理策略：调度过于密集执行器来不及处理时的处理策略；
    - 单机串行：调度请求进入单机执行器后，调度请求进入FIFO队列并以串行方式运行；
    - 丢弃后续调度：调度请求进入单机执行器后，发现执行器存在运行的调度任务，本次请求将会被丢弃并标记为失败；
    - 覆盖之前调度：调度请求进入单机执行器后，发现执行器存在运行的调度任务，将会终止运行中的调度任务并清空队列，然后运行本地调度任务；
- 增量增新建议将阻塞策略设置为丢弃后续调度或者单机串行
    - 设置单机串行时应该注意合理设置重试次数(失败重试的次数*每次执行时间<任务的调度周期)，重试的次数如果设置的过多会导致数据重复，例如任务30秒执行一次，每次执行时间需要20秒，设置重试三次，如果任务失败了，第一个重试的时间段为1577755680-1577756680，重试任务没结束，新任务又开启，那新任务的时间段会是1577755680-1577758680
- [增量参数设置](https://github.com/WeiYe-Jing/datax-web/blob/master/doc/datax-web/time-increment-desc.md)
- [分区参数设置](https://github.com/WeiYe-Jing/datax-web/blob/master/doc/datax-web/partition-dynamic-param.md)

### 7. 任务列表

![](https://datax-web.oss-cn-hangzhou.aliyuncs.com/doc/job.png)

### 8. 可以点击查看日志，实时获取日志信息,终止正在执行的datax进程

![](https://datax-web.oss-cn-hangzhou.aliyuncs.com/doc/job_log.png)
![](https://datax-web.oss-cn-hangzhou.aliyuncs.com/doc/log_stat.png)
![](https://datax-web.oss-cn-hangzhou.aliyuncs.com/doc/log_detail.png)

### 9.任务资源监控

![](https://datax-web.oss-cn-hangzhou.aliyuncs.com/doc/monitor.png)

### 10. admin可以创建用户，编辑用户信息

![](https://datax-web.oss-cn-hangzhou.aliyuncs.com/doc/user.png)


# UI

[前端github地址](https://github.com/WeiYe-Jing/datax-web-ui)

# 项目成员

- water

```
非常荣幸成为datax-web的Committer，从早期datax手工编写任务+配置，到datax-web界面化勾选创建任务+配置信息+调度管理，datax-web将数据同步工作的效率提升不少，相信后面后成为etl中不可或缺的生产力……
```

- Alecor

```
非常荣幸成为datax-web的Committer，datax-web旨在帮助用户从datax配置中解放出来，提供datax的Web化的管理能力。希望datax-web能为更多有需要的人服务，带来更好的简单、易用的体验！
```

- zhouhongfa

- liukunyuan

感谢贡献！

# Contributing

Contributions are welcome! Open a pull request to fix a bug, or open an Issue to discuss a new feature or change.

欢迎参与项目贡献！比如提交PR修复一个bug，或者新建 Issue 讨论新特性或者变更。

# Copyright and License

MIT License

Copyright (c) 2020 WeiYe

产品开源免费，并且将持续提供免费的社区技术支持。个人或企业内部可自由的接入和使用。

> 欢迎在 [登记地址](https://github.com/WeiYe-Jing/datax-web/issues/93) 登记，登记仅仅为了产品推广和提升社区开发的动力。

# v-2.1.2

### 新增

1. 添加项目管理模块，可对任务分类管理；
2. 对RDBMS数据源增加批量任务创建功能，选择数据源，表即可根据模板批量生成DataX同步任务；
3. JSON构建增加ClickHouse数据源支持；
4. 执行器CPU.内存.负载的监控页面图形化；
5. RDBMS数据源增量抽取增加主键自增方式并优化页面参数配置；
6. 更换MongoDB数据源连接方式,重构HBase数据源JSON构建模块；
7. 脚本类型任务增加停止功能；
8. rdbms json构建增加postSql，并支持构建多个preSql，postSql；
9. 合并datax-registry模块到datax-rpc中；
10.数据源信息加密算法修改及代码优化；
11.时间增量同步支持更多时间格式；
12.日志页面增加DataX执行结果统计数据；

### 升级：

1. PostgreSql，SQLServer，Oracle 数据源JSON构建增加schema name选择；
2. DataX JSON中的字段名称与数据源关键词一致问题优化；
3. 任务管理页面按钮展示优化；
4. 日志管理页面增加任务描述信息；
5. JSON构建前端form表单不能缓存数据问题修复;
6. HIVE JSON构建增加头尾选项参数;

### 备注：
2.1.1版本不建议升级，数据源信息加密方式变更会导致之前已加密的数据源解密失败，任务运行失败。
如果需要升级请重建数据源，任务。

# v-2.1.1

### 新增

1. 添加HBase数据源支持，JSON构建可通过HBase数据源获取hbaseConfig，column；
2. 添加MongoDB数据源支持，用户仅需要选择collectionName即可完成json构建；
3. 添加执行器CPU.内存.负载的监控页面；
4. 添加24类插件DataX JSON配置样例
5. 公共字段（创建时间，创建人，修改时间，修改者）插入或更新时自动填充
6. 对swagger接口进行token验证
7. 任务增加超时时间，对超时任务kill datax进程，可配合重试策略避免网络问题导致的datax卡死。

### 升级：

1. 数据源管理对用户名和密码进行加密，提高安全性；
2. 对JSON文件中的用户名密码进行加密，执行DataX任务时解密
3. 对页面菜单整理，图标升级，提示信息等交互优化；
4. 日志输出取消项目类名等无关信息，减小文件大小，优化大文件输出，优化页面展示；
5. logback为从yml中获取日志路径配置

### 修复：

1. 任务日志过大时，查看日志报错，请求超时；

# 项目规划

![](https://datax-web.oss-cn-hangzhou.aliyuncs.com/doc/plan.png)

# Contact us

### QQ交流群 795380631
