package com.wangxun.autotest.ui.core;

import com.wangxun.autotest.ui.bean.ConfigBean;
import com.wangxun.autotest.ui.util.*;
import com.wangxun.util.CommonMethod;
import com.wangxun.util.DateUtil;
import com.wangxun.util.StringUtil;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.io.IOException;
import java.util.*;


public class Initial {
    private class InvalidTest { }
    private static Logger logger = Logger.getLogger(Initial.class.getName());
    // 等待时间
    protected int waitTime;
    // 测试项目配置文件（必填）
    protected String configFilePath;
    // 测试数据存储路径（必填）
    private String testDataExcelPath;
    // 元素定位对应sheet名（必填）
    private String elementSheet;
    // 测试用例对应sheet名
    protected String testCaseSheet;
    //测试用例中action对应的行数
    protected String actionRow;
    //测试用例中action对应的列数
    protected String actionCol;


    // 截屏存放路径
    protected String screenDir;

    // 测试报告存储文件夹
    public static String testReportDir;
    // 测试报告名称
    protected String testReportName;
    // 当前运行测试class的类名
    public static String testClassName;

    // 元素定位对象
    protected HashMap<String, HashMap<String,HashMap<String, String>>> elementData;
    // 测试用例对象
    protected HashMap<String, ArrayList<HashMap<String, String>>> testCaseData;
    // 测试app的对象名
    protected String appClass;
    // 回写用例脚本开关
    protected boolean writeScript;
    // 回写测试结果开关
    protected boolean writeResult;
    // log开关
    private boolean logSwitch;

    // 测试报告testsummary sheet名称
    protected String testSummarySheetName;

    // 测试项目名称
    private String projectName;
    // 测试项目简介
    private String projectInfo;
    // 测试范围
    private String testSpecification;

    // 每个case执行成功后的message
    public static String successMessage;
    // 每个case的介绍
    public static String caseInfo;
    // 每个class的介绍
    public static String classInfo;
    //判断用例成功后是否截图
    public static boolean ifScreen;

    // 测试脚本数据
    public static List<List<String>> testScriptData = new ArrayList<List<String>>();
    // 测试结果数据
    public static List<List<String>> testResultData = new ArrayList<List<String>>();
    // log数据
    public static List<List<String>> logData = new ArrayList<List<String>>();
    // 测试app类型
    public static String platform;
    // 存放配置文件路径的文件，和envName字段配合使用
    protected String coreFilePath;
    // 环境变量值，为了jekins集成设计的字段
    protected String envName;
    // 火狐浏览器路径
    protected String firefoxPath;
    // 谷歌浏览器路径
    protected String chromePath;
    // IE浏览器路径
    protected String iePath;
    // report文件夹路径，不带时间
    private String reportDirPathNoTime;
    //测试报告路径
    private String reportPath;
    // 当前执行的时间
    private static final String currentTime = DateUtil.getCurrentDate("yyyyMMdd-HHmmss-SSS");
    // api测试地址
    public static String apiUrl;
    // 测试报告保留分数
    private int keepReportCount;

    //report数量控制中间变量
    public static int runTimes;
    // app存放文件夹（android项目必填）
    protected String appDir;

    // andorid配置信息
    /*
     * apkName = ticket11-27.apk appPackage = cn.beyondsoft.wicket mainActivity
     * = cn.beyondsoft.wicket.LoddingActivity unicodeKeyboard=true
     * resetKeyboard=true
     */
    protected int basicWindowX;
    protected int basicWindowY;
    protected boolean unicodeKeyboard;
    protected boolean resetKeyboard;
    // android项目必填
    protected String androidDeviceName;
    // android项目必填
    protected String apkName;
    // android项目必填
    protected String appPackage;
    // android项目必填
    protected String mainActivity;
    protected String platformVersion;
    protected  int newCommandTimeout;

    public static ConfigBean configBean = new ConfigBean();

