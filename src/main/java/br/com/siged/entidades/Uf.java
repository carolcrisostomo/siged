package br.com.siged.entidades;

import java.io.Serializable;

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
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Marcelo
 */
@Entity
@Table(name = "uf")
@NamedQueries({
    @NamedQuery(name = "Uf.findAll", query = "SELECT u FROM Uf u ORDER BY u.descricao"),
    @NamedQuery(name = "Uf.findById", query = "SELECT u FROM Uf u WHERE u.id = :id"),
    @NamedQuery(name = "Uf.findByPaisId", query = "SELECT u FROM Uf u WHERE u.paisId.id = :paisId ORDER BY u.descricao"),
    @NamedQuery(name = "Uf.findByDescricao", query = "SELECT u FROM Uf u WHERE u.descricao = :descricao")})
@SequenceGenerator(name="sequence", sequenceName="seq_uf", allocationSize=1)
public class Uf implements Serializable {
	
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence")
    @Column(name = "id")
    private Long id;
    
    @NotEmpty(message=" Campo Obrigatório")
    @Column(name = "descricao")
    private String descricao;
    
    @NotNull
    @JoinColumn(name = "pais_id", referencedColumnName = "id")
    @ManyToOne @JsonIgnore
    private Pais paisId;
    
    public Uf() {
    }

    public Uf(Long id) {
        this.id = id;
    }

    public Uf(Long id, String descricao) {
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

    public Pais getPaisId() {
        return paisId;
    }

    public void setPaisId(Pais paisId) {
        this.paisId = paisId;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof Uf)) {
            return false;
        }
        Uf other = (Uf) object;
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
