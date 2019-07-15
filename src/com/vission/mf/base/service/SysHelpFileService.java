package com.vission.mf.base.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vission.mf.base.exception.ServiceException;
import com.vission.mf.base.model.bo.DataGrid;
import com.vission.mf.base.model.po.SysHelpFile;
import com.vission.mf.base.util.FileUtil;

/**
 * 功能/模块 ：帮助文件下载
 */

@Service("sysHelpFileService")
@Transactional
public class SysHelpFileService extends BaseService {

	/**
	 * 将帮助文档的的信息转换成DataGrid类型
	 * @param path
	 * @return
	 */
	public DataGrid findAll(String path) throws ServiceException{
		if(path == null || path.trim().length() == 0){
			throw new ServiceException("未配置帮助文档属性");
		}
		DataGrid dg = new DataGrid();
		List<SysHelpFile> helpFileList = new ArrayList<SysHelpFile>();
		File file = new File(path);
		if(file.exists()){
			if(file.isDirectory()){
				File[] docFiles = file.listFiles();
				for(File docFile : docFiles){
					if(docFile.getName().indexOf(".")>=0){
						SysHelpFile helpFile = new SysHelpFile();
						helpFile.setFileName(docFile.getName());
						helpFile.setType(docFile.getName().substring(docFile.getName().lastIndexOf(".")+1).toUpperCase());
						if(FileUtil.iconMap.containsKey(helpFile.getType())){
							helpFile.setFileIcon(FileUtil.iconMap.get(helpFile.getType()));
						}else{
							helpFile.setFileIcon(FileUtil.iconMap.get("OTH"));
						}
						helpFile.setFileSize(docFile.length());
						helpFileList.add(helpFile);
					}
				}
				if(helpFileList.size() == 0){
					throw new ServiceException("帮助文档路径[" + path + "]下不存在文件，请检查！");
				}
			}else{
				throw new ServiceException("帮助文档路径[" + path + "]不是目录格式，请检查！");
			}
		}else{
			throw new ServiceException("帮助文档路径[" + path + "]不存在，请检查！");
		}
		dg.setRows(helpFileList);
		return dg;
	}
}
