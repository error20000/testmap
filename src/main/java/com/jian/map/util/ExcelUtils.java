package com.jian.map.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelUtils {
    
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
 
}
