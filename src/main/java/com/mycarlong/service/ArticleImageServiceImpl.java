package com.mycarlong.service;


import com.mycarlong.dto.FileSaveResponse;
import com.mycarlong.entity.Article;
import com.mycarlong.entity.ArticleImage;
import com.mycarlong.repository.ArticleImageRepository;
import com.mycarlong.repository.ArticleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

	@Override
	public void saveArticleImg(Article article, String fileIndex, MultipartFile articleImgFile) {

		try {
			String imageOriginName = articleImgFile.getOriginalFilename();
			String imageSavedName = "";
			String imageSavedPath = "";

			//파일 업로드
			if(!StringUtils.isEmpty(imageOriginName)){
				ResponseEntity<?> imgSaveResult = fileService.uploadFile(article.getTitle(), article.getAuthor(),fileIndex,
																   articleImgFile.getOriginalFilename(),
				                                                   articleImgFile.getBytes());
				if (imgSaveResult.getBody() instanceof FileSaveResponse) {
					FileSaveResponse temp  = (FileSaveResponse) imgSaveResult.getBody();
					imageSavedName = temp.getFileSavedName();
					imageSavedPath = temp.getFileUploadFullUrl();
					log.info("imageSavedName: {} \n imageSavedPath: {} ",imageSavedName, imageSavedPath);
				}
			}
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
	}


		@Override
		public void updateItemImg(Article article, String fileIndex, MultipartFile articleImgFile) throws Exception {
			try {
					if (!articleImgFile.isEmpty()) {
						Long articleImgId = article.getThisImgList().get(Integer.parseInt(fileIndex)).getId();
						ArticleImage savedArticleImg = articleImageRepository.findById(articleImgId)
								.orElseThrow(EntityNotFoundException::new);

						if (!StringUtils.isEmpty(savedArticleImg.getImageSavedPath())) {
							fileService.deleteFile(articleImgLocation + "/" +
									                           savedArticleImg.getImageSavedName());
						}

						String oriImgName = articleImgFile.getOriginalFilename();
						ResponseEntity<?> imgSaveResult = fileService.uploadFile(article.getTitle(), article.getAuthor(),fileIndex,
						                                        articleImgFile.getOriginalFilename(),
						                                        articleImgFile.getBytes());
						if (imgSaveResult.getBody() instanceof FileSaveResponse) {
							FileSaveResponse temp  = (FileSaveResponse) imgSaveResult.getBody();
							String imageSavedName = temp.getFileSavedName();
							String imageSavedPath = temp.getFileUploadFullUrl();
							//log.info("imageSavedName: {} \n imageSavedPath: {} ",imageSavedName, imageSavedPath);
							savedArticleImg.updateArticleImg(oriImgName, imageSavedName, imageSavedPath);
						}


					}

			} catch (Exception e) {
				log.warn(e.getMessage());
			}
		}


//		public ResponseEntity<FileSaveResponse> saveTest (ArticleFormDto articleFormDto)  throws IOException
//		{ //ArticleImage articleImg, MultipartFile articleImgFileList) throws IOException {
//
//			try {
//				String imageOriginName = articleFormDto.getArticleImgList().get(0).getOriginalFilename();
//				String imageSavedName = "";
//				String imageSavedPath = "";
//				MultipartFile articleImgFile = articleFormDto.getArticleImgList().get(0);
//				String fileIndex = String.valueOf(articleFormDto.getArticleImgList().indexOf(articleImgFile));
//
//
//				if (!StringUtils.isEmpty(imageOriginName)) {
//					imageSavedName = fileService.uploadFile(articleFormDto.getTitle(), articleFormDto.getAuthor(),
//					                                        fileIndex,
//					                                        imageOriginName, articleImgFile.getBytes()
//					                                       );
//					imageSavedPath = "/images/article/" + imageSavedName;
//				}
//				//상품 이미지 정보 저장
//				//				articleImg.updateArticleImg(imageOriginName, imageSavedName, imageSavedPath);
//				ArticleImage articleImg = ArticleImage.builder()
//						.imageOriginName(imageOriginName)
//						.imageSavedName(imageSavedName)
//						.imageSavedPath(imageSavedPath)
//						.build();
//				articleImageRepository.save(articleImg);
//				logger.info("게시글 이미지 저장성공");
//				articleFormDto.setProcessed(true);
//
//				FileSaveResponse fileSaveResponse = FileSaveResponse.builder()
//						.fileName(imageOriginName)
//						.fileSavedName(imageSavedName)
//						.savedUserName(articleFormDto.getAuthor())
//						.associatedArticleTitle(articleFormDto.getTitle())
//						.build();
//
//				return ResponseEntity.status(HttpStatus.OK).body(fileSaveResponse);
//			} catch (Exception e) {
//				logger.warn(e.getMessage());
//			}
//			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
//		}

		//	public void testImgUpload(MultipartFile articleImgFile) throws IOException {
		//		String imageOriginName = articleImgFile.getOriginalFilename();
		//		String imageSavedName = "";
		//		String imageSavedPath = "";
		//
		////		imageSavedName = fileServiceImpl.uploadFile(articleImgLocation, imageOriginName,
		////		                                            articleImgFile.getBytes());
		//		imageSavedPath = "/images/article/" + imageSavedName;
		//		fileServiceImpl.uploadFile(imageOriginName, articleImgFile.getBytes());
		//	}


	}