package br.com.siged.filtro;


public class GastoFiltro {
	
	private Long evento;
	private Long tipoGasto;
	private Long fonteGasto;
	private Long participante;
	private String numeroEmpenho;
	private String numeroProcesso;
	private String nomeParticipante;
	
	public String getNomeParticipante() {
		return nomeParticipante;
	}
	public void setNomeParticipante(String nomeParticipante) {
		this.nomeParticipante = nomeParticipante;
	}
	public Long getEvento() {
		return evento;
	}
	public void setEvento(Long evento) {
		this.evento = evento;
	}
	public Long getTipoGasto() {
		return tipoGasto;
	}
	public void setTipoGasto(Long tipoGasto) {
		this.tipoGasto = tipoGasto;
	}
	public Long getFonteGasto() {
		return fonteGasto;
	}
	public void setFonteGasto(Long fonteGasto) {
		this.fonteGasto = fonteGasto;
	}
	public Long getParticipante() {
		return participante;
	}
	public void setParticipante(Long participante) {
		this.participante = participante;
	}
	public String getNumeroEmpenho() {
		return numeroEmpenho;
	}
	public void setNumeroEmpenho(String numeroEmpenho) {
		this.numeroEmpenho = numeroEmpenho;
	}
	public String getNumeroProcesso() {
		return numeroProcesso;
	}
	public void setNumeroProcesso(String numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}
	
	
}
