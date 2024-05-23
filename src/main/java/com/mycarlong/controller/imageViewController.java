package com.mycarlong.controller;


import com.mycarlong.dto.ArticleDto;
import com.mycarlong.dto.ArticleImageDto;
import com.mycarlong.entity.Article;
import com.mycarlong.exception.DataMismatchException;
import com.mycarlong.service.ArticleImageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequiredArgsConstructor
public class imageViewController {

	private final ArticleImageServiceImpl articleImageService;
	private Logger logger = LoggerFactory.getLogger(imageViewController.class);

	@Value("${CloudFrontURL}")
	private String cloudFrontURL;


	@GetMapping("/image/{fileName}")
	public ResponseEntity<String> viewImageCloudFront(@PathVariable String fileName) {
		logger.info("fileName : {}", fileName);

		//		ArticleImageDto foundImage = articleImageService.findImageByName(fileName);
		ArticleImageDto foundImage = articleImageService.findImageByName(fileName);
		if (foundImage.getImageSavedPath() == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		logger.info("foundImage.getImageSavedName() : {}", foundImage.getImageSavedName());
		HttpHeaders header = new HttpHeaders();
		String imgUrl = null;
		try {
			imgUrl = cloudFrontURL + foundImage.getImageSavedName();
			if (imgUrl.isEmpty()) {
				imgUrl = "LOAD IMAGE SAVED PATH WAS FAILED";
			}
			return new ResponseEntity<>(imgUrl, HttpStatus.OK);
		} catch (Exception e) {
			imgUrl = e.getMessage();
		}
		return new ResponseEntity<>(imgUrl, HttpStatus.OK);

	}

}