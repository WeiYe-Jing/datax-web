# datax-web

## data-admin

### 制作方式

- 编译datax-web

- 将packages下的datax-admin_${version}.tar.gz包移动至docker/admin文件夹一下

- 使用如下命令build

  ```shell
  #目前所在目录为${datax-web}/docker/admin
  docker build -t datax-admin-alpine:${version} . 
  ```

  

## data-executor

### 制作方式

- 将packages下的datax-executor{version}.tar.gz包移动至docker/executor文件夹一下

- 使用如下命令

  ```shell
  #目前所在目录为${datax-web}/docker/executor
  docker build -t datax-executor-alpine:${version} . 
  ```

  

## docker-compose

1. 在以上两步中已经将其build完成了,在docker目录下创建如下文件夹:

```shell
 #如果对docker-compose.yml进行修改后,其为自己修改的文件夹下创建
 mkdir -p mysql
```

   

2. 将项目中bin目录下的db文件夹整体移动至mysql下
```shell
  #目前所在目录为${datax-web}/docker
   cp ../bin/db docekr/
```



   

3. 运行如下命令,即可正常启动容器

   ```shell
   #目前所在目录为${datax-web}/docker
   docker-compose up -d 
   ```



## 相关参数介绍

- ### datax-admin
```shell
-e DB_HOST 127.0.0.1 #指定admin连接数据库的ip,注意,如果没有提前配置mysql,请按照readme依次配置导入相关数据库表,如果为本地mysql,请指定当前局域网的ip
-e DB_PORT 3306 #指定admin连接数据库的端口
-e DB_USERNAME=root
-e DB_PASSWORD=root
-e DB_DATABASE=datax_web #指定使用的数据库
-e SERVER_PORT=9527 #指定admin启动的port,注意改port为executor的DATAX_ADMIN_PORT
-e MAIL_USERNAME=username #指定mail的username
-e MAIL_PASSWORD=password #指定password
#注:先阶段只支持qq邮箱
```

###### agg:

```shell
#手动编译运行方法
docker build -t datax-admin-alpine:2.1.2 . \
&& docker run \
-p 9527:9527 \
--name datax-admin-alpine \
-e DB_USERNAME=root -e DB_PASSWORD=root -e DB_HOST=192.168.0.100 -e DB_PORT=3306 \
datax-admin-alpine:2.1.2 
#pull镜像运行方法
 docker run \
-p 9527:9527 \
--name datax-admin-alpine \
-e DB_USERNAME=root -e DB_PASSWORD=root -e DB_HOST=192.168.0.100 -e DB_PORT=3306 \
741069229/datax-admin-alpine:v2.1.2
```

- ### data-executor

 ```shell
  -e SERVER_PORT=8081 #指定executor的启动端口
  -e DATAX_ADMIN_HOST=127.0.0.1 #指定admin的ip 
  -e DATAX_ADMIN_PORT=9527  #指定admin的端口号
 ```

- ###### agg  

```shell
 docker run \
-p 8081:8081 \
--name datax-executor-alpine \
-e SERVER_PORT=8081  -e DATAX_ADMIN_HOST=127.0.0.1 -e DATAX_ADMIN_PORT=9527 \
741069229/datax-executor-alpine:v2.1.2
```













