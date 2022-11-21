package br.com.siged.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import javax.validation.constraints.NotNull;

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
@Table(name = "tipo_localizacao_evento")
@NamedQueries({
    @NamedQuery(name = "TipoLocalizacaoEvento.findAll", query = "SELECT t FROM TipoLocalizacaoEvento t ORDER BY t.descricao ASC"),
//  @NamedQuery(name = "TipoLocalizacaoEvento.findAll", query = "SELECT t FROM TipoLocalizacaoEvento t ORDER BY t.cidadePais ASC"),
    @NamedQuery(name = "TipoLocalizacaoEvento.findById", query = "SELECT t FROM TipoLocalizacaoEvento t WHERE t.id = :id"),
    @NamedQuery(name = "TipoLocalizacaoEvento.findByDescricao", query = "SELECT t FROM TipoLocalizacaoEvento t WHERE t.descricao = :descricao")})
//	@NamedQuery(name = "TipoLocalizacaoEvento.findByCidadePais", query = "SELECT t FROM TipoLocalizacaoEvento t WHERE t.cidadePais = :cidadePais")})
	@SequenceGenerator(name="sequence", sequenceName="seq_tipo_localizacao_evento", allocationSize=1)
@JsonIgnoreProperties(value={"paisId", "municipio", "UfMunicipio"})
public class TipoLocalizacaoEvento implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence")
    @Column(name = "id")
    private Long id;
    
	@NotEmpty(message=" Campo Obrigatório")
    @Column(name = "descricao")
    private String descricao;
	
	@Column(name = "endereco")
    private String endereco;
    
	@JoinColumn(name = "pais_id", referencedColumnName = "id")
    @ManyToOne @JsonIgnore
    private Pais paisId;
	
	@JoinColumn(name="cidade_id", referencedColumnName="ID")
	@ManyToOne @JsonIgnore
	private Municipio municipio;
	
	@NotNull(message=" Campo Obrigatório")
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "TPMODALIDADE")
	private TipoLocalizacaoModalidade tipoLocalizacaoModalidade;
	
	//@NotEmpty(message=" Campo Obrigatório")
    @Column(name = "cidade_pais") 
    private String cidadePais;
	
	@Transient @JsonIgnore
	private UfMunicipio UfMunicipio;
    
    public TipoLocalizacaoEvento() {
    }

    public TipoLocalizacaoEvento(Long id) {
        this.id = id;
    }

    public TipoLocalizacaoEvento(Long id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }
    public TipoLocalizacaoEvento(Long id, String descricao, String cidadePais) {
        this.id = id;
        this.descricao = descricao;
        this.cidadePais = cidadePais;
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
    
    public String getCidadePais() {
    	return cidadePais;
    }
    public void setCidadePais(String cidadePais) {
    	this.cidadePais = cidadePais;
    }
    
    public String getEndereco() {
    	return endereco;
    }
    
    public void setEndereco(String endereco) {
    	this.endereco = endereco;
    }
    
    public Pais getPaisId() {
        return paisId;
    }

    public void setPaisId(Pais paisId) {
        this.paisId = paisId;
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
	
	public boolean isInternet() {
	    return this.tipoLocalizacaoModalidade.equals(TipoLocalizacaoModalidade.EAD);
	}
	
	public boolean isPresencial() {
	    return this.tipoLocalizacaoModalidade.equals(TipoLocalizacaoModalidade.PRESENCIAL);
	}
	
	public TipoLocalizacaoModalidade getTipoLocalizacaoModalidade() {
		return tipoLocalizacaoModalidade;
	}

	public void setTipoLocalizacaoModalidade(TipoLocalizacaoModalidade tipoLocalizacaoModalidade) {
		this.tipoLocalizacaoModalidade = tipoLocalizacaoModalidade;
	}

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof TipoLocalizacaoEvento)) {
            return false;
        }
        TipoLocalizacaoEvento other = (TipoLocalizacaoEvento) object;
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
