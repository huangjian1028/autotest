@echo off
set currentdir=%cd%
set testngdir=%cd%
echo %currentdir%
echo "��ʼ����"
call mvn-install.bat
echo "��ʼ���ɴ���"
cd %currentdir%
call make-code.bat
echo "�ٴα���"
cd %currentdir%
call mvn-install.bat

echo "ִ���Զ�������"
cd %currentdir%
call test.bat
call pause