package com.mycarlong.service;


import com.mycarlong.repository.ArticleImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleImageService {
	@Value("${uploadPath}")
	private String articleImgLocation;

	private final ArticleImageRepository articleImageRepository;



}
