package com.lcwd.elestronic.store.Exceptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import com.lcwd.elestronic.store.Dto.ApiResponseMsg;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	//handle resource not found exception
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponseMsg> resourceNotFoundExceptionHandler(ResourceNotFoundException es){
		
		
		logger.info("Exception Handler Invoked !!");
		
		
		ApiResponseMsg msg = new ApiResponseMsg();
		msg.setMessage(es.getMessage());
		msg.setStatus(HttpStatus.NOT_FOUND);
		msg.setSuccess(true);
		
		
		return new ResponseEntity<>(msg,HttpStatus.NOT_FOUND);
	}
	
	
	
	// for methodArgumentNotValidException
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
		
		List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
		
		Map<String, Object> response = new HashMap<>();
		
		allErrors.stream().forEach(objError -> {
			String message = objError.getDefaultMessage();
			String field = ((FieldError)objError).getField();
			response.put(field, message);
		});
		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
	}
	
	
	// Handled bad api request
	
	@ExceptionHandler(BadApiRequestException.class)
	public ResponseEntity<ApiResponseMsg> HandleBadApiRequest(BadApiRequestException es){
		
		
		logger.info(" Bad Api Request !!");
		
		
		ApiResponseMsg msg = new ApiResponseMsg();
		msg.setMessage(es.getMessage());
		msg.setStatus(HttpStatus.BAD_REQUEST);
		msg.setSuccess(false);
		
		
		return new ResponseEntity<>(msg,HttpStatus.BAD_REQUEST);
	}
}
