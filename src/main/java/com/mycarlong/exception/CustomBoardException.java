package com.mycarlong.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CustomBoardException extends RuntimeException {
	private String message; // 에러 메시지
	private Throwable cause; // 원인이 되는 예외

	public CustomBoardException(String message, Throwable cause) {
		super(message, cause);
		this.message = message;
		this.cause = cause;
	}

	//Response DTO
	@Data
	@AllArgsConstructor
	public static class Response {
		private String code;
		private String msg;
	}
}