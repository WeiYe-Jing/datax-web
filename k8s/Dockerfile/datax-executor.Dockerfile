# docker build -t yangzhuangqiu/datax:executor-2.1.2  .
FROM yangzhuangqiu/base:alpine-python2.7-java1.8
LABEL MAINTAINER="yangzhuangqiu <yangzhuangqiu@qq.com>"
WORKDIR /opt/
ADD datax.tar.gz /opt/
ADD datax-executor_2.1.2_1.tar.gz /opt/

ENTRYPOINT exec java $JAVA_OPTS -classpath /opt/datax-executor/lib/*:/opt/datax-executor/conf:. com.wugui.datax.executor.DataXExecutorApplication
