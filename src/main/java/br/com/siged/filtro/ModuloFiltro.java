package br.com.siged.filtro;

import java.util.Date;

public class ModuloFiltro {

	private Long evento;
	private String eventoTitulo;
	private Date data1;
	private Date data2;
	public Long getEvento() {
		return evento;
	}
	public void setEvento(Long evento) {
		this.evento = evento;
	}
	public String getEventoTitulo() {
		return eventoTitulo;
	}
	public void setEventoTitulo(String eventoTitulo) {
		this.eventoTitulo = eventoTitulo;
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
