
package br.com.siged.entidades.externas;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * @author Marcelo
 *
 *
 *
 * sca.grupo
 *
 * Table(name = "grupo", nome_do_banco = "sca")
 * Table(name = "usuario", nome_do_banco = "sca")
 * Table(name = "tb_uf", nome_do_banco = = "srh")
 * Table(name = "tb_municipio", nome_do_banco = = "srh")
 * Table(name = "entidade", nome_do_banco = = "sapjava")
 * Table(name = "localidade", nome_do_banco = = "sapjava")
 * Table(name = "setor", nome_do_banco = = "sapjava")
 */
@Entity
//@Table(name = "entidade", schema = "sapjava")
@Table(name = "sapjava_entidade")
@NamedQueries({
		@NamedQuery(name = "Entidade.findAll", query = "SELECT e FROM Entidade e  WHERE e.fiscalizado = 1  and e.tpentidadeesfera=2 and e.identidade <> 87 ORDER BY e.dsentidade"),
		@NamedQuery(name = "Entidade.findById", query = "SELECT e FROM Entidade e  WHERE e.identidade = :identidade"),
		@NamedQuery(name = "Entidade.findByDescricao", query = "SELECT e FROM Entidade e  WHERE e.dsentidade = :dsentidade"),
		@NamedQuery(name = "Entidade.findBySigla", query = "SELECT e FROM Entidade e WHERE e.dsentidadesigla = :dsentidadesigla") })
