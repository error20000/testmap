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
@Table("s_user")
public class User  extends Base<User> {
	
	//field
	@PrimaryKey(type=PrimaryKeyType.AUTO_INCREMENT)
	@Excel(name="pid", sort=0, length="11", isNull=0 )
	private int pid;
	@Excel(name="username", sort=1, length="20", isNull=1 )
	private String username;
	@Excel(name="password", sort=2, length="32", isNull=1 )
	private String password;
	@Excel(name="nick", sort=3, length="20", isNull=1 )
	private String nick;
	@Excel(name="admin  0: no, 1: yes", sort=4, value="0", length="4", isNull=1 )
	private int admin;
	@Excel(name="color", sort=4, length="10", isNull=1 )
	private String color;
	
	//get set
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public int getAdmin() {
		return admin;
	}
	public void setAdmin(int admin) {
		this.admin = admin;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}

}
