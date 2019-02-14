package com.jian.map.config;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jian.tools.core.CacheObject;
import com.jian.tools.core.CacheTools;
import com.jian.tools.core.ResultKey;
import com.jian.tools.core.ResultTools;
import com.jian.tools.core.Tips;

/**
 * 根据情况需要重写接口
 * @author liujian
 */
@Component
public class VerifyConfig {
	
	public static Config config = null;
	
	@Autowired
	public void setConfig(Config config){
		VerifyConfig.config = config;
	}

	/**
	 * 授权验证<br/>
	 * 步骤：<br/>
	 * 1、获取登录用户。<br/>
	 * 2、获取接口。<br/>
	 * 3、查询接口权限。<br/>
	 * 通过情况：<br/>
	 * 1、如果找不到接口，通过。<br/>
	 * 2、如果接口是开放的，通过。<br/>
	 * 3、如果有接口权限，通过。<br/>
	 * @param req
	 * @return 通过返回null
	 */
	public static Map<String, Object> verifyAuth(HttpServletRequest req){
		//TODO do something
		
		return null;
	}
	
	/**
	 * 参数签名验证
	 * @param req
	 * @return 通过返回null
	 */
	public static Map<String, Object> verifySign(HttpServletRequest req){
		//TODO do something
		
		return null;
	}
	
	/**
	 * 登录验证
	 * @param req
	 * @return 通过返回null
	 */
	public static Map<String, Object> verifyLogin(HttpServletRequest req){
		//TODO do something
		//本地session验证.
		return verifyLoginNormal(req);
	}
	
	private static Map<String, Object> verifyLoginNormal(HttpServletRequest req){
//		HttpSession session = req.getSession();
//		Object temp = session.getAttribute(config.login_session_key);
		
		String userId = req.getHeader("user");
		CacheObject test = CacheTools.getCacheObj("login_user_"+userId);
		if(test == null){
			return ResultTools.custom(Tips.ERROR111).put(ResultKey.DATA, "verifyLoginNormal session is null!").build();
		}
		return null;
	}
	
	
}
