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
@Table(name = "srh_tb_municipio")
//@Table(name = "tb_municipio", schema = "srh")
@NamedQueries({
		@NamedQuery(name = "Municipio.findAll", query = "SELECT m FROM Municipio m ORDER BY m.nome"),
		@NamedQuery(name = "Municipio.findById", query = "SELECT m FROM Municipio m WHERE m.id = :id"),
		@NamedQuery(name = "Municipio.findByNome", query = "SELECT m FROM Municipio m WHERE m.nome = :nome"),
		@NamedQuery(name = "Municipio.findByUfMunicipio", query = "SELECT m FROM Municipio m WHERE m.ufMunicipio = :ufMunicipio ORDER BY m.nome")})
public class Municipio implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name="UF", length = 25, nullable = false)
	private String ufMunicipio;

	@Column(name = "NOME")
	private String nome;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getUfMunicipio() {
		return ufMunicipio;
	}

	public void setUfMunicipio(String ufMunicipio) {
		this.ufMunicipio = ufMunicipio;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {

		if (!(object instanceof Municipio)) {
			return false;
		}
		Municipio other = (Municipio) object;
		if ((this.id == null && other.id != null)
				|| (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

}
