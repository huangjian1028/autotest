package com.wangxun.autotest.ui.core;

import com.wangxun.autotest.ui.util.ImageUtils;
import com.wangxun.util.DateUtil;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.awt.*;
import java.io.*;
import java.util.List;

public class UI extends Initial {
	private class InvalidTest { }
	private static Logger logger = Logger.getLogger(UI.class.getName());
	public static WebDriver driver;
	protected AndroidDriver<AndroidElement> androidDriver;
	protected IOSDriver<IOSElement> iosDriver;
	protected WebDriverWait wait;

	public void clickElement(String page, String name) {
		log("点击 " + page + " 页面的 " + name + " 元素。");
		try {
			findElement(page, name).click();
			sleep(1000);
		} catch (Exception e) {
		    logger.error("error:",e);
			Assert.fail("点击失败。"+getErrorInfo());
		}
	}
    public void waitDisplayAndclickElement(String page, String name) {
        waitDisplay(page,name,3);
        clickElement(page,name);
    }


	public void sendKeys(String page, String name, String value) {
		log("输入 " + value + " 到 " + page
				+ " 页面的 " + name + " 元素。");
		try {
			findElement(page, name).sendKeys(value);
			sleep(100);
		} catch (Exception e) {
			Assert.fail("输入失败。"+getErrorInfo());
		}
	}

	public void clear(String page, String name) {
		log("清空 " + page + " 页面的 " + name + " 元素的值。");
		try {
			findElement(page, name).clear();
			sleep(100);
		} catch (Exception e) {
			Assert.fail("清空失败。"+getErrorInfo());
		}
	}

	public void clear(String page, String name, String defaultStr) {
        log("清空 " + page + " 页面的 " + name + " 元素的值。");
		while (true) {
			WebElement ele = findElement(page, name);
			if (!getElementText(page, name).contains(defaultStr)) {
				try {
					ele.clear();
				} catch (Exception e) {
					Assert.fail("清空失败。");
				}
			} else {
				break;
			}
		}
	}

	public String getElementText(String page, String name) {
		log("获取 " + page + " 页面的 " + name
				+ " 元素的值。");
		String value = findElement(page, name).getText();
		if (value != null) {
			log("获取的值为 \"" + value + "\".");
			return value;
		} else {
			log("获取的值为空");
		}
		return null;
	}

