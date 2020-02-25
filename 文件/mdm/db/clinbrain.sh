#!/bin/sh
PORTS=(8761 8888 3000 9999 4008 8077 4009)
MODULES=(clinbrain-eureka clinbrain-config clinbrain-auth clinbrain-gateway clinbrain-upms clinbrain-dataserver clinbrain-metadata)
MODULE_NAMES=("Eureka Register Center" "Config Center" "Oauth Center" "Gateway" "User Center" "Clinbrain Data Server" "Data Resource Server")
JARS=(clinbrain-eureka.jar clinbrain-config.jar clinbrain-auth.jar clinbrain-gateway.jar clinbrain-upms-biz.jar clinbrain-dataserver.jar clinbrain-metadata.jar)
JAR_PATH='/opt/clb-web/metadatajars'
LOG_PATH='/opt/clb-web/metadatajars/logs'
start() {
  local MODULE=
  local MODULE_NAME=
  local JAR_NAME=
  local command="$1"
  local commandOk=0
  local count=0
  local okCount=0
  local port=0
  for((i=0;i<${#MODULES[@]};i++))
  do
    MODULE=${MODULES[$i]}
    MODULE_NAME=${MODULE_NAMES[$i]}
    JAR_NAME=${JARS[$i]}
    PORT=${PORTS[$i]}
    if [ "$command" == "all" ] || [ "$command" == "$MODULE" ];then
      commandOk=1
      count=0
      PID=`ps -ef |grep $(echo $JAR_NAME | awk -F/ '{print $NF}') | grep -v grep | awk '{print $2}'`
      if [ -n "$PID" ];then
        echo "$MODULE---$MODULE_NAME:was running,PID=$PID"
      else
        exec nohup java -jar $JAR_PATH/$JAR_NAME >> $LOG_PATH/nohup.out &
        PID=`netstat -tunlp | grep $PORT | awk '{print $7}' | cut -d/ -f 1`
        while [ -z "$PID" ]
        do
          if (($count == 30));then
            echo "$MODULE---$MODULE_NAME:$(expr $count \* 10)s is not start,please check your program!"
            break
          fi
          count=$(($count+1))
          echo "$MODULE_NAME启动中.................."
          sleep 10s
          PID=`netstat -tunlp | grep $PORT | awk '{print $7}' | cut -d/ -f 1`
        done
        okCount=$(($okCount+1))
        echo "$MODULE---$MODULE_NAME:has start success,PID=$PID"
      fi
    fi
  done
  if(($commandOk == 0));then
    echo "the second param enter eg:linbrain-eureka|clinbrain-config|clinbrain-auth|clinbrain-gateway|clinbrain-upms|clinbrain-dataserver|clinbrain-metadata"
  else
    echo "............there has:$okCount server started..........."
  fi
}

stop() {
  local MODULE=
  local MODULE_NAME=
  local JAR_NAME=
  local command="$1"
  local commandOk=0
  local okCount=0
  for((i=0;i<${#MODULES[@]};i++))
  do
    MODULE=${MODULES[$i]}
    MODULE_NAME=${MODULE_NAMES[$i]}
    JAR_NAME=${JARS[$i]}
    if [ "$command" = "all" ] || [ "$command" = "$MODULE" ];then
      commandOk=1
      PID=`ps -ef |grep $(echo $JAR_NAME | awk -F/ '{print $NF}') | grep -v grep | awk '{print $2}'`
      if [ -n "$PID" ];then
        echo "$MODULE---$MODULE_NAME:prestart end,PID=$PID"
        kill -9 $PID
        PID=`ps -ef |grep $(echo $JAR_NAME | awk -F/ '{print $NF}') | grep -v grep | awk '{print $2}'`
        while [ -n "$PID" ]
        do
          sleep 3s
          PID=`ps -ef |grep $(echo $JAR_NAME | awk -F/ '{print $NF}') | grep -v grep | awk '{print $2}'`
        done
        echo "$MODULE---$MODULE_NAME:success end"
        okCount=$(($okCount+1))
      else
        echo "$MODULE---$MODULE_NAME:didn't run"
      fi
    fi
  done
  if (($commandOk == 0));then
    echo "the second param enter eg:clinbrain-eureka|clinbrain-config|clinbrain-auth|clinbrain-gateway|clinbrain-upms|clinbrain-dataserver|clinbrain-metadata"
  else
    echo "............there has:$okCount server started..........."
  fi
}


case "$1" in
  start)
    start "$2"
  ;;
  stop)
    stop "$2"
  ;;
  restart)
    stop "$2"
    sleep 3s
    start "$2"
  ;;
  *)
    echo "the first param enter eg:start|stop|restart"
    exit 1
  ;;
esac
