package com.mycarlong.controller;


import com.mycarlong.dto.ArticleDto;
import com.mycarlong.dto.ReplyDto;
import com.mycarlong.service.ArticleImageServiceImpl;
import com.mycarlong.service.ArticleServiceImpl;
import com.mycarlong.service.ReplyServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

	private final ArticleServiceImpl articleService;
	private final ReplyServiceImpl replyService;


	@GetMapping("/articles")
	public List<ArticleDto> viewArticleList() {
		return articleService.findAllArticle();
	}

	@GetMapping("/articles?year={year}&model={model}")
	public List<ArticleDto> viewArticleListFilteredModel(
			@PathVariable String year,
            @PathVariable String model) {
		String category = model+"-"+year;
		return articleService.findByModelAndYear(category);
	}


	@GetMapping("/article/{articleId}")
	public ArticleDto viewArticleDetail(@PathVariable Long articleId) {
		return articleService.viewArticleDetail(articleId);
	}

	@PostMapping("/articles")
	public void registArticle(@RequestBody ArticleDto articleDto ,
	                          @RequestParam("imgFiles") List<MultipartFile> imgFileList) throws IOException {
		articleService.registArticle(articleDto, imgFileList); //file upload contains in articleService;
	}


	@DeleteMapping("/article/{articleId}")
	public void deleteArticle(@PathVariable Long articleId, ArticleDto articleDto) {
		articleService.deleteArticle(articleId, articleDto);
	} //by 'on delete cascade' reply and file delete together.

	@PutMapping("/article/{articleId}")
	public void updateArticle(@PathVariable Long articleId,
		                          @RequestBody ArticleDto articleDto) {
	        articleService.modifyArticle(articleId,articleDto);
	    }

	@PostMapping("/article/{articleId}/reply")
	public void replyArticleRegistration(@PathVariable Long articleId,
	                                     @RequestBody ReplyDto replyDto) {
		replyDto.setArticleId(articleId);
		replyService.registReply(replyDto);
	}//must be preceded POST ARTICLE



}