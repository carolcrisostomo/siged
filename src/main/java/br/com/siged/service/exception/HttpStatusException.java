package br.com.siged.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class HttpStatusException extends HttpStatusCodeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HttpStatusException(HttpStatus statusCode) {
		super(statusCode);
	}
	
	public HttpStatusException(HttpStatus statusCode, String statusText) {
		super(statusCode, statusText);
	}

}
