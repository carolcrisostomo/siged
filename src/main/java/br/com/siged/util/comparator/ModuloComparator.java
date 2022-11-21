package br.com.siged.util.comparator;

import java.util.Collections;
import java.util.Comparator;

import br.com.siged.entidades.Modulo;

public enum ModuloComparator implements Comparator<Modulo>{

	OrderByDataInicio {
		public int compare(Modulo modulo, Modulo anotherModulo) {
			return modulo.getDataInicio().compareTo(anotherModulo.getDataInicio());
		}
	};
	
	public abstract int compare(Modulo modulo, Modulo anotherModulo);
	
	public Comparator<Modulo> asc() {
		return this;
	}
	
	public Comparator<Modulo> desc() {
		return Collections.reverseOrder(this);
	}
}
