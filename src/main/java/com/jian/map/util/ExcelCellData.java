package com.jian.map.util;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;

public class ExcelCellData {
	
	private HSSFCellStyle style;
	private Object value;
	private int columnWidth = 15;  //0-255
	
	public ExcelCellData() {
		super();
	}
	
	public ExcelCellData(Object value) {
		super();
		this.value = value;
	}
	
	public ExcelCellData(HSSFCellStyle style, Object value) {
		super();
		this.style = style;
		this.value = value;
	}
	
	public ExcelCellData(HSSFCellStyle style, Object value, int columnWidth) {
		super();
		this.style = style;
		this.value = value;
		this.columnWidth = columnWidth;
	}
	
	
	
	public HSSFCellStyle getStyle() {
		return style;
	}
	public void setStyle(HSSFCellStyle style) {
		this.style = style;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public int getColumnWidth() {
		return columnWidth;
	}
	public void setColumnWidth(int columnWidth) {
		this.columnWidth = columnWidth;
	}
}
