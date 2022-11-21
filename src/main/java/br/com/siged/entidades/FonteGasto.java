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
@Table(name = "fonte_gasto")
@NamedQueries({
    @NamedQuery(name = "FonteGasto.findAll", query = "SELECT f FROM FonteGasto f ORDER BY f.descricao ASC"),
    @NamedQuery(name = "FonteGasto.findById", query = "SELECT f FROM FonteGasto f WHERE f.id = :id"),
    @NamedQuery(name = "FonteGasto.findByDescricao", query = "SELECT f FROM FonteGasto f WHERE f.descricao = :descricao")})
@SequenceGenerator(name="sequence", sequenceName="seq_fonte_gasto", allocationSize=1)
public class FonteGasto implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence")
    @Column(name = "id")
    private Long id;
    
	@NotEmpty(message=" Campo Obrigatório")
    @Column(name = "descricao")
    private String descricao;
    
    public FonteGasto() {
    }

    public FonteGasto(Long id) {
        this.id = id;
    }

    public FonteGasto(Long id, String descricao) {
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

        if (!(object instanceof FonteGasto)) {
            return false;
        }
        FonteGasto other = (FonteGasto) object;
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
