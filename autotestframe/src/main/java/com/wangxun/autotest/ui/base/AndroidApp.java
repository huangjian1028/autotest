package com.wangxun.autotest.ui.base;


import com.wangxun.autotest.ui.core.UI;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.AndroidTouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AndroidApp extends UI {
    private class InvalidTest { }
	private static Logger logger = Logger.getLogger(AndroidApp.class.getName());

	public void runAndroidApp() {

		try {
			initialAndroidData();
            DesiredCapabilities des = new DesiredCapabilities();
            des.setCapability("app", appDir+"/"+apkName+".apk");
            des.setCapability("platformName", platform);//平台名称
            des.setCapability("platformVersion", platformVersion);//手机操作系统版本p
//            System.out.println("platFormVersion:"+platformVersion);
//            des.setCapability("udid", "192.168.229.101:5555");//连接的物理设备的唯一设备标识
            des.setCapability("unicodeKeyboard", unicodeKeyboard);//支持中文输入
            des.setCapability("resetKeyboard", resetKeyboard);//支持中文输入

            //必须
            des.setCapability("deviceName", androidDeviceName);//使用的手机类型或模拟器类型  UDID
            des.setCapability("appPackage", appPackage);//App安装后的包名,注意与原来的CalcTest.apk不一样
            des.setCapability("appActivity", mainActivity);//app测试人员常常要获取activity，进行相关测试,后续会讲到
            des.setCapability("newCommandTimeout", newCommandTimeout);//命令等待超时时间

//            des.setCapability("newCommandTimeout", "10");//没有新命令时的超时时间设置
//            des.setCapability("nosign", "True");//跳过检查和对应用进行 debug 签名的步骤

			androidDriver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), des);
			driver =androidDriver;
            basicWindowX = androidDriver.manage().window().getSize().getWidth();
            basicWindowY = androidDriver.manage().window().getSize().getHeight();
            logger.info("设备最大宽度:"+basicWindowX);
			logger.info("设备最大高度:"+basicWindowY);
		} catch (Exception e) {
			e.printStackTrace();
		}


