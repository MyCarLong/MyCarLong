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


import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "https://mymcl.live")
@Slf4j
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

	private final ArticleServiceImpl articleService;
	private final ReplyServiceImpl replyService;

	private final Logger logger = LoggerFactory.getLogger(BoardController.class);


	@Operation(summary = "Get a list of articles")
	@CrossOrigin(origins = "http://")
	@GetMapping("/article")
	public ResponseEntity<BoardResponseEntity> viewArticleList() {
		//게시글 응답 엔티티를 생성합니다
		BoardResponseEntity responseEntity = BoardResponseEntity.builder()
				.articleList(articleService.findAllArticle()) //findAllArticle()을 통해 모든 게시글을 읽어옵니다.
				.status(HttpStatus.OK)
				.build();
		return new ResponseEntity<>(responseEntity, responseEntity.getStatus());
	}

	@Operation(summary = "Get a list of articles filtered by category")
	@GetMapping("/article/category") //GET매핑
	public ResponseEntity<BoardResponseEntity> viewArticleListFilteredModel(
			@Parameter(description = "Category to filter by")
			@RequestParam String category) {
		//		String category = model+"-"+year;
		String[] model = category.split("_"); //입력받은 카테고리를 쪼개서 model,year 파라미터로 분리합니다.
		BoardResponseEntity responseEntity = BoardResponseEntity.builder().build();

		//카테고리 기준 게시글 조회 결과가 잘 응답받은 경우
		if (articleService.findByCategory(category) != null) {
			responseEntity.setArticleList(articleService.findByCategory(category));
			responseEntity.setStatus(HttpStatus.OK);

		//응답 중 오류로 null이 반환된 경우
		} else if (articleService.findByCategory(category) == null) {
			responseEntity.setErrorDetails(Map.of("parameter", "model-year", "model", model[0], "year", model[1]));
			responseEntity.setStatus(HttpStatus.NOT_FOUND);

		} else { //그 외 알 수 없는 경우는 내부서버 오류로 처리한다.
			responseEntity.setErrorDetails(Map.of("Description", "Internal Server Error"));
			responseEntity.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(responseEntity, responseEntity.getStatus());
	}


	@Operation(summary = "Get details of an article")
	@GetMapping("/article/{articleId}") //게시글 상세보기 지원 메소드
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
	//게시글 쓰기 POST 매핑
	@PostMapping(value = "/article", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<BoardResponseEntity> registArticle(@RequestPart("title") String title,
	                                                         @RequestPart("content") String content,
	                                                         @RequestPart("author") String author,
	                                                         @RequestPart("category") String category,
	                                                         @RequestPart("imgFileList") List<MultipartFile> imgFileList) throws IOException {
		// ArticleDto articleDto) throws IOException { //,@RequestPart("imgFileList") List<MultipartFile> imgFileList
		title = FileNameUtil.sanitizeFileName(title);
		content = FileNameUtil.sanitizeFileName(content);
		author = FileNameUtil.sanitizeFileName(author);
		category = FileNameUtil.sanitizeFileName(category);

		//입력 받은 게시글 정보들을 DTO로 빌드하여 서비스 로직으로 보낸다.
		ArticleFormDto articleDto = ArticleFormDto.builder()
				.title(title)
				.content(content)
				.author(author)
				.category(category)
				.articleImgList(imgFileList)
				.build();
		logger.info("articleDto {} , imgFileList{}", articleDto, articleDto.getArticleImgList()); //imgFileList);
		//게시글 등록 메소드 호출
		articleService.registArticle(articleDto, articleDto.getArticleImgList());//imgFileList); //file upload contains
		// in articleService;
		//마친 후 응답 엔티티 생성
		BoardResponseEntity responseEntity = BoardResponseEntity.builder()
				.status(HttpStatus.OK)
				.build();
		return new ResponseEntity<>(responseEntity, responseEntity.getStatus());
	}

	//삭제는 별도의 엔드포인트를 생성하지 않고 DELETE 메소드를 받아 처리한다.
	@Operation(summary = "Delete an article")
	@DeleteMapping("/article/{articleId}")
	public ResponseEntity<BoardResponseEntity> deleteArticle(
			@Parameter(description = "ID of the article to delete")
			@PathVariable Long articleId, ArticleFormDto articleDto) {
		//게시글 번호와 articleDTO를 기반으로 삭제 로직 실행
		articleService.deleteArticle(articleId, articleDto);
		BoardResponseEntity responseEntity = BoardResponseEntity.builder()
				.status(HttpStatus.OK)
				.build();
		return new ResponseEntity<>(responseEntity, responseEntity.getStatus());
	} //by 'on delete cascade' reply and file delete together.

	@Operation(summary = "Update an article")
	@PutMapping("/article/{articleId}")
	//업데이트는 PUT 메소드를 통해 진행한다.
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
	//댓글 역시 GET 메서드를 통해 일어온다. 엔드포인트는 명사형태를 추구한다.
	public ResponseEntity<BoardResponseEntity> getArticleRegistration(
			@Parameter(description = "ID of the article to get registration for")
			@PathVariable Long articleId) {

		BoardResponseEntity responseEntity = BoardResponseEntity.builder()
				.status(HttpStatus.OK)
				.replyFormDtoList(replyService.getAllReply(articleId))
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

		//입력받은 댓글 폼을 DTO에 담아낸다.
		ReplyFormDto replyFormDto = ReplyFormDto.builder()
				.author(author)
				.content(content)
				.articleId(articleId)
				.build();
        //게시글 등록 로직에 보낸다.
		replyService.registReply(replyFormDto);
		BoardResponseEntity responseEntity = BoardResponseEntity.builder()
				.status(HttpStatus.OK)
				.build();
		return new ResponseEntity<>(responseEntity, responseEntity.getStatus());
	}//must be preceded POST ARTICLE

	@Operation(summary = "Get registration of an article")
	@PutMapping("/article/{articleId}/reply")
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


    //데이터베이스 접근 예외가 발생한 경우 해당 핸들러가 예외를 처리한다.
	@ExceptionHandler(DatabaseAccessException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public DatabaseAccessException.Response handleDatabaseAccessException(DatabaseAccessException e) {
		e.printStackTrace();
		return new DatabaseAccessException.Response("500", e.getMessage());
	}
    //응답과 요청의 데이터 종류가 다르거나 내부오류로 인해 발생하는 예외를 처리한다.
	@ExceptionHandler(DataMismatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public DataMismatchException.Response handleDataMismatchException(DataMismatchException e) {
		e.printStackTrace();
		return new DataMismatchException.Response("400", e.getMessage());
	}
    //게시글, 댓글에 대한 권한이 없는 경우 예외를 처리한다.
	@ExceptionHandler(AuthorizationException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public AuthorizationException.Response handleAuthorizationException(AuthorizationException e) {
		e.printStackTrace();
		return new AuthorizationException.Response("401", e.getMessage());
	}
    //파일 업로드간 예외가 발생할 경우 예외를 핸들링하는 메서드이다.
	@ExceptionHandler(FileUploadException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public FileUploadException.Response handleFileUploadException(FileUploadException e) {
		e.printStackTrace();
		return new FileUploadException.Response("500", e.getMessage());
	}



}