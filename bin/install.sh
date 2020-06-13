#!/bin/bash


export BASE_LOG_DIR=""
export BASE_CONF_DIR=""
export BASE_DATA_DIR=""
DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
SHELL_LOG="${DIR}/console.out"

export SQL_SOURCE_PATH="${DIR}/db/datax_web.sql"

MODULES_DIR="${DIR}/../modules"
PACKAGE_DIR="${DIR}/../packages"

MODULE_LIST=()
CONF_FILE_PATH="bin/configure.sh"
FORCE_INSTALL=false
SKIP_PACKAGE=false
SAFE_MODE=true
UNSAFE_COMMAND=""
USER=`whoami`
SUDO_USER=false

usage(){
   printf "\033[1m Install usage:\n\033[0m"
   printf "\t%-15s  %-15s  %-2s \n" "-m|--modules" "modules to install" "Define the modules to install"
   printf "\t%-15s  %-15s  %-2s \n" "-f|--force" "force install" "Force program to install modules"
   printf "\t%-15s  %-15s  %-2s \n" "--skip-pack" "do not decompress" "Skip the phrase of decompressing packages"
   printf "\t%-15s  %-15s  %-2s \n" "--unsafe" "unsafe mode" "Will clean the module directory existed"
   printf "\t%-15s  %-15s  %-2s \n" "--safe" "safe mode" "Will not modify the module directory existed (Default)"
   printf "\t%-15s  %-15s  %-2s \n" "-h|--help" "usage" "View command list"
}

function LOG(){
  currentTime=`date "+%Y-%m-%d %H:%M:%S.%3N"`
  echo -e "$currentTime [${1}] ($$) $2" | tee -a ${SHELL_LOG}
}

is_sudo_user(){
  sudo -v >/dev/null 2>&1
}

