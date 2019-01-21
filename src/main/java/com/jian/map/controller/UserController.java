package com.jian.map.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jian.annotation.API;
import com.jian.annotation.ParamsInfo;
import com.jian.map.config.Config;
import com.jian.map.entity.User;
import com.jian.map.service.UserService;
import com.jian.tools.core.JsonTools;
import com.jian.tools.core.MapTools;
import com.jian.tools.core.ResultKey;
import com.jian.tools.core.ResultTools;
import com.jian.tools.core.Tips;
import com.jian.tools.core.Tools;

@Controller
@RequestMapping("/api/user")
@API(info="", entity={User.class})
public class UserController extends BaseController<User> {

	@Autowired
	private UserService service;
	@Autowired
	private Config config;
	
	@Override
	public void initService() {
		super.service = service;
	}
	
	//TODO 基本方法
	
	@Override
	@RequestMapping("/add")
    @ResponseBody
	@API(name="新增", 
		info="需登录认证", 
		request={
				//add request
				@ParamsInfo(name="username", type="String", isNull=1,  info="用户名"),
				@ParamsInfo(name="password", type="String", isNull=1,  info="密码"),
				@ParamsInfo(name="nick", type="String", isNull=1,  info="昵称"),
				@ParamsInfo(name="admin", type="int", isNull=1,  info="超管  0：否，1：是"),
		}, 
		response={
				@ParamsInfo(name=ResultKey.CODE, type="int", info="返回码"),
				@ParamsInfo(name=ResultKey.MSG, type="String", info="状态描述"),
				@ParamsInfo(name=ResultKey.DATA, type="", info="数据集"),
		})
	public String add(HttpServletRequest req) {
		return super.add(req);
	}


	@Override
	@RequestMapping("/update")
    @ResponseBody
	@API(name="修改", 
		info="需登录认证", 
		request={
				//modify request
				@ParamsInfo(info="修改条件："),
				@ParamsInfo(name="pid", type="int", isNull=0,  info="编号"),
				@ParamsInfo(info="可修改字段："),
				@ParamsInfo(name="username", type="String", isNull=1,  info="用户名"),
				@ParamsInfo(name="password", type="String", isNull=1,  info="密码"),
				@ParamsInfo(name="nick", type="String", isNull=1,  info="昵称"),
				@ParamsInfo(name="admin", type="int", isNull=1,  info="超管  0：否，1：是"),
		}, 
		response={
				@ParamsInfo(name=ResultKey.CODE, type="int", info="返回码"),
				@ParamsInfo(name=ResultKey.MSG, type="String", info="状态描述"),
				@ParamsInfo(name=ResultKey.DATA, type="", info="数据集"),
		})
	public String update(HttpServletRequest req) {
		return super.update(req);
	}

	@Override
	@RequestMapping("/delete")
    @ResponseBody
	@API(name="删除", 
		info="需登录认证", 
		request={
				//delete request
				@ParamsInfo(name="pid", type="int", isNull=0,  info="编号"),
		}, 
		response={
				@ParamsInfo(name=ResultKey.CODE, type="int", info="返回码"),
				@ParamsInfo(name=ResultKey.MSG, type="String", info="状态描述"),
				@ParamsInfo(name=ResultKey.DATA, type="", info="数据集"),
		})
	public String delete(HttpServletRequest req) {
		return super.delete(req);
	}

	@Override
	@RequestMapping("/findPage")
    @ResponseBody
	@API(name="分页查询", 
		info="需登录认证", 
		request={
				@ParamsInfo(name="page", type="int", isNull=0, info="页码"),
				@ParamsInfo(name="rows", type="int", isNull=0, info="每页条数"),
				//findPage request
				@ParamsInfo(info="可选条件："),
				@ParamsInfo(name="pid", type="int", isNull=1,  info="编号"),
				@ParamsInfo(name="username", type="String", isNull=1,  info="用户名"),
				@ParamsInfo(name="password", type="String", isNull=1,  info="密码"),
				@ParamsInfo(name="nick", type="String", isNull=1,  info="昵称"),
				@ParamsInfo(name="admin", type="int", isNull=1,  info="超管  0：否，1：是"),
		}, 
		response={
				@ParamsInfo(name=ResultKey.CODE, type="int", info="返回码"),
				@ParamsInfo(name=ResultKey.MSG, type="String", info="状态描述"),
				@ParamsInfo(name=ResultKey.DATA, type="Array", info="数据集"),
				@ParamsInfo(name=ResultKey.TOTAL, type="int", info="总数"),
		})
	public String findPage(HttpServletRequest req) {
		return super.findPage(req);
	}

