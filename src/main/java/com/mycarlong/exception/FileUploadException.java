package com.mycarlong.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FileUploadException extends CustomBoardException {
	public FileUploadException(String message, Throwable cause) {
		super(message, cause);
	}
}