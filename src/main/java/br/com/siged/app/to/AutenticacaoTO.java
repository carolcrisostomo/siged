package br.com.siged.app.to;

import br.com.siged.entidades.Usuario;

public class AutenticacaoTO {
	
	private String login;
	private String cpf;
	private String nome;
	private String email;
	private String token;
	
	public AutenticacaoTO(Usuario usuario){
		
		this.login = usuario.getUsername();
		this.nome = usuario.getNome();
		this.cpf = usuario.getCpf();
		this.email = usuario.getEmail();
		
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}	

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	

}
