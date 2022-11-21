package br.com.siged.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.siged.entidades.externas.UsuarioSCA;

/**
 *
 * @author Breno
 */
@Entity
@Table(name = "usuario")
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u"),
    @NamedQuery(name = "Usuario.findByLogin", query = "SELECT u FROM Usuario u WHERE TRIM(UPPER(u.username)) = TRIM(UPPER(:login))"),
    @NamedQuery(name = "Usuario.findByCpf", query = "SELECT u FROM Usuario u WHERE u.cpf = :cpf"),
    @NamedQuery(name = "Usuario.findByEmail", query = "SELECT u FROM Usuario u WHERE TRIM(UPPER(u.email)) = TRIM(UPPER(:email))"),
    @NamedQuery(name = "Usuario.findById", query = "SELECT u FROM Usuario u WHERE u.id = :id")
})  
@SequenceGenerator(name="sequence", sequenceName="seq_usuario", allocationSize=1)
    public class Usuario implements Serializable, UserDetails {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence")
    @Column(name = "id")
    private Long id;
    
    @Column(name = "nome")
    private String nome;
    
    @Column(name = "login")
    private String username;
    
    @Column(name = "senha")
    private String password;
    
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
        
    @Column(name = "forcaratualizacaoemail")
    private boolean forcarAtualizacaoEmail;
    
   	@Transient
    private String repetirsenha;
    
    @Transient
	private Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
    
    public Usuario() {}

    public Usuario(Long id, String nome, String login, String senha, int tipo,
			boolean ativo, boolean senhaexpirada, String cpf, Date dataalteracao,
			Date datainclusao, String email, String observacao) {
		this.id = id;
		this.nome = nome;
		this.username = login;
		this.password = senha;
		this.tipo = tipo;
		this.ativo = ativo;
		this.senhaexpirada = senhaexpirada;
		this.cpf = cpf;
		this.dataalteracao = dataalteracao;
		this.datainclusao = datainclusao;
		this.email = email;
		this.observacao = observacao;
	}	

	public Usuario(Long id) {
        this.id = id;
    }
	
	public Usuario (UsuarioSCA usuarioSca){		
		this.id = usuarioSca.getId();
		this.nome = usuarioSca.getNome();
		this.username = usuarioSca.getLogin();
		this.password = usuarioSca.getSenha();
		this.tipo = 0;
		this.ativo = usuarioSca.isAtivo();
		this.senhaexpirada = usuarioSca.isSenhaexpirada();
		this.cpf = usuarioSca.getCpf();
		this.dataalteracao = usuarioSca.getDataalteracao();
		this.datainclusao = usuarioSca.getDatainclusao();
		this.email = usuarioSca.getEmail();
		this.observacao = usuarioSca.getObservacao();
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

	@Override
	public String getUsername() {
		return username;
	}

	public void setUsername(String login) {
		this.username = login;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public void setPassword(String senha) {
		this.password = senha;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	@Override
	public boolean isEnabled() {
		return ativo;
	}

	public void setEnabled(boolean ativo) {
		this.ativo = ativo;
	}

	public boolean isSenhaexpirada() {
		return senhaexpirada;
	}

	public void setSenhaexpirada(boolean senhaexpirada) {
		this.senhaexpirada = senhaexpirada;
	}

	public String getCpf() {
    	if (cpf != null) {
    		return cpf.replace(".", "").replace("-", "");
    	} else {
    		return null;
    	}
	}

	public void setCpf(String cpf) {
    	if (cpf != null) {
    		this.cpf = cpf.replace(".", "").replace("-", "");
    	} else {
    		this.cpf = null;
    	}	
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
	
	public String getRepetirsenha() {
		return repetirsenha;
	}

	public void setRepetirsenha(String repetirsenha) {
		this.repetirsenha = repetirsenha;
	}
	
	public boolean isForcarAtualizacaoEmail() {
		return forcarAtualizacaoEmail;
	}

	public void setForcarAtualizacaoEmail(boolean forcarAtualizacaoEmail) {
		this.forcarAtualizacaoEmail = forcarAtualizacaoEmail;
	}

	public void setAuthorities(Collection<GrantedAuthority> authorities) {
    	this.authorities = authorities;
    }
	
	public Collection<GrantedAuthority> getAuthorities() {
    	return this.authorities;
    }
    
    public Boolean hasAuthority(String authority){
    	for (GrantedAuthority aut : authorities) {
			if(aut.getAuthority().equals(authority)){
				return true;
			}
		}
    	return false;
    }
    
    public Boolean isAdministrador() {
    	return hasAuthority("ROLE_ADMINISTRADOR");
    }
    
    public Boolean isAdministradorConsulta() {
    	return hasAuthority("ROLE_ADMINISTRADORCONS");
    }
    
    public Boolean isAluno() {
    	return hasAuthority("ROLE_ALUNO");
    }
    
    public Boolean isChefe() {
    	return hasAuthority("ROLE_CHEFE");
    }

	@Override
	public String toString() {
		return nome;
	}		

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
}