    protected void getConfig(){
        Properties properties = CommonMethod.getProperties(configFilePath);
        //通用
        configBean.setProjectName(properties.getProperty("projectName"));
        configBean.setProjectInfo(properties.getProperty("projectInfo"));
        configBean.setTestSpecification(properties.getProperty("testSpecification"));
        configBean.setTestDataExcelPath(properties.getProperty("testDataExcelPath"));
//        configBean.setTestCaseSheetNames(properties.getProperty("testCaseSheetNames"));
        configBean.setTestCaseSheetName(properties.getProperty("testCaseSheetName"));
        configBean.setElementSheet(properties.getProperty("elementSheet"));
        configBean.setIfScreen(Boolean.valueOf(properties.getProperty("ifScreen")));
        configBean.setWaitTime(Integer.valueOf(properties.getProperty("waitTime","0")));


        configBean.setTestReportDir(properties.getProperty("testReportDir"));
//        configBean.setReportCount(Integer.valueOf(properties.getProperty("reportCount","0")));

        //web
//        configBean.setBrowserType(properties.getProperty("browserType"));
        configBean.setChromePath(properties.getProperty("chromePath"));
        configBean.setFirefoxPath(properties.getProperty("firefoxPath"));
        configBean.setIePath(properties.getProperty("iePath"));



        //android
        configBean.setAppDir(properties.getProperty("appDir"));
        configBean.setUnicodeKeyboard(Boolean.valueOf(properties.getProperty("unicodeKeyboard")));
        configBean.setResetKeyboard(Boolean.valueOf(properties.getProperty("resetKeyboard")));
        configBean.setAndroidDeviceName(properties.getProperty("androidDeviceName"));
        configBean.setApkName(properties.getProperty("apkName"));
        configBean.setAppPackage(properties.getProperty("appPackage"));
        configBean.setMainActivity(properties.getProperty("mainActivity"));
        configBean.setPlatformVersion(properties.getProperty("platformVersion"));
        if(properties.getProperty("newCommandTimeout")!=null){
            configBean.setNewCommandTimeout(Integer.valueOf(properties.getProperty("newCommandTimeout")));
        }
        else{
            configBean.setNewCommandTimeout(60);
        }

    }





    protected String getErrorInfo() {
        String info = "";
        if (actionRow != null){
            info = "\ntestCaseSheet:" + testCaseSheet + ",row:"+actionRow + ",col:"+actionCol;
        }
        return info;
    }



//    protected void logWarning(Object content) {
//        CommonTools.log(content, 4);
//    }

    protected HashMap<String, HashMap<String, HashMap<String, String>>> getElementData() {
        if (testDataExcelPath == null) {
            logger.warn("Please provide testDataExcelPath.");
        } else if (elementSheet == null) {
            logger.warn("Please provide elementSheet.");
        } else {
            logger.info("testDataExcelPath:"+testDataExcelPath);
            logger.info("elementSheet:"+elementSheet);
            HashMap<String, HashMap<String, HashMap<String, String>>> eledata = PoiReadElementData.getElementData(testDataExcelPath, elementSheet);

            return eledata;
        }
        return null;

    }
    protected HashMap<String, ArrayList<HashMap<String, String>>> getTestCaseData() {
        if (testDataExcelPath == null) {
            logger.warn("Please provide testCaseExcelPath.");
        } else if (testCaseSheet == null) {
            logger.warn("Please provide testCaseSheet.");
        } else {
            logger.info("testDataExcelPath:"+testDataExcelPath);
            logger.info("testCaseSheet:"+testCaseSheet);
            HashMap<String, ArrayList<HashMap<String, String>>> data = PoiReadTestCaseData.getTestCaseData(testDataExcelPath,testCaseSheet);
            return data;


        }
        return null;
    }
    public void changeToThirdAppElementData(String excelPath, String elementSheet) {
        HashMap<String, HashMap<String,HashMap<String, String>>> data = PoiReadElementData.getElementData(testDataExcelPath, testCaseSheet);

        elementData = data;

    }

