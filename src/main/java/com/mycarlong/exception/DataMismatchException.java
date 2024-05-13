package com.mycarlong.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DataMismatchException extends CustomBoardException {
	public DataMismatchException(String message, Throwable cause) {
		super( message,  cause);
	}
}
