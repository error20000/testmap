package com.jian.map.util;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * poi 3.17
 * @author Administrator
 *
 */
public class ExcelUtils {
    
	/**
	 * 导入excel
	 * @param inputStream
	 * @return
	 */
    public static List<String> importExcel(InputStream inputStream) {
    	List<String> list = new ArrayList<>();
        try {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            //获取sheet的行数
            int rows = sheet.getPhysicalNumberOfRows();
            for (int i = 0; i < rows; i++) {
                //过滤表头行
                if (i == 0) {
                    continue;
                }
                //获取当前行的数据
                Row row = sheet.getRow(i);
                String str = "";
                for (Cell cell : row) {
                    if (cell.getCellTypeEnum().equals(CellType.NUMERIC)) {
                    	str += "," + cell.getNumericCellValue();
                    }
                    if (cell.getCellTypeEnum().equals(CellType.STRING)) {
                    	str += "," + cell.getStringCellValue();
                    }
                    if (cell.getCellTypeEnum().equals(CellType.BOOLEAN)) {
                    	str += "," + cell.getBooleanCellValue();
                    }
                    if (cell.getCellTypeEnum().equals(CellType.ERROR)) {
                    	str += "," + cell.getErrorCellValue();
                    }
                }
                list.add(str.substring(1));
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
    

    
    public static List<ExcelCellData> parseHead(String head) {
    	//解析数据
    	List<ExcelCellData> headData = new ArrayList<>();
		String[] heads = head.split(",");
		ExcelCellData node = null;
        for (int i = 0; i < heads.length; i++) {
        	node = new ExcelCellData();
        	node.setStyle(null);
        	node.setValue(heads[i]);
        	headData.add(node);
        }
        return headData;
    }
    
    public static void exportExcel(String fileName, HttpServletResponse resp, List<ExcelCellData> headData, List<List<ExcelCellData>> data) {

    	//实例化HSSFWorkbook
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建一个Excel表单，参数为sheet的名字
        HSSFSheet sheet = workbook.createSheet("sheet");

		//设置表头
        setHead(sheet, headData);
		//设置表体
        for (List<ExcelCellData> row : data) {
        	setBody(sheet, row);
		}
    	
		//输出
		resp.addHeader("Content-Disposition","attachment;filename="+fileName);
		// response.addHeader("Content-Length", "" + JSONArray.fromObject(list).toString().getBytes().length);
		resp.setContentType("application/vnd.ms-excel;charset=utf-8");
		try {
			OutputStream toClient = new BufferedOutputStream(resp.getOutputStream());
			workbook.write(toClient);
			workbook.close();
			toClient.flush();
			toClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public static void setHead(HSSFSheet sheet, List<ExcelCellData> headData) {
    	/*//表头行
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        for (int i = 0; i < headData.size(); i++) {
        	sheet.setColumnWidth(i, headData.get(i).getColumnWidth() * 256);
        }
        //创建表头
        HSSFCell cell;
        for (int i = 0; i < headData.size(); i++) {
        	ExcelCellData node = headData.get(i);
        	HSSFCellStyle style = node.getStyle();
        	String value = (String) node.getValue();
            cell = row.createCell(i);
            cell.setCellValue(value);
            if(style != null){
            	cell.setCellStyle(style);
            }
        }*/
    	//设置样式
    	HSSFWorkbook workbook = sheet.getWorkbook();
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        //style.setDataFormat(HSSFDataFormat.getBuiltinFormat("yyyy-MM-dd HH:mm:ss"));
		ExcelCellData node = null;
        for (int i = 0; i < headData.size(); i++) {
        	node = headData.get(i);
        	node.setStyle(style);
        }
        
    	setBody(sheet, headData);
    }
    
    public static void setBody(HSSFSheet sheet, List<ExcelCellData> rowData) {
    	int lastRowNum = sheet.getPhysicalNumberOfRows();//获得总行数  1。。。。
    	if(lastRowNum == 0){ //无表头
            //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
            for (int i = 0; i < rowData.size(); i++) {
            	// 15  在EXCEL文档中实际列宽为14.29
                sheet.setColumnWidth(i, (int)(( rowData.get(i).getColumnWidth() + 0.72) * 256) );
            }
    	}
    	//创建新行
        HSSFRow row = sheet.createRow(lastRowNum);  //创建行  0。。。。
        HSSFCell cell;
        for (int i = 0; i < rowData.size(); i++) {
        	ExcelCellData node = rowData.get(i);
        	HSSFCellStyle style = node.getStyle();
        	Object value = node.getValue();
            cell = row.createCell(i);
            if(value instanceof String){
            	cell.setCellValue((String)value);
            }else if(value instanceof Number){
            	cell.setCellValue((double)value);
            }else if(value instanceof Date){
            	cell.setCellValue((Date)value);
            }else if(value instanceof Calendar){
            	cell.setCellValue((Calendar)value);
            }else if(value instanceof RichTextString){
            	cell.setCellValue((RichTextString)value);
            }
            if(style != null){
            	cell.setCellStyle(style);
            }
        }
    }
    
}

