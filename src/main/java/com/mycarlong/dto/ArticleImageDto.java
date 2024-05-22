package com.mycarlong.dto;


import com.mycarlong.entity.ArticleImage;
import lombok.*;
import org.modelmapper.ModelMapper;

@Getter @Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ArticleImageDto {
	private Long imageId;

	private String imageOriginName;

	private String imageSavedName;

	private String imageSavedPath;

	private String contentType;

	private static ModelMapper modelMapper = new ModelMapper();

	private Long articleId;

//	private MultipartFile imgFile;

	public static ArticleImageDto of(ArticleImage articleImg) {
		ArticleImageDto dto = ArticleImageDto.builder()
				.imageId(articleImg.getId())
				.imageOriginName(articleImg.getImageOriginName())
				.imageSavedName(articleImg.getImageSavedName())
				.imageSavedPath(articleImg.getImageSavedPath())
				.contentType(articleImg.getFileExtension())
				.articleId(articleImg.getArticle().getId())
				.build();

		return dto;

	}
}
