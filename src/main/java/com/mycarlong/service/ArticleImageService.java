package com.mycarlong.service;

import com.mycarlong.entity.ArticleImage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ArticleImageService {

	void saveArticleImg(ArticleImage articleImg, List<MultipartFile> articleImgFileList) throws IOException;

	void updateItemImg(Long articleImgId, List<MultipartFile> articleImgFileList) throws Exception;
}
