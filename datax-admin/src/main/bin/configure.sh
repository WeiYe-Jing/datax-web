#!/bin/bash

DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
SHELL_LOG="${DIR}/console.out"
SERVER_NAME="datax-admin"
USER=`whoami`
SAFE_MODE=true
SUDO_USER=false
ENV_FILE_PATH="${DIR}/env.properties"

usage(){
  printf "Configure usage:\n"
  printf "\t%-10s  %-10s  %-2s \n" --server "server-name" "Name of datax-admin server"
  printf "\t%-10s  %-10s  %-2s \n" --unsafe "unsafe mode" "Will clean the directory existed"
  printf "\t%-10s  %-10s  %-2s \n" --safe "safe mode" "Will not modify the directory existed (Default)"
  printf "\t%-10s  %-10s  %-2s \n" "-h|--help" "usage" "List help document"
}

LOG(){
  currentTime=`date "+%Y-%m-%d %H:%M:%S.%3N"`
  echo -e "$currentTime [${1}] ($$) $2" | tee -a ${SHELL_LOG}
}

interact_echo(){
  while [ 1 ]; do
    read -p "$1 (Y/N)" yn
    if [ "${yn}x" == "Yx" ] || [ "${yn}x" == "yx" ]; then
      return 0
    elif [ "${yn}x" == "Nx" ] || [ "${yn}x" == "nx" ]; then
      return 1
    else
      echo "Unknown choise: [$yn], please choose again."
    fi
  done
}

is_sudo_user(){
  sudo -v >/dev/null 2>&1
}

