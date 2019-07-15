package com.vission.mf.base.model.bo;

import org.springframework.web.multipart.MultipartFile;

public class UploadJson {
	private MultipartFile file;

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}
}
