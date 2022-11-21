package br.com.siged.filtro;


public class ParticipanteFiltro {
	private String nome;
	private Long tipoPublicoAlvo;
	
	private String cpf;
	private Long formacaoAcademica;
	private Long nivelEscolaridade;
	
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Long getTipoPublicoAlvo() {
		return tipoPublicoAlvo;
	}
	public void setTipoPublicoAlvo(Long tipoPublicoAlvo) {
		this.tipoPublicoAlvo = tipoPublicoAlvo;
	}

	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public Long getFormacaoAcademica() {
		return formacaoAcademica;
	}
	public void setFormacaoAcademica(Long formacaoAcademica) {
		this.formacaoAcademica = formacaoAcademica;
	}
	public Long getNivelEscolaridade() {
		return nivelEscolaridade;
	}
	public void setNivelEscolaridade(Long nivelEscolaridade) {
		this.nivelEscolaridade = nivelEscolaridade;
	}
	
	
}
