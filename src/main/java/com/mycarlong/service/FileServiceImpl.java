package com.mycarlong.service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.mycarlong.dto.FileSaveResponse;
import com.mycarlong.exception.CustomBoardException;
import com.mycarlong.utils.FileChecker;
import com.rometools.rome.feed.rss.Cloud;
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
	private final AmazonS3 amazonS3;

	/*
	private final AmazonS3Client amazonS3Client;  // uncommente WHEN AWS S3 FILE UPLOAD TEST
    */
	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;

	@Value("${CloudFrontURL}")
	private String cdnUrl;

//	@Value("${uploadPath}")
//	private String uploadPath;

//	@Operation(summary = "Upload a file")
//	@ApiResponses({
//			@ApiResponse(responseCode = "200", description = "File uploaded successfully"),
//			@ApiResponse(responseCode = "400", description = "Bad request")
//	})
//
//	@Override
//	public ResponseEntity<?> uploadFile(String title, String author, String fileIndex, String originalFileName, byte[] fileData) {
//		if (checker.isAllowedExtension(fileData)) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("fail to Upload file because fileData is Null");
//		}
//		//TODO: 파일저장시 제목 게시글제목_저자_UUID_FileSetNum.jpg 같은 방식으로 고치기
//
//		UUID uuid = UUID.randomUUID();
//		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
//		String savedFileName = String.format("%s_%s_%s_%s%s", title, author, fileIndex, uuid, extension);
//		//		String savedFileName = title + "_" + author + "_" +fileIndex+"_"+ uuid + extension;
//		String fileUploadFullUrl = uploadPath + savedFileName;  // 'file:' 접두사 제거
//
//
//		try (FileOutputStream fos = new FileOutputStream(fileUploadFullUrl)) {
//			fos.write(fileData);
//			log.info("파일을 저장하였습니다: {}", fileUploadFullUrl);
//			FileSaveResponse fResponse=  FileSaveResponse.builder()
//					.fileName(originalFileName)
//					.fileSavedName(savedFileName)
//					.fileUploadFullUrl(fileUploadFullUrl)
//					.savedUserName(author)
//					.associatedArticleTitle(title)
//					.fileExtension(extension)
//					.build();
//
//
//			return ResponseEntity.status(HttpStatus.OK).body(fResponse);
//		} catch (Exception e) {
//			log.error("파일 저장 중 오류 발생: {}", e.getMessage());
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); //"fail to Upload file due to an error";
//		} finally {
//			uuid = null;
//		}
//	}


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
	public FileSaveResponse uploadToS3(String title, String author, String fileIndex, String originalFileName, MultipartFile multipartFile) throws IOException {
		if (checker.isAllowedExtension(multipartFile.getBytes())) {
			return new FileSaveResponse(new CustomBoardException.Response("500", "file upload Error"));
		}

		String originalFilename = multipartFile.getOriginalFilename();

		UUID uuid = UUID.randomUUID();
		String extension = multipartFile.getContentType();
		String extensionForSave = originalFileName.substring(originalFileName.lastIndexOf("."));
		String savedFileName = String.format("%s_%s_%s_%s%s", title, author, fileIndex, uuid, extensionForSave);


		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(multipartFile.getSize());
		metadata.setContentType(extension);

		FileSaveResponse saveResponse = FileSaveResponse.builder()
				.fileName(originalFileName)
				.fileSavedName(savedFileName)
				.savedUserName(author)
				.associatedArticleTitle(title)
				.fileExtension(extension)
				.build();
		try{

			//			PutObjectResult putObjectResult = amazonS3.putObject(new PutObjectRequest(
			//					bucketName, savedFileName, multipartFile.getInputStream(),metadata).withCannedAcl(CannedAccessControlList.PublicRead));
			PutObjectResult putObjectResult = amazonS3.putObject(bucketName, savedFileName, multipartFile.getInputStream(),metadata);

			String savedURL = amazonS3.getUrl(bucketName, originalFilename).toString(); // bucket에 저장된 이름으로 URL 반환

			saveResponse.setFileUploadFullUrl(cdnUrl+savedFileName);
			saveResponse.setPutObjectResult(putObjectResult);
			//			amazonS3.putObject(bucketName, originalFilename, multipartFile.getInputStream(), metadata); //실제 파일 업로드
		} catch(IOException e){
			saveResponse.setResponse(new CustomBoardException.Response("500", "fileuploadError"));
		}
		//		이미지 저장경로 =  amazonS3.getUrl(bucketName, imageSavedName).toString()
		return saveResponse;

	}

	@Override
	public String modifyFileFromS3(String upload, MultipartFile multipartFile) throws IOException {
		return "";
	}

	@Override
	public void deleteFileFromS3(String fileName) throws IOException {
		amazonS3.deleteObject(bucketName, fileName);
	}

	public void deleteImage(String originalFilename)  {

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