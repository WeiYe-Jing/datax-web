# docker build -t yangzhuangqiu/datax:admin-2.1.2 .
FROM yangzhuangqiu/java:8u191-alpine
LABEL MAINTAINER="yangzhuangqiu <yangzhuangqiu@qq.com>"
WORKDIR /opt/
ADD datax-admin_2.1.2_1.tar.gz /opt/

ENTRYPOINT exec java $JAVA_OPTS -classpath /opt/datax-admin/lib/*:/opt/datax-admin/conf:. com.wugui.datax.admin.DataXAdminApplication
