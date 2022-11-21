package br.com.siged.entidades;

public enum TipoLocalizacaoModalidade {

	PRESENCIAL("Presencial"),
	EAD("EAD"),
	PRESENCIAL_E_EAD("Presencial e EAD");
	
	private String label;
	
	TipoLocalizacaoModalidade(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return this.label;
	}
}
