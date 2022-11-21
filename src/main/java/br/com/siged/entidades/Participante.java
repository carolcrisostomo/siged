package br.com.siged.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.siged.entidades.externas.Entidade;
import br.com.siged.entidades.externas.Localidade;
import br.com.siged.entidades.externas.Municipio;
import br.com.siged.entidades.externas.Setor;
import br.com.siged.entidades.externas.UfMunicipio;
import br.com.siged.util.Util;

/**
 *
 * @author Marcelo
 */
@Entity
@Table(name = "participante")
@NamedQueries({
	@NamedQuery(name = "Participante.findAll", query = "SELECT p FROM Participante p ORDER BY p.nome"),
	@NamedQuery(name = "Participante.findById", query = "SELECT p FROM Participante p WHERE p.id = :id"),
	@NamedQuery(name = "Participante.findByCelular", query = "SELECT p FROM Participante p WHERE p.celular = :celular"),
	@NamedQuery(name = "Participante.findByCpf", query = "SELECT p FROM Participante p WHERE p.cpf = :cpf"),
	@NamedQuery(name = "Participante.findByEmail", query = "SELECT p FROM Participante p WHERE TRIM(UPPER(p.email)) = TRIM(UPPER(:email))"),
	@NamedQuery(name = "Participante.findByEntidade", query = "SELECT p FROM Participante p WHERE p.entidade = :entidade ORDER BY p.nome"),
	@NamedQuery(name = "Participante.findByTelefone", query = "SELECT p FROM Participante p WHERE p.telefone = :telefone ORDER BY p.nome"),    
	@NamedQuery(name = "Participante.findByTipo", query = "SELECT p FROM Participante p WHERE p.tipoId.id = :tipoId ORDER BY p.nome"),
	@NamedQuery(name = "Participante.findByTipoAndEventoId", query = "SELECT i.participanteId FROM Inscricao i WHERE i.participanteId.id in (SELECT p FROM Participante p WHERE p.tipoId.id = :tipoId) AND i.eventoId.id = :eventoId AND i.confirmada = 'S' ORDER BY i.participanteId.nome"),     
	@NamedQuery(name = "Participante.findByEventoId", query = "SELECT i.participanteId FROM Inscricao i WHERE i.eventoId.id = :eventoId and i.confirmada = 'S' ORDER BY i.participanteId.nome"),    
	@NamedQuery(name = "Participante.findByEventoIdAndModuloId", query = "SELECT i.participanteId FROM Inscricao i WHERE i.participanteId.id not in (SELECT n.participanteId.id from Nota n WHERE n.eventoId.id = :eventoId and n.moduloId.id = :moduloId) and i.eventoId.id = :eventoId and i.confirmada = 'S' ORDER BY i.participanteId.nome"),    
	@NamedQuery(name = "Participante.findByObservacao", query = "SELECT p FROM Participante p WHERE p.observacao = :observacao"),
	@NamedQuery(name = "Participante.findParticipanteEficaciaAvaliada", query = "SELECT p FROM Participante p WHERE p.id in (SELECT a.participanteId FROM AvaliacaoEficacia a) ORDER BY p.nome"),
	@NamedQuery(name = "Participante.findSevidoresInscricaoConfirmada", query = "SELECT i.participanteId FROM Inscricao i WHERE i.eventoId.id = :eventoId and i.confirmada = 'S' and i.participanteId.tipoId in (1, 4) ORDER BY i.participanteId.nome"),
	@NamedQuery(name = "Participante.findParticipantesComCertificadosEmitidos", query = "SELECT p FROM Participante p WHERE p.id IN (SELECT DISTINCT ce.participante.id FROM CertificadoEmitido ce) ORDER BY p.nome"),
	@NamedQuery(name = "Participante.findByEventoIdParticipanteEficaciaAvaliada", query = "SELECT p FROM Participante p WHERE p.id in (SELECT a.participanteId FROM AvaliacaoEficacia a WHERE a.eventoId.id = :eventoId) ORDER BY p.nome")})
@SequenceGenerator(name="sequence", sequenceName="seq_participante", allocationSize=1)

@JsonIgnoreProperties(value={"formacaoId", "escolaridadeId", "setorId", "orgaoId", "tipoId", "frequenciaList", "inscricaoList", "notaList","paisId","municipio", "ufMunicipio", "cidade"})

