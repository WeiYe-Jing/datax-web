#!/bin/bash
#

FRIEND_NAME=DATAX-EXEXUTOR
MAIN_CLASS=com.wugui.datax.executor.DataXExecutorApplication
if [ ! ${ENV_FILE} ]; then
    ENV_FILE="env.properties"
fi
SLEEP_TIMEREVAL_S=2

abs_path(){
    SOURCE="${BASH_SOURCE[0]}"
    while [ -h "${SOURCE}" ]; do
        DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"
        SOURCE="$(readlink "${SOURCE}")"
        [[ ${SOURCE} != /* ]] && SOURCE="${DIR}/${SOURCE}"
    done
    echo "$( cd -P "$( dirname "${SOURCE}" )" && pwd )"
}

function LOG(){
  currentTime=`date "+%Y-%m-%d %H:%M:%S.%3N"`
  echo -e "$currentTime [${1}] ($$) $2" | tee -a ${SHELL_LOG}
}

verify_java_env(){
  if [ "x${JAVA_HOME}" != "x" ]; then
    ${JAVA_HOME}/bin/java -version >/dev/null 2>&1
  else
    java -version >/dev/null 2>&1
  fi
  if [ $? -ne 0 ]; then
    cat 1>&2 <<EOF
+========================================================================+
| Error: Java Environment is not availiable, Please check your JAVA_HOME |
+------------------------------------------------------------------------+
EOF
  return 1
  fi
  return 0
}

load_env(){
    LOG INFO "load environment variables"
    while read line
    do
        if [[ ! -z $(echo "${line}" | grep "=") ]]; then
                key=${line%%=*}
                value=${line#*=}
                key1=$(echo ${key} | tr '.' '_')
                if [ -z $(echo "${key1}" | grep -P '\s*#+.*') ]; then
                        eval "${key1}=${value}"
                fi
        fi
    done < "${BIN}/${ENV_FILE}"
}

BIN=`abs_path`
SHELL_LOG="${BIN}/console.out"
load_env

#verify environment
verify_java_env
if [ $? -ne 0 ]; then
  exit $?
fi

if [[ ! ${SERVICE_LOG_PATH} ]]; then
    SERVICE_LOG_PATH=${BIN}/../logs
fi

if [[ ! ${SERVICE_CONF_PATH} ]]; then
    SERVICE_CONF_PATH=${BIN}/../conf
fi

if [[ ! ${DATA_PATH} ]]; then
   DATA_PATH=${BIN}/../data
fi

if [[ ! ${JSON_PATH} ]]; then
   JSON_PATH=${BIN}/../json
fi

if [[ ! ${PYTHON_PATH} ]]; then
   PYTHON_PATH=${PYTHON_PATH}
fi

if [[ ! ${SERVER_PORT} ]]; then
   SERVER_PORT=9004
fi

if [[ ! ${EXECUTOR_PORT} ]]; then
   EXECUTOR_PORT=9999
fi

if [[ ! ${DATAX_ADMIN_PORT} ]]; then
   DATAX_ADMIN_PORT=9527
fi




if [[ ! ${JAVA_OPTS} ]]; then
    JAVA_OPTS=" -Xms2g -Xmx2g -XX:+HeapDumpOnOutOfMemoryError -Dfile.encoding=UTF-8"
fi

if [[ ! ${REMOTE_DEBUG_SWITCH} ]]; then
    REMOTE_DEBUG_SWITCH=false
fi

if [[ ! ${REMOTE_DEBUG_PORT} ]]; then
    REMOTE_DEBUG_PORT="8089"
fi

LIB_PATH=${BIN}/../lib
USER_DIR=${BIN}/../
CLASSPATH=${LIB_PATH}"/*:"${SERVICE_CONF_PATH}":."
if [ ${REMOTE_DEBUG_SWITCH} == true ]; then
    JAVA_OPTS=${JAVA_OPTS}" -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=${REMOTE_DEBUG_PORT}"
fi
JAVA_OPTS=${JAVA_OPTS}" -XX:HeapDumpPath="${SERVICE_LOG_PATH}" -Dlog.path="${SERVICE_LOG_PATH}
JAVA_OPTS=${JAVA_OPTS}" -Duser.dir="${USER_DIR}
JAVA_OPTS=${JAVA_OPTS}" -Dserver.port="${SERVER_PORT}" -Ddata.path="${DATA_PATH}" -Dexecutor.port="${EXECUTOR_PORT}" -Djson.path="${JSON_PATH}" -Dpython.path="${PYTHON_PATH}" -Ddatax.admin.port="${DATAX_ADMIN_PORT}
if [ "x"${PID_FILE_PATH} != "x" ]; then
  JAVA_OPTS=${JAVA_OPTS}" -Dpid.file="${PID_FILE_PATH}
fi
JAVA_OPTS=${JAVA_OPTS}" -Dlogging.config="${SERVICE_CONF_PATH}"/logback.xml"
JAVA_OPTS=${JAVA_OPTS}" -classpath "${CLASSPATH}

if [ "x${JAVA_HOME}" != "x" ]; then
  EXE_JAVA=${JAVA_HOME}"/bin/java "${JAVA_OPTS}" "${MAIN_CLASS}
  JPS=${JAVA_HOME}/bin/jps
else
  EXE_JAVA="java "${JAVA_OPTS}" "${MAIN_CLASS}
  JPS="jps"
fi

usage(){
    echo " usage is [start|stop|shutdown|restart]"
}

# check if the process still in jvm
status_class(){
    local p=""
    if [ "x"${PID_FILE_PATH} != "x" ]; then
      if [ -f ${PID_FILE_PATH} ]; then
        local pid_in_file=`cat ${PID_FILE_PATH} 2>/dev/null`
        if [ "x"${pid_in_file} !=  "x" ]; then
          p=`${JPS} -q | grep ${pid_in_file} | awk '{print $1}'`
        fi
      fi
    else
      p=`${JPS} -l | grep "$2" | awk '{print $1}'`
    fi
    if [ -n "$p" ]; then
        # echo "$1 ($2) is still running with pid $p"
        return 0
    else
        # echo "$1 ($2) does not appear in the java process table"
        return 1
    fi
}

wait_for_startup(){
    local now_s=`date '+%s'`
    local stop_s=$((${now_s} + $1))
    while [ ${now_s} -le ${stop_s} ];do
        status_class ${FRIEND_NAME} ${MAIN_CLASS}
        if [ $? -eq 0 ]; then
            return 0
        fi
        sleep ${SLEEP_TIMEREVAL_S}
        now_s=`date '+%s'`
    done
    exit 1
}

wait_for_stop(){
    local now_s=`date '+%s'`
    local stop_s=$((${now_s} + $1))
    while [ ${now_s} -le ${stop_s} ];do
        status_class ${FRIEND_NAME} ${MAIN_CLASS}
        if [ $? -eq 1 ]; then
            return 0
        fi
        sleep ${SLEEP_TIMEREVAL_S}
        now_s=`date '+%s'`
    done
    return 1
}

start_m(){
    status_class ${FRIEND_NAME} ${MAIN_CLASS}
    if [ $? -eq 0 ]; then
        LOG INFO "${FRIEND_NAME} has been started in process"
        exit 0
    fi
    LOG INFO ${EXE_JAVA}
    nohup ${EXE_JAVA} >${SHELL_LOG} 2>&1 &
    LOG INFO "Waiting ${FRIEND_NAME} to start complete ..."
    wait_for_startup 20
    if [ $? -eq 0 ]; then
        LOG INFO "${FRIEND_NAME} start success"
        return 0
    else
        LOG ERROR "${FRIEND_NAME} start exceeded over 20s" >&2
        return 1
    fi
}

stop_m(){
    local p=""
    if [ "x"${PID_FILE_PATH} != "x" ]; then
      if [ -f ${PID_FILE_PATH} ]; then
        local pid_in_file=`cat ${PID_FILE_PATH} 2>/dev/null`
        if [ "x"${pid_in_file} !=  "x" ]; then
          p=`${JPS} -q | grep ${pid_in_file} | awk '{print $1}'`
        fi
      fi
    else
      p=`${JPS} -l | grep "${MAIN_CLASS}" | awk '{print $1}'`
    fi
    if [ -z "${p}" ]; then
        LOG INFO "${FRIEND_NAME} didn't start successfully, not found in the java process table"
        return 0
    fi
    LOG INFO "Killing ${FRIEND_NAME} (pid ${p}) ..."
    case "`uname`" in
        CYCGWIN*) taskkill /PID "${p}" ;;
        *) kill -SIGTERM "${p}" ;;
    esac
    LOG INFO "Waiting ${FRIEND_NAME} to stop complete ..."
    wait_for_stop 20
    if [ $? -eq 0 ]; then
        LOG INFO "${FRIEND_NAME} stop success"
        return 0
    else
        LOG ERROR "${FRIEND_NAME} stop exceeded over 20s" >&2
        return 1
    fi
}

shutdown_m(){
    local p=""
    if [ "x"${PID_FILE_PATH} != "x" ]; then
      if [ -f ${PID_FILE_PATH} ]; then
        local pid_in_file=`cat ${PID_FILE_PATH} 2>/dev/null`
        if [ "x"${pid_in_file} !=  "x" ]; then
          p=`${JPS} -q | grep ${pid_in_file} | awk '{print $1}'`
        fi
      fi
    else
      p=`${JPS} -l | grep "${MAIN_CLASS}" | awk '{print $1}'`
    fi
    if [ -z "${p}" ]; then
         LOG INFO "${FRIEND_NAME} didn't start successfully, not found in the java process table"
        return 0
    fi
    LOG INFO "Killing ${FRIEND_NAME} (pid ${p}) ..."
    case "`uname`" in
        CYCGWIN*) taskkill /F /PID "${p}" ;;
        *) kill -9 "${p}" ;;
    esac
}

restart_m(){
    stop_m
    if [ $? -eq 0 ]; then
        start_m
        exit $?
    else
        LOG ERROR "${FRIEND_NAME} restart fail" >&2
        exit 1
    fi
}
if [ ! $1 ]; then
    usage
    exit 1;
fi
case $1 in
    start) start_m;;
    stop) stop_m;;
    shutdown) shutdown_m;;
    restart) restart_m;;
    *)
       usage
       exit 1
     ;;
esac
exit $?