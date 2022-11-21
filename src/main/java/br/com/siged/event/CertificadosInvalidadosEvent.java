package br.com.siged.event;

import org.springframework.context.ApplicationEvent;

import br.com.siged.entidades.Evento;
import br.com.siged.entidades.Participante;

public class CertificadosInvalidadosEvent extends ApplicationEvent{
	
	private static final long serialVersionUID = 1L;
	
	private Evento evento;
	
	private Participante participante;
	
	private MotivoCertificadosInvalidados motivo;

	public CertificadosInvalidadosEvent(Object source, Evento evento, Participante participante, MotivoCertificadosInvalidados motivo) {
		super(source);
		this.evento = evento;
		this.participante = participante;
		this.motivo = motivo;
	}
	
	public Evento getEvento() {
		return this.evento;
	}
	
	public Participante getParticipante(){
		return this.participante;
	}
	
	public MotivoCertificadosInvalidados getMotivo() {
		return this.motivo;
	}

}