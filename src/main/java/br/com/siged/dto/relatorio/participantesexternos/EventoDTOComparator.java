package br.com.siged.dto.relatorio.participantesexternos;

import java.util.Collections;
import java.util.Comparator;

public enum EventoDTOComparator implements Comparator<EventoDTO>{

	OrderByDataInicio {
		public int compare(EventoDTO one, EventoDTO other) {
			return one.getInicioRealizacao().compareTo(other.getInicioRealizacao());
		}
	};
	
	public abstract int compare(EventoDTO one, EventoDTO other);
	
	public Comparator<EventoDTO> asc() {
		return this;
	}
	
	public Comparator<EventoDTO> desc() {
		return Collections.reverseOrder(this);
	}
}
