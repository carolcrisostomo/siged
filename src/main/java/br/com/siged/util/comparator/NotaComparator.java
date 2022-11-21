package br.com.siged.util.comparator;

import java.util.Collections;
import java.util.Comparator;

import br.com.siged.entidades.Nota;

public enum NotaComparator implements Comparator<Nota>{

	OrderByParticipante {
		public int compare(Nota nota, Nota another) {
			String participante = nota.getParticipanteId().getNome();
			String outroParticipante = another.getParticipanteId().getNome();
			
			return participante.compareToIgnoreCase(outroParticipante);
		}
	};
	
	public abstract int compare(Nota nota, Nota another);
	
	public Comparator<Nota> asc() {
		return this;
	}
	
	public Comparator<Nota> desc() {
		return Collections.reverseOrder(this);
	}
}
