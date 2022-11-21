package br.com.siged.dto.inscricao;

import br.com.siged.entidades.Participante;

public class InscricaoLoteDTO {
	
	private Participante participante;
	
	private Boolean confirmada;
	
	private String motivo;

	public Participante getParticipante() {
		return participante;
	}

	public void setParticipante(Participante participante) {
		this.participante = participante;
	}

	public Boolean getConfirmada() {
		return confirmada;
	}

	public void setConfirmada(Boolean confirmada) {
		this.confirmada = confirmada;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

}
