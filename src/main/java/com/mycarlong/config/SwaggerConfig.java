package com.mycarlong.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;



@OpenAPIDefinition(
		info = @Info(title = "MyCarLong API 명세서",
				description = "MyCarLong API 명세서",
				version = "v0.1"))
public class SwaggerConfig {
	private static final String API_NAME = "MyCarLong API";
	private static final String API_VERSION = "0.0.1";
	private static final String API_DESCRIPTION = "MyCarLong API 명세서";

	@Bean
	public GroupedOpenApi chatOpenApi() {
		// "/v1/**" 경로에 매칭되는 API를 그룹화하여 문서화한다.
		String[] paths = {"/**"};

		return GroupedOpenApi.builder()
				.group("MyCarLong API v0.1")  // 그룹 이름을 설정한다.
				.pathsToMatch(paths)     // 그룹에 속하는 경로 패턴을 지정한다.
				.build();
	}
}