	@Override
	@RequestMapping("/findOne")
    @ResponseBody
	@API(name="查询一个", 
		info="需登录认证", 
		request={
				//findOne request
				@ParamsInfo(info="可选条件："),
				@ParamsInfo(name="pid", type="int", isNull=1,  info="编号"),
				@ParamsInfo(name="username", type="String", isNull=1,  info="用户名"),
				@ParamsInfo(name="password", type="String", isNull=1,  info="密码"),
				@ParamsInfo(name="nick", type="String", isNull=1,  info="昵称"),
				@ParamsInfo(name="admin", type="int", isNull=1,  info="超管  0：否，1：是"),
				@ParamsInfo(info="注意：以上条件不可同时为空。"),
		}, 
		response={
				@ParamsInfo(name=ResultKey.CODE, type="int", info="返回码"),
				@ParamsInfo(name=ResultKey.MSG, type="String", info="状态描述"),
				@ParamsInfo(name=ResultKey.DATA, type="Object", info="数据集"),
		})
	public String findOne(HttpServletRequest req) {
		return super.findOne(req);
	}

	@Override
	@RequestMapping("/findList")
    @ResponseBody
	@API(name="查询多个", 
		info="需登录认证", 
		request={
				//findList request
				@ParamsInfo(info="可选条件："),
				@ParamsInfo(name="pid", type="int", isNull=1,  info="编号"),
				@ParamsInfo(name="username", type="String", isNull=1,  info="用户名"),
				@ParamsInfo(name="password", type="String", isNull=1,  info="密码"),
				@ParamsInfo(name="nick", type="String", isNull=1,  info="昵称"),
				@ParamsInfo(name="admin", type="int", isNull=1,  info="超管  0：否，1：是"),
				@ParamsInfo(info="注意：以上条件不可同时为空。"),
		}, 
		response={
				@ParamsInfo(name=ResultKey.CODE, type="int", info="返回码"),
				@ParamsInfo(name=ResultKey.MSG, type="String", info="状态描述"),
				@ParamsInfo(name=ResultKey.DATA, type="Array", info="数据集"),
		})
	public String findList(HttpServletRequest req) {
		return super.findList(req);
	}
	
	//TODO 自定义方法

	@RequestMapping("/login")
    @ResponseBody
	@API(name="登录", 
		info="", 
		request={
				@ParamsInfo(name="username", type="String", isNull=0,  info="用户名"),
				@ParamsInfo(name="password", type="String", isNull=0,  info="密码"),
		}, 
		response={
				@ParamsInfo(name=ResultKey.CODE, type="int", info="返回码"),
				@ParamsInfo(name=ResultKey.MSG, type="String", info="状态描述"),
				@ParamsInfo(name=ResultKey.DATA, type="Array", info="数据集"),
		})
	public String login(HttpServletRequest req) {
		Map<String, Object> vMap = null;
		//sign
		vMap = verifySign(req);
		if(vMap != null){
			return JsonTools.toJsonString(vMap);
		}
		
		//参数
		String username = Tools.getReqParamSafe(req, "username");
		String password = Tools.getReqParamSafe(req, "password");
		vMap = Tools.verifyParam("username", username, 0, 0);
		if(vMap != null){
			return ResultTools.custom(Tips.ERROR206, "username").toJSONString();
		}
		vMap = Tools.verifyParam("password", password, 0, 0);
		if(vMap != null){
			return ResultTools.custom(Tips.ERROR206, "password").toJSONString();
		}
		
		//检查
		User user = service.findOne(MapTools.custom().put("username", username).build());
		if(user == null){
			return ResultTools.custom(Tips.ERROR109).toJSONString();
		}
		if(!user.getPassword().equals(Tools.md5(password))){
			return ResultTools.custom(Tips.ERROR110).toJSONString();
		}
		
		//保存
		HttpSession session = req.getSession();
		session.setAttribute(config.login_session_key, user);
		user.setPassword("");
		return ResultTools.custom(Tips.ERROR1).put(ResultKey.DATA, user).toJSONString();
	}
	
	@RequestMapping("/logout")
    @ResponseBody
	@API(name="退出", 
		info="", 
		request={
		}, 
		response={
				@ParamsInfo(name=ResultKey.CODE, type="int", info="返回码"),
				@ParamsInfo(name=ResultKey.MSG, type="String", info="状态描述"),
				@ParamsInfo(name=ResultKey.DATA, type="Array", info="数据集"),
		})
	public String logout(HttpServletRequest req) {
		//保存
		HttpSession session = req.getSession();
		session.removeAttribute(config.login_session_key);
		
		return ResultTools.custom(Tips.ERROR1).toJSONString();
	}
	
	@RequestMapping("/isLogin")
    @ResponseBody
	@API(name="检查登录", 
		info="", 
		request={
		}, 
		response={
				@ParamsInfo(name=ResultKey.CODE, type="int", info="返回码"),
				@ParamsInfo(name=ResultKey.MSG, type="String", info="状态描述"),
				@ParamsInfo(name=ResultKey.DATA, type="Array", info="数据集"),
		})
	public String isLogin(HttpServletRequest req) {
		//保存
		HttpSession session = req.getSession();
		Object test = session.getAttribute(config.login_session_key);
		if(test == null){
			return ResultTools.custom(Tips.ERROR0).toJSONString();
		}else {
			return ResultTools.custom(Tips.ERROR1).toJSONString();
		}
	}
}
