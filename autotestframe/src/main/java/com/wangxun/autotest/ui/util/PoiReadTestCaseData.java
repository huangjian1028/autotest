package com.wangxun.autotest.ui.util;

import com.wangxun.util.PoiUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class PoiReadTestCaseData {
    private class InvalidTest { }
    private static Logger logger = Logger.getLogger(PoiReadTestCaseData.class.getName());
    public static ArrayList<HashMap<String, String>> getData(String excelPath, String sheet){
        ArrayList<HashMap<String, String>> data = null;
        try {
            data=PoiUtil.readExcel(excelPath,sheet,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        logger.info("data"+data);
        return data;
    }
    public static HashMap<String, ArrayList<HashMap<String, String>>> getTestCaseData(String excelPath, String sheet){
        ArrayList<HashMap<String, String>> originData = getData(excelPath, sheet);
        HashMap<String, ArrayList<HashMap<String, String>>> data = new LinkedHashMap<String, ArrayList<HashMap<String, String>>>();
        ArrayList<HashMap<String, String>> list=null;

        String trueTestCase = "";
        for(int i=0;i<originData.size();i++){
            HashMap<String, String> rowData = originData.get(i);
            if(!StringUtils.isEmpty(rowData.get("Action"))){
                rowData.put("row",String.valueOf(i+3));
                if(i==0){
                    trueTestCase=rowData.get("TestCase");
                }
                String testCase=rowData.get("TestCase");
//            logger.info(rowData);

                if(!StringUtils.isEmpty(testCase)){
                    if(i==0){
                        list = new ArrayList<HashMap<String, String>>();
                        data.put(testCase,list);
                        list.add(rowData);
                        trueTestCase=testCase;
                    }
                    else{
                        if (!testCase.equals(trueTestCase)){
                            list = new ArrayList<HashMap<String, String>>();
                            data.put(testCase,list);
                            list.add(rowData);
                            trueTestCase=testCase;
                        }
                        else{
                            list.add(rowData);
                        }
                    }

                }
                else{
                    list.add(rowData);
                }
            }
        }
//        logger.info("data"+data);
        return data;
        
    }

    public static String getTestClass(String excelPath, String sheet){
        String data=null;
        try {
            PoiUtil.getSheetToEdit(excelPath,sheet);
            Sheet editSheet = PoiUtil.getCurrentSheet();
            data= editSheet.getRow(0).getCell(1).getStringCellValue();
            PoiUtil.closeWithoutSave();

        } catch (IOException e) {
            e.printStackTrace();
        }
        String aa = null;
        try {
            //解决cmd执行命令是出现中文乱码
            aa = new String(data.getBytes(), "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return data;

    }

    public static void main(String[] args) {
        PropertyConfigurator.configure("src/main/resources/config/log4j.properties");
        String testDataExcelPath="src/main/resources/projects/web/demo/WebAppDemo.xls";
        String testCaseSheet="case_12306";
        getTestCaseData(testDataExcelPath,testCaseSheet);
    }
}
