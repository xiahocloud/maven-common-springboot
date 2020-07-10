@echo off

set "CURRENT_DIR=%~dp0"
cd %CURRENT_DIR%

echo %CURRENT_DIR%
if not exist "var" (
	mkdir var
)

set pidfile=var/app.pid
set logfile=var/app.log


rem set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_92

set PATH=%JAVA_HOME%\bin

set CLASSPATH=.;classes;${artifactId}.jar;%JAVA_HOME%\lib\dt.jar;%JAVA_HOME%\lib\tools.jar

echo classpath  %CLASSPATH%
java -version

:start
java -classpath "%CLASSPATH%" ${package}.Application > %logfile%
:end