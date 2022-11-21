package br.com.siged.entidades.externas;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
//@Table(name = "localidade", schema = "sapjava")
@Table(name = "sapjava_localidade")
public class Localidade implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "IDLOCALIDADE")
	private Long idLocalidade;

	@Column(name = "DSLOCALIDADE")
	private String dsLocalidade;
	
	public Localidade() {
		
	}

	public Localidade(Long idLocalidade, String dsLocalidade) {
		super();
		this.idLocalidade = idLocalidade;
		this.dsLocalidade = dsLocalidade;
	}

	public Long getIdLocalidade() {
		return idLocalidade;
	}

	public void setIdLocalidade(Long idLocalidade) {
		this.idLocalidade = idLocalidade;
	}

	public String getDsLocalidade() {
		return dsLocalidade;
	}

	public void setDsLocalidade(String dsLocalidade) {
		this.dsLocalidade = dsLocalidade;
	}		
	
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idLocalidade != null ? idLocalidade.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {

		if (!(object instanceof Localidade)) {
			return false;
		}
		Localidade other = (Localidade) object;
		if ((this.idLocalidade == null && other.idLocalidade != null)
				|| (this.idLocalidade != null && !this.idLocalidade.equals(other.idLocalidade))) {
			return false;
		}
		return true;
	}
}
