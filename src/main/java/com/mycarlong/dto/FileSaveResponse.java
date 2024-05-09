package com.mycarlong.dto;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FileSaveResponse {
	private String fileName;
	private String fileSavedName;
	private String fileUploadFullUrl;
	private String savedUserName;
	private String associatedArticleTitle;


}