public class Participante implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence")
	@Column(name = "id")
	private Long id;

	@Column(name = "celular")
	private String celular;

	@NotEmpty(message=" Campo Obrigatório")
	@Column(name = "cpf", unique=true)
	private String cpf;

	@NotEmpty(message=" Campo Obrigatório")
	@Column(name = "email")
	private String email;

	@Column(name = "entidade")
	private String entidade;

	@Column(name = "lotacao")
	private String lotacao;

	@NotEmpty(message=" Campo Obrigatório")
	@Column(name = "nome")
	private String nome;

	@Column(name = "matricula")
	private String matricula;

	@Column(name = "cargo")
	private String cargo;

	@Column(name = "profissao")
	@Size(max = 70, message = " Máximo de {max} carácteres")
	private String profissao;

	@Column(name = "telefone")
	private String telefone;

	@Column(name = "observacao", length = 2000)
	@Basic(fetch = FetchType.LAZY)
	private String observacao;

	@JoinColumn(name = "formacao_id", referencedColumnName = "id")
	@ManyToOne(fetch = FetchType.LAZY) @JsonIgnore
	private FormacaoAcademica formacaoId;

	@JoinColumn(name = "escolaridade_id", referencedColumnName = "id")
	@ManyToOne(fetch = FetchType.LAZY) @JsonIgnore
	private NivelEscolaridade escolaridadeId;

	@JoinColumn(name = "setor_id", referencedColumnName = "IDSETOR")
	@ManyToOne(fetch = FetchType.LAZY) @JsonIgnore
	private Setor setorId;
	
	@JoinColumn(name = "pais_id", referencedColumnName = "id")
    @ManyToOne @JsonIgnore
    private Pais paisId;	

	@JoinColumn(name = "municipio_id", referencedColumnName = "ID")
	@ManyToOne(fetch = FetchType.LAZY) @JsonIgnore
	private Municipio municipio; 
	
	// 
	@JoinColumn(name = "cidade_id", referencedColumnName = "ID")
    @ManyToOne @JsonIgnore
    private Cidade cidade; 

	@Transient @JsonIgnore
	private UfMunicipio ufMunicipio;

	@Transient @JsonIgnore
	private Localidade localidade;
	
	@Transient @JsonIgnore
	private String administracaoPublica;
	
	@JoinColumn(name = "orgao_id", referencedColumnName = "IDENTIDADE")
	@ManyToOne(fetch = FetchType.LAZY) @JsonIgnore
	private Entidade orgaoId;

	@JoinColumn(name = "tipo_id", referencedColumnName = "id")
	@ManyToOne(fetch = FetchType.LAZY) @JsonIgnore
	private TipoPublicoAlvo tipoId;

	@Column(name = "data_cadastro")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCadastro;
	
	@Column(name = "responsavelevento")
	private boolean responsavelEvento;
	
	@Transient
	private Long idTipoOcupacao;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "participanteList") @JsonIgnore
	private List<Frequencia> frequenciaList;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "participanteId") @JsonIgnore
	private List<Inscricao> inscricaoList;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "participanteId") @JsonIgnore
	private List<Nota> notaList;

	public List<Inscricao> getInscricaoList() {
		return inscricaoList;
	}

	public void setInscricaoList(List<Inscricao> inscricaoList) {
		this.inscricaoList = inscricaoList;
	}

	public List<Nota> getNotaList() {
		return notaList;
	}

	public void setNotaList(List<Nota> notaList) {
		this.notaList = notaList;
	}	

	public Participante() {
	}

	public Participante(Long id, String celular, String cpf, String email,
			String entidade, String nome, String telefone, String observacao,
			FormacaoAcademica formacaoId, NivelEscolaridade escolaridadeId,
			Setor setorId, TipoPublicoAlvo tipoId, String matricula,
			String cargo, String profissao, Entidade orgaoId,
			UfMunicipio ufMunicipio, Municipio municipio, Date dataCadastro) {		
		this.id = id;
		this.celular = celular;
		this.cpf = cpf;
		this.email = email;
		this.entidade = entidade;
		this.nome = nome;
		this.telefone = telefone;
		this.observacao = observacao;
		this.formacaoId = formacaoId;
		this.escolaridadeId = escolaridadeId;
		this.setorId = setorId;
		this.tipoId = tipoId;
		this.matricula = matricula;
		this.cargo = cargo;
		this.profissao = profissao;
		this.orgaoId = orgaoId;
		this.ufMunicipio = ufMunicipio;
		this.municipio = municipio;
		this.dataCadastro = dataCadastro;
	}
	
	public Participante(Long id, String cpf, String nome) {
		this.id = id;
		this.cpf = cpf;		
		this.nome = nome;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}    

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getCpf() {
		if (cpf != null) {
			return cpf.replace(".", "").replace("-", "");
		} else {
			return null;
		}
	}

	public String getCpfFormatado() {
		if (cpf != null) {
			return Util.format("###.###.###-##", cpf);
		} else {
			return null;
		}
	}

	public void setCpf(String cpf) {
		if (cpf != null) {
			this.cpf = cpf.replace(".", "").replace("-", "");
		} else {
			this.cpf = null;
		}	
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEntidade() {
		return entidade;
	}

	public void setEntidade(String entidade) {
		this.entidade = entidade;
	}    

	public String getLotacao() {
		return lotacao;
	}

	public void setLotacao(String lotacao) {
		this.lotacao = lotacao;
	}

	public String getNome() {
		return nome;
	}

	public String getNomeCpf() {
		return nome + " - " + Util.format("###.###.###-##", cpf);
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public List<Frequencia> getFrequenciaList() {
		return frequenciaList;
	}

	public void setFrequenciaList(List<Frequencia> frequenciaList) {
		this.frequenciaList = frequenciaList;
	}

	public FormacaoAcademica getFormacaoId() {
		return formacaoId;
	}

	public void setFormacaoId(FormacaoAcademica formacaoId) {
		this.formacaoId = formacaoId;
	}

	public NivelEscolaridade getEscolaridadeId() {
		return escolaridadeId;
	}

	public void setEscolaridadeId(NivelEscolaridade escolaridadeId) {
		this.escolaridadeId = escolaridadeId;
	}

	public Setor getSetorId() {
		return setorId;
	}

	public void setSetorId(Setor setorId) {
		this.setorId = setorId;
	}
	
	public Pais getPaisId() {
		return paisId;
	}
	
	public void setPaisId(Pais paisId) {
		this.paisId = paisId;
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}
	
	//
	public Cidade getCidade() {
		return cidade;
	}

	public void setidade(Cidade cidade) {
		this.cidade = cidade;
	}
	

	public UfMunicipio getUfMunicipio() {
		return ufMunicipio;
	}

	public void setUfMunicipio(UfMunicipio ufMunicipio) {
		this.ufMunicipio = ufMunicipio;
	}

	public TipoPublicoAlvo getTipoId() {
		return tipoId;
	}

	public void setTipoId(TipoPublicoAlvo tipoId) {
		this.tipoId = tipoId;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String getProfissao() {
		return profissao;
	}

	public void setProfissao(String profissao) {
		this.profissao = profissao;
	}	

	public Entidade getOrgaoId() {
		return orgaoId;
	}

	public void setOrgaoId(Entidade orgaoId) {
		this.orgaoId = orgaoId;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	
	public boolean isResponsavelEvento() {
		return this.responsavelEvento;
	}

	public void setResponsavelEvento(boolean responsavelEvento) {
		this.responsavelEvento = responsavelEvento;
	}
	
	public Long getIdTipoOcupacao() {
		return idTipoOcupacao;
	}

	public void setIdTipoOcupacao(Long idTipoOcupacao) {
		this.idTipoOcupacao = idTipoOcupacao;
	}	

	public Localidade getLocalidade() {
		
		if(localidade == null && this.orgaoId != null)
			this.localidade = orgaoId.getLocalidade();
		
		return localidade;
	}

	public void setLocalidade(Localidade localidade) {
		this.localidade = localidade;
	}	
	
	public String getAdministracaoPublica() {
		if (this.administracaoPublica == null || this.administracaoPublica.isEmpty()) {
			Localidade localidade = getLocalidade();
			if(localidade != null) {
				if (localidade.getIdLocalidade() == 1L) {
					administracaoPublica = "estadual";
				} else {
					administracaoPublica = "municipal";
				}
			}
				
		}		
		return administracaoPublica;
	}

	public void setAdministracaoPublica(String administracaoPublica) {
		this.administracaoPublica = administracaoPublica;
	}
	
	public Boolean isNovo() {
		if(this.id != null)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {

		if (!(object instanceof Participante)) {
			return false;
		}
		Participante other = (Participante) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		if(cpf==null)
			return nome;
		else
			return nome + " - "  + Util.format("###.###.###-##", cpf);
	}

}