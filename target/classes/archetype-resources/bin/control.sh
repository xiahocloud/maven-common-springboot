#!/bin/bash
#set JAVA_HOME
#JAVA_HOME=/home/chj/tomcat/java/jdk1.8.0_73

workspace=$(cd $(dirname $0)/;pwd)
cd $workspace

mkdir -p var

pidfile=var/app.pid
logfile=var/app.log

function check(){
	if [ -f $pidfile ];then
		pid=`cat $pidfile`
		if [ -n $pid ];then
			running=`ps -p $pid|grep -v "PID TTY" |wc -l`
			return $running
		fi
	fi
	return 0
}

function checkJava(){
    cygwin=false
    darwin=false
    case "`uname`" in
    CYGWIN*) cygwin=true;;
    Darwin*) darwin=true;;
    OS400*) os400=true;;
    esac


    if [ -z $JAVA_HOME -a -n $JAVA_HOME ]; then
      if $darwin; then
        if [ -x '/usr/libexec/java_home' ] ; then
          export JAVA_HOME=`/usr/libexec/java_home`
        # Bugzilla 37284 (reviewed).
        elif [ -d "/System/Library/Frameworks/JavaVM.framework/Versions/CurrentJDK/Home" ]; then
          export JAVA_HOME="/System/Library/Frameworks/JavaVM.framework/Versions/CurrentJDK/Home"
        fi
      else
        JAVA_PATH=`which java 2>/dev/null`
        if [ "$JAVA_PATH" != "" ]; then
          JAVA_PATH=`dirname $JAVA_PATH 2>/dev/null`
          JAVA_HOME=`dirname $JAVA_PATH 2>/dev/null`
        fi
      fi
      if [ -z $JAVA_HOME -a -n $JAVA_HOME ]; then
        echo "Neither the JAVA_HOME environment variable is defined"
        echo "At least one of these environment variable is needed to run this program"
        exit 1
      fi
    fi

    echo JAVA_HOME $JAVA_HOME

    if [ ! -x "$JAVA_HOME"/bin/java ]; then
        echo "The JAVA_HOME environment variable is not defined correctly"
        echo "This environment variable is needed to run this program"
        echo "NB: JAVA_HOME should point to a JDK not a JRE"
        exit 1
    fi


    $JAVA_HOME/bin/java -version
}

function start(){
	check
	checkJava
	running=$?
	if [ $running -gt 0 ];then
		echo -n "running, pid="
		cat $pidfile
		return 0
	fi
	class_path=.:classes:${artifactId}.jar
	
  	echo classpath  $class_path
	nohup $JAVA_HOME/bin/java -classpath $class_path ${package}.Application &> $logfile &
	sleep 1
	running=`ps -p $!|grep -v "PID TTY"|wc -l`
	if [ $running -gt 0 ];then
		echo $!>$pidfile
		echo "started, pid=$!"
	else
		echo "start failed"
	fi
}

function stop(){
	pid=`cat $pidfile`
	kill $pid
	rm -f $pidfile
	echo "stopped"
}

function restart(){
	stop
	sleep 1
	start
}

function status(){
	check
	running=$?
	if [ $running -gt 0 ];then
		echo "started"
	else
		echo "stopped"
	fi
}

function tailf(){
    tail -f $logfile
}

function help(){
	echo "start|stop|restart|status"
}
if [ "$1" == "start" ];then
    start
elif [ "$1" == "stop" ];then
	stop
elif [ "$1" == "restart" ];then
	restart
elif [ "$1" == "status" ];then
	status
elif [ "$1" == "tail" ];then
	tailf
else
	help
fi