//		driver.manage().timeouts().implicitlyWait(waitTime, TimeUnit.SECONDS);
		wait = new WebDriverWait(androidDriver, waitTime);

	}
	
	public void quit() {

		createTestReport();
		androidDriver.quit();
	}
	protected AndroidElement findElement(String page, String name) {
		return (AndroidElement)super.findElement(page,name);
	}


	public void press(int x,int y ){
        log("点击坐标 ("+x+","+y+")。");
		AndroidTouchAction touchAction = new AndroidTouchAction(androidDriver);
        touchAction.press(PointOption.point(x,y)).release().perform();
	}

	public void swipeOfType(String type) {
		try {
			int windowlenX = 0;
			int windowlenY = 0;
			String swipeLeft = "left";
			String swipeRight = "right";
			String swipeUp = "up";
			String swipeDown = "down";
			windowlenX = androidDriver.manage().window().getSize().getWidth();
			windowlenY = androidDriver.manage().window().getSize().getHeight();
			AndroidTouchAction touchAction = new AndroidTouchAction(androidDriver);
//			logger.info("windowlenX:"+windowlenX);
//			logger.info("windowlenY:"+windowlenY);
			Duration duration = Duration.ofMillis(3000);

			if (type.equalsIgnoreCase(swipeLeft)) {
				log("向左滑动。");
				touchAction.press(PointOption.point((int)(windowlenX*0.8),(int) (windowlenY*0.5)))
						.waitAction(WaitOptions.waitOptions(duration)).moveTo(PointOption.point((int)(windowlenX*0.1),(int) (windowlenY*0.5)))
						.release().perform();
//				androidDriver.swipe((int) (windowlenX * 0.9),
//						(int) (windowlenY * 0.5), (int) (windowlenX * 0.1),
//						(int) (windowlenY * 0.5), 4000);
			}
			if (type.equalsIgnoreCase(swipeRight)) {
				log("向右滑动。");
				touchAction.press(PointOption.point((int)(windowlenX*0.1),(int) (windowlenY*0.5)))
						.waitAction(WaitOptions.waitOptions(duration)).moveTo(PointOption.point((int)(windowlenX*0.8),(int)(windowlenY*0.5)))
						.release().perform();
//				androidDriver.swipe((int) (windowlenX * 0.1),
//						(int) (windowlenY * 0.5), (int) (windowlenX * 0.9),
//						(int) (windowlenY * 0.5), 4000);
			}
			if (type.equalsIgnoreCase(swipeUp)) {
				log("向上滑动。");
				touchAction.press(PointOption.point(windowlenX/2,(int) (windowlenY*0.8)))
						.waitAction(WaitOptions.waitOptions(duration)).moveTo(PointOption.point(windowlenX/2,(int)(windowlenY*0.2)))
						.release().perform();
//				androidDriver.swipe((int) (windowlenX * 0.5),
//						(int) (windowlenY * 0.8), (int) (windowlenX * 0.5),
//						(int) (windowlenY * 0.2), 4000);
			}
			if (type.equalsIgnoreCase(swipeDown)) {
				log("向下滑动。");
				touchAction.press(PointOption.point(windowlenX/2,(int) (windowlenY*0.2)))
						.waitAction(WaitOptions.waitOptions(duration)).moveTo(PointOption.point(windowlenX/2,(int) (windowlenY*0.8)))
						.release().perform();

//				androidDriver.swipe((int) (windowlenX * 0.5),
//						(int) (windowlenY * 0.2), (int) (windowlenX * 0.5),
//						(int) (windowlenY
//
//			sleep(500); * 0.8), 4000);
			}
			sleep(3000);
		} catch (Exception e) {
			logger.error("error:",e);
			Assert.fail("Fail to swip " + type + "." +getErrorInfo());
		}

	}

    public String runTestCase(String testCase) {

        ArrayList<HashMap<String, String>> cases = testCaseData
                .get(testCase);
        String suiteScript = "";
        try{
            for (int i = 0; i < cases.size(); i++) {
                String script = "";
                String action = cases.get(i).get("Action");
                String page = cases.get(i).get("Page");
                String name = cases.get(i).get("Element");
                String value = cases.get(i).get("Value");
                String row = cases.get(i).get("row");
                int rowin = Integer.parseInt(row);
                actionRow = String.valueOf(rowin);
                actionCol = "8";

                if (action.equals("click")) {
                    clickElement(page, name);

                    script = appClass + "." + "clickElement(\"" + page + "\",\""
                            + name + "\");";

                } else if (action.equals("sleep")) {
                    int v = Integer.parseInt(value);
                    sleep(v);
                    script = appClass + ".sleep(" + v + ");";

                } else if (action.equals("waitDisplay")) {
                    waitDisplay(page, name);
                    script = appClass + "." + "waitDisplay(\"" + page + "\",\""
                            + name + "\");";
                } else if (action.equals("clear")) {
                    clear(page, name);
                    script = appClass + "." + "clear(\"" + page + "\",\"" + name
                            + "\");";

                } else if (action.equals("sendKeys")) {
                    sendKeys(page, name, value);
                    script = appClass + "." + "sendKeys(" + page + "\",\"" + name
                            + "\",\"" + value + ");";

                } else if (action.equals("waitDisplayAndClick")) {
                    waitDisplayAndclickElement(page, name);

                } else if (action.equals("appPress")) {
                    int x = Integer.valueOf(value.split(",")[0]);
                    int y = Integer.valueOf(value.split(",")[1]);
                    press(x, y);

                } else if (action.equals("appSwipe")) {
                    swipeOfType(value);
                } else if (action.equals("getScreen")) {
                    getScreen(value);
//					logResult(rowin);
                    putResultData(rowin, "P");
                    script = appClass + "." + "getScreen(\"" + value + "\");";
                    putScriptData(rowin, script);
                    suiteScript = suiteScript+script;

                } else if (action.equals("assertEquals")) {
                    String actual = getElementText(page, name);
                    assertEquals(actual, value);
                    script = appClass + "." + "assertEquals(" + appClass + "."
                            + "getElementText(\"" + page + "\",\"" + name + "\")"
                            + "," + "\"" + value + "\");";

                } else if (action.equals("assertContains")) {
                    String actual = getElementText(page, name);
                    assertContains(actual, value);
                    script = appClass + "." + "assertContains(" + appClass + "."
                            + "getElementText(\"" + page + "\",\"" + name + "\")"
                            + "," + "\"" + value + "\");";

                } else if (action.equals("runCase")) {
                    log("Run the \"" + value + "\" test case.");
                    script = runTestCase(value);

                } else if (action.equals("runCode")) {

                    String[] sourceStrArray = value.split(",");
                    String caseName = "";
                    int intCount = 0;
                    if (sourceStrArray.length == 1) {
                        caseName = value;
                        intCount = 1;
                    } else {
                        caseName = sourceStrArray[0];
                        String count = sourceStrArray[1];
                        intCount = Integer.valueOf(count);
                    }
                    log("Run the " + caseName + " test case for " + intCount
                            + " times.");
                    for (int k = 0; k < intCount; k++) {
                        script = runTestCase(caseName);
                    }

                    script = "for(int k =0;k<" + intCount + ";k++){" + "    "
                            + script + "}";

                }

                else {
                    log("Can not run the action");
                    continue;

                }
//				logResult(rowin);
                putResultData(rowin, "P");
                putScriptData(rowin, script);
                suiteScript = suiteScript + script;
                actionRow = null;

            }
        } catch (Exception e) {
            Assert.fail("Fail to run the test case "+testCase+"."+getErrorInfo());
        }

        return suiteScript;
    }
	private void test(){
		AndroidElement a = new AndroidElement();
		a.replaceValue("2");
	}

}
