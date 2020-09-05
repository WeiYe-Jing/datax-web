FROM alpine:3.10.3


MAINTAINER shizeying
## 设置默认语言环境
ENV LANG=C.UTF-8
ENV DATA_WEB=2.1.2
ENV DATAX_VERSION=2.1.2_1
ENV DATAX_NAME=datax-executor
LABEL datax-executor-versiom="${DATAX_VERSION}" \
      datax-executor-versiom="${DATAX_VERSION}" \
			org.label-schema.version="${version}" \
			org.label-schema.vcs-url="https://github.com/WeiYe-Jing/datax-web" \
			org.opencontainers.image.documentation="https://github.com/WeiYe-Jing/datax-web/blob/master/doc/datax-web/datax-web-deploy.md"

ADD ${DATAX_NAME}_${DATAX_VERSION}.tar.gz /opt/
ADD wait-for-it.sh /etc/init.d/

# 安装 GNU libc (aka glibc)和C.UTF-8 locale的依赖 以及设置时区
# 下面这么长一串，主要是通过apk安装glibc的依赖，他的作用主要是本地化支持，和字符集的切换。
RUN sed -i 's|http://dl-cdn.alpinelinux.org|https://mirrors.aliyun.com|g' /etc/apk/repositories && \
    ALPINE_GLIBC_BASE_URL="https://github.com/sgerrand/alpine-pkg-glibc/releases/download" && \
    ALPINE_GLIBC_PACKAGE_VERSION="2.27-r0" && \
    ALPINE_GLIBC_BASE_PACKAGE_FILENAME="glibc-$ALPINE_GLIBC_PACKAGE_VERSION.apk" && \
    ALPINE_GLIBC_BIN_PACKAGE_FILENAME="glibc-bin-$ALPINE_GLIBC_PACKAGE_VERSION.apk" && \
    ALPINE_GLIBC_I18N_PACKAGE_FILENAME="glibc-i18n-$ALPINE_GLIBC_PACKAGE_VERSION.apk" && \
    apk add --no-cache bash && \
    apk add --no-cache --virtual=.build-dependencies wget ca-certificates && \
    echo \
    "-----BEGIN PUBLIC KEY-----\
    MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApZ2u1KJKUu/fW4A25y9m\
    y70AGEa/J3Wi5ibNVGNn1gT1r0VfgeWd0pUybS4UmcHdiNzxJPgoWQhV2SSW1JYu\
    tOqKZF5QSN6X937PTUpNBjUvLtTQ1ve1fp39uf/lEXPpFpOPL88LKnDBgbh7wkCp\
    m2KzLVGChf83MS0ShL6G9EQIAUxLm99VpgRjwqTQ/KfzGtpke1wqws4au0Ab4qPY\
    KXvMLSPLUp7cfulWvhmZSegr5AdhNw5KNizPqCJT8ZrGvgHypXyiFvvAH5YRtSsc\
    Zvo9GI2e2MaZyo9/lvb+LbLEJZKEQckqRj4P26gmASrZEPStwc+yqy1ShHLA0j6m\
    1QIDAQAB\
    -----END PUBLIC KEY-----" | sed 's/   */\n/g' > "/etc/apk/keys/sgerrand.rsa.pub" && \
    wget \
    "$ALPINE_GLIBC_BASE_URL/$ALPINE_GLIBC_PACKAGE_VERSION/$ALPINE_GLIBC_BASE_PACKAGE_FILENAME" \
    "$ALPINE_GLIBC_BASE_URL/$ALPINE_GLIBC_PACKAGE_VERSION/$ALPINE_GLIBC_BIN_PACKAGE_FILENAME" \
    "$ALPINE_GLIBC_BASE_URL/$ALPINE_GLIBC_PACKAGE_VERSION/$ALPINE_GLIBC_I18N_PACKAGE_FILENAME" && \
    apk add --no-cache \
    "$ALPINE_GLIBC_BASE_PACKAGE_FILENAME" \
    "$ALPINE_GLIBC_BIN_PACKAGE_FILENAME" \
    "$ALPINE_GLIBC_I18N_PACKAGE_FILENAME" && \
    \
    rm "/etc/apk/keys/sgerrand.rsa.pub" && \
    /usr/glibc-compat/bin/localedef --force --inputfile POSIX --charmap UTF-8 "$LANG" || true && \
    echo "export LANG=$LANG" > /etc/profile.d/locale.sh && \
    \
    apk del glibc-i18n && \
    rm "/root/.wget-hsts" && \
    apk del .build-dependencies && \
    rm \
    "$ALPINE_GLIBC_BASE_PACKAGE_FILENAME" \
    "$ALPINE_GLIBC_BIN_PACKAGE_FILENAME" \
    "$ALPINE_GLIBC_I18N_PACKAGE_FILENAME" && \
    apk update && apk add --no-cache tzdata && \
    ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone && \
    echo -e '#!/bin/bash\nls --color=auto -lah "$@"' > /usr/bin/ll && \
    chmod +x /usr/bin/ll   /etc/init.d/wait-for-it.sh && \
    apk add  -U   bash  python2   rsync libselinux  openjdk8   && \
#    apk add  -U   bash  python2 supervisor  rsync libselinux  openjdk8 maven  && \
    apk add --no-cache tzdata \
        && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
        && echo "Asia/Shanghai" > /etc/timezone && \
        rm -rf /var/cache/apk/* && \
        rm -rf /tmp/* /var/cache/apk/* /tmp/* /var/tmp/* $HOME/.cache

#        &&rm -rf /var/cache/apk/* /tmp/* /var/tmp/* $HOME/.cache && \
#        sed -i  '/<mirrors>/a\   \
#         <mirror> \n \
#         <id>nexus-aliyun</id> \n  \
#          <mirrorOf>central</mirrorOf> \n \
#        <name>Nexus aliyun</name>  \n \
#         <url>https://maven.aliyun.com/repository/central</url>  \n \
#          <\/mirror>  '    /usr/share/java/maven-3/conf/settings.xml  && \
#          mkdir /opt/datax-web  /usr/local/share/datax-web

ARG SERVER_PORT=8081
ARG DATAX_ADMIN_HOST=127.0.0.1
ARG DATAX_ADMIN_PORT=9527
ARG EXECUTOR_PORT=9999
ARG PYTHON_PATH=/opt/${DATAX_NAME}/python
ARG DATA_PATH=/opt/${DATAX_NAME}/data
ARG SERVICE_LOG_PATH=/opt/${DATAX_NAME}/logs
ARG JSON_PATH=/opt/${DATAX_NAME}/json


ENV SERVER_PORT=${SERVER_PORT} \
    DATAX_ADMIN_HOST=${DATAX_ADMIN_HOST} \
    DATAX_ADMIN_PORT=${DATAX_ADMIN_PORT} \
    EXECUTOR_PORT=${EXECUTOR_PORT} \
    CLASSPATH=/opt/${DATAX_NAME}/lib/*:/opt/${DATAX_NAME}/conf:.  \
    JSON_PATH=${JSON_PATH} \
    PYTHON_PATH=${PYTHON_PATH} \
    DATA_PATH=${DATA_PATH} \
    SERVICE_LOG_PATH=${SERVICE_LOG_PATH}
RUN mkdir    /opt/${DATAX_NAME}/json /opt/${DATAX_NAME}/python /opt/${DATAX_NAME}/data /opt/${DATAX_NAME}/logs


EXPOSE ${SERVER_PORT}
WORKDIR /opt
ENTRYPOINT exec java com.wugui.datax.executor.DataXExecutorApplication


