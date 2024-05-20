package com.mycarlong.dto;


import com.mycarlong.entity.ArticleImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ArticleImageDto {
	private Long imageId;

	private String imageOriginName;

	private String imageSavedName;

	private String imageSavedPath;

	private static ModelMapper modelMapper = new ModelMapper();

	private Long articleId;

//	private MultipartFile imgFile;

	public static ArticleImageDto of(ArticleImage articleImg) {
		ArticleImageDto dto = ArticleImageDto.builder()
				.imageId(articleImg.getId())
				.imageOriginName(articleImg.getImageOriginName())
				.imageSavedName(articleImg.getImageSavedName())
				.imageSavedPath(articleImg.getImageSavedPath())
				.articleId(articleImg.getArticle().getId())
				.build();

		return dto;

	}
}
