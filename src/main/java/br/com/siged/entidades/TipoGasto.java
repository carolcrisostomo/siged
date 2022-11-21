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
@Table(name = "tipo_gasto")
@NamedQueries({
    @NamedQuery(name = "TipoGasto.findAll", query = "SELECT t FROM TipoGasto t ORDER BY t.descricao ASC"),
    @NamedQuery(name = "TipoGasto.findById", query = "SELECT t FROM TipoGasto t WHERE t.id = :id"),
    @NamedQuery(name = "TipoGasto.findByDescricao", query = "SELECT t FROM TipoGasto t WHERE t.descricao = :descricao")})
@SequenceGenerator(name="sequence", sequenceName="seq_tipo_gasto", allocationSize=1)
public class TipoGasto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final Long INSTRUTOR_INTERNO_ID = 7l;
	
	public static final Long INSTRUTOR_EXTERNO_ID = 8l;
    
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence")
    @Column(name = "id")
    private Long id;
    
	@NotEmpty(message=" Campo Obrigatório")
    @Column(name = "descricao")
    private String descricao;
	
    public TipoGasto() {
    }

    public TipoGasto(Long id) {
        this.id = id;
    }

    public TipoGasto(Long id, String descricao) {
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
    
    public boolean isInstrutorInterno() {
		if(this.id != null && this.id.equals(TipoGasto.INSTRUTOR_INTERNO_ID)){
			return true;
		}
		return false;
	}
	
	public boolean isInstrutorExterno() {
		if(this.id != null && this.id.equals(TipoGasto.INSTRUTOR_EXTERNO_ID)){
			return true;
		}
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

        if (!(object instanceof TipoGasto)) {
            return false;
        }
        TipoGasto other = (TipoGasto) object;
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
