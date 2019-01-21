package com.jian.map.entity;

//import
import com.jian.annotation.PrimaryKey;
import com.jian.annotation.PrimaryKeyType;
import com.jian.annotation.Table;
import com.jian.annotation.Excel;

/**
 * @author liujian
 * @Date 
 */
@Table("s_content")
public class Content  extends Base<Content> {
	
	//field
	@PrimaryKey(type=PrimaryKeyType.AUTO_INCREMENT)
	@Excel(name="编号", sort=0, length="11", isNull=0 )
	private int pid;
	@Excel(name="用户pid", sort=1, value="0", length="11", isNull=1 )
	private int user;
	@Excel(name="日期", sort=2, length="20", isNull=1 )
	private String date;
	@Excel(name="用户位置", sort=3, length="255", isNull=1 )
	private String local;
	@Excel(name="用户绘制区域", sort=4, length="", isNull=1 )
	private String path;
	@Excel(name="用户评价类型", sort=5, value="0", length="4", isNull=1 )
	private int type;
	@Excel(name="用户选择的评价", sort=6, length="10", isNull=1 )
	private String option;
	@Excel(name="用户自定义的评价", sort=7, length="255", isNull=1 )
	private String content;
	
	//get set
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public int getUser() {
		return user;
	}
	public void setUser(int user) {
		this.user = user;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getLocal() {
		return local;
	}
	public void setLocal(String local) {
		this.local = local;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

}
