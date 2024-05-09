package com.mycarlong.service;

import com.mycarlong.entity.Article;
import org.springframework.web.multipart.MultipartFile;

public interface ArticleImageService {

	void saveArticleImg(Article article, String fileIndex, MultipartFile articleImgFileList);
//	void updateItemImg(Long articleImgId, MultipartFile articleImgFileList) throws Exception;
	void updateItemImg(Article article, String fileIndex, MultipartFile articleImgFile) throws Exception;
}
