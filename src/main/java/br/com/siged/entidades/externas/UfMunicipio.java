package br.com.siged.entidades.externas;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * @author Marcelo
 */
@Entity
@Table(name = "srh_tb_uf")
//@Table(name = "tb_uf", schema = "srh")
@NamedQueries({
		@NamedQuery(name = "UfMunicipio.findAll", query = "SELECT u FROM UfMunicipio u ORDER BY u.nome"),
		@NamedQuery(name = "UfMunicipio.findByUf", query = "SELECT u FROM UfMunicipio u WHERE u.uf = :uf ORDER BY u.nome"),
		@NamedQuery(name = "UfMunicipio.findByNome", query = "SELECT u FROM UfMunicipio u WHERE u.nome = :nome")})
public class UfMunicipio implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="UF", length = 25, nullable = false)
	private String uf;

	@Column(name = "NOME")
	private String nome;
	

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
