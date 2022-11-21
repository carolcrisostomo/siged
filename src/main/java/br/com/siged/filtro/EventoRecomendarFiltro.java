package br.com.siged.filtro;

import java.util.Date;

public class EventoRecomendarFiltro {
	
	private Date data1;
	private Date data2;
	private String titulo;
	private Long setor;
	private String aprovado;
	
	public String getAprovado() {
		return aprovado;
	}
	public void setAprovado(String aprovado) {
		this.aprovado = aprovado;
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
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public Long getSetor() {
		return setor;
	}
	public void setSetor(Long setor) {
		this.setor = setor;
	}
	
}
