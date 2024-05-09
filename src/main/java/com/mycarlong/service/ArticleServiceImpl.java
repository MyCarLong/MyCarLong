package com.mycarlong.service;

import com.mycarlong.dto.ArticleDto;
import com.mycarlong.entity.Article;
import com.mycarlong.entity.ArticleImage;
import com.mycarlong.repository.ArticleImageRepository;
import com.mycarlong.repository.ArticleRepository;
import com.mycarlong.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService{

	private final ArticleRepository articleRepository;
	private final ArticleImageServiceImpl articleImageService;
	private final ArticleImageRepository articleImageRepository;
	private final ReplyRepository replyRepository;

	@Override
	public List<ArticleDto> findAllArticle() {
		List<Article> articleList = articleRepository.findAll();
		return articleList.stream().map(ArticleDto::of).collect(Collectors.toList());
	}

	@Override
	public List<ArticleDto> findFiftyArticldOrderByDesc() {
		PageRequest pageRequest = PageRequest.of(0, 50, Sort.by("id").descending());
		Page<Article> page = articleRepository.findAll(pageRequest);
		List<Article> articles = page.getContent();
		return  articles.stream().map(ArticleDto::of).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void registArticle(ArticleDto articleDto, List<MultipartFile> imgFileList) throws IOException {
		Article article = articleDto.createArticle();
		articleRepository.save(article);

		for(int i=0;i<imgFileList.size();i++){
			ArticleImage articleImage = new ArticleImage();
			articleImage.setArticle(article);
			articleImage.setImageSetNum(i);

			articleImageService.saveArticleImg(article, String.valueOf(i) , imgFileList.get(i));
		}

	}

	@Override
	public ArticleDto viewArticleDetail(Long articleId) {
		Article article = articleRepository.findById(articleId).orElse(null); //Optional 객체로 반환하기 때문에 orElse 추가
		return ArticleDto.of(article);
	}



	@Override
	public void modifyArticle(Long articleId, ArticleDto articleDto) {
		Article existingArticle = articleRepository.findById(articleId).orElse(null); // 게시글 조회
		verifyUser(articleDto);
		// 2. 데이터 수정
		assert existingArticle != null;
		existingArticle.updateThis(articleDto);
		// 3. 데이터 저장
		articleRepository.save(existingArticle);
	}

	@Override
	public void deleteArticle(Long articleId, ArticleDto articleDto) {
		verifyUser(articleDto);
		Article existingArticle = articleRepository.findById(articleId).orElse(null); // 게시글 조회
		articleRepository.deleteById(articleId);
	}

	/*
	* 요청자가 author인 경우에만 게시글의 modify , delete를 가능케 한다. <br>
	*
	* 요청자를 확인하는 함수 verifyUser()
	* */
	private boolean verifyUser( ArticleDto articleDto) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			String username = ((UserDetails) principal).getUsername();
			if (!username.equals(articleDto.getAuthor())) {
				throw new AccessDeniedException("You are not the author of this article.");
			}
		}
		return true;
	}
}
