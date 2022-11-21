package br.com.siged.entidades;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class EventoProvedorJoinId implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "evento_id")
	private Evento evento;
	
	@ManyToOne
	@JoinColumn(name = "provedor_id")
	private ProvedorEvento provedor;

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	public ProvedorEvento getProvedor() {
		return provedor;
	}

	public void setProvedor(ProvedorEvento provedor) {
		this.provedor = provedor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((evento == null) ? 0 : evento.hashCode());
		result = prime * result + ((provedor == null) ? 0 : provedor.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventoProvedorJoinId other = (EventoProvedorJoinId) obj;
		if (evento == null) {
			if (other.evento != null)
				return false;
		} else if (!evento.equals(other.evento))
			return false;
		if (provedor == null) {
			if (other.provedor != null)
				return false;
		} else if (!provedor.equals(other.provedor))
			return false;
		return true;
	}
	
	
	
}
