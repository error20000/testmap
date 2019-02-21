package com.jian.map.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jian.annotation.API;
import com.jian.annotation.ParamsInfo;
import com.jian.map.entity.Content;
import com.jian.map.entity.Options;
import com.jian.map.entity.User;
import com.jian.map.service.ContentService;
import com.jian.map.service.OptionsService;
import com.jian.map.service.UserService;
import com.jian.map.util.Utils;
import com.jian.tools.core.CacheObject;
import com.jian.tools.core.CacheTools;
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
	private UserService uService;
	@Autowired
	private OptionsService oService;
	
	private String[] drawTypeNames = {"","Point","Line","Rectangle","Circle","Polygon"};
	
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
	
	/*@RequestMapping("/excel")
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
		//查询用户
		List<User> allUser = uService.findAll();
		List<Options> allOptions = oService.findAll();

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
			String head = "No.,User,Color,Time,Area Type,Area LAT/LONG,Option Comment,User Comment";
			head += "\n";
			toClient.write(head.getBytes("utf-8"));
			//遍历导出数据
			for (Content node : list) {
				User tmp = formatUser(allUser, node.getUser());
				String optStr = formatOptions(allOptions, node.getOption());
				String cStr = formatContent(node.getContent());
				
				String str = node.getPid()+",";
				str += (tmp == null ? node.getUser() : tmp.getUsername())+",";
				str += (tmp == null ? "" : tmp.getColor())+",";
				str += "\"" + node.getDate()+ "\""+",";
//				str += "\"" + (Tools.isNullOrEmpty(node.getLocal()) ? "" : node.getLocal().replace("\"", "\"\""))+ "\""+",";
				str += "\"" + drawTypeNames[node.getType()]+ "\""+",";
				str += "\"" + (Tools.isNullOrEmpty(node.getPath()) ? "" : node.getPath().replace("\"", "\"\""))+ "\""+",";
				str += optStr+",";
				str += "\"" + (cStr.replace("\"", "\"\""))+ "\""+",";
				str +=  "\n";
				toClient.write(str.getBytes("utf-8"));
			}
			
			toClient.flush();
			toClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}*/
	

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
		//查询用户
		List<User> allUser = uService.findAll();
		List<Options> allOptions = oService.findAll();

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
		resp.addHeader("Content-Disposition","attachment;filename=comment.xls");
		resp.setContentType("application/vnd.ms-excel;charset=utf-8");
		try {
			OutputStream toClient = new BufferedOutputStream(resp.getOutputStream());
			//实例化HSSFWorkbook
            HSSFWorkbook workbook = new HSSFWorkbook();
            //创建一个Excel表单，参数为sheet的名字
            HSSFSheet sheet = workbook.createSheet("sheet");

			//设置表头
			String head = "No.,User,Color,Time,Area Type,Area LAT/LONG,Option Comment,User Comment";
			String[] heads = head.split(",");
            HSSFRow row = sheet.createRow(0);
            //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
            for (int i = 0; i <= heads.length; i++) {
            	sheet.setColumnWidth(i, (int)(( 15 + 0.72) * 256)); // 15 在EXCEL文档中实际列宽为14.29
            }
            //设置为居中加粗,格式化时间格式
            HSSFCellStyle style = workbook.createCellStyle();
            HSSFFont font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            style.setDataFormat(HSSFDataFormat.getBuiltinFormat("yyyy/MM/dd HH:mm:ss"));
            //创建表头名称
            HSSFCell cell;
            for (int j = 0; j < heads.length; j++) {
                cell = row.createCell(j);
                cell.setCellValue(heads[j]);
                cell.setCellStyle(style);
            }
			//遍历导出数据
			for (int i = 0; i < list.size(); i++) {
                Content node = list.get(i);
				User tmp = formatUser(allUser, node.getUser());
				String optStr = formatOptions(allOptions, node.getOption());
				String cStr = formatContent(req, node.getContent());

				HSSFRow rowc = sheet.createRow(i+1);
				rowc.createCell(0).setCellValue(node.getPid());
				rowc.createCell(1).setCellValue(tmp == null ? node.getUser()+"" : tmp.getUsername());
				if(tmp != null && !Tools.isNullOrEmpty(tmp.getColor())) {
					byte r = (byte)Integer.parseInt(tmp.getColor().substring(1, 3), 16); 
					byte g = (byte)Integer.parseInt(tmp.getColor().substring(3, 5), 16); 
					byte b = (byte)Integer.parseInt(tmp.getColor().substring(5, 7), 16); 
					//调色板  版号：8-64
					HSSFPalette customPalette = workbook.getCustomPalette();
					customPalette.setColorAtIndex((short)(8+node.getPid()), r, g, b);
					HSSFCellStyle s = workbook.createCellStyle();
					//前景色
					//s.setFillForegroundColor((short)(8+node.getPid()));
					//s.setFillPattern(FillPatternType.SOLID_FOREGROUND);
					//字体色
					HSSFFont f = workbook.createFont();
					f.setColor((short)(8+node.getPid()));
					s.setFont(f);
					
					HSSFCell cellc = rowc.createCell(2);
					cellc.setCellStyle(s);
					cellc.setCellValue(tmp.getColor());
				}else {
					rowc.createCell(2).setCellValue("");
				}
//	            rowc.createCell(2).setCellValue(tmp == null ? "" : tmp.getColor());
				rowc.createCell(3).setCellValue(node.getDate());
				rowc.createCell(4).setCellValue(drawTypeNames[node.getType()]);
				rowc.createCell(5).setCellValue(node.getPath());
				rowc.createCell(6).setCellValue(optStr);
				rowc.createCell(7).setCellValue(cStr);
			}
			workbook.write(toClient);
			workbook.close();
			toClient.flush();
			toClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	//TODO 自定义方法

	private User formatUser(List<User> users, int userId) {
		User temp = null;
		for (User user : users) {
			if(user.getPid() == userId) {
				temp = user;
				break;
			}
		}
		return temp;
	}
	private String formatOptions(List<Options> options, String opts) {
		String temp = "";
		if (Tools.isNullOrEmpty(opts)) {
			return temp;
		}
		String[] strs = opts.split(",");
		for (int i = 0; i < strs.length; i++) {
			String name = "";
			for (Options opt : options) {
				if(opt.getPid() == Tools.parseInt(strs[i])) {
					name = opt.getName();
					break;
				}
			}
			if(Tools.isNullOrEmpty(name)) {
				name = strs[i];
			}
			temp += "\r\n" + name;
		}
		if (Tools.isNullOrEmpty(temp)) {
			return temp;
		}
		return temp.substring("\r\n".length());
	}
	private String formatContent(HttpServletRequest req, String content) {
		String url = req.getRequestURL().toString().split("/api/")[0];
		return content.replaceAll("<[^img][^>]*?>", "").replaceAll("src=\"/", "src=\""+url+"/");
	}
	
	private User getLoginUser(HttpServletRequest req){

//		HttpSession session = req.getSession();
//		User user = (User)session.getAttribute(config.login_session_key);
		
		String userId = req.getHeader("userId");
		if(Tools.isNullOrEmpty(userId)) {
			userId = Tools.getReqParamSafe(req, "userId");
		}
		CacheObject test = CacheTools.getCacheObj("login_user_"+userId);
		User user = (User)test.getValue();
		
		return user;
	}
	
	public static void main(String[] args) {
		int test1 = Integer.parseInt("DC", 16);
		int test2 = Integer.parseInt("14", 16);
		int test3 = Integer.parseInt("14", 16);
		System.out.println(test1);
		System.out.println(test2);
		System.out.println(test3);
		System.out.println((byte)220);
		System.out.println((byte)test1);
		
		
		System.out.println(((15*7+5)/7*256)/256);
		
	}
}
