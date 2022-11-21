package br.com.siged.service.exception;

public class NaoPodeEnviarEmailException extends BusinessException{

	private static final long serialVersionUID = 1L;

	public NaoPodeEnviarEmailException(String message) {
		super(message);
	}

}
