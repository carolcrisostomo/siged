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
@Table(name = "nivel_escolaridade")
@NamedQueries({
    @NamedQuery(name = "NivelEscolaridade.findAll", query = "SELECT n FROM NivelEscolaridade n"),
    @NamedQuery(name = "NivelEscolaridade.findById", query = "SELECT n FROM NivelEscolaridade n WHERE n.id = :id"),
    @NamedQuery(name = "NivelEscolaridade.findByDescricao", query = "SELECT n FROM NivelEscolaridade n WHERE n.descricao = :descricao")})
@SequenceGenerator(name="sequence", sequenceName="seq_nivel_escolaridade", allocationSize=1)
public class NivelEscolaridade implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence")
    @Column(name = "id")
    private Long id;
    
	@NotEmpty(message="* Campo Obrigatório")
    @Column(name = "descricao")
    private String descricao;
    
    public NivelEscolaridade() {
    }

    public NivelEscolaridade(Long id) {
        this.id = id;
    }

    public NivelEscolaridade(Long id, String descricao) {
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

    	if (!(object instanceof NivelEscolaridade)) {
            return false;
        }
        NivelEscolaridade other = (NivelEscolaridade) object;
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
