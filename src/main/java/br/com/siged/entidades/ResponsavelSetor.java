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

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.com.siged.entidades.externas.Setor;
import br.com.siged.entidades.externas.UsuarioSCA;

/**
 * 
 * @author André
 */
@Entity
@Table(name = "RESPONSAVEL_SETOR")
@NamedQueries({
		@NamedQuery(name = "ResponsavelSetor.findAll", query = "SELECT rs FROM ResponsavelSetor rs"),
		@NamedQuery(name = "ResponsavelSetor.findById", query = "SELECT rs FROM ResponsavelSetor rs WHERE rs.id = :id"),
		@NamedQuery(name = "ResponsavelSetor.findAtivoByResponsavel", query = "SELECT rs FROM ResponsavelSetor rs WHERE rs.responsavel = :responsavel AND rs.ativo = :ativo"),
		@NamedQuery(name = "ResponsavelSetor.findAtivoByResponsavelImediato", query = "SELECT rs FROM ResponsavelSetor rs WHERE rs.responsavelImediato = :responsavelImediato AND rs.ativo = :ativo"),
		@NamedQuery(name = "ResponsavelSetor.findBySetorId", query = "SELECT rs FROM ResponsavelSetor rs JOIN FETCH rs.setor s WHERE s.id = :setorId"),
		@NamedQuery(name = "ResponsavelSetor.findAtivoBySetorId", query = "SELECT rs FROM ResponsavelSetor rs JOIN FETCH rs.setor s WHERE s.id = :setorId AND rs.ativo = :ativo")})
@SequenceGenerator(name="sequence", sequenceName="seq_responsavel_setor" , allocationSize=1)
@JsonIgnoreProperties(value={"setor", "responsavel", "responsavelImediato"})
public class ResponsavelSetor implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence")
	@Column(name = "id")
	private Long id;

	@JoinColumn(name = "SETOR_ID", referencedColumnName = "IDSETOR")
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Setor setor;

	@JoinColumn(name = "RESPONSAVEL_ID", referencedColumnName = "id")
	@ManyToOne(fetch = FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	@JsonIgnore
	private UsuarioSCA responsavel;

	@JoinColumn(name = "RESPONSAVEL_IMEDIATO_ID", referencedColumnName = "id")
	@ManyToOne(fetch = FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	@JsonIgnore
	private UsuarioSCA responsavelImediato;

	@Column(name = "FL_RESPONSAVEL_ATIVO")
    private boolean ativo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	public UsuarioSCA getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(UsuarioSCA responsavel) {
		this.responsavel = responsavel;
	}

	public UsuarioSCA getResponsavelImediato() {
		return responsavelImediato;
	}

	public void setResponsavelImediato(UsuarioSCA responsavelImediato) {
		this.responsavelImediato = responsavelImediato;
	}

	public boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResponsavelSetor other = (ResponsavelSetor) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
