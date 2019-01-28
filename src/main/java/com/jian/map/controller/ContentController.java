package com.jian.map.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jian.annotation.API;
import com.jian.annotation.ParamsInfo;
import com.jian.map.config.Config;
import com.jian.map.entity.Content;
import com.jian.map.entity.User;
import com.jian.map.service.ContentService;
import com.jian.map.util.Utils;
import com.jian.tools.core.JsonTools;
import com.jian.tools.core.ResultKey;
import com.jian.tools.core.ResultTools;
import com.jian.tools.core.Tips;
import com.jian.tools.core.Tools;

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
		//登录用户
		User user = getLoginUser(req);
		if(user == null){
			return ResultTools.custom(Tips.ERROR111).toJSONString();
		}
		
		//保存
		Content obj = Tools.getReqParamsToObject(req, new Content());
		obj.setUser(user.getPid());
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
		List<String> pkeys = Utils.getPrimaryKeys(Content.class);//获取主键
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
		String page = Tools.getReqParamSafe(req, "page");
		String rows = Tools.getReqParamSafe(req, "rows");
		String startDate = Tools.getReqParamSafe(req, "start");
		String endDate = Tools.getReqParamSafe(req, "end");
		vMap = Tools.verifyParam("page", page, 0, 0, true);
		if(vMap != null){
			return JsonTools.toJsonString(vMap);
		}
		vMap = Tools.verifyParam("rows", rows, 0, 0, true);
		if(vMap != null){
			return JsonTools.toJsonString(vMap);
		}
		int start = Tools.parseInt(page) <= 1 ? 0 : (Tools.parseInt(page) - 1) * Tools.parseInt(rows);
		//参数
		String wsql = " 1=1 ";
		Map<String, Object> condition = Tools.getReqParamsToMap(req, Content.class);
		for (String key : condition.keySet()) {
			wsql += " and `"+key+"` = :"+key;
		}
		if(!Tools.isNullOrEmpty(startDate)) {
			wsql += " and `date` >= :startDate";
			condition.put("startDate", startDate);
		}
		if(!Tools.isNullOrEmpty(endDate)) {
			wsql += " and `date` <= :endDate";
			condition.put("endDate", endDate);
		}
		List<Content> list = service.getDao().findList(wsql,condition, start, Tools.parseInt(rows));
		long total = service.getDao().size(wsql,condition);
        return ResultTools.custom(Tips.ERROR1).put(ResultKey.TOTAL, total).put(ResultKey.DATA, list).toJSONString();
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
		
		//参数
		String wsql = " 1=1 ";
		String start = Tools.getReqParamSafe(req, "start");
		String end = Tools.getReqParamSafe(req, "end");
		Map<String, Object> condition = Tools.getReqParamsToMap(req, Content.class);
		for (String key : condition.keySet()) {
			wsql += " and `"+key+"` = :"+key;
		}
		if(!Tools.isNullOrEmpty(start)) {
			wsql += " and `date` >= :start";
			condition.put("start", start);
		}
		if(!Tools.isNullOrEmpty(end)) {
			wsql += " and `date` <= :end";
			condition.put("end", end);
		}
		
		List<Content> list = service.getDao().findList(wsql, condition);
        return ResultTools.custom(Tips.ERROR1).put(ResultKey.DATA, list).toJSONString();
	}

	@Override
	@RequestMapping("/findAll")
    @ResponseBody
	@API(name="查询所有", 
		info="", 
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
	
	@RequestMapping("/excel")
    @ResponseBody
	@API(name="导出excel", 
		info="", 
		request={
		}, 
		response={
				@ParamsInfo(name=ResultKey.CODE, type="int", info="返回码"),
				@ParamsInfo(name=ResultKey.MSG, type="String", info="状态描述"),
				@ParamsInfo(name=ResultKey.DATA, type="Array", info="数据集"),
		})
	public String excel(HttpServletRequest req, HttpServletResponse resp) {
		
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

		String wsql = " 1=1 ";
		String start = Tools.getReqParamSafe(req, "start");
		String end = Tools.getReqParamSafe(req, "end");
		//查询
		List<Content> list = null;
		Map<String, Object> condition = Tools.getReqParamsToMap(req, Content.class);
		for (String key : condition.keySet()) {
			wsql += " and `"+key+"` = :"+key;
		}
		if(!Tools.isNullOrEmpty(start)) {
			wsql += " and `date` >= :start";
			condition.put("start", start);
		}
		if(!Tools.isNullOrEmpty(end)) {
			wsql += " and `date` <= :end";
			condition.put("end", end);
		}
		if(condition == null || condition.isEmpty()){
			list = service.findAll();
		}else {
			list = service.getDao().findList(wsql, condition);
		}

		//执行
		resp.addHeader("Content-Disposition","attachment;filename=comment.csv");
		// response.addHeader("Content-Length", "" + JSONArray.fromObject(list).toString().getBytes().length);
		resp.setContentType("application/octet-stream;charset=utf-8");
		try {
			OutputStream toClient = new BufferedOutputStream(resp.getOutputStream());
			//header
			String head = "pid,user pid,date,user location,draw type,draw area,option,content";
			head += "\n";
			toClient.write(head.getBytes("utf-8"));
			//遍历导出数据
			for (Content node : list) {
				String str = node.getPid()+",";
				str += node.getUser()+",";
				str += "\"" + node.getDate()+ "\""+",";
				str += "\"" + (Tools.isNullOrEmpty(node.getLocal()) ? "" : node.getLocal().replace("\"", "\"\""))+ "\""+",";
				str += "\"" + node.getType()+ "\""+",";
				str += "\"" + (Tools.isNullOrEmpty(node.getPath()) ? "" : node.getPath().replace("\"", "\"\""))+ "\""+",";
				str += "\"" + (Tools.isNullOrEmpty(node.getOption()) ? "" : node.getOption().replace("\"", "\"\""))+ "\""+",";
				str += "\"" + (Tools.isNullOrEmpty(node.getContent()) ? "" : node.getContent().replace("\"", "\"\""))+ "\""+",";
				str +=  "\n";
				toClient.write(str.getBytes("utf-8"));
			}
			
			toClient.flush();
			toClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	//TODO 自定义方法

	
	private User getLoginUser(HttpServletRequest req){

		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(config.login_session_key);
		
		return user;
	}
}
