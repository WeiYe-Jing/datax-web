#!/bin/bash


DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
SHELL_LOG="${DIR}/console.out"
SERVER_NAME="datax-executor"
USER=`whoami`
SAFE_MODE=true
SUDO_USER=false
ENV_FILE_PATH="${DIR}/env.properties"

usage(){
  printf "Configure usage:\n"
  printf "\t%-10s  %-10s  %-2s \n" --server "server-name" "Name of datax-executor server"
  printf "\t%-10s  %-10s  %-2s \n" --unsafe "unsafe mode" "Will clean the directory existed"
  printf "\t%-10s  %-10s  %-2s \n" --safe "safe mode" "Will not modify the directory existed (Default)"
  printf "\t%-10s  %-10s  %-2s \n" "-h|--help" "usage" "List help document"
}

function LOG(){
  currentTime=`date "+%Y-%m-%d %H:%M:%S.%3N"`
  echo -e "$currentTime [${1}] ($$) $2" | tee -a ${SHELL_LOG}
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

JSON_PATH=${BIN}/../json
if [ "x${BASE_DATA_DIR}" != "x" ]; then
  JSON_PATH=${BASE_DATA_DIR}/${SERVER_NAME_SIMPLE}
  sed -ri "s![#]?(JSON_PATH=)\S*!\1${JSON_PATH}!g" ${ENV_FILE_PATH}
fi

# Start to make directory
LOG INFO "\033[1m Start to build directory\033[0m"
mkdir_p ${LOG_PATH}
mkdir_p ${CONF_PATH}
mkdir_p ${DATA_PATH}
mkdir_p ${JSON_PATH}
if [ "x${BASE_CONF_DIR}" != "x" ]; then
  LOG INFO "\033[1m Start to copy configuration file/directory\033[0m"
  # Copy the configuration file
  copy_replace application.yml
  copy_replace logback.xml
fi
