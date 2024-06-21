package com.lcwd.elestronic.store.Exceptions;

public class BadApiRequestException extends RuntimeException{

	public BadApiRequestException(String message) {
		super(message);
	
	}
	
	public BadApiRequestException() {
		super("Bad Request");
	}

	
}
