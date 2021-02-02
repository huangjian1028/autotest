package com.wangxun.autotest.ui.util;

import com.wangxun.util.StringUtil;
import jxl.format.Colour;
import jxl.read.biff.BiffException;
import jxl.write.WritableCellFormat;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CreateReport {
	private class InvalidTest { }
	public static void writeTestSummaryToExcel(String path,String sheetName,List<Map<String, String>> classData)
			throws RowsExceededException, WriteException, BiffException, IOException {
//        System.out.println("开始生成测试报告");
//        System.out.println(path);
//        System.out.println(sheetName);
//        System.out.println(classData);
		if (classData.size() != 0) {
			WritableCellFormat bodyFormat = JXLTool.getFormat(10, false);
			JXLTool.openSheet(path, sheetName);
			int startRow = JXLTool.getRows();
			int methodsCounts = classData.size();
			int successCount = 0;
			int failureCount = 0;
			int skippedCount = 0;
			String totalTimeString = JXLTool.getContents(4, 4);
			String[] aa = totalTimeString.split("s");
			float TotalTime = Float.parseFloat(aa[0]);
			for (int i = 0; i < classData.size(); i++) {
				String className = classData.get(i).get("className");
				String method = classData.get(i).get("method");
				String time = classData.get(i).get("time");
				String status = classData.get(i).get("status");
				String comment = classData.get(i).get("comment");
				String screenPath = classData.get(i).get("screenPath");
				String abScreenPath = classData.get(i).get("abScreenPath");
				String caseInfo = classData.get(i).get("caseInfo");
				String classInfo = classData.get(i).get("classInfo");
				float time1 = Float.parseFloat(time) / 1000;
				TotalTime = TotalTime + Float.parseFloat(time) / 1000;
				time = String.valueOf(time1) + "s";
				if (i == 0) {
					JXLTool.writeLastRow(0, className,bodyFormat);
					if (classInfo != null) {
						JXLTool.writeSameRow(1, classInfo,bodyFormat);
					} else {
						JXLTool.writeSameRow(1, "",bodyFormat);
					}

					JXLTool.writeSameRow(3, "",bodyFormat);
					JXLTool.setHyperLinkForSheet(3, startRow, className + "-log", className, 0, 1);
				} else {
					JXLTool.writeLastRow(0, "",bodyFormat);
				}

				JXLTool.writeSameRow(5, method,bodyFormat);
				JXLTool.writeSameRow(7, time,bodyFormat);
				if (caseInfo != null) {
					JXLTool.writeSameRow(6, caseInfo,bodyFormat);
				} else {
					JXLTool.writeSameRow(6, "",bodyFormat);
				}

				if (screenPath != null) {
					JXLTool.setHyperLinkByFormu(8, JXLTool.getRows() - 1, abScreenPath, method);
				} else {
					JXLTool.writeSameRow(8, "",bodyFormat);
				}
				JXLTool.writeSameRow(10, comment,bodyFormat);
				if (status.equals("Success")) {
					successCount = successCount + 1;
					JXLTool.writeData(9, JXLTool.getRows() - 1, status,JXLTool.getFormat(10, false, true,Colour.GREEN));
				}
				if (status.equals("Failure")) {
					failureCount = failureCount + 1;
					JXLTool.writeData(9, JXLTool.getRows() - 1, status,JXLTool.getFormat(10, false, true, Colour.RED));
				}
				if (status.equals("Skipped")) {
					skippedCount = skippedCount + 1;
					JXLTool.writeData(9, JXLTool.getRows() - 1, status,JXLTool.getFormat(10, false, true, Colour.YELLOW));
				}
			}
			String classSuccessRate = CommonTools.getPercent(successCount, methodsCounts);
			JXLTool.writeData(2, startRow, classSuccessRate,bodyFormat);
			JXLTool.writeData(4, startRow, String.valueOf(methodsCounts),bodyFormat);
			int endRow = JXLTool.getRows();
			JXLTool.mergeCells(0, startRow, 0, endRow - 1);
			JXLTool.mergeCells(1, startRow, 1, endRow - 1);
			JXLTool.mergeCells(2, startRow, 2, endRow - 1);
			JXLTool.mergeCells(3, startRow, 3, endRow - 1);
			JXLTool.mergeCells(4, startRow, 4, endRow - 1);
			int oldSuccessCount = Integer.parseInt(JXLTool.getContents(0, 4));
			int oldFailureCount = Integer.parseInt(JXLTool.getContents(1, 4));
			int oldSkippedCount = Integer.parseInt(JXLTool.getContents(2, 4));

			int newSuccessCount = successCount + oldSuccessCount;
			int newFailureCount = failureCount + oldFailureCount;
			int newSiippedCount = skippedCount + oldSkippedCount;
			int total = newSuccessCount + newFailureCount + newSiippedCount;
			JXLTool.writeData(0, 4, String.valueOf(newSuccessCount),bodyFormat);
			JXLTool.writeData(1, 4, String.valueOf(newFailureCount),bodyFormat);
			JXLTool.writeData(2, 4, String.valueOf(newSiippedCount),bodyFormat);
			String allPercent = CommonTools.getPercent(newSuccessCount, total);
			JXLTool.writeData(3, 4, allPercent,bodyFormat);
			JXLTool.writeData(4, 4, String.format("%.3f", TotalTime) + "s",bodyFormat);
			JXLTool.writeData(5, 4, String.valueOf(total),bodyFormat);
			JXLTool.close();
		} else {
			System.out.println("Please run the test case in listener mode.");
		}
	}
	
	public static void writeLogToExcel(String path,String sheetName,List<List<String>> logData, String testSummarySheetName)
    {
//        System.out.println(path);
//        System.out.println(sheetName);
//        System.out.println(logData);
//        System.out.println(testSummarySheetName);
		WritableCellFormat bodyFormat = JXLTool.getFormat(10, false,true);
		WritableCellFormat titleFormat = JXLTool.getFormat(12, true,true);

		JXLTool.openSheet(path, sheetName);

		JXLTool.setHyperLinkForSheet(0, 0, "Back",
				testSummarySheetName, 0, 0);

		JXLTool.setColumnView(1, 150);

		JXLTool.setColumnView(0, 40);

		JXLTool.setVerticalFreeze(1);

		int currentRow = 1;
		List<Integer> firstCol = new ArrayList<Integer>();
//        System.out.println(logData.size());
		for (int i = 0; i < logData.size(); i++) {
			String title = logData.get(i).get(0);
			String content = logData.get(i).get(1);
			if (content==null){
			    content="";
            }
			if (content.length()>32700){
				content = content.substring(0, 32700);
			}
			if (title == "") {
				JXLTool.writeLastRow(0, title,bodyFormat);
				JXLTool.writeSameRow(1, content,bodyFormat);
			} else {
				currentRow = JXLTool.getRows();
				firstCol.add(currentRow);
				JXLTool.writeLastRow(0, title,titleFormat);
				JXLTool.writeSameRow(1, content,titleFormat);
			}
			//sheet.mergeCells(1, sheet.getRows()-1, 3, sheet.getRows()-1);
		}

		firstCol.add(JXLTool.getRows());
		if (logData.size()>0){
			if (logData.get(0).get(0) == "ClassName") {
				for (int i = 0; i < firstCol.size() - 3; i++) {
					int endRow = firstCol.get(i + 3) - 1;
					int startRow = firstCol.get(i + 2);
					if (endRow > startRow) {
						JXLTool.mergeCells(0, startRow, 0, endRow);
					}
				}

			} else {
				for (int i = 0; i < firstCol.size() - 1; i++) {
					int endRow = firstCol.get(i + 1) - 1;
					int startRow = firstCol.get(i);
					if (endRow > startRow) {
						JXLTool.mergeCells(0, startRow, 0, endRow);
					}
				}
			}
		}
		JXLTool.close();
	}
	
	public static void createReportExcel(String reportDir, String testReportName, String sheetName, String projectName,String projectInfo,String testSpecification) {

		String path = reportDir+ "/"+ testReportName + ".xls";
//        System.out.println(reportDir);
//        System.out.println(path);
		File f = new File(path);
		if (!f.exists()) {
			try {
				if (!(new File(reportDir).isDirectory())) { // 判断是否存在该目录
					new File(reportDir).mkdir(); // 如果不存在则新建一个目录
				}
				String navigation[] = { "Success", "Failure ", "Skipped",
						"Success Rate", "Time Consuming", "Total" };
				String classNavigation[] = { "Test Suite", "Suite Summary",
						"Success Rate", "Log", "Case Counts", "Test Case",
						"Case Summary", "Time Consuming", "Screenshot", "Status",
						"Comment" };
				if (StringUtil.isEmpty(sheetName)){
					sheetName = "TestSummary";
				}
				JXLTool.createExcel(path, sheetName);
				JXLTool.openSheet(path, sheetName);
				WritableCellFormat bodyFormat = JXLTool.getFormat(10, false,true);
				WritableCellFormat titleFormat = JXLTool.getFormat(10, true,true,Colour.BLUE_GREY);
				


				for (int i = 0; i < navigation.length; i++) {
					JXLTool.writeData(i, 3, navigation[i], titleFormat);
				}
				JXLTool.writeData(0, 0, "Project Name", titleFormat);
				JXLTool.writeData(0, 1, "Project Info", titleFormat);
				JXLTool.writeData(0, 2, "Test Specification",titleFormat);
				JXLTool.writeData(1, 0, projectName, bodyFormat);
				JXLTool.writeData(1, 1, projectInfo, bodyFormat);
				JXLTool.writeData(1, 2, testSpecification,bodyFormat);
				JXLTool.mergeCells(1, 0, 5, 0);
				JXLTool.mergeCells(1, 1, 5, 1);
				JXLTool.mergeCells(1, 2, 5, 2);
				JXLTool.writeData(0, 4, "0", bodyFormat);
				JXLTool.writeData(1, 4, "0", bodyFormat);
				JXLTool.writeData(2, 4, "0", bodyFormat);
				JXLTool.writeData(4, 4, "0s", bodyFormat);
				JXLTool.writeData(3, 4, "0", bodyFormat);
				JXLTool.writeData(5, 4, "0", bodyFormat);

				for (int i = 0; i < classNavigation.length; i++) {
					JXLTool.writeData(i, 7, classNavigation[i],titleFormat);
				}
				JXLTool.setColumnView(0, 11);
				JXLTool.setColumnView(1, 11);
				JXLTool.setColumnView(2, 11);
				JXLTool.setColumnView(3, 11);
				JXLTool.setColumnView(4, 11);
				JXLTool.setColumnView(5, 18);
				JXLTool.setColumnView(6, 30);
				JXLTool.setColumnView(7, 11);
				JXLTool.setColumnView(8, 18);
				JXLTool.setColumnView(9, 10);
				JXLTool.setColumnView(10, 50);
				JXLTool.close();
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}

	}
	
	public static void writeScriptToExcel(String excelPath, String sheetName,List<List<String>> testScriptData){

		JXLTool.openSheet(excelPath, sheetName);
		for (int i = 0; i < testScriptData.size(); i++) {
			int row = Integer.parseInt(testScriptData.get(i).get(0));
			String result = testScriptData.get(i).get(1);
			JXLTool.writeData(12, row, result);
		}
		JXLTool.close();
	}
	
	public static void writeResultToExcel(String excelPath, String sheetName,List<List<String>> testResultData){
		
		JXLTool.openSheet(excelPath, sheetName);
		for (int i = 0; i < testResultData.size(); i++) {
			int row = Integer.parseInt(testResultData.get(i).get(0));
			String result = testResultData.get(i).get(1);
			JXLTool.writeData(12, row, result);
		}
		JXLTool.close();
	}
}
