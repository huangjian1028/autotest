@echo off
cd ../../../../
set workSpace=%cd%
cd target/lib

setlocal enabledelayedexpansion
for %%i in (*.jar) do (
set CLASSPATH=!CLASSPATH!%cd%\%%i;
)
echo %CLASSPATH%

echo "通过修改excelPath和summarySheet的值来选择测试哪个项目"
set excelPath=D:/svn/mine/code/autotest/autotestdemo/src/main/resources/projects/web/demo/WebAppDemo.xls
set summarySheet=testsummary

java -classpath "%CLASSPATH%" com.wangxun.autotest.ui.util.MakeTestData %excelPath% %summarySheet% %workSpace%

EndLocal