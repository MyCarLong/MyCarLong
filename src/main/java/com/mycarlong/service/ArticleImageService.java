package com.mycarlong.service;

import com.mycarlong.entity.Article;
import org.springframework.web.multipart.MultipartFile;

public interface ArticleImageService {

	String saveArticleImg(Article article, String fileIndex, MultipartFile articleImgFileList);
//	void updateItemImg(Long articleImgId, MultipartFile articleImgFileList) throws Exception;
	String updateItemImg(Article article, String fileIndex, MultipartFile articleImgFile) throws Exception;
}
