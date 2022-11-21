package br.com.siged.service.exception;

public class RequisicaoMalFormuladaException extends BusinessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public RequisicaoMalFormuladaException(String message) {
		super(message);
	}
	
	public RequisicaoMalFormuladaException() {
		super("Não foi possível processar a requisição");
	}

}