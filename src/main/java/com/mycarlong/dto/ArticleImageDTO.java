package com.mycarlong.dto;


import com.mycarlong.entity.ArticleImage;
import org.modelmapper.ModelMapper;

public class ArticleImageDTO {
	private Long id;

	private String imageOriginName;

	private String imageSavedName;

	private String imageSavedPath;

	private static ModelMapper modelMapper = new ModelMapper();

	private Long articleId;

	public static ArticleImageDTO of(ArticleImage articleImg) {
		return modelMapper.map(articleImg,ArticleImageDTO.class);
	}
}
