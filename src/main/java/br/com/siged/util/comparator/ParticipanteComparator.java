package br.com.siged.util.comparator;

import java.util.Collections;
import java.util.Comparator;

import br.com.siged.entidades.Participante;

public enum ParticipanteComparator implements Comparator<Participante> {

	OrderByNome {
		@Override
		public int compare(Participante participante, Participante other) {
			return participante.getNome().compareTo(other.getNome());
		}
		
	};
	
	public abstract int compare(Participante participante, Participante other);
	
	public Comparator<Participante> asc() {
		return this;
	}
	
	public Comparator<Participante> desc() {
		return Collections.reverseOrder(this);
	}

}
