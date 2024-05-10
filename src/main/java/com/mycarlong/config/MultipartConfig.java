package com.mycarlong.config;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MultipartConfig {
	@Bean
	public MultipartResolver multipartResolver() {
		return new StandardServletMultipartResolver();
	}
	@Value("${uploadPath}")
	private String uploadPath;
	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setLocation(uploadPath);
		factory.setMaxRequestSize(DataSize.ofMegabytes(100L));
		factory.setMaxFileSize(DataSize.ofMegabytes(100L));

		return factory.createMultipartConfig();
	}
}