package com.mycarlong.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter @Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Reply extends BaseTimeEntity {

	@Id @Column(name = "reply_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String author;

	@Column(length = 100)
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "articleId")
	private Article article;

}
