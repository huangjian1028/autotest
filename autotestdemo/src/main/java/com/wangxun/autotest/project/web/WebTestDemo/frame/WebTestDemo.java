package com.wangxun.autotest.project.web.WebTestDemo.frame;
import com.wangxun.autotest.ui.base.WebApp;
import org.apache.log4j.PropertyConfigurator;
public class WebTestDemo extends WebApp {
    public void initialTestData(){
        PropertyConfigurator.configure("../config/log4j.properties");
        coreFilePath = "../projects/web/WebTestDemo/core.properties";
        envName = "testing";
    }
}
