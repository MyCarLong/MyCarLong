package com.mycarlong.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthorizationException extends CustomBoardException {
	public AuthorizationException(String message, Throwable cause) {
		super(message, cause);
	}
}
