package com.wangxun.autotest.project.web.WebTestDemo.testcase;
import com.wangxun.autotest.project.web.WebTestDemo.frame.WebTestDemo;
import com.wangxun.util.StringUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
@Listeners
public class case2 {
    WebTestDemo WebTestDemo= new WebTestDemo();
    @BeforeClass
    public void setUp(){
        WebTestDemo.initialTestData();
        WebTestDemo.runChromeApp();
        WebTestDemo.logClassInfo("测试集描述");
    }
    @AfterClass
    public void tearDown() {
        WebTestDemo.quit();
    }
    @Test
    public void test03_test(){
        WebTestDemo.logTestDescription("测试用例描述");
        WebTestDemo.get("https://kyfw.12306.cn/otn/leftTicket/init");
        WebTestDemo.sleep(5000);
        WebTestDemo.waitDisplay("车票预订","出发地");
        WebTestDemo.clickElement("车票预订","出发地");
        WebTestDemo.sendKeys("车票预订","出发地","咸宁南");
        WebTestDemo.clickElement("车票预订","咸宁南");
        WebTestDemo.clickElement("车票预订","目的地");
        WebTestDemo.sendKeys("车票预订","目的地","庙山");
        WebTestDemo.waitDisplay("车票预订","庙山");
        WebTestDemo.clickElement("车票预订","庙山");
        WebTestDemo.logSuccessMessage("用例执行成功提示信息");
    }
}
