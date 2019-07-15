package com.vission.mf.base.model.po;

import java.text.DecimalFormat;

import com.vission.mf.base.BaseEntity;

/**
* 功能/模块 ：系统菜单实体类
 */

public class SysHelpFile extends BaseEntity{

	private static final long serialVersionUID = 1L;
	private String fileName;
	private String fileIcon;
	private long fileSize;
	private String type;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFileIcon() {
		return fileIcon;
	}

	public void setFileIcon(String fileIcon) {
		this.fileIcon = fileIcon;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	
	public String getStrFileSize(){
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = ""; 
        if (fileSize < 1024) {
            fileSizeString = df.format((double) fileSize) + "B";
        } else if (fileSize < 1024*1024) {
            fileSizeString = df.format((double) fileSize / 1024) + "K";
        } else if (fileSize < 1024*1024*1024) {
            fileSizeString = df.format((double) fileSize / (1024*1024)) + "M";
        } else {
            fileSizeString = df.format((double) fileSize / (1024*1024*1024)) + "G";
        }
        return fileSizeString;
	}
}
