package com.mycarlong.controller;


import com.mycarlong.dto.ArticleDto;
import com.mycarlong.service.ArticleImageServiceImpl;
import com.mycarlong.service.ArticleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

	private final ArticleServiceImpl articleService;
	private final ArticleImageServiceImpl articleImageService;

	/* 글 조회 */
	@GetMapping("/view/list")
	public List<ArticleDto> viewArticleList() {
		return articleService.findAllArticle();
	}
//
//
	@GetMapping("/view/detail/{articleId}")
	public ArticleDto viewArticleDetail(@PathVariable Long articleId) {
		return articleService.viewArticleDetail(articleId);
	}
	/* 글 등록 */
	@PostMapping("/new")
	public void registArticle(@RequestBody ArticleDto articleDto ,
	                          @RequestParam("imgFiles") List<MultipartFile> imgFileList) throws IOException {
		articleService.registArticle(articleDto, imgFileList); //file upload contains in articleService;

	}

//	@PostMapping("/reply")
//	public void replyArticleRegistration(
//			@PathVariable ReplyDto replyDto) {
//		articleService.replyArticleRegistration(replyDto);
//	}//must be preceded POST ARTICLE
	/* 글 삭제 */
	@DeleteMapping("/del/{articleId}")
	public void deleteArticle(@PathVariable Long articleId, ArticleDto articleDto) {
		articleService.deleteArticle(articleId, articleDto);
	} //by 'on delete cascade' reply and file delete together.
//
// 	/* 글 업데이트 */
//	@PutMapping("/view/mod/{article_id}")
//	public void updateArticle( @RequestBody ArticleDto articleDto,
//			@PathVariable Long article_id) {
//        articleService.updateArticle(article_id);
//    }
//}
}