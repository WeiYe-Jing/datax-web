# datax-web部署方法
1. 下载本代码
2. 在datax-web根目录执行`mvn clean install`
3. 运行完成后会在根目录下生成`build`文件夹，里面有一个`datax-web-{VERSION}.tar.gz`的文件
4. 上传到服务器，执行`tar -zxvf datax-web-{VERSION}.tar.gz`解压
5. 进入解压后的目录，找到`bin`目录下面的`install.sh`文件，运行`./install.sh`配置
6. 四次提示都输入`y`
```shell
2023-10-26 22:52:46.687 [INFO] (27880) Creating directory: [/home/tomcat/datax/datax-web/datax-web-2.1.2/bin/../modules].
2023-10-26 22:52:46.712 [INFO] (27880)  ####### Start To Uncompress Packages ######
2023-10-26 22:52:46.716 [INFO] (27880) Uncompressing....
Do you want to decompress this package: [datax-admin_2.1.2_1.tar.gz]? (Y/N)y
2023-10-26 22:52:49.988 [INFO] (27880)  Uncompress package: [datax-admin_2.1.2_1.tar.gz] to modules directory
Do you want to decompress this package: [datax-executor_2.1.2_1.tar.gz]? (Y/N)y
2023-10-26 22:53:01.070 [INFO] (27880)  Uncompress package: [datax-executor_2.1.2_1.tar.gz] to modules directory
2023-10-26 22:53:01.473 [INFO] (27880)  ####### Finish To Umcompress Packages ######
Scan modules directory: [/home/tomcat/datax/datax-web/datax-web-2.1.2/bin/../modules] to find server under dataxweb
2023-10-26 22:53:01.478 [INFO] (27880)  ####### Start To Install Modules ######
2023-10-26 22:53:01.480 [INFO] (27880) Module servers could be installed:
 [datax-admin]  [datax-executor] 
Do you want to confiugre and install [datax-admin]? (Y/N)y
2023-10-26 22:53:51.775 [INFO] (27880)  Install module server: [datax-admin]
Start to make directory
2023-10-26 22:53:51.805 [INFO] (27944)  Start to build directory
2023-10-26 22:53:51.809 [INFO] (27944) Creating directory: [/home/tomcat/datax/datax-web/datax-web-2.1.2/modules/datax-admin/bin/../logs].
2023-10-26 22:53:51.814 [INFO] (27944) Directory or file: [/home/tomcat/datax/datax-web/datax-web-2.1.2/modules/datax-admin/bin/../conf] has been exist
2023-10-26 22:53:51.818 [INFO] (27944) Creating directory: [/home/tomcat/datax/datax-web/datax-web-2.1.2/modules/datax-admin/bin/../data].
end to make directory
Start to initalize database
Do you want to confiugre and install [datax-executor]? (Y/N)y
2023-10-26 22:54:02.278 [INFO] (27880)  Install module server: [datax-executor]
2023-10-26 22:54:02.306 [INFO] (27974)  Start to build directory
2023-10-26 22:54:02.310 [INFO] (27974) Creating directory: [/home/tomcat/datax/datax-web/datax-web-2.1.2/modules/datax-executor/bin/../logs].
2023-10-26 22:54:02.315 [INFO] (27974) Directory or file: [/home/tomcat/datax/datax-web/datax-web-2.1.2/modules/datax-executor/bin/../conf] has been exist
2023-10-26 22:54:02.319 [INFO] (27974) Creating directory: [/home/tomcat/datax/datax-web/datax-web-2.1.2/modules/datax-executor/bin/../data].
2023-10-26 22:54:02.324 [INFO] (27974) Creating directory: [/home/tomcat/datax/datax-web/datax-web-2.1.2/modules/datax-executor/bin/../json].
2023-10-26 22:54:02.329 [INFO] (27880)  ####### Finish To Install Modules ######
```
7. 修改`datax-admin`连接配置。**此处和开源初版的配置不同，因为改成了ORACLE**`
vim ./modules/datax-admin/conf/application.yml`，修改用户名、密码、驱动、connection-test-query，db-type改为oracle，id-type改为INPUT，以及其他的${}变量处
```yaml
spring:
  #数据源
  datasource:
    #    username: root
    #password: root
    #url: jdbc:mysql://localhost:3306/datax_web?serverTimezone=Asia/Shanghai&useLegacyDatetimeCode=false&useSSL=false&nullNamePatternMatchesAll=true&useUnicode=true&characterEncoding=UTF-8
    password: wHnUPiaoboduX6RW
    username: prip2
    url: jdbc:oracle:thin:@10.2.0.45:1521:prip
    driver-class-name: oracle.jdbc.driver.OracleDriver
    hikari:
      ## 最小空闲连接数量
      minimum-idle: 5
      ## 空闲连接存活最大时间，默认600000（10分钟）
      idle-timeout: 180000
      ## 连接池最大连接数，默认是10
      maximum-pool-size: 10
      ## 数据库连接超时时间,默认30秒，即30000
      connection-timeout: 30000
      connection-test-query: SELECT 1 from dual
      ##此属性控制池中连接的最长生命周期，值0表示无限生命周期，默认1800000即30分钟
      max-lifetime: 1800000
mybatis-plus:
  # mapper.xml文件扫描
  mapper-locations: classpath*:/mybatis-mapper/*Mapper.xml
  # 实体扫描，多个package用逗号或者分号分隔
  #typeAliasesPackage: com.yibo.essyncclient.*.entity
  global-config:
    # 数据库相关配置
    db-config:
      # 主键类型  AUTO:"数据库ID自增", INPUT:"用户输入ID", ID_WORKER:"全局唯一ID (数字类型唯一ID)", UUID:"全局唯一ID UUID";
      id-type: INPUT
      # 字段策略 IGNORED:"忽略判断",NOT_NULL:"非 NULL 判断"),NOT_EMPTY:"非空判断"
      field-strategy: NOT_NULL
      # 驼峰下划线转换
      column-underline: true
      # 逻辑删除
      logic-delete-value: 0
      logic-not-delete-value: 1
      # 数据库类型
      db-type: ORACLE
```
8. 修改`datax-executor`配置
```text
vi ./modules/{module_name}/bin/env.properties

### 执行datax的python脚本地址
PYTHON_PATH=/home/tomcat/datax/datax/bin/datax.py

### 保持和datax-admin服务的端口一致；默认是9527，如果没改datax-admin的端口，可以忽略
DATAX_ADMIN_PORT=
```
9. 启动和关闭
启动：sh /home/tomcat/datax/datax-web/datax-web-2.1.2/bin/start-all.sh
关闭：sh /home/tomcat/datax/datax-web/datax-web-2.1.2/bin/stop-all.sh.sh
10. 日志
datax-web start-all.sh会同时启动两个进程，一个datax-admin，一个datax-executor。前者负责能看到的web系统，后者负责调度具体的任务  
管理系统的日志路径：datax/datax-web/datax-web-2.1.2/modules/datax-admin/bin/console.out  
任务执行发起的日志路径：datax/datax-web/datax-web-2.1.2/modules/datax-executor/bin/console.out 
datax-admin的console.out中包含 >>>>>>>>> init datax-web admin scheduler success，代表启动成功
页面中资源监控可以看到 执行器：datax-executor注册地址xxxx代表datax-executor成功
11. 
