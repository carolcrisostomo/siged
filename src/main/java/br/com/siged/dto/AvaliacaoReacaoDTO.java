package br.com.siged.dto;

public class AvaliacaoReacaoDTO {
	
	private Long participanteId;
	
	private Long moduloId;
	
	private Long eventoId;
	
	private int pendentes;
	
	public AvaliacaoReacaoDTO(){
		this.participanteId = new Long(0);
		this.moduloId = new Long(0);
		this.eventoId = new Long(0);
		this.pendentes = 0;
	}

	public Long getParticipanteId() {
		return participanteId;
	}

	public void setParticipanteId(Long participanteId) {
		this.participanteId = participanteId;
	}

	public Long getModuloId() {
		return moduloId;
	}

	public void setModuloId(Long moduloId) {
		this.moduloId = moduloId;
	}

	public Long getEventoId() {
		return eventoId;
	}

	public void setEventoId(Long eventoId) {
		this.eventoId = eventoId;
	}
	
	public int getPendentes(){
		return this.pendentes;
	}
	
	public void incrementarPendentes(){
		pendentes++;
	}
}
