package com.mycarlong.service;

public interface FileService {

	String uploadFile(String uploadPath, String originalFileName, byte[] fileData);

	void deleteFile(String filePath);
}
