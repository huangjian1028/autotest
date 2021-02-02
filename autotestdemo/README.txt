作者：听风
邮箱：59853844@qq.com

环境要求
java1.8
maven3.3.9

步骤：
1、进入autotestdemo\src\main\resources\bin目录中，分别执行mvn-install-util-jar.bat和mvn-install-autotest-jar.bat脚本
2、修改make-code.bat文件中的excelPath和summarySheet参数，如下
set excelPath=D:\IWorkspace\autotestdemo\src\main\resources\projects\web\demo\WebAppDemo.xls
set summarySheet=testsummary
3、执行start-auto-test.bat文件


android自动化测试注意点
1、http://tools.android-studio.org/index.php/sdk 下载sdk
2、解压后设置环境变量
3、下载appium-desktop-setup-1.10.0安装包，并安装启动
4、android设备用usb连接上电脑
