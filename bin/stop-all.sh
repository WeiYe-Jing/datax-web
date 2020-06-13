#!/bin/bash


STOP_MODULES=("datax-admin"  "datax-executor" )


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
SHELL_LOG="${BIN}/console.out"

LOG INFO "\033[1m Try to Stop Modules In Order \033[0m"
for module in ${STOP_MODULES[@]}
do
  ${BIN}/stop.sh -m ${module}
  if [ $? != 0 ]; then
    LOG ERROR "\033[1m Stop Modules [${module}] Failed! \033[0m"
    exit 1
  fi
done