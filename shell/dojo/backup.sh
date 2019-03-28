#!/bin/bash

IGNORE="--exclude=var --exclude=logs --exclude=.vertx --exclude=jdk8*"
APP_HOME=~/xjxcloud
BAK_HOME=~/backup
APP_NAME=$( basename $APP_HOME )

backup() {
  suffix=`date "+%Y%m%d%H%M%S"`
  rsync -av --delete $IGNORE  $APP_HOME/ $BAK_HOME/${APP_NAME}_${suffix}
}

restore() {
  src=$BAK_HOME/$1
  if [ ! -d $src ]; then
    echo "Backup [$src] not exist"
    exit -3
  fi
  echo "restore $src started..."
  rsync -av --delete $IGNORE $src/ $APP_HOME
  echo "restore $src completed"
}

if [ $# -eq 0 ]; then
  backup
elif [ "x$1" = "xrestore-last" ]; then
  # sort by file name, and use the last one as backup source
  bak_src=`cd $BAK_HOME;ls -r | grep ${APP_NAME}_ | head -n 1`
  if [ "$bak_src" = "" ]; then
    echo "no backup file found"
    exit -2
  fi
  restore $bak_src
elif [ "x$1" = "xrestore" ]; then
  if [ "$2" = "" ]; then
    echo 'please specify the source used to restore.'
    exit -1
  fi
  restore $2
else
  echo "Invalid command"
  echo -e "[Usage]:\n  1. 备份：./backup.sh \n    备份到\"~/backup/xjxcloud_yyyyMMddHHmmss目录下\""
  echo "  2. 恢复最近的一次备份：./backup.sh restore-last"
  echo "  3. 恢复指定的备份：./backup.sh restore xjxcloud_yyyyMMddHHmmss"
  echo "  * yyyyMMddHHmmss代表时间戳"
  exit -1
fi
