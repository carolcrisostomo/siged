package br.com.siged.filtro;

import java.util.Date;

public class InscricaoFiltro {
	
	private Long evento;
	private Date data1;
	private Date data2;
	private String indicada;
	private String confirmada;
	private String aprovado;
	private Long participanteId;
	private Long tipoParticipanteId;
	private String nomeParticipante;
	
	public InscricaoFiltro() {}
	
	public InscricaoFiltro(Long evento, Long tipoParticipanteId, String indicada, String confirmada) {
		this.evento = evento;
		this.tipoParticipanteId = tipoParticipanteId;
		this.indicada = indicada;
		this.confirmada = confirmada;
	}
	
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
	public Date getData1() {
		return data1;
	}
	public void setData1(Date data1) {
		this.data1 = data1;
	}
	public Date getData2() {
		return data2;
	}
	public void setData2(Date data2) {
		this.data2 = data2;
	}
	public String getIndicada() {
		return indicada;
	}
	public void setIndicada(String indicada) {
		this.indicada = indicada;
	}
	public String getConfirmada() {
		return confirmada;
	}
	public void setConfirmada(String confirmada) {
		this.confirmada = confirmada;
	}
	
	public String getAprovado() {
		return aprovado;
	}

	public void setAprovado(String aprovado) {
		this.aprovado = aprovado;
	}

	public Long getParticipanteId() {
		return participanteId;
	}
	public void setParticipanteId(Long participanteId) {
		this.participanteId = participanteId;
	}
	public Long getTipoParticipanteId() {
		return tipoParticipanteId;
	}
	public void setTipoParticipanteId(Long tipoParticipanteId) {
		this.tipoParticipanteId = tipoParticipanteId;
	}
	
	
}
