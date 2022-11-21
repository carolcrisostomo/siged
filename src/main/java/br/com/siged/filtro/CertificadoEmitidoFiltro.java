package br.com.siged.filtro;

import java.util.Date;


public class CertificadoEmitidoFiltro {
	
	private Long evento;
	private Long participante;
	private Date data1;
	private Date data2;
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
	public Long getParticipante() {
		return participante;
	}
	public void setParticipante(Long participante) {
		this.participante = participante;
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
	
}
