package com.mycarlong.repository;

import com.mycarlong.entity.ArticleImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleImageRepository extends JpaRepository<ArticleImage, Long> {
	String returnImgPath(Long articleId);  //제목으로 찾기
}
