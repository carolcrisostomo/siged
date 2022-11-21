package br.com.siged.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "cidade")
@NamedQueries({
    @NamedQuery(name = "Cidade.findAll", query = "SELECT c FROM Cidade c ORDER BY c.descricao"),
    @NamedQuery(name = "Cidade.findById", query = "SELECT c FROM Cidade c WHERE c.id = :id"),
    @NamedQuery(name = "Cidade.findByUfId", query = "SELECT c FROM Cidade c WHERE c.ufId.id = :ufId ORDER BY c.descricao"),
    @NamedQuery(name = "Cidade.findByDescricao", query = "SELECT c FROM Cidade c WHERE c.descricao = :descricao")})
@SequenceGenerator(name="sequence", sequenceName="seq_cidade", allocationSize=1)
public class Cidade implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence")
    @Column(name = "id")
    private Long id;
    
	@NotEmpty(message=" Campo Obrigatório")
    @Column(name = "descricao")
    private String descricao;
    
	@NotNull
    @JoinColumn(name = "uf_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY) @JsonIgnore
    private Uf ufId;

    public Cidade() {
    }

    public Cidade(Long id) {
        this.id = id;
    }

    public Cidade(Long id, String descricao) {
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

    public Uf getUfId() {
        return ufId;
    }

    public void setUfId(Uf ufId) {
        this.ufId = ufId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof Cidade)) {
            return false;
        }
        Cidade other = (Cidade) object;
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
