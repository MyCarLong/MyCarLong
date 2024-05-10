package com.mycarlong.config;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class BoardExceptionList {

	@ExceptionHandler(DatabaseAccessException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Response handleDatabaseAccessException(DatabaseAccessException e) {
		e.printStackTrace();
		return new Response("500", e.getMessage());
	}

	@ExceptionHandler(DataMismatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Response handleDataMismatchException(DataMismatchException e) {
		e.printStackTrace();
		return new Response("400", e.getMessage());
	}

	@ExceptionHandler(AuthorizationException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public Response handleAuthorizationException(AuthorizationException e) {
		e.printStackTrace();
		return new Response("401", e.getMessage());
	}

	@ExceptionHandler(FileUploadException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Response handleFileUploadException(FileUploadException e) {
		e.printStackTrace();
		return new Response("500", e.getMessage());
	}

    ////////////////////////////////////////////////////////////
	public static class DatabaseAccessException extends CustomBoardException {
		public DatabaseAccessException(String message, Throwable cause) {
			super( message,  cause);
		}
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public static class DataMismatchException extends CustomBoardException {
		public DataMismatchException(String message, Throwable cause) {
			super( message,  cause);
		}
	}
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public static class AuthorizationException extends CustomBoardException {
		public AuthorizationException(String message, Throwable cause) {
			super(message, cause);
		}
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public static class FileUploadException extends CustomBoardException {
		public FileUploadException(String message, Throwable cause) {
			super(message, cause);
		}
	}

	//Response DTO
	@Data
	@AllArgsConstructor
	static class Response {
		private String code;
		private String msg;
	}

}