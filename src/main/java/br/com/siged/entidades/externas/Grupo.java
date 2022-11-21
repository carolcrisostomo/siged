/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.siged.entidades.externas;

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


/**
 *
 * @author Marcelo
 */
@Entity
//@Table(name = "grupo", schema="sca")
@Table(name = "sca_grupo")
@NamedQueries({
    @NamedQuery(name = "Grupo.findAll", query = "SELECT u FROM Grupo u WHERE u.sistema=11"),
    @NamedQuery(name = "Grupo.findById", query = "SELECT u FROM Grupo u WHERE u.sistema=11 and u.id = :id"),
    @NamedQuery(name = "Grupo.findByNome", query = "SELECT u FROM Grupo u WHERE u.sistema=11 and u.nome = :nome")
})  
@SequenceGenerator(name="sequence", sequenceName="seq_grupo", allocationSize=1)
public class Grupo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence")
    @Column(name = "id")
    private Long id;
    @Column(name = "sistema")
    private int sistema;
    @Column(name = "nome")
    private String nome;
    
    public Grupo() {
    }

    public Grupo(Long id) {
        this.id = id;
    }
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getSistema() {
		return sistema;
	}

	public void setSistema(int sistema) {
		this.sistema = sistema;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

    @Override
    public String toString() {
        return nome;
    }
}
