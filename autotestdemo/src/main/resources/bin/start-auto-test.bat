@echo off
set currentdir=%cd%
set testngdir=%cd%
echo %currentdir%
echo "开始编译"
call mvn-install.bat
echo "开始生成代码"
cd %currentdir%
call make-code.bat
echo "再次编译"
cd %currentdir%
call mvn-install.bat

echo "执行自动化测试"
cd %currentdir%
call test.bat
call pause