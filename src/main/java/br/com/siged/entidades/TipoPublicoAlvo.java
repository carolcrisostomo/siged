package br.com.siged.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Marcelo
 */
@Entity
@Table(name = "tipo_publico_alvo")
@NamedQueries({
    @NamedQuery(name = "TipoPublicoAlvo.findAll", query = "SELECT t FROM TipoPublicoAlvo t"),
    @NamedQuery(name = "TipoPublicoAlvo.findById", query = "SELECT t FROM TipoPublicoAlvo t WHERE t.id = :id"),
    @NamedQuery(name = "TipoPublicoAlvo.findAllNotServidor", query = "SELECT t FROM TipoPublicoAlvo t WHERE t.id NOT IN (1, 4)"),
    @NamedQuery(name = "TipoPublicoAlvo.findByDescricao", query = "SELECT t FROM TipoPublicoAlvo t WHERE t.descricao = :descricao")})
@SequenceGenerator(name="sequence", sequenceName="seq_tipo_publico_alvo", allocationSize=1)
public class TipoPublicoAlvo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static final Long SERVIDOR_ID = 1l;
	
	public static final Long MEMBRO_ID = 4l;
	
	public static final Long JURISDICIONADO_ID = 2l;
	
	public static final Long SOCIEDADE_ID = 3l;
	
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence")
    @Column(name = "id")
    private Long id;
    
	@NotEmpty(message="* Campo Obrigatório")
    @Column(name = "descricao")
    private String descricao;
	
	@Column(name = "abreviacao")
	private String abreviacao;
	
    public TipoPublicoAlvo() {
    }

    public TipoPublicoAlvo(Long id) {
        this.id = id;
    }

    public TipoPublicoAlvo(Long id, String descricao) {
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

    public String getAbreviacao() {
		return abreviacao;
	}

	public void setAbreviacao(String abreviacao) {
		this.abreviacao = abreviacao;
	}
	
	public boolean isServidor() {
    	if(id != null && id.equals(SERVIDOR_ID)) 
    		return true;
    	return false;
    }
	
	public boolean isJurisdicionado() {
    	if(id != null && id.equals(JURISDICIONADO_ID)) 
    		return true;
    	return false;
    }
	
	public boolean isSociedade() {
    	if(id != null && id.equals(SOCIEDADE_ID)) 
    		return true;
    	return false;
    }
	
	public boolean isMembro() {
    	if(id != null && id.equals(MEMBRO_ID)) 
    		return true;
    	return false;
    }

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof TipoPublicoAlvo)) {
            return false;
        }
        TipoPublicoAlvo other = (TipoPublicoAlvo) object;
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
