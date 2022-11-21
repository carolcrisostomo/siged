package br.com.siged.entidades.externas;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.siged.util.Util;

/**
 *
 * @author Breno
 */
@Entity
@Table(name = "sca_usuario")
//@Table(name = "usuario", schema="sca")
@NamedQueries({
    @NamedQuery(name = "UsuarioSCA.findAll", query = "SELECT u FROM UsuarioSCA u WHERE u.ativo = 1 ORDER BY u.nome"),
    @NamedQuery(name = "UsuarioSCA.findByLogin", query = "SELECT u FROM UsuarioSCA u WHERE UPPER(u.login) = UPPER(:login)"),
    @NamedQuery(name = "UsuarioSCA.findByCpf", query = "SELECT u FROM UsuarioSCA u WHERE u.cpf = :cpf"),
    @NamedQuery(name = "UsuarioSCA.findById", query = "SELECT u FROM UsuarioSCA u WHERE u.id = :id")
})  
    public class UsuarioSCA implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    private Long id;
    
    @Column(name = "nome")
    private String nome;
    
    @Column(name = "login")
    private String login;
    
    @Column(name = "senha")
    private String senha;
    
    @Column(name = "tipo")
    private int tipo;
    
    @Column(name = "ativo")
    private boolean ativo;
    
    @Column(name = "senhaexpirada")
    private boolean senhaexpirada;
    
    @Column(name = "cpf")
    private String cpf;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "dataalteracao")
    private Date dataalteracao;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "datainclusao")
    private Date datainclusao;  
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "observacao", length = 2000)
    private String observacao;

    
    public UsuarioSCA() {}

    public UsuarioSCA(Long id, String nome, String login, String senha, int tipo,
			boolean ativo, boolean senhaexpirada, String cpf, Date dataalteracao,
			Date datainclusao, String email, String observacao) {
		super();
		this.id = id;
		this.nome = nome;
		this.login = login;
		this.senha = senha;
		this.tipo = tipo;
		this.ativo = ativo;
		this.senhaexpirada = senhaexpirada;
		this.cpf = cpf;
		this.dataalteracao = dataalteracao;
		this.datainclusao = datainclusao;
		this.email = email;
		this.observacao = observacao;
	}

    public UsuarioSCA(Long id, String nome) {
		this.id = id;
		this.nome = nome;
	}
    
	public UsuarioSCA(Long id) {
        this.id = id;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public boolean isSenhaexpirada() {
		return senhaexpirada;
	}

	public void setSenhaexpirada(boolean senhaexpirada) {
		this.senhaexpirada = senhaexpirada;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Date getDataalteracao() {
		return dataalteracao;
	}

	public void setDataalteracao(Date dataalteracao) {
		this.dataalteracao = dataalteracao;
	}

	public Date getDatainclusao() {
		return datainclusao;
	}

	public void setDatainclusao(Date datainclusao) {
		this.datainclusao = datainclusao;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	@Override
	public String toString() {
		if (cpf != null){
			return nome + " - " + Util.format("###.###.###-##", cpf);
		} else {
			return nome;
		}
    	
	}
}