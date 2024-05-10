package com.mycarlong.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@Getter @Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ArticleFormDto {

	@NotBlank(message = "제목은 비워둘 수 없습니다.")
	private String title;

	@NotBlank(message = "내용은 비워둘 수 없습니다.")
	private String content;

	@NotBlank(message = "글쓴이는 비워둘 수 없습니다.")
	private String author;

	private String category;

	@Builder.Default
	private List<MultipartFile> articleImgList = new ArrayList<>();

	private boolean processed;
}
