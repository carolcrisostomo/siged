package br.com.siged.util.comparator;

import java.util.Collections;
import java.util.Comparator;

import br.com.siged.entidades.AvaliacaoReacaoNota;

public enum AvaliacaoReacaoNotaComparator implements Comparator<AvaliacaoReacaoNota> {

	OrderByOrdemQuestao {
		public int compare(AvaliacaoReacaoNota avaliacao, AvaliacaoReacaoNota outraAvaliacao) {
			return avaliacao.getQuestao().getOrdem().compareTo(outraAvaliacao.getQuestao().getOrdem());
		}
	};
	
	public abstract int compare(AvaliacaoReacaoNota avaliacao, AvaliacaoReacaoNota outraAvaliacao);
	
	public Comparator<AvaliacaoReacaoNota> asc() {
		return this;
	}
	
	public Comparator<AvaliacaoReacaoNota> desc() {
		return Collections.reverseOrder(this);
	}
}
