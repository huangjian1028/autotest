package com.wangxun.autotest.ui.util;

import com.wangxun.autotest.ui.core.Initial;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonTools {
	private class InvalidTest { }
	static Properties PROPERTIES = new Properties(System.getProperties());
	static String regEx = "[\u4e00-\u9fa5]";
	static Pattern pat = Pattern.compile(regEx);

	public static String getOSName() {
		return PROPERTIES.getProperty("os.name");
	}

	public static String getCurrentTime() {
		Date date = new Date();
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd-HHmmss-SSS");
		return f.format(date);
	}

	public static String getCurrentTime1() {
		Date date = new Date();
		SimpleDateFormat f = new SimpleDateFormat("MMdd-HHmmss");
		return f.format(date);
	}


	public static void deleteFile(String filePath) {
		File f = new File(filePath);
		deleteFile(f);
	}

	public static void log(Object content, Integer type) {

		switch (type) {
		case 1: {
			System.out.println(getCurrentTime() + " INFO - " + content);
			break;
		}
		case 2: {
			System.err.println(getCurrentTime() + " ERROR - " + content);
			break;
		}
		case 3: {
			System.out.println(getCurrentTime() + " WARNING - " + content);
			break;
		}
		case 4: {
			System.err.println(getCurrentTime() + " WARNING - " + content);
			break;
		}
		}

	}

	public static void log(Object content) {

		log(content, 1);
	}

	public static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static Properties getConfigFormatData(String configFileName) {
		try {
			Properties pro = new Properties();
			FileInputStream fis = new FileInputStream(configFileName);
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader brProp = new BufferedReader(isr);
			pro.load(brProp);
			brProp.close();
			return pro;
		} catch (Exception e) {
			throw new IllegalStateException("Can't locate config file "
					+ configFileName, e);
		}
	}

	public static String getProperties(String dirNamePath, String name) {



		Properties properties = getConfigFormatData(dirNamePath);
		return properties.getProperty(name);

	}

	public static String getPercent(int numerator, int denominator) {
		float numerator1 = numerator;
		float successRate = numerator1 / denominator;
		DecimalFormat df = new DecimalFormat("0.00%");
		String successRate1 = df.format(successRate);
		return successRate1;
	}

	public static int getFileCount(String dirPath) {
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		int count = files.length;
		return count;
	}

	public static void keepFileCount(String dirPath, int count) {
		if (!(new File(dirPath).isDirectory())) { // 判断是否存在该目录
			FileOperate.createDir(dirPath); // 如果不存在则新建一个目录
		}
		if (Initial.runTimes == 1){
			count = count-1;
		}
		int actualCount = getFileCount(dirPath);
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		int j = 0;
		for(int i = 0; i <files.length; i++){
			if (j<actualCount - count){
				deleteFile(files[i]);
				j = j+1;
				
			}			
		}
	}


	public static void deleteFile(File file) {
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法 你应该知道 是删除的意思;
			} else if (file.isDirectory()) { // 否则如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					deleteFile(files[i]);
				}
			}
			file.delete();
		} else {
			System.out.println("所删除的文件不存在！" + '\n');
		}
	}


	
	public static boolean isIn(String substring, String[] source) {
		if (source == null || source.length == 0) {
			return false;
		}
		for (int i = 0; i < source.length; i++) {
			String aSource = source[i];
			if (aSource.equals(substring)) {
				return true;
			}
		}
		return false;
		}

	public static String getValidSheetName(String sheetName) {
		if (sheetName.length() > 31) {
			return sheetName.substring(sheetName.length() - 31,
					sheetName.length());
		}
		return sheetName;
	}



	public static String getConfigValue(Properties configProperties,
			String propertieName) {

		// if the specified propertieName exist as an environment variable.
		// if so , use it. otherwise , search the configJsonNode specified.
		String returnValue = StringUtils.defaultString(System
				.getenv(propertieName));
		if (StringUtils.isBlank(returnValue)) {
			try {
				returnValue = configProperties.getProperty(propertieName);
			} catch (NullPointerException e) {
				System.err.println("Cannot locate config file at "
						+ propertieName + ". Will try continue without it.");
				return "";
			}
		}
		return returnValue;
	}



	public static String getStr(int num, String str) {
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < num; i++) {
			sb.append(str);
		}
		return sb.toString();
	}
	
	public static boolean isContainsChinese(String str){
		
		Matcher matcher = pat.matcher(str);
		boolean flg = false;
		if (matcher.find())	{
			flg = true;
		}
		return flg;
	}
	
	public static String getEnv(String key){
		String value = System.getenv(key);
		Map<String, String> vv = System.getenv();
		Properties ff = System.getProperties();
		System.out.println(vv);
		System.out.println(ff);
		System.out.println(value);
		return value;
	}
	
	public static void setEnvProperty(String key,String value){
		System.setProperty(key, value);
	}
	
	public static String getStringByMultiply(String src,int num){
		String des = "";
		for (int i=0;i<num; i++){
			des = des + src;
		}
		return des;
	}

	public static void main(String[] args) throws RowsExceededException,
			BiffException, WriteException, IOException {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String data = df.format(new Date());
		System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
		System.out.println(data.substring(8, 10));

		System.out.println("end");
		String a = "343: 4";
		System.out.println(isContainsChinese(a));
		System.out.println("end");
		setEnvProperty("tt", "33");
		getEnv("autoTestReportName");
	}

}
