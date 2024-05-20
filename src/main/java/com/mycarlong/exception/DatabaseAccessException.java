package com.mycarlong.exception;

////////////////////////////////////////////////////////////
public class DatabaseAccessException extends CustomBoardException {
	public DatabaseAccessException(String message, Throwable cause) {
		super( message,  cause);
	}
}