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
@Table(name = "tipo_area_tribunal")
@NamedQueries({
    @NamedQuery(name = "TipoAreaTribunal.findAll", query = "SELECT t FROM TipoAreaTribunal t ORDER BY t.descricao"),
    @NamedQuery(name = "TipoAreaTribunal.findById", query = "SELECT t FROM TipoAreaTribunal t WHERE t.id = :id"),
    @NamedQuery(name = "TipoAreaTribunal.findByDescricao", query = "SELECT t FROM TipoAreaTribunal t WHERE t.descricao = :descricao")})
@SequenceGenerator(name="sequence", sequenceName="seq_tipo_area_tribunal", allocationSize=1)
public class TipoAreaTribunal implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence")
    @Column(name = "id")
    private Long id;
    
	@NotEmpty(message="* Campo Obrigatório")
    @Column(name = "descricao")
    private String descricao;

    public TipoAreaTribunal() {
    }

    public TipoAreaTribunal(Long id) {
        this.id = id;
    }

    public TipoAreaTribunal(Long id, String descricao) {
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

        if (!(object instanceof TipoAreaTribunal)) {
            return false;
        }
        TipoAreaTribunal other = (TipoAreaTribunal) object;
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
