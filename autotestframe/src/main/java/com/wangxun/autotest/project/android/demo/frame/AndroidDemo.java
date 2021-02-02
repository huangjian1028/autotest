package com.wangxun.autotest.project.android.demo.frame;
import com.wangxun.autotest.ui.base.AndroidApp;
import org.apache.log4j.PropertyConfigurator;

public class AndroidDemo extends AndroidApp {
    public void initialTestData(){
        PropertyConfigurator.configure("src/main/resources/config/log4j.properties");
        coreFilePath = "src/main/resources/projects/android/demo/core.properties";
        envName = "testing";
    }
}
