![Java](https://woolson.gitee.io/npmer-badge/Java-555555-1.8-44cc11-check-ffffff-square-gradient-shadow.svg)
![](https://img.shields.io/badge/springboot-2.1.4.RELEASE-red.svg)
![](https://img.shields.io/badge/qq%E7%BE%A4-776939467-green.svg)

## DataX-Web

DataX阿里的开源的时候并未提供任何可视化界面，我们在使用的过程中，需要将Json配置文件放到Datax的job路径下，随着业务的增加，配置文件不方便管理和迁移并且每次执行都需要记录命令。
目前DataX只支持单机版，多节点之间的协作不能控制，我们希望存在一款有友好的可视化界面，支持定时任务,支持分布式的数据同步利器，这也是该项目的目标。

## System Requirements

- Language: Java 8
- Environment: MacOS, Windows,Linux
- Database: Mysql5.7
- Python2.7(支持Python3需要修改替换datax/bin下面的三个python文件，替换文件在doc/datax源码阅读笔记/datax-python3下)


## TODO

* [x] springboot重构项目
* [x] 集成swagger，方便调试
* [x] 集成mybatis plus和Mysql数据库存放应用数据
* [x] 网页端修改并持久化job配置的json到数据库
* [x] 网页端实时查看抽取日志，类似Jenkins的日志控制台输出功能
* [x] 实时查看抽取日志BUG功能修复
* [ ] job运行记录展示，页面操作停止datax作业（开发中）
* [ ] 实现datax分布式作业（开发中）
* [ ] 实现datax集成定时任务（开发中）
* [ ] 网页端各种读写插件模板生成，可以在页面组装使用
* [ ] 实现部分写插件支持自动建表功能


## UI

[前端github地址](https://github.com/WeiYe-Jing/datax-vue-admin.git)

## Quick Start

### 1. 下载datax打包之后的文件或者github拉取datax代码打包，配置环境变量
```
 DATAX_HOME=G:\learndemo\springboot-datax\datax\bin
```

### 2. 执行datax-web/db下面的sql文件并修改application.yml数据库配置信息

### 3. application.yml配置数据抽取日志文件保存路径
                          
```
etlLogDir: D:\temp\logs\datax-web\
```

### 4. idea启动 datax-admin

### 5. 启动成功后打开页面
http://localhost:8080/index.html#/datax/job
![](https://github.com/WeiYe-Jing/datax-web/blob/master/doc/img/20191119100901.png)

### 6. 点击作业配置，创建作业
![](https://github.com/WeiYe-Jing/datax-web/blob/master/doc/img/20191119101258.png)

### 7. 完成创建作业之后，点击同步任务，选择任务，点击启动
![](https://github.com/WeiYe-Jing/datax-web/blob/master/doc/img/20191119101431.png)

### 8. 可以点击查看日志，实时获取日志信息
![](https://github.com/WeiYe-Jing/datax-web/blob/master/doc/img/20191119102551.png)

## Contact us

### QQ交流群 776939467
