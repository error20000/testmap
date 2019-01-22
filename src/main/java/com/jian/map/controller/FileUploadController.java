package com.jian.map.controller;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jian.map.util.UploadUtils;
import com.jian.tools.core.ResultTools;
import com.jian.tools.core.Tips;
import com.jian.tools.core.Tools;


@Controller
@RequestMapping("/api/file")
public class FileUploadController {
	
	@ResponseBody
	@RequestMapping("/uploadImg")
	public String upload(HttpServletRequest request, HttpServletResponse response, MultipartFile file) {
		String dir = Tools.getReqParamSafe(request, "dir");
		dir = Tools.isNullOrEmpty(dir) ? "" : dir.endsWith("/") ? dir : dir + "/";
		
		try {
			InputStream in = file.getInputStream();
			return UploadUtils.uploadImg(file.getOriginalFilename(), in, dir);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResultTools.custom(Tips.ERROR0).toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/uploadFile")
	public String uploadFile(HttpServletRequest request, HttpServletResponse response, MultipartFile file) {
		String dir = Tools.getReqParamSafe(request, "dir");
		dir = Tools.isNullOrEmpty(dir) ? "" : dir.endsWith("/") ? dir : dir + "/";
		
		try {
			InputStream in = file.getInputStream();
			return UploadUtils.uploadFile(file.getOriginalFilename(), in, dir);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResultTools.custom(Tips.ERROR0).toJSONString();
	}


	/**
	 * 根据请求头解析出文件名 请求头的格式：火狐和google浏览器下：form-data; name="file";filename="snmp4j--api.zip" 
	 * IE浏览器下：form-data; name="file";filename="E:\snmp4j--api.zip"
	 * 
	 * @param header
	 *            请求头
	 * @return 文件名
	 */
	public String getFileName(String header) {
		/**
		 * String[] tempArr1 =
		 * header.split(";");代码执行完之后，在不同的浏览器下，tempArr1数组里面的内容稍有区别
		 * 火狐或者google浏览器下：tempArr1={form-data,name="file",filename="snmp4j--api.zip"}
		 * IE浏览器下：tempArr1={form-data,name="file",filename="E:\snmp4j--api.zip"}
		 */
		String[] tempArr1 = header.split(";");
		/**
		 * 火狐或者google浏览器下：tempArr2={filename,"snmp4j--api.zip"}
		 * IE浏览器下：tempArr2={filename,"E:\snmp4j--api.zip"}
		 */
		String[] tempArr2 = tempArr1[2].split("=");
		// 获取文件名，兼容各种浏览器的写法
		String fileName = tempArr2[1].substring(tempArr2[1].lastIndexOf("\\") + 1).replaceAll("\"", "");
		return fileName;
	}

}
