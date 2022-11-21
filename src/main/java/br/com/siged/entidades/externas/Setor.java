package br.com.siged.entidades.externas;

import org.hibernate.annotations.Type;

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
@Table(name = "sapjava_setor")
//@Table(name = "setor", schema = "sapjava")
@NamedQueries({
		@NamedQuery(name = "Setor.findAll", query = "SELECT s FROM  Setor s WHERE s.ativo = 1 AND s.tpSetor = 1 ORDER BY s.descricao"),
		@NamedQuery(name = "Setor.findAllSemTemp", query = "SELECT s FROM Setor s WHERE s.ativo = 1 AND s.tpSetor = 1 AND s.id <> 0 ORDER BY s.descricao"),
		@NamedQuery(name = "Setor.findById", query = "SELECT s FROM Setor s WHERE s.id = :id"),
		@NamedQuery(name = "Setor.findByDescricao", query = "SELECT s FROM Setor s WHERE s.descricao = :descricao"),
		@NamedQuery(name = "Setor.findBySigla", query = "SELECT s FROM Setor s WHERE s.sigla = :sigla") })
public class Setor implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "IDSETOR")
	private Long id;

	@Column(name = "NMSETOR")
	private String descricao;

	@Column(name = "DSABREVIATURASETOR")
	private String sigla;

	@Column(name = "TPAREA")
	private Integer tipo;

	@Column(name = "IDSETORSUPERIOR")
	private Integer setorId;
	
	@Column(name = "FLATIVO")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean ativo;
	
	@Column(name = "TPSETOR")
	private Long tpSetor;

	public Setor() {
	}

	public Setor(Long id) {
		this.id = id;
	}

	public Setor(Long id, String descricao, String sigla, Integer tipo, Integer setorId) {
		super();
		this.id = id;
		this.descricao = descricao;
		this.sigla = sigla;
		this.tipo = tipo;
		this.setorId = setorId;
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

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	
	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public Long getTpSetor() {
		return tpSetor;
	}

	public void setTpSetor(Long tpSetor) {
		this.tpSetor = tpSetor;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {

		if (!(object instanceof Setor)) {
			return false;
		}
		Setor other = (Setor) object;
		if ((this.id == null && other.id != null)
				|| (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return descricao;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public Integer getSetorId() {
		return setorId;
	}

	public void setSetorId(Integer setorId) {
		this.setorId = setorId;
	}

}
