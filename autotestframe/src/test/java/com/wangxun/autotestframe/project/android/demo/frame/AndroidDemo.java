package com.wangxun.autotestframe.project.android.demo.frame;
import com.wangxun.autotest.ui.base.AndroidApp;

public class AndroidDemo extends AndroidApp {
        public void initialTestData(){
                coreFilePath = "src/test/config/android/demo/core.properties";
                envName = "testing";
        }
}
