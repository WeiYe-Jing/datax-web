#!/bin/sh

DATAX_SCRIPT="${BASH_SOURCE-$0}"
DATAX_SCRIPT=`dirname ${DATAX_SCRIPT}`
DATAX_HOME=`cd ${DATAX_SCRIPT}; cd ..; pwd`
DATAX_WEB_HOME=${DATAX_HOME}/plugin/web/

JAVA_COMMAND="java -Ddatax.home=${DATAX_HOME} -jar ${DATAX_WEB_HOME}/datax-web-0.0.1-SNAPSHOT.jar"

echo $JAVA_COMMAND

$JAVA_COMMAND