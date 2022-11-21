package br.com.siged.entidades;

public enum StatusEvento {

	REALIZADO("realizados"),
	EMANDAMENTO("emandamento"),
	PREVISTO("previstos");
	
	private String filtro;
	
	StatusEvento(String filtro) {
		this.filtro = filtro;
	}
	
	public String getFiltro() {
		return this.filtro;
	}
}
