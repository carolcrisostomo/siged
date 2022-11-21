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
@Table(name = "formacao_academica")
@NamedQueries({
    @NamedQuery(name = "FormacaoAcademica.findAll", query = "SELECT f FROM FormacaoAcademica f ORDER BY f.descricao"),
    @NamedQuery(name = "FormacaoAcademica.findById", query = "SELECT f FROM FormacaoAcademica f WHERE f.id = :id"),
    @NamedQuery(name = "FormacaoAcademica.findByDescricao", query = "SELECT f FROM FormacaoAcademica f WHERE f.descricao = :descricao")})
@SequenceGenerator(name="sequence", sequenceName="seq_formacao_academica", allocationSize=1)
public class FormacaoAcademica implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence")
    @Column(name = "id")
    private Long id;
    
	@NotEmpty(message=" Campo Obrigatório")
    @Column(name = "descricao")
    private String descricao;
    
    public FormacaoAcademica() {
    }

    public FormacaoAcademica(Long id) {
        this.id = id;
    }

    public FormacaoAcademica(Long id, String descricao) {
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

        if (!(object instanceof FormacaoAcademica)) {
            return false;
        }
        FormacaoAcademica other = (FormacaoAcademica) object;
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
