@echo off
set currentdir=%cd%
cd ../lib
echo currentPath=%cd%




setlocal enabledelayedexpansion
for %%i in (*.jar) do (
set CLASSPATH=!CLASSPATH!%cd%\%%i;
)



java -classpath %CLASSPATH% org.testng.TestNG %currentdir%\testng.xml

EndLocal
call pause