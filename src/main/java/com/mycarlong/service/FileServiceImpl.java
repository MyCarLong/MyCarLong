package com.mycarlong.service;


import com.mycarlong.dto.FileSaveResponse;
import com.mycarlong.utils.FileChecker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * 파일 업로드를 수행하는 서비스 클래스입니다.
 * 작성자: KGH
 * 작성일시: 2024-04-25
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class FileServiceImpl implements FileService{

	private final FileChecker checker;
	/*
	private final AmazonS3Client amazonS3Client;  // uncommente WHEN AWS S3 FILE UPLOAD TEST

	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;
	 */
	@Value("${uploadPath}")
	private String uploadPath;

	@Operation(summary = "Upload a file")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "File uploaded successfully"),
			@ApiResponse(responseCode = "400", description = "Bad request")
	})
	@Override
	public ResponseEntity<?> uploadFile(String title, String author, String fileIndex, String originalFileName, byte[] fileData) {
		if (!checker.isAllowedExtension(fileData)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("fail to Upload file because fileData is Null");
		}
		//TODO: 파일저장시 제목 게시글제목_저자_UUID_FileSetNum.jpg 같은 방식으로 고치기

		UUID uuid = UUID.randomUUID();
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
		String savedFileName = String.format("%s_%s_%s_%s%s", title, author, fileIndex, uuid, extension);
//		String savedFileName = title + "_" + author + "_" +fileIndex+"_"+ uuid + extension;
		String fileUploadFullUrl = uploadPath + savedFileName;  // 'file:' 접두사 제거

		log.info("생성된 파일 업로드 경로입니다: {}", fileUploadFullUrl);

		try (FileOutputStream fos = new FileOutputStream(fileUploadFullUrl)) {
			fos.write(fileData);
			log.info("파일을 저장하였습니다: {}", fileUploadFullUrl);
			FileSaveResponse fResponse=  FileSaveResponse.builder()
					.fileName(originalFileName)
					.fileSavedName(savedFileName)
					.fileUploadFullUrl(fileUploadFullUrl)
					.savedUserName(author)
					.associatedArticleTitle(title)
					.build();


			return ResponseEntity.status(HttpStatus.OK).body(fResponse);
		} catch (Exception e) {
			log.error("파일 저장 중 오류 발생: {}", e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); //"fail to Upload file due to an error";
		} finally {
			uuid = null;
		}
	}


	public String updateFile(String title, String author, String fileIndex, String originalFileName, byte[] fileData) {
		if (!checker.isAllowedExtension(fileData)) {
			return "fail to Upload file because fileData is Null";
		}
		//TODO: 파일저장시 제목 게시글제목_저자_UUID_FileSetNum.jpg 같은 방식으로 고치기

		UUID uuid = UUID.randomUUID();
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
		String savedFileName = String.format("%s_%s_%s_%s%s", title, author, fileIndex, uuid, extension);
		//		String savedFileName = title + "_" + author + "_" +fileIndex+"_"+ uuid + extension;
		String fileUploadFullUrl = uploadPath + savedFileName;  // 'file:' 접두사 제거

		log.info("생성된 파일 업로드 경로입니다: {}", fileUploadFullUrl);

		try (FileOutputStream fos = new FileOutputStream(fileUploadFullUrl)) {
			fos.write(fileData);
			log.info("파일을 저장하였습니다: {}", fileUploadFullUrl);
			FileSaveResponse fResponse=  FileSaveResponse.builder()
					.fileName(originalFileName)
					.fileSavedName(savedFileName)
					.fileUploadFullUrl(fileUploadFullUrl)
					.savedUserName(author)
					.associatedArticleTitle(title)
					.build();
			return "success";
		} catch (Exception e) {
			log.error("파일 저장 중 오류 발생: {}", e.getMessage());
			e.printStackTrace();
			return "fail"; //"fail to Upload file due to an error";
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

	@Override
	public String uploadToS3(String upload, MultipartFile multipartFile) throws IOException {
		return "";
	}

	@Override
	public String modifyFileFromS3(String upload, MultipartFile multipartFile) throws IOException {
		return "";
	}

	@Override
	public void deleteFileFromS3(String fileName) throws IOException {

	}

/*
	private static final String FILE_EXTENSION_SEPARATOR = ".";

	private static String buildFileName(String category, String originalFileName) {
		int fileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR);
		String fileExtension = originalFileName.substring(fileExtensionIndex);
		String fileName = originalFileName.substring(0, fileExtensionIndex);
		String now = String.valueOf(System.currentTimeMillis());

		return category + CATEGORY_PREFIX + fileName + TIME_SEPARATOR + now + fileExtension;
	}*/
}