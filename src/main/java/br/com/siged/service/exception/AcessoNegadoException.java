package br.com.siged.service.exception;

public class AcessoNegadoException extends BusinessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AcessoNegadoException(String message) {
		super(message);
	}
	
	public AcessoNegadoException() {
		super("Acesso Negado");
	}

}
