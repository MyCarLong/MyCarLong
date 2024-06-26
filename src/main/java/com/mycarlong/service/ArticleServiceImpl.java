package com.mycarlong.service;

import com.mycarlong.controller.BoardController;
import com.mycarlong.dto.ArticleImageDto;
import com.mycarlong.exception.*;
import com.mycarlong.dto.ArticleDto;
import com.mycarlong.dto.ArticleFormDto;
import com.mycarlong.entity.Article;
import com.mycarlong.entity.ArticleImage;
import com.mycarlong.repository.ArticleImageRepository;
import com.mycarlong.repository.ArticleRepository;
import com.mycarlong.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

	private final ArticleRepository articleRepository;
	private final ArticleImageServiceImpl articleImageService;
	private final ArticleImageRepository articleImageRepository;
	private final ReplyRepository replyRepository;

	private final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);

	@Override
	public List<ArticleDto> findAllArticle() {
		try {
			List<Article> articleList = articleRepository.findAll();
			List<ArticleDto> articleDtoList = new ArrayList<>();
			for (Article article : articleList) {
				List<ArticleImageDto> imageDtoList = article.getArticleImageList().stream()
						.map(ArticleImageDto::of)
						.collect(Collectors.toList());
				ArticleDto articleDto = ArticleDto.of(article);
				articleDto.setArticleImageList(imageDtoList);
				articleDtoList.add(articleDto);
			}
			return articleDtoList;
		} catch (Exception e) {
			throw new DatabaseAccessException("데이터베이스 접근 에러발생", e);
		}
	}



	//	@Override
	//	public List<ArticleDto> findFiftyArticldOrderByDesc() {
	//		return pagingArticle(20, "null");  //내부에서 이미 예외처리 하므로 별도 예외처리 X
	//	}

	@Override
	@Transactional
	public void registArticle(ArticleFormDto articleDto, List<MultipartFile> imgFileList) {
		try {
			Article article = articleDto.createArticle();
			//이미지 업로드가 없어도 게시글 등록은 되어야 하기 때문에 null이거나 비어있을 경우 이미지 등록을 실행한다.
			if (imgFileList != null && !imgFileList.isEmpty()) {
				for (int i = 0; i < imgFileList.size(); i++) {
					ArticleImage articleImage = new ArticleImage(); //각 반복마다 새로운 이미지 Entity를 만든다
					articleImage.setArticle(article); //article을 매핑해준다
					articleImage.setImageSetNum(i);   //이미지의 세트넘버를 설정해준다
					logger.info(" Article Image = {}", articleImage);
					//				articleImageService.saveArticleImg(article, String.valueOf(i), imgFileList.get(i));
					articleImageService.saveArticleImgS3(article, String.valueOf(i), imgFileList.get(i));

				}
			}
			//			article.setThisImgList(articleImageList);
			articleRepository.save(article);
		} catch (Exception e) {
			throw new FileUploadException("File Upload 혹은 이미지 등록 중 에러발생", e);
		}

	}


	@Override
	public void modifyArticle(Long articleId, ArticleFormDto articleDto) {
		// 게시글 조회 , null이 아닌 경우만 반환
		Article existingArticle = articleRepository.findById(articleId).orElse(null);
//		verifyUser(articleDto); //요청유저 = 작성자인지 확인한다.
		// 2. 데이터 수정
		assert existingArticle != null;
		existingArticle.updateThis(articleDto);
		// 3. 데이터 저장
		articleRepository.save(existingArticle);
	}

	@Override
	public void deleteArticle(Long articleId, ArticleFormDto articleDto) {
//		verifyUser(articleDto); //요청자가 게시글 작성자와 동일한지 확인한다.
		Article existingArticle = articleRepository.findById(articleId).orElse(null); // 게시글 조회를 해보고
		if (existingArticle == null) { // 조회 후 게시글이 없다면 예외처리
			throw new DataMismatchException("제공된 Article ID에 해당하는 게시글을 찾을 수 없어 삭제가 완료되지않았습니다..", null);
		}
		articleRepository.deleteById(articleId); //있다면 삭제를 진행한다.
	}

