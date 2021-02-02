package com.wangxun.autotest.project.web.demo.frame;
import com.wangxun.autotest.ui.base.WebApp;
import org.apache.log4j.PropertyConfigurator;
public class WebDemo1 extends WebApp {
    public void initialTestData(){
    PropertyConfigurator.configure("src/main/resources/config/log4j.properties");
    coreFilePath = "src/main/resources/projects/web/demo/core.properties";
    envName = "testing";
    }
}
