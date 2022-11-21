package br.com.siged.util.comparator;

import java.util.Collections;
import java.util.Comparator;

import br.com.siged.entidades.Inscricao;

public enum InscricaoComparator implements Comparator<Inscricao> {

	OrderByNomeParticipante {
		@Override
		public int compare(Inscricao inscricao, Inscricao anotherInscricao) {
			return inscricao.getParticipanteId().getNome().compareTo(anotherInscricao.getParticipanteId().getNome());
		}
	};
	
	public abstract int compare(Inscricao inscricao, Inscricao anotherInscricao);
	
	public Comparator<Inscricao> asc() {
		return this;
	}
	
	public Comparator<Inscricao> desc() {
		return Collections.reverseOrder(this);
	}
}