public class Entidade implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private static final long TCE_CE = 87l;

	@Id
	@Column(name = "Identidade")
	private Long identidade;

	@Column(name = "Cnpj")
	private String cnpj;

	@Column(name = "Dsentidade")
	private String dsentidade;
	
	@Column(name = "Dsenderecoentidade")
	private String dsenderecoentidade;

	@Column(name = "Dsbairroentidade")
	private String dsbairroentidade;
	
	@Column(name = "Dscidadeentidade")
	private String dscidadeentidade;

	@Column(name = "Dsestadoentidade")
	private String dsestadoentidade;
	
	@Column(name = "Dscepentidade")
	private String dscepentidade;

	@Column(name = "Dsfoneentidade")
	private String dsfoneentidade;
	
	@Column(name = "Dsfaxentidade")
	private String dsfaxentidade;
	
	@Column(name = "Dsemailentidade")
	private String dsemailentidade;

	@Column(name = "Dsentidadesigla")
	private String dsentidadesigla;
	
	@Column(name = "Dssenha")
	private String dssenha;

	@Column(name = "Idsetor")
	private Integer idsetor;
	
	@Column(name = "Tpentidadeesfera")
	private Integer tpentidadeesfera;

	@Column(name = "Fiscalizado")
	private Integer fiscalizado;
	
	@Column(name = "Idsetororigem")
	private Integer idsetororigem;
	
	@JoinColumn(name = "idlocalidade", referencedColumnName = "idlocalidade")
	@ManyToOne
	private Localidade localidade;

	public Entidade() {
	}

	public Entidade(Long identidade) {
		this.identidade = identidade;
	}

	public Entidade(Long identidade, String cnpj, String dsentidade, String dsenderecoentidade, String dsbairroentidade,
			String dscidadeentidade, String dsestadoentidade, String dscepentidade, String dsfoneentidade,
			String dsfaxentidade, String dsemailentidade, String dsentidadesigla, String dssenha, Integer idsetor,
			Integer tpentidadeesfera, Integer fiscalizado, Integer idsetororigem, Integer setorId, Localidade localidade) {
		super();

		this.identidade = identidade;
		this.cnpj = cnpj;
		this.dsentidade = dsentidade;
		this.dsenderecoentidade = dsenderecoentidade;
		this.dsbairroentidade = dsbairroentidade;
		this.dscidadeentidade = dscidadeentidade;
		this.dsestadoentidade = dsestadoentidade;
		this.dscepentidade = dscepentidade;
		this.dsfoneentidade = dsfoneentidade;
		this.dsfaxentidade = dsfaxentidade;
		this.dsemailentidade = dsemailentidade;
		this.dsentidadesigla = dsentidadesigla;
		this.dssenha = dssenha;
		this.idsetor = idsetor;
		this.tpentidadeesfera = tpentidadeesfera;
		this.fiscalizado = fiscalizado;
		this.idsetororigem = idsetororigem;
		this.localidade = localidade;
	}

	public Long getIdentidade() {
		return identidade;
	}

	public void setIdentidade(Long identidade) {
		this.identidade = identidade;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getDsentidade() {
		return dsentidade;
	}

	public void setDsentidade(String dsentidade) {
		this.dsentidade = dsentidade;
	}

	public String getDsenderecoentidade() {
		return dsenderecoentidade;
	}

	public void setDsenderecoentidade(String dsenderecoentidade) {
		this.dsenderecoentidade = dsenderecoentidade;
	}

	public String getDsbairroentidade() {
		return dsbairroentidade;
	}

	public void setDsbairroentidade(String dsbairroentidade) {
		this.dsbairroentidade = dsbairroentidade;
	}

	public String getDscidadeentidade() {
		return dscidadeentidade;
	}

	public void setDscidadeentidade(String dscidadeentidade) {
		this.dscidadeentidade = dscidadeentidade;
	}

	public String getDsestadoentidade() {
		return dsestadoentidade;
	}

	public void setDsestadoentidade(String dsestadoentidade) {
		this.dsestadoentidade = dsestadoentidade;
	}

	public String getDscepentidade() {
		return dscepentidade;
	}

	public void setDscepentidade(String dscepentidade) {
		this.dscepentidade = dscepentidade;
	}

	public String getDsfoneentidade() {
		return dsfoneentidade;
	}

	public void setDsfoneentidade(String dsfoneentidade) {
		this.dsfoneentidade = dsfoneentidade;
	}

	public String getDsfaxentidade() {
		return dsfaxentidade;
	}

	public void setDsfaxentidade(String dsfaxentidade) {
		this.dsfaxentidade = dsfaxentidade;
	}

	public String getDsemailentidade() {
		return dsemailentidade;
	}

	public void setDsemailentidade(String dsemailentidade) {
		this.dsemailentidade = dsemailentidade;
	}

	public String getDsentidadesigla() {
		return dsentidadesigla;
	}

	public void setDsentidadesigla(String dsentidadesigla) {
		this.dsentidadesigla = dsentidadesigla;
	}

	public String getDssenha() {
		return dssenha;
	}

	public void setDssenha(String dssenha) {
		this.dssenha = dssenha;
	}

	public Integer getIdsetor() {
		return idsetor;
	}

	public void setIdsetor(Integer idsetor) {
		this.idsetor = idsetor;
	}

	public Integer getTpentidadeesfera() {
		return tpentidadeesfera;
	}

	public void setTpentidadeesfera(Integer tpentidadeesfera) {
		this.tpentidadeesfera = tpentidadeesfera;
	}

	public Integer getFiscalizado() {
		return fiscalizado;
	}

	public void setFiscalizado(Integer fiscalizado) {
		this.fiscalizado = fiscalizado;
	}

	public Integer getIdsetororigem() {
		return idsetororigem;
	}

	public void setIdsetororigem(Integer idsetororigem) {
		this.idsetororigem = idsetororigem;
	}	

	public Localidade getLocalidade() {
		return localidade;
	}

	public void setLocalidade(Localidade localidade) {
		this.localidade = localidade;
	}
	
	//TCE-CE
	public boolean isTCE() {
    	if(identidade != null && identidade.equals(TCE_CE)) 
    		return true;
    	return false;
    }

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (identidade != null ? identidade.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {

		if (!(object instanceof Entidade)) {
			return false;
		}
		Entidade other = (Entidade) object;
		if ((this.identidade == null && other.identidade != null)
				|| (this.identidade != null && !this.identidade.equals(other.identidade))) {
			return false;
		}
		return true;
	}

}