clean_modules(){
  if [ ${#MODULE_LIST[@]} -gt 0 ]; then
    for server in ${MODULE_LIST[@]}
    do
      rm -rf ${MODULES_DIR}/${server}
    done
  else
    rm -rf ${MODULES_DIR}/*
  fi
}

uncompress_packages(){
  local list=`ls ${PACKAGE_DIR}`
  for pack in ${list}
  do
    local uncompress=true
    if [ ${#PACKAGE_NAMES[@]} -gt 0 ]; then
      uncompress=false
      for server in ${PACKAGE_NAMES[@]}
      do
        if [ ${server} == ${pack%%.tar.gz*} ] || [ ${server} == ${pack%%.zip*} ]; then
          uncompress=true
          break
        fi
      done
    fi
    if [ ${uncompress} == true ]; then
      if [[ ${pack} =~ tar\.gz$ ]]; then
        local do_uncompress=0
        if [ ${FORCE_INSTALL} == false ]; then
          interact_echo "Do you want to decompress this package: [${pack}]?"
          do_uncompress=$?
        fi
        if [ ${do_uncompress} == 0 ]; then
          LOG INFO "\033[1m Uncompress package: [${pack}] to modules directory\033[0m"
          tar --skip-old-files -zxf ${PACKAGE_DIR}/${pack} -C ${MODULES_DIR}
        fi
      elif [[ ${pack} =~ zip$ ]]; then
        local do_uncompress=0
        if [ ${FORCE_INSTALL} == false ]; then
          interact_echo "Do you want to decompress this package: [${pack}]?"
          do_uncompress=$?
        fi
        if [ ${do_uncompress} == 0 ]; then
          LOG INFO "\033[1m Uncompress package: [${pack}] to modules directory\033[0m"
          unzip -nq ${PACKAGE_DIR}/${pack} -d ${MODULES_DIR}
        fi
      fi
      # skip other packages
    fi
  done
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

install_modules(){
  LOG INFO "\033[1m ####### Start To Install Modules ######\033[0m"
  LOG INFO "Module servers could be installed:"
  for server in ${MODULE_LIST[@]}
  do
    printf "\\033[1m [${server}] \033[0m"
  done
  echo ""
  for server in ${MODULE_LIST[@]}
  do
    if [ ${FORCE_INSTALL} == false ]; then
      interact_echo "Do you want to confiugre and install [${server}]?"
      if [ $? == 0 ]; then
        LOG INFO "\033[1m Install module server: [${server}]\033[0m"
        # Call configure.sh
        ${MODULES_DIR}/${server}/${CONF_FILE_PATH} ${UNSAFE_COMMAND}
      fi
    else
      LOG INFO "\033[1m Install module server: [${server}]\033[0m"
      # Call configure.sh
      ${MODULES_DIR}/${server}/${CONF_FILE_PATH} ${UNSAFE_COMMAND}
    fi
  done
  LOG INFO "\033[1m ####### Finish To Install Modules ######\033[0m"
}

scan_to_install_modules(){
  echo "Scan modules directory: [$1] to find server under dataxweb"
  let c=0
  ls_out=`ls $1`
  for dir in ${ls_out}
  do
    if test -e "$1/${dir}/${CONF_FILE_PATH}"; then
      MODULE_LIST[$c]=${dir}
      ((c++))
    fi
  done
  install_modules
}

while [ 1 ]; do
  case ${!OPTIND} in
  -h|--help)
    usage
    exit 0
  ;;
  -m|--modules)
    i=1
    if [ -z $2 ]; then
      echo "Empty modules"
      exit 1
    fi
    while [ 1 ]; do
     split=`echo $2|cut -d "," -f${i}`
      if [ "$split" != "" ];
      then
        c=$(($i - 1))
        MODULE_LIST[$c]=${split}
        i=$(($i + 1))
      else
        break
      fi
      if [ "`echo $2 |grep ","`" == "" ]; then
        break
      fi
    done
    shift 2
  ;;
  -f|--force)
    FORCE_INSTALL=true
    shift 1
  ;;
  --skip-pack)
    SKIP_PACKAGE=true
    shift 1
  ;;
  --safe)
    SAFE_MODE=true
    UNSAFE_COMMAND=""
    shift 1
  ;;
  --unsafe)
    SAFE_MODE=false
    UNSAFE_COMMAND="--unsafe"
    shift 1
  ;;
  "")
    break
  ;;
  *)
    echo "Argument error! " 1>&2
    exit 1
  ;;
  esac
done

is_sudo_user
if [ $? == 0 ]; then
  SUDO_USER=true
fi
MODULE_LIST_RESOLVED=()
if [ ${#MODULE_LIST[@]} -gt 0 ]; then
  c=0
  RESOLVED_DIR=${PACKAGE_DIR}
  if [ ${SKIP_PACKAGE} == true ]; then
    RESOLVED_DIR=${MODULES_DIR}
  fi
  for server in ${MODULE_LIST[@]}
  do
    server_list=`ls ${RESOLVED_DIR} | grep -E "^(${server}|${server}_[0-9]+\\.[0-9]+\\.[0-9]+\\.RELEASE_[0-9]+)(\\.tar\\.gz|\\.zip|)$"`
    for _server in ${server_list}
    do
      # More better method to cut string?
      _server=${_server%%.tar.gz*}
      _server=${_server%%zip*}
      MODULE_LIST_RESOLVED[$c]=${_server}
      c=$(($c + 1))
    done
  done
  if [ ${SKIP_PACKAGE} == true ]; then
    MODULE_LIST=${MODULE_LIST_RESOLVED}
  else
    PACKAGE_NAMES=${MODULE_LIST_RESOLVED}
  fi
fi

if [ ! -d ${MODULES_DIR} ]; then
  LOG INFO  "Creating directory: ["${MODULES_DIR}"]."
  mkdir -p ${MODULES_DIR}
fi

if [ ${SAFE_MODE} == false ]; then
  LOG INFO  "\033[1m ####### Start To Clean Modules Directory ######\033[0m"
  LOG INFO  "Cleanning...."
  if [ ${MODULES_DIR} == "" ] || [ ${MODULES_DIR} == "/" ]; then
    LOG INFO  "Illegal modules directory: ${MODULES_DIR}" 1>&2
    exit 1
  fi
  clean_modules
  LOG INFO "\033[1m ####### Finish To Clean Modules Directory ######\033[0m"
fi

if [ ${SKIP_PACKAGE} == false ]; then
  LOG INFO  "\033[1m ####### Start To Uncompress Packages ######\033[0m"
  LOG INFO  "Uncompressing...."
  uncompress_packages
  LOG INFO  "\033[1m ####### Finish To Umcompress Packages ######\033[0m"
fi

if [ ${#MODULE_LIST[@]} -gt 0 ]; then
  for server in ${MODULE_LIST}
  do
    if [ ! -f ${MODULES_DIR}/${server}/${CONF_FILE_PATH} ]; then
      LOG INFO  "Module [${server}] defined doesn't have configure.sh shell" 1>&2
      exit 1
    fi
  done
  install_modules
else
  # Scan modules directory
  scan_to_install_modules ${MODULES_DIR}
fi

exit 0

