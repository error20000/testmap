package com.jian.map;

import com.jian.auto.AutoCreateManager;
import com.jian.auto.Config;
import com.jian.auto.ConfigDB;

public class Test {
	public static void main(String[] args) {
		String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/testmap?characterEncoding=utf8";
		String user = "root";
		String password = "123456";
		String driverClass = "com.mysql.jdbc.Driver";
		String prefix = "s_";
		String separator = "_";
		//包配置
		Config config = new Config("com.jian.map");
		//数据库配置
		ConfigDB cdb = new ConfigDB(jdbcUrl, user, password, driverClass, prefix, separator);
		AutoCreateManager test =  new AutoCreateManager(config, cdb, 1);
		test.start();
//		test.start(tableName);
		
	}
		
}
