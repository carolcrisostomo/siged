package br.com.siged.dto.relatorio.avaliacaoreacao;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe para os dados de quantitativos das Questões das Avaliações de Reação agrupadas por Tipo
 * @author Rafael de Castro
 *
 */
public class TipoQuestaoDTO {

	private String descricao;
	
	private List<QuestaoDTO> questoes;
	
	public TipoQuestaoDTO(){
		this.questoes = new ArrayList<>();
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<QuestaoDTO> getQuestoes() {
		return questoes;
	}

	public void setQuestoes(List<QuestaoDTO> questoes) {
		this.questoes = questoes;
	}
	
	public Integer totalQuestoesExcelente(){
		int total = 0;
		for (QuestaoDTO questao : this.questoes) {
			total += questao.getExcelente();
		}
		return total;
	}
	
	public Integer totalQuestoesBom(){
		int total = 0;
		for (QuestaoDTO questao : this.questoes) {
			total += questao.getBom();
		}
		return total;
	}
	
	public Integer totalQuestoesRegular(){
		int total = 0;
		for (QuestaoDTO questao : this.questoes) {
			total += questao.getRegular();
		}
		return total;
	}
	
	public Integer totalQuestoesInsuficiente(){
		int total = 0;
		for (QuestaoDTO questao : this.questoes) {
			total += questao.getInsuficiente();
		}
		return total;
	}
	
	@Override
	public String toString() {
		String retorno = this.descricao + "\n";
		for(QuestaoDTO questao : this.questoes){
			retorno += "\t" + questao.toString() + "\n";
		}
		
		return retorno;
	}
}
