package com.jian.map.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jian.annotation.API;
import com.jian.annotation.ParamsInfo;
import com.jian.tools.core.CacheObject;
import com.jian.tools.core.CacheTools;
import com.jian.tools.core.JsonTools;
import com.jian.tools.core.MapTools;
import com.jian.tools.core.ResultKey;
import com.jian.tools.core.ResultTools;
import com.jian.tools.core.Tips;
import com.jian.tools.core.Tools;
import com.jian.map.config.Config;
import com.jian.map.entity.Options;
import com.jian.map.entity.User;
import com.jian.map.service.OptionsService;
import com.jian.map.util.Utils;

@Controller
@RequestMapping("/api/options")
@API(info="", entity={Options.class})
public class OptionsController extends BaseController<Options> {

	@Autowired
	private OptionsService service;
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
				@ParamsInfo(name="name", type="String", isNull=1,  info="名称"),
				@ParamsInfo(name="status", type="int", isNull=1,  info="状态  0：禁用，1：启用"),
		}, 
		response={
				@ParamsInfo(name=ResultKey.CODE, type="int", info="返回码"),
				@ParamsInfo(name=ResultKey.MSG, type="String", info="状态描述"),
				@ParamsInfo(name=ResultKey.DATA, type="", info="数据集"),
		})
	public String add(HttpServletRequest req) {
		
		Map<String, Object> vMap = null;
		//登录
		vMap = verifyLogin(req);
		if(vMap != null){
			return JsonTools.toJsonString(vMap);
		}
		//sign
		vMap = verifySign(req);
		if(vMap != null){
			return JsonTools.toJsonString(vMap);
		}
		//权限
		vMap = verifyAuth(req);
		if(vMap != null){
			return JsonTools.toJsonString(vMap);
		}
		//登录用户
		User user = getLoginUser(req);
		if(user == null){
			return ResultTools.custom(Tips.ERROR111).toJSONString();
		}
		if(user.getAdmin() != 1){
			return ResultTools.custom(Tips.ERROR201).toJSONString();
		}
		
		//保存
		Options obj = Tools.getReqParamsToObject(req, new Options());
		int res = service.add(obj);
		if(res > 0){
			return ResultTools.custom(Tips.ERROR1).put(ResultKey.DATA, res).toJSONString();
		}else{
			return ResultTools.custom(Tips.ERROR0).toJSONString();
		}
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
				@ParamsInfo(name="name", type="String", isNull=1,  info="名称"),
				@ParamsInfo(name="status", type="int", isNull=1,  info="状态  0：禁用，1：启用"),
		}, 
		response={
				@ParamsInfo(name=ResultKey.CODE, type="int", info="返回码"),
				@ParamsInfo(name=ResultKey.MSG, type="String", info="状态描述"),
				@ParamsInfo(name=ResultKey.DATA, type="", info="数据集"),
		})
	public String update(HttpServletRequest req) {
		
		Map<String, Object> vMap = null;
		//登录
		vMap = verifyLogin(req);
		if(vMap != null){
			return JsonTools.toJsonString(vMap);
		}
		//sign
		vMap = verifySign(req);
		if(vMap != null){
			return JsonTools.toJsonString(vMap);
		}
		//权限
		vMap = verifyAuth(req);
		if(vMap != null){
			return JsonTools.toJsonString(vMap);
		}
		//登录用户
		User user = getLoginUser(req);
		if(user == null){
			return ResultTools.custom(Tips.ERROR111).toJSONString();
		}
		if(user.getAdmin() != 1){
			return ResultTools.custom(Tips.ERROR201).toJSONString();
		}
		
		//参数
		List<String> pkeys = Utils.getPrimaryKeys(Options.class);//获取主键
		if(pkeys == null || pkeys.isEmpty()){
			return ResultTools.custom(Tips.ERROR206).toJSONString();
		}
		Map<String, Object> condition = new HashMap<String, Object>();
		for (String str : pkeys) {
			String strv = Tools.getReqParamSafe(req, str);
			vMap = Tools.verifyParam(str, strv, 0, 0);
			if(vMap != null){
				return ResultTools.custom(Tips.ERROR206, str).toJSONString();
			}
			condition.put(str, strv);
		}
		Map<String, Object> setValues = Tools.getReqParamsToMap(req, Options.class);
		//保存
		int res = service.modify(setValues, condition);
		if(res > 0){
			return ResultTools.custom(Tips.ERROR1).toJSONString();
		}else{
			return ResultTools.custom(Tips.ERROR0).put(ResultKey.DATA, res).toJSONString();
		}
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
		
		Map<String, Object> vMap = null;
		//登录
		vMap = verifyLogin(req);
		if(vMap != null){
			return JsonTools.toJsonString(vMap);
		}
		//sign
		vMap = verifySign(req);
		if(vMap != null){
			return JsonTools.toJsonString(vMap);
		}
		//权限
		vMap = verifyAuth(req);
		if(vMap != null){
			return JsonTools.toJsonString(vMap);
		}
		//登录用户
		User user = getLoginUser(req);
		if(user == null){
			return ResultTools.custom(Tips.ERROR111).toJSONString();
		}
		if(user.getAdmin() != 1){
			return ResultTools.custom(Tips.ERROR201).toJSONString();
		}
		
		//参数
		List<String> pkeys = Utils.getPrimaryKeys(Options.class);//获取主键
		if(pkeys == null || pkeys.isEmpty()){
			return ResultTools.custom(Tips.ERROR206).toJSONString();
		}
		Map<String, Object> condition = new HashMap<String, Object>();
		for (String str : pkeys) {
			String strv = Tools.getReqParamSafe(req, str);
			vMap = Tools.verifyParam(str, strv, 0, 0);
			if(vMap != null){
				return ResultTools.custom(Tips.ERROR206, str).toJSONString();
			}
			condition.put(str, strv);
		}
		//保存
		int res = service.delete(condition);
		if(res > 0){
			return ResultTools.custom(Tips.ERROR1).toJSONString();
		}else{
			return ResultTools.custom(Tips.ERROR0).put(ResultKey.DATA, res).toJSONString();
		}
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
				@ParamsInfo(name="name", type="String", isNull=1,  info="名称"),
				@ParamsInfo(name="status", type="int", isNull=1,  info="状态  0：禁用，1：启用"),
		}, 
		response={
				@ParamsInfo(name=ResultKey.CODE, type="int", info="返回码"),
				@ParamsInfo(name=ResultKey.MSG, type="String", info="状态描述"),
				@ParamsInfo(name=ResultKey.DATA, type="Array", info="数据集"),
				@ParamsInfo(name=ResultKey.TOTAL, type="int", info="总数"),
		})
	public String findPage(HttpServletRequest req) {
		//登录用户
		User user = getLoginUser(req);
		if(user == null){
			return ResultTools.custom(Tips.ERROR111).toJSONString();
		}
		if(user.getAdmin() != 1){
			return ResultTools.custom(Tips.ERROR201).toJSONString();
		}
		
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
				@ParamsInfo(name="name", type="String", isNull=1,  info="名称"),
				@ParamsInfo(name="status", type="int", isNull=1,  info="状态  0：禁用，1：启用"),
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
				@ParamsInfo(name="name", type="String", isNull=1,  info="名称"),
				@ParamsInfo(name="status", type="int", isNull=1,  info="状态  0：禁用，1：启用"),
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
	

	@RequestMapping("/findAll")
    @ResponseBody
	@API(name="查询所有", 
		info="需登录认证", 
		request={
		}, 
		response={
				@ParamsInfo(name=ResultKey.CODE, type="int", info="返回码"),
				@ParamsInfo(name=ResultKey.MSG, type="String", info="状态描述"),
				@ParamsInfo(name=ResultKey.DATA, type="Array", info="数据集"),
		})
	public String findAll(HttpServletRequest req) {
		return super.findAll(req);
	}
	
	//TODO 自定义方法
	

	@RequestMapping("/all")
    @ResponseBody
	public String all(HttpServletRequest req) {
		
		Map<String, Object> vMap = null;
		//sign
		vMap = verifySign(req);
		if(vMap != null){
			return JsonTools.toJsonString(vMap);
		}
		
		List<Options> list = service.findList(MapTools.custom().put("status", 1).build());
        return ResultTools.custom(Tips.ERROR1).put(ResultKey.DATA, list).toJSONString();
	}
	
	private User getLoginUser(HttpServletRequest req){

//		HttpSession session = req.getSession();
//		User user = (User)session.getAttribute(config.login_session_key);
		
		String userId = req.getHeader("user");
		CacheObject test = CacheTools.getCacheObj("login_user_"+userId);
		User user = (User)test.getValue();
		
		return user;
	}
}
