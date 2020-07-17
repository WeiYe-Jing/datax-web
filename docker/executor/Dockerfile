FROM wenyangchou/jdk-maven:8.0
MAINTAINER zhouwenyang
ADD datax-executor-2.1.2.jar /root/app.jar
ADD datax /root/datax
RUN mkdir /root/log
RUN mkdir /root/json
ARG SERVER_PORT=8081
ARG DATAX_ADMIN_HOST=127.0.0.1
ARG DATAX_ADMIN_PORT=9527
ARG EXECUTOR_PORT=9999
ENV SERVER_PORT=${SERVER_PORT} \
    DATAX_ADMIN_HOST=${DATAX_ADMIN_HOST} \
    DATAX_ADMIN_PORT=${DATAX_ADMIN_PORT} \
    EXECUTOR_PORT=${EXECUTOR_PORT}
EXPOSE 8081
ENTRYPOINT ["java","-jar","-Duser.timezone=GMT+08","-Ddatax.executor.jsonpath=/root/json","-Dlogging.path=/root/log","-Ddatax.pypath=/root/datax","-Ddatax.job.executor.logpath=/root/log","/root/app.jar"]