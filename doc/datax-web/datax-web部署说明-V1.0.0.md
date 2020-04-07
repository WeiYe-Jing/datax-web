# datax-web部署方案

[datax-web](https://github.com/WeiYe-Jing/datax-web)是针对datax开发的一款可视化解决方案，可以在web端实现对datax的可视化操作，以及相关的任务创建、管理、调度以及日志查看。现在简单介绍一下在linux环境下datax-web相关部署方案。


## 一、依赖资源
1、[datax：datax-web的核心资源包](https://github.com/alibaba/DataX)  
2、jdk（jdk版本建议1.8.201以上）  
3、Maven（编译工具）   
4、python （推荐python 2.7, python 3需要修改datax相关配置）  
5、tomcat  （可不装）
6、mysql 5.7 或者8 （用于存放datax-web相关用户信息以及任务信息）

## 二、安装方案

由于datax 以及data-web均需要依赖于java以及python环境，为了保障后续测试以及系统稳定运行，我们先安装java等相关环境。如果相关环境已经搭建好，可以直接查看第七节，datax-web部署的相关内容。

### 1.安装java
#### 1.1、查找java相关的列表
 ```
yum -y list java*
yum search jdk
 ```

#### 1.2、安装jdk
 ```
yum install java-1.8.0-openjdk.x86_64
 ```

#### 1.3、完成安装后验证
 ```
java -version
 ```

#### 1.4、通过yum安装的默认路径为：/usr/lib/jvm

#### 1.5、将jdk的安装路径加入到JAVA_HOME,添加环境变量
 ```
vi /etc/profile
 ```

在文件最后加入：
 ```
#set java environment
JAVA_HOME=/usr/lib/jvm/jre-1.6.0-openjdk.x86_64
PATH=$PATH:$JAVA_HOME/bin
CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
export JAVA_HOME CLASSPATH PATH
```

修改/etc/profile之后让其生效
```
. /etc/profile 
 ```
（注意 . 之后应有一个空格）


### 2、安装tomcat
jar包需要承载在tomcat中运行，不知道为啥，直接用java运行jar包会报错，先暂且装上tomcat吧

#### 2.1、安装 
```
yum -y install tomcat 
```
#### 2.2、查看状态 
```
systemctl status tomcat 
```
#### 2.3、启动tomcat: 
```
systemctl start tomcat
```
开机自启动的后面再配置

### 3、安装mysql（8.0）

#### 3.1安装前先更新系统所有包
```
sudo yum update
```
（感觉不更新也没事，还好服务器网速快，几下就更新完了）


#### 3.2添加 Yum 包
```
wget https://dev.mysql.com/get/mysql80-community-release-el7-1.noarch.rpm
```
 或者 
```
wget http://repo.mysql.com/mysql80-community-release-el7-1.noarch.rpm
```

#### 3.3 安装发行包
```
sudo rpm -ivh mysql80-community-release-el7-1.noarch.rpm
```

#### 3.4 安装社区版server
```
sudo yum -y install mysql-community-server
```

#### 3.5 启动守护进程
```
sudo systemctl start mysqld
```

#### 3.6 查看状态
```
sudo systemctl status mysqld
```

#### 3.7查看版本
```
mysql -V
```

#### 3.8 修改密码
MySQL 安装过程中会为 root 用户生成一个临时密码，保存在 /var/log/mysqld.log 中。通过以下命令查看：
```
sudo grep 'temporary password' /var/log/mysqld.log
```

进入 MySQL 客户端修改：
```
mysql -u root -p
```
输入以上文件查看的密码，进入mysql
```
ALTER USER 'root'@'localhost' IDENTIFIED BY 'your passowrd';
ALTER USER 'root'@ IDENTIFIED BY 'your passowrd';
```
***记住一定要加 ; ,否则Mysql无法运行***

密码强度要求是：不少于12字符，必须包含大写字母、小写字母、数字和特殊字符。

#### 3.9 配置远程访问
mysql配置好后，无法远程访问，需要进行相关配置

登录mysql后进行相关更改,将原有本地（localhost）访问权限修改为*
```
mysql -u root -p;
use mysql;
select host, user, authentication_string, plugin from user;
GRANT ALL ON *.* TO 'root'@'%';
update user set host = '%' where user = 'root';
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'root';
FLUSH PRIVILEGES;

```
按CTRL+D 退出mysql编辑模式

#### 3.10 防火墙相关配置
此步骤完成后，可以使用Navicat对mysql进行测试。如果无法连接，可以检查防火墙的情况，查看是否开启了相关端口。
```
systemctl status firewalld
firewall-cmd --query-port=3306/tcp
firewall-cmd --add-port=3306/tcp --permanent
firewall-cmd --reload
firewall-cmd --query-port=3306/tcp
```

如果数据库工具可以连接，说明mysql已经配置完成。



### 4 python 安装
linux中自带python 版本，可以直接进行查看
输入
```
python
```
直接查看结果，如果没有，再自己安装

### 5 datax安装
#### 5.1 下载
直接到github下载[datax部署包](http://datax-opensource.oss-cn-hangzhou.aliyuncs.com/datax.tar.gz)即可，不需要对源码进行编译

#### 5.2 解压
安装后对文件进行解压
一般将文件放到usr/local/src/ 文件夹下
```
cd /usr/local/src
tar -zxvf datax.tar.gz
chmod 755 datax
cd datax
```

#### 5.3 测试
测试datax是否安装好
```
python  /usr/local/src/datax/bin/datax.py   /usr/local/src/datax/job/job.json
```
通过查看控制台结果，可以判断是否安装成功

#### 5.4 环境变量配置（非必须）
 ```
vi /etc/profile
 ```
在文件最后加入：
 ```
#set datax environment
DATAX_HOME=/usr/local/src/datax
PATH=$PATH:$DATAX_HOME/bin
export DATAX_HOME  PATH
```

修改/etc/profile之后让其生效
```
. /etc/profile 
 ```

此处配置完成后，后续配置datax-web后可以免去配置一些项目，如果不配环境变量，系统会默认读取/datax-executor/src/main/resources/application.yml文件下的logpath、jsonpath、pypath。如果配置了环境变量，上面三个path可以忽略，系统会优先使用环境变量。

### 6 maven
安装maven是为了编译data-web，datax-web其中的配置文件需要根据环境进项更改，编译成对应的jar包，因此需要安装maven进行编译

```
sudo wget http://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -O /etc/yum.repos.d/epel-apache-maven.repo
sudo sed -i s/\$releasever/6/g /etc/yum.repos.d/epel-apache-maven.repo
sudo yum install -y maven
 
mvn -version
Apache Maven 3.3.9 (bb52d8502b132ec0a5a3f4c09453c07478323dc5; 2015-11-11T00:41:47+08:00)
Maven home: /usr/share/apache-maven
Java version: 1.7.0_79, vendor: Oracle Corporation
Java home: /usr/java/jdk1.7.0_79/jre
Default locale: en_US, platform encoding: UTF-8
OS name: "linux", version: "2.6.32-642.15.1.el6.x86_64", arch: "amd64", family: "unix"
```

### 7 部署 datax-web
####  7.1 下载
到github上下载datax-web
```
git clone https://github.com/WeiYe-Jing/datax-web.git
```
没有安装git的话可以直接下载zip，并进行解压

#### 7.2 数据库构建
在datax-web文件夹中，找到 doc/db/datax_web.sql 文件，用navicat等软件连接mysql数据库后，新建datax_web数据库,然后执行数据库脚本。  
**（注意不要执行其他sql脚本，并且执行完成后检查执行结，目前版本有12张表，部分表因mysql版本问题可能执行不成功，要找出来及时处理，本次使用mysql8.0。可以执行成功，使用mysql5.7版本，时间字段创建报错导致失败）**



#### 7.3 配置更改
datax-web 中比较核心的就是datax-admin（负责UI界面以及接口层）组件以及datax-executor组件（datax调度器）。
datax-admin中需要配置数据库相关信息
datax-executor需要配置 datax-admin发布的地址、调度器端口、自身日志位置以及datax（datax.py文件位置）
datax-executor配置datax-admin的端口以及地址并且执行启动后，会自动在data-admin中注册执行器

datax-admin 修改文件位置  /datax-admin/src/main/resources/application.yml
```

 datasource:
    username: root
    password: your password
    url: jdbc:mysql://ip:port/datax_web?serverTimezone=Asia/Shanghai&useLegacyDatetimeCode=false&useSSL=false&nullNamePatternMatchesAll=true&useUnicode=true&characterEncoding=UTF-8
    driver-class-name: com.mysql.jdbc.Driver

```

datax-executor 修改文件位置  /datax-executor/src/main/resources/application.yml

```
datax:
  job:
    admin:
      ### datax-web admin address  datax-admin发布后的端口以及地址，此处一定要跟实际相符
      addresses: http://ip:port
    executor:
      appname: datax-executor
      ip:
      ### 此处端口为执行器端口
      port: 5555
      ### job log path （此处是datax-web执行器自身日志存储位置）
      logpath: /usr/local/src/dataxweb/logs   
      ### job log retention days
      logretentiondays: 30
    ### job, access token
    accessToken:

  executor:
  ### 临时文件json保存位置
    jsonpath: /usr/local/src/dataxweb/json/

  #datax自身的 data.py的保存位置，此处一定要配置正确
  pypath: /usr/local/src/datax/bin/datax.py

```

如果不配DATAX_HOME 环境变量，系统会默认读取/datax-executor/src/main/resources/application.yml文件下的logpath、jsonpath、pypath。如果配置了环境变量，上面三个path可以忽略，系统会优先使用环境变量。

#### 7.4 编译
   datax-web并不是可执行文件，需要编译生成jar包才能运行。可以先进入datax-web 文件夹中，然后运行mvn命令
   ```
   mvn package -Dmaven.test.skip=true
   ```
   运行该命令后，会在各自的target文件夹下生成对应的jar包
    /datax-admin/target/datax-admin-2.1.1.jar 以及  /datax-executor/target/datax-executor-2.1.1.jar
    将以上两个文件拷贝至程序执行文件夹就可以了，本次存放在  /usr/local/src/dataxweb文件夹下
    

#### 7.5 执行

  jar包需要用命令启用以后才能执行，此次选用nohup命令，保障程序可以在后台运行，即使关闭控制台后，程序依然可以安全运行。

  ```
nohup java -Xmx1024M -Xms1024M -Xmn448M -XX:MaxMetaspaceSize=192M -XX:MetaspaceSize=192M -jar datax-admin-2.1.1.jar --server.port=9999&

nohup java -Xmx1024M -Xms1024M -Xmn448M -XX:MaxMetaspaceSize=192M -XX:MetaspaceSize=192M -jar datax-executor-2.1.1.jar --server.port=6888&
  ```

  （此处datax-admin设定的端口需要与datax-executor中配置的admin.addresses端口完全一致，否则会导致执行器无法正确注册，datax-executor的端口可以自行配置，但是不能与datax-executor配置的executor.port重复，否则会发生端口被占用的情况，此处还不是明白为啥一个程序要两个端口）


##### 7.6 运行
   
   部署完成后，在浏览器中输入 http://ip:port/index.html 就可以访问对应的主界面（ip为datax-admin部署所在服务器ip,port为为datax-admin 指定的运行端口）

   输入用户名 admin  密码 123456 就可以直接访问系统



