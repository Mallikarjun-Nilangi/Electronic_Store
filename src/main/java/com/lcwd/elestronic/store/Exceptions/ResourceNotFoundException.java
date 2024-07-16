package com.lcwd.elestronic.store.Exceptions;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

import lombok.Builder;

@Builder
@AllArgsConstructor
public class ResourceNotFoundException extends RuntimeException{

	private String message;
	private boolean success;
	private HttpStatus status;
	
	
	public ResourceNotFoundException() {
		super("Resource not found !!");
	}
	
	

	public ResourceNotFoundException(String message) {
		super(message);
		this.message = message;
	
	}



	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public HttpStatus getStatus() {
		return status;
	}



	public void setStatus(HttpStatus status) {
		this.status = status;
	}
	
	
	
    
}
