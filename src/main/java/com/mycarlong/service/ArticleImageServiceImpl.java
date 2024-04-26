package com.mycarlong.service;


import com.mycarlong.entity.ArticleImage;
import com.mycarlong.repository.ArticleImageRepository;
import com.mycarlong.repository.ArticleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
import java.util.List;

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
	private final FileServiceImpl fileServiceImpl;

	private final Logger logger = LoggerFactory.getLogger(ArticleImageServiceImpl.class);

	@Override
	public void saveArticleImg(ArticleImage articleImg, List<MultipartFile> articleImgFileList) throws IOException {

		try {
			for (MultipartFile articleImgFile : articleImgFileList) {
				String imageOriginName = articleImgFile.getOriginalFilename();
				String imageSavedName = "";
				String imageSavedPath = "";


				if (!StringUtils.isEmpty(imageOriginName)) {
					imageSavedName = fileServiceImpl.uploadFile(articleImgLocation, imageOriginName,
					                                            articleImgFile.getBytes());
					imageSavedPath = "/images/article/" + imageSavedName;
				}
				//상품 이미지 정보 저장
				articleImg.updateArticleImg(imageOriginName, imageSavedName, imageSavedPath);
				articleImageRepository.save(articleImg);
				logger.info("게시글 저장성공");
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
	}

	@Override
	public void updateItemImg(Long articleImgId, List<MultipartFile> articleImgFileList) throws Exception {
		try {
			for (MultipartFile articleImgFile : articleImgFileList) {
				if (!articleImgFile.isEmpty()) {
					ArticleImage savedArticleImg = articleImageRepository.findById(articleImgId)
							.orElseThrow(EntityNotFoundException::new);

					if (!StringUtils.isEmpty(savedArticleImg.getImageSavedPath())) {
						fileServiceImpl.deleteFile(articleImgLocation + "/" +
								                           savedArticleImg.getImageSavedName());
					}

					String imageOriginName = articleImgFile.getOriginalFilename();
					String imageSavedName = fileServiceImpl.uploadFile(articleImgLocation,
					                                                   imageOriginName, articleImgFile.getBytes()
					                                                  );
					String imageSavedPath = "/image/article/" + imageSavedName;
					savedArticleImg.updateArticleImg(imageOriginName, imageSavedName, imageSavedPath);
				}
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
	}
}
