package br.com.siged.service.exception;

import br.com.siged.service.exception.BusinessException;

public class SemestreInvalidoException extends BusinessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SemestreInvalidoException(String message) {
		super(message);
	}

}
