package com.wangxun.autotest.ui.util;

import com.wangxun.autotest.ui.core.Initial;
import com.wangxun.autotest.ui.core.UI;
import com.wangxun.util.DateUtil;
import com.wangxun.util.FileUtil;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestngListener extends TestListenerAdapter {
	private class InvalidTest { }
    //类名
	private String className;
    //类介绍
    private String classInfo;
	//测试用例名称
	private String method;
    //用例介绍
    private String caseInfo;
	//测试耗时
	private Long time;
	//执行完后的描述
	private String comment;
	//截图绝对路径
	private String screenPath;
	//截图相对路径
	private String abScreenPath;
	//成功信息
	private String successMessage;


	public static List<Map<String, String>> classData = new ArrayList<Map<String, String>>();

	private void test(ITestResult tr, String status) {
		Map<String, String> methodData = new HashMap<String, String>();
		className = Initial.testClassName;

		method = tr.getName();
		time = tr.getEndMillis() - tr.getStartMillis();
		successMessage = Initial.successMessage;
		caseInfo = Initial.caseInfo;
		classInfo = Initial.classInfo;
		if (status == "Failure") {
			CommonTools.log(method + " Failure");
			List<String> path = null;
			if(!Initial.platform.equalsIgnoreCase("api")){
				try {
					path = takeScreenShot(tr);				
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				screenPath = path.get(0);
				abScreenPath = path.get(1);
			}
			comment = "Failure info - " + tr.getThrowable().getMessage();

		} else if (status == "Success") {
			screenPath = null;
			if (successMessage == null) {
				comment = "Success info - " + "No comment";
			} else {
				comment = "Success info - " + successMessage;
				Initial.successMessage = null;
			}
			if (Initial.ifScreen == true){
				List<String> path = null;
				if(!Initial.platform.equalsIgnoreCase("api")){
					try {
						path = takeScreenShot(tr);				
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					screenPath = path.get(0);
					abScreenPath = path.get(1);
				}
			}
		} else if (status == "Skipped") {
			screenPath = null;
			comment = "Skipped info - " + tr.getThrowable().getMessage();
			if (Initial.ifScreen == true){
				List<String> path = null;
				if(!Initial.platform.equalsIgnoreCase("api")){
					try {
						path = takeScreenShot(tr);				
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					screenPath = path.get(0);
					abScreenPath = path.get(1);
				}
			}

		}

		methodData.put("className", className);
		methodData.put("classInfo", classInfo);
		methodData.put("caseInfo", caseInfo);
		methodData.put("method", method);
		methodData.put("time", time + "");
		methodData.put("status", status);
		methodData.put("comment", comment);
		methodData.put("screenPath", screenPath);
		methodData.put("abScreenPath", abScreenPath);
		classData.add(methodData);
        System.out.println(classData);
	}

	@Override
	public void onTestFailure(ITestResult tr) {
		super.onTestFailure(tr);
		test(tr, "Failure");
	}

	@Override
	public void onTestSuccess(ITestResult tr) {
		super.onTestSuccess(tr);
		test(tr, "Success");
	}

	@Override
	public void onTestSkipped(ITestResult tr) {
		super.onTestSkipped(tr);
		test(tr, "Skipped");
	}

	@Override
	public void onTestStart(ITestResult tr) {
		super.onTestStart(tr);
		System.out.println("The case " + tr.getName() + " belong to the " + tr.getTestClass().getName() + " Start.");
	}

	@Override
	public void onFinish(ITestContext testContext) {
		super.onFinish(testContext);
		System.out.println("The cases end on " + testContext.getEndDate());
		System.out.println("The failed case counts is " + testContext.getFailedTests().size());
		System.out.println("The skipped case counts is " + testContext.getSkippedTests().size());
		System.out.println("The success case counts is " + testContext.getPassedTests().size());

	}

	@Override
	public void onStart(ITestContext testContext) {
		super.onStart(testContext);
		System.out.println("The cases start on " + testContext.getStartDate());
		System.out.println("Start to excute " + testContext.getAllTestMethods().length + " case");
	}

	private List<String> takeScreenShot(ITestResult tr) throws InterruptedException, IOException {
		Thread.sleep(3000);
		String dir_name = Initial.configBean.getTestReportDir()+"/screen/";
		File scrFile = ((TakesScreenshot) UI.driver).getScreenshotAs(OutputType.FILE);

		if (!(new File(dir_name).isDirectory())) { // 判断是否存在该目录
			new File(dir_name).mkdir(); // 如果不存在则新建一个目录
		}
		String imgName = DateUtil.getCurrentDate("yyyyMMdd-HHmmss-SSS") + "_" + tr.getName() + ".png";
		//绝对路径
		String filepath = dir_name + imgName;
		//相对路径
		String abFilePath = "screen/" +imgName;
		FileUtil.copyFile(scrFile, new File(filepath));
//		Reporter.setCurrentTestResult(tr);
//		Reporter.log(filepath);
//		Reporter.log("<img src=\"../" + filepath + "\"/>");
		List<String> path = new ArrayList<String>();
		path.add(filepath);
		path.add(abFilePath);
		return path;

	}

}
