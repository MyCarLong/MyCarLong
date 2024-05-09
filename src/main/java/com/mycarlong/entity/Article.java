package com.mycarlong.entity;

import com.mycarlong.dto.ArticleDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;


/**
 *Entity Class,  For Generate 'article' Table. <br>
 *
 *Do Not use about hasReplyFlag variable's Setter. <br>
 *
 *Use initializeHasReplyFlag() method to initialize.
 */
@Entity
@Getter @Setter
@Table(name = "article")
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Article extends BaseTimeEntity {

	@Id
	@Column(name = "articleId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;


	@Column(name = "title")
	private String title;


	@Column(name = "content" , length = 255 , columnDefinition = "TEXT")
	private String content;

	@NotEmpty(message = "글쓴이를 입력해주세요.")
	private String author;

	private String category;

	private int hasReply;
	private boolean hasRelplyFlag;

	private int hasImgaes;
	private boolean hasImgaesFlag;

	@OneToMany(mappedBy = "article" ,  cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ArticleImage>  thisImgList;

	@OneToMany(mappedBy = "article" ,  cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Reply> thisReplyList;


	/**
	 * Initializes the 'hasReplyFlag' boolean based on the 'hasReply' value.
	 *
	 * @return true if 'hasReply' is 1, false otherwise
	 */

	//booleand Type 'hasReplyFlag'를 초기화 하는 함수
	public boolean initializeHasReplyFlag() {
		boolean flag = false;
		switch (this.hasReply) {
			case (-1):
				break; // if int type parameter ->(this.hasReply)<- is '-1' pass this switch
			case (1):
				flag = true;
		}
		return this.hasRelplyFlag = flag;
	}
	public void updateThis(ArticleDto articleDto){
		this.title = articleDto.getTitle();
		this.content = articleDto.getContent();
	}
}
