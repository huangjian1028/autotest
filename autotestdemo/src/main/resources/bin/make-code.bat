@echo off
cd ../../../../
set workSpace=%cd%
cd target/lib

setlocal enabledelayedexpansion
for %%i in (*.jar) do (
set CLASSPATH=!CLASSPATH!%cd%\%%i;
)
echo %CLASSPATH%

echo "ͨ���޸�excelPath��summarySheet��ֵ��ѡ������ĸ���Ŀ"
set excelPath=D:/svn/mine/code/autotest/autotestdemo/src/main/resources/projects/web/demo/WebAppDemo.xls
set summarySheet=testsummary

java -classpath "%CLASSPATH%" com.wangxun.autotest.ui.util.MakeTestData %excelPath% %summarySheet% %workSpace%

EndLocal