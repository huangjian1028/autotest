package com.wangxun.autotest.ui.util;

import com.wangxun.autotest.ui.bean.ConfigBean;
import com.wangxun.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.dom4j.Document;
import org.dom4j.Element;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MakeTestData {
    private class InvalidTest { }
    private static Logger logger = Logger.getLogger(MakeTestData.class.getName());
    private static String midDirT;
    private static String charset="utf-8";

    public static HashMap<String ,String> getVar(HashMap<String, ArrayList<HashMap<String, String>>> testCaseData){
        HashMap<String ,String> result = new HashMap<>();
        for(Map.Entry<String, ArrayList<HashMap<String, String>>> entry: testCaseData.entrySet()) {
            String caseName = entry.getKey();
            ArrayList<HashMap<String, String>> caseData = entry.getValue();
            for (HashMap<String, String> row : caseData) {
                String action = row.get("Action");
                String page = row.get("Page");
                String element = row.get("Element");
                String value = row.get("Value");
                if ("setVar".contains(action)){
                    String[] data = value.split(",");
                    result.put(data[0],data[1]);
                }
            }
        }
        return result;
    }

    public static void makeTestCaseClass(String excelPath, String sheet,ConfigBean configBean,String classInfo,String destDir){
        if(!new File(destDir).exists()){
            FileUtil.makeDir(destDir);
        }
        String projectName = configBean.getProjectName();
        HashMap<String, ArrayList<HashMap<String, String>>> testCaseData = PoiReadTestCaseData.getTestCaseData(excelPath, sheet);
        String newLine = "    ";
        StringBuffer str = new StringBuffer();
        if ("Web".equals(configBean.getPlatform())){
            str.append("package com.wangxun.autotest.project.web."+projectName+".testcase;\r\n");
            str.append("import com.wangxun.autotest.project.web."+projectName+".frame."+projectName+";\r\n");
        }
        else if("Android".equals(configBean.getPlatform())){
            str.append("package com.wangxun.autotest.project.android."+projectName+".testcase;\r\n");
            str.append("import com.wangxun.autotest.project.android."+projectName+".frame."+projectName+";\r\n");
        }
        str.append("import com.wangxun.util.StringUtil;\r\n");
        str.append("import org.testng.annotations.AfterClass;\r\n");
        str.append("import org.testng.annotations.BeforeClass;\r\n");
        str.append("import org.testng.annotations.Listeners;\r\n");
        str.append("import org.testng.annotations.Test;\r\n");
        str.append("@Listeners\r\n");
        str.append("public class "+sheet+" {\r\n");
        str.append(newLine+projectName+" "+projectName+"= new "+projectName+"();\r\n");

        HashMap<String, String> vars = getVar(testCaseData);
        for (Map.Entry<String,String> entry:vars.entrySet()){
            String value = entry.getValue();
            if(value.contains("getRandomStr(")){
//                System.out.println(value);
                String method = getRegexValue(value,"\\$\\{(.*)\\}");
                str.append(newLine+"String "+entry.getKey()+"=StringUtil."+method+";\r\n");
            }
            else{
                str.append(newLine+"String "+entry.getKey()+"=\""+value+"\";\r\n");
            }

        }
        
//        str.append(newLine+"String test=StringUtil.getRandomStr(3)"+";\r\n");

        str.append(newLine+"@BeforeClass\r\n");
        str.append(newLine+"public void setUp(){\r\n");
        str.append(copyStr(newLine,2)+projectName+".initialTestData();\r\n");
        if("Chrome".equals(configBean.getBrowserType())){
            str.append(copyStr(newLine,2)+projectName+".runChromeApp();\r\n");
        }
        else if("IE".equals(configBean.getBrowserType())){
            str.append(copyStr(newLine,2)+projectName+".runIEApp();\r\n");
        }
        else if("Firefox".equals(configBean.getBrowserType())){
            str.append(copyStr(newLine,2)+projectName+".runFirefoxApp();\r\n");
        }
        if("Android".equals(configBean.getPlatform())){
            str.append(copyStr(newLine,2)+projectName+".runAndroidApp();\r\n");
        }

        str.append(copyStr(newLine,2)+projectName+".logClassInfo(\""+classInfo+"\");\r\n");
        str.append(newLine+"}\r\n");
        str.append(newLine+"@AfterClass\r\n");
        str.append(newLine+"public void tearDown() {\r\n");
        str.append(copyStr(newLine,2)+projectName+".quit();\r\n");
        str.append(newLine+"}\r\n");

        for(Map.Entry<String, ArrayList<HashMap<String, String>>> entry: testCaseData.entrySet()){
            String caseName = entry.getKey();
            ArrayList<HashMap<String, String>> caseData = entry.getValue();
//            logger.info(caseData);
            str.append(newLine+"@Test\r\n");
            str.append(newLine+"public void "+caseName+"(){\r\n");
            str.append(copyStr(newLine,2)+projectName+".logTestDescription(\""+caseData.get(0).get("Description")+"\");\r\n");
            for(HashMap<String,String> row:caseData){
                String action = row.get("Action");
                String page = row.get("Page");
                String element = row.get("Element");
                String value = row.get("Value");
                String[] values = null;
                if (!StringUtils.isEmpty(value)){
                    values = value.split(",");
                }

                if (action.equals("click")) {
                    str.append(copyStr(newLine,2)+projectName+".clickElement(\""+page+"\",\""+element+"\");\r\n");
                }
                else if (action.equals("clear")) {
                    str.append(copyStr(newLine,2)+projectName+".clear(\""+page+"\",\""+element+"\");\r\n");
                }
                else if (action.equals("sleep")) {
                    str.append(copyStr(newLine,2)+projectName+".sleep("+value+");\r\n");
                }
                else if (action.equals("get")) {
                    str.append(copyStr(newLine,2)+projectName+".get(\""+value+"\");\r\n");
                }
                else if (action.equals("waitDisplay")) {
                    str.append(copyStr(newLine,2)+projectName+".waitDisplay(\""+page+"\",\""+element+"\");\r\n");
                }
                else if (action.equals("waitDisplayAndClick")) {
                    str.append(copyStr(newLine,2)+projectName+".waitDisplayAndclickElement(\""+page+"\",\""+element+"\");\r\n");
                }
                else if (action.equals("acceptAlert")) {
                    str.append(copyStr(newLine,2)+projectName+".acceptAlert();\r\n");
                }
                else if (action.equals("getElementAttribute")) {
                    str.append(copyStr(newLine,2)+"String "+values[1]+"="+projectName+".getElementAttribute(\""+page+"\",\""+element
                            +"\",\""+values[0]+"\");\r\n");
                }
                else if (action.equals("getElementText")) {
                    str.append(copyStr(newLine,2)+"String "+value+"="+projectName+".getElementText(\""+page+"\",\""+element
                            +"\");\r\n");
                }
                else if (action.equals("getElementTextLen")) {
                    str.append(copyStr(newLine,2)+"String "+value+"="+projectName+".getElementTextLen(\""+page+"\",\""+element
                            +"\");\r\n");
                }
                else if (action.equals("sendKeys")) {
                    str.append(copyStr(newLine,2)+projectName+".sendKeys(\""+page+"\",\""+element+"\","+changeVarStr(value)+");\r\n");
                }
                else if (action.equals("assertContains")) {
                    if (!StringUtils.isEmpty(element)){
                        str.append(copyStr(newLine,2)+projectName+".assertContains("+""+projectName+".getElementText(\""+page+"\", \""+element+"\"),"+changeVarStr(value)+");\r\n");
                    }
                    else{
                        str.append(copyStr(newLine,2)+projectName+".assertContains("+changeVarStr(values[0])+","+changeVarStr(values[1])+");\r\n");
                    }
                }
                else if (action.equals("assertNotContains")) {
                    if (!StringUtils.isEmpty(element)){
                        str.append(copyStr(newLine,2)+projectName+".assertNotContains("+""+projectName+".getElementText(\""+page+"\", \""+element+"\"),"+changeVarStr(value)+");\r\n");
                    }
                    else{
                        str.append(copyStr(newLine,2)+projectName+".assertNotContains("+changeVarStr(values[0])+","+changeVarStr(values[1])+");\r\n");
                    }
                }

            }

//            str.append(copyStr(newLine,2)+projectName+".runTestCase(\""+caseName+"\");\r\n");
            str.append(copyStr(newLine,2)+projectName+".logSuccessMessage(\""+caseData.get(0).get("SuccessInfo")+"\");\r\n");
            str.append(newLine+"}\r\n");
        }

        str.append("}\r\n");
//        CommonMethod.writeFile(destDir+"/"+sheet+".java",str.toString(),false);
        FileUtil.writeFileWithCharset(destDir+"/"+sheet+".java",str.toString(),charset);
    }
    public static void makeMainClass(String projectName,String platForm,String destDir){
        if(!new File(destDir).exists()){
            FileUtil.makeDir(destDir);
        }
        String newLine = "    ";
        StringBuffer str = new StringBuffer();
        if ("Web".equals(platForm)){
            str.append("package com.wangxun.autotest.project.web."+projectName+".frame;\r\n");
            str.append("import com.wangxun.autotest.ui.base.WebApp;\r\n");
            str.append("import org.apache.log4j.PropertyConfigurator;\r\n");
            str.append("public class "+projectName+" extends WebApp {\r\n");
            str.append(newLine+"public void initialTestData(){\r\n");
            str.append(copyStr(newLine,2)+"PropertyConfigurator.configure(\""+midDirT+"/config/log4j.properties\");\r\n");
            str.append(copyStr(newLine,2)+"coreFilePath = \""+midDirT+"/projects/web/"+projectName+"/core.properties\";\r\n");
        }
        if("Android".equals(platForm)){
            str.append("package com.wangxun.autotest.project.android."+projectName+".frame;\r\n");
            str.append("import com.wangxun.autotest.ui.base.AndroidApp;\r\n");
            str.append("import org.apache.log4j.PropertyConfigurator;\r\n");
            str.append("public class "+projectName+" extends AndroidApp {\r\n");
            str.append(newLine+"public void initialTestData(){\r\n");
            str.append(copyStr(newLine,2)+"PropertyConfigurator.configure(\""+midDirT+"/config/log4j.properties\");\r\n");
            str.append(copyStr(newLine,2)+"coreFilePath = \""+midDirT+"/projects/android/"+projectName+"/core.properties\";\r\n");
        }

        str.append(copyStr(newLine,2)+"envName = \"testing\";\r\n");
        str.append(newLine+"}\r\n");
        str.append("}\r\n");
//        CommonMethod.writeFile(destDir+"/"+projectName+".java",str.toString(),false);
        FileUtil.writeFileWithCharset(destDir+"/"+projectName+".java",str.toString(),charset);
    }
    public static void makeProperties(ConfigBean configBean,String destDir){
        if(!new File(destDir).exists()){
            FileUtil.makeDir(destDir);
        }
        StringBuffer coreStr = new StringBuffer();
        StringBuffer configStr = new StringBuffer();


        //拼接core
        coreStr.append("testing = "+midDirT+"/projects/");
        if("Web".equals(configBean.getPlatform())){
            coreStr.append("web/");
        }
        if("Android".equals(configBean.getPlatform())){
            coreStr.append("android/");
        }
        coreStr.append(configBean.getProjectName());
        coreStr.append("/config.properties\r\n");

        //拼接config
        configStr.append("projectName = "+configBean.getProjectName()+"\r\n");
        configStr.append("projectInfo = "+configBean.getProjectInfo()+"\r\n");
        configStr.append("testSpecification = "+configBean.getTestSpecification()+"\r\n");
        configStr.append("testDataExcelPath = "+configBean.getTestDataExcelPath()+"\r\n");
        if(!configBean.getTestCaseSheetNames().contains(",")){
            configStr.append("testCaseSheetName = "+configBean.getTestCaseSheetNames()+"\r\n");
        }
        configStr.append("elementSheet = "+configBean.getElementSheet()+"\r\n");
        configStr.append("ifScreen = "+configBean.isIfScreen()+"\r\n");
        configStr.append("waitTime = "+10+"\r\n");
        configStr.append("testReportDir = "+configBean.getTestReportDir()+"\r\n");
//        configStr.append("reportCount = "+configBean.getReportCount()+"\r\n");
//        configStr.append("browserType = "+configBean.getBrowserType()+"\r\n");

//        logger.info(configBean.getPlatform());
        if ("Web".equals(configBean.getPlatform())){
            configStr.append("chromePath = "+configBean.getChromePath()+"\r\n");
            configStr.append("iePath = "+configBean.getIePath()+"\r\n");
            configStr.append("firefoxPath = "+configBean.getFirefoxPath()+"\r\n");
        }
        if ("Android".equals(configBean.getPlatform())){
            configStr.append("appDir = "+configBean.getAppDir()+"\r\n");
            configStr.append("apkName = "+configBean.getApkName()+"\r\n");
            configStr.append("androidDeviceName = "+configBean.getAndroidDeviceName()+"\r\n");
            configStr.append("appPackage = "+configBean.getAppPackage()+"\r\n");
            configStr.append("mainActivity = "+configBean.getMainActivity()+"\r\n");
            configStr.append("newCommandTimeout = "+configBean.getNewCommandTimeout()+"\r\n");

        }

        FileUtil.writeFileWithCharset(destDir+"/core.properties",coreStr.toString(),charset);
        FileUtil.writeFileWithCharset(destDir+"/config.properties",configStr.toString(),charset);
//        FileUtil.writeFile(destDir+"/core.properties",coreStr.toString(),false);
//        FileUtil.writeFile(destDir+"/config.properties",configStr.toString(),false);
    }

    public static HashMap<String, String> getSummary(String excelPath, String summarySheet){
        ArrayList<HashMap<String, String>> originData=null;
        HashMap<String, String> data = new HashMap<String, String>();
        try {
            originData = PoiUtil.readExcel(excelPath, summarySheet);
//            logger.info(originData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(HashMap<String,String> row:originData){
            if(!StringUtils.isEmpty(row.get("name"))){
                data.put(row.get("name"),row.get("value"));
            }
        }
        return data;
    }

    public static ConfigBean getConfigBean(String excelPath, String summarySheet){
        HashMap<String, String> data=null;
        data = getSummary(excelPath, summarySheet);
//        logger.info(data);
        ConfigBean configBean = JsonUtil.mapToJavaBean(data, ConfigBean.class);
        String abExcelPath = new File(excelPath).getAbsolutePath().replace("\\","/");
        configBean.setTestDataExcelPath(abExcelPath);

//        logger.info(configBean);
        return configBean;
    }

    public static String copyStr(String str,int nun){
        String data="";
        for(int i=0;i<nun;i++){
            data+=str;
        }
        return data;
    }
    public static void make(String excelPath, String summarySheet){
        String workSpace = System.getProperty("user.dir");
        make(excelPath,summarySheet,workSpace,midDirT);

    }

    public static void make(String excelPath, String summarySheet,String workSpace,String midDir){
        midDirT = midDir;
        ConfigBean configBean = getConfigBean(excelPath, summarySheet);
        String projectName=configBean.getProjectName();
        String platform = configBean.getPlatform();
        String testcaseSheetNames = configBean.getTestCaseSheetNames();
        String[] caseSheetList = testcaseSheetNames.split(",");
        String propertiesDir=null;
        String mainClassDir=null;
        String testClassDir=null;
        String testngDir=workSpace+"/src/main/resources/bin";
        if("Web".equals(platform)){
            propertiesDir = workSpace+"/src/main/resources/projects/web/"+projectName;
            mainClassDir = workSpace+"/src/main/java/com/wangxun/autotest/project/web/"+projectName+"/frame";
            testClassDir = workSpace+"/src/main/java/com/wangxun/autotest/project/web/"+projectName+"/testcase";
        }
        if("Android".equals(platform)){
            propertiesDir = workSpace+"/src/main/resources/projects/android/"+projectName;
            mainClassDir = workSpace+"/src/main/java/com/wangxun/autotest/project/android/"+projectName+"/frame";
            testClassDir = workSpace+"/src/main/java/com/wangxun/autotest/project/android/"+projectName+"/testcase";
        }
        logger.info("propertiesDir:"+propertiesDir);
        logger.info("mainClassDir:"+mainClassDir);
        logger.info("testClassDir:"+testClassDir);
        logger.info("testngDir:"+testngDir);
        logger.info("projectName:"+projectName);
        logger.info("platform:"+platform);
        logger.info("testcaseSheetNames:"+testcaseSheetNames);

        logger.info("configBean:"+configBean);
        makeProperties(configBean,propertiesDir);
        makeMainClass(projectName,platform,mainClassDir);
        for(String caseSheet:caseSheetList){
            String classInfo = PoiReadTestCaseData.getTestClass(excelPath,caseSheet);
            System.out.println("classInfo:"+classInfo);
            makeTestCaseClass(excelPath,caseSheet,configBean,classInfo,testClassDir);
        }

        makeTestngFile(excelPath,summarySheet,testngDir);

    }

    public static void makeTestngFile(String excelPath, String summarySheet,String destDir){
        if(!new File(destDir).exists()){
            FileUtil.makeDir(destDir);
        }
        ConfigBean configBean = getConfigBean(excelPath,summarySheet);
        Document doc = XmlUtil.createDocument();
        Element suite = doc.addElement("suite");
        XmlUtil.writeEle(suite,XmlUtil.getMap("name","Default suite"));
        Element test = suite.addElement("test");
        XmlUtil.writeEle(test,XmlUtil.getMap("name,verbose","Default suite,2"));
        Element classes = test.addElement("classes");
        String[] caseSheets = configBean.getTestCaseSheetNames().split(",");
        for(String caseSheet:caseSheets){
            HashMap<String, ArrayList<HashMap<String, String>>> testCaseData = PoiReadTestCaseData.getTestCaseData(excelPath,caseSheet);
            Element classEle = classes.addElement("class");
            if("Web".equals(configBean.getPlatform())){
                XmlUtil.writeEle(classEle,XmlUtil.getMap("name","com.wangxun.autotest.project.web."+configBean.getProjectName()
                        +".testcase."+caseSheet+""));
            }
            if("Android".equals(configBean.getPlatform())){
                XmlUtil.writeEle(classEle,XmlUtil.getMap("name","com.wangxun.autotest.project.android."+configBean.getProjectName()
                        +".testcase."+caseSheet+""));
            }

            Element methods = classEle.addElement("methods");
            for(Map.Entry<String, ArrayList<HashMap<String, String>>> entry:testCaseData.entrySet()){
                String methodName = entry.getKey();
                String execute = entry.getValue().get(0).get("Execute");
                ArrayList<HashMap<String, String>> value = entry.getValue();
                if (execute.equals("Y")){
                    Element include = methods.addElement("include");
                    XmlUtil.writeEle(include,XmlUtil.getMap("name",methodName));
                }
            }
        }
        

        Element listeners = test.addElement("listeners");
        Element listener = listeners.addElement("listener");
        XmlUtil.writeEle(listener,XmlUtil.getMap("class-name","com.wangxun.autotest.ui.util.TestngListener"));
        try {
            XmlUtil.writeDocument(doc,destDir+"/"+"testng-auto.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getRegexValue(String str,String regex){
        String data="";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        if (m.find()) {
            data = m.group(1);
        }
//        System.out.println(data);
        return data.trim();
    }
    public static ArrayList<String> getValue(String str){
        ArrayList<String> list = new ArrayList<>();
        String first = "";
        String var = getRegexValue(str,"\\$\\{(.*)\\}");
        String sencond = "";
        String[] firstList = str.split("\\$");
        String[] twoList = str.split("}");
        if (firstList.length>0){
            first = firstList[0];
        }
        if (twoList.length>1){
            sencond = twoList[twoList.length-1];
        }
        list.add(first);
        list.add(var);
        list.add(sencond);
//        System.out.println(list);
        return list;
    }
    
    public static String changeVarStr(String str){
        ArrayList<String> list = getValue(str);
        StringBuffer data = new StringBuffer();
        if(!StringUtils.isEmpty(list.get(0))){
            if (!StringUtils.isEmpty(list.get(1))){
                data.append("\""+list.get(0)+"\"+");
            }
            else{
                data.append("\""+list.get(0)+"\"");
            }

        }
        if(!StringUtils.isEmpty(list.get(1))){
            if(!StringUtils.isEmpty(list.get(2))){
                data.append(list.get(1)+"+");
            }
            else{
                data.append(list.get(1));
            }
        }
        if(!StringUtils.isEmpty(list.get(2))){
            data.append("\""+list.get(2)+"\"");
        }
        if (StringUtils.isEmpty(data.toString())){
            return "\"\"";
        }
        return data.toString();
    }


    public static void main(String[] args) {
//        PropertyConfigurator.configure("src/main/resources/config/log4j.properties");
//        String testDataExcelPath="D:\\日志平台\\日志平台.xls";
//        String summarySheet="testsummary";
//        String workSpace = System.getProperty("user.dir");
        PropertyConfigurator.configure("../config/log4j.properties");
        String testDataExcelPath=args[0];
        String summarySheet=args[1];
        String workSpace = args[2];
        System.out.println("testDataExcelPath:"+testDataExcelPath);
        System.out.println("summarySheet:"+summarySheet);
        System.out.println("workSpace:"+workSpace);


        make(testDataExcelPath,summarySheet,workSpace,"..");

//        System.out.println(getRegexValue("${getRandomStr(3)}","\\$\\{\\}"));
//        System.out.println(changeVarStr("${tttt}"));


//        String testCaseSheet="case_12306";
//
//        String testClassDir="D:/";
//        String projectName = "WebDemo1";
//        String classInfo="3333";
//
//        String platForm="web";
//        String mainClassDir="D:/";
//        String propertiesDir="D:/";

//        makeTestCaseClass(testDataExcelPath,testCaseSheet,projectName,classInfo,testClassDir);
//        makeMainClass(projectName,platForm,mainClassDir);
//        makeProperties(testDataExcelPath,summarySheet,propertiesDir);

//        make(testDataExcelPath,summarySheet,workSpace,"src/main/resources");
//        makeTestngFile("D:/");
//        makeTestngFile(testDataExcelPath,summarySheet,propertiesDir);
    }
}
