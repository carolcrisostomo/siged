package br.com.siged.dto.relatorio.avaliacaoreacao;

/**
 * Classe para transportar os dados das observações feita nas Avaliações de Reação
 * @author Rafael de Castro
 *
 */
public class Comentario {
	
	private String nomeParticipante;
	
	private String comentario;
	
	public Comentario(){
		this.nomeParticipante = "NÃO IDENTIFICADO";
		this.comentario = "";
	}

	public String getNomeParticipante() {
		return nomeParticipante;
	}

	public void setNomeParticipante(String nomeParticipante) {
		this.nomeParticipante = nomeParticipante;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	
	@Override
	public String toString() {
		return this.nomeParticipante + ": " + this.comentario;
	}

}
