package com.mycarlong.repository;

import com.mycarlong.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> , QuerydslPredicateExecutor<Article> , PagingAndSortingRepository<Article, Long> {
	List<Article> findByTitle(String title);  //제목으로 찾기

	List<Article> findByContent(String content);  //제목으로 찾기

	List<Article> findByTitleOrContent(String title, String content); //제목 혹은 내용으로 찾기

	List<Article> findByCategory(String category); //카테고리로 찾기

	List<Article> findByModelAndYear(String category);

	Page<Article> findByModelAndYear(Pageable pageable);

	//	@Query("select i from Article i where i.ArticleContent like " +
//			"%:ArticleDetail% order by i.price desc")
//	List<Article> findByArticleDetail(@Param("ArticleDetail") String ArticleDetail);
//
//	@Query(value="select * from Article i where i.Article_Content like " +
//			"%:ArticleDetail% order by i.content desc", nativeQuery = true)
//	List<Article> findByArticleDetailByNative(@Param("ArticleDetail") String ArticleDetail);

}
