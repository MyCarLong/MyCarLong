package com.mycarlong.controller;


import com.mycarlong.exception.*;
import com.mycarlong.dto.ArticleFormDto;
import com.mycarlong.dto.BoardResponseEntity;
import com.mycarlong.dto.ReplyFormDto;
import com.mycarlong.service.ArticleServiceImpl;
import com.mycarlong.service.ReplyServiceImpl;
import com.mycarlong.utils.FileNameUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

	private final ArticleServiceImpl articleService;
	private final ReplyServiceImpl replyService;

	private Logger logger = LoggerFactory.getLogger(BoardController.class);


	@Operation(summary = "Get a list of articles")
	@GetMapping("/article")
	public ResponseEntity<BoardResponseEntity> viewArticleList() {
		BoardResponseEntity responseEntity = BoardResponseEntity.builder()
				.articleList(articleService.findAllArticle())
				.status(HttpStatus.OK)
				.build();
		return new ResponseEntity<>(responseEntity, responseEntity.getStatus());
	}

	@Operation(summary = "Get a list of articles filtered by category")
	@GetMapping("/article?category={category}")
	public ResponseEntity<BoardResponseEntity> viewArticleListFilteredModel(
			@Parameter(description = "Category to filter by")
			@RequestParam String category) {
		//		String category = model+"-"+year;
		String[] model = category.split("-");
		BoardResponseEntity responseEntity = BoardResponseEntity.builder().build();
		if (articleService.findByCategory(category) != null) {
			responseEntity.setArticleList(articleService.findByCategory(category));
			responseEntity.setStatus(HttpStatus.OK);
		} else if (articleService.findByCategory(category) == null) {
			responseEntity.setErrorDetails(Map.of("parameter", "model-year", "model", model[0], "year", model[1]));
			responseEntity.setStatus(HttpStatus.NOT_FOUND);
		} else {
			responseEntity.setErrorDetails(Map.of("Description", "Internal Server Error"));
			responseEntity.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(responseEntity, responseEntity.getStatus());
	}


	@Operation(summary = "Get details of an article")
	@GetMapping("/article/{articleId}")
	public ResponseEntity<BoardResponseEntity> viewArticleDetail(
			@Parameter(description = "ID of the article to get details for")
			@PathVariable Long articleId) {
		BoardResponseEntity responseEntity = BoardResponseEntity.builder()
				.article(articleService.viewArticleDetail(articleId))
				.status(HttpStatus.OK)
				.build();
		return new ResponseEntity<>(responseEntity, responseEntity.getStatus());
	}

	@Operation(summary = "Register a new article")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Article registered successfully",
					content = {@Content(mediaType = "application/json",
							schema = @Schema(implementation = BoardResponseEntity.class))}),
			@ApiResponse(responseCode = "400", description = "Invalid input data",
					content = @Content)})
	@PostMapping(value = "/article", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<BoardResponseEntity> registArticle(@RequestParam("title") String title,
	                                                         @RequestParam("content") String content,
	                                                         @RequestParam("author") String author,
	                                                         @RequestParam("category") String category,
	                                                         @RequestPart("imgFileList") List<MultipartFile> imgFileList) throws IOException {
		// ArticleDto articleDto) throws IOException { //,@RequestPart("imgFileList") List<MultipartFile> imgFileList
		title = FileNameUtil.sanitizeFileName(title);
		content = FileNameUtil.sanitizeFileName(content);
		author = FileNameUtil.sanitizeFileName(author);
		category = FileNameUtil.sanitizeFileName(category);

		ArticleFormDto articleDto = ArticleFormDto.builder()
				.title(title)
				.content(content)
				.author(author)
				.category(category)
				.articleImgList(imgFileList)
				.build();
		logger.info("articleDto {} , imgFileList{}", articleDto, articleDto.getArticleImgList()); //imgFileList);
		articleService.registArticle(articleDto, articleDto.getArticleImgList());//imgFileList); //file upload contains
		// in articleService;
		BoardResponseEntity responseEntity = BoardResponseEntity.builder()
				.status(HttpStatus.OK)
				.build();
		return new ResponseEntity<>(responseEntity, responseEntity.getStatus());
	}


	@Operation(summary = "Delete an article")
	@DeleteMapping("/article/{articleId}")
	public ResponseEntity<BoardResponseEntity> deleteArticle(
			@Parameter(description = "ID of the article to delete")
			@PathVariable Long articleId, ArticleFormDto articleDto) {
		articleService.deleteArticle(articleId, articleDto);
		BoardResponseEntity responseEntity = BoardResponseEntity.builder()
				.status(HttpStatus.OK)
				.build();
		return new ResponseEntity<>(responseEntity, responseEntity.getStatus());
	} //by 'on delete cascade' reply and file delete together.

	@Operation(summary = "Update an article")
	@PutMapping("/article/{articleId}")
	public ResponseEntity<BoardResponseEntity> updateArticle(
			@Parameter(description = "ID of the article to update")
			@PathVariable Long articleId,
			@Parameter(description = "Updated article data")
			@RequestBody ArticleFormDto articleDto) {
		articleService.modifyArticle(articleId, articleDto);
		BoardResponseEntity responseEntity = BoardResponseEntity.builder()
				.status(HttpStatus.OK)
				.build();
		return new ResponseEntity<>(responseEntity, responseEntity.getStatus());
	}

	//Reply Controll Part
	@Operation(summary = "Get registration of an article")
	@GetMapping("/article/{articleId}/reply")
	public ResponseEntity<BoardResponseEntity> getArticleRegistration(
			@Parameter(description = "ID of the article to get registration for")
			@PathVariable Long articleId) {
		replyService.getAllReply(articleId);
		BoardResponseEntity responseEntity = BoardResponseEntity.builder()
				.status(HttpStatus.OK)
				.build();
		return new ResponseEntity<>(responseEntity, responseEntity.getStatus());
	}

	@Operation(summary = "Register a reply to an article")
	@PostMapping("/article/{articleId}/reply")
	public ResponseEntity<BoardResponseEntity> replyArticleRegistration(
			@Parameter(description = "ID of the article to reply to")
			@PathVariable Long articleId,
			@Parameter(description = "Author of the reply")
			@RequestParam("author") String author,
			@Parameter(description = "Content of the reply")
			@RequestParam("content") String content) {
		//@RequestPart ReplyFormDto replyFormDto) {
		ReplyFormDto replyFormDto = ReplyFormDto.builder()
				.author(author)
				.content(content)
				.articleId(articleId)
				.build();

		replyService.registReply(replyFormDto);
		BoardResponseEntity responseEntity = BoardResponseEntity.builder()
				.status(HttpStatus.OK)
				.build();
		return new ResponseEntity<>(responseEntity, responseEntity.getStatus());
	}//must be preceded POST ARTICLE

	@Operation(summary = "Get registration of an article")
	@PutMapping("/article/{articleId}/reply/{replyId}")
	public ResponseEntity<BoardResponseEntity> updateArticleRegistration(
			@Parameter(description = "ID of the article to get registration for")
			@PathVariable Long articleId,
			@Parameter(description = "Content of the reply")
			@RequestBody ReplyFormDto replyFormDto) {
		replyService.modifyReply(articleId,replyFormDto);
		BoardResponseEntity responseEntity = BoardResponseEntity.builder()
				.status(HttpStatus.OK)
				.build();
		return new ResponseEntity<>(responseEntity, responseEntity.getStatus());
	}

	@Operation(summary = "Get registration of an article")
	@DeleteMapping("/article/{articleId}/reply/{replyId}")
	public ResponseEntity<BoardResponseEntity> deleteReplyFromAritcle(
			@Parameter(description = "ID of the reply to  Delete for")
			@PathVariable Long articleId,
			@PathVariable Long replyId) {
		replyService.deleteReply(articleId,replyId);
		BoardResponseEntity responseEntity = BoardResponseEntity.builder()
				.status(HttpStatus.OK)
				.build();
		return new ResponseEntity<>(responseEntity, responseEntity.getStatus());
	}



	@ExceptionHandler(DatabaseAccessException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public DatabaseAccessException.Response handleDatabaseAccessException(DatabaseAccessException e) {
		e.printStackTrace();
		return new DatabaseAccessException.Response("500", e.getMessage());
	}

	@ExceptionHandler(DataMismatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public DataMismatchException.Response handleDataMismatchException(DataMismatchException e) {
		e.printStackTrace();
		return new DataMismatchException.Response("400", e.getMessage());
	}

	@ExceptionHandler(AuthorizationException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public AuthorizationException.Response handleAuthorizationException(AuthorizationException e) {
		e.printStackTrace();
		return new AuthorizationException.Response("401", e.getMessage());
	}

	@ExceptionHandler(FileUploadException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public FileUploadException.Response handleFileUploadException(FileUploadException e) {
		e.printStackTrace();
		return new FileUploadException.Response("500", e.getMessage());
	}



}