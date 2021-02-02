package com.wangxun.example;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class common {

    //遍历map方法1
    public static void traversalMap1(HashMap<String ,String> map){
        for (String key:map.keySet()){
            System.out.println("key:"+key);
            System.out.println("value:"+map.get(key));
        }
    }
    //遍历map方法2，建议用此方法
    public static void traversalMap2(HashMap<String ,String> map){
        for (Entry<String,String> entry:map.entrySet()){
            System.out.println("key:"+entry.getKey());
            System.out.println("value:"+entry.getValue());
        }
    }

    //遍历map方法3
    public static void traversalMap3(HashMap<String ,String> map){
        Iterator<Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, String> entry = it.next();
            System.out.println("key:"+entry.getKey());
            System.out.println("value:"+entry.getValue());
        }
    }

    public static void traversalList1(ArrayList<String> list){
        for(String value:list){
            System.out.println("value:"+value);
        }
    }
    public static void traversalList2(ArrayList<String> list){
        for(int i=0;i<list.size();i++){
            System.out.println("value:"+list.get(i));
        }
    }
    public static void traversalList3(ArrayList<String> list){
        Iterator<String> it = list.iterator();
        while(it.hasNext()){
            String value = it.next();
            System.out.println("value:"+value);
        }
    }

    /**
    * @Description: 把json字符串转换成json对象
    * @Param [json]
    * @return void
    * @Author wangxun 
    * @Date 2018/12/25 14:23
    */
    public static void traversalJson(String json){
        Gson gson = new Gson();
        JsonObject jsonObject = (JsonObject) new JsonParser().parse(json);
        for(Entry<String, JsonElement> entry:jsonObject.entrySet()){
            System.out.println("key:"+entry.getKey());
            System.out.println("value:"+entry.getValue());
        }
    }

    /**
    * @Description: 读文件，可以指定编码格式
    * @Param [filePath, character]
    * @return java.lang.String
    * @Author wangxun
    * @Date 2018/12/25 14:44
    */
    public static String readFile(String filePath, String character) {
        File file = new File(filePath);
        StringBuilder result = new StringBuilder();
        BufferedReader reader = null;
        try {

            if (StringUtils.isEmpty(character)){
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            }
            else{
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), character));
            }

            String tempString = null;
            // System.out.println(tempString);
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                result.append(tempString + System.lineSeparator());
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return result.toString();
    }

    public static void writeFile(String filePath, String text,boolean append) {
        try {

            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWritter = new FileWriter(filePath,append);
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
            bufferWritter.write(text);
            bufferWritter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * @Description: 读文件，但是不能指定编码格式
    * @Param [filePath]
    * @return java.lang.String
    * @Author wangxun 
    * @Date 2018/12/25 14:43
    */
    public  static String readFile2(String filePath){
        FileReader fileReader = null;
        StringBuilder result = new StringBuilder();
        try {
            fileReader = new FileReader(filePath);
            BufferedReader bufferedReader= new BufferedReader(fileReader);
            String tempString = null;
            // System.out.println(tempString);
            while ((tempString = bufferedReader.readLine()) != null) {
                // 显示行号
                result.append(tempString + System.lineSeparator());
            }
            fileReader.close();
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();


    }

    public static void main(String[] args) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("key1","value1");
        map.put("key2","value2");
        System.out.println("执行遍历map方法1");
        traversalMap1(map);
        System.out.println("执行遍历map方法2");
        traversalMap2(map);
        System.out.println("执行遍历map方法3");
        traversalMap3(map);

        ArrayList<String> list = new ArrayList<String>();
        list.add("value1");
        list.add("value2");
        System.out.println("执行遍历list方法1");
        traversalList1(list);
        System.out.println("执行遍历list方法2");
        traversalList2(list);
        System.out.println("执行遍历list方法3");
        traversalList3(list);



        System.out.println("执行遍历json方法");
        String json="{\"key1\":\"value1\",\"key2\":\"value2\"}";
        traversalJson(json);
        System.out.println("执行读文件方法");
        String file="E:/test.txt";
        System.out.println(file);
        System.out.println(readFile(file,"gbk"));


    }
}
