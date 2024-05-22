package com.mycarlong.dto;


import com.amazonaws.services.s3.model.PutObjectResult;
import com.mycarlong.exception.CustomBoardException;
import lombok.*;

@Builder
@Getter @Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class FileSaveResponse {
	private String fileName;
	private String fileSavedName;
	private String fileUploadFullUrl;
	private String savedUserName;
	private String associatedArticleTitle;
	private String fileExtension;

	private CustomBoardException.Response response;

	private PutObjectResult putObjectResult;

	public boolean isEmpty() {
		return fileName == null && fileSavedName == null && fileUploadFullUrl == null && savedUserName == null && associatedArticleTitle == null && fileExtension == null && response == null && putObjectResult == null;
	}

	public FileSaveResponse(CustomBoardException.Response response){
		this.response = response;
	}


}
