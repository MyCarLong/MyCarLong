package com.mycarlong.service;

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

	private Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);
	@Override
	public List<ArticleDto> findAllArticle() {
		try {
			//article 리스트를 불러온다
			List<Article> articleList = articleRepository.findAll();

			//article 리스트를 article DTO로 변환한다.
			List<ArticleDto> articleDtoList = articleList.stream().map(ArticleDto::of).toList();

			//img List 를 불러온다
			for (ArticleDto articleDto : articleDtoList) {
				for (Article article : articleList) {
					List<ArticleImage> articleImages = article.getArticleImageList();
					articleDto.setArticleImageList(articleImages.stream().map(ArticleImageDto::of).toList());
				}
			}
			//변환한 articld DTO list 의 각각 dto에 이미지 DTO 리스트를 set 한다


			return articleDtoList;

//
//			//			return articleList.stream().map(ArticleDto::of).collect(Collectors.toList());
//			//TODO : article DTO에 이미지DTO 추가해서 반환하기 작업 끝 24-05-13 13:10 여기 다음부터 작업할것.
//			return imageDtoInjection(articleList, articleDtoList);
		} catch (Exception e) {
			throw new DatabaseAccessException("데이터베이스 접근 에러발생", e);
		}
	}


	@Override
	public List<ArticleDto> findFiftyArticldOrderByDesc() {
		return pagingArticle(20,"null");  //내부에서 이미 예외처리 하므로 별도 예외처리 X
	}

	@Override
	@Transactional
	public void registArticle(ArticleFormDto articleDto, List<MultipartFile> imgFileList) {
		try {
			Article article = articleDto.createArticle();

			for(int i=0;i<imgFileList.size();i++){
				ArticleImage articleImage = new ArticleImage();
				articleImage.setArticle(article);
				articleImage.setImageSetNum(i);
				logger.info(" Article Image = {}",articleImage);


				articleImageService.saveArticleImg(article, String.valueOf(i) , imgFileList.get(i));
//				article.getThisImgList().add(articleImage);
			}
//			article.setThisImgList(articleImageList);
			articleRepository.save(article);
		} catch (Exception e) {
			throw new FileUploadException("File Upload 혹은 이미지 등록 중 에러발생", e);
		}

	}

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



	@Override
	public void modifyArticle(Long articleId, ArticleFormDto articleDto) {
		Article existingArticle = articleRepository.findById(articleId).orElse(null); // 게시글 조회
		verifyUser(articleDto);
		// 2. 데이터 수정
		assert existingArticle != null;
		existingArticle.updateThis(articleDto);
		// 3. 데이터 저장
		articleRepository.save(existingArticle);
	}

	@Override
	public void deleteArticle(Long articleId, ArticleFormDto articleDto) {
		verifyUser(articleDto);
		Article existingArticle = articleRepository.findById(articleId).orElse(null); // 게시글 조회
		if (existingArticle == null) {
			throw new DataMismatchException("제공된 Article ID에 해당하는 게시글을 찾을 수 없어 삭제가 완료되지않았습니다..", null);
		}
		articleRepository.deleteById(articleId);
	}

	/*
	* 요청자가 author인 경우에만 게시글의 modify , delete를 가능케 한다. <br>
	*
	* 요청자를 확인하는 함수 verifyUser()
	* */
	private void verifyUser(ArticleFormDto articleDto) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			String username = ((UserDetails) principal).getUsername();
			if (!username.equals(articleDto.getAuthor())) {
				throw new AuthorizationException("게시글에 대한 권한이 없는 유저입니다..", null);
			}
		}
	}
	@Override
	public List<ArticleDto> findByCategory(String category) {
		List<Article> articleList = articleRepository.findByCategoryOrderByRegTimeDesc(category);
		List<ArticleDto> articleDtoList = new ArrayList<>();
		if (articleList == null) {
			throw new DataMismatchException("제공된 category 에 해당하는 데이터를 찾을 수 없습니다. 파라미터 형식 'model-year'", null);
		}

		return pagingArticleByCategory(20,category , articleList, articleDtoList);
		//return articleList.stream().map(ArticleDto::of).collect(Collectors.toList());
	}

	private List<ArticleDto> imageDtoInjection(List<Article> articleList, List<ArticleDto> articleDtoList) {
		for (Article article : articleList) {
			List<ArticleImage> articleImages = articleImageRepository.findByArticleIdOrderByImageSetNum(article.getId());
			List<ArticleImageDto> imageDtos = articleImages.stream().map(ArticleImageDto::of).toList();

			ArticleDto articleDto = ArticleDto.of(article);    // convert
			articleDto.setArticleImageList(imageDtos);    // converted image dto set
			articleDtoList.add(articleDto);    // add dto in dto list

			logger.info("articleDtoList List Img toList: {}", articleDtoList.get(0).getArticleImageList());
		}
		return articleDtoList;
	}


	private List<ArticleDto> pagingArticle(int size , String attribute ) {
		PageRequest pageRequest = PageRequest.of(0, size, Sort.by("id").descending());
		Page<Article> page = articleRepository.findAll(pageRequest);
		List<Article> articles = page.getContent();
		return  articles.stream().map(ArticleDto::of).collect(Collectors.toList());
		}


	private  List<ArticleDto> pagingArticleByCategory(int size, String category ,
	                                                  List<Article>  articleList,  List<ArticleDto> articleDtoList) {
			PageRequest pageRequests = PageRequest.of(0, size, Sort.by("regTime").descending());
			Page<Article> pages = articleRepository.findByCategoryOrderByRegTimeDesc(pageRequests , category);

			List<Article> articles = pages.getContent();
			return  imageDtoInjection(articles,articleDtoList); // articles.stream().map(ArticleDto::of).collect(Collectors.toList());
		}
	}


