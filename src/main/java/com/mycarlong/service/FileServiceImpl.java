package com.mycarlong.service;


import com.mycarlong.utils.FileChecker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

/**
 * 파일 업로드를 수행하는 서비스 클래스입니다.
 * 작성자: KGH
 * 작성일시: 2024-04-25
 */

@Service
@Slf4j
public class FileServiceImpl implements FileService{

	private final FileChecker checker;

	public FileServiceImpl(){
		this.checker = new FileChecker();
	}


	@Operation(summary = "Upload a file")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "File uploaded successfully"),
			@ApiResponse(responseCode = "400", description = "Bad request")
	})
	@Override
	public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) {
		if (!checker.isAllowedExtension(fileData)) {
			return "fail to Upload file because fileData is Null";
		}

		UUID uuid = UUID.randomUUID();
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
		String savedFileName = uuid + extension;
		String fileUploadFullUrl = uploadPath + "/" + savedFileName;
		log.info("생성된 파일 업로드 경로입니다: {}", fileUploadFullUrl);

		try (FileOutputStream fos = new FileOutputStream(fileUploadFullUrl)) {
			fos.write(fileData);
			log.info("파일을 저장하였습니다: {}", fileUploadFullUrl);
			return savedFileName;
		} catch (Exception e) {
			log.error("파일 저장 중 오류 발생: {}", e.getMessage());
			return "fail to Upload file due to an error";
		}
	}


	@Operation(summary = "Delete a file")
	@Override
	public void deleteFile(String filePath){
		File deleteFile = new File(filePath);
		if(deleteFile.exists()) {
			if (deleteFile.delete()) {
				log.info("파일을 삭제하였습니다: {}", filePath);
			} else {
				log.warn("파일 삭제 실패: {}", filePath);
			}
		} else {
			log.info("파일이 존재하지 않습니다.");
		}
	}

}