package com.mycarlong.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableSwagger2
@OpenAPIDefinition(
		info = @Info(title = "MyCarLong API", version = "v0.1"),
		security = @SecurityRequirement(name = "bearerAuth")
)
public class SwaggerConfig {
		private static final String API_NAME = "MyCarLong API";
		private static final String API_VERSION = "0.0.1";
		private static final String API_DESCRIPTION = "MyCarLong API 명세서";

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build();
	}

		public ApiInfo apiInfo() {
			return new ApiInfoBuilder()
					.title(API_NAME)
					.version(API_VERSION)
					.description(API_DESCRIPTION)
					.build();
		}
	}

