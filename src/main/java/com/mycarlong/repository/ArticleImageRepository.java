package com.mycarlong.repository;

import com.mycarlong.entity.ArticleImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleImageRepository extends JpaRepository<ArticleImage, Long> {
//	List<ArticleImage> findByArticleId(Long article);
//
//	ArticleImage findByArticleIdAndImageSetNum(Long article, int imageSetNum);  //TODO: 여기서 오류 발생
}
