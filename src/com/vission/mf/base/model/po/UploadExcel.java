package com.vission.mf.base.model.po;

import org.springframework.web.multipart.MultipartFile;

public class UploadExcel {

	private MultipartFile file;

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}
	
	
}
