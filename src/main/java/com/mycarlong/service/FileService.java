package com.mycarlong.service;

import com.mycarlong.dto.FileSaveResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface FileService {

//	ResponseEntity<?> uploadFile(String title, String author, String fileIndex, String originalFileName, byte[] fileData);
	void deleteFile(String filePath) throws IOException;

	FileSaveResponse uploadToS3(String title, String author, String fileIndex, String originalFileName, MultipartFile multipartFile) throws IOException;

	String modifyFileFromS3(String upload , MultipartFile multipartFile) throws IOException;
	void deleteFileFromS3(String fileName) throws IOException;
}