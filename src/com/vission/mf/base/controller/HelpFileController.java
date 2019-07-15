package com.vission.mf.base.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vission.mf.base.enums.BaseConstants;
import com.vission.mf.base.exception.ServiceException;
import com.vission.mf.base.model.bo.DataGrid;
import com.vission.mf.base.service.SysHelpFileService;
import com.vission.mf.base.util.FileUtil;

/**
 * 功能/模块 ：帮助文件下载
 */

@Controller
@RequestMapping("/helpfile")
public class HelpFileController extends BaseController {

	@Autowired
	private SysHelpFileService sysHelpFileService;

	@RequestMapping("/list")
	public String list() {
		return "base/helpFile";
	}

	@RequestMapping("/findAll")
	@ResponseBody
	public DataGrid findAll(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		String path = BaseConstants.getHelpFilePath().replaceAll("\\\\", "/");
		return sysHelpFileService.findAll(path);
	}

	@RequestMapping("/download")
	public void download(HttpServletRequest request,
			HttpServletResponse response, String fileName)
			throws ServiceException {
		response.setCharacterEncoding("UTF-8");
		try {
			String path = BaseConstants.getHelpFilePath().replaceAll("\\\\", "/");
			File file = new File(path+"/"+fileName);
			if(file.exists()){
				FileUtil.download(fileName, path+"/"+fileName, request, response);
			}else{
				String name =new String(fileName.getBytes("ISO-8859-1"),"UTF-8");
				file = new File(path+"/"+name);
				if(file.exists()){
					FileUtil.download(name, path+"/"+name, request, response);
				}
			}					
		} catch (Exception e) {
			throw new ServiceException(e);
		}		
	}
}