abs_path(){
    SOURCE="${BASH_SOURCE[0]}"
    while [ -h "${SOURCE}" ]; do
        DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"
        SOURCE="$(readlink "${SOURCE}")"
        [[ ${SOURCE} != /* ]] && SOURCE="${DIR}/${SOURCE}"
    done
    echo "$( cd -P "$( dirname "${SOURCE}" )" && pwd )"
}

check_exist(){
    if test -e "$1"; then
        LOG INFO "Directory or file: [$1] has been exist"
        if [ $2 == true ]; then
           LOG INFO "Configure program will shutdown..."
           exit 0
        fi
    fi
}

copy_replace(){
    file_name=$1
     if test -e "${CONF_PATH}/${file_name}";then
        if [ ${SAFE_MODE} == true ]; then
            check_exist "${CONF_PATH}/${file_name}" true
        fi
        LOG INFO "Delete file or directory: [${CONF_PATH}/${file_name}]"
        rm -rf ${CONF_PATH}/${file_name}
    fi
    if test -e "${DIR}/../conf/${file_name}";then
        LOG INFO "Copy from ${DIR}/../conf/${file_name}"
        cp -R ${DIR}/../conf/${file_name} ${CONF_PATH}/
    fi
}

mkdir_p(){
    if [ ${SAFE_MODE} == true ]; then
      check_exist $1 false
    fi
    if [ ! -d $1 ]; then
        LOG INFO "Creating directory: ["$1"]."
        #mkdir -p $1
        if [ ${SUDO_USER} == true ]; then
          sudo mkdir -p $1 && sudo chown -R ${USER} $1
        else
          mkdir -p $1
        fi
    fi
}

while [ 1 ]; do
  case ${!OPTIND} in
  --server)
    SERVER_NAME=$2
    shift 2
  ;;
  --unsafe)
    SAFE_MODE=false
    shift 1
  ;;
  --safe)
    SAFE_MODE=true
    shift 1
  ;;
  --help|-h)
    usage
    exit 0
  ;;
  *)
    break
  ;;
  esac
done

is_sudo_user
if [ $? == 0 ]; then
  SUDO_USER=true
fi

BIN=`abs_path`
SERVER_NAME_SIMPLE=${SERVER_NAME/datax-/}

LOG_PATH=${BIN}/../logs
if [ "x${BASE_LOG_DIR}" != "x" ]; then
    LOG_PATH=${BASE_LOG_DIR}/${SERVER_NAME_SIMPLE}
    sed -ri "s![#]?(WEB_LOG_PATH=)\S*!\1${LOG_PATH}!g" ${ENV_FILE_PATH}
fi

CONF_PATH=${BIN}/../conf
if [ "x${BASE_CONF_DIR}" != "x" ]; then
  CONF_PATH=${BASE_CONF_DIR}/${SERVER_NAME_SIMPLE}
  sed -ri "s![#]?(WEB_CONF_PATH=)\S*!\1${CONF_PATH}!g" ${ENV_FILE_PATH}
fi

DATA_PATH=${BIN}/../data
if [ "x${BASE_DATA_DIR}" != "x" ]; then
  DATA_PATH=${BASE_DATA_DIR}/${SERVER_NAME_SIMPLE}
  sed -ri "s![#]?(DATA_PATH=)\S*!\1${DATA_PATH}!g" ${ENV_FILE_PATH}
fi

echo "Start to make directory"
# Start to make directory
LOG INFO "\033[1m Start to build directory\033[0m"
mkdir_p ${LOG_PATH}
mkdir_p ${CONF_PATH}
mkdir_p ${DATA_PATH}
if [ "x${BASE_CONF_DIR}" != "x" ]; then
  LOG INFO "\033[1m Start to copy configuration file/directory\033[0m"
  # Copy the configuration file
  copy_replace bootstrap.properties
  copy_replace application.yml
  copy_replace logback.xml
  copy_replace i18n
  copy_replace mybatis-mapper
  copy_replace static
fi
echo "end to make directory"


BOOTSTRAP_PROP_FILE="${CONF_PATH}/bootstrap.properties"
# Start to initalize database
echo "Start to initalize database"
if [ "x${SQL_SOURCE_PATH}" != "x" ] && [ -f "${SQL_SOURCE_PATH}" ]; then
   `mysql --version >/dev/null 2>&1`
   if [ $? == 0 ]; then
      LOG INFO "\033[1m Scan out mysql command, so begin to initalize the database\033[0m"
      interact_echo "Do you want to initalize database with sql: [${SQL_SOURCE_PATH}]?"
      if [ $? == 0 ]; then
        read -p "Please input the db host(default: 127.0.0.1): " HOST
        if [ "x${HOST}" == "x" ]; then
          HOST="127.0.0.1"
        fi
        while [ 1 ]; do
          read -p "Please input the db port(default: 3306): " PORT
          if [ "x${PORT}" == "x" ]; then
            PORT=3306
            break
          elif [ ${PORT} -gt 0 ] 2>/dev/null; then
            break
          else
            echo "${PORT} is not a number, please input again"
          fi
        done
        read -p "Please input the db username(default: root): " USERNAME
        if [ "x${USERNAME}" == "x" ]; then
          USERNAME="root"
        fi
        read -p "Please input the db password(default: ""): " PASSWORD
        read -p "Please input the db name(default: dataxweb)" DATABASE
        if [ "x${DATABASE}" == "x" ]; then
          DATABASE="dataxweb"
        fi
        mysql -h ${HOST} -P ${PORT} -u ${USERNAME} -p${PASSWORD}  --default-character-set=utf8 -e \
        "CREATE DATABASE IF NOT EXISTS ${DATABASE}; USE ${DATABASE}; source ${SQL_SOURCE_PATH};"
        sed -ri "s![#]?(DB_HOST=)\S*!\1${HOST}!g" ${BOOTSTRAP_PROP_FILE}
        sed -ri "s![#]?(DB_PORT=)\S*!\1${PORT}!g" ${BOOTSTRAP_PROP_FILE}
        sed -ri "s![#]?(DB_USERNAME=)\S*!\1${USERNAME}!g" ${BOOTSTRAP_PROP_FILE}
        sed -ri "s![#]?(DB_PASSWORD=)\S*!\1${PASSWORD}!g" ${BOOTSTRAP_PROP_FILE}
        sed -ri "s![#]?(DB_DATABASE=)\S*!\1${DATABASE}!g" ${BOOTSTRAP_PROP_FILE}
      fi
   fi
fi