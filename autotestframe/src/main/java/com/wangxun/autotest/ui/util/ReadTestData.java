package com.wangxun.autotest.ui.util;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadTestData {
	private class InvalidTest { }
	public List<Map<String, String>> getTestData(String excelpath, String tablename) {
		String path = excelpath;
		Workbook readwb = null;
		InputStream instream = null;
		try {
			instream = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			readwb = Workbook.getWorkbook(instream);
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Sheet readsheet = readwb.getSheet(tablename);
		int rsColumns = readsheet.getColumns();
		int rsRows = readsheet.getRows();
		if (rsRows == 0) {
			throw new IllegalStateException("There is now row found in excel file [" + excelpath + "], can't "
					+ "generate map from column name to column index. ");
		}

		String key[] = new String[rsColumns];
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		for (int j = 0; j < rsColumns; j++) {
			Cell cell = readsheet.getCell(j, 0);
			key[j] = cell.getContents();
		}

		for (int i = 1; i < rsRows; i++)

		{
			if (readsheet.getCell(0, i).getContents() != "") {
				Map<String, String> map = new HashMap<String, String>();
				for (int j = 0; j < rsColumns; j++) {
					Cell cell = readsheet.getCell(j, i);
					map.put(key[j], cell.getContents());
				}
				data.add(map);
			}
		}
		readwb.close();
		return data;
	}

	private List<List<String>> getData(String excelpath, String tablename) {
/*		String currentPath = System.getProperty("user.dir");
		String path = currentPath + "/" + excelpath;*/
		Workbook readwb = null;
		InputStream instream = null;
		try {
			instream = new FileInputStream(excelpath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			readwb = Workbook.getWorkbook(instream);
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Sheet readsheet = readwb.getSheet(tablename);
		int rsColumns = readsheet.getColumns();
		int rsRows = readsheet.getRows();

		if (rsRows == 0) {
			throw new IllegalStateException("There is now row found in excel file [" + excelpath + "], can't "
					+ "generate map from column name to column index. ");
		}
		for (int i = 1; i < rsRows; i++) {
			if (readsheet.getCell(0, i).getContents() == "") {
				rsRows = i;
				break;
			}
		}
		for (int i = 1; i < rsColumns; i++) {
			if (readsheet.getCell(i, 0).getContents() == "") {
				rsColumns = i;
				break;
			}
		}

		List<List<String>> data = new ArrayList<List<String>>();

		for (int i = 1; i < rsRows; i++)

		{
			if (readsheet.getCell(0, i).getContents() != "") {
				List<String> rowdata = new ArrayList<String>();

				for (int j = 0; j < rsColumns; j++) {

					Cell cell = readsheet.getCell(j, i);
					rowdata.add(cell.getContents());

				}
				data.add(rowdata);
			}
		}
		readwb.close();
		return data;
	}

	public Object[][] getTestDataForTestNG(String excelpath, String tablename) {
		List<List<String>> data = this.getData(excelpath, tablename);
		List<Object[]> d = new ArrayList<Object[]>();
		int rowSize = data.size();
		int cowSize = data.get(0).size();

		for (int i = 0; i < rowSize; i++) {
			Object[] row = new Object[cowSize];
			for (int j = 0; j < cowSize; j++) {
				row[j] = data.get(i).get(j);

			}

			d.add(row);
		}

		Object[][] newData = new Object[rowSize][cowSize];
		for (int i = 0; i < rowSize; i++) {

			newData[i] = d.get(i);
		}
		return newData;

	}
}
