package com.mycarlong.repository;

import com.mycarlong.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> , QuerydslPredicateExecutor<Article> , PagingAndSortingRepository<Article, Long> {

	//List<Article> findByCategory(); //
	List<Article> findByCategoryOrderByRegTimeDesc(String category);

	Page<Article> findByCategoryOrderByRegTimeDesc(Pageable pageableCategory , String category);

	@Query("SELECT a FROM Article a WHERE a.category = :category ORDER BY a.regTime")
	List<Article> findByCategory(String category);

//	@Query(value="select * from Article i where i.Article_Content like " +
//			"%:ArticleDetail% order by i.content desc", nativeQuery = true)
//	List<Article> findByArticleDetailByNative(@Param("ArticleDetail") String ArticleDetail);

}
