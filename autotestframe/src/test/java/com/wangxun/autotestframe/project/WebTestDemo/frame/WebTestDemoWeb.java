package com.wangxun.autotestframe.project.WebTestDemo.frame;
import com.wangxun.autotest.ui.base.WebApp;

public class WebTestDemoWeb extends WebApp {
        public void initialTestData(){
                coreFilePath = "src/test/config/WebTestDemo/Web/core.properties";
                envName = "testing";
        }
}
