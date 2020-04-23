![Java](https://woolson.gitee.io/npmer-badge/Java-555555-1.8-44cc11-check-ffffff-square-gradient-shadow.svg)
![](https://img.shields.io/badge/springboot-2.1.4.RELEASE-red.svg)
![](https://img.shields.io/badge/qq%E7%BE%A4-795380631-green.svg)

# DataX-Web

DataX阿里在开源的时候并未提供任何可视化界面，我们在使用的过程中，需要将Json配置文件放到DataX的job路径下，随着业务的增加，配置文件不方便管理和迁移并且每次执行都需要记录命令。
目前DataX只支持单机版，多节点之间的协作不能控制，我们希望能有一款有友好的可视化界面，支持定时任务,支持分布式的数据同步利器，这也是该项目的目标。

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


# Quick Start：

##### 请点击：[Quick Start](https://github.com/WeiYe-Jing/datax-web/blob/master/userGuid.md)


# Introduction：

### 1.执行器配置(使用开源项目xxl-job)

![](http://q7vnain67.bkt.clouddn.com/executor.png)

- 1、"调度中心OnLine:"右侧显示在线的"调度中心"列表, 任务执行结束后, 将会以failover的模式进行回调调度中心通知执行结果, 避免回调的单点风险;
- 2、"执行器列表" 中显示在线的执行器列表, 可通过"OnLine 机器"查看对应执行器的集群机器;

#### 执行器属性说明

![](http://q7vnain67.bkt.clouddn.com/add_executor.png)

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

![](http://q7vnain67.bkt.clouddn.com/add_datasource.png)

第四步使用

### 3.创建任务模版

![](http://q7vnain67.bkt.clouddn.com/template_list.png)

第四步使用



### 4. 构建JSON脚本

- 1.步骤一，步骤二，选择第二步中创建的数据源，JSON构建目前支持的数据源有hive,mysql,oracle,postgresql,sqlserver,hbase,mongodb其它数据源的JSON构建正在开发中,暂时需要手动编写。

![](http://q7vnain67.bkt.clouddn.com/build.png)

- 2.字段映射


![](http://q7vnain67.bkt.clouddn.com/mapping.png)

- 3.点击构建，生成json,此时可以选择复制json然后创建任务，选择datax任务，将json粘贴到文本框。也可以点击选择模版，直接生成任务。

![](http://q7vnain67.bkt.clouddn.com/select_template.png)


### 5.任务创建介绍（关联模版创建任务不再介绍，具体参考4. 构建JSON脚本）

#### DataX任务

![](http://q7vnain67.bkt.clouddn.com/datax.png)

#### Shell任务

![](http://q7vnain67.bkt.clouddn.com/shell.png)

#### Python任务

![](http://q7vnain67.bkt.clouddn.com/python.png)

#### PowerShell任务

![](http://q7vnain67.bkt.clouddn.com/powershell.png)

- 任务类型：目前支持DataX任务、Shell任务、Python任务、PowerShell任务；
- 阻塞处理策略：调度过于密集执行器来不及处理时的处理策略；
    - 单机串行：调度请求进入单机执行器后，调度请求进入FIFO队列并以串行方式运行；
    - 丢弃后续调度：调度请求进入单机执行器后，发现执行器存在运行的调度任务，本次请求将会被丢弃并标记为失败；
    - 覆盖之前调度：调度请求进入单机执行器后，发现执行器存在运行的调度任务，将会终止运行中的调度任务并清空队列，然后运行本地调度任务；
- 增量增新建议将阻塞策略设置为丢弃后续调度或者单机串行
    - 设置单机串行时应该注意合理设置重试次数(失败重试的次数*每次执行时间<任务的调度周期)，重试的次数如果设置的过多会导致数据重复，例如任务30秒执行一次，每次执行时间需要20秒，设置重试三次，如果任务失败了，第一个重试的时间段为1577755680-1577756680，重试任务没结束，新任务又开启，那新任务的时间段会是1577755680-1577758680
- [增量参数设置](https://github.com/WeiYe-Jing/datax-web/blob/master/doc/datax-web/%E5%8A%A8%E6%80%81%E5%8F%82%E6%95%B0%E5%AE%8C%E6%88%90%E5%A2%9E%E9%87%8F%E6%8A%BD%E5%8F%96.md)
- [分区参数设置](https://github.com/WeiYe-Jing/datax-web/blob/master/doc/datax-web/%E5%88%86%E5%8C%BA%E5%8A%A8%E6%80%81%E4%BC%A0%E5%8F%82%E4%BD%BF%E7%94%A8.md)

### 6. 任务列表

![](http://q7vnain67.bkt.clouddn.com/job.png)

### 7. 可以点击查看日志，实时获取日志信息,终止正在执行的datax进程

![](http://q7vnain67.bkt.clouddn.com/job_log.png)
![](http://q7vnain67.bkt.clouddn.com/log_detail.png)

### 8.任务资源监控

![](http://q7vnain67.bkt.clouddn.com/monitor.png)

### 9. admin可以创建用户，编辑用户信息

![](http://q7vnain67.bkt.clouddn.com/user.png)

### 10.DataX JSON样例([样例地址](https://github.com/WeiYe-Jing/datax-web/blob/dev/doc/db/demo_job_info.sql))

![](http://q7vnain67.bkt.clouddn.com/json_demo.png)

# UI

[前端github地址](https://github.com/WeiYe-Jing/datax-web-ui)

# Contributing

Contributions are welcome! Open a pull request to fix a bug, or open an Issue to discuss a new feature or change.

欢迎参与项目贡献！比如提交PR修复一个bug，或者新建 Issue 讨论新特性或者变更。

# Copyright and License

MIT License

Copyright (c) 2020 WeiYe

产品开源免费，并且将持续提供免费的社区技术支持。个人或企业内部可自由的接入和使用。

> 欢迎在 [登记地址](https://github.com/WeiYe-Jing/datax-web/issues/14 ) 登记，登记仅仅为了产品推广和提升社区开发的动力。


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

![](http://q7vnain67.bkt.clouddn.com/plan.png)

# Contact us

### QQ交流群 795380631
