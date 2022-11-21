package br.com.siged.entidades;

/**
 * 
 * @author estag_12977 (Rafael de Castro)
 * Enum para padronizar o status do Participante no Evento
 */
public enum StatusDesempenho {
	APROVADO("S", "Aprovado"),
	REPROVADO("N", "Reprovado"),
	PARCIAL("--", "Parcial"),
	NENHUM("-", "Não informado");
	
	private String aprovado;
	
	private String descricao;
	
	StatusDesempenho(String aprovado, String descricao) {
		this.aprovado = aprovado;
		this.descricao = descricao;
	}
	
	public String getAprovado() {
		return this.aprovado;
	}
	
	public String getDescricao() {
		return this.descricao;
	}
	
	public static StatusDesempenho fromAprovado(String arovado) {
		for (StatusDesempenho status : values()) {
			if (status.getAprovado().equals(arovado)) {
				return status;
			}
		}

		throw new IllegalArgumentException("Sigla inválida ou não corresponde à um Status de Desempenho");
	}

}
