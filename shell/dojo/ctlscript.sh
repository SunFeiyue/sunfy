#!/usr/bin/env bash


PRG="$0"
PRGDIR=`dirname "$PRG"`
[ -z "$APP_HOME" ] && APP_HOME=`cd "$PRGDIR/.." >/dev/null; pwd`

APP_NAME=CallbackRouter
APP_MAIN=com.zywulian.callback.router.Main
JAVA_OPTS="-Duser.timezone=GMT+8 -server -Xms124m -Xmx256m -XX:+PrintGCTimeStamps -XX:+PrintGCDetails -Xloggc:./${APP_NAME}_GC.log -XX:+HeapDumpOnOutOfMemoryError"
PID=0


usage() {
  echo "usage: $0 (start|stop|restart|status)"
}

getPID() {
  javaps=`jps -l | grep $APP_MAIN`
  if [ -n "$javaps" ]; then
    PID=`echo $javaps | awk '{print $1}'`
  else
    PID=0
  fi
}

shutdown() {
  getPID
  if [ $PID -ne 0 ]; then
    echo "Shutdown $APP_NAME: $PID"
    kill -9 $PID
    if [ $? -eq 0 ]; then
      echo "Shutdown succeed"
    else
      echo "Shutdown failed"
    fi
  else
    echo "$APP_NAME not running"
  fi
}

startup() {
  getPID

  if [ $PID -ne 0 ]; then
    echo "already started"
  else
    echo "Starting ..."
    cd $APP_HOME
    nohup java $JAVA_OPTS -classpath "$APP_HOME/classes:$APP_HOME/lib/*" $APP_MAIN >>"./logs/start.log"  2>&1 &

    getPID

    if [ $PID -ne 0 ]; then
      echo "Start succeed: $PID"
    else
      echo "Failed"
    fi
  fi
}

status() {
  getPID

  if [ $PID -eq 0 ]; then
    echo "$APP_NAME is stopped"
  else
    echo "$APP_NAME is running [$PID]"
  fi
}

if [ "$#" -ne 1 ]; then
  usage
  exit -1
fi

if [ "x$1" = "xstart" ]; then
  startup
elif [ "x$1" = "xstop" ]; then
  shutdown
elif [ "x$1" = "xrestart" ]; then
  shutdown
  startup
elif [ "x$1" = "xstatus" ]; then
  status
else
  usage
  exit -1
fi
