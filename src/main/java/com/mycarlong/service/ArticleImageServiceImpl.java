package com.mycarlong.service;


import com.mycarlong.dto.ArticleImageDto;
import com.mycarlong.dto.FileSaveResponse;
import com.mycarlong.entity.Article;
import com.mycarlong.entity.ArticleImage;
import com.mycarlong.repository.ArticleImageRepository;
import com.mycarlong.repository.ArticleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
//@PropertySource(value = "classpath:BoardSetting.yml") //application.yml에 import 하는식으로 attached
public class ArticleImageServiceImpl implements ArticleImageService {
	@Value("${articleImgLocation}")
	private String articleImgLocation;

	private final ArticleImageRepository articleImageRepository;
	private final ArticleRepository articleRepository;
	private final FileServiceImpl fileService;

	private Logger logger = LoggerFactory.getLogger(ArticleImageServiceImpl.class);

//	@Override
//	public String saveArticleImg(Article article, String fileIndex, MultipartFile articleImgFile) {
//
//		try {
//			String imageOriginName = articleImgFile.getOriginalFilename();
//			String imageSavedName = "";
//			String imageSavedPath = "";
//
//			//파일 업로드
//			if(imageOriginName != null){
//				ResponseEntity<?> imgSaveResult = fileService.uploadToS3(article.getTitle(), article.getAuthor(),fileIndex,
//				                                                         articleImgFile.getOriginalFilename(),
//				                                                         articleImgFile.getBytes());
//				if (imgSaveResult.getBody() instanceof FileSaveResponse) {
//					FileSaveResponse temp  = (FileSaveResponse) imgSaveResult.getBody();
//					imageSavedName = temp.getFileSavedName();
//					imageSavedPath = temp.getFileUploadFullUrl();
//					String fileExtension = temp.getFileExtension();
//					log.info("imageSavedName: {} \n imageSavedPath: {} ",imageSavedName, imageSavedPath);
//
//					ArticleImage articleImg = ArticleImage.builder()
//							.imageOriginName(imageOriginName)
//							.imageSavedName(imageSavedName)
//							.imageSavedPath(imageSavedPath)
//							.imageSetNum(Integer.parseInt(fileIndex))
//							.article(article)
//							.fileExtension(fileExtension)
//							.build();
//
//					articleImageRepository.save(articleImg);
//					return imageSavedPath;
//				}
//			}
//
//		} catch (Exception e) {
//			log.warn(e.getMessage());
//		}
//		return "error";
//	}

	@Override
	public String saveArticleImgS3(Article article, String fileIndex, MultipartFile articleImgFile) {

		try {
			String imageOriginName = articleImgFile.getOriginalFilename();
			String imageSavedName = "";
			String imageSavedPath = "";

			//파일 업로드
			if(imageOriginName != null){
				FileSaveResponse imgSaveResult = fileService.uploadToS3(article.getTitle(),
				                                                        article.getAuthor(),
				                                                        fileIndex,
				                                                        imageOriginName,
				                                                        articleImgFile);
				if (!imgSaveResult.isEmpty()) {
					imageSavedName = imgSaveResult.getFileSavedName();
					imageSavedPath = imgSaveResult.getFileUploadFullUrl();
					String fileExtension = imgSaveResult.getFileExtension();

					ArticleImage articleImg = ArticleImage.builder()
							.imageOriginName(imageOriginName)
							.imageSavedName(imageSavedName)
							.imageSavedPath(imageSavedPath)
							.imageSetNum(Integer.parseInt(fileIndex))
							.article(article)
							.fileExtension(fileExtension)
							.build();
					logger.info("이미지 레포지토리 세이브전 확인 : {}", articleImg);
					articleImageRepository.save(articleImg);
					return imageSavedPath;
				}
			}

		} catch (Exception e) {
			log.warn(e.getMessage());
		}
		return "error";
	}
//
//
//	@Override
//	public String updateItemImg(Article article, String fileIndex, MultipartFile articleImgFile) throws Exception {
//		try {
//			if (!articleImgFile.isEmpty()) {
//				Long articleImgId = article.getArticleImageList().get(Integer.parseInt(fileIndex)).getId();
//				ArticleImage savedArticleImg = articleImageRepository.findById(articleImgId)
//						.orElseThrow(EntityNotFoundException::new);
//
//				if (!StringUtils.isEmpty(savedArticleImg.getImageSavedPath())) {
//					fileService.deleteFile(articleImgLocation + "/" +
//							                       savedArticleImg.getImageSavedName());
//				}
//
//				String oriImgName = articleImgFile.getOriginalFilename();
//				ResponseEntity<?> imgSaveResult = fileService.uploadToS3(article.getTitle(), article.getAuthor(),fileIndex,
//				                                                         articleImgFile.getOriginalFilename(),
//				                                                         articleImgFile.getBytes());
//				if (imgSaveResult.getBody() instanceof FileSaveResponse) {
//					FileSaveResponse temp  = (FileSaveResponse) imgSaveResult.getBody();
//					String imageSavedName = temp.getFileSavedName();
//					String imageSavedPath = temp.getFileUploadFullUrl();
//					String fileExtension = temp.getFileExtension();
//					//log.info("imageSavedName: {} \n imageSavedPath: {} ",imageSavedName, imageSavedPath);
//					savedArticleImg.updateArticleImg(oriImgName, imageSavedName, imageSavedPath,fileExtension);
//				}
//
//
//			}
//
//		} catch (Exception e) {
//			log.warn(e.getMessage());
//		}
//		return fileIndex;
//	}

	/**
	 * return File saved Name
	 * */
	@Override
	public ArticleImageDto findImageByName(String imageSavedName) {
		ArticleImage image = articleImageRepository.findByImageSavedName(imageSavedName);
		return ArticleImageDto.of(image);
	}

}