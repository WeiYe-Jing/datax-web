## dockerfile
将 admin 和 executor 分开构建和部署，executor 需要向 admin 注册，故需要增加 admin 的 host 配置项：datax.admin.host，类似 datax.admin.port，在 executor 模块的配置文件中


## k8s 部署
1. 准备好mysql，执行db下的sql脚本。（可在k8s安装mysql）
2. 配置数据库信息 datax-admin-bootstrap-conf.yaml，然后执行 `kubectl apply -f datax-admin-bootstrap-conf.yaml`
3. 启动 datax-admin: `kubectl apply -f deploy-datax-admin.yaml` 
4. 启动 datax-executor: `kubectl apply -f deploy-datax-executor.yaml`

> 说明：
1. datax-admin 使用 ingress(traefik) 访问，可自行更改。 
2. executor 启动数量可由 deploy-datax-executor.yaml 中 spec.replicas 更改
3. executor 启动 netty server(需要更改代码，非守护线程)，本身并不需要 servlet 容器。不需要引入 spring-boot-starter-web ，引入 spring-boot-starter 即可，也就不需要 server.port 参数 

