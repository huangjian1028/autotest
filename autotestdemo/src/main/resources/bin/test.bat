@echo off
set testngdir=%cd%
cd ../../../../
cd target/lib

setlocal enabledelayedexpansion
for %%i in (*.jar) do (
set CLASSPATH=!CLASSPATH!%cd%\%%i;
)
echo %CLASSPATH%

echo "执行自动化测试"
java -classpath "%CLASSPATH%" org.testng.TestNG %testngdir%\testng-auto.xml


EndLocal