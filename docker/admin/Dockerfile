FROM wenyangchou/jdk:8.0
MAINTAINER zhouwenyang
ADD datax-admin-2.1.2.jar /root/app.jar
RUN mkdir /root/log
ARG SERVER_PORT=9527
ARG DB_USERNAME=username
ARG DB_PASSWORD=password
ARG DB_HOST=127.0.0.1
ARG DB_PORT=3306
ARG DB_DATABASE=dataxweb
ARG MAIL_USERNAME=username
ARG MAIL_PASSWORD=password
ENV SERVER_PORT=${SERVER_PORT} \
    DB_USERNAME=${DB_USERNAME} \
    DB_PASSWORD=${DB_PASSWORD} \
    DB_HOST=${DB_HOST} \
    DB_PORT=${DB_PORT} \
    DB_DATABASE=${DB_DATABASE} \
    MAIL_USERNAME=${MAIL_USERNAME} \
    MAIL_PASSWORD=${MAIL_PASSWORD}
EXPOSE 9527
ENTRYPOINT ["java","-jar","-Duser.timezone=GMT+08","-Dlogging.level.path=/root/log","/root/app.jar"]