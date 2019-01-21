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
import com.jian.tools.core.JsonTools;
import com.jian.tools.core.ResultKey;
import com.jian.tools.core.ResultTools;
import com.jian.tools.core.Tips;
import com.jian.tools.core.Tools;
import com.jian.map.config.Config;
import com.jian.map.entity.Content;
import com.jian.map.entity.User;
import com.jian.map.service.ContentService;

@Controller
@RequestMapping("/api/content")
@API(info="", entity={Content.class})
public class ContentController extends BaseController<Content> {

	@Autowired
	private ContentService service;
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
				@ParamsInfo(name="user", type="int", isNull=1,  info="用户pid"),
				@ParamsInfo(name="local", type="String", isNull=1,  info="用户位置"),
				@ParamsInfo(name="path", type="String", isNull=1,  info="用户绘制区域"),
				@ParamsInfo(name="type", type="int", isNull=1,  info="用户评价类型"),
				@ParamsInfo(name="option", type="String", isNull=1,  info="用户选择的评价"),
				@ParamsInfo(name="content", type="String", isNull=1,  info="用户自定义的评价"),
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
		
		//保存
		Content obj = Tools.getReqParamsToObject(req, new Content());
		//获取登录用户
		HttpSession session = req.getSession();
		User test = (User)session.getAttribute(config.login_session_key);
		if(test == null){
			return ResultTools.custom(Tips.ERROR111).toJSONString();
		}
		obj.setUser(test.getPid());
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
				@ParamsInfo(name="user", type="int", isNull=1,  info="用户pid"),
				@ParamsInfo(name="local", type="String", isNull=1,  info="用户位置"),
				@ParamsInfo(name="path", type="String", isNull=1,  info="用户绘制区域"),
				@ParamsInfo(name="type", type="int", isNull=1,  info="用户评价类型"),
				@ParamsInfo(name="option", type="String", isNull=1,  info="用户选择的评价"),
				@ParamsInfo(name="content", type="String", isNull=1,  info="用户自定义的评价"),
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
				@ParamsInfo(name="user", type="int", isNull=1,  info="用户pid"),
				@ParamsInfo(name="date", type="String", isNull=1,  info="日期"),
				@ParamsInfo(name="local", type="String", isNull=1,  info="用户位置"),
				@ParamsInfo(name="path", type="String", isNull=1,  info="用户绘制区域"),
				@ParamsInfo(name="type", type="int", isNull=1,  info="用户评价类型"),
				@ParamsInfo(name="option", type="String", isNull=1,  info="用户选择的评价"),
				@ParamsInfo(name="content", type="String", isNull=1,  info="用户自定义的评价"),
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
				@ParamsInfo(name="user", type="int", isNull=1,  info="用户pid"),
				@ParamsInfo(name="date", type="String", isNull=1,  info="日期"),
				@ParamsInfo(name="local", type="String", isNull=1,  info="用户位置"),
				@ParamsInfo(name="path", type="String", isNull=1,  info="用户绘制区域"),
				@ParamsInfo(name="type", type="int", isNull=1,  info="用户评价类型"),
				@ParamsInfo(name="option", type="String", isNull=1,  info="用户选择的评价"),
				@ParamsInfo(name="content", type="String", isNull=1,  info="用户自定义的评价"),
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
				@ParamsInfo(name="user", type="int", isNull=1,  info="用户pid"),
				@ParamsInfo(name="date", type="String", isNull=1,  info="日期"),
				@ParamsInfo(name="local", type="String", isNull=1,  info="用户位置"),
				@ParamsInfo(name="path", type="String", isNull=1,  info="用户绘制区域"),
				@ParamsInfo(name="type", type="int", isNull=1,  info="用户评价类型"),
				@ParamsInfo(name="option", type="String", isNull=1,  info="用户选择的评价"),
				@ParamsInfo(name="content", type="String", isNull=1,  info="用户自定义的评价"),
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
	
}
