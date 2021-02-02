package com.wangxun.autotest.project.web.demo.testcase;

import com.wangxun.autotest.project.web.demo.frame.WebDemo;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners
public class case_12306_2 {
    WebDemo WebTestDemoWeb= new WebDemo();
    @BeforeClass
    public void setUp(){
        WebTestDemoWeb.initialTestData();
        WebTestDemoWeb.runChromeApp();
        WebTestDemoWeb.logClassInfo("测试集描述");
    }
    @AfterClass
    public void tearDown() {
        WebTestDemoWeb.quit();
    }
    @Test
    public void test02_test(){
//        WebTestDemoWeb.logTestDescription("测试用例描述");
//        WebTestDemoWeb.get("https://kyfw.12306.cn/otn/leftTicket/init");
//        WebTestDemoWeb.sleep(5000);
//        WebTestDemoWeb.waitDisplay("车票预订","出发地");
//        WebTestDemoWeb.clickElement("车票预订","出发地");
//        WebTestDemoWeb.sendKeys("车票预订","出发地","咸宁南");
//        WebTestDemoWeb.clickElement("车票预订","咸宁南");
//        WebTestDemoWeb.clickElement("车票预订","目的地");
//        WebTestDemoWeb.sendKeys("车票预订","目的地","庙山");
//        WebTestDemoWeb.waitDisplay("车票预订","庙山");
//        WebTestDemoWeb.clickElement("车票预订","庙山");
//        WebTestDemoWeb.logSuccessMessage("用例执行成功提示信息");
        WebTestDemoWeb.runTestCase("test02_test");
    }
}
