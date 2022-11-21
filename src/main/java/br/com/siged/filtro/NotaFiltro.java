package br.com.siged.filtro;


public class NotaFiltro {
	
	private Long evento;
	private Long modulo;
	private Long participante;
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
	public Long getModulo() {
		return modulo;
	}
	public void setModulo(Long modulo) {
		this.modulo = modulo;
	}
	public Long getParticipante() {
		return participante;
	}
	public void setParticipante(Long participante) {
		this.participante = participante;
	}
	
	
}
