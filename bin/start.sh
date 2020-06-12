#!/bin/bash


MODULE_NAME=""
MODULE_DEFAULT_PREFIX="datax-"

usage(){
  echo "Usage is [-m module will be started]"
}

function LOG(){
  currentTime=`date "+%Y-%m-%d %H:%M:%S.%3N"`
  echo -e "$currentTime [${1}] ($$) $2" | tee -a ${SHELL_LOG}
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

BIN=`abs_path`
MODULE_DIR=${BIN}/../modules
SHELL_LOG="${BIN}/console.out"

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

start_single_module(){
  LOG INFO "\033[1m ####### Begin To Start Module: [$1] ######\033[0m"
  if [ -f "${MODULE_DIR}/$1/bin/$1.sh" ]; then
    ${MODULE_DIR}/$1/bin/$1.sh start
  elif [[ $1 != ${MODULE_DEFAULT_PREFIX}* ]] && [ -f "${MODULE_DIR}/${MODULE_DEFAULT_PREFIX}$1/bin/${MODULE_DEFAULT_PREFIX}$1.sh" ]; then
    interact_echo "Do you mean [${MODULE_DEFAULT_PREFIX}$1] ?"
    if [ $? == 0 ]; then
      ${MODULE_DIR}/${MODULE_DEFAULT_PREFIX}$1/bin/${MODULE_DEFAULT_PREFIX}$1.sh start
    fi
  else
    LOG ERROR "Cannot find the startup script for module: [$1], please check your installation"
    exit 1
  fi
}

while [ 1 ]; do
  case ${!OPTIND} in
  -m|--modules)
    if [ -z $2 ]; then
      LOG ERROR "No module provided"
      exit 1
    fi
    MODULE_NAME=$2
    shift 2
  ;;
  "")
    break
  ;;
  *)
    usage
    exit 1
  ;;
  esac
done

if [ "x${MODULE_NAME}" == "x" ]; then
  usage
  exit 1
fi

start_single_module ${MODULE_NAME}
exit $?