	public String getElementTextLen(String page, String name) {
		log("获取 " + page + " 页面的 " + name
				+ " 元素的值的长度。");
		String len = getElementText(page,name).length()+"";
		log("长度为 \"" + len + "\".");
		return len;
	}

	
	public void executeShell(String shell) {
		log("To run the shell \""+shell+"\".");
        BufferedReader br2 = null;
        String line = null;
        String result = "The result after executing the shell is:\n";
        InputStream is = null;
        InputStreamReader isReader = null;
        try {
            Process proc = Runtime.getRuntime().exec(shell);
            is = proc.getInputStream();
            isReader = new InputStreamReader(is, "utf-8");
            br2 = new BufferedReader(isReader);
            while ((line = br2.readLine()) != null) {
            	result = result +line +"\n";
            }
            sleep(500);
        } catch (IOException e) {
            Assert.fail("Fail to execute the script " + shell + " "+getErrorInfo());
        } finally {
            if (isReader != null) {
                try {
                    isReader.close();
                } catch (IOException e) {
                }
            }

            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {

                }
            }

            if (br2 != null) {
                try {
                    br2.close();
                } catch (IOException e) {

                }
            }
        }
        log(result);
    }

	public boolean isSlected(String page, String name) {
		return findElement(page, name).isSelected();
	}

	public boolean isDisplayed(String page, String name) {
		return findElement(page, name).isDisplayed();
	}

	public boolean isEnabled(String page, String name) {
		return findElement(page, name).isEnabled();
	}


	public WebElement findElementByClassNameIndex(String className, int index) {

		List<WebElement> eles = driver.findElements(By.className(className));
		return eles.get(index);
	}
	
	public WebElement findElementByIdIndex(String id, int index) {
		
		List<WebElement> eles = driver.findElements(By.id(id));
		return eles.get(index);		
	}

	public WebElement findElementByNameIndex(String name, int index) {
		List<WebElement> eles = driver.findElements(By.name(name));
		return eles.get(index);	
	}
	
	/**
	* @Description: 根据字符串起始位置修改定位符
	* @Param [page, name, begin, end, newValue]
	* @return void
	* @Author wangxun 
	* @Date 2019-02-16 20:00
	*/
	private void modidyElement(String page,String name,int begin,int end,String newValue){
		String location = elementData.get(page).get(name).get("Location");
		String str1 = location.substring(0, Integer.valueOf(begin));
		String str2 = location.substring(Integer.valueOf(end)+1, location.length() );
		String newLocation = str1+newValue+str2;
		elementData.get(page).get(name).put("Location", newLocation);
	}

	public void waitAndclickByModifyElement(String page,String name,int begin,int end,String newValue){
		modidyElement(page, name, begin, end, newValue);
		waitDisplay(page, name);
		clickElement(page, name);
	}
	
	protected WebElement findElement(String page, String name) {
		try {
            log("开始定位 " + page + " 页面的 " + name + " 元素。");
			String selecttype = elementData.get(page).get(name)
					.get("SelectType");
			String location = elementData.get(page).get(name).get("Location");

			if (selecttype.equals("css")) {
				return driver.findElement(By.cssSelector(location));
			} else if (selecttype.equals("id")) {
				return driver.findElement(By.id(location));
			} else if (selecttype.equals("xpath")) {
				return driver.findElement(By.xpath(location));
			} else if (selecttype.equals("name")) {
				return driver.findElement(By.name(location));

			} else if (selecttype.equals("linktext")) {
				return driver.findElement(By.linkText(location));

			} else if (selecttype.equals("partiallinktext")) {
				return driver.findElement(By.partialLinkText(location));
			} else if (selecttype.equals("tagname")) {
				return driver.findElement(By.tagName(location));
			} else if (selecttype.equals("classByIndex")) {
				String[] sourceStrArray = location.split(",");
				String classname = sourceStrArray[0];
				String index = sourceStrArray[1];
				int in = Integer.parseInt(index);
				return findElementByClassNameIndex(classname, in);
			} else if (selecttype.equals("nameByIndex")) {
				String[] sourceStrArray = location.split(",");
				String name2 = sourceStrArray[0];
				String index = sourceStrArray[1];
				int in = Integer.parseInt(index);
				return findElementByNameIndex(name2, in);
			} else if (selecttype.equals("idByIndex")) {
				String[] sourceStrArray = location.split(",");
				String id = sourceStrArray[0];
				String index = sourceStrArray[1];
				int in = Integer.parseInt(index);
				return findElementByIdIndex(id, in);
/*			} else if (ui instanceof AndroidApp) {
				if (selecttype.equals("scrollname")) {
					return androidDriver.scrollTo(location);
				}*/
			} else {
				log("不支持 "+selecttype+" 定位方式。");
			}
		} catch (Exception e) {
			logger.error("error:",e);
			Assert.fail("未定位到 " + page + " 页面的 " + name+ " 元素。"+getErrorInfo());
		}
		return null;
	}


	public void waitDisplay(String page, String name) {
		waitDisplay(page, name, 2);
	}

	protected void waitDisplayAfterLoading(String page, String name,
			String loadingPage, String loadingName) {
		waitDisplayAfterLoading(page, name, loadingPage, loadingName, 10);
	}

	protected void waitDisplayAfterLoading(String page, String name,
			String loadingPage, String loadingName, int count) {
		boolean status = false;
		for (int i = 0; i < count; i++) {
			if (!verifyDisplay(loadingPage, loadingName)) {
				status = true;
				waitDisplay(page, name, 10);
				break;
			}
		}
		if (status == false) {
			Assert.fail("The UI is still loading.");
		}

	}

	public void waitDisplay(String page, String name, int count) {
		boolean status = false;
		for (int i = 0; i < count; i++) {
			status = verifyDisplay(page, name);
			if (status == false) {
				if (i < count - 1) {
					log("再次定位 " + page + " 页面的 " + name
							+ " 元素。");
				}
				continue;
			} else {
				break;
			}
		}
		if (status == false) {
			Assert.fail("未定位到 " + page + " 页面的 " + name
                    + " 元素。"+getErrorInfo());

		}

	}

	public boolean verifyDisplay(String page, String name) {

		try {
			String selecttype = elementData.get(page).get(name)
					.get("SelectType");
			String location = elementData.get(page).get(name).get("Location");
			log("开始定位 " + page + " 页面的 " + name + " 元素。");
			if (selecttype.equals("css")) {
				
				wait.until(ExpectedConditions.presenceOfElementLocated(By
						.cssSelector(location)));

			} else if (selecttype.equals("id")) {

				wait.until(ExpectedConditions.presenceOfElementLocated(By
						.id(location)));

			} else if (selecttype.equals("xpath")) {

				wait.until(ExpectedConditions.presenceOfElementLocated(By
						.xpath(location)));

			} else if (selecttype.equals("linktext")) {

				wait.until(ExpectedConditions.presenceOfElementLocated(By
						.linkText(location)));

			} else if (selecttype.equals("name")) {

				wait.until(ExpectedConditions.presenceOfElementLocated(By
						.name(location)));

			} else if (selecttype.equals("partiallinktext")) {

				wait.until(ExpectedConditions.presenceOfElementLocated(By
						.partialLinkText(location)));

			} else if (selecttype.equals("tagname")) {

				wait.until(ExpectedConditions.presenceOfElementLocated(By
						.tagName(location)));

			} else if (selecttype.equals("classByIndex")) {
				
				String[] sourceStrArray = location.split(",");
				String classname = sourceStrArray[0];
				wait.until(ExpectedConditions.presenceOfElementLocated(By
						.className(classname)));

			} else if (selecttype.equals("idByIndex")) {
				
				String[] sourceStrArray = location.split(",");
				String id = sourceStrArray[0];
				wait.until(ExpectedConditions.presenceOfElementLocated(By
						.id(id)));

			} else if (selecttype.equals("nameByIndex")) {
				
				String[] sourceStrArray = location.split(",");
				String name2 = sourceStrArray[0];
				wait.until(ExpectedConditions.presenceOfElementLocated(By
						.name(name2)));

			} else {
				log("Don't support the type.");
				return false;
			}
		} catch (Exception e) {

			log("未定位到 " + page + " 页面的 " + name + " 元素。");
			return false;
		}
		log("定位到 " + page + " 页面的 " + name + " 元素。");
		sleep(500);
		return true;
	}


	public String getScreen(String fileName) {
		File scrFile = null;

		scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

		if (!(new File(screenDir).isDirectory())) { // 判断是否存在该目录
			new File(screenDir).mkdir(); // 如果不存在则新建一个目录
		}
		try {
			log("To get screen. The screen fileName is "+ fileName + ".");
			String path = "";
			if (fileName == ""){
				path = screenDir + "/"+ DateUtil.getCurrentDate("yyyyMMdd-HHmmss-SSS")+".png";
			}
			else{
				path = screenDir + "/"+DateUtil.getCurrentDate("yyyyMMdd-HHmmss-SSS") + "_"
						+ fileName + ".png";
			}

			FileUtils.copyFile(scrFile, new File(path));
			return path;
		} catch (Exception e) {
			Assert.fail("Get screen failed."+getErrorInfo());

		}
		return null;

	}

	public void getScreen() {
		getScreen("");
	}

	private String getScreenReturnPath() {
		return getScreen("");
	}

	public void getElementScreen(String page, String name) {
		String srcImg = getScreenReturnPath();
		File srcImg1 = new File(srcImg);
		String destImg = screenDir;
		String imgName = srcImg1.getName();
		int x = getElementLocateX(page, name);
		int y = getElementLocateY(page, name);
		int elementX = getElementX(page, name);
		int elementY = getElementY(page, name);
		try {
			ImageUtils.cutImage(srcImg, destImg + "/cut by element_ " + name
					+ " " + imgName, x, y, elementX, elementY);
		} catch (Exception e) {
			Assert.fail("Get element screen failed.\n");

		}
	}

	public void getScreenMarkedByText(String content) {

		String srcImg = getScreenReturnPath();
		File srcImg1 = new File(srcImg);
		String destImg = screenDir;
		String name = srcImg1.getName();
		try {
			ImageUtils.markImageByText(srcImg, destImg + "/marked by text "
					+ content + " " + name, content, Color.red, "黑体", 13);
		} catch (Exception e) {
			Assert.fail("Get element screen failed.\n");
		}
	}

	public int getElementX(String page, String name) {
		return findElement(page, name).getSize().getWidth();
	}

	public int getElementY(String page, String name) {
		return findElement(page, name).getSize().getHeight();
	}

	public int getElementLocateX(String page, String name) {
		return findElement(page, name).getLocation().getX();
	}

	public int getElementLocateY(String page, String name) {
		return findElement(page, name).getLocation().getY();
	}
	


}
