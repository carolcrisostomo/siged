package br.com.siged.service.exception;

public class NaoPodeRealizarInscricaoException extends BusinessException {

	private static final long serialVersionUID = 1L;
	
	private Boolean acessoNegado = false;

	public NaoPodeRealizarInscricaoException(String message) {
		super(message);
	}
	
	public NaoPodeRealizarInscricaoException(String message, Boolean acessoNegado) {
		super(message);
		this.acessoNegado = acessoNegado;
	}
	
	public Boolean isAcessoNegado() {
		return acessoNegado;
	}
	
}
