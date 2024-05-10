package com.mycarlong.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
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
	@JsonBackReference
	@JoinColumn(name = "articleId")
	private Article article;

}
