package com.mycarlong.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

	String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws IOException;

	void deleteFile(String filePath) throws IOException;

	String uploadToS3(String upload ,  MultipartFile multipartFile) throws IOException;
	String modifyFileFromS3(String upload ,  MultipartFile multipartFile) throws IOException;
	void deleteFileFromS3(String fileName) throws IOException;
}
