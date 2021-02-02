package com.wangxun.autotest.ui.util;

import java.io.*;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class FileOperate {
	private class InvalidTest { }
	static final int BUFFER = 8192;

	private static File zipFile;

	public static void copyFolder(String oldPath, String newPath) {

		try {
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}

				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath
							+ "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子文件夹
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			System.out.println("复制整个文件夹内容操作出错");
			e.printStackTrace();

		}

	}

	public static boolean createDir(String destDirName) {
		File dir = new File(destDirName);
		if (dir.exists()) {
			System.out.println("创建目录" + destDirName + "失败，目标目录已经存在");
			return false;
		}

		if (!destDirName.endsWith(File.separator)) {
			destDirName = destDirName + File.separator;
		}
		// 创建目录
		if (dir.mkdirs()) {
			System.out.println("创建目录" + destDirName + "成功！");
			return true;
		} else {
			System.out.println("创建目录" + destDirName + "失败！");
			return false;
		}
	}

	public static String getFileName(String filePath) {
		String[] list = filePath.split("/");
		return list[list.length - 1];
	}

	public static void copyFile(String oldPath, String newPath) {
		int byteread = 0;
		try {
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				fs.close();
				System.out.println("复制" + oldPath + "文件到" + newPath + "成功");
			}
		} catch (Exception e) {
			System.out.println("复制" + oldPath + "文件到" + newPath + "出错");
			e.printStackTrace();
		}
	}

	public static String getCurrentPath() {
		return System.getProperty("user.dir").replace("\\", "/");
	}


	public static void compress(String zipFilePath, String... pathName) {
		zipFile = new File(zipFilePath);
		ZipOutputStream out = null;
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
			CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,
					new CRC32());
			out = new ZipOutputStream(cos);
			String basedir = "";
			for (int i = 0; i < pathName.length; i++) {
				compress(new File(pathName[i]), out, basedir);
			}
			out.close();
			System.out.println("压缩成功");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void compress(String zipFilePath, String srcPathName) {
		zipFile = new File(zipFilePath);
		File file = new File(srcPathName);
		if (!file.exists())
			throw new RuntimeException(srcPathName + "不存在！");
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
			CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,
					new CRC32());
			ZipOutputStream out = new ZipOutputStream(cos);
			String basedir = "";
			compress(file, out, basedir);
			out.close();
			System.out.println("压缩成功");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void compress(File file, ZipOutputStream out, String basedir) {
		/* 判断是目录还是文件 */
		if (file.isDirectory()) {
			// System.out.println("压缩：" + basedir + file.getName());
			compressDirectory(file, out, basedir);
		} else {
			// System.out.println("压缩：" + basedir + file.getName());
			compressFile(file, out, basedir);
		}
	}

	/** 压缩一个目录 */
	private static void compressDirectory(File dir, ZipOutputStream out,
			String basedir) {
		if (!dir.exists())
			return;

		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			/* 递归 */
			compress(files[i], out, basedir + dir.getName() + "/");
		}
	}

	/** 压缩一个文件 */
	private static void compressFile(File file, ZipOutputStream out,
			String basedir) {
		if (!file.exists()) {
			return;
		}
		try {
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(file));
			ZipEntry entry = new ZipEntry(basedir + file.getName());
			out.putNextEntry(entry);
			int count;
			byte data[] = new byte[BUFFER];
			while ((count = bis.read(data, 0, BUFFER)) != -1) {
				out.write(data, 0, count);
			}
			bis.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String setPath(String dirname) {
		String currentPath = System.getProperty("user.dir");
		if (dirname.contains(":") ||dirname.substring(0, 1).contains("/")){
			return dirname;
		}
		return currentPath + "/" +dirname;
	}
	
	public static void deleteFile(String filePath) {
		File f = new File(filePath);
		deleteFile(f);
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
	
	public static int getFileCount(String dirPath) {
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		int count = files.length;
		return count;
	}

	public static void keepFileCount(String dirPath, int count) {
		if (!(new File(dirPath).isDirectory())) { // 判断是否存在该目录
			new File(dirPath).mkdir(); // 如果不存在则新建一个目录
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

	
	public static void main(String[] args) {
/*		FileOperate.copyFolder("D:/learning/svn/workplace/ATF_CustomReport"
				+ "/resources/icon", "D:/learning/svn/workplace/ATF_CustomReport"
				+ "/" + "report");*/
//		deleteFile("D:/test.xls");
		int a = getFileCount("testReport/WebTestDemo_web");
		System.out.println(a);
		// FileOperate.getFileName("D:/testss.zip");
		// FileOperate.copyFile("D:/testss.zip", "D:/testss11.zip");
		// FileOperate.compress("D:/testss.zip","D:/learning/svn/workplace/ATF_CustomReport");
		// FileOperate.compress("D:/testss.zip","D:/learning/svn/workplace/ATF_CustomReport");
		// zc.compress("D:/learning/svn/workplace/ATF_CustomReport/logs","D:/learning/svn/workplace/ATF_CustomReport/ATF_Report__test_scripts_ATF_Slider_lua2016-05-31.xls");
	}
}
