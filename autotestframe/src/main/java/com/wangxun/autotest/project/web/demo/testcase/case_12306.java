package com.wangxun.autotest.project.web.demo.testcase;
import com.wangxun.autotest.project.web.demo.frame.WebDemo1;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
@Listeners
public class case_12306 {
    WebDemo1 WebDemo1= new WebDemo1();
    @BeforeClass
    public void setUp(){
        WebDemo1.initialTestData();
        WebDemo1.runChromeApp();
        WebDemo1.logClassInfo("3333");
    }
    @AfterClass
    public void tearDown() {
        WebDemo1.quit();
    }
    @Test
    public void test02_test(){
        WebDemo1.runTestCase("test02_test");
    }
    @Test
    public void test03_test(){
        WebDemo1.runTestCase("test03_test");
    }
}
