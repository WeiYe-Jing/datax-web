![Java](https://woolson.gitee.io/npmer-badge/Java-555555-1.8-44cc11-check-ffffff-square-gradient-shadow.svg)
![springboot](https://woolson.gitee.io/npmer-badge/springboot-555555-2.x-44cc11-check-ffffff-square-gradient-shadow.svg)
## preparation
- Language: Java 8
- Environment: MacOS, 16G RAM
- Database: Mysql5.7
- 建议Python2.7


## todo list

* [x] springboot重构项目
* [x] 集成swagger，方便调试
* [x] 集成mybatis plus和Mysql数据库存放应用数据
* [x] 网页端修改并持久化job配置的json到数据库
* [x] 网页端实时查看抽取日志，类似Jenkins的日志控制台输出功能
* [x] 实时查看抽取日志BUG功能修复2019-11-07
* [ ] 网页端各种读写插件模板生成，可以在页面组装使用
* [ ] 实现datax分布式作业
* [ ] 实现部分写插件支持自动建表功能


## 前端项目
[github地址](https://github.com/zhouhongfa/datax-vue-admin.git)
## how to run
### 1. 下载datax打包之后的文件或者github拉取datax代码打包，配置环境变量
```
 DATAX_HOME=G:\learndemo\springboot-datax\datax\bin
```

### 2. 执行datax-web/db下面的sql文件并修改application.yml数据库配置信息

### 3. application.yml配置数据抽取日志文件保存路径
                          
```
etlLogDir: D:\temp\logs\datax-web\
```

### 4. 终端访问测试作业接口
```
curl http://localhost:8080/startJob
```
可以看到成功跑完一个datax作业
![](https://raw.githubusercontent.com/peter1040080742/picbed/master/20190505162333.png)

### 5. 打开网页端启动作业
http://localhost:8080/index.html#/datax/job
![](https://raw.githubusercontent.com/huzekang/picbed/master/20190617120207.png)

### 6. 在线查看作业日志
![](https://raw.githubusercontent.com/huzekang/picbed/master/20190708102445.png)
