package com.wangxun.autotest.project.android.demo.testcase;


import com.wangxun.autotest.project.android.demo.frame.AndroidDemo;
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
        androidDemo.logTestDescription("测试用例描述");
        androidDemo.waitDisplayAndclickElement("首页", "访问SD卡");
        androidDemo.waitDisplayAndclickElement("首页", "拨打电话和管理电话");
        androidDemo.waitDisplayAndclickElement("首页", "使用此设备的位置信息");
        androidDemo.sleep(2000);
        androidDemo.waitDisplayAndclickElement("首页", "关注");
        androidDemo.waitDisplay("关注", "为你推荐",2);
        androidDemo.waitDisplayAndclickElement("首页", "头条");

        androidDemo.swipeOfType("down");
        androidDemo.swipeOfType("up");
        androidDemo.swipeOfType("left");
        androidDemo.swipeOfType("right");

    }
}
