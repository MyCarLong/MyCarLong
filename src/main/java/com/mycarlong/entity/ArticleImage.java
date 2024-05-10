package com.mycarlong.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleImage {

	@Id @Column(name = "imageId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String imageOriginName;

	private String imageSavedName;

	private String imageSavedPath;

	private int imageSetNum;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	@JoinColumn(name = "articleId")
	private Article article;

	public void updateArticleImg(String imageOriginName, String imageSavedName, String imageSavedPath){
		this.imageOriginName = imageOriginName;
		this.imageSavedName = imageSavedName;
		this.imageSavedPath = imageSavedPath;
	}

//	//articleId로 일치하는 Img 찾기
//	public String returnImgPath (Long articleId) {
//		String imgPath;
//		if (this.articleId.getId().equals(articleId)) {
//			imgPath = this.imageSavedPath + this.imageSavedName;
//		}else {
//			imgPath = null;
//		}
//		return imgPath;
//	}
}

