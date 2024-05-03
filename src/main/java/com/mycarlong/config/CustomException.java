package com.mycarlong.config;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
* 이 클래스는 사용자 정의 예외처리를 위한 클래스 입니다.
* 작성자 : 김근휘
* 작성일시 : 2024-05-01
* */
@ControllerAdvice
public class CustomException {
	@ApiResponses(value = {
			@ApiResponse(responseCode = "500", description = "WebDriver initialization failed")
	})
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "WebDriver initialization failed")
	public static class WebDriverInitializationException extends RuntimeException {
		public WebDriverInitializationException(String message, Throwable cause) {
			super(message, cause);
		}
	}

	@ApiResponses(value = {
			@ApiResponse(responseCode = "404", description = "Element not found")
	})
	@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Element not found")
	public static class ElementNotFoundException extends RuntimeException {
		public ElementNotFoundException(String message) {
			super(message);
		}
	}

	@ApiResponses(value = {
			@ApiResponse(responseCode = "408", description = "Page load timeout")
	})
	@ResponseStatus(code = HttpStatus.REQUEST_TIMEOUT, reason = "Page load timeout")
	public static class PageLoadTimeoutException extends RuntimeException {
		public PageLoadTimeoutException(String message) {
			super(message);
		}
	}

	@ApiResponses(value = {
			@ApiResponse(responseCode = "404", description = "Target not found")
	})
	@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Target not found")
	public static class TargetNotFoundException extends RuntimeException {
		public TargetNotFoundException(String message) {
			super(message);
		}
	}

	@ApiResponses(value = {
			@ApiResponse(responseCode = "404", description = "Parameters (Model / Year) not found")
	})
	@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Parameters (Model / Year) not found")
	public static class ParameterNotFoundException extends RuntimeException {
		public ParameterNotFoundException(String message) {
			super(message);
		}
	}


	@ApiResponses(value = {
			@ApiResponse(responseCode = "500", description = "Error during Merging DATA")
	})
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Error during Merging DATA")
	public static class MergingDataException extends RuntimeException {
		public MergingDataException(String message) {
			super(message);
		}
	}

	@ApiResponses(value = {
			@ApiResponse(responseCode = "500", description = "Error during Merging DATA")
	})
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Error during Merging DATA")
	public static class InformationNotFoundException extends RuntimeException {
		public InformationNotFoundException(String message) {
			super(message);
		}
	}
}
