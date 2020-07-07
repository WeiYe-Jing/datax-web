### 环境准备
#### 1）基础软件安装
- MySQL (5.5+) 必选，对应客户端可以选装, Linux服务上若安装mysql的客户端可以通过部署脚本快速初始化数据库
- JDK (1.8.0_xxx) 必选
- Maven (3.6.1+) 必选
- DataX 必选
- Python (2.x) (支持Python3需要修改替换datax/bin下面的三个python文件，替换文件在doc/datax-web/datax-python3下)
 必选，主要用于调度执行底层DataX的启动脚本，默认的方式是以Java子进程方式执行DataX，用户可以选择以Python方式来做自定义的改造

### DataX安装请参考：[DataX](https://github.com/WeiYe-Jing/datax-web/blob/master/userGuid.md)

### DataX Web安装包准备
#### 1）下载官方提供的版本tar版本包
[点击下载](https://pan.baidu.com/s/13yoqhGpD00I82K4lOYtQhg) 提取码：cpsk

#### 2） 编译打包（官方提供的tar包跳过）
直接从Git上面获得源代码，在项目的根目录下执行如下命令
```
mvn clean install 
```
执行成功后将会在工程的build目录下生成安装包
```
build/datax-web-{VERSION}.tar.gz
```

### 开始部署
#### 1）解压安装包
在选定的安装目录，解压安装包
```
tar -zxvf datax-web-{VERSION}.tar.gz
```
#### 2）执行一键安装脚本
进入解压后的目录，找到bin目录下面的install.sh文件，如果选择交互式的安装，则直接执行
```
./bin/install.sh
```
在交互模式下，对各个模块的package压缩包的解压以及configure配置脚本的调用，都会请求用户确认,可根据提示查看是否安装成功，如果没有安装成功，可以重复尝试；
如果不想使用交互模式，跳过确认过程，则执行以下命令安装
```
./bin/install.sh --force
```
#### 3）数据库初始化
如果你的服务上安装有mysql命令，在执行安装脚本的过程中则会出现以下提醒：
```
Scan out mysql command, so begin to initalize the database
Do you want to initalize database with sql: [{INSTALL_PATH}/bin/db/datax-web.sql]? (Y/N)y
Please input the db host(default: 127.0.0.1): 
Please input the db port(default: 3306): 
Please input the db username(default: root): 
Please input the db password(default: ): 
Please input the db name(default: exchangis)
```
按照提示输入数据库地址，端口号，用户名，密码以及数据库名称，大部分情况下即可快速完成初始化。
如果服务上并没有安装mysql命令，则可以取用目录下/bin/db/datax-web.sql脚本去手动执行，完成后修改相关配置文件
```
vi ./modules/datax-admin/conf/bootstrap.properties
```
```
#Database
#DB_HOST=
#DB_PORT=
#DB_USERNAME=
#DB_PASSWORD=
#DB_DATABASE=
```
按照具体情况配置对应的值即可。


#### 4) 配置
安装完成之后，


在项目目录：
/modules/datax-admin/bin/env.properties 配置邮件服务(可跳过)

```
MAIL_USERNAME=""
MAIL_PASSWORD=""
```


在项目目录下/modules/datax-execute/bin/env.properties 指定PYTHON_PATH的路径

```
vi ./modules/{module_name}/bin/env.properties

### 执行datax的python脚本地址
PYTHON_PATH=

### 保持和datax-admin服务的端口一致；默认是9527，如果没改datax-admin的端口，可以忽略
DATAX_ADMIN_PORT=

````


#### 5）启动服务
##### - 一键启动所有服务
```
./bin/start-all.sh
```
中途可能发生部分模块启动失败或者卡住，可以退出重复执行，如果需要改变某一模块服务端口号，则：
```
vi ./modules/{module_name}/bin/env.properties
```
找到SERVER_PORT配置项，改变它的值即可。
当然也可以单一地启动某一模块服务：
```
./bin/start.sh -m {module_name}
```

##### - 一键取消所有服务
```
./bin/stop-all.sh
```
当然也可以单一地停止某一模块服务：
```
./bin/stop.sh -m {module_name}
```

#### 6）查看服务（注意！注意！）

 在Linux环境下使用JPS命令，查看是否出现DataXAdminApplication和DataXExecutorApplication进程，如果存在这表示项目运行成功
 
 #### 如果项目启动失败，请检查启动日志：modules/datax-admin/bin/console.out或者modules/datax-executor/bin/console.out

---
Tips: 脚本使用的都是bash指令集，如若使用sh调用脚本，可能会有未知的错误

#### 7）运行
   
   部署完成后，在浏览器中输入 http://ip:port/index.html 就可以访问对应的主界面（ip为datax-admin部署所在服务器ip,port为为datax-admin 指定的运行端口）

   输入用户名 admin  密码 123456 就可以直接访问系统
   
### 8) 运行日志
   部署完成之后，在modules/对应的项目/data/applogs下(用户也可以自己指定日志，修改application.yml
   中的logpath地址即可)，用户可以根据此日志跟踪项目实际启动情况   
   
### 9）集群部署

   修改modules/datax-executor/conf/application.yml文件下admin.addresses地址。
   为了方便单机版部署，项目目前没有将ip部分配置到env.properties，部署多节点时可以将整个地址作为变量配置到env文件。
   
   将官方提供的tar包或者编译打包的tar包上传到服务节点，按照步骤5中介绍的方式单一地启动某一模块服务即可。例如执行器需要部署多个节点，仅需启动执行器项目，执行
    ```
    ./bin/start.sh -m datax-executor
    ```
   
调度中心、执行器支持集群部署，提升调度系统容灾和可用性。
   
    * 1.调度中心集群：
       
        DB配置保持一致；<br>
        集群机器时钟保持一致（单机集群忽视）；<br>
        
    * 2.执行器集群:
    
        执行器回调地址(admin.addresses）需要保持一致；执行器根据该配置进行执行器自动注册等操作。
        
        同一个执行器集群内AppName（executor.appname）需要保持一致；调度中心根据该配置动态发现不同集群的在线执行器列表。
        
        
### 10) Contact us

### QQ交流群 795380631   