//	/*
//	 * 요청자가 author인 경우에만 게시글의 modify , delete를 가능케 한다. <br>
//	 *
//	 * 요청자를 확인하는 함수 verifyUser()
//	 * */
//	private void verifyUser(ArticleFormDto articleDto) {
//		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); // 시큐리티홀더에서 인증정보를 받아온다
//		if (principal instanceof UserDetails) { //UserDetails 객체라면
//			String username = ((UserDetails) principal).getUsername(); //객체에서 이름을 추출한다.
//			if (!username.equals(articleDto.getAuthor())) {  //이름 비교로 게시자와 동일한지 확인
//				throw new AuthorizationException("게시글에 대한 권한이 없는 유저입니다..", null);
//			}
//		}
//	}


	@Override
	public ArticleDto viewArticleDetail(Long articleId) {
		Article article = articleRepository.findById(articleId).orElse(null); //Optional 객체로 반환하기 때문에 orElse 추가
		if (article == null) {
			throw new DataMismatchException("제공된 Article ID에 해당하는 게시글을 찾을 수 없습니다.", null);
		}
		ArticleDto articleDto = ArticleDto.of(article);  //dto 생성
		articleDto.setArticleImageList(article.getArticleImageList().stream().map(ArticleImageDto::of).toList()); // dto 에 이미지리스트 삽입
		return articleDto;
	}


	//	@Override
	//	public List<ArticleDto> findByCategory(String category) {
	//
	//		List<Article> articleList = articleRepository.findByCategory(category);
	////		articleList.sort(Comparator.comparing(Article::getRegTime));
	//
	//		List<ArticleDto> articleDtoList = new ArrayList<>();
	//		for (Article article : articleList) {
	//			List<ArticleImage> foundArticleImages = article.getArticleImageList();//articleImageRepository.findByArticleId(article.getId());
	//			logger.info("foundArticleImages 확인하기:{}", article.getArticleImageList());
	//			List<ArticleImageDto> foundArticleImagesDto = new ArrayList<>();
	//			for (ArticleImage articleImage: foundArticleImages) {
	//				//logger.info("articleImage 이름 , 연관 아티클아이디 확인하기:{}", articleImage.getImageSavedName(), articleImage.getArticle().getId());
	//				ArticleImageDto articleImageDto = ArticleImageDto.builder()
	//						.imageId(articleImage.getId())
	//						.imageOriginName(articleImage.getImageOriginName())
	//						.imageSavedName(articleImage.getImageSavedName())
	//						.imageSavedPath(articleImage.getImageSavedPath())
	//						.articleId(articleImage.getArticle().getId())
	//						.build();
	//				foundArticleImagesDto.add(articleImageDto);
	//			}
	//
	//			ArticleDto newArticleDto = ArticleDto.of(article);
	//			newArticleDto.setArticleImageList(foundArticleImagesDto);
	//			articleDtoList.add(newArticleDto);
	//		}
	//		return articleDtoList;
	//	}

	@Override
	public List<ArticleDto> findByCategory(String category) {
		List<Article> articleList = articleRepository.findByCategory(category);
		return articleList.stream().map(article -> { //list를 stream으로 풀어 헤치고 map()으로 풀어헤친 결과를
			//이미지 DTO클래스의 묶음을 만들어 담아내는데 사용하고,
			List<ArticleImageDto> foundArticleImagesDto = article.getArticleImageList().stream()
					.map(ArticleImageDto::of)
					.collect(Collectors.toList());
			ArticleDto newArticleDto = ArticleDto.of(article); // 게시글 DTO클래스로도 바꾼다음
			newArticleDto.setArticleImageList(foundArticleImagesDto);
			return newArticleDto;   // DTO를 반환하는데
		}).collect(Collectors.toList());  //반환된 DTO를 list로 다시 묶어낸다
	}



	private List<ArticleDto> imageDtoInjection(List<Article> articleList, List<ArticleDto> articleDtoList) {
		for (Article article : articleList) {
			List<ArticleImage> articleImages = articleImageRepository.findByArticleId(article.getId());
			logger.info("articleImages 불러온 결과 ! : {}", articleImages);
			List<ArticleImageDto> imageDtos = articleImages.stream().map(ArticleImageDto::of).toList();

			ArticleDto articleDto = ArticleDto.of(article);    // convert
			articleDto.setArticleImageList(imageDtos);    // converted image dto set
			articleDtoList.add(articleDto);    // add dto in dto list

			logger.info("articleDtoList List Img toList: {}", articleDtoList.get(0).getArticleImageList());
		}
		return articleDtoList;
	}

	public List<ArticleDto> pagingArticle(int size, String attribute) {
		PageRequest pageRequest = PageRequest.of(0, size, Sort.by("id").descending());
		Page<Article> page = articleRepository.findAll(pageRequest);
		List<Article> articles = page.getContent();
		return articles.stream().map(ArticleDto::of).collect(Collectors.toList());
	}
}

//
//	private  List<ArticleDto> pagingArticleByCategory(int size, String category ,
//	                                                  List<Article>  articleList,  List<ArticleDto> articleDtoList) {
//			PageRequest pageRequests = PageRequest.of(0, size, Sort.by("regTime").descending());
//			Page<Article> pages = articleRepository.findByCategoryOrderByRegTimeDesc(pageRequests , category);
//
//			List<Article> articles = pages.getContent();
//			return  imageDtoInjection(articles,articleDtoList); // articles.stream().map(ArticleDto::of).collect(Collectors.toList());
//		}
//	}