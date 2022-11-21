package br.com.siged.filtro;

import java.util.Date;


public class AvaliacaoEficaciaFiltro {
	
	private Long evento;
	private Long participante;
	private Date data_cadastro1;
	private Date data_cadastro2;
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
	public Date getData_cadastro1() {
		return data_cadastro1;
	}
	public void setData_cadastro1(Date data_cadastro1) {
		this.data_cadastro1 = data_cadastro1;
	}
	public Date getData_cadastro2() {
		return data_cadastro2;
	}
	public void setData_cadastro2(Date data_cadastro2) {
		this.data_cadastro2 = data_cadastro2;
	}
	
	
	
}
