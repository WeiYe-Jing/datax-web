#!/bin/bash

START_MODULES=("datax-admin"  "datax-executor" )

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

LOG INFO "\033[1m Try To Start Modules In Order \033[0m"
for module in ${START_MODULES[@]}
do
  ${BIN}/start.sh -m ${module}
  if [ $? != 0 ]; then
    LOG ERROR "\033[1m Start Modules [${module}] Failed! \033[0m"
    exit 1
  fi
done