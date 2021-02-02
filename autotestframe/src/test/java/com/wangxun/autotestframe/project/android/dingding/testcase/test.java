package com.wangxun.autotestframe.project.android.dingding.testcase;


import com.wangxun.autotestframe.project.android.dingding.frame.AndroidDemo;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners
public class test {
    AndroidDemo androidDemo= new AndroidDemo();
    @BeforeClass
    public void setUp(){
        androidDemo.initialTestData();
        androidDemo.runAndroidApp();
        androidDemo.logClassInfo("测试集描述");
    }
    @AfterClass
    public void tearDown() {
            androidDemo.quit();
    }
    @Test
    public void test02_test(){
//        androidDemo.logTestDescription("测试用例描述");
//        androidDemo.waitDisplayAndclickElement("首页", "访问SD卡");
//        androidDemo.waitDisplayAndclickElement("首页", "拨打电话和管理电话");
//        androidDemo.waitDisplayAndclickElement("首页", "登录");
//        androidDemo.sleep(2000);
//        androidDemo.waitDisplay("登录","登录");
//        androidDemo.sendKeys("登录","手机号","18627802681");
//        androidDemo.sendKeys("登录","密码","1234567q");
//        androidDemo.clickElement("登录","登录");
//        androidDemo.sleep(5000);
//        androidDemo.press(980,1670);
//        androidDemo.waitDisplayAndclickElement("下边栏","工作");
//        androidDemo.waitDisplay("工作","工作webkit");
//        androidDemo.swipeOfType("up");
//        androidDemo.swipeOfType("up");
//        androidDemo.swipeOfType("up");
//        androidDemo.press(360,888);
//        androidDemo.waitDisplayAndclickElement("首页", "使用此设备的位置信息");
//        androidDemo.sleep(10000);
        androidDemo.runTestCase("test02_test");


    }
}
