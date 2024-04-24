package com.mycarlong.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleImage {

	@Id @Column(name = "image_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String imageOriginName;

	private String imageSavedName;

	private String imageSavedPath;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "article_id")
	private Article articleId;

	public void updateArticleImg(String imageOriginName, String imageSavedName, String imageSavedPath){
		this.imageOriginName = imageOriginName;
		this.imageSavedName = imageSavedName;
		this.imageSavedPath = imageSavedPath;
	}
}
