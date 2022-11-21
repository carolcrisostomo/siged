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
@Table(name = "tipo_evento")
@NamedQueries({
    @NamedQuery(name = "TipoEvento.findAll", query = "SELECT t FROM TipoEvento t ORDER BY t.descricao"),
    @NamedQuery(name = "TipoEvento.findById", query = "SELECT t FROM TipoEvento t WHERE t.id = :id"),
    @NamedQuery(name = "TipoEvento.findByDescricao", query = "SELECT t FROM TipoEvento t WHERE t.descricao = :descricao")})
@SequenceGenerator(name="sequence", sequenceName="seq_tipo_evento", allocationSize=1)
public class TipoEvento implements Serializable {
    
	private static final long serialVersionUID = 1L;
	
	public static final long CURSO = 1l;
    
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence")
    @Column(name = "id")
    private Long id;
    
	@NotEmpty(message=" Campo Obrigatório")
    @Column(name = "descricao")
    private String descricao;

    public TipoEvento() {
    }

    public TipoEvento(Long id) {
        this.id = id;
    }

    public TipoEvento(Long id, String descricao) {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof TipoEvento)) {
            return false;
        }
        TipoEvento other = (TipoEvento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descricao;
    }
    
    public boolean isCurso() {
    	if (id != null && id.equals(CURSO)) {
    		return true;    		
    	}
    	return false;
    }

}
