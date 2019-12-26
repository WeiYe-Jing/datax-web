![Java](https://woolson.gitee.io/npmer-badge/Java-555555-1.8-44cc11-check-ffffff-square-gradient-shadow.svg)
![](https://img.shields.io/badge/springboot-2.1.4.RELEASE-red.svg)
![](https://img.shields.io/badge/qq%E7%BE%A4-776939467-green.svg)

## DataX-Web

DataX阿里的开源的时候并未提供任何可视化界面，我们在使用的过程中，需要将Json配置文件放到DataX的job路径下，随着业务的增加，配置文件不方便管理和迁移并且每次执行都需要记录命令。
目前DataX只支持单机版，多节点之间的协作不能控制，我们希望能有一款有友好的可视化界面，支持定时任务,支持分布式的数据同步利器，这也是该项目的目标。

## System Requirements

- Language: Java 8<br>
  Python2.7(支持Python3需要修改替换datax/bin下面的三个python文件，替换文件在doc/datax-web/datax-python3下)
- Environment: MacOS, Windows,Linux
- Database: Mysql5.7



## Features

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

## Quick Start

### 1. 下载datax打包之后的文件或者github拉取datax代码打包

### 2. 拉取release最新版本到本地代码库，执行doc/db下面的sql文件

### 3. 修改datax_admin下application.yml的数据库配置信息及邮件地址信息

### 4. 修改datax-executor下application.yml文件

- 1、datax.job.admin.addresses(调度中心地址，多个以逗号分隔)
- 2、datax.job.executor.logpath(数据抽取日志文件保存路径)
- 3、datax.executor.jsonpath(datax json临时文件保存路径)
- 4、datax.pypath(datax/bin/datax.py)注意：是第一步中DataX打包好的，DataX启动文件的地址

### 5.执行器配置(使用开源项目xxl-job)
![](https://github.com/WeiYe-Jing/datax-web/blob/master/doc/img/executor.png)
- 1、"调度中心OnLine:"右侧显示在线的"调度中心"列表, 任务执行结束后, 将会以failover的模式进行回调调度中心通知执行结果, 避免回调的单点风险;
- 2、"执行器列表" 中显示在线的执行器列表, 可通过"OnLine 机器"查看对应执行器的集群机器;
#### 执行器属性说明
![](https://github.com/WeiYe-Jing/datax-web/blob/master/doc/img/add_executor.png)
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
                     
### 6. idea启动 datax-admin，datax-executor

### 7. 启动成功后打开页面（默认管理员用户名：admin 密码：123456）
http://localhost:8080/index.html#/dashboard
![](https://github.com/WeiYe-Jing/datax-web/blob/master/doc/img/dashboard.png)

### 8. 构建JSON脚本
![](https://github.com/WeiYe-Jing/datax-web/blob/master/doc/img/build.png)

### 9. 创建任务
![](https://github.com/WeiYe-Jing/datax-web/blob/master/doc/img/add_job.png)

- 阻塞处理策略：调度过于密集执行器来不及处理时的处理策略；
    - 单机串行：调度请求进入单机执行器后，调度请求进入FIFO队列并以串行方式运行；
    - 丢弃后续调度：调度请求进入单机执行器后，发现执行器存在运行的调度任务，本次请求将会被丢弃并标记为失败；
    - 覆盖之前调度：调度请求进入单机执行器后，发现执行器存在运行的调度任务，将会终止运行中的调度任务并清空队列，然后运行本地调度任务；
[增量参数设置](https://github.com/WeiYe-Jing/datax-web/blob/master/doc/datax-web/%E5%8A%A8%E6%80%81%E5%8F%82%E6%95%B0%E5%AE%8C%E6%88%90%E5%A2%9E%E9%87%8F%E6%8A%BD%E5%8F%96.md
)
### 10. 任务列表
![](https://github.com/WeiYe-Jing/datax-web/blob/master/doc/img/job.png)

### 11. 可以点击查看日志，实时获取日志信息,终止正在执行的datax进程
![](https://github.com/WeiYe-Jing/datax-web/blob/master/doc/img/job_log.png)
![](https://github.com/WeiYe-Jing/datax-web/blob/master/doc/img/log_detail.png)

### 12. admin可以创建用户，编辑用户信息
![](https://github.com/WeiYe-Jing/datax-web/blob/master/doc/img/user.png)

## Linux部署说明
Quick Start操作完前四步之后
- 5、执行mvn package -Dmaven.test.skip=true 
- 6、分别将datax-admin、datax-executor模块target下datax-admin-1.0.0.jar、datax-executor-1.0.0.jar放到对应服务器
- 7、分别执行java命令启动项目 nohup java -Xmx1024M -Xms1024M -Xmn448M -XX:MaxMetaspaceSize=192M -XX:MetaspaceSize=192M -jar datax-admin-1.0.0.jar --server.port=8080&
## UI
[前端github地址](https://github.com/WeiYe-Jing/datax-vue-admin.git)

## 完成功能
- 修复前端页面自适应问题
- 修复添加任务时json转换失败的问题
- 修复构建json时数据库连接增加问题
- 1、指定增量字段，自动获取每次的数据区间
- 2、页面配置datax启动参数
- 3、数据源连接错误提醒功能
## TODO List
- 1、从源表到目标端表的自动创建
- 2、任务批量导入功能


## Contact us

### QQ交流群 776939467
