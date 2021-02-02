package com.wangxun.autotest.project.web.Flylog.frame;
import com.wangxun.autotest.ui.base.WebApp;
import org.apache.log4j.PropertyConfigurator;
public class Flylog extends WebApp {
    public void initialTestData(){
        PropertyConfigurator.configure("../config/log4j.properties");
        coreFilePath = "../projects/web/Flylog/core.properties";
        envName = "testing";
    }
}