    public void changeToMainAppElementData() {
        elementData = getElementData();
    }



//
//    protected Map<String, Object> getTestCaseData() {
//        if (testDataExcelPath == null) {
//            logWarning("Please provide testCaseExcelPath.");
//        } else if (testCaseSheet == null) {
//            logWarning("Please provide testCaseSheet.");
//        } else {
//            String[] testCaseSheets = configBean.getTestCaseSheetNames().split(",");
//            if (CommonTools.isIn(testCaseSheet, testCaseSheets)){
//                ReadTestCasesData testCaseData = new ReadTestCasesData(
//                        testDataExcelPath, testCaseSheet);
//
//                Map<String, Object> data = null;
//                data = testCaseData.getdata();
//                return data;
//            }
//
//        }
//        return null;
//    }
//
//
    protected String getClassName() {
        try {
            ITestResult it = Reporter.getCurrentTestResult();
            String classNamePath = it.getTestClass().getName();

            return classNamePath;
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return "";
    }

    protected String getTestMethodName() {
        try {
            ITestResult it = Reporter.getCurrentTestResult();
            String testMethodName = it.getName();

            return testMethodName;
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return "";
    }


    protected String getTestClassName() {
        String className = getClassName();
        String[] ddd = className.split("\\.");
        return StringUtil.getStr(ddd[ddd.length - 1],31);
//        return StringUtil.getStr(ddd[ddd.length - 2] + "."
//                + ddd[ddd.length - 1],31);
    }


    protected String getInitialPropertiesPath(String coreFilePath, String key) {
        String result = System.getProperty(key);
        if (StringUtil.isEmpty(result)){
            result = CommonMethod.getProperty(coreFilePath,key);
        }
        return result;
    }


    public void log(String content) {
        if (logSwitch == true) {
            String time = DateUtil.getCurrentDate();
            logger.info(time + " INFO - " + content);
            putLogData("", time + " INFO - " + content);

        }
    }


    protected void putScriptData(Integer row, String script) {
        String rowString = String.valueOf(row);
        List<String> result = new ArrayList<String>();
        result.add(rowString);
        result.add(script);
        testScriptData.add(result);
    }

    protected void putResultData(Integer row, String result) {
        String rowString = String.valueOf(row);
        List<String> data = new ArrayList<String>();
        data.add(rowString);
        data.add(result);
        testResultData.add(data);
    }

    protected void putLogData(String title, String info) {
        List<String> data = new ArrayList<String>();
        data.add(title);
        data.add(info);
        logData.add(data);
    }

    public void logTestDescription(String content) {
        try {
            String testMethod = getTestMethodName();
            putLogData(testMethod, "Test case description: " + content);
            caseInfo = content;
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public void logSuccessMessage(String content) {
        successMessage = content;
        try {
            putLogData("", "Success info: " + content);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public void logClassInfo(String content) {
        try {
            putLogData("ClassName", testClassName);
            putLogData("Description", content);
            classInfo = content;
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public void sleep(int time) {
        log("休眠 " + time + " 毫秒。");
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        CommonMethod.sleep(time);
    }



    public void assertFail(String message) {
        Assert.fail(message);
    }

    public void assertContains(String actual, String expected) {
        log("AssertContains, expected[" + expected + "], actual[" + actual + "].");
        if (!actual.contains(expected)) {
            Assert.fail("Expected [" + expected + "], but found [" + actual
                    + "].");
        }
    }

    public void assertNotContains(String actual, String expected) {
        log("AssertNotContains, not expected[" + expected + "], actual[" + actual + "].");
        if (actual.contains(expected)) {
            Assert.fail("Not expected [" + expected + "], but found [" + actual
                    + "].");
        }
    }

    public void assertEquals(String actual, String expected) {
        log("AssertEquals expected[" + expected + "], actual[" + actual + "].");
        Assert.assertEquals(actual, expected);
    }

    protected void createTestReport() {

//            String excelPath = testDataExcelPath;
//            if (writeResult == true) {
//                CreateReport.writeResultToExcel(excelPath, testCaseSheet, testResultData);
//                testResultData.clear();
//            }
//            if (writeScript == true) {
//                CreateReport.writeScriptToExcel(excelPath, testCaseSheet, testScriptData);
//                testScriptData.clear();
//            }

            CreateReport.writeLogToExcel(reportPath, testClassName, logData,testSummarySheetName);
            logData.clear();
        try {
            CreateReport.writeTestSummaryToExcel(reportPath, testSummarySheetName, TestngListener.classData);
        } catch (WriteException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        TestngListener.classData.clear();

    }
    public List<Map<String, String>> getTestData(String testDataSheet) {

        ReadTestData readtestdata = new ReadTestData();
        List<Map<String, String>> data = null;
        data = readtestdata.getTestData(testDataExcelPath, testDataSheet);

        return data;
    }

    public Object[][] getTestDataForTestNG(String testDataSheet) {

        ReadTestData readtestdata = new ReadTestData();
        Object[][] data = null;
        data = readtestdata.getTestDataForTestNG(testDataExcelPath,
                testDataSheet);
        return data;
    }



    protected void initialData() throws WriteException, BiffException,
            IOException {
//        PropertyConfigurator.configure("src/main/resources/config/log4j.properties");
//        PropertyConfigurator.configure("../config/log4j.properties");
//        runTimes ++;
        configFilePath = getInitialPropertiesPath(coreFilePath, envName);
        logger.info("configFilePath:"+configFilePath);
        getConfig();
        testDataExcelPath = configBean.getTestDataExcelPath();
        elementSheet = configBean.getElementSheet();
        testCaseSheet = configBean.getTestCaseSheetName();
        if (StringUtils.isEmpty(testCaseSheet)){
            String testClassName = getTestClassName();
            testCaseSheet = testClassName;
        }

        projectName = configBean.getProjectName();
        projectInfo = configBean.getProjectInfo();
        testSpecification = configBean.getTestSpecification();

        waitTime = configBean.getWaitTime();

        screenDir = configBean.getTestReportDir();

//        appClass = projectName +configBean.getPlatform();
        writeScript = true;
        writeResult = true;
        ifScreen = configBean.isIfScreen();
        logSwitch = true;



        elementData = getElementData();
        logger.info("elementData:"+elementData);
        testCaseData = getTestCaseData();
        logger.info("testCaseData:"+testCaseData);

//        testClassName = getTestClassName();
        testReportDir = configBean.getTestReportDir();


//        keepReportCount = configBean.getReportCount();

//        CommonTools.keepFileCount(reportDirPathNoTime, keepReportCount);
        if(StringUtil.isEmpty(testReportName)){
            testReportName = projectName+"-TestReport"+"(" + currentTime + ")";
        }
        reportPath = testReportDir+ "/"+testReportName + ".xls";
        if (StringUtil.isEmpty(testSummarySheetName)){
            testSummarySheetName = "TestSummary";
        }
        testClassName = getTestClassName();
        CreateReport.createReportExcel(testReportDir, testReportName, testSummarySheetName,projectName, projectInfo, testSpecification);

        JXLTool.createSheet(reportPath,testClassName, 999);



    }

    protected void initialWebData() throws WriteException, BiffException,
            IOException {
        platform = "Web";
        initialData();
        iePath = configBean.getIePath();
        firefoxPath = configBean.getFirefoxPath();
        chromePath = configBean.getChromePath();

    }
    protected void initialAndroidData() throws WriteException, BiffException,
            IOException {
        platform = "Android";
        initialData();
        appDir = configBean.getAppDir();
        androidDeviceName = configBean.getAndroidDeviceName();
        apkName =configBean.getApkName();
        appPackage = configBean.getAppPackage();
        mainActivity = configBean.getMainActivity();
        unicodeKeyboard =configBean.isUnicodeKeyboard();
        resetKeyboard = configBean.isResetKeyboard();
        platformVersion=configBean.getPlatformVersion();
        newCommandTimeout = configBean.getNewCommandTimeout();

    }

//    protected void initialIOSData() throws WriteException, BiffException,
//            IOException {
//        platform = "Ios";
//        initialData();
//        appDir = getAppDir();
//        iosApp = getProperties("iosApp");
//        iosDeviceName = getProperties("iosDeviceName");
//        platformVersion = getProperties("platformVersion");
//        platform = getProperties("platform");
//        platformName = getProperties("platformName");
//        browserName = getProperties("browserName");
//
//    }

}
