package com.wangxun.autotest.ui.base;

import com.wangxun.autotest.ui.core.UI;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.awt.*;
import java.io.IOException;
import java.util.*;

public class WebApp extends UI {
	private class InvalidTest { }
	private static Logger logger = Logger.getLogger(WebApp.class.getName());
	protected String main_window;

	public void runChromeApp() {



		try {
			initialWebData();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (chromePath != "") {
			System.setProperty("webdriver.chrome.driver", chromePath);
			driver = new ChromeDriver();
		}
		wait = new WebDriverWait(driver, waitTime);

	}

	public void runIEApp() {

		try {
			initialWebData();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (iePath != "") {
			System.setProperty("webdriver.ie.driver", iePath);
			DesiredCapabilities ieCapabilities = DesiredCapabilities
					.internetExplorer();
			ieCapabilities
					.setCapability(
							InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
							true);				
			driver = new InternetExplorerDriver(ieCapabilities);
		}			

		wait = new WebDriverWait(driver, waitTime);

	}

	public void runFirefoxApp() {

		try {
			initialWebData();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (firefoxPath == "") {
			driver = new FirefoxDriver();
		} else {
			System.setProperty("webdriver.firefox.bin", firefoxPath);
			driver = new FirefoxDriver();
		}
		wait = new WebDriverWait(driver, waitTime);


	}
	
	public void quit() {


		createTestReport();

		driver.quit();
	}
	
	public void get(String url) {
		log("打开 " + url + "。");
		try{
			driver.get(url);
		} catch (Exception e) {
			Assert.fail("打开失败。"+getErrorInfo());
		}			
	}

	public void getTitle() {
		log("Get title.");
		driver.getTitle();
	}
	
	public void selectOption(String page,String name,String optionValue){
		Select globalSelect = new Select(findElement(page, name));
		globalSelect.selectByVisibleText(optionValue);
	}

	public void quitWithoutTestData() {
		platform = null;
		driver.quit();
	}

	public String getCurrentUrl() {
		log("Get current url.");
		return driver.getCurrentUrl();
	}

	public Set<String> getWindowHandles() {
		log("Get window handles.");
		return driver.getWindowHandles();
	}

	public String getCurrentWindow() {
		log("Get current window handle.");
		return driver.getWindowHandle();
	}

	public boolean isAlertPresent() {
		sleep(500);
		try {
			driver.switchTo().alert();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void scrollTo(int height) {
		try {
			log("Scroll to " + height);
			String setscroll = "document.documentElement.scrollTop=" + height;
			executeJavaScript(setscroll);
			sleep(1000);
		} catch (Exception e) {
			log("Fail to scroll.");
		}
	}

	private void printAllWindows(){
        Set<String> windows = driver.getWindowHandles();
        logger.info("打印所有的window");
        Iterator iter = windows.iterator();
        while(iter.hasNext()){
            String s = (String)iter.next();
            logger.info("window:"+s);
        }
    }
	public void scrollToBottom() {
		scrollTo(10000);
	}

	public void switchToNewWindow() {
		log("Switch to the new window.");
		sleep(1000);
        printAllWindows();
		try{
			for (String window : driver.getWindowHandles()) {
				if (!window.equals(driver.getWindowHandle())) {
					driver.switchTo().window(window);
				}
			}
		} catch (Exception e) {
			Assert.fail("Fail to switch to the new window."+getErrorInfo());
		}

	}

	public void back() {
		log("back.");
		driver.navigate().back();
		sleep(1000);
	}

	public void forward() {
		log("forward.");
		driver.navigate().forward();
		sleep(1000);
	}
	
	protected void executeJavaScript(String js, WebElement element) {
		((JavascriptExecutor) driver).executeScript(js, element);
	}
	
	public void executeJavaScript(String js) {
		log("执行js语句 \""+js+"\"。");
		try{
			((JavascriptExecutor) driver).executeScript(js);
		} catch (Exception e) {
			Assert.fail("执行js语句失败。"+getErrorInfo());
		}
		sleep(100);
		
	}
	
	public void srollToElement(String page, String name) {
		log("Scroll to the " + name + " element on the " + page + "page.");
		executeJavaScript("arguments[0].scrollIntoView()",
				findElement(page, name));
	}

	public void clickElementByJS(String page, String name) {
		log("Click the " + name + " element on the " + page + "page by JS.");
		WebElement element = findElement(page, name);
		executeJavaScript("arguments[0].click();", element);
	}

	public void moveMouseOn(String page, String name) {

		WebElement element = findElement(page, name);
		log("Moving mouse to element: " + name + ".");
		Actions actions = new Actions(driver);
		actions.moveToElement(element).build().perform();
	}

	public void switchToMainWindow() {
		log("Switch to the main window.");
        printAllWindows();
		try{
			Set<String> getWindows = driver.getWindowHandles();
			for (String win : getWindows) {
				main_window = win;
				break;
			}

			for (String win : getWindows) {
				if (!win.equals(main_window)) {
					driver.switchTo().window(win);
					driver.close();
				}
			}
			driver.switchTo().window(main_window);
		} catch (Exception e) {
			Assert.fail("Fail to switch to the main window."+getErrorInfo());
		}


	}

	public void acceptAlert() {
		sleep(500);
		String str = getAlertText();
		log("Accept the " + str + " Alert.");
		driver.switchTo().alert().accept();
	}
	
	public void dismissAlert() {
		sleep(500);
		String str = getAlertText();
		log("Dismiss the " + str + " Alert.");
		driver.switchTo().alert().dismiss();
	}

	public String getAlertText() {
		Alert alert = driver.switchTo().alert();
		String str = alert.getText();
		return str;
	}

	public void closeAlert() {
		while (isAlertPresent()) {
			String str = getAlertText();
			log("Close the " + str + " Alert.");
			driver.switchTo().alert().accept();
		}
	}

	public void KeyPress(int key) {
		Robot robot;
		try {
			robot = new Robot();
			robot.keyPress(key);
			robot.keyRelease(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void switchToFrame(String nameOrId) {
		try{
			driver.switchTo().frame(nameOrId);
		} catch (Exception e) {
			Assert.fail("Fail to switch to the frame "+nameOrId +"."+getErrorInfo());
		}
		
	}

	public void switchToFrame(int index) {
		driver.switchTo().frame(index);
	}

	public void switchToFrame(String page, String name) {
		driver.switchTo().frame(findElement(page, name));
	}

	public void switchToDefaultContent() {
		log("Switch to the default content.");
		try{
			driver.switchTo().defaultContent();
		} catch (Exception e) {
			Assert.fail("Fail to switch to the default frame "+getErrorInfo());
		}
	}

	public String getTableRowLocationByCss(String location, int index) {

		StringBuffer sb = new StringBuffer(location);
		int i = location.indexOf("tr:nth-child");
		sb.deleteCharAt(i + 13);
		sb.insert(i + 13, index);
		return sb.toString();
	}

	public String getElementAttribute(String page, String name,String attribute) {
		log("To get the "+attribute+" of the " + name + " element on the " + page
				+ " page.");
		String value = findElement(page, name).getAttribute(attribute);
		if (value != null) {
			log("The "+attribute+" is \"" + value + "\".");
			return value;
		} else {
			log("The "+attribute+" is null.");
		}
		return null;
	}

	public void clickElementByCss(String selector) {
		driver.findElement(By.cssSelector(selector)).click();
	}

	public void clickByXpath(String xpathExpression) {
		driver.findElement(By.xpath(xpathExpression)).click();
	}

	public void sendKeysByCss(String selector, String keysToSend) {
		driver.findElement(By.cssSelector(selector)).sendKeys(keysToSend);
	}

	public void sendKeysByXpath(String xpathExpression, String keysToSend) {
		driver.findElement(By.xpath(xpathExpression)).sendKeys(keysToSend);
	}

	public void waitDisplayByCss(String selector) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By
				.cssSelector(selector)));
	}

	public void waitDisplayByXpath(String xpathExpression) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By
				.xpath(xpathExpression)));
	}

	public void clearByCss(String selector) {
		driver.findElement(By.cssSelector(selector)).clear();
	}

	public void clearByXpath(String xpathExpression) {
		driver.findElement(By.xpath(xpathExpression)).clear();
	}
	

	public String runTestCase(String testCase) {

        ArrayList<HashMap<String, String>> cases = testCaseData
				.get(testCase);
        logger.info("cases:"+cases);
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
				} else if (action.equals("waitDisplayAndClick")) {
					waitDisplayAndclickElement(page, name);
					script = appClass + "." + "waitDisplayAndclickElement(\"" + page + "\",\""
							+ name + "\");";
				} else if (action.equals("clear")) {
					clear(page, name);
					script = appClass + "." + "clear(\"" + page + "\",\"" + name
							+ "\");";

				} else if (action.equals("sendKeys")) {
					sendKeys(page, name, value);
					script = appClass + "." + "sendKeys(" + page + "\",\"" + name
							+ "\",\"" + value + ");";

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

				} else if (action.equals("get")) {
					get(value);
					script = appClass + "." + "get(\"" + value + "\");";

				} else if (action.equals("switchToFrame")) {
					switchToFrame(value);
					script = appClass + "." + "switchToFrame(\"" + value + "\");";

				} else if (action.equals("switchToDefaultContent")) {
					switchToDefaultContent();
					script = appClass + "." + "switchToDefaultContent();";

				} else if (action.equals("switchToNewWindow")) {
					switchToNewWindow();
					script = appClass + "." + "switchToNewWindow();";

				} else if (action.equals("switchToMainWindow")) {
					switchToMainWindow();
					script = appClass + "." + "switchToMainWindow();";
					
				} else if (action.equals("executeShell")) {
					executeShell(value);
					script = appClass + "." + "executeShell(\"" + value + "\");";
					
				} else if (action.equals("executeJS")) {
					executeJavaScript(value);
					script = appClass + "." + "executeJavaScript(\"" + value + "\");";
					
				} else if (action.equals("acceptAlert")) {
					acceptAlert();
					script = appClass + "." + "acceptAlert();";
					
				} else if (action.equals("dismissAlert")) {
					dismissAlert();
					script = appClass + "." + "dismissAlert();";
					
				} else if (action.equals("selectOption")) {
					selectOption(page, name, value);
					script = appClass + "." + "selectOption(" + page + "\",\"" + name
							+ "\",\"" + value + "\");";
					
				} else if (action.equals("waitAndclickByMdyEle")) {
					String[] sourceStrArray = value.split(",");
					int begin = Integer.valueOf(sourceStrArray[0]);
					int end = Integer.valueOf(sourceStrArray[1]);
					String newValue = sourceStrArray[2];
					waitAndclickByModifyElement(page, name, begin, end, newValue);
					script = appClass + "." + "waitAndclickByModifyElement(" + page + "\",\"" + name
							+ "\"," + begin + ","+end+",\""+value+"\");";
					
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
			logger.error("error,",e);
			Assert.fail("Fail to run the test case "+testCase+"."+getErrorInfo());
		}

		return suiteScript;
	}

}
