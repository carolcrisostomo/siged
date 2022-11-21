package br.com.siged.service.exception;

public class NaoPodeCancelarInscricaoException extends BusinessException {

	private static final long serialVersionUID = 1L;
	
	private Boolean acessoNegado = false;

	public NaoPodeCancelarInscricaoException(String message) {
		super(message);
	}
	
	public NaoPodeCancelarInscricaoException(String message, Boolean acessoNegado) {
		super(message);
		this.acessoNegado = acessoNegado;
	}
	
	public Boolean isAcessoNegado() {
		return acessoNegado;
	}
	
}
