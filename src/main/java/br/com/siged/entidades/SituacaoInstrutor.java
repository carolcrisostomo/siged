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
@Table(name = "situacao_instrutor")
@NamedQueries({
    @NamedQuery(name = "SituacaoInstrutor.findAll", query = "SELECT s FROM SituacaoInstrutor s"),
    @NamedQuery(name = "SituacaoInstrutor.findById", query = "SELECT s FROM SituacaoInstrutor s WHERE s.id = :id"),
    @NamedQuery(name = "SituacaoInstrutor.findByDescricao", query = "SELECT s FROM SituacaoInstrutor s WHERE s.descricao = :descricao")})
@SequenceGenerator(name="sequence", sequenceName="seq_situacao_instrutor", allocationSize=1)
public class SituacaoInstrutor implements Serializable {
    
	private static final long serialVersionUID = 1L;
	
	public static final Long CADASTRADO = 1l;
	public static final Long PRE_CADASTRADO = 2l;
	public static final Long NAO_ACEITO = 3l;
    
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence")
    @Column(name = "id")
    private Long id;
    
	@NotEmpty(message="* Campo Obrigatório")
    @Column(name = "descricao")
    private String descricao;
    
    public SituacaoInstrutor() {
    }

    public SituacaoInstrutor(Long id) {
        this.id = id;
    }

    public SituacaoInstrutor(Long id, String descricao) {
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
    
    public boolean isCadastrado() {
    	if(id != null && id.equals(CADASTRADO)) 
    		return true;
    	return false;
    }
    
    public boolean isPreCadastrado() {
    	if(id != null && id.equals(PRE_CADASTRADO)) 
    		return true;
    	return false;
    }
    
    public boolean isNaoAceito() {
    	if(id != null && id.equals(NAO_ACEITO)) 
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

        if (!(object instanceof SituacaoInstrutor)) {
            return false;
        }
        SituacaoInstrutor other = (SituacaoInstrutor) object;
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
