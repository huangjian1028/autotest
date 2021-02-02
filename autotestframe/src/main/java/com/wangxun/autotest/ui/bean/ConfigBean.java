package com.wangxun.autotest.ui.bean;

import com.wangxun.util.JsonUtil;

public class ConfigBean {

    private String projectName;
    private String projectInfo;
    private String platform;
    private String testSpecification;
    private String testDataExcelPath;
    private String elementSheet;
    private String browserType;
    private String chromePath;
    private String iePath;
    private String firefoxPath;
    private boolean ifScreen;
    private int reportCount;
    private String testReportDir;
    private int waitTime;
    private String testCaseSheetNames;
    private String testCaseSheetName;
    private int newCommandTimeout;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }

    // andorid配置信息
    private String appDir;
    private boolean unicodeKeyboard;
    private boolean resetKeyboard;
    // android项目必填
    private String androidDeviceName;
    // android项目必填
    private String apkName;
    // android项目必填
    private String appPackage;
    // android项目必填
    private String mainActivity;
    private String platformVersion;



    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getElementSheet() {
        return elementSheet;
    }

    public void setElementSheet(String elementSheet) {
        this.elementSheet = elementSheet;
    }

    public String getChromePath() {
        return chromePath;
    }

    public void setChromePath(String chromePath) {
        this.chromePath = chromePath;
    }

    public String getIePath() {
        return iePath;
    }

    public void setIePath(String iePath) {
        this.iePath = iePath;
    }

    public String getFirefoxPath() {
        return firefoxPath;
    }

    public void setFirefoxPath(String firefoxPath) {
        this.firefoxPath = firefoxPath;
    }

    public String getBrowserType() {
        return browserType;
    }

    public void setBrowserType(String browserType) {
        this.browserType = browserType;
    }

    public String getProjectInfo() {
        return projectInfo;
    }

    public void setProjectInfo(String projectInfo) {
        this.projectInfo = projectInfo;
    }

    public String getTestSpecification() {
        return testSpecification;
    }

    public void setTestSpecification(String testSpecification) {
        this.testSpecification = testSpecification;
    }

    public String getTestDataExcelPath() {
        return testDataExcelPath;
    }

    public void setTestDataExcelPath(String testDataExcelPath) {
        this.testDataExcelPath = testDataExcelPath;
    }

    public boolean isIfScreen() {
        return ifScreen;
    }

    public void setIfScreen(boolean ifScreen) {
        this.ifScreen = ifScreen;
    }

    public int getReportCount() {
        return reportCount;
    }

    public void setReportCount(int reportCount) {
        this.reportCount = reportCount;
    }

    public String getTestReportDir() {
        return testReportDir;
    }

    public void setTestReportDir(String testReportDir) {
        this.testReportDir = testReportDir;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public String getTestCaseSheetNames() {
        return testCaseSheetNames;
    }

    public void setTestCaseSheetNames(String testCaseSheetNames) {
        this.testCaseSheetNames = testCaseSheetNames;
    }

    public String getAppDir() {
        return appDir;
    }

    public void setAppDir(String appDir) {
        this.appDir = appDir;
    }

    public boolean isUnicodeKeyboard() {
        return unicodeKeyboard;
    }

    public void setUnicodeKeyboard(boolean unicodeKeyboard) {
        this.unicodeKeyboard = unicodeKeyboard;
    }

    public boolean isResetKeyboard() {
        return resetKeyboard;
    }

    public void setResetKeyboard(boolean resetKeyboard) {
        this.resetKeyboard = resetKeyboard;
    }

    public String getAndroidDeviceName() {
        return androidDeviceName;
    }

    public void setAndroidDeviceName(String androidDeviceName) {
        this.androidDeviceName = androidDeviceName;
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public String getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(String mainActivity) {
        this.mainActivity = mainActivity;
    }

    public String getPlatformVersion() {
        return platformVersion;
    }

    public void setPlatformVersion(String platformVersion) {
        this.platformVersion = platformVersion;
    }

    public String getTestCaseSheetName() {
        return testCaseSheetName;
    }

    public void setTestCaseSheetName(String testCaseSheetName) {
        this.testCaseSheetName = testCaseSheetName;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public int getNewCommandTimeout() {
        return newCommandTimeout;
    }

    public void setNewCommandTimeout(int newCommandTimeout) {
        this.newCommandTimeout = newCommandTimeout;
    }
}
