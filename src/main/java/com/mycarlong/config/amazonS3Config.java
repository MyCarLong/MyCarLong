package com.mycarlong.config;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class amazonS3Config (

		@Value("${aws.s3.accessKey}")
		private String accessKey,
		@Value("${aws.s3.secretKey}")
		private String secretKey;
		) {
	@Bean
	fun amazonS3Client():AmazonS3 {
		return AmazonS3ClientBuilder.standard()
				.withCredentials(
						AWSStaticCredentialsProvider(BasicAWSCredentials(accessKey, secretKey))
				                )
				.withRegion(Regions.AP_NORTHEAST_2)
				.build()
	}
}