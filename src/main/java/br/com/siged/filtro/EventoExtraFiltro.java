package br.com.siged.filtro;

import java.util.Date;

public class EventoExtraFiltro {

	private String cpf;
	private String curso;
	private Date data1;
	private Date data2;
	private String indicada;
	private Long servidor;
	
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getCurso() {
		return curso;
	}
	public void setCurso(String curso) {
		this.curso = curso;
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
	
	public Long getServidor() {
		return servidor;
	}
	public void setServidor(Long servidor) {
		this.servidor = servidor;
	}
	
}
