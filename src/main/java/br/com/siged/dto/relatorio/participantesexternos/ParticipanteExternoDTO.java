package br.com.siged.dto.relatorio.participantesexternos;

import br.com.siged.entidades.StatusDesempenho;

public class ParticipanteExternoDTO {

	private Long id;
	private String nome;
	private String cpf;
	private String frequencia;
	private String nota;
	private StatusDesempenho statusDesempenho;
	
	public ParticipanteExternoDTO(Long id, String nome, String cpf, String frequencia, String nota, StatusDesempenho statusDesempenho) {
		this.id = id;
		this.nome = nome;
		this.cpf = cpf;
		this.frequencia = frequencia;
		this.nota = nota;
		this.statusDesempenho = statusDesempenho;
	}

	public String getFrequencia() {
		return frequencia;
	}

	public String getNota() {
		return nota;
	}

	public String getDesempenho() {
		return statusDesempenho.getDescricao();
	}
	
	public StatusDesempenho getStatusDesempenho() {
		return statusDesempenho;
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getCpf() {
		return cpf;
	}
	
	@Override
	public String toString() {
		String retorno = this.nome + " - " + this.cpf + " - frequencia: " + this.frequencia + " - nota: " + this.nota + " - desempenho: " + this.statusDesempenho.getDescricao();
		return retorno;
	}
	
}
