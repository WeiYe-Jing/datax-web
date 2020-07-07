### 下载依赖文件
```
mkdir -p /opt/datax-web
cd /opt/datax-web
yum install -y svn
svn checkout https://github.com/WeiYe-Jing/datax-web/trunk/docker
```
### 安装MySQL数据库
```
docker run --name mysql5728 \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=rootpassw0rd \
  --restart=unless-stopped \
  -d mysql:5.7.28
```
### 为datax-web初始化MySQL数据库
```
mysql -h172.17.10.94 -uroot -prootpassw0rd
```
```
CREATE DATABASE `dataxweb` DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
use dataxweb;
source /opt/datax-web/docker/datax_web.sql;
flush privileges;
```
### datax-web容器介绍
容器已经集成
- jdk1.8.0_251
- https://github.com/alibaba/DataX
- datax-web-2.1.2版 https://github.com/WeiYe-Jing/datax-web
以上都是在2020.6.24下载

### 为datax-web修改接入mysql配置文件
```
vi /opt/datax-web/docker/bootstrap.properties
```
### 启动datax-web容器
```
docker run --name datax-web \
  -p 9527:9527 \
  -v /opt/datax-web/docker/bootstrap.properties:/opt/datax-web-2.1.2/modules/datax-admin/conf/bootstrap.properties \
  --restart=unless-stopped \
  -d registry.cn-hangzhou.aliyuncs.com/hd2020/ka:datax-web-20200624
```
### 登录datax-web
http://172.17.10.94:9527/index.html
admin
123456

### 查看datax-admin日志
```
docker logs -f --tail 1000 datax-web
```
### 查看datax-executor日志
```
docker exec -it datax-web bash;
```
```
tail -F /opt/datax-web-2.1.2/modules/datax-executor/bin/console.out
```
