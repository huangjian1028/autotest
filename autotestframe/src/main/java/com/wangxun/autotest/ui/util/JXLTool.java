package com.wangxun.autotest.ui.util;

import jxl.Sheet;
import jxl.SheetSettings;
import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.*;
import jxl.format.VerticalAlignment;
import jxl.read.biff.BiffException;
import jxl.write.*;
import jxl.write.WritableFont.FontName;
import jxl.write.biff.RowsExceededException;

import java.io.File;
import java.io.IOException;
import java.lang.Boolean;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JXLTool {
	private class InvalidTest { }
	protected static Workbook wb;
	protected static WritableWorkbook wbe;
	protected static WritableSheet currentSheet;



	
    public static void createExcel(String path, String sheetName) {
        if (new File(path).exists()) {
            new File(path).delete();
        }
        try {
            wbe = Workbook.createWorkbook(new File(path));
            wbe.createSheet(sheetName, 0);
            wbe.write();
            wbe.close();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    
    
    public static void createSheet(String path, String sheetName, int index) {

        try {

            String[] sheets = wbe.getSheetNames();
            boolean status = false;
            for (String s : sheets) {        
                if (s.equals(sheetName)) {   
                	status = true;
                }
            }
            if (!status){
                wb = Workbook.getWorkbook(new File(path));
                wbe = Workbook.createWorkbook(new File(path), wb);
            	wbe.createSheet(sheetName, index);
            	wbe.write();
    			wbe.close();
    			wb.close();
            }           			
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    
    public static void openSheet(String path, String sheetName) {

        try {
            wb = Workbook.getWorkbook(new File(path));
            wbe = Workbook.createWorkbook(new File(path), wb);
            currentSheet = wbe.getSheet(sheetName);

        } catch (Exception e) {
        }
    }
    
	public static void close(){
		try {
			wbe.write();
			wbe.close();
			wb.close();
        } catch (Exception e) {
        }
	}
	
    public static int getRows() {
    	
        return currentSheet.getRows();
    }

    public static int getCols() {

        return currentSheet.getColumns();
    }

    public static String[] getSheetNames() {

        return wbe.getSheetNames();
    }
    
	public static void writeLastRow(int col, String content){
		
		try {
			int row = currentSheet.getRows();
			Label label = new Label(col, row, content);
			currentSheet.addCell(label);
		} catch (Exception e) {
        }
	}

	public static void writeLastRow(int col, String content, WritableCellFormat format){
		
		try {
			int row = currentSheet.getRows();
			Label label = new Label(col, row, content, format);
			currentSheet.addCell(label);
		} catch (Exception e) {
        }
	}
	
	public static void writeSameRow(int col, String content){
		
		try {
			int row = currentSheet.getRows();
			Label label = new Label(col, row-1, content);
			currentSheet.addCell(label);
		} catch (Exception e) {
        }
	}
	
	public static void writeSameRow(int col, String content, WritableCellFormat format){
		
		try {
			int row = currentSheet.getRows();
			Label label = new Label(col, row-1, content, format);
			currentSheet.addCell(label);
		} catch (Exception e) {
        }
	}

	public static void writeData(int cow, int row, String content, String comment, WritableCellFormat format){

		try {
			Label label = new Label(cow, row, content,format);
			if (comment!=null && comment!=""){
				WritableCellFeatures wcfeatures = new WritableCellFeatures();  
				wcfeatures.setComment(comment);  
				label.setCellFeatures(wcfeatures);  
			}
			currentSheet.addCell(label);
		} catch (Exception e) {
        }
	}

	public static void writeData(int cow, int row, String content, String comment){

		try {
			Label label = new Label(cow, row, content);
			if (comment!=null && comment!=""){
				WritableCellFeatures wcfeatures = new WritableCellFeatures();  
				wcfeatures.setComment(comment);  
				label.setCellFeatures(wcfeatures);  
			}
			currentSheet.addCell(label);
		} catch (Exception e) {
        }
	}
	
	public static void writeData(int cow, int row, String content){

		writeData(cow, row, content,"");
	}

	public static void writeData(int cow, int row, String content, WritableCellFormat format){

		writeData(cow, row, content,"",format);
	}

	public static void mergeCells(int col1, int row1, int col2, int row2){
		try {
			currentSheet.mergeCells(col1, row1, col2, row2);
		} catch (Exception e) {
        }
	}

	public static void setColumnView(int col, int width) {
		
		try {
			currentSheet.setColumnView(col, width);
		} catch (Exception e) {
        }
	}

	public static WritableCellFormat getFormat(FontName fontName, int size, boolean isBold,Colour fontColour,Boolean wrap,Colour backGroundColour,VerticalAlignment verticalAlignment){
		
		try {
			WritableFont font = new WritableFont(fontName, size);
			font.setColour(fontColour);
			if(isBold){
				font.setBoldStyle(WritableFont.BOLD);
			}			
			WritableCellFormat format = new WritableCellFormat(font);
			format.setWrap(wrap);
			format.setVerticalAlignment(verticalAlignment);
			format.setBackground(backGroundColour);
			format.setBorder(Border.ALL, BorderLineStyle.THIN);
			return format;
		} catch (Exception e) {
			return null;
        }
	}
	
	public static WritableCellFormat getFormat(int size, boolean isBold, Colour fontColour,Boolean wrap,Colour backGroundColour){
		
		return getFormat(WritableFont.ARIAL, size, isBold, fontColour, wrap, backGroundColour, VerticalAlignment.CENTRE);
	}
	
	public static WritableCellFormat getFormat(int size, boolean isBold, Boolean wrap,Colour backGroundColour){
		
		return getFormat(WritableFont.ARIAL, size, isBold, Colour.BLACK, wrap, backGroundColour, VerticalAlignment.CENTRE);
	}
	
	public static WritableCellFormat getFormat(int size, boolean isBold, Colour fontColour,Boolean wrap){
		
		return getFormat(WritableFont.ARIAL, size, isBold, fontColour, wrap, Colour.WHITE, VerticalAlignment.CENTRE);
	}
	
	public static WritableCellFormat getFormat(int size, boolean isBold, Boolean wrap){
		
		return getFormat(WritableFont.ARIAL, size, isBold, Colour.BLACK, wrap, Colour.WHITE, VerticalAlignment.CENTRE);
	}
	
	public static WritableCellFormat getFormat(int size, boolean isBold){
		
		return getFormat(WritableFont.ARIAL, size, isBold, Colour.BLACK, true, Colour.WHITE, VerticalAlignment.CENTRE);
	}

	public static void setHyperLinkForFile(int col, int row, String filePath, String desc){
		
		try {
			WritableHyperlink link = new WritableHyperlink(col, row, new File(filePath), desc);
			currentSheet.addHyperlink(link);
		} catch (Exception e) {
        }
	}

	public static void setHyperLinkForSheet(int col, int row, String desc, String sheetName, int destCol, int destRow){
		
		try {
			WritableSheet desSheet = wbe.getSheet(sheetName);
			WritableHyperlink link = new WritableHyperlink(col, row, desc, desSheet, destCol, destRow);
			currentSheet.addHyperlink(link);
		} catch (Exception e) {
        }
	}

	public static void setHyperLinkByFormu(int col, int row, String path, String name){
		
		try {
			WritableFont font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false,
					UnderlineStyle.SINGLE);
			font.setColour(Colour.BLUE);
			WritableCellFormat format = new WritableCellFormat(font);
			format.setWrap(true);
			format.setBorder(Border.ALL, BorderLineStyle.THIN);
			String formu = "HYPERLINK(\"" + path + "\",\"" + name + "\")";
			Formula formula = new Formula(col, row, formu, format);
			currentSheet.addCell(formula);
		} catch (Exception e) {
        }
	}

	public static void setVerticalFreeze(int col) {
		SheetSettings setting = currentSheet.getSettings();
		setting.setVerticalFreeze(col);
	}

	public static void copySheet(String modelPath, String destPath, String sheetName){
		
		try {
			WritableWorkbook wbe = Workbook.createWorkbook(new File(destPath));
			Workbook model = Workbook.getWorkbook(new File(modelPath));
			Sheet modelSheet = model.getSheet(0);
			wbe.importSheet(sheetName, 0, modelSheet);
			wbe.write();
			wbe.close();
		} catch (Exception e) {
        }
	}

    public static void addList(int col, int row, String content, List<String> contentArray, String comment) {
    	try {
    		Label label = new Label(col, row, content, getFormat(10, false));
            WritableCellFeatures cellFeatures = new WritableCellFeatures();
            cellFeatures.setDataValidationList(contentArray);
            cellFeatures.setComment(comment);  
            label.setCellFeatures(cellFeatures);
            currentSheet.addCell(label);
    	} catch (Exception e) {
        }

    }

    public static String getContents(int col, int row) {
        try {
            return currentSheet.getCell(col, row).getContents();
        } catch (Exception e) {

        }
        return "";
    }

    public static void writeImageToExcel(double beginCol, double beginRow, double strideCol, double strideRow, String filePath) {

        File fileImage = new File(filePath);
        WritableImage image = new WritableImage(beginCol, beginRow, strideCol, strideRow, fileImage);
        currentSheet.addImage(image);
    }

	public static void deleteSheet(String path, String name){
		try {
			wb = Workbook.getWorkbook(new File(path));
			wbe = Workbook.createWorkbook(new File(path), wb);
			String[] sheetNames = wbe.getSheetNames();
			Map<String, String> map = new HashMap<String, String>();
			for (int i = 0; i < sheetNames.length; i++) {
				String j = Integer.toString(i);
				map.put(sheetNames[i], j);
			}
			for (String sheetName : sheetNames) {
				if (sheetName.contains(name)) {
					wbe.removeSheet(Integer.parseInt(map.get(sheetName)));
				}
			}
			wbe.write();
			wbe.close();
		} catch (Exception e) {

        }

	}
	public static void main(String[] args) throws RowsExceededException, BiffException, WriteException, IOException {

		createExcel("D:/TestReport(20181023-103937-677).xls", "eee");
//		createSheet("D:/test.xls", "eee1",1);

/*		openSheet("D:/test.xls", "eee1");
		writeLastRow(0, "3334334");
		writeLastRow(0, "3334334");

		WritableCellFormat format = getFormat(10, false);
		writeLastRow(0, "3334334", format);
		writeSameRow(1, "3334334", format);
		writeSameRow(2, "3334334", format);
		setVerticalFreeze(4);
		setHyperLinkByFormu(5, 5, "C:/test.jpg", "test222");
        List<String> contentArray = new ArrayList<String>();
        contentArray.add("css");
        contentArray.add("id");
        contentArray.add("name");
        contentArray.add("xpath");
        writeData(1, 1, "23", "343343");
		close();*/
	}
}
