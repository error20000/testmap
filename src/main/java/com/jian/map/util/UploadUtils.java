package com.jian.map.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jian.map.App;
import com.jian.map.config.Config;
import com.jian.tools.core.DateTools;
import com.jian.tools.core.ResultKey;
import com.jian.tools.core.ResultTools;
import com.jian.tools.core.Tips;
import com.jian.tools.core.Tools;

@Component
public class UploadUtils {

	private static String imgFiles = "bmp,gif,jpeg,jpg,png,svg";
	private static String docFiles = "csv,doc,docx,pdf,ppt,pptx,txt,xls,xlsx,md";
	private static String movFiles = "3gp,asf,avi,flv,f4v,mov,mkv,mp4,ogg,webm,wmv,ra,ram,rm,rmvb,swf,mp3,aac,wav,flac,wma";
	private static String allowFiles = imgFiles+","+docFiles+","+movFiles+","+"rar,zip,tar,gz,7z,psd,xml";
	
	public static Config baseConfig = null;
	
	@Autowired
	public void setConfig(Config config){
		UploadUtils.baseConfig = config;
//		UploadUtils.imgFiles = config.upload_imgFiles;
//		UploadUtils.docFiles = config.upload_docFiles;
//		UploadUtils.movFiles = config.upload_movFiles;
//		UploadUtils.allowFiles = config.upload_allowFiles;
	}
	
	//--------------------------------------------------------------------------------------------------上传图片
	/**
	 * 图片上传。支持格式  {@link #imgFiles}
	 * @param fileName  文件名，如：test.jpg
	 * @param in	文件的输入流。
	 * @param outDir	文件输出的目录（需要“/”结尾）。
	 * @return json字符串，code=1，表示上传成功。
	 * @see #upload(String, InputStream, String, String)
	 */
	public static String uploadImg(String fileName, InputStream in, String outDir){
		return upload(fileName, in, outDir, imgFiles);
	}

	//--------------------------------------------------------------------------------------------------上传文档
	/**
	 * 文档上传。支持格式  {@link #docFiles}
	 * @param fileName  文件名，如：test.doc
	 * @param in	文件的输入流。
	 * @param outDir	文件输出的目录（需要“/”结尾）。
	 * @return json字符串，code=1，表示上传成功。
	 * @see #upload(String, InputStream, String, String)
	 */
	public static String uploadDoc(String fileName, InputStream in, String outDir){
		return upload(fileName, in, outDir, docFiles);
	}

	//--------------------------------------------------------------------------------------------------上传文件
	/**
	 * 文件上传。支持格式  {@link #allowFiles}
	 * @param fileName  文件名，如：test.doc
	 * @param in	文件的输入流。
	 * @param outDir	文件输出的目录（需要“/”结尾）。
	 * @return json字符串，code=1，表示上传成功。
	 * @see #upload(String, InputStream, String, String)
	 */
	public static String uploadFile(String fileName, InputStream in, String outDir){
		return upload(fileName, in, outDir, allowFiles);
	}

	//--------------------------------------------------------------------------------------------------上传视频
	/**
	 * 文件上传。支持格式  {@link #movFiles}
	 * @param fileName  文件名，如：test.doc
	 * @param in	文件的输入流。
	 * @param outDir	文件输出的目录（需要“/”结尾）。
	 * @return json字符串，code=1，表示上传成功。
	 * @see #upload(String, InputStream, String, String)
	 */
	public static String uploadMov(String fileName, InputStream in, String outDir){
		return upload(fileName, in, outDir, movFiles);
	}
	
	
	
	//--------------------------------------------------------------------------------------------------基础上传
	/**
	 * 基础上传文件
	 * @param fileName  文件名，如：test.jpg
	 * @param in	文件的输入流。
	 * @param outDir	文件输出的目录（需要“/”结尾）。
	 * @param limit		文件类型限制，如：jpg,png,doc,txt...。多个逗号分隔。
	 * @return json字符串，code=1，表示上传成功。
	 * @see #upload(String, InputStream, String)
	 */
	public static String upload(String fileName, InputStream in, String outDir, String limit){
		//判断类型
		String suffix = fileName.substring(fileName.lastIndexOf(".")+1);
		if(!(limit.toLowerCase()+",").contains(suffix.toLowerCase()+",")){
			return ResultTools.custom(Tips.ERROR212).toJSONString();
		}
		//上传
		Map<String, Object> res = upload(fileName, in, outDir);
		if(res == null){
			return  ResultTools.custom(Tips.ERROR0).toJSONString();
		}
		return ResultTools.custom(Tips.ERROR1).put(ResultKey.DATA, res).toJSONString();
	}
	
	/**
	 * 基础上传文件
	 * @param fileName  文件名，如：test.txt
	 * @param in	文件的输入流。
	 * @param outDir	文件输出的目录（需要“/”结尾）。
	 * @return	map 文件相关信息，如文件大小、后缀、上传名称、上传路径。
	 */
	public static Map<String, Object> upload(String fileName, InputStream in, String outDir){
		
		//生成输出文件
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		
		int random = new Random().nextInt(10000);
		String str = random+"";
		for (int i = 0; i <  4 - str.length(); i++) {
			str = "0" + str;
		}
		String outFileName = DateTools.formatDate("yyyyMMddHHmmssSSS") + str + "."+ suffix;
		String outFilePath = outDir + DateTools.formatDate("yyyyMMdd") + "/" + outFileName;
		String basePath = Tools.isNullOrEmpty(baseConfig.upload_path) ? App.rootPath + "upload/" : baseConfig.upload_path;
		basePath = basePath.endsWith("/") ? basePath : basePath + "/";
		
		//上传
		Map<String, Object> res = null;
		try {
			res = new HashMap<>();
			File outFile = new File(basePath + outFilePath);
			File pfile = outFile.getParentFile();
			if(!pfile.exists()){
				pfile.mkdirs();
			}
			OutputStream out = new FileOutputStream(outFile);
			byte[] buffer = new byte[1024];
			long size = 0;
			int count = -1;
			while ((count = in.read(buffer)) != -1) {
				out.write(buffer, 0, count);
				size += count;
			}
			in.close();
			out.close();
			out.flush();
			//返回结果
			res.put("type", suffix);
			res.put("size", size);
			res.put("name", outFileName);
			res.put("path", outFilePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return res;
	}
}
