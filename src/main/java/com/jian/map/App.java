package com.jian.map;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Base64;
import java.util.Enumeration;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages="com.jian.map")
@EnableAutoConfiguration
public class App {
	
	public static String rootPath = "";
	public static ApplicationContext applicationContext = null;
	public static String[] scanBasePackages = {};
	
	public static void main(String[] args) throws Exception {
		//项目目录
		rootPath = App.class.getResource("/").getPath().replace("/target/classes/", "/");
    	System.out.println(rootPath);
		//扫描范围
    	if(App.class.isAnnotationPresent(SpringBootApplication.class)){
    		SpringBootApplication sba = App.class.getAnnotation(SpringBootApplication.class);
    		scanBasePackages = sba.scanBasePackages();
    	}
    	System.out.println(scanBasePackages);
		//启动
        applicationContext = SpringApplication.run(App.class, args);
    }


	// 配置http
	/*@Bean
	public ServletWebServerFactory servletContainer() {
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
		tomcat.addAdditionalTomcatConnectors(createStandardConnector()); // 添加http
		return tomcat;
	}
	
    private Connector createStandardConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setPort(8075);
        return connector;
    }*/

    //set http url auto change to https
   /* @Bean
    public ServletWebServerFactory servletContainer(){
    	TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory(){
			@Override 
			protected void postProcessContext(Context context) { 
				SecurityConstraint securityConstraint = new SecurityConstraint(); 
				securityConstraint.setUserConstraint("CONFIDENTIAL"); 
				SecurityCollection collection=new SecurityCollection(); 
				collection.addPattern("/*"); 
				securityConstraint.addCollection(collection); 
				context.addConstraint(securityConstraint); 
			}
    	};
        tomcat.addAdditionalTomcatConnectors(httpConnector());
        return tomcat;
    }

    private Connector httpConnector(){
        Connector connector=new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(8075);
        connector.setSecure(false);
        connector.setRedirectPort(8065);
        return connector;
    }*/
    

}
