package com.mycarlong.service;


import com.mycarlong.dto.ArticleDto;
import com.mycarlong.entity.Article;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@Slf4j
@SpringBootTest
@RequiredArgsConstructor
public class ArticleServiceImplTests {

    @Autowired
    private ArticleServiceImpl articleService;

    public void createArticle() {
        Article article = Article.builder()
                .title("테스트작성자")
                .content("테스트내용")
                .author("테스트글쓴이")
                .category("질문")
                .build();

        ArticleDto articleDto = ArticleDto.of(article);
        articleService.registArticle(articleDto);
    }
    @Test
    public void 아티클_모두_읽어오기테스트() {
        //Given
        for (int i = 0; i < 10; i++) {
            createArticle();
        }
        //When
        List<ArticleDto> articleDtos = articleService.findAllArticle();

        //Then
        for (ArticleDto articleDto : articleDtos) {
            log.info(articleDto.toString());
        }
    }

}
