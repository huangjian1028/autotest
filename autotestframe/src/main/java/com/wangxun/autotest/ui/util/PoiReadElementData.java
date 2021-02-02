package com.wangxun.autotest.ui.util;

import com.wangxun.autotest.ui.core.Initial;
import com.wangxun.util.PoiUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class PoiReadElementData {
    private class InvalidTest { }
    private static Logger logger = Logger.getLogger(Initial.class.getName());
    public static ArrayList<HashMap<String, String>> getData(String excelPath, String sheet){
        ArrayList<HashMap<String, String>> data = null;
        try {
            data=PoiUtil.readExcel(excelPath,sheet,0);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        logger.info("data"+data);
        return data;
    }
    public static HashMap<String, HashMap<String,HashMap<String, String>>> getElementData(String excelPath, String sheet){
        ArrayList<HashMap<String, String>> originData = getData(excelPath, sheet);
        HashMap<String, HashMap<String,HashMap<String, String>>> data = new HashMap<>();
        HashMap<String,HashMap<String, String>> map=null;

        String truePage = "";
        for(int i=0;i<originData.size();i++){
            HashMap<String, String> rowData = originData.get(i);
            if (!StringUtils.isEmpty(rowData.get("Element Name"))){
                rowData.put("row",String.valueOf(i+2));
                if(i==0){
                    truePage=rowData.get("Page");
                }
                String page=rowData.get("Page");
                String elementName = rowData.get("Element Name");
//            logger.info(rowData);

                if(!StringUtils.isEmpty(page)){
                    if(i==0){
                        map = new HashMap<>();
                        data.put(page,map);
                        map.put(elementName,rowData);
                        truePage=page;
                    }
                    else{
                        if (!page.equals(truePage)){
                            map = new HashMap<>();
                            data.put(page,map);
                            map.put(elementName,rowData);
                            truePage=page;
                        }
                        else{
                            map.put(elementName,rowData);
                        }
                    }

                }
                else{
                    map.put(elementName,rowData);
                }
            }
        }
//        logger.info("data"+data);
        return data;
        
    }

    public static void main(String[] args) {
        PropertyConfigurator.configure("src/main/resources/config/log4j.properties");
        String testDataExcelPath="src/main/resources/projects/web/demo/WebAppDemo.xls";
        String testCaseSheet="12306_elements";
        getElementData(testDataExcelPath,testCaseSheet);
    }
}
