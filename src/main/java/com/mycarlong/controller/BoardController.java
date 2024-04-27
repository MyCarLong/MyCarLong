package com.mycarlong.controller;


import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/board")
public class BoardController {
	/* 글 조회 */
	@GetMapping("/view/list")
	public List<ArticleDto> viewArticleList() {
		return articleService.findArticleList();
	}
	@GetMapping("/view/detail")
	public ArticleDto viewArticleDetail(@PathVariable Long article_id) {
		return articleService.findArticleDetail();
	}
	/* 글 등록 */
	@PostMapping("/regitst")
	public void registArticle(@RequestBody ArticleDto articleDto) {
		articleService.registerArticle(); //file upload contains in articleService;
	}

	@PostMapping("/reply")
	public void replyArticleRegistration(
			@PathVariable ReplyDto replyDto) {
		articleService.replyArticleRegistration(replyDto);
	}//must be preceded POST ARTICLE
	/* 글 삭제 */
	@DeleteMapping("/del")
	public void deleteArticle(@PathVariable Long article_id) {
		articleService.deleteArticle(article_id);
	} //by 'on delete cascade' reply and file delete together.

 	/* 글 업데이트 */
	@PutMapping("/view/mod/{article_id}")
	public void updateArticle( @RequestBody ArticleDto articleDto
			@PathVariable Long article_id) {
        articleService.updateArticle(article_id);
    }
}
