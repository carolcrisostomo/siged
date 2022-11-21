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
@Table(name = "modalidade")
@NamedQueries({
    @NamedQuery(name = "Modalidade.findAll", query = "SELECT m FROM Modalidade m"),
    @NamedQuery(name = "Modalidade.findById", query = "SELECT m FROM Modalidade m WHERE m.id = :id"),
    @NamedQuery(name = "Modalidade.findByDescricao", query = "SELECT m FROM Modalidade m WHERE m.descricao = :descricao")})
@SequenceGenerator(name="sequence", sequenceName="seq_modalidade", allocationSize=1)
public class Modalidade implements Serializable {
    
	private static final long serialVersionUID = 1L;
	
	public static final Long PRESENCIAL = 1l;
	public static final Long EAD = 2l;
    
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence")
    @Column(name = "id")
    private Long id;
    
	@NotEmpty(message="* Campo Obrigatório")
    @Column(name = "descricao")
    private String descricao;

    public Modalidade() {
    }

    public Modalidade(Long id) {
        this.id = id;
    }

    public Modalidade(Long id, String descricao) {
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
    
    public boolean isEAD() {
    	if(id != null && id.equals(EAD)) 
    		return true;
    	return false;
    }
    
    public boolean isPresencial() {
    	if(id != null && id.equals(PRESENCIAL)) 
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

        if (!(object instanceof Modalidade)) {
            return false;
        }
        Modalidade other = (Modalidade) object;
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
