package br.com.siged.entidades;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.siged.entidades.externas.Municipio;
import br.com.siged.entidades.externas.UfMunicipio;

/**
 *
 * @author Marcelo
 */
@Entity
@Table(name = "provedor_evento")
@NamedQueries({
    @NamedQuery(name = "ProvedorEvento.findAll", query = "SELECT p FROM ProvedorEvento p ORDER BY p.descricao ASC"),
    @NamedQuery(name = "ProvedorEvento.findById", query = "SELECT p FROM ProvedorEvento p WHERE p.id = :id"),
    @NamedQuery(name = "ProvedorEvento.findByDescricao", query = "SELECT p FROM ProvedorEvento p WHERE p.descricao = :descricao"),
    @NamedQuery(name = "ProvedorEvento.findByAgencia", query = "SELECT p FROM ProvedorEvento p WHERE p.agencia = :agencia"),
    @NamedQuery(name = "ProvedorEvento.findByBairro", query = "SELECT p FROM ProvedorEvento p WHERE p.bairro = :bairro"),
    @NamedQuery(name = "ProvedorEvento.findByBanco", query = "SELECT p FROM ProvedorEvento p WHERE p.banco = :banco"),
    @NamedQuery(name = "ProvedorEvento.findByCnpj", query = "SELECT p FROM ProvedorEvento p WHERE p.cnpj = :cnpj"),
    @NamedQuery(name = "ProvedorEvento.findByComplemento", query = "SELECT p FROM ProvedorEvento p WHERE p.complemento = :complemento"),
    @NamedQuery(name = "ProvedorEvento.findByContaCorrente", query = "SELECT p FROM ProvedorEvento p WHERE p.contaCorrente = :contaCorrente"),
    @NamedQuery(name = "ProvedorEvento.findByContato", query = "SELECT p FROM ProvedorEvento p WHERE p.contato = :contato"),
    @NamedQuery(name = "ProvedorEvento.findByEmail", query = "SELECT p FROM ProvedorEvento p WHERE p.email = :email"),
    @NamedQuery(name = "ProvedorEvento.findByLogradouro", query = "SELECT p FROM ProvedorEvento p WHERE p.logradouro = :logradouro"),
    @NamedQuery(name = "ProvedorEvento.findByNumero", query = "SELECT p FROM ProvedorEvento p WHERE p.numero = :numero"),
    @NamedQuery(name = "ProvedorEvento.findByTelefone", query = "SELECT p FROM ProvedorEvento p WHERE p.telefone = :telefone"),
    @NamedQuery(name = "ProvedorEvento.findByCep", query = "SELECT p FROM ProvedorEvento p WHERE p.cep = :cep"),
    @NamedQuery(name = "ProvedorEvento.findByCelular", query = "SELECT p FROM ProvedorEvento p WHERE p.celular = :celular")})
@SequenceGenerator(name="sequence", sequenceName="seq_provedor_evento", allocationSize=1)
@JsonIgnoreProperties(value={"paisId", "municipio", "UfMunicipio"})
public class ProvedorEvento implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final Long REDE_ESCOLAS = 381L;
	
	public static final Long IPC = 1L;
	
	public static final Long TCE_CE = 501L;
	
	public static final Long IRB = 21L;
	
	public static final List<Long> PROVEDORES_CERTIFICADO = Arrays.asList(IPC, REDE_ESCOLAS, IRB);
    
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence")
    @Column(name = "id")
    private Long id;
    
	@NotEmpty(message=" Campo Obrigatório")
    @Column(name = "descricao")
    private String descricao;
    
	@Column(name = "agencia")
    private String agencia;
    
	@Column(name = "bairro")
    private String bairro;
    
	@Column(name = "banco")
    private String banco;
    
	@Column(name = "cnpj")
    private String cnpj;
    
	@Column(name = "complemento")
    private String complemento;
    
	@Column(name = "conta_corrente")
    private String contaCorrente;
    
	@Column(name = "contato")
    private String contato;
    
	@Column(name = "email")
    private String email;
    
	@Column(name = "logradouro")
    private String logradouro;
    
	@Column(name = "numero")
    private String numero;
    
	@Column(name = "telefone")
    private String telefone;
    
	@Column(name = "cep")
    private String cep;
    
	@Column(name = "celular")
    private String celular;
	
	@JoinColumn(name = "pais_id", referencedColumnName = "id")
    @ManyToOne @JsonIgnore
    private Pais paisId;
	
	@JoinColumn(name="cidade_id", referencedColumnName="ID")
	@ManyToOne @JsonIgnore
	private Municipio municipio;
	
	@Transient @JsonIgnore
	private UfMunicipio UfMunicipio;
    
	public ProvedorEvento() {
    }

    public ProvedorEvento(Long id) {
        this.id = id;
    }

    public ProvedorEvento(Long id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }   

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getContaCorrente() {
        return contaCorrente;
    }

    public void setContaCorrente(String contaCorrente) {
        this.contaCorrente = contaCorrente;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }    

    public Pais getPaisId() {
		return paisId;
	}

	public void setPaisId(Pais paisId) {
		this.paisId = paisId;
	}

    public String getCep() {
        return cep;
    }

    public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	public UfMunicipio getUfMunicipio() {
		return UfMunicipio;
	}

	public void setUfMunicipio(UfMunicipio ufMunicipio) {
		UfMunicipio = ufMunicipio;
	}

	public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }
    
    public boolean isRedeEscolas() {
		if(id == null) {
			return false;
		} else {
			if(!id.equals(REDE_ESCOLAS)) {
				return false;
			} else {
				return true;
			}
		}
	}
    
    public boolean isIPC() {
		if(id == null) {
			return false;
		} else {
			if(!id.equals(IPC)) {
				return false;
			} else {
				return true;
			}
		}
	}
    
    //IRB
    public boolean isIRB() {
		if(id == null) {
			return false;
		} else {
			if(!id.equals(IRB)) {
				return false;
			} else {
				return true;
			}
		}
	}
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof ProvedorEvento)) {
            return false;
        }
        ProvedorEvento other = (ProvedorEvento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descricao;
    }

}
