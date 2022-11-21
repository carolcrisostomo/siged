/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
@Table(name = "area_conhecimento")
@NamedQueries({
    @NamedQuery(name = "AreaConhecimento.findAll", query = "SELECT a FROM AreaConhecimento a"),
    @NamedQuery(name = "AreaConhecimento.findById", query = "SELECT a FROM AreaConhecimento a WHERE a.id = :id"),
    @NamedQuery(name = "AreaConhecimento.findByDescricao", query = "SELECT a FROM AreaConhecimento a WHERE a.descricao = :descricao")})
@SequenceGenerator(name="sequence", sequenceName="seq_area_conhecimento", allocationSize=1)
public class AreaConhecimento implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence" )
    @Column(name = "id")
    private Long id;
    
	@NotEmpty(message=" Campo Obrigatório")
    @Column(name = "descricao")
    private String descricao;

    public AreaConhecimento() {
    }

    public AreaConhecimento(Long id) {
        this.id = id;
    }

    public AreaConhecimento(Long id, String descricao) {
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
        if (!(object instanceof AreaConhecimento)) {
            return false;
        }
        AreaConhecimento other = (AreaConhecimento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.siged.entidades.AreaConhecimento[id=" + id + "]";
    }

}
