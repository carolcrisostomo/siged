package br.com.siged.filtro;

import java.util.Date;

import br.com.siged.entidades.StatusEvento;

public class EventoFiltro {
	
	private Long eventoId;
	private Long tipoPublicoAlvo;
	private Date previsto1;
	private Date previsto2;
	private Date realizacao1;
	private Date realizacao2;
	private Long tipoEvento;
	private String identificador;
	private String titulo;
	private Long provedorId;
	private Long[] provedores;
	private Long modalidadeId;
	private int comoInstrutor;
	private int semAcoes;
	private String participanteCPF;
	private StatusEvento statusEvento;
	private StatusEvento[] statusEventoList;
	
	public EventoFiltro() {}
	
	public EventoFiltro(StatusEvento statusEvento) {
		this.statusEvento = statusEvento;
	}
	
	public EventoFiltro(StatusEvento statusEvento, Long modalidadeId) {
		this.statusEvento = statusEvento;
		this.modalidadeId = modalidadeId;
	}
	
	public Long getEventoId() {
		return eventoId;
	}
	public void setEventoId(Long eventoId) {
		this.eventoId = eventoId;
	}
	public Long getTipoPublicoAlvo() {
		return tipoPublicoAlvo;
	}
	public void setTipoPublicoAlvo(Long tipoPublicoAlvo) {
		this.tipoPublicoAlvo = tipoPublicoAlvo;
	}
	public Date getPrevisto1() {
		return previsto1;
	}
	public void setPrevisto1(Date previsto1) {
		this.previsto1 = previsto1;
	}
	public Date getPrevisto2() {
		return previsto2;
	}
	public void setPrevisto2(Date previsto2) {
		this.previsto2 = previsto2;
	}
	public Date getRealizacao1() {
		return realizacao1;
	}
	public void setRealizacao1(Date realizacao1) {
		this.realizacao1 = realizacao1;
	}
	public Date getRealizacao2() {
		return realizacao2;
	}
	public void setRealizacao2(Date realizacao2) {
		this.realizacao2 = realizacao2;
	}
	public Long getTipoEvento() {
		return tipoEvento;
	}
	public void setTipoEvento(Long tipoEvento) {
		this.tipoEvento = tipoEvento;
	}
	public String getIdentificador() {
		return identificador;
	}
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public Long getProvedorId() {
		return provedorId;
	}
	public void setProvedorId(Long provedorId) {
		this.provedorId = provedorId;
	}
	public Long[] getProvedores() {
		return provedores;
	}
	public void setProvedores(Long... provedores) {
		this.provedores = provedores;
	}
	public Long getModalidadeId() {
		return modalidadeId;
	}
	public void setModalidadeId(Long modalidadeId) {
		this.modalidadeId = modalidadeId;
	}
	public int getComoInstrutor() {
		return comoInstrutor;
	}
	public void setComoInstrutor(int comoInstrutor) {
		this.comoInstrutor = comoInstrutor;
	}
	public int getSemAcoes() {
		return semAcoes;
	}
	public void setSemAcoes(int semAcoes) {
		this.semAcoes = semAcoes;
	}
	public String getParticipanteCPF() {
		return participanteCPF;
	}
	public void setParticipanteCPF(String participanteCPF) {
		this.participanteCPF = participanteCPF;
	}
	public StatusEvento getStatusEvento() {
		return statusEvento;
	}
	public void setStatusEvento(StatusEvento statusEvento) {
		this.statusEvento = statusEvento;
	}
	public StatusEvento[] getStatusEventoList() {
		return statusEventoList;
	}
	public void setStatusEventoList(StatusEvento... statusEventoList) {
		this.statusEventoList = statusEventoList;
	}
}
