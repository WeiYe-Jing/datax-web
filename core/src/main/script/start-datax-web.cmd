@echo off
rem 设置编码为utf8
chcp 65001
rem 获取父级目录
for %%d in (%~dp0..) do set ParentDirectory=%%~fd
echo ParentDirectory=%ParentDirectory%

set DATAX_WEB_HOME=%ParentDirectory%\plugin\web\
set JAVA_COMMAND=java -Datax.home=%ParentDirectory% -jar %DATAX_WEB_HOME%datax-web-0.0.1-SNAPSHOT.jar
echo %JAVA_COMMAND%
%JAVA_COMMAND